//(c) A+ Computer Science 
//www.apluscompsci.com

//Name - Lucas Xie
//Date - 10/6/22
//Class - AP CSA P5
//Lab  - 3.4

import java.util.Scanner;

import static java.lang.Double.parseDouble;
import static java.lang.System.*;
import static java.lang.Math.*;

public class Distance
{
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_RESET = "\u001B[0m";

	Scanner input = new Scanner(System.in);
	private int xOne,yOne,xTwo,yTwo;
	private double distance;

	public Distance() // Default constructor
	{
		xOne = 0;
		yOne = 0;
		xTwo = 0;
		yTwo = 0;
	}

	public Distance(int x1, int y1, int x2, int y2) // Initializer constructor
	{
		xOne = x1;
		yOne = y1;
		xTwo = x2;
		yTwo = y2;
	}
	public void getDistance() // Custom inputs
	{
		xOne = (int)prompt(ANSI_BLUE + "Type in the value of x\u2081. " + ANSI_RESET,"Error: invalid value.", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		yOne = (int)prompt(ANSI_BLUE + "Now type in the value of y\u2081. " + ANSI_RESET, "Error: invalid value.", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

		xTwo = (int)prompt(ANSI_YELLOW + "\nType in the value of x\u2082. " + ANSI_RESET,"Error: invalid value.", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		yTwo = (int)prompt(ANSI_YELLOW + "Now type in the value of y\u2082. " + ANSI_RESET, "Error: invalid value.", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

		distance = Math.sqrt(Math.pow(xTwo - xOne, 2) + Math.pow(yTwo - yOne, 2));
		System.out.printf(ANSI_GREEN + "\nPRESTO! The magic number is %.3f.\n" + ANSI_RESET, distance);
	}

	public void getDistance(String dataName) // Preset inputs
	{
		distance = Math.sqrt(Math.pow(xTwo - xOne, 2) + Math.pow(yTwo - yOne, 2));
		System.out.printf(ANSI_GREEN + "The magic number is %.3f for data set " + dataName + ".\n" + ANSI_RESET, distance);
	}


	public double prompt(String message, String errorMessage, double min, double max) // Stolen from my piggy bank project
	{
		double nextInput = 0;
		boolean isValid;

		while (true) {
			if (!(message.matches("NO_MESSAGE"))) {
				System.out.print(message);
			}

			try {
				nextInput = parseDouble(input.next());
				input.nextLine();
				isValid = true;
			}

			catch (Exception e) {
				isValid = false;
			}

			if (nextInput > min && nextInput < max && isValid)
			{
				return nextInput;
			}
			else
			{
				System.out.println(ANSI_RED + errorMessage + ANSI_RESET);
			}
		}
	}
}