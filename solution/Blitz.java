package solution;

import global.ServerConfig;
import rep.Cubie;
import rep.CubieCube;

/**
 * Try to solve whole cube in a small amount of moves.
 * @author Harel Fuchs and Naama Baidatch
 */
public class Blitz {
    
    public static boolean solve(CubieCube cube) {
       int j;
       for(j=0; j <= ServerConfig.blitzMoves; j++){
           if (Thistle.pruningDFS(cube,j,0,-1,Orientation.last)) break;
       }
       if (cube.isSolved()) {
           // found a solution!
           for(int i=0; i<j ;i++){
               SolUtils.addToSolution("" + Moves.numToMove(Thistle.moveType[i]) + Thistle.moveReps[i]);
        	   //SolUtils.addToSolution("" + Moves.numToMove(Thistle.moveType[i]) + Thistle.moveReps[i]);
           }
           return true;
       }
       return false;
   }

    /**
     * This is a heuristic to get a very loose lower bound for solving a cube entirely.
     * It works very well on short solutions (saves approximately two thirds of the required time!).
     * We took this idea from Antony Boucher's implementation:
     * http://tomas.rokicki.com/cubecontest/boucher.txt
     */
    public static int getBlitzBound(CubieCube cube) {
        int sum = 0;
        CubieCube csol = new CubieCube(); // a solved cube.
        for (int i = 0; i < Cubie.NOF_CUBIES.ordinal(); i++) {
            if (csol.positions[i] != cube.positions[i]){
                sum++;
            }
        }
        switch(sum){
        case 0:
        case 8:
            return 0;    
        case 13: 
        case 16:
            return 1;
        default:
            return 2;
        } 
    }

}
