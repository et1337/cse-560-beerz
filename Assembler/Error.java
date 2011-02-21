package Assembler

public static class Error {
	/**
	 * Line number associated with this error. Optional.
	 */
	private int line;
	
	/**
	 * Message to the user describing this error. Required.
	 */
	private String message;
	
	/**
	 * True if and only if this error has a line number associated with it.
	 */
	private boolean hasLine;
	
	/**
	 * Creates a new error associated with a line number.
	 * @param _line The line number to associate with this error.
	 * @param _message A message to the user describing this error.
	 */
	public Error(int _line, String _message) {
		this.line = _line;
		this.message = _message;
		this.hasLine = true;
	}
	
	/**
	 * Creates a new error without associating it with a line number.
	 * @param _message A message to the user describing this error.
	 */
	public Error(String _message) {
		this.message = _message;
		this.hasLine = false;
	}
	
	/**
	 * Used to determine whether this error is associated with a line number.
	 * @return true if and only if this error has a line number associated with it.
	 */
	public boolean hasLineNumber() {
		return this.hasLine;
	}
	
	/**
	 * Gets the line number associated with this error.
	 * Will return 0 by default if no line number is associated.
	 * @return The line number associated with this error, or 0 if no line is associated.
	 */
	public int getLineNumber() {
		return this.line;
	}
	
	/**
	 * Gets the message describing this error to the user.
	 * @return A message describing this error to the user.
	 */
	public String getMessage() {
		return this.message;
	}
}