public class Instruction {

	private InstructionDefinition definition;
	private Operand[] operands;
	private String source;
	private String name;
	
	public Instruction(String name, String source) {
		this.name = name;
		this.source = source;
	}
	
	public void setDefinition(InstructionDefinition definition) {
		this.definition = definition;
		OperandDefinition[] definitions = this.definition.getOperandDefinitions();
		for (int i = 0; i < definitions.length; i++) {
			this.operands[i].setDefinition(definitions[i]);
		}
	}
	
	public void setOperands(String[] values, LiteralTable literals, int origin) throws Exception {
		this.operands = new Operand[values.length];
		for (int i = 0; i < values.length; i++) {
			this.operands[i] = new Operand(values[i], literals, origin);
		}
	}
	
	public String getSource() {
		return this.source;
	}
	
	public String getName() {
		return this.name;
	}
	
	public InstructionDefinition getDefinition() {
		return this.definition;
	}
	
	public Operand[] getOperands() {
		return this.operands;
	}
	
	public int[] getCodes(SymbolTable symbols, LiteralTable literals) throws Exception {
		int[] result = this.definition.getOperations();
		if (this.operands != null) {
			for (Operand operand : this.operands) {
				operand.insert(result, symbols, literals);
			}
		}
		return result;
	}
}