// a static class for the computation of Grobner bases and polynomial gcds, to be completed soon

import java.math.*;

public class Grobner
{
  public static int gcd(int a, int b)
  {
    return new BigInteger(a + "").gcd(new BigInteger(b + "")).intValue();
  }
}
