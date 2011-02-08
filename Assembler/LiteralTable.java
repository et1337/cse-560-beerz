import java.util.Map;
import java.util.HashMap;

public class LiteralTable {

	private int index = 0;
	private Map<int, int> map = new HashMap<int, int>();
	
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