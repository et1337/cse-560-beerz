package Simulator.instructions;

import java.io.PrintStream;
import java.io.InputStream;
import Simulator.state.MachineState;
import Common.MemoryBank;
import Common.ByteOperations;

/**
 * Handles the Jump Subroutine (register mode) instruction.
 */
public class JumpSubroutineRegisterHandler extends InstructionHandler {
	/**
	 * Executes the given instruction, manipulating the given MachineState
	 * accordingly.
	 * 
	 * @param instruction
	 *            The integer value of the instruction to execute, including the
	 *            four op-code bits.
	 * @param state
	 *            The MachineState to use and modify.
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
	/**
	 * Offset of the low bit of the Link bit.
	 */
	private static final int L_LOW_BIT = 11;
	/**
	 * Offset of the high bit of the Link bit.
	 */
	private static final int L_HI_BIT = 12;
	/**
	 * Register to hold the return pc instruction.
	 */
	private static final int REG = 7;
	@Override
	public void execute(PrintStream output, InputStream input, int instruction, MachineState state, MemoryBank memory) {
		int pc = state.programCounter;
		// extract the base register.
		int baseRegister = ByteOperations.extractValue(instruction,
				BASE_LOW_BIT, BASE_HI_BIT);
		// extract the page offset
		int index = ByteOperations.extractValue(instruction, INDEX_LOW_BIT,
				INDEX_HI_BIT);
		int linkBit = ByteOperations.extractValue(instruction, L_LOW_BIT,
				L_HI_BIT);
		// Set register seven equal to the incoming program counter if link bit
		// is set
		if (linkBit == 1) {
			state.registers[REG] = (short) pc;
		}
		index = index & ZERO_MASK;
		state.programCounter = (memory.read(state.registers[baseRegister] + index));
	}
	
	@Override
	public String getName() {
		return "Jump Subroutine Register";
	}
}