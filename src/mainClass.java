import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class mainClass {

	
	/*
	 * The below variable decides which is the run that is needed
	 * Values:
	 *  1. Backtracking
	 *  2. Backtracking + MRV hueristics
	 *  3. Backtracking + MRV + Forward Checking
	 *  4. Backtracking + MRV + Constraint Propagation
	 */
	public static int run=1;
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ArrayList<String> inputBoard = new ArrayList<String>();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		SudokuBoard SB = null;
		try {

			String input = reader.readLine();
			System.out.println(input);
			
			
			String[] sizes = input.split(",");
			int BoardSize = Integer.parseInt(sizes[0].trim());
			int M = Integer.parseInt(sizes[1].trim());
			int K = Integer.parseInt(sizes[2].trim());


			
			for(int i=0;i<BoardSize;i++) {
				input = reader.readLine();
				inputBoard.add(input);
			}

			SB = new SudokuBoard(inputBoard, BoardSize, M, K, run);	
			SB.PrintBoardForDebugging();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
