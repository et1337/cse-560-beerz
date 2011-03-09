package Loader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.List;
import java.util.LinkedList;
import Common.MemoryBank;
import Common.SymbolTable;

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
		
		// IO stream for all program and trace output
		PrintStream printStream = System.out;
		
		List<String> inputFiles = new LinkedList<String>();
		
		try {
			boolean hasOutputFile = false;
			for (int i = 0; i < args.length; i++) {
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
					hasOutputFile = true;
				}
				else if (!hasOutputFile) {
					inputFiles.add(args[i]);
				}
				else {
					Main.printUsageInformation();
					return;
				}
			}
			
			if (!hasOutputFile) {
				Main.printUsageInformation();
				return;
			}
			
			// Load all the file data into a string
			List<String> fileData = new LinkedList<String>();
			for (String inputFile : inputFiles) {
				try {
					fileData.add(Main.readAllText(inputFile));
				}
				catch (IOException e) {
					System.out.println("Failed to open file \"" + inputFile + "\" for reading.");
					return;
				}
			}
			
			List<ObjectFile> objectFiles = new LinkedList<ObjectFile>();
			List<SymbolTable> symbolTables = new LinkedList<SymbolTable>();
			
			for (String file : fileData) {
				try {
					ObjectFile objectFile = Loader.load(file);
					objectFiles.add(objectFile);
					symbolTables.add(objectFile.getSymbols());
				}
				catch (Exception e) {
					printStream.println(e.getMessage());
					return;
				}
			}
			
			try {
				int i = 0;
				MemoryBank bank = null;
				int startAddress = 0;
				for (ObjectFile file : objectFiles) {
					if (i == 0) {
						startAddress = file.getStartAddress();
						bank = file.getMemoryBank();
					}
					else {
						MemoryBank other = file.getMemoryBank();
						SymbolTable symbols = file.getSymbols();
						symbols.relocate(0, bank.getLastAddress() + 1);
						other.resolveSymbols(symbolTables, file.getSymbolEntries());
						other.relocate(0, bank.getLastAddress() + 1, file.getRelocationRecords()).insertInto(bank);
					}
					i++;
				}
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
		System.out.println("Usage:\tjava Loader.Main [inputfiles] -o outfile");
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
	
	/**
	 * Writes all given text to the file existing at the given path location.
	 * If the file already exists, it is overwritten. If not, it is created.
	 * @param filename The filename of the file to write to.
	 * @param data The text to write to the file.
	 */
	private static void writeAllText(String filename, String data) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(filename));
		out.write(data);
		out.close();
    }
}