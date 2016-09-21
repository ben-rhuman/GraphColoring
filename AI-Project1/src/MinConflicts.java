
import java.util.Random;

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
           
            pointArray[i].color = rnd;
        }
    }

    public void cleanUp() {
        int[] conflicts = new int[pointArray.length]; 

        for (int i = 0; i < 1000000; i++) { //iterating until desired max iterations
            if (checkDone()) {
                System.out.println("graph colored");
                return;
            }
            int counter = 0;
            for (int j = 0; j < pointArray.length; j++) { //create a conflict list
            if (hasConflict(j)) {
                conflicts[counter] = j;
                counter++;
            }
        }
            rnd = new Random().nextInt(counter);
            fixConflict(conflicts[rnd]);

        }
        System.out.println("fail");
    }

    public void fixConflict(int index) {
        if (hasConflict(index) == false) {
                return;
            }
        
        int oldConflictNum = 0;
        int color = 1;
        
        for (int i = 1; i <= kColors; i++) { //check which color is the best
            int newConflictNum = 0;

            for (int j = 0; j < pointArray.length; j++) { //checking if the color is the same
                if (graphAM[index][j] == 1) {
                    if (pointArray[j].color == i) {
                        newConflictNum++;
                    }
                }
            }
            if (newConflictNum <= oldConflictNum || i == 1) { //find color that causest least conflicts
                if(newConflictNum == oldConflictNum){
                   rnd = new Random().nextInt(2);
                   if(rnd == 0){
                       continue; //keep chosen color the same
                   } 
                }
                color = i;
                oldConflictNum = newConflictNum;
            }
        }
        pointArray[index].color = color;
    }

    public boolean hasConflict(int index) {

        for (int j = 0; j < pointArray.length; j++) { //checking if the color is the same
            if (graphAM[index][j] == 1) {
                if (pointArray[j].color == pointArray[index].color) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean checkDone() {
        for (int i = 0; i < pointArray.length; i++) {
            if (hasConflict(i) == true) {
                return false;
            }
        }
        return true;
    }

}    //end MinConflicts class
