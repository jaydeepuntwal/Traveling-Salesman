//******************************************************************************
//
// File:    TSPPath.java
// 
// This Java source file uses the Parallel Java 2 Library ("PJ2") developed by
// Prof. Alan Kaminsky (RIT).
//
//******************************************************************************

import java.io.IOException;

import edu.rit.io.InStream;
import edu.rit.io.OutStream;
import edu.rit.pj2.Tuple;
import edu.rit.pj2.Vbl;
import edu.rit.util.IntList;

public class TSPPath extends Tuple implements Vbl {

	int rank;
	private double cost;
	private IntList path;

	TSPPath() {
		this.path = new IntList();
		this.cost = Double.MAX_VALUE;
	}

	TSPPath(double cost, IntList path) {
		this.cost = cost;
		this.path = new IntList(path);
	}
	
	public IntList getPath() {
		return new IntList(path);
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
		out.writeDouble(cost);
		out.writeFields(path);
	}

	/*
	 * Serialize Read
	 */
	public void readIn(InStream in) throws IOException {
		rank = in.readInt();
		cost = in.readDouble();
		path = in.readFields(path);
	}

	@Override
	public TSPPath clone() {
		return new TSPPath(this.cost, this.path);
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
		this.rank = arg.rank;
		this.cost = arg.cost;
		this.path = new IntList(arg.path);
	}
	
	@Override
	public String toString() {
		IntList temp = new IntList(this.path);
		String output = "";
		while (!temp.isEmpty()) {
			if (temp.size() != 1)
				output += temp.removeFirst() + " --> ";
			else
				output += temp.removeFirst();
		}

		output = output + "\n" + String.format("Total Cost: %.3f Km%n", cost);
		return output;
	}
}
