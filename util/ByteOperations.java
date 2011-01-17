package util;

/**
 * Utility class containing methods for dealing with bits and bytes.
 */
public class ByteOperations {
	/**
	 * The radix used in dealing with hexadecimal numbers.
	 */
	private static final int HEXADECIMAL_RADIX = 16;
	
	/**
	 * Parses the given hex value into an integer value.
	 * Upper- and lower-case letters are acceptable. Whitespace and other characters are not acceptable.
	 * Throws an exception if an invalid character is found.
	 * @param hex A string representing a hex value.
	 * @return An integer value representing the input hex value.
	 */
	public static int parseHex(String hex) throws Exception {
		int result = 0;
		int multiplier = 1;
		char[] digits = hex.toCharArray();
		for (int i = digits.length - 1; i >= 0; i--) {
			int digit = Character.digit(digits[i], ByteOperations.HEXADECIMAL_RADIX);
			if(digit < 0) // Invalid character
				throw new Exception("Cannot parse \"" + hex + "\" as a hex value.");
			result += multiplier * digit;
			multiplier *= ByteOperations.HEXADECIMAL_RADIX;
		}
		return result;
	}
	
	public static int extractValue(int value, int start, int end) {
		int mask = 0;
		for(int i = start; i < end; i++) {
			mask |= 1 << i;
		}
		return (value & mask) >> start;
	}
	
	/**
	 * Gets a hexadecimal string representation of the given value. Eliminates all but the least significant hex digits specified by numCharacters.
	 * @param value The integer value to convert to hexadecimal.
	 * @param numCharacters The number of least significant hex digits to display.
	 * @return A string hexadecimal representation of the given integer value.
	 */
	public static String getHex(int value, int numCharacters) {
		char[] result = new char[numCharacters];
		for (int i = 0; i < numCharacters; i++) {
			result[(numCharacters - 1) - i] = Character.forDigit(value % ByteOperations.HEXADECIMAL_RADIX, ByteOperations.HEXADECIMAL_RADIX);
			value /= ByteOperations.HEXADECIMAL_RADIX;
		}
		return new String(result);
	}
}