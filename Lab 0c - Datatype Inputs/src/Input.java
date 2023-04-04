//(c) A+ Computer Science
//www.apluscompsci.com

//Name - Lucas Xie
//Date - 9/13/22
//Class - AP CSA P5
//Lab - 0c

import static java.lang.System.*; // System is imported so we can cut "System." out of our prints.
import java.util.Scanner;
import java.util.UUID;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Input
{
	static char responseConfirm; // Probably not the best way to fix this problem, but it's for a static/nonstatic issue
	static double finalPrice;
	static Scanner keyboard = new Scanner(System.in);


	// Note: all the \u001B[XXm escape codes are for ANSI colors
	public static void main(String[] args)
	{
		int intOne, intTwo;
		double doubleOne, doubleTwo;
		float floatOne, floatTwo;
		short shortOne, shortTwo;
		String purchaseId = UUID.randomUUID().toString().toUpperCase(); // https://www.baeldung.com/java-uuid
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss"); //https://www.baeldung.com/java-simple-date-format


		out.println("\n");
		out.println("\u001B[36mThank you valued customer for shopping at Generic Supermarket.");
		out.println("Before you are charged for your purchase, we would appreciate your feedback through a 'harmless' little survey.");
		out.println("Please answer honestly, as there are definitely no financial consequences to your responses.\u001B[0m");
		out.println();
		out.println("==========================================================================");
		out.print("How many purchases have you made at GSM? ");
		intOne = keyboard.nextInt();


		System.out.print("How many purchases do you foresee making at GSM in the next month? ");
		intTwo = keyboard.nextInt();


		System.out.print("How much money do you have? ");
		doubleOne = keyboard.nextDouble();

		System.out.print("Amount you'd love to win from a lotto? ");
		doubleTwo = keyboard.nextDouble();

		System.out.print("How much would you charge yourself for this purchase? ");
		floatOne = keyboard.nextFloat();

		System.out.print("How much would you charge your worst enemy for this purchase? ");
		floatTwo = keyboard.nextFloat();

		System.out.print("On a scale of " + Short.MIN_VALUE + " to " + Short.MAX_VALUE + ", how would you rate your shopping experience? ");
		shortOne = keyboard.nextShort();

		out.print("Pick your favorite number. ");
		shortTwo = keyboard.nextShort();

		finalPrice = (double)(intOne - intTwo + doubleOne + doubleTwo - floatOne + floatTwo * (shortOne / shortTwo));


		out.println("==========================================================================");
		out.println("\u001B[32mHere are your responses:");
		out.println("# of purchases: " + intOne);
		out.println("# of foreseen purchases: " + intTwo);
		out.printf("Balance: $" + "%.2f\n", doubleOne); // Cut it to only 2 decimal places
		out.printf("Lotto prediction: $" + "%.2f\n", doubleTwo);
		out.printf("Wanted price (self): $" + "%.2f\n", floatOne);
		out.printf("Wanted price (enemy): $" + "%.2f\n", floatTwo);
		out.println("Satisfaction: " + shortOne);
		out.println("Favorite #: " + shortTwo);
		out.println("\u001B[34m\nPurchase ID: " + purchaseId);
		out.println("Timestamp: " + java.time.LocalDate.now() + " " + formatter.format(new Date()) + "\u001B[0m"); // LocalTime.now() outputs a time with nanosec so we have to format it
		confirmationMessage();
	}


	public static void confirmationMessage()
	{
		out.println("\nIs this correct? (Y/N) ");
		responseConfirm = keyboard.next().charAt(0);
		keyboard.nextLine();

		if (responseConfirm == 'Y' || responseConfirm == 'y')
		{
			out.printf("\u001B[32mThank you for your feedback! Your purchase amounts to $" + "%.2f.\u001B[0m", finalPrice);
		}
		else if (responseConfirm == 'N' || responseConfirm == 'n')
		{
			out.print("\u001B[35mThank you for your participation. Try another purchase to do another survey.\u001B[0m");
			exit(0);
		}
		else
		{
			out.println("\u001B[31mError: invalid character. Permitted chars: Y, y, N, n.\u001B[0m");
			confirmationMessage();
		}
	}
}