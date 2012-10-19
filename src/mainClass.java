import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class mainClass {

	
	/*
	 * The below variable decides which is the run that is needed
	 * Values:
	 *  1. Backtracking
	 *  2. Backtracking + MRV heuristics
	 *  3. Backtracking + MRV + Forward Checking
	 *  4. Backtracking + MRV + Constraint Propagation
	 */
	public static int run=4;
	
	/*
	 * Recursive DFS
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
			
			
			
		}
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
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

			SB = new SudokuBoard(inputBoard, BoardSize, M, K, run);	
									
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
				System.out.println("popCounter=" +popCounter);
				flagPathFound = 1;
				System.exit(0);//If you remove this line then all the possible paths will be found 
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

	}

}
