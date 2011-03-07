package Simulator.instructions;

import Simulator.state.MachineState;
import Simulator.state.MemoryBank;
import Simulator.util.ByteOperations;
import java.io.PrintStream;
import java.io.InputStream;

/**
 * Handles the Branch instruction.
 */
public class BranchHandler extends InstructionHandler {
	/**
	 * Executes the given instruction, manipulating the given MachineState
	 * accordingly.
	 * 
	 * @param instruction
	 *            The integer value of the instruction to execute, including the
	 *            four op-code bits.
	 * @param state
	 *            The MachineState to use and modify.
	 */
	/**
	 * Offset of the low bit of the branch code in the instruction.
	 */
	private static final int CCR_LOW_BIT = 9;
	/**
	 * Offset of the high bit of the branch code in the instruction.
	 */
	private static final int CCR_HI_BIT = 12;
	/**
	 * Offset of the low bit of the page offset in the instruction.
	 */
	private static final int PG_LOW_BIT = 0;
	/**
	 * Offset of the high bit of the page offset in the instruction
	 */
	private static final int PG_HI_BIT = 9;

	/**
	 * Number of bit to shift the program counter in order to clear out the low
	 * order bits.
	 */
	private static final int SHIFT = 9;
	/**
	 * Value of branch if positive.
	 */
	private static final int BR_POS = 1;
	/** 
	 * Value of branch if zero.
	 */
	private static final int BR_ZERO = 2;
	/**
	 * Value of branch if not negative.
	 */
	private static final int BR_NOT_NEG = 3;
	/**
	 * Value of branch if negative.
	 */
	private static final int BR_NEG = 4;
	/**
	 * Value of branch if not zero.
	 */
	private static final int BR_NOT_ZERO = 5;
	/**
	 * Value of branch if not positive.
	 */
	private static final int BR_NOT_POS = 6;
	/**
	 * Value of branch always.
	 */
	private static final int BR_ALWAYS = 7;
	
	/**
	 * This method extracts the branch code from the instruction and then compares it
	 * to one of seven possible branch codes (the eight being a nop) and then modifes
	 * the program counter by concatenating the upper 7 bits of the program counter
	 * with the page offset specified by the instruction. 
	 */
	@Override
	public void execute(PrintStream output, InputStream input, int instruction, MachineState state, MemoryBank memory) {
		int pc = state.programCounter;
		int branchCode = ByteOperations.extractValue(instruction, CCR_LOW_BIT,
				CCR_HI_BIT);

		int pgOffset = ByteOperations.extractValue(instruction, PG_LOW_BIT,
				PG_HI_BIT);
		switch (branchCode) {
			case 0:
				break;
			case BR_POS:
				if (state.ccrPositive == true) {
					pc = pc >> SHIFT;
					pc = pc << SHIFT;
					pc = pc + pgOffset;
					state.programCounter = pc;
				}
				break;
			case BR_ZERO:
				if (state.ccrZero == true) {
					pc = pc >> SHIFT;
					pc = pc << SHIFT;
					pc = pc + pgOffset;
					state.programCounter = pc;
				}
				break;
			case BR_NOT_NEG:
				if (state.ccrPositive == true || state.ccrZero == true) {
					pc = pc >> SHIFT;
					pc = pc << SHIFT;
					pc = pc + pgOffset;
					state.programCounter = pc;
				}
				break;
			case BR_NEG:
				if (state.ccrNegative == true) {
					pc = pc >> SHIFT;
					pc = pc << SHIFT;
					pc = pc + pgOffset;
					state.programCounter = pc;
				}
				break;
			case BR_NOT_ZERO:
				if (state.ccrPositive == true || state.ccrNegative == true) {
					pc = pc >> SHIFT;
					pc = pc << SHIFT;
					pc = pc + pgOffset;
					state.programCounter = pc;
				}
				break;
			case BR_NOT_POS:
				if (state.ccrNegative == true || state.ccrZero == true) {
					pc = pc >> SHIFT;
					pc = pc << SHIFT;
					pc = pc + pgOffset;
					state.programCounter = pc;
				}
				break;
			case BR_ALWAYS:
				if (state.ccrPositive == true || state.ccrNegative == true || state.ccrZero == true) {
					pc = pc >> SHIFT;
					pc = pc << SHIFT;
					pc = pc + pgOffset;
					state.programCounter = pc;
				}
				break;
		}
	}
	
	@Override
	public String getName() {
		return "Branch";
	}
}
