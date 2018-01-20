public class Circle
{
  private HomogenousPolynomial coeff;
  private HomogenousVector radCoeffs;

  public Circle()
  {
    coeff = new HomogenousPolynomial("1");
    radCoeffs = new HomogenousVector(2);
  }

  public Circle(HomogenousPolynomial coeff, HomogenousVector radCoeffs)
  {
    if (coeff.equalsZero())
      throw new IllegalArgumentException("coefficient must be nonzero");
    if (coeff.degree() + 2 != radCoeffs.degree())
      throw new IllegalArgumentException("degree of radical coefficients must be two more than degree of coefficient");
    this.coeff = coeff;
    this.radCoeffs = radCoeffs;
  }

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

  public Circle(String coeffString, String radXString, String radYString, String radZString)
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

  public Circle(Circle other)
  {
    coeff = other.coeff;
    radCoeffs = other.radCoeffs;
  }

  public Circle(Point P1, Point P2, Point P3)
  {
    Circle circle = Geometry.circumcircle(P1, P2, P3);
    coeff = circle.coeff;
    radCoeffs = circle.radCoeffs;
  }

  public boolean equals(Circle other)
  {
    return radCoeffs.times(other.coeff).equals(other.radCoeffs.times(coeff));
  }

  public Point center()
  {
    HomogenousPolynomial xCoord = new HomogenousPolynomial("a^2").times(radCoeffs.dot(new HomogenousVector("-2", "1", "1"))).plus(new HomogenousPolynomial("b^2-c^2").times(radCoeffs.dot(new HomogenousVector("0", "1", "-1"))).plus(coeff.times(new HomogenousPolynomial("-a^4+a^2b^2+a^2c^2"))));
    HomogenousPolynomial yCoord = new HomogenousPolynomial("b^2").times(radCoeffs.dot(new HomogenousVector("1", "-2", "1"))).plus(new HomogenousPolynomial("c^2-a^2").times(radCoeffs.dot(new HomogenousVector("-1", "0", "1"))).plus(coeff.times(new HomogenousPolynomial("a^2b^2-b^4+b^2c^2"))));
    HomogenousPolynomial zCoord = new HomogenousPolynomial("c^2").times(radCoeffs.dot(new HomogenousVector("1", "1", "-2"))).plus(new HomogenousPolynomial("a^2-b^2").times(radCoeffs.dot(new HomogenousVector("1", "-1", "0"))).plus(coeff.times(new HomogenousPolynomial("a^2c^2+b^2c^2-c^4"))));
    return new Point(xCoord, yCoord, zCoord);
  }

  public String toString()
  {
    return "( " + coeff + " ) * ( a^2yz+b^2zx+c^2xy ) = ( x+y+z ) * ( ( " + radCoeffs.getX() + " )x+( " + radCoeffs.getY() + " )y+( " + radCoeffs.getZ() + " )z )";
  }

  public HomogenousPolynomial getCoeff()
  {
    return coeff;
  }

  public HomogenousVector getRadCoeffs()
  {
    return radCoeffs;
  }

  public boolean contains(Point P)
  {
    return P.on(this);
  }

  public Line radAxis(Circle other)
  {
    return Geometry.radAxis(this, other);
  }

  public boolean isTangent(Line l)
  {
    return Geometry.isTangent(l, this);
  }

  public boolean isTangent(Circle other)
  {
    return Geometry.isTangent(this, other);
  }
}
