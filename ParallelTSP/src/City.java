public class City {

	int id;
	double x;
	double y;

	City(int id, double x, double y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}

	public double distance(City c) {
		double euc = Math.pow((this.x - c.x), 2) + Math.pow((this.y - c.y), 2);
		return euc;
	}

}
