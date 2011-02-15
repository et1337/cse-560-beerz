package Assembler;

// Enumerates the possible types of Operands:
// Immediate - represents a constant value (e.g. xA7, #13)
// Register - corresponds to a register R0-R7.
// Literal - references a literal (e.g. =#100, =x5)
// Symbol - references a symbol in the symbol table
public enum OperandType {
	IMMEDIATE, REGISTER, LITERAL, SYMBOL
}