package solution;

import rep.Cubie;
import rep.CubieCube;
import global.Utils;

/**
 * This class contains pruning tables which are used to determine the "depth" (how far we are from the solution) of each cube state.<br>
 * <br>
 * When we search for a solution we always have a limit of moves that we are allowed to make, so we consult these tables to see if a solution
 * is possible under this constraint. The "depth" mentioned above is not exact, but an upper bound.<br>
 * <br>
 * Each stage has two pruning tables, except from the first (stage 0) that has only one.
 * In each stage of the solution we examine different attributes of the cube, and so we divide the cube-space into equality groups.<br>
 * <br>
 * For example, in the first stage we divide the cubes into groups depending on how many flipped edges they have. Since there are 12 edges
 * in total, and each one can be either flipped or not, we have a total of 2^12 = 4096 equality classes, so this stage's table will be of size 4096. 
 *
 * @author Harel Fuchs and Naama Baidatch
 */
public class Tables {    
    
    public static int[][] tables = new int[8][];
    
    //                                      stage0    stage1      stage2     stage3
    public static final int[] tableLength = {1,4096,  6561,4096,  256,1536,  13824,576};
    
    /* ===============================
     *        TABLES GENERATION
     * =============================*/
    
    /**
     * we use this function to generate the pruning tables.
     */
    public static void generateAllTables(){
        for (int i = 0; i < tables.length; generateTable(i++));
    }
    
    /**
     * we use this function to generate a specific pruning table.
     * @param tableIndex
     */
    public static void generateTable(int tableIndex){
        int numpos=1, d=1, tlen = tableLength[tableIndex];
        int[] table = new int[tlen];
        for (int i = 0; i < table.length; table[i++] = 0); //clearing the table

        //mark solved position as depth 1
        CubieCube cube = new CubieCube(); // a solved cube
        table[getTablePos(cube, tableIndex)] = 1;
        
        // while there are new positions of depth d
        while(numpos != 0){
            numpos=0;
            // find each position of depth d
            for(int i=0; i<tlen; i++){
                if( table[i]==d ){ // found a position of depth d.
                    setCubeToTableEntry(cube, tableIndex, i); //construct that cube position
                    // try each move any amount (1-3)
                    for( int move=0; move<6; move++){
                        for( int times=1; times<4; times++){
                            cube.apply(move);
                            // check that move is allowed in this stage
                            if ( times==2 || move >= (tableIndex&6)) {
                                // get resulting position in table
                                int pos = getTablePos(cube,tableIndex);
                                if(table[pos] == 0){ // if got a new position in table...
                                    table[pos]=d+1; // mark that position as depth d+1
                                    numpos++;
                                }
                            }
                        }
                        cube.apply(move); //reset this move.
                    }
                }
            }
            d++;
        }
        
        // save to database
        tables[tableIndex] = table;
    }
    
  
    /**
     * 
     * @param cube
     * @param tableIdx
     * @return the index of the entry in table #tableIdx that matches the cube's state
     */
    public static int getTablePos(CubieCube cube, int tableIdx){
        int i,n=0;
        switch(tableIdx){
        case 1:
        	/* 
        	 * EDGEFLIP
        	 * Edge orientations can be represented by 12 bits - each bit corresponds to an edge cubie,
        	 * and states whether it's flipped (1) or not (0) 
        	 */
            for(i = 0; i < 12; i++) 
                n += cube.orientations[i] << i;
            break;
        case 2:
        	/* 
        	 * CORNERTWIST 
        	 * very similar to the EDGEFLIP, only now we are working in base 3 instead of base 2,
        	 * since each corner cubie has 3 possible orientations
        	 */
            for(i = 19; i > 11; i--) 
                n = n*3 + cube.orientations[i];
            break;
        case 3:
            /* 
             * UD SLICE (The layer between the U and D layers)
             * n is a bitmap whose bits signal which edge cubies currently reside in the UD layer.
             */
            for(i = 0; i < 12; i++) 
                n+= ((cube.positions[i].ordinal() & 8) != 0) ? (1<<i) : 0;
            break;
        case 4:
            /* 
             * FB SLICE (The layer between the F and B layers)
             * n is a bitmap whose bits signal which edge cubies currently reside in the FB layer.
             * in this step it's sufficient to look only at the first 8 cubies, because the cubies of the
             * UD slice have already been taken care of. So we have only 8 bits.
             */
            for(i=0; i<8; i++) 
                n+= ((cube.positions[i].ordinal() & 4) != 0) ? (1<<i) : 0;
            break;
        case 5:
        	/* 
        	 * TETRADS
        	 * Tetrad = 4 cubies which are not adjacent in the solved cube.
        	 * 1st tetrad: cubies 12-15
        	 * 2nd tetrad: cubies 16-19
        	 */
            int corners[] = new int[8];
            int corners2[] = new int[4]; //represent the inner permutation of the corner cubies per tetrad
            int j=0,k=0,l;
            
            // separate pieces for twist/parity determination
            for(i=0; i<8; i++){
                if(((l = cube.positions[i+12].ordinal()-12) & 4) != 0){
                    corners[l] = k++;
                    n += 1<<i; // n is 8 bits, set bit if corner belongs in 2nd tetrad.
                }else 
                    corners[j++] = l;
            }
            
            // permute the 1st tetrad so it's solved (0123), and apply the same permutation on the 2nd tetrad.
            for(i=0; i<4; i++) {
                corners2[i] = corners[4+corners[i]];
            }
            
            // solve one piece of second tetrad
            for(i=3; i>0; i--) {
                corners2[i] ^= corners2[0];
            }
            
            // TWIST of 2nd tetrad
            n = n*6 + corners2[1]*2 - 2;
            if (corners2[3] < corners2[2])
                n++;
            
            break;
        case 6:
            /* 
             * FIX POSITIONS
             * we use base 24 (4!) since we work in groups of 4, and each group has 
             * 4! possible arrangement of positions.
             */
            n = Utils.permtonum(cube,0) * 24 * 24   /* edges of LR slice */
                +Utils.permtonum(cube,4) * 24       /* edges of FB slice */
                +Utils.permtonum(cube,12);          /* corners of first tetrad */
            break;
        case 7:
            /* FIX POSITIONS - continued */
            n = Utils.permtonum(cube,8) * 24        /* edges of UD slice */
                +Utils.permtonum(cube,16);          /* corners of second tetrad */
            break;
        }
        return n;
    }
    
    /**
     * Sets the input cube to correspond to the n-th entry of the t-th talbe
     * @param c - the cube to be affected
     * @param t - table index of the relevant stage
     * @param n - table entry index
     */
    public static void setCubeToTableEntry(CubieCube c, int t, int n){
        int i, j=12, k=0;
        CubieCube clean = new CubieCube(); // solved cube
        
        switch(t){
        // case 0 does nothing so leaves cube solved
        case 1: 
        	/* 
        	 * EDGEFLIP
        	 * Edge orientations can be represented by 12 bits - each bit corresponds to an edge cubie,
        	 * and states whether it's flipped (1) or not (0) 
        	 */
            for(i=0; i<12; i++){
                clean.orientations[i] = n % 2;
                n >>= 1;
            }
            break;
        case 2: 
        	/* 
        	 * CORNERTWIST 
        	 * very similar to the EDGEFLIP, only now we are working in base 3 instead of base 2
        	 * since each corner cubie has 3 possible orientations
        	 */
            for(i=12; i<20; i++) {
            	clean.orientations[i] = n % 3;
            	n /= 3;
            }
            break;
        case 3: 
        	/* 
        	 * UD SLICE (The layer between the U and D layers)
        	 * n is a bitmap whose bits signal which edge cubies currently reside in the UD layer.
        	 */
            for(i=0; i<12; i++, n>>=1){
            	if (n%2 == 1)
            		clean.positions[i]= Cubie.FR; // a cubie which belongs in the UD slice
            	else
            		clean.positions[i]= Cubie.UF; // a cubie which does not belong in the UD slice
            }
            break;
        case 4:
            /* 
             * FB SLICE (The layer between the F and B layers)
             * n is a bitmap whose bits signal which edge cubies currently reside in the FB layer.
             * in this step it's sufficient to look only at the first 8 cubies, because the cubies of the
             * UD slice have already been taken care of. So we have only 8 bits.
             */
            for(i=0; i<8; i++, n>>=1){
            	if (n%2 == 1)
            		clean.positions[i]= Cubie.UR; // a cubie which belongs in the FB slice
            	else
            		clean.positions[i]= Cubie.UF; // a cubie which does not belong in the FB slice
            }
            break;
        case 5: /* TETRADS & TWIST */
            
            // possible permutations for the second tetrad under the constraint that the first cubie of this tetrad (16th) is in place.
            int[] corner_permutations = 
                {
                    16, 17, 18, 19, 
                    16, 17, 19, 18, 
                    16, 18, 17, 19, 
                    16, 19, 17, 18, 
                    16, 18, 19, 17, 
                    16, 19, 18, 17
                };
            int cornidx = (n%6)*4; // starting position of permutation.
            int[] c_perm = new int[4]; // current permutation (corresponding to n)
            for (int l = 0; l < c_perm.length; c_perm[l] = corner_permutations[cornidx + l++]);
            
            n /= 6;
            for(i=0; i<8; i++, n>>=1){
                // If bit is set, put a cubie from the 2nd tetrad according to the permutation.
                // Else, put a cubie from the 1st tetrad, permutation doesn't matter.
                clean.positions[i+12]= ((n%2) != 0) ? Cubie.values()[c_perm[k++]] : Cubie.values()[j++];
            }
            break;
        case 6:
            /* 
             * FIX POSITIONS
             * we use base 24 (4!) since we work in groups of 4, and each group has 
             * 4! possible arrangement of positions.
             */
            Utils.numtoperm(clean,n%24,12);n/=24;   /* corners of first tetrad */
            Utils.numtoperm(clean,n%24,4); n/=24;   /* edges of FB slice */
            Utils.numtoperm(clean,n   ,0);          /* edges of LR slice */        
            break;
        case 7:
            /* FIX POSITIONS - continued */
            Utils.numtoperm(clean,n/24,8);  /* corners of second tetrad */
            Utils.numtoperm(clean,n%24,16); /* edges of UD slice */  
            break;
        }
        clean.cloneInto(c);
    }

}
