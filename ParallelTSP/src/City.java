//******************************************************************************
//
// File:    City.java
//
//******************************************************************************

/**
 * Class City defines a city using its x and y co-ordinates
 * 
 * @author Jaydeep Untwal, Sushil Mohite, Harsh Sadhvani
 */
public class City {

	private double x;
	private double y;

	City(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Calculate euclidean distance between two cities
	 * 
	 * @param c City
	 * @return euclidean distance
	 */
	public double distance(City c) {
		double euc = Math.sqrt(Math.pow((this.x - c.x), 2)
				+ Math.pow((this.y - c.y), 2));
		return euc;
	}

}
