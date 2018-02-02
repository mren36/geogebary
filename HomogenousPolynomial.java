// a class that represents a homogenous polynomial with integer coefficients in three variables a, b, c

import java.math.*;

public class HomogenousPolynomial
{
  private BigInteger[][] poly; // an array that stores the coefficients of the polynomial
                        // poly[i][j] denotes the coefficient of a^ib^jc^(d-i-j)
                        // where d is the degree of the polynomial

  public HomogenousPolynomial(int d) // returns a polynomial of degree d with coefficients all 0
  {
    poly = new BigInteger[d + 1][d + 1];
  }

  public HomogenousPolynomial(BigInteger[][] poly) // returns a polynomial with coefficients specified by the array
  {
    if (poly.length != poly[0].length)
      throw new IllegalArgumentException("number of rows must equal number of columns");
    else
      this.poly = poly;
  }

  public HomogenousPolynomial(HomogenousPolynomial other) // copy constructor
  {
    this.poly = other.poly;
  }

  public HomogenousPolynomial(String polyString) // returns a polynomial given its String form
                                                 // no spaces between coefficients and terms ordered a,b,c
                                                 // example: a^3-ab^2+3ab^2+7c^3
                                                 // non-example: a+b^2 (not homogenous)
                                                 // non-example: a+cb (cb not in order a,b,c and should be bc instead)
                                                 // non-example: a + b (spacing between terms)
  {
    String s = addPlusOne(polyString);
    String[] monomials = s.split("\\+");
    if (monomials.length == 0)
      throw new IllegalArgumentException("string cannot be empty");
    BigInteger[] leadingTerm = coeffPow(monomials[0]);
    int degree = leadingTerm[1].add(leadingTerm[2].add(leadingTerm[3])).intValue();
    BigInteger[][] poly = new BigInteger[degree + 1][degree + 1];
    for (int i = 0; i <= degree; i++)
      for (int j = 0; j <= degree; j++)
        poly[i][j] = BigInteger.ZERO;
    for (int i = 0; i < monomials.length; i++)
    {
      BigInteger[] term = coeffPow(monomials[i]);
      if (term[1].add(term[2].add(term[3])).intValue() != degree)
        throw new IllegalArgumentException("polynomial must be homogenous");
      poly[term[1].intValue()][term[2].intValue()] = poly[term[1].intValue()][term[2].intValue()].add(term[0]);
    }
    this.poly = poly;
  }

  public int degree() // returns the degree of the polynomial
  {
    return poly.length - 1;
  }

  public HomogenousPolynomial plus(HomogenousPolynomial other) // returns the polynomial sum of this and other
  {
    if (this.degree() != other.degree())
      throw new IllegalArgumentException("degrees must be the same");
    BigInteger[][] newPoly = new BigInteger[degree() + 1][degree() + 1];
    for (int i = 0; i <= degree(); i++)
      for (int j = 0; j <= degree(); j++)
        newPoly[i][j] = poly[i][j].add(other.poly[i][j]);
    return new HomogenousPolynomial(newPoly);
  }

  public HomogenousPolynomial times(BigInteger c) // returns the product of the polynomial and an integer c
  {
    BigInteger[][] newPoly = new BigInteger[degree() + 1][degree() + 1];
    for (int i = 0; i <= degree(); i++)
      for (int j = 0; j <= degree(); j++)
        newPoly[i][j] = c.multiply(poly[i][j]);
    return new HomogenousPolynomial(newPoly);
  }

  public HomogenousPolynomial div(BigInteger c) // returns the quotient of the polynomial and an integer c
  {
    BigInteger[][] newPoly = new BigInteger[degree() + 1][degree() + 1];
    for (int i = 0; i <= degree(); i++)
      for (int j = 0; j <= degree(); j++)
        newPoly[i][j] = poly[i][j].divide(c);
    return new HomogenousPolynomial(newPoly);
  }

  public BigInteger gcd() // returns the gcd of the coefficients of the polynomial, defined to be 0 if all coefficients are 0
  {
    BigInteger gcd = BigInteger.ZERO;
    for (int i = 0; i <= degree(); i++)
      for (int j = 0; j <= degree(); j++)
        gcd = gcd.gcd(poly[i][j]);
    return gcd;
  }

  public HomogenousPolynomial minus(HomogenousPolynomial other) // returns the polynomial difference between this and other
  {
    return plus(other.times(new BigInteger("-1")));
  }

  public HomogenousPolynomial times(HomogenousPolynomial other) // returns the polynomial product of this and other
  {
    BigInteger[][] newPoly = new BigInteger[degree() + other.degree() + 1][degree() + other.degree() + 1];
    for (int i = 0; i < newPoly.length; i++)
      for (int j = 0; j < newPoly.length; j++)
        newPoly[i][j] = BigInteger.ZERO;
    for (int i1 = degree() + 1; i1 >= 0; i1--)
      for (int j1 = degree() - i1; j1 >= 0; j1--)
        for (int i2 = other.degree() + 1; i2 >= 0; i2--)
          for (int j2 = other.degree() - i2; j2 >= 0; j2--)
            newPoly[i1 + i2][j1 + j2] = newPoly[i1 + i2][j1 + j2].add(poly[i1][j1].multiply(other.poly[i2][j2]));
    return new HomogenousPolynomial(newPoly);
  }

  public HomogenousPolynomial pow(int p) // returns the polynomial raised to the pth power
  {
    if (p < 1)
      throw new IllegalArgumentException("power must be positive integer");
    if (p == 1)
      return new HomogenousPolynomial(this);
    return times(pow(p - 1));
  }

  public HomogenousVector times(HomogenousVector v) // returns the product of the polynomial by a HomogenousVector
  {
    return v.times(this);
  }

  public boolean equalsZero() // returns wehther the polynomial is the zero polynomial, i.e. has all coefficients 0
  {
    for (int i = 0; i <= degree(); i++)
      for (int j = 0; j <= degree(); j++)
        if (!poly[i][j].equals(BigInteger.ZERO))
          return false;
    return true;
  }

  public boolean equals(HomogenousPolynomial other) // returns whether the polynomial has the same coefficients as other
  {
    return minus(other).equalsZero();
  }

  public BigInteger eval(BigInteger a, BigInteger b, BigInteger c)
  {
    BigInteger p = BigInteger.ZERO;
    for (int i = 0; i <= degree(); i++)
      for (int j = 0; j <= degree() - i; j++)
      p = p.add(a.pow(i).multiply(b.pow(j)).multiply(c.pow(degree() - i - j)).multiply(poly[i][j]));
    return p;
  }

  public BigDecimal eval(BigDecimal a, BigDecimal b, BigDecimal c)
  {
    BigDecimal p = BigDecimal.ZERO;
    for (int i = 0; i <= degree(); i++)
      for (int j = 0; j <= degree() - i; j++)
      p = p.add(a.pow(i).multiply(b.pow(j)).multiply(c.pow(degree() - i - j)).multiply(new BigDecimal(poly[i][j])));
    return p;
  }

  public String toString() // returns the String form of the polynomial, ordered lexicographically as a>b>c
  {
    String polyString = "";
    int d = this.degree();
    for (int i = d + 1; i >= 0; i--)
      for (int j = d - i; j >= 0; j--)
        if (poly[i][j] != BigInteger.ZERO)
          polyString += "+" + poly[i][j] + "a^" + i + "b^" + j + "c^" + (d - i - j);
    if (polyString.equals(""))
      return "0";
    polyString = polyString.replace("+-", "-");
    polyString = polyString.replace("a^0", "");
    polyString = polyString.replace("b^0", "");
    polyString = polyString.replace("c^0", "");
    polyString = polyString.replace("+1a", "+a");
    polyString = polyString.replace("-1a", "-a");
    polyString = polyString.replace("+1b", "+b");
    polyString = polyString.replace("-1b", "-b");
    polyString = polyString.replace("+1c", "+c");
    polyString = polyString.replace("-1c", "-c");
    int index = polyString.indexOf("a^1");
    while (index >= 0)
    {
      if ("1234567890".indexOf(polyString.charAt(index + 3)) == -1)
        polyString = polyString.substring(0, index + 1) + polyString.substring(index + 3);
      index = polyString.indexOf("a^1", index + 1);
    }
    index = polyString.indexOf("b^1");
    while (index >= 0)
    {
      if ("1234567890".indexOf(polyString.charAt(index + 3)) == -1)
        polyString = polyString.substring(0, index + 1) + polyString.substring(index + 3);
      index = polyString.indexOf("b^1", index + 1);
    }
    index = polyString.indexOf("c^1");
    while (index >= 0)
    {
      if ("1234567890".indexOf(polyString.charAt(index + 3)) == -1)
        polyString = polyString.substring(0, index + 1) + polyString.substring(index + 3);
      index = polyString.indexOf("c^1", index + 1);
    }
    if (polyString.charAt(0) == '+')
      polyString = polyString.substring(1);
    return polyString;
  }

  private static String addPlus(String polyString) // private helper method for toString(), adds + between each term
  {
    String s = polyString;
    int index = s.indexOf("-");
    if (index == -1)
      return s;
    while (index >= 0)
    {
      s = s.substring(0, index) + "+" + s.substring(index);
      index = s.indexOf("-", index + 2);
    }
    return s;
  }

  private static String addPlusOne(String polyString) // private helper method for toString(), adds coefficients of 1 and -1
  {
    if (polyString.length() == 0)
      throw new IllegalArgumentException("string cannot be empty");
    String s = addPlus(polyString);
    if ("abc".indexOf(s.charAt(0)) != -1)
      s = 1 + s;
    int index = s.indexOf("+a");
    if (index >= 0)
    {
      while (index >= 0)
      {
        s = s.substring(0, index) + "+1a" + s.substring(index + 2);
        index = s.indexOf("+a");
      }
    }
    index = s.indexOf("+b");
    if (index >= 0)
    {
      while (index >= 0)
      {
        s = s.substring(0, index) + "+1b" + s.substring(index + 2);
        index = s.indexOf("+b");
      }
    }
    index = s.indexOf("+c");
    if (index >= 0)
    {
      while (index >= 0)
      {
        s = s.substring(0, index) + "+1c" + s.substring(index + 2);
        index = s.indexOf("+c");
      }
    }
    index = s.indexOf("-a");
    if (index >= 0)
    {
      while (index >= 0)
      {
        s = s.substring(0, index) + "-1a" + s.substring(index + 2);
        index = s.indexOf("-a");
      }
    }
    index = s.indexOf("-b");
    if (index >= 0)
    {
      while (index >= 0)
      {
        s = s.substring(0, index) + "-1b" + s.substring(index + 2);
        index = s.indexOf("-b");
      }
    }
    index = s.indexOf("-c");
    if (index >= 0)
    {
      while (index >= 0)
      {
        s = s.substring(0, index) + "-1c" + s.substring(index + 2);
        index = s.indexOf("-c");
      }
    }
    if (s.charAt(0) == '+')
      s = s.substring(1);
    return s;
  }

  private static BigInteger[] coeffPow(String monomial) // private helper method for toString(), returns coefficient and powers of a monomial
  {
    int aIndex = monomial.indexOf("a");
    int bIndex = monomial.indexOf("b");
    int cIndex = monomial.indexOf("c");
    if ((aIndex >= 0 && bIndex >= 0 && aIndex > bIndex) || (bIndex >= 0 && cIndex >= 0 && bIndex > cIndex) || (aIndex >= 0 && cIndex >= 0 && aIndex > cIndex))
      throw new IllegalArgumentException("monomial must be of form Ia^Nb^Nc^N where I is an integer, N is nonnegative integer");
    BigInteger coeffLong = BigInteger.ZERO;
    BigInteger aPow = BigInteger.ZERO;
    BigInteger bPow = BigInteger.ZERO;
    BigInteger cPow = BigInteger.ZERO;
    String coeff = "";
    String a = "";
    String b = "";
    String c = "";
    if (aIndex == -1)
    {
      if (bIndex == -1)
      {
        if (cIndex == -1)
          coeff = monomial;
        else
        {
          coeff = monomial.substring(0, cIndex);
          c = monomial.substring(cIndex);
        }
      }
      else
      {
        if (cIndex == -1)
        {
          coeff = monomial.substring(0, bIndex);
          b = monomial.substring(bIndex);
        }
        else
        {
          coeff = monomial.substring(0, bIndex);
          b = monomial.substring(bIndex, cIndex);
          c = monomial.substring(cIndex);
        }
      }
    }
    else
    {
      if (bIndex == -1)
      {
        if (cIndex == -1)
        {
          coeff = monomial.substring(0, aIndex);
          a = monomial.substring(aIndex);
        }
        else
        {
          coeff = monomial.substring(0, aIndex);
          a = monomial.substring(aIndex, cIndex);
          c = monomial.substring(cIndex);
        }
      }
      else
      {
        if (cIndex == -1)
        {
          coeff = monomial.substring(0, aIndex);
          a = monomial.substring(aIndex, bIndex);
          b = monomial.substring(bIndex);
        }
        else
        {
          coeff = monomial.substring(0, aIndex);
          a = monomial.substring(aIndex, bIndex);
          b = monomial.substring(bIndex, cIndex);
          c = monomial.substring(cIndex);
        }
      }
    }
    try
    {
      coeffLong = new BigInteger(coeff);
    }
    catch (NumberFormatException e)
    {
      throw new IllegalArgumentException("monomial must be of form Ia^Nb^Nc^N where I is an integer, N is nonnegative integer");
    }
    if (a.equals(""))
      aPow = BigInteger.ZERO;
    else if (a.equals("a"))
      aPow = BigInteger.ONE;
    else
    {
      if (a.charAt(1) == '^')
        try
        {
          aPow = new BigInteger(a.substring(2));
          if (aPow.compareTo(BigInteger.ZERO) < 0)
            throw new IllegalArgumentException("monomial must be of form Ia^Nb^Nc^N where I is an integer, N is nonnegative integer");
        }
        catch (NumberFormatException e)
        {
          throw new IllegalArgumentException("monomial must be of form Ia^Nb^Nc^N where I is an integer, N is nonnegative integer");
        }
      else
        throw new IllegalArgumentException("monomial must be of form Ia^Nb^Nc^N where I is an integer, N is nonnegative integer");
    }
    if (b.equals(""))
      bPow = BigInteger.ZERO;
    else if (b.equals("b"))
      bPow = BigInteger.ONE;
    else
    {
      if (b.charAt(1) == '^')
        try
        {
          bPow = new BigInteger(b.substring(2));
          if (bPow.compareTo(BigInteger.ZERO) < 0)
            throw new IllegalArgumentException("monomial must be of form Ia^Nb^Nc^N where I is an integer, N is nonnegative integer");
        }
        catch (NumberFormatException e)
        {
          throw new IllegalArgumentException("monomial must be of form Ia^Nb^Nc^N where I is an integer, N is nonnegative integer");
        }
      else
        throw new IllegalArgumentException("monomial must be of form Ia^Nb^Nc^N where I is an integer, N is nonnegative integer");
    }
    if (c.equals(""))
      cPow = BigInteger.ZERO;
    else if (c.equals("c"))
      cPow = BigInteger.ONE;
    else
    {
      if (c.charAt(1) == '^')
        try
        {
          cPow = new BigInteger(c.substring(2));
          if (cPow.compareTo(BigInteger.ZERO) < 0)
            throw new IllegalArgumentException("monomial must be of form Ia^Nb^Nc^N where I is an integer, N is nonnegative integer");
        }
        catch (NumberFormatException e)
        {
          throw new IllegalArgumentException("monomial must be of form Ia^Nb^Nc^N where I is an integer, N is nonnegative integer");
        }
      else
        throw new IllegalArgumentException("monomial must be of form Ia^Nb^Nc^N where I is an integer, N is nonnegative integer");
    }
    BigInteger[] mono = new BigInteger[4];
    mono[0] = coeffLong;
    mono[1] = aPow;
    mono[2] = bPow;
    mono[3] = cPow;
    return mono;
  }
}
