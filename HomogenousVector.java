public class HomogenousVector
{
  private HomogenousPolynomial x;
  private HomogenousPolynomial y;
  private HomogenousPolynomial z;

  public HomogenousVector()
  {
    x = new HomogenousPolynomial(0);
    y = new HomogenousPolynomial(0);
    z = new HomogenousPolynomial(0);
  }

  public HomogenousVector(HomogenousPolynomial x, HomogenousPolynomial y, HomogenousPolynomial z)
  {
    if (x.degree() != y.degree() || y.degree() != z.degree() || z.degree() != x.degree())
      throw new IllegalArgumentException("polynomials must be same degree");
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public HomogenousVector(String xString, String yString, String zString)
  {
    HomogenousPolynomial x = new HomogenousPolynomial(xString);
    HomogenousPolynomial y = new HomogenousPolynomial(yString);
    HomogenousPolynomial z = new HomogenousPolynomial(zString);
    if (x.degree() != y.degree() || y.degree() != z.degree() || z.degree() != x.degree())
      throw new IllegalArgumentException("polynomials must be same degree");
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public int degree()
  {
    return x.degree();
  }

  public HomogenousVector plus(HomogenousVector other)
  {
    if (degree() != other.degree())
      throw new IllegalArgumentException("vectors must be same degree");
    return new HomogenousVector(x.plus(other.x), y.plus(other.y), z.plus(other.z));
  }

  public HomogenousVector timesConstant(int c)
  {
    return new HomogenousVector(x.timesConstant(c), y.timesConstant(c), z.timesConstant(c));
  }

  public HomogenousVector times(HomogenousPolynomial p)
  {
    return new HomogenousVector(p.times(x), p.times(y), p.times(z));
  }

  public HomogenousVector times(HomogenousVector v)
  {
    return new HomogenousVector(x.times(v.getX()), y.times(v.getY()), z.times(v.getZ()));
  }

  public HomogenousPolynomial dot(HomogenousVector other)
  {
    return x.times(other.x).plus(y.times(other.y).plus(z.times(other.z)));
  }

  public HomogenousVector cross(HomogenousVector other)
  {
    HomogenousPolynomial newX = y.times(other.z).minus(z.times(other.y));
    HomogenousPolynomial newY = z.times(other.x).minus(x.times(other.z));
    HomogenousPolynomial newZ = x.times(other.y).minus(y.times(other.x));
    return new HomogenousVector(newX, newY, newZ);
  }

  public boolean perp(HomogenousVector other)
  {
    return dot(other).equalsZero();
  }

  public boolean equals(HomogenousVector other)
  {
    return cross(other).equalsZero();
  }

  public boolean equalsZero()
  {
    return x.equalsZero() && y.equalsZero() && z.equalsZero();
  }

  public String toString()
  {
    return "(" + x + ", " + y + ", " + z + ")";
  }

  public HomogenousPolynomial getX()
  {
    return x;
  }

  public HomogenousPolynomial getY()
  {
    return y;
  }

  public HomogenousPolynomial getZ()
  {
    return z;
  }
}
