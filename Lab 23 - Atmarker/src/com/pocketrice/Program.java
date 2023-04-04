package com.pocketrice;//(c) A+ Computer Science
//www.apluscompsci.com
//Name - lucas xie

import java.util.HashSet;
import java.util.Scanner;

import static com.pocketrice.AnsiCode.*;

public class Program
{
	public static void main(String[] args)
	{
		Atmarker atma = new Atmarker(8,8, /* ratio: */ 0.5); // *** HEY! *** <-- Tweak this atmark (@) value to your liking. Higher = larger clusters, lower = smaller/more clusters.
		System.out.println(atma);

		while (true) {
			int ccol = (int) prompt("Select x-coord » (?,?)", "Error: must be within [0,0] and [" + (atma.getRows() - 1) + "," + (atma.getCols() - 1) + "]!", 0, atma.getRows() - 1, true);
			int crow = (int) prompt("Select y-coord » (" + ccol + ",?)", "Error: must be within [0,0] and [" + (atma.getRows() - 1) + "," + (atma.getCols() - 1) + "]!", 0, atma.getCols() - 1, true);
			atma.setSelCell(crow, ccol);
			System.out.println("\n\n\n\n\n\n\n\n\n\n" + atma + "\nCluster size: " + atma.countAtmas(crow, ccol, new HashSet<>()));

			switch (prompt("\n\nDone. Next?\n" + ANSI_BLUE + "(Input CONTINUE, QUIT, or SEED to set seed.)" + ANSI_RESET, "Error: invalid choice.\n\n", new String[]{"continue", "quit", "seed"}, false, false)) {
				case "continue" -> {
					atma.setSelCell(-1,-1);
					System.out.println("\n\n\n\n\n\n\n" + atma);
				}

				case "quit" -> {
					System.exit(0);
				}

				case "seed" -> {
					atma.setSeed((long) prompt("Set the new seed (current: " + atma.getSeed() + ").", "Error: invalid number. Due to limitations, it must be an int.\n\n", Integer.MIN_VALUE, Integer.MAX_VALUE, true));
					System.out.println(ANSI_GREEN + "Done!\n\n\n\n\n\n\n" + ANSI_RESET);
					System.out.println(atma);
				}
			}
		}
	}

	public static String prompt(String message, String errorMessage, String[] bounds, boolean lineMode, boolean isCaseSensitive) // <+> APM
	{
		Scanner input = new Scanner(System.in);
		String nextInput;

		while (true)
		{
			System.out.print(message);
			if (!message.equals(""))
				System.out.println();

			if (lineMode) {
				input.nextLine();
				nextInput = input.nextLine();
			}
			else {
				nextInput = input.next();
			}

			if (!isCaseSensitive)
			{
				nextInput = nextInput.toLowerCase();

				for (int i = 0; i < bounds.length; i++)
					bounds[i] = bounds[i].toLowerCase();
			}

			if (nextInput.matches(String.join("|", bounds)) || bounds[0].equals("")) {
				return nextInput;
			} else {
				System.out.println(ANSI_RED + errorMessage + ANSI_RESET);
			}

		}
	}

	public static double prompt(String message, String errorMessage, double min, double max, boolean isIntegerMode)
	{
		Scanner input = new Scanner(System.in);
		String nextInput;
		double parsedInput = 0;
		boolean isValid;

		while (true) {
			System.out.print(message);
			if (!message.equals(""))
				System.out.println();

			nextInput = input.next();
			try {

				if (!isIntegerMode) {
					parsedInput = Double.parseDouble(nextInput);
				} else {
					parsedInput = Integer.parseInt(nextInput);
				}

				input.nextLine();
				isValid = true;
			} catch (Exception e) {
				isValid = false;
			}

			if (parsedInput >= min && parsedInput <= max && isValid) {
				return parsedInput;
			} else {
				System.out.println(ANSI_RED + errorMessage + ANSI_RESET);
			}
		}
	}
}