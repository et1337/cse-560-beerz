public class InstructionDefinition {

	private int[] operations;
	private OperandDefinition[] operands;
	private String name;
	
	public int[] getOperations() {
		return this.operations;
	}
	
	public OperandDefinition[] getOperandDefinitions() {
		return this.operands;
	}
	
	public InstructionDefinition(int numOperations) {
		this.operations = new int[numOperations];
		this.operands = new OperandDefinition[numOperations];
		for (int i = 0; i < numOperations; i++) {
			this.operands[i] = new OperandDefinition(false, false, i, 0, 16);
		}
	}
	
	public InstructionDefinition(String name, int[] operations, OperandDefinition[] operands) {
		this.name = name;
		this.operations = operations;
		this.operands = operands;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getSize() {
		return this.operations.length;
	}
}