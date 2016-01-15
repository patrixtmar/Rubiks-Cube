package solution;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;

import rep.Actions;
import rep.CubieCube;

/**
 * an Implementation of Thistlethwaite's algorithm, based on Jaap Scherphuis' implementation.
 * see: http://tomas.rokicki.com/cubecontest/winners.html (2nd entry in table)
 * 
 * @author Harel Fuchs and Naama Baidatch
 */
public class Thistle {
    
    /**
     * list of the move types performed on the cube for a specific stage. 
     */
    public static int moveType[] = new int[20];

    //Stores the translated solution to return to Tilted Twister
    
    ///public static String TranslatedSolution = "";
    
    /**
     * how many times to perform each move in moveType
     */
    public static int moveReps[] = new int[20];
    
    /**
     * Current algorithm's stage<br>
     * STAGE -1 : Try to solve whole cube in a small number of moves.<br>
     * STAGE  0 : Fixes the orientation of the edges.<br>
     * STAGE  1 : Fixes the orientation of the corners.<br>
     *            Places the middle layer edges into their slice.<br>
     * STAGE  2 : Fixes the edges of the L and R faces.<br>
     *  		  Puts the corner cubies into their correct tetrads.<br>
     *  		  Makes the parity of the edges permutation even.<br>
     *  		  Makes the parity of the corners permutation even.<br>
     *  		  Fixes the total twist of each tetrad.<br>
     * STAGE  3 : Solves the cube
     */
    public static int stage = -1;
        
    public static List<String> solution = new ArrayList<String>();
    
    public static Actions[] robotSolution;
    
    /**
     * Computes solution for the input cube, using Thistle's algorithm.
     * @param cube
     */
    public static void solve(CubieCube cube) {
        int i,j;
        for (i = 0; i < moveType.length; moveType[i++] = -1);
        
        /* 
         * Pruning tables used for the algorithm.
    	 * For more info, see "Tables" class description.
    	 */
        Tables.generateAllTables();
        // first we try to solve everything in a small amount of moves.
        boolean blitzSuccessful = Blitz.solve(cube);
        
        if (!blitzSuccessful) {  
            // Full Thistle's solution. 
            // we have four stages, on each we are looking for a solution to take us from the current to the next one.
            for(stage=0 ; stage<4; stage++){
                // first try to solve with 0 moves, then 1, then 2... until we find a solution for the stage
                for(j=0; !pruningDFS(cube,j,0,-1,Orientation.last); j++);
                // found a solution!
                for(i=0; i<j; i++){
                    SolUtils.addToSolution("" + Moves.numToMove(moveType[i]) + moveReps[i]);

                }
            }
        }
        
        // Translation of "human" moves to robot moves.
        //robotSolution = SolUtils.humanToRobo();
        SolUtils.printSolution(); 
        //String solution = SolUtils.printSolution(); 
    }

    /** DFS using the pruning tables. 
     * @param movesleft - how many moves we got left to complete the <u>current stage</u>
     * @param movecount - how many moves we have already done in the <u>current stage</u>
     * @param last - the last move performed on the cube.
     * @return TRUE iff we find a solution for this stage.
     */
    public static boolean pruningDFS(CubieCube cube, int movesleft, int movecount, int last, int orientation){
        
        // Blitz mode
        if (stage == -1 && cube.isSolved()) return true;
        
        // not enough moves remain - failed.
        if (needMoreMoves(cube, movesleft)) return false;

        /* 
         * current state is solvable in the remaining moves, yet there are no moves left.
         * meaning - we have solved this stage.
         */
        if (movesleft == 0) {
            Orientation.last = orientation;
            return true;
        }

        // not solved yet.
        
        /*
         * Note that after each move the robot makes, some of the moves will be better follow-ups,
         * since it will take less time to bring them to face the bottom (this is how the robot makes moves).
         * Consider for example making a move on the 'DOWN' side. In order to make a move on the "RIGHT" side, we will have
         * to bring it to the bottom, that is, making extra 3 robot moves. Opposed to that, if we were to make a move on
         * the "LEFT" side, our overhead will be only 1 extra robot move.  
         * Here we try to find a solution which is also efficient for the robot to follow in terms of the overhead of
         * bringing the next side to face the bottom.
         */
        int[] moves = Orientation.getFollowingMoves(orientation);
        
        //try all valid moves in current stage 
        for(int i=0; i < 6; i++){
            
            // avoid doing the last move again
            if (moves[i] == last) continue;
            
            /*
             *  don't apply the opposite move after D/L/B moves.
             *  This way, we don't get sequences like "U1D1U1" which are not optimal for the robot to make
             *  and instead we will have "U2D1" which is much more efficient.
             */
            if (moves[i] == Moves.opposite(last) && Moves.isDLB(moves[i])) continue;
            
            moveType[movecount] = moves[i];
            // apply this move 1-3 times
            for(int reps=0; ++reps<4;){
                cube.apply(moves[i]);
                orientation = Orientation.reOrient(orientation, moves[i]);
                moveReps[movecount] = reps;
                //Check if the current stage allows this kind of move
                if(Moves.isMoveAllowed(moves[i],reps)){
                    // continue searching
                    if (pruningDFS(cube, movesleft-1, movecount+1, moves[i], orientation)) 
                        return true; 
                }
            }
            // restore move (4 of the same move is like no move at all)
            cube.apply(moves[i]);
            orientation = Orientation.reOrient(orientation, moves[i]);
        }
        // no solution found
        return false;
    }

    private static boolean needMoreMoves(CubieCube cube, int movesleft) {
        if (stage == -1){ // Blitz mode
            if (movesleft == 0) return true;
            if (movesleft < Blitz.getBlitzBound(cube)) return true;
            return false;
        }
        for (int i = 0; i < 2; i++) {  // each stage has two pruning tables
            int[] table = Tables.tables[stage*2 + i];
            int idx = Tables.getTablePos(cube, stage*2 + i);
            // the table contains for each cube configuration how many moves are required to solve the current stage
            int neededMoves = table[idx] - 1; 
            if (neededMoves > movesleft) return true;
        }
        return false;
    }
   
}
