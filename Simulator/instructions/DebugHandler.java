package instructions;
import state.MachineState;
import state.MemoryBank;
import util.ByteOperations;
import java.io.PrintStream;
import java.io.InputStream;
/**
 * Handles the Debug instruction.
 */
public class DebugHandler extends InstructionHandler {
	/**
	 * Executes the given instruction, manipulating the given MachineState accordingly.
	 * @param instruction The integer value of the instruction to execute, including the four op-code bits.
	 * @param state The MachineState to use and modify.
	 */
	/**
	 * Total number of registers.
	 */
	private static final int REG_COUNT = 8;
	/** 
	 * This method displays the contents of the program counter, registers and the ccr.
	 */
	@Override
	public void execute(PrintStream output, InputStream input, int instruction, MachineState state, MemoryBank memory) {
		StringBuffer registers = new StringBuffer();
		StringBuffer registerLabels = new StringBuffer();
		output.println("Registers:");
		for (int i = 0; i < REG_COUNT; i++) {
			registers.append("0x");
			registers.append(ByteOperations.getHex(state.registers[i], 4));
			registers.append(" ");
			registerLabels.append("r");
			registerLabels.append(i + 1);
			registerLabels.append("     ");
		}
		output.println(registers.toString());
		output.println(registerLabels.toString());
		
		int n = state.ccrNegative ? 1 : 0;
		int z = state.ccrZero ? 1 : 0;
		int p = state.ccrPositive ? 1 : 0;
		output.println("0x" + ByteOperations.getHex(state.programCounter, 4) + " " + n + "      " + z + "      " + p);
		output.println("PC     N      Z      P");
	}
	
	@Override
	public String getName() {
		return "Debug";
	}
}