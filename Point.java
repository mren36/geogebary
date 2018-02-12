import java.awt.*;
import javax.swing.*;
import java.math.*;

/**
 * Point: a class that represents a point with barycentric coordinates (x : y : z) for some HomogenousVector (x, y, z)
 */
public class Point
{
  private HomogenousVector coords; // stores the coordinates of the point

  /**
   * Initializes a point with the given coordinates.
   * @param coords HomogeneousVector containing coordinates
   */
  public Point(HomogenousVector coords)
  {
    if (coords.equalsZero())
      throw new IllegalArgumentException("coordinates cannot all be zero");
    HomogenousVector v = coords;
    v.reduce();
    this.coords = v;
  }

  /**
   * Initializes a point with the given x, y, and z polynomials.
   * @param x first polynomial
   * @param y second polynomial
   * @param z third polynomial
   */
  public Point(HomogenousPolynomial x, HomogenousPolynomial y, HomogenousPolynomial z) // sets the coordinates of the point
                                                                                       // by inputting polynomials
  {
    coords = new HomogenousVector(x, y, z);
    if (coords.equalsZero())
      throw new IllegalArgumentException("coordinates cannot all be zero");
    HomogenousVector v = coords;
    v.reduce();
    this.coords = v;
  }

  /**
   * Initializes a point with the given x, y, and z strings.
   * @param xString
   * @param yString
   * @param zString
   */
  public Point(String xString, String yString, String zString)
  {
    coords = new HomogenousVector(xString, yString, zString);
    if (coords.equalsZero())
      throw new IllegalArgumentException("coordinates cannot all be zero");
    HomogenousVector v = coords;
    v.reduce();
    this.coords = v;
  }

  /**
   * Initializes a point as the intersection of the given lines, determined by the cross product of their coefficients.
   * @param l1 line one
   * @param l2 line two
   */
  public Point(Line l1, Line l2)
  {
    if (l1.equals(l2))
      throw new IllegalArgumentException("lines cannot be the same");
    HomogenousVector v = l1.getCoeffs().cross(l2.getCoeffs());
    v.reduce();
    this.coords = v;
  }

  /**
   * Initializes a point with the given point.
   * @param other point
   */
  public Point(Point other)
  {
    coords = other.coords;
  }

  /**
   * Returns whether this point has the same coordinates as the given point.
   * @param other point
   * @return
   */
  public boolean equals(Point other)
  {
    return coords.equals(other.coords);
  }

  /**
   * Returns the coordinates of this point.
   * @return
   */
  public HomogenousVector getCoords()
  {
    return coords;
  }

  /**
   * Returns the coordinates of this point as a String.
   * @return
   */
  public String toString()
  {
    return "( " + coords.getX() + " : " + coords.getY() + " : " + coords.getZ() + " )";
  }

  /**
   * Returns the line that passes through this point and the given point.
   * @param other point
   * @return
   */
  public Line interpolate(Point other)
  {
    return new Line(this, other);
  }

  /**
   * Returns whether this point lies on the given line, checking by dot product of coordinates and coefficients.
   * @param other line
   * @return
   */
  public boolean on(Line other)
  {
    return coords.perp(other.getCoeffs());
  }

  /**
   * Returns a simplified reflection of this point over the given point.
   * @param other point
   * @return
   */
  public Point reflectOver(Point other)
  {
    return Geometry.reflect(this, other);
  }

  /**
   * Returns this point reflected over the given line.
   * @param other line
   * @return
   */
  public Point reflectOver(Line other)
  {
    return Geometry.reflect(this, other);
  }

  /**
   * Returns the weight of the point.
   * @return sum of coordinates.
   */
  public HomogenousPolynomial weight()
  {
    return coords.weight();
  }

  /**
   * Returns x coordinate.
   * @return
   */
  public HomogenousPolynomial getX()
  {
    return coords.getX();
  }

  /**
   * Returns y coordinate.
   * @return
   */
  public HomogenousPolynomial getY()
  {
    return coords.getY();
  }

  /**
   * Returns z coordinate.
   * @return
   */
  public HomogenousPolynomial getZ()
  {
    return coords.getZ();
  }

  /**
   * Returns whether this point is on the given circle.
   * @param c circle
   * @return
   */
  public boolean on(Circle c)
  {
    HomogenousPolynomial pow = c.getCoeff().times(
            new HomogenousPolynomial("a^2").times(coords.getY().times(coords.getZ()))
            .plus(new HomogenousPolynomial("b^2").times(coords.getZ().times(coords.getX())))
            .plus(new HomogenousPolynomial("c^2").times(coords.getX().times(coords.getY()))));
    HomogenousPolynomial rad = weight().times(coords.dot(c.getRadCoeffs()));
    return pow.equals(rad);
  }

  /**
   * Draws the point in g given coordinates of triangle.
   * @param ax
   * @param ay
   * @param bx
   * @param by
   * @param cx
   * @param cy
   * @return
   */
  public int[] screenCoords(int ax, int ay, int bx, int by, int cx, int cy)
  {
    int a = (int) Math.round(Math.sqrt((bx - cx) * (bx - cx) + (by - cy) * (by - cy)));
    int b = (int) Math.round(Math.sqrt((cx - ax) * (cx - ax) + (cy - ay) * (cy - ay)));
    int c = (int) Math.round(Math.sqrt((ax - bx) * (ax - bx) + (ay - by) * (ay - by)));
    BigDecimal[] u = coords.eval(new BigInteger(a + ""), new BigInteger(b + ""), new BigInteger(c + ""));
    double[] v = new double[3];
    v[0] = u[0].doubleValue();
    v[1] = u[1].doubleValue();
    v[2] = u[2].doubleValue();
    int x = (int) Math.round(v[0] * ax + v[1] * bx + v[2] * cx);
    int y = (int) Math.round(v[0] * ay + v[1] * by + v[2] * cy);
    int[] sC = new int[2];
    sC[0] = x;
    sC[1] = y;
    return sC;
  }

  /**
   * Returns the
   * @param c
   * @return
   */
  public Line polar(Circle c)
  {
    return c.polar(this);
  }

  /**
   * Returns the simplified average of this point and the given point.
   * @param P2 other point
   * @param w1 weight one
   * @param w2 weight two
   * @return
   */
  public Point average(Point P2, int w1, int w2)
  {
    return Geometry.average(this, P2, w1, w2);
  }

  /**
   * Returns the simplified midpoint of this point and the given point.
   * @param P2
   * @return
   */
  public Point midpoint(Point P2)
  {
    return Geometry.midpoint(this, P2);
  }

  /**
   * Returns the polynomial-averaged point of this point and the given point.
   * @param P2
   * @param w1
   * @param w2
   * @return
   */
  public Point polyAverage(Point P2, HomogenousPolynomial w1, HomogenousPolynomial w2)
  {
    return Geometry.polyAverage(this, P2, w1, w2);
  }

  /**
   * Returns the foot from this point to the line through the given points.
   * @param P1 first point
   * @param P2 second point
   * @return
   */
  public Point foot(Point P1, Point P2)
  {
    return Geometry.foot(this, P1, P2);
  }

  /**
   * Returns the centroid of the triangle defined by this point and the given points.
   * @param P2 second point
   * @param P3 third point
   * @return
   */
  public Point centroid(Point P2, Point P3)
  {
    return Geometry.centroid(this, P2, P3);
  }

  /**
   * Returns the circumenter of the triangle defined by this point and the given points.
   * @param P2 second point
   * @param P3 third point
   * @return
   */
  public Point circumcenter(Point P2, Point P3)
  {
    return Geometry.circumcenter(this, P2, P3);
  }

  /**
   * Returns the hashcode of this point.
   * @return
   */
  public int hashCode()
  {
    return coords.hashCode();
  }
}
