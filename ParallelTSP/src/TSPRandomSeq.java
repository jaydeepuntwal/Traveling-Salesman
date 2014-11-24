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

			shuffleList(initial.path, seed);
			initial = new TSPPath(getDistance(initial.path, distance),
					initial.path);

			for (long i = 0; i < T; i++) {
				TSPPath candidate = initial.clone();
				shuffleList(candidate.path, seed);
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

			System.out.println("Total Cost: " + initial.cost / 1000 + " Km.");

		}

		public static int getDistance(IntList path, int[][] distance) {

			int dist = 0;

			for (int i = 0; i < path.size() - 1; i++) {
				dist += distance[path.get(i)][path.get(i + 1)];
			}

			dist += distance[path.get(path.size() - 1)][path.get(0)];

			return dist;
		}

		public static void shuffleList(IntList initial, long seed) {

			Random r = new Random(seed);

			for (int i = initial.size() - 1; i > 0; --i) {
				int j = r.nextInt(i);// random between 0 and i
				initial.swap(i, j);
			}

		}
	}

}
