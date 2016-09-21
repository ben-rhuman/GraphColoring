/**
 * 17 September 2016
 *
 * @author benrhuman Backtracking algorithm with forward checking
 */
public class ForwardChecking {

    private int[][] graph;
    private Point[] point;
    private int numColors;
    private int[][] domain;

    public ForwardChecking(int[][] graph, Point[] point, int numColors) {
        this.graph = graph;
        this.point = point;
        this.numColors = numColors;
        domain = new int[point.length][numColors];
        for (int i = 0; i < point.length; i++) {
            for (int j = 0; j < numColors; j++) {
                domain[i][j] = j + 1;
            }
        }
        boolean success = BTForwardCheck(0);
        if(success){
            System.out.println("Graph colored");
            for(int i = 0; i < point.length; i++){
                System.out.println("Point " + i + ": " + point[i].color + ", ");
            }
        }else{
            System.out.println("Failed to Color");
        }
    }

    public boolean BTForwardCheck(int current) { //Recursively traverse graph selecting node colors
        if (current == point.length) {
            return true; //Successfully finished coloring graph.
        }
        for (int i = 0; i < domain[current].length; i++) {
            if (domain[current][i] != 0) {
//                int c = domain[current][i];
//                domain[current][i] = 0;
                if (colorable(current, domain[current][i])) {
                    point[current].color = domain[current][i];

                    for (int j = 0; j < graph.length; j++) { //Searching through all connecting nodes
                        if (graph[current][j] == 1) { //If connection
                            domain[current][point[current].color - 1] = 0;  // Remove the selected color from the nodes domain.
                        }
                    }

                    if (!lookForward(current)) { //check if any domains were wiped out.
                        restoreDomain(current);
                    } else {
                        if (BTForwardCheck(current + 1)) {
                            return true;
                        } else{
                            restoreDomain(current);
                        }
                    }
                }

            }
        }
        return false;
    }

    public boolean lookForward(int current) { // Checks to see if any nodes have been reduced to a empty domain
        for (int i = 0; i < graph.length; i++) { //Searching through all connecting nodes
            if (graph[current][i] == 1) { //If connection
                if (emptyDomain(i)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void restoreDomain(int current) {
        for (int j = 0; j < graph.length; j++) { //Searching through all connecting nodes
            if (graph[current][j] == 1) { //If connection
                domain[current][point[current].color - 1] = point[current].color;  //reverse the previous domain deletion
            }
        }
        point[current].color = 0;
    }

    public boolean emptyDomain(int num) {
        for (int j = 0; j < domain[num].length; j++) {
            if (domain[num][j] != 0) {
                return false;
            }
        }
        return true;
    }

    private boolean colorable(int p, int c) { //p is current point position in graph, c is current testing color.
        for (int i = 0; i < graph.length; i++) { //Searching through all connecting nodes
            if (graph[p][i] == 1) { //If connection
                if (point[i].color == c) { //If a connecting point has the same color as the one selected for point p return false
                    return false; // Can't color with selceted color
                }
            }
        }
        return true; //No connecting nodes with some same color found, p is colorable
    }
}
