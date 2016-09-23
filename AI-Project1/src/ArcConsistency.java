
/**
 * 17 September 2016
 *
 * @author benrhuman Backtracking algorithm with forward checking
 */
public class ArcConsistency {

    private int[][] graph;
    private Point[] point;
    private int numColors;
    private int[][] domain;
    private int decisions;

    public ArcConsistency(int[][] graph, Point[] point, int numColors) {
        this.graph = graph;
        this.point = point;
        this.numColors = numColors;

        domain = new int[point.length][numColors]; //set up the domain of all nodes
        for (int i = 0; i < point.length; i++) {
            for (int j = 0; j < numColors; j++) {
                domain[i][j] = j + 1;
            }
        }

        boolean success = BTArcConsistency(0);
        if (success) {
            System.out.println("Graph colored");
             System.out.println("\n"+"decision number:" + decisions + "\n" );
            for (int i = 0; i < point.length; i++) {
                System.out.println("Point " + i + ": " + point[i].color + ", ");
            }
        } else {
            System.out.println("Failed to Color");
             System.out.println("\n"+"decision number:" + decisions + "\n" );
        }
    }

    public boolean BTArcConsistency(int current) {
        if (current == point.length) {
            return true; //Successfully finished coloring graph.
        }
        for (int i = 0; i < domain[current].length; i++) {
            if (domain[current][i] != 0) {
                if (colorable(current, domain[current][i])) {
                    point[current].color = domain[current][i];
                    decisions++;

                    updateSingleDomain(current); //update the domain of only the current node

                    if (updateDomains(current) == false) { //if one of the domains become empty 
                        point[current].color = 0; //reset color 
                        restoreSingleDomain(current); //reset domain
                        return false;
                    }

                    if (BTArcConsistency(current + 1)) {
                        return true;
                    }
                    point[current].color = 0; //reset color if the next coloring failed
                    restoreSingleDomain(current); //reset domain
                }
            }
        }

        return false;
    }

    public void restoreSingleDomain(int current) { //put back the entire domain
        for (int i = 0; i < domain[current].length; i++) {
            domain[current][i] = i + 1;
        }
    }

    public void updateSingleDomain(int current) { //wipe out the domain except for the chosen color
        for (int i = 0; i < domain[current].length; i++) {
            if (domain[current][i] != point[current].color - 1) {
                domain[current][i] = 0;
            }
        }
    }

    public boolean revised(int current, int adjacent) { //search for matching colors in the domains

        for (int i = 0; i < domain[current].length; i++) { // searching through the domain of current node

            for (int j = 0; j < domain[adjacent].length; j++) { //compare it to domain of the adjacent node

                if (domain[current][i] != 0 && domain[adjacent][j] != 0 && domain[current][i] == domain[adjacent][j]) { //if there is a value in the domains that are inconsistent, and not 0
                    domain[adjacent][j] = 0;

                    if (emptyDomain(adjacent)) { //if the domain has been emptied
                        domain[adjacent][j] = domain[current][i]; //revert the domain to its previous state
                        return false;
                    }
                    updateDomains(adjacent); //update the domain if there was a change

                }
            }
        }
        return true;
    }

    public boolean updateDomains(int current) { //update all adjacent domains
        for (int j = 0; j < graph.length; j++) { //Searching through all connecting nodes
            if (graph[current][j] == 1) { //If connection
                if (revised(current, j)) { //if the revision did not cause an empty domain
                    return true;
                }

            }
        }
        return false;
    }

    public boolean emptyDomain(int num) {
        decisions++;
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
