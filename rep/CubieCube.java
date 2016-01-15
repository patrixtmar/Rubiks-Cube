package rep;

import global.Utils;
import solution.Moves;

/**
 * cube represented by its cubies - positions and orientations.
 * This representation is being used in phase 2 (Solving).
 * @author Harel Fuchs and Naama Baidatch
 */
public class CubieCube {
    
    /** 
     * cubies positions.<br>
     * each array element contains the cubie number which currently resides in it.<br>
     * for the solved cube, the positions and their numbers are:<br>
     * UF(0) DF(1) UB(2) DB(3) UR(4) DR(5) UL(6) DL(7) FR(8) FL(9) BR(10) BL(11)<br> 
     * UFR(12) UBL(13) DFL(14) DBR(15) DLB(16) DRF(17) URB(18) ULF(19)<br>
     * Notice that cubies 0-7 are edges, and 8-19 are corners.
     */
    public Cubie[] positions;
    
    /**
     * each cubie can be "flipped" internally. this array holds information of
     * each of the cubies' internal orientation. 
     */
    public int[] orientations;
    
    /**
     * Generates a solved cube.
     */
    public CubieCube(){
        positions = new Cubie[20];
        orientations = new int[20];
        for (int i = 0; i < 20; i++){
            positions[i] = Cubie.values()[i];
            orientations[i] = 0; 
        };
    }
    
    /**
     * @param move - the move number to apply to the cube.
     * @return This function changes the positions and orientations of the cubies according to the move. 
     */
    public void apply(int move){
        cyclePositions(move);
        twistOrientations(move);
    }
    
    public void apply(int m, int times) {
        for (int i = 0; i < times; i++) {
            apply(m);
        }
    }
    
    /**
     * @param move - the move to apply to the cube.
     * @return This function changes the positions and orientations of the cubies according to the move. 
     */
    public void apply(char move){
        apply(Moves.moveToNum(move));
    }
    
    /**
     * @param moves - string of moves to perform on cube, separated by spaces<br>
     * valid input example: "U+D2R-".<br>
     * instead of +/- notation one can use 1/3 respectively: "U1D2R3".
     */
    public void apply(String moves){
        String[] mvs = Utils.split(moves,' ');
        for (int i = 0; i < mvs.length; i++) {
            int move = Moves.moveToNum(mvs[i].charAt(0));
            int times = 0;
            switch(mvs[i].charAt(1)){
            case '+':
            case '1':
                times = 1;
                break;
            case '2':
                times = 2;
                break;
            case '-':
            case '3':
                times = 3;
                break;
            }
            
            apply(move, times);
        }
    }
    
    /**
     * @param move - the move to apply to the cube.
     * @return This function changes the positions of the cubies according to the move. 
     */
    public void cyclePositions(int move){
        int[] mt = Moves.basicPositionMoveTable[move]; //move table for given move.
        Cubie tmp;
        int tmp2;
        
        //edges
        tmp = positions[mt[3]];
        tmp2 = orientations[mt[3]]; //of course, we also need to update the orientations order accordingly
        for (int i = 3; i > 0; i--) {
            positions[mt[i]] = positions[mt[i-1]];
            orientations[mt[i]] = orientations[mt[i-1]];
        }
        positions[mt[0]] = tmp;
        orientations[mt[0]] = tmp2;
        
        //corners
        tmp = positions[mt[7]];
        tmp2 = orientations[mt[7]];
        for (int i = 7; i > 4; i--) {
            positions[mt[i]] = positions[mt[i-1]];
            orientations[mt[i]] = orientations[mt[i-1]];
        }
        positions[mt[4]] = tmp;
        orientations[mt[4]] = tmp2;
    }
    
    /**
     * @param move - the move to apply to the cube.
     * @return This function changes the orientations of the cubies according to the move. 
     */
    public void twistOrientations(int move){
        int[] mt = Moves.basicPositionMoveTable[move]; //move table for given move.
        //Corners: affected in the moves R,L,F,B.
        if(move<4){
            // cubies on positions 5,7 get +2 (CCW rotation) 
            orientations[mt[7]] = (orientations[mt[7]] + 2) % 3;
            orientations[mt[5]] = (orientations[mt[5]] + 2) % 3;
            // cubies on positions 4,6 get +1 (CW rotation)
            orientations[mt[6]] = (orientations[mt[6]] + 1) % 3;
            orientations[mt[4]] = (orientations[mt[4]] + 1) % 3;
        }
        //Edges: affected in the moves F,B
        if(move<2){
            for (int i = 0; i < 4; i++) {
                //flip the edge
                orientations[mt[i]] = 1 - orientations[mt[i]];
            }
        }
    }

    public void cloneInto(CubieCube c) {
        for (int i = 0; i < 20; i++){
            c.positions[i] = positions[i];
            c.orientations[i] = orientations[i]; 
        };
    }

    public boolean isSolved() {
        CubieCube cc = new CubieCube(); //solved cube
        for (int i = 0; i < 20; i++){
            if (positions[i] != cc.positions[i]) return false;
            if (orientations[i] != cc.orientations[i]) return false;
        };
        return true;
    }
    
}
