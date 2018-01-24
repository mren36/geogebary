// static class with useful methods for geometric constructions

public class Geometry
{
  public static final Line INF_LINE = new Line("1", "1", "1"); // static final variable storing the line at infinity

  public static Point average(Point P1, Point P2, int w1, int w2) // returns the weighted average Q of two points
                                                                  // such that QP1 : QP2 = w1 : w2 (distances directed)
  {
    HomogenousVector v = P1.getCoords().times(w1).times(P2.weight()).plus(P2.getCoords().times(-w2).times(P1.weight()));
    v.reduce();
    return new Point(v);
  }

  public static Point midpoint(Point P1, Point P2) // returns the midpoint of P1 and P2
  {
    return average(P1, P2, 1, -1);
  }

  public static Point reflect(Point P1, Point P2) // returns the reflection of P1 over P2
  {
    return average(P1, P2, 1, 2);
  }

  public static Point polyAverage(Point P1, Point P2, HomogenousPolynomial w1, HomogenousPolynomial w2) // returns the weighted average of
                                                                                                        // the points by two polynomials
  {
    HomogenousVector v = P1.getCoords().times(w1).plus(P2.getCoords().times(w2.times(-1)));
    v.reduce();
    return new Point(v);
  }

  public static Point infPoint(Line l) // returns the infinity point on l, i.e. the intersection of l with the line at infinity
  {
    if (l.equals(INF_LINE))
      throw new IllegalArgumentException("line cannot be line at infinity");
    return new Point(l, INF_LINE);
  }

  public static Line parallel(Point P, Line l) // returns the parallel to l through P
  {
    return new Line(P, infPoint(l));
  }

  public static Point perpInfPoint(Line l) // returns the infinity point perpendicular to l, i.e. the intersection
                                           // of a perpendicular to l with the line at infinity using a given formula
  {
    Point infPoint = infPoint(l);
    HomogenousVector cotAngles = new HomogenousVector("b^2+c^2-a^2", "c^2+a^2-b^2", "a^2+b^2-c^2");
    return new Point(INF_LINE, new Line(cotAngles.times(infPoint.getCoords())));
  }

  public static Line perp(Point P, Line l) // returns the perpendicular to l through P
  {
    return new Line(P, perpInfPoint(l));
  }

  public static Line perp(Point P, Point P1, Point P2) // returns the perpendicular to the line through P1 and P2 through P
  {
    if (P1.equals(P2))
      throw new IllegalArgumentException("points cannot be the same");
    return perp(P, new Line(P1, P2));
  }

  public static Point foot(Point P, Line l) // returns the foot from P to l
  {
    if (P.on(l))
      return P;
    return new Point(l, perp(P, l));
  }

  public static Point foot(Point P, Point P1, Point P2) // returns the foot from P to the line through P1 and P2
  {
    if (P1.equals(P2))
      throw new IllegalArgumentException("points cannot be the same");
    return foot(P, new Line(P1, P2));
  }

  public static Point reflect(Point P, Line l) // returns the reflection of P over l
  {
    return reflect(P, foot(P, l));
  }

  public static boolean parallel(Line l1, Line l2) // returns whether l1 and l2 are parallel, i.e. intersect on the line at infinity
  {
    if (l1.equals(l2))
      return true;
    return (new Point(l1, l2)).on(INF_LINE);
  }

  public static boolean perp(Line l1, Line l2) // returns whether l1 and l2 are perpendicular
  {
    return parallel(perp(new Point("1", "0", "0"), l1), l2);
  }

  public static boolean col(Point P1, Point P2, Point P3) // returns whether P1, P2, and P3 are collinear
  {
    if (P1.equals(P2) || P2.equals(P3) || P3.equals(P1))
      return true;
    return P1.on(new Line(P2, P3));
  }

  public static boolean conc(Line l1, Line l2, Line l3) // returns whether l1, l2, and l3 are concurrent
  {
    if (l1.equals(l2) || l2.equals(l3) || l3.equals(l1))
      return true;
    return l1.contains(new Point(l2, l3));
  }

  public static Line perpBisector(Point P1, Point P2) // returns the perpendicular bisector of P1 and P2
  {
    return perp(midpoint(P1, P2), new Line(P1, P2));
  }

  public static Point centroid(Point P1, Point P2, Point P3) // returns the centroid of triangle P1P2P3
  {
    return average(P1, midpoint(P2, P3), -1, 2);
  }

  public static Point circumcenter(Point P1, Point P2, Point P3) // returns the circumcenter of triangle P1P2P3
  {
    if (P1.equals(P2) || P2.equals(P3) || P3.equals(P1))
      throw new IllegalArgumentException("points must be distinct");
    return new Point(perpBisector(P1, P2), perpBisector(P1, P3));
  }

  public static Point orthocenter(Point P1, Point P2, Point P3) // returns the orthocenter of triangle P1P2P3
  {
    if (P1.equals(P2) || P2.equals(P3) || P3.equals(P1))
      throw new IllegalArgumentException("points must be distinct");
    return new Point(perp(P2, new Line(P1, P3)), perp(P3, new Line(P1, P2)));
  }

  public static Line eulerLine(Point P1, Point P2, Point P3) // returns the Euler line of triangle P1P2P3
  {
    if (P1.equals(P2) || P2.equals(P3) || P3.equals(P1))
      throw new IllegalArgumentException("points must be distinct");
    if (centroid(P1, P2, P3).equals(orthocenter(P1, P2, P3)))
      throw new IllegalArgumentException("triangle cannot be equilateral");
    return new Line(centroid(P1, P2, P3), orthocenter(P1, P2, P3));
  }

  public static HomogenousPolynomial det(HomogenousVector v1, HomogenousVector v2, HomogenousVector v3) // returns the determinant of
                                                                                                        // the matrix [v1, v2, v3]
  {
    return v1.dot(v2.cross(v3));
  }

  public static Circle circumcircle(Point P1, Point P2, Point P3) // returns the circle through P1, P2, P3
                                                                  // determined by a given formula
  {
    if (Geometry.col(P1, P2, P3))
      throw new IllegalArgumentException("triangle must be nondegenerate");
    HomogenousPolynomial weight1 = P1.weight();
    HomogenousPolynomial weight2 = P2.weight();
    HomogenousPolynomial weight3 = P3.weight();
    HomogenousVector squ = new HomogenousVector("a^2", "b^2", "c^2");
    HomogenousVector v1 = P1.getCoords();
    HomogenousVector v2 = P2.getCoords();
    HomogenousVector v3 = P3.getCoords();
    HomogenousPolynomial x1 = P1.getX();
    HomogenousPolynomial y1 = P1.getY();
    HomogenousPolynomial z1 = P1.getZ();
    HomogenousPolynomial x2 = P2.getX();
    HomogenousPolynomial y2 = P2.getY();
    HomogenousPolynomial z2 = P2.getZ();
    HomogenousPolynomial x3 = P3.getX();
    HomogenousPolynomial y3 = P3.getY();
    HomogenousPolynomial z3 = P3.getZ();
    HomogenousPolynomial power1 = squ.dot(new HomogenousVector(y1.times(z1), z1.times(x1), x1.times(y1)));
    HomogenousPolynomial power2 = squ.dot(new HomogenousVector(y2.times(z2), z2.times(x2), x2.times(y2)));
    HomogenousPolynomial power3 = squ.dot(new HomogenousVector(y3.times(z3), z3.times(x3), x3.times(y3)));
    HomogenousPolynomial X1 = y2.times(z3).plus(y3.times(z2).times(-1)).times(weight2).times(weight3).times(power1);
    HomogenousPolynomial X2 = y3.times(z1).plus(y1.times(z3).times(-1)).times(weight3).times(weight1).times(power2);
    HomogenousPolynomial X3 = y1.times(z2).plus(y2.times(z1).times(-1)).times(weight1).times(weight2).times(power3);
    HomogenousPolynomial Y1 = z2.times(x3).plus(z3.times(x2).times(-1)).times(weight2).times(weight3).times(power1);
    HomogenousPolynomial Y2 = z3.times(x1).plus(z1.times(x3).times(-1)).times(weight3).times(weight1).times(power2);
    HomogenousPolynomial Y3 = z1.times(x2).plus(z2.times(x1).times(-1)).times(weight1).times(weight2).times(power3);
    HomogenousPolynomial Z1 = x2.times(y3).plus(x3.times(y2).times(-1)).times(weight2).times(weight3).times(power1);
    HomogenousPolynomial Z2 = x3.times(y1).plus(x1.times(y3).times(-1)).times(weight3).times(weight1).times(power2);
    HomogenousPolynomial Z3 = x1.times(y2).plus(x2.times(y1).times(-1)).times(weight1).times(weight2).times(power3);
    HomogenousPolynomial radX = X1.plus(X2.plus(X3));
    HomogenousPolynomial radY = Y1.plus(Y2.plus(Y3));
    HomogenousPolynomial radZ = Z1.plus(Z2.plus(Z3));
    HomogenousPolynomial det = det(v1.times(weight1), v2.times(weight2), v3.times(weight3));
    return new Circle(det, radX, radY, radZ);
  }

  public static boolean cyclic(Point P1, Point P2, Point P3, Point P4) // returns whether P1, P2, P3, P4 are concyclic
  {
    if (P1.equals(P2) || P1.equals(P3) || P1.equals(P4) || P2.equals(P3) || P2.equals(P4) || P3.equals(P4))
      return true;
    if (col(P1, P2, P3))
      return col(P1, P2, P4);
    return P4.on(circumcircle(P1, P2, P3));
  }

  public static Point secondInt(Line l, Circle c, Point P) // returns the second intersection of l and c given one intersection P
  {
    if (!(P.on(l) && P.on(c)))
      throw new IllegalArgumentException("point must lie on line and circle");
    return P.reflectOver(Geometry.perp(c.center(), l));
  }

  public static Point secondInt(Circle c1, Circle c2, Point P) // returns the second intersection of c1 and c2 given one intersection P
  {
    if (c1.equals(c2))
      throw new IllegalArgumentException("circles must be distinct");
    if (!(P.on(c1) && P.on(c2)))
      throw new IllegalArgumentException("point must lie on both circles");
    return P.reflectOver(new Line(c1.center(), c2.center()));
  }

  public static Line radAxis(Circle c1, Circle c2) // returns the radical axis of c1 and c2, the line through their two intersection points
  {
    if (c1.equals(c2))
      throw new IllegalArgumentException("circles must be distinct");
    return new Line(c2.getCoeff().times(c1.getRadCoeffs()).plus(c1.getCoeff().times(c2.getRadCoeffs().times(-1))));
  }

  public static Point radCenter(Circle c1, Circle c2, Circle c3) // returns the radical center of c1, c2, c3
                                                                 // the concurrence point of the pairwise radical axes
  {
    if (c1.equals(c2) || c2.equals(c3) || c3.equals(c1))
      throw new IllegalArgumentException("circles must be pairwise distinct");
    return new Point(radAxis(c1, c2), radAxis(c1, c3));
  }

  public static boolean isTangent(Circle c1, Circle c2) // returns whether c1 and c2 are tangent
  {
    if (c1.equals(c2))
      throw new IllegalArgumentException("circles must be distinct");
    return foot(c1.center(), radAxis(c1, c2)).on(c1);
  }

  public static boolean isTangent(Line l, Circle c) // returns whether l and c are tangent
  {
    return foot(c.center(), l).on(c);
  }

  public static Line tangentLine(Point P, Circle c) // returns the tangent line to c through a point P on it
  {
    if (!P.on(c))
      throw new IllegalArgumentException("point must be on circle");
    return perp(P, new Line(P, c.center()));
  }

  public static Circle pedal(Point A, Point B, Point C, Point P) // returns the circumcircle of the pedal triangle of P with respect to ABC
  {
    if (P.on(circumcircle(A, B, C)))
      throw new IllegalArgumentException("point cannot be on circumcircle, use Simson line function instead");
    return new Circle(foot(P, new Line(B, C)), foot(P, new Line(C, A)), foot(P, new Line(A, B)));
  }

  public static Line simson(Point A, Point B, Point C, Point P) // returns the Simson line of a point P on the circumcircle of ABC
  {
    if (!P.on(circumcircle(A, B, C)))
      throw new IllegalArgumentException("point must be on circumcircle");
    return new Line(foot(P, new Line(A, B)), foot(P, new Line(A, C)));
  }

  public static Point extension(Point P1, Point P2, Point Q1, Point Q2) // returns the intersection of lines P1P2 and Q1Q2
  {
    if (P1.equals(P2) || Q1.equals(Q2))
      throw new IllegalArgumentException("points cannot be the same");
    return new Point(new Line(P1, P2), new Line(Q1, Q2));
  }

  public static boolean coaxial(Circle c1, Circle c2, Circle c3) // returns whether c1, c2, and c3 are coaxial, i.e. have two common points
  {
    if (c1.equals(c2) || c2.equals(c3) || c3.equals(c1))
      return true;
    return (radAxis(c1, c2).equals(radAxis(c1, c3)));
  }

  public static boolean conc(Circle c1, Circle c2, Circle c3) // returns whether c1, c2, and c3 are concurrent, i.e. have a common point
  {
    if (c1.equals(c2) || c2.equals(c3) || c3.equals(c1))
      return true;
    return (radCenter(c1, c2, c3).on(c1));
  }

  public static Circle centered(Point P, Point Q) // returns the circle centered at P through Q
  {
    Point A = new Point("1", "0", "0");
    Point B = new Point("0", "1", "0");
    Point C = new Point("0", "0", "1");
    if (P.equals(Q))
      throw new IllegalArgumentException("points cannot be the same");
    if (P.equals(A))
      return new Circle(Q, Q.reflectOver(new Line(B, P)), Q.reflectOver(new Line(C, P)));
    else if (P.equals(B))
      return new Circle(Q, Q.reflectOver(new Line(A, P)), Q.reflectOver(new Line(C, P)));
    else
      return new Circle(Q, Q.reflectOver(new Line(A, P)), Q.reflectOver(new Line(B, P)));
  }

  public static Circle diameter(Point P1, Point P2) // returns the circle with diameter P1P2
  {
    if (P1.equals(P2))
      throw new IllegalArgumentException("points cannot be the same");
    Point A = new Point("1", "0", "0");
    Point B = new Point("0", "1", "0");
    Point C = new Point("0", "0", "1");
    if (new Line(P1, P2).parallel(new Line(A, B)) || new Line(P1, P2).perp(new Line(A, B)))
      return new Circle(P1, P2, extension(P1, infPoint(new Line(A, B)), P2, perpInfPoint(new Line(A, B))));
    else
      return new Circle(P1, P2, extension(P1, infPoint(new Line(A, C)), P2, perpInfPoint(new Line(A, C))));
  }
}
