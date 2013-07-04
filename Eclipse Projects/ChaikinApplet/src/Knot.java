/* This source has been formatted by an unregistered SourceFormatX */
/* If you want to remove this info, please register this shareware */
/* Please visit http://www.textrush.com to get more information    */



/**
 * Represents a point of a curve that keeps track of both the previouse and the next 
 * point in the curve. Stores it's current coordinates in the visual area.
 * 
 * @author plamen
 *
 */
public class Knot implements java.io.Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 5523834361895560547L;

    double x;

    double y;

    int error;

    Knot back;

    Knot next;


    public Knot(){

    }

    /**
     * Construcs a new knot from the coordinates of a previose one.
     * i.e. copies the knot.
     * 
     * @param knot the knot to be duplicated
     */
    public Knot(Knot knot)
    {
        x = knot.x;
        y = knot.y;
        error = 0;
    }

    /**
     * Construct a knot from its coordinates in the visual area.
     * 
     * @param dx
     * @param dy
     */
    public Knot(double dx, double dy)
    {
        x = dx;
        y = dy;
        error = 0;
    }

    /**
     * Calculate the distence between the given knots.
     * 
     * @param k1
     * @param k2
     * 
     * @return the distence between the knots
     */
    public static double distance(Knot k1, Knot k2)
    {
        DoubleVector v1 = new DoubleVector(k1.x, k1.y);
        DoubleVector v2 = new DoubleVector(k2.x, k2.y);
        return DoubleVector.distance(v1, v2);
    }

    /**
     * Insert the given knot, so that it will be positioned as 
     * the next knot of the current one in the curve. 
     * 
     * @param other
     */
    public void append(Knot other)
    {
        Knot dummy = other.back;
        this.next = other;
        other.back = this;
        this.back = dummy;
        dummy.next = this;
    }

    /**
     * Moves the current knot coordinates with the given direction vector and 
     * the coefficient to multiply the direction vector with.
     * 
     * @param direction
     * @param coeff
     */
    public void move(DoubleVector direction, double coeff)
    {
        x = x + coeff * direction.x;
        y = y + coeff * direction.y;
    }

    /**
     * Moves the current knot coordinates with the given direction vector.
     * 
     * @param direction
     */
    public void move(DoubleVector direction)
    {
        x = x + direction.x;
        y = y + direction.y;
    }

    @Override public String toString()
    {
        return "(" + x + ", " + y + ")";
    }

}
