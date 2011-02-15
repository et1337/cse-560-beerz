package Assembler;

// An InstructionDefinition represents a certain type of assembly instruction (like
// ADD, LD, STR, etc.). Once an assembly instruction is matched with a definition, the
// definition is used to turn the source code into binary executable code.
// An InstructionDefinition is comprised of:
// A name, used to match instruction names extracted from source code lines.
// A collection of binary values which represent the basis of the final binary code;
// this is the binary executable code before the Operand values are inserted.
// A collection of OperandDefinitions which specify which Operands are acceptable
	// for this instruction, and how they are to be inserted into the binary base values.
public class InstructionDefinition {
	
	// A collection of binary values which represent the basis of the final binary code.
	private int[] operations;
	
	// A collection of OperandDefinitions which specify which Operands are acceptable
	// for this instruction, and how they are to be inserted into the binary base values.
	private OperandDefinition[] operands;
	
	// The name of this instruction type, used to match instruction names extracted
	// from source code lines.
	private String name;
	
	// Instantiates a new default InstructionDefinition. That is, it will create a
	// definition that maps to "numOperations" address slots. It will also create
	// an appropriate number of OperandDefinitions, so that the value of each address
	// slot will be completely determined by one Operand.
	// This is a convenience instruction that allows the Assembler to easily insert
	// values directly into the program (for example, when executing .STRZ).
	public InstructionDefinition(String name, int numOperations) {
		this.name = name;
		this.operations = new int[numOperations];
		this.operands = new OperandDefinition[numOperations];
		for (int i = 0; i < numOperations; i++) {
			this.operands[i] = new OperandDefinition(false, new OperandType[] { OperandType.IMMEDIATE }, i, 15, 0);
		}
	}
	
	// Instantiates a new InstructionDefinition with the given name, binary base values,
	// and OperandDefinitions.
	public InstructionDefinition(String name, int[] operations, OperandDefinition[] operands) {
		this.name = name;
		this.operations = operations;
		this.operands = operands;
	}
	
	// Returns true if the given Instruction (presumably initialized with a name and
	// a collection of Operands) is acceptable for this definition.
	// Namely, it must have the same name as this definition, it must have exactly
	// one Operand for each OperandDefinition in this definition, and the Operands
	// must be acceptable for all the OperandDefinitions.
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
	
	// Returns the base binary values this instruction maps to.
	// This is the binary executable code before the Operand values are inserted.
	public int[] getOperations() {
		return this.operations;
	}
	
	// Gets the collection of OperandDefinitions for this definition.
	public OperandDefinition[] getOperandDefinitions() {
		return this.operands;
	}
	
	// Gets the name of this definition. Used to match instruction names
	// extracted from source code lines.
	public String getName() {
		return this.name;
	}
	
	// Gets the number of address slots this instruction requires.
	public int getSize() {
		return this.operations.length;
	}
}