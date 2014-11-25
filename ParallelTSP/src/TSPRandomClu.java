//******************************************************************************
//
// File:    TSPClu.java
// 
// This Java source file uses the Parallel Java 2 Library ("PJ2") developed by
// Prof. Alan Kaminsky (RIT).
//
//******************************************************************************

import java.io.IOException;
import java.util.Scanner;

import edu.rit.io.InStream;
import edu.rit.io.OutStream;
import edu.rit.pj2.Job;
import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;
import edu.rit.pj2.Tuple;
import edu.rit.util.IntList;
import edu.rit.util.Random;

public class TSPRandomClu extends Job {

	public void main(String[] args) {

		// Input
		int N = Integer.parseInt(args[0]);
		long T = Long.parseLong(args[1]);
		long seed = Long.parseLong(args[2]);

		int K = workers();
		if (K == DEFAULT_WORKERS) {
			K = 1;
		}

		masterFor(0, N - 1, TSPWorkerTask.class).args(args);

		// Set up Final task.
		rule().atFinish().task(TSPReduceTask.class).args("" + K)
				.runInJobProcess();
	}

	private static class TSPWorkerTask extends Task {

		TSPPath bestPath;
		City[] cities;

		@Override
		public void main(String[] args) throws Exception {

			// Input
			final int N = Integer.parseInt(args[0]);
			final long T = Long.parseLong(args[1]);
			final long seed = Long.parseLong(args[2]);
			Random prng = new Random(seed);

			// Generate Cities
			cities = new City[N];

			for (int i = 0; i < N; i++) {
				double x = prng.nextDouble() * 100;
				double y = prng.nextDouble() * 100;
				cities[i] = new City(i, x, y);
			}

			// Nearest Neighbor

			bestPath = new TSPPath();

			workerFor().exec(new Loop() {

				double min;
				int index;
				boolean[] visited;
				IntList listOfCities;
				TSPPath thrPath;
				double cost;
				int lastCity;

				@Override
				public void start() {
					visited = new boolean[N];
					listOfCities = new IntList();
					thrPath = threadLocal(bestPath);
				}

				@Override
				public void run(int i) throws Exception {

					// Initialize the cost to zero for a new path
					cost = 0;

					for (int j = 0; j < N; j++) {
						visited[j] = false;
					}

					// We start from the ith city
					visited[i] = true;

					// Add the first city to the list
					listOfCities.addLast(i);

					while (listOfCities.size() != N) {
						min = Double.MAX_VALUE;
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

					thrPath.reduce(new TSPPath(cost, listOfCities));

				}

			});

			bestPath.rank = taskRank();
			putTuple(bestPath);
		}
	}

	private static class TSPReduceTask extends Task {

		@Override
		public void main(String[] args) throws Exception {

			int K = Integer.parseInt(args[0]);

			TSPPath template = new TSPPath();
			template.rank = 0;
			TSPPath tt;

			TSPPath bestTT = null;

			for (int i = 0; i < K; i++) {
				tt = takeTuple(template);
				if (bestTT == null) {
					bestTT = tt;
				} else if (tt.cost < bestTT.cost) {
					bestTT = tt;
				}
				template.rank++;
			}

			// Display results
			while (!bestTT.path.isEmpty()) {
				if (bestTT.path.size() != 1)
					System.out.print(bestTT.path.removeFirst() + " --> ");
				else
					System.out.println(bestTT.path.removeFirst());
			}

			System.out.printf("Total Cost: %.3f Km%n", bestTT.cost);

		}

	}

}
