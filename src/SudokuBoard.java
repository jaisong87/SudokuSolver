import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;


public class SudokuBoard {

	//Board Size
	public static int size;

	
	public sudokuPosition[][] board;//Store Board Configuration
	
	//Below are dimensions of small squares (Given in problem)
	public static int M;
	public static int K;
	
	/*
	 * The below queue contains the object of each cell on the board which are empty.
	 * Hence if the PriorityQueue is empty then the goal state is reached.
	 */
	PriorityQueue<sudokuPosition> queue;

	/*
	 * The below ArrayLists contain the objects of the sudokuPosition class, row-wise
	 * Col-wise and Small Blocks
	 */
	ArrayList<ArrayList<sudokuPosition>> Rows;
	ArrayList<ArrayList<sudokuPosition>> Cols;
	ArrayList<ArrayList<sudokuPosition>> SmallBlocks;
	
	/*
	 * The below variable decides which is the run that is needed
	 * Values:
	 *  1. Backtracking
	 *  2. Backtracking + MRV heuristics
	 *  3. Backtracking + MRV + Forward Checking
	 *  4. Backtracking + MRV + Constraint Propagation
	 * If you want to modify it use the mainClass.java file (That is the only position where I
	 * intend it to change.
	 */
	public static int run;
	
	/*
	 * This constructor is used only the first time for initialization. Called from 
	 * the mainclass.
	 */
	SudokuBoard(ArrayList<String> inputBoard, int BoardSize, int M, int K, int run){		

		SudokuBoard.size = BoardSize;
		SudokuBoard.M = M;
		SudokuBoard.K = K;
		SudokuBoard.run = run;
		
		//Initializing the in
		board = new sudokuPosition[SudokuBoard.size][SudokuBoard.size];
		
		Comparator<sudokuPosition> comp = new sudokuPosition.PositionComparator();
		queue = new PriorityQueue<sudokuPosition>(10, comp);
		
		Rows = new ArrayList<ArrayList<sudokuPosition>>();
		Cols = new ArrayList<ArrayList<sudokuPosition>>();
		SmallBlocks = new ArrayList<ArrayList<sudokuPosition>>();
		
		
		//Initializing the Rows/Cols/Squares
		for (int count=0; count < SudokuBoard.size; count++) {
			ArrayList<sudokuPosition> row = new ArrayList<sudokuPosition>();
			Rows.add(row);
			ArrayList<sudokuPosition> col = new ArrayList<sudokuPosition>();
			Cols.add(col);
			ArrayList<sudokuPosition> sqr = new ArrayList<sudokuPosition>();
			SmallBlocks.add(sqr);
		}
				
		int row = 0;
		for(String i: inputBoard) {
			int col = 0;			
			String[] rowElements = i.split(",");
			for(String j: rowElements) {
				if (j.equals("_")) {
					List<Integer> possibleValues = new ArrayList<Integer>();
					for (int vals = 1; vals <= SudokuBoard.size; vals++) {
						possibleValues.add(vals);
					}
					sudokuPosition SP = new sudokuPosition(row, col, SudokuBoard.run, false, -1, possibleValues, (size-1)*3);
					board[row][col] = SP;
					Rows.get(row).add(SP);
					Cols.get(col).add(SP);
					SmallBlocks.get(returnSmallSquareIndex(row, col)).add(SP);
				}
				else {
					List<Integer> possibleValues = new ArrayList<Integer>();
					int givenValue = Integer.parseInt(j);
					possibleValues.add(givenValue);
					sudokuPosition SP = new sudokuPosition(row, col, SudokuBoard.run, true, givenValue, possibleValues, 0);
					board[row][col] = SP;
					Rows.get(row).add(SP);
					Cols.get(col).add(SP);
					SmallBlocks.get(returnSmallSquareIndex(row, col)).add(SP);
				}
				col++;
			}
			row++;
		}
		
		/* 
		 * Once the board is initialized now it is time to initialize the values of the dataStructure
		 *  "possibleValues" and "counter" for sudokuPosition objects
		 */
		
		for (int i=0; i < SudokuBoard.size; i++) {
			for (int j=0; j < SudokuBoard.size; j++) {
				sudokuPosition tmp = board[i][j];
				if (tmp.containsValue) {
					removeValueFrom(Rows.get(tmp.x), tmp.value);
					removeValueFrom(Cols.get(tmp.y), tmp.value);
					removeValueFrom(SmallBlocks.get(returnSmallSquareIndex(tmp.x, tmp.y)), tmp.value);
				}
			}
		}
		
		//Update the counter value for all the sudokuPosition Objects
		for (int i=0; i < SudokuBoard.size; i++) {
			for (int j=0; j < SudokuBoard.size; j++) {
				sudokuPosition tmp = board[i][j];
				if (!tmp.containsValue) {
					tmp.counter = CountEmptyPositions(Rows.get(tmp.x),Cols.get(tmp.y), 
							SmallBlocks.get(returnSmallSquareIndex(tmp.x, tmp.y))) - 3;
				}
			}
		}
		
	}
		
	
	/*
	 * The below functions prints the board
	 */
	public void PrintBoard(){
		for (int i=0; i < SudokuBoard.size; i++) {
			for (int j=0; j < SudokuBoard.size; j++) {
				sudokuPosition tmp = board[i][j];
				if (!tmp.containsValue) {
					System.out.print(tmp.value + ",");
				}
				else {
					System.out.print("_,");
				}
			}
			System.out.println("");
		}
	}

	/*
	 * The below functions is for Debugging
	 */
	public void PrintBoardForDebugging(){
		for (int i=0; i < SudokuBoard.size; i++) {
			for (int j=0; j < SudokuBoard.size; j++) {
				sudokuPosition tmp = board[i][j];
				if (tmp.containsValue) {
					System.out.print("{" + tmp.value + "}");
				}
				else {
					System.out.print("{" + tmp.counter + tmp.possibleValues + "}");
				}
			}
			System.out.println("");
		}
	}
	
	public int CountEmptyPositions(ArrayList<sudokuPosition> row,
		ArrayList<sudokuPosition> col, ArrayList<sudokuPosition> SmallBlocks) {
		Set<sudokuPosition> SPSet = new HashSet<sudokuPosition>();
		for (sudokuPosition spTemp: row) {
			if (!spTemp.containsValue) {
				SPSet.add(spTemp);
			}
		}
		for (sudokuPosition spTemp: col) {
			if (!spTemp.containsValue) {
				SPSet.add(spTemp);
			}
		}
		for (sudokuPosition spTemp: SmallBlocks) {
			if (!spTemp.containsValue) {
				SPSet.add(spTemp);
			}
		}
		return SPSet.size();
	}

	public void removeValueFrom(ArrayList<sudokuPosition> temp, int val) {
		for (sudokuPosition spTemp: temp) {
			spTemp.remove(val);
		}
	}
		
	public int returnSmallSquareIndex(int row, int col) {
		return (row/SudokuBoard.M)*(SudokuBoard.size/SudokuBoard.K) + col/SudokuBoard.K;
	}
	
}
