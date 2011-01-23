package testing;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import state.MemoryBank;
import state.MachineState;
import instructions.StoreHandler;

public class StoreTest {
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
		this.state.programCounter = 0x2000;
	}
	
	@Test
	public void storeTest() {
		this.state.registers[4] = (short) 0x800B;
		new StoreHandler().execute(0x38F8, this.state, this.bank);
		assertEquals("Location 0x20F8 should hold -0x7FF5", -0x7FF5, this.bank.read(0x20F8));
		
		this.state.registers[4] = (short) 0x0;
		new StoreHandler().execute(0x3822, this.state, this.bank);
		assertEquals("Location 0x2022 should hold 0x0", 0x0, this.bank.read(0x2022));
		
		this.state.registers[4] = (short) 0x7FFF;
		new StoreHandler().execute(0x3800, this.state, this.bank);
		assertEquals("Location 0x2000 should hold 0x7FFF", 0x7FFF, this.bank.read(0x2000));
		
		this.state.registers[4] = (short) 0xEE;
		new StoreHandler().execute(0x39F8, this.state, this.bank);
		assertEquals("Location 0x21F8 should hold 0xEE", 0xEE, this.bank.read(0x21F8));
		
	}
}
