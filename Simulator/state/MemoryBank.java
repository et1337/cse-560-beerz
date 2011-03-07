package Simulator.state;
import java.io.PrintStream;
import java.util.Map;
import java.util.HashMap;
import Simulator.util.ByteOperations;

/**
 * A MemoryBank represents the state of memory in a Machine.
 */
public class MemoryBank {

	/**
	 * Number of bits to shift the page index left.
	 */
	private static final int PAGE_SHIFT = 9;

	/**
	 * The concrete representation of the memory. This dictionary maps 16-bit
	 * unsigned addresses to 16-bit signed values.
	 */
	private Map<Integer, Short> data;
	
	/**
	 * Creates a new MemoryBank with zeroes for all memory values.
	 */
	public MemoryBank() {
		this.data = new HashMap<Integer, Short>();
	}
	
	/**
	 * Sets the memory cell at the given address to the given value.
	 * @param address The 16-bit unsigned address of the cell to modify.
	 * @param value The 16-bit signed value to store in the memory cell.
	 */
	public void write(int address, short value) {
		this.data.put(address, value);
	}
	
	/**
	 * Gets the value of the memory cell at the given address.
	 * @param address The 16-bit unsigned address of the cell to read.
	 * @return The 16-bit signed value stored at the specified memory cell.
	 */
	public short read(int address) {
		if (this.data.containsKey(address))
			return this.data.get(address);
		return 0;
	}
	
	/**
	 * Prints the state of the given memory page (bit-shifted all the way
	 * to the right) to the given output stream.
	 * @param output The IO stream to output to.
	 * @param page The memory page to print (bit-shifted all the way to the
	 * right).
	 */
	public void displayPage(PrintStream output, int page) {
		int pageStart = page << MemoryBank.PAGE_SHIFT;
		int pageEnd = ((page + 1) << MemoryBank.PAGE_SHIFT) + 8;
		StringBuffer line = new StringBuffer();
		for(int i = pageStart; i < pageEnd; i++) {
			if ((i - pageStart) % 8 == 0) {
				if (i - pageStart > 0) {
					output.println(line);
					line = new StringBuffer();
				}
				line.append("0x" + ByteOperations.getHex(i, 4) + ": ");
			}
			line.append(ByteOperations.getHex(this.read(i), 4));
			line.append(" ");
		}
	}
}