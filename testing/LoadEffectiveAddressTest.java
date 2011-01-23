package testing;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import state.MemoryBank;
import state.MachineState;
import instructions.LoadEffectiveAddressHandler;

public class LoadEffectiveAddressTest {
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

	@Test
	public void loadZeroTest(){
		this.state.programCounter = 0x35DF;
		new LoadEffectiveAddressHandler().execute(0xE200, this.state, this.bank);
		assertEquals("The value in register 1 should equal 0x3400", 0x3400, this.state.registers[1]);
		assertEquals("The positive CCR should be set", true, this.state.ccrPositive);
		
		this.state.programCounter = 0x1000;
		new LoadEffectiveAddressHandler().execute(0xE200, this.state, this.bank);
		assertEquals("The value in register 1 should be 0x1000", 0x1000, this.state.registers[1]);
		assertEquals("The CCR should be set to positive", true, this.state.ccrPositive);
	}
	
	@Test
	public void loadNegativeTest(){
		this.state.programCounter = 0xFE26;
		new LoadEffectiveAddressHandler().execute(0xE3FF, this.state, this.bank);
		assertEquals("The value in register 1 should equal -1", -1, this.state.registers[1]);
		assertEquals("The Negative ccr should be set", true, this.state.ccrNegative);
	}
	
	@Test
	public void loadEffectiveTest(){
		this.state.programCounter = 0x1220;
		new LoadEffectiveAddressHandler().execute(0xE03E, this.state, this.bank);
		assertEquals("The value in register 0 should be 123E", 0x123E, this.state.registers[0]);
		assertEquals("The Positive CCR should be set", true, this.state.ccrPositive);
		
		
		this.state.programCounter = 0x2A;
		new LoadEffectiveAddressHandler().execute(0xE000, this.state, this.bank);
		assertEquals("The value in register 0 should be 0x0", 0x0, this.state.registers[0]);
		assertEquals("The Zero CCR should be set", true, this.state.ccrZero);
	}
}
