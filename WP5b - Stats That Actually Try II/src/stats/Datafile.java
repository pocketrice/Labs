// WP5b - Stats That Actually Try Stuff (STATS) Rewrite - Lucas Xie 1/12/23 AP CSA P5

package stats;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Datafile extends File { // datafile is the same as File but with some extra things (metadata). Stats are handled in 'Statistics' (despite being file-specific), and everything else that's file-specific is kept in Datafile. Maybe.
    // Datafile should keep metadata, op units, line counts, file type, etc. in mind. This reduces Statistics class to just handling stats stuff.


    // While stats are handled in Statistics, each Datafile must house its individual stats. So the privs are per-file but their calcs are from Stats.
    String basename;
    final long creationTime;
    long modificationTime, byteSize = -1;
    double mean, pVariance, sVariance, pStDev, sStDev, firstQuartile, secondQuartile, thirdQuartile, interquartileRange;
    int[] range, numbers;
    List<Integer> mode;
    int totalCount, oddCount, evenCount, luckySevenCount;
    optimalUnit sizeUnit;


    // These units are used for determining and printing the size of a file.
    public enum optimalUnit {
        B,
        KB,
        MB,
        GB
    }



    // Constructor using a String pathname.
    public Datafile(String pathname) throws IOException {
        super(pathname);
        assert (isDatafile(this)) : "Error: file at " + pathname + " is not a datafile.";
        basename = this.getName().substring(0,this.getName().lastIndexOf('.'));
        creationTime = getMillisecSinceUnixEpoch();
        modificationTime = getMillisecSinceUnixEpoch();
        byteSize = getFileSize();
        sizeUnit = getOptimalUnit(this);
        populateStatistics(new Statistics(this));
    }


    // Constructor using a File object.
    public Datafile(File file) throws IOException {
        super(file.getPath());
        assert (isDatafile(this)) : "Error: inherited file is not a datafile.";
        basename = this.getName().substring(0,this.getName().lastIndexOf('.'));
        creationTime = getMillisecSinceUnixEpoch();
        modificationTime = getMillisecSinceUnixEpoch();
        byteSize = getFileSize();
        sizeUnit = getOptimalUnit(this);
        populateStatistics(new Statistics(this));
    }

    // Returns milliseconds since 0:00 1/1/1970 (Unix epoch).
    // @param N/A
    // @return milliseconds since Unix epoch (0:00 1/1/1970)
    public static long getMillisecSinceUnixEpoch() { // unix epoch = 1/1/1970
        return System.currentTimeMillis();
    }


    // Returns the file size of the Datafile in bytes.
    // @param N/A
    // @return file size at the file at datafile's absolute path in bytes
    public long getFileSize() throws IOException {
        return Files.size(Path.of(this.getAbsolutePath()));
    }


    // Determines the optimal unit for a given datafile's size.
    // @param df - datafile
    // @return optimal size unit
    public static optimalUnit getOptimalUnit(Datafile df) {
        assert (df.byteSize != -1) : "Warning: Datafile " + df.getName() + " byte size not initialized.";
        if (df.byteSize < 1000) return optimalUnit.B;
        else if (df.byteSize < 1000000) return optimalUnit.KB;
        else if (df.byteSize < 1000000000) return optimalUnit.MB;
        else return optimalUnit.GB;
    }


    // Given datafile size (bytes) and optimal unit, returns the size in that unit.
    // @param size - size of datafile in bytes
    // @param ou - calculated optimal unit
    // @return size of datafile in OU
    public static double parseOptimalSize(long size, optimalUnit ou) {
        double optimalSize = 0;

        switch (ou) {
            case B -> {
                optimalSize = size;
            }

            case KB -> {
                optimalSize = size / 1000d;
            }

            case MB -> {
                optimalSize = size / 1000000d;
            }

            case GB -> {
                optimalSize = size / 1000000000d;
            }
        }

        return Statistics.truncate(optimalSize, 2);
    }



    // Determines whether or not a File can be considered a valid Datafile.
    // @param file - any File
    // @return whether or not the file is a valid datafile
    public static boolean isDatafile(File file) throws FileNotFoundException {
        Scanner reader = new Scanner(new FileReader(file));
        // For a datafile to be valid it must contain N lines with each line containing an integer (N integers).
        int lineCount = getLineCount(file);
        int dataCount = 0;

        while (reader.hasNextLine()) {
            try {
                Integer.parseInt(reader.nextLine());
                dataCount++;
            }
            catch (Exception e) {
                return false;
            }
        }

        return (lineCount == dataCount);
    }


    // Retrieves the number of lines in a File.
    // @param file - any File
    // @return # of lines in file
    public static int getLineCount(File file) throws FileNotFoundException {
        Scanner reader = new Scanner(new FileReader(file));
        int lineCount = 0;

        while (reader.hasNextLine()) {
            lineCount++;
            reader.nextLine();
        }

        return lineCount;
    }



    // Calculates and populates statistic metrics for the given Datafile.
    // @param st - a Statistics object
    // @return N/A
    public void populateStatistics(Statistics st) throws IOException {
        numbers = extractData(this);
        Arrays.sort(numbers);

        totalCount = st.getMetrics()[0];
        oddCount = st.getMetrics()[1];
        evenCount = st.getMetrics()[2];
        luckySevenCount = st.getMetrics()[3];

        mean = st.getMean();
        mode = st.getMode();
        range = st.getRange(); // No assertion necessary b/c getRange() always returns 2-length array
        pStDev = st.getStandardDeviation(true);
        sStDev = st.getStandardDeviation(false);
        pVariance = st.getVariance(pStDev);
        sVariance = st.getVariance(sStDev);

        firstQuartile = st.getQuartiles()[0];
        secondQuartile = st.getQuartiles()[1];
        thirdQuartile = st.getQuartiles()[2];
        interquartileRange = st.getQuartiles()[3];
    }


    // Extracts all data from a Datafile into an array of integers.
    // @param file - any File
    // @return ordered array of all integer data from file
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



    // Retrieves the number of data in a Datafile.
    // @param file - any File
    // @return # of data in file
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
