/**
 * Sudoku Solving by GA 
 * By Maulik Shah
 * 06 Dec,2016
 */
package edu.uga.ai.ec.run;

import edu.uga.ai.ec.*;
import java.util.*;

/**
 * This class runs the GA for the Sudoku Puzzle.
 * @author Maulik
 */
public class RunSudokuGA {
    //Main Method.
    public static void main(String[] args){
//Create the Puzzle.
// Very Easy
//         ArrayList<Integer> firstRow = new ArrayList<Integer>(Arrays.asList(3, 5, 2,4, 7, 6, 0, 0, 0));
//         ArrayList<Integer> secondRow = new ArrayList<Integer>(Arrays.asList(1, 6, 8, 9, 5, 0, 0, 0, 4));
//         ArrayList<Integer> thirdRow = new ArrayList<Integer>(Arrays.asList(0, 4, 9, 0, 0, 3, 6, 0, 5));
//         ArrayList<Integer> fourthRow = new ArrayList<Integer>(Arrays.asList(0, 2, 5, 0, 9, 0, 8, 1, 0));
//         ArrayList<Integer> fifthRow = new ArrayList<Integer>(Arrays.asList(6, 0, 3, 0, 4, 1, 0, 0, 7));
//         ArrayList<Integer> sixthRow = new ArrayList<Integer>(Arrays.asList(0, 7, 1, 0, 0, 0, 0, 0, 2));
//         ArrayList<Integer> seventhRow = new ArrayList<Integer>(Arrays.asList(0, 9, 0, 3, 0, 5, 2, 4, 0));
//         ArrayList<Integer> eighthRow = new ArrayList<Integer>(Arrays.asList(0, 1, 0, 0, 0, 9, 0, 5, 6));
//         ArrayList<Integer> ninthRow = new ArrayList<Integer>(Arrays.asList(5, 0, 0, 1, 0, 4, 9, 7, 0));
        
//       Create the Puzzle.
//       Easy.
//         ArrayList<Integer> firstRow = new ArrayList<Integer>(Arrays.asList(0, 5, 2, 0, 0, 6, 0, 0, 0));
//         ArrayList<Integer> secondRow = new ArrayList<Integer>(Arrays.asList(1, 6, 0, 9, 0, 0, 0, 0, 4));
//         ArrayList<Integer> thirdRow = new ArrayList<Integer>(Arrays.asList(0, 4, 9, 8, 0, 3, 6, 2, 0));
//         ArrayList<Integer> fourthRow = new ArrayList<Integer>(Arrays.asList(4, 0, 0, 0, 0, 0, 8, 0, 0));
//         ArrayList<Integer> fifthRow = new ArrayList<Integer>(Arrays.asList(0, 8, 3, 2, 0, 1, 5, 9, 0));
//         ArrayList<Integer> sixthRow = new ArrayList<Integer>(Arrays.asList(0, 0, 1, 0, 0, 0, 0, 0, 2));
//         ArrayList<Integer> seventhRow = new ArrayList<Integer>(Arrays.asList(0, 9, 7, 3, 0, 5, 2, 4, 0));
//         ArrayList<Integer> eighthRow = new ArrayList<Integer>(Arrays.asList(2, 0, 0, 0, 0, 9, 0, 5, 6));
//         ArrayList<Integer> ninthRow = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 1, 0, 0, 9, 7, 0));
        

//         Create the Puzzle.
//         Medium
//         ArrayList<Integer> firstRow = new ArrayList<Integer>(Arrays.asList(9, 1, 6,0, 0, 4, 0, 7, 2));
//         ArrayList<Integer> secondRow = new ArrayList<Integer>(Arrays.asList(8, 0, 0, 6, 2, 0, 0, 5, 0));
//         ArrayList<Integer> thirdRow = new ArrayList<Integer>(Arrays.asList(5, 0, 0, 0, 0, 8, 9, 3, 0));
//         ArrayList<Integer> fourthRow = new ArrayList<Integer>(Arrays.asList(0, 6, 0, 0, 0, 0, 2, 0, 0));
//         ArrayList<Integer> fifthRow = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 2, 0, 7, 0, 0, 0));
//         ArrayList<Integer> sixthRow = new ArrayList<Integer>(Arrays.asList(0, 0, 5, 0, 0, 0, 0, 9, 0));
//         ArrayList<Integer> seventhRow = new ArrayList<Integer>(Arrays.asList(0, 9, 7, 8, 0, 0, 0, 0, 3));
//         ArrayList<Integer> eighthRow = new ArrayList<Integer>(Arrays.asList(0, 8, 0, 0, 7, 6, 0, 0, 9));
//         ArrayList<Integer> ninthRow = new ArrayList<Integer>(Arrays.asList(4, 5, 0, 1, 0, 0, 6, 8, 7));
         
  //Create the Puzzle.
//         Hard
//         ArrayList<Integer> firstRow = new ArrayList<Integer>(Arrays.asList(6, 0, 0,3, 0, 0, 1, 0, 0));
//         ArrayList<Integer> secondRow = new ArrayList<Integer>(Arrays.asList(0, 7, 1, 6, 2, 0, 0, 0, 0));
//         ArrayList<Integer> thirdRow = new ArrayList<Integer>(Arrays.asList(8, 0, 5, 0, 0, 1, 0, 0, 0));
//         ArrayList<Integer> fourthRow = new ArrayList<Integer>(Arrays.asList(5, 0, 0, 8, 7, 0, 9, 0, 1));
//         ArrayList<Integer> fifthRow = new ArrayList<Integer>(Arrays.asList(0, 0, 9, 0, 0, 0, 6, 0, 0));
//         ArrayList<Integer> sixthRow = new ArrayList<Integer>(Arrays.asList(4, 0, 7, 0, 6, 9, 0, 0, 8));
//         ArrayList<Integer> seventhRow = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 2, 0, 0, 8, 0, 7));
//         ArrayList<Integer> eighthRow = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 8, 6, 4, 1, 0));
//         ArrayList<Integer> ninthRow = new ArrayList<Integer>(Arrays.asList(0, 0, 8, 0, 0, 3, 0, 0, 2));
         
     
//         Hardest
         ArrayList<Integer> firstRow = new ArrayList<Integer>(Arrays.asList(3, 0, 0,0, 0, 9, 4, 0, 7));
         ArrayList<Integer> secondRow = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0, 8, 6, 0));
         ArrayList<Integer> thirdRow = new ArrayList<Integer>(Arrays.asList(0, 0, 8, 1, 0, 4, 3, 0, 0));
         ArrayList<Integer> fourthRow = new ArrayList<Integer>(Arrays.asList(0, 3, 1, 7, 0, 0, 0, 0, 0));
         ArrayList<Integer> fifthRow = new ArrayList<Integer>(Arrays.asList(0, 9, 4, 0, 0, 0, 7, 8, 0));
         ArrayList<Integer> sixthRow = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 5, 1, 3, 0));
         ArrayList<Integer> seventhRow = new ArrayList<Integer>(Arrays.asList(0, 0, 9, 5, 0, 3, 2, 0, 0));
         ArrayList<Integer> eighthRow = new ArrayList<Integer>(Arrays.asList(0, 8, 5, 0, 0, 0, 0, 0, 0));
         ArrayList<Integer> ninthRow = new ArrayList<Integer>(Arrays.asList(1, 0, 3, 6, 0, 0, 0, 0, 4));



         //Create the puzzle
         ArrayList<ArrayList<Integer>> sudokuPuzzle = new ArrayList<ArrayList<Integer>>(Arrays.asList(firstRow,secondRow,thirdRow,fourthRow,fifthRow,sixthRow,seventhRow,eighthRow,ninthRow));
         
         //Create the object of the Sudoku implementation and run the GA.
         SudokuImplementation sudoku = new SudokuImplementation(sudokuPuzzle);
         sudoku.runGeneticAlgorithm();
    }//end method.
}//end class.
