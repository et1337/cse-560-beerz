package instructions;
import state.MachineState;
import state.MemoryBank;
import util.ByteOperations;
/**
 * Handles a certain type of instruction.
 */
public class JumpSubroutineImmediateHandler extends InstructionHandler {
	/**
	 * Executes the given instruction, manipulating the given MachineState accordingly.
	 * @param instruction The integer value of the instruction to execute, including the four op-code bits.
	 * @param state The MachineState to use and modify.
	 */
	/**
	 * Offset of the low bit of the page offset in the instruction.
	 */
	private static final int PG_LOW_BIT = 0;
	/**
	 * Offset of the high bit of the page offset in the instruction
	 */
	private static final int PG_HI_BIT = 9;
	@Override
	/**
	 * Execute will extract the page offset from the instruction,
	 * store the current program counter in register seven, then
	 * set the program counter to the page offset.
	 */
	public void execute(int instruction, MachineState state, MemoryBank memory) {
		int pc = state.programCounter;
		int pgOffset = ByteOperations.extractValue(instruction, PG_LOW_BIT, PG_HI_BIT);
		
		// Set register seven equal to 
	}
}