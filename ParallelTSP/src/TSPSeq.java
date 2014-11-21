import java.util.Scanner;
import edu.rit.pj2.Task;
import edu.rit.util.IntList;

public class TSPSeq extends Task {

	public void main(String[] args) throws Exception {

		Scanner matrixScanner = new Scanner(System.in);

		int matrixSize = matrixScanner.nextInt();
		
		matrixScanner.nextLine();

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
				visited[j] = false;
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
			if (tspPath.path.size() != 1)
				System.out.print(city[tspPath.path.removeFirst()] + " --> ");
			else
				System.out.print(city[tspPath.path.removeFirst()] + " - Total Cost : ");
		}
		
		System.out.print(tspPath.cost);
	}
}
