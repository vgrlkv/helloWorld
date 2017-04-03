package by.myCalc;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MyFileReader {
	private String sDir;

	public static void main() {
	}

	public ArrayList<String> readData() {
		String inputLine = null;
		ArrayList<String> list = new ArrayList<String>();
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please provide the path to input file");
		sDir = scanner.nextLine();
		scanner.close();
		try (BufferedReader bReader = new BufferedReader(new FileReader(sDir))) {
			while ((inputLine = bReader.readLine()) != null) {
				list.add(inputLine);
			}

		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}

		return list;
	}

	public void writeData(ArrayList<String> arr) {
		sDir = sDir.replaceAll("input_", "output_");
		try (FileWriter fWriter = new FileWriter(sDir)) {
			for (String str : arr) {
				fWriter.write(str + System.getProperty("line.separator"));
			}
			fWriter.close();
			System.out.println("The results are in the " + sDir);
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
