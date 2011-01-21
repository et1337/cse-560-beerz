package testing;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import state.MemoryBank;
import state.MachineState;
import instructions.NotHandler;
 /**
  * 
  * @author Elliot
  *
  * Tests the functionality of the Not function and that the CCRs are changed when necessary.
  */
public class NotTest {

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
	 * Tests the not function with a positive number.
	 */
	@Test
	public void notPosTest() {
		this.state.registers[4] = 0x16EF;
		new NotHandler().execute(0x9300, this.state, this.bank);
		assertEquals("Should equal -0x15F0", -0x16F0, this.state.registers[1] );
		assertEquals("CCR negative bit should be set", true, this.state.ccrNegative);
	}
	/**
	 * Tests the not function with a negative number.
	 * 
	 */
	@Test
	public void notNegTest() {
		this.state.registers[4] = -0x2AEF;
		new NotHandler().execute(0x9300, this.state, this.bank);
		assertEquals("Should equal 0x2AEE", 0x2AEE, this.state.registers[1]);
		assertEquals("CCR positive bit should be set", true, this.state.ccrPositive);
	}
	
	/**
	 * Tests the not function with zero.
	 */
	@Test
	public void notZeroTest() {
		this.state.registers[4] = 0;
		new NotHandler().execute(0x9D00, this.state, this.bank);
		assertEquals("Should equal -1", -1, this.state.registers[6]);
		assertEquals("CCR negative bit should be set", true, this.state.ccrNegative);
		
	}
	
	/**
	 * Tests the not function with -1.
	 */
	@Test
	public void notNegOneTest() {
		this.state.registers[4] = -1;
		new NotHandler().execute(0x9D00, this.state, this.bank);
		assertEquals("Should equal 0", 0, this.state.registers[6]);
		assertEquals("CCR zero bit should be set", true, this.state.ccrZero);
	}
}
