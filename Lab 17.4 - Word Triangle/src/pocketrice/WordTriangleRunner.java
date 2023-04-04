package pocketrice;//(c) A+ Computer Science
//www.apluscompsci.com
//Name - Lucas Xie

import java.util.Scanner;

import static pocketrice.AnsiCode.*;
public class WordTriangleRunner
{
	public static void main(String[] args)
	{
		while (true) {
			WordTriangle.printTriangle(prompt(ANSI_CYAN + "Input word for triangulation." + ANSI_RESET, false, true));

			switch (prompt(ANSI_BLUE + "\nContinue?" + ANSI_RESET, "Error: invalid choice.", new String[]{"yes", "no","y","n"}, false, false)) {
				case "yes", "y" -> {
					System.out.println("\n\n\n");
				}

				case "no", "n" -> {
					System.exit(0);
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
				System.out.println(AnsiCode.ANSI_RED + errorMessage + AnsiCode.ANSI_RESET);
			}

		}
	}

	public static String prompt(String message, boolean lineMode, boolean isCaseSensitive) // <+> APM
	{
		Scanner input = new Scanner(System.in);
		String nextInput;

		System.out.print(message);
		if (!message.equals(""))
			System.out.println();

		if (lineMode) {
			input.nextLine();
			nextInput = input.nextLine();
		} else {
			nextInput = input.next();
		}

		if (!isCaseSensitive) {
			nextInput = nextInput.toLowerCase();
		}

		return nextInput;
	}
}