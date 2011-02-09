import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class LiteralTable {

	private int index = 0;
	private Map<Integer, Integer> map = new HashMap<Integer, Integer>();
	private int offset;
	
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public int getOffset() {
		return this.offset;
	}
	
	public void define(int value) {
		if (!this.map.containsKey(value)) {
			this.map.put(value, this.index);
			this.index++;
		}
	}
	
	public int getAddress(int value) {
		return this.map.get(value) + this.offset;
	}
	
	public Set<Map.Entry<Integer, Integer>> getEntries() {
		return this.map.entrySet();
	}
}