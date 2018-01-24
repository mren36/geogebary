// a class that represents a vector of 3 homogenous polynomials of the same degree

public class HomogenousVector
{
  private HomogenousPolynomial x; // the first polynomial
  private HomogenousPolynomial y; // the second polynomial
  private HomogenousPolynomial z; // the third polynomial

  public HomogenousVector() // returns a new vector of all degree 0 zero polynomials
  {
    x = new HomogenousPolynomial(0);
    y = new HomogenousPolynomial(0);
    z = new HomogenousPolynomial(0);
  }

  public HomogenousVector(int i) // returns a new vector of all degree i zero polynomials
  {
    x = new HomogenousPolynomial(i);
    y = new HomogenousPolynomial(i);
    z = new HomogenousPolynomial(i);
  }

  public HomogenousVector(HomogenousPolynomial x, HomogenousPolynomial y, HomogenousPolynomial z) // sets x, y, z
  {
    if (x.degree() != y.degree() || y.degree() != z.degree() || z.degree() != x.degree())
      throw new IllegalArgumentException("polynomials must be same degree");
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public HomogenousVector(String xString, String yString, String zString) // sets x, y, z in String forms
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

  public HomogenousVector(HomogenousVector other) // copy constructor
  {
    x = other.x;
    y = other.y;
    z = other.z;
  }

  public void reduce() // factors out the gcd of the coefficients of the polynomial
  {
    int Xgcd = x.gcd();
    int Ygcd = y.gcd();
    int Zgcd = z.gcd();
    int gcd = HomogenousPolynomial.gcd(Xgcd, HomogenousPolynomial.gcd(Ygcd, Zgcd));
    if (gcd != 0)
    {
      x = x.div(gcd);
      y = y.div(gcd);
      z = z.div(gcd);
    }
  }

  public int degree() // returns the degree of the polynomials
  {
    return x.degree();
  }

  public HomogenousVector plus(HomogenousVector other) // adds the vectors this and other, component wise
  {
    if (degree() != other.degree())
      throw new IllegalArgumentException("vectors must be same degree");
    return new HomogenousVector(x.plus(other.x), y.plus(other.y), z.plus(other.z));
  }

  public HomogenousVector times(int c) // multiplies the vector by an integer, to each component
  {
    return new HomogenousVector(x.times(c), y.times(c), z.times(c));
  }

  public HomogenousVector times(HomogenousPolynomial p) // multiplies the vectors by a HomogenousPolynomial, to each component
  {
    return new HomogenousVector(p.times(x), p.times(y), p.times(z));
  }

  public HomogenousVector times(HomogenousVector v) // mulitplies the vectors this and other, component wise
  {
    return new HomogenousVector(x.times(v.getX()), y.times(v.getY()), z.times(v.getZ()));
  }

  public HomogenousPolynomial dot(HomogenousVector other) // computes the dot product of this and other
  {
    return x.times(other.x).plus(y.times(other.y).plus(z.times(other.z)));
  }

  public HomogenousVector cross(HomogenousVector other) // computes the cross product of this and other
  {
    HomogenousPolynomial newX = y.times(other.z).minus(z.times(other.y));
    HomogenousPolynomial newY = z.times(other.x).minus(x.times(other.z));
    HomogenousPolynomial newZ = x.times(other.y).minus(y.times(other.x));
    return new HomogenousVector(newX, newY, newZ);
  }

  public boolean perp(HomogenousVector other) // returns whether the dot product of this and other is 0
  {
    return dot(other).equalsZero();
  }

  public boolean equals(HomogenousVector other) // returns whether the vectors have their polynomials in the same ratio, checks via cross product
  {
    if (this.equalsZero())
      return other.equalsZero();
    if (other.equalsZero())
      return this.equalsZero();
    return cross(other).equalsZero();
  }

  public boolean equalsZero() // returns whether all three entries are zero polynomials
  {
    return x.equalsZero() && y.equalsZero() && z.equalsZero();
  }

  public String toString() // returns String form of vector as ordered triple
  {
    return "(" + x + ", " + y + ", " + z + ")";
  }

  public HomogenousPolynomial getX() // getter method for x
  {
    return x;
  }

  public HomogenousPolynomial getY() // getter method for y
  {
    return y;
  }

  public HomogenousPolynomial getZ() // getter method for z
  {
    return z;
  }

  public HomogenousPolynomial weight() // returns the sum of x, y, z
  {
    return x.plus(y.plus(z));
  }
}
