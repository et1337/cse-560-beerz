package Assembler;

import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.io.PrintStream;
import java.io.OutputStream;
import util.ByteOperations;

// A Program contains an in-memory representation of an assembly Program, which can
// be rendered into binary form with the getCode function.
public class Program {
	
	// Contains the names and values of all symbols defined in this Program.
	private SymbolTable symbols;
	
	// Contains the addresses and values of all literals defined in this Program.
	private LiteralTable literals;
	
	// Contains all Instructions (source code, binary code, and Operand values) in this Program.
	private List<Instruction> instructions;
	
	// The address (relative to the origin) to start execution at.
	private int startAddress;
	
	// The absolute address of the first memory slot used by the Program. Any relocatable
	// values in the Program are offset by this address.
	private int origin;
	
	// Name for the program. Used as the segment name in the header record.
	private String name;
	
	// Instantiates a new Program with the given data.
	public Program(String name, SymbolTable symbols, LiteralTable literals, List<Instruction> instructions, int startAddress, int origin) {
		this.symbols = symbols;
		this.literals = literals;
		this.instructions = instructions;		
		this.startAddress = startAddress;
		this.origin = origin;
		this.name = name;
	}
	
	// Gets the object code for this Program, optionally displaying a
	// listing for the user.
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
		
		String header = "H" + this.name + ByteOperations.getHex(this.origin, 4) + ByteOperations.getHex(this.literals.getOffset() + this.literals.getEntries().size(), 4);
		out.println(header);
		result.append(header);
		result.append("\n");
		
		int address = 0;
		
		// Output modification records. Note: modification records use bitmask format.
		// That is, any bits that need relocated will be 1, all other bits will be 0.
		for (Instruction instruction : this.instructions) {
			int[] codes = new int[instruction.getDefinition().getSize()];
			OperandDefinition[] operandDefinitions = instruction.getDefinition().getOperandDefinitions();
			for (int i = 0; i < operandDefinitions.length; i++) {
				OperandDefinition operandDefinition = operandDefinitions[i];
				if (operandDefinition.isRelocatable()) {
					codes[operandDefinition.getOperationIndex()] |= operandDefinition.getMask();
				}
			}
			for (int i = 0; i < codes.length; i++) {
				if (codes[i] != 0) {
					String code2 = ByteOperations.getHex(address + i, 4) + ByteOperations.getHex(codes[i] & 0xffff, 4);
					result.append("M");
					result.append(code2);
					result.append("\n");
				}
			}
			address += codes.length;
		}
		
		address = 0;
		
		// Output instructions.
		for (Instruction instruction : this.instructions) {
			try {
				int[] codes = instruction.getCodes(this.symbols, this.literals, this.origin);
				String code = "        ";
				if (codes.length > 0) {
					code = ByteOperations.getHex(address, 4) + ByteOperations.getHex(codes[0], 4);
					result.append("T");
					result.append(code);
					result.append("\n");
				}
				out.println(code + "    " + instruction.getSource());
				for (int i = 1; i < codes.length; i++) {
					String code2 = ByteOperations.getHex(address + i, 4) + ByteOperations.getHex(codes[i], 4);
					out.println(code2);
					result.append("T");
					result.append(code2);
					result.append("\n");
				}
				address += codes.length;
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
			result.append("\n");
			out.println(code);
		}
		
		String end = "E" + ByteOperations.getHex(this.startAddress, 4);
		result.append(end);
		out.println(end);
		
		// Return resulting code.
		return result.toString();
	}
}