public class Geometry
{
  public static final Line INF_LINE = new Line("1", "1", "1");

  public static Point average(Point p1, Point p2, int w1, int w2)
  {
    return new Point(p1.getCoords().timesConstant(w1).plus(p2.getCoords().timesConstant(-w2)));
  }

  public static Point midpoint(Point p1, Point p2)
  {
    return average(p1, p2, 1, -1);
  }

  public static Point reflect(Point p1, Point p2)
  {
    return average(p1, p2, 2, 1);
  }

  public static Point polyAverage(Point p1, Point p2, HomogenousPolynomial w1, HomogenousPolynomial w2)
  {
    return new Point(p1.getCoords().times(w1).plus(p2.getCoords().times(w2.timesConstant(-1))));
  }

  public static Point infPoint(Line l)
  {
    if (l.equals(INF_LINE))
      throw new IllegalArgumentException("line cannot be line at infinity");
    return new Point(l, INF_LINE);
  }

  public static Line parallel(Point p, Line l)
  {
    return new Line(p, infPoint(l));
  }

  public static Point perpInfPoint(Line l)
  {
    Point infPoint = infPoint(l);
    HomogenousVector cotAngles = new HomogenousVector("b^2+c^2-a^2", "c^2+a^2-b^2", "a^2+b^2-c^2");
    return new Point(INF_LINE, new Line(cotAngles.times(infPoint.getCoords())));
  }

  public static Line perp(Point p, Line l)
  {
    return new Line(p, perpInfPoint(l));
  }

  public static Point foot(Point p, Line l)
  {
    if (p.on(l))
      return p;
    return new Point(l, perp(p, l));
  }

  public static Point reflect(Point p, Line l)
  {
    return reflect(p, foot(p, l));
  }

  public static boolean parallel(Line l1, Line l2)
  {
    if (l1.equals(l2))
      return true;
    return (new Point(l1, l2)).on(INF_LINE);
  }

  public static boolean perp(Line l1, Line l2)
  {
    return parallel(perp(new Point("1", "0", "0"), l1), l2);
  }

  public static boolean col(Point p1, Point p2, Point p3)
  {
    if (p1.equals(p2) || p2.equals(p3) || p3.equals(p1))
      return true;
    return p1.on(new Line(p2, p3));
  }

  public static boolean conc(Line l1, Line l2, Line l3)
  {
    if (l1.equals(l2) || l2.equals(l3) || l3.equals(l1))
      return true;
    return l1.contains(new Point(l2, l3));
  }
}
