package solution;

import rep.Cubie;

/**
 * @author Harel Fuchs and Naama Baidatch
 */
public class Moves {

    /**
     * Valid moves to perform on cube.
     * for example, 'U' is spinning the Up side 90 clockwise
     */
    public static final String moves = "FBRLUD";
    
    /**
     * Allowed moves per stage:<br>
     * Stage  0: U,D,R,L,F,B<br>
     * Stage  1: U,D,R,L,F2,B2<br>
     * Stage  2: U,D,R2,L2,F2,B2<br>
     * Stage  3: U2,D2,R2,L2,F2,B2<br>
     */
    public static final String[] allowedMoves = {
        "FBRLUD", // stage 0
        "RLUD",   // stage 1
        "UD",     // stage 2
        ""        // stage 3
    };
    
    /**
     * @param m - move number
     * @return the move character corresponding to the number.
     */
    public static char numToMove(int m){
        if (m < 0 || m > 5) return '\0';
        return moves.charAt(m);
    }

    /**
     * @param move
     * @return number corresponding to that move.
     */
    public static int moveToNum(char move) {
        return moves.indexOf(move);
    }
    
    /**
     * each move (F,B,R,L,U,D) is a combination of two cycles of length 4 each. <br>
     * these cycles represent the new positions of the cubies under a certain move. <br> 
     * 1st cycle - edge cubies <br>
     * 2nd cycle - corner cubies <br><br>
     * this table only deals with positions, not orientations.
     * <br>
     * for example: <br>
     * the 1st cycle in F is 0, 8, 1, 9. this means that when an F move is carried out, the '0' cubie
     * will move to the '8' cubie's position, the '8' will move to '1' and so on.<br><br>
     * the numbers represent cubies, in the same order they are listed in the Cubie enum.
     * 
     */
    public static int[][] basicPositionMoveTable = {
        {0, 8,  1, 9,  19, 12, 17, 14}, // F
        {2, 11, 3, 10, 18, 13, 16, 15}, // B
        {4, 10, 5, 8,  12, 18, 15, 17}, // R
        {6, 9,  7, 11, 13, 19, 14, 16}, // L
        {0, 6,  2, 4,  12, 19, 13, 18}, // U
        {1, 5,  3, 7,  14, 17, 15, 16}  // D
    };

    /**
     * @param move code
     * @return opposite side of move, e.g. for 'U' return 'D'
     */
    public static int opposite(int move) {
        if (move%2 == 0) return move+1;
        return move-1;
    }

    /**
     * @param move code
     * @return whether this move code translates into U,R or F.
     */
    public static boolean isDLB(int move) {
        return (move % 2 == 1);
    }

    /**
     * @param move - move to perform
     * @param reps - how many times to do it
     * @param stage - current stage
     * @return TRUE iff the move is allowed in the current stage.
     */
    public static boolean isMoveAllowed(int move, int reps) {
        if (Thistle.stage == -1) return true; // Blitz mode
        if (reps == 2) return true; // half turns are always allowed.
        String validMoves = allowedMoves[Thistle.stage]; //need to divide stage by 2 since it's being promoted by 2 each time.
        return (validMoves.indexOf(Moves.numToMove(move)) != -1);
    }

    public static boolean moveAffectsCubie(int m, Cubie cubie) {
        String s_cubie = cubie.toString();
        return (s_cubie.indexOf(numToMove(m)) != -1);
    }

}