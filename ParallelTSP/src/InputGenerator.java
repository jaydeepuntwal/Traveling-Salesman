import java.util.ArrayList;
import java.util.Scanner;

import edu.rit.pj2.Job;
import edu.rit.pj2.Task;
import edu.rit.util.Random;

public class InputGenerator extends Job {

	public void main(String args[]) {
		rule().task(InputGen.class).runInJobProcess().args(args);
	}

	private static class InputGen extends Task {
		int distMat[][];
		String cities[];
		int n;
		GoogleMapsAPI gapi;

		public void main(String[] args) throws Exception {

			Scanner sc = new Scanner(System.in);
			gapi = new GoogleMapsAPI();
			n = Integer.parseInt(args[0]);

			Random r = new Random(Long.parseLong(args[1]));

			sc.nextLine();

			ArrayList<String> allCities = new ArrayList<String>();
			while (sc.hasNext()) {
				allCities.add(sc.nextLine());
			}

			if (n < 1 || n > allCities.size())
				System.out.println("Number of cities should be between 1 and "
						+ allCities.size());
			cities = new String[n];

			for (int i = 0; i < n; i++) {
				int randomIndex = (r.nextInt(allCities.size()));
				cities[i] = allCities.get(randomIndex);
				allCities.remove(randomIndex);
			}

			sc.close();
			distMat=gapi.getDistance(cities,"AIzaSyDug4zcmw_tZRZQBcIKv6awK2c718nFKVQ");
			System.out.println(n);
	
			for(String c : cities){
				System.out.println(c);
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

}
