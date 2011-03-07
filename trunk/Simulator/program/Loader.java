package Simulator.program;
import java.util.List;
import java.util.LinkedList;
import Simulator.util.ByteOperations;
import Simulator.state.MemoryBank;

/**
 * Given the data from a file, this class loads the data into a MemoryBank, validating for errors as it goes.
 */
public class Loader {
	/**
	 * Represents a loader validation error.
	 */
	private static class Error {
		/**
		 * Line number associated with this error. Optional.
		 */
		private int line;
		
		/**
		 * Message to the user describing this error. Required.
		 */
		private String message;
		
		/**
		 * True if and only if this error has a line number associated with it.
		 */
		private boolean hasLine;
		
		/**
		 * Creates a new error associated with a line number.
		 * @param _line The line number to associate with this error.
		 * @param _message A message to the user describing this error.
		 */
		public Error(int _line, String _message) {
			this.line = _line;
			this.message = _message;
			this.hasLine = true;
		}
		
		/**
		 * Creates a new error without associating it with a line number.
		 * @param _message A message to the user describing this error.
		 */
		public Error(String _message) {
			this.message = _message;
			this.hasLine = false;
		}
		
		/**
		 * Used to determine whether this error is associated with a line number.
		 * @return true if and only if this error has a line number associated with it.
		 */
		public boolean hasLineNumber() {
			return this.hasLine;
		}
		
		/**
		 * Gets the line number associated with this error.
		 * Will return 0 by default if no line number is associated.
		 * @return The line number associated with this error, or 0 if no line is associated.
		 */
		public int getLineNumber() {
			return this.line;
		}
		
		/**
		 * Gets the message describing this error to the user.
		 * @return A message describing this error to the user.
		 */
		public String getMessage() {
			return this.message;
		}
	}
	
	/**
	 * Required length of a header record line.
	 */
	private static final int HEADER_RECORD_LENGTH = 15;
	
	/**
	 * Required length of a text record line.
	 */
	private static final int TEXT_RECORD_LENGTH = 9;
	
	/**
	 * Required length of an end record line.
	 */
	private static final int END_RECORD_LENGTH = 5;
	
	/**
	 * Maximum memory segment size
	 */
	private static final int MAX_ADDRESS = 65535;
	
	/**
	 * Loads the given data into the memory bank. If syntax errors are
	 * encountered, this method will collect them all into a single string
	 * and throw an exception containing information about the errors.
	 * @param data The data to process (presumably loaded straight from a file).
	 * @param bank The memory bank to load data into.
	 * @return the address to start execution from.
	 */
	public static int load(String data, MemoryBank bank) throws Exception {
		int startAddress = 0;
		int firstAddress = 0;
		int lastAddress = 0;
		int lineNumber = 1;
		boolean hasHeaderRecord = false;
		boolean hasEndRecord = false;
		List<Error> errors = new LinkedList<Error>();
		for (String line : data.split("\n")) {
			try {
				line = line.trim();
				
				if (line.length() == 0)
					continue;
					
				if (line.startsWith("H")) {
					// Header record
					if (line.length() != Loader.HEADER_RECORD_LENGTH)
						errors.add(new Error(lineNumber, "Length of header record is incorrect. Should be " + Integer.toString(Loader.HEADER_RECORD_LENGTH) + " characters."));
					firstAddress = ByteOperations.parseHex(line.substring(7, 11));
					lastAddress = firstAddress + ByteOperations.parseHex(line.substring(11)) - 1;
					if(firstAddress > Loader.MAX_ADDRESS || lastAddress > Loader.MAX_ADDRESS) {
						errors.add(new Error(lineNumber, "Memory segment length is too large for virtual machine."));
					}
					hasHeaderRecord = true;
				}
				else if (line.startsWith("T")) {
					// Text record
					if (line.length() != Loader.TEXT_RECORD_LENGTH)
						errors.add(new Error(lineNumber, "Length of text record is incorrect. Should be " + Integer.toString(Loader.TEXT_RECORD_LENGTH) + " characters."));
					int address = ByteOperations.parseHex(line.substring(1, 5));
					if (address < firstAddress || address > lastAddress)
						errors.add(new Error(lineNumber, "Text record address 0x" + line.substring(1, 5) + " exists outside program memory range specified in header record."));
					short value = (short)ByteOperations.parseHex(line.substring(5));
					bank.write(address, value);
				}
				else if (line.startsWith("E")) {
					// End record
					if(line.length() != Loader.END_RECORD_LENGTH)
						errors.add(new Error(lineNumber, "Length of end record is incorrect. Should be " + Integer.toString(Loader.END_RECORD_LENGTH) + " characters."));
					startAddress = ByteOperations.parseHex(line.substring(1));
					if(startAddress < firstAddress || startAddress > lastAddress) {
						errors.add(new Error(lineNumber, "Execution start address 0x" + line.substring(1).toLowerCase() + " outside specified memory segment range."));
					}
					hasEndRecord = true;
				}
				else {
					// Syntax error
					errors.add(new Error(lineNumber, "First character of line is invalid; must be 'H', 'T', or 'E'."));
				}
			} catch (Exception e) {
				errors.add(new Error(lineNumber, e.getMessage()));
			}
			
			lineNumber++;
		}
		
		if(!hasHeaderRecord)
			errors.add(new Error("Object file does not contain a header record."));
		
		if(!hasEndRecord)
			errors.add(new Error("Object file does not contain an end record."));
			
		if(errors.size() > 0) {
			// We have errors; throw an exception describing them
			StringBuffer msg = new StringBuffer();
			for(Error e : errors) {
				msg.append("Load error: ");
				if(e.hasLineNumber()) {
					msg.append("Line ");
					msg.append(Integer.toString(e.getLineNumber()));
					msg.append(" - ");
				}
				msg.append(e.getMessage());
				msg.append("\n");
			}
			throw new Exception(msg.toString());
		}
		
		return startAddress;
	}
}