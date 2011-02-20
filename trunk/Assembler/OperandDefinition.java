package Assembler;

import java.util.Arrays;

/**
 * An OperandDefinition states which values are acceptable for Operands that use
 * this definition, and where to insert the binary value into the resulting
 * executable code.
 * 
 */
public class OperandDefinition {

	/**
	 * Right-aligned index of the most significant bit this definition's
	 * Operands are allowed to affect in the executable code.
	 */
	private int mostSignificant;

	/**
	 * Right-aligned index of the least significant bit this definition's
	 * Operands are allowed to affect in the executable code.
	 */
	private int leastSignificant;

	/**
	 * For Instructions that map to multiple memory addresses, this index
	 * identifies which memory address to insert the Operand into.
	 */
	private int opIndex;

	/**
	 * True if this definition's Operands are relocatable, false if not.
	 */
	private boolean relocatable;

	/**
	 * Any Operands that use this definition must have a type that is containe
	 * in this collection.
	 */
	private OperandType[] acceptableTypes;

	/**
	 * Instantiates a new OperandDefinition with the assumption that the
	 * Instruction in question maps to only one memory address. See the other
	 * OperandDefinition constructor.
	 * 
	 * @param relocatable a boolean that determines if the Operand is relocatable
	 * @param acceptableTypes an array of of valid OperandTypes
	 * @param mostSignificant the most significant bit of the Operand
	 * @param leastSignificant the least significant bit of the Operand
	 */
	public OperandDefinition(boolean relocatable,
			OperandType[] acceptableTypes, int mostSignificant,
			int leastSignificant) {
		this(relocatable, acceptableTypes, 0, mostSignificant, leastSignificant);
	}

	/**
	 * Instantiates a new OperandDefinition with the following properties for
	 * any Operands that use this definition. relocatable - true if the Operand
	 * is relocatable, false if not. acceptableTypes - the Operand's type must
	 * be contained in this collection. opIndex - the Operand's binary value
	 * will be inserted in this memory address, relative to the start of the
	 * instruction. mostSignificant - the Operand's binary value, when inserted
	 * into the executable code,will not affect bits beyond the right-aligned
	 * index of this bit. leastSignificant - the Operand's binary value will be
	 * inserted into the executable code starting at the right-aligned index of
	 * this bit.
	 * 
	 * @param relocatable a boolean that determines if the Operand is relocatable
	 * @param acceptableTypes an array of of valid OperandTypes
	 * @param opIndex where the Operands binary value is inserted
	 * @param mostSignificant the most significant bit of the Operand
	 * @param leastSignificant the least significant bit of the Operand
	 */
	public OperandDefinition(boolean relocatable,
			OperandType[] acceptableTypes, int opIndex, int mostSignificant,
			int leastSignificant) {
		this.relocatable = relocatable;
		this.acceptableTypes = acceptableTypes;
		this.mostSignificant = mostSignificant;
		this.leastSignificant = leastSignificant;
		this.opIndex = opIndex;
	}

	/**
	 * Returns true if the given Operand meets the requirements of this
	 * definition. Namely, the type of the Operand must be contained in this
	 * definition's collection of acceptable OperandTypes.
	 * 
	 * @param operand the Operand to be checked
	 * @return true if and only the Operand is valid
	 */
	public boolean isAcceptable(Operand operand) {
		return Arrays.asList(this.acceptableTypes).contains(operand.getType());
	}

	/**
	 * Returns a bitmask which, when anded together with a binary Operand value,
	 * will zero out any bits which are not allowed to be affected by the value.
	 * 
	 * @return a bitmask to eliminate unused bits in the Operand
	 */
	public int getMask() {
		int result = 0;
		for (int i = this.mostSignificant; i >= this.leastSignificant; i--) {
			result |= 1 << i;
		}
		return result;
	}

	/**
	 * Gets the right-aligned index of the most significant bit this
	 * definition's Operands are allowed to affect in the executable code.
	 * 
	 * @return the Operand's most significant bit
	 */
	public int getMostSignificantBit() {
		return this.mostSignificant;
	}

	/**
	 * Gets the right-aligned index of the least significant bit this
	 * definition's Operands are allowed to affect in the executable code.
	 * 
	 * @return the Operand's least significant bit
	 */
	public int getLeastSignificantBit() {
		return this.leastSignificant;
	}

	/**
	 * For Instructions that map to multiple memory addresses, this index
	 * identifies which memory address to insert the Operand into.
	 * 
	 * @return the memory address to insert the Operand into
	 */
	public int getOperationIndex() {
		return this.opIndex;
	}

	/**
	 * True if this definition's Operands are relocatable, false if not.
	 * 
	 * @return returns true if an only if the Operand is relocatable
	 */
	public boolean isRelocatable() {
		return this.relocatable;
	}

	/**
	 * Any Operands that use this definition must have a type that is contained
	 * in this collection.
	 * 
	 * @return an array of valid OperandTypes
	 */
	public OperandType[] getAcceptableTypes() {
		return this.acceptableTypes;
	}
}