package Simulator.testing;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.OutputStream;
import java.io.PrintStream;
import Common.MemoryBank;
import Simulator.state.MachineState;
import Simulator.program.Machine;
import Simulator.program.ExecutionMode;

public class MachineTest extends TestBase {
	/**
	 * Initial state of memory.
	 */
	private MemoryBank bank;
	/**
	 * Initial state of Machine
	 */
	private Machine machine;
	
	@Before
	public void setUp() {
		bank = new MemoryBank();
	}
	 /**
	  * Tests the Machine with a program.
	  */
	@Test
	public void machineTest1() {
		this.bank.write(0x3000, (short) 0x221A);
		this.bank.write(0x3001, (short) 0x201D);
		this.bank.write(0x3002, (short) 0x5680);
		this.bank.write(0x3003, (short) 0x0C05);
		this.bank.write(0x3004, (short) 0x9020);
		this.bank.write(0x3005, (short) 0x8000);
		this.bank.write(0x3006, (short) 0x301E);
		this.bank.write(0x3007, (short) 0xB21B);
		this.bank.write(0x3008, (short) 0x201F);
		this.bank.write(0x3009, (short) 0xF021);
		this.bank.write(0x300A, (short) 0xF025);
		
		this.bank.write(0x301A, (short) 0xFFFF);
		this.bank.write(0x301B, (short) 0x300B);
		this.bank.write(0x301C, (short) 0x7FFF);
		this.bank.write(0x301D, (short) 0x40E2);
		this.bank.write(0x301E, (short) 0x00CF);
		this.bank.write(0x301F, (short) 0x0043);
		
		// Initialize the machine with a null output stream
		machine = new Machine(TestBase.nullOutStream, bank);
		
		try {
			machine.run(0x3000, ExecutionMode.QUIET);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		MachineState state = machine.getState();
		assertEquals("The CCR should be set to Positive", true, state.ccrPositive);
		assertEquals("Register 0 should hold 0x43", 0x43, state.registers[0]);
		assertEquals("Memory location 0x300A should hold -1", -1, state.registers[1]);
	}
	
}
