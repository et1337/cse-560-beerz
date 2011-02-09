import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;

public class Main {
	/**
	 * Program entry point.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		// Print usage data if necessary
		if (args.length < 1 || args[0].equals("--help")) {
			Main.printUsageInformation();
			return;
		}
		try {
			String data = Main.readAllText(args[0]);
			Parser parser = new Parser();
			Program program = parser.parse(data);
			program.printListing();
		}
		catch (IOException e) {
			System.out.println("Failed to open file \"" + args[0] + "\".");
			return;
		}
	}
	
	/**
	 * Prints usage information for users of this program.
	 */
	private static void printUsageInformation() {
		System.out.println("Usage:\tjava Main inputfile [options]");
		System.out.println("\t-o outputfile\tSpecify name of output object file.");
		System.out.println("\t-l\t\tGenerate and display source code listing.");
	}
	
	/**
	 * Reads all text in the file existing at the given path location into a string.
	 * @param filename Path to the desired file.
	 * @return A string containing all the data existing in the desired file.
	 */
	private static String readAllText(String filename) throws IOException {
        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
		int numRead = 0;
		String line = null;
		while ((line = reader.readLine()) != null) {
			fileData.append(line);
			fileData.append("\n");
		}
        reader.close();
        return fileData.toString();
    }
}