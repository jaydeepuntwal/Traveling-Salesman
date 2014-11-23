import java.util.ArrayList;
import java.util.Scanner;

import edu.rit.pj2.Job;
import edu.rit.pj2.Task;
import java.util.Random;

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

			for (int i = 0; i < n; i++) {
				city[i] = scan.nextLine();
			}

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					distance[i][j] = scan.nextInt();
				}
			}

			scan.close();
			int T = Integer.parseInt(args[0]);
			long minDist = Long.MAX_VALUE;
			int randomValues[] = new int[n];
			int finalValues[] = new int[n];
			ArrayList<Long> sums = new ArrayList<Long>();
			for (int i = 0; i < n; i++) {
				randomValues[i] = i;
			}
			for (int i = 0; i < T; i++) {
				long sum = 0;

				for (int j = 1; j < n; j++) {
					sum += distance[randomValues[0]][randomValues[j]];
					if(j==n-1)
						sum += distance[randomValues[j]][randomValues[0]];

				}
				if (sum < minDist) {
					minDist = sum;
					sums.add(sum);

					System.arraycopy(randomValues, 0, finalValues, 0, n);
				}
				arrayShuffle(randomValues);//, Long.parseLong(args[1]));
			}

			for (int i = 0; i < n; i++) {
					System.out.print(city[finalValues[i]] + " --> ");
				
			}
			System.out.print(city[finalValues[0]]);

			System.out.println("\nTotal distance :" + minDist);
			System.out.println(sums);
			System.out.println(sums.size());
		}

		public void arrayShuffle(int[] arr) {
			int indexRandom, temp;
			Random random = new Random();
			for (int i = arr.length - 1; i > 0; i--) {
				indexRandom = random.nextInt(i + 1);
				temp = arr[indexRandom];
				arr[indexRandom] = arr[i];
				arr[i] = temp;
			}
		}
	}

}
