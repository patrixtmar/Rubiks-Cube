package global;

import java.util.ArrayList;
import java.util.List;

import rep.Actions;
import rep.Color;
import rep.Cube;
import rep.Cubie;
import rep.CubieCube;
import rep.Side;

/**
 * Global utilities class
 * @author Harel Fuchs and Naama Baidatch
 */
public class Utils {
	
	static public int sizeOfSide = 3;
	
	public static int translateColorToInt(Color color){
    	switch (color){
	    case Red:
			return 0;
		case Green:
			return 1;
		case Blue:
			return 2;
		case Yellow:
			return 3;
		case Orange:
			return 5;
		case White:
			return 6;
		default:
			return -1;
    	}
	}
	
	public static Color translateIntToColor(int color){
		switch (color){
		case 0:
			return Color.Red;
		case 1:
			return Color.Green;
		case 2:
			return Color.Blue;
		case 3:
			return Color.Yellow;
		case 5:
			return Color.Orange;
		case 6:
			return Color.White;
		default:
			return Color.Black;
		}
	}
    
    /**
     * @param p - starting index for cubies.positions
     * @param c - cube
     * @return a number (0..23) that represents the permutation of the first 4
     * cubies from index p.
     */
    public static int permtonum(CubieCube c, int p){
        int n=0;
        for (int a=0; a<4; a++) {
            n*=4-a;
            for(int b=a; ++b<4; )
                if (c.positions[p+b].ordinal() < c.positions[p+a].ordinal()) 
                    n++;
        }
        return n;
    }

    /**
     * @param cube
     * @param n id of a move of length 4 (e.g. URRL)
     * @param offset - in the cube's positions array<br>
     * Will perform on cube the permutation represented by the number.
     */
    public static void numtoperm(CubieCube cube, int n, int offset){
        cube.positions[offset+3] = Cubie.values()[offset];
        for (int a=2; a >- 1 ; a--){
            cube.positions[offset + a] = Cubie.values()[n % (4-a) + offset];
            n /= 4-a;
            for (int b = a; ++b<4; )
                if ( cube.positions[offset+b].ordinal() >= cube.positions[offset+a].ordinal()) 
                    cube.positions[offset+b] = Cubie.nextCubie(cube.positions[offset+b]);
        }
    }
    
    /**
     * NXT uses older version of java before this method was introduced to String.
     * @param str
     * @param delimiter
     * @return
     */
    public static String[] split(String str, char delimiter){
    	List<String> lst = new ArrayList<String>();
    	String s = "";
    	for (int i = 0; i < str.length(); i++) {
			char next = str.charAt(i);
			if (next == delimiter && !s.equals("")){
			lst.add(s);
				s = "";
			} else if (next != delimiter){
				s += next;
			}
		}
   	if (!s.equals("")) lst.add(s);
    	return lst.toArray(new String[0]);
   }

	public static char serializeAction(Actions action) {
		switch (action){
		case BSTB_B:
			return '1';
		case BSTB_D:
			return '2';
		case BSTB_F:
			return '3';
		case BSTB_L:
			return '4';
		case BSTB_R:
			return '5';
		case BSTB_U:
			return '6';
		case SPIN_BOTTOM_180:
			return '7';
		case SPIN_BOTTOM_90CCW:
			return '8';
		case SPIN_BOTTOM_90CW:
			return '9';
		default: 
			throw new IllegalArgumentException("Error: action "+action+" should not be called");		
		}
	}

	public static String serializeSolution(Actions[] robotSolution){
		String ret = "";
		for (int i = 0; i < robotSolution.length; i++) {
			ret += serializeAction(robotSolution[i]);
		}
		return ret;
	}
	
	public static Actions deserializeAction(char action) {
		switch (action){
		case '1':
			return Actions.BSTB_B;
		case '2':
			return Actions.BSTB_D;
		case '3':
			return Actions.BSTB_F;
		case '4':
			return Actions.BSTB_L;
		case '5':
			return Actions.BSTB_R;
		case '6':
			return Actions.BSTB_U;
		case '7':
			return Actions.SPIN_BOTTOM_180;
		case '8':
			return Actions.SPIN_BOTTOM_90CCW;
		case '9':
			return Actions.SPIN_BOTTOM_90CW;
		default:
			throw new IllegalArgumentException("Error: string "+action+" is invalid");
		}
	}

	/**
	 * @param cube
	 * @return
	 */
	
	public static String serializeCube(Cube cube) {
		String ret = "";
    	Color[] currentSquares;
		for (Side.SideName side : Side.SideName.values()) {
			currentSquares = cube.getSide(side).getSq();
			for (int j = 0; j < currentSquares.length; j++) {
				ret+=Utils.serializeColor(currentSquares[j]);				
			}
		}
		return ret;
	}

	public static char serializeColor(Color color){
		switch (color){
		case Blue:
			return '1';
		case Green:
			return '2';
		case Orange:
			return '3';
		case Red:
			return '4';
		case White:
			return '5';
		case Yellow:
			return '6';
		default:
			throw new IllegalArgumentException("Error: Illegal color");	
		}
	}

	public static Cube deserializeCube(String cube){
		Cube ret = new Cube();
		int counter = 0;
		for (Side.SideName side : Side.SideName.values()) {
			Color[] currentSquares = ret.getSide(side).getSq();
			for (int j = 0; j < currentSquares.length; j++) {
				currentSquares[j] = Utils.deserializeColor(cube.charAt(counter++));
			       //System.out.println(currentSquares[j]); 
			}
			ret.getSide(side).setCenter(currentSquares[4]);
		}
		return ret;
	}

	public static Color deserializeColor(char color){
		switch (color){
		case '1':
			return Color.Blue;
		case '2':
			return Color.Green;
		case '3':
			return Color.Orange;
		case '4':
			return Color.Red;
		case '5':
			return Color.White;
		case '6':
			return Color.Yellow;
		default:
			throw new IllegalArgumentException("Error: no such color");
		}
	}

	public static Actions[] actionListToArray(List<Actions> list) {
		Actions[] ret = new Actions[list.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = list.get(i);
		}
		return ret;
	}
	
	/**
	 * returns whether a given permutation has a positive signature (even parity)
	 */
	public static boolean isPermutationEven(int[] numbers){
		int count = 0;
		for (int i = 0; i < numbers.length; i++) {
			if (numbers[i] != i){
				for (int j = i+1; j < numbers.length; j++) {
					if (numbers[j] == i){
						// swap i and j
						int t = numbers[i];
						numbers[i] = numbers[j];
						numbers[j] = t;
						break;
					}
				}
				// now numbers[i] == i
				count ++;
			}
		}
		return (count % 2 == 0);
	}
   
}
