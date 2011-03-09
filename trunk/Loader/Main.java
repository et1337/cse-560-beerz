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
import Common.ByteOperations;

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
		PrintStream outputStream = null;
		
		List<String> inputFiles = new LinkedList<String>();
		
		boolean generateListing = false;
		
		int origin = 0;
		
		try {
			boolean hasOutputFile = false;
			for (int i = 0; i < args.length; i++) {
				if (args[i].equals("-o")) {
					// Set up an output file
					i++;
					if (i < args.length) {
						try {
							outputStream = new PrintStream(args[i]);
						}
						catch (IOException e) {
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
				else if (args[i].equals("-l")) {
					generateListing = true;
				}
				else if (args[i].equals("-a")) {
					i++;
					if (i < args.length) {
						try {
							origin = ByteOperations.parseHex(args[i]);
						}
						catch (Exception e) {
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
			
			String segmentName = "      ";
			int startAddress = 0;
			
			int address = origin;
			int i = 0;
			for (String file : fileData) {
				try {
					ObjectFile objectFile = Loader.load(file);
					if (address != 0) {
						if (objectFile.isRelocatable())
							objectFile.relocate(0, address);
						else
							throw new Exception("Object file \"" + file + "\" was expected to be relocatable, but is not.");
					}
					if (i == 0) {
						startAddress = objectFile.getStartAddress();
						segmentName = objectFile.getSegmentName();
					}
					address = objectFile.getMemoryBank().getLastAddress() + 1;
					objectFiles.add(objectFile);
					symbolTables.add(objectFile.getSymbols());
				}
				catch (Exception e) {
					printStream.println(e.getMessage());
					return;
				}
				i++;
			}
			
			try {
				MemoryBank result = new MemoryBank();
				for (ObjectFile file : objectFiles) {
					MemoryBank bank = file.getMemoryBank();
					bank.resolveSymbols(symbolTables, file.getSymbolEntries());
					bank.insertInto(result);
				}
				// If we relocated any files, make sure they don't span multiple memory pages
				if (objectFiles.size() > 1 && (result.getFirstAddress() & 0xFE00) != (result.getLastAddress() & 0xFE00)) {
					throw new Exception("Resulting program would span multiple memory pages. " +
						" First address: " + ByteOperations.getHex(result.getFirstAddress(), 4) + 
						" Last address: " + ByteOperations.getHex(result.getLastAddress(), 4));
				}
				String header = "H" + segmentName + ByteOperations.getHex(result.getFirstAddress(), 4) + ByteOperations.getHex(1 + result.getLastAddress() - result.getFirstAddress(), 4);
				String textRecords = result.getRecords();
				String end = "E" + ByteOperations.getHex(startAddress, 4);
				if (generateListing) {
					printStream.println(header);
					printStream.print(textRecords);
					printStream.println(end);
				}
				outputStream.println(header);
				outputStream.print(textRecords);
				outputStream.print(end);
			}
			catch (Exception e) {
				printStream.println(e.getMessage());
				return;
			}
		}
		finally {
			// No matter what happens, close our output stream if it's open.
			outputStream.close();
		}
	}
	
	/**
	 * Prints usage information for users of this program.
	 */
	private static void printUsageInformation() {
		System.out.println("Usage:\tjava Loader.Main [inputfiles] -o outfile [options]");
		System.out.println("\t-l\tGenerate listing");
		System.out.println("\t-a addr\tRelocate program to addr (4-digit hex memory address)");
		System.out.println("Note: if linking an absolute object file, it must come first in the file list.");
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