import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import edu.rit.pj2.Job;
import edu.rit.pj2.Task;
import edu.rit.util.IntList;
import edu.rit.util.Random;

public class TSPRandomSeq extends Task {

	public void main(String args[]) {

		// Input
		int N = Integer.parseInt(args[0]);
		long T = Long.parseLong(args[1]);
		long seed = Long.parseLong(args[2]);
		Random prng = new Random(seed);

		// Generate Cities
		City[] cities = new City[N];

		for (int i = 0; i < N; i++) {
			double x = prng.nextDouble();
			double y = prng.nextDouble();
			cities[i] = new City(i, x, y);
		}

		// Nearest Neighbor Algorithm
		double min;
		int index;
		boolean[] visited = new boolean[N];
		IntList listOfCities;
		TSPPath tspPath = new TSPPath();
		double cost;
		int lastCity;

		// Determines the first city from where the path is to be started
		for (int i = 0; i < N; i++) {

			// Initialize the cost to zero for a new path
			cost = 0;

			for (int j = 0; j < N; j++) {
				visited[j] = false;
			}

			// We start from the ith city
			visited[i] = true;

			// Initialize the list
			listOfCities = new IntList();

			// Add the first city to the list
			listOfCities.addLast(i);

			while (listOfCities.size() != N) {
				min = Integer.MAX_VALUE;
				lastCity = listOfCities.get(listOfCities.size() - 1);
				index = -1;
				for (int j = 0; j < N; j++) {
					if ((cities[lastCity].distance(cities[j]) < min)
							&& (!visited[j])) {
						min = cities[lastCity].distance(cities[j]);
						index = j;
					}
				}

				cost += min;
				visited[index] = true;
				listOfCities.addLast(index);
			}

			cost += cities[listOfCities.get(listOfCities.size() - 1)]
					.distance(cities[i]);
			listOfCities.addLast(i);

			tspPath.reduce(new TSPPath(cost, listOfCities));
		}

		for (long i = 0; i < T; i++) {
			TSPPath candidate = tspPath.clone();
			shuffle(candidate);
			candidate.cost = getDistance(candidate.path, cities);
			tspPath.reduce(candidate);
		}

		// Display results
		while (!tspPath.path.isEmpty()) {
			if (tspPath.path.size() != 1)
				System.out.print(cities[tspPath.path.removeFirst()] + " --> ");
			else
				System.out.println(cities[tspPath.path.removeFirst()]);
		}

		System.out.println("Total Cost: " + tspPath.cost + " Km.");

	}

	private void shuffle(TSPPath candidate) {
		List<Integer> path = new ArrayList<Integer>();
		for (int i = 0; i < candidate.path.size(); i++) {
			path.add(candidate.path.get(i));
		}
		candidate.path.clear();
		Collections.shuffle(path);

		for (Integer i : path) {
			candidate.path.addLast(i);
		}

	}

	public static int getDistance(IntList path, City[] cities) {

		int dist = 0;

		for (int i = 0; i < path.size() - 1; i++) {
			dist += cities[path.get(i)].distance(cities[path.get(i + 1)]);
		}

		dist += cities[path.get(path.size() - 1)].distance(cities[path.get(0)]);

		return dist;
	}

}
