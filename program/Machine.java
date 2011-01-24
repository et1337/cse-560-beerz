package program;
import java.io.PrintStream;
import instructions.InstructionMappings;
import state.MachineState;
import state.MemoryBank;
import util.ByteOperations;

/**
 * This class represents a virtual machine capable of executing instructions stored in a MemoryBank.
 * This machine supports the following instructions:
 * ADD
 * AND (immediate mode and register mode)
 * BR (8 different modes for dealing with the CCRs)
 * DBUG
 * JSR
 * JSRR
 * LD
 * LDI
 * LDR
 * LEA
 * NOT
 * RET
 * ST
 * STI
 * STR
 * TRAP
 */
public class Machine {
	/**
	 * MemoryBank representing the machine's memory.
	 */
	private MemoryBank memory;
	
	/**
	 * MachineState representing the current state of this Machine.
	 */
	private MachineState state;
	
	/**
	 * IO stream for displaying output.
	 */
	private PrintStream output;
	
	/**
	 * Offset of the low bit of the page in the instruction.
	 */
	private static final int PG_LOW_BIT = 9;
	/**
	 * Offset of the high bit of the page in the instruction
	 */
	private static final int PG_HI_BIT = 16;
	
	/**
	 * Creates a new virtual machine using the given MemoryBank.
	 * @param _memory The MemoryBank to use to represent the machine's memory.
	 */
	public Machine(PrintStream _output, MemoryBank _memory) {
		this.memory = _memory;
		this.output = _output;
		this.state = new MachineState();
	}
	
	/**
	 * Begins quiet execution at the given address in memory.
	 * @param startAddress The memory address to begin execution at.
	 */
	public void run(int startAddress, ExecutionMode mode) throws Exception {
		this.state.programCounter = startAddress;
		int page = 0;
		while (this.state.executing) {
			page = ByteOperations.extractValue(this.state.programCounter, Machine.PG_LOW_BIT, Machine.PG_HI_BIT);
			if (mode == ExecutionMode.TRACE || mode == ExecutionMode.STEP) {
				this.memory.displayPage(this.output, page);
				this.state.display(this.output);
			}
			
			if (mode == ExecutionMode.STEP) {
				System.in.read();
			}
		
			int instruction = this.memory.read(this.state.programCounter);
			if (mode == ExecutionMode.TRACE || mode == ExecutionMode.STEP) {
				output.println("Executing instruction: " + InstructionMappings.getInstructionName(InstructionMappings.getOpCode(instruction)));
			}
			this.execute(instruction);
			
		}
		if (mode == ExecutionMode.TRACE || mode == ExecutionMode.STEP) {
			this.memory.displayPage(this.output, page);
			this.state.display(this.output);
		}
	}
	
	/**
	 * Executes the given instruction.
	 */
	private void execute(int instruction) throws Exception {
		this.state.programCounter++;
		InstructionMappings.execute(this.output, instruction, this.state, this.memory);
	}
	
	/**
	 * Gets a new MachineState which describes the current state of this machine.
	 * @return A new MachineState describing the current state of this machine.
	 */
	public MachineState getState() {
		return this.state.clone();
	}
}