//(c) A+ Computer Science
//www.apluscompsci.com
//Name - Lucas Xie

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static java.lang.System.*;
import static java.lang.Math.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;

public class Monsters
{
    private Monster[] monsterHorde;

    public Monsters(int size)
    {
        monsterHorde = new Monster[size];
    }

    public void set(int spot, Monster m) {
       monsterHorde[spot] = m;
    }

    public Monster[] getLargest()
    {
        Arrays.sort(monsterHorde);
        Set<Monster> largestMonsters = new HashSet<>();
        largestMonsters.add(monsterHorde[monsterHorde.length - 1]);

        for (int i = 0; i < monsterHorde.length; i++) {
            if (monsterHorde[i].getSize() == monsterHorde[monsterHorde.length - 1].getSize()) {
                largestMonsters.add(monsterHorde[i]);
                break;
            }
        }

        return largestMonsters.toArray(new Monster[0]);
    }

    public Monster[] getSmallest()
    {
        Arrays.sort(monsterHorde);
        Set<Monster> smallestMonsters = new HashSet<>();
        smallestMonsters.add(monsterHorde[0]);

        for (int i = 1; i < monsterHorde.length; i++) {
            if (monsterHorde[i].getSize() == monsterHorde[0].getSize()) {
                smallestMonsters.add(monsterHorde[i]);
                break;
            }
        }

        return smallestMonsters.toArray(new Monster[0]);
    }

    public void sortHorde() {
        Arrays.sort(monsterHorde);
    }

    @Override
    public String toString()
    {
        String hordeString = "";
        for (Monster monst : monsterHorde) {
            hordeString += monst.toString() + " (" + monst.getSize() + "), ";
        }
        return hordeString.substring(0, hordeString.length() - 2);
    }
}