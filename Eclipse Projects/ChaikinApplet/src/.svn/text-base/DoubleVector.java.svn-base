

import java.io.Serializable;

/**
 * Represents a vector with double coordinates and predefined size.
 * i.e. calculated at defenition time.
 * 
 * @author plamen
 */
public class DoubleVector implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -173252758470763964L;

	double x;

	double y;

	double size;

	public DoubleVector() {
		x = 0.0;
		y = 0.0;
		size = 0.0;
	}

	/**
	 * Construct a new double vector representation of 
	 * the coordinates of the point given.
	 * 
	 * @param dx
	 * @param dy
	 */
	public DoubleVector(double dx, double dy) {
		x = dx;
		y = dy;
		size = getSize(x, y);
	}
	
	/**
	 * Returns the size of the vector.
	 * Use this method in order to precalculate the size of the vector.
	 * 
	 * @return the size of the vector
	 */
	public double getSize() {
		size = getSize(x, y);
		return size;
	}
	
	/**
	 * Normalize the currrent vector.
	 */
	public void normalize() {
		size = Math.sqrt((x) * (x) + (y) * (y));
		if (size != 0) {
			x = (double) (x / size);
			y = (double) (y / size);
			size = 1.0;
		} else {
			size = 0.0;
		}
	}
	
	/**
	 * Creates a new normalized vector for the current.
	 * 
	 * @return the new normalized vector
	 */
	public DoubleVector createNormalized() {
		DoubleVector v = new DoubleVector(this.x, this.y);
		v.size = Math.sqrt((v.x) * (v.x) + (v.y) * (v.y));
		if (v.size != 0) {
			v.x = (double) (v.x / v.size);
			v.y = (double) (v.y / v.size);
			v.size = 1.0;
		} else {
			v.size = 0.0;
		}
		return v;
	}

	/**
	 * Calculates the distence between the given two points in double vector representation.
	 * 
	 * @param v1
	 * @param v2
	 * @return the distance between the two poins 
	 */
	public static double distance(DoubleVector v1, DoubleVector v2) {
		DoubleVector v = new DoubleVector((v2.x - v1.x), (v2.y - v1.y));
		return v.size;
	}

	/**
	 * Calculate the scalar product of the given vectors.
	 * 
	 * @param dv1
	 * @param dv2
	 * @return the scalar product
	 */
	public static double scalarProduct(DoubleVector dv1, DoubleVector dv2) {
		return (dv1.x * dv2.x + dv1.y * dv2.y);
	}
	
	/**
	 * Calculate the cosine between the given vectors.
	 * 
	 * @param dv1
	 * @param dv2
	 * @return
	 */
	public static double cosineAngle(DoubleVector dv1, DoubleVector dv2) {
		double sdv1 = dv1.getSize();
		double sdv2 = dv2.getSize();
		if ((sdv1 == 0.0) || (sdv2 == 0.0)) {
			return 0.0;
		} else {
			return ((double) (Math.acos(scalarProduct(dv1, dv2) / (sdv1 * sdv2))));
		}
	}

	/**
	 * Helper method that calculates the size of the vector.
	 * 
	 * @param dx
	 * @param dy
	 * @return the size of the vector
	 */
	private double getSize(double dx, double dy) {
		return Math.sqrt((double) ((dx * dx) + (dy * dy)));
	}
	
}
