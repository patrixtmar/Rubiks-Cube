package rep;

import main.Main;
import main.Phase;
import rep.Side.SideName;

/**
 * a Rubik's cube representation for phase 1 (Reading)
 * representing a Cube by its 6 sides.
 * @author Harel Fuchs and Naama Baidatch
 */
public class Cube{

    public Side up; 
    public Side down; 
    public Side left; 
    public Side right;
    public Side front;
    public Side back; 
    
    /* 
     * A dummy cube's orientation won't be updated.
     */
	public boolean dummy = false;
   
    public Cube() {
        super();
        up = new Side(); 
        down = new Side(); 
        left = new Side(); 
        right = new Side();
        front = new Side();
        back = new Side();
    }
    
    /**
     * Initialize all sides with a 54 long string (9colors x 6sides).<br>
     * Chars: W(hite) R(ed) B(lue) Y(ellow) O(range) G(reen)<br>
     * Order of sides: URDLFB (Order of reading) <br>
     * <b><i>NOTE: WHEN USING THIS METHOD YOU NEED TO MANUALLY POSITION RED SIDE IN FRONT AND WHITE SIDE ON TOP</i></b>
     * @param sides - the string.
     */
    public Cube(String sides){
        super();
        int i=0;
        up = new Side(sides.substring(i,i+9));      i+=9;
        right = new Side(sides.substring(i,i+9));   i+=9;
        down = new Side(sides.substring(i,i+9));    i+=9;
        left = new Side(sides.substring(i,i+9));    i+=9;
        front = new Side(sides.substring(i,i+9));   i+=9;
        back = new Side(sides.substring(i,i+9));
    }
    
    /**
     * brings the cube to solved state using a string.
     * this also puts the locations in the classic positions. (White=up, Red=front)
     * used for debugging.
     */
    public void toSolvedStr(){
    	Cube cube = new Cube("WWWWWWWWWBBBBBBBBBYYYYYYYYYGGGGGGGGGRRRRRRRRROOOOOOOOO");
    	up = cube.up;
    	down = cube.down;
    	left = cube.left;
    	right = cube.right;
    	front = cube.front;
    	back = cube.back;
    }
    /**
     * puts the cube in the solved state.
     */
    public void toSolved(){
        up.fillWithColor(Color.White);
        down.fillWithColor(Color.Yellow);
        left.fillWithColor(Color.Green);
        right.fillWithColor(Color.Blue);
        front.fillWithColor(Color.Red);
        back.fillWithColor(Color.Orange);
    }

    public Side getSide(SideName side){
        switch (side){
        case UP:
            return up;
        case DOWN:
            return down;
        case LEFT:
            return left;
        case RIGHT:
            return right;
        case FRONT:
            return front;
        case BACK:
            return back;
        default:
            return null;
        }   
    }
 
    public void initializeSide(Side side, int idx){
        switch (idx){
        case 0:
            this.up = side;
            this.up.setLocation(SideName.UP);
            break;
        case 1:
            this.right = side;
            this.right.setLocation(SideName.RIGHT);
            break;
        case 2:
            this.down = side;
            this.down.setLocation(SideName.DOWN);
            break;
        case 3:
            this.left = side;
            this.left.setLocation(SideName.LEFT);
            break;
        case 4:
            this.front = side;
            this.front.setLocation(SideName.FRONT);
            break;
        case 5:
            this.back = side;
            this.back.setLocation(SideName.BACK);
            break;
        default:
            break;
        }
    }
    
    
/*  
 * Orientation Functions
 */
    
    /**
     * @param location (e.g. UP, DOWN, LEFT...)
     * @return name of the side that currently resides in this location. 
     */
    public SideName getSideByLocation(SideName location){
    	for (SideName side : SideName.values()){ 
    		if (getSide(side).getLocation().equals(location))
    			return side;
    	}
    	throw new IllegalArgumentException("Something is wrong with this cube - side "+location+" does not exist");
    }
    
    /**
     * 
     * @param location (e.g. UP, DOWN, LEFT...)
     * @return the cube's side that currently resides in this location. 
     */
    public Side getActualSideByLocation(SideName location){
    	return this.getSide(getSideByLocation(location));
    }
    
    /**
     * re-orient the cube upon spin/flip.
     * NOTE: This only comes to update which side resides where.
     *       It does NOT comes to update the inner-colors of each side. 
     * @param action = the action performed so re-orientation is needed
     * @param times = number of times the action was performed
     */
    public void updateOrientation(Actions action, int times) {
    	Side u,d,r,l,f,b;
    	if (dummy || Main.phase == Phase.READ) return;
        for (int i=0; i<times; i++){
        	u = getActualSideByLocation(SideName.UP);
        	d = getActualSideByLocation(SideName.DOWN);
        	r = getActualSideByLocation(SideName.RIGHT);
        	l = getActualSideByLocation(SideName.LEFT);
        	f = getActualSideByLocation(SideName.FRONT);
        	b = getActualSideByLocation(SideName.BACK);
	        switch (action){
	        case SPIN90CW:
                b.setLocation(SideName.RIGHT);
                l.setLocation(SideName.BACK);
                f.setLocation(SideName.LEFT);
                r.setLocation(SideName.FRONT);
                break;
            case SPIN90CCW:
                b.setLocation(SideName.LEFT);
                r.setLocation(SideName.BACK);
                f.setLocation(SideName.RIGHT);
                l.setLocation(SideName.FRONT);
                break;
            case FLIP:
                u.setLocation(SideName.LEFT);
                r.setLocation(SideName.UP);
                d.setLocation(SideName.RIGHT);
                l.setLocation(SideName.DOWN);
                break;
            default:
                break;
            }
        }
    }
	
	public Side getSideByCenterColor(Color color){
	    SideName sidename = getSideNameByCenterColor(color);
		return getSide(sidename);
	}
	
	public SideName getSideNameByCenterColor(Color color){
        Side currentSide;
        for (SideName sidename : SideName.values()){
            currentSide = getSide(sidename);
            if (currentSide.getCenter().equals(color))
                return sidename;
        }
        throw new IllegalArgumentException("Color number "+color+" does not exist");
    }
    
    /** 
     * Deduces cubies representation from Sides representation.
     * this must be called only after reading from cube is complete. 
     */
    public CubieCube cubieCubeFromSides(){
        CubieCube cube = new CubieCube();
        String[] cubies = new String[Cubie.NOF_CUBIES.ordinal()];
        
        // get cubie string representation
        // Edge cubies
        cubies[Cubie.UF.ordinal()] = colorsToCubie(up.sq[7],    front.sq[1],    null);
        cubies[Cubie.DF.ordinal()] = colorsToCubie(down.sq[1],  front.sq[7],    null);
        cubies[Cubie.UB.ordinal()] = colorsToCubie(up.sq[1],    back.sq[1],     null);
        cubies[Cubie.DB.ordinal()] = colorsToCubie(down.sq[7],  back.sq[7],     null);
        cubies[Cubie.UR.ordinal()] = colorsToCubie(up.sq[5],    right.sq[1],    null);
        cubies[Cubie.DR.ordinal()] = colorsToCubie(down.sq[5],  right.sq[7],    null);
        cubies[Cubie.UL.ordinal()] = colorsToCubie(up.sq[3],    left.sq[1],     null);
        cubies[Cubie.DL.ordinal()] = colorsToCubie(down.sq[3],  left.sq[7],     null);
        cubies[Cubie.FR.ordinal()] = colorsToCubie(front.sq[5], right.sq[3],    null);
        cubies[Cubie.FL.ordinal()] = colorsToCubie(front.sq[3], left.sq[5],     null);
        cubies[Cubie.BR.ordinal()] = colorsToCubie(back.sq[3],  right.sq[5],    null);
        cubies[Cubie.BL.ordinal()] = colorsToCubie(back.sq[5],  left.sq[3],     null);
        // Corner cubies
        cubies[Cubie.UFR.ordinal()] = colorsToCubie(up.sq[8],   front.sq[2],    right.sq[0]);
        cubies[Cubie.UBL.ordinal()] = colorsToCubie(up.sq[0],   back.sq[2],     left.sq[0]);
        cubies[Cubie.DFL.ordinal()] = colorsToCubie(down.sq[0], front.sq[6],    left.sq[8]);
        cubies[Cubie.DBR.ordinal()] = colorsToCubie(down.sq[8], back.sq[6],     right.sq[8]);
        cubies[Cubie.DLB.ordinal()] = colorsToCubie(down.sq[6], left.sq[6],     back.sq[8]);
        cubies[Cubie.DRF.ordinal()] = colorsToCubie(down.sq[2], right.sq[6],    front.sq[8]);
        cubies[Cubie.URB.ordinal()] = colorsToCubie(up.sq[2],   right.sq[2],    back.sq[0]);
        cubies[Cubie.ULF.ordinal()] = colorsToCubie(up.sq[6],   left.sq[2],     front.sq[0]);
        
        // configure positions
        for (int i = 0; i < cubies.length; i++) {
        	String cubieName = Cubie.reorderCubieName(cubies[i]);
        	if (Cubie.cubieExists(cubieName))
        		cube.positions[i] = Cubie.valueOf(cubieName);
            else
            	return null; // error occurred during read.  
        }
        
        // configure orientations
        for (int i = 0; i < cubies.length; i++) {
            cube.orientations[i] = Cubie.getCubieOrientation(cubies[i]);
        }
        
        return cube;
    }

    /**
     * 
     * @param color1
     * @param color2
     * @param color3 (optional, in case of corner cubie)
     * @return cubie of these colors, e.g. UFR
     */
    private String colorsToCubie(Color color1, Color color2, Color color3) {
        String cubie = "";
        cubie += getSideNameByCenterColor(color1).toString().charAt(0);
        cubie += getSideNameByCenterColor(color2).toString().charAt(0);
        if (color3 != null)
            cubie += getSideNameByCenterColor(color3).toString().charAt(0);
        
        //cubie = Cubie.reorderCubieName(cubie);
        return cubie;
    } 
	
}
