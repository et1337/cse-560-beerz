package Assembler;

/**
 * An Instruction corresponds to one line of assembly source code. It may
 * correspond to 0 or more memory allocations in the final program. An
 * Instruction has a definition, a name, and a collection of Operands. When
 * rendering binary executable code, the Instruction will insert all of its
 * operands into its definition and return the result (see getCodes).
 */
public class Instruction {

	/**
	 * Defines the binary layout of this Instruction
	 */
	private InstructionDefinition definition;

	/**
	 * Collection of operands for this Instruction
	 */
	private Operand[] operands;

	/**
	 * Source assembly code line corresponding to this Instruction
	 */
	private String source;

	/**
	 * Parsed from the source code; used to match up with the correct
	 * InstructionDefinition.
	 */
	private String name;

	/**
	 * Instantiates an empty Instruction with the given name and line of source
	 * code.
	 */
	public Instruction(String name, String source) {
		this.name = name;
		this.source = source;
	}

	/**
	 * Sets the definition of this Instruction, as well as the definition of all
	 * this Instruction's Operands. Note: setOperands must be called first.
	 * 
	 * @param definition
	 *            the InstructionDefinition to be set
	 */
	public void setDefinition(InstructionDefinition definition) throws Exception {
		this.definition = definition;
		OperandDefinition[] definitions = this.definition
				.getOperandDefinitions();
		if (definitions != null) {
			for (int i = 0; i < definitions.length; i++) {
				this.operands[i].setDefinition(definitions[i]);
			}
		}
	}

	/**
	 * Initializes the values of this Instruction's Operands using the given
	 * string values extracted straight from the source code. The LiteralTable
	 * is needed because literals are defined in the Operand constructor.
	 * 
	 * @param values
	 *            the String extracted from the source code
	 * @param literals
	 *            the LiteralTable used by this Instruction
	 * @throws Exception
	 */
	public void setOperands(String[] values, LiteralTable literals)
			throws Exception {
		this.operands = new Operand[values.length];
		for (int i = 0; i < values.length; i++) {
			this.operands[i] = new Operand(values[i], literals);
		}
	}

	/**
	 * @returns the line of assembly source code associated with this
	 *          Instruction.
	 */
	public String getSource() {
		return this.source;
	}

	/**
	 * @return the name of this Instruction.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the definition of this Instruction.
	 */
	public InstructionDefinition getDefinition() {
		return this.definition;
	}

	/**
	 * @return the collection of Operands for this Instruction.
	 */
	public Operand[] getOperands() {
		return this.operands;
	}
	
	/** 
	 * @return masks An array of bitmasks which specify which parts of the instruction
	 * are relocatable.
	 */
	public int[] getRelocationMasks(SymbolTable symbols) {
		int[] masks = new int[this.definition.getOperations().length];
		for (int i = 0; i < this.operands.length; i++) {
			Operand operand = this.operands[i];
			if (operand.isRelocatable(symbols)) {
				masks[operand.getDefinition().getOperationIndex()] |= operand.getDefinition().getMask();
			}
		}
		return masks;
	}

	/**
	 * Gets the final executable binary codes called for by this Instruction.
	 * The SymbolTable and LiteralTable are used to resolve Operand values.
	 * 
	 * @param symbols
	 *            the SymbolTable used by this Instruction
	 * @param literals
	 *            the LiteralTable used by this Instruction
	 * @return the binary codes used by this Instruction
	 * @throws Exception
	 */
	public int[] getCodes(SymbolTable symbols, LiteralTable literals)
			throws Exception {
		int[] result = this.definition == null ? new int[] { } : this.definition.getOperations();
		if (this.operands != null && result.length > 0) {
			for (Operand operand : this.operands) {
				operand.insert(result, symbols, literals);
			}
		}
		return result;
	}
}