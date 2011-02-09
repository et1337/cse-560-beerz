import java.util.Map;
import java.util.HashMap;

public class LiteralTable {

	private int index = 0;
	private Map<Integer, Integer> map = new HashMap<Integer, Integer>();
	
	public void define(int value) {
		if (!this.map.containsKey(value)) {
			this.map.put(value, this.index);
			this.index++;
		}
	}
	
	public int getAddress(int value) {
		return this.map.get(value);
	}
}