import instructions.InstructionMappings;
import state.MachineState;
import state.MemoryBank;

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
	 * Creates a new virtual machine using the given MemoryBank.
	 * @param _memory The MemoryBank to use to represent the machine's memory.
	 */
	public Machine(MemoryBank _memory) {
		this.memory = _memory;
		this.state = new MachineState();
	}
	
	/**
	 * Begins quiet execution at the given address in memory.
	 * @param startAddress The memory address to begin execution at.
	 */
	public void run(int startAddress) throws Exception {
		this.state.programCounter = startAddress;
		while (this.state.executing) {
			this.execute(this.memory.read(this.state.programCounter));
		}
	}
	
	/**
	 * Executes the given instruction.
	 */
	private void execute(int instruction) throws Exception {
		this.state.programCounter++;
		InstructionMappings.execute(instruction, this.state, this.memory);
	}
	
	/**
	 * Gets a new MachineState which describes the current state of this machine.
	 * @return A new MachineState describing the current state of this machine.
	 */
	public MachineState getState() {
		return this.state.clone();
	}
}