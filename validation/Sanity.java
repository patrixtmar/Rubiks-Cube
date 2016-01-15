package validation;

import global.Utils;
import rep.Cubie;
import rep.CubieCube;

/**
 * Sanity runtime validations for making sure we are reading the cube with no errors.
 * @author Harel Fuchs and Naama Baidatch
 */
public class Sanity {
    
    static int[] colorCounters;
    static boolean[] centerCounters;
    
    public static void init(){
        colorCounters = new int[10];
        centerCounters = new boolean[10];
    }

    /**
     * validate we don't get a color more than 9 times.
     */
    public static boolean validateGottenColors(int gotColor) {
        if (++colorCounters[gotColor] <= 9) return true;
        init(); //test failed, so need to re-init sanity.
        Log.e("got 10 tiles of the same color");
        return false;
    }
    
    public static boolean validateCenterColors(int gotColor) {
        if (centerCounters[gotColor]){
            init(); //test failed, so need to re-init sanity.
            Log.e("got two centers of the same color");
            return false;
        }
        centerCounters[gotColor] = true;
        return true;
    }
    
    public static boolean validateAllCubies(CubieCube cc) {
    	Cubie[] positions = cc.positions;
    	int[] found = new int[20];
    	for (int i = 0; i < found.length; found[i++]=0);
    	
    	for (int i = 0; i < positions.length; i++) {
			found[positions[i].ordinal()] += 1;
		}
    	
        for (int i = 0; i < found.length; i++) {
			if(found[i] != 1) {
				System.out.println("Not all Cubies exist. re-reading cube");
				return false;
			}
		}
        return true;
    }
    
    /**
     * <p>Fun fact: if we disassemble the cube then put it back together randomly, there is an 11/12 chance
     * that the result will be an invalid cube.<br>
     * This method comes to validate that we read the cube correctly.</p>
     * <p><b>Theorem:</b> A cube's configuration is valid iff:<br>
     * 		1. Parity(Edge-Cubies-Permutation) = Parity(Corner-Cubies-Permutation)<br>
     * 		2. Sum of Edge cubies' orientations is 0 modulo 2<br>
     * 		3. Sum of Corner cubies' orientations is 0 modulo 3<br>
     * for proof, see:<br>
     * http://www.math.harvard.edu/~jjchen/docs/Group%20Theory%20and%20the%20Rubik's%20Cube.pdf</p>
     * @param cube
     * @return whether cube is of valid configuration,
     * that is, if we can achieve this cube starting from the solved cube, making only 
     * valid moves (U,F,R,D,B,L)
     */
    public static boolean isCubeConfigurationValid(CubieCube cube){
    	// test 1: Parity(Edge-Cubies-Permutation) = Parity(Corner-Cubies-Permutation)
    	int[] edges = new int[12];
    	int[] corners = new int[8];
    	for (int i = 0; i < cube.positions.length; i++) {
			if(i<12) edges[i] = cube.positions[i].ordinal();
			else corners[i-12] = cube.positions[i].ordinal() - 12;
		}
    	if (Utils.isPermutationEven(edges) != Utils.isPermutationEven(corners)) 
    		return false;
    	
    	// test 2: Sum of Edge cubies' orientations is 0 modulo 2
    	// test 3: Sum of Corner cubies' orientations is 0 modulo 3
    	int edgeOriCount=0;
    	int cornerOriCount=0;
    	for (int i = 0; i < cube.orientations.length; i++) {
			if(i<12) edgeOriCount += cube.orientations[i];
			else cornerOriCount += cube.orientations[i];
		}
    	if (edgeOriCount%2 != 0) return false;
    	if (cornerOriCount%3 != 0) return false;
    	
    	// All tests passed
    	return true;
    }

}
