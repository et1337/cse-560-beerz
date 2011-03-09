package Common;

/**
 * A Symbol is basically a name/value pair with an extra flag that
 * defines whether the Symbol is relocatable or consant. There are also flags
 * indicating whether the Symbol is imported, exported, or neither.
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
	 * True iff this symbol must be defined in another linked object file.
	 */
	private boolean _isImport;
	
	/**
	 * True iff this symbol will be exported to other linked object files.
	 */
	private boolean _isExport;
	
	/**
	 * Instantiates a new Symbol with the given name and value.
	 * Also initializes the relocatable flag.
	 * */
	public Symbol(String name, int value, boolean isRelocatable) {
		this.name = name;
		this.value = value;
		this.isRelocatable = isRelocatable;
		this._isImport = false;
		this._isExport = false;
	}
	
	/**
	 * Instantiates a new import Symbol with the given name.
	 * Also initializes the relocatable flag.
	 * */
	public Symbol(String name) {
		this.name = name;
		this.value = 0;
		this.isRelocatable = false;
		this._isImport = true;
		this._isExport = false;
	}
	
	/**
	 * Gets the name of this Symbol.
	 * @return the Symbol name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Sets the value of this Symbol.
	 * @param the new value for this Symbol.
	 */
	public void setValue(int value) {
		this.value = value;
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
	
	/**
	 * Sets the export flag for this Symbol.
	 */
	public void setExport() {
		this._isExport = true;
	}
	
	/**
	 * True iff the import flag is set for this Symbol, indicating
	 * that this symbol must be defined in another linked object file.
	 * @return true iff the import flag is set for this Symbol.
	 */
	public boolean isImport() {
		return this._isImport;
	}
	
	/**
	 * True iff the export flag is set for this Symbol, indicating
	 * that this symbol will be exported to other linked object files.
	 * @return true iff the export flag is set for this Symbol.
	 */
	public boolean isExport() {
		return this._isExport;
	}
}