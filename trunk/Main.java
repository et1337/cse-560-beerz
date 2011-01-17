import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import state.MemoryBank;

public class Main {
	/**
	 * Program entry point.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		// Print usage data if necessary
		if(args.length != 1 || args[0] == "--help") {
			Main.printUsageInformation();
			return;
		}
		
		// Path to the object file to load
		String filename = args[0];
		
		MemoryBank memory = new MemoryBank();
		
		// Load all the file data into a string
		String fileData = "";
		try {
			fileData = Main.readAllText(filename);
		}
		catch (IOException e) {
			System.out.println("Failed to open file \"" + filename + "\" for reading.");
		}
		
		// Load the file data into the memory bank
		int startAddress = 0;
		try {
			startAddress = Loader.load(fileData, memory);
			Machine machine = new Machine(memory);
			machine.run(startAddress);
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * Prints usage information for users of this program.
	 */
	private static void printUsageInformation() {
		System.out.println("Usage: java Main filename");
	}
	
	/**
	 * Reads all text in the file existing at the given path location into a string.
	 * @param filename Path to the desired file.
	 * @return A string containing all the data existing in the desired file.
	 */
	private static String readAllText(String filename) throws IOException {
        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
		char[] chars = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(chars)) > -1) {
			fileData.append(String.valueOf(chars));
		}
        reader.close();
        return fileData.toString();
    }
}