
import java.util.PriorityQueue;
import java.util.Random;
import java.awt.geom.Line2D;
import java.util.Comparator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Stack;

public class AIProject1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("CSCI 446 Project 1 Graph Coloring Testing Results:");

        for (int numColors = 4; numColors > 3; numColors--) {
            System.out.println("Coloring with " + numColors + " colors.");
            for (int graphSize = 10; graphSize < 71; graphSize = graphSize + 10) {
                System.out.println("Number of Nodes in Graph: " + graphSize + "\n");
                GraphGenerator g = new GraphGenerator(graphSize);

//                MinConflicts mc = new MinConflicts(g.pointArray, g.graphAM, numColors);
//                resetPoints(g.pointArray);
                SimpleBacktracking b = new SimpleBacktracking(g.graphAM, g.pointArray, numColors);
//                resetPoints(g.pointArray);

//                ForwardChecking fc = new ForwardChecking(g.graphAM, g.pointArray, numColors);
                System.out.println("---------------------------------------------------------------------");
            }
        }
//        int graphSize = 30;
//        int numColors = 4;
//        GraphGenerator g = new GraphGenerator(graphSize);
//        SimpleBacktracking b = new SimpleBacktracking(g.graphAM, g.pointArray, numColors);
//        for (int i = 0; i < g.pointArray.length; i++) {
//            g.pointArray[i].color = 0;
//        }
//        ForwardChecking fc = new ForwardChecking(g.graphAM, g.pointArray, numColors);
//        MinConflicts mc = new MinConflicts(g.pointArray, g.graphAM, numColors);
        // TODO code application logic here
    }

    private static void resetPoints(Point[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i].color = 0;
        }
    }

}//End Main

//--------------------------------------------------------------
class GraphGenerator {

    //Variables
    private int numPoints;
    public Point[] pointArray;
    public int[][] graphAM;  //In retrospect this is a poor/slow way to keep track of connections. 

    public GraphGenerator(int n) {
        numPoints = n;
        pointArray = new Point[n];
        graphAM = new int[n][n];

        randomPoints();

        populateQueues();

        populateGraph();

//         Point a = new Point(0.02707113059452626,0.9671230434505473,4);
//         Point b = new Point(0.38437959656722354,0.027193847055366205,4);
//         Point c = new Point(0.14999844517548033,0.31780590530446806,4);
//         Point d = new Point(0.38437959656722354,0.027193847055366205,4);
//            Point a = new Point(0.8133917953434713,0.18436621569849176,4);
//            Point b = new Point(0.3124566274656816,0.6363805057973766,6);
//            Point c = new Point(0.16509109363644414,0.7129002547046351,4);
//            Point d = new Point(0.8133917953434713,0.18436621569849176,4);
//        Point a = new Point(0.0,0.0,4);
//        Point b = new Point(0.0,4.0,4);
//        Point c = new Point(0.0,4.0,4);
//        Point d = new Point(0.0,8.0,4);
        //System.out.println("\n"+ checkIntersect(a,b,c,d));
        //System.out.println("\n");
//              
        //printAM();  //Used to test the correctness of the graph generator
    }

    private void randomPoints() {

        boolean copy;

        for (int i = 0; i < numPoints; i++) {
            double x = Math.random();
            double y = Math.random();
            copy = false;

            for (int j = 0; j < numPoints; j++) {
                if (pointArray[j] != null) {
                    if (x == pointArray[j].x && y == pointArray[j].y) {
                        i--;
                        copy = true;
                        continue;
                    }
                }
            }
            if (copy == false) {

                Point p = new Point(x, y, numPoints);
                pointArray[i] = p;
                p.position = i;

                //p.print(); // Also used to test correctness of the graph
                //System.out.println("\n");
            }

        }
    }

    public void populateQueues() {
        for (int i = 0; i < pointArray.length; i++) {
            for (int j = 0; j < pointArray.length; j++) {
                if (i != j) {
                    PriorityNode n = new PriorityNode(pointArray[j], calcDistance(pointArray[i], pointArray[j]));
                    pointArray[i].queue.add(n);
                }
            }
        }
    }

    public double calcDistance(Point p1, Point p2) {
        return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
    }

    public void populateGraph() {
        boolean done = false;

        while (done == false) {
            int counter = 0;

            Point w = getRandPoint();
            Point z = w.queue.remove().point;
            calcConnection(w, z);

            for (int i = 0; i < pointArray.length; i++) {
                if (pointArray[i].queue.size() == 0) {
                    counter++;
                }
            }
            if (counter == numPoints) {
                done = true;
            }
        }
    }

    public Point getRandPoint() {

        int rnd = new Random().nextInt(pointArray.length);
        if (pointArray[rnd].queue.size() == 0) {
            return getRandPoint();
        }

        return pointArray[rnd];
    }

    public void calcConnection(Point a, Point p) {
        boolean conflict = false;

        for (int i = 0; i < numPoints; i++) {

            for (int j = 0; j < numPoints; j++) {

                if (graphAM[i][j] == 1) {

                    conflict = checkIntersect(a, p, pointArray[i], pointArray[j]);

                    if (conflict == true) {
                        break;
                    }
                }
            }
            if (conflict == true) {
                break;
            }
        }

        if (conflict == false) {

            graphAM[a.position][p.position] = 1;

            graphAM[p.position][a.position] = 1;

        }
    }

    public boolean checkIntersect2(Point x, Point y, Point a, Point b) {
        boolean intersect = Line2D.linesIntersect(x.x, x.y, y.x, y.y, a.x, a.y, b.x, b.y);
        return intersect;
    	//Line2D line1 = new Line2D.Double(x.x, x.y, y.x, y.y);
        //Line2D line2 = new Line2D.Double(a.x, a.y, b.x, b.y);

        //boolean result = line1.intersectsLine(line2);
        //return result;
    }

    public boolean checkIntersect(Point a, Point b, Point c, Point d) {
        BigDecimal b1, b2, x, m1, m2, ax, ay, bx, by, cx, cy, dx, dy;

        ax = BigDecimal.valueOf(a.x);
        //ax = ax.setScale(16,BigDecimal.ROUND_DOWN);
        ay = BigDecimal.valueOf(a.y);
        //ay = ay.setScale(16,BigDecimal.ROUND_DOWN);
        bx = BigDecimal.valueOf(b.x);
        //bx = bx.setScale(16,BigDecimal.ROUND_DOWN);
        by = BigDecimal.valueOf(b.y);
        //by = by.setScale(16,BigDecimal.ROUND_DOWN);
        cx = BigDecimal.valueOf(c.x);
        //cx = cx.setScale(16,BigDecimal.ROUND_DOWN);
        cy = BigDecimal.valueOf(c.y);
        //cy = cy.setScale(16,BigDecimal.ROUND_DOWN);
        dx = BigDecimal.valueOf(d.x);
        //dx = dx.setScale(16,BigDecimal.ROUND_DOWN);
        dy = BigDecimal.valueOf(d.y);

        if (((a.x == b.x) && (a.y == b.y)) || ((c.x == d.x) && (c.y == d.y))) { //check if points are the same
            System.out.println("this method compares lines not points");
            return true;
        }

        if (checkIfSameLine(a, b, c, d)) {
            return true;
        }

        //check if a single point from each line is the same
        if (((ax.compareTo(cx) == 0) && (ay.compareTo(cy) == 0)) || ((ax.compareTo(dx) == 0) && (ay.compareTo(dy) == 0)) || ((bx.compareTo(cx) == 0) && (by.compareTo(cy) == 0)) || ((bx.compareTo(dx) == 0) && (by.compareTo(dy) == 0))) {
            return false;
        }

        if ((a.x == b.x) || (c.x == d.x)) { //checking for vertical lines

            if ((a.x == b.x) && (c.x == d.x)) { //if both are vertical lines
                if (a.x != c.x) { //if both vertical lines have different x
                    return false;
                } else {
                    return (((c.y < b.y) && (c.y > a.y)) || ((c.y > b.y) && (c.y < a.y)) || ((d.y < b.y) && (d.y > a.y)) || ((d.y > b.y) && (d.y < a.y))); //if they overlap

                }
            } else { //one is vertical
                if (a.x == b.x) {
                    //get equation of line for other line

                    //m2= (c.y-d.y)/(c.x-d.x); //slope
                    m2 = cy.subtract(dy).divide(cx.subtract(dx), BigDecimal.ROUND_DOWN);
                    //b2 = c.y - m2*c.x; //y intercept
                    b2 = cy.subtract(m2.multiply(cx));

                    if (m2.signum() == 0) {//if other is horizontal
                        //x = a.x;
                        x = ax;

                    } else {

                        //x = (a.x-b2)/m2; //possible intersection point
                        x = ax.subtract(b2).divide(m2, BigDecimal.ROUND_DOWN);
                    }

                    //return ((min(a.y,b.y) < b2) && (b2 < max(a.y,b.y))) && ((min(c.x,d.x) < x) && (x < max(c.x,d.x))); //check if x is in both lines
                    return (((min(ay, by).compareTo(b2) == -1) && (max(ay, by).compareTo(b2) == 1)) && ((min(cx, dx).compareTo(x) == -1) && (max(cx, dx).compareTo(x) == 1)));

                } else { //if other line is vertical
                    //get equation of line for other line
                    //m1= (a.y-b.y)/(a.x-b.x);
                    m1 = ay.subtract(by).divide(ax.subtract(bx), BigDecimal.ROUND_DOWN);
                    //b1 = a.y - m1*a.x;
                    b1 = ay.subtract(m1.multiply(ax));

                    if (m1.signum() == 0) {//if other is horizontal
                        x = cx;

                    } else {

                        //x = (c.x-b1)/m1; //possible intersection point
                        x = cx.subtract(b1).divide(m1, BigDecimal.ROUND_DOWN);
                    }

                    //return ((min(a.x,b.x) < x) && (x < max(a.x,b.x))) && ((min(c.y,d.y) < b1) && (b1 < max(c.y,d.y))); //check if x is in both lines
                    return (((min(ax, bx).compareTo(x) == -1) && (max(ax, bx).compareTo(x) == 1)) && ((min(cy, dy).compareTo(b1) == -1) && (max(cy, dy).compareTo(b1) == 1)));

                }

            }

        } //end check for vertical lines

        //m1= (a.y-b.y)/(a.x-b.x); //slope for line 1
        m1 = ay.subtract(by).divide(ax.subtract(bx), BigDecimal.ROUND_DOWN);
        //m2= (c.y-d.y)/(c.x-d.x); //slope for line 2
        m2 = cy.subtract(dy).divide(cx.subtract(dx), BigDecimal.ROUND_DOWN);

        //b1 = a.y - m1*a.x; //y intercept for line 1
        b1 = ay.subtract(m1.multiply(ax));
        //b2 = c.y - m2*c.x; //y intercept for line 2
        b2 = cy.subtract(m2.multiply(cx));

        if ((m1 == m2)) { //check if lines are parallel

            if (b1 == b2) { //if lines have same y int.

                return (((c.x < b.x) && (c.x > a.x)) || ((c.x > b.x) && (c.x < a.x)) || ((d.x < b.x) && (d.x > a.x)) || ((d.x > b.x) && (d.x < a.x))); //if they overlap

            } else { //if lines are parallel but not intersecting
                return false;
            }
        }

        //x = (b2-b1)/(m1-m2); //possible intersection point
        x = b2.subtract(b1).divide(m1.subtract(m2), BigDecimal.ROUND_DOWN);

        //return (((min(a.x,b.x) < x) && (x < max(a.x,b.x))) && ((min(c.x,d.x) < x) && (x < max(c.x,d.x)))); //check if x is in both
        return (((min(ax, bx).compareTo(x) == -1) && (max(ax, bx).compareTo(x) == 1)) && ((min(cx, dx).compareTo(x) == -1) && (max(cx, dx).compareTo(x) == 1)));
    }

    public boolean checkIfSameLine(Point a, Point b, Point c, Point d) {
        if (((a.x == c.x) && (a.y == c.y)) && ((b.x == d.x) && (b.y == d.y))) {
            return true;
        }
        if (((b.x == c.x) && (b.y == c.y)) && ((a.x == d.x) && (a.y == d.y))) {
            return true;
        }
        return false;
    }

    public BigDecimal min(BigDecimal a, BigDecimal b) {
        if (a.compareTo(b) == -1) {
            return a;
        } else {
            return b;
        }
    }

    public BigDecimal max(BigDecimal a, BigDecimal b) {
        if (a.compareTo(b) == 1) {
            return a;
        } else {
            return b;
        }
    }

    public void printAM() {
        for (int i = 0; i < pointArray.length; i++) {
            for (int j = 0; j < pointArray.length; j++) {
                System.out.print(graphAM[j][i]); //swapped i/j to print normally
            }
            System.out.println();
        }
    }

}//End GraphGenerator

//--------------------------------------------------------------
class Point {

    public double x = 0.0;
    public double y = 0.0;
    public PriorityQueue<PriorityNode> queue;
    public int position;
    public int color; //0 = no color, 1 = R, 2 = G, 3 = B, 4 = Y

    public Point(double x, double y, int numPoints) {// Start Point, a Point consists of an X value and a Y value
        this.x = x;
        this.y = y;
        color = 0;
        Comparator<PriorityNode> comparator = new PointComparator();
        queue = new PriorityQueue<>(numPoints, comparator);
    }// End Point

    public void print() {// Start Print
        System.out.print("(" + x + "," + y + ")");
    }// End Print

}// End Point class

//--------------------------------------------------------------
class PriorityNode {

    public Point point;
    public double distance;

    public PriorityNode(Point p, double d) {
        point = p;
        distance = d;
    }

    public void print() {// Start Print
        System.out.print("(" + point.x + "," + point.y + ")  ::::  " + distance);
    }// End Print
}//End PriorityNode

//-------------------------------------------------------------
class PointComparator implements Comparator<PriorityNode> {

    @Override
    public int compare(PriorityNode n1, PriorityNode n2) {
        if (n1.distance < n2.distance) {
            return -1;
        }
        if (n1.distance > n2.distance) {
            return 1;
        }
        return 0;
    }
}
//-------------------------------------------------------

class Pair {

    public Point left;
    public Point right;
    public double distance;

    public Pair(Point left, Point right) {//A pair consists of two Points and the distance between them.
        this.left = left;
        this.right = right;
        if (right == null) {
            distance = Double.POSITIVE_INFINITY; //Returns positive infinity if there is only one point in a pair
        } else {
            distance = calcDist(left, right);
        }
    }//End of Pair constructor

    private double calcDist(Point pl, Point pr) {
        return Math.sqrt((pl.x - pr.x) * (pl.x - pr.x) + (pl.y - pr.y) * (pl.y - pr.y));
    }// End of calcDist
}//End pair class
