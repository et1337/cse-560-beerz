package instructions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import state.MachineState;
import state.MemoryBank;
import util.ByteOperations;
import java.io.PrintStream;
import java.util.Random;

/**
 * Handles a certain type of instruction.
 */
public class TrapHandler extends InstructionHandler {
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
	@Override
	public void execute(PrintStream output, int instruction, MachineState state, MemoryBank memory) {
		int pc = state.programCounter;
		int trapVector = ByteOperations.extractValue(instruction, 0, 8);
		int inputValue = 0;
		switch (trapVector) {
		case 0x25:
			state.executing = false;
			break;
		case 0x31:
			output.print(state.registers[0]);
			break;
		case 0x21:
			output.print((char) state.registers[0]);
			break;
		case 0x43:
			Random RND = new Random();
			state.registers[0] = (short) RND.nextInt();
			if (state.registers[0] == 0) {
				state.ccrZero = true;
				state.ccrPositive = false;
				state.ccrNegative = false;
			} else {
				if (state.registers[0] > 0) {
					state.ccrPositive = true;
					state.ccrZero = false;
					state.ccrNegative = false;
				} else {
					if (state.registers[0] < 0) {
						state.ccrNegative = true;
						state.ccrZero = false;
						state.ccrPositive = false;
					}
				}
			}

			break;
		case 0x22:
			int memLocation = state.registers[0];
			while (memory.read(memLocation) != 0) {
				output.print((char) memory.read(memLocation));
				memLocation++;
			}
			break;
		case 0x23:
			InputStreamReader reader = new InputStreamReader(System.in);

			output.print("? ");
			try {
				inputValue = reader.read();
			} catch (IOException e) {
				e.printStackTrace(output);
			}
			inputValue = ByteOperations.extractValue(inputValue, 0, 8);
			state.registers[0] = (short) inputValue;
			if (state.registers[0] == 0) {
				state.ccrZero = true;
				state.ccrPositive = false;
				state.ccrNegative = false;
			} else {
				if (state.registers[0] > 0){
					state.ccrNegative = false;;
					state.ccrPositive = true;
					state.ccrZero = false;
				} else {
					state.ccrNegative = true;
					state.ccrPositive = false;
					state.ccrZero = false;
				}
			}
			break;
		case 0x33:
			output.print("d? ");
			int number = 0;
			BufferedReader reader1 = null;
			try {
				reader1 = new BufferedReader(new InputStreamReader(System.in));
				String input = reader1.readLine();
				number = Integer.parseInt(input);
				reader1.close();
			}
			catch (IOException e) {
				e.printStackTrace(output);
			}
			catch (NumberFormatException e) {
				output.println("Input by user was not a number.");
			}

			state.registers[0] = (short) number;
			if (state.registers[0] == 0) {
				state.ccrZero = true;
				state.ccrPositive = false;
				state.ccrNegative = false;
			} else {
				if (state.registers[0] > 0){
					state.ccrNegative = false;;
					state.ccrPositive = true;
					state.ccrZero = false;
				} else {
					state.ccrNegative = true;
					state.ccrPositive = false;
					state.ccrZero = false;
				}
			}
			break;
		}
		state.programCounter = pc;
	}
	
	@Override
	public String getName() {
		return "Trap";
	}
}