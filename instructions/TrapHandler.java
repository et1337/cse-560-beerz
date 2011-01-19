package instructions;
import java.io.IOException;
import state.MachineState;
import state.MemoryBank;
import util.ByteOperations;
import java.util.Random;
/**
 * Handles a certain type of instruction.
 */
public class TrapHandler extends InstructionHandler {
	/**
	 * Executes the given instruction, manipulating the given MachineState accordingly.
	 * @param instruction The integer value of the instruction to execute, including the four op-code bits.
	 * @param state The MachineState to use and modify.
	 */
	@Override
	public void execute(int instruction, MachineState state, MemoryBank memory) {
		int trapVector = ByteOperations.extractValue(instruction, 0, 8);
		switch (trapVector) {
			case 0x25:
				state.executing = false;
				break;
			case 0x31:
				System.out.print(state.registers[0]);
				break;
			case 0x21:
				System.out.print((char)state.registers[0]);
				break;
			case 0x43:
				Random RND = new Random();
				state.registers[0]=(short)RND.nextInt();
				if (state.registers[0] == 0) {
					state.ccrZero = true;
				} else {
					if (state.registers[0] > 0) {
						state.ccrPositive = true;
						state.ccrZero = false;
					} else {
						if (state.registers[0] < 0) {
							state.ccrNegative = true;
							state.ccrZero = false;
						}
					}
				}
		
				break;
				
				
		}
	}
}