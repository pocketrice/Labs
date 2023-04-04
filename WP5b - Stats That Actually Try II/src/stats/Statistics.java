// WP5b - Stats That Actually Try Stuff (STATS) Rewrite - Lucas Xie 1/12/23 AP CSA P5

package stats;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;



public class Statistics { // Statistics runs stats operations on each Datafile.

    // ANSI codes used in the console for colored text.
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";



    Datafile df;

    public Statistics(Datafile df) {
        this.df = df;
    }

    // Truncates a value to a specified mantissa length (# of decimal places).
    // @param value - any double
    // @param mantissaLength - non-negative integer
    // @return a double truncated to a specified mantissa length
    public static double truncate(double value, int mantissaLength) // <+> APM
    {
        return BigDecimal.valueOf(value).setScale(mantissaLength, RoundingMode.HALF_EVEN).doubleValue();
    }


    // Calculates the population and/or sample standard deviation.
    // @param isPopulationStDev - true for popu stdev, false for sample stdev
    // @return respective standard deviation of datafile
    public double getStandardDeviation(boolean isPopulationStDev) { // sqrt(Σ(x-μ)^2/N) OR sqrt(Σ(x-μ)^2/(N-1)) <-- only difference is N-1. Fun fact: this odd change is called Bessel's correction.
        assert (df.mean != 0) : ANSI_YELLOW + "(!) Warning: the mean may not have been calculated yet (current value: 0)!" + ANSI_RESET;
        assert (df.totalCount != 0) : ANSI_YELLOW + "(!) Warning: the total count may not have been set yet (current value: 0)!" + ANSI_RESET;

        double totalMeanDistSq = 0;

        for (int num : df.numbers) {
            totalMeanDistSq += Math.pow(num - df.mean, 2);
        }

        return (isPopulationStDev) ? truncate(Math.sqrt(totalMeanDistSq / df.totalCount), 2) : truncate(Math.sqrt(totalMeanDistSq / (df.totalCount - 1)), 2);
    }



    // Finds the mode(s) for the Datafile.
    // @param N/A
    // @return list of modes for datafile
    public List<Integer> getMode() {
        Map<Integer, Integer> numCounts = new HashMap<>();

        for (int num : df.numbers) {
            numCounts.put(num, (numCounts.containsKey(num)) ? numCounts.get(num) + 1 : 1);
        }

        int maxCount = numCounts.values().stream().max(Integer::compare).get(); // :: allows for calling the method without defining the objects to use. stream.max().get() compares each value to each other (based on a Comparator) and returns the max value.

        return numCounts.keySet().stream().filter(k -> numCounts.get(k) == maxCount).toList();
    }


    // Finds the range for the datafile, inclusive.
    // @param N/A
    // @return range of datafile, inclusive
    public int[] getRange() {
        return new int[]{df.numbers[0], df.numbers[df.numbers.length-1]};
    }


    // Finds the mean for the datafile.
    // @param N/A
    // @return mean of datafile
    public double getMean() {
        return truncate((double) Arrays.stream(df.numbers).sum() / df.numbers.length, 2);
    }


    // Finds the variance of the datafile (works for both popu. and sample stdev)
    // @param stdev - (any) standard deviation of datafile
    // @return respective variance
    public double getVariance(double stdev) { // Does not distinguish b/w population and sample; that must be determined/passed in manually.
        return truncate(Math.pow(stdev, 2), 2);
    }


    // Gets various metrics about the datafile.
    // @param N/A
    // @return array of metrics
    public int[] getMetrics() {
        int[] metrics = {0,0,0,0}; // {total, odds, evens, lucky sevens}

        metrics[0] = df.numbers.length;
        for (int num : df.numbers) {
           if (num % 2 != 0) metrics[1]++;
           else metrics[2]++;
           if (num % 7 == 0) metrics[3]++;
        }

        return metrics;
    }

    // Calculates the four quartiles for the datafile.
    // @param N/A
    // @return array of four quartilic metrics
    public double[] getQuartiles() {
        // You may assume numbers is already sorted (low -> high). For quartile formulas, since they may produce a decimal, the two indices it is close to are averaged.
        double[] quartileMetrics = {0,0,0,0}; // {Q1, Q2, Q3, IQ range}
        assert (df.totalCount != 0) : "(!) Warning: the total count may not have been set yet (current value: 0)!";

        quartileMetrics[0] = ((double)(df.totalCount + 1)/4 % 1 == 0) ? df.numbers[(df.totalCount+1)/4] : (df.numbers[(df.totalCount+1)/4] + df.numbers[(int) Math.ceil((double)(df.totalCount+1)/4)]) / 2.0; // Q1 = (n+1)/4 term
        quartileMetrics[1] = ((double)(df.totalCount + 1)/2 % 1 == 0) ? df.numbers[(df.totalCount+1)/2] : (df.numbers[(df.totalCount+1)/2] + df.numbers[(int) Math.ceil((double)(df.totalCount+1)/2)]) / 2.0;// Q2 = (n+1)/2 term (median)
        quartileMetrics[2] = (3 * (double)(df.totalCount + 1)/4 % 1 == 0) ? df.numbers[3*(df.totalCount+1)/4] : (df.numbers[3*(df.totalCount+1)/4] + df.numbers[(int) Math.ceil(3*(double)(df.totalCount+1)/4)]) / 2.0; // Q3 = 3(n+1)/4 term
        quartileMetrics[3] = quartileMetrics[2] - quartileMetrics[0]; // Interquartile range = Q3 - Q1

        return quartileMetrics;
    }
}
