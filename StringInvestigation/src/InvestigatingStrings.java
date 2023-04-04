public class InvestigatingStrings
  
{
  public static void main(String[] args)
  {
    //Uncomment each line of code one by one
    
    String greeting = "Hi World!";
    greeting = greeting.toLowerCase(); 
    System.out.println(greeting);
    
    greeting = "Hi World!";
    greeting = greeting.toUpperCase();
    System.out.println(greeting);

    String needsTrim = "  trim me!  ";
    System.out.println(needsTrim.trim());

    String aName = "Mat";
    String anotherName = "Mat";
    if (aName.equals(anotherName))
       System.out.println("the same");

    aName = "Mat";
    if (aName.equalsIgnoreCase("MAT"))
       System.out.println("the same");

    aName = "Mat";
    System.out.println(aName.compareTo("Rob"));
    System.out.println(aName.compareTo("Mat"));
    System.out.println(aName.compareTo("Amy"));
//    
    String word1, word2;
    word1 = new String("hello");
    word2 = new String("hello");
    System.out.println(word1 == word2);
    System.out.println(word1.equals(word2));
  }
}