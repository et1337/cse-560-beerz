package instructions;
import state.MachineState;
import state.MemoryBank;
import util.ByteOperations;
import java.io.PrintStream;
import java.io.InputStream;
/**
 * Handles a certain type of instruction.
 */
public class ReturnHandler extends InstructionHandler {
	/**
	 * Executes the given instruction, manipulating the given MachineState accordingly.
	 * @param instruction The integer value of the instruction to execute, including the four op-code bits.
	 * @param state The MachineState to use and modify.
	 */
	/**
	 * Register to hold the return pc instruction.
	 */
	private static final int REG = 7;
	@Override
	public void execute(PrintStream output, InputStream input, int instruction, MachineState state, MemoryBank memory) {
		state.programCounter = state.registers[REG];
	}
	
	@Override
	public String getName() {
		return "Return";
	}
}