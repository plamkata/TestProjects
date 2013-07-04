

import java.awt.Dimension;

/**
 * Represents a subdivision of a curve that keeps both the initial curve
 * and the current subdivided curve of Chaikin's algorithm. Also contains the logic
 * of the algorithm providing interface for step by step execution.
 * 
 * Curves are represented as initial {@link Knot} and end {@link Knot}. Unnaturally, initial and end 
 * knots are not visible, but they only denote the begining and the end of the curve. This flexible 
 * representation gives us the oportunity to implement the algorithm for both closed and open curvse. 
 *  
 * This class also provides functionality for moving an initial point of the subdivision applying
 * all necessary changes to the current subdivided curve of the algorithm. The move operation
 * updates only part of the subdivided curve achieving best performance. Note, however, that this is 
 * only supported for closed initial curves.
 * 
 * @author plamen
 *
 */
public class Subdivision {

	int error = 0;

	double ww, hh;

	private int numberOfPoints = 0;

	private int numberOfPointsBuffer = 0;

	Knot DHead;

	Knot DTail;

	Knot IHead;

	Knot ITail;

	Knot Head;

	Knot Tail;

	int numberOfSubdivisions = 0;

	boolean startboundary = false;

	boolean endboundary = false;

	double scale = 100.0;

	private Dimension aread;

	double ratio = (1.0 / 4.0);
	
	private double[] moveCoefficients = {};

	/**
	 * Constructs a subdivision with (800, 400) screen size.
	 *
	 */
	public Subdivision() {
		aread = new Dimension(800, 400);
		listInitialize();
	}

	/**
	 * Construct a subdivision with the given screen size.
	 * 
	 * @param ww
	 * @param hh
	 */
	public Subdivision(int ww, int hh) {
		aread = new Dimension(ww, hh);
		listInitialize();

	}

	/**
	 * Perform initialization for both the initial and the current curves 
	 * of the subdivision.
	 */
	public void initPoly() {
		Knot current = IHead;
		if (IHead.next != ITail) {
			Head = new Knot();
			Tail = new Knot();
			init(Head, Tail);
			System.gc();
			while (current.next != ITail) {
				current = current.next;
				new Knot(current.x, current.y).append(Tail);
			}
			setNumberOfPoints(getNumberOfPointsBuffer());
			numberOfSubdivisions = 0;
			moveCoefficients = new double[0];
		}
	}

	/**
	 * Perform one step of Chaikin's algorithm on the current curve
	 * by subdividing the current curve and forming a new curve.
	 * 
	 * All segments of the current curve are divided by two points using ratio.
	 * 
	 * @param closed specify if the curve is closed
	 */
	public void apply(boolean closed) {
		if (getNumberOfPoints() >= 3) {
			Knot now = Head;

			if (closed) {
				createSubKnot(Tail.back, now.next).append(DTail);
				numberOfPoints++;
			}
			while (now.next.next != Tail) {
				now = now.next;
				createSubKnot(now.next, now).append(DTail);
				createSubKnot(now, now.next).append(DTail);
				numberOfPoints += 2;
			}
			now = now.next;
			if (closed) {
				createSubKnot(Head.next, now).append(DTail);
				numberOfPoints++;
			}

			Knot dummy;
			if (startboundary) {
				if (!closed) {
					dummy = DHead.next;
					dummy.back = DHead.next;
					Head.next.next = dummy;
					numberOfPoints++;
				} else {
					dummy = DHead.next;
					dummy.back = Head;
					Head.next = dummy;
				}
			} else {
				dummy = DHead.next;
				dummy.back = Head;
				Head.next = dummy;
			}
			if (endboundary) {
				if (!closed) {
					dummy = DTail.back;
					dummy.next = Tail.back;
					Tail.back.back = dummy;
					numberOfPoints++;
				} else {
					dummy = DTail.back;
					dummy.next = Tail;
					Tail.back = dummy;
				}
			} else {
				dummy = DTail.back;
				dummy.next = Tail;
				Tail.back = dummy;
			}
			DHead = new Knot();
			DTail = new Knot();
			init(DHead, DTail);
			System.gc();

		}
		numberOfSubdivisions++;
		if (moveCoefficients != null && moveCoefficients.length != numberOfSubdivisions) {
			moveCoefficients = calculateMoveCoeff();
		}
	}

	/**
	 * Creates the subknot intersecting the given knot segmet in ratio.
	 * Note that the left and the right knots should be consequent, i.e. 
	 * they should be the left and the right point of one segment of a curve.
	 * 
	 * @param left the left knot of the segmnt
	 * @param right the right knot of the segment
	 * @return a new knot on the given segment, which intersects the segment in 
	 * ratio : 1 - ratio.
	 */
	private Knot createSubKnot(Knot left, Knot right) {
		Knot subKnot = new Knot();
		updateSubKnot(subKnot, left, right);
		return subKnot;
	}
	
	/**
	 * Updates the given subknot coordinates, given the previouse knot segmet.
	 * Note that all tree knots lie on one line. This method should be invoked
	 * in case some of the left or right corners of the given segment changes 
	 * in order to update the coordinates of the subKnot. 
	 *  
	 * @param subKnot the subKnot to be updated
	 * @param left the left corner of the segment
	 * @param right the right corner of the segment
	 */
	private void updateSubKnot(Knot subKnot, Knot left, Knot right) {
		subKnot.x = ratio * left.x + (1 - ratio) * right.x;
		subKnot.y = ratio * left.y + (1 - ratio) * right.y;
	}
	
	/**
	 * Searches the initial curve of the subdivision for a Knot located 
	 * at the given coordinates. The match of both coordinates is identified
	 * with cerain precision of 0.1.
	 * 
	 * @param x
	 * @param y
	 * @return the knot located at the given coordinates
	 */
	public Knot findOriginalKnotAt(double x, double y) {
		double precision = 0.1;
		
		Knot now = IHead;
		while (now.next != ITail) {
			now = now.next;
			if (Math.abs(x - now.x) < precision && 
					Math.abs(y - now.y) < precision) {
				return now;
			}
		}
		return null;
	}
	
	/**
	 * Refreshes calculations for the part of the subdivided curve that 
	 * depends on initial Knot given. The knot parameter should be a knot 
	 * on the initial curve of the subvdivision.
	 * 
	 * @param k 
	 */
	@Deprecated
	public void Refresh(Knot k) {
		if (getNumberOfPoints() >= 3) {
			Knot subNow = findSubknot(k.back);
			if (subNow != null) {
				Subdivision corner = createCorner(k);
				
				corner.initPoly();
				for (int i = 0; i < numberOfSubdivisions; i++) {
					corner.apply(false);
				}
				
				Knot now = corner.Head;
				while (now.next != corner.Tail) {
					now = now.next;
					subNow.x = now.x;
					subNow.y = now.y;
					subNow = subNow.next;
				}
			}
		}
	}

	/**
	 * Creates a corner subdivision of tree knots by the corner, 
	 * whose angle is situated at knot k contained in a paerticular curve.
	 * 
	 * @param k should be an initial knot
	 * @return the new corner subdivision
	 */
	@Deprecated
	private Subdivision createCorner(Knot k) {
		Subdivision corner = new Subdivision();
		new Knot(k.back).append(corner.Tail);
		new Knot(k.back).append(corner.ITail);
		new Knot(k).append(corner.Tail);
		new Knot(k).append(corner.ITail);
		new Knot(k.next).append(corner.Tail);
		new Knot(k.next).append(corner.ITail);
		corner.setNumberOfPoints(corner.getNumberOfPoints() + 3);
		corner.setNumberOfPointsBuffer(corner.getNumberOfPointsBuffer() + 3);
		return corner;
	}

	/**
	 * Moves the given initial Knot with the given vector direction updating 
	 * the subdivided current curve of the algorithm. The operation is supported 
	 * only for closed curves. The operation is impemented for high performance 
	 * manipulation on the curve constructed.
	 * 
	 * @param iKnot the initial knot to be moved
	 * @param direction the direction and size of the translation
	 */
	public void move(Knot iKnot, DoubleVector direction) {
		if (iKnot == null) return;
		
		iKnot.move(direction);
		
		if (numberOfSubdivisions >= 1 && numberOfPoints >= 3) {
			// update the curve
			Knot iFirst = iback(iKnot);
			Knot current = findSubknot(iFirst);
			for (int index = 0; index < moveCoefficients.length; index++) {
				current = next(current);
				current.move(direction, moveCoefficients[index]);
			}
			current = next(current);
			current.move(direction, 1 - ratio);
			
			current = next(current);
			current.move(direction, 1 - ratio);
			for (int index = moveCoefficients.length - 1; index >= 0; index--) {
				current = next(current);
				current.move(direction, moveCoefficients[index]);
			}
		} else if (numberOfSubdivisions == 0) {
			int index = index(IHead, iKnot);
			Knot child = knotAt(Head, index);
			child.move(direction);
		}
	}
	
	/**
	 * Helper method that calculates the coefficients to multiply the initial 
	 * point's move vector with, for part of the subKnots of the current curve 
	 * (only those, which should be updated). Move coeficients should be recalculated 
	 * each time a step of the algorithm is performed (since the higher the step of 
	 * the algorithm, the more points of the subdivided curve should be updated). 
	 *  
	 * @return the move coeficients for the current algorithm step
	 */
	private double[] calculateMoveCoeff() {
		int length = 3*pow2(numberOfSubdivisions - 1) - 2;
		double[] coeffs = new double[length];
		
		if (moveCoefficients.length >= 1) {
			int count = 0;
			int index = 0;

			coeffs[count++] = ratio*moveCoefficients[index];
			coeffs[count++] = (1 - ratio)*moveCoefficients[index];
			
			for ( ; index + 1 < moveCoefficients.length; index++) {
				coeffs[count++] = calculateLeftCoeff(moveCoefficients[index], moveCoefficients[index + 1]);
				coeffs[count++] = calculateRightCoeff(moveCoefficients[index], moveCoefficients[index + 1]);
			}
			
			coeffs[count++] = calculateLeftCoeff(moveCoefficients[index], 1 - ratio);
			coeffs[count++] = calculateRightCoeff(moveCoefficients[index], 1 - ratio);
		} else {
			coeffs[0] = ratio;
		}
		return coeffs;
	}
	
	/**
	 * Helper method for calculation of a new left coefficient, out of two previouse coefficients.
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	private double calculateLeftCoeff(double left, double right) {
		return Math.min(left, right) + ratio * Math.abs(left - right);
	}
	
	/**
	 * Helper method for calculation of a new right coefficient, out of two previouse coefficients.
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	private double calculateRightCoeff(double left, double right) {
		return Math.min(left, right) + (1 - ratio) * Math.abs(left - right);
	}
	
	/**
	 * Achieves cyclic walk around a curve. When last knot (before Tail) is reached
	 * the first knot (after Head) is returned.
	 *  
	 * @param now the current knot
	 * @return the next knot
	 */
	private Knot next(Knot now) {
		boolean end = now == null || now.next == Tail;
		if (end) {
			return Head.next;
		}
		return now.next;
	}
	
	/**
	 * Achieves cyclic walk around a curve. When first knot (after Head) is reached
	 * the last knot (before Tail) is returned.
	 *  
	 * @param now the current knot
	 * @return the previouse knot
	 */
	private Knot back(Knot now) {
		boolean start = now == null || now.back == Head;
		if (start) {
			return Tail.back;
		}
		return now.back;
	}
	
	/**
	 * Achieves cyclic walk around a curve. When last knot (before ITail) is reached
	 * the first knot (after IHead) is returned.
	 *  
	 * @param now the current knot
	 * @return the next knot
	 */
	private Knot inext(Knot now) {
		boolean end = now == null || now.next == ITail;
		if (end) {
			return IHead.next;
		}
		return now.next;
	}
	
	/**
	 * Achieves cyclic walk around a curve. When first knot (after IHead) is reached
	 * the last knot (before ITail) is returned.
	 *  
	 * @param now the current knot
	 * @return the previouse knot
	 */
	private Knot iback(Knot now) {
		boolean start = now == null || now.back == IHead;
		if (start) {
			return ITail.back;
		}
		return now.back;
	}
	
	/**
	 * Finds the corresponding subKnot in the current subdivided curve 
	 * for the given initial knot.
	 * 
	 * @param iKnot initial knot
	 * @return iKnot's corresponding subKnot (on the current subdivided curve)
	 */
	private Knot findSubknot(Knot iKnot) {
		int index = index(IHead, iKnot);
		if (index != -1) {
			int coef = pow2(numberOfSubdivisions);
			return knotAt(Head, coef * index);
		} else {
			return null;
		}
	}

	/**
	 * Calculate the value for power of 2 (i.e. 2**pow).
	 * 
	 * @param pow the power to calculate
	 * @return the value for power of 2 (i.e. 2**pow)
	 */
	private int pow2(int pow) {
		int coef = 1;
		for (int i = 0; i < pow; i++) coef = coef*2;
		return coef;
	}
	
	/**
	 * Find the knot at the specified index starting from head.
	 * 
	 * @param head the start knot of the curve
	 * @param index the index of the searched knot
	 * @return the knot at the specified position
	 */
	private Knot knotAt(Knot head, int index) {
		int i = 0;
		Knot now = head;
		while (now.next != null) {
			now = now.next;
			if (i == index) break;
			i++;
		}
		if (now.next == null && i != index) {
			now = null;
		}
		return now;
	}
	
	/**
	 * Find the index of the specified knot, starting from the givven head.
	 * 
	 * @param head the start knot of the curve
	 * @param k the knot to find
	 * @return the index of the knot found
	 */
	private int index(Knot head, Knot k) {
		int index = 0;
		Knot now = head;
		while (now.next != null) {
			now = now.next;
			if (now == k) break;
			index++;
		}
		
		if (now.next == null && now != k) {
			index = -1;
		} 
		return index;
	}

	/**
	 * Perform initialization of the corners of all subdivision curves.
	 */
	private void listInitialize() {
		System.gc();
		numberOfPoints = 0;
		numberOfSubdivisions = 0;
		moveCoefficients = new double[0];
		numberOfPointsBuffer = 0;
		Head = new Knot();
		Tail = new Knot();
		init(Head, Tail);
		DHead = new Knot();
		DTail = new Knot();

		init(DHead, DTail);
		IHead = new Knot();
		ITail = new Knot();

		init(IHead, ITail);
	}

	/* private */

	/**
	 * Initialize start/end knots of a curve.
	 * 
	 * @param h
	 * @param t
	 */
	private void init(Knot h, Knot t) {
		h.next = new Knot();
		t.back = new Knot();
		h.next = t;
		t.back = h;
	}

	/**
	 * Delete the specified knot from it's contained curve.
	 *  
	 * @param now should not be equal to the head, nor to the tail of the curve
	 */
	public void deleteKnot(Knot now) {
		Knot dummy = now.next.next;
		dummy.back = now;
		now.next = dummy;
	}

	/**
	 * Insert the specified knot before the second knot in
	 * second knot's curve.
	 * 
	 * @param now the knot to be inserted.
	 * @param after
	 */
	public void insert(Knot now, Knot after) {
		Knot dummy = now.next;
		now.next = after;
		after.back = now;
		after.next = dummy;
		dummy.back = after;
	}

	void setNumberOfPoints(int numberOfPoints) {
		this.numberOfPoints = numberOfPoints;
	}

	public int getNumberOfPoints() {
		return numberOfPoints;
	}

	void setNumberOfPointsBuffer(int numberOfPointsBuffer) {
		this.numberOfPointsBuffer = numberOfPointsBuffer;
	}

	public int getNumberOfPointsBuffer() {
		return numberOfPointsBuffer;
	}
}
