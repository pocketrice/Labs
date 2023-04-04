package pocketrice;//(c) A+ Computer Science
//www.apluscompsci.com
//Name - Lucas Xie

public class WordTriangle
{
	//instance variables and constructors could be present, but are not necessary
	public static void printTriangle(String word)
	{
		for (int i = 0; i <= word.length(); i++) {
			for (int j = 0; j < i; j++) {
				System.out.print(word.substring(0,i));
			}
			System.out.println();
		}
	}
}