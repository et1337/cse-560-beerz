package Loader;
import Common.SymbolTable;
import Common.MemoryBank;
import Common.SymbolEntry;
import java.util.List;

/**
 *  An ObjectFile consists of a MemoryBank, a SymbolTable, a segment name, a list of SymbolEntries, a start address, and a list of relocation records.
 */
public class ObjectFile {
	
	/**
	 * List of SymbolEntries representing imported Symbols.
	 */
	private List<SymbolEntry> symbolEntries;
	
	/**
	 * List of SymbolEntries representing relocation records.
	 */
	private List<SymbolEntry> relocationRecords;
	
	/**
	 * MemoryBank representing the text records of this ObjectFile.
	 */
	private MemoryBank memory;
	
	/**
	 * SymbolTable representing the Symbols defined in this ObjectFile.
	 */
	private SymbolTable symbols;
	
	/**
	 * True iff this ObjectFile is relocatable.
	 */
	private boolean relocatable;
	
	/**
	 * Execution start address.
	 */
	private int startAddress;
	
	/**
	 * Segment name.
	 */
	private String name;
	
	/**
	 * Instantiates a new ObjectFile with the given data.
	 * @param symbolEntries List of SymbolEntries representing imported Symbols.
	 * @param relocationRecords List of SymbolEntries representing relocation records.
	 * @param memory MemoryBank representing the text records of this ObjectFile.
	 * @param symbols SymbolTable representing the Symbols defined in this ObjectFile.
	 * @param relocatable True iff this ObjectFile is relocatable.
	 * @param startAddress Execution start address.
	 * @param Segment name.
	 */
	public ObjectFile(
			List<SymbolEntry> symbolEntries,
			List<SymbolEntry> relocationRecords,
			SymbolTable symbols,
			MemoryBank memory,
			boolean relocatable,
			int startAddress,
			String name) {
		this.symbolEntries = symbolEntries;
		this.relocationRecords = relocationRecords;
		this.symbols = symbols;
		this.memory = memory;
		this.relocatable = relocatable;
		this.startAddress = startAddress;
		this.name = name;
	}
	
	/**
	 * Gets the List of SymbolEntries representing imported Symbols.
	 * @return the List of SymbolEntries representing imported Symbols.
	 */
	public List<SymbolEntry> getSymbolEntries() {
		return this.symbolEntries;
	}
	
	/**
	 * Gets the List of SymbolEntries representing relocation records.
	 * @return the List of SymbolEntries representing relocation records.
	 */
	public List<SymbolEntry> getRelocationRecords() {
		return this.relocationRecords;
	}
	
	/**
	 *  Gets the MemoryBank representing the text records of this ObjectFile.
	 *  @return the MemoryBank representing the text records of this ObjectFile.
	 */
	public MemoryBank getMemoryBank() {
		return this.memory;
	}
	
	/**
	 *  Gets the SymbolTable representing the Symbols defined in this ObjectFile.
	 *  @return the SymbolTable representing the Symbols defined in this ObjectFile.
	 */
	public SymbolTable getSymbols() {
		return this.symbols;
	}
	
	/**
	 * Returns true iff this ObjectFile is relocatable.
	 * @return true iff this ObjectFile is relocatable.
	 */
	public boolean isRelocatable() {
		return this.relocatable;
	}
	
	/**
	 * Returns the address to start execution at in this ObjectFile.
	 * @return the address to start execution at in this ObjectFile.
	 */
	public int getStartAddress() {
		return this.startAddress;
	}
	
	/**
	 * Returns the segment name specified in this ObjectFile.
	 * @return the segment name specified in this ObjectFile.
	 */
	public String getSegmentName() {
		return this.name;
	}
	
	/**
	 * Relocates this ObjectFile's SymbolTable, MemoryBank, and start address.
	 * @param a the original load start address.
	 * @param b the new load start address.
	 */
	public void relocate(int a, int b) {
		this.symbols.relocate(a, b);
		this.memory.relocate(a, b, this.relocationRecords);
		for (SymbolEntry entry : this.symbolEntries) {
			entry.setAddress(entry.getAddress() + b - a);
		}
		for (SymbolEntry entry : this.relocationRecords) {
			entry.setAddress(entry.getAddress() + b - a);
		}
		this.startAddress += b - a;
	}
}