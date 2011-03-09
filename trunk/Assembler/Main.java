package Assembler;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.LinkedList;

public class Main {

	/**
	 * Program entry point.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) throws Exception {
		// Print usage data if necessary
		if (args.length < 2 || args[0].equals("--help") || args[0].equals("/?")) {
			Main.printUsageInformation();
			return;
		}
		
		List<String> inputFiles = new LinkedList<String>();
		boolean generateListing = false;
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-l")) {
				// Generate a listing
				generateListing = true;
			}
			else if (!generateListing) {
				inputFiles.add(args[i]);
			}
			else {
				Main.printUsageInformation();
				return;
			}
		}
		
		if (inputFiles.size() == 0) {
			Main.printUsageInformation();
			return;
		}
		
		try {
			for (String inputFile : inputFiles) {
				String data = Main.readAllText(inputFile);
				Assembler assembler = new Assembler();
				Program program = assembler.assemble(inputFile, data);
				
				String result = program.getCode(generateListing);
				int dot = inputFile.lastIndexOf('.');
				if (dot == -1) {
					dot = inputFile.length();
				}
				Main.writeAllText(inputFile.substring(0, dot) + ".out", result);
			}
		}
		catch (IOException e) {
			System.out.println("Failed to assemble program due to an IO error.");
			return;
		}
		catch (Exception e) {
			if (e.getMessage() != null) {
				System.out.println(e.getMessage());
			} else {
				e.printStackTrace();
			}
			return;
		}
	}
	
	/**
	 * Prints usage information for users of this program.
	 */
	private static void printUsageInformation() {
		System.out.println("Usage:\tjava Assembler.Main inputfiles [options]");
		System.out.println("\tinputfiles\tSpecify path to input assembly files.");
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