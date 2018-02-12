import java.awt.*;
import javax.swing.*;
import java.math.*;

/**
 * A class that represents a line with barycentric equation ux+vy+wz=0 for some HomogenousVector (u, v, w)
 */
public class Line
{
  private HomogenousVector coeffs; // stores the coefficients of the line equation

  public Line(HomogenousVector coeffs) // sets the coefficients of the line equation
  {
    if (coeffs.equalsZero())
      throw new IllegalArgumentException("line equation coordinates cannot all be zero");
    HomogenousVector v = coeffs;
    v.reduce();
    this.coeffs = v;
  }

  /**
   * Initializes a line with the coefficients of the line equation given by their homogeneous polynomials.
   * @param x first polynomial
   * @param y second polynomial
   * @param z third polynomial
   */
  public Line(HomogenousPolynomial x, HomogenousPolynomial y, HomogenousPolynomial z)

  {
    coeffs = new HomogenousVector(x, y, z);
    if (coeffs.equalsZero())
      throw new IllegalArgumentException("line equation coordinates cannot all be zero");
    HomogenousVector v = coeffs;
    v.reduce();
    this.coeffs = v;
  }

  /**
   * Initializes a line with the coefficients of the line equation given by polynomial strings.
   * @param xString first polynomial
   * @param yString second polynomial
   * @param zString third polynomial
   */
  public Line(String xString, String yString, String zString) //
  {
    coeffs = new HomogenousVector(xString, yString, zString);
    if (coeffs.equalsZero())
      throw new IllegalArgumentException("line equation coordinates cannot all be zero");
    HomogenousVector v = coeffs;
    v.reduce();
    this.coeffs = v;
  }

  /**
   * Initializes a line with the coefficients of the line equation given by the cross product of the coordinates of the
   * two points.
   * @param P1 first point
   * @param P2 second point
   */
  public Line(Point P1, Point P2)
  {
    if (P1.equals(P2))
      throw new IllegalArgumentException("points cannot be the same");
    HomogenousVector v = P1.getCoords().cross(P2.getCoords());
    v.reduce();
    this.coeffs = v;
  }

  /**
   * Initializes a line with the coefficients of the line equation given by another line.
   * @param other line
   */
  public Line(Line other)
  {
    coeffs = other.coeffs;
  }

  /**
   * Returns whether the line equations are the same.
   * @param other line
   * @return True if coefficients are equal. False otherwise.
   */
  public boolean equals(Line other)
  {
    return coeffs.equals(other.coeffs);
  }

  /**
   * Returns a homogeneous vector containing the coefficients of the line equation.
   * @return coefficients
   */
  public HomogenousVector getCoeffs()
  {
    return coeffs;
  }

  /**
   * Returns a String of the line equation.
   * @return line equation
   */
  public String toString()
  {
    return "( " + coeffs.getX() + " ) x + ( " + coeffs.getY() + " ) y + ( " + coeffs.getZ() + " ) z = 0";
  }

  /**
   * Returns the intersection of this line with the given live.
   * @param other line
   * @return point of intersection
   */
  public Point intersect(Line other)
  {
    return new Point(this, other);
  }

  /**
   * Returns whether the line passes through a given point P.
   * @param P point
   * @return True if intersects. False otherwise.
   */
  public boolean contains(Point P)
  {
    return coeffs.perp(P.getCoords());
  }

  /**
   * Returns whether this line is parallel to the given line.
   * @param other
   * @return True if parallel. False otherwise.
   */
  public boolean parallel(Line other)
  {
    return Geometry.parallel(this, other);
  }

  /**
   * Returns whether this line is perpendicular to the given line.
   * @param other
   * @return True if parallel. False otherwise.
   */
  public boolean perp(Line other)
  {
    return Geometry.perp(this, other);
  }

  /**
   * Returns the x coefficient.
   * @return x coefficient
   */
  public HomogenousPolynomial getX()
  {
    return coeffs.getX();
  }

  /**
   * Returns y coefficient.
   * @return y coefficient
   */
  public HomogenousPolynomial getY()
  {
    return coeffs.getY();
  }

  /**
   * Returns z coefficient.
   * @return z coefficient
   */
  public HomogenousPolynomial getZ()
  {
    return coeffs.getZ();
  }

  /**
   * Returns whether this line is tangent to the given circle.
   * @param circle
   * @return True if tangent. False otherwise.
   */
  public boolean isTangent(Circle c)
  {
    return Geometry.isTangent(this, c);
  }

  /**
   * Draws a line given coefficients.
   * @param ax
   * @param ay
   * @param bx
   * @param by
   * @param cx
   * @param cy
   * @param width
   * @param height
   * @return
   */
  public int[] screenCoords(int ax, int ay, int bx, int by, int cx, int cy, int width, int height)
  {
    Point P1, P2;
    if (equals(new Line("0", "0", "1")))
    {
      P1 = new Point("1", "0", "0");
      P2 = new Point("0", "1", "0");
    }
    else if (equals(new Line("0", "1", "0")))
    {
      P1 = new Point("1", "0", "0");
      P2 = new Point("0", "0", "1");
    }
    else if (new Point("1", "0", "0").on(this))
    {
      P1 = new Point("1", "0", "0");
      P2 = new Point(this, new Line("1", "0", "0"));
    }
    else
    {
      P1 = new Point(this, new Line("0", "1", "0"));
      P2 = new Point(this, new Line("0", "0", "1"));
    }
    int a = (int) Math.round(Math.sqrt((bx - cx) * (bx - cx) + (by - cy) * (by - cy)));
    int b = (int) Math.round(Math.sqrt((cx - ax) * (cx - ax) + (cy - ay) * (cy - ay)));
    int c = (int) Math.round(Math.sqrt((ax - bx) * (ax - bx) + (ay - by) * (ay - by)));
    BigDecimal[] u1 = P1.getCoords().eval(new BigInteger(a + ""), new BigInteger(b + ""), new BigInteger(c + ""));
    BigDecimal[] u2 = P2.getCoords().eval(new BigInteger(a + ""), new BigInteger(b + ""), new BigInteger(c + ""));
    double[] v1 = new double[3];
    double[] v2 = new double[3];
    v1[0] = u1[0].doubleValue();
    v1[1] = u1[1].doubleValue();
    v1[2] = u1[2].doubleValue();
    v2[0] = u2[0].doubleValue();
    v2[1] = u2[1].doubleValue();
    v2[2] = u2[2].doubleValue();
    double x1 = v1[0] * ax + v1[1] * bx + v1[2] * cx;
    double y1 = v1[0] * ay + v1[1] * by + v1[2] * cy;
    double x2 = v2[0] * ax + v2[1] * bx + v2[2] * cx;
    double y2 = v2[0] * ay + v2[1] * by + v2[2] * cy;
    int[] sC = new int[4];
    if (x1 == x2)
    {
      sC[0] = (int) Math.round(x1);
      sC[1] = 0;
      sC[2] = (int) Math.round(x1);
      sC[3] = height;
      return sC;
    }
    if (y1 == y2)
    {
      sC[0] = 0;
      sC[1] = (int) Math.round(y1);
      sC[2] = width;
      sC[3] = (int) Math.round(y2);
      return sC;
    }
    int topCoord = (int) (Math.round((x1 * y2 - x2 * y1) / (y2 - y1)));
    int bottomCoord = (int) (Math.round((x1 * (height - y2) - x2 * (height - y1)) / (y1 - y2)));
    int leftCoord = (int) (Math.round((y1 * x2 - y2 * x1) / (x2 - x1)));
    int rightCoord = (int) (Math.round((y1 * (width - x2) - y2 * (width - x1)) / (x1 - x2)));
    boolean top = topCoord >= 0 && topCoord <= width;
    boolean bottom = bottomCoord >= 0 && bottomCoord <= width;
    boolean left = leftCoord >= 0 && leftCoord <= height;
    boolean right = rightCoord >= 0 && rightCoord <= height;
    if (left && right)
    {
      sC[0] = 0;
      sC[1] = leftCoord;
      sC[2] = width;
      sC[3] = rightCoord;
    }
    else if (bottom && right)
    {
      sC[0] = bottomCoord;
      sC[1] = height;
      sC[2] = width;
      sC[3] = rightCoord;
    }
    else if (bottom && left)
    {
      sC[0] = bottomCoord;
      sC[1] = height;
      sC[2] = 0;
      sC[3] = leftCoord;
    }
    else if (top && right)
    {
      sC[0] = topCoord;
      sC[1] = 0;
      sC[2] = width;
      sC[3] = rightCoord;
    }
    else if (top & left)
    {
      sC[0] = topCoord;
      sC[1] = 0;
      sC[2] = 0;
      sC[3] = leftCoord;
    }
    else if (top && bottom)
    {
      sC[0] = topCoord;
      sC[1] = 0;
      sC[2] = bottomCoord;
      sC[3] = height;
    }
    return sC;
  }

  /**
   * Returns the pole of this line given a circle.
   * @param c
   * @return Pole point.
   */
  public Point pole(Circle c)
  {
    return c.pole(this);
  }

  /**
   * Returns the hash code.
   * @return hash
   */
  public int hashCode()
  {
    return coeffs.hashCode();
  }
}
