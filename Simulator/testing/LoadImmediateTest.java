package Simulator.testing;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import Common.MemoryBank;
import Simulator.state.MachineState;
import Simulator.instructions.LoadImmediateHandler;

public class LoadImmediateTest extends TestBase {
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
	
	@Test
	public void loadFromLaterAddressTest(){
		bank.write(0x302F, (short) 0x30FF);
		bank.write(0x30FF, (short) 0x00AA);
		new LoadImmediateHandler().execute(0xA22F, this.state, this.bank);
		assertEquals("Register 1 should hold 0x00AA", 0x00AA, this.state.registers[1]);
		assertEquals("CCR should be set to positive", true, this.state.ccrPositive);
	}
	
	@Test
	public void loadFromEarlierAddressTest() {
		this.state.programCounter = 0x3055;
		bank.write(0x3005, (short) 0x3002);
		bank.write(0x3002, (short) 0xFFFF);
		new LoadImmediateHandler().execute(0xA205, this.state, this.bank);
		assertEquals("Register 1 should hold -1", -1, this.state.registers[1]);
		assertEquals("CCR should be set to negative", true, this.state.ccrNegative);
	}
	 /**
	  * Tests the Load Immediate function with an earlier address outside of the current range 
	  * for the load instruction.
	  */
	@Test
	public void loadAddressOutsideEarlierTest() {
		bank.write(0x3010, (short) 0x1000);
		bank.write(0x1000, (short) 0x0);
		new LoadImmediateHandler().execute(0xA210, this.state, this.bank);
		assertEquals("Register 1 should hold 0", 0, this.state.registers[1]);
		assertEquals("CCR should be set to zero", true, this.state.ccrZero);
		
	}
	
	/**
	 * Tests the Load Immediate Function with a later address outside of the current range for
	 * the load instruction.
	 */
	@Test
	public void loadAddressOutsideLaterTest() {
		bank.write(0x30FF, (short) 0x5000);
		bank.write(0x5000, (short) 0x0ADF);
		new LoadImmediateHandler().execute(0xA0FF, this.state, this.bank);
		assertEquals("Register 1 should hold 0x0ADF", 0x0ADF, this.state.registers[0]);
		assertEquals("CCR should be set to positive", true, this.state.ccrPositive);
	}
	
	
}
