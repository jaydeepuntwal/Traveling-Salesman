import edu.rit.pj2.Job;
import edu.rit.pj2.Task;
import edu.rit.pj2.Tuple;

public class TSPClu extends Job {

	public void main(String[] args) {

		int n = Integer.parseInt(args[0]);

		String arguments = "";

		for (int i = 0; i < args.length; i++) {
			arguments += args[i];

			if (i != args.length - 1) {
				arguments += " ";
			}
		}

		masterFor(0, n, TSPWorkerTask.class).args(arguments);

		int K = workers();
		if (K == DEFAULT_WORKERS) {
			K = 1;
		}

		// Set up Final task.
		rule().atFinish().task(TSPReduceTask.class).args("" + K)
				.runInJobProcess();
	}

	private static class TSPTuple extends Tuple {

	}

	private static class TSPWorkerTask extends Task {

		@Override
		public void main(String[] args) throws Exception {
			
		}

	}

	private static class TSPReduceTask extends Task {

		@Override
		public void main(String[] args) throws Exception {
			int K = Integer.parseInt(args[0]);

			for (int i = 0; i < K; i++) {
				
			}

		}

	}

}
