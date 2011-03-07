package Simulator.instructions;
import java.io.PrintStream;
import java.io.InputStream;
import Simulator.state.MachineState;
import Simulator.state.MemoryBank;
import Simulator.util.ByteOperations;
/**
 * Handles the Jump Subroutine (immediate mode) instruction.
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
	
	/**
	 * Offset of the low bit of the Link bit.
	 */
	private static final int L_LOW_BIT = 11;
	/**
	 * Offset of the high bit of the Link bit.
	 */
	private static final int L_HI_BIT = 12;
	/**
	 * Number of bit to shift the program counter in order to clear out the low
	 * order bits.
	 */
	private static final int SHIFT = 9;
	/**
	 * Register to hold the return pc instruction.
	 */
	private static final int REG = 7;
	@Override
	/**
	 * Execute will extract the page offset from the instruction,
	 * store the current program counter in register seven, then
	 * set the program counter to the page offset.
	 */
	public void execute(PrintStream output, InputStream input, int instruction, MachineState state, MemoryBank memory) {
		int pc = state.programCounter;
		int pgOffset = ByteOperations.extractValue(instruction, PG_LOW_BIT, PG_HI_BIT);
		int linkBit = ByteOperations.extractValue(instruction, L_LOW_BIT, L_HI_BIT);
		// Set register seven equal to the incoming program counter if link bit is set
		if (linkBit == 1) {
			state.registers[REG] = (short) pc;
		}
		
		
		// Set program counter equal to page offset
		pc = pc >> SHIFT;
		pc = pc << SHIFT;
		pc = pc + pgOffset;
		state.programCounter = pc;		
	}
	
	@Override
	public String getName() {
		return "Jump Subroutine Immediate";
	}
}