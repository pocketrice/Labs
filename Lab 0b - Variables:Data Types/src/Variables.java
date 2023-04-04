//(c) A+ Computer Science
//www.apluscompsci.com

//Name - Lucas Xie
//Date - 09/07/22
//Class - AP CSA P5
//Lab - 0b

public class Variables
{
	public static void main ( String[] args )
	{
		//define 1 variable of each of the
		//following data types
		//byte		short		int 		long
		//float		double
		//char      boolean		String


/* =====================================================
      NOTE: each variable's value represents the maximum positive limit for its corresponding data type (just for fun)
======================================================  */

		//integer variables
		byte byteOne = 127;
		short shortOne = 32767;
		int intOne = 2000000000;
		long longOne = 2147483647;


		//decimal variables
		float floatOne = 3.402823466E38f; // We have to specify that it's a float via 'f'
		double doubleOne = 1.7976931E308;

		//other integer types
		char charOne = '∆'; // Hint: single quotes = char, double quotes = string

		//other types
		boolean boolOne = true;
		String stringOne = "String Theory";


		// ANSI colors
		String ANSI_CYAN = "\u001B[36m";
		String ANSI_YELLOW = "\u001B[33m";
		String ANSI_GREEN = "\u001b[32m";
		String ANSI_MAGENTA = "\u001b[35m";

		//output your information here
		System.out.println("///////////////////////////////////");
		System.out.println("* Lucas Xie\t\t\t\t 09/07/22 *");
		System.out.println("*                                 *");
		System.out.println(ANSI_CYAN + "*         integer types           *");
		System.out.println("*                                 *");
		System.out.println("* 8 bit - byte = "+byteOne+"\t\t\t  *");
		System.out.println("* 16 bit - short = "+shortOne+"\t\t  *");
		System.out.println("* 32 bit - int = "+intOne+"\t\t  *");
		System.out.println("* 64 bit - long = "+longOne+"\t  *");
		System.out.println("*                                 *");
		System.out.println(ANSI_MAGENTA + "*                                 *");
		System.out.println("*         decimal types           *");
		System.out.println("*                                 *");
		System.out.println("* 32 bit - float = "+floatOne+"\t  *");
		System.out.println("* 64 bit - double = "+doubleOne+" *");
		System.out.println("*                                 *");
		System.out.println(ANSI_GREEN + "*                                 *");
		System.out.println("*      other integer types        *");
		System.out.println("*                                 *");
		System.out.println("* 16 bit unsigned - char = "+charOne+"\t  *");
		System.out.println("*                                 *");
		System.out.println(ANSI_YELLOW + "*                                 *");
		System.out.println("*          other types            *");
		System.out.println("*                                 *");
		System.out.println("* 1 bit - boolean = "+boolOne+"\t\t  *");
		System.out.println("* ?? bit - string = "+stringOne+" *");
		System.out.println("///////////////////////////////////");











	}
}