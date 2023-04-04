// WP5b - Stats That Actually Try Stuff (STATS) Rewrite - Lucas Xie 1/12/23 AP CSA P5

package stats;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Program { // create Datafiles, run stats on DFs, handle input
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    private List<Datafile> datafiles;
    private long operationTime = -1; // opTime formatted in millisecs

    // Basic func'l
    // * Rich files (datafiles).
    // * Several file support
    // * Time taken for operation
    // * Split files (specify which line # only)
    // * Merge files (simply appends to end)
    // * Manage files (rename, sort)
    // * Selective ops

    public Program(List<Datafile> dfs) {
        datafiles = dfs;
    }

    public Program(List<Datafile> dfs, long prevTime) {
        datafiles = dfs;
        operationTime = prevTime;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Path dfDirectory = Path.of("src/data");
        Program program = new Program(retrieveDatafiles(dfDirectory));
        boolean isSameExistingFile = false;
        Datafile df = null;

        while (true) {

            // File selection. This is skipped if isSameExistingFile is true, which depends on a user response at the end of a cycle.
            if (!isSameExistingFile) {
                program.datafiles = retrieveDatafiles(dfDirectory);
                switch (prompt(ANSI_BLUE + "◇ Would you like to analyze an existing datafile, generate a new datafile, or terminate?" + ANSI_RESET, "Error: invalid option. Please select from 'EXISTING', 'NEW', or 'TERMINATE'.", new String[]{"existing", "new", "terminate"}, false, false)) {

                    // Existing and valid datafiles are pulled from the src/data folder. The user is notified of which datafiles are available.
                    case "existing" -> {
                        System.out.println(ANSI_CYAN + "Found a total of " + program.datafiles.size() + " datafiles in directory 'src/data/'." + ANSI_RESET);
                        System.out.println("➢ Recognized datafiles: " + grammaticParse(program.datafiles.stream().map(File::getName).collect(Collectors.toSet()).toArray(), "") + "\n");

                        df = new Datafile("src/data/" + prompt(ANSI_BLUE + "Select a datafile to access." + ANSI_RESET, "Error: invalid name.", toStringArray(program.datafiles.stream().map(d -> d.getName()).collect(Collectors.toSet())), false, false));
                        Statistics stats = new Statistics(df);
                    }


                    // A new datafile is generated based on parameters set by the user. There is the option to overwrite if the specified file already exists.
                    case "new" -> {
                        String dfname = prompt(ANSI_PURPLE + "Name the new file (including filetype)." + ANSI_RESET, "Error: invalid name.", new String[]{""}, false, true);
                        boolean shouldOverwriteFile = (program.checkForExistingFile(dfname)) ? Boolean.parseBoolean(prompt("The file '" + dfname + "' already exists. Overwrite?", "Error: invalid option.", new String[]{"true", "false"}, false, false)) : true;
                        if (!shouldOverwriteFile) dfname = dfname + "_copy"; // QUICK FIX PLEASE IGNORE
                        int dataCount = (int) prompt(ANSI_PURPLE + "Set the amount of data to generate." + ANSI_RESET, "Error: invalid amount.", 1, Integer.MAX_VALUE, true);
                        int lowerBound = (int) prompt(ANSI_PURPLE + "Set the lower bound for number generation." + ANSI_RESET, "Error: invalid number.", 0, Integer.MAX_VALUE, true);
                        int upperBound = (int) prompt(ANSI_PURPLE + "Set the upper bound for number generation." + ANSI_RESET, "Error: invalid number.", 0, Integer.MAX_VALUE, true);

                        df = new Datafile(program.generateData(dfname, dataCount, lowerBound, upperBound, shouldOverwriteFile).getPath());
                        program.datafiles.add(df);
                    }


                    // The program is terminated safely.
                    case "terminate" -> {
                        System.out.println(ANSI_GREEN + "(!) Finished operations at " + parseTime(Datafile.getMillisecSinceUnixEpoch()) + "." + ANSI_RESET);
                        System.exit(0);
                    }
                }
            }


            // Prompts the user to select an operation to conduct on the specified datafile.

            boolean isUsingDatafile = true;

            while (isUsingDatafile) {
                long pretime = Datafile.getMillisecSinceUnixEpoch(); // This timestamp is used later to calculate the amount of time in ms taken to do an operation.

                switch (prompt(ANSI_BLUE + "\n\n◇ Select an operation to execute on datafile " + df + "." + ANSI_RESET, "Error: invalid operation. You may MERGE, SPLIT, ANALYZE, see INFO, or EXIT operations.", new String[]{"merge", "split", "analyze", "info", "rename", "delete", "exit"}, false, false)) {
                    case "merge" -> { // Merge two datafiles together. This generates a completely new file, rather than overwriting one of them.
                        Datafile dfb = getDatafileByName(program.datafiles, "src/data/" + prompt(ANSI_YELLOW + "Merge with which datafile?" + ANSI_RESET, "Error: that datafile does not exist.", toStringArray(program.datafiles.toArray()), false, false));
                        program.datafiles.add(mergeDatafiles(df, dfb));
                        program.datafiles.remove(dfb);
                        program.datafiles.remove(df);

                    }

                    case "split" -> { // Split datafile at specified line number. This generates two new datafiles rather than overwrite the prev. file.
                        int splitLine = (int) prompt(ANSI_YELLOW + "Split file at which data line?" + ANSI_RESET, "Error: invalid line number (0-" + df.totalCount + ")", 0, df.totalCount - 1, true);
                        program.datafiles.addAll(splitDatafile(splitLine, df));
                        program.datafiles.remove(df);
                    }

                    case "analyze" -> { // Print calculated statistics for the datafile.
                        df.modificationTime = Datafile.getMillisecSinceUnixEpoch();

                        System.out.println(ANSI_CYAN + "➢ Mean: " + df.mean);
                        System.out.println("  Mode(s): " + grammaticParse(df.mode.toArray(), "and"));
                        System.out.println("  Range: " + Arrays.toString(df.range));
                        System.out.println(ANSI_PURPLE + "➢ Standard Deviation (popu.): " + df.pStDev);
                        System.out.println("  Standard Deviation (samp.): " + df.sStDev);
                        System.out.println("  Variance (popu.): " + df.pVariance);
                        System.out.println("  Variance (samp.): " + df.sVariance);
                        System.out.println(ANSI_BLUE + "➢ Quartile I: " + df.firstQuartile);
                        System.out.println("  Quartile II: " + df.secondQuartile);
                        System.out.println("  Quartile III: " + df.thirdQuartile);
                        System.out.println("  Interquartile range: " + df.interquartileRange);

                        System.out.println(ANSI_GREEN + "\n(✓)" + ANSI_RESET + " Processed a total of " + df.totalCount + " numbers (" + df.evenCount + " even, " + df.oddCount + " odd) with " + df.luckySevenCount + " being lucky sevens!");
                    }

                    case "info" -> { // Add'l file info such as file size and timestamps are printed.
                        df.modificationTime = Datafile.getMillisecSinceUnixEpoch();

                        System.out.println("➢ Filepath: " + df.getPath());
                        System.out.println("➢ Total data: " + df.totalCount);
                        System.out.println("➢ Size: " + Datafile.parseOptimalSize(df.getFileSize(), df.sizeUnit) + " " + df.sizeUnit);
                        System.out.println("➢ Creation time: " + parseTime(df.creationTime)); // Write to separate file in order to store ctime properly (this right now only stores instantiation time)
                        System.out.println("➢ Last modified time: " + parseTime(df.modificationTime));
                        if (program.operationTime != -1)
                            System.out.println("➢ Last operation time: " + program.operationTime + " ms");
                    }

                    case "rename" -> { // Rename file (including filetype suffix).
                        int dfIndex = program.datafiles.indexOf(getDatafileByName(program.datafiles, df.getName()));
                        if (df.renameTo(new File("src/data/" + prompt("Set the new name of file " + df.getName() + ".", "Error: invalid file name.", new String[]{""}, false, false)))) {
                            program.datafiles.set(dfIndex, df); // Update Program datafile set
                            System.out.println(ANSI_GREEN + "Successfully renamed file to " + df.getName() + ".");
                        } else System.out.println(ANSI_RED + "Error: failed to rename file " + df + ".");
                    }

                    case "delete" -> { // Delete a file.
                        switch (prompt(ANSI_RED + "Warning! This will permanently delete this file (a really long time!). Confirm your choice by typing " + ANSI_RESET + df.getName() + ANSI_RED + " or type 'CANCEL'.", "Error: invalid choice.", new String[]{"cancel", df.getName()}, false, true)) {
                            case "cancel" -> {
                                break;
                            }

                            default -> { // The only other option MUST be df.getName(); this is a workaround since switch only accepts consts
                                program.datafiles.remove(df);
                                System.out.println("◇ Successfully deleted file " + df.getName() + ".");
                                df.delete();
                                isSameExistingFile = false;
                            }
                        }
                    }

                    case "exit" -> {
                        // Ask the user whether or not they want to continue operations on the selected file. This sets isSameExistingFile for use in the beginning of another "cycle".
                        switch (prompt(ANSI_BLUE + "Stop working on file " + df.getName() + "?" + ANSI_RESET, "Error: invalid choice.", new String[]{"yes", "no"}, false, false)) {
                            case "yes" -> {
                                isUsingDatafile = false;
                            }
                            case "no" -> {
                                isUsingDatafile = true;
                            }
                        }
                    }
                }
                // Calculate and print amount of time taken to do operation.
                long posttime = Datafile.getMillisecSinceUnixEpoch();
                program.operationTime = posttime - pretime;
                System.out.println(ANSI_GREEN + "❖ Operation successful (took " + program.operationTime + " ms).\n" + ANSI_RESET);
            }
        }
    }


    // Splits the datafile into two separate files. The old file is not overwritten as it is trashed.
    // @param line - line # to split file at
    // @param df - datafile to be split
    // @return two resulting datafiles from splitting at specified line #
    private static Set<Datafile> splitDatafile(int line, Datafile df) throws IOException {
        File dfa = new File("src/data/" + df.basename + "_A.txt");
        File dfb = new File("src/data/" + df.basename + "_B.txt");

        try {
            Scanner reader = new Scanner(df);
            FileWriter dataWriter = new FileWriter(dfa.getPath());

            for (int i = 0; i < line; i++) {
                dataWriter.write(reader.nextLine() + "\n");
            }

            dataWriter = new FileWriter(dfb.getPath());
            for (int i = line; i < df.totalCount; i++) {
                dataWriter.write(reader.nextLine() + "\n");
            }

            dataWriter.close();
            System.out.println(ANSI_RESET + "Split file " + df + " at line " + line + " into files " + dfa + " and " + dfb + ".\n\n" + ANSI_RESET);

        }
        catch (Exception e) {
            System.out.println(ANSI_RED + "(x) Error: " + e + ANSI_RESET);
            e.printStackTrace();
        }

        return Set.of(new Datafile(dfa.getPath()), new Datafile(dfb.getPath()));
    }


    // Merge two datafiles together. The old file is not overwritten as it is trashed.
    // @param dfa - first datafile; in new file precedes second file
    // @param dfb - second datafile
    // @return merged datafile (order: dfa, dfb)
    private static Datafile mergeDatafiles(Datafile dfa, Datafile dfb) throws IOException {
        File dfc = new File("src/data/" + dfa.basename + "_merged.txt");

        try {
            Scanner readerA = new Scanner(dfa);
            Scanner readerB = new Scanner(dfb);
            FileWriter dataWriter = new FileWriter(dfc.getPath());

            for (int i = 0; i < dfa.totalCount; i++) {
                dataWriter.write(readerA.nextLine() + "\n");
            }
            for (int i = 0; i < dfb.totalCount; i++) {
                dataWriter.write(readerB.nextLine() + "\n");
            }

            dataWriter.close();
            System.out.println(ANSI_RESET + "Wrote " + Datafile.getDataLength(dfc) + " data to file " + dfc.getName() + "!\n\n" + ANSI_RESET);

        }
        catch (Exception e) {
            System.out.println(ANSI_RED + "(x) Error: " + e + ANSI_RESET);
            e.printStackTrace();
        }

        return new Datafile(dfc);
    }


    // Search through a specified directory for valid datafiles.
    // @param directoryPath - absolute path to valid directory
    // @return list of all valid datafiles in specified directory
    public static List<Datafile> retrieveDatafiles(Path directoryPath) {
        File directory;
        List<Datafile> datafiles = new ArrayList<>();

        try {
            directory = new File(directoryPath.toUri());
            assert (directory.isDirectory()) : "Validated file but failed to read directory of " + directoryPath.toString();

            for (File file : directory.listFiles()) {
                if (Datafile.isDatafile(file)) {
                    datafiles.add(new Datafile(file.getPath()));
                }
            }
        }
        catch (Exception e) {
            System.err.println("Invalid file/directory at " + directoryPath.toString() + ": " + e);
        }

        return datafiles;
    }


    // A purely aesthetic fancy spinning "progress indicator" for dramatic effect.
    // @param delay - how long each interval waits
    // @param loadMessage - message to display alongside indicator
    // @param completionMessage - message to display after done
    // @param iterations - how many times to spin
    // @returns N/A
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


    // Given an array of any object type, generates a grammatically correct list in English parsed with commas and a conjunction for the last 2 items.
    // @param array - generic array of objects
    // @param conjunction - string to be used to grammatically conjoin last two items (e.g. "this, that, AND those")
    // @return grammatically parsed string
    public static <T> String grammaticParse(T[] array, String conjunction) { // Yoinked from Digitridoo
        StringBuilder gParsedString = new StringBuilder(Arrays.toString(array));

        gParsedString.deleteCharAt(0)
                .deleteCharAt(gParsedString.length()-1);

        if (array.length > 1 && !conjunction.equals("")) gParsedString.insert(gParsedString.lastIndexOf(",") + 1, " " + conjunction);

        return gParsedString.toString();
    }



    // ** NOTE: the file is NOT a dat. Thus the first line is not a data length indicator.

    // Generates and returns a new datafile according to specified specs.
    // @param fileName - name of datafile (including filetype suffix)
    // @param dataCount - number of data to generate
    // @param lowerBound - lowest possible value generated, inclusive
    // @param upperBound - highest possible value generated, exclusive
    // @param shouldOverwriteFile - in the event the specified file exists, should it be overwritten?
    // @return datafile specific to specified parameters
    public File generateData(String fileName, int dataCount, int lowerBound, int upperBound, boolean shouldOverwriteFile) throws IOException, InterruptedException {
        File dataFile = new File("src/data/" + fileName);
        if (!dataFile.createNewFile() && shouldOverwriteFile) {
            System.out.println(ANSI_RED + "(!) File already exists! Overwriting " + dataFile.getName() + " (prev. " + Datafile.getDataLength(dataFile) + " lines)." + ANSI_RESET);
            dataFile.delete();
        }

        fancyDelay(300, ANSI_CYAN + "❖ Writing new data file..." + ANSI_RESET, "", 4);

        try {
            FileWriter dataWriter = new FileWriter(dataFile.getPath());

            for (int i = 0; i < dataCount; i++) {
                dataWriter.write((int) (Math.random() * (upperBound - lowerBound)) + lowerBound + "\n");
            }
            dataWriter.close();
            System.out.println(ANSI_RESET + "Wrote " + Datafile.getDataLength(dataFile) + " data to file " + dataFile.getName() + "!\n\n" + ANSI_RESET);

        }
        catch (Exception e) {
            System.out.println(ANSI_RED + "(x) Error: " + e + ANSI_RESET);
            e.printStackTrace();
        }

        return dataFile;
    }


    // Prompts the user with a message and returns user input; allows for restraints, case sensitivity, a toggle for next() or nextLine(), and handles error messages.
    // @param message - initial text message to display to the user
    // @param errorMessage - message to display in the event the input is invalid (does not match bounds)
    // @param bounds - array of strings that rep. a whitelist of acceptable inputs
    // @param lineMode - whether or not to read next() or nextLine()
    // @param isCaseSensitive - whether or not to ignore case
    // @return user-inputted string that is valid
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


    // The "numeric equivalent" of the prompt outlined previously. Bounds are instead numbers rather than a whitelist, a toggle between integer and double is available.
    // @param message - same as prev prompt
    // @param errorMessage - same as prev prompt
    // @param min - lowest bound, exclusive
    // @param max - highest bound, exclusive
    // @param isIntegerMode - whether or not to only accept integers
    // @return user-inputted double/integer that is valid
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

    // Given an array of any objects, generates an equal-size array with each item parsed via their toString().
    // @param array - generic array of objects
    // @return same-sized array with each object toString()ed
    public static <T> String[] toStringArray(T[] array) { // <+> APM
        String[] newArray = new String[array.length];

        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i].toString();
        }

        return newArray;
    }


    // Overload of toStringArray() for Sets of objects.
    // @param set - generic set of objects
    // @return same-sized array with each object toString()ed
    public static <T> String[] toStringArray(Set<T> set) { // <+> APM
        String[] newArray = new String[set.size()];
        int index = 0;

        for (T item : set) {
            newArray[index] = item.toString();
            index++;
        }

        return newArray;
    }


    // Converts from milliseconds since Unix epoch to human-readable time and date.
    // @param msSinceUnixEpoch - milliseconds since Unix epoch (0:00 1/1/1970)
    // @return human-readable time and date
    public static String parseTime(long msSinceUnixEpoch) {
        Date date = new Date(msSinceUnixEpoch);
        DateFormat dformat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        dformat.setTimeZone(TimeZone.getDefault());

        return dformat.format(date);
    }


    // Checks the Program's datafiles if a datafile of a specified name exists.
    // @param dfname - name of datafile
    // @return whether or not program.datafiles contains a datafile of specified name
    public boolean checkForExistingFile(String dfname) {
        for (Datafile df : datafiles) {
            if (df.getName().equals(dfname)) return true;
        }

        return false;
    }


    // Returns a datafile from a list of datafiles given its String name, if it exists.
    // @param dfs - any list of datafiles
    // @param dfname - name of datafile
    // @return datafile of specified name from specified list, if existent
    public static Datafile getDatafileByName(List<Datafile> dfs, String dfname) {
        for (Datafile df : dfs) {
            if (df.getName().equals(dfname)) return df;
        }

        return null;
    }
}
