public class Line
{
  private HomogenousVector coeffs;

  public Line(HomogenousVector coeffs)
  {
    if (coeffs.equalsZero())
      throw new IllegalArgumentException("line equation coordinates cannot all be zero");
    HomogenousVector v = coeffs;
    v.reduce();
    this.coeffs = v;
  }

  public Line(HomogenousPolynomial x, HomogenousPolynomial y, HomogenousPolynomial z)
  {
    coeffs = new HomogenousVector(x, y, z);
    if (coeffs.equalsZero())
      throw new IllegalArgumentException("line equation coordinates cannot all be zero");
    HomogenousVector v = coeffs;
    v.reduce();
    this.coeffs = v;
  }

  public Line(String xString, String yString, String zString)
  {
    coeffs = new HomogenousVector(xString, yString, zString);
    if (coeffs.equalsZero())
      throw new IllegalArgumentException("line equation coordinates cannot all be zero");
    HomogenousVector v = coeffs;
    v.reduce();
    this.coeffs = v;
  }

  public Line(Point P1, Point P2)
  {
    if (P1.equals(P2))
      throw new IllegalArgumentException("points cannot be the same");
    HomogenousVector v = P1.getCoords().cross(P2.getCoords());
    v.reduce();
    this.coeffs = v;
  }

  public Line(Line other)
  {
    coeffs = other.coeffs;
  }

  public boolean equals(Line other)
  {
    return coeffs.equals(other.coeffs);
  }

  public HomogenousVector getCoeffs()
  {
    return coeffs;
  }

  public String toString()
  {
    return "( " + coeffs.getX() + " ) x + ( " + coeffs.getY() + " ) y + ( " + coeffs.getZ() + " ) z = 0";
  }

  public Point intersect(Line other)
  {
    return new Point(this, other);
  }

  public boolean contains(Point P)
  {
    return coeffs.perp(P.getCoords());
  }

  public boolean parallel(Line other)
  {
    return Geometry.parallel(this, other);
  }

  public boolean perp(Line other)
  {
    return Geometry.perp(this, other);
  }

  public HomogenousPolynomial getX()
  {
    return coeffs.getX();
  }

  public HomogenousPolynomial getY()
  {
    return coeffs.getY();
  }

  public HomogenousPolynomial getZ()
  {
    return coeffs.getZ();
  }

  public boolean isTangent(Circle c)
  {
    return Geometry.isTangent(this, c);
  }
}
