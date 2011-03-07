package Simulator.testing;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import Simulator.state.MemoryBank;
import Simulator.program.Loader;

/**
 * Tests the Loader component of the machine.
 * @author Elliot
 *
 */

public class LoaderTest extends TestBase {
	/**
	 * Initial state of memory
	 */
	private MemoryBank bank;
	
	@Before
	public void setUp() {
		bank = new MemoryBank();
	}
	/**
	 * Tests the Loader function with a properly formed string.
	 */
	@Test
	public void loaderTest() {
		String instructions = new String();
		int startaddress = 0;
		instructions = "HBEEERZ30000003\n" +
				"T3000E300\n" +
				"T300156E0\n" +
				"T300254A0\n" +
				"E3000";
		try {
			new Loader();
			startaddress = Loader.load(instructions, bank);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertEquals("Location 0x3000 should hold -0x1D00", -0x1D00, this.bank.read(0x3000));
		assertEquals("Location 0x3001 should hold 0x56E0", 0x56E0, this.bank.read(0x3001));
		assertEquals("Location 0x3002 should hold 0x54A0", 0x54A0, this.bank.read(0x3002));
		assertEquals("Loader should return starting address of 0x3000", 0x3000, startaddress);
	}
	
	/**
	 * Tests the loader function with a improperly formed header.
	 */
	@Test
	public void loaderHeaderErrorTest(){
		String instructions = new String();
		@SuppressWarnings("unused")
		int startaddress = 0;
		boolean thrownError = false;
		instructions = "BEEERZ30000003\n" +
		"T3000E300\n" +
		"T300156E0\n" +
		"T300254A0\n" +
		"E3000";
		
		try {
			new Loader();
			startaddress = Loader.load(instructions, bank);
		} catch (Exception e) {
			thrownError = true;
		}
		
		assertEquals("Error should be thrown for improperly formed header", true, thrownError);
	}
	/**
	 * Tests the loader function without a footer.
	 */
	@Test
	public void loaderFooterErrorTest() {
		String instructions = new String();
		@SuppressWarnings("unused")
		int startaddress = 0;
		boolean thrownError = false;
		instructions = "HBEEERZ30000003\n" +
		"T3000E300\n" +
		"T300156E0\n" +
		"T300254A0\n";
		
		try{
			new Loader();
			startaddress = Loader.load(instructions, bank);
		} catch (Exception e) {
			thrownError = true;
		}
		
		assertEquals("Error should be thrown for lack of footer", true, thrownError);
	}
	/**
	 * Tests the loader function with an improperly formed entry.
	 */
	@Test
	public void loaderEntryErrorTest() {
		String instructions = new String();
		@SuppressWarnings("unused")
		int startaddress = 0;
		boolean thrownError = false;
		instructions = "HBEEERZ30000003\n" +
		"T3000E300\n" +
		"300156E0\n" +
		"T300254A0\n" +
		"E3000";
		 
		try{
			new Loader();
			startaddress = Loader.load(instructions, bank);
		} catch (Exception e) {
			thrownError = true;
		}
		
		assertEquals("Error should be thrown for improper instruction", true, thrownError);
	}
}
