package testing;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import state.MemoryBank;
import state.MachineState;
import instructions.TrapHandler;

/**
 * Tests the functionality of the Trap function.
 */
public class TrapTest {

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
	 * Test the halt trap vector.
	 */
	@Test
	public void haltTest() {
		new TrapHandler().execute(0xf025, this.state, this.bank);
		assertTrue("Trap - halt", this.state.executing == false);
	}
}