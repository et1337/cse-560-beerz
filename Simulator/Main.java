package Simulator;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import Common.MemoryBank;
import Simulator.program.Loader;
import Simulator.program.Machine;
import Simulator.program.ExecutionMode;

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
		
		// Default run mode: quiet
		ExecutionMode mode = ExecutionMode.QUIET;
		
		// IO stream for all program and trace output
		PrintStream printStream = System.out;
		
		try {
			for (int i = 1; i < args.length; i++) {
				if (args[i].equals("-o")) {
					// Set up an output file
					i++;
					if(i < args.length) {
						try {
							printStream = new PrintStream(args[i]);
						} catch (IOException e) {
							System.out.println("Failed to open file \"" + args[i] + "\" for writing.");
							return;
						}
					}
					else {
						Main.printUsageInformation();
						return;
					}
				}
				else if (args[i].equals("-r")) {
					// Set the execution mode
					i++;
					if (i < args.length) {
						String modeString = args[i].toLowerCase();
						if (modeString.equals("quiet")) {
							mode = ExecutionMode.QUIET;
						}
						else if (modeString.equals("trace")) {
							mode = ExecutionMode.TRACE;
						}
						else if (modeString.equals("step")) {
							mode = ExecutionMode.STEP;
						}
						else {
							Main.printUsageInformation();
							return;
						}
					}
					else {
						Main.printUsageInformation();
						return;
					}
				}
				else {
					Main.printUsageInformation();
					return;
				}
			}
			
			// Don't let the user try to run in step mode and use an output file.
			// They would have a blank screen waiting for them to press a key for each instruction.
			if (printStream != System.out && mode == ExecutionMode.STEP) {
				System.out.println("Executing in step mode with an output file is not allowed.");
				return;
			}
			
			// Path to the object file to load
			String filename = args[0];
			
			// Load all the file data into a string
			String fileData = "";
			try {
				fileData = Main.readAllText(filename);
			}
			catch (IOException e) {
				System.out.println("Failed to open file \"" + filename + "\" for reading.");
				return;
			}
			
			
			int startAddress = 0;
			try {
				// Load the file data into the memory bank
				MemoryBank memory = new MemoryBank();
				startAddress = Loader.load(fileData, memory);
				
				// Run it!
				Machine machine = new Machine(printStream, memory);
				machine.run(startAddress, mode);
			}
			catch (Exception e) {
				printStream.println(e.getMessage());
				return;
			}
		}
		finally {
			// No matter what happens, close our output stream if it's open.
			if (printStream != System.out) {
				printStream.close();
			}
		}
	}
	
	/**
	 * Prints usage information for users of this program.
	 */
	private static void printUsageInformation() {
		System.out.println("Usage:\tjava Simulator.Main inputfile [options]");
		System.out.println("\t-o outputfile\tRedirect output to specified file.");
		System.out.println("\t-r quiet\tRun the program in quiet mode.");
		System.out.println("\t-r trace\tRun the program in trace mode.");
		System.out.println("\t-r step\tRun the program in step mode.");
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