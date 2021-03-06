//******************************************************************************
//
// File:    TSPRandomSeq.java
// 
// This Java source file uses the Parallel Java 2 Library ("PJ2") developed by
// Prof. Alan Kaminsky (RIT).
//
//******************************************************************************

import java.util.BitSet;
import edu.rit.pj2.Task;
import edu.rit.util.IntList;
import edu.rit.util.Random;

/**
 * Class TSPRandomSeq is a sequential program that searches a minimum cost path
 * for given list of cities using Nearest Neighbor Algorithm
 * 
 * Nearest Neighbor (NN) Algorithm starts with each of N cities and recursively
 * travel to next minimum city. The minimum of all is selected.
 * 
 * Usage: <TT>java pj2 TSPRandomSeq <I>N</I> <I>Seed</I>...</TT>
 * 
 * @author Jaydeep Untwal, Sushil Mohite, Harsh Sadhvani
 * @version 25-Nov-2014
 */
public class TSPSeq extends Task {

	/**
	 * Main
	 */
	public void main(String args[]) {

		// Validate input
		validateInput(args);

		// Input
		int N = Integer.parseInt(args[0]);
		long seed = Long.parseLong(args[1]);
		Random prng = new Random(seed);

		// Generate Cities
		City[] cities = new City[N];

		for (int i = 0; i < N; i++) {
			double x = prng.nextDouble() * 100;
			double y = prng.nextDouble() * 100;
			cities[i] = new City(x, y);
		}

		// Nearest Neighbor Algorithm
		double min;
		int index;
		BitSet visited;
		IntList listOfCities;
		double cost;
		int lastCity;
		TSPPath tspPath = new TSPPath();

		// Determines the first city from where the path is to be started
		for (int i = 0; i < N; i++) {

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

			tspPath.reduce(new TSPPath(cost, listOfCities));
		}

		// Print Results
		System.out.println(tspPath);
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

		if (temp.length != 2) {
			usage();
		}

		int N = 0;
		try {
			N = Integer.parseInt(temp[0]);
			Long.parseLong(temp[1]);
		} catch (NumberFormatException e) {
			usage(e.getMessage());
		}

		if (N < 1) {
			usage("<N> should be in the range 1 <= N");
		}

	}

}
