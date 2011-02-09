import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import util.ByteOperations;

public class Parser {

	private InstructionDefinition[] instructionDefinitions;
	
	public Parser() {
		this.instructionDefinitions = new InstructionDefinition[] {
			new InstructionDefinition(
				"ADD",
				new int[] { 0x1000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER }, 11, 9),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER }, 8, 6),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER }, 2, 0)
				}),
			new InstructionDefinition(
				"ADD",
				new int[] { 0x1020 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER }, 11, 9),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER }, 8, 6),
					new OperandDefinition(false, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 4, 0)
				}),
			new InstructionDefinition(
				"AND",
				new int[] { 0x5000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER }, 11, 9),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER }, 8, 6),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER }, 2, 0)
				}),
			new InstructionDefinition(
				"AND",
				new int[] { 0x5020 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER }, 11, 9),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER }, 8, 6),
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
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER }, 8, 6),
					new OperandDefinition(false, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 5, 0)
				}),
			new InstructionDefinition(
				"JMPR",
				new int[] { 0xC800 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER }, 8, 6),
					new OperandDefinition(false, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 5, 0)
				}),
			new InstructionDefinition(
				"LD",
				new int[] { 0x2000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER }, 11, 9),
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"LDI",
				new int[] { 0xA000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER }, 11, 9),
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"LDR",
				new int[] { 0x6000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER }, 11, 9),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER }, 8, 6),
					new OperandDefinition(false, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 5, 0)
				}),
			new InstructionDefinition(
				"LEA",
				new int[] { 0xE000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER }, 11, 9),
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"NOT",
				new int[] { 0x9000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER }, 11, 9),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER }, 8, 6)
				}),
			new InstructionDefinition(
				"RET",
				new int[] { 0xD000 },
				new OperandDefinition[] { }),
			new InstructionDefinition(
				"ST",
				new int[] { 0x3000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER }, 11, 9),
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"STI",
				new int[] { 0xB000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER }, 11, 9),
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"STR",
				new int[] { 0x7000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER }, 11, 9),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER }, 8, 6),
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
	
	public Program parse(String data) {
		String[] lines = data.split("\n");
		SymbolTable symbols = new SymbolTable();
		LiteralTable literals = new LiteralTable();
		List<Instruction> instructions = new LinkedList<Instruction>();
		int startAddress = 0;
		int location = 0;
		int origin = 0;
		for(String line : lines) {
			if(line.charAt(0) == ';')
				continue; // Skip comment lines
				
			try {
			
				String label = line.substring(0, 6).trim();
				String op = line.substring(9, 4).trim();
				String[] operands = this.getOperands(line);
				
				if(!label.equals("") && !op.equals(".EQU")) {
					symbols.define(new Symbol(label, location, true));
				}
				
				Instruction instruction = new Instruction(op, line);
				
				if (op.equals(".ORIG")) {
					instruction.setDefinition(new InstructionDefinition(0));
					if (operands.length > 1 || operands.length == 0) {
						// Error
					}
					origin = ByteOperations.parseHex(operands[0]);
				}
				else if (op.equals(".EQU")) {
					instruction.setDefinition(new InstructionDefinition(0));
					if (operands.length > 1 || operands.length == 0 || label == "") {
						// Error
					}
					symbols.define(label, operands[0]);
				}
				else if (op.equals(".FILL")) {
					instruction.setDefinition(new InstructionDefinition(1));
					if (operands.length > 1 || operands.length == 0) {
						// Error
					}
					instruction.setOperands(operands, literals);
				}
				else if (op.equals(".STRZ")) {
					if (operands.length > 1 || operands.length == 0) {
						// Error
					}
					String stringLiteral = operands[0];
					instruction.setDefinition(new InstructionDefinition(stringLiteral.length() + 1));
					String[] chars = new String[stringLiteral.length() + 1];
					for (int i = 0; i < stringLiteral.length(); i++) {
						chars[i] = "x" + ByteOperations.getHex((int) stringLiteral.charAt(i), 4);
					}
					chars[stringLiteral.length()] = "x0000";
					instruction.setOperands(chars, literals);
				}
				else if (op.equals(".END")) {
					instruction.setDefinition(new InstructionDefinition(0));
					if (operands.length > 1) {
						// Error
					}
					if (operands.length == 1) {
						startAddress = ByteOperations.parseHex(operands[0]);
					}
				}
				else if (op.equals(".BLKW")) {
					instruction.setDefinition(new InstructionDefinition(0));
					if (operands.length > 1 || operands.length == 0) {
						// Error
					}
					location += ByteOperations.parseHex(operands[0]);
				}
				else {
					instruction.setOperands(operands, literals);
					InstructionDefinition definition = this.getInstructionDefinition(instruction);
					if (definition == null) {
						// Error
						definition = new InstructionDefinition(operands.length);
					}
					instruction.setDefinition(definition);
				}
				location += instruction.getDefinition().getSize();
				instructions.add(instruction);
			}
			catch (Exception e) {
				// Error handling
			}
		}
		return new Program(symbols, instructions, startAddress, origin);
	}
	
	protected InstructionDefinition getInstructionDefinition(Instruction instruction) {
		for (InstructionDefinition definition : this.instructionDefinitions) {
			if (definition.isAcceptable(instruction)) {
				return definition;
			}
		}
		return null;
	}
	
	protected String[] getOperands(String line) {
		int commentIndex = line.indexOf(";");
		String trimmed = line.substring(17, commentIndex == -1 ? line.length() - 17 : commentIndex).trim();
		ArrayList<String> result = new ArrayList<String>();
		boolean inQuotes = false;
		String currentOperand = "";
		for(int i = 0; i < trimmed.length(); i++) {
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
				else if (c == ',') {
					result.add(currentOperand);
					currentOperand = "";
				}
			}
		}
		if(!currentOperand.equals("")) {
			result.add(currentOperand);
		}
		String[] array = new String[result.size()];
		return result.toArray(array);
	}
}