package testing;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import state.MemoryBank;
import state.MachineState;
import instructions.JumpSubroutineImmediateHandler;
/**
 * Tests the functionality of the Jump Subroutine Immediate test.
 * 
 * @author Elliot
 *
 */
public class JumpSubroutineImmediateTest {
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
	/**
	 * Tests the Jump Subroutine Immediate function.
	 */
	@Test
	public void jumpImmLTest() {
		new JumpSubroutineImmediateHandler().execute(0x493D, this.state, this.bank);
		assertEquals("The value of the PC should be 0x313D", 0x313D, this.state.programCounter);
		assertEquals("The value of register 7 should equal 0x3000", 0x3000, this.state.registers[7]);
		
		new JumpSubroutineImmediateHandler().execute(0x4821, this.state, this.bank);
		assertEquals("The value of the PC should be 0x3021", 0x3021, this.state.programCounter);
		assertEquals("The value of register 7 should equal 0x313D", 0x313D, this.state.registers[7]);
	}
	/**
	 * Tests the Jump Immediate function (No L bit set).
	 */
	@Test
	public void jumpImmNoLTest() {
		new JumpSubroutineImmediateHandler().execute(0x405A, this.state, this.bank);
		assertEquals("The value of the program counter should be 0x305A", 0x305A, this.state.programCounter);
		assertEquals("Register 7 should be empty", 0x0, this.state.registers[7]);
		
		new JumpSubroutineImmediateHandler().execute(0x412B, this.state, this.bank);
		assertEquals("The value of the program counter should be 0x312B", 0x312B, this.state.programCounter);
		assertEquals("Register 7 should be empty", 0x0, this.state.registers[7]);
	}
}
