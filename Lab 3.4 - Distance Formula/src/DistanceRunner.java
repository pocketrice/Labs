//(c) A+ Computer Science
//www.apluscompsci.com

//Name - Lucas Xie
//Date - 10/6/22
//Class - AP CSA P5
//Lab  - 3.4

import java.util.Scanner;
import static java.lang.System.*;
import static java.lang.Math.*;

public class DistanceRunner
{
	public static void main( String[] args )
	{
		// Premade inputs
		Distance sampleA = new Distance(1,1,2,1);
		sampleA.getDistance("Sample A");

		Distance sampleB = new Distance(1,1,-2,2);
		sampleB.getDistance("Sample B");

		Distance sampleC = new Distance(1,1,0,0);
		sampleC.getDistance("Sample C");

		// Custom inputs
		System.out.println("\nNow, feed me some custom data and I'll spit out a magic number!");
		Distance measuringStick = new Distance();
		measuringStick.getDistance();
	}
}