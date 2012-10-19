
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//This class is the Position class for Sudoku puzzle

public class sudokuPosition {

	public int x = -1; //X co-ordinate
	public int y = -1; //Y co-ordinate
	
	public boolean containsValue; 
	
	//The value of the position
	public int value;
	
	/*
	 * The below variable decides which is the run that is needed
	 * Values:
	 *  1. Backtracking
	 *  2. Backtracking + MRV hueristics
	 *  3. Backtracking + MRV + Forward Checking
	 *  4. Backtracking + MRV + Constraint Propagation
	 * If you want to modify it use the mainClass.java file (That is the only position where I
	 * intend it to change.
	 */
	public static int run;
	
	//The arrayList carrying Integer's has a list of all possible values for the position (X,Y)   
	public List<Integer> possibleValues = new ArrayList<Integer>(); 
	
	//The counter below counts the number of Positions which are depended on the current position
	public int counter = -1; 

	sudokuPosition(int row, int col, int run, boolean containsValue, int value, List<Integer> vals, int count) {
		sudokuPosition.run = run;
		this.containsValue = containsValue;
		this.value = value;
		this.possibleValues = vals;
		this.x = row;
		this.y = col;
		this.counter = count;
	}
	
	public static class PositionComparator implements Comparator<sudokuPosition> {
		
		public int compare(sudokuPosition o1, sudokuPosition o2) {
			if (run == 1) {
				return 0;
			}
			if (run == 2 || run == 3 || run == 4) {
				if(o2.possibleValues.size() > o1.possibleValues.size()) {
					return -1;
				}
				else {
					if(o2.possibleValues.size() < o1.possibleValues.size()) {
						return 1;
					}
					else{
						if (o1.counter > o2.counter) {
							return -1;
						}
						else {
							if (o1.counter == o2.counter) {
								return 0;
							}
							else {
								return 1;
							}
						}
					}
				}
			}
			return 0;
		}
	}
	
	public void remove(Integer e) {
		if (possibleValues.contains(e)) {
			possibleValues.remove(e);
		}		
	}
	
	public void add(Integer e) {
		if (!possibleValues.contains(e)) {
			possibleValues.add(e);
		}		
	}
	
	/*
	 * The equals and Hashcode are for Set Comparison	
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	
	@Override
    public boolean equals(Object arg0) {
		sudokuPosition obj=(sudokuPosition)arg0;
		if (obj.x == this.x && obj.y == this.y) {
			return true;
		}
		return false;
	}
	
	@Override
    public int hashCode() {
        return x*10 + y;
    }

}
