import java.util.Scanner;
import edu.rit.pj2.Task;
import edu.rit.util.IntList;

public class TSPSeq extends Task {

	public void main(String[] args) {

		Scanner scan = new Scanner(System.in);

		int n = scan.nextInt();

		scan.nextLine();

		String[] city = new String[n];
		int[][] distance = new int[n][n];

		for (int i = 0; i < n; i++) {
			city[i] = scan.nextLine();
		}

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				distance[i][j] = scan.nextInt();
			}
		}

		scan.close();

		int min;
		int index;
		boolean[] visited = new boolean[n];
		IntList listOfCities;
		TSPPath tspPath = new TSPPath();
		int cost;

		// Determines the first city from where the path is to be started
		for (int i = 0; i < n; i++) {

			// Initialize the cost to zero for a new path
			cost = 0;

			for (int j = 0; j < n; j++) {
				visited[j] = false;
			}

			// We start from the ith city
			visited[i] = true;

			// Initialize the list
			listOfCities = new IntList();

			// Add the first city to the list
			listOfCities.addLast(i);

			while (listOfCities.size() != n) {
				min = Integer.MAX_VALUE;
				index = -1;
				for (int j = 0; j < n; j++) {
					if ((distance[i][j] < min) && (!visited[j])) {
						min = distance[i][j];
						index = j;
					}
				}

				cost += min;
				visited[index] = true;
				listOfCities.addLast(index);
			}

			cost += distance[listOfCities.get(listOfCities.size() - 1)][i];
			listOfCities.addLast(i);

			tspPath.reduce(new TSPPath(cost, listOfCities));
		}

		// Display results
		while (!tspPath.path.isEmpty()) {
			if (tspPath.path.size() != 1)
				System.out.print(city[tspPath.path.removeFirst()] + " --> ");
			else
				System.out.println(city[tspPath.path.removeFirst()]);
		}

		System.out.println("Total Cost: " + tspPath.cost);
	}
}
