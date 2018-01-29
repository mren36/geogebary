// represents a circle with barycentric equation k(a^2yz+b^2zx+c^2xy)=(x+y+z)(ux+vy+wz)
// k, u, v, w are integer coefficient homogenous polynomials with deg(k)+2=deg(u)=deg(v)=deg(w)

import java.awt.*;
import javax.swing.*;

public class Circle
{
  private HomogenousPolynomial coeff; // represents the k coefficient in front of the left hand side in the equation above
  private HomogenousVector radCoeffs; // represents the coefficients u,v,w of the radical axis of the circle with the circumcircle
                                      // of the reference triangle in the right hand side of the equation above

  public Circle() // returns the circumcircle of the reference triangle
  {
    coeff = new HomogenousPolynomial("1");
    radCoeffs = new HomogenousVector(2);
  }

  public Circle(HomogenousPolynomial coeff, HomogenousVector radCoeffs) // sets coeff and radCoeff
  {
    if (coeff.equalsZero())
      throw new IllegalArgumentException("coefficient must be nonzero");
    if (coeff.degree() + 2 != radCoeffs.degree())
      throw new IllegalArgumentException("degree of radical coefficients must be two more than degree of coefficient");
    this.coeff = coeff;
    this.radCoeffs = radCoeffs;
  }

  public Circle(HomogenousPolynomial coeff, HomogenousPolynomial radX, HomogenousPolynomial radY, HomogenousPolynomial radZ) // sets k, u, v, w
  {
    radCoeffs = new HomogenousVector(radX, radY, radZ);
    if (coeff.equalsZero())
      throw new IllegalArgumentException("coefficient must be nonzero");
    if (coeff.degree() + 2 != radCoeffs.degree())
      throw new IllegalArgumentException("degree of radical coefficients must be two more than degree of coefficient");
    this.coeff = coeff;
    this.radCoeffs = radCoeffs;
  }

  public Circle(String coeffString, String radXString, String radYString, String radZString) // sets k, u, v, w by String
  {
    HomogenousPolynomial coeff = new HomogenousPolynomial(coeffString);
    HomogenousVector radCoeffs = new HomogenousVector(radXString, radYString, radZString);
    if (coeff.equalsZero())
      throw new IllegalArgumentException("coefficient must be nonzero");
    if (coeff.degree() + 2 != radCoeffs.degree())
      throw new IllegalArgumentException("degree of radical coefficients must be two more than degree of coefficient");
    this.coeff = coeff;
    this.radCoeffs = radCoeffs;
  }

  public Circle(Circle other) // copy constructor
  {
    coeff = other.coeff;
    radCoeffs = other.radCoeffs;
  }

  public Circle(Point P1, Point P2, Point P3) // returns the circle through P1, P2, and P3
  {
    Circle circle = Geometry.circumcircle(P1, P2, P3);
    coeff = circle.coeff;
    radCoeffs = circle.radCoeffs;
  }

  public Circle(Point P1, Point P2) // returns the circle with diamter P1P2
  {
    Circle circle = Geometry.diameter(P1, P2);
    coeff = circle.coeff;
    radCoeffs = circle.radCoeffs;
  }

  public boolean equals(Circle other) // returns whether this and other have the same equation up to scaling
  {
    return radCoeffs.times(other.coeff).equals(other.radCoeffs.times(coeff));
  }

  public Point center() // returns the cetner of the circle, given by a formula
  {
    HomogenousPolynomial xCoord = new HomogenousPolynomial("a^2").times(radCoeffs.dot(new HomogenousVector("-2", "1", "1"))).plus(new HomogenousPolynomial("b^2-c^2").times(radCoeffs.dot(new HomogenousVector("0", "1", "-1"))).plus(coeff.times(new HomogenousPolynomial("-a^4+a^2b^2+a^2c^2"))));
    HomogenousPolynomial yCoord = new HomogenousPolynomial("b^2").times(radCoeffs.dot(new HomogenousVector("1", "-2", "1"))).plus(new HomogenousPolynomial("c^2-a^2").times(radCoeffs.dot(new HomogenousVector("-1", "0", "1"))).plus(coeff.times(new HomogenousPolynomial("a^2b^2-b^4+b^2c^2"))));
    HomogenousPolynomial zCoord = new HomogenousPolynomial("c^2").times(radCoeffs.dot(new HomogenousVector("1", "1", "-2"))).plus(new HomogenousPolynomial("a^2-b^2").times(radCoeffs.dot(new HomogenousVector("1", "-1", "0"))).plus(coeff.times(new HomogenousPolynomial("a^2c^2+b^2c^2-c^4"))));
    return new Point(xCoord, yCoord, zCoord);
  }

  public String toString() // returns a String for the equation of the circle
  {
    return "( " + coeff + " ) * ( a^2yz+b^2zx+c^2xy ) = ( x+y+z ) * ( ( " + radCoeffs.getX() + " )x+( " + radCoeffs.getY() + " )y+( " + radCoeffs.getZ() + " )z )";
  }

  public HomogenousPolynomial getCoeff() // getter for coeff
  {
    return coeff;
  }

  public HomogenousVector getRadCoeffs() // getter for radCoeff
  {
    return radCoeffs;
  }

  public boolean contains(Point P) // return whether P is on the circle
  {
    return P.on(this);
  }

  public Line radAxis(Circle other) // returns the radical axis of this and other
  {
    return Geometry.radAxis(this, other);
  }

  public boolean isTangent(Line l) // returns whether l is tangent to the circle
  {
    return Geometry.isTangent(l, this);
  }

  public boolean isTangent(Circle other) // returns whether this and other are tangent
  {
    return Geometry.isTangent(this, other);
  }

  public HomogenousPolynomial[] radSqu()
  {
    HomogenousPolynomial[] frac = new HomogenousPolynomial[2];
    HomogenousPolynomial area16 = new HomogenousPolynomial("-a^4-b^4-c^4+2a^2b^2+2b^2c^2+2a^2c^2");
    HomogenousPolynomial squSum = new HomogenousPolynomial("a^2+b^2+c^2");
    HomogenousPolynomial squA = new HomogenousPolynomial("-a^2+b^2+c^2");
    HomogenousPolynomial squB = new HomogenousPolynomial("a^2-b^2+c^2");
    HomogenousPolynomial squC = new HomogenousPolynomial("a^2+b^2-c^2");
    HomogenousPolynomial squAB = squA.times(squB);
    HomogenousPolynomial squBC = squB.times(squC);
    HomogenousPolynomial squCA = squC.times(squA);
    HomogenousPolynomial squABC = squA.times(squBC);
    HomogenousPolynomial u = radCoeffs.getX();
    HomogenousPolynomial v = radCoeffs.getY();
    HomogenousPolynomial w = radCoeffs.getZ();
    HomogenousPolynomial f = coeff;
    HomogenousPolynomial f2 = coeff.pow(2);
    HomogenousPolynomial uv2 = u.minus(v).pow(2);
    HomogenousPolynomial vw2 = v.minus(w).pow(2);
    HomogenousPolynomial wu2 = w.minus(u).pow(2);
    frac[0] = (squSum.times(area16).times(f2)).minus(squABC.times(f2)).plus(squA.times(vw2).times(4)).plus(squB.times(wu2).times(4)).plus(squC.times(uv2).times(4)).minus(radCoeffs.weight().times(area16).times(4).times(f)).plus(squBC.times(u).times(f).times(4)).plus(squCA.times(v).times(f).times(4)).plus(squAB.times(w).times(f).times(4));
    frac[1] = area16.times(8).times(f2);
    System.out.println();
    return frac;
  }

  public Line polar(Point P)
  {
    Point O = center();
    if (P.equals(O))
      return Geometry.INF_LINE;
    return Geometry.radAxis(this, new Circle(O, P));
  }

  public Point inverse(Point P)
  {
    Point O = center();
    if (P.equals(O))
      throw new IllegalArgumentException("cannot invert center");
    if (P.on(Geometry.INF_LINE))
      throw new IllegalArgumentException("cannot invert infinity point");
    return Geometry.foot(O, Geometry.radAxis(this, new Circle(O, P)));
  }

  public Point pole(Line l)
  {
    return inverse(Geometry.foot(center(), l));
  }

  public void draw(Graphics g, int ax, int ay, int bx, int by, int cx, int cy)
  {
    HomogenousPolynomial[] rad = radSqu();
    double a = Math.sqrt((bx - cx) * (bx - cx) + (by - cy) * (by - cy));
    double b = Math.sqrt((cx - ax) * (cx - ax) + (cy - ay) * (cy - ay));
    double c = Math.sqrt((ax - bx) * (ax - bx) + (ay - by) * (ay - by));
    double[] centerV = center().getCoords().eval(a, b, c);
    int centerX = (int) Math.round(centerV[0] * ax + centerV[1] * bx + centerV[2] * cx);
    int centerY = (int) Math.round(centerV[0] * ay + centerV[1] * by + centerV[2] * cy);
    int r = (int) Math.round(Math.sqrt(rad[0].eval(a, b, c) / rad[1].eval(a, b, c)));
    g.drawOval(centerX - r, centerY - r, 2 * r, 2 * r);
  }
}
