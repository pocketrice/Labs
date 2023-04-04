//(c) A+ Computer Science
//www.apluscompsci.com
//Name - Lucas Xie

public class Prime
{
	private int number;
	private boolean isPrime;

	public Prime(int num) {
		number = num;
		isPrime = determinePrimeness(num);
	}

	public static boolean determinePrimeness(int num) { // Prime if only divisible by itself and 1.
		for (int i = 2; i < num; i++) {
			if ((double)num / i == (double)(num / i)) // Produces an integer
				return false;
		}

		return true;
	}

	public String toString()
	{
		return "" + number + " is " + ((isPrime) ? "" : "not ") + "prime.";
	}
}