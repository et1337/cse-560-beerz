package Common;

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
	 */
	public SymbolEntry(String symbol, int address, int mostSignificantBit, int leastSignificantBit) {
		this.symbol = symbol;
		this.address = address;
		this.mostSignificantBit = mostSignificantBit;
		this.leastSignificantBit = leastSignificantBit;
	}
	
	/**
	 * Instantiates a new SymbolEntry with no symbol (indicating a relocation record).
	 */
	public SymbolEntry(int address, int mostSignificantBit, int leastSignificantBit) {
		this(null, address, mostSignificantBit, leastSignificantBit);
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
	
	/**
	 * Get bitmask for this SymbolEntry.
	 */
	public short getMask() {
		short result = 0;
		for (int i = this.mostSignificantBit; i >= this.leastSignificantBit; i--) {
			result |= 1 << i;
		}
		return result;
	}
	
	/**
	 * Get inverse bitmask for this SymbolEntry.
	 */
	public short getInverseMask() {
		short result = 0;
		for (int i = 16; i > this.mostSignificantBit; i--) {
			result |= 1 << i;
		}
		for (int i = this.leastSignificantBit - 1; i >= 0; i--) {
			result |= 1 << i;
		}
		return result;
	}
}