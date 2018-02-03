// a class that represents a line with barycentric equation ux+vy+wz=0 for some HomogenousVector (u, v, w)

import java.awt.*;
import javax.swing.*;
import java.math.*;

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

  public Line(HomogenousPolynomial x, HomogenousPolynomial y, HomogenousPolynomial z) // sets the coefficients of the line equation
                                                                                      // by their homogenous polynomials
  {
    coeffs = new HomogenousVector(x, y, z);
    if (coeffs.equalsZero())
      throw new IllegalArgumentException("line equation coordinates cannot all be zero");
    HomogenousVector v = coeffs;
    v.reduce();
    this.coeffs = v;
  }

  public Line(String xString, String yString, String zString) // sets the coefficients of the line equation by polynomial Strings
  {
    coeffs = new HomogenousVector(xString, yString, zString);
    if (coeffs.equalsZero())
      throw new IllegalArgumentException("line equation coordinates cannot all be zero");
    HomogenousVector v = coeffs;
    v.reduce();
    this.coeffs = v;
  }

  public Line(Point P1, Point P2) // returns the line through two points, computes via the cross product of their coordinates
  {
    if (P1.equals(P2))
      throw new IllegalArgumentException("points cannot be the same");
    HomogenousVector v = P1.getCoords().cross(P2.getCoords());
    v.reduce();
    this.coeffs = v;
  }

  public Line(Line other) // copy constructor
  {
    coeffs = other.coeffs;
  }

  public boolean equals(Line other) // returns whether the line equations are the same
  {
    return coeffs.equals(other.coeffs);
  }

  public HomogenousVector getCoeffs() // gets coefficients of the line equation
  {
    return coeffs;
  }

  public String toString() // returns line equation in String form
  {
    return "( " + coeffs.getX() + " ) x + ( " + coeffs.getY() + " ) y + ( " + coeffs.getZ() + " ) z = 0";
  }

  public Point intersect(Line other) // returhs the intersection of this and other
  {
    return new Point(this, other);
  }

  public boolean contains(Point P) // returns whether the line passes through a point P
  {
    return coeffs.perp(P.getCoords());
  }

  public boolean parallel(Line other) // returns whether the line is parallel to other
  {
    return Geometry.parallel(this, other);
  }

  public boolean perp(Line other) // returns whether the line is perpendicular to other
  {
    return Geometry.perp(this, other);
  }

  public HomogenousPolynomial getX() // getter for x coefficent
  {
    return coeffs.getX();
  }

  public HomogenousPolynomial getY() // getter for y coefficient
  {
    return coeffs.getY();
  }

  public HomogenousPolynomial getZ() // getter for z coefficient
  {
    return coeffs.getZ();
  }

  public boolean isTangent(Circle c) // returns whether the line is tangent to c
  {
    return Geometry.isTangent(this, c);
  }

  public int[] screenCoords(int ax, int ay, int bx, int by, int cx, int cy, int width, int height) // draws the line in g given coefficients
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

  public Point pole(Circle c)
  {
    return c.pole(this);
  }

  public int hashCode()
  {
    return toString().hashCode();
  }
}
