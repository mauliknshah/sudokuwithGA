/**
 * Sudoku Solving by GA 
 * By Maulik Shah
 * 06 Dec,2016
 */


/**
 * Notes : Try to use scramble mutation.
 */

package edu.uga.ai.ec;

import java.util.*;
import edu.uga.ai.ec.beans.*;
        

/**
 * This class is an implementation class for the Sudoku Puzzle.
 * @author Maulik
 */
public class SudokuImplementation {
    private ArrayList<ArrayList<Integer>> sudokuProblem; //Sudoku problem, to be entered while implementing this class.
    private ArrayList<Chromosome> population = new ArrayList<Chromosome>(SudokuConstants.POPULATION_SIZE);//Population.
    private ArrayList<ArrayList<Chromosome>> populationIslands = new ArrayList<ArrayList<Chromosome>>();//Population ISLANDS
    private ArrayList<Integer> populationFitness = new ArrayList<Integer> (SudokuConstants.POPULATION_SIZE); // Fitness of the Population.
    private ArrayList<ArrayList<Integer>> populationFitnessIslands = new ArrayList<ArrayList<Integer>>();//Population.
    private boolean diversityTooLow = false;//Flag if diversity is below some level.
    private int diversityMaintananceCounter = 0; //Diversity Maintance cycle counter.
    
    private int selectionSize = SudokuConstants.PARENTS_SIZE * 2;//Selection Size.
    private int totalCycle = 0; //Cycle Count.
     
    
    /**
     * This constructor allows to enter the user to enter the sudoku problem.
     * @param sudokuProblem is the problem entered by the end user for computation.
     */
    public SudokuImplementation(ArrayList<ArrayList<Integer>> sudokuProblem){
        this.sudokuProblem = sudokuProblem;
    }
    
    /**
     * This method returns a mutated individual.
     * @param individual is the individual to mutate.
     * @param type is the island mutation type.
     * @return is the mutated individual.
     */
    public Chromosome mutation(Chromosome individual,int cycle, int type){
        Chromosome mutatedIndividual = null;
        
        //Update the mutation ratio.
        double mutationRate = (1 - ((double)cycle/(double)(SudokuConstants.EVOLUTION_CYCLES)));
        if(diversityTooLow){
            mutationRate = 0.9; // 90%
        }
        
        
        double mutateOrNot = new Random().nextDouble();
        
//        System.out.println("Mutation Rate:"  + mutationRate + " Random Number Mutate:" + mutateOrNot);
        
        if(mutateOrNot <= mutationRate){
            int selectMutation = new Random().nextInt(10);
            //Swap mutation 
            if(selectMutation >=9){
                
                if(type == 0 || type == 2){
                    //               System.out.println("1");
                    mutatedIndividual = mutationIFixedColumnMemberMutation(individual);     
                }else{
                    //                System.out.println("2");
                    mutatedIndividual = mutationIFixedBoxMemberMutation(individual);
                }

            }else{
                
                if(type == 0 || type == 1){
                      //                System.out.println("3");
                      mutatedIndividual = mutationScramble(individual);    
                }else{
                    //                System.out.println("4");
                    mutatedIndividual = mutationSwap(individual);
                }

            }
        }else{
            mutatedIndividual  = individual;
        }
        
        //Return.
        return mutatedIndividual;
    }//end method.
    
    /**
     * This method does an informed mutation,where it finds the 
     * boxes with maximum number of fixed elements. 
     * After going through one by one of this area, it tries to minimize errors
     * in this region via a few number of exchanges.
     * @param individual is the individual to mutate.
     * @return mutated individual.
     */
    public Chromosome mutationIFixedBoxMemberMutation(Chromosome individual){
//        System.out.println("Informed mutation individual : " + individual);
        Chromosome mutatedIndividual = new Chromosome();//Mutated Individual.
        mutatedIndividual.setSudokuSolution(individual.getSudokuSolution());
        ArrayList<Integer> boxState = null;
        //Iterate through all the columns. for the mutation.
           for(int row=0 ; row < SudokuConstants.PROBLEM_SIZE ; row += Math.sqrt(SudokuConstants.PROBLEM_SIZE)){
                //Same way increase the column steps by 3. 
                for(int column=0 ; column < SudokuConstants.PROBLEM_SIZE ; column += Math.sqrt(SudokuConstants.PROBLEM_SIZE)){

                    int fixedElementCounter = 0;
                    //Iterate through Box's rows and columns and find the fixed number of elements.
                    for(int boxRow = row ; boxRow < row+ Math.sqrt(SudokuConstants.PROBLEM_SIZE); boxRow++){
                        for(int boxColumn = column ; boxColumn < column+ Math.sqrt(SudokuConstants.PROBLEM_SIZE); boxColumn++){
                             if(this.sudokuProblem.get(boxRow).get(boxColumn) != 0){
                                fixedElementCounter++;//increase the counter for the number of fixed elements. 
                             }//end if.
                        }//end for box column.     
                    }//end for box row.
//                    System.out.println("Sudoku Problem:" + this.sudokuProblem);
//                    System.out.println("Row:"+row + " Column:" + column);
//                    System.out.println("Fixed Elements:" + fixedElementCounter);
                    
                    //If the column has more than half of fixed memeber of columns then start the swap.
                    if(fixedElementCounter > (SudokuConstants.PROBLEM_SIZE /3)){
//                        System.out.println("Processing Mutation: ");
                        
                        //Iterate through each row.
                        for(int boxRow = row ; boxRow < row + Math.sqrt(SudokuConstants.PROBLEM_SIZE) ; boxRow++){
                            for(int boxcolumn = column ; boxcolumn < column + Math.sqrt(SudokuConstants.PROBLEM_SIZE) ; boxcolumn++){
                                
                                //Get the box statistics.
                                boxState = mutatedIndividual.getBoxStates(row,column); 
//                                System.out.println("Box State:" + boxState);

                                //mutated row.
                                ArrayList<Integer> mutatedRow = new ArrayList<Integer>(mutatedIndividual.getSudokuSolution().get(boxRow));//Create a new copy of the row. 


                               //Get the particular element.
                               int element = mutatedRow.get(boxcolumn);

                               //If the element to switch is not the fixed one.
                               if(this.sudokuProblem.get(boxRow).get(boxcolumn) != element){
                                    //If the number of elements with a number is greater than
                                   //1 then replace it with the element which are not there
                                   //in the column.
                                   if(boxState.get(element-1) > 1){
                                       //Find the zero count element for replacement.
                                       int elementToReplace = 0;
                                       for(int findZeroCountElement =0 ; findZeroCountElement < SudokuConstants.PROBLEM_SIZE; findZeroCountElement++){
                                           //Find the element with zero count in the column.
                                           if(boxState.get(findZeroCountElement) == 0 && !(this.sudokuProblem.get(boxRow).contains(findZeroCountElement + 1))){
                                               elementToReplace = findZeroCountElement + 1; //Set the element.(Add 1 for actual value).
//                                               System.out.println("Element To Replace:"  + elementToReplace);
                                               break;//exit the loop.
                                           }//end if.
                                       }//end for.

                                       //If the element to replace is found,then go aheand and replace.
                                       if(elementToReplace != 0){
                                        //Search through the solution row and replace the element with
                                        //that of more than one counter.
                                        for(int elementCount = 0 ; elementCount < SudokuConstants.PROBLEM_SIZE ; elementCount++){
                                             //find the particular element to replace.  
                                             if(elementToReplace == mutatedRow.get(elementCount)){
                                                  //Swap the members. 
                                                  mutatedRow.set(elementCount, mutatedRow.get(boxcolumn));
                                                  mutatedRow.set(boxcolumn, elementToReplace);
//                                                  System.out.println("Element Replaced:" + mutatedRow);
                                                  break;
                                             }//end if.
                                        }//end for element Count.
                                       }//end if.
                                   }else{
                                       //Do nothing.
                                   }//end.
                               }//end if fixed element.

                               //Add the newly created row to the mutated individual.
//                               System.out.println("Final Add Row:" + mutatedRow);
                               mutatedIndividual.getSudokuSolution().set(boxRow,mutatedRow);          
                            }//end box column.
                        }//end for box-rows.
                    }else{
                        //Do Nothing.
                    }//end if -else.
                }//end for column
            }//end for row.    
            
        mutatedIndividual.setFitness(mutatedIndividual.fitnessFunction());
//        System.out.println("Mutation:" + mutatedIndividual);
        return mutatedIndividual;//Return.
    }//end method.
    
    
    
    /**
     * This method does an informed kind of mutation,where it finds the columns
     * and boxes with maximum number of fixed elements. 
     * After going through one by one of this area, it tries to minimize errors
     * in this region via a few number of exchanges.
     * @param individual is the individual to mutate.
     * @return mutated individual.
     */
    public Chromosome mutationIFixedColumnMemberMutation(Chromosome individual){
//        System.out.println("Informed mutation individual : " + individual);
        Chromosome mutatedIndividual = new Chromosome();//Mutated Individual.
        mutatedIndividual.setSudokuSolution(individual.getSudokuSolution());
        ArrayList<Integer> columnState = null;
        //Iterate through all the columns. for the mutation.
        for(int count = 0 ; count < SudokuConstants.PROBLEM_SIZE; count++){
            //Get the occurance of the fixed element in the column.
            int fixedElementCounter = 0;
            for(int fixedElementInColumn = 0 ; fixedElementInColumn < SudokuConstants.PROBLEM_SIZE; fixedElementInColumn++){
                if(this.sudokuProblem.get(fixedElementInColumn).get(count) != 0){
                    fixedElementCounter++;//increase the counter for the number of fixed elements. 
                }//end if.
            }//end for.
            
//            System.out.println("Column:" + count + " Fixed Elements :" + fixedElementCounter);
            
            //If the column has more than half of fixed memeber of columns then start the swap.
            if(fixedElementCounter > (SudokuConstants.PROBLEM_SIZE /3)){
                
//                System.out.println("Processing Mutation: ");
                //Iterate through each row.
                for(int row = 0 ; row < SudokuConstants.PROBLEM_SIZE ; row++){
                    //Get the column statistics.
                    columnState = mutatedIndividual.getColumnStates(count);
//                    System.out.println("Column State:" + columnState);
                    
                    //mutated row.
                    ArrayList<Integer> mutatedRow = new ArrayList<Integer>(mutatedIndividual.getSudokuSolution().get(row));//Create a new copy of the row. 
           
                   
                   //Get the particular element.
                   int element = mutatedRow.get(count);
                   
                   //If the element to switch is not the fixed one.
                   if(this.sudokuProblem.get(row).get(count) != element){
                        //If the number of elements with a number is greater than
                       //1 then replace it with the element which are not there
                       //in the column.
                       if(columnState.get(element-1) > 1){
                           //Find the zero count element for replacement.
                           int elementToReplace = 0;
                           for(int findZeroCountElement =0 ; findZeroCountElement < SudokuConstants.PROBLEM_SIZE; findZeroCountElement++){
                               //Find the element with zero count in the column.
                               if(columnState.get(findZeroCountElement) == 0 && !(this.sudokuProblem.get(row).contains(findZeroCountElement + 1))){
                                   elementToReplace = findZeroCountElement + 1; //Set the element.(Add 1 for actual value).
                               }//end if.
                           }//end for.

                           //If the element to replace is found,then go aheand and replace.
                           if(elementToReplace != 0){
                            //Search through the solution row and replace the element with
                            //that of more than one counter.
                            for(int elementCount = 0 ; elementCount < SudokuConstants.PROBLEM_SIZE ; elementCount++){
                                 //find the particular element to replace.  
                                 if(elementToReplace == mutatedRow.get(elementCount)){
                                      //Swap the members. 
                                      mutatedRow.set(elementCount, mutatedRow.get(count));
                                      mutatedRow.set(count, elementToReplace);
                                 }//end if.
                            }//end for.
                           }//end if.
                       }else{
                           //Do nothing.
                       }//end.
                   }//end if fixed element.
                   
                   //Add the newly created row to the mutated individual.
                    mutatedIndividual.getSudokuSolution().set(row,mutatedRow);         
                }//end for rows.
            }else{
                //Do Nothing.
            }
        }//end column.    
        mutatedIndividual.setFitness(mutatedIndividual.fitnessFunction());
//        System.out.println("Mutation:" + mutatedIndividual);
        return mutatedIndividual;//Return.
    }//end method.
    
    
    
    /**
     * This method does a scramble mutation on a particular permutation of the chromosome
     * of the sudoku puzzle. It leaves out the fixed elements and crates a new permutation
     * out of the remaining one and create scramble mutation.
     * @param individual is the individual to mutate.
     * @return is the mutated individual.
     */
    public Chromosome mutationScramble(Chromosome individual){
//        System.out.println("Individual to mutate:" + individual);
        Chromosome mutatedChromosome = new Chromosome();
        ArrayList<Integer> mutatedRow   = null;
        //Iterate through all the rows for scrambling.
        for(int row=0 ; row < SudokuConstants.PROBLEM_SIZE;row++){
                mutatedRow = new ArrayList<Integer> (this.sudokuProblem.get(row));
                ArrayList<Integer> scrambleSizeRow = new ArrayList<Integer> ();
                ArrayList<Integer> mutatedScrambleSizeRow = null;
                int scrambleRowSize = 0 ; //Scrambled Row size.
                //Iterate through all the elements and create new arrays for Scramble mutation with 
                //fixed elements removed.
                for(int elementPosition=0 ; elementPosition < SudokuConstants.PROBLEM_SIZE;elementPosition++){
                    //Only if the current position is not fixed.
                    if(mutatedRow.get(elementPosition) == 0){
                        scrambleSizeRow.add(individual.getSudokuSolution().get(row).get(elementPosition));
                        scrambleRowSize++;
                    }//end if.
                }//end loop.

                //Initial Position.
                int swapInit = new Random().nextInt(scrambleRowSize - 1);//Initial position that needed to scramble.
                //End Position.
                int swapEnd = swapInit;
                //Chose the interval for doing scramble mutation.
                while(!(swapEnd - swapInit > 0)){
                   swapEnd = new Random().nextInt(scrambleRowSize);//End position that needed to be scrambled.
                }

    //            System.out.println("Row:" + row + " Start:" + swapInit + " End:" + swapEnd);
                //Scramble the positions. Iterate the loop for count of the problem size devided by 10.
                mutatedScrambleSizeRow = new ArrayList<Integer>(scrambleSizeRow);
                ArrayList<Integer> scrambledPositions = new ArrayList<Integer>(swapEnd - swapInit);
                for(int i = swapInit ; i <= swapEnd; i++){
                    int randomSwap = -2;
                    //Find a position for putting in the scramblled row.
                    do{
                      randomSwap = new Random().nextInt(swapEnd-swapInit + 1) + swapInit;
                    }while(scrambledPositions.contains(randomSwap));

                    scrambledPositions.add(randomSwap);//Add the position to the scrambled positions array.

                    //Add it to the mutated Scrambled row.
                    mutatedScrambleSizeRow.set(i, scrambleSizeRow.get(randomSwap));//Put the element in the small sized permutation.
                }//end for.
    //            System.out.println("Mutated Scramble Sized Row:" + mutatedScrambleSizeRow); 
                //Now copy the small sized permutation to the original size permutation.
                int mutatedElement = 0;
                for(int element = 0 ; element < scrambleRowSize  ; element++){
                    while(true){
                        //If the position is not fixed size.
                        if(mutatedRow.get(mutatedElement)== 0){
                            mutatedRow.set(mutatedElement++, mutatedScrambleSizeRow.get(element));//Set the element on the blank position.
                            break;
                        }//end if.
                        mutatedElement++;
                    }//end while.
                }//end copying.
                mutatedChromosome.getSudokuSolution().add(mutatedRow);
            }//end for.
        mutatedChromosome.setFitness(mutatedChromosome.fitnessFunction());//Calculate the fitness Function.
//        System.out.println("Scramble Mutation Individual:" + mutatedChromosome);
        return mutatedChromosome;
    }//end method.
    
    
    /**
     * This method does a simple swap mutation on the chromosome. 
     * It changes random individual's position in the solution.
     * One Caution : The fixed elements mentioned in the problems are not 
     * swapped anywhere. 
     * @param individual is the individual to mutate.
     * @return mutated individual.
     */
    public Chromosome mutationSwap(Chromosome individual){
        Chromosome mutatedIndividual = new Chromosome();//Mutated Individual.
        
        
        //Iterate through all the rows for the mutation.
        for(int count = 0 ; count < SudokuConstants.PROBLEM_SIZE; count++){
            
            ArrayList<Integer> mutatedRow = new ArrayList<Integer>(individual.getSudokuSolution().get(count));//Create a new copy of the row.
            int randomPercent = new Random().nextInt(4);
            if(randomPercent == 1){
                int swaptimes = new Random().nextInt(2) + 1;
                for(int swaps = 0; swaps<swaptimes ; swaps ++){
                    //Find the start position to swap.
                //Make sure not to touch the fixed elements mentioned in the elements.
                int startPosition = -1 , endPosition = -1;
                //Find the first position.
                while(true){
                    startPosition = new Random().nextInt(SudokuConstants.PROBLEM_SIZE);
                    //Only if the problem doesn't have any fixed value on that position.
                    if(this.sudokuProblem.get(count).get(startPosition) == 0){
                        break;//Continue to finding the last position.
                    }//end if.
                }//end loop.

                //Find the end position.
                while(true){
                    endPosition = new Random().nextInt(SudokuConstants.PROBLEM_SIZE);
                    //Only if the problem doesn't have any fixed value on that position.
                    if(this.sudokuProblem.get(count).get(endPosition) == 0 && startPosition != endPosition){
                        break;//Continue to finding the last position.
                    }//end if.
                }//end loop.

    //            System.out.println("Mutation:" + "\n" + "Start" + startPosition + "End:" + endPosition);
                //Swap the individuals.
                int tempSwap = mutatedRow.get(startPosition);
                mutatedRow.set(startPosition, mutatedRow.get(endPosition));
                mutatedRow.set(endPosition, tempSwap);
               }//end loop swaps.
            }//end if.
            
            //Add the newly created row to the mutated individual.
            mutatedIndividual.getSudokuSolution().add(mutatedRow);
            
        }//end for.
        
        mutatedIndividual.setFitness(mutatedIndividual.fitnessFunction());
//        System.out.println("Mutation:" + mutatedIndividual);
        return mutatedIndividual;//Return.
    }//end method.
    
    
    /**
     * Here the recombination type is PMX. Partially Mapped Crossover with specialized operator
     * for the current problem. We can say it as informed mutation.
     * Here, first the fixed elements are removed from the permutation,so that the remaining can
     * be used for the crossover and mutation.
     * Certain part of the first parent is copied into the child for each row of the puzzle.
     * The rest part is taken from the second parent using PMX.
     * Only one child is created here and is forwarded for mutation.
     * @param parents is the individuals which take part in the recombination.
     * @return is the new instance after the recombination is done.
     */
    public Chromosome recombination(ArrayList<Chromosome> parents,int cycle){
//        System.out.println("Recombination");
        Chromosome child = new Chromosome();//Create a new individual with nill configuration.
        
         //Update the recombination ratio.
        double reCombRate = (0 + ((double)cycle/((double)SudokuConstants.EVOLUTION_CYCLES * 2)));
        
        double reCombOrNot = new Random().nextDouble();
        
//        System.out.println("Crossover Rate:"  + reCombRate + " Crossover or not:" + reCombOrNot);
        
        //Perform recombination with probablity.
       if(reCombOrNot <= reCombRate){
            int selectRecomb = new Random().nextInt(2);
            if(selectRecomb == 1){
//                System.out.println("Recombination 1");
              //PMX Recombination
//////              System.out.println("PMX...");
              child = recombinationPMX(parents);
            }else{
//                System.out.println("Recombination 2");
            // Uni-order Recombination
////            System.out.println("Uni-Order...");
              child = recombinationUniformOrder(parents);
            }
        }else{
            child = parents.get(0);//Assign first parent to the child.
        }//end if-else.
        
        return child;
    }//end method.
   
    
     /**
     * This method returns an individual after recombination.
     * @param parents is the individuals which take part in the recombination.
     * @return is the new instance after the recombination is done.
     */
    public Chromosome recombinationPMX(ArrayList<Chromosome> parents){
        
//        System.out.println("Parent 1:" + parents.get(0));
//        System.out.println("Parent 2:" + parents.get(1));
        Chromosome child = new Chromosome();////Create a new individual with nill configuration.
        
        
        //For each row apply PMX.
        for(int row=0 ; row < SudokuConstants.PROBLEM_SIZE;row++){
            ArrayList<Integer> childRow = new ArrayList<Integer> (this.sudokuProblem.get(row));
            ArrayList<ArrayList<Integer>> pmxParentsRow = new ArrayList<ArrayList<Integer>>(SudokuConstants.PARENTS_SIZE); 
            int pmxPermutationSize = 0;
        
            //Add new rows in the pmxParentlist.
            for(int parentCount = 0 ; parentCount < SudokuConstants.PARENTS_SIZE ; parentCount++){
                pmxParentsRow.add(new ArrayList<Integer>());
            }//end loop.


            //Iterate through all the elements and create new arrays for PMX with 
            //fixed elements removed.
            for(int elementPosition=0 ; elementPosition < SudokuConstants.PROBLEM_SIZE;elementPosition++){
                //Only if the current position is not fixed.
                if(childRow.get(elementPosition) == 0){
                    for(int parentCount = 0 ; parentCount < SudokuConstants.PARENTS_SIZE;parentCount++){
                        //Copy the element of the particular row to the newly created list for PMX.
                        pmxParentsRow.get(parentCount).add(parents.get(parentCount).getSudokuSolution().get(row).get(elementPosition));
                    }//end for.
                    pmxPermutationSize++;
                }//end if.
            }//end loop.
            
            //Find the start-Point and endPoint.
            int startPoint = new Random().nextInt(pmxPermutationSize - 2) + 1 ; //Start Point should not be in the second last to first position of array.
            int endPoint = startPoint;
            //End Point should be greater than the start point.
            while(endPoint - startPoint < 1)
                endPoint = new Random().nextInt(pmxPermutationSize - 1) + 1;//End point should not be the first position in the array. 
       
//            System.out.println("Parents:" + pmxParentsRow.get(0) + "\n" + pmxParentsRow.get(1));
//            System.out.println("Row:" + row + "Start:" + startPoint +  " End Point:" + endPoint + "PMX Permuation size:" + pmxPermutationSize);
            
            //Create a new arraylist for applying PMX for the reduced sized arrays.
            ArrayList<Integer> pmxRecombinationRow = new ArrayList<Integer>(pmxPermutationSize);
            //Load array with all zeroes.
            for(int addZero=0 ; addZero < pmxPermutationSize ; addZero++)
                pmxRecombinationRow.add(0);
            
            //PMX Recombination
            //Copy the first parent into the child.
            for(int copyPosition = startPoint ; copyPosition <=endPoint ; copyPosition++){
                pmxRecombinationRow.set(copyPosition,pmxParentsRow.get(0).get(copyPosition));//Hardcoded zero. As it will use only two parents.
            }//end for.
            
//            System.out.println("Child Row after first parent copy.:" + pmxRecombinationRow);
            
            //Start with PMX for the second child elements.
            for(int secondParentPosition = startPoint; secondParentPosition <= endPoint; secondParentPosition++){
                boolean isElementProcessed = false; //flag to indicate if the element processed or not.
                
                //If the element is already on the child's crossed over region than leave it.
                for(int childPosition = startPoint; childPosition <= endPoint; childPosition++){
                    //If the value mathes then state that element processed and quit.
                    if(pmxRecombinationRow.get(childPosition).intValue() == pmxParentsRow.get(1).get(secondParentPosition).intValue()){
                        isElementProcessed = true;
                        break;
                    }
                }//end for child
                  
//                System.out.println("Element Processed:" + isElementProcessed);
                
                // If the element does not exist in the crossovered region than
               // check the value of the Parent 1 in the same position say it 'new'. Now find this 'new' element in the Parent 2.
               // If the element lies in the crossovered region than repeat the loop until it is out of that region.
                if(!isElementProcessed){
                    //Next element in the first parent which will be used to link with the second parent.
                    int linkElement = pmxParentsRow.get(0).get(secondParentPosition);
                    
                    //Loop through until you find solution.
                    while(!isElementProcessed){
                        for(int linkPosition=0 ; linkPosition < pmxPermutationSize ; linkPosition++){
                            //If the linked element of Parent 1 lies in the crossed over region in Parent 2 on position x, 
                            //then assign Parent 1's value on position x to link element and start the search again.
                            // If the linked element of Parent 1 lies in out side the crossover region in Parent 2 on position x, 
                            // then copy the original element in Parent 2 to Child on that position x.   

                            if(linkElement == pmxParentsRow.get(1).get(linkPosition)  && (linkPosition < startPoint || linkPosition > endPoint)){
                                pmxRecombinationRow.set(linkPosition, pmxParentsRow.get(1).get(secondParentPosition));
                                isElementProcessed = true;
                                break;
                            }else if (linkElement == pmxParentsRow.get(1).get(linkPosition)  && (linkPosition >= startPoint || linkPosition <= endPoint)){
                                linkElement = pmxParentsRow.get(0).get(linkPosition);
                                break;
                            }//end else if.
                        }//end for link position.
                    }//end while.
                }//end if element processed.
            }//end for second parent.
//            System.out.println("Child Row after seoond child crossover region element copy:" + pmxRecombinationRow);
            
            //Now copy the remaining elements in the recombination array as it is from second parent.
            for(int remainingPosition = 0; remainingPosition < pmxPermutationSize ; remainingPosition++){
                //If no value is assigned to the child element.
                if(pmxRecombinationRow.get(remainingPosition) == 0){
                    pmxRecombinationRow.set(remainingPosition, pmxParentsRow.get(1).get(remainingPosition));
                }//end if.
            }//end for.
//            System.out.println("Row after final PMX:" + pmxRecombinationRow);
            
            
            
            //Now copy the element to the Child row.
            for(Integer pmxRecombElement : pmxRecombinationRow){
                int childRowCount = 0;
                //Find the vacant element.
                while(childRow.get(childRowCount) != 0){
                    childRowCount++;
                }//end if.
                childRow.set(childRowCount,pmxRecombElement);
            }//end if.
            
//            System.out.println("Child Row to be added:" + childRow);
            child.getSudokuSolution().add(childRow);
        }//end for.
        
        child.setFitness(child.fitnessFunction());
        
//        System.out.println("Crossover Child:" + child);
        return child;
    }//end method.
    
    
    
    
    /**
     * This method returns an individual after recombination of type
     * order recombination.
     * @param parents is the individuals which take part in the recombination.
     * @return is the new instance after the recombination is done.
     */
    public Chromosome recombinationUniformOrder(ArrayList<Chromosome> parents){
        Chromosome child = new Chromosome();////Create a new individual with nill configuration.
        
        
        //For each row apply PMX.
        for(int row=0 ; row < SudokuConstants.PROBLEM_SIZE;row++){
            ArrayList<Integer> childRow = new ArrayList<Integer> (this.sudokuProblem.get(row));
            ArrayList<ArrayList<Integer>> uniOrderParentsRow = new ArrayList<ArrayList<Integer>>(SudokuConstants.PARENTS_SIZE); 
            int uniOrderPermutationSize = 0;
        
            //Add new rows in the pmxParentlist.
            for(int parentCount = 0 ; parentCount < SudokuConstants.PARENTS_SIZE ; parentCount++){
                uniOrderParentsRow.add(new ArrayList<Integer>());
            }//end loop.


            //Iterate through all the elements and create new arrays for PMX with 
            //fixed elements removed.
            for(int elementPosition=0 ; elementPosition < SudokuConstants.PROBLEM_SIZE;elementPosition++){
                //Only if the current position is not fixed.
                if(childRow.get(elementPosition) == 0){
                    for(int parentCount = 0 ; parentCount < SudokuConstants.PARENTS_SIZE;parentCount++){
                        //Copy the element of the particular row to the newly created list for PMX.
                        uniOrderParentsRow.get(parentCount).add(parents.get(parentCount).getSudokuSolution().get(row).get(elementPosition));
                    }//end for.
                    uniOrderPermutationSize++;
                }//end if.
            }//end loop.
            
            //Create an array list to store mutated elements. //Copy all the elements in the list to that arraylist.
            ArrayList<Integer> uniOrderMutatedRow = new ArrayList<Integer>(uniOrderParentsRow.get(0));
//            System.out.println("Parent 1 :" + uniOrderParentsRow.get(0) + " Parent 2:" + uniOrderParentsRow.get(1));
            //Copy the first parent element into the child.
            for(int uniOrderCount = 0 ,copyCount =1 ; uniOrderCount < uniOrderPermutationSize && copyCount < uniOrderPermutationSize; uniOrderCount++){
                boolean isCopied = false;
                //Iterate through newly created row and replace the even placed elements.
                for(int uniOrderReplace = 0 ; uniOrderReplace < uniOrderPermutationSize ; uniOrderReplace+=2){
                    //Check if the element already exist or not.
                    if(uniOrderMutatedRow.get(uniOrderReplace) == uniOrderParentsRow.get(1).get(uniOrderCount)){
                        isCopied = true;
                        break;
                    }//end if.
                }//end for.
                
                //Copy to the child.
                if(!isCopied){
                    uniOrderMutatedRow.set(copyCount, uniOrderParentsRow.get(1).get(uniOrderCount));
                    copyCount +=2;
                }//end if.
            }//end for.
           
//            System.out.println("Mutated Child:" + uniOrderMutatedRow);
            
            
            
            //Now copy the element to the Child row.
            for(Integer uniOrderRecombElement : uniOrderMutatedRow){
                int childRowCount = 0;
                //Find the vacant element.
                while(childRow.get(childRowCount) != 0){
                    childRowCount++;
                }//end if.
                childRow.set(childRowCount,uniOrderRecombElement);
            }//end if.
            
//            System.out.println("Child Row to be added:" + childRow);
            child.getSudokuSolution().add(childRow);
        }//end for.
        
        child.setFitness(child.fitnessFunction());
        
//        System.out.println("Crossover Child:" + child);
        return child;
    }//end method.
    
    
    
    /**
     * This method does the parent selection from the population to find the solution of the
     * sudoku puzzle.
     * @return is the list of parents who will participate in the recombination.
     * @param cycle is the current cycle.
     * @param populationSegment is the current island's population segment.
     * @param populationSegmentFitness  is the current island's population fitness segment.
     */
    public ArrayList<Chromosome> parentSelection(int cycle,ArrayList<Chromosome> populationSegment,ArrayList<Integer> populationSegmentFitness){
//        System.out.println("Parent Selection");
        if(this.diversityTooLow){
            selectionSize = SudokuConstants.PARENTS_SIZE; //If the population is under some diversity than remove selection pressure.
        }else{
            selectionSize = (2*SudokuConstants.PARENTS_SIZE) + (int)(((double)cycle/ (double)SudokuConstants.EVOLUTION_CYCLES)*10); // Increase selection pressure as evolution goes on
        }
        
        
        ArrayList<Chromosome> parents = new ArrayList<Chromosome>(SudokuConstants.PARENTS_SIZE);
        ArrayList<Integer> selectionSequenceList = new ArrayList<Integer>(selectionSize);//List of 5 individual's sequence.
        ArrayList<Integer> selectionFitnessList = new ArrayList<Integer>(selectionSize);//List of 5 individual's fitness
        
        
        //Add elements to the random sequence till selection size. 
        while(selectionSequenceList.size() <  selectionSize){
                int randomSeq = new Random().nextInt(populationSegment.size());
                //If the list does not contain the random number.
                if(!selectionSequenceList.contains(randomSeq)){
                   selectionSequenceList.add(randomSeq);  
                   selectionFitnessList.add(populationSegmentFitness.get(randomSeq));
                }//end if.
        }//end while.
        
        
        //Find the best 2 out of the 5. 
        //can be changed later to be dynamic.
        int parent1 = 0; // pointer to the fittenst parent.
        int parent2 = 1; // pointer to the second fittest parent.
        
        for(int i = 2; i < selectionSize ; i++){
            if(selectionFitnessList.get(i) < selectionFitnessList.get(parent1)){
                parent1 = i;
            }else if(selectionFitnessList.get(i) < selectionFitnessList.get(parent2)){
                parent2 = i;
            }else{
                //Do nothing.
            }//end if -else ladder.
        }//end for.
        
        //Add two parent to the list.
        parents.add(populationSegment.get(selectionSequenceList.get(parent1)));
        parents.add(populationSegment.get(selectionSequenceList.get(parent2)));
        
//        System.out.println("-----Selection-----");
//        System.out.println("Selected Parent 1:" + parents.get(0));
//        System.out.println("Selected Parent 2:" + parents.get(1));
//        
        return parents;
    }//end method.
    
    
    /**
     * This method checks if the diversity has fall below certain range,then 
     * apply the mechanism to update it.
     * @param populationSegment is the current island's population segment.
     * @param populationSegmentFitness  is the current island's population fitness segment.
     */
    public void diversityCheck(ArrayList<Chromosome> populationSegment,ArrayList<Integer> populationSegmentFitness){
        this.diversityTooLow = false; //Reset the diversith too low value.
        //Count through the loop.
        ArrayList<Integer> diffrentValues = new ArrayList<Integer>();
        ArrayList<Integer> diffrentValueCount = new ArrayList<Integer>();
        
        for(int populationCount = 0 ; populationCount < populationSegment.size(); populationCount++){
            //Check if the array contains a particular fitness value or not.
            if(!diffrentValues.contains(populationSegmentFitness.get(populationCount))){
                diffrentValues.add(populationSegmentFitness.get(populationCount));
                diffrentValueCount.add(1);
            }else{
                //Loop through the differetn values.
                for(int count = 0 ; count < diffrentValues.size() ; count++){
                    //If the element matches a value in the array then
                    //Add value to it.
                    if(diffrentValues.get(count) == populationSegmentFitness.get(populationCount)){
                        diffrentValueCount.set(count,diffrentValueCount.get(count) + 1);//Increase the count of the elements.
                    }//end if.
                }//end for.
            }//end if-else.
        }//end loop.
        
        //Check if any of the counter increases the half of the population size.
        for(int diffValueCounter : diffrentValueCount){
            if(diffValueCounter > (populationSegment.size()/ 1.5)){
                this.diversityTooLow = true;
                this.diversityMaintananceCounter = 0;
            }//end if.
        }//end for.
        
    }//end method.
    
    /**
     * This method different techniques for the survivor selection.
     * @param child is the newly created child.
     * @param cycle is the current cycle.
     * @param populationSegment is the current island's population segment.
     * @param populationSegmentFitness  is the current island's population fitness segment.
     */
    public void survivorSelection(Chromosome child,int cycle,ArrayList<Chromosome> populationSegment,ArrayList<Integer> populationSegmentFitness){
//           System.out.println("Survivor Selection");
//           Check on every 0.5% of evolution cycles.
           if(SudokuConstants.EVOLUTION_CYCLES > 500 &&  cycle%(SudokuConstants.EVOLUTION_CYCLES/500) == 0){
               diversityCheck(populationSegment,populationSegmentFitness);//Check diversity.
           }//end if.
            
           if(this.diversityTooLow && (diversityMaintananceCounter < (2 * populationSegment.size()))){
               survivorSelectionRandom(child,populationSegment,populationSegmentFitness);
               diversityMaintananceCounter++;
           } else{
               survivorSelectionAvgWorst(child,populationSegment,populationSegmentFitness);
           }//end if-else.
        
//          if(cycle < (SudokuConstants.EVOLUTION_CYCLES/10)){
               
//          }else{
//              survivorSelectionRemoveWeakes(child);
//          }//end if-else.
    }//end method.
    
    
    /**
     * This method performs survivor selection for the newly created individuals. 
     * The selection is an elite strategy here on the basis of fitness, which replaces
     * the weakest individual form the population.
     * @param child is the newly created individual after recombination and mutation.
     * @param populationSegment is the current island's population segment.
     * @param populationSegmentFitness  is the current island's population fitness segment.
     */
    public void survivorSelectionRemoveWeakes(Chromosome child,ArrayList<Chromosome> populationSegment,ArrayList<Integer> populationSegmentFitness){
                //Get the least fitness valued individual.
        int sequence = 0;
        int leastFitness = populationSegmentFitness.get(sequence);
        //Route through the whole list and find the list fitness.
        for(int i=0 ; i < populationSegmentFitness.size(); i++ ){
            //If current individual's fitness function value is greater than the leastFitness
            //then put current as least and update the sequence. 
            if(leastFitness < populationSegmentFitness.get(i)){
                leastFitness = populationSegmentFitness.get(i);
                sequence = i;
            }//end if.
        }//end for.
        
        //Set the individual to population and fitness list.
//        System.out.println("---------Survivor Selection---------");
//        System.out.println("Least fitness Sequence : " + (sequence+1) + " Fitness:" + leastFitness);
        //Replace the individual based on elite strategy.
        if(leastFitness > child.getFitness()){
            populationSegment.set(sequence, child);
            populationSegmentFitness.set(sequence, child.getFitness());  
            
            
//            System.out.println("Child:" + child);
//            System.out.println("Child Replaced this individual: " + populationSegment.get(sequence) + " Fitness:" + populationSegmentFitness.get(sequence));
        }else{
//            System.out.println("No Replacement.");
        }//end if-else
    }
    
    
    /**
     * This method first counts the average fitness of the population.
     * Now randomly choose any individual from the population and check if its fitness
     * is worse than the average than replace.
     * @param child is the newly created individual after recombination and mutation.
     * @param populationSegment is the current island's population segment.
     * @param populationSegmentFitness  is the current island's population fitness segment.
     */
    public void survivorSelectionAvgWorst(Chromosome child,ArrayList<Chromosome> populationSegment,ArrayList<Integer> populationSegmentFitness){
        double avgFitness = 0;//Average Fitenss.
        //Route through the whole list and find the list fitness.
        for(int i=0 ; i < populationSegmentFitness.size(); i++ ){
            avgFitness += populationSegmentFitness.get(i);
        }//end for.
        
        avgFitness /= populationSegment.size();
        
        //Loop until replacement done.
        while(true){
            int randomIndividual = new Random().nextInt(populationSegment.size());
            
            //If the randomly choosen individual's fitness is below the average fitness,
            // replace the individual with the current one. 
            if(populationSegmentFitness.get(randomIndividual) >= avgFitness){
                populationSegment.set(randomIndividual, child);
                populationSegmentFitness.set(randomIndividual, child.getFitness());  
                break;
            }//end if.
        }//end while.
    }//end method.
    
    
    /**
     * This method do not put any constraints and replace the new child with any
     * random individual in the population.
     * @param child is the newly created individual after recombination and mutation.
     * @param populationSegment is the current island's population segment.
     * @param populationSegmentFitness  is the current island's population fitness segment.
     */
    public void survivorSelectionRandom(Chromosome child,ArrayList<Chromosome> populationSegment,ArrayList<Integer> populationSegmentFitness){
            int randomIndividual = new Random().nextInt(populationSegment.size());
            
            //Replace the random individual with the new child.
            populationSegment.set(randomIndividual, child);
            populationSegmentFitness.set(randomIndividual, child.getFitness());  
       
    }//end method.
    
    
    
    /**
     * This method initializes the population of the individuals for the Sudoku Puzzle.
     * Here,the problem is taken in the consideration to create different individuals.
     * The fixed elements in the Sudoku puzzle are not modified. 
     */
    public void initialize(){
        //Initialize all the elements in the population.
        for(int count = 1 ; count <= SudokuConstants.POPULATION_SIZE; count++){
            
            Chromosome individual = new Chromosome(this.sudokuProblem);
            
//            System.out.println("---------------");
//            System.out.println("Individual:" + individual);
            
            this.population.add(individual);//Add the individual.
            this.populationFitness.add(individual.getFitness());//Add the fitness list value.
        }//end for.
    }//end method.
     
    /**
     * This method runs GA for a particular island in the island population model.
     * @param populationSegment is a particular segment of the overall population.
     * @param populationSegmentFitness is the fitness of the array of the population segment.
     * @param switchCycle is the cycle till the next island exchange happens.
     * @param type is the type of island for selecting mutations.
     * @return is the final position of the zero.
     */
    public int runEA(ArrayList<Chromosome> populationSegment,ArrayList<Integer> populationSegmentFitness,int switchCycle,int type){
        int finalPosition = -1;
        System.out.println("PopulationFitness" + populationSegmentFitness);
        //Iterate through cycles. 
        for(int cycle = 0; cycle < SudokuConstants.EXCHANGE_CYCLE ; cycle++){
             //Run the whole cycle.
             survivorSelection(mutation(recombination(parentSelection(cycle,populationSegment,populationSegmentFitness),cycle),cycle,type),cycle,populationSegment,populationSegmentFitness);
             totalCycle++;
//             System.out.println("Type:" + type + "Cycle: " + cycle + " PopulationFitness:" + populationSegmentFitness);
             
             if(populationSegmentFitness.contains(0)){
                 
                 for(int findPosition = 0 ; findPosition < populationSegment.size(); findPosition++){
                    if(populationSegmentFitness.get(findPosition) == 0){
                        finalPosition = findPosition;
                        System.out.println("Final : " + populationSegment.get(findPosition));
                        break;
                    }
                 }//end for.
                 
                 if(finalPosition != -1)
                     break;
             }//end if.
        }//end for.
        
        return finalPosition;
    }//end method.
    
    
    
    /**
     * This method exchanges best individual from each of the island to the other island.
     * @param populationSegment is the arraylist of all islands.
     * @param populationSegmentFitness is the fitness list of all the islands.
     * @param cycle  is the current running cycle.
     */
    public void exchangeBest(ArrayList<ArrayList<Chromosome>> populationSegment,ArrayList<ArrayList<Integer>> populationSegmentFitness,int cycle){
        //iterate through each segments.
        for(int segment = 0 ; segment < SudokuConstants.ISLANDS;segment++){
            //Find the best fitness.
            int bestFitnessPosition = 0;
            int bestFitness = 1000;
            for(int segmentSize = 0; segmentSize < populationSegment.size(); segmentSize++){
                //Find the best fitness and its position.
                if(populationSegmentFitness.get(segment).get(segmentSize) < bestFitness){
                    bestFitness = populationSegmentFitness.get(segment).get(segmentSize);
                    bestFitnessPosition = segmentSize;
                }
            }//end for.
             //Copy to all other segments.
             for(int copySegment = 0 ; copySegment < SudokuConstants.ISLANDS;copySegment++){
                 if(copySegment != segment)//Don't copy into itself
                 survivorSelection(populationSegment.get(segment).get(bestFitnessPosition),cycle, populationSegment.get(copySegment), populationSegmentFitness.get(copySegment));
             }//end for.
        }//end for.
    }//end method.
    
    /**
     * This method runs the Genetic Algorithm with the population model available.
     * The system uses island model for managing the population.
     */
    public void runGeneticAlgorithm(){
        //Initialize the population
        initialize();
        
//        System.out.println("Fitness:" + this.populationFitness);
        for(int islandSize = 0 ; islandSize < SudokuConstants.ISLANDS ; islandSize++){
                int segmentStart = islandSize * (SudokuConstants.POPULATION_SIZE/SudokuConstants.ISLANDS);
                int segmentEnd = segmentStart + (SudokuConstants.POPULATION_SIZE/SudokuConstants.ISLANDS) - 1;
                populationIslands.add(new ArrayList<Chromosome> (population.subList(segmentStart,segmentEnd)));//Set the list.
                populationFitnessIslands.add(new ArrayList<Integer> (populationFitness.subList(segmentStart,segmentEnd)));//Set the list.
//                System.out.println("Population: " + islandSize + "  " + populationIslands.get(islandSize) + "   :" + populationFitnessIslands.get(0));
        }
        
        //Start Cycles.
        
        for(int cycle = 0 ; cycle < SudokuConstants.EVOLUTION_CYCLES; cycle += (4*SudokuConstants.EXCHANGE_CYCLE)){
            for(int islandSize = 0 ; islandSize < SudokuConstants.ISLANDS ; islandSize++){
                //Run EA for all the islands.
                if(runEA(populationIslands.get(islandSize),populationFitnessIslands.get(islandSize),cycle,islandSize%4) != -1){
                    System.out.println("Cycles:" + this.totalCycle);
                    cycle = SudokuConstants.EVOLUTION_CYCLES;
                    break; //End the loop.
                }//end if.
            }//end.       
            
            exchangeBest(populationIslands,populationFitnessIslands,cycle);
        }//end for.
        //Copy the population segments into the population.
        for(int islandSize = 0,count =0 ; islandSize < SudokuConstants.ISLANDS ; islandSize++){
            for(int segmentSize = 0 ; segmentSize < populationIslands.size() ; segmentSize++ ){
                population.set(count, populationIslands.get(islandSize).get(segmentSize));
                populationFitness.set(count, populationFitnessIslands.get(islandSize).get(segmentSize));
            }//end for.
        }//end for.
        
    }//end method.
    
    
}//end class. 
