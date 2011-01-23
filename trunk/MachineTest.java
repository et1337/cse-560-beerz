import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import state.MemoryBank;
import state.MachineState;


public class MachineTest {
	/**
	 * Initial state of memory.
	 */
	private MemoryBank bank;
	/**
	 * Initial state of Machine
	 */
	private Machine machine;
	
	@Before
	public void setUp() {
		bank = new MemoryBank();

	}
	
	@Test
	public void machineAddTest() {
		this.bank.write(0x3000, (short) 0x221A);
		this.bank.write(0x3001, (short) 0x201D);
		this.bank.write(0x3002, (short) 0x5680);
		this.bank.write(0x3003, (short) 0x0C05);
		this.bank.write(0x3004, (short) 0x9020);
		this.bank.write(0x3005, (short) 0x8000);
		this.bank.write(0x3006, (short) 0x301E);
		this.bank.write(0x3007, (short) 0xB21B);
		this.bank.write(0x3008, (short) 0x201F);
		this.bank.write(0x3009, (short) 0xF021);
		this.bank.write(0x300A, (short) 0xF025);
		
		this.bank.write(0x301A, (short) 0xFFFF);
		this.bank.write(0x301B, (short) 0x300B);
		this.bank.write(0x301C, (short) 0x7FFF);
		this.bank.write(0x301D, (short) 0x40E2);
		this.bank.write(0x301E, (short) 0x00CF);
		this.bank.write(0x301F, (short) 0x0043);
		machine = new Machine(bank);
		
		try {
			machine.run(0x3000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
