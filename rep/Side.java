package rep;

/**
 * A representation for a side of the cube.
 * A side is represented by its nine colors.
 * @author Harel Fuchs and Naama Baidatch
 *
 */
public class Side {
    
    public enum SideName {
        UP, DOWN, LEFT, RIGHT, FRONT, BACK;
    }
    
    /* color of the center */
    private Color center; 
    
    /* location of the side throughout the program */
    public SideName location;
    
    /**
     * Square Structure: Each side is a 9-long array of colors
     * 
     *         012
     *         3U5
     *         678
     *         
     *    012  012  012  012
     *    3L5  3F5  3R5  3B5
     *    678  678  678  678
     *         
     *         012
     *         3D5
     *         678
     */
    Color[] sq; // squares
    
    public Side(){
        sq = new Color[9];
    }
    
    public Side(Color[] sq) {
        super();
        this.setCenter(sq[4]);
        this.sq = sq;
    }
	
    /**
     * Initialize side with a string.
     * Chars: W(hite) R(ed) B(lue) Y(ellow) O(range) G(reen)
     * @param sides - the string.
     */
	public Side(String side) {
		sq = new Color[9];
        for (int i = 0; i < side.length(); i++) {
            sq[i] = charToColor(side.charAt(i));
        }
        setCenter(sq[4]);
        location = colorToDefaultLocation(sq[4]);
    }

    public void setSq(Color[] sq) {
        for (int i=0; i<9; i++){
            this.sq[i] = sq[i];
        }
    }

	public SideName getLocation() {
		return location;
	}

	public void setLocation(SideName location) {
		this.location = location;
	}
	
	public Color[] getSq() {
        return sq;
    }

	public Color getCenter() {
		return center;
	}
	
	public void setCenter(Color center) {
		this.center = center;
	}

    public void fillWithColor(Color color) {
        for (int i=0; i<9; i++){
            this.sq[i] = color;
        }
        setCenter(color);
    }
    
    private static Color charToColor(char c) {
        switch(c){
        case 'W': return Color.White;
        case 'Y': return Color.Yellow;
        case 'R': return Color.Red;
        case 'O': return Color.Orange;
        case 'B': return Color.Blue;
        case 'G': return Color.Green;
        }
        return null;
    }
    
    private static SideName colorToDefaultLocation(Color color) {
        switch(color){
        case White: return SideName.UP;
        case Yellow: return SideName.DOWN;
        case Red: return SideName.FRONT;
        case Orange: return SideName.BACK;
        case Blue: return SideName.RIGHT;
        case Green: return SideName.LEFT;
		case Black:
		default:
			break;
        }
        return null;
    }
}
