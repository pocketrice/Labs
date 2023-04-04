//(c) A+ Computer Science
// www.apluscompsci.com
//Name -

import static java.lang.System.*;

public class WordFunRunner
{
	public static void main( String args[] )
	{
	   WordFun world = new WordFun("Hello World");
	   world.processWords();

	   //add more test cases
		WordFun Bob = new WordFun("Jim Bob");
		WordFun CS = new WordFun("Computer Science");
		WordFun Scramble = new WordFun("UIL TCEA");
		WordFun Winner = new WordFun("State Champions");

		Bob.processWords();
		CS.processWords();
		Scramble.processWords();
		Winner.processWords();
	}
}