package Simulator.testing;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import Simulator.state.MemoryBank;
import Simulator.state.MachineState;
import Simulator.instructions.ReturnHandler;

public class ReturnTest extends TestBase {
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
	
	@Test
	public void returnTest() {
		this.state.programCounter = 0x4000;
		this.state.registers[7] = (short) 0xE007;
		new ReturnHandler().execute(0xD000, this.state, this.bank);
		assertEquals("Program counter should equal -0x1FF9", -0x1FF9, this.state.programCounter);
		
		this.state.programCounter = 0x0;
		this.state.registers[7] = (short) 0x27EE;
		new ReturnHandler().execute(0xD000, this.state, this.bank);
		assertEquals("Program counter should equal 0x27EE", 0x27EE, this.state.programCounter);
	}
}
