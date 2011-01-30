package testing;
import java.io.PrintStream;
import java.io.OutputStream;
import java.io.InputStream;

/**
 * Base class for all JUnit test fixtures.
 */
public class TestBase {

	/**
	 * Null output stream. This is used when executing without an output stream, so no
	 * NullPointerExceptions are raised.
	 */
	protected static final PrintStream nullOutStream = new PrintStream(new OutputStream() {
		public void close() {}
		public void flush() {}
		public void write(byte[] b) {}
		public void write(byte[] b, int off, int len) {}
		public void write(int b) {}
	});
	
	/**
	 * Null input stream. This is used when testing, so no
	 * NullPointerExceptions are raised.
	 */
	protected static final InputStream nullInStream = new InputStream() {
		public int read() { return 0; }
	};
}