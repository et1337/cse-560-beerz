package instructions;
import state.MachineState;
import state.MemoryBank;
import util.ByteOperations;
/**
 * Handles a certain type of instruction.
 */
public class StoreImmediateHandler extends InstructionHandler {
	/**
	 * Executes the given instruction, manipulating the given MachineState accordingly.
	 * @param instruction The integer value of the instruction to execute, including the four op-code bits.
	 * @param state The MachineState to use and modify.
	 */
	/**
	 * Offset of the first bit of the destination register.
	 */
	private static final int SRC_LOW_BIT = 9;
	/**
	 * Offset of the last bit of the destination register.
	 */
	private static final int SRC_HI_BIT = 12;
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
		// Get current program counter value
		int pc = state.programCounter;
		
		// Get source register
		int srcRegister = ByteOperations.extractValue(instruction, SRC_LOW_BIT, SRC_HI_BIT);
		
		// Extract page offset
		int pgOffset = ByteOperations.extractValue(instruction, PG_LOW_BIT, PG_HI_BIT);
		
		// Use pc and offset to form address
		pc = pc >> SHIFT;
		pc = pc << SHIFT;
		pc = pc + pgOffset;
		
		// Write value in source register to address formed above
		memory.write(memory.read(pc), state.registers[srcRegister]);
		
		// Update the CCR base on the contents of the source register
		if (state.registers[srcRegister] == 0) {
			state.ccrZero = true;
			state.ccrNegative = false;
			state.ccrPositive = false;
		} else {
			if (state.registers[srcRegister] > 0) {
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