import java.util.ArrayList;
import java.util.Scanner;

import edu.rit.pj2.Task;
import edu.rit.pj2.IntVbl;

import edu.rit.pj2.Job;
import edu.rit.pj2.Loop;

public class InputGenerator extends Job {

	public void main(String args[]) {
		rule().task(InputGen.class).runInJobProcess().args(args);
	}

	private static class InputGen extends Task {
		IntVbl distMat[][];
		String cities[];
		int n;
		GoogleMapsAPI gapi;

		public void main(String[] args) {

			Scanner sc = new Scanner(System.in);
			gapi = new GoogleMapsAPI();
			n = Integer.parseInt(args[0]);

			distMat = new IntVbl[n][n];
			sc.nextLine();

			ArrayList<String> allCities = new ArrayList<String>();
			while (sc.next() != null) {
				allCities.add(sc.nextLine());
			}

			if (n > allCities.size())
				System.out.println("Number of cities in file is "
						+ allCities.size());
			cities = new String[n];

			for (int i = 0; i < n; i++) {
				int randomIndex = ((int) Math.random()) % allCities.size();
				cities[i] = allCities.get(randomIndex);
				allCities.remove(randomIndex);
			}

	

			sc.close();
			System.out.println(n);
			for (int i = 0; i < n; i++) {

				System.out.println(cities[i]);
			}

			parallelFor(0, n - 1).exec(new Loop() {

				public void run(int i) {
					for (int j = 0; j < n; j++) {
						int dist = gapi.getDistance(cities[i], cities[j]);
						distMat[i][j] = new IntVbl(dist);
						distMat[j][i] = new IntVbl(dist);

					}
				}
			});

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {

					System.out.print(distMat[i][j].item);
					if (j != n - 1)
						System.out.print(" ");
				}
				if (i != n - 1)
					System.out.println();
			}

		}
	}

}
