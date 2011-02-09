import util.ByteOperations;
public class Operand {
	private OperandDefinition definition;
	private String value;
	private OperandType type;
	private int origin;
	
	public Operand(String value, LiteralTable literals, int origin) throws Exception {
		this.value = value;
		this.origin = origin;
		this.type = Operand.determineType(this.value);
		if (this.type == OperandType.LITERAL) {
			literals.define(Operand.parseConstant(this.value));
		}
	}
	
	public void setDefinition(OperandDefinition definition) {
		this.definition = definition;
	}
	
	public OperandType getType() {
		return this.type;
	}
	
	public void insert(int[] ops, SymbolTable symbols, LiteralTable literals) throws Exception {
		int x = Operand.getValue(this.value, symbols, literals);
		if (this.definition.isRelocatable() && this.type == OperandType.IMMEDIATE) {
			x -= this.origin & 0x01FF;
		}
		ops[this.definition.getOperationIndex()] |= this.definition.getMask() & (x << this.definition.getLeastSignificantBit());
	}
	
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