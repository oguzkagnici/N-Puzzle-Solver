
package project;
   
import java.io.File;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * N-Puzzle Solver
 * @author oguzkagnici
 *
 */

public class Solver {
	private PriorityObject winner;
	
	

	// priority = moves + manhattan
	// if priority is low, it's good.
	// find a solution to the initial board
	
	//Constructor for the Solver class
	public Solver(Puzzle root) {
		System.out.println("Starting the solver...");
		if (root == null)
			throw new IllegalArgumentException();		
		solve(root);
		System.out.println("Solving is finished...");
	}


	//Actual method that solves the puzzle object via A* Search Algorithm
	private void solve(Puzzle root) {
		
		//Checks the puzzle to see if it is solvable
//		ArrayList<Integer> solveChecker = new ArrayList<>();
//		for (int i = 0; i < root.getTiles().length; i++) {
//			for (int j = 0; j < root.getTiles().length; j++) {
//				solveChecker.add(root.getTiles()[i][j]);
//			}
//		}
//		int inversion=0;
//		for (int i = 0; i < root.dimension()*root.dimension()-1; i++) {
//			for (int j = i+1; j < root.dimension()*root.dimension(); j++) {
//				if (solveChecker.get(i)!=0 && solveChecker.get(j)!=0 && solveChecker.get(i)>solveChecker.get(j)) {
//					inversion+=1;
//				}
//			}
//			
//		}
//		
//		if (root.dimension()%2!=0 && inversion%2!=0) {
//			System.out.println("No solution found");
//			winner = new PriorityObject(root, 0, null);
//			return;
//		}
//		else if (root.dimension()%2==0) {
//			if (root.dimension()-root.getEmptyRowIndex()%2==0 && inversion%2==0) {
//				System.out.println("No solution found");
//				winner = new PriorityObject(root, 0, null);
//				return;
//			}
//			
//			else if (root.dimension()-root.getEmptyRowIndex()%2!=0 && inversion%2!=0) {
//				System.out.println("No solution found");
//				winner = new PriorityObject(root, 0, null);
//				return;
//			}
//		}
		
		
		PriorityObject initialObject = new PriorityObject(root, 0, null); //Creates the PriorityObject of the given board
		PriorityQueue<PriorityObject> boards = new PriorityQueue<PriorityObject>(initialObject.comparator()); //Holds the PriorityObjects
		
		boards.add(initialObject);
		Puzzle currPuzzle = root;
		
		while (!boards.isEmpty()) { //Main loop for A* Search. Checks each possible state and selects the one with minimum estimated cost.
			for (Puzzle puzzle : currPuzzle.getAdjacents()) {
				PriorityObject newObject = new PriorityObject(puzzle, initialObject.getG()+1, initialObject);
				if (newObject.getBoard()!=initialObject.getBoard()) {
					boards.add(newObject);
				}
				
			}
			initialObject= boards.poll();
			currPuzzle=initialObject.getBoard();
			
			if (currPuzzle.isCompleted()) {
				winner=initialObject;
				break;
			}
		}
	}
	
	//Returns the number of moves made in the solution (Minimum number of moves)
	public int getMoves() {
		return winner.getG();
	}
	
	//Returns the solution path by traversing backwards from the final state via prev field
	public Iterable<Puzzle> getSolution() {
		Stack<Puzzle> steps = new Stack<>();
		ArrayList<Puzzle> stepsArray = new ArrayList<>();
		PriorityObject current= winner;
		while (current!=null) {
			steps.push(current.getBoard());
			current=current.prev;
		}
		int size = steps.size();
		
		for (int i = 0; i < size; i++) {
			stepsArray.add(steps.pop());
		}
		return stepsArray;
	}
	
	
	//Used to store puzzle and other fields such as f(cost) and prev to simplify the solution
	private class PriorityObject {
		private Puzzle board;
		
		private int f; //total estimated cost for the solution
		private PriorityObject prev;
		private int g; //moves made so far
		
		
		//Constructor for the PriorityObject class
		public PriorityObject(Puzzle board, int g, PriorityObject prev) {
			this.board=board;
			this.g=g;
			this.prev=prev;
			this.f = g+board.h();
		}
		
		//Returns the puzzle board
		private Puzzle getBoard() {
			return board;
		}
		
		//Returns the moves made so far
		private int getG() {
			return g;
		}
		
		
		
		//Returns the custom comparator to compare PriorityObject objects depending on their f field
		public Comparator<PriorityObject> comparator(){
			return new CustomComparator();
		}
		
		//Class to compare PriorityObjects
		private class CustomComparator implements Comparator<PriorityObject>{
			public int compare(PriorityObject o1, PriorityObject o2) {
				if (o1.f>o2.f) {
					return 1;
				}
				else if (o1.f<o2.f) {
					return -1;
				}
				else {
					return 0;
				}
			}
		}
	}

	// test client
	public static void main(String[] args) throws IOException {
		
		
		//Reads the input file and creates a 2D array to create the puzzle object
		File input = new File("input.txt");
		Scanner scanner = new Scanner(input);
		int size = scanner.nextInt();
		int size1 = size;
		int[][] tiles = new int[size][size];
		scanner.nextLine();
		while (scanner.hasNext() && size1-- >0) {
			String[] splitted = scanner.nextLine().split(" ");
			int[] values = Arrays.stream(splitted).mapToInt(Integer::parseInt).toArray();
			tiles[size-size1-1]=values;
			
		}
		
		
		Puzzle initial = new Puzzle(tiles); //Creates the initial puzzle object
		// Read this file int by int to create 
		// the initial board (the Puzzle object) from the file
		
		
		
		// solve the puzzle here. Note that the constructor of the Solver class already calls the 
		// solve method. So just create a solver object with the Puzzle Object you created above 
		// is given as argument, as follows:
		
		Solver solver = new Solver(initial);  // where initial is the Puzzle object created from input file

		// You can use the following part as it is. It creates the output file and fills it accordingly.
		File output = new File("output.txt");
		output.createNewFile();
		PrintStream write = new PrintStream(output);
		write.println("Minimum number of moves = " + solver.getMoves());
		for (Puzzle board : solver.getSolution())
			write.println(board);
		
			
		
	}
}

