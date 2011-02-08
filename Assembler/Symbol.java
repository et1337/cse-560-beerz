public class Symbol {
	
	private String name;
	private int value;
	private boolean isRelocatable;
	
	public Symbol(String name, int value, boolean isRelocatable) {
		this.name = name;
		this.value = value;
		this.isRelocatable = isRelocatable;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public boolean isRelocatable() {
		return this.isRelocatable;
	}
}