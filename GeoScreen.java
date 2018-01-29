// displays the geometry diagram in a JPanel

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GeoScreen extends JPanel implements MouseListener
{
  private int ax = 250, ay = 325, bx = 200, by = 550, cx = 500, cy = 550; // stores coordinates of the triangles
  private Set<Point> points = new HashSet<Point>(); // stores points in the diagram
  private Set<Line> lines = new HashSet<Line>(); // stores lines in the diagram
  private Set<Circle> circles = new HashSet<Circle>(); // stores circles in the diagram
  private static Point A = new Point("1", "0", "0");
  private static Point B = new Point("0", "1", "0");
  private static Point C = new Point("0", "0", "1");
  private static Line a = new Line("1", "0", "0");
  private static Line b = new Line("0", "1", "0");
  private static Line c = new Line("0", "0", "1");
  private static int width = 800;
  private static int height = 800;

  public GeoScreen(int width, int height)
  {
    points.add(A);
    points.add(B);
    points.add(C);
    lines.add(a);
    lines.add(b);
    lines.add(c);
    addMouseListener(this);
  }

  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    for (Point P : points)
      P.draw(g, ax, ay, bx, by, cx, cy);
    for (Line l : lines)
      l.draw(g, ax, ay, bx, by, cx, cy, width, height);
    for (Circle c : circles)
      c.draw(g, ax, ay, bx, by, cx, cy);
  }

  public void add(Point P)
  {
    points.add(P);
  }

  public void add(Line l)
  {
    lines.add(l);
  }

  public void add(Circle c)
  {
    circles.add(c);
  }

  public static void main(String[] args)
  {
    JFrame window = new JFrame("mren.Geogebary!");
    window.setBounds(0, 0, width, height);
    GeoScreen panel = new GeoScreen(width, height);
    Point D = Geometry.midpoint(B, C);
    Point E = Geometry.midpoint(C, A);
    Point F = Geometry.midpoint(A, B);
    Point G = Geometry.centroid(A, B, C);
    Point O = Geometry.circumcenter(A, B, C);
    Point H = Geometry.orthocenter(A, B, C);
    Point X = Geometry.foot(A, B, C);
    Point Y = Geometry.foot(B, C, A);
    Point Z = Geometry.foot(C, A, B);
    Point Ha = Geometry.midpoint(A, H);
    Point Hb = Geometry.midpoint(B, H);
    Point Hc = Geometry.midpoint(C, H);
    Point A1 = A.reflectOver(O);
    Point B1 = B.reflectOver(O);
    Point C1 = C.reflectOver(O);
    Line a1 = new Line(A, O);
    Line b1 = new Line(B, O);
    Line c1 = new Line(C, O);
    Line d1 = new Line(H, D);
    Line e1 = new Line(H, E);
    Line f1 = new Line(H, F);
    Line d = new Line(A, D);
    Line e = new Line(B, E);
    Line f = new Line(C, F);
    Line x = new Line(A, X);
    Line y = new Line(B, Y);
    Line z = new Line(C, Z);
    Line p = new Line(O, D);
    Line q = new Line(O, E);
    Line r = new Line(O, F);
    Line euler = new Line(O, H);
    Circle cir = new Circle(A, B, C);
    Circle nine = new Circle(D, E, F);
    panel.add(D);
    panel.add(E);
    panel.add(F);
    panel.add(G);
    panel.add(H);
    panel.add(O);
    panel.add(H);
    panel.add(X);
    panel.add(Y);
    panel.add(Z);
    panel.add(Ha);
    panel.add(Hb);
    panel.add(Hc);
    panel.add(A1);
    panel.add(B1);
    panel.add(C1);
    panel.add(d);
    panel.add(e);
    panel.add(f);
    panel.add(x);
    panel.add(y);
    panel.add(z);
    panel.add(p);
    panel.add(q);
    panel.add(r);
    panel.add(euler);
    panel.add(a1);
    panel.add(b1);
    panel.add(c1);
    panel.add(d1);
    panel.add(e1);
    panel.add(f1);
    panel.add(cir);
    panel.add(nine);
    panel.setBackground(Color.WHITE);

    Container c = window.getContentPane();
    c.add(panel);

    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setVisible(true);
  }

  public void mouseExited(MouseEvent e)
  {

  }

  public void mouseEntered(MouseEvent e)
  {

  }

  public void mouseReleased(MouseEvent e)
  {

  }

  public void mousePressed(MouseEvent e)
  {

  }

  public void mouseClicked(MouseEvent e)
  {
    int x = e.getX();
    int y = e.getY();
  }
}
