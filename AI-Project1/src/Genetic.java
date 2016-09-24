
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * 
 * @author Ben Rhuman, Danny Kumpf, Isaac Sotelo
 */
public class Genetic {

    private int[][] graph;
    private Point[] point;
    private int numColors;
    private List<int[]> graphPool;
    private List<Integer> poolFitness;  // Lower number the better. Our fitness factor is number of conflicts
    private List<int[]> graphWinner;
    private List<Integer> winFitness;
    private int numTournaments;
    private int numInPool;
    private boolean graphColored;
    private int[] coloredGraph;
    private int decisions;

    public Genetic(int[][] graph, Point[] point, int numColors) {
        this.graph = graph;
        this.point = point;
        this.numColors = numColors;
        numInPool = 20;
        numTournaments = 500000;
        graphColored = false;

        graphPool = new ArrayList();
        poolFitness = new ArrayList();
        graphWinner = new ArrayList();
        winFitness = new ArrayList();

        for (int i = 0; i < numTournaments; i++) {
            tournament();
            if (graphColored == true) { //Stops the tournaments since a solution was found
                System.out.println("Graph Colored.");

                for (int k = 0; k < point.length; k++) { //printing points and colors

                    System.out.print("Point " + (k) + ": " + point[k].color + " \n");
                    if (k == point.length - 1) {
                        System.out.println("\n");
                    }
                }
                break;
            }
            graphPool.addAll(graphWinner);   // Puts the winning graphs into the graphpool to be run trhough more tournaments
            graphWinner.removeAll(graphWinner);
        }
        if (graphColored == false) { //If its uncolored then genetic algorithm failed for the set number of tournaments
            System.out.println("Graph Failed to Colored.");

            for (int k = 0; k < point.length; k++) { //printing points and colors

                System.out.print("Point " + (k) + ": " + point[k].color + " \n");
                if (k == point.length - 1) {
                    System.out.println("\n");
                }
            }
        }
        System.out.println("Decision number:" + decisions + "\n");

    }//End Genetic

    private void tournament() {

        while (graphWinner.size() < numInPool) {  //Runs tournaments until there are enough crossed over results to fill a graphPool.

            if (graphPool.isEmpty()) { //If we just put our crossover values in we dont want to overwrite them.
                for (int i = 0; i < numInPool; i++) {
                    graphPool.add(new int[point.length]);
                }
                createPool();
            }
            for (int i = 0; i < numInPool; i++) {
                poolFitness.add(0);
            }
            checkPoolFit(); //Assigns a fitness to each
            if (graphColored == true) {
                return;
            }

            while (!graphPool.isEmpty()) {  //Run tournaments and crossover results until there are no more graphs in pool
                runTournament();
                runTournament();
                crossover();
            }
        }

    }//End Tournament

    private void createPool() {  //Generated a graph pool.
        for (int i = 0; i < numInPool; i++) {
            for (int j = 0; j < point.length; j++) {
                graphPool.get(i)[j] = new Random().nextInt(numColors) + 1;  //Randomly sets the color of each node.
                decisions++;
            }
        }
    }//End CreatePool

    private void checkPoolFit() {
        for (int i = 0; i < graphPool.size(); i++) {
            for (int k = 0; k < graphPool.get(i).length; k++) {
                for (int j = 0; j < graph.length; j++) { //Searching through all connecting nodes
                    if (graph[k][j] == 1) { //If connection
                        if (graphPool.get(i)[k] == graphPool.get(i)[j]) {
                            poolFitness.set(i, poolFitness.get(i) + 1);  //Sets the fitness to the number of conflicts that occur within each graph.
                            decisions++;
                        }
                    }
                }
            }
            if (poolFitness.get(i) == 0) {  //If fitness is 0 then the graph is colored.
                coloredGraph = graphPool.get(i);
                graphColored = true;
            }
        }
    }//End CheckPoolFit

//    private void crossover() {
//        for (int i = 0; i < graphWinner.get(graphWinner.size() - 2).length; i += 2) {
//            graphWinner.get(graphWinner.size() - 2)[i] = graphWinner.get(graphWinner.size() - 1)[i]; //Croses over ever other node in the graph. Hopefully this synthesis leads to a more fit graph.
//            decicions++;
//        }
//        graphWinner.remove(graphWinner.size() - 1); //Removes the non crossed over graph.
//    }//End Crossover
    private void crossover() {
        for (int i = graphWinner.get(graphWinner.size() - 2).length / 2; i < graphWinner.get(graphWinner.size() - 2).length; i++) {
            graphWinner.get(graphWinner.size() - 2)[i] = graphWinner.get(graphWinner.size() - 1)[i]; //Croses over half of each graph. Tried two different 
            decisions++;
        }
        graphWinner.remove(graphWinner.size() - 1); //Removes the non crossed over graph.
    }//End Crossover

    private void runTournament() { //Compares two random graphs and selects the fitest of the two.
        int num1 = new Random().nextInt(graphPool.size());
        int num2;
        do {
            num2 = new Random().nextInt(graphPool.size());
            decisions++;
        } while (num2 == num1);

        if (poolFitness.get(num1) > poolFitness.get(num2)) {  //Adds the fitest to the graphWinner
            graphWinner.add(graphPool.get(num2));
            winFitness.add(poolFitness.get(num2));
        } else {
            graphWinner.add(graphPool.get(num1));
            winFitness.add(poolFitness.get(num1));
        }

        if (num2 > num1) { //Remove the larger index first to avoid aout of bounds problems.
            graphPool.remove(num2);
            poolFitness.remove(num2);
            graphPool.remove(num1);
            poolFitness.remove(num1);
        } else {
            graphPool.remove(num1);
            poolFitness.remove(num1);
            graphPool.remove(num2);
            poolFitness.remove(num2);
        }
        decisions += 8;
    }
}
