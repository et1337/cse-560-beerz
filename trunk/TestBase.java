import java.io.PrintStream;
import java.io.OutputStream;

public class TestBase {
	protected static final PrintStream nullStream = new PrintStream(new OutputStream() {
		public void close() {}
		public void flush() {}
		public void write(byte[] b) {}
		public void write(byte[] b, int off, int len) {}
		public void write(int b) {}
	});
}