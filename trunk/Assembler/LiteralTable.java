package Assembler;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

/**
 *  Represents a table of literals accumulated during the first assembler pass.
 */
public class LiteralTable {

	/**
	 * The minimum value a decimal literal is allowed to have.
	 */
	protected static final int MIN_VALUE_DECIMAL = -32768;
	
	/**
	 * The maximum value a decimal literal is allowed to have.
	 */
	protected static final int MAX_VALUE_DECIMAL = 32767;
	
	/**
	 * The maximum value a hex literal is allowed to have.
	 */
	protected static final int MAX_VALUE_HEX = 0xFFFF;
	
	/**
	 *  The address (relative to this.offset) of the next literal to be inserted.
	 */
	private int index = 0;
	
	/**
	 *  Internal representation mapping literal values to relative addresses.
	 */
	private Map<Integer, Integer> map = new HashMap<Integer, Integer>();
	
	/**
	 *  The address (relative to the program origin) of the first literal in this table.
	 */
	private int offset;
	
	/**
	 *  Sets the address (relative to the program origin) of the first literal in this table.
	 * @param offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	/**
	 *  Gets the address (relative to the program origin) of the first literal in this table.
	 * @return the offset
	 */
	public int getOffset() {
		return this.offset;
	}
	
	/**
	 *  Defines a new literal with the given integer value.
	 * @param value the Integer value of the literal
	 * @param isHex true if the value is hexadecimal
	 */
	public void define(int value, boolean isHex) throws Exception {
		if (isHex) {
			if (value < 0 || value > LiteralTable.MAX_VALUE_HEX) {
				throw new Exception("Literal value \"" + Integer.toString(value) + "\" must be between 0 and "
					+ Integer.toString(LiteralTable.MAX_VALUE_HEX) + ".");
			}
		}
		else if (value < LiteralTable.MIN_VALUE_DECIMAL || value > LiteralTable.MAX_VALUE_DECIMAL) {
			throw new Exception("Literal value \"" + Integer.toString(value) + "\" must be between "
				+ Integer.toString(LiteralTable.MIN_VALUE_DECIMAL) + " and "
				+ Integer.toString(LiteralTable.MAX_VALUE_DECIMAL) + ".");
		}
		if (!this.map.containsKey(value)) {
			this.map.put(value, this.index);
			this.index++;
		}
	}
	
	/**
	 *  Gets the address (relative to the program origin) where the given literal value is stored.
	 * @param value the Integer value of the literal
	 * @return the Integer value of the address
	 */
	public int getAddress(int value) {
		return this.map.get(value) + this.offset;
	}
	
	/**
	 *  Gets the set of all address/value pairs in this table.
	 * @return the set of all address/value pairs
	 */
	public Set<Map.Entry<Integer, Integer>> getEntries() {
		return this.map.entrySet();
	}
}