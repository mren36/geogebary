// displays the geometry diagram in a JPanel

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GeoScreen extends JPanel implements MouseListener
{
  static final long serialVersionUID = 42L;
  private int ax = 400, ay = 100, bx = 275, by = 400, cx = 625, cy = 400; // stores coordinates of the triangles
  private Set<screenPoint> points = new HashSet<screenPoint>(); // stores points in the diagram
  private Set<screenLine> lines = new HashSet<screenLine>(); // stores lines in the diagram
  private Set<screenCircle> circles = new HashSet<screenCircle>(); // stores circles in the diagram
  private static Point A = new Point("1", "0", "0");
  private static Point B = new Point("0", "1", "0");
  private static Point C = new Point("0", "0", "1");
  private static Line a = new Line("1", "0", "0");
  private static Line b = new Line("0", "1", "0");
  private static Line c = new Line("0", "0", "1");
  private static int width = 1500;
  private static int height = 1500;
  private int toolState = -1;
  private int pointState = 0;
  private int lineState = 0;
  private int circleState = 0;
  private ArrayList<Point> selectedPoints = new ArrayList<Point>();
  private ArrayList<Line> selectedLines = new ArrayList<Line>();
  private ArrayList<Circle> selectedCircles = new ArrayList<Circle>();
  private ArrayList<tool> tools = new ArrayList<tool>();

  public GeoScreen(int width, int height)
  {
    points.add(new screenPoint(A));
    points.add(new screenPoint(B));
    points.add(new screenPoint(C));
    points.add(new screenPoint(new Point("a", "b", "c")));
    lines.add(new screenLine(a));
    lines.add(new screenLine(b));
    lines.add(new screenLine(c));
    circles.add(new screenCircle(new Circle(A, B, C)));
    addMouseListener(this);
    for (int i = 0; i < 19; i++)
      tools.add(new tool(i));
  }

  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    g.setColor(Color.BLACK);

    for (screenPoint sP : points)
      if (!selectedPoints.contains(sP.P))
        g.fillOval(sP.coords[0] - 3, sP.coords[1] - 3, 6, 6);

    for (screenLine sl : lines)
      if (!selectedPoints.contains(sl.l))
        g.drawLine(sl.coords[0], sl.coords[1], sl.coords[2], sl.coords[3]);

    for (screenCircle sc : circles)
      if (!selectedPoints.contains(sc.c))
        g.drawOval(sc.coords[0] - sc.coords[2], sc.coords[1] - sc.coords[2], 2 * sc.coords[2], 2 * sc.coords[2]);

    g.setColor(Color.RED);

    for (screenPoint sP : points)
      if (selectedPoints.contains(sP.P))
        g.fillOval(sP.coords[0] - 3, sP.coords[1] - 3, 6, 6);

    for (screenLine sl : lines)
      if (selectedLines.contains(sl.l))
        g.drawLine(sl.coords[0], sl.coords[1], sl.coords[2], sl.coords[3]);

    for (screenCircle sc : circles)
      if (selectedCircles.contains(sc.c))
        g.drawOval(sc.coords[0] - sc.coords[2], sc.coords[1] - sc.coords[2], 2 * sc.coords[2], 2 * sc.coords[2]);

    g.setColor(Color.BLUE);
    g.fillRect(0, 0, 90, 30);
    g.setColor(Color.GREEN);
    g.setFont(new Font("TimesRoman", Font.BOLD, 20));
    g.drawString("ENTER", 10, 20);

    for (int i = 0; i < tools.size(); i++)
    {
      g.setColor(Color.WHITE);
      g.fillRect(0, tools.get(i).boxTop, 90, tools.get(i).boxTop + 30);

      g.setColor(Color.BLACK);
      if (toolState == i)
        g.setColor(Color.RED);

      g.drawRect(0, tools.get(i).boxTop, 90, tools.get(i).boxBottom - tools.get(i).boxTop);
      g.drawString(i + "", 40, (tools.get(i).boxBottom + tools.get(i).boxTop) / 2 + 5);
    }
  }

  public void reset()
  {
    selectedPoints = new ArrayList<Point>();
    selectedLines = new ArrayList<Line>();
    selectedCircles = new ArrayList<Circle>();
  }

  public void add(Point P)
  {
    points.add(new screenPoint(P));
  }

  public void add(Line l)
  {
    lines.add(new screenLine(l));
  }

  public void add(Circle c)
  {
    circles.add(new screenCircle(c));
  }

  public void remove(Point P)
  {
    points.remove(new screenPoint(P));
  }

  public void remove(Line l)
  {
    lines.remove(new screenLine(l));
  }

  public void remove(Circle c)
  {
    circles.remove(new screenCircle(c));
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

    if (x <= 90)
    {
      if (y <= 30)
      {
        if (toolState < 0)
          return;

        if (tools.get(toolState).numPoint == selectedPoints.size() && tools.get(toolState).numLine == selectedLines.size() && tools.get(toolState).numCircle == selectedCircles.size())
        {
          if (toolState == 0)
            add(new Line(selectedPoints.get(0), selectedPoints.get(1)));
          if (toolState == 1)
            add(new Point(selectedLines.get(0), selectedLines.get(1)));
          if (toolState == 2)
            add(new Circle(selectedPoints.get(0), selectedPoints.get(1), selectedPoints.get(2)));
          if (toolState == 3)
            add(Geometry.midpoint(selectedPoints.get(0), selectedPoints.get(1)));
          if (toolState == 4)
            add(Geometry.reflect(selectedPoints.get(0), selectedPoints.get(1)));
          if (toolState == 5)
            add(Geometry.perp(selectedPoints.get(0), selectedLines.get(0)));
          if (toolState == 6)
            add(Geometry.parallel(selectedPoints.get(0), selectedLines.get(0)));
          if (toolState == 7)
            add(Geometry.radAxis(selectedCircles.get(0), selectedCircles.get(1)));
          if (toolState == 8)
            add(selectedCircles.get(0).center());
          if (toolState == 9)
            add(Geometry.secondInt(selectedLines.get(0), selectedCircles.get(0), selectedPoints.get(0)));
          if (toolState == 10)
            add(Geometry.secondInt(selectedCircles.get(0), selectedCircles.get(1), selectedPoints.get(0)));
          if (toolState == 11)
            add(Geometry.tangentLine(selectedPoints.get(0), selectedCircles.get(0)));
          if (toolState == 15)
            remove(selectedPoints.get(0));
          if (toolState == 16)
            remove(selectedLines.get(0));
          if (toolState == 17)
            remove(selectedCircles.get(0));
          if (toolState == 18)
            add(new Circle(selectedPoints.get(0), selectedPoints.get(1)));
          reset();
          repaint();
        }
        return;
      }
      else
      {
        for (int i = 0; i < tools.size(); i++)
          if (tools.get(i).boxTop <= y && y <= tools.get(i).boxBottom)
          {
            toolState = i;
            reset();
            repaint();
          }
        return;
      }
    }

    if (toolState < 0)
      return;

    Point P = clickPoint(x, y);
    Line l = clickLine(x, y);
    Circle c = clickCircle(x, y);

    if (P != null)
      tools.get(toolState).click(P);
    else if (l != null)
      tools.get(toolState).click(l);
    else if (c != null)
      tools.get(toolState).click(c);

    repaint();
  }

  public Point clickPoint(int x, int y)
  {
    for (screenPoint sP : points)
      if (sP.click(x, y))
        return sP.P;
    return null;
  }

  public Line clickLine(int x, int y)
  {
    for (screenLine sl : lines)
      if (sl.click(x, y))
        return sl.l;
    return null;
  }

  public Circle clickCircle(int x, int y)
  {
    for (screenCircle sc : circles)
      if (sc.click(x, y))
        return sc.c;
    return null;
  }

  protected class screenPoint
  {
    private Point P;
    private int[] coords;

    public screenPoint(Point P)
    {
      this.P = P;
      coords = P.screenCoords(ax, ay, bx, by, cx, cy);
    }

    public boolean click(int x, int y)
    {
      return (x - coords[0]) * (x - coords[0]) + (y - coords[1]) * (y - coords[1]) < 20;
    }

    public boolean equals(screenPoint other)
    {
      return P.equals(other.P);
    }

    public int hashCode()
    {
      return P.hashCode();
    }
  }

  protected class screenLine
  {
    private Line l;
    private int[] coords;

    public screenLine(Line l)
    {
      this.l = l;
      coords = l.screenCoords(ax, ay, bx, by, cx, cy, width, height);
    }

    public boolean click(int x, int y)
    {
      int d = (coords[0] - coords[2]) * (coords[0] - coords[2]) + (coords[1] - coords[3]) * (coords[1] - coords[3]);
      int d1 = (x - coords[0]) * (x - coords[0]) + (y - coords[1]) * (y - coords[1]);
      int d2 = (x - coords[2]) * (x - coords[2]) + (y - coords[3]) * (y - coords[3]);
      return Math.sqrt(d1) + Math.sqrt(d2) < Math.sqrt(d) + 1;
    }

    public boolean equals(screenLine other)
    {
      return l.equals(other.l);
    }

    public int hashCode()
    {
      return l.hashCode();
    }
  }

  protected class screenCircle
  {
    private Circle c;
    private int[] coords;

    public screenCircle(Circle c)
    {
      this.c = c;
      coords = c.screenCoords(ax, ay, bx, by, cx, cy);
    }

    public boolean click(int x, int y)
    {
      int d2 = (x - coords[0]) * (x - coords[0]) + (y - coords[1]) * (y - coords[1]);
      return (coords[2] - 3) * (coords[2] - 3) <= d2 && d2 <= (coords[2] + 3) * (coords[2] + 3);
    }

    public boolean equals(screenCircle other)
    {
      return c.equals(other.c);
    }

    public int hashCode()
    {
      return c.hashCode();
    }
  }

  protected class tool
  {
    private int id;
    private int numPoint;
    private int numLine;
    private int numCircle;
    private int boxTop;
    private int boxBottom;

    public tool(int i)
    {
      id = i;
      boxTop = 30 + 30 * i;
      boxBottom = 59 + 30 * i;

      if (i == 0) // add the line through two points
      {
        numPoint = 2;
        numLine = 0;
        numCircle = 0;
      }

      if (i == 1) // add the intersection of two lines
      {
        numPoint = 0;
        numLine = 2;
        numCircle = 0;
      }

      if (i == 2) // add the circle through three points
      {
        numPoint = 3;
        numLine = 0;
        numCircle = 0;
      }

      if (i == 3) // add the midpoint of two points
      {
        numPoint = 2;
        numLine = 0;
        numCircle = 0;
      }

      if (i == 4) // reflect the first point over the second points
      {
        numPoint = 2;
        numLine = 0;
        numCircle = 0;
      }

      if (i == 5) // add the perpendicular to a line through a point
      {
        numPoint = 1;
        numLine = 1;
        numCircle = 0;
      }

      if (i == 6) // add the parallel to a line through a point
      {
        numPoint = 1;
        numLine = 1;
        numCircle = 0;
      }

      if (i == 7) // add the radical axis of two circles
      {
        numPoint = 0;
        numLine = 0;
        numCircle = 2;
      }

      if (i == 8) // add the center of a circle
      {
        numPoint = 0;
        numLine = 0;
        numCircle = 1;
      }

      if (i == 9) // second intersection of a line and a circle given a common point
      {
        numPoint = 1;
        numLine = 1;
        numCircle = 1;
      }

      if (i == 10) // second intersection of two circles given a common point
      {
        numPoint = 1;
        numLine = 0;
        numCircle = 2;
      }

      if (i == 11) // draws tangent to a circle through a point on it
      {
        numPoint = 1;
        numLine = 0;
        numCircle = 1;
      }

      if (i == 12) // check if a point is on a line
      {
        numPoint = 1;
        numLine = 1;
        numCircle = 0;
      }

      if (i == 13) // check if a point is on a circle
      {
        numPoint = 1;
        numLine = 0;
        numCircle = 1;
      }

      if (i == 14) // check if a line is tangent to a circle
      {
        numPoint = 0;
        numLine = 1;
        numCircle = 1;
      }

      if (i == 15) // delete a point
      {
        numPoint = 1;
        numLine = 0;
        numCircle = 0;
      }

      if (i == 16) // delete a line
      {
        numPoint = 0;
        numLine = 1;
        numCircle = 0;
      }

      if (i == 17) // delete a circle
      {
        numPoint = 0;
        numLine = 0;
        numCircle = 1;
      }

      if (i == 18) // circle with diameter given by the two points
      {
        numPoint = 2;
        numLine = 0;
        numCircle = 0;
      }
    }

    public void click(Point P)
    {
      if (selectedPoints.contains(P))
        selectedPoints.remove(selectedPoints.indexOf(P));
      else if (selectedPoints.size() < numPoint)
        selectedPoints.add(P);
    }

    public void click(Line l)
    {
      if (selectedLines.contains(l))
        selectedLines.remove(selectedLines.indexOf(l));
      else if (selectedLines.size() < numLine)
        selectedLines.add(l);
    }

    public void click(Circle c)
    {
      if (selectedCircles.contains(c))
        selectedCircles.remove(selectedCircles.indexOf(c));
      else if (selectedCircles.size() < numCircle)
        selectedCircles.add(c);
    }
  }

  public static void main(String[] args)
  {
    JFrame window = new JFrame("Geogebary");
    window.setBounds(0, 0, 800, 800);
    GeoScreen panel = new GeoScreen(800, 800);
    panel.setBackground(Color.WHITE);

    Container c = window.getContentPane();
    c.add(panel);

    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setVisible(true);
  }
}
