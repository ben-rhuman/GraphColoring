
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

    private Point[] pointArray;
    private int[][] graphAM;
    private int kColors;
    private int rnd;
    private int decisions;

    public MinConflicts(Point[] p, int[][] am, int c) {
        pointArray = p;
        graphAM = am;
        kColors = c;

        addColors();

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

        for (int i = 0; i < 1000000; i++) { //iterating until desired max iterations
            if (checkDone()) {
                System.out.println("graph colored");
                System.out.println("\n" + "decision number:" + decisions + "\n");

                for (int k = 0; k < pointArray.length; k++) {
                    System.out.println("Point " + k + ": " + pointArray[k].color + ", ");

                }
                return;
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

        System.out.println("fail");
        System.out.println("\n" + "decision number:" + decisions + "\n");
    }

    public void fixConflict(Point point) {
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
