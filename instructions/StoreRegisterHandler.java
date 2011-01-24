package instructions;
import state.MachineState;
import state.MemoryBank;
import util.ByteOperations;
import java.io.PrintStream;
import java.io.InputStream;
/**
 * Handles a certain type of instruction.
 */
public class StoreRegisterHandler extends InstructionHandler {
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
	 * Offset of the first bit of the base register.
	 */
	private static final int BASE_LOW_BIT = 6;
	/**
	 * Offset of the last bit of the base register.
	 */
	private static final int BASE_HI_BIT = 9;
	/**
	 * Offset of the low bit of the index.
	 */
	private static final int INDEX_LOW_BIT = 0;
	/**
	 * Offset of the high bit of the index.
	 */
	private static final int INDEX_HI_BIT = 6;
	/**
	 * Value used to zero extend the value of the index.
	 */
	private static final int ZERO_MASK = 0x003F;
	
	@Override
	public void execute(PrintStream output, InputStream input, int instruction, MachineState state, MemoryBank memory) {
		// extract source register
		int srcRegister = ByteOperations.extractValue(instruction,
				SRC_LOW_BIT, SRC_HI_BIT);
		//extract the base register.
		int baseRegister = ByteOperations.extractValue(instruction,
				BASE_LOW_BIT, BASE_HI_BIT);
		//extract the page offset
		int index = ByteOperations.extractValue(instruction, INDEX_LOW_BIT,
				INDEX_HI_BIT);
		//zero extend the index
		index = index & ZERO_MASK;
		//write the value in the source register to the address in the base register
		//plus the index
		memory.write((state.registers[baseRegister] + index), state.registers[srcRegister]);
	}
	
	@Override
	public String getName() {
		return "Store Register";
	}
}