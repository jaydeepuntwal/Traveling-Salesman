import edu.rit.util.Random;

public class InputGen2 {

	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]);

		int distMat[][] = new int[n][n];
		Random r = new Random(Long.parseLong(args[1]));
		System.out.println(n);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {

				if (i != j && distMat[i][j] != 0) {
					int rNo = r.nextInt(20000) + 1700;
					distMat[i][j] = rNo;
					distMat[j][i] = rNo;
				}

			}

		}
		for (int i = 0; i < n; i++) {
			System.out.println(i + 1);
		}
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {

				System.out.print(distMat[i][j]);
				if (j != n - 1)
					System.out.print(" ");
			}
			if (i != n - 1)
				System.out.println();
		}
	}
}