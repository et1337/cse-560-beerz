package Simulator.testing;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import Simulator.state.MemoryBank;
import Simulator.state.MachineState;
import Simulator.instructions.LoadHandler;

/**
 * 
 * @author Elliot
 * 
 * Tests the Load instruction and verifies that the proper CCRs are being set.
 *
 */
public class LoadTest extends TestBase {


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
	 * Tests loading data from an address at a higher address than the current program counter.
	 */
	@Test
	public void loadLaterAddressTest() {
		bank.write(0x307A, (short) 0x009C);
		new LoadHandler().execute(0x207A, this.state, this.bank);
		assertEquals("Register 0 should hold 0x009C", 0x009C, this.state.registers[0]);
		assertEquals("CCR should be set to positive", true, this.state.ccrPositive);
		assertEquals("CCR should not be set to zero", false, this.state.ccrZero);
		assertEquals("CCR should not be set to negative", false, this.state.ccrZero);
		
	}
	
	/**
	 * Tests loading data from an address at a lower address than the current program counter.
	 */
	@Test
	public void loadEarlierAddressTest() {
		this.state.programCounter = 0x3080;
		bank.write(0x3020, (short) 0x00AA);
		new LoadHandler().execute(0x2220, this.state, this.bank);
		assertEquals("Register 1 should hold 0x00AA", 0x00AA, this.state.registers[1]);
		assertEquals("CCR should be set to positive", true, this.state.ccrPositive);
	}
	
	/**
	 * Tests loading negative data from an address.
	 */
	@Test
	public void loadNegativeDataTest(){
		bank.write(0x30AA, (short) 0xFFFF);
		new LoadHandler().execute(0x22AA, this.state, this.bank);
		assertEquals("Register 1 should hold -1", -1, this.state.registers[1]);
		assertEquals("CCR should be set to negative", true, this.state.ccrNegative);
	}
	
	/**
	 * Tests loading data (all zeros) from an address.
	 */
	@Test
	public void loadZeroDataTest() {
		this.state.programCounter = 0x3012;
		bank.write(0x31FF, (short) 0x0000);
		new LoadHandler().execute(0x23FF, this.state, this.bank);
		assertEquals("Register 1 should be set to 0x0", 0x0, this.state.registers[1]);
		assertEquals("CCR should be set to zero", true, this.state.ccrZero);
	}
}
