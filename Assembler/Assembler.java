package Assembler;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

// The Assembler has no state; it contains a constant instruction definition table,
// but other than that it is only used to assemble Programs.
public class Assembler {

	// Definitions of program instructions. Psuedo-ops are not defined in this table.
	private InstructionDefinition[] instructionDefinitions;
	
	// Instantiates a new Assembler. Initializes the instruction definition table.
	public Assembler() {
		
		// Initialize instruction definition table.
		this.instructionDefinitions = new InstructionDefinition[] {
			new InstructionDefinition(
				"ADD",
				new int[] { 0x1000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 11, 9),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 8, 6),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 2, 0)
				}),
			new InstructionDefinition(
				"ADD",
				new int[] { 0x1020 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 11, 9),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 8, 6),
					new OperandDefinition(false, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 4, 0)
				}),
			new InstructionDefinition(
				"AND",
				new int[] { 0x5000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 11, 9),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 8, 6),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 2, 0)
				}),
			new InstructionDefinition(
				"AND",
				new int[] { 0x5020 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 11, 9),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 8, 6),
					new OperandDefinition(false, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 4, 0)
				}),
			new InstructionDefinition(
				"BRN",
				new int[] { 0x0800 },
				new OperandDefinition[] {
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"BRZ",
				new int[] { 0x0400 },
				new OperandDefinition[] {
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"BRP",
				new int[] { 0x0200 },
				new OperandDefinition[] {
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"BRNZ",
				new int[] { 0x0C00 },
				new OperandDefinition[] {
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"BRNP",
				new int[] { 0x0A00 },
				new OperandDefinition[] {
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"BRZP",
				new int[] { 0x0600 },
				new OperandDefinition[] {
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"BRNZP",
				new int[] { 0x0E00 },
				new OperandDefinition[] {
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"DBUG",
				new int[] { 0x8000 },
				new OperandDefinition[] { }),
			new InstructionDefinition(
				"JSR",
				new int[] { 0x4000 },
				new OperandDefinition[] {
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"JMP",
				new int[] { 0x4800 },
				new OperandDefinition[] {
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"JSRR",
				new int[] { 0xC000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 8, 6),
					new OperandDefinition(false, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 5, 0)
				}),
			new InstructionDefinition(
				"JMPR",
				new int[] { 0xC800 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 8, 6),
					new OperandDefinition(false, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 5, 0)
				}),
			new InstructionDefinition(
				"LD",
				new int[] { 0x2000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 11, 9),
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL, OperandType.LITERAL }, 8, 0)
				}),
			new InstructionDefinition(
				"LDI",
				new int[] { 0xA000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 11, 9),
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"LDR",
				new int[] { 0x6000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 11, 9),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 8, 6),
					new OperandDefinition(false, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 5, 0)
				}),
			new InstructionDefinition(
				"LEA",
				new int[] { 0xE000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 11, 9),
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"NOT",
				new int[] { 0x9000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 11, 9),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 8, 6)
				}),
			new InstructionDefinition(
				"RET",
				new int[] { 0xD000 },
				new OperandDefinition[] { }),
			new InstructionDefinition(
				"ST",
				new int[] { 0x3000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 11, 9),
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"STI",
				new int[] { 0xB000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 11, 9),
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"STR",
				new int[] { 0x7000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 11, 9),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 8, 6),
					new OperandDefinition(false, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 6, 0)
				}),
			new InstructionDefinition(
				"TRAP",
				new int[] { 0xF000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 7, 0)
				})
		};
	}
	
	// Assembles the given code into a Program.
	public Program assemble(String filename, String data) {	
		String[] lines = data.split("\n");
		SymbolTable symbols = new SymbolTable();
		LiteralTable literals = new LiteralTable();
		List<Instruction> instructions = new LinkedList<Instruction>();
		String segmentName = this.getNameFromFilename(filename);
		int startAddress = 0;
		int location = 0;
		int origin = 0;
		int lineNumber = 1;
		boolean relocatable = false;
		for (String line : lines) {
		
			if (line.charAt(0) == ';')
				continue; // Skip comment lines
				
			try {
			
				String label = line.substring(0, 7).trim();
				String op = line.substring(9, 14).trim();
				String[] operands = this.getOperands(line);
				
				if (!label.equals("")) {
					if (symbols.hasSymbol(label)) {
						// Error: symbol redefined
					}
					if (!op.equals(".EQU")) {
						symbols.define(new Symbol(label, location, true));
					}
				}
				
				Instruction instruction = new Instruction(op, line);
				
				// First check if the instruction is a psuedo-op.
				if (op.equals(".ORIG")) {
					instruction.setDefinition(new InstructionDefinition(".ORIG", 0, false));
					if (operands.length > 1) {
						// Error
					}
					
					if (!label.equals("")) {
						segmentName = String.format("%1$-6s", label); // Pad segment name if necessary
					}
					if (operands.length == 1) {
						if (Operand.determineType(operands[0]) != OperandType.IMMEDIATE) {
							// Error
						}
						else {
							origin = Operand.parseConstant(operands[0]);
							location = origin;
							symbols.define(new Symbol(label, location, true));
						}
					}
					else {
						relocatable = true;
					}
				}
				else if (op.equals(".EQU")) {
					instruction.setDefinition(new InstructionDefinition(".EQU", 0, false));
					if (operands.length > 1 || operands.length == 0 || label == "") {
						// Error
					}
					OperandType type = Operand.determineType(operands[0]);
					if (type == OperandType.SYMBOL) {
						symbols.define(label, operands[0]);
					}
					else {
						symbols.define(new Symbol(label, Operand.parseConstant(operands[0]), false));
					}
				}
				else if (op.equals(".FILL")) {
					if (operands.length > 1 || operands.length == 0) {
						// Error
					}
					OperandType type = Operand.determineType(operands[0]);
					boolean fillRelocatable = false;
					if (type == OperandType.SYMBOL) {
						if (!symbols.hasSymbol(operands[0])) {
							// Error
						}
						else {
							Symbol symbol = symbols.get(operands[0]);
							fillRelocatable = symbol.isRelocatable();
						}
					}
					instruction.setOperands(operands, literals);
					instruction.setDefinition(
						new InstructionDefinition(
							".FILL",
							new int[] { 0x000 },
							new OperandDefinition[] {
								new OperandDefinition(fillRelocatable, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 0, 15, 0)
							}));
				}
				else if (op.equals(".STRZ")) {
					if (operands.length > 1 || operands.length == 0) {
						// Error
					}
					String stringLiteral = operands[0];
					String[] chars = new String[stringLiteral.length() + 1];
					for (int i = 0; i < stringLiteral.length(); i++) {
						chars[i] = "x" + ByteOperations.getHex((int) stringLiteral.charAt(i), 4);
					}
					chars[stringLiteral.length()] = "x0000";
					instruction.setOperands(chars, literals);
					instruction.setDefinition(
						new InstructionDefinition(".STRZ", stringLiteral.length() + 1, true));
				}
				else if (op.equals(".END")) {
					instruction.setDefinition(new InstructionDefinition(".END", 0, false));
					if (operands.length > 1) {
						// Error
					}
					if (operands.length == 1) {
						OperandType type = Operand.determineType(operands[0]);
						startAddress = Operand.getValue(operands[0], symbols, literals);
						if (type == OperandType.IMMEDIATE) {
							startAddress -= origin & 0x01ff;
						}
					}
				}
				else if (op.equals(".BLKW")) {
					if (operands.length > 1 || operands.length == 0) {
						// Error
					}
					int size = Operand.getValue(operands[0], symbols, literals);
					instruction.setDefinition(new InstructionDefinition(".BLKW", size, false));
				}
				else {
					// The instruction is not a pseudo-op. Look it up in the instruction definition table.
					instruction.setOperands(operands, literals);
					InstructionDefinition definition = this.getInstructionDefinition(instruction);
					if (definition == null) {
						// Error
						System.out.println("Error: undefined operation \"" + op + "\"");
						definition = new InstructionDefinition(op, operands.length, false);
					}
					instruction.setDefinition(definition);
				}
				location += instruction.getDefinition().getSize();
				instructions.add(instruction);
			}
			catch (Exception e) {
				// Error handling
				System.out.println("Error on line " + Integer.toString(lineNumber) + ": ");
				e.printStackTrace();
			}
			lineNumber++;
		}
		literals.setOffset(location);
		return new Program(segmentName, relocatable, symbols, literals, instructions, startAddress, origin);
	}
	
	// Given an Instruction with a name and collection of Operands, finds an InstructionDefinition
	// in the instruction definition table that matches the Instruction.
	protected InstructionDefinition getInstructionDefinition(Instruction instruction) {
		for (InstructionDefinition definition : this.instructionDefinitions) {
			if (definition.isAcceptable(instruction)) {
				return definition;
			}
		}
		return null;
	}
	
	// Extracts the raw string values of the Operands in a given line of source code.
	protected String[] getOperands(String line) {
		String trimmed = line.substring(17).trim();
		ArrayList<String> result = new ArrayList<String>();
		boolean inQuotes = false;
		String currentOperand = "";
		for (int i = 0; i < trimmed.length(); i++) {
			char c = trimmed.charAt(i);
			if (inQuotes) {
				if (c == '"') {
					inQuotes = false;
					result.add(currentOperand);
					currentOperand = "";
				}
				else {
					currentOperand += c;
				}
			}
			else {
				if (c == '"') {
					inQuotes = true;
				}
				else if(c == ';') {
					break;
				}
				else if (c == ',') {
					result.add(currentOperand.trim());
					currentOperand = "";
				}
				else {
					currentOperand += c;
				}
			}
		}
		if (!currentOperand.trim().equals("")) {
			result.add(currentOperand.trim());
		}
		String[] array = new String[result.size()];
		return result.toArray(array);
	}
	
	// Returns a usable segment name for a program from the given filename.
	protected String getNameFromFilename(String filename) {
		return String.format("%1$-6s", filename.substring(0, 6));
	}
}