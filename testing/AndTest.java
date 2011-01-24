package testing;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import state.MemoryBank;
import state.MachineState;
import instructions.AndHandler;

/**
 * 
 * @author Elliot
 *
 *	Tests the functionality of the And function and verifies CCRs are being set correctly.
 */
public class AndTest {

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
	 * First set of tests test the and operation with 2 registers and check the CCRs.
	 */
	
	/**
	 * Tests the and operation with two negative numbers.
	 */
	@Test
	public void andNegTest() {
		state.registers[1] = -100;
		state.registers[3] = -2867;
		new AndHandler().execute (0x52C1, this.state, this.bank);
		assertEquals("Should equal -2932", -2932, state.registers[1]);
		assertEquals("CCR should be negative", state.ccrNegative, true);
	}
	
	/**
	 * Tests the and operation with two positive numbers.
	 */
	@Test
	public void andPosTest() {
		state.registers[1] = 0x56;
		state.registers[3] = 0x678;
		new AndHandler().execute(0x52C1, this.state, this.bank);
		assertEquals("Should equal 0x50", this.state.registers[1], 0x50);
		assertEquals("CCR Positive bit should be set", this.state.ccrPositive, true);
	}
	
	/**
	 * Tests the adnd operation with zero and another number.
	 */
	@Test
	public void andZeroTest() {
		//With a positive number
		state.registers[1] = 0x0;
		state.registers[3] = 0x894;
		new AndHandler().execute(0x52C1, this.state, this.bank);
		assertEquals("Should equal 0x0", 0x0, this.state.registers[1]);
		assertEquals("Zero bit should be set", this.state.ccrZero, true);
		
		//With a negative number
		state.registers[3] = -0x894;
		new AndHandler().execute(0x52C1, this.state, this.bank);
		assertEquals("Should Equal 0", 0x0, this.state.registers[1]);
		assertEquals("Zero bit should be set", this.state.ccrZero, true);
	}
	
	/**
	 * Tests and operation with a number and negative -1.
	 */
	@Test
	public void andNegOneTest() {

		state.registers[1] = -1;
		state.registers[3] = 45;
		new AndHandler().execute(0x52C1, this.state, this.bank);
		assertEquals("Should equal 45", this.state.registers[1], 45);
		assertEquals("CCR bit should be positive", this.state.ccrPositive, true);
	}
	
	/**
	 * Tests the and operation with a negative and positive number.
	 */
	public void andMixedTest() {
		state.registers[1] = -0x56;
		state.registers[3] = 0x830;
		new AndHandler().execute(0x52C1, this.state, this.bank);
		assertEquals("Should equal 0x820", 0x820, this.state.registers[1]);
		assertEquals("CCR bit should be positive", this.state.ccrPositive, true);
	}
	
	/**
	 * Second set of tests test the and operations with a register and an immediate constant.
	 * 
	 */
	
	/**
	 * Tests the and operation with a positive immediate constant and a number.
	 */
	@Test
	public void andPosImmTest() {
		//With a positive number
		state.registers[1] = 0x6EF;
		new AndHandler().execute(0x586D, this.state, this.bank);
		assertEquals("Should equal D", 0xD, this.state.registers[4]);
		assertEquals("Positive ccr should be set", this.state.ccrPositive, true);
		
		//With a negative number
		state.registers[1] = -0x6EF;
		new  AndHandler().execute(0x586D, this.state, this.bank);
		assertEquals("Should equal 1", 0x1, this.state.registers[4]);
		assertEquals("Positive ccr should be set", this.state.ccrPositive, true);
		
	}
	 /**
	  * Tests the and operation with a negative immediate constant and a number.
	  */
	@Test
	public void andNegImmTest() {
		//With a negative number
		state.registers[1] = -0x6EF;
		new AndHandler().execute(0x587D, this.state, this.bank);
		assertEquals("Should equal -0x6EF", -0x6EF, this.state.registers[4]);
		assertEquals("CCR bit should be negative", true, this.state.ccrNegative);
		
		//With a positive number
		state.registers[1] = 0x5AE;
		new AndHandler().execute(0x587D, this.state, this.bank);
		assertEquals("Should equal 5AC", 0x5AC, this.state.registers[4]);
		assertEquals("CCR bit should be positive", true, this.state.ccrPositive);
		
	}
	
	/**
	 * Tests the and operation with a zero immediate constant and a number.
	 */
	
	@Test
	public void andZeroImmTest(){
		state.registers[1] = 0x3ACF;
		new AndHandler().execute(0x5860, this.state, this.bank);
		assertEquals("Should equal 0x0", 0x0, this.state.registers[4]);
		assertEquals("CCR bit should be zero", true, this.state.ccrZero);
	}
}
