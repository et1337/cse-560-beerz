import util.ByteOperations;
public class Operand {
	private OperandDefinition definition;
	private String value;
	private OperandType type;
	
	public Operand(String value, LiteralTable literals) throws Exception {
		this.value = value;
		char firstCharacter = this.value.charAt(0);
		if (firstCharacter == '=') {
			this.type = OperandType.LITERAL;
		}
		else if (firstCharacter == 'R') {
			this.type = OperandType.REGISTER;
		}
		else if (firstCharacter == '#' || firstCharacter == 'x') {
			this.type = OperandType.IMMEDIATE;
		}
		else {
			this.type = OperandType.SYMBOL;
		}
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
		ops[this.definition.getOperationIndex()] |= this.definition.getMask() & (x << this.definition.getLeastSignificantBit());
	}
	
	public static int getValue(String value, SymbolTable symbols, LiteralTable literals) throws Exception {
		if (value.charAt(0) == '=') {
			return literals.getAddress(Operand.parseConstant(value));
		}
		else if (value.charAt(0) == '#' || value.charAt(0) == 'x') {
			return Operand.parseConstant(value);
		}
		else if (symbols.hasSymbol(value)) {
			Symbol symbol = symbols.get(value);
			//if (symbol.isRelocatable() && !this.definition.isRelocatable()) {
			//	// Error
			//}
			return symbol.getValue();
		}
		else {
			// Error: undefined symbol
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