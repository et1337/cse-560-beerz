package Simulator.testing;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import Simulator.state.MemoryBank;
import Simulator.state.MachineState;
import Simulator.instructions.StoreRegisterHandler;

public class StoreRegisterTest extends TestBase {
	/**
	 * Contains the initial state of the memory used for testing.
	 */
	private MemoryBank bank;
	
	/**
	 * Contains the initial state of the machine used for testing.
	 */
	private MachineState state;
	
	/**
	 * Set up the initial state of the machine and memory for all the tests.
	 */
	@Before
	public void init() {
		bank = new MemoryBank();
		state = new MachineState();
		this.state.programCounter = 0x3000;
	}
	/**
	 * Tests Store Register function out of the range of the program counter, in an address
	 * after the program counter.
	 */
	@Test
	public void storeRegisterLaterTest() {
		this.state.registers[1] = 0x0ABC;
		this.state.registers[3] = 0x5000;
		new StoreRegisterHandler().execute(0x72D9, this.state, this.bank);
		assertEquals("Location 0x5019 should hold 0xABC", 0xABC, this.bank.read(0x5019));
		
	}
	/**
	 * Tests Store Register function out of the range of the program counter, in an address 
	 * before the program counter.
	 */
	@Test
	public void storeRegisterEarlierTest() {
		this.state.registers[1] = (short) 0xEFFF;
		this.state.registers[3] = 0x1000;
		new StoreRegisterHandler().execute(0x72C7, this.state, this.bank);
		assertEquals("Location 0x1007 should hold -0x1001", -0x1001, this.bank.read(0x1007));
		
	}
	 /**
	  * Tests Store Register function in the range of the program counter.
	  */
	@Test
	public void storeRegisterWithinRangeTest() {
		this.state.registers[1] = 0x0;
		this.state.registers[3] = 0x3100;
		this.bank.write(0x3139, (short) 0xEEEE);
		new StoreRegisterHandler().execute(0x72F9, this.state, this.bank);
		assertEquals("Location 0x3139 should hold 0x0", 0x0, this.bank.read(0x3139));
	}
}
