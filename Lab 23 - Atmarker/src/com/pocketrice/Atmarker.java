package com.pocketrice;//(c) A+ Computer Science
//www.apluscompsci.com
//Name - lucas xie

import java.util.Arrays;
import java.util.Random;
import java.util.Set;

import static com.pocketrice.AnsiCode.*;
public class Atmarker
{
   private String[][] atMat;
   private int[] selCell;
   public double atmaRate;
   private long seed;

	public Atmarker()
	{
		seed = (long) (Math.random() * Integer.MAX_VALUE);
		Random rng = new Random(seed);
		atmaRate = 0.5;

		atMat = new String[8][8];
		for (int i = 0; i < atMat.length; i++) {
			for (int j = 0; j < atMat[0].length; j++) {
				atMat[i][j] = (rng.nextDouble() <= atmaRate) ? "@" : "-";
			}
		}

		selCell = new int[]{-1,-1};
	}

	public Atmarker(int rows, int cols, double ratio) {
		seed = (long) (Math.random() * Integer.MAX_VALUE);
		Random rng = new Random(seed);
		atmaRate = ratio;

		atMat = new String[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				atMat[i][j] = (rng.nextDouble() <= atmaRate) ? "@" : "-";
			}
		}


		selCell = new int[]{-1,-1};
	}

	public Atmarker(int rows, int cols, double atmaRate, long seed) {
		this.seed = seed;
		Random rng = new Random(seed);

		atMat = new String[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				atMat[i][j] = (rng.nextDouble() <= atmaRate) ? "@" : "-";
			}
		}
	}

	public int countAtmas(int r, int c, Set<Integer[]> checked)
	{
		// check self
		// add to checked
		// check trbl
		for (Integer[] coord : checked) if (coord[0] == r && coord[1] == c) return 0;

		if (r >= atMat.length || r < 0 || c >= atMat[0].length || c < 0) {
			return 0;
		}
		else {
			checked.add(new Integer[]{r,c});
		}



		return (atMat[r][c].equals("@") ? 1 + countAtmas(r+1,c, checked) + countAtmas(r-1,c,checked) + countAtmas(r,c+1,checked) + countAtmas(r,c-1,checked) : 0);
	}

	public int getRows() {
		return atMat.length;
	}

	public int getCols() {
		return atMat[0].length;
	}
	public long getSeed() {
		return seed;
	}

	public void setSelCell(int r, int c) {
		selCell = new int[]{r,c};
	}

	public void setSeed(long seed) {
		Atmarker other = new Atmarker(getRows(), getCols(), atmaRate, seed); // Regenerate atmat with seed
		this.seed = seed;
		this.atMat = other.atMat;
		selCell = new int[]{-1,-1};
	}


	public String toString()
	{
		// Print a view of the matrix and atmarker seed.
		String str = "";
		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < getCols(); j++) {
				str += ((i == selCell[0] && j == selCell[1]) ? ANSI_RED : ((atMat[i][j].equals("@")) ? ANSI_BLUE : ANSI_RESET)) + atMat[i][j] + "   " + ANSI_RESET;
			}
			str += "\n";
		}

		str += ANSI_PURPLE + "Seed: " + seed + "\t" + ANSI_CYAN + "Cell: " + ((selCell[0] < 0) ? "[?,?]" : Arrays.toString(selCell)) + ANSI_RESET;

		return str;
	}
}