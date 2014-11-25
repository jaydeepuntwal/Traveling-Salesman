public class City {

	private double x;
	private double y;

	City(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double distance(City c) {
		double euc = Math.sqrt(Math.pow((this.x - c.x), 2)
				+ Math.pow((this.y - c.y), 2));
		return euc;
	}

}
