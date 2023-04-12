package com.pocketrice;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.pocketrice.AnsiCode.*;

public class Wordworm {
    private final File df;
    private final long data;

    public Wordworm(File file) throws IOException {
        df = file;
        data = getLineCount(df);
    }

    public void boot() throws InterruptedException {
        System.out.println(ANSI_BLUE + "***   Welcome to WORDWORM -- a scholar's best friend   ***" + ANSI_RESET);
        fancyDelay(500, "Booting...", ANSI_GREEN + "Ready!" + ANSI_RESET, 3);
        System.out.println("\n\n");
    }
    public void run() throws FileNotFoundException, InterruptedException {
        boot();
        Map<String, Integer> ops = new HashMap<>();
        ops.put("wordcount", 1);
        ops.put("longest", 1);
        ops.put("novowels", 1);
        ops.put("averagelength", 1);
        ops.put("sameends", 1);

        ops.put("letterfreq", 2);
        ops.put("commonlength", 2);
        ops.put("commonends", 2);
        ops.put("palindrome", 2);
        ops.put("commonpair", 2);

        ops.put("scrabble", 3);
        ops.put("fourtwo", 3);
        ops.put("dollarwords", 3);
        ops.put("nondesc", 3);
        ops.put("vowels", 3);

        int selLevel = (int) prompt("Select operation level (1-3).", "Error: select a # from 1-3.", 1, 3, true);

        while (true) {
            // Forloop replaces ops.keySet().stream().filter(k -> ops.get(k) == selLevel)... b/c can't use changing vars in lambda. Annoying...
            List<String> temp = new ArrayList<>(ops.keySet());
            for (int i = temp.size() - 1; i >= 0; i--) {
                if (ops.get(temp.get(i)) != selLevel) temp.remove(temp.get(i));
            }

            String[] selOps = temp.stream().map(String::toUpperCase).toArray(String[]::new);

            String selection = prompt("Available ops: " + grammaticParse(selOps, ",") + ANSI_BLUE + "\n(Input an operation, SHIFT to change levels, or QUIT.)" + ANSI_RESET,  "Error: invalid choice.\n\n", ArrayUtils.addAll(selOps, "SHIFT", "quit"), false, false);
            long pretime = System.currentTimeMillis();

            switch (selection) {
                // Level 1 -- note that each case contains independent code and can (mostly) run on their own.
                case "wordcount" -> {
                    System.out.println(data + " words total.");
                }

                case "longest" -> {
                    // Map and NavigableMap are interfaces. So, you can specify navmap to specifically be able to navigate the map (e.g. get last entry).
                    NavigableMap<Integer, List<String>> lenMap = new TreeMap<>();

                    read().forEach(s -> {
                        if (lenMap.putIfAbsent(s.length(), new ArrayList<>(List.of(s))) != null)
                            lenMap.get(s.length()).add(s);
                    });

                    System.out.println("Longest word (" + lenMap.lastKey() + " letters): " + ANSI_PURPLE + grammaticParse(lenMap.lastEntry().getValue(), ",") + ANSI_RESET);
                }

                case "novowels" -> {
                    List<String> strs = read().stream().filter(s -> !s.toLowerCase().matches("[a-zA-Z]*[aeiou][a-zA-Z]*")).toList();

                    System.out.println("Vowelless words: " + ANSI_PURPLE + grammaticParse(strs.toArray(), ",") + ANSI_RESET);
                    System.out.println("\nTruly vowelless words (no Y): " + ANSI_CYAN + grammaticParse(strs.stream().filter(s -> !s.toLowerCase().contains("y")).toArray(), ",") + ANSI_RESET);
                }

                case "averagelength" -> {
                    System.out.println("Average length is " + truncate((double) read().stream().map(String::length).reduce(Integer::sum).orElse(0) / data, 2) + " letters.");
                }

                case "sameends" -> {
                    List<String> strs = read().stream().filter(s -> s.charAt(0) == s.charAt(s.length() - 1)).toList();
                    System.out.println("Same-end words: " + ANSI_PURPLE + grammaticParse(strs, ",") + ANSI_RESET);
                    System.out.println("\nTotal of " + strs.size() + " same-end words.");
                }


                // Level 2
                case "letterfreq" -> {
                    Map<Character, Integer> freqMap = new HashMap<>(); // Using a treemap instead of hashmap allows for natural sorting.

                    read().forEach(s -> {
                        for (Character c : s.toCharArray()) {
                            freqMap.put(c, (freqMap.containsKey(c)) ? freqMap.get(c) + 1 : 1);
                        }
                    });

                    // Breakdown: take keyset (chars) and sort them based on descending count (comparator.reversed()).
                    Character[] sortedChars = freqMap.keySet().stream().sorted(Comparator.comparing(freqMap::get).reversed()).toArray(Character[]::new);

                    for (Character c : sortedChars) {
                        double percent = truncate((double) freqMap.get(c) / freqMap.values().stream().reduce(Integer::sum).orElse(0) * 100, 2);
                        System.out.println(c + " -- " + percent + "%");
                    }
                }

                case "commonlength" -> {
                    Map<Integer, List<String>> lenMap = new HashMap<>();

                    read().forEach(s -> {
                        if (lenMap.putIfAbsent(s.length(), new ArrayList<>(List.of(s))) != null)
                            lenMap.get(s.length()).add(s);
                    });

                    Map.Entry<Integer, List<String>> commonEntry = lenMap.entrySet().stream().max(Comparator.comparingInt(e -> e.getValue().size())).orElseThrow();
                    String percent = percentify(commonEntry.getValue().size(), data);
                    System.out.println("Most common length " + "[" + percent + "] of " + commonEntry.getKey() + " letters: " + ANSI_PURPLE + grammaticParse(commonEntry.getValue(), ",") + ANSI_RESET);
                    System.out.println("\nTotal of " + commonEntry.getValue().size() + " words of most common length.");
                }

                case "commonends" -> {
                    Map<Character, Integer> begMap = new HashMap<>();
                    Map<Character, Integer> endMap = new HashMap<>();

                    read().forEach(s -> {
                        char begChar = s.charAt(0);
                        char endChar = s.charAt(s.length()-1);

                        begMap.put(begChar, (begMap.containsKey(begChar)) ? begMap.get(begChar) + 1 : 1);
                        endMap.put(endChar, (endMap.containsKey(endChar)) ? endMap.get(endChar) + 1 : 1);
                    });

                    // Optimization of Comparator.comparing(Map.Entry::getValue)
                    Map.Entry<Character, Integer> begMax = begMap.entrySet().stream().max(Map.Entry.comparingByValue()).orElseThrow();
                    Map.Entry<Character, Integer> endMax = endMap.entrySet().stream().max(Map.Entry.comparingByValue()).orElseThrow();

                    String begPercent = percentify(begMax.getValue(), data);
                    String endPercent = percentify(endMax.getValue(), data);
                    System.out.println("Most common beginning char [" + begPercent + "]: " + begMax.getKey());
                    System.out.println("Most common end char [" + endPercent + "]: " + endMax.getKey());
                }

                case "palindrome" -> {
                    List<String> pals = new ArrayList<>();
                    Palindrome palchecker = new Palindrome();

                    read().forEach(s -> {
                        palchecker.setString(s);
                        if (palchecker.isPalindromic()) pals.add(s);
                    });

                    System.out.println("Palindromes: " + ANSI_PURPLE + grammaticParse(pals, ",") + ANSI_RESET);
                }

                case "commonpair" -> {
                    Map<String, Integer> pairMap = new HashMap<>();
                    read().forEach(s -> {
                        for (int i = 0; i < s.length() - 2; i++) {
                            String sub = s.substring(i, i+2);
                            pairMap.put(sub, (pairMap.containsKey(sub)) ? pairMap.get(sub) + 1 : 1);
                        }
                    });

                    // Optimization of Comparator.comparing(Map.Entry::getValue());
                    Map.Entry<String, Integer> maxPair = pairMap.entrySet().stream().max(Map.Entry.comparingByValue()).orElseThrow();
                    String percent = percentify(maxPair.getValue(), data);
                    System.out.println("Most common pair [" + percent + "]: " + maxPair.getKey());
                }


                // Level 3
                case "scrabble" -> {
                    // Official scrabble points
                    Map<Character, Integer> scrabblePts = new HashMap<>();
                    putAll(scrabblePts, Map.entry('u', 1), Map.entry('t', 1), Map.entry('s', 1), Map.entry('r', 1), Map.entry('o', 1), Map.entry('n', 1), Map.entry('l', 1), Map.entry('i', 1), Map.entry('e', 1), Map.entry('a', 1));
                    putAll(scrabblePts, Map.entry('g', 2), Map.entry('d', 2));
                    putAll(scrabblePts, Map.entry('p', 3), Map.entry('m', 3), Map.entry('c', 3), Map.entry('b', 3));
                    putAll(scrabblePts, Map.entry('y', 4), Map.entry('w', 4), Map.entry('v', 4), Map.entry('h', 4), Map.entry('f', 4));
                    putAll(scrabblePts, Map.entry('k', 5));
                    putAll(scrabblePts, Map.entry('x', 8), Map.entry('j', 8));
                    putAll(scrabblePts, Map.entry('q', 10), Map.entry('z', 10));

                    // Plucked from "letterfreq"
                    Map<Character, Integer> freqMap = new HashMap<>();
                    AtomicLong charCount = new AtomicLong(); // This is odd, but atomic is necessary for changing outside-scope vals in lambda.

                    read().forEach(s -> {
                        for (Character c : s.toCharArray()) {
                            freqMap.put(c, (freqMap.containsKey(c)) ? freqMap.get(c) + 1 : 1);
                            charCount.getAndIncrement();
                        }
                    });

                    Character comLetter = freqMap.keySet().stream().max(Comparator.comparing(freqMap::get)).orElseThrow();
                    freqMap.keySet().stream().sorted(Comparator.comparing(freqMap::get).reversed())
                            .forEach(k -> {
                                int points = Math.round((float) freqMap.get(comLetter) / freqMap.get(k));
                                AnsiCode color = (scrabblePts.get(k) > points) ? ANSI_GREEN : (scrabblePts.get(k) < points) ? ANSI_RED : NONE;
                                System.out.println(k + " (" + percentify(freqMap.get(k), charCount.get()) + ") -- " + points + " pts " + color + "(actual: " + scrabblePts.get(k) + ")" + ANSI_RESET);
                            });
                }

                case "fourtwo" -> {
                    List<String> strs = new ArrayList<>();

                    // https://stackoverflow.com/questions/31977356/using-streams-to-manipulate-a-string
                    // Stream a String into a Stream<Character>.
                    read().forEach(s -> {
                        long uniqueCount = s.chars().mapToObj(ch -> (char)ch).distinct().count();
                        if (uniqueCount <= 2 && s.length() >= 4) strs.add(s);
                    });

                    strs.sort(Comparator.comparing(String::length));


                    Integer[] lens = strs.stream().map(String::length).distinct().toArray(Integer[]::new);
                    for (Integer len : lens) {
                        System.out.println(len + ": " + ANSI_PURPLE + grammaticParse(strs.stream().filter(s -> s.length() == len).toArray(), ",") + ANSI_RESET);
                        System.out.println();
                    }
                }

                case "dollarwords" -> {
                    String[] strs = read().stream().filter(s -> s.chars().mapToObj(ch -> (char)ch - ('a'-1)).reduce(Integer::sum).orElse(0) == 100).toArray(String[]::new);
                    System.out.println("Dollar words: " + ANSI_PURPLE + grammaticParse(strs, ",") + ANSI_RESET);
                    System.out.println("\nShortest candidate: " + ANSI_RED + Arrays.stream(strs).min(Comparator.comparing(String::length)).orElseThrow() + ANSI_RESET);
                }

                case "nondesc" -> {
                    List<String> regStrs = new ArrayList<>();

                    read().forEach(s -> {
                        boolean isNonDesc = true;

                        for (int i = 0; i < s.length() - 1; i++) {
                            if (s.charAt(i) > s.charAt(i + 1)) {
                                isNonDesc = false;
                                break;
                            }
                        }

                        if (isNonDesc) regStrs.add(s);
                    });


                    // https://stackoverflow.com/questions/30053487/how-to-check-if-exists-any-duplicate-in-java-8-streams
                    // Using allMatch, check for duplicates by taking advantage of Set.add, which can check if an item is already in set.
                    List<String> strictStrs = regStrs.stream().filter(s -> s.chars().mapToObj(ch -> (char)ch).allMatch(new HashSet<>()::add)).toList();

                    System.out.println("Regular matches (non-descending alpha): " + ANSI_PURPLE + grammaticParse(regStrs, ",") + ANSI_RESET);
                    System.out.println("\nStrict matches (ascending alpha): " + ANSI_CYAN + grammaticParse(strictStrs, ",") + ANSI_RESET);
                }

                case "vowels" -> {
                    List<String> aeiouStrs = new ArrayList<>();
                    List<String> uoieaStrs = new ArrayList<>();
                    List<String> aeiouyStrs = new ArrayList<>();

                    read().forEach(s -> {
                        String filtered = s.chars().mapToObj(ch -> Character.toString((char)ch)).filter(c -> c.matches("[aeiou]")).reduce((str, sub) -> str + sub).orElse("");

                        if (filtered.equals("aeiou")) aeiouStrs.add(s);
                        if (filtered.equals("uoiea")) uoieaStrs.add(s);
                    });

                    aeiouStrs.forEach(s -> {
                        String filtered = s.chars().mapToObj(ch -> Character.toString((char)ch)).filter(str -> str.matches("[aeiouy]")).reduce((str, sub) -> str + sub).orElse("");
                        if (filtered.equals("aeiouy")) aeiouyStrs.add(s);
                    });

                    System.out.println("'aeiou' words: " + ANSI_PURPLE + grammaticParse(aeiouStrs, ",") + ANSI_RESET);
                    System.out.println("\n'aeiouy' words: " + ANSI_RED + grammaticParse(aeiouyStrs, ",") + ANSI_RESET);
                    System.out.println("\n'uoiea' words: " + ANSI_CYAN + grammaticParse(uoieaStrs, ",") + ANSI_RESET);
                }



                case "shift" -> {
                    boolean isValid = false;
                    while (!isValid) {
                        int sel = (int) prompt("Select level to shift to (1-3).", "Error: invalid level.", 1,3,true);

                        if (sel != selLevel) {
                            System.out.println("Shifted level from " + selLevel + " to " + sel + ".");
                            selLevel = sel;
                            isValid = true;
                        }
                        else
                            System.out.println(ANSI_RED + "Error: cannot switch to already selected level (" + selLevel + ")!\n\n\n" + ANSI_RESET);
                    }
                }

                case "quit" -> {
                    System.exit(0);
                }
            }

            long posttime = System.currentTimeMillis();

            if (!selection.equals("shift"))
                yieldWait(ANSI_CYAN, "\n\n\n\n\n\n\n✔ Done (" + (posttime - pretime) + " ms).");
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        }
    }

    public String prompt(String message, boolean lineMode, boolean isCaseSensitive) // <+> APM
    {
        Scanner input = new Scanner(System.in);
        String nextInput;

        System.out.print(message);
        if (!message.equals(""))
            System.out.println();

        if (lineMode) {
            nextInput = input.nextLine();
        } else {
            nextInput = input.next();
        }

        if (!isCaseSensitive) {
            nextInput = nextInput.toLowerCase();
        }

        return nextInput;
    }

    public String prompt(String message, String errorMessage, String[] bounds, boolean lineMode, boolean isCaseSensitive) // <+> APM
    {
        Scanner input = new Scanner(System.in);
        String nextInput;

        while (true) {
            System.out.print(message);
            if (!message.equals(""))
                System.out.println();

            if (lineMode) {
                nextInput = input.nextLine();
            } else {
                nextInput = input.next();
            }

            if (!isCaseSensitive) {
                nextInput = nextInput.toLowerCase();

                for (int i = 0; i < bounds.length; i++)
                    bounds[i] = bounds[i].toLowerCase();
            }

            if (nextInput.matches(String.join("|", bounds)) || bounds[0].equals("")) {
                return nextInput;
            } else {
                System.out.println(ANSI_RED + errorMessage + "\n\n" + ANSI_RESET);
            }

        }
    }


    public String prompt(String message, String errorMessage, Pattern regex, boolean lineMode) // <+> APM
    {
        Scanner input = new Scanner(System.in);
        String nextInput;

        while (true) {
            System.out.print(message);
            if (!message.equals(""))
                System.out.println();

            if (lineMode) {
                nextInput = input.nextLine();
            } else {
                nextInput = input.next();
            }

            if (regex.matcher(nextInput).find()) {
                return nextInput;
            } else {
                System.out.println(ANSI_RED + errorMessage + "\n\n" + ANSI_RESET);
            }

        }
    }

    public double prompt(String message, String errorMessage, double min, double max, boolean isIntegerMode) {
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
                System.out.println(ANSI_RED + errorMessage + "\n\n" + ANSI_RESET);
            }
        }
    }

    public void fancyDelay(long delay, String loadMessage, String completionMessage, int iterations) throws InterruptedException { // Yoinked from SchudawgCannoneer
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

    public void yieldWait(AnsiCode color, String message) {
        prompt(color + message + " Type anything to continue..." + ANSI_RESET, false, false);
    }

    public ImmutableList<String> read() throws FileNotFoundException {
        List<String> list = new ArrayList<>();
        Scanner reader = new Scanner(df);

        while (reader.hasNext())
            list.add(reader.next());

        return ImmutableList.copyOf(list);
    }




    public static <T> String grammaticParse(T[] arr, String conjunction) { // <+> APM
        return Arrays.stream(arr).map(Object::toString).reduce((str, s) -> str + ((arr.length != 1 && !conjunction.isBlank()) ? conjunction + " " : " ") + s ).orElse("");
    }

    public static <E> String grammaticParse(Collection<E> col, String conjunction) {
        return grammaticParse(col.toArray(), conjunction);
    }

    @SafeVarargs
    public static <K,V> void putAll(Map<K,V> map, Map.Entry<K,V>... entries) {
        for (Map.Entry<K,V> entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }
    }

    public static String percentify(long count, long total) {
        return truncate((double) count * 100 / total, 2) + "%";
    }


    public static long getLineCount(File df) throws IOException {
        try (Stream<String> stream = Files.lines(df.toPath(), StandardCharsets.UTF_8)) {
            return stream.count();
        }
    }

    public static double truncate(double value, int mantissaLength) // <+> APM
    {
        return BigDecimal.valueOf(value).setScale(mantissaLength, RoundingMode.HALF_EVEN).doubleValue();
    }
}
