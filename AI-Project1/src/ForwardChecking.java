
/**
 * 17 September 2016
 *
 * @author benrhuman Backtracking algorithm with forward checking
 */
public class ForwardChecking {

    private final int[][] graph;
    private final Point[] point;
    private final int numColors;
    private final int[][] domain;
    private int decisions;
    private int nodeColored; //for printing

    public ForwardChecking(int[][] graph, Point[] point, int numColors) {
        this.graph = graph;
        this.point = point;
        this.numColors = numColors;
        domain = new int[point.length][numColors];
        
        System.out.println("1. Setting up the domains of each node\n");
        for (int i = 0; i < point.length; i++) {
            for (int j = 0; j < numColors; j++) {
                domain[i][j] = j + 1;
            }
        }
        
        //final long startTime = System.currentTimeMillis();
        boolean success = BTForwardCheck(0);
        //final long endTime = System.currentTimeMillis();
        
        if(success){
            System.out.println("Graph Colored");
        System.out.println("Decision number:" + decisions + "\n");
        
        System.out.println("Number of times a node was given a color: " + nodeColored+ '\n');
        
        for (int k = 0; k < point.length; k++) { //printing points and colors
 
                    System.out.print("Point " + (k) + ": " + point[k].color + " \n");
                    if(k == point.length-1){
                        System.out.println("\n");
                    }
                }

        }else{
            System.out.println("Failed to Color");
        System.out.println("Decision number:" + decisions + "\n");
        
        System.out.println("Number of times a node was given a color: " + nodeColored+ '\n');
        
        for (int k = 0; k < point.length; k++) { //printing points and colors

                    System.out.print("Point " + (k) + ": " + point[k].color + " \n");
                    if(k == point.length-1){
                        System.out.println("\n");
                    }
                }
        }
        
        //System.out.println("\n    Total execution time: " + (endTime - startTime) + "\n"  );
    }

    public boolean BTForwardCheck(int current) { //Recursively traverse graph selecting node colors
        if (current == point.length) {
            return true; //Successfully finished coloring graph.
        }
        for (int i = 0; i < domain[current].length; i++) {
            if (domain[current][i] != 0) {

                if (colorable(current, domain[current][i])) {
                    point[current].color = domain[current][i];
                      decisions++;
                      nodeColored++;
                      
                       if (i == 0 && (current == 0)) {//printing a step
                           System.out.println("1. Pick a valid color for a node");
                           System.out.println("Current color of first node: "+ point[current].color + "\n");
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
                        System.out.println("\n2. Update the domains of its adjacent nodes.\n");

                    }
                                
                      
                    for (int j = 0; j < graph.length; j++) { //Searching through all connecting nodes
                        if (graph[current][j] == 1) { //If connection
                            domain[j][point[current].color - 1] = 0;  // Remove the selected color from the nodes domain.
                        }
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

                    if (!lookForward(current)) { //check if any domains were wiped out.
                        restoreDomain(current);
                    } else {
                        if (i == 0 && (current == 0)) {//printing a step
                        System.out.println("3. Repeat with the next node until a solution is found or a domain is emptied");
                    }
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
                domain[j][point[current].color - 1] = point[current].color;  //reverse the previous domain deletion
            }
        }
        point[current].color = 0;
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
