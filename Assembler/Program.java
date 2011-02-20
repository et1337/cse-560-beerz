package Assembler;

import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.io.PrintStream;
import java.io.OutputStream;

/**
 *  A Program contains an in-memory representation of an assembly Program, which can be rendered into binary form with the getCode function.
 *
 */
public class Program {
	
	/**
	 *  Contains the names and values of all symbols defined in this Program.
	 */
	private SymbolTable symbols;
	
	/**
	 *  Contains the addresses and values of all literals defined in this Program.
	 */
	private LiteralTable literals;
	
	/**
	 *  Contains all Instructions (source code, binary code, and Operand values) in this Program.
	 */
	private List<Instruction> instructions;
	
	/**
	 *  The address (relative to the origin) to start execution at.
	 */
	private int startAddress;
	
	/**
	 *  The absolute address of the first memory slot used by the Program. Any relocatable values in the Program are offset by this address.
	 */
	private int origin;
	
	/**
	 *  Name for the program. Used as the segment name in the header record.
	 */
	private String name;
	
	/**
	 *  True if the program is relocatable.
	 */
	private boolean isRelocatable;
	
	/**
	 *  Instantiates a new Program with the given data.
	 * @param name the name of the program
	 * @param isRelocatable a boolean that indicates whether the program is relocatable or not
	 * @param symbols a Symboltable of the program's contained symbols
	 * @param literals a LiteralTable of the program's contained literals
	 * @param instructions a List of the program's instructions
	 * @param startAddress the starting address of the program
	 * @param origin the origin of the program
	 */
	public Program(String name, boolean isRelocatable, SymbolTable symbols, LiteralTable literals, List<Instruction> instructions, int startAddress, int origin) {
		this.name = name;
		this.isRelocatable = isRelocatable;
		this.symbols = symbols;
		this.literals = literals;
		this.instructions = instructions;		
		this.startAddress = startAddress;
		this.origin = origin;
	}
	
	/**
	 *  Gets the object code for this Program, optionally displaying a listing for the user.
	 * @param printListing a boolean to determine whether or not to print a program listing
	 * @return a String representing the object file
	 */
	public String getCode(boolean printListing) {
	
		PrintStream out = null;
		if (printListing) {
			out = System.out;
		}
		else {
			out = new PrintStream(new OutputStream() {
				public void close() {}
				public void flush() {}
				public void write(byte[] b) {}
				public void write(byte[] b, int off, int len) {}
				public void write(int b) {}
			});
		}
		
		StringBuffer result = new StringBuffer();
		
		String header = "H"
			+ this.name
			+ ByteOperations.getHex(this.origin, 4)
			+ ByteOperations.getHex(this.literals.getOffset()
			+ this.literals.getEntries().size(), 4);
		result.append(header);
		result.append("\r\n");
		
		int address = this.origin;
		
		// Output instructions.
		for (Instruction instruction : this.instructions) {
			try {
				int[] codes = instruction.getCodes(this.symbols, this.literals);
				if (codes.length > 0) {
					int[] relocationMasks = instruction.getDefinition().getRelocationMasks();
					for (int i = 0; i < codes.length; i++) {
						String code = ByteOperations.getHex(address + i, 4) + ByteOperations.getHex(codes[i], 4);
						String output = code;
						if (i == 0) {
							output += "    " + instruction.getSource();
						}
						out.println(output);
						result.append("T");
						result.append(code);
						if (this.isRelocatable) {
							// For our purposes, we only have two types of relocatable addresses:
							// 9-bit page address offsets, and full sixteen bit addresses.
							if (ByteOperations.getBit(relocationMasks[i], 15)) {
								// It's a full sixteen bit modification record
								result.append("M1");
							}
							else if (ByteOperations.getBit(relocationMasks[i], 0)) {
								// It's a 9-bit page address offset.
								result.append("M0");
							}
						}
						result.append("\r\n");
					}
				}
				else {
					out.println("            " + instruction.getSource());
				}
				address += instruction.getDefinition().getSize();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// Output literal table. 
		Iterator<Map.Entry<Integer, Integer>> entryIterator = this.literals.getEntries().iterator();
		while (entryIterator.hasNext()) {
			Map.Entry<Integer, Integer> entry = entryIterator.next();
			String code = ByteOperations.getHex(entry.getValue() + this.literals.getOffset(), 4) + ByteOperations.getHex(entry.getKey(), 4);
			result.append("T");
			result.append(code);
			result.append("\r\n");
			out.println(code);
		}
		
		result.append("E" + ByteOperations.getHex(this.startAddress, 4));
		
		// Return resulting code.
		return result.toString();
	}
}