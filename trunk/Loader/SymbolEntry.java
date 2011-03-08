package Loader;

/**
 *  A SymbolEntry consists of a symbol, an address, and a bit mask.
 */
public class SymbolEntry {
	
	/**
	 * The name of the Symbol to link this entry with.
	 */
	private String symbol;
	
	/**
	 *  The address to insert the symbol into.
	 */
	private int address;
	
	/**
	 *  The least significant bit to insert the Symbol into.
	 */
	private int leastSignificantBit;
	
	/**
	 *  The most significant bit to insert the Symbol into.
	 */
	private int mostSignificantBit;
	
	/**
	 * Instantiates a new SymbolEntry with the given Symbol, address, and bit mask.
	 * */
	public SymbolEntry(String symbol, int address, int mostSignificantBit, int leastSignificantBit) {
		this.symbol = symbol;
		this.address = address;
		this.mostSignificantBit = mostSignificantBit;
		this.leastSignificantBit = leastSignificantBit;
	}
	
	/**
	 * Gets the name of the Symbol associated with this SymbolEntry.
	 * @return the Symbol associated with this SymbolEntry.
	 */
	public String getSymbol() {
		return this.symbol;
	}
	
	/**
	 * Gets the address of this SymbolEntry.
	 * @return the address of the SymbolEntry
	 */
	public int getAddress() {
		return this.address;
	}
	
	/**
	 *  Gets the least significant bit to insert the Symbol into.
	 *  @return the least significant bit to insert the Symbol into.
	 */
	public int getLeastSignificantBit() {
		return this.leastSignificantBit;
	}
	
	/**
	 *  Gets the most significant bit to insert the Symbol into.
	 *  @return the most significant bit to insert the Symbol into.
	 */
	public int getMostSignificantBit() {
		return this.mostSignificantBit;
	}
}