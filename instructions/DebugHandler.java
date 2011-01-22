package instructions;
import state.MachineState;
import state.MemoryBank;
import util.ByteOperations;
/**
 * Handles a certain type of instruction.
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
	 * Mask used to eliminate top 8 bits.
	 */
	private static final int HEX_MASK = 0x0000FFFF;
	/** 
	 * This method displays the contents of the program counter, registers and the ccr.
	 */
	@Override
	public void execute(int instruction, MachineState state, MemoryBank memory) {
	System.out.println("Progam Counter = 0x" + Integer.toHexString(state.programCounter & HEX_MASK).toUpperCase());
		int n = 0;
		int z = 0;
		int p = 0;
	
		for (int i = 0; i < REG_COUNT; i++) 
		{
			System.out.println("Register " + i + " = 0x" + Integer.toHexString(state.registers[i] & HEX_MASK).toUpperCase());
		}
		if (state.ccrNegative == true) {
			n = 1;
		}
		if (state.ccrZero == true) {
			z = 1;
		}
		if (state.ccrPositive == true) {
			p = 1;
		}
		System.out.println("CCR: N = " + n + " Z = " + z + " P = " + p);
	}
}