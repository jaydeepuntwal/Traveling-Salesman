import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import edu.rit.pj2.Job;
import edu.rit.pj2.Task;
import edu.rit.util.IntList;
import edu.rit.util.Random;

public class TSPRandomSeq extends Job {

	public void main(String[] args) {
		rule().task(TSPRandomSequential.class).runInJobProcess().args(args);
	}

	public static class TSPRandomSequential extends Task {
		public void main(String args[]) {

			Scanner scan = new Scanner(System.in);

			int n = scan.nextInt();

			scan.nextLine();

			String[] city = new String[n];
			int[][] distance = new int[n][n];

			TSPPath initial = new TSPPath();

			for (int i = 0; i < n; i++) {
				initial.path.addLast(i);
				city[i] = scan.nextLine();
			}

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					distance[i][j] = scan.nextInt();
				}
			}

			scan.close();

			long T = Long.parseLong(args[0]);
			long seed = Long.parseLong(args[1]);
			Random prng = new Random(seed);

			int min;
			int index;
			boolean[] visited = new boolean[n];
			IntList listOfCities;
			TSPPath tspPath = new TSPPath();
			int cost;
			int lastCity;

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
					lastCity = listOfCities.get(listOfCities.size() - 1);
					index = -1;
					for (int j = 0; j < n; j++) {
						if ((distance[lastCity][j] < min) && (!visited[j])) {
							min = distance[lastCity][j];
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

			for (long i = 0; i < T; i++) {
				TSPPath candidate = tspPath.clone();
				shuffle(candidate);
				candidate.cost = getDistance(candidate.path, distance);
				initial.reduce(candidate);
			}

			initial.path.addLast(initial.path.get(0));

			// Display results
			while (!initial.path.isEmpty()) {
				if (initial.path.size() != 1)
					System.out
							.print(city[initial.path.removeFirst()] + " --> ");
				else
					System.out.println(city[initial.path.removeFirst()]);
			}

			System.out.println("Total Cost: " + initial.cost + " Km.");

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

		public static int getDistance(IntList path, int[][] distance) {

			int dist = 0;

			for (int i = 0; i < path.size() - 1; i++) {
				dist += distance[path.get(i)][path.get(i + 1)];
			}

			dist += distance[path.get(path.size() - 1)][path.get(0)];

			return dist;
		}
	}

}
