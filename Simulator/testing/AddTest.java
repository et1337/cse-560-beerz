package Simulator.testing;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import Simulator.state.MemoryBank;
import Simulator.state.MachineState;
import Simulator.instructions.AddHandler;
/**
 * 
 * @author Elliot
 * 
 * Tests the functionality of the Add function
 *
 */

public class AddTest extends TestBase {
	
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
	 * First set of tests are for register plus register addition.
	 */
	
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
		assertEquals("CCR Positive bit should be set", state.ccrPositive, true);
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
		assertEquals("CCR Zero bit should be set", state.ccrZero, true);
		
		state.registers[3] = 0x4;
		new AddHandler().execute(0x12C1, this.state, this.bank);
		reg1 = state.registers[1];
		assertEquals("Should equal 0x4", 0x4, reg1);
		assertEquals("CCR Positive should be set", state.ccrPositive, true);
		assertEquals("CCR Zero should not be set", state.ccrZero, false);
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
		assertEquals("CCR Negative should be set", state.ccrNegative, true);
		assertEquals("CCR Zero should not be set", state.ccrZero, false);
	}
	
	/**
	 * Tests addition with a negative and positive number.
	 */
	@Test
	public void addMixedTest() {
		//Result will be negative
		state.registers[5] = -0x10AA;
		state.registers[1] = 0x4AD;
		new AddHandler().execute(0x1A45, this.state, this.bank);
		assertEquals("Should equal -0xBFD", -0xBFD, this.state.registers[5]);
		assertEquals("Negative CCR should be set", true, this.state.ccrNegative);
		
		//Result will be positive
		state.registers[5] = 0x10AA;
		state.registers[1] = -0x4AD;
		new AddHandler().execute(0x1A45, this.state, this.bank);
		assertEquals("Should equal 0xBFD", 0xBFD, this.state.registers[5]);
		assertEquals("Positive ccr should be set", true, this.state.ccrPositive);
		
		//Result will be zero
		state.registers[5] = 0x4EA;
		state.registers[1] = -0x4EA;
		new AddHandler().execute(0x1A45, this.state, this.bank);
		assertEquals("Should equal 0x0", 0x0, this.state.registers[5]);
		assertEquals("CCR bit should be zero", true, this.state.ccrZero);
		
		
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
		assertEquals("CCR Negative should be set", state.ccrNegative, true);
	}
	
	/**
	 * Tests overflow produced by the addition of two negative numbers
	 */
	@Test
	public void addPosOverflowTest() {
		state.registers[5] = -32764;
		state.registers[1] = -32764;
		new AddHandler().execute(0x1A45, this.state, this.bank);
		assertEquals("Should equal 8", state.registers[5], 8);		
		assertEquals("CCR Positive should be set", state.ccrPositive, true);
	}
	
	/**
	 * Second set of tests are for register plus immediate operand
	 * 
	 */
	
	/**
	 * Tests addition of two positive numbers.
	 */
	@Test
	public void addPosImmTest() {
		state.registers[1] = 0x2A;
		new AddHandler().execute(0x166F, this.state, this.bank);
		assertEquals("Should equal 0x39", 0x39, state.registers[3]);
		assertEquals("CCR positive should be set", state.ccrPositive, true);
		
	}
	
	/**
	 * Tests addition of a number plus zero.
	 */
	@Test
	public void addZeroImmTest() {
		state.registers[1] = 0x2A;
		new AddHandler().execute(0x1660, this.state, this.bank);
		assertEquals("Should be 0x2A", 0x2A, state.registers[3]);
		assertEquals("CCR zero shouldn't be set", state.ccrZero, false);
	}
	
	/**
	 * Tests addition of two negative numbers.
	 */
	@Test
	public void addNegImmTest() {
		state.registers[1] = -365;
		new AddHandler().execute(0x167B, this.state, this.bank);
		assertEquals("Should equal -370", -370, state.registers[3]);
		assertEquals("CCR Negative should be set", state.ccrNegative, true);
	}
	
}