package state;
import java.io.PrintStream;
import instructions.DebugHandler;

/**
 * This class represents the state of a virtual machine, not including the memory, which is represented by a MemoryBank.
 */
public class MachineState {
	/**
	 * The number of registers supported by all Machines.
	 */
	public static final int NUM_REGISTERS = 8;

	/**
	 * Represents the negative condition code register.
	 * True if and only if the last written register is negative.
	 */
	public boolean ccrNegative = false;
	
	/**
	 * Represents the positive condition code register.
	 * True if and only if the last written register is positive.
	 */
	public boolean ccrPositive = false;
	
	/**
	 * Represents the zero condition code register.
	 * True if and only if the last written register is zero.
	 */
	public boolean ccrZero = true;
	
	/**
	 * True if and only if the machine is actively executing.
	 */
	public boolean executing = true;
	
	/**
	 * Array representing all the registers in the machine. The initial value of all registers is zero.
	 */
	public short[] registers = new short[MachineState.NUM_REGISTERS];
	
	public int programCounter;
	
	public MachineState clone() {
		MachineState x = new MachineState();
		x.ccrNegative = this.ccrNegative;
		x.ccrPositive = this.ccrPositive;
		x.ccrZero = this.ccrZero;
		x.programCounter = this.programCounter;
		x.executing = this.executing;
		for(int i = 0; i < MachineState.NUM_REGISTERS; i++) {
			x.registers[i] = this.registers[i];
		}
		return x;
	}
	
	/**
	 * Outputs this MachineState to the given IO stream according to the DEBUG
	 * instruction specifications.
	 */
	public void display(PrintStream output) {
		new DebugHandler().execute(output, 0, this, null);
	}
}