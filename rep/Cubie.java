package rep;

/**
 * Enumeration of the different cubies. Not including center cubies, which are immovable.
 * @author Harel Fuchs and Naama Baidatch
 */
public enum Cubie {
        
    /* Edge Cubies */
    /* 0  */ UF,
    /* 1  */ DF,
    /* 2  */ UB,
    /* 3  */ DB,
    /* 4  */ UR,
    /* 5  */ DR,
    /* 6  */ UL,
    /* 7  */ DL,
    /* 8  */ FR,
    /* 9  */ FL,
    /* 10 */ BR,
    /* 11 */ BL,
    
    /* Corner Cubies */
    /* 12 */ UFR,
    /* 13 */ UBL,
    /* 14 */ DFL,
    /* 15 */ DBR,
    /* 16 */ DLB,
    /* 17 */ DRF,
    /* 18 */ URB,
    /* 19 */ ULF,
    /* 20 */ NOF_CUBIES;

    public static Cubie nextCubie(Cubie cubie) {
        int idx = (cubie.ordinal() + 1) % NOF_CUBIES.ordinal();
        return values()[idx];
    }
    
    /**
     * @param cubie - name of cubie
     * @return reordered name of cubie by the convention<br>
     * for example, RUF ==> UFR
     */
    public static String reorderCubieName(String cubie){
        Cubie[] vals = values();
        for (int i = 0; i < vals.length; i++) {
            for (int j = 0; j < cubie.length(); j++) {
                if (vals[i].toString().indexOf(cubie.charAt(j)) == -1)
                        break;
                if (j+1 == cubie.length()) return vals[i].toString();
            }
        }
        return "";
    }
    
    public static boolean isCorner(Cubie c){
        return c.ordinal() > 11;
    }

    public static int getCubieOrientation(String cubie) {
        // corners have 3 possible orientations, egeds only 2.
        int modulu = isCorner(valueOf(reorderCubieName(cubie)))? 3 : 2;
        int last = 0; //the last facelet index that was used to update this cubie's orientation.
        int orient = 0;
        for (int idx = 0; idx < modulu; idx++) {
            String face = cubie.charAt(idx) + "";
            /*
             * When coming to determine a cubie's orientation, we look at its facelets in the order given
             * by its location name. For example, if the UFL cubie is found at the UFR location, we will call it ULF.
             * We now look at the "highest rank" representative of the cubie. The ranking order is RLFBUD. So in our
             * example the highest rank of ULF is U. The orientation will be the index of U in the string "ULF", which is 0. 
             */
            int i = "RLFBUD".indexOf(face);
            if (i > last){
                last = i;
                orient = idx;
            }  
        }
        return orient;
    }

	public static boolean cubieExists(String cubieName) {
		for (Cubie c : Cubie.values()) {
    		if (c.name().equals(cubieName)){
    			return true;
    		}
    	}
		return false;
	}
}
