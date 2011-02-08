public class Instruction {

	private InstructionDefinition definition;
	private Operand[] operands;
	private String source;
	
	public Instruction(String source) {
		this.source = source;
	}
	
	public void setDefinition(InstructionDefinition definition) {
		this.definition = definition;
		this.source = source;
		this.operands = new Operand[numOperands];
	}
	
	public void setOperands(String[] values) {
		OperandDefinition[] definitions = this.definition.getOperandDefinitions();
		if (values.length != definitions.length) {
			// Error
		}
		for (int i = 0; i < values.length; i++) {
			this.operands[i] = new Operand(definitions[i], values[i]);
		}
	}
	
	public String getSource() {
		return this.source;
	}
	
	public InstructionDefinition getDefinition() {
		return this.definition;
	}
	
	public int[] getCodes(SymbolTable symbols, LiteralTable literals) {
		int[] result = this.definition.getOperations();
		for (Operand operand in this.operands) {
			operand.insert(result, symbols, literals);
		}
		return result;
	}
}