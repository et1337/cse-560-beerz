package instructions;
import state.MachineState;
import state.MemoryBank;
import util.ByteOperations;
import java.io.PrintStream;
import java.io.InputStream;
/**
 * Handles the Load (immediate mode) instruction.
 */
public class LoadImmediateHandler extends InstructionHandler {
	/**
	 * Executes the given instruction, manipulating the given MachineState accordingly.
	 * @param instruction The integer value of the instruction to execute, including the four op-code bits.
	 * @param state The MachineState to use and modify.
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
	public void execute(PrintStream output, InputStream input, int instruction, MachineState state, MemoryBank memory) {
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
		state.registers[destRegister] = memory.read(memory.read(pc));
		//update the CCR base on the contents of the destination register
		state.updateCcr(state.registers[destRegister]);
	}
	
	@Override
	public String getName() {
		return "Load Immediate";
	}
}