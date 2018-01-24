public class Point
{
  private HomogenousVector coords;

  public Point(HomogenousVector coords)
  {
    if (coords.equalsZero())
      throw new IllegalArgumentException("coordinates cannot all be zero");
    HomogenousVector v = coords;
    v.reduce();
    this.coords = v;
  }

  public Point(HomogenousPolynomial x, HomogenousPolynomial y, HomogenousPolynomial z)
  {
    coords = new HomogenousVector(x, y, z);
    if (coords.equalsZero())
      throw new IllegalArgumentException("coordinates cannot all be zero");
    HomogenousVector v = coords;
    v.reduce();
    this.coords = v;
  }

  public Point(String xString, String yString, String zString)
  {
    coords = new HomogenousVector(xString, yString, zString);
    if (coords.equalsZero())
      throw new IllegalArgumentException("coordinates cannot all be zero");
    HomogenousVector v = coords;
    v.reduce();
    this.coords = v;
  }

  public Point(Line l1, Line l2)
  {
    if (l1.equals(l2))
      throw new IllegalArgumentException("lines cannot be the same");
    HomogenousVector v = l1.getCoeffs().cross(l2.getCoeffs());
    v.reduce();
    this.coords = v;
  }

  public Point(Point other)
  {
    coords = other.coords;
  }

  public boolean equals(Point other)
  {
    return coords.equals(other.coords);
  }

  public HomogenousVector getCoords()
  {
    return coords;
  }

  public String toString()
  {
    return "( " + coords.getX() + " : " + coords.getY() + " : " + coords.getZ() + " )";
  }

  public Line interpolate(Point other)
  {
    return new Line(this, other);
  }

  public boolean on(Line l)
  {
    return coords.perp(l.getCoeffs());
  }

  public Point reflectOver(Point other)
  {
    return Geometry.reflect(this, other);
  }

  public Point reflectOver(Line l)
  {
    return Geometry.reflect(this, l);
  }

  public HomogenousPolynomial weight()
  {
    return coords.weight();
  }

  public HomogenousPolynomial getX()
  {
    return coords.getX();
  }

  public HomogenousPolynomial getY()
  {
    return coords.getY();
  }

  public HomogenousPolynomial getZ()
  {
    return coords.getZ();
  }

  public boolean on(Circle c)
  {
    HomogenousPolynomial pow = c.getCoeff().times(new HomogenousPolynomial("a^2").times(coords.getY().times(coords.getZ())).plus(new HomogenousPolynomial("b^2").times(coords.getZ().times(coords.getX()))).plus(new HomogenousPolynomial("c^2").times(coords.getX().times(coords.getY()))));
    HomogenousPolynomial rad = weight().times(coords.dot(c.getRadCoeffs()));
    return pow.equals(rad);
  }
}
