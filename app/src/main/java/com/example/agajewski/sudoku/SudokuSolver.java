/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.agajewski.sudoku;

import java.util.Arrays;

/**
 *
 * @author Aspire
 */
public class SudokuSolver {
    Integer[][] matrix;
    
    public SudokuSolver() {
    }
    
    public SudokuSolver(Integer[][] matrix) {
        this.matrix = matrix;
    }
    
    void solve(Integer[][] sudoku, int idx){
        System.out.println(idx);
        int size = sudoku.length;
        if(idx == size*size){
            if(isSolution(sudoku)){
                System.out.println("Found a solution via very naive algorithm: ");
               // printSolution(sudoku);
                for(int x = 0; x < 6; x++){
                    for(int y = 0; y < 6; y++){
                        System.out.print(sudoku[y][x]);
                    }
                    System.out.println();
                }
                System.out.println();
            }
        } else{
            int row = idx / size;
            int col = idx % size;
            if(sudoku[row][col] != 0){ // the square is already filled in, don't do anything 
                solve(sudoku,idx+1);
            } else{
                for(int i = 1; i <= 6; i++){
                    sudoku[row][col] = i;
                    solve(sudoku,idx+1); // continue with the next square
                    sudoku[row][col] = 0; // unmake move
                }
            }
        }
    }
    
    // Precondition: `sudoku` all contains numbers from 1..9 and is a 9x9 board
    // Returns true if and only if sudoku is a valid solved sudoku board
    boolean isSolution(Integer[][] sudoku){
        final int N = 6;
        final int side = 2;
        final int side2 = 3;
        boolean[] mask = new boolean[N+1];
        
        // Check rows
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                int num = sudoku[i][j];
                //System.out.println(i + " " + j + " " + num);
                if(mask[num]) 
                    return false;
                mask[num] = true;
            }
            Arrays.fill(mask,false);
        }
        
        // Check columns
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                int num = sudoku[j][i];
                if(mask[num]) return false;
                mask[num] = true;
            }
            Arrays.fill(mask,false);
        }
        
        // Check subsquares
        
        for(int i = 0; i < N; i += side){
            for(int j = 0; j < N; j+= side2){
                Arrays.fill(mask,false);
                for(int di = 0; di < side; di++){
                    for(int dj = 0; dj < side2; dj++){
                        int num = sudoku[i+di][j+dj];
                        if(mask[num]) return false;
                    }
                }
            }
        }
        
        return true; // Everything validated!
    }

    public void runSolver(Integer[][] sudoku){
        solve(sudoku,0);
    }
    public static void main(String[] args){
        Integer[][] a = {{1, 0, 6, 0, 0, 2},
                        {2, 5, 0, 0, 4, 1},
                        {0, 0, 2, 1, 0, 0},
                        {0, 6, 1, 4, 2, 0},
                        {3, 0, 0, 0, 0 ,0},
                        {0, 1, 0, 2, 3, 4}};
        SudokuSolver sol = new SudokuSolver();
        sol.runSolver(a);
    }
}
