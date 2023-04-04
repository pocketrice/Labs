import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;



public class Statistics {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    

    private File dataFile;
    private double mean, pVariance, sVariance, pStDev, sStDev, firstQuartile, secondQuartile, thirdQuartile, interquartileRange;
    private int[] range, numbers;
    private List<Integer> mode;
    private int totalCount, oddCount, evenCount, luckySevenCount; // Todo: build on this and add a bunch of useless but fun stats.


    public Statistics(int dataCount, int lowerBound, int upperBound) throws IOException, InterruptedException {
        dataFile = generateData(dataCount, lowerBound, upperBound);
        numbers = extractData(dataFile);
        Arrays.sort(numbers);

        totalCount = getMetrics()[0];
        oddCount = getMetrics()[1];
        evenCount = getMetrics()[2];
        luckySevenCount = getMetrics()[3];

        mean = getMean();
        mode = getMode();
        range = getRange(); // No assertion necessary b/c getRange() always returns 2-length array
        pStDev = getStandardDeviation(true);
        sStDev = getStandardDeviation(false);
        pVariance = getVariance(pStDev);
        sVariance = getVariance(sStDev);

        firstQuartile = getQuartiles()[0];
        secondQuartile = getQuartiles()[1];
        thirdQuartile = getQuartiles()[2];
        interquartileRange = getQuartiles()[3];
    }

    public Statistics(File df) throws IOException {
        dataFile = df;
        numbers = extractData(dataFile);
        Arrays.sort(numbers);

        totalCount = getMetrics()[0];
        oddCount = getMetrics()[1];
        evenCount = getMetrics()[2];
        luckySevenCount = getMetrics()[3];

        mean = getMean();
        mode = getMode();
        range = getRange();
        pStDev = getStandardDeviation(true);
        sStDev = getStandardDeviation(false);
        pVariance = getVariance(pStDev);
        sVariance = getVariance(sStDev);

        firstQuartile = getQuartiles()[0];
        secondQuartile = getQuartiles()[1];
        thirdQuartile = getQuartiles()[2];
        interquartileRange = getQuartiles()[3];
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Statistics stats = new Statistics(3000,0,100);
        System.out.println(ANSI_CYAN + "➢ Mean: " + stats.mean);
        System.out.println("  Mode(s): " + grammaticParse(stats.mode.toArray(), "and"));
        System.out.println("  Range: " + Arrays.toString(stats.range));
        System.out.println(ANSI_PURPLE + "➢ Standard Deviation (popu.): " + stats.pStDev);
        System.out.println("  Standard Deviation (samp.): " + stats.sStDev);
        System.out.println("  Variance (popu.): " + stats.pVariance);
        System.out.println("  Variance (samp.): " + stats.sVariance);
        System.out.println(ANSI_BLUE + "➢ Quartile I: " + stats.firstQuartile);
        System.out.println("  Quartile II: " + stats.secondQuartile);
        System.out.println("  Quartile III: " + stats.thirdQuartile);
        System.out.println("  Interquartile range: " + stats.interquartileRange);

        System.out.println(ANSI_GREEN + "\n(✓)" + ANSI_RESET + " Processed a total of " + stats.totalCount + " numbers (" + stats.evenCount + " even, " + stats.oddCount + " odd) with " + stats.luckySevenCount + " being lucky sevens!");
    }

    public static void fancyDelay(long delay, String loadMessage, String completionMessage, int iterations) throws InterruptedException { // Yoinked from SchudawgCannoneer
        int recursionCount = 0;
        System.out.print(loadMessage + " /");

        while (recursionCount < iterations) {
            TimeUnit.MILLISECONDS.sleep(delay);
            System.out.print("\b—");
            TimeUnit.MILLISECONDS.sleep(delay);
            System.out.print("\b\\");
            TimeUnit.MILLISECONDS.sleep(delay);
            System.out.print("\b|");
            TimeUnit.MILLISECONDS.sleep(delay);
            System.out.print("\b/");
            recursionCount++;
        }
        if (!completionMessage.isBlank()) System.out.print("\b\n" + completionMessage + "\n" + ANSI_RESET);
        else System.out.println();
    }

    public static <T> String grammaticParse(T[] array, String conjunction) { // Yoinked from Digitridoo
        StringBuilder gParsedString = new StringBuilder(Arrays.toString(array));

        gParsedString.deleteCharAt(0)
                .deleteCharAt(gParsedString.length()-1);

        if (array.length > 1) gParsedString.insert(gParsedString.lastIndexOf(",") + 1, " " + conjunction);

        return gParsedString.toString();
    }

    public double truncate(double value, int mantissaLength) // <+> APM
    {
        return BigDecimal.valueOf(value).setScale(mantissaLength, RoundingMode.HALF_EVEN).doubleValue();
    }

    public double getStandardDeviation(boolean isPopulationStDev) { // sqrt(Σ(x-μ)^2/N) OR sqrt(Σ(x-μ)^2/(N-1)) <-- only difference is N-1. Fun fact: this odd change is called Bessel's correction!
        assert (mean != 0) : ANSI_YELLOW + "(!) Warning: the mean may not have been calculated yet (current value: 0)!" + ANSI_RESET;
        assert (totalCount != 0) : ANSI_YELLOW + "(!) Warning: the total count may not have been set yet (current value: 0)!" + ANSI_RESET;

        double totalMeanDistSq = 0;

        for (int num : numbers) {
            totalMeanDistSq += Math.pow(num - mean, 2);
        }

        return (isPopulationStDev) ? truncate(Math.sqrt(totalMeanDistSq / totalCount), 2) : truncate(Math.sqrt(totalMeanDistSq / (totalCount - 1)), 2);
    }

    public List<Integer> getMode() {
        Map<Integer, Integer> numCounts = new HashMap<>();

        for (int num : numbers) {
            numCounts.put(num, (numCounts.containsKey(num)) ? numCounts.get(num) + 1 : 1);
        }

        int maxCount = numCounts.values().stream().max(Integer::compare).get(); // :: allows for calling the method without defining the objects to use. stream.max().get() compares each value to each other (based on a Comparator) and returns the max value.

        return numCounts.keySet().stream().filter(k -> numCounts.get(k) == maxCount).toList();
    }

    public int[] getRange() {
        return new int[]{numbers[0], numbers[numbers.length-1]};
    }

    public double getMean() {
        return truncate((double) Arrays.stream(numbers).sum() / numbers.length, 2);
    }

    public double getVariance(double stdev) { // Does not distinguish b/w population and sample; that must be determined/passed in manually.
        return truncate(Math.pow(stdev, 2), 2);
    }

    public File generateData(int dataCount, int lowerBound, int upperBound) throws IOException, InterruptedException {
        File dataFile = new File("src/gen_data.txt");
        if (!dataFile.createNewFile()) {
            System.out.println(ANSI_RED + "(!) File already exists! Overwriting " + dataFile.getName() + " (prev. " + getDataLength(dataFile) + " lines)." + ANSI_RESET);
            dataFile.delete();
        }

        fancyDelay(300, ANSI_CYAN + "❖ Writing new data file..." + ANSI_RESET, "", 4);

        try {
            FileWriter dataWriter = new FileWriter("src/gen_data.txt");

            for (int i = 0; i < dataCount; i++) {
                dataWriter.write((int) (Math.random() * (upperBound - lowerBound)) + lowerBound + "\n");
            }
            dataWriter.close();
            System.out.println(ANSI_RESET + "Wrote " + getDataLength(dataFile) + " data to file " + dataFile.getName() + "!\n\n" + ANSI_RESET);

        }
        catch (Exception e) {
            System.out.println(ANSI_RED + "(x) Error: " + e + ANSI_RESET);
            e.printStackTrace();
        }

        return dataFile;
    }

    public int[] getMetrics() {
        int[] metrics = {0,0,0,0}; // {total, odds, evens, lucky sevens}

        metrics[0] = numbers.length;
        for (int num : numbers) {
           if (num % 2 != 0) metrics[1]++;
           else metrics[2]++;
           if (num % 7 == 0) metrics[3]++;
        }

        return metrics;
    }

    public double[] getQuartiles() {
        // You may assume numbers is already sorted (low -> high). For quartile formulas, since they may produce a decimal, the two indices it is close to are averaged.
        double[] quartileMetrics = {0,0,0,0}; // {Q1, Q2, Q3, IQ range}
        assert (totalCount != 0) : "(!) Warning: the total count may not have been set yet (current value: 0)!";

        quartileMetrics[0] = ((double)(totalCount + 1)/4 % 1 == 0) ? numbers[(totalCount+1)/4] : (numbers[(totalCount+1)/4] + numbers[(int) Math.ceil((double)(totalCount+1)/4)]) / 2.0; // Q1 = (n+1)/4 term
        quartileMetrics[1] = ((double)(totalCount + 1)/2 % 1 == 0) ? numbers[(totalCount+1)/2] : (numbers[(totalCount+1)/2] + numbers[(int) Math.ceil((double)(totalCount+1)/2)]) / 2.0;// Q2 = (n+1)/2 term (median)
        quartileMetrics[2] = (3 * (double)(totalCount + 1)/4 % 1 == 0) ? numbers[3*(totalCount+1)/4] : (numbers[3*(totalCount+1)/4] + numbers[(int) Math.ceil(3*(double)(totalCount+1)/4)]) / 2.0; // Q3 = 3(n+1)/4 term
        quartileMetrics[3] = quartileMetrics[2] - quartileMetrics[0]; // Interquartile range = Q3 - Q1

        return quartileMetrics;
    }

    // NOTE: the file is NOT a dat. Thus the first line is not a data length indicator.
    public static int[] extractData(File file) throws IOException { // todo: write a generic extractData method. Due to project restrictions an array must be used, and so this is hard-coded for now.
        int lineIndex = 0;
        Scanner reader = new Scanner(new FileReader(file));
        
        int[] numbers = new int[getDataLength(file)];

        while (reader.hasNextInt()) {
            try {
                numbers[lineIndex] = reader.nextInt();
                lineIndex++;
            }
            catch (Exception e) {
                throw new IOException("(x) Unable to parse integer at line " + (lineIndex+1) + "!");
            }
        }

        return numbers;
    }

    public static int getDataLength(File file) throws FileNotFoundException { // NOT APM.
        Scanner reader = new Scanner(new FileReader(file));
        int dataCount = 0;

        while (reader.hasNextInt()) {
            reader.nextInt();
            dataCount++;
        }

        return dataCount;
    }

}
