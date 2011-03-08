package Simulator.testing;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import Common.MemoryBank;
import Simulator.state.MachineState;
import Simulator.instructions.TrapHandler;

/**
 * Tests the functionality of the Trap function.
 */
public class TrapTest extends TestBase {

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
	
	/**
	 * Tests the inn function.
	 */
	@Test
	public void innTest() {
		//Type 111 in console
		new TrapHandler().execute("111\r\n", 0xF033, this.state, this.bank);
		assertEquals("Register 0 should hold 111", 111, this.state.registers[0]);
		assertEquals("The CCRs should be set to only positive", true, this.state.ccrPositive);
		
		//Type -32768 in console
		new TrapHandler().execute("-32768\r\n", 0xF033, this.state, this.bank);
		assertEquals("Register 0 should hold -32768", -32768, this.state.registers[0]);
		assertEquals("The CCR should be set to only negative", true, this.state.ccrNegative);
		
		//Type 0 in console
		new TrapHandler().execute("0\r\n", 0xF033, this.state, this.bank);
		assertEquals("Register 0 should hold 0", 0, this.state.registers[0]);
		assertEquals("The CCR should be set to only zero", true, this.state.ccrZero);
	}

	/**
	 * Tests the in trap vector.
	 */
	@Test
	public void inTest() {
		//Type 'E' in the console
		new TrapHandler().execute("E\r\n", 0xF023, this.state, this.bank);
		assertEquals("Register 0 should equal the integer value of 'E'", 0x45, state.registers[0]);
		assertEquals("CCR should be set to positive", true, this.state.ccrPositive);
		
		//Type 'v' in the console
		new TrapHandler().execute("v\r\n", 0xF023, this.state, this.bank);
		assertEquals("Register 0 should equal the integer value of 'v'", 0x76, state.registers[0]);
		
		//Type '#' in the console
		new TrapHandler().execute("#\r\n", 0xF023, this.state, this.bank);
		assertEquals("Register 0 should equal the integer value of '#'", 0x23, state.registers[0]);
		
		//Type '8' in the console
		new TrapHandler().execute("8\r\n", 0xF023, this.state, this.bank);
		assertEquals("Register 0 should equal the integer value of '8'", 0x38, state.registers[0]);
	}
	
	/**
	 * Tests the halt trap vector.
	 */
	@Test
	public void haltTest() {
		new TrapHandler().execute(0xf025, this.state, this.bank);
		assertEquals("Trap - halt", this.state.executing, false);
	}
	
	/**
	 * Tests the out vector.
	 */
	@Test
	public void outTest() {
		//Should output the letter 'A'
		this.state.registers[0] = 0x41;
		assertEquals("Should output the letter 'A'", new TrapHandler().execute(0xF021, this.state, this.bank), "A");
		
		//Should output the letter 'z'
		this.state.registers[0] = 0x7A;
		assertEquals("Should output the letter 'z'", new TrapHandler().execute(0xF021, this.state, this.bank), "z");
		
		//Should output the character '!'
		this.state.registers[0] = 0x21;
		assertEquals("Should output the character '!'", new TrapHandler().execute(0xF021, this.state, this.bank), "!");
	}
	
	/**
	 * Tests the puts vector.
	 */
	@Test
	public void putsTest() {
		//Should print out "Test Message 1?"
		this.state.registers[0] = 0x4000;
		this.bank.write(0x4000, (short) 0x0A); //Newline
		this.bank.write(0x4001, (short) 0x54); //T
		this.bank.write(0x4002, (short) 0x65); //e
		this.bank.write(0x4003, (short) 0x73); //s
		this.bank.write(0x4004, (short) 0x74); //t
		this.bank.write(0x4005, (short) 0x20); //(space)
		this.bank.write(0x4006, (short) 0x4D); //M
		this.bank.write(0x4007, (short) 0x65); //e
		this.bank.write(0x4008, (short) 0x73); //s
		this.bank.write(0x4009, (short) 0x73); //s
		this.bank.write(0x400A, (short) 0x61); //a
		this.bank.write(0x400B, (short) 0x67); //g
		this.bank.write(0x400C, (short) 0x65); //e
		this.bank.write(0x400D, (short) 0x20); //(space)
		this.bank.write(0x400E, (short) 0x31); //1
		this.bank.write(0x400F, (short) 0x3F); //?
		this.bank.write(0x4010, (short) 0x00); //null
		this.bank.write(0x4011, (short) 0x23); //#
		this.bank.write(0x4012, (short) 0x26); //&
		assertEquals("Should print out \"\\nTest Message 1?\"", new TrapHandler().execute(0xF022, this.state, this.bank), "\nTest Message 1?");	
	}
	
	/**
	 * Tests the outn vector.
	 */
	@Test
	public void outnTest() {		
		//Should output -2
		this.state.registers[0] = (short) 0xFFFE;
		assertEquals("Should output -2", new TrapHandler().execute(0xF031, this.state, this.bank), "-2");
		
		//Should output 32767
		this.state.registers[0] = (short) 0x7FFF;
		assertEquals("Should output 32767", new TrapHandler().execute(0xF031, this.state, this.bank), "32767");
		
		//Should output 0
		this.state.registers[0] = (short) 0x0;
		assertEquals("Should output 0", new TrapHandler().execute(0xF031, this.state, this.bank), "0");
	}
	/**
	 * Tests the rnd vector.
	 */
	@Test
	public void rndTest() {
		int cycles = 10;
		while (cycles > 0) {
			new TrapHandler().execute(0xF043, this.state, this.bank);
			int reg0 = this.state.registers[0];
			assertEquals("The random number should be greater than 0x8000", true, -0x8000 <= reg0);
			assertEquals("The random number should be less than 0x7FFF", true, reg0 <= 0x7FFF);
			cycles--;
		}
	}
}