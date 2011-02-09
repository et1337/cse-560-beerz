import java.util.Arrays;
public class OperandDefinition {

	private int mostSignificant;
	private int leastSignificant;
	private int opIndex;
	private boolean relocatable;
	private OperandType[] acceptableTypes;
	
	public OperandDefinition(boolean relocatable, OperandType[] acceptableTypes, int mostSignificant, int leastSignificant) {
		this(relocatable, acceptableTypes, 0, mostSignificant, leastSignificant);
	}
	
	public OperandDefinition(boolean relocatable, OperandType[] acceptableTypes, int opIndex, int mostSignificant, int leastSignificant) {
		this.relocatable = relocatable;
		this.acceptableTypes = acceptableTypes;
		this.mostSignificant = mostSignificant;
		this.leastSignificant = leastSignificant;
		this.opIndex = opIndex;
	}
	
	public boolean isAcceptable(Operand operand) {
		return Arrays.asList(this.acceptableTypes).contains(operand.getType());
	}
	
	public int getMask() {
		int result = 0;
		for(int i = this.mostSignificant; i >= this.leastSignificant; i--) {
			result |= 1 << i;
		}
		return result;
	}

	public int getMostSignificantBit() {
		return this.mostSignificant;
	}
	
	public int getLeastSignificantBit() {
		return this.leastSignificant;
	}
	
	public int getOperationIndex() {
		return this.opIndex;
	}
	
	public boolean isRelocatable() {
		return this.relocatable;
	}
	
	public OperandType[] getAcceptableTypes() {
		return this.acceptableTypes;
	}
}