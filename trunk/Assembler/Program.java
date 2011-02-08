import java.util.List;

public class Program {

	private SymbolTable symbols;
	private List<Instruction> instructions;
	private int startAddress;
	private int origin;
	
	public Program(SymbolTable symbols, List<Instruction> instructions, int startAddress, int origin) {
		this.symbols = symbols;
		this.instructions = instructions;
		this.startAddress = startAddress;
		this.origin = origin;
	}
}