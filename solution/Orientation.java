package solution;

import global.Utils;

import java.util.Arrays;

import rep.Color;
import rep.Cube;

/**
 * There are 24 possible cube orientations:
 * 6 ways to choose which color is on top * 4 ways to choose a neighboring color to be in front
 * This class contains orientation tables to help us with the algorithm.
 * @author Harel Fuchs and Naama Baidatch
 */
public class Orientation {
    
    /*
     * Representing an orientation:
     * we use two digits, first one corresponds to the current side on top, and the second
     * corresponds to the one in front.
     */
    
    private static final int U = 0;
    private static final int F = 1;
    private static final int R = 2;
    private static final int L = 3;
    private static final int B = 4;
    private static final int D = 5;
    private static final int X = -1; //invalid
    
    /** last orientation of the last stage */
    public static int last = 1;
    
    public static int[] getFollowingMoves(int orientation) {
        int serial = serialOrientation[orientation];
        if (serial == -1) return new int[]{D,U,L,R,B,F};
        return followingMoves[serial];
    }
    
    public static int reOrient(int currentOrientation, int move) {
        int newDown = newDownFromMove(move);
        int up = currentOrientation / 10;
        int front = currentOrientation % 10;
        if (newDown == front){
            // need to replace the front
            front = currentRight[up][front];
        }
        else if (newDown == 5-front){
            // need to replace the front
            front = currentRight[up][5-front];
        } 
        up = 5 - newDown;
        return up*10 + front;
    }
    
    private static int newDownFromMove(int move) {
        int moves[] = {F,B,R,L,U,D};
        return moves[move];
    }
    
    private static int moveFromNewDown(int newDown) {
        int downs[] = {4,0,2,3,1,5};
        return downs[newDown];
    }

    /* first index is up, second is front */
    public static int[][] currentRight = {
        /*       U   F   R   L   B   D  */
        /* U */ {X,  R,  B,  F,  L,  X},
        /* F */ {L,  X,  U,  D,  X,  R},
        /* R */ {F,  D,  X,  X,  U,  B},
        /* L */ {B,  U,  X,  X,  D,  F},
        /* B */ {R,  X,  D,  U,  X,  L},
        /* D */ {X,  L,  F,  B,  R,  X},
    };
    
    private static int[] serialOrientation = {-1, 0, 1, 2, 3, -1, -1, -1, -1, -1, 4, -1, 5, 6, -1, 7, -1, -1, -1, -1, 8, 9, -1, -1, 10, 11, -1, -1, -1, -1, 12, 13, -1, -1, 14, 15, -1, -1, -1, -1, 16, -1, 17, 18, -1, 19, -1, -1, -1, -1, -1, 20, 21, 22, 23, -1};
    
    private static int[][] followingMoves = {
        {5, 3, 4, 0, 1, 2},
        {5, 0, 4, 2, 3, 1},
        {5, 1, 4, 3, 2, 0},
        {5, 2, 4, 1, 0, 3},
        {1, 2, 0, 4, 5, 3},
        {1, 5, 0, 2, 3, 4},
        {1, 4, 0, 3, 2, 5},
        {1, 3, 0, 5, 4, 2},
        {3, 1, 2, 4, 5, 0},
        {3, 4, 2, 0, 1, 5},
        {3, 5, 2, 1, 0, 4},
        {3, 0, 2, 5, 4, 1},
        {2, 0, 3, 4, 5, 1},
        {2, 5, 3, 0, 1, 4},
        {2, 4, 3, 1, 0, 5},
        {2, 1, 3, 5, 4, 0},
        {0, 3, 1, 4, 5, 2},
        {0, 4, 1, 2, 3, 5},
        {0, 5, 1, 3, 2, 4},
        {0, 2, 1, 5, 4, 3},
        {4, 2, 5, 0, 1, 3},
        {4, 1, 5, 2, 3, 0},
        {4, 0, 5, 3, 2, 1},
        {4, 3, 5, 1, 0, 2},
    };
    
    
    /**
     * helper function to generate the following moves table
     */
    private static void printFollowingMoves(){
        for (int up = 0; up < 6; up++) {
            for (int front = 0; front < 6; front++) {
                if (serialOrientation[up*10+front] != -1) { //valid orientation
                    int atRight = currentRight[up][front];
                    System.out.print("{" + moveFromNewDown(5 - up) + ", "); //down is trivially best
                    System.out.print(moveFromNewDown(5 - atRight) + ", "); //left is second best
                    System.out.print(moveFromNewDown(up) + ", "); // then up
                    System.out.print(moveFromNewDown(front) + ", "); // front
                    System.out.print(moveFromNewDown(5-front) + ", "); // back
                    System.out.println(moveFromNewDown(atRight) + "},"); //right is worst
                }
            } 
        }
    }
    
    /**
     * helper function to generate the serial orientation table
     */
    private static void printSerialOrientation() {
        int[] serials = new int[56];
        for (int i = 0; i < serials.length; serials[i++]=-1);
        int k=0;
        for (int up = 0; up < 6; up++) {
            for (int front = 0; front < 6; front++) {
                if (currentRight[up][front] != X) //valid orientation
                    serials[up*10 + front] = k++;
            }
        }
        System.out.println(Arrays.toString(serials));
    }

    public static int orientationFromCube(Cube cube) {
        int colorUp = Utils.translateColorToInt(cube.up.getSq()[4]);
        int colorFront = Utils.translateColorToInt(cube.front.getSq()[4]);
        // UFRLBD
        // 602153
        int[] map = {F,L,R,D,X,B,U};
        return (map[colorUp] * 10 + map[colorFront]);
    }

}
