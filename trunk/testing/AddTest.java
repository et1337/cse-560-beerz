package testing;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import state.MemoryBank;
import state.MachineState;
import instructions.AddHandler;
/**
 * 
 * @author Elliot
 * 
 * Tests the functionality of the Add function
 *
 */

public class AddTest {
	
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
	}
	
	/**
	 * Tests positive addition
	 */
	@Test
	public void addPosTest() {
		state.registers[1] = 0x41;
		state.registers[3] = 0x2A;
		new AddHandler().execute(0x12C1, this.state, this.bank);
		int reg1 = state.registers[1];
		assertEquals("Should equal 6B", 0x6B, reg1);		 
	}
	
	/**
	 * Tests addition with zero
	 */
	
	@Test
	public void addZeroTest() {
		state.registers[1] = 0x0;
		state.registers[3] = 0x0;
		new AddHandler().execute(0x12C1, this.state, this.bank);
		int reg1 = state.registers[1];
		assertEquals("Should equal 0x0", 0x0, reg1);
		
		state.registers[3] = 0x4;
		new AddHandler().execute(0x12C1, this.state, this.bank);
		reg1 = state.registers[1];
		assertEquals("Should equal 0x4", 0x4, reg1);
	}
	
	/**
	 * Tests negative addition
	 */
	@Test
	public void addNegTest() {
		state.registers[5] = -365;
		state.registers[1] = -264;
		new AddHandler().execute(0x1A45, this.state, this.bank);
		assertEquals("Should equal -265", -629, state.registers[5]);
	}
	
	/**
	 * Tests overflow produced by the addition of two positive numbers.
	 * Example given in the Machine Characteristics handout.
	 */
	@Test
	public void addNegOverflowTest() {
		state.registers[5] = 32767;
		state.registers[1] = 32767;
		new AddHandler().execute(0x1A45, this.state, this.bank);
		assertEquals("Should equal 0xFFFE", state.registers[5], -2);
	}
	
	/**
	 * Tests overflow produced by the addition of two negative numbers
	 */
	@Test
	public void addPosOverflowTest(){
		state.registers[5] = -32764;
		state.registers[1] = -32764;
		new AddHandler().execute(0x1A45, this.state, this.bank);
		assertEquals("Should equal 8", state.registers[5], 8);		
	}
	
}