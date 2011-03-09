package Loader;
import Common.SymbolTable;
import Common.MemoryBank;
import Common.SymbolEntry;
import java.util.List;

/**
 *  An ObjectFile consists of a MemoryBank, a SymbolTable, a list of SymbolEntries, a start address, and a list of relocation records.
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
	 * Instantiates a new SymbolEntry with the given Symbol, address, and bit mask.
	 * */
	public ObjectFile(List<SymbolEntry> symbolEntries, List<SymbolEntry> relocationRecords, SymbolTable symbols, MemoryBank memory, boolean relocatable, int startAddress) {
		this.symbolEntries = symbolEntries;
		this.relocationRecords = relocationRecords;
		this.symbols = symbols;
		this.memory = memory;
		this.relocatable = relocatable;
		this.startAddress = startAddress;
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
}