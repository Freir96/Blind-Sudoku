package com.example.agajewski.sudoku;


import java.util.Arrays;
import java.util.Random;



/**
 * @author Adam Gajewski
 */
public class SmallSudoku {
    Integer[][] matrix;
    Integer[][] matrixToSolve;
    SmallTemplate template;
    int size;
    //AccessibilityManager am;

    public SmallSudoku() {

        matrix = new Integer[6][6];
        matrixToSolve = new Integer[6][6];
        size = 6;
        boolean[][] tmp = {{true, false, true, true, true, false},
                {true, false, false, false, false, true},
                {false, true, true, false, false, true},
                {false, true, true, true, true, false},
                {true, false, false, false, false, false},
                {false, true, false, true, true, true}};
        template = new SmallTemplate(tmp, Orientation.VERTICAL);
        generateSudoku();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //System.out.print(template.getPoint(j, j));
                if (template.getPoint(i, j))
                    matrixToSolve[i][j] = matrix[i][j];
                else
                    matrixToSolve[i][j] = 0;
            }
        }
        printFull();
        printToDo();
    }

    public boolean isFinished(){
        for(int j = 0; j < 6; j++){
            for(int i = 0; i < 6; i++){
                if(0 == matrixToSolve[i][j])
                    return false;
            }
        }
        return true;
    }

    public Integer getMatrixToSolve(int x, int y) {
        return matrixToSolve[x][y];
    }

    public void setMatrixToSolve(Integer matrixNumber,int x, int y ) {
        this.matrixToSolve[x][y] = matrixNumber;
    }

    public boolean getTemplate(int x, int y) {
        return template.getPoint(x, y);
    }

    private int getRowSum(int col){
        int sum = 0;
        for(int r = 0; r < size; r++){
            sum = sum + matrixToSolve[r][col];
        }
        return sum;
    }

    private int getColSum(int row){
        int sum = 0;
        for(int c = 0; c < size; c++){
            sum = sum + matrixToSolve[row][c];
        }
        return sum;
    }

    public boolean isCorrect() {
        for (int i = 0; i < size; i++) {
            /*for (int j = 0; j < size; j++) {
                if (matrix[i][j] != matrixToSolve[i][j])
                    return false;
            }*/
            if(getColSum(i) != 21 || getRowSum(i) != 21)
                return false;
        }
        return true;
    }

    public void printFull() {
        for (int j = 0; j < size; j++) {
            for (int i = 0; i < size; i++) {
                System.out.print(matrix[i][j]);
            }
            System.out.println();
        }
    }

    public void printToDo() {
        for (int j = 0; j < size; j++) {
            for (int i = 0; i < size; i++) {
                System.out.print(matrixToSolve[i][j]);
            }
            System.out.println();
        }
    }

    private void shiftBy(int shift, int lineY) {
        for (int i = shift; i < size; i++) {
            matrix[i][lineY] = matrix[i - shift][lineY - 1];
        }
        for (int i = 0; i < shift; i++) {
            matrix[i][lineY] = matrix[size - shift + i][lineY - 1];
        }
    }

    private void generateFirstLine() {
        Random generator = new Random();
        Integer[] tmparr = new Integer[6];
        int tmp;
        for (int i = 0; i < size; i++) {
            tmp = generator.nextInt(size) + 1;
            while (Arrays.asList(tmparr).indexOf(tmp) != -1) {
                tmp = generator.nextInt(size) + 1;
            }
            tmparr[i] = tmp;
        }
        for (int i = 0; i < size; i++) {
            matrix[i][0] = tmparr[i];
        }
    }

    private void generateSudoku() {
        generateFirstLine();
        int shift = 2;
        for (int i = 1; i < size; i++) {
            if (i % 3 == 0)
                shift = 1;
            else
                shift = 2;
            shiftBy(shift, i);
        }

    }
    /*
    public void testSolve(){
        matrix[0][0] = 0;
        SudokuSolver solver = new SudokuSolver();
        solver.solve(matrix,0);
        //System.out.println(solver.isSolution(matrix));
    }*/

    public static void main(String[] args) {
        SmallSudoku a = new SmallSudoku();
        a.printFull();
        System.out.println();
        a.printToDo();
        //a.testSolve();
    }

    private void solveSudoku() {

    }
}
