import java.io.IOException;

import edu.rit.io.InStream;
import edu.rit.io.OutStream;
import edu.rit.pj2.Job;
import edu.rit.pj2.Task;
import edu.rit.pj2.Tuple;
import edu.rit.pj2.Vbl;
import edu.rit.util.IntList;

public class TSPClu extends Job {

	public void main(String[] args) {

		int n = Integer.parseInt(args[0]);

		int K = workers();
		if (K == DEFAULT_WORKERS) {
			K = 1;
		}

		masterFor(0, n - 1, TSPWorkerTask.class).args(args);

		// Set up Final task.
		rule().atFinish().task(TSPReduceTask.class).args("" + K + " " + args)
				.runInJobProcess();
	}

	private static class InformationTuple extends Tuple {
		int n;
		String[] cities;
		int[][] distance;

		InformationTuple() {

		}

		InformationTuple(int n, String[] cities, int[][] distance) {
			this.n = n;
			this.cities = cities;
			this.distance = distance;
		}

		/*
		 * Serialize Write
		 */
		public void writeOut(OutStream out) throws IOException {
			out.writeInt(n);
			out.writeStringArray(cities);

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
			cities = in.readStringArray();

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					distance[i][j] = in.readInt();
				}
			}
		}

	}

	private static class TSPTuple extends Tuple implements Vbl {

		int rank;
		int cost;
		IntList path;

		TSPTuple() {
			this.path = new IntList();
		}

		TSPTuple(int cost, IntList path) {
			this.cost = cost;
			this.path = path;
		}

		/*
		 * Match template with rank
		 */
		public boolean matchContent(Tuple target) {
			TSPTuple rt = (TSPTuple) target;
			return this.rank == rt.rank;
		}

		/*
		 * Serialize Write
		 */
		public void writeOut(OutStream out) throws IOException {
			out.writeInt(rank);
			out.writeInt(cost);
			out.writeFields(path);
		}

		/*
		 * Serialize Read
		 */
		public void readIn(InStream in) throws IOException {
			rank = in.readInt();
			cost = in.readInt();
			path = in.readFields(path);
		}

		@Override
		public TSPTuple clone() {
			return new TSPTuple(this.cost, new IntList(this.path));
		}

		@Override
		public void reduce(Vbl arg0) {
			TSPTuple arg = (TSPTuple) arg0;
			if (arg.cost < this.cost) {
				this.set(arg0);
			}
		}

		@Override
		public void set(Vbl arg0) {
			TSPTuple arg = (TSPTuple) arg0;
			this.cost = arg.cost;
			this.path = new IntList(arg.path);
		}

	}

	private static class TSPWorkerTask extends Task {

		@Override
		public void main(String[] args) throws Exception {
			System.out.println(args[0]);
		}

	}

	private static class TSPReduceTask extends Task {

		@Override
		public void main(String[] args) throws Exception {
			int K = Integer.parseInt(args[0]);

			TSPTuple template = new TSPTuple();
			template.rank = 0;

			TSPTuple tt;

			TSPTuple bestTT = null;

			for (int i = 0; i < K; i++) {
				tt = takeTuple(template);
				if (bestTT == null) {
					bestTT = tt;
				} else if (tt.cost < bestTT.cost) {
					bestTT = tt;
				}
				template.rank++;
			}

			for (int i = 0; i < bestTT.path.size(); i++) {
				System.out.println(i);
			}

			System.out.println(bestTT.cost);

		}

	}

}
