package Assembler;

/**
 * An Operand represents a value (a Symbol, register identifier, literal, or
 * immediate value) to be inserted into an instruction.
 */

public class Operand {

	/**
	 * Definition of this Operand.
	 */
	private OperandDefinition definition;

	/**
	 * The string value of this Operand extracted straight from the source code.
	 */
	private String value;

	/**
	 * Indicates what type of Operand this is (register identifier, symbol,
	 * literal, or immediate value)
	 */
	private OperandType type;

	/**
	 * Instantiates a new Operand with the given string value extracted from the
	 * source. If the Operand is a literal, defines a literal in the given
	 * LiteralTable.
	 * 
	 * @param value
	 *            String extracted from the source
	 * @param literals
	 *            A Literaltable
	 * @throws Exception
	 */
	public Operand(String value, LiteralTable literals) throws Exception {
		this.value = value;
		this.type = Operand.determineType(this.value);
		if (this.type == OperandType.LITERAL) {
			literals.define(Operand.parseConstant(this.value));
		}
	}

	/**
	 * Sets the definition of this Operand. No work is done at this point.
	 * 
	 * @param definition
	 *            OperandDefinition
	 */
	public void setDefinition(OperandDefinition definition) {
		this.definition = definition;
	}

	/**
	 * Returns what type of Operand this is (register identifier, symbol,
	 * literal, or immediate value)
	 * 
	 * @return the type of the current Operand
	 */
	public OperandType getType() {
		return this.type;
	}

	/**
	 * Inserts the binary value of this Operand into the given instruction
	 * (ops). The given SymbolTable and LiteralTable are used to resolve the
	 * binary value of the Operand.
	 * 
	 * @param ops
	 *            the op code of the assembly instruction
	 * @param symbols
	 *            The SymbolTable used to create this instruction
	 * @param literals
	 *            The LiteralTable used to create this instruction
	 * @throws Exception
	 */
	public void insert(int[] ops, SymbolTable symbols, LiteralTable literals)
			throws Exception {
		int x = Operand.getValue(this.value, symbols, literals);
		ops[this.definition.getOperationIndex()] |= this.definition.getMask()
				& (x << this.definition.getLeastSignificantBit());
	}

	/**
	 * Given the string representation of an Operand, returns the OperandType
	 * that describes it. (register identifier, symbol, literal, or immediate
	 * value)
	 * 
	 * @param value
	 *            the String representation of an Operand
	 * @return the OperandType
	 */
	public static OperandType determineType(String value) {
		char firstCharacter = value.charAt(0);
		if (firstCharacter == '=') {
			return OperandType.LITERAL;
		} else if (firstCharacter == '#' || firstCharacter == 'x') {
			return OperandType.IMMEDIATE;
		} else if (firstCharacter == 'R') {
			return OperandType.REGISTER;
		} else if (firstCharacter == '"') {
			return OperandType.STRING;
		} else {
			return OperandType.SYMBOL;
		}
	}
	
	public static int getValue(String value, SymbolTable symbols,
			LiteralTable literals) throws Exception {
		return Operand.getValue(value, symbols, null, literals);
	}

	/**
	 * Gets the binary Operand value represented by the given string. The
	 * SymbolTable and LiteralTable are used to resolve the binary value. Note:
	 * relocatable immediate values are NOT relocated by this function.
	 * 
	 * @param value
	 *            the String representation of an Operand
	 * @param symbols
	 *            the SymbolTable used by the Operand
	 * @param definition
	 *            optional definition of the Operand
	 * @param literals
	 *            the Literaltable used by the Operand
	 * @return the value of the Operand
	 * @throws Exception
	 */
	public static int getValue(String value, SymbolTable symbols,
			OperandDefinition definition, LiteralTable literals)
			throws Exception {
		OperandType type = Operand.determineType(value);
		if (definition != null) {
			if (!definition.isAcceptable(type)) {
				throw new Exception("Incorrect operand type for operand \"" + value + "\".");
			}
		}
		switch (type) {
			case LITERAL:
				return literals.getAddress(Operand.parseConstant(value));
			case IMMEDIATE:
				return Operand.parseConstant(value);
			case REGISTER:
				return Operand.parseConstant(value);
			case SYMBOL:
				if (symbols.hasSymbol(value)) {
					Symbol symbol = symbols.get(value);
					if (symbol.isRelocatable()
						&& (definition != null &&
						!definition.isRelocatable())) {
						// Error: attempting to using a relocatable symbol for a
						// non-relocatable operand.
						throw new Exception("Relocatable symbol \"" +
							value + "\" can not be used in a non-relocatable operand.");
					}
					return symbol.getValue();
				} else {
					throw new Exception("Undefined symbol \"" + value + "\".");
				}
		}
		return 0;
	}

	/**
	 * Parses a register identifier, literal value, decimal value, or
	 * hexadecimal value into an integer number.
	 * 
	 * @param value
	 *            the String value of the Operand
	 * @return the Integer value of the Operand
	 * @throws Exception
	 */
	public static int parseConstant(String value) throws Exception {
		int result = 0;
		String v = new String(value);
		if (v.charAt(0) == '=') {
			v = v.substring(1);
		}
		char firstCharacter = v.charAt(0);
		v = v.substring(1);
		if (firstCharacter == '#') {
			result = Integer.valueOf(v, 10);
		} else if (firstCharacter == 'x') {
			result = ByteOperations.parseHex(v);
		} else if (firstCharacter == 'R') {
			result = Integer.valueOf(v, 10);
		} else {
			throw new Exception("Failed to parse immediate operand \"" + value + "\".");
		}
		return result;
	}
}