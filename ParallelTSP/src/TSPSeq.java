import java.io.File;
import java.util.Scanner;

import edu.rit.pj2.Task;
import edu.rit.util.IntList;

public class TSPSeq extends Task {

	public void main(String[] args) throws Exception {

		Scanner matrixScanner = new Scanner(new File(args[0]));

		int matrixSize = matrixScanner.nextInt();

		int[][] distance = new int[matrixSize][matrixSize];
		String[] city = new String[matrixSize];

		for (int i = 0; i < matrixSize; i++) {
			city[i] = matrixScanner.nextLine();
		}

		for (int i = 0; i < matrixSize; i++) {
			for (int j = 0; j < matrixSize; j++) {
				distance[i][j] = matrixScanner.nextInt();
			}
		}
		
		matrixScanner.close();
		
		int min;
		int index;
		boolean[] visited = new boolean[matrixSize];
		IntList listOfCities;
		TSPPath tspPath = new TSPPath();
		int cost;
		
		// Determines the first city from where the path is to be started
		for (int i = 0; i < matrixSize; i++) {
			
			// Initialize the cost to zero for a new path
			cost = 0;
			
			for (int j = 0; j < matrixSize; j++) {
				visited[i] = false;
			}
			
			// We start from the ith city
			visited[i] = true;
			
			// Initialize the list
			listOfCities = new IntList();
			
			// Add the first city to the list
			listOfCities.addLast(i);
			
			while (listOfCities.size() != matrixSize) {
				min = Integer.MAX_VALUE;
				index = -1;
				for (int j = 0; j < matrixSize; j++) {
					if ((distance[i][j] < min) && (!visited[j])) {
						min = distance[i][j];
						index = j;
					}
				}
				
				cost += min;
				visited[index] = true;
				listOfCities.addLast(index);						
			}
			
			tspPath.reduce(new TSPPath(cost, listOfCities));
		}
		
		// Display results
		while (!tspPath.path.isEmpty()) {
			System.out.println(city[tspPath.path.removeFirst()]);
			System.out.println(tspPath.cost);
		}
	}

	/*private void performTSP(int currCity, int matrixSize, int[][] distance) {

		int min = distance[currCity][currCity + 1];
		int index = currCity + 1;
		boolean[] visited = new boolean[matrixSize];
		
		for (int i = 0; i < matrixSize; i++) {
			visited[i] = false;
		}
		
		visited[currCity] = true;
		
		List<Integer> listOfCities = new ArrayList<Integer>();
		listOfCities.add(currCity);
		
		while (listOfCities.size() != matrixSize) {
			for (int i = 0; i < matrixSize; i++) {
				if ((distance[currCity][i] < min) && (!visited[i])) {
					min = distance[currCity][i];
					index = i;
				}
			}
			
			visited[index] = true;
			listOfCities.add(index);						
		}
	}*/
}
