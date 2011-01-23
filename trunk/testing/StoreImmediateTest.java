package testing;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import state.MemoryBank;
import state.MachineState;
import instructions.StoreImmediateHandler;

public class StoreImmediateTest {
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
	public void StoreImmTest() {
		this.state.registers[6] = (short) 0xAAAA;
		bank.write(0x302F, (short) 0x30FF);
		new StoreImmediateHandler().execute(0xBC2F, this.state, this.bank);
		assertEquals("Memory location 0x30FF should hold -0x5556", -0x5556, this.bank.read(0x30FF));
		
		this.state.registers[6] = (short) 0x2ADF;
		bank.write(0x30F1, (short) 0x2012);
		new StoreImmediateHandler().execute(0xBCF1, this.state, this.bank);
		assertEquals("Memory Location 0x2012 should hold 0x2ADF", 0x2ADF, this.bank.read(0x2012));
		
		this.state.registers[6] = (short) 0xB;
		bank.write(0x3022, (short) 0x56A5);
		new StoreImmediateHandler().execute(0xBC22, this.state, this.bank);
		assertEquals("Memory location 0x56A5 should hold 0xB", 0xB, this.bank.read(0x56A5));

		this.state.programCounter = 0x3050;
		this.state.registers[6] = (short) 0x0045;
		bank.write(0x3004, (short) 0x10AA);
		new StoreImmediateHandler().execute(0xBC04, this.state, this.bank);
		assertEquals("Memory location Ox10AA should hold 0x45", 0x45, this.bank.read(0x10AA));
	}
}
