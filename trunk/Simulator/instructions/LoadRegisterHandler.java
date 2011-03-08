package Simulator.instructions;
import java.io.PrintStream;
import java.io.InputStream;
import Simulator.state.MachineState;
import Common.MemoryBank;
import Common.ByteOperations;
/**
 * Handles the Load (register mode) instruction.
 */
public class LoadRegisterHandler extends InstructionHandler {
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
		// extract destination register
		int destRegister = ByteOperations.extractValue(instruction,
				DEST_LOW_BIT, DEST_HI_BIT);
		//extract the base register.
		int baseRegister = ByteOperations.extractValue(instruction,
				BASE_LOW_BIT, BASE_HI_BIT);
		//extract the page offset
		int index = ByteOperations.extractValue(instruction, INDEX_LOW_BIT,
				INDEX_HI_BIT);
		index = index & ZERO_MASK;
		state.registers[destRegister] = memory.read(state.registers[baseRegister] + index);
		
		//update the CCR base on the contents of the destination register
		state.updateCcr(state.registers[destRegister]);
	}
	
	@Override
	public String getName() {
		return "Load Register";
	}
}