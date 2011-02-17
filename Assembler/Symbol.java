package Assembler;

/**
 *  A Symbol is basically a name/value pair with an extra flag that
 * defines whether the Symbol is relocatable or consant.
 */
public class Symbol {
	
	/**
	 * The name of the Symbol.
	 */
	private String name;
	
	/**
	 *  The value of the Symbol.
	 */
	private int value;
	
	/**
	 *  Whether the Symbol is relocatable or not.
	 */
	private boolean isRelocatable;
	
	/**
	 * Instantiates a new Symbol with the given name and value.
	 * Also initializes the relocatable flag.
	 * */
	public Symbol(String name, int value, boolean isRelocatable) {
		this.name = name;
		this.value = value;
		this.isRelocatable = isRelocatable;
	}
	
	/**
	 * Gets the name of this Symbol.
	 * @return the Symbol name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the value of this Symbol.
	 * @return the Integer value of the Symbol
	 */
	public int getValue() {
		return this.value;
	}
	
	/**
	 *  Returns true if this Symbol is relocatable, false if not.
	 *  @return true if and only if the Symbol is relocatable.
	 */
	public boolean isRelocatable() {
		return this.isRelocatable;
	}
}