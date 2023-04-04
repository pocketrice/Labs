//(c) A+ Computer Science
// www.apluscompsci.com
//Name -


public class WordFun
{
	private String word;

	public WordFun(String w)
	{
		word = w;
	}

	public void processWords() {
		System.out.println(word);
		word = uppercasate();
		System.out.println(word);
		word = hyphenate();
		System.out.println(word);
	}
	public String uppercasate()
	{
		return word.toUpperCase();
	}

	public String hyphenate()
	{
		return word.replace(" ", "-");
	}
}