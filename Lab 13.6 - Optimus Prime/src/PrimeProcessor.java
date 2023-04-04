//(c) A+ Computer Science
//www.apluscompsci.com
//Name - Lucas Xie

import java.io.IOException;
import java.io.File;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class PrimeProcessor
{
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";

	public static void main(String[] args) throws IOException, InterruptedException {
		int IOCount = 0;
		Scanner IOInput = new Scanner(new File("src/primes.dat"));
		int dataCount = IOInput.nextInt(); // The first line = total data count
		IOInput.nextLine(); // Skip to first line of actual data

		fancyDelay(300, ANSI_BLUE + "➢ I/O: Loading 'primes.dat'...", "Done!\n" + ANSI_RESET, 4);

		try {
			for (int i = 0; i < dataCount; i++) {
				Prime prnum = new Prime(IOInput.nextInt());
				System.out.println(prnum);
				IOCount++;
			}
		}
		catch (Exception e) {
			System.out.println(ANSI_RED + "(x) I/O: Error! " + e + ANSI_RESET);
		}

		fancyDelay(300, ANSI_BLUE + "\n➢ I/O: Wrapping up some loose ends...", "All set!\n" + ANSI_RESET, 4);

		System.out.println(ANSI_GREEN + "(✓) I/O: Successfully processed " + IOCount + " data!" + ANSI_RESET);
		System.out.println(ANSI_PURPLE + "❖ I/O closed.");
	}

	public static void fancyDelay(long delay, String loadMessage, String completionMessage, int iterations) throws InterruptedException { // Yoinked from SchudawgCannoneer
		int recursionCount = 0;
		System.out.print(loadMessage + " /");

		while (recursionCount < iterations) {
			TimeUnit.MILLISECONDS.sleep(delay);
			System.out.print("\b\u2014");
			TimeUnit.MILLISECONDS.sleep(delay);
			System.out.print("\b\\");
			TimeUnit.MILLISECONDS.sleep(delay);
			System.out.print("\b|");
			TimeUnit.MILLISECONDS.sleep(delay);
			System.out.print("\b/");
			recursionCount++;
		}
		System.out.print("\b\n" + completionMessage + "\n" + ANSI_RESET);
	}
}