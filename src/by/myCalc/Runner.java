package by.myCalc;

import java.io.IOException;
import java.util.ArrayList;
import java.text.ParseException;

public class Runner {
	public static void main(String[] args) {
		ArrayList<String> inputArr = new ArrayList<String>();
		ArrayList<String> outputArr = new ArrayList<String>();
		MyFileReader myFileReader = new MyFileReader();
		RPN myRPN = new RPN();
		EngineerCalc myEngineerCalc = new EngineerCalc();
		inputArr = myFileReader.readData();
		for (String inputLine : inputArr) {
			try {
				outputArr.add(myRPN.calculateRPN(myEngineerCalc.parse(inputLine), inputLine));
//				outputArr.add(myRPN.calculateRPN(myRPN.parse(inputLine), inputLine));
			} catch (ParseException ex) {
				System.out.println(ex.getMessage());
			}
		}

		myFileReader.writeData(outputArr);
	}

}
