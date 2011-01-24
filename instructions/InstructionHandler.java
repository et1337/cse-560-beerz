package instructions;
import state.MachineState;
import state.MemoryBank;
import java.io.PrintStream;
import java.io.OutputStream;
/**
 * Handles a certain type of instruction.
 */
public class InstructionHandler {

	/**
	 * Null output stream. This is used when executing without an output stream, so no
	 * NullPointerExceptions are raised.
	 */
	protected static final PrintStream nullStream = new PrintStream(new OutputStream() {
		public void close() {}
		public void flush() {}
		public void write(byte[] b) {}
		public void write(byte[] b, int off, int len) {}
		public void write(int b) {}
	});
	
	/**
	 * Executes the given instruction, manipulating the given MachineState accordingly.
	 * @param output The IO stream to print any output to.
	 * @param instruction The integer value of the instruction to execute, including the four op-code bits.
	 * @param state The MachineState to use and modify when executing.
	 * @param memory The MemoryBank to use and modify when executing.
	 */
	public void execute(PrintStream output, int instruction, MachineState state, MemoryBank memory) {
	}
	
	/**
	 * Convenience method that allows tests to execute the instruction without providing an output stream.
	 * @param output The IO stream to print any output to.
	 * @param instruction The integer value of the instruction to execute, including the four op-code bits.
	 * @param state The MachineState to use and modify when executing.
	 * @param memory The MemoryBank to use and modify when executing.
	 */
	public void execute(int instruction, MachineState state, MemoryBank memory) {
		this.execute(InstructionHandler.nullStream, instruction, state, memory);
	}
	
	/**
	 * Gets the name of the instruction this class handles.
	 * @return The name of the instruction this class handles.
	 */
	public String getName() {
		return "";
	}
}