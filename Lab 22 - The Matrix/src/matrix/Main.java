package matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static matrix.AnsiCode.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("You take the " + ANSI_BLUE + "blue pill" + ANSI_RESET + "... the story ends, you wake up in your bed and you'll return to your blissful, oblivious life of summing the Matrix's cells. You take the " + ANSI_RED + "red pill" + ANSI_RESET + "... you play a nice little game of Tic-Tac-Toe, and I show you how deep the rabbit hole goes.");
        switch(prompt(ANSI_YELLOW + "(Type either RED or BLUE to make your selection. Good luck, Neo.)" + ANSI_RESET, "Error: invalid choice.\n\n", new String[]{"red", "blue"}, false, false)) {
            case "red" -> {
                System.out.println(ANSI_RED);
                MatrixTicTacToe mt = new MatrixTicTacToe(new File("src/matrix/tictactoe.dat"));
                mt.processBoards();
            }

            case "blue" -> {
                System.out.println(ANSI_BLUE);
                MatrixSum ms = new MatrixSum(new File("src/matrix/matsum.dat"));
                ms.processCells();
            }
        }
    }

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
}