package Assembler;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import Common.ByteOperations;
import Common.Error;
import Common.Symbol;
import Common.SymbolTable;

/**
 * The Assembler has no state; it contains a constant instruction definition
 * table, but other than that it is only used to assemble Programs.
 * 
 */
public class Assembler {

	/**
	 * The maximum number of symbols a program is allowed to define.
	 */
	protected static final int MAX_SYMBOLS = 100;
	
	/**
	 * The maximum number of literals a program is allwoed to define.
	 */
	protected static final int MAX_LITERALS = 50;
	
	/**
	 * The maximum number of source records a program is allowed to compile to.
	 */
	protected static final int MAX_SOURCE_RECORDS = 200;

	/**
	 * Assembles the given code into a Program.
	 * 
	 * @param filename the file name of the source code
	 * @param data
	 * @return the assembled program
	 */
	public Program assemble(String filename, String data) throws Exception {
		List<Error> errors = new LinkedList<Error>();
		SymbolTable symbols = new SymbolTable();
		LiteralTable literals = new LiteralTable();
		List<Instruction> instructions = new LinkedList<Instruction>();
		String segmentName = this.getNameFromFilename(filename);
		int startAddress = 0;
		int location = 0;
		int origin = 0;
		int lineNumber = 1;
		boolean relocatable = false;
		boolean hasOrig = false;
		boolean hasEnd = false;
		
		String[] exports = new String[] { };
		
		String[] lines = data.split("\n");
		for (String line : lines) {

			if (line.length() >= 1 && line.charAt(0) == ';') {
				lineNumber++;
				continue; // Skip comment lines
			}

			try {

				// Extract the label
				String label = line.length() >= 8 ? line.substring(0, 7).trim() : "";
				
				if (!label.equals("")) {
					char c = label.charAt(0);
					if (c == 'x'
						|| c == 'R'
						|| c == '#'
						|| c == '=') {
						errors.add(new Error(lineNumber, "Label name must not start with 'x', 'R', '#', or '='."));
					}
					if (label.contains(" ") || label.contains(",")) {
						errors.add(new Error(lineNumber, "Label name must not contain spaces or commas."));
					}
				}
				
				// Check for spacing errors.
				String space1 = line.length() >= 9 ? line.substring(7, 9).trim() : "";
				String space2 = line.length() >= 17 ? line.substring(14, 17).trim() : "";
				if (!space1.equals("") || !space2.equals("")) {
					// Should be blank spaces between label, operation, and operands.
					errors.add(new Error(lineNumber, "Incorrect spacing."));
				}
				
				String op = line.length() > 9 ? line.substring(9, Math.min(line.length(), 14)).trim() : "";
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
				
					if (hasOrig) {
						// Can't have multiple .ORIG instructions
						errors.add(new Error(lineNumber, "Multiple .ORIG instructions are not allowed."));
					}
					hasOrig = true;
					
					if (instructions.size() > 0) {
						// .ORIG must be first instruction
						errors.add(new Error(lineNumber, ".ORIG instruction must be first non-comment line."));
					}
					instruction.setOperands(operands, literals);
					instruction.setDefinition(new InstructionDefinition(
							".ORIG", 0, false));
					if (operands.length > 1) {
						// Error
						errors.add(new Error(".ORIG may have a maximum of one operand; " 
						+ Integer.toString(operands.length) + " operands were given."));
					}

					if (!label.equals("")) {
						// Pad segment name if necessary
						segmentName = String.format("%1$-6s", label);
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
					instruction.setOperands(operands, literals);
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
							Symbol symbol = symbols.get(operands[0]);
							if (symbol.isImport()) {
								errors.add(new Error(lineNumber, ".EQU cannot be used with imported symbols."));
							} else {
								symbols.define(label, operands[0]);
							}
						} else if (type == OperandType.IMMEDIATE) {
							symbols.define(new Symbol(label, Operand
									.parseConstant(operands[0]), false));
						} else {
							// Error
							errors.add(new Error(lineNumber, ".EQU operand must be a symbol " +
								" or a constant value."));
						}
					}
				} else if (op.equals(".FILL")) {
					if (operands.length != 1) {
						// Error
						errors.add(new Error(lineNumber, ".FILL requires one operand. " 
							+ Integer.toString(operands.length) + " were given."));
					}
					else {
						instruction.setOperands(operands, literals);
						instruction.setDefinition(new InstructionDefinition(
								".FILL", new int[] { 0x000 },
								new OperandDefinition[] { new OperandDefinition(
										true, new OperandType[] {
											OperandType.IMMEDIATE,
											OperandType.SYMBOL }, 15, 0) }));
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
						// Remove quotes
						stringLiteral = stringLiteral.substring(1, stringLiteral.length() - 1);
						
						// Create character operands
						String[] chars = new String[stringLiteral.length() + 1];
						for (int i = 0; i < stringLiteral.length(); i++) {
							chars[i] = "x"
									+ ByteOperations.getHex(
											(int) stringLiteral.charAt(i), 4);
						}
						chars[stringLiteral.length()] = "x0000"; // Null terminator
						
						instruction.setOperands(chars, literals);
						instruction.setDefinition(new InstructionDefinition(
								".STRZ", stringLiteral.length() + 1, true));
					}
				} else if (op.equals(".END")) {
					if (!label.equals("")) {
						errors.add(new Error(lineNumber, "No label allowed on .END instruction."));
					}
					if (hasEnd) {
						// Can't have multiple .END instructions
						errors.add(new Error(lineNumber, "Multiple .END instructions are not allowed."));
					}
					hasEnd = true;
					instruction.setOperands(operands, literals);
					OperandDefinition[] ops = null;
					// Optional operand
					if (operands.length > 0) {
						ops = new OperandDefinition[] { new OperandDefinition(
							true, new OperandType[] {
								OperandType.IMMEDIATE,
								OperandType.SYMBOL }, 15, 0) };
					} else {
						ops = new OperandDefinition[] { };
					}
					instruction.setDefinition(new InstructionDefinition(
						".END", new int[] { }, ops));
					if (operands.length > 0) {
						startAddress = Operand.getValue(
							operands[0],
							symbols,
							instruction.getDefinition().getOperandDefinitions()[0],
							Operand.determineType(operands[0]),
							literals);
					} else {
						startAddress = origin; // Default start address = origin.
					}
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
									OperandType.SYMBOL }, 15, 0),
							Operand.determineType(operands[0]), literals);
						if (size < 0) { // Operand.getValue will allow negative numbers.
							size = 0;
							errors.add(new Error(lineNumber, ".BLKW requires a positive operand."));
						}
					}
					instruction.setOperands(operands, literals);
					instruction.setDefinition(new InstructionDefinition(
							".BLKW", size, false));
				} else if (op.equals(".ENT")) {
					instruction.setOperands(operands, literals);
					instruction.setDefinition(new InstructionDefinition(".ENT", 0, false));
					if (!label.equals("")) {
						errors.add(new Error(lineNumber, "No label allowed on .ENT instruction."));
					}
					if (operands.length == 0) {
						errors.add(new Error(lineNumber, ".ENT requires at least one operand."));
					} else {
						for (String operand : operands) {
							if (Operand.determineType(operand) != OperandType.SYMBOL) {
								errors.add(new Error(lineNumber, ".ENT operand \"" + operand +
									"\" must be a valid symbol name."));
							}
						}
						exports = operands;
					}
				} else if (op.equals(".EXT")) {
					instruction.setOperands(operands, literals);
					instruction.setDefinition(new InstructionDefinition(".EXT", 0, false));
					if (!label.equals("")) {
						errors.add(new Error(lineNumber, "No label allowed on .EXT instruction."));
					}
					if (operands.length == 0) {
						errors.add(new Error(lineNumber, ".EXT requires at least one operand."));
					} else {
						for (String operand : operands) {
							if (Operand.determineType(operand) != OperandType.SYMBOL) {
								errors.add(new Error(lineNumber, ".EXT operand \"" + operand +
									"\" must be a valid symbol name."));
							} else {
								symbols.define(new Symbol(operand));
							}
						}
					}
				} else {
					// The instruction is not a pseudo-op. Look it up in the
					// instruction definition table.
					instruction.setOperands(operands, literals);
					InstructionDefinition definition = this.getInstructionDefinition(instruction);
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
				if (e.getMessage() != null) {
					// Error handling
					errors.add(new Error(lineNumber, e.getMessage()));
				} else {
					e.printStackTrace();
				}
			}
			
			lineNumber++;
		}
		literals.setOffset(location);
		
		int lastAddress = literals.getOffset() + literals.getEntries().size();
		if (relocatable && (lastAddress & 0xFE00) > 0) {
			errors.add(new Error("Program spans multiple memory pages. Relocate or shrink the program to fit inside one memory page."));
		}
		
		if (origin < 0 || lastAddress > 0xFFFF) {
			errors.add(new Error("Program loads into memory outside the addressable range."));
		}
		
		if (symbols.size() > Assembler.MAX_SYMBOLS) {
			errors.add(new Error("Program exceeds limit for maximum number of symbols."));
		}
		
		if (literals.getEntries().size() > Assembler.MAX_LITERALS) {
			errors.add(new Error("Program exceeds limit for maximum number of literals."));
		}
		
		if (lastAddress - origin > Assembler.MAX_SOURCE_RECORDS) {
			errors.add(new Error("Program exceeds limit for maximum number of source records."));
		}
		
		if (!hasOrig || !hasEnd) {
			errors.add(new Error("Program is missing .ORIG and/or .END instructions."));
		}
		
		for (String export : exports) {
			Symbol symbol = symbols.get(export);
			if (symbol == null) {
				errors.add(new Error("Undefined .ENT symbol \"" + export + "\"."));
			} else {
				symbol.setExport();
			}
		}
		
		if (errors.size() > 0) {
			// Output each error message that we have encountered
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
				currentOperand += c;
				if (c == '"') {
					inQuotes = false;
					result.add(currentOperand);
					currentOperand = "";
				}
			} else {
				if (c == '"') {
					currentOperand += c;
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
		return String.format("%1$-6s", filename.substring(0, Math.min(filename.length(), 6)));
	}
}