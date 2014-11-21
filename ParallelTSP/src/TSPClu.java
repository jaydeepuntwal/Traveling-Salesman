import java.io.IOException;
import java.util.Scanner;

import edu.rit.io.InStream;
import edu.rit.io.OutStream;
import edu.rit.pj2.Job;
import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;
import edu.rit.pj2.Tuple;
import edu.rit.util.IntList;

public class TSPClu extends Job {

	public void main(String[] args) {

		Scanner scan = new Scanner(System.in);

		int n = Integer.parseInt(scan.nextLine());
		String[] city = new String[n];
		int[][] distance = new int[n][n];

		scan.nextLine();

		for (int i = 0; i < n; i++) {
			city[i] = scan.nextLine();
		}

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				distance[i][j] = scan.nextInt();
			}
		}

		scan.close();

		int K = workers();
		if (K == DEFAULT_WORKERS) {
			K = 1;
		}

		TSPInformation info = new TSPInformation(n, city, distance);
		putTuple(info);

		masterFor(0, n - 1, TSPWorkerTask.class);

		// Set up Final task.
		rule().atFinish().task(TSPReduceTask.class).args("" + K)
				.runInJobProcess();
	}

	private static class TSPInformation extends Tuple {
		int n;
		String[] city;
		int[][] distance;

		TSPInformation() {

		}

		TSPInformation(int n, String[] cities, int[][] distance) {
			this.n = n;
			this.city = cities;
			this.distance = distance;
		}

		/*
		 * Serialize Write
		 */
		public void writeOut(OutStream out) throws IOException {
			out.writeInt(n);
			out.writeStringArray(city);

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					out.writeInt(distance[i][j]);
				}
			}

		}

		/*
		 * Serialize Read
		 */
		public void readIn(InStream in) throws IOException {
			n = in.readInt();
			city = in.readStringArray();

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					distance[i][j] = in.readInt();
				}
			}
		}

	}

	private static class TSPWorkerTask extends Task {

		static TSPPath bestPath = new TSPPath();

		@Override
		public void main(String[] args) throws Exception {
			TSPInformation infoTemplate = new TSPInformation();
			final TSPInformation info = readTuple(infoTemplate);

			workerFor().exec(new Loop() {

				int min;
				int index;
				boolean[] visited;
				IntList listOfCities;
				TSPPath thrPath;
				int cost;

				@Override
				public void start() {
					thrPath = threadLocal(bestPath);
				}

				@Override
				public void run(int i) throws Exception {

					cost = 0;

					visited = new boolean[info.n];

					for (int j = 0; j < info.n; j++) {
						visited[j] = false;
					}

					// We start from the ith city
					visited[i] = true;

					// Initialize the list
					listOfCities = new IntList();

					// Add the first city to the list
					listOfCities.addLast(i);

					while (listOfCities.size() != info.n) {
						min = Integer.MAX_VALUE;
						index = -1;
						for (int j = 0; j < info.n; j++) {
							if ((info.distance[i][j] < min) && (!visited[j])) {
								min = info.distance[i][j];
								index = j;
							}
						}

						cost += min;
						visited[index] = true;
						listOfCities.addLast(index);
					}

					//cost += info.distance[listOfCities.get(listOfCities.size() - 1)][i];
					//listOfCities.addLast(i);

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

			TSPInformation infoTemplate = new TSPInformation();
			TSPInformation info = takeTuple(infoTemplate);

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
					System.out.print(info.city[bestTT.path.removeFirst()]
							+ " --> ");
				else
					System.out.println(info.city[bestTT.path.removeFirst()]);
			}

			System.out.println("Total Cost: " + bestTT.cost);

		}

	}

}
