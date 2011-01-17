package state;
import java.util.Map;
import java.util.HashMap;

public class MemoryBank {
	private Map<Integer, Short> data;
	public MemoryBank() {
		this.data = new HashMap<Integer, Short>();
	}
	
	public void write(int address, short value) {
		this.data.put(address, value);
	}
	
	public short read(int address) {
		if (this.data.containsKey(address))
			return this.data.get(address);
		return 0;
	}
}