package Assembler;

/** An InstructionDefinition represents a certain type of assembly instruction (like
 * ADD, LD, STR, etc.). Once an assembly instruction is matched with a definition, the
 * definition is used to turn the source code into binary executable code.
 * An InstructionDefinition is comprised of:
 * A name, used to match instruction names extracted from source code lines.
 * A collection of binary values which represent the basis of the final binary code;
 * this is the binary executable code before the Operand values are inserted.
 * A collection of OperandDefinitions which specify which Operands are acceptable
 * for this instruction, and how they are to be inserted into the binary base values.
 */
  public class InstructionDefinition
{
	
	/**
	 *  A collection of binary values which represent the basis of the final binary code.
	 */
	private int[] operations;
	
	/**
	 * A collection of OperandDefinitions which specify which Operands are acceptable
	 * for this instruction, and how they are to be inserted into the binary base values.
	 */
	 private OperandDefinition[] operands;
	 
	
	/** 
	 * The name of this instruction type, used to match instruction names extracted
	 * from source code lines.
	 */
	 private String name;
	 
	/**
	 * Represents the number of memory slots this instruction takes up.
	 * May not correspond to the size of the operations array.
	 */
	private int size;
	
	/** 
	 * Instantiates a new default InstructionDefinition. That is, it will create a
	 * definition that maps to numOperations address slots. If createOperands is
	 * specified, it will also create an appropriate number of OperandDefinitions,
	 * so the value of each address slot will be completely determined by one Operand.
	 * This is a convenience instruction that allows the Assembler to easily insert
	 * values directly into the program (for example, when executing .STRZ).
	 * @param name A string that is the English description of the instruction.
	 * @param numOperations An integer that represents the number of operations.
	 * @param createOperands A boolean used to determine whether or not to create the
	 * 		  OperandDefinition. 
	 */
	public InstructionDefinition(String name, int numOperations, boolean createOperands) {
		this.name = name;
		this.size = numOperations;
		if (createOperands) {
			this.operations = new int[numOperations];
			this.operands = new OperandDefinition[numOperations];
			for (int i = 0; i < numOperations; i++) {
				this.operands[i] = new OperandDefinition(false, new OperandType[] { OperandType.IMMEDIATE }, i, 15, 0);
			}
		}
		else {
			this.operations = new int[0];
			this.operands = new OperandDefinition[0];
		}
	}
	
	/** 
	 * Instantiates a new InstructionDefinition with the given name, binary base values,
	 * and OperandDefinitions.
	 * @param name A string that is the English description of the instruction.
	 * @param operations An array of integers
	 * @param operands An array of OperandDefinitions.
	 */
	public InstructionDefinition(String name, int[] operations, OperandDefinition[] operands) {
		this.name = name;
		this.size = operations.length;
		this.operations = operations;
		this.operands = operands;
	}
	
	/** 
	 * Returns true if the given Instruction (presumably initialized with a name and
	 * a collection of Operands) is acceptable for this definition.
	 * Namely, it must have the same name as this definition, it must have exactly
	 * one Operand for each OperandDefinition in this definition, and the Operands
	 * must be acceptable for all the OperandDefinitions.
	 * @param instruction The instruction to be checked for validity.
	 * @return true if and only if the instruction is a valid instruction name.
	 */
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
	
	/**  
	 * This is the binary executable code before the Operand values are inserted.
	 * @return the base binary values this instruction maps to.
	 */
	public int[] getOperations() {
		return (int[])this.operations.clone();
	}
	
	/** 
	 * Gets the collection of OperandDefinitions for this definition.
	 * @return An array of OperandDefinitions.
	 */
	public OperandDefinition[] getOperandDefinitions() {
		return this.operands;
	}
	
	/** 
	 * @return masks An array of bitmasks which specify which parts of the instruction
	 * are relocatable.
	 */
	public int[] getRelocationMasks() {
		int[] masks = new int[this.operations.length];
		for (int i = 0; i < this.operands.length; i++) {
			OperandDefinition operandDefinition = this.operands[i];
			if (operandDefinition.isRelocatable()) {
				masks[operandDefinition.getOperationIndex()] |= operandDefinition.getMask();
			}
		}
		return masks;
	}
	
	/**
	 * Gets the name of this definition. Used to match instruction names
	 * extracted from source code lines.
	 * @return A string representing the name of the definition.
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * @return An integer representing the number of address slots this instruction requires.
	 */
	public int getSize() {
		return this.size;
	}
}