package testing;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import state.MemoryBank;
import state.MachineState;
import instructions.BranchHandler;

public class BranchTest {
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
	public void setUp() {
		bank = new MemoryBank();
		state = new MachineState();
		
		this.state.programCounter = 0x3000;
		this.state.registers[1] = 0x4E9;
		this.state.registers[3] = -0xEA;
		this.state.registers[4] = -0x12BD;
		this.state.registers[5] = 0x7FFF;
		
	}
	
	@Test
	public void alwaysBranch() {
		new BranchHandler().execute(0x0EA2, this.state, this.bank);
		assertEquals ("Program counter should be incremented by 0xA2", 0x30A2, this.state.programCounter);
	}
	
	
	@Test
	public void nopBranch() {
		new BranchHandler().execute(0x0 , this.state, this.bank);
		assertEquals("Program counter should not increment", 0x3000, this.state.programCounter);
	}
	
	@Test
	public void positiveBranch() {
		this.state.ccrPositive = true;
		new BranchHandler().execute(0x0203, this.state, this.bank);
		assertEquals ("Program counter should be incremented by 3", 0x3003, this.state.programCounter);
		
		this.state.programCounter = 0x3000;
		new BranchHandler().execute(0x06FF, this.state, this.bank);
		assertEquals("Program counter should be incremented by FF", 0x30FF, this.state.programCounter);
		
		this.state.programCounter = 0x3000;
		new BranchHandler().execute(0x0EA1, this.state, this.bank);
		assertEquals("Program counter should be incremented by A1", 0x30A1, this.state.programCounter);
		
		this.state.programCounter = 0x3000;
		new BranchHandler().execute(0x082B, this.state, this.bank);
		assertEquals("Program counter should not be incremented (no branch)", 0x3000, this.state.programCounter);
	}
	
	@Test
	public void negativeBranch() {
		this.state.ccrNegative = true;
		new BranchHandler().execute(0x0203, this.state, this.bank);
		assertEquals ("Program counter should be not be incremented", 0x3000, this.state.programCounter);
		
		this.state.programCounter = 0x3000;
		new BranchHandler().execute(0x0880, this.state, this.bank);
		assertEquals("Program counter should be incremented by 0x80", 0x3080, this.state.programCounter);
		
		this.state.programCounter = 0x3000;
		new BranchHandler().execute(0x082B, this.state, this.bank);
		assertEquals("Program counter be incremented by 0x2B)", 0x302B, this.state.programCounter);
	
	}
	
	@Test
	public void zeroBranch() {
		this.state.ccrZero = true;
		new BranchHandler().execute(0x04F0, this.state, this.bank);
		assertEquals("The counter should be incremented by 0xF0", 0x30F0, this.state.programCounter);
		
		this.state.programCounter = 0x3000;
		new BranchHandler().execute(0x0203, this.state, this.bank);
		assertEquals("This counter should not be incremented", 0x3000, this.state.programCounter);
	}
}
