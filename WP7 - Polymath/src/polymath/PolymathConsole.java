package polymath;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static polymath.AnsiCode.*;
public class PolymathConsole {
    public static String prompt(String message, String errorMessage, String[] bounds, boolean lineMode, boolean isCaseSensitive) // <+> APM
    {
        Scanner input = new Scanner(System.in);
        String nextInput;

        while (true)
        {
            System.out.print(message);
            if (!message.equals(""))
                System.out.println();

            if (lineMode) {
                input.nextLine();
                nextInput = input.nextLine();
            }
            else {
                nextInput = input.next();
            }

            if (!isCaseSensitive)
            {
                nextInput = nextInput.toLowerCase();

                for (int i = 0; i < bounds.length; i++)
                    bounds[i] = bounds[i].toLowerCase();
            }

            if (nextInput.matches(String.join("|", bounds)) || bounds[0].equals("")) {
                return nextInput;
            } else {
                System.out.println(ANSI_RED + errorMessage + ANSI_RESET);
            }

        }
    }

    public static double prompt(String message, String errorMessage, double min, double max, boolean isIntegerMode)
    {
        Scanner input = new Scanner(System.in);
        String nextInput;
        double parsedInput = 0;
        boolean isValid;

        while (true) {
            System.out.print(message);
            if (!message.equals(""))
                System.out.println();

            nextInput = input.next();
            try {

                if (!isIntegerMode) {
                    parsedInput = Double.parseDouble(nextInput);
                } else {
                    parsedInput = Integer.parseInt(nextInput);
                }

                input.nextLine();
                isValid = true;
            } catch (Exception e) {
                isValid = false;
            }

            if (parsedInput >= min && parsedInput <= max && isValid) {
                return parsedInput;
            } else {
                System.out.println(ANSI_RED + errorMessage + ANSI_RESET);
            }
        }
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
        if (!completionMessage.isBlank()) System.out.print("\b" + completionMessage + "\n" + ANSI_RESET);
        else System.out.println();
    }

    public static String cardinalToOrdinal(int cardinal) {
        String ordinal = "" + cardinal;

        if (cardinal % 10 == 1 && cardinal != 11) ordinal += "st";
        else if (cardinal % 10 == 2) ordinal += "nd";
        else if (cardinal % 10 == 3) ordinal += "rd";
        else ordinal += "th";

        return ordinal;
    }

    public static long getMillisecSinceUnixEpoch() { // unix epoch = 1/1/1970
        return System.currentTimeMillis();
    }

    public static <T> String grammaticParse(T[] array, String conjunction) { // <+> APM
        StringBuilder gParsedString = new StringBuilder(Arrays.toString(array));

        gParsedString.deleteCharAt(0)
                .deleteCharAt(gParsedString.length()-1);

        if (array.length > 1 && !conjunction.equals("")) gParsedString.insert(gParsedString.lastIndexOf(",") + 1, " " + conjunction);

        return gParsedString.toString();
    }

    public static double truncate(double value, int mantissaLength) // <+> APM
    {
        return BigDecimal.valueOf(value).setScale(mantissaLength, RoundingMode.HALF_EVEN).doubleValue();
    }





    public static void main(String[] args) throws InterruptedException {
        IrregularPolygon irrPoly = null;

        switch (prompt(ANSI_BLUE + "\n\n\n\n❖ Define irrpoly manually (point-by-point) or automatically?" + ANSI_RESET + "\nSelect manual or auto.", "Error: invalid option. Select from either manual or auto.", new String[]{"manual", "auto"}, false, false)) {
            case "manual" -> {
                List<Point> pointList = new ArrayList<>();

                boolean isInputtingPoints = true;
                while (isInputtingPoints) {
                    int xVal = (int) prompt("\n\nInput the x coordinate of the " + cardinalToOrdinal(pointList.size() + 1) + " point. " + ANSI_PURPLE + ">> 「 " + ANSI_RESET + "?" + ANSI_PURPLE + "," + ANSI_RESET + " ?" + ANSI_PURPLE + " 」" + ANSI_RESET, "Error: invalid integer. Must be between -100000 and 100000!", -100000, 100000, true);
                    int yVal = (int) prompt("Input the y coordinate of the " + cardinalToOrdinal(pointList.size() + 1) + " point. " + ANSI_PURPLE + ">> 「 " + xVal + "," + ANSI_RESET + " ?" + ANSI_PURPLE + " 」" + ANSI_RESET, "Error: invalid integer. Must be between -100000 and 100000!", -100000, 100000, true);

                    System.out.println(ANSI_CYAN + "✓ Successfully set the " + cardinalToOrdinal(pointList.size() + 1) + " point in the irrpoly. >> (" + xVal + ", " + yVal + ")\n" + ANSI_RESET);
                    pointList.add(new Point(xVal, yVal));

                    if (pointList.size() >= 3) {
                        switch (prompt(ANSI_YELLOW + "♢ The current irrpoly (" + pointList.size() + " sides) is eligible for construction as it exceeds the minimum of 3 points. Continue adding points or build irrpoly?" + ANSI_RESET + "\nSelect continue or build.", "Error: invalid option. Select from either continue or build.", new String[]{"continue", "build"}, false, false)) {
                            case "continue" -> {
                            }

                            case "build" -> {
                                isInputtingPoints = false;
                            }
                        }
                    }
                }

                irrPoly = new IrregularPolygon(pointList);
            }

            case "auto" -> {
                switch (prompt("♢ Define irrpoly side count?", "Error: invalid option.", new String[]{"yes", "y", "no", "n"}, false, false)) {
                    case "y", "yes" -> {
                        irrPoly = new IrregularPolygon((int) prompt("♢ Define the irrpoly's side count (3-15).", "Error: invalid number.", 3, 15, true));
                    }

                    case "n", "no" -> {
                        irrPoly = new IrregularPolygon();
                    }
                }
            }
        }

        long pretime = getMillisecSinceUnixEpoch();
        fancyDelay(400, ANSI_BLUE + "\n\n\nBuilding irrpoly...", "Done!" + ANSI_RESET, 5);
        long posttime = getMillisecSinceUnixEpoch();
        System.out.println(ANSI_GREEN + "✓ Successfully built " + irrPoly + " (" + (posttime - pretime) + " ms).\n");

        System.out.println("Irrpoly points: " + grammaticParse(Arrays.stream(irrPoly.points()).map(p -> "[" + p.getX() + ", " + p.getY() + "]").toArray(), ""));
        System.out.println("Irrpoly perimeter: " + truncate(irrPoly.perimeter(), 2) + " units");
        System.out.println("Irrpoly area: " + truncate(irrPoly.area(), 2) + " u^2" + ANSI_RESET);
    }
}
