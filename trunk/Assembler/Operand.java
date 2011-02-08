public class Operand {
	private OperandDefinition definition;
	private String value;
	
	public Operand(OperandDefinition definition, String value) {
		this.definition = definition;
		this.value = value;
		if (!definition.canBeLiteral() && this.value.charAt(0) == '=') {
			// Error
		}
		if (!definition.isRelocatable()) {
			// Check that it's an immediate value
		}
	}
	
	public void insert(int[] ops, SymbolTable symbols) {
		ops[this.definition.getOperationIndex()] |= this.definition.translateValue(Operand.getValue(this.definition.getType(), this.value, symbol));
	}
	
	public static int getValue(String value, SymbolTable symbols, LiteralTable literals) {
		if (symbols.hasSymbol(value)) {
			return symbols.get(value);
		}
		else {
			
		}
	}
}