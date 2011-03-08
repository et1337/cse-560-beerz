package Simulator.instructions;

import Simulator.state.MachineState;
import Common.MemoryBank;
import Common.ByteOperations;
import java.io.PrintStream;
import java.io.InputStream;

/**
 * Handles the Add instruction.
 */
public class AddHandler extends InstructionHandler {
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
	 * The mask used to determine whether the add instruction is SR1 + SR2, or
	 * SR1 + immediate.
	 */
	private static final int FLAG_BIT = 0x0020;
	/**
	 * Offset of the first bit of the destination register.
	 */
	private static final int DEST_LOW_BIT = 9;
	/**
	 * Offset of the last bit of the destination register.
	 */
	private static final int DEST_HI_BIT = 12;
	/**
	 * Offset of the first bit of the 1st source register.
	 */
	private static final int SRC1_LOW_BIT = 6;
	/**
	 * Offset of the last bit of the 1st source register.
	 */
	private static final int SRC1_HI_BIT = 9;
	/**
	 * Offset of the first bit of the 2nd source register.
	 */
	private static final int SRC2_LOW_BIT = 0;
	/**
	 * Offset of the last bit of the 2nd source register.
	 */
	private static final int SRC2_HI_BIT = 3;
	/**
	 * Offset of the first bit of the immediate value.
	 */
	private static final int IMM_LOW_BIT = 0;
	/**
	 * Offset of the last bit of the immediate value.
	 */
	private static final int IMM_HI_BIT = 5;
	/**
	 * Value used to check if the immediate value is positive/negative.
	 */
	private static final int SIGN_FLAG = 16;
	/**
	 * Value used to determine the sign bit of the immediate value.
	 */
	private static final int SIGN_MASK = 0x10;
	/**
	 * Value used to sign-extend the immediate value to 16 bits.
	 */
	private static final int SIGN_EXTEND = 0xFFF0;

	@Override
	public void execute(PrintStream output, InputStream input, int instruction, MachineState state, MemoryBank memory) {
		//extract destination register
		int destRegister = ByteOperations.extractValue(instruction,
				DEST_LOW_BIT, DEST_HI_BIT);
		//extract the 1st source register
		int src1Register = ByteOperations.extractValue(instruction,
				SRC1_LOW_BIT, SRC1_HI_BIT);
		//check to see if add is one source register or two source registers
		if ((instruction & FLAG_BIT) == 0) {
			//extract 2nd source register
			int src2Register = ByteOperations.extractValue(instruction,
					SRC2_LOW_BIT, SRC2_HI_BIT);
			//add 1st and 2nd source registers and store in destination register
			state.registers[destRegister] = (short) (state.registers[src1Register] + state.registers[src2Register]);
		} else {
			//extract immediate value
			short immediateValue = (short) ByteOperations.extractValue(
					instruction, IMM_LOW_BIT, IMM_HI_BIT);
			//determine sign of immediate value and extend it accordingly
			if ((immediateValue & SIGN_MASK) == SIGN_FLAG) {
				immediateValue = (short) (immediateValue | SIGN_EXTEND);
			}
			//add the source register with the immediate value and store in in destination register
			state.registers[destRegister] = (short) (state.registers[src1Register] + immediateValue);

		}
		//update the CCR base on the contents of the destination register
		state.updateCcr(state.registers[destRegister]);
	}
	
	@Override
	public String getName() {
		return "Add";
	}
}
