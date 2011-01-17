package instructions;
import java.util.Map;
import java.util.HashMap;
import state.MachineState;
import state.MemoryBank;
import util.ByteOperations;

/**
 * Static class for mapping op codes to instructions.
 */
public class InstructionMappings {
	/**
	 * Lookup table used to map instruction codes to handlers.
	 */
	private static Map<Integer, InstructionHandler> mappings = new HashMap<Integer, InstructionHandler>();
	
	// Note: the static initializer block must come *after* the definition of "mappings".
	static {
		// ADD
		InstructionMappings.mappings.put(0x1, new AddHandler());
		
		// AND
		InstructionMappings.mappings.put(0x5, new AndHandler());
		
		// BR
		InstructionMappings.mappings.put(0x0, new BranchHandler());
		
		// DBUG
		InstructionMappings.mappings.put(0x8, new DebugHandler());
		
		// JSR
		InstructionMappings.mappings.put(0x4, new JumpSubroutineImmediateHandler());
		
		// JSRR
		InstructionMappings.mappings.put(0xc, new JumpSubroutineRegisterHandler());
		
		// LD
		InstructionMappings.mappings.put(0x2, new LoadHandler());
		
		// LDI
		InstructionMappings.mappings.put(0xa, new LoadImmediateHandler());
		
		// LDR
		InstructionMappings.mappings.put(0x6, new LoadRegisterHandler());
		
		// LEA
		InstructionMappings.mappings.put(0xe, new LoadEffectiveAddressHandler());
		
		// NOT
		InstructionMappings.mappings.put(0x9, new NotHandler());
		
		// RET
		InstructionMappings.mappings.put(0xd, new ReturnHandler());
		
		// ST
		InstructionMappings.mappings.put(0x3, new StoreHandler());
		
		// STI
		InstructionMappings.mappings.put(0xb, new StoreImmediateHandler());
		
		// STR
		InstructionMappings.mappings.put(0x7, new StoreRegisterHandler());
		
		// TRAP
		InstructionMappings.mappings.put(0xf, new TrapHandler());
	}
	
	/**
	 * Extract the op code from the four most significant bits of the given 16-bit instruction.
	 * @return The integer value of the op code extracted from the given instruction.
	 */
	public static int getOpCode(int instruction) {
		return ByteOperations.extractValue(instruction, 12, 16);
	}
	
	/**
	 * Get the InstructionHandler associated with the given op code. Returns null if no matching InstructionHandler is found.
	 * @return The InstructionHandler associated with the given op code.
	 */
	public static InstructionHandler getHandler(int opCode) {
		if (InstructionMappings.mappings.containsKey(opCode))
			return InstructionMappings.mappings.get(opCode);
		else
			return null;
	}
	
	/**
	 * Executes the given instruction, using and modifying the given MachineState and MemoryBank.
	 * @param instruction The 16-bit instruction code to execute.
	 * @param state The MachineState to use and modify.
	 * @param memory The MemoryBank to use and modify.
	 */
	public static void execute(int instruction, MachineState state, MemoryBank memory) throws Exception {
		int opCode = InstructionMappings.getOpCode(instruction);
		InstructionHandler handler = InstructionMappings.getHandler(opCode);
		if(handler != null)
			handler.execute(instruction, state, memory);
		else
			throw new Exception("Execution error: invalid op code 0x" + ByteOperations.getHex(opCode, 1) + ".");
	}
}