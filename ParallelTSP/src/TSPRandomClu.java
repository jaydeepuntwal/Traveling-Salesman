//******************************************************************************
//
// File:    TSPClu.java
// 
// This Java source file uses the Parallel Java 2 Library ("PJ2") developed by
// Prof. Alan Kaminsky (RIT).
//
//******************************************************************************

import java.util.BitSet;

import edu.rit.pj2.Job;
import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;
import edu.rit.util.IntList;
import edu.rit.util.Random;

public class TSPRandomClu extends Job {

	public void main(String[] args) {

		// Input
		int N = Integer.parseInt(args[0]);
		long T = Long.parseLong(args[1]);

		int K = workers();
		if (K == DEFAULT_WORKERS) {
			K = 1;
		}

		T = T / K;

		masterFor(0, N - 1, TSPWorkerTask.class).args(args[0], "" + T, args[2]);

		// Set up Final task.
		rule().atFinish().task(TSPReduceTask.class).args("" + K)
				.runInJobProcess();
	}

	private static class TSPWorkerTask extends Task {

		TSPPath bestPath;
		City[] cities;

		private void shuffle(TSPPath candidate, City[] cities, long seed) {
			IntList candidateList = candidate.getPath();
			Random random = new Random(seed);
			for (int i = candidateList.size() - 1; i > 0; i--) {
				int indexRandom = random.nextInt(i + 1);
				candidateList.swap(i, indexRandom);
			}

			double dist = 0;

			for (int i = 0; i < candidateList.size() - 1; i++) {
				dist += cities[candidateList.get(i)]
						.distance(cities[candidateList.get(i + 1)]);
			}

			dist += cities[candidateList.get(candidateList.size() - 1)]
					.distance(cities[candidateList.get(0)]);

			candidate = new TSPPath(dist, candidateList);
		}

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
				cities[i] = new City(x, y);
			}

			// Nearest Neighbor

			bestPath = new TSPPath();

			workerFor().exec(new Loop() {

				double min;
				int index;
				BitSet visited;
				IntList listOfCities;
				TSPPath tspPath;
				double cost;
				int lastCity;

				@Override
				public void start() {
					tspPath = threadLocal(bestPath);
				}

				@Override
				public void run(int i) throws Exception {

					// Initialize the cost to zero for a new path
					cost = 0;

					visited = new BitSet(N);

					// We start from the ith city
					visited.set(i);

					// Initialize the list
					listOfCities = new IntList();

					// Add the first city to the list
					listOfCities.addLast(i);

					while (listOfCities.size() < N) {
						min = Double.MAX_VALUE;
						lastCity = listOfCities.get(listOfCities.size() - 1);
						index = visited.nextClearBit(0);
						for (int j = 0; j < N; j++) {
							if ((cities[lastCity].distance(cities[j]) < min)
									&& (!visited.get(j))) {
								min = cities[lastCity].distance(cities[j]);
								index = j;
							}
						}

						cost += min;
						visited.set(index);
						listOfCities.addLast(index);
					}

					cost += cities[listOfCities.get(listOfCities.size() - 1)]
							.distance(cities[i]);
					listOfCities.addLast(i);

					tspPath.reduce(new TSPPath(cost, listOfCities));

				}

			});

			for (long i = 0; i < T; i++) {
				TSPPath candidate = bestPath.clone();
				shuffle(candidate, cities, seed);
				bestPath.reduce(candidate);
			}

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

			TSPPath bestTT = new TSPPath();

			for (int i = 0; i < K; i++) {
				tt = takeTuple(template);
				bestTT.reduce(tt);
				template.rank++;
			}

			// Display results
			System.out.println(bestTT);
		}

	}

}
