package com.example.agajewski.sudoku;

/**
 *
 * @author Aspire
 */
public class SmallTemplate {
    boolean[][] sudokuTamplate;
    Orientation rectangleOrientation;

    public SmallTemplate(boolean[][] template, Orientation o) {
        rectangleOrientation = o;
        this.sudokuTamplate = template;
    }
    public boolean getPoint(int x, int y){
        return sudokuTamplate[x][y];
    }
}
