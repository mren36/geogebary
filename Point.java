// a class that represents a point with barycentric coordinates (x : y : z) for some HomogenousVector (x, y, z)

public class Point
{
  private HomogenousVector coords; // stores the coordinates of the point

  public Point(HomogenousVector coords) // sets the coordinates of the point
  {
    if (coords.equalsZero())
      throw new IllegalArgumentException("coordinates cannot all be zero");
    HomogenousVector v = coords;
    v.reduce();
    this.coords = v;
  }

  public Point(HomogenousPolynomial x, HomogenousPolynomial y, HomogenousPolynomial z) // sets the coordinates of the point
                                                                                       // by inputting polynomials
  {
    coords = new HomogenousVector(x, y, z);
    if (coords.equalsZero())
      throw new IllegalArgumentException("coordinates cannot all be zero");
    HomogenousVector v = coords;
    v.reduce();
    this.coords = v;
  }

  public Point(String xString, String yString, String zString) // sets the coordinates of the opint by inputting Strings
  {
    coords = new HomogenousVector(xString, yString, zString);
    if (coords.equalsZero())
      throw new IllegalArgumentException("coordinates cannot all be zero");
    HomogenousVector v = coords;
    v.reduce();
    this.coords = v;
  }

  public Point(Line l1, Line l2) // returns the interesection of two lines, determined by the cross product of their coefficients
  {
    if (l1.equals(l2))
      throw new IllegalArgumentException("lines cannot be the same");
    HomogenousVector v = l1.getCoeffs().cross(l2.getCoeffs());
    v.reduce();
    this.coords = v;
  }

  public Point(Point other) // copy constructor
  {
    coords = other.coords;
  }

  public boolean equals(Point other) // returns whether other has the same coordinates
  {
    return coords.equals(other.coords);
  }

  public HomogenousVector getCoords() // getter for coords
  {
    return coords;
  }

  public String toString() // returns String format of the point coordinates
  {
    return "( " + coords.getX() + " : " + coords.getY() + " : " + coords.getZ() + " )";
  }

  public Line interpolate(Point other) // returns line through this and other
  {
    return new Line(this, other);
  }

  public boolean on(Line l) // returns whether this is on line l, checks by dot product of coordinates and coefficients
  {
    return coords.perp(l.getCoeffs());
  }

  public Point reflectOver(Point other) // returns the reflection of this over other
  {
    return Geometry.reflect(this, other);
  }

  public Point reflectOver(Line l) // returns the reflection of the point over l
  {
    return Geometry.reflect(this, l);
  }

  public HomogenousPolynomial weight() // returns the weight of the point, i.e. the sum of its coordinates
  {
    return coords.weight();
  }

  public HomogenousPolynomial getX() // getter for x coordinate
  {
    return coords.getX();
  }

  public HomogenousPolynomial getY() // getter for y coordinate
  {
    return coords.getY();
  }

  public HomogenousPolynomial getZ() // getter for z coordinate
  {
    return coords.getZ();
  }

  public boolean on(Circle c) // returns whether this is on c, checks by plugging coordinates into circle equation
  {
    HomogenousPolynomial pow = c.getCoeff().times(new HomogenousPolynomial("a^2").times(coords.getY().times(coords.getZ())).plus(new HomogenousPolynomial("b^2").times(coords.getZ().times(coords.getX()))).plus(new HomogenousPolynomial("c^2").times(coords.getX().times(coords.getY()))));
    HomogenousPolynomial rad = weight().times(coords.dot(c.getRadCoeffs()));
    return pow.equals(rad);
  }
}
