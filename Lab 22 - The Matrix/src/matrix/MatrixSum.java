package matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// note to self: being lazy so don't reuse any of this!! lots of hardcoded and lazy code
public class MatrixSum {
    private int[][] cells;
    private int[][] matrix = {{1,2,3,4,5},{6,7,8,9,0},{6,7,1,2,5},{6,7,8,9,0},{5,4,3,2,1}};
    private File df;
    private int dfLength;

    public MatrixSum(File file) throws FileNotFoundException {
        df = file;
        dfLength = new Scanner(df).nextInt();
        extractCells();
    }

    public void processCells() {
        for (int i = 0; i < cells.length; i++) {
            System.out.println(readCell(cells[i][0], cells[i][1]));
        }
    }
    public void extractCells() throws FileNotFoundException {
        Scanner reader = new Scanner(df);
        reader.nextInt(); // skip first line

        cells = new int[dfLength][2];
        for (int i = 0; i < dfLength; i++) {
            for (int j = 0; j < 2; j++) {
                cells[i][j] = reader.nextInt();
            }
        }
    }

    public int readCell(int row, int col) {
        int cellVal = 0;
        for (int i = Math.max(0,row - 1); i <= Math.min(row + 1, matrix.length - 1); i++) {
            for (int j = Math.max(0, col - 1); j <= Math.min(col + 1, matrix[0].length - 1); j++) {
                cellVal += matrix[i][j];
            }
        }

        return cellVal;
    }
}