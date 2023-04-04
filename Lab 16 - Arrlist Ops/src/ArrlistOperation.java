//Name - Lucas Xie

// ** This is all three labs combined into one. I'd like "roman numbers" to be graded. The given test cases are already loaded and will output on run.


// More test cases? Type the input "rn(int OR roman numeral)". For formatting info input "help()"

// You can test several test cases at once like this: "ma(command + command + command...)"
// Example: "ma(rn(30) + rn(XVII) + rn(MMXLVII))" will run all three test cases.

// Thanks!


import org.apache.commons.lang3.NotImplementedException;
import org.javatuples.Pair;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.DoubleStream;

import static java.util.Objects.isNull;

public class ArrlistOperation {
    private static final Scanner input = new Scanner(System.in);
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    private static final Set<String> ninetiesWordset = new HashSet<>(Set.of("tubular", "awesome", "radical", "incredible", "groovy", "funky", "sweet", "amazing", "impressive", "gnarly", "cool")); // Totally tubular.
    private final List<String> opsCache;

    public ArrlistOperation(List<String> fCache) { // Precache some data
        opsCache = fCache;
    }

    public ArrlistOperation() { // No precaching
        opsCache = new ArrayList<>();
    }

    enum dataStructs {
        ARRAY,
        LIST,
        SET,
        STACK,
        QUEUE,
        NONE
    }

    public static void main(String[] args)
    {
        boolean isFirstTime = true;
        ArrlistOperation arrOps = new ArrlistOperation(); // <-- LOAD CACHE HERE

        if (arrOps.opsCache.size() != 0) {
            System.out.println(ANSI_BLUE + "(✓) Successfully precached " + arrOps.opsCache.size() + " data. Executing..." + ANSI_RESET);
        }

        while (true) {
            if (arrOps.opsCache.size() == 0) {
                if (isFirstTime) {
                    System.out.println(ANSI_CYAN + "➢ Input an arrlist operation command (options are ll, ml, cc, rc). Wrap with ma() for multiactions. Type 'help()' for formatting help. Use 'end()' to terminate the program." + ANSI_RESET);
                    isFirstTime = false;
                } else
                    System.out.println(ANSI_CYAN + "\n\n➢ Input an arrlist operation command. Use 'end()' to terminate program. Need help? -- use 'help()'" + ANSI_RESET);

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
            if (!trimmedAction.replaceAll("\\[.*?]|\\{.*?}|\\(.*?\\)|Q<.*?>|S<.*?>", "").isBlank())
                opArgs.addAll(trimAll(trimmedAction.replaceAll("\\[.*?]|\\{.*?}", "").split(","))); // Get individual args (after removing arrays, b/c they also use commas!) -- won't always run b/c if there are no args left it'll still add ""

            //opArgs = opArgs.stream().map(s -> s.replaceAll("[\\[{\\]}]", "")).toList(); // Remove all delimiters from each opArg



        } catch (Exception e) {
            if (allowFeedback) {
                System.out.println(ANSI_RED + "(x) Error: commands must be followed by parentheses!");
                System.out.println("Acceptable formats are:"); // GET 'EM!
                System.out.println("\t* ll(?) = language libs");
                System.out.println("\t* ml(?) = manage langlibs");
                System.out.println("\t* rc(List<Integer>) = repetition check");
                System.out.println("\t* cc(List<Integer>) = cascade check\n");
                System.out.println("\t* ma(command + command...) = multiaction" + ANSI_RESET);

                wasSuccessfulExecution = false;
            }
            isNoParentheses = true;
        }

        if (!isNoParentheses) {
            switch (opCommand) {
                case "ll" -> {
                    langLibs();
                }

                case "ml" -> {

                }

                case "rc" -> {
                    if (opArgs.size() == 1 && isCollectionOrArrayParsable(opArgs.get(0)) && determineStructType(opArgs.get(0)) != dataStructs.ARRAY) {
                        System.out.println(ANSI_BLUE + "The list " + (repetitionCheck((List<Integer>) parseCollection(opArgs.get(0).replaceAll("\\[|]|\\{|}|\\(|\\)|Q<|S<", ""), determineStructType(opArgs.get(0)))) ? "does indeed contain a repeat." : "doesn't have any repeats -- " + weightedRandom(ninetiesWordset.toArray(), new double[ninetiesWordset.size()], true) + "!" + ANSI_RESET));
                        wasErrorThrownOrCommandValid = true;
                    }
                }

                case "cc" -> {
                    if (opArgs.size() == 1 && isCollectionOrArrayParsable(opArgs.get(0)) && determineStructType(opArgs.get(0)) != dataStructs.ARRAY) {
                        System.out.println(ANSI_PURPLE + "The list " + ((cascadeCheck((List<Integer>) parseCollection(opArgs.get(0).replaceAll("\\[|]|\\{|}|\\(|\\)|Q<|S<", ""), determineStructType(opArgs.get(0))))) ? "cascades -- " + weightedRandom(ninetiesWordset.toArray(), new double[ninetiesWordset.size()], true) + "!" : "does not cascade :("));
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
                    System.out.println(ANSI_BLUE + "\n\n(✓) Arrlist operations complete -- program terminated." + ANSI_RESET);
                    System.exit(0);
                }

                case "help" -> {
                    System.out.println(ANSI_BLUE + "\n*****************************************************************************************************************************************\n");
                    System.out.println("\tArrlist ops, like floop ops, are done through commands -- formatted much like calling methods.");
                    System.out.println("\tUnlike arrays (format as [a,b,c..]), arrlists must be declared as {a,b,c..}. You cannot use non-bracket notation for lists.");
                    System.out.println("\tPossible commands:");
                    System.out.println("\t\t* ll(?) = language libs");
                    System.out.println("\t\t* ml(?) = manage langlibs");
                    System.out.println("\t\t* cc(List<Integer>) = cascade check");
                    System.out.println("\t\t* rc(List<Integer>) = repetition check\n");
                    System.out.println("\t\t* ma(command + command...) = multiaction");
                    System.out.println("\tNote: you normally can only call one command per action. Use multiaction instead for bulk ops.");
                    System.out.println("\n*****************************************************************************************************************************************\n" + ANSI_RESET);
                    wasErrorThrownOrCommandValid = true;
                }

                default -> {
                    if (allowFeedback) {
                        System.out.println(ANSI_RED + "Error: invalid command. Valid commands are ll, gl, cc, rc, ma." + ANSI_RESET);
                    }
                    wasErrorThrownOrCommandValid = true;
                    wasSuccessfulExecution = false;
                }
            }

            if (!wasErrorThrownOrCommandValid) {
                if (allowFeedback) {
                    System.out.println(ANSI_RED + "Error: invalid args. Acceptable formats are:");
                    System.out.println("\t* ll(?) = langLibs");
                    System.out.println("\t* ml(?) = manageLangLibs");
                    System.out.println("\t* cc(List<Integer>) = cascadeCheck");
                    System.out.println("\t* rc(List<Integer>) = repetitionCheck");
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

    public static void langLibs() {

        getLangLibs(langDat, libsDat,);
    }

    public static void writeDat(List<String> libs) throws IOException {
        File langDat = new File("/lang.dat");
        File libsDat = new File("/libs.dat");
        BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream) libs));
        String line = reader.readLine();
        while (!line.isEmpty()) {
            
        }
    }

    public static <T extends List<E>, E> T readDat() {

    }


    public static boolean repetitionCheck(List<Integer> list) {
        for (Integer item : list) {
            if (list.indexOf(item) != list.lastIndexOf(item)) return true;
        }
        return false;
    }

    public static boolean cascadeCheck(List<Integer> list) {
        for (int i = 0; i < list.size()-1; i++) {
            if (list.get(i+1) > list.get(i)) return false;
        }
        return true;
    }


    @SafeVarargs
    public static <T extends Collection<E>, E> void bulkAdd(T collection, E... items) { // <+> APM -- this may seem redundant (see List.addAll) but this might be useful when adding a bunch of individual items.
        collection.addAll(Arrays.asList(items));
    }

    @SafeVarargs
    public static <T,K> void bulkMap(Map<T,K> map, Pair<T,K>... entries) {
        for (Pair<T,K> entry : entries) {
            map.put(entry.getValue0(), entry.getValue1());
        }
    }

    public static boolean isCollectionOrArrayParsable(String string) { // <+> APM - todo: use generics to avoid a million overloads (e.g. double, boolean..). Only implement for primitives though!
        // The string WILL include identifying brackets ("\\[|]|\\{|}|\\(|\\)|Q<|S<").
        String startDelimiter = (String.valueOf(string.charAt(0)).matches("[\\[{(]")) ? String.valueOf(string.charAt(0)) : string.substring(0,2);
        String endDelimiter = String.valueOf(string.charAt(string.length()-1));
        Map<String, String> delimiterPairs = new HashMap<>();
        bulkMap(delimiterPairs, Pair.with("[", "]"), Pair.with("(", ")"), Pair.with("{", "}"), Pair.with("Q<", ">"), Pair.with("S<", ">"));

        if (!startDelimiter.matches("Q<|S<|\\[|\\{|\\(") || !endDelimiter.matches("[]})>]") || !delimiterPairs.get(startDelimiter).equals(endDelimiter)) return false; // Unrecognized delimiters or they do not match each other.


        for (String element : string.replaceAll("\\" + startDelimiter + "|\\" + endDelimiter, "").split(",")) {
            try {
                Integer.parseInt(element);
            }
            catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    public static dataStructs determineStructType(String string) {
        // You can assume the string is struct-valid -- so it will be in a correct form.
        String startDelimiter = (String.valueOf(string.charAt(0)).matches("[\\[{(]")) ? String.valueOf(string.charAt(0)) : string.substring(0,2);

        switch (startDelimiter) {
            // [a,b,c..]- array
            case "[" -> { return dataStructs.ARRAY; }

            // {a,b,c..} - list
            case "{" -> { return dataStructs.LIST; }

            // (a,b,c..) - set
            case "(" -> { return dataStructs.SET; }

            // Q<a,b,c..> - queue
            case "Q<" -> { return dataStructs.QUEUE; }

            // S<a,b,c..> - stack
            case "S<" -> { return dataStructs.STACK; }

            // None of the cases valid (theoretically should NEVER occur.)
            default -> {
                System.err.println(ANSI_RED + "(!) Error: unexpected 'NONE' result from determineStructType().");
                return dataStructs.NONE;
            }
        }
    }

    public static Collection<Integer> parseCollection(String string, dataStructs structType) {
        int elementCount = string.split(",").length;
        Collection<Integer> collection = null; // Have to instantiate

        for (int i = 0; i < elementCount; i++) {
            switch (structType.name()) {
                case "LIST" -> {
                    if (isNull(collection)) collection = new ArrayList<>();
                }

                case "QUEUE" -> { // ??
                    if (isNull(collection)) collection = new PriorityQueue<>();
                }

                case "SET" -> {
                    if (isNull(collection)) collection = new HashSet<>();
                }

                case "STACK" -> {
                    if (isNull(collection)) collection = new Stack<>();
                }

                default -> {
                    return null;
                }
            }
            collection.add(Integer.parseInt(string.split(",")[i]));
        }

        return collection;
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

    public static <T> T weightedRandom(T[] choices, double[] weights, boolean autoEqualize) // <+> APM
    {
        double rng = Math.random();

        if (autoEqualize) {
            Arrays.fill(weights, 1.0 / choices.length);
        }

        assert (DoubleStream.of(weights).sum() != 1) : "weightedRandom weights do not add up to 1 (= " + DoubleStream.of(weights).sum() + ")!";
        assert (choices.length == weights.length) : "weightedRandom choice (" + choices.length + ") and weights (" + weights.length + ") array are not the same length!";

        for (int i = 0; i < weights.length; i++) {
            if (rng < weights[i])
                return choices[i];
            else
                rng -= weights[i];
        }

        return null;
    }
}
