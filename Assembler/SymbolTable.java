import java.util.Map;
import java.util.HashMap;

public class SymbolTable {

	// Internal representation mapping names to Symbols.
	private Map<String, Symbol> map = new HashMap<String, Symbol>();
	
	// Adds the given Symbol to this SymbolTable.
	public void define(Symbol symbol) {
		this.map.put(symbol.getName(), symbol);
	}
	
	// Defines an alias for another Symbol.
	public void define(String alias, String target) {
		Symbol symbol = this.get(target);
		this.define(new Symbol(alias, symbol.getValue(), symbol.isRelocatable()));
	}
	
	// Returns true if this table contains a Symbol that matches the given name.
	public boolean hasSymbol(String name) {
		return this.map.containsKey(name);
	}
	
	// Gets the Symbol mapped to the given name, or null if none exists.
	public Symbol get(String name) {
		return this.map.get(name);
	}
}