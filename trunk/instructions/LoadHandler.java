package instructions;

import state.MachineState;
import state.MemoryBank;
import util.ByteOperations;

/**
 * Handles a certain type of instruction.
 */
public class LoadHandler extends InstructionHandler {
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
	 * Offset of the first bit of the destination register.
	 */
	private static final int DEST_LOW_BIT = 9;
	/**
	 * Offset of the last bit of the destination register.
	 */
	private static final int DEST_HI_BIT = 12;
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

	@Override
	public void execute(int instruction, MachineState state, MemoryBank memory) {
		//get the value in the pc
		int pc = state.programCounter;
		// extract destination register
		int destRegister = ByteOperations.extractValue(instruction,
				DEST_LOW_BIT, DEST_HI_BIT);
		//extract the page offset
		int pgOffset = ByteOperations.extractValue(instruction, PG_LOW_BIT,
				PG_HI_BIT);
		
		pc = pc >> SHIFT;
		pc = pc << SHIFT;
		pc = pc + pgOffset;
		state.registers[destRegister] = memory.read(pc);
		//update the CCR base on the contents of the destination register
		if (state.registers[destRegister] == 0) {
			state.ccrZero = true;
			state.ccrNegative = false;
			state.ccrPositive = false;
		} else {
			if (state.registers[destRegister] > 0) {
				state.ccrZero = false;
				state.ccrNegative = false;
				state.ccrPositive = true;
			} else {
				state.ccrZero = false;
				state.ccrNegative = true;
				state.ccrPositive = false;
			}
		}
	}
}