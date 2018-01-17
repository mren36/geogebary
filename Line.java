public class Line
{
  private HomogenousVector coeffs;

  public Line(HomogenousVector coeffs)
  {
    if (coeffs.equalsZero())
      throw new IllegalArgumentException("line equation coordinates cannot all be zero");
    this.coeffs = coeffs;
  }

  public Line(HomogenousPolynomial x, HomogenousPolynomial y, HomogenousPolynomial z)
  {
    coeffs = new HomogenousVector(x, y, z);
    if (coeffs.equalsZero())
      throw new IllegalArgumentException("line equation coordinates cannot all be zero");
    this.coeffs = coeffs;
  }

  public Line(String xString, String yString, String zString)
  {
    coeffs = new HomogenousVector(xString, yString, zString);
    if (coeffs.equalsZero())
      throw new IllegalArgumentException("line equation coordinates cannot all be zero");
    this.coeffs = coeffs;
  }

  public Line(Point p1, Point p2)
  {
    if (p1.equals(p2))
      throw new IllegalArgumentException("points cannot be the same");
    this.coeffs = p1.getCoords().cross(p2.getCoords());
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

  public boolean contains(Point p)
  {
    return coeffs.perp(p.getCoords());
  }
}
