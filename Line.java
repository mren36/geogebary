// a class that represents a line with barycentric equation ux+vy+wz=0 for some HomogenousVector (u, v, w)

import java.awt.*;
import javax.swing.*;

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

  public void draw(Graphics g, int ax, int ay, int bx, int by, int cx, int cy, int width, int height) // draws the line in g given coefficients
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
    double a = Math.sqrt((bx - cx) * (bx - cx) + (by - cy) * (by - cy));
    double b = Math.sqrt((cx - ax) * (cx - ax) + (cy - ay) * (cy - ay));
    double c = Math.sqrt((ax - bx) * (ax - bx) + (ay - by) * (ay - by));
    double[] v1 = P1.getCoords().eval(a, b, c);
    double[] v2 = P2.getCoords().eval(a, b, c);
    double x1 = v1[0] * ax + v1[1] * bx + v1[2] * cx;
    double y1 = v1[0] * ay + v1[1] * by + v1[2] * cy;
    double x2 = v2[0] * ax + v2[1] * bx + v2[2] * cx;
    double y2 = v2[0] * ay + v2[1] * by + v2[2] * cy;
    if (x1 == x2)
    {
      if (x1 < 0 || x1 > width)
        return;
      g.drawLine((int) Math.round(x1), 0, (int) Math.round(x1), height);
      return;
    }
    if (y1 == y2)
    {
      if (y1 < 0 || y1 > height)
        return;
      g.drawLine(0, (int) Math.round(y1), width, (int) Math.round(y1));
      return;
    }
    int topCoord = (int) (Math.round((x1 * y2 - x2 * y1) / (y2 - y1)));
    int bottomCoord = (int) (Math.round((x1 * (height - y2) - x2 * (height - y1)) / (y1 - y2)));
    int leftCoord = (int) (Math.round((y1 * x2 - y2 * x1) / (x2 - x1)));
    int rightCoord = (int) (Math.round((y1 * (width - x2) - y2 * (width - x1)) / (x1 - x2)));
    boolean top = topCoord >= 0 && topCoord <= width;
    boolean bottom = bottomCoord >= 0 && bottomCoord <= width;
    boolean left = leftCoord >= 0 && leftCoord <= height;
    boolean right = rightCoord >= 0 && rightCoord <= height;
    if (top && bottom)
      g.drawLine(topCoord, 0, bottomCoord, height);
    else if (top && left)
      g.drawLine(topCoord, 0, 0, leftCoord);
    else if (top && right)
      g.drawLine(topCoord, 0, width, rightCoord);
    else if (bottom && left)
      g.drawLine(bottomCoord, height, 0, leftCoord);
    else if (bottom & right)
      g.drawLine(bottomCoord, height, width, rightCoord);
    else if (left && right)
        g.drawLine(0, leftCoord, width, rightCoord);
  }
}
