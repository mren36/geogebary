public class Point
{
  private HomogenousVector coords;

  public Point(HomogenousVector coords)
  {
    if (coords.equalsZero())
      throw new IllegalArgumentException("coordinates cannot all be zero");
    this.coords = coords;
  }

  public Point(Line l1, Line l2)
  {
    if (l1.equals(l2))
      throw new IllegalArgumentException("lines cannot be the same");
    this.coords = l1.getCoeffs().cross(l2.getCoeffs());
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
}
