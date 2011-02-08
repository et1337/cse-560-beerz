import java.util.Map;
import java.util.HashMap;

public class SymbolTable {

	private Map<String, Symbol> map = new HashMap<String, Symbol>();
	
	public void define(Symbol symbol) {
		this.map.put(symbol.getName(), symbol);
	}
	
	public void define(String alias, String target) {
		this.define(alias, this.get(target));
	}
	
	public boolean hasSymbol(String name) {
		return this.map.containsKey(name);
	}
	
	public int get(String name) {
		return this.map.get(name);
	}
}