/**
 * This enumeration defines all the possible execution modes for a Machine.
 * 
 * Quiet mode displays no output other than error messages and program output.
 * 
 * Trace mode runs the full program, displaying the memory page and state of
 * the registers before and after program execution. After each instruction
 * is executed, the program also displays the instruction code and any
 * memory addresses and registers that were affected.
 * 
 * Step mode behaves like Trace mode, but waits for user input before
 * executing each instruction.
 */
public enum ExecutionMode {
	QUIET, TRACE, STEP
}