/**
 * Sudoku Solving by GA 
 * By Maulik Shah
 * 06 Dec,2016
 */
package edu.uga.ai.ec.beans;

import java.util.*;
/**
 * This class is the representation of the Sudoku Problem.
 * The representation is simple nested 9 member permutations. 
 * @author Maulik
 */
public class Chromosome {
    
    private ArrayList<ArrayList<Integer>> sudokuSolution;
    private int fitness;
    
   
    /**
     * Constructor to initiate the random values for a given problem.
     * @param sudokuPuzzle the Sudoku Puzzle is the puzzle to be solved and it's value can not be
     * modified. There will be a '0' on the empty places. 
     */
    public Chromosome(ArrayList<ArrayList<Integer>> sudokuPuzzle){
       this.sudokuSolution = new ArrayList<ArrayList<Integer>>(SudokuConstants.PROBLEM_SIZE); //Add 9 different ArrayList in the Solution.
        
        //Iterate through all the rows of the problem to create a random solution.
        for(ArrayList<Integer> problemRow : sudokuPuzzle){
            ArrayList<Integer> solutionRow = new ArrayList<Integer>(SudokuConstants.PROBLEM_SIZE); //Create a new row for the solution.
            
            //Iterate through all the elements of the problem row.
            for(Integer element: problemRow){
                if(element != 0){
                    solutionRow.add(element);
                }else{
                    while(true){
                        int randomNumber = new Random().nextInt(SudokuConstants.PROBLEM_SIZE) + 1; // This will give the number between 1 and 9.
                        //If the newly found element is not in the current solutio or problems. Also exclude 0.
                        if(!problemRow.contains(randomNumber) && !solutionRow.contains(randomNumber)){
                             solutionRow.add(randomNumber);
                             break;//Break the loop to the next element.
                        }//end if
                    }//end while true.
                }//end if-else.
            }//end for.
            
            sudokuSolution.add(solutionRow);//Add the row to the solution individual.
        }//end Row for.
        
        setFitness(fitnessFunction());
    }//end constructor. 
    
    /**
     * This constructor is used for just initializing the solution to an empty variable and a fitness 0.
     * It can be used for creating new individual from scratch without randomization.
     */
    public Chromosome(){
        this.sudokuSolution = new ArrayList<ArrayList<Integer>>(SudokuConstants.PROBLEM_SIZE); //Add 9 different ArrayList in the Solution.
        setFitness(0);
    }
    
    /**
     * This function calculates the fitness of the individual.
     * The fitness is based on a panelty here. For a solution with 9*9 sudoku puzzle.
     * The rows are permutation : so no chances of getting a duplicate
     * The columns , if contains any duplicates, number of duplicates will be added in the penalty.
     * The boxes (0,0 - 2,2 ; 3,0 - 5,2 ; 6,0 - 8,2; 0,3-2,5; 3,3- 5,5 ; 6,3-8-5; 0,6 - 2,8 ; 3,6 - 5,8 ; 6,6 - 8,8) if contains
     * any duplicate values, the number of duplicates added as penalty.
     * Parameters : individual is the 2D array of 9*9 matrix of sudoku solution.
     * @return fitness of the individual.
     */
    public int fitnessFunction(){
        int fitness=0;
        
        //Iterate through all the columns of the problem.
        for(int column = 0 ; column < SudokuConstants.PROBLEM_SIZE ; column++){
            //This array list will count the number of occurance of an element in 
            //a particular column.
            //Each place in arraylist represent the same value in the puzzle.
            //e.g. panaltyCounter(0) = 3. means there are 3 occurance of 1's in the column.
            ArrayList<Integer> penaltyCounter = new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0,0,0,0));//ArrayList of 9 zeroes.
            for(int row = 0 ; row < SudokuConstants.PROBLEM_SIZE; row++){
                 int elementValue = this.sudokuSolution.get(row).get(column); // Value on a particular place in sudoku.
                 penaltyCounter.set(elementValue-1,penaltyCounter.get(elementValue-1) + 1);//Increase the counter.
            }//end rows.
            //Sum all the penalty.
//            System.out.println("Penalty for Column :" + (column+1) + " counter:" + penaltyCounter);
            for(Integer penaltyForColumn : penaltyCounter){
                //If a number occurs more than once in the column
                //add penalty.
                if(penaltyForColumn > 1 ){
                    fitness += penaltyForColumn;
                }else if(penaltyForColumn == 0){//For not having a value add a penalty.
                    fitness += 1;//Add one to fitness.
                }
            }//end for.
        }//end columns.        
        
        
        //Now count penalty for 3*3 boxes. 
        //Step Size is 3 for correct devision.
        for(int row=0 ; row < SudokuConstants.PROBLEM_SIZE ; row += Math.sqrt(SudokuConstants.PROBLEM_SIZE)){
            //Same way increase the column steps by 3. 
            for(int column=0 ; column < SudokuConstants.PROBLEM_SIZE ; column += Math.sqrt(SudokuConstants.PROBLEM_SIZE)){
                //This array list will count the number of occurance of an element in 
                //a particular box of 3*3(for 9*9 sudoku puzzle).
                //Each place in arraylist represent the same value in the puzzle.
                //e.g. panaltyCounter(0) = 3. means there are 3 occurance of 1's in the column.
                ArrayList<Integer> penaltyCounter = new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0,0,0,0));//ArrayList of 9 zeroes.
                //Iterate through Box's rows and columsn.
                for(int boxRow = row ; boxRow < row+ Math.sqrt(SudokuConstants.PROBLEM_SIZE); boxRow++){
                    for(int boxColumn = column ; boxColumn < column+ Math.sqrt(SudokuConstants.PROBLEM_SIZE); boxColumn++){
                        int elementValue = this.sudokuSolution.get(boxRow).get(boxColumn); // Value on a particular place in sudoku.
                        penaltyCounter.set(elementValue-1,penaltyCounter.get(elementValue-1) + 1);//Increase the counter.
                    }//end for box column.
                }//end for box row.
                
                
//                System.out.println("Penalty for Box :" + row + "-" + column + " counter:" + penaltyCounter);
                //Sum all the penalty for a particular box.
                for(Integer penaltyForColumn : penaltyCounter){
                    //If a number occurs more than once in the column
                    //add penalty.
                    if(penaltyForColumn > 1){
                        fitness += penaltyForColumn;
                    }//end if.
                }//end for.
            }//end for- column.
        }//end for-row.
        return fitness;
    }//end function.

    /**
     * This method returns an array with specification of number of occurance of an
     * element in the column.
     * @param columnIndex is the column to search.
     * @return array with specification of the element counts.
     */
    public ArrayList<Integer> getColumnStates(int columnIndex){
        //This array list will count the number of occurance of an element in 
        //a particular column.
        ArrayList<Integer> columnStates = new ArrayList<Integer> (Arrays.asList(0,0,0,0,0,0,0,0,0));
        
        for(int row = 0 ; row < SudokuConstants.PROBLEM_SIZE; row++){
            int elementValue = this.sudokuSolution.get(row).get(columnIndex); // Value on a particular place in sudoku.
            columnStates.set(elementValue-1,columnStates.get(elementValue-1) + 1);//Increase the counter.
        }//end rows.
        
        return columnStates;
    }//end method.
    
    
    /**
     * This method returns an array with specification of number of occurance of an
     * element in the box provided starting point in row and column..
     * @param row is the starting row value of the box.
     * @param column is the starting column value of the box.
     * @return array with specification of the element counts.
     */
    public ArrayList<Integer> getBoxStates(int row, int column){
        //This array list will count the number of occurance of an element in 
        //a particular box.
        ArrayList<Integer> boxStates = new ArrayList<Integer> (Arrays.asList(0,0,0,0,0,0,0,0,0));
//        System.out.println("In Box States: Row:" + row + " column:" + column);
        //Iterate through each row.
        for(int boxRow = row ; boxRow < row + Math.sqrt(SudokuConstants.PROBLEM_SIZE) ; boxRow++){
            for(int boxcolumn = column ; boxcolumn < column + Math.sqrt(SudokuConstants.PROBLEM_SIZE) ; boxcolumn++){
                int elementValue = this.sudokuSolution.get(boxRow).get(boxcolumn); // Value on a particular place in sudoku.
                boxStates.set(elementValue-1,boxStates.get(elementValue-1) + 1);//Increase the counter.
//                System.out.println("BoxStates Status:" + boxStates);
            }//end column.   
        }//end row.
            
        return boxStates;
    }//end method.
    
    public ArrayList<ArrayList<Integer>> getSudokuSolution() {
        return sudokuSolution;
    }

    //Set the whole solution.
    public void setSudokuSolution(ArrayList<ArrayList<Integer>> sudokuSolution) {
        this.sudokuSolution = new ArrayList<ArrayList<Integer>> (sudokuSolution);
    }
    
    //Set a particular Row.
    public void setSudokuSolution(int rowIndex, ArrayList<Integer> sudokuRow) {
        this.sudokuSolution.set(rowIndex, sudokuRow);
    }
    
    //Set a particular Element.
    public void setSudokuSolution(int rowIndex,int columnIndex,int element) {
        this.sudokuSolution.get(rowIndex).set(columnIndex,element);
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }
    
    
    /**
     * This method is an Override call of the toString method for this class.
     * @return individual's values.
     */
    @Override
    public String toString(){
       String result = "Individual : " ;
       
       //Iterate through all the permutation rows.
       for(ArrayList<Integer> sudokuRow: this.sudokuSolution){
           result += "\n"+ sudokuRow.toString();
       }//end loop.
       
       result  += "\n Fitness:" + this.fitness;
       return result;
    }//end 
    
}//end class.
