package instructions;
import state.MachineState;
import state.MemoryBank;
import util.ByteOperations;
import java.io.PrintStream;
import java.io.InputStream;
/**
 * Handles a certain type of instruction.
 */
public class NotHandler extends InstructionHandler {
	/**
	 * Executes the given instruction, manipulating the given MachineState accordingly.
	 * @param instruction The integer value of the instruction to execute, including the four op-code bits.
	 * @param state The MachineState to use and modify.
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
	 * Offset of the first bit of the 1st source register.
	 */
	private static final int SRC1_LOW_BIT = 6;
	/**
	 * Offset of the last bit of the 1st source register.
	 */
	private static final int SRC1_HI_BIT = 9;
	

	@Override
	public void execute(PrintStream output, InputStream input, int instruction, MachineState state, MemoryBank memory) {
		//extract destination register
		int destRegister = ByteOperations.extractValue(instruction,
				DEST_LOW_BIT, DEST_HI_BIT);
		//extract the  source register
		int src1Register = ByteOperations.extractValue(instruction,
				SRC1_LOW_BIT, SRC1_HI_BIT);
		//perform the logical not operation
		state.registers[destRegister] = (short) (~state.registers[src1Register]);
		//update the CCR base on the contents of the destination register
		state.updateCcr(state.registers[destRegister]);
	}
	
	@Override
	public String getName() {
		return "Not";
	}
}