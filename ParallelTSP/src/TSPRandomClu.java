//******************************************************************************
//
// File:    TSPRandomClu.java
// 
// This Java source file uses the Parallel Java 2 Library ("PJ2") developed by
// Prof. Alan Kaminsky (RIT).
//
//******************************************************************************

import edu.rit.pj2.Job;
import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;
import edu.rit.util.IntList;
import edu.rit.util.Random;

/**
 * Class TSPRandomClu is a cluster parallel program that searches a minimum cost
 * path for given list of cities using Heuristic Search
 * 
 * It divides T iterations to partitions and each partition is given to a worker
 * task, where it shuffles the tour in parallel on multiple threads in hope of
 * searching a minimum cost path. If found, it reduces it to the best path.
 * 
 * Worker then puts a tuple in tuple space for the reducer task.
 * 
 * A separate Reduce Task collects all tuples after the all workers finish and
 * prints the best path
 * 
 * Usage:
 * <TT>java pj2 workers=<I>K</I> TSPRandomClu <I>N</I> <I>T</I> <I>Seed</I>...</TT>
 * 
 * @author Jaydeep Untwal, Sushil Mohite, Harsh Sadhvani
 * @version 25-Nov-2014
 */
public class TSPRandomClu extends Job {

	/**
	 * Main
	 */
	public void main(String[] args) {

		// Validate Input
		validateInput(args);

		// Input
		long T = Long.parseLong(args[1]);

		// Number of workers
		int K = workers();
		if (K == DEFAULT_WORKERS) {
			K = 1;
		}

		masterFor(0, T, TSPWorkerTask.class).args(args[0], args[2]);

		// Set up Final task.
		rule().atFinish().task(TSPReduceTask.class).args("" + K)
				.runInJobProcess();
	}

	/**
	 * Worker Task to perform Nearest Neighbor Algorithm with Heuristic Search
	 * on a chunk
	 */
	private static class TSPWorkerTask extends Task {

		TSPPath bestPath;
		City[] cities;

		/**
		 * Shuffle a tour
		 * 
		 * @param candidate
		 *            Tour
		 * @param cities
		 * @param seed
		 */
		private void shuffle(TSPPath candidate, City[] cities, long seed) {
			IntList candidateList = candidate.getPath();
			Random random = new Random(seed);
			for (int i = candidateList.size() - 1; i > 0; i--) {
				int indexRandom = random.nextInt(i + 1);
				candidateList.swap(i, indexRandom);
			}

			candidate = new TSPPath(getDistance(candidateList), candidateList);
		}

		/**
		 * Return the total distance of the tour
		 * 
		 * @param candidateList
		 *            Tour
		 * @return double total distance
		 */
		private double getDistance(IntList candidateList) {
			double dist = 0;

			for (int i = 0; i < candidateList.size() - 1; i++) {
				dist += cities[candidateList.get(i)]
						.distance(cities[candidateList.get(i + 1)]);
			}

			dist += cities[candidateList.get(candidateList.size() - 1)]
					.distance(cities[candidateList.get(0)]);

			return dist;
		}

		/**
		 * Main
		 */
		@Override
		public void main(String[] args) throws Exception {

			// Input
			final int N = Integer.parseInt(args[0]);
			final long seed = Long.parseLong(args[1]);
			Random prng = new Random(seed);

			IntList initial = new IntList();

			// Generate Cities
			cities = new City[N];

			for (int i = 0; i < N; i++) {
				double x = prng.nextDouble() * 100;
				double y = prng.nextDouble() * 100;
				cities[i] = new City(x, y);
				initial.addLast(i);
			}

			bestPath = new TSPPath(getDistance(initial), initial);

			workerFor().exec(new Loop() {

				TSPPath thrPath;

				public void start() {
					thrPath = threadLocal(bestPath);
				}

				public void run(int i) {
					TSPPath candidate = bestPath.clone();
					shuffle(candidate, cities, seed + rank());
					thrPath.reduce(candidate);
				}

			});

			// Put Tuple
			bestPath.rank = taskRank();
			putTuple(bestPath);
		}
	}

	/**
	 * Reduce class to get all tuples and print the best
	 */
	private static class TSPReduceTask extends Task {

		@Override
		public void main(String[] args) throws Exception {

			// Number of workers
			int K = Integer.parseInt(args[0]);

			// Template
			TSPPath template = new TSPPath();
			template.rank = 0;

			// Tuple
			TSPPath tt;

			// Best Tuple
			TSPPath bestTT = new TSPPath();

			// Get Tuples
			for (int i = 0; i < K; i++) {
				tt = takeTuple(template);
				bestTT.reduce(tt);
				template.rank++;
			}

			// Display results
			System.out.println(bestTT);
		}

	}

	/**
	 * This method displays the error message generated when the user enters a
	 * wrong input.
	 * 
	 * @param msg
	 *            error message
	 */
	private void usage(String msg) {
		System.err.println("TSPRandomClu: " + msg);
		usage();
	}

	/**
	 * This method displays the command used to run the program and is fired
	 * when the user enters a wrong input.
	 */
	private void usage() {
		System.err
				.println("Usage: java pj2 workers=<K> TSPRandomClu <N> <T> <seed>");
		System.err.println("where <K> is a number of workers");
		System.err
				.println("where <N> is a number of type int giving the number of cities >= 1");
		System.err.println("and <T> is a number of type long >= 0");
		System.err
				.println("and <seed> is a number of type long giving the random seed.");
		throw new IllegalArgumentException();
	}

	/**
	 * This method validates the input to check if the input is an integer
	 * greater than or equal to zero.
	 * 
	 * @param temp
	 */
	private void validateInput(String[] temp) {

		if (temp.length != 3) {
			usage();
		}

		int N = 0;
		long T = 0;
		try {
			N = Integer.parseInt(temp[0]);
			T = Long.parseLong(temp[1]);
			Long.parseLong(temp[2]);
		} catch (NumberFormatException e) {
			usage(e.getMessage());
		}

		if (N < 1) {
			usage("<N> should be in the range 1 <= N");
		}

		if (T < 0) {
			usage("<T> should be in the range 0 <= T");
		}
	}

}
