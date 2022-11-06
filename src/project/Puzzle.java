
package project;


import java.util.Stack;



public class Puzzle {
	private final int[][] tiles; //tiles field represents the board of the puzzle
	private int emptyColIndex,emptyRowIndex; //These fields store the index of the empty tile according to row and column

	
	
	//Constructor for the Puzzle class, copies the given array into the tiles field
	public Puzzle(int[][] tiles) {
		this.tiles= new int[tiles.length][tiles[0].length];
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				this.tiles[i][j]=tiles[i][j];
				if (tiles[i][j]==0) {
					emptyRowIndex=i;
					emptyColIndex=j;
				}
			}
		}
	}

	//Converts tiles field to string
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(tiles.length + "\n");
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				str.append(" "+tiles[i][j]);
			}
			str.append("\n");
		}
		return str.toString();

	}
	
	//Returns the dimension of the puzzle board
	public int dimension() {
		return this.tiles.length;
	}


	// sum of Manhattan distances between tiles and goal
	// The Manhattan distance between a board and the goal board is the sum
	// of the Manhattan distances (sum of the vertical and horizontal distance)
	// from the tiles to their goal positions.
	public int h() {
		int result = 0;
		for (int i = 0; i < dimension(); i++) {
			for (int j = 0; j < dimension(); j++) {
				int number = tiles[i][j];
				if (number!=0) {
					int targetRow,targetCol;
					int subtraction = 0;
					while (number>dimension()) {
						number-=dimension();
						subtraction++;
					}
					targetCol=number-1;
					targetRow=subtraction;
					result+= Math.abs(i-targetRow)+ Math.abs(j-targetCol);
				}
			}
		}
		return result;
	}
	
	
	//Checks whether the current puzzle is done
	public boolean isCompleted() {
		if (h()==0) {
			return true;
		}
		return false;
	}


	// Returns any kind of collection that implements iterable.
	// For this implementation, I choose stack.
	
	//Checks whether the empty tile can move to each direction. If so, makes the move and adds that board to the stack
	public Iterable<Puzzle> getAdjacents() {
		int[][] copy = copy(this.tiles);
		Stack<Puzzle> adjacents = new Stack<>();
		if (emptyRowIndex<copy.length-1) {
			int temp = copy[emptyRowIndex+1][emptyColIndex];
			copy[emptyRowIndex+1][emptyColIndex]=0;
			copy[emptyRowIndex][emptyColIndex]=temp;
			adjacents.push(new Puzzle(copy));
			copy[emptyRowIndex+1][emptyColIndex]=temp;
			copy[emptyRowIndex][emptyColIndex]=0;
			
		}
		
		if (emptyRowIndex>0) {
			int temp = copy[emptyRowIndex-1][emptyColIndex];
			copy[emptyRowIndex-1][emptyColIndex]=0;
			copy[emptyRowIndex][emptyColIndex]=temp;
			adjacents.push(new Puzzle(copy));
			copy[emptyRowIndex-1][emptyColIndex]=temp;
			copy[emptyRowIndex][emptyColIndex]=0;
		}
		
		if (emptyColIndex<copy.length-1) {
			int temp = copy[emptyRowIndex][emptyColIndex+1];
			copy[emptyRowIndex][emptyColIndex+1]=0;
			copy[emptyRowIndex][emptyColIndex]=temp;
			adjacents.push(new Puzzle(copy));
			copy[emptyRowIndex][emptyColIndex+1]=temp;
			copy[emptyRowIndex][emptyColIndex]=0;
		}
		
		if (emptyColIndex>0) {
			int temp = copy[emptyRowIndex][emptyColIndex-1];
			copy[emptyRowIndex][emptyColIndex-1]=0;
			copy[emptyRowIndex][emptyColIndex]=temp;
			adjacents.push(new Puzzle(copy));
			copy[emptyRowIndex][emptyColIndex-1]=temp;
			copy[emptyRowIndex][emptyColIndex]=0;
		}
		return adjacents;
		
	}
	
	//Copies a 2D Array into a new one
	private int[][] copy(int[][] source) {
		int[][] copy = new int[source.length][source.length];
		for (int i = 0; i < source.length; i++) {
			for (int j = 0; j < source.length; j++) {
				copy[i][j]=source[i][j];
			}
		}
		return copy;
	}
	
	//Returns the tiles field
	public int[][] getTiles(){
		return this.tiles;
	}
	
	public int getEmptyRowIndex() {
		return emptyRowIndex;
	}


	// You can use this main method to see your Puzzle structure.
	// Actual solving operations will be conducted in Solver.main method
//	public static void main(String[] args) {
//		int[][] array = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
//		Puzzle board = new Puzzle(array);
//		System.out.println(board);
//		System.out.println(board.dimension());
//		System.out.println(board.h());
//		System.out.println(board.isCompleted());
//		Iterable<Puzzle> itr = board.getAdjacents();
//		for (Puzzle neighbor : itr) {
//			System.out.println(neighbor);
//			System.out.println(neighbor.equals(board));
//		}
//	}
}

