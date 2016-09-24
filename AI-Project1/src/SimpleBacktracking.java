
/**
 * 17 September 2016
 *
 * @author benrhuman
 * Simple backtracking algorithm
 */
public class SimpleBacktracking {

    private final int[][] graph;
    private final Point[] point;
    private final int numColors;
    private int decisions;
    private int nodeColored; //for printing
    private int backTrack;

    public SimpleBacktracking(int[][] graph, Point[] point, int numColors) {
        this.graph = graph;
        this.point = point;
        this.numColors = numColors;

        boolean success = backtrackColoring(0);
        if (success) {
            System.out.println("Graph colored");
            System.out.println("Decision number:" + decisions + "\n");

            System.out.println("Number of times a node was given a color: " + nodeColored + " ");
            System.out.println("Number of times it backtracked: " + backTrack + " ");
            for (int k = 0; k < point.length; k++) { //printing points and colors
  
                System.out.print("Point " + (k) + ": " + point[k].color + " \n");
                if (k == point.length - 1) {
                    System.out.println("\n");
                }
            }
        } else {
            System.out.println("Failed to Color");
            System.out.println("Decision number:" + decisions + "\n");

            System.out.println("Number of times a node was given a color: " + nodeColored + " ");
            System.out.println("Number of times it backtracked: " + backTrack + " ");

            for (int k = 0; k < point.length; k++) { //printing points and colors
  
                System.out.print("Point " + (k) + ": " + point[k].color + " \n");
                if (k == point.length - 1) {
                    System.out.println("\n");
                }
            }
        }
    }

    private boolean backtrackColoring(int current) {
        if (current == point.length) {
            return true; //Successfully finished coloring graph.
        }
        for (int c = 1; c < numColors + 1; c++) { //Cycles through colors 1-3 or 1-4 looking for a possible color for the node.
            if (c == 1 && (current == 0)) {//printing a step
                System.out.println("1. Pick a color for a node");
                System.out.println("Current color: " + c + "\n");
            }
            if (c == 1 && (current == 0)) {//printing a step
                System.out.println("2. Check if that color is not the same as any adjacent node's color");
                System.out.println("outcome of test: " + colorable(current, c) + "\n");
            }
            if (colorable(current, c)) { //If the node has found a successfull coloring we color it.
                point[current].color = c;
                decisions++;
                nodeColored++;

                if (c == 1 && (current == 0)) {//printing a step
                    System.out.println("3. Give the node the valid color");
                    System.out.println("Current color of first node: " + point[current].color + "\n");
                }

                if (c == 1 && (current == 0)) {//printing a step
                    System.out.println("4. Repeat with the next node until each node has a valid color");

                }

                if (backtrackColoring(current + 1)) {
                    return true;
                }
                backTrack++;
                point[current].color = 0; // Reset the color if the next coloring failed.
            }
        }
        return false;
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
