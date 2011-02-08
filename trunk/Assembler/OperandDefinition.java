public class OperandDefinition {

	private int start;
	private int end;
	private int opIndex;
	private boolean relocatable;
	private boolean literal;
	
	public OperandDefinition(boolean relocatable, boolean literal, int opIndex, int start, int end) {
		this.relocatable = relocatable;
		this.literal = literal;
		if (this.literal && !this.relocatable) {
			// Invalid state
		}
		this.start = start;
		this.end = end;
		this.opIndex = opIndex;
	}
	
	public int getMask() {
		int result = 0;
		for(int i = start; i < end; i++) {
			result |= 1 << (16 - i);
		}
		return result;
	}
	
	public int translateValue(int value) {
		return this.getMask() & (value << (16 - end));
	}
	
	public int getOperationIndex() {
		return this.opIndex;
	}
	
	public boolean isRelocatable() {
		return this.relocatable;
	}
	
	public boolean canBeLiteral() {
		return this.literal;
	}
}