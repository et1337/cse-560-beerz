package testing;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import state.MemoryBank;
import state.MachineState;
import instructions.StoreRegisterHandler;

public class StoreRegisterTest {
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
	public void storeRegisterTest() {
		this.state.registers[1] = 0x0ABC;
		this.state.registers[3] = 0x5000;
		new StoreRegisterHandler().execute(0x72D9, this.state, this.bank);
		assertEquals("Location 0x5019 should hold 0xABC", 0xABC, this.bank.read(0x5019));
		
		this.state.programCounter = 0x3000;
		this.state.registers[1] = 0x00FB;
		this.state.registers[2] = 0x1006;
		new StoreRegisterHandler().execute(0x7209, this.state, this.bank);
		assertEquals("Location 0x1009 should hold 0xFB", 0xFB, this.bank.read(0x1009));
		
		
		
		
	}
	
	
}
