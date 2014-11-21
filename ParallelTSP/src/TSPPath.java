import java.io.IOException;

import edu.rit.io.InStream;
import edu.rit.io.OutStream;
import edu.rit.pj2.Tuple;
import edu.rit.pj2.Vbl;
import edu.rit.util.IntList;

public class TSPPath extends Tuple implements Vbl {

	int rank;
	int cost;
	IntList path;

	TSPPath() {
		this.path = new IntList();
		this.cost = Integer.MAX_VALUE;
	}

	TSPPath(int cost, IntList path) {
		this.cost = cost;
		this.path = path;
	}

	/*
	 * Match template with rank
	 */
	public boolean matchContent(Tuple target) {
		TSPPath rt = (TSPPath) target;
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
	public TSPPath clone() {
		return new TSPPath(this.cost, new IntList(this.path));
	}

	@Override
	public void reduce(Vbl arg0) {
		TSPPath arg = (TSPPath) arg0;
		if (arg.cost < this.cost) {
			this.set(arg0);
		}
	}

	@Override
	public void set(Vbl arg0) {
		TSPPath arg = (TSPPath) arg0;
		this.cost = arg.cost;
		this.path = new IntList(arg.path);
	}
}
