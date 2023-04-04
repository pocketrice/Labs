//(c) A+ Computer Science
//www.apluscompsci.com
//Name -

import java.util.Scanner;

public class StringRunner
{
	public static void main ( String[] args )
	{
		Scanner keyboard = new Scanner(System.in);

		// Test inputs
		System.out.println("Analyzing some words...");
		StringStuff testA = new StringStuff("apluscompsci.com");
		testA.wordAnalysis();

		StringStuff testB = new StringStuff("statechamps");
		testB.wordAnalysis();

		StringStuff testC = new StringStuff("W");
		testC.wordAnalysis();


		// Custom input
		System.out.println("What's the next word to check? ");
		StringStuff word = new StringStuff(keyboard.next());
		System.out.println();
		word.wordAnalysis();
	}
}