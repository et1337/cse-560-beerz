package Assembler;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * The Assembler has no state; it contains a constant instruction definition
 * table, but other than that it is only used to assemble Programs.
 * 
 */
public class Assembler {

	/**
	 * Assembles the given code into a Program.
	 * 
	 * @param filename the file name of the source code
	 * @param data
	 * @return the assembled program
	 */
	public Program assemble(String filename, String data) throws Exception {
		List<Error> errors = new LinkedList<Error>();
	
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
				
				// Check for spacing errors.
				String space1 = line.length() >= 9 ? line.substring(7, 9).trim() : "";
				String space2 = line.length() >= 17 ? line.substring(14, 17).trim() : "";
				if (!space1.equals("") || !space2.equals("")) {
					// Should be blank spaces between label, operation, and operands.
					errors.add(new Error(lineNumber, "Incorrect spacing."));
				}
				
				String op = line.substring(9, 14).trim();
				String[] operands = this.getOperands(line);

				if (!label.equals("")) {
					if (symbols.hasSymbol(label)) {
						// Error: symbol redefined
						errors.add(new Error(lineNumber, "Symbol redefinition is not allowed."));
					}
					if (!op.equals(".EQU")) {
						symbols.define(new Symbol(label, location, true));
					}
				}

				Instruction instruction = new Instruction(op, line);

				// First check if the instruction is a psuedo-op.
				if (op.equals(".ORIG")) {
					instruction.setDefinition(new InstructionDefinition(
							".ORIG", 0, false));
					if (operands.length > 1) {
						// Error
						errors.add(new Error(".ORIG may have a maximum of one operand; " 
						+ Integer.toString(operands.length) + " operands were given."));
					}

					if (!label.equals("")) {
						segmentName = String.format("%1$-6s", label); // Pad
																		// segment
																		// name
																		// if
																		// necessary
					}
					if (operands.length == 1) {
						if (Operand.determineType(operands[0]) != OperandType.IMMEDIATE) {
							// Error
							errors.add(new Error(lineNumber, "Operand of .ORIG must be an immediate value."));
						} else {
							origin = Operand.parseConstant(operands[0]);
							location = origin;
							symbols.define(new Symbol(label, location, true));
						}
					} else {
						relocatable = true;
					}
				} else if (op.equals(".EQU")) {
					instruction.setDefinition(new InstructionDefinition(".EQU",
							0, false));
					if (operands.length != 1 || label == "") {
						// Error
						errors.add(new Error(lineNumber, "Incorrect usage of .EQU." + 
						" Requires a label and one operand."));
					}
					else {
						OperandType type = Operand.determineType(operands[0]);
						if (type == OperandType.SYMBOL) {
							symbols.define(label, operands[0]);
						} else {
							symbols.define(new Symbol(label, Operand
									.parseConstant(operands[0]), false));
						}
					}
				} else if (op.equals(".FILL")) {
					if (operands.length != 1) {
						// Error
						errors.add(new Error(lineNumber, ".FILL requires one operand. " 
						+ Integer.toString(operands.length) + " were given."));
					}
					else {
						OperandType type = Operand.determineType(operands[0]);
						boolean fillRelocatable = false;
						if (type == OperandType.SYMBOL) {
							if (!symbols.hasSymbol(operands[0])) {
								// Error
								errors.add(new Error(lineNumber, "Symbol referenced in operand not defined."));
							} else {
								Symbol symbol = symbols.get(operands[0]);
								fillRelocatable = symbol.isRelocatable();
							}
						}
						instruction.setOperands(operands, literals);
						instruction.setDefinition(new InstructionDefinition(
								".FILL", new int[] { 0x000 },
								new OperandDefinition[] { new OperandDefinition(
										fillRelocatable, new OperandType[] {
											OperandType.IMMEDIATE,
											OperandType.SYMBOL }, 0, 15, 0) }));
						if (!instruction.getDefinition().isAcceptable(instruction)) {
							errors.add(new Error(lineNumber, "Incorrect operands for .FILL operation."));
						}
					}
				} else if (op.equals(".STRZ")) {
					if (operands.length != 1) {
						// Error
						errors.add(new Error(lineNumber, ".STRZ requires one operand. " 
						+ Integer.toString(operands.length) + " were given."));
					}
					else {
						String stringLiteral = operands[0];
						OperandType type = Operand.determineType(stringLiteral);
						if (type != OperandType.STRING) {
							errors.add(new Error(lineNumber, "Incorrect operand type for .STRZ operation."));
						}
						String[] chars = new String[stringLiteral.length() + 1];
						for (int i = 0; i < stringLiteral.length(); i++) {
							chars[i] = "x"
									+ ByteOperations.getHex(
											(int) stringLiteral.charAt(i), 4);
						}
						chars[stringLiteral.length()] = "x0000";
						instruction.setOperands(chars, literals);
						instruction.setDefinition(new InstructionDefinition(
								".STRZ", stringLiteral.length() + 1, true));
					}
				} else if (op.equals(".END")) {
					instruction.setOperands(operands, literals);
					instruction.setDefinition(new InstructionDefinition(
						".END", new int[] { },
						new OperandDefinition[] { new OperandDefinition(
							true, new OperandType[] {
								OperandType.IMMEDIATE,
								OperandType.SYMBOL }, 0, 0) }));
					startAddress = Operand.getValue(operands[0], symbols,
						instruction.getDefinition().getOperandDefinitions()[0], literals);
				} else if (op.equals(".BLKW")) {
					int size = 0;
					if (operands.length != 1) {
						// Error
						errors.add(new Error(lineNumber, ".BLKW requires one operand.  "
						+ operands.length + " were given."));
					}
					else {
						size = Operand.getValue(operands[0], symbols, new OperandDefinition(
								false, new OperandType[] {
									OperandType.IMMEDIATE,
									OperandType.SYMBOL },
								0, 0),
							literals);
					}
					instruction.setDefinition(new InstructionDefinition(
							".BLKW", size, false));
				} else {
					// The instruction is not a pseudo-op. Look it up in the
					// instruction definition table.
					instruction.setOperands(operands, literals);
					InstructionDefinition definition = this
							.getInstructionDefinition(instruction);
					if (definition == null) {
						// Error
						errors.add(new Error(lineNumber, "Could not find definition for " +
						"operation \"" + op + "\" with matching operands."));
						definition = new InstructionDefinition(op,
								operands.length, false);
					}
					instruction.setDefinition(definition);
				}
				if (instruction.getDefinition() != null) {
					location += instruction.getDefinition().getSize();
				}
				instructions.add(instruction);
			} catch (Exception e) {
				// Error handling
				errors.add(new Error(lineNumber, e.getMessage()));
			}
			lineNumber++;
		}
		literals.setOffset(location);
		
		int lastAddress = literals.getOffset() + literals.getEntries().size();
		if ((origin & 0xFFFFFE00) != (lastAddress & 0xFFFFFE00)) {
			errors.add(new Error("Program spans multiple memory pages. Relocate or shrink the program to fit inside one memory page."));
		}
		
		if (origin < 0 || lastAddress > 0xFFFF) {
			errors.add(new Error("Program loads into memory outside the addressable range."));
		}
		
		if (errors.size() > 0) {
			//Output each error message that we have encountered
			StringBuffer msg = new StringBuffer();
			for(Error e : errors) {
				msg.append("Assemble error: ");
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
		
		return new Program(segmentName, relocatable, symbols, literals,
				instructions, startAddress, origin);
	}

	/**
	 * Given an Instruction with a name and collection of Operands, finds an
	 * InstructionDefinition in the instruction definition table that matches
	 * the Instruction.
	 * 
	 * @param instruction
	 *            the instruction to be looked up
	 * @return null
	 */
	protected InstructionDefinition getInstructionDefinition(
			Instruction instruction) {
		for (InstructionDefinition definition : InstructionDefinition.getTable()) {
			if (definition.isAcceptable(instruction)) {
				return definition;
			}
		}
		return null;
	}

	/**
	 * Extracts the raw string values of the Operands in a given line of source
	 * code.
	 * 
	 * @param line the line of code to extract the value of the Operands from
	 * @return an array of strings containing the Operands
	 */
	protected String[] getOperands(String line) throws Exception {
		String trimmed = line.length() >= 17 ? line.substring(17).trim() : "";
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
				} else {
					currentOperand += c;
				}
			} else {
				if (c == '"') {
					inQuotes = true;
				} else if (c == ';') {
					break;
				} else if (c == ',') {
					result.add(currentOperand.trim());
					currentOperand = "";
				} else {
					currentOperand += c;
				}
			}
		}
		if (inQuotes) {
			// Un-closed quotes
			throw new Exception("Detected string operand with unclosed quotation mark.");
		}
		if (!currentOperand.trim().equals("")) {
			result.add(currentOperand.trim());
		}
		String[] array = new String[result.size()];
		return result.toArray(array);
	}

	/**
	 * Returns a usable segment name for a program from the given filename.
	 * 
	 * @param filename
	 *            the filename extracted from the assembly code
	 * @return the segment name from the program
	 */
	protected String getNameFromFilename(String filename) {
		return String.format("%1$-6s", filename.substring(0, 6));
	}
}