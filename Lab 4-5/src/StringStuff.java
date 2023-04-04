//(c) A+ Computer Science
//www.apluscompsci.com
//Name -

public class StringStuff
{
	private final String word;
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_CYAN = "\u001B[36m";

	public StringStuff( String w )
	{
		word = w;
	}

 	public String getFirstLastLetters()
 	{
 		return "" + word.charAt(0) + word.charAt(word.length()-1);
	}
	
 	public String getMiddleLetter()
	{
		return "" + word.charAt(word.length()/2);
	}	
	
 	public boolean sameFirstLastLetters()
 	{
		return word.charAt(0) == word.charAt(word.length() - 1); // this is weird, but it will eval to either true or false (so no need for if-else)
	}	

	public void wordAnalysis()
	{
		System.out.println(ANSI_BLUE + "WORD: " + word);
		System.out.println(ANSI_CYAN + "Ends of the string: " + getFirstLastLetters());
		System.out.println("Middle letter: " + getMiddleLetter());
		System.out.println("if (firstLetter == lastLetter)? " + sameFirstLastLetters() + "\n" + ANSI_RESET);
	}

 	public String toString()
 	{
 		return "" + word;
	}
}