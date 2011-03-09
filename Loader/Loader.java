package Loader;
import java.util.List;
import java.util.LinkedList;
import Common.ByteOperations;
import Common.MemoryBank;
import Common.Error;
import Common.Symbol;
import Common.SymbolTable;
import Common.SymbolEntry;

/**
 * Given the data from a file, this class loads the data into a MemoryBank, validating for errors as it goes.
 */
public class Loader {
	/**
	 * Required length of a header record line.
	 */
	private static final int HEADER_RECORD_LENGTH = 15;
	
	/**
	 * Required length of a text record line without modification record.
	 */
	private static final int TEXT_RECORD_LENGTH = 9;
	
	/**
	 * Required length of a text record line with modification record.
	 */
	private static final int TEXT_RECORD_MODIFICATION_LENGTH = 11;
	
	/**
	 * Required length of an end record line.
	 */
	private static final int END_RECORD_LENGTH = 5;
	
	/**
	 * Required minimum length of an export record line.
	 */
	private static final int EXPORT_RECORD_MIN_LENGTH = 7;
	
	/**
	 * Required minimum length of an import record line.
	 */
	private static final int IMPORT_RECORD_MIN_LENGTH = 6;
	
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
	public static ObjectFile load(String data) throws Exception {
		int startAddress = 0;
		int firstAddress = 0;
		int lastAddress = 0;
		int lineNumber = 1;
		boolean hasHeaderRecord = false;
		boolean hasEndRecord = false;
		boolean relocatable = false;
		List<Error> errors = new LinkedList<Error>();
		SymbolTable symbols = new SymbolTable();
		MemoryBank memory = new MemoryBank();
		List<SymbolEntry> symbolEntries = new LinkedList<SymbolEntry>();
		List<SymbolEntry> relocationRecords = new LinkedList<SymbolEntry>();
		String segmentName = "";
		for (String line : data.split("\n")) {
			try {
				line = line.trim();
				
				if (line.length() == 0)
					continue;
					
				if (line.startsWith("H")) {
					// Header record
					if (line.length() != Loader.HEADER_RECORD_LENGTH)
						errors.add(new Error(lineNumber, "Length of header record is incorrect. Should be " + Integer.toString(Loader.HEADER_RECORD_LENGTH) + " characters."));
					if (line.substring(7, 11).equals("MMMM"))
						relocatable = true;
					else
						firstAddress = ByteOperations.parseHex(line.substring(7, 11));
					segmentName = line.substring(1, 7);
					lastAddress = firstAddress + ByteOperations.parseHex(line.substring(11)) - 1;
					if(firstAddress > Loader.MAX_ADDRESS || lastAddress > Loader.MAX_ADDRESS) {
						errors.add(new Error(lineNumber, "Memory segment length is too large for virtual machine."));
					}
					hasHeaderRecord = true;
				}
				else if (line.startsWith("T")) {
					// Text record
					boolean invalidLength = false;
					if (line.length() >= Loader.TEXT_RECORD_LENGTH) {
						int address = ByteOperations.parseHex(line.substring(1, 5));
						if (address < firstAddress || address > lastAddress)
							errors.add(new Error(lineNumber, "Text record address 0x" + line.substring(1, 5) + " exists outside program memory range specified in header record."));
						short value = (short)ByteOperations.parseHex(line.substring(5, 9));
						memory.write(address, value);
						if (line.length() == Loader.TEXT_RECORD_MODIFICATION_LENGTH) {
							// Add a relocation record
							boolean isShortRecord = line.charAt(10) == '0';
							boolean isLongRecord = line.charAt(10) == '1';
							if (line.charAt(9) != 'M' || (!isShortRecord && !isLongRecord))
								errors.add(new Error(lineNumber, "Malformed modification record. Must be \"M0\" or \"M1\"."));
							else
								relocationRecords.add(new SymbolEntry(address, isShortRecord ? 5 : 16, 0));
						}
						else if (line.length() != Loader.TEXT_RECORD_LENGTH)
							invalidLength = true;
					}
					else
						invalidLength = true;
					if (invalidLength)
						errors.add(new Error(lineNumber, "Length of text record is incorrect. Should be " + Integer.toString(Loader.TEXT_RECORD_LENGTH) + " or " + Integer.toString(Loader.TEXT_RECORD_MODIFICATION_LENGTH) + " characters."));
				}
				else if (line.startsWith("X")) {
					if (line.length() < Loader.EXPORT_RECORD_MIN_LENGTH)
						errors.add(new Error(lineNumber, "Length of export record is incorrect. Should be at least " + Integer.toString(Loader.EXPORT_RECORD_MIN_LENGTH) + " characters."));
					else {
						if (line.charAt(1) != 'R' && line.charAt(1) != 'A')
							errors.add(new Error(lineNumber, "Second character of export record should be 'A' for absolute or 'R' for relative."));
						String name = line.substring(6);
						String error = Loader.validateSymbolName(name);
						if (error != null)
							errors.add(new Error(lineNumber, error));
						else
							symbols.define(new Symbol(name, ByteOperations.parseHex(line.substring(2, 6)), line.charAt(1) == 'R'));
					}
				}
				else if (line.startsWith("I")) {
					if (line.length() < Loader.EXPORT_RECORD_MIN_LENGTH)
						errors.add(new Error(lineNumber, "Length of export record is incorrect. Should be at least " + Integer.toString(Loader.EXPORT_RECORD_MIN_LENGTH) + " characters."));
					else {
						String name = line.substring(7);
						String error = Loader.validateSymbolName(name);
						if (error != null)
							errors.add(new Error(lineNumber, error));
						else {
							symbolEntries.add(
								new SymbolEntry(
									name,
									ByteOperations.parseHex(line.substring(1, 5)),
									ByteOperations.parseHex(line.substring(5, 6)),
									ByteOperations.parseHex(line.substring(6, 7))
									)
								);
						}
					}
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
					errors.add(new Error(lineNumber, "First character of line is invalid; must be 'H', 'T', 'E', 'I', or 'X'."));
				}
			} catch (Exception e) {
				errors.add(new Error(lineNumber, e.getMessage()));
			}
			
			lineNumber++;
		}
		
		if (!hasHeaderRecord)
			errors.add(new Error("Object file does not contain a header record."));
		
		if (!hasEndRecord)
			errors.add(new Error("Object file does not contain an end record."));
			
		if (errors.size() > 0) {
			// We have errors; throw an exception describing them
			StringBuffer msg = new StringBuffer();
			for (Error e : errors) {
				msg.append("Load error: ");
				if (e.hasLineNumber()) {
					msg.append("Line ");
					msg.append(Integer.toString(e.getLineNumber()));
					msg.append(" - ");
				}
				msg.append(e.getMessage());
				msg.append("\n");
			}
			throw new Exception(msg.toString());
		}
		
		return new ObjectFile(symbolEntries, relocationRecords, symbols, memory, relocatable, startAddress, segmentName);
	}
	
	/**
	 * Returns null if the given Symbol name is valid, or an error description if it is invalid.
	 @ return null if the given Symbol name is valid, or an error description if it is invalid.
	 */
	private static String validateSymbolName(String name) {
		char c = name.charAt(0);
		if (c == 'x'
			|| c == 'R'
			|| c == '#'
			|| c == '=') {
			return "Symbol name must not start with 'x', 'R', '#', or '='.";
		}
		if (name.contains(" ") || name.contains(",")) {
			return "Symbol name must not contain spaces or commas.";
		}
		return null;
	}
}