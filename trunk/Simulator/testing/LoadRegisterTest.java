package Simulator.testing;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import Common.MemoryBank;
import Simulator.state.MachineState;
import Simulator.instructions.LoadRegisterHandler;
 /**
  * 
  * @author Elliot
  * Tests the Load Register instruction and verifies that the proper CCRs are being set.
  */
public class LoadRegisterTest extends TestBase {
	/**
	 * Contains the initial state of the memory used for testing.  Also, the CCRs are checked
	 * to verify they are being set correctly.
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
	 * Tests the Load Register function by loading an address after the program counter.
	 */
	@Test
	public void loadLaterAddressTest() {
		this.state.registers[3] = 0x4000;
		this.bank.write(0x400F, (short) 0x0ABC);
		new LoadRegisterHandler().execute (0x62CF, this.state, this.bank);
		assertEquals("Register 1 should hold 0xABC", 0x0ABC, this.state.registers[1]);
		assertEquals("CCR positive bit should be set", true, this.state.ccrPositive);
	}
	/**
	 * Tests the Load Register function by loading an address before the program counter.
	 */
	@Test
	public void loadEarlierAddressTest() {
		this.state.registers[3] = 0x2000;
		this.bank.write(0x202F, (short) 0xFFFE);
		new LoadRegisterHandler().execute(0x62EF, this.state, this.bank);
		assertEquals("Register 1 should hold -2", -2, this.state.registers[1]);
		assertEquals("Negative CCR should be set", true, this.state.ccrNegative);
	}
	
	/**
	 * Tests the Load Register function by loading an address within the current range of the
	 * program counter.
	 */
	@Test
	public void loadWithinRangeAddressTest() {
		this.state.registers[3] = 0x3080;
		this.bank.write(0x30B1, (short) 0x0);
		new LoadRegisterHandler().execute(0x62F1, this.state, this.bank);
		assertEquals("Register 1 should hold 0", 0, this.state.registers[1]);
		assertEquals("Zero ccr should be set", true, this.state.ccrZero);
	}
}
