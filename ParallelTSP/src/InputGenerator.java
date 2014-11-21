import java.util.Scanner;

// Test 2

import edu.rit.pj2.IntVbl;
import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;

public class InputGenerator extends Task {
	IntVbl distMat[][];
	int n;
	GoogleMapsAPI gapi = new GoogleMapsAPI();

	public void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		gapi = new GoogleMapsAPI();

		n = sc.nextInt();
		distMat = new IntVbl[n][n];
		final IntVbl distMat[][] = new IntVbl[n][n];

		final String cities[] = new String[n];

		for (int i = 0; i < n; i++) {

			cities[i] = sc.next();
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
