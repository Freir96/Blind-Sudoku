/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.agajewski.sudoku;

//import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 *
 * @author Aspire
 */
public class BlindSudoku {
    int[][] solved = new int[6][6];
    int[][] toSolve = new int[6][6];
    final int n = 6;
    final int xSpace = 2;
    final int ySpace = 3;
    
    public int[][] getSolved() {
        return solved;
    }

    public void setSolvedNumber(int number, int x, int y) {
        this.solved[x][y] = number;
    }
    
    boolean isInLine(int x, int y, int val){
        for(int i = 0; i < n; i++){
            if(solved[x][i] == val || solved[i][y] == val)
                return true;
        }
        return false;
    }
    
    boolean isInRect(int x, int y, int val){
        int rectX = x / xSpace;
        int rectY = y / ySpace;
        for(int i = 0, j = 0; j < ySpace;){
            System.out.println(i + " " + j);
            System.out.println((xSpace * rectX) + " " + (ySpace * rectY));
            System.out.println((int)(xSpace * rectX + i) + " " + (int)(ySpace * rectY + j));
            System.out.println();
            if(solved[xSpace *rectX + i][ySpace *rectY + j] == val){
                return true;
            }  
            i++;
            if(i == xSpace){
                i = 0;
                j++;
            }
        }
        return false;
    }
    
    final int randomGenerator(int num) { 
        return (int) Math.floor((Math.random()*num+1)); 
    }
    
    void fillDiagonal(){
        for(int i = 0; i < n; i++){
            solved[i][i] = randomGenerator(6);
        }
    }
    
    boolean fillRemaining(int i, int j) { 
        //  System.out.println(i+" "+j); 
        if (j>=n && i<n-1) { 
            i = i + 1; 
            j = 0; 
        } 
        if (i>=n && j>=n) 
            return true; 
  
        if (i < xSpace) { 
            if (j < ySpace) 
                j = ySpace; 
        } 
        else if (i < n-xSpace) { 
            if (j==(int)(i/ySpace)*xSpace) 
                j =  j + ySpace; 
        } 
        else{ 
            if (j == n-ySpace) { 
                i = i + 1; 
                j = 0; 
                if (i>=n) 
                    return true; 
            } 
        } 
        for (int num = 1; num <= n; num++) { 
            if (!isInLine(i, j, num) && !isInRect(i, j, num)) { 
                solved[i][j] = num; 
                if (fillRemaining(i, j+1)) 
                    return true; 
                solved[i][j] = 0; 
            } 
        } 
        return false; 
    }
    
    final void randomRemove(){
        int removeX = randomGenerator(5);
        int removeY = randomGenerator(5);
        while(solved[removeX][removeY] == 0){
            removeX = randomGenerator(5);
            removeY = randomGenerator(5);
        }
        solved[removeX][removeY] = 0;
    }
    
    final boolean fillIn(){
        int val;
        for(int j = 0; j < n; j++){
            for(int i = 0; i < n; i++){
                if(solved[i][j] == 0){
                    val = randomGenerator(6);
                    if(isInLine(i, j, val) || isInRect(i, j, val)){
                        val = 1;
                        while((isInLine(i, j, val) || isInRect(i, j, val)) && val < 7){
                            val++;
                        }
                        if(val < 7)
                            setSolvedNumber(val, i, j);
                        else
                            return false;
                    }
                    else
                        setSolvedNumber(val, i, j);
                }
            }
        }
        return true;
    }
    
    public BlindSudoku(){
        int val;
        boolean finished = false;
        //while(!finished){
            for(int i = 0; i < n; i++){
                for(int j = 0; j < n; j++){
                    solved[i][j] = 0;
                }
            }

        while(!fillIn()){
            randomRemove();
        }
        
    }
    public void printSudoku(){
        for(int y = 0; y < n; y++){
            if(y % 3 == 0)
                System.out.println();
            for(int x = 0; x < n; x++){
                System.out.print(" ");
                if(x % 2 == 0)
                    System.out.print(" ");
                System.out.print(solved[x][y]);
                
            }
            System.out.println();
            
        }
        System.out.println();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BlindSudoku blindSudoku = new BlindSudoku();
        blindSudoku.printSudoku();
    }
    
}
