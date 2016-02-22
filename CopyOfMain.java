/***
 * @author Andreas Loizou
 * Date:	22 February 2016
 * Version 0.000000001
 * Purpose: To join two files based on some keys just like joins in SQL
 * 
 * Assumptions
 * 1. Files have headers
 * 2. Files have at least one line (header)
 * 3. Files are consistent (no lines with no values, less colums etc)
 * 
 * Supports: 
 * 1. Multiple Keys
 * 2. many to many (files with the same key multiple times)
 * 
 * TODO
 * 1. Add Graphical User Interface
 * 2. Select Keys based on column Names
 * 3. Validate files before using them
 * 4. Find whether the files have unique keys or not
 * 5. Apply splitting based on several character (currently only pipe is implemented) and on different file
 * 6. Allow several types of joins
 * 7. Comment the file
 * 8. Escape characters when parsing files
 * 9. See whether files have headers
 * 
 * Wont Implement
 * 1. Selection of specific columns
 */

import java.util.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CopyOfMain {

	/*Parameters - To be read from outside*/
	static String delimiter = "\\|";
	static String delimiterToPrint = "|";

	enum JoinType {
		Inner
	};// not implemented right now, Left, Right, Outer};

	public static void main(String args[]) throws IOException {
		long startTime = System.currentTimeMillis();
		/*Parameters - To be read from outside*/
		String filename1 = "C:\\Users\\751909\\Desktop\\datasets\\Customers.txt";
		String filename2 = "C:\\Users\\751909\\Desktop\\datasets\\Accounts.txt";
		String outputFilename = "C:\\Users\\751909\\Desktop\\datasets\\output2.txt";
		Scanner c1 = new Scanner(new FileReader(new File(filename1)));
		Scanner c2 = new Scanner(new FileReader(new File(filename2)));
		String header1 = c1.nextLine();// Get header if it has
		String header2 = c2.nextLine();// Get header if it has

		ArrayList<Integer> keyIndexes1 = new ArrayList<Integer>();
		ArrayList<Integer> keyIndexes2 = new ArrayList<Integer>();
		// IMPORTANT:
		// The keys should appear in the same order in the two array lists
		keyIndexes1.add(1); // cust_id
		keyIndexes1.add(0); // obs_pt_date
		keyIndexes2.add(2); // cust_id
		keyIndexes2.add(1); // obs_pt_date
		HashMapList<String, String> M1 = new HashMapList<String, String>();
		HashMapList<String, String> M2 = new HashMapList<String, String>();
		add(c1, M1, keyIndexes1, filename1);
		add(c2, M2, keyIndexes2, filename2);
		// Better iterate on the smaller map?

		List<String> lines = new ArrayList<String>();
		lines.add(header1 + delimiterToPrint + header2);
		for (String key : M1.keySet()) {
			if (M2.containsKey(key)) {
				// System.out.println(key+": "+" "+M1.get(key)+" "+M2.get(key));
				ArrayList<String> values1 = M1.get(key);
				ArrayList<String> values2 = M2.get(key);
				for (String string1 : values1) {
					for (String string2 : values2) {
						lines.add(key + ":" + string1 + delimiterToPrint + string2);
					}
				}
			}
		}
		Path file = Paths.get(outputFilename);
		Files.write(file, lines, Charset.forName("UTF-8"));
		double endTime = System.currentTimeMillis();
		double totalTime = endTime - startTime;
		System.out.println(totalTime / 1000 + " seconds");
	}

	private static void add(Scanner c1, HashMapList<String, String> M,
			ArrayList<Integer> keys, String filename) {
		while (c1.hasNext()) {
			String line = c1.nextLine();
			String S[] = line.split(delimiter);
			// Exception should be thrown
			/*
			 * if (M.containsKey(concatenateKeys(S, keys))){
			 * System.out.println("ERROR! Key "+concatenateKeys(S,
			 * keys)+" was found multiple times in file "+filename);
			 * 
			 * }
			 */
			M.put(concatenateKeys(S, keys), line);
		}
	}

	private static String concatenateKeys(String[] line, ArrayList<Integer> keys) {
		StringBuilder sb = new StringBuilder();
		for (int key : keys) {
			sb.append(line[key]);
		}
		return sb.toString();
	}
}