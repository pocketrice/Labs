//(c) A+ Computer Science
//www.apluscompsci.com
//Name - Lucas Xie
//Date - 9/28/22

public class CircleRunner
{
 public static void main( String[] args )
   {
  System.out.printf( "Circle area is :: %.2f\n" , Circle.area( 7.5 ) );
  System.out.printf( "Circle perimeter is :: %.2f\n" , Circle.perimeter( 7.5 ) );
  System.out.printf( "Circle area is :: %.2f\n" , Circle.area( 10 ) );
  System.out.printf( "Circle perimeter is :: %.2f\n" , Circle.perimeter( 10 ) );  

  System.out.println("\n\033[0;36mAnd two more test cases...\033[0;0m");
  circleInfo(101);
  circleInfo(99.952);
 }

 public static void circleInfo(double radius)
 {
  System.out.printf("Circle area is :: %.2f\n" , Circle.area(radius));
  System.out.printf("Circle perimeter is :: %.2f\n" , Circle.perimeter(radius));
 }
}