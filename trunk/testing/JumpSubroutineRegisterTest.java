package testing;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import state.MemoryBank;
import state.MachineState;
import instructions.JumpSubroutineRegisterHandler;

public class JumpSubroutineRegisterTest {

	/**
	 * Contains the initial state of memory for testing.
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
	public void jumpRegLTest(){
		this.state.registers[1] = 0x2000;
		new JumpSubroutineRegisterHandler().execute(0xC846, this.state, this.bank);
		assertEquals("Program Counter should equal 0x2036", 0x2036, this.state.programCounter);
		assertEquals("Register 7 should equal 0x3000", 0x3000, this.state.registers[7]);
		
		this.state.registers[6] = 0x1200;
		new JumpSubroutineRegisterHandler().execute(0xC98D, this.state, this.bank);
		assertEquals("Program Counter should equal 0x120D", 0x120D, this.state.programCounter);
		assertEquals("Register 7 should equal 0x2030", 0x2030, this.state.registers[7]);		
	}
	
	@Test
	public void jumpRegNoLTest() {
		this.state.registers[1] = 0x500D;
		new JumpSubroutineRegisterHandler().execute(0xC07F, this.state, this.bank);
		assertEquals("Program Counter should equal 0x507F", 0x507F, this.state.programCounter);
		assertEquals("Register 7 should be empty", 0x0, this.state.registers[7]);
		
		this.state.registers[1] = 0x7000;
		new JumpSubroutineRegisterHandler().execute(0xC00F, this.state, this.bank);
		assertEquals("Program Counter should equal 0x700F", 0x700F, this.state.programCounter);
		assertEquals("Register 7 should be empty", 0x0, this.state.registers[7]);
	}
}
