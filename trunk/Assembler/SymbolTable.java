package Assembler;

import java.util.Map;
import java.util.HashMap;

public class SymbolTable {

	/**
	 *  Internal representation mapping names to Symbols.
	 */
	private Map<String, Symbol> map = new HashMap<String, Symbol>();
	
	/**
	 *  Adds the given Symbol to this SymbolTable.
	 * @param symbol the Symbol to be added to the Symboltable
	 */
	public void define(Symbol symbol) {
		this.map.put(symbol.getName(), symbol);
	}
	
	/**
	 *  Defines an alias for another Symbol.
	 * @param alias 
	 * @param target
	 */
	public void define(String alias, String target)
		throws Exception {
		if (alias.equals(target)) {
			throw new Exception("Cannot alias a symbol to itself.");
		}
		Symbol symbol = this.get(target);
		this.define(new Symbol(alias, symbol.getValue(), symbol.isRelocatable()));
	}
	
	/**
	 *  Returns true if this table contains a Symbol that matches the given name.
	 * @param name the String to be checked if it matched a Symbol
	 * @return true if and only if the name matches a Symbol
	 */
	public boolean hasSymbol(String name) {
		return this.map.containsKey(name);
	}
	
	/**
	 *  Gets the Symbol mapped to the given name, or null if none exists.
	 * @param name the String to be looked up
	 * @return the Symbol mapped to the given name
	 */
	public Symbol get(String name) {
		return this.map.get(name);
	}
}