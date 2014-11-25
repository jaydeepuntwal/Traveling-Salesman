//******************************************************************************
//
// File:    TSPRandomSeq.java
// 
// This Java source file uses the Parallel Java 2 Library ("PJ2") developed by
// Prof. Alan Kaminsky (RIT).
//
//******************************************************************************

import edu.rit.pj2.Task;
import edu.rit.util.IntList;
import edu.rit.util.Random;

/**
 * Class TSPRandomSeq is a sequential program that searches a minimum cost path
 * for given list of cities using Heuristic Search
 * 
 * Usage: <TT>java pj2 TSPRandomSeq <I>N</I> <I>T</I> <I>Seed</I>...</TT>
 * 
 * @author Jaydeep Untwal, Sushil Mohite, Harsh Sadhvani
 * @version 25-Nov-2014
 */
public class TSPRandomSeq extends Task {

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
			random.setSeed(seed + indexRandom);
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
			dist += cities[candidateList.get(i)].distance(cities[candidateList
					.get(i + 1)]);
		}

		dist += cities[candidateList.get(candidateList.size() - 1)]
				.distance(cities[candidateList.get(0)]);

		return dist;
	}

	/**
	 * Main
	 */
	public void main(String args[]) {

		// Validate input
		validateInput(args);

		// Input
		int N = Integer.parseInt(args[0]);
		long T = Long.parseLong(args[1]);
		long seed = Long.parseLong(args[2]);
		Random prng = new Random(seed);

		// Generate Cities
		cities = new City[N];
		IntList initial = new IntList();

		for (int i = 0; i < N; i++) {
			double x = prng.nextDouble() * 100;
			double y = prng.nextDouble() * 100;
			cities[i] = new City(x, y);
			initial.addLast(i);
		}

		bestPath = new TSPPath(getDistance(initial), initial);

		// Heuristic Search
		for (long i = 0; i < T; i++) {
			TSPPath candidate = bestPath.clone();
			shuffle(candidate, cities, seed);
			bestPath.reduce(candidate);
		}

		// Print Results
		System.out.println(bestPath);
	}

	/**
	 * This method displays the error message generated when the user enters a
	 * wrong input.
	 * 
	 * @param msg
	 *            error message
	 */
	private void usage(String msg) {
		System.err.println("TSPRandomSeq: " + msg);
		usage();
	}

	/**
	 * This method displays the command used to run the program and is fired
	 * when the user enters a wrong input.
	 */
	private void usage() {
		System.err.println("Usage: java pj2 TSPRandomSeq <N> <T> <seed>");
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
