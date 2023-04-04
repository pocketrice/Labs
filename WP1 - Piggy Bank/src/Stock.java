import java.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.concurrent.TimeUnit;

public class Stock implements Runnable {
    private double initialValue, growthRate, value;
    private List<Double> stockHistory;
    private int numOfStocks;
    private String scope;
    private String fourScore; // Instead of sAbbrev, it's sFourScore b/c it rhymes
    static Stock[] allStocks;
    PiggyBank tempBank = new PiggyBank();



    public Stock(double iV, int nOS, double gR, String sc, String fs)
    {
        initialValue = iV;
        numOfStocks = nOS;
        growthRate = gR;
        scope = sc; // fix this right now or something
        fourScore = fs;

        stockHistory = new ArrayList<>();
        value = initialValue;
    }

    public Stock(String sc, String fourScore, double[] skews)
    {
        initialValue = truncate(skewedRandom(skews[0]) * 1000 + 500, 2); // iV [500, 1500)
        numOfStocks = (int)(skewedRandom(skews[1]) * 300) + 25; // nOS [25, 325)
        growthRate = truncate(skewedRandom(skews[2]) * 5 - 2.5, 2); // gR [-2.5, 2.5)
        scope = sc;
        this.fourScore = fourScore;

        stockHistory = new ArrayList<>();
        value = initialValue;
    }

    public double skewedRandom(double skew)
    {
        while (true) {
            long seed = System.currentTimeMillis(); // pretty useless but this may come in handy later
            Random rng = new Random(seed);
            double randomDouble = rng.nextDouble();

            if (randomDouble + skew <= 1 && randomDouble + skew >= 0) { // "Clamp off" the extended bit of the skewed random (see below for e.g.)
                return randomDouble;
            }
        }
        // scope = 10, skew = 2, offset = 0
        // Goal: [0, 10) skewed to 2
        // Skewed random: [2, 12)

        // Clamp off the (10, 12) bit.

    }

    public boolean probability(double[] probabilities, int focusIndex)
    {
        return true;
    }

    public double truncate(double value, int mantissaLength)
    {
        return BigDecimal.valueOf(value).setScale(mantissaLength, RoundingMode.HALF_EVEN).doubleValue();
    }

    public String formatCurrency(double value)
    {
        return String.format("%.2f", value);
    }

    public static void main(String[] args) throws InterruptedException {
        Stock QuarterlyAndPenniless500 = new Stock("0.1+,0.2-", "QPFH", new double[]{0.3, 0.5, 0.01});
        Stock GameCorner = new Stock(200, 300, 1.9, "0.5+,1.3-", "GMCR");
        Stock BirdTechnicalInstitute = new Stock(120, 200, 1.2, "0.3+,0.2-", "BTHI");
        Stock SpamInc = new Stock(600, 200, 1.05, "0.1+,0.05-", "SINC");
        Stock WariosWarGyros = new Stock("0.3+,0.2-","WWRG", new double[]{0.1, -0.2, 0.01});
        Stock Hogcoin = new Stock(50, 500, 3, "0.9+,1.1-", "HGCN");
        Stock PonziPyramid = new Stock("1.5+,7-", "PZIP", new double[]{0.7, -0.2, -0.8});
        Stock CardinalCapital = new Stock("0.8+,0.1-","CRDC", new double[]{-0.1, 0.4, -0.2});
        Stock Heisenburger = new Stock(800, 10, 0.8, "0.3+,0.1-", "HBGR");
        Stock Prismatica = new Stock("0.3+,0.2-","PRMA", new double[]{-0.4, 0.3, 0.6});

        Thread mainThread = new Thread(Hogcoin);
        Thread stockRollThread = new Thread(SpamInc);

         allStocks = new Stock[]{QuarterlyAndPenniless500, GameCorner, BirdTechnicalInstitute, SpamInc, WariosWarGyros, Hogcoin, PonziPyramid, CardinalCapital, Heisenburger, Prismatica};

         mainThread.start();

        while (true)
        {
            stockTime(10, allStocks); // Not debugging interval should be 60
        }
    }

    public static void stockTime(long secInterval, Stock[] stocks) throws InterruptedException {
        TimeUnit.SECONDS.sleep(secInterval);
        logStocks(stocks); // Log prior to rolling
        rollStocks(stocks);

        if (Math.random() <= 0.2) // todo: probability method
        {
            stockEvent(stocks);
            System.out.println(PiggyBank.ANSI_YELLOW + "A stock event occurred." + PiggyBank.ANSI_RESET);
        }
        System.out.println();
    }

    public static void stockGraph(Stock stock, int[] axisBinds) // Visual graph representation
    {
        //      HGCN   -~-  12:44:24  -~-  +3.2% (lifetime)
        // 750 ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
        //  |  ░░░░░░░░░░░░░▓▓▓▓░░░░░░░░░░░░░░░░░░░░░░░▓▓▓▒
        // 500 ░░▓▓░░░░░░░░▓▓▒▒▒▓▓▒░░░░░░░░░░░░░░░░░▓▓▓▒▒▓▓
        //  |  ░░▓▒▓▒░░░░░░▓▒░░░▒▓▓▒░░░░░░░░░░░░▓▓▓▓▓▒▒░░░▒
        // 250 ░▓▓▒▓▓▓▒░░░▓▓▒░░░░▒▓▒░░░░░░░░░░▓▓▓▒▒▒░░░░░░░
        //  |  ░▓▒░▒▓▓▓▒░░▓▒░░░░░▒▓▓▒░░░░░░░░▓▓▒░░░░░░░░░░░
        //  0  ▓▒░░░░▒▓▓▒▓▒░░░░░░░░▒▓▒▓▓▓▓▒▒▓▒▒░░░░░░░░░░░░
        //     00 - 04 - 08 - 12 - 16 - 20 - 24 - 28 - 32



        // - Check which mark the entry is closest to (rounding??). This is marked as ▓
        // - High jumps should make sure to be connected (is there a way to check chars all around, e.g. each line = an array of chars?)
        // - Flat lines at end should have a "dark gradient" -- 80% ▓, 20% ▒. The endpoints should never be ▒.
        // - Put ▒ one char below ▓. What to do when sharp ROC??
        // - Colors??? (check google stock format)
        // - Three modes: 5m, 10m, lifetime.
        // - may have to smooth out the graph / have mid values by rolling every 10 sec i dunno


        String graphMode = "lifetime";
        System.out.println("    " + stock.fourScore + "   -~-   " + LocalTime.now() + "   -~-   " + "+3.2% (" + graphMode + ")");

        int horAxisOrigin = axisBinds[0]; // 0
        int horAxisEnd = axisBinds[1]; // 32
        int vertAxisOrigin = axisBinds[2]; // 0
        int vertAxisEnd = axisBinds[3]; // 750

        int[] horAxisSteps = new int[9]; // The actual number labels on the axis. Hor has 9 'steps'.
        int[] vertAxisSteps = new int[4];

        int horStepWidth = String.valueOf(horAxisEnd).length(); // Max width of step (hAE would have the maximum value, and thus max width).
        int vertStepWidth = String.valueOf(vertAxisEnd).length();

        StringBuilder horAxis = new StringBuilder();
        StringBuilder vertAxis = new StringBuilder();

        for (int i = 0; i < horAxisSteps.length; i++)
        {
            horAxisSteps[horAxisSteps.length - i - 1] = horAxisEnd - horAxisEnd/8 * i; // 32, 28, 24, 20, 16, 12, 8, 4, 0

            horAxis.append(horAxisSteps[i]);
            if (i != horAxisSteps.length - 1) // If not at end of axis steps
                horAxis.append(" - ");

            if (i < vertAxisSteps.length)
                vertAxisSteps[i] = vertAxisEnd - vertAxisEnd/3 * i; // 750, 500, 250, 0
        }



        for (int i = 0; i < 7; i++)
        {
            char[] row = new char[50];
            java.util.Arrays.fill(row, '░');
            System.out.println(row);
        }
        System.out.println(horAxis);

    }

    public void run()
    {
        while (true)
        {
            String action = tempBank.prompt("Next action?", "Error: invalid action.", new String[]{"graph", "forceroll"}, "NO_CONTEXT");

            if (action.equals("graph")) // Replace with switch?
            {
                int stockIndex = (int)tempBank.prompt("Which stock?", "Error: invalid stock.", 0, allStocks.length - 1, true, "NO_CONTEXT");
                System.out.println("Accessing stock index " + stockIndex + " (" + allStocks[stockIndex].toString() + ")...");
                stockGraph(allStocks[stockIndex], new int[]{0, 30, 0, 750});
            }
            else if (action.equals("forceroll"))
            {
                int stockIndex = (int)tempBank.prompt("Which stock?", "Error: invalid stock.", 0, allStocks.length - 1, true, "NO_CONTEXT");
                System.out.println("Force-rolling stock index " + stockIndex + " (" + allStocks[stockIndex].toString() + ")...");
                rollStocks(new Stock[]{allStocks[stockIndex]});
            }
        }

    }

    public String toString()
    {
        return fourScore;
    }

    public static void rollStocks(Stock[] stocks) // Based on sc and gR roll the stocks forward
    {
        for (Stock stock : stocks)
        {
            String[] totalProbability = stock.scope.replace("+", "").replace("-", "").split(","); // Total of prob+ and prob- together
            Double growthProbability = Double.parseDouble(totalProbability[0]) / Double.parseDouble(totalProbability[0]) + Double.parseDouble(totalProbability[1]); // prob+ / prob+ and prob-
            stock.value *= (stock.growthRate) + Double.parseDouble(stock.scope.substring(0, stock.scope.indexOf("+")));
            stock.value = stock.truncate(stock.value, 2);

            if (stock.stockHistory.size() != 0)
            {
                System.out.println(PiggyBank.ANSI_BLUE + "Stock " + stock.fourScore + " rolled. " + stock.formatCurrency(stock.stockHistory.get(stock.stockHistory.size()-1)) + " -> " + stock.formatCurrency(stock.value) + PiggyBank.ANSI_RESET);
            }

        }
    }

    public static void logStocks(Stock[] stocks) // Keep a record of stocks history and manage log length accordingly.
    {
        for (Stock stock : stocks)
        {
            stock.stockHistory.add(stock.value);
            System.out.println(PiggyBank.ANSI_GREEN + stock.fourScore + " updated (" + stock.stockHistory.size() + " entries)" + PiggyBank.ANSI_RESET);

            if (stock.stockHistory.size() > 5) // Not debugging max should be 30? 50?
            {
                stock.stockHistory.remove(0);
                System.out.println(PiggyBank.ANSI_RED + "Removed one entry from " + stock.fourScore + ". (" + stock.stockHistory.size() + " entries)");
            }
        }
    }

    public static void stockEvent(Stock[] stocks) // Extreme stock event
    {
        for (Stock stock : stocks)
        {

        }
    }

    public static void watchStock(Stock stock, boolean isAddToWatchlist) // Watchlist or unwatch a stock
    {

    }
}
