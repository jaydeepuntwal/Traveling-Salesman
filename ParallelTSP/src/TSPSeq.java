import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import edu.rit.pj2.Task;

public class TSPSeq extends Task {

	public void main(String[] args) throws Exception {

		Scanner matrixScanner = new Scanner(new File(args[0]));

		int matrixSize = matrixScanner.nextInt();

		int[][] distance = new int[matrixSize][matrixSize];
		String[] city = new String[matrixSize];

		for (int i = 0; i < matrixSize; i++) {
			city[i] = matrixScanner.nextLine();
		}

		for (int i = 0; i < matrixSize; i++) {
			for (int j = 0; j < matrixSize; j++) {
				distance[i][j] = matrixScanner.nextInt();
			}
		}
		
		matrixScanner.close();
		
		int min;
		int index;
		boolean[] visited = new boolean[matrixSize];
		List<Integer> listOfCities;
		
		// wow wow
		for (int i = 0; i < matrixSize; i++) {
			
			min = distance[i][i + 1];
			index = i + 1;
			
			for (int j = 0; j < matrixSize; j++) {
				visited[i] = false;
			}
			
			visited[i] = true;
			
			listOfCities = new ArrayList<Integer>();
			listOfCities.add(i);
			
			while (listOfCities.size() != matrixSize) {
				for (int j = 0; j < matrixSize; j++) {
					if ((distance[i][j] < min) && (!visited[j])) {
						min = distance[i][j];
						index = j;
					}
				}
				
				visited[index] = true;
				listOfCities.add(index);						
			}
		}
	}

	/*private void performTSP(int currCity, int matrixSize, int[][] distance) {

		int min = distance[currCity][currCity + 1];
		int index = currCity + 1;
		boolean[] visited = new boolean[matrixSize];
		
		for (int i = 0; i < matrixSize; i++) {
			visited[i] = false;
		}
		
		visited[currCity] = true;
		
		List<Integer> listOfCities = new ArrayList<Integer>();
		listOfCities.add(currCity);
		
		while (listOfCities.size() != matrixSize) {
			for (int i = 0; i < matrixSize; i++) {
				if ((distance[currCity][i] < min) && (!visited[i])) {
					min = distance[currCity][i];
					index = i;
				}
			}
			
			visited[index] = true;
			listOfCities.add(index);						
		}
	}*/
}
