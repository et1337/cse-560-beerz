public class InstructionDefinition {

	private int[] operations;
	private OperandDefinition[] operands;
	private String name;
	
	public InstructionDefinition(int numOperations) {
		this.operations = new int[numOperations];
		this.operands = new OperandDefinition[numOperations];
		for (int i = 0; i < numOperations; i++) {
			this.operands[i] = new OperandDefinition(false, new OperandType[] { OperandType.IMMEDIATE }, i, 15, 0);
		}
	}
	
	public InstructionDefinition(String name, int[] operations, OperandDefinition[] operands) {
		this.name = name;
		this.operations = operations;
		this.operands = operands;
	}
	
	public boolean isAcceptable(Instruction instruction) {
		if (!this.name.equals(instruction.getName())) {
			return false;
		}
		Operand[] instructionOperands = instruction.getOperands();
		if (instructionOperands.length != this.operands.length) {
			return false;
		}
		for (int i = 0; i < this.operands.length; i++) {
			if (!this.operands[i].isAcceptable(instructionOperands[i])) {
				return false;
			}
		}
		return true;
	}
	
	public int[] getOperations() {
		return this.operations;
	}
	
	public OperandDefinition[] getOperandDefinitions() {
		return this.operands;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getSize() {
		return this.operations.length;
	}
}