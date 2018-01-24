// represents a circle with barycentric equation k(a^2yz+b^2zx+c^2xy)=(x+y+z)(ux+vy+wz)
// k, u, v, w are integer coefficient homogenous polynomials with deg(k)+2=deg(u)=deg(v)=deg(w)

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
}
