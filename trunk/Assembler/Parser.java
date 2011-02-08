import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

public class Parser {

	private Map<String, InstructionDefinition> instructionDefinitions = new HashMap<String, InstructionDefinition>();
	
	public Parser() {
		this.instructionDefinitions.put("ADD", new InstructionDefinition // ...
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
			
			String label = line.substring(0, 5).trim();
			if(!label.equals("")) {
				symbols.define(new Symbol(label, location, true));
			}
			String op = line.substring(9, 4).trim();
			String[] operands = this.getOperands(line);
			
			Instruction instruction = new Instruction(line);
			
			if (op.equals(".ORIG")) {
				instruction.setDefinition(new InstructionDefinition(0));
				if (operands.length > 1 || operands.length == 0) {
					// Error
				}
				origin = ByteOperations.parseHex(operands[0]);
			}
			else if (op.equals(".EQU")) {
				instruction.setDefinition(new InstructionDefinition(0));
				if (operands.length > 1 || operands.length == 0) {
					// Error
				}
				symbols.define(label, operands[0]);
			}
			else if (op.equals(".FILL")) {
				instruction.setDefinition(new InstructionDefinition(1));
				if (operands.length > 1 || operands.length == 0) {
					// Error
				}
				instruction.setOperands(operands);
			}
			else if (op.equals(".STRZ")) {
				if (operands.length > 1 || operands.length == 0) {
					// Error
				}
				String stringLiteral = operands[0];
				instruction.setDefinition(new InstructionDefinition(stringLiteral.length() + 1));
				String[] chars = new String[stringLiteral.length() + 1];
				for (int i = 0; i < stringLiteral.length(); i++) {
					chars[i] = ByteOperations.getHex((int) stringLiteral.charAt(i), 4);
				}
				chars[stringLiteral.length()] = "0000";
				instruction.setOperands(chars);
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
				InstructionDefinition definition = this.instructionDefinitions.get(op);
				if (definition == null) {
					// Error
					definition = new InstructionDefinition(operands.length);
				}
				instruction.setDefinition(definition);
				instruction.setOperands(operands);
			}
			location += instruction.getDefinition().getSize();
			instructions.add(instruction);
		}
		return new Program(symbols, instructions, startAddress, origin);
	}
	
	protected String[] getOperands(String line) {
		int commentIndex = line.indexOf(";");
		String trimmed = line.substring(17, commentIndex == -1 ? line.length() - 17 : commentIndex).trim();
		ArrayList<String> result = new ArrayList<String>();
		boolean inQuotes = false;
		String currentOperand = "";
		for(int i = 0; i < trimmed.length(); i++) {
			char c = trimmed.charAt(i);
			if(inQuotes) {
				if(c.equals('"') {
					inQuotes = false;
					result.add(currentOperand);
					currentOperand = "";
				}
				else {
					currentOperand += c;
				}
			}
			else {
				if(c.equals('"')) {
					inQuotes = true;
				}
				else if(c.equals(',')) {
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