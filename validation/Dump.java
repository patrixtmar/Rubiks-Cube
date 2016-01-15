package validation;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import rep.Cube;
import rep.CubieCube;
import rep.Side;
import rep.Side.SideName;

/**
 * printing to screen some cube data for debug purposes.
 * @author Harel Fuchs and Naama Baidatch
 */
public class Dump {
        
    public static void cubies(CubieCube cube) {
        System.out.println("Positions:");
        for (int i = 0; i < cube.positions.length; i++) {
            System.out.print(cube.positions[i]+" ");
        }
        System.out.println();
        System.out.println("Orientations:");
        for (int i = 0; i < cube.orientations.length; i++) {
            System.out.print(cube.orientations[i]+" ");
        }
        System.out.println();  
    }
    
    /**
     * @param cube
     * prints the cube to the screen
     */
    public static void cubeSides(Cube cube){
        System.out.println("\nPrinting Cube's Sides:");
        System.out.println("======================\n");
        
        System.out.println("UP:");
        side(cube.getSide(SideName.UP));
        System.out.println();
        
        System.out.println("LEFT:");
        side(cube.getSide(SideName.LEFT));
        System.out.println();
        
        System.out.println("RIGHT:");
        side(cube.getSide(SideName.RIGHT));
        System.out.println();
        
        System.out.println("FRONT:");
        side(cube.getSide(SideName.FRONT));
        System.out.println();
        
        System.out.println("BACK:");
        side(cube.getSide(SideName.BACK));
        System.out.println();
        
        System.out.println("DOWN:");
        side(cube.getSide(SideName.DOWN));
        System.out.println();
    }

    private static void side(Side side) {
        for(int i = 0 ; i < 9 ; i++){
            if (i%3 == 0) System.out.println();
            String col = (side.getSq()[i]).toString();
            System.out.print(col);
            spaces(10-col.length());
        }
        System.out.println();
    }

    private static void spaces(int times) {
        for (int j = 0; j < times; j++) {
            System.out.print(" ");
        }   
    }

	public static void locations(Cube c) {
		LCD.clear();
        LCD.drawString("up:"+c.up.location, 0, 0);
        LCD.drawString("down:"+c.down.location, 0, 1);
        LCD.drawString("front:"+c.front.location, 0, 2);
        LCD.drawString("back:"+c.back.location, 0, 3);
        LCD.drawString("left:"+c.left.location, 0, 4);
        LCD.drawString("right:"+c.right.location, 0, 5);
        Button.waitForAnyPress();
	}
   

}
