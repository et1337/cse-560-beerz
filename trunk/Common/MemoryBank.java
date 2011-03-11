package Common;
import java.io.PrintStream;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;

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
	 * The first address with data in this MemoryBank.
	 */
	private int firstAddress = Integer.MAX_VALUE;
	
	/**
	 * The last address with data in this MemoryBank.
	 */
	private int lastAddress;
	
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
		this.adjustBounds(address);
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
	 * Expands the first or last address of this MemoryBank to include the given value.
	 * @param address an address that must be included in the range [this.firstAddress, this.lastAddress]
	 */
	public void adjustBounds(int address) {
		if (address < this.firstAddress) {
			this.firstAddress = address;
		}
		if (address > this.lastAddress) {
			this.lastAddress = address;
		}
	}
	
	/**
	 * Gets the first address with data in this MemoryBank.
	 * @return the first address with data in this MemoryBank.
	 */
	public int getFirstAddress() {
		return this.firstAddress;
	}
	
	/**
	 * Gets the last address with data in this MemoryBank.
	 * @return the last address with data in this MemoryBank.
	 */
	public int getLastAddress() {
		return this.lastAddress;
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
	
	/**
	 * Relocate the data in this MemoryBank from the given starting location
	 * to the given new location, using the given relocation records.
	 * @param a the original start address.
	 * @param b the new start address.
	 * @param relocationRecords a List of relocation records used to modify the text records.
	 */
	public void relocate(int a, int b, List<SymbolEntry> relocationRecords) {
		for (SymbolEntry entry : relocationRecords) {
			short value = this.read(entry.getAddress());
			short address = (short)((value & entry.getMask()) >> entry.getLeastSignificantBit());
			address += b - a;
			value &= entry.getInverseMask();
			value |= (address << entry.getLeastSignificantBit()) & entry.getMask();
			this.write(entry.getAddress(), value);
		}
		MemoryBank bank = new MemoryBank();
		for (Map.Entry<Integer, Short> entry : this.data.entrySet()) {
			bank.write(entry.getKey() + b - a, entry.getValue());
		}
		this.firstAddress += b - a;
		this.lastAddress += b - a;
		this.data = bank.data;
	}
	
	/**
	 * Insert values from the given SymbolTables into the memory locations specified by the given
	 * List of SymbolEntries.
	 * @param symbols List of SymbolTables to pull values from.
	 * @param symbolEntries List of SymbolEntries specifying where in memory the Symbols are to be inserted.
	 */
	public void resolveSymbols(List<SymbolTable> symbols, List<SymbolEntry> symbolEntries) throws Exception {
		for (SymbolEntry entry : symbolEntries) {
			short value = this.read(entry.getAddress());
			boolean foundNewValue = false;
			short newValue = 0;
			for (SymbolTable table : symbols) {
				if (table.hasSymbol(entry.getSymbol())) {
					newValue = (short)table.get(entry.getSymbol()).getValue();
					foundNewValue = true;
				}
			}
			if (!foundNewValue)
				throw new Exception("Undefined symbol \"" + entry.getSymbol() + "\".");
			value &= entry.getInverseMask();
			value |= (newValue << entry.getLeastSignificantBit()) & entry.getMask();
			this.write(entry.getAddress(), value);
		}
	}
	
	/**
	 * Insert the data from this MemoryBank into the given MemoryBank, overwriting any overlapping data.
	 * @param bank the MemoryBank to insert data into.
	 */
	public void insertInto(MemoryBank bank) {
		for (Map.Entry<Integer, Short> entry : this.data.entrySet()) {
			bank.write(entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * Gets a String of text records representing this MemoryBank.
	 * @return a String of text records representing this MemoryBank.
	 */
	public String getRecords() {
		StringBuffer result = new StringBuffer();
		List<Map.Entry<Integer, Short>> list = new LinkedList<Map.Entry<Integer, Short>>(this.data.entrySet());
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getKey())
				.compareTo(((Map.Entry) (o2)).getKey());
			}
		});
		for (Map.Entry<Integer, Short> entry : list) {
			result.append("T");
			result.append(ByteOperations.getHex(entry.getKey(), 4));
			result.append(ByteOperations.getHex(entry.getValue(), 4));
			result.append("\r\n");
		}
		return result.toString();
	}
}