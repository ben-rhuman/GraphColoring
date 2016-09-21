/**
 * 17 September 2016
 *
 * @author benrhuman 
 * Simple backtracking algorithm
 */
public class SimpleBacktracking {

    private int[][] graph;
    private Point[] point;
    private int numColors;

    public SimpleBacktracking(int[][] graph, Point[] point, int numColors) {
        this.graph = graph;
        this.point = point;
        this.numColors = numColors;

        boolean success = backtrackColoring(0);
        if(success){
            System.out.println("Graph colored");
            for(int i = 0; i < point.length; i++){
                System.out.println("Point " + i + ": " + point[i].color + ", ");
            }
        }else{
            System.out.println("Failed to Color");
        }
    }

    private boolean backtrackColoring(int current) {
        if (current == point.length) {
            return true; //Successfully finished coloring graph.
        }
        for (int c = 1; c < numColors + 1; c++) { //Cycles through colors 1-3 or 1-4 looking for a possible color for the node.
            if (colorable(current,c)) { //If the node has found a success full coloring we color it.
                point[current].color = c;
                if(backtrackColoring(current + 1)){
                    return true;
                }          
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
