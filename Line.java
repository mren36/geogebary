// a class that represents a line with barycentric equation ux+vy+wz=0 for some HomogenousVector (u, v, w)

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
}
