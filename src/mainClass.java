import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.text.NumberFormat;

public class mainClass {

	
	/*
	 * The below variable decides which is the run that is needed
	 * Values:
	 *  1. Backtracking
	 *  2. Backtracking + MRV heuristics
	 *  3. Backtracking + MRV + Forward Checking
	 *  4. Backtracking + MRV + Constraint Propagation
	 */
	public static int run=1;
	
	/*
	 * Recursive DFS. This function is only partially done because it is never used.
	 */
	
	public void evaluate(SudokuBoard currentBoard) {
		
		if (currentBoard.isGoalState()) {
			currentBoard.PrintBoard();
			return;
		}
		
		sudokuPosition top = currentBoard.queue.poll();
		top.containsValue = true;
		for (int val: top.possibleValues) {
			top.value = val;
			top.remove(val);
			currentBoard.removeValueFromTogether(currentBoard.Rows.get(top.x), 
					currentBoard.Cols.get(top.y), 
					currentBoard.SmallBlocks.get(currentBoard.returnSmallSquareIndex(top.x, top.y)), val);
			//change counter values for everyone and reprioritize queue
			//stopped coding this function because I found better ways 
		}
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		if (args.length != 1) {
			System.out.println("Usage: java -jar <jarfile> <[1-4]> '<' inputfile ");
			System.out.println("The 1-4 is for-> 1:Backtracking 2:Backtracking + MRV " +
					"3:Backtracking + MRV + Forward Checking 4:Backtracking + MRV + Constraint Propagation");
			System.exit(0);
		}
		
		run = Integer.parseInt(args[0]);
		
		switch(run) {
		  case 1: System.out.println("Running Backtracking");
		    break;
		  case 2: System.out.println("Running Backtracking + MRV heuristics");	
		  	break;
		  case 3: System.out.println("Running Backtracking + MRV + Forward Checking"); 
		    break;
		  case 4: System.out.println("Running Backtracking + MRV + Constraint Propagation"); 
		    break;
		  default:System.out.println("'run' value invalid"); System.exit(0); 
		}
		
		ArrayList<String> inputBoard = new ArrayList<String>();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		SudokuBoard SB = null;
		try {

			String input = reader.readLine();
			
			
			String[] sizes = input.split(",");
			int BoardSize = Integer.parseInt(sizes[0].trim());
			int M = Integer.parseInt(sizes[1].trim());
			int K = Integer.parseInt(sizes[2].trim());


			
			for(int i=0;i<BoardSize;i++) {
				input = reader.readLine();
				inputBoard.add(input);
			}

			SB = new SudokuBoard(inputBoard, BoardSize, M, K, run, 0);	
									
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		//The DFS operation starts here:
		Stack<SudokuBoard> SBStack = new Stack<SudokuBoard>();
		SBStack.push(SB);
		int flagPathFound = 0;
		int popCounter = 0;
		while(!SBStack.empty()) {
			SudokuBoard temp = SBStack.pop();
			//System.out.println("popCounter=" +popCounter);
			popCounter++;
			//temp.PrintBoard();
			
			if (temp.isGoalState()) {
				temp.PrintBoard();
				System.out.println("Nodes Expanded = " +popCounter);
				System.out.println("Constraint checks = " + temp.constraintChecks);
				flagPathFound = 1;
				SBStack.clear();//If you remove this line then all the possible paths will be found 
			}
			else {
				ArrayList<SudokuBoard> successors = temp.Successor(); 
				Collections.reverse(successors);
				
				for (SudokuBoard i: successors) {
					//forward checking value is always set to true when (run != 3)
					if (i.forwardCheckingSuccess) {
						SBStack.push(i);
					}

				}
			}
		}
		if (flagPathFound == 0) {
			System.out.println("No end state could be found");
		}
		
		
		Runtime runtime = Runtime.getRuntime();
	    NumberFormat format = NumberFormat.getInstance();

	    StringBuilder sb = new StringBuilder();
	    long maxMemory = runtime.maxMemory();
	    long allocatedMemory = runtime.totalMemory();
	    long freeMemory = runtime.freeMemory();

	    sb.append("used memory: " + format.format((allocatedMemory-freeMemory) / 1024) + "KB\n");
	    sb.append("free memory: " + format.format(freeMemory / 1024) + "KB\n");
	    sb.append("allocated memory: " + format.format(allocatedMemory / 1024) + "KB\n");
	    sb.append("max memory: " + format.format(maxMemory / 1024) + "KB\n");
	    sb.append("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "KB\n");
	    System.out.println("------ Stats on memory usage (including JVM) ------ KB\n"+sb);
		
		

	}

}
