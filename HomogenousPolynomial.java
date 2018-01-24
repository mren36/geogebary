public class HomogenousPolynomial
{
  private int[][] poly;

  public HomogenousPolynomial(int degree)
  {
    poly = new int[degree + 1][degree + 1];
  }

  public HomogenousPolynomial(int[][] poly)
  {
    if (poly.length != poly[0].length)
      throw new IllegalArgumentException("number of rows must equal number of columns");
    else
      this.poly = poly;
  }

  public HomogenousPolynomial(HomogenousPolynomial other)
  {
    this.poly = other.poly;
  }

  public HomogenousPolynomial(String polyString)
  {
    String s = addPlusOne(polyString);
    String[] monomials = s.split("\\+");
    if (monomials.length == 0)
      throw new IllegalArgumentException("string cannot be empty");
    int[] leadingTerm = coeffPow(monomials[0]);
    int degree = leadingTerm[1] + leadingTerm[2] + leadingTerm[3];
    int[][] poly = new int[degree + 1][degree + 1];
    for (int i = 0; i < monomials.length; i++)
    {
      int[] term = coeffPow(monomials[i]);
      if (term[1] + term[2] + term[3] != degree)
        throw new IllegalArgumentException("polynomial must be homogenous");
      poly[term[1]][term[2]] += term[0];
    }
    this.poly = poly;
  }

  public int degree()
  {
    return poly.length - 1;
  }

  public HomogenousPolynomial plus(HomogenousPolynomial other)
  {
    if (this.degree() != other.degree())
      throw new IllegalArgumentException("degrees must be the same");
    int[][] newPoly = new int[degree() + 1][degree() + 1];
    for (int i = 0; i <= degree(); i++)
      for (int j = 0; j <= degree(); j++)
        newPoly[i][j] = poly[i][j] + other.poly[i][j];
    return new HomogenousPolynomial(newPoly);
  }

  public HomogenousPolynomial times(int c)
  {
    int[][] newPoly = new int[degree() + 1][degree() + 1];
    for (int i = 0; i <= degree(); i++)
      for (int j = 0; j <= degree(); j++)
        newPoly[i][j] = c * poly[i][j];
    return new HomogenousPolynomial(newPoly);
  }

  public HomogenousPolynomial div(int c)
  {
    int[][] newPoly = new int[degree() + 1][degree() + 1];
    for (int i = 0; i <= degree(); i++)
      for (int j = 0; j <= degree(); j++)
        newPoly[i][j] = poly[i][j] / c;
    return new HomogenousPolynomial(newPoly);
  }

  public int gcd()
  {
    int gcd = 0;
    for (int i = 0; i <= degree(); i++)
      for (int j = 0; j <= degree(); j++)
        gcd = gcd(gcd, poly[i][j]);
    return gcd;
  }

  public HomogenousPolynomial minus(HomogenousPolynomial other)
  {
    return plus(other.times(-1));
  }

  public HomogenousPolynomial times(HomogenousPolynomial other)
  {
    int[][] newPoly = new int[degree() + other.degree() + 1][degree() + other.degree() + 1];
    for (int i1 = degree() + 1; i1 >= 0; i1--)
      for (int j1 = degree() - i1; j1 >= 0; j1--)
        for (int i2 = other.degree() + 1; i2 >= 0; i2--)
          for (int j2 = other.degree() - i2; j2 >= 0; j2--)
            newPoly[i1 + i2][j1 + j2] += poly[i1][j1] * other.poly[i2][j2];
    return new HomogenousPolynomial(newPoly);
  }

  public HomogenousPolynomial pow(int p)
  {
    if (p < 1)
      throw new IllegalArgumentException("power must be positive integer");
    if (p == 1)
      return new HomogenousPolynomial(this);
    return times(pow(p - 1));
  }

  public HomogenousVector times(HomogenousVector v)
  {
    return v.times(this);
  }

  public boolean equalsZero()
  {
    for (int i = 0; i <= degree(); i++)
      for (int j = 0; j <= degree(); j++)
        if (poly[i][j] != 0)
          return false;
    return true;
  }

  public boolean equals(HomogenousPolynomial other)
  {
    return minus(other).equalsZero();
  }

  public String toString()
  {
    String polyString = "";
    int d = this.degree();
    for (int i = d + 1; i >= 0; i--)
      for (int j = d - i; j >= 0; j--)
        if (poly[i][j] != 0)
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

  private static String addPlus(String polyString)
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

  private static String addPlusOne(String polyString)
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

  private static int[] coeffPow(String monomial)
  {
    int aIndex = monomial.indexOf("a");
    int bIndex = monomial.indexOf("b");
    int cIndex = monomial.indexOf("c");
    if ((aIndex >= 0 && bIndex >= 0 && aIndex > bIndex) || (bIndex >= 0 && cIndex >= 0 && bIndex > cIndex) || (aIndex >= 0 && cIndex >= 0 && aIndex > cIndex))
      throw new IllegalArgumentException("monomial must be of form Ia^Nb^Nc^N where I is an integer, N is nonnegative integer");
    int coeffInt = 0;
    int aPow = 0;
    int bPow = 0;
    int cPow = 0;
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
      coeffInt = Integer.parseInt(coeff);
    }
    catch (NumberFormatException e)
    {
      throw new IllegalArgumentException("monomial must be of form Ia^Nb^Nc^N where I is an integer, N is nonnegative integer");
    }
    if (a.equals(""))
      aPow = 0;
    else if (a.equals("a"))
      aPow = 1;
    else
    {
      if (a.charAt(1) == '^')
        try
        {
          aPow = Integer.parseInt(a.substring(2));
          if (aPow < 0)
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
      bPow = 0;
    else if (b.equals("b"))
      bPow = 1;
    else
    {
      if (b.charAt(1) == '^')
        try
        {
          bPow = Integer.parseInt(b.substring(2));
          if (bPow < 0)
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
      cPow = 0;
    else if (c.equals("c"))
      cPow = 1;
    else
    {
      if (c.charAt(1) == '^')
        try
        {
          cPow = Integer.parseInt(c.substring(2));
          if (cPow < 0)
            throw new IllegalArgumentException("monomial must be of form Ia^Nb^Nc^N where I is an integer, N is nonnegative integer");
        }
        catch (NumberFormatException e)
        {
          throw new IllegalArgumentException("monomial must be of form Ia^Nb^Nc^N where I is an integer, N is nonnegative integer");
        }
      else
        throw new IllegalArgumentException("monomial must be of form Ia^Nb^Nc^N where I is an integer, N is nonnegative integer");
    }
    int[] mono = new int[4];
    mono[0] = coeffInt;
    mono[1] = aPow;
    mono[2] = bPow;
    mono[3] = cPow;
    return mono;
  }

  public static int gcd(int a, int b)
  {
    if (a == 0)
      return b;
    if (b == 0)
      return a;
    return gcd(b, a%b);
  }
}
