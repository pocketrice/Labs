//Name - Lucas Xie

// ** This is all three labs combined into one. I'd like "roman numbers" to be graded. The given test cases are already loaded and will output on run.


// More test cases? Type the input "rn(int OR roman numeral)". For formatting info input "help()"

// You can test several test cases at once like this: "ma(command + command + command...)"
// Example: "ma(rn(30) + rn(XVII) + rn(MMXLVII))" will run all three test cases.

// Thanks!


import org.apache.commons.lang3.math.NumberUtils;
import org.javatuples.Pair;
import org.javatuples.Tuple;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArrayOperation {
    static Scanner input = new Scanner(System.in);
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final List<Pair<Integer, String>> decRomanConvFactors = new ArrayList<>();
    public static final List<Pair<Integer, String>> decPrecDrcf = new ArrayList<>(); // decPrecDrcf = Decremented-precedent Decimal-Roman Conversion Factors. What an odd name.

    private final List<String> opsCache;

    public ArrayOperation(List<String> fCache) { // Precache some data
        opsCache = fCache;
    }

    public ArrayOperation() { // No precaching
        opsCache = new ArrayList<>();
    }

    public static void main(String[] args)
    {
        bulkAdd(decRomanConvFactors, Pair.with(1000, "M"), Pair.with(900, "CM"), Pair.with(500, "D"), Pair.with(400, "CD"), Pair.with(100, "C"),  Pair.with(90, "XC"), Pair.with(50, "L"), Pair.with(40, "XL"), Pair.with(10, "X"), Pair.with(9, "IX"), Pair.with(5, "V"), Pair.with(4, "IV"),  Pair.with(1, "I"));
        bulkAdd(decPrecDrcf, Pair.with(900, "CM"), Pair.with(1000, "M"), Pair.with(400, "CD"), Pair.with(500, "D"), Pair.with(90, "XC"), Pair.with(100, "C"), Pair.with(40, "XL"), Pair.with(50, "L"), Pair.with(9, "IX"), Pair.with(10, "X"), Pair.with(4, "IV"), Pair.with(5, "V"),  Pair.with(1, "I"));

        boolean isFirstTime = true;
        ArrayOperation arrOps = new ArrayOperation(new ArrayList<>(List.of("rn(MMDXII)", "sn([33,24,23224,22,3])", "luvs([42,32,2,34,3])"))); // <-- LOAD CACHE HERE

        if (arrOps.opsCache.size() != 0) {
            System.out.println(ANSI_BLUE + "(✓) Successfully precached " + arrOps.opsCache.size() + " data. Executing..." + ANSI_RESET);
        }

        while (true) {
            if (arrOps.opsCache.size() == 0) {
                if (isFirstTime) {
                    System.out.println(ANSI_CYAN + "➢ Input an array operation command (options are luvs, sn, rn). Wrap with ma() for multiactions. Type 'help()' for formatting help. Use 'end()' to terminate the program." + ANSI_RESET);
                    isFirstTime = false;
                } else
                    System.out.println(ANSI_CYAN + "\n\n➢ Input an array operation command. Use 'end()' to terminate program. Need help? -- use 'help()'" + ANSI_RESET);

                executeArrOperation(input.nextLine().trim(), arrOps.opsCache, true);
            } else {
                processOpCache(arrOps.opsCache);
                arrOps.opsCache.clear();
            }
        }
    }

    public static boolean executeArrOperation (String action, List<String> opCache, boolean allowFeedback) {
        boolean wasErrorThrownOrCommandValid = false, isNoParentheses = false, wasSuccessfulExecution = true;

        String opCommand = action.split("\\(")[0];
        List<String> opArgs = new ArrayList<>();

        try {
            // sn([3,2,4], [22,4], 3)
            String trimmedAction = action.substring(action.indexOf("(") + 1, action.indexOf(")")); // Remove () delimiters

            opArgs.addAll(trimAll(getSubstrings(trimmedAction, Pattern.compile("\\[.*?]|\\{.*?}")))); // Get all arrays (delimited by {} or [])
            if (!trimmedAction.replaceAll("\\[.*?]|\\{.*?}", "").isBlank())
                opArgs.addAll(trimAll(trimmedAction.replaceAll("\\[.*?]|\\{.*?}", "").split(","))); // Get individual args (after removing arrays, b/c they also use commas!) -- won't always run b/c if there are no args left it'll still add ""

            opArgs = opArgs.stream().map(s -> s.replaceAll("[\\[{\\]}]", "")).toList(); // Remove all delimiters from each opArg



        } catch (Exception e) {
            if (allowFeedback) {
                System.out.println(ANSI_RED + "(x) Error: commands must be followed by parentheses!");
                System.out.println("Acceptable formats are:"); // GET 'EM!
                System.out.println("\t* luvs(int[]) = 'last-unique-value' sum");
                System.out.println("\t* sn(int[]) = smallest number");
                System.out.println("\t* rn(int||roman numeral) = roman numerics\n");
                System.out.println("\t* ma(command + command...) = multiaction" + ANSI_RESET);

                wasSuccessfulExecution = false;
            }
            isNoParentheses = true;
        }

        if (!isNoParentheses) {
            switch (opCommand) {
                case "luvs" -> {
                    if (opArgs.size() == 1 && isArrayParsable(opArgs.get(0))) {
                        System.out.println(ANSI_PURPLE + "The luvs' " + lastUniqueValueSum(parseArray(opArgs.get(0))) + ", luv!" + ANSI_RESET);
                        wasErrorThrownOrCommandValid = true;
                    }
                }

                case "sn" -> {
                    if (opArgs.size() == 1 && isArrayParsable(opArgs.get(0))) {
                        System.out.println(ANSI_YELLOW + "The smallest of the bunch is " + smallestNumber(parseArray(opArgs.get(0))) + "." + ANSI_RESET);
                        wasErrorThrownOrCommandValid = true;
                    }
                }

                case "rn" -> {
                    if (opArgs.size() == 1 && (isIntParsableAndBounded(opArgs.get(0), 1, 4000)) || isRomanNumParsable(opArgs.get(0))) {
                        System.out.println(((NumberUtils.isParsable(opArgs.get(0))) ? "Romanized, your number " + ANSI_CYAN + "(" + opArgs.get(0) + ")" + ANSI_RESET + " is hereby " + romanNumerics(Integer.parseInt(opArgs.get(0))) : "Decimized, your number " + ANSI_CYAN + "(" + opArgs.get(0) + ")" + ANSI_RESET + " is hereby " + romanNumerics(opArgs.get(0))) + ".");
                        wasErrorThrownOrCommandValid = true;
                    }
                }

                case "ma" -> { // ma(rn(arg) + luvs(arg[]))
                    if (opCache.size() != 0) {
                        return false; // No concurrent multiactions!
                    }
                    List<String> floopCommands = trimAll(action.substring(action.indexOf("(")+1, action.lastIndexOf(")")).trim().split("\\+"));

                    if (!(floopCommands.size() == 0)) {
                        System.out.println(ANSI_BLUE + "❖ Multiaction valid and cached (" + floopCommands.size() + " actions). Executing..." + ANSI_RESET);

                        if (floopCommands.size() == 1)
                            System.out.println(ANSI_BLUE + " (!) Redundancy warning: multiaction was called but only uses 1 action." + ANSI_RESET);


                        opCache.addAll(floopCommands);

                        wasErrorThrownOrCommandValid = true;
                    }
                }

                case "end" -> {
                    System.out.println(ANSI_BLUE + "\n\n(✓) Array operations complete -- program terminated." + ANSI_RESET);
                    System.exit(0);
                }

                case "help" -> {
                    System.out.println(ANSI_BLUE + "\n************************************************************************************************************\n");
                    System.out.println("\tArray ops, like floop ops, are done through commands -- formatted much like calling methods.");
                    System.out.println("\tArrays may be formatted as [a,b,c..], {a,b,c..}, or simply a,b,c... -- use brackets as delimiters between arrays.");
                    System.out.println("\tPossible commands:");
                    System.out.println("\t\t* luvs(int[]) = 'last-unique-value' sum");
                    System.out.println("\t\t* sn(int[]) = smallest number");
                    System.out.println("\t\t* rn(int||roman numeral) = roman numerics\n");
                    System.out.println("\t\t* ma(command + command...) = multiaction");
                    System.out.println("\tNote: you normally can only call one command per action. Use multiaction instead for bulk ops.");
                    System.out.println("\n************************************************************************************************************\n" + ANSI_RESET);
                    wasErrorThrownOrCommandValid = true;
                }

                default -> {
                    if (allowFeedback) {
                        System.out.println(ANSI_RED + "Error: invalid command. Valid commands are luvs, sn, rn, ma." + ANSI_RESET);
                    }
                    wasErrorThrownOrCommandValid = true;
                    wasSuccessfulExecution = false;
                }
            }

            if (!wasErrorThrownOrCommandValid) {
                if (allowFeedback) {
                    System.out.println(ANSI_RED + "Error: invalid args. Acceptable formats are:");
                    System.out.println("\t* luvs(int[]) = lastUniqueValueSum");
                    System.out.println("\t* sn(int[]) = smallestNumber");
                    System.out.println("\t* rn(int||roman numeral) = romanNumerics");
                    System.out.println("\t* ma(command + command...) = multiaction");
                }
                wasSuccessfulExecution = false;
            }
        }

        return wasSuccessfulExecution;
    }

    public static void processOpCache(List<String> opCache) {
        for (String cachedItem : opCache) {
            if (!executeArrOperation(cachedItem, opCache, false))
                System.out.println(ANSI_RED + "Error in cache execution: " + cachedItem + " (index " + opCache.indexOf(cachedItem) + ") could not execute." + ANSI_RESET);

            System.out.println();
        }
    }

    public static int lastUniqueValueSum(int[] nums) { // Luvs!
        int lastNum = nums[nums.length-1];
        int luvSum = 0;

        for (int num : nums) {
            if (num > lastNum)
                luvSum += num;
        }

        return (nums.length == 1 || luvSum == 0) ? -1 : luvSum;
    }

    public static int smallestNumber(int[] nums) {
        int lowestNum = Integer.MAX_VALUE;

        for (int num : nums) {
            lowestNum = Math.min(lowestNum, num);
        }

        return lowestNum;
    }

    public static int romanNumerics(String romanNum) {
        int decimizedNum = 0;

        for (Pair<Integer, String> cFactor : decPrecDrcf) {
            int decFactor = cFactor.getValue0();
            String romanFactor = cFactor.getValue1();

            while (romanNum.contains(romanFactor)) {
                romanNum = romanNum.replaceFirst(romanFactor, "");
                decimizedNum += decFactor;
            }
        }

        return decimizedNum;
    }

    public static String romanNumerics(int decNum) {
        StringBuilder romanizedNum = new StringBuilder();

        for (Pair<Integer, String> cFactor : decRomanConvFactors) {
            int decFactor = cFactor.getValue0();
            String romanFactor = cFactor.getValue1();

            while (decFactor <= decNum) {
                romanizedNum.append(romanFactor);
                decNum -= decFactor;
            }
        }

        return romanizedNum.toString();
    }

    @SafeVarargs
    public static <T extends Collection<E>, E> void bulkAdd(T collection, E... items) { // <+> APM -- this may seem redundant (see List.addAll) but this might be useful when adding a bunch of individual items.
        collection.addAll(Arrays.asList(items));
    }

    public static boolean isRomanNumParsable(String string) { // <+> APM?
        // ** PRELIMINARY CHECKS **
        Matcher matcher = Pattern.compile("[CDILMVXcdilmvx]").matcher(string);

        if (!matcher.find()) // Ensure string contains only letters that rom-nums use (CDILMVX).
            return false;

        if (bulkContains(string, Pattern.compile("CM[C|D]|CD[C|L]|XC[L|X]|XLX|IX[V|I]|IVI"))) // Some special cases with the "decremented rom nums" that i can't be bothered to try to generify :p
            return false;



        // ** DCRF CHECKS **
        for (Pair<Integer, String> cFactor : decRomanConvFactors) {
            int maxRomanNumCount = (cFactor.getValue1().length() == 2) ? 1 : 4; // Decremented rom nums (IV, IX...) can only occur once. Standard rom nums only four times (this automatically prevents any rom nums above 4000/MMMM, too).
            String stringPrefix = safeSubstring(string, 0, string.indexOf(cFactor.getValue1()));

            if (!stringPrefix.isBlank() && tupledContains(decRomanConvFactors, stringPrefix + cFactor.getValue1())) { // Special case: since decPrecDrcf can't be used, this workaround ensures "decremented rom nums" still take precedence.
                while (string.contains(stringPrefix + cFactor.getValue1())) {
                    string = string.replace(stringPrefix + cFactor.getValue1(), "");
                }

                stringPrefix = "";
            }

            if (!stringPrefix.isBlank()|| numOfStringContent(string, Pattern.compile(cFactor.getValue1())) > maxRomanNumCount) {  // A roman numeral should be passed in with the RIGHT orderings (and should have proper romnum counts).
                return false;
            }

            string = string.replaceAll(cFactor.getValue1(), ""); // <+> All checks passed, remove all of that romnum from the string.

            if (string.isBlank()) // Prematurely cleared the string = all done, parsable!
                return true;
        }
        return false; // If no conv factors worked, then this string isn't romnum-parsable.
    }

    /*
    public static int anchoredStrIndex(String string, int index, String... edits) {// <+> APM -- get index after

    }

    public static String[] splitAt(String string, int[] indices) {

    }*/
    public static boolean isArrayParsable(String string) { // <+> APM - todo: use generics to avoid a million overloads (e.g. double, boolean..). Only implement for primitives though!
        for (String element : string.split(",")) {
            try {
                Integer.parseInt(element); // Disallowed: extra commas (
            }
            catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    public static int[] parseArray(String string) { // <+> APM - todo: see above
        int elementCount = string.split(",").length;
        int[] parsedArray = new int[elementCount];

       for (int i = 0; i < elementCount; i++) {
           parsedArray[i] = Integer.parseInt(string.split(",")[i]);
       }

        return parsedArray;
    }

    public static <T extends String> List<T> trimAll(T[] array) { // <+> APM (overloaded)
        List<T> trimmedArray = new ArrayList<>();

        for (T item : array)
            trimmedArray.add((T) item.trim());
        return trimmedArray; // Technical limitation: cannot feasibly return T[] -- have to create a list.
    }


    public static String safeSubstring(String string, int startIndex, int endIndex) { // <+> APM - adapted from safeIndex
        int maxIndex = string.length();

        startIndex = Math.max(startIndex, 0); // disallowed sIndex = negative
        endIndex = Math.max(0, Math.min(endIndex, maxIndex));

        return string.substring(startIndex, endIndex);
    }

    public static int numOfStringContent(String string, Pattern pattern) { // <+> APM
        int count = 0;
        Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            count++;
        }

        return count;
    }

    public static String[] getSubstrings(String string, Pattern pattern) { // <+> APM
        List<String> substrings = new ArrayList<>();
        String editedString = string;
        Matcher matcher = pattern.matcher(editedString);

        while (matcher.find()) {
            substrings.add(matcher.group(0));
            editedString = editedString.replaceFirst(String.valueOf(pattern), "");
        }

        return substrings.toArray(new String[0]);
    }

    @SafeVarargs
    public static <T extends Collection<E>, K, E extends Tuple> boolean tupledContains(T tuples, K... elements) { // <+> APM -  Not only can it check all members of tuples... but it also supports SEVERAL elements for contain() checking! 2 for 1, heck yeah :)
        int elementMatchCount = 0; // If elements.length = eMC, then all elements are found in the collection.

        for (K element : elements) {
            for (E tuple : tuples) {
                for (int i = 0; i < tuple.getSize(); i++) {
                    if (tuple.getValue(i).equals(element))
                        elementMatchCount++;
                }
            }
        }

        return (elementMatchCount == elements.length);
    }

    public static boolean bulkContains(String string, Pattern... patterns) { // <+> APM - string (and more generalized) variant of tupleContains
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(string);

            if (!matcher.find()) {
                return false; // Not in string = false. There is no middle-ground!
            }
        }

        return true; // All patterns were matched.
    }

    public static boolean isIntParsableAndBounded(String string, int lowerBound, int upperBound) {// <+> APM - oddly specific, but useful for checking not just string int-parsable BUT ALSO if between bounds. Because too lazy to double-up if statements :)
        try {
            return (Integer.parseInt(string) >= lowerBound && Integer.parseInt(string) <= upperBound); // Int-parsable; check for bounds
        }
        catch (Exception e) {
            return false; // Not int-parsable = false
        }
    }
}
