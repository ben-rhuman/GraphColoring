
import java.util.Random;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author benrhuman
 */
public class MinConflicts {

    private final Point[] pointArray;
    private final int[][] graphAM;
    private final int kColors;
    private int rnd;
    private int decisions;
    private int fixConflicts; //for printing
    private int reRandomize; //for printing

    public MinConflicts(Point[] p, int[][] am, int c) {
        pointArray = p;
        graphAM = am;
        kColors = c;

        addColors();
        System.out.println("\n1. Adding colors");

        for (int k = 0; k < pointArray.length; k++) { //printing points and colors
            
            System.out.print("Point " + (k) + ": " + pointArray[k].color + " \n");
            if (k == pointArray.length - 1) {
                System.out.println("\n");
            }
        }

        System.out.println("2. Creating a conflict list and attempting to resolve it\n");
        cleanUp();
    }

    public void addColors() {
        for (int i = 0; i < pointArray.length; i++) {
            rnd = new Random().nextInt(kColors) + 1;
            decisions++;
            pointArray[i].color = rnd;
        }
    }

    public void cleanUp() {

        for (int i = 0; i < 500000 * pointArray.length; i++) { //iterating until desired max iterations
            if (checkDone()) {
                System.out.println("Graph Colored");
                System.out.println("Decision number:" + decisions + "\n");

                System.out.println("Number of conflict fixing attempts: " + fixConflicts + '\n');
                System.out.println("Number of re-randomization of colors: " + reRandomize + '\n');

                for (int k = 0; k < pointArray.length; k++) { //printing points and colors

                    System.out.print("Point " + (k) + ": " + pointArray[k].color + " \n");
                    if (k == pointArray.length - 1) {
                        System.out.println("\n");
                    }
                }
                return;
            }

            if (i % 4000 == 0) { //If it hasn't yet found a solution its not likely it will with the current conflicts. Rerandomize and try again.
                reRandomize++;
                addColors();
            }

            ArrayList<Point> conflicts = new ArrayList<Point>();

            for (int j = 0; j < pointArray.length; j++) { //create a conflict list
                if (hasConflict(pointArray[j])) {
                    conflicts.add(pointArray[j]);

                }
            }

            rnd = new Random().nextInt(conflicts.size());

            decisions++;

            fixConflict(conflicts.get(rnd));

        }

        System.out.println("Failed to Color");
        System.out.println("Decision number:" + decisions + "\n");

        System.out.println("Number of conflict fixing attempts: " + fixConflicts + '\n');
        System.out.println("Number of re-randomization of colors: " + reRandomize + '\n');

        for (int k = 0; k < pointArray.length; k++) { //printing points and colors
            
            System.out.print("Point " + (k) + ": " + pointArray[k].color + " ");
            if (k == pointArray.length - 1) {
                System.out.println("\n");
            }
        }
    }

    public void fixConflict(Point point) {
        fixConflicts++;
        if (hasConflict(point) == false) {
            return;
        }

        int oldConflictNum = numConflicts(point);
        int color = point.color;

        for (int i = 1; i <= kColors; i++) { //check which color is the best
            int newConflictNum = 0;

            for (int j = 0; j < pointArray.length; j++) { //checking if the color is the same as adjacent
                if (graphAM[point.position][j] == 1) {
                    if (pointArray[j].color == i) {
                        newConflictNum++;
                    }
                }
            }
            if (newConflictNum == oldConflictNum) {

            }
            if (newConflictNum <= oldConflictNum) { //find color that causest least conflicts
                if (newConflictNum == oldConflictNum) {
                    rnd = new Random().nextInt(2);
                    if (rnd == 0) {
                        continue; //keep chosen color the same
                    }
                }
                color = i;
                oldConflictNum = newConflictNum;
            }
        }
        point.color = color;
    }

    public int numConflicts(Point point) {
        int count = 0;
        for (int i = 0; i < pointArray.length; i++) { //checking if the color is the same
            if (graphAM[point.position][i] == 1) {
                if (pointArray[i].color == point.color) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean hasConflict(Point point) {

        for (int j = 0; j < pointArray.length; j++) { //checking if the color is the same
            if (graphAM[point.position][j] == 1) {
                if (pointArray[j].color == point.color) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean checkDone() { //check if there are any conflicts
        for (int i = 0; i < pointArray.length; i++) {
            if (hasConflict(pointArray[i]) == true) {
                return false;
            }
        }
        return true;
    }

}    //end MinConflicts class
