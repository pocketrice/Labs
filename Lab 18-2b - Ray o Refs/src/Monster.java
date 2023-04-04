//(c) A+ Computer Science
//www.apluscompsci.com
//Name - Lucas Xie

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.DoubleStream;

import static java.lang.System.*;

public class Monster implements Comparable<Monster>
{
    private final String[] radAdjectives = {"Exalted", "Spirited", "Honorable", "Mighty", "Chump", "Noble", "Valiant", "Cold-Hearted", "Sheepish", "Chilly", "Forgotten", "Ferocious", "Monster", "Modest", "Supreme", "Innocent", "Sleazy", "Oddball", "Grumbly", "Slab", "Lazy", "Forsaken", "Hungry"};
    private String name;
    private String title;
    private int size;

    public Monster()
    {
        name = "Nameless";
        size = 0;
    }

    public Monster(String n, int size)
    {
        name = n;
        title = weightedRandom(radAdjectives, new double[radAdjectives.length], true);
        this.size = size;
    }

    public String getName()
    {
        return name;
    }

    public int getSize()
    {
        return size;
    }

    @Override
    public int compareTo(Monster object) {
        return (this.size - ((Monster) object).size);
    }


    public boolean checkForSameName(Monster other)
    {
        return (this.name.equals(other.name));
    }

    public String toString()
    {
        return name + " the " + title;
    }

    public static <T> T weightedRandom(T[] choices, double[] weights, boolean autoEqualize)
    {
        double rng = Math.random();

        if (autoEqualize) {
            Arrays.fill(weights, 1.0 / choices.length);
        }

        assert (DoubleStream.of(weights).sum() != 1) : "Error: weightedRandom weights do not add up to 1 (= " + DoubleStream.of(weights).sum() + ")!";
        assert (choices.length == weights.length) : "Error: weightedRandom choice (" + choices.length + ") and weights (" + weights.length + ") array are not the same length!";

        for (int i = 0; i < weights.length; i++) {
            if (rng < weights[i])
                return choices[i];
            else
                rng -= weights[i];
        }

        return null;
    }
}