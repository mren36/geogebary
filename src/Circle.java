import java.math.*;

/**
 * Circle: Class that represents a circle with barycentric equation k(a^2yz+b^2zx+c^2xy)=(x+y+z)(ux+vy+wz)
 * where k, u, v, w are integer coefficient homogenous polynomials with deg(k)+2=deg(u)=deg(v)=deg(w)
 */
public class Circle
{
  private HomogenousPolynomial coeff; // represents the k coefficient in front of the left hand side in the equation above
  private HomogenousVector radCoeffs; // represents the coefficients u,v,w of the radical axis of the circle with the circumcircle
                                      // of the reference triangle in the right hand side of the equation above

  /**
   * Initializes a circle as the circumcircle of the reference triangle.
   */
  public Circle()
  {
    coeff = new HomogenousPolynomial("1");
    radCoeffs = new HomogenousVector(2);
  }

  /**
   * Initializes a circle with the given coefficients and radical coefficients.
   * @param coeff
   * @param radCoeffs
   */
  public Circle(HomogenousPolynomial coeff, HomogenousVector radCoeffs)
  {
    if (coeff.equalsZero())
      throw new IllegalArgumentException("coefficient must be nonzero");
    if (coeff.degree() + 2 != radCoeffs.degree())
      throw new IllegalArgumentException("degree of radical coefficients must be two more than degree of coefficient");
    this.coeff = coeff;
    this.radCoeffs = radCoeffs;
  }

  /**
   * Initializes a circle with k, u, v, and w values given the coefficients and radical coefficients as X, Y, Z, polynomials.
   * @param coeff
   * @param radX
   * @param radY
   * @param radZ
   */
  public Circle(HomogenousPolynomial coeff, HomogenousPolynomial radX, HomogenousPolynomial radY, HomogenousPolynomial radZ)
  {
    radCoeffs = new HomogenousVector(radX, radY, radZ);
    if (coeff.equalsZero())
      throw new IllegalArgumentException("coefficient must be nonzero");
    if (coeff.degree() + 2 != radCoeffs.degree())
      throw new IllegalArgumentException("degree of radical coefficients must be two more than degree of coefficient");
    this.coeff = coeff;
    this.radCoeffs = radCoeffs;
  }

  /**
   * Initializes a circle with k, u, v, and w values given the coefficients and radical coefficients as strings.
   * @param coeffString
   * @param radXString
   * @param radYString
   * @param radZString
   */
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

  /**
   * Initializes this circle as a copy of the given circle.
   * @param other
   */
  public Circle(Circle other)
  {
    coeff = other.coeff;
    radCoeffs = other.radCoeffs;
  }

  /**
   * Initializes a circle that passes through the three given points
   * @param P1
   * @param P2
   * @param P3
   */
  public Circle(Point P1, Point P2, Point P3)
  {
    Circle circle = Geometry.circumcircle(P1, P2, P3);
    coeff = circle.coeff;
    radCoeffs = circle.radCoeffs;
  }

  /**
   * Initializes a circle with the diameter connecting the two given points.
   * @param P1
   * @param P2
   */
  public Circle(Point P1, Point P2)
  {
    Circle circle = Geometry.diameter(P1, P2);
    coeff = circle.coeff;
    radCoeffs = circle.radCoeffs;
  }

  /**
   * Returns whether this circle and the given circle have the same barycentric equation when scaled.
   * @param other
   * @return
   */
  public boolean equals(Circle other)
  {
    return radCoeffs.times(other.coeff).equals(other.radCoeffs.times(coeff));
  }

  /**
   * Returns the center of this circle.
   * @return
   */
  public Point center()
  {
    HomogenousPolynomial xCoord = new HomogenousPolynomial("a^2").times(radCoeffs.dot(new HomogenousVector("-2", "1", "1"))).plus(new HomogenousPolynomial("b^2-c^2").times(radCoeffs.dot(new HomogenousVector("0", "1", "-1"))).plus(coeff.times(new HomogenousPolynomial("-a^4+a^2b^2+a^2c^2"))));
    HomogenousPolynomial yCoord = new HomogenousPolynomial("b^2").times(radCoeffs.dot(new HomogenousVector("1", "-2", "1"))).plus(new HomogenousPolynomial("c^2-a^2").times(radCoeffs.dot(new HomogenousVector("-1", "0", "1"))).plus(coeff.times(new HomogenousPolynomial("a^2b^2-b^4+b^2c^2"))));
    HomogenousPolynomial zCoord = new HomogenousPolynomial("c^2").times(radCoeffs.dot(new HomogenousVector("1", "1", "-2"))).plus(new HomogenousPolynomial("a^2-b^2").times(radCoeffs.dot(new HomogenousVector("1", "-1", "0"))).plus(coeff.times(new HomogenousPolynomial("a^2c^2+b^2c^2-c^4"))));
    return new Point(xCoord, yCoord, zCoord);
  }

  /**
   * Returns the barycentric equation of the circle as a String.
   * @return
   */
  public String toString()
  {
    return "( " + coeff + " ) * ( a^2yz+b^2zx+c^2xy ) = ( x+y+z ) * ( ( " + radCoeffs.getX() + " )x+( " + radCoeffs.getY() + " )y+( " + radCoeffs.getZ() + " )z )";
  }

  /**
   * Returns the coefficients that describe this circle.
   * @return
   */
  public HomogenousPolynomial getCoeff()
  {
    return coeff;
  }

  /**
   * Returns the radical coefficients taht describe this circle.
   * @return
   */
  public HomogenousVector getRadCoeffs() // getter for radCoeff
  {
    return radCoeffs;
  }

  /**
   * Returns whether the given point is on the circle.
   * @param P
   * @return
   */
  public boolean contains(Point P)
  {
    return P.on(this);
  }

  /**
   * Returns the radical axis of this circle and the given circle.
   * @param other
   * @return
   */
  public Line radAxis(Circle other)
  {
    return Geometry.radAxis(this, other);
  }

  /**
   * Returns whether this circle is tangent to the given line.
   * @param l
   * @return
   */
  public boolean isTangent(Line l)
  {
    return Geometry.isTangent(l, this);
  }

  /**
   * Returns whether this circle and the given circle are tangent.
   * @param other
   * @return
   */
  public boolean isTangent(Circle other)
  {
    return Geometry.isTangent(this, other);
  }

  /**
   *
   * @return
   */
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
    frac[0] = (squSum.times(area16).times(f2)).minus(squABC.times(f2)).plus(squA.times(vw2).times(new BigInteger("4"))).plus(squB.times(wu2).times(new BigInteger("4"))).plus(squC.times(uv2).times(new BigInteger("4"))).minus(radCoeffs.weight().times(area16).times(new BigInteger("4")).times(f)).plus(squBC.times(u).times(f).times(new BigInteger("4"))).plus(squCA.times(v).times(f).times(new BigInteger("4"))).plus(squAB.times(w).times(f).times(new BigInteger("4")));
    frac[1] = area16.times(new BigInteger("8")).times(f2);
    System.out.println();
    return frac;
  }

  /**
   *
   * @param P
   * @return
   */
  public Line polar(Point P)
  {
    Point O = center();
    if (P.equals(O))
      return Geometry.INF_LINE;
    return Geometry.radAxis(this, new Circle(O, P));
  }

  /**
   * Returns the given point inverted about this circle's center.
   * @param P
   * @return
   */
  public Point inverse(Point P)
  {
    Point O = center();
    if (P.equals(O))
      throw new IllegalArgumentException("cannot invert center");
    if (P.on(Geometry.INF_LINE))
      throw new IllegalArgumentException("cannot invert infinity point");
    return Geometry.foot(O, Geometry.radAxis(this, new Circle(O, P)));
  }

  /**
   *
   * @param l
   * @return
   */
  public Point pole(Line l)
  {
    return inverse(Geometry.foot(center(), l));
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
  public int[] screenCoords(int ax, int ay, int bx, int by, int cx, int cy)
  {
    HomogenousPolynomial[] rad = radSqu();
    int a = (int) Math.round(Math.sqrt((bx - cx) * (bx - cx) + (by - cy) * (by - cy)));
    int b = (int) Math.round(Math.sqrt((cx - ax) * (cx - ax) + (cy - ay) * (cy - ay)));
    int c = (int) Math.round(Math.sqrt((ax - bx) * (ax - bx) + (ay - by) * (ay - by)));
    BigDecimal[] centerU = center().getCoords().eval(new BigInteger(a + ""), new BigInteger(b + ""), new BigInteger(c + ""));
    double[] centerV = new double[3];
    centerV[0] = centerU[0].doubleValue();
    centerV[1] = centerU[1].doubleValue();
    centerV[2] = centerU[2].doubleValue();
    int[] sC = new int[3];
    sC[0] = (int) Math.round(centerV[0] * ax + centerV[1] * bx + centerV[2] * cx);
    sC[1] = (int) Math.round(centerV[0] * ay + centerV[1] * by + centerV[2] * cy);
    sC[2] = (int) Math.round(Math.sqrt((rad[0].eval(new BigInteger(a + ""), new BigInteger(b + ""), new BigInteger(c + ""))).divide(rad[1].eval(new BigInteger(a + ""), new BigInteger(b + ""), new BigInteger(c + ""))).doubleValue()));
    return sC;
  }

  /**
   * Returns the hashcode of this circle, determined by its coefficients.
   * @return
   */
  public int hashCode()
  {
    return coeff.hashCode();
  }
}
