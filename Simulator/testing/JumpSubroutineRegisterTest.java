package Simulator.testing;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import Simulator.state.MemoryBank;
import Simulator.state.MachineState;
import Simulator.instructions.JumpSubroutineRegisterHandler;

public class JumpSubroutineRegisterTest extends TestBase {

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
		bank.write(0x120d, (short) 0x45D3);
		bank.write(0x2035, (short) 0x1920);
		bank.write(0x5025, (short) 0x1002);
		bank.write(0x700F, (short) 0x4343);
	}
	
	@Test
	public void jumpRegLTest(){
		this.state.registers[1] = 0x2000;
		new JumpSubroutineRegisterHandler().execute(0xC875, this.state, this.bank);
		assertEquals("Program Counter should equal 0x1920", 0x1920, this.state.programCounter);
		assertEquals("Register 7 should equal 0x3000", 0x3000, this.state.registers[7]);
		
		this.state.registers[6] = 0x1200;
		new JumpSubroutineRegisterHandler().execute(0xC98D, this.state, this.bank);
		assertEquals("Program Counter should equal 0x120D", 0x45D3, this.state.programCounter);
		assertEquals("Register 7 should equal 0x1920", 0x1920, this.state.registers[7]);		
	}
	
	@Test
	public void jumpRegNoLTest() {
		this.state.registers[1] = 0x500D;
		new JumpSubroutineRegisterHandler().execute(0xC058, this.state, this.bank);
		assertEquals("Program Counter should equal 0x1002", 0x1002, this.state.programCounter);
		assertEquals("Register 7 should be empty", 0x0, this.state.registers[7]);
		
		this.state.registers[0] = 0x7000;
		new JumpSubroutineRegisterHandler().execute(0xC00F, this.state, this.bank);
		assertEquals("Program Counter should equal 0x4343", 0x4343, this.state.programCounter);
		assertEquals("Register 7 should be empty", 0x0, this.state.registers[7]);
	}
}
