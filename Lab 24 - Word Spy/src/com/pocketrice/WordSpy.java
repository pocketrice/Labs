package com.pocketrice;
//(c) A+ Computer Science
//www.apluscompsci.com
//Name - lucas xie

import java.util.*;

import org.javatuples.*;

import static com.pocketrice.AnsiCode.*;

public class WordSpy
{
    private String[][] m;
	private String word;
	private Pair<Integer[], Direction> matchLoc; // < cell, dir >
	private long seed;


	public enum Direction { // APM
        NONE(0,0,-1),
		UP(0,-1,0,"NORTH"),
		RIGHT(1,0,1,"EAST"),
		DOWN(0,1,2,"SOUTH"),
		LEFT(-1,0,3,"WEST"),
		UPRIGHT(1,-1,4,"NORTHEAST"),
		UPLEFT(-1,-1,5,"NORTHWEST"),
		DOWNRIGHT(1,1,6,"SOUTHEAST"),
		DOWNLEFT(-1,1,7,"SOUTHWEST");


		Direction(int x, int y, int index, String... aliases) {
			this.x = x;
			this.y = y;
            this.index = index;
			this.aliases = aliases;
		}

		public Direction getInstance(String alias) {
			for (Direction dir : Direction.values())
				if (Arrays.asList(dir.aliases).contains(alias)) return dir;
			return NONE;
		}

        public Direction getInstance(int index) {
            for (Direction dir : Direction.values())
                if (dir.index == index) return dir;
            return NONE;
        }

        @Override
        public String toString() {
            return aliases[0].charAt(0) + aliases[0].substring(1).toLowerCase();
        }

		private int x, y, index;
		private String[] aliases;
	}


    public WordSpy(int size, String str) {
		// Cuts the string into a 2D matrix of chars based on size.
		assert(str.length() == size * size);

		m = new String[size][size];
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				m[i][j] = str.substring(i * size + j, i * size + j + 1);

        word = "";
        matchLoc = new Pair<>(new Integer[]{-1,-1}, Direction.NONE);
    }

	public WordSpy(int size) {
		seed = (long) (Math.random() * Integer.MAX_VALUE);
		Random rng = new Random(seed);

		m = new String[size][size];
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				m[i][j] = String.valueOf((char) (rng.nextInt(26) + 'A'));

        word = "";
        matchLoc = new Pair<>(new Integer[]{-1,-1}, Direction.NONE);
	}

	public WordSpy(int size, long seed) {
		this.seed = seed;
		Random rng = new Random(seed);

		m = new String[size][size];
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				m[i][j] = String.valueOf((char) (rng.nextInt(26) + 'A'));

        word = "";
        matchLoc = new Pair<>(new Integer[]{-1,-1}, Direction.NONE);
	}

    public long getSeed() {
        return seed;
    }

    public Pair<Integer[], Direction> getMatchLoc() {
        return matchLoc;
    }

	public void setSeed(long seed) {
		WordSpy other = new WordSpy(this.m.length, seed); // Regenerate obj with seed
		this.seed = seed;
		this.m = other.m;
	}

    public void setWord(String str) {
        word = str;
    }

    public void reset() {
        word = "";
        matchLoc = new Pair<>(new Integer[]{-1,-1}, Direction.NONE);
    }

    public boolean isSpied(String word) {
        word = word.toUpperCase(); // Being lazy!!

        // (A) Skip checks if word is invalid (too long or invalid chars)
		if (word.length() > m.length || Arrays.stream(toStrCharArray(word)).noneMatch(s -> {
			for (String[] row : m)
				for (String str : row)
					if (str.equals(s)) return true;
			return false;

		})) return false;

		// (B) Search for potential starting positions (== first char of word)
		List<Integer[]> candidates = new ArrayList<>();

		for (int i = 0; i < m.length; i++)
			for (int j = 0; j < m.length; j++)
				if (m[i][j].equalsIgnoreCase(word.substring(0, 1)))
					candidates.add(new Integer[]{i, j});

		// (C) Check all directions of each candidate for any matches
		for (Integer[] cell : candidates) {
			for (Direction dir : Arrays.stream(Direction.values()).filter(d -> !d.equals(Direction.NONE)).toList())
				if (checkMatch(word, new int[]{cell[0], cell[1]}, dir))  {
                    matchLoc = new Pair<>(cell, dir);
                    return true;
                }
		}
		return false;
	}

	public boolean checkMatch(String str, int[] cell, Direction dir) {
        // Enforce valid bounds (short-circuit, so this won't eval an invalid bound) and ensure that all chars within word length in dir match up (basically, that the word is where it's supposed to be).
		for (int i = 0; i < str.length(); i++)
			if (cell[0] + dir.y * i < 0 || cell[0] + dir.y * i > m.length - 1 || cell[1] + dir.x * i < 0 || cell[1] + dir.x * i > m.length - 1 || !m[cell[0] + dir.y * i][cell[1] + dir.x * i].equalsIgnoreCase(str.substring(i,i+1))) return false;

		return true;
	}


	public static String[] toStrCharArray(String word) {
        // chars kinda suck (why can't I stream char[] and turn it into a string[]?? why??) so being lazy again :)
		String[] arr = new String[word.length()];

		for (int i = 0; i < word.length(); i++) {
			arr[i] = word.substring(i, i+1);
		}
		return arr;
	}
    public static String safeSubstring(String str, int a, int b) { // Adapted from safeIndex() [see Lab 10 - BBB]
        int maxIndex = str.length();

        a = Math.max(Math.min(a, maxIndex), 0);
        b = Math.min(b, maxIndex);

        return str.substring(a,b);
    }

    public static String safeSubstring(String str, int a) {
        int maxIndex = str.length();

        a = Math.min(a, maxIndex);

        return str.substring(a);
    }


    public void spyWords(String... words) {
        for (String word : words) {
            setWord(word);
            boolean isSpied = this.isSpied(word); // Must call isSpied() before to populate matchLoc. Probably a better idea to split that functionality as a separate method (e.g. findMatches) but being lazy ;p
            System.out.println("\n\n\n\n\n\n\n\n" + this);
            System.out.println(word.toUpperCase() + ((isSpied) ? " spied in word pool! " + ANSI_GREEN + "(Orientation: " + matchLoc.getValue1() + ")" : " not spied."));
            reset();
        }
    }


    @Override
    public String toString() {
        // Defining these as vectors might be preferred (for checking direction).
		String str = "";
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m.length; j++) {
                // (a) disp = 0 check
                // (a) equal component mags (this is a lazy patch for some diagonal bug i haven't any clue why occurs)
				// (b) same "arm" check? (elim easy invalids) -- do this via vector-like direction compare. A standard slope check (y/x) doesn't work because only one "arm" of that sloped line works. (really really hoping this actually works)
				// (c) check if out of word length (displacement mag).

                // Note: doubles are stupid so I had to spend 30+ minutes trying to figure out a discrepancy. That has been patched with a Math.round(), thankfully.
                int dispX = j - matchLoc.getValue0()[1];
                int dispY = i - matchLoc.getValue0()[0];
                Direction dir = matchLoc.getValue1();
                AnsiCode color;

                if (dispX == 0 && dispY == 0)
                    color = (m[i][j].equalsIgnoreCase(safeSubstring(word, 0,1))) ? ANSI_RED : ANSI_CYAN;
                else
                    color = ((dir.index <= 3 || Math.abs(dispX) == Math.abs(dispY)) && (dispX / ((dispX == 0) ? 1 : Math.abs(dispX)) == dir.x && dispY / ((dispY == 0) ? 1 : Math.abs(dispY)) == dir.y) && Math.round(Math.sqrt(Math.pow(dispX, 2) + Math.pow(dispY, 2)) / Math.sqrt(Math.pow(dir.x, 2) + Math.pow(dir.y, 2))) < word.length()) ? ANSI_YELLOW : ANSI_CYAN;

                str += color + m[i][j] + "   " + ANSI_RESET;
			}
			str += "\n";
		}

		str += ANSI_PURPLE + "Seed: " + ((seed == 0) ? "N/A" : seed) + "\t" + ANSI_CYAN + "Word: " + (word.isEmpty() ? "N/A" : word) + ANSI_RESET;

		return str;
    }


}
