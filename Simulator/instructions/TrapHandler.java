package Simulator.instructions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.InputStream;
import java.util.Random;
import Simulator.state.MachineState;
import Common.MemoryBank;
import Common.ByteOperations;

/**
 * Handles the Trap instruction.
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
	public void execute(PrintStream output, InputStream input, int instruction, MachineState state, MemoryBank memory) {
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
			state.updateCcr(state.registers[0]);
			break;
		case 0x22:
			int memLocation = state.registers[0];
			while (memory.read(memLocation) != 0) {
				output.print((char) memory.read(memLocation));
				memLocation++;
			}
			break;
		case 0x23:
			InputStreamReader reader = new InputStreamReader(input);

			output.print("? ");
			try {
				inputValue = reader.read();
			} catch (IOException e) {
				e.printStackTrace(output);
			}
			inputValue = ByteOperations.extractValue(inputValue, 0, 8);
			state.registers[0] = (short) inputValue;
			state.updateCcr(state.registers[0]);
			break;
		case 0x33:
			output.print("d? ");
			int number = 0;
			try {
				BufferedReader reader1 = new BufferedReader(new InputStreamReader(input));
				number = Integer.parseInt(reader1.readLine());
				reader1.close();
			}
			catch (IOException e) {
				e.printStackTrace(output);
				e.printStackTrace();
			}
			catch (NumberFormatException e) {
				output.println("Input by user was not a number.");
			}

			state.registers[0] = (short) number;
			state.updateCcr(state.registers[0]);
			break;
		}
		state.programCounter = pc;
	}
	
	@Override
	public String getName() {
		return "Trap";
	}
}