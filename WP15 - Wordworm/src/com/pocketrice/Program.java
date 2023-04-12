package com.pocketrice;

import java.io.File;
import java.io.IOException;

public class Program {
    public static void main(String[] args) throws IOException, InterruptedException {
        File df = new File("src/com/pocketrice/dict.txt");
        Wordworm ww = new Wordworm(df);
        ww.run();
    }
}
