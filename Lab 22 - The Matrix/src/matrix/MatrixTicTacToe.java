package matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class MatrixTicTacToe{
    private char[][] board = new char[3][3];
    private File df;
    private int dfLength;
    private String[] boards;

    public MatrixTicTacToe(File file) throws FileNotFoundException {
        df = file;
        dfLength = new Scanner(df).nextInt();
        extractBoards();
    }

    public void processBoards() {
        for (int i = 0; i < boards.length; i++) {
            loadBoard(i);
            System.out.println(analyzeBoard() + "\n\n");
        }
    }
    public void extractBoards() throws FileNotFoundException {
        boards = new String[dfLength];
        Scanner reader = new Scanner(df);
        reader.nextLine();

        for (int i = 0; i < dfLength; i++) {
            boards[i] = reader.nextLine();
        }
    }

    public void loadBoard(int index) {
        if (index > dfLength) throw new IndexOutOfBoundsException();
        char[] charset = boards[index].toCharArray();

        for (int i = 0; i < 3; i++) {
            board[i] = new char[]{charset[i * 3], charset[1 + i * 3], charset[2 + i * 3]};
        }
    }

    public String analyzeBoard() {
        for (char[] row : board) {
            System.out.println(row[0] + " " + row[1] + " " + row[2] + "\n");
        }
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2]) return ((board[i][0] == 'O') ? "Noughts" : "Crosses") + " win. (horizontal - row " + (i+1) + ")";
            if (board[0][i] == board[1][i] && board[2][i] == board[1][i]) return ((board[i][0] == 'O') ? "Noughts" : "Crosses") + " win. (vertical - col " + (i+1) + ")";
        }

        if ((board[0][0] == board[1][1] && board[2][2] == board[1][1])) return ((board[1][1] == 'O') ? "Noughts" : "Crosses") + " win. (major diagonal)";
        if ((board[0][2] == board[1][1] && board[2][0] == board[1][1])) return ((board[1][1] == 'O') ? "Noughts" : "Crosses") + " win. (minor diagonal)";
        else return "It's a tie.";
    }

}