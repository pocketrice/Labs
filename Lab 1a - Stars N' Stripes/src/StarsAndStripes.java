//� A+ Computer Science
//www.apluscompsci.com

//Name - Lucas Xie
//Date - 9/14/22
//Class - AP CSA P5
//Lab  - 1a

import static java.lang.System.*;

public class StarsAndStripes
{
   public void Title()
   {
      out.println("\u001b[33mHere's some free stars n' stripes!");
      printTwoBlankLines();
   }

   public void printTwentyStars()
   {
      out.println("*".repeat(20));
   }

   public void printTwentyDashes()
   {
      out.println("\u001b[31m" + "-".repeat(20) + "\u001b[0m");
   }

   public void printTwoBlankLines()
   {
      out.println();
      out.println();
   }
   
   public void printASmallBox()
   {
      printTwentyDashes();
      printTwentyStars();
      printTwentyDashes();
      printTwentyStars();
      printTwentyDashes();
      printTwentyStars();
      printTwentyDashes();
   }
 
   public void printABigBox()
   {
      printASmallBox();
      printASmallBox();
   }   
}