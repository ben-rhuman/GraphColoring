
/**
 * 17 September 2016
 *
 * @author benrhuman Backtracking algorithm with forward checking
 */
public class ArcConsistency {

    private final int[][] graph;
    private final Point[] point;
    private final int numColors;
    private final int[][] domain;
    private int decisions;
    private int pickingColor;//for printing
    private int domainEmpty; //for printing

    public ArcConsistency(int[][] graph, Point[] point, int numColors) {
        this.graph = graph;
        this.point = point;
        this.numColors = numColors;

        System.out.println("1. Setting up the domains of each node\n");
        domain = new int[point.length][numColors]; //set up the domain of all nodes
        for (int i = 0; i < point.length; i++) {
            for (int j = 0; j < numColors; j++) {
                domain[i][j] = j + 1;
            }
        }

        boolean success = BTArcConsistency(0);
        if (success) {
            System.out.println("Graph Colored");
            System.out.println("Decision number:" + decisions + "\n");

            System.out.println("Number of times a node was given a color: " + pickingColor + " ");
            System.out.println("Number of times a domain was found to be empty: " + domainEmpty + " ");
            for (int k = 0; k < point.length; k++) { //printing points and colors

                System.out.print("Point " + (k) + ": " + point[k].color + " \n");
                if (k == point.length - 1) {
                    System.out.println("\n");
                }
            }
        } else {

            System.out.println("Failed to Color");
            System.out.println("Decision number:" + decisions + "\n");

            System.out.println("Number of times a node was given a color: " + pickingColor + " ");
            System.out.println("Number of times a domain was found to be empty: " + domainEmpty + " ");
            for (int k = 0; k < point.length; k++) { //printing points and colors

                System.out.print("Point " + (k) + ": " + point[k].color + " \n");
                if (k == point.length - 1) {
                    System.out.println("\n");
                }
            }
        }
    }

    public boolean BTArcConsistency(int current) {

        if (current == point.length) { //if it has checked every node
            return true; //Successfully finished coloring graph.
        }
        for (int i = 0; i < domain[current].length; i++) { //selecting a color
            if (domain[current][i] != 0) { //check if the color is within the domain
                if (colorable(current, domain[current][i])) { //check if the color does not cause conflicts
                    point[current].color = domain[current][i];

                    decisions++;
                    pickingColor++;

                    if (i == 0 && (current == 0)) {//printing a step
                        System.out.println("Current domain of first node:");
                        for (int k = 0; k < domain[current].length; k++) { //printing domain
                            System.out.print(domain[current][k] + " ");
                        }
                        System.out.println("");
                        System.out.println("\n2. Give a node a color and update its domain.\n");

                    }

                    updateSingleDomain(current); //update the domain of only the current node

                    if (i == 0 && (current == 0)) {//printing a step
                        System.out.println("Current domain of first node:");
                        for (int k = 0; k < domain[current].length; k++) { //printing domain
                            System.out.print(domain[current][k] + " ");
                        }
                        System.out.println(" ");
                        

                    }

                    if (i == 0 && (current == 0)) {//printing a step
                        System.out.println("Current domain of adjacent nodes:");
                        for (int z = 0; z < graph.length; z++) { //Searching through all connecting nodes
                            if (graph[current][z] == 1) { //If connection 
                                System.out.println("");
                                System.out.print("node: " + point[z].position + "\n");
                                for (int k = 0; k < domain[z].length; k++) { //printing domain of an adjacent node
                                    System.out.print(domain[z][k] + " ");
                                }
                                System.out.println("");
                            }
                        }
                        System.out.println("\n3. Make sure the domains of its adjacent nodes are compliant.\n  3a. If that creates a change in a node's domain update its adjacent ones too. ");
                        System.out.println("");
                        System.out.println("4. Update the domains of its adjacent nodes.\n");

                    }
                    if (updateDomains(current) == false) { //if one of the domains become empty 
                        point[current].color = 0; //reset color 
                        restoreSingleDomain(current); //reset domain
                        return false;
                    }

                    if (i == 0 && (current == 0)) {//printing a step
                        System.out.println("Current domain of adjacent nodes:");
                        for (int z = 0; z < graph.length; z++) { //Searching through all connecting nodes
                            if (graph[current][z] == 1) { //If connection 
                                System.out.println("");
                                System.out.print("node: " + point[z].position + "\n");
                                for (int k = 0; k < domain[z].length; k++) { //printing domain of an adjacent node
                                    System.out.print(domain[z][k] + " ");
                                }
                                System.out.println("");
                            }
                        }

                        System.out.println("");

                    }
                    if (i == 0 && (current == 0)) {//printing a step
                        System.out.println("5. Repeat with the next node until a solution is found or a domain is emptied");
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
            if (domain[current][i] != point[current].color) { /////chaaaaaaaaaaaaaaaaaaaanged
                domain[current][i] = 0;
            }
        }
    }

    public boolean revised(int current, int adjacent) { //search for matching colors in the domains

        for (int i = 0; i < domain[current].length; i++) { // searching through the domain of current node

            // for (int j = 0; j < domain[adjacent].length; j++) { //compare it to domain of the adjacent node
// chaaaaaaaaannnnnged below and above
            if (domain[current][i] != 0 && domain[adjacent][i] != 0 && point[current].color == domain[adjacent][i]) { //if there is a value in the domains that are inconsistent, and not 0
                domain[adjacent][i] = 0;

                if (emptyDomain(adjacent)) { //if the domain has been emptied
                    domain[adjacent][i] = domain[current][i]; //revert the domain to its previous state
                    return false;
                }
                updateDomains(adjacent); //update the domain if there was a change

            }
            //}
        }
        return true;
    }

    public boolean updateDomains(int current) { //update all adjacent domains
        for (int j = 0; j < graph.length; j++) { //Searching through all connecting nodes
            if (graph[current][j] == 1) { //If connection
                if (revised(current, j) == false) { //if the revision did not cause an empty domain
                    return false; //////chaaaaaaaaaaaanged
                }

            }
        }
        return true;
    }

    public boolean emptyDomain(int num) {
        decisions++;
        for (int j = 0; j < domain[num].length; j++) {
            if (domain[num][j] != 0) {
                return false;
            }
        }
        domainEmpty++;
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
