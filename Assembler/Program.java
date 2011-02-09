import java.util.List;
import util.ByteOperations;

public class Program {

	private SymbolTable symbols;
	private LiteralTable literals;
	private List<Instruction> instructions;
	private int startAddress;
	private int origin;
	
	public Program(SymbolTable symbols, LiteralTable literals, List<Instruction> instructions, int startAddress, int origin) {
		this.symbols = symbols;
		this.literals = literals;
		this.instructions = instructions;
		this.startAddress = startAddress;
		this.origin = origin;
	}
	
	public String getCode() {
		return null;
	}
	
	public void printListing() {
		int size = 0;
		for (Instruction instruction : this.instructions) {
			size += instruction.getDefinition().getSize();
		}
		this.literals.setOffset(size);
		int address = 0;
		for (Instruction instruction : this.instructions) {
			try {
				int[] codes = instruction.getCodes(this.symbols, this.literals);
				String code = "        ";
				if (codes.length > 0) {
					code = ByteOperations.getHex(address, 4) + ByteOperations.getHex(codes[0], 4);
				}
				System.out.println(code + "    " + instruction.getSource());
				for (int i = 1; i < codes.length; i++) {
					System.out.println(ByteOperations.getHex(address + i, 4) + ByteOperations.getHex(codes[i], 4));
				}
				address += codes.length;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}