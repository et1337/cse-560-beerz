import util.ByteOperations;

// An Operand represents a value (a Symbol, register identifier, literal, or
// immediate value) to be inserted into an instruction.
public class Operand {

	// Definition of this Operand.
	private OperandDefinition definition;
	
	// The string value of this Operand extracted straight from the source code.
	private String value;
	
	// Indicates what type of Operand this is (register identifier, symbol,
	// literal, or immediate value)
	private OperandType type;
	
	// Instantiates a new Operand with the given string value extracted from the
	// source. If the Operand is a literal, defines a literal in the given
	// LiteralTable.
	public Operand(String value, LiteralTable literals) throws Exception {
		this.value = value;
		this.type = Operand.determineType(this.value);
		if (this.type == OperandType.LITERAL) {
			literals.define(Operand.parseConstant(this.value));
		}
	}
	
	// Sets the definition of this Operand. No work is done at this point.
	public void setDefinition(OperandDefinition definition) {
		this.definition = definition;
	}
	
	// Returns what type of Operand this is (register identifier, symbol,
	// literal, or immediate value)
	public OperandType getType() {
		return this.type;
	}
	
	// Inserts the binary value of this Operand into the given instruction (ops).
	// The given SymbolTable and LiteralTable are used to resolve the binary value
	// of the Operand. The origin is the first address of the program; it is used to
	// offset relocatable immediate values.
	public void insert(int[] ops, SymbolTable symbols, LiteralTable literals, int origin) throws Exception {
		int x = Operand.getValue(this.value, symbols, literals);
		if (this.definition.isRelocatable() && this.type == OperandType.IMMEDIATE) {
			x -= origin & 0x01ff;
		}
		ops[this.definition.getOperationIndex()] |= this.definition.getMask() & (x << this.definition.getLeastSignificantBit());
	}
	
	// Given the string representation of an Operand, returns the OperandType that
	// describes it. (register identifier, symbol, literal, or immediate value)
	public static OperandType determineType(String value) {
		char firstCharacter = value.charAt(0);
		if (firstCharacter == '=') {
			return OperandType.LITERAL;
		}
		else if (firstCharacter == '#' || firstCharacter == 'x') {
			return OperandType.IMMEDIATE;
		}
		else if (firstCharacter == 'R') {
			return OperandType.REGISTER;
		}
		else {
			return OperandType.SYMBOL;
		}
	}
	
	// Gets the binary Operand value represented by the given string. The SymbolTable
	// and LiteralTable are used to resolve the binary value. Note: relocatable
	// immediate values are NOT relocated by this function.
	public static int getValue(String value, SymbolTable symbols, LiteralTable literals) throws Exception {
		OperandType type = Operand.determineType(value);
		switch (type) {
			case LITERAL:
				return literals.getAddress(Operand.parseConstant(value));
			case IMMEDIATE:
				return Operand.parseConstant(value);
			case REGISTER:
				return Operand.parseConstant(value);
			case SYMBOL:
				if (symbols.hasSymbol(value)) {
					//if (symbol.isRelocatable() && !this.definition.isRelocatable()) {
					//	// Error
					//}
					return symbols.get(value).getValue();
				}
				else {
					// Error: undefined symbol
				}
		}
		return 0;
	}
	
	// Parses a register identifier, literal value, decimal value, or hexadecimal value
	// into an integer number.
	public static int parseConstant(String value) throws Exception {
		int result = 0;
		if (value.charAt(0) == '=') {
			value = value.substring(1);
		}
		char firstCharacter = value.charAt(0);
		value = value.substring(1);
		if (firstCharacter == '#') {
			result = Integer.valueOf(value, 10);
		}
		else if (firstCharacter == 'x') {
			result = ByteOperations.parseHex(value);
		}
		else if (firstCharacter == 'R') {
			result = Integer.valueOf(value, 10);
		}
		else {
			// Error
		}
		return result;
	}
}