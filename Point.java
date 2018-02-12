import java.awt.*;
import javax.swing.*;
import java.math.*;

/**
 * a class that represents a point with barycentric coordinates (x : y : z) for some HomogenousVector (x, y, z)
 */
public class Point
{
  private HomogenousVector coords; // stores the coordinates of the point

  /**
   *
   * @param coords
   */
  public Point(HomogenousVector coords) // sets the coordinates of the point
  {
    if (coords.equalsZero())
      throw new IllegalArgumentException("coordinates cannot all be zero");
    HomogenousVector v = coords;
    v.reduce();
    this.coords = v;
  }

  /**
   *
   * @param x
   * @param y
   * @param z
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
   *
   * @param xString
   * @param yString
   * @param zString
   */
  public Point(String xString, String yString, String zString) // sets the coordinates of the opint by inputting Strings
  {
    coords = new HomogenousVector(xString, yString, zString);
    if (coords.equalsZero())
      throw new IllegalArgumentException("coordinates cannot all be zero");
    HomogenousVector v = coords;
    v.reduce();
    this.coords = v;
  }

  /**
   *
   * @param l1
   * @param l2
   */
  public Point(Line l1, Line l2) // returns the interesection of two lines, determined by the cross product of their coefficients
  {
    if (l1.equals(l2))
      throw new IllegalArgumentException("lines cannot be the same");
    HomogenousVector v = l1.getCoeffs().cross(l2.getCoeffs());
    v.reduce();
    this.coords = v;
  }

  /**
   *
   * @param other
   */
  public Point(Point other) // copy constructor
  {
    coords = other.coords;
  }

  /**
   *
   * @param other
   * @return
   */
  public boolean equals(Point other) // returns whether other has the same coordinates
  {
    return coords.equals(other.coords);
  }

  /**
   *
   * @return
   */
  public HomogenousVector getCoords() // getter for coords
  {
    return coords;
  }

  /**
   *
   * @return
   */
  public String toString() // returns String format of the point coordinates
  {
    return "( " + coords.getX() + " : " + coords.getY() + " : " + coords.getZ() + " )";
  }

  /**
   *
   * @param other
   * @return
   */
  public Line interpolate(Point other) // returns line through this and other
  {
    return new Line(this, other);
  }

  /**
   *
   * @param l
   * @return
   */
  public boolean on(Line l) // returns whether this is on line l, checks by dot product of coordinates and coefficients
  {
    return coords.perp(l.getCoeffs());
  }

  /**
   *
   * @param other
   * @return
   */
  public Point reflectOver(Point other) // returns the reflection of this over other
  {
    return Geometry.reflect(this, other);
  }

  /**
   *
   * @param l
   * @return
   */
  public Point reflectOver(Line l) // returns the reflection of the point over l
  {
    return Geometry.reflect(this, l);
  }

  /**
   *
   * @return
   */
  public HomogenousPolynomial weight() // returns the weight of the point, i.e. the sum of its coordinates
  {
    return coords.weight();
  }

  /**
   *
   * @return
   */
  public HomogenousPolynomial getX() // getter for x coordinate
  {
    return coords.getX();
  }

  /**
   *
   * @return
   */
  public HomogenousPolynomial getY() // getter for y coordinate
  {
    return coords.getY();
  }

  /**
   *
   * @return
   */
  public HomogenousPolynomial getZ() // getter for z coordinate
  {
    return coords.getZ();
  }

  /**
   *
   * @param c
   * @return
   */
  public boolean on(Circle c) // returns whether this is on c, checks by plugging coordinates into circle equation
  {
    HomogenousPolynomial pow = c.getCoeff().times(new HomogenousPolynomial("a^2").times(coords.getY().times(coords.getZ())).plus(new HomogenousPolynomial("b^2").times(coords.getZ().times(coords.getX()))).plus(new HomogenousPolynomial("c^2").times(coords.getX().times(coords.getY()))));
    HomogenousPolynomial rad = weight().times(coords.dot(c.getRadCoeffs()));
    return pow.equals(rad);
  }

  /**
   *
   * @param ax
   * @param ay
   * @param bx
   * @param by
   * @param cx
   * @param cy
   * @return
   */
  public int[] screenCoords(int ax, int ay, int bx, int by, int cx, int cy) // draws the point in g given coordinates of triangle
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
   *
   * @param c
   * @return
   */
  public Line polar(Circle c)
  {
    return c.polar(this);
  }

  // Simplified average Method

  /**
   *
   * @param P2
   * @param w1
   * @param w2
   * @return
   */
  public Point average(Point P2, int w1, int w2)
  {
    return Geometry.average(this, P2, w1, w2);
  }

  // Simplified midpoint Method

  /**
   *
   * @param P2
   * @return
   */
  public Point midpoint(Point P2) // returns the midpoint of P1 and P2
  {
    return Geometry.midpoint(this, P2);
  }

  // Simplified reflect Method

  /**
   *
   * @param P2
   * @return
   */
  public Point reflect(Point P2) // returns the reflection of P1 over P2
  {
    return Geometry.reflect(this, P2);
  }

  /**
   *
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
   *
   * @param P1
   * @param P2
   * @return
   */
  public Point foot(Point P1, Point P2) // returns the foot from P to the line through P1 and P2
  {
    return Geometry.foot(this, P1, P2);
  }

  /**
   *
   * @param P2
   * @param P3
   * @return
   */
  public Point centroid(Point P2, Point P3) // returns the centroid of triangle P1P2P3
  {
    return Geometry.centroid(this, P2, P3);
  }

  /**
   *
   * @param P1
   * @param P2
   * @param P3
   * @return
   */
  public Point circumcenter(Point P1, Point P2, Point P3)
  {
    return Geometry.circumcenter(this, P2, P3);
  }

  /**
   *
   * @return
   */
  public int hashCode()
  {
    return coords.hashCode();
  }
}
