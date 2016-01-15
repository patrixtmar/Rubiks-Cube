package solution;

import java.util.ArrayList;
import java.util.List;

//import com.mydomain.Thistlethwaite;

import rep.Actions;
//import rep.Color;
import rep.Cube;
import rep.Side.SideName;

//import robo.Arm;
//import robo.Tray;
//import tests.*;

/**
 * Solution utilities
 * @author Harel Fuchs and Naama Baidatch
 */
public class SolUtils {

    public static Actions[] humanToRobo(){
        List<Actions> actions = new ArrayList<Actions>();
        String[] sol = Thistle.solution.toArray(new String[0]);
        
        Actions[] spinArray = {Actions.SPIN_BOTTOM_90CW, Actions.SPIN_BOTTOM_180, Actions.SPIN_BOTTOM_90CCW};
                
        for (int i = 0; i < sol.length; i++) {
            char side = sol[i].charAt(0);
            int times = sol[i].charAt(1) - 48; //48 is the offset of '0'
            Actions bstb = Actions.valueOf("BSTB_"+side);
            Actions spin = spinArray[times - 1];
            actions.add(bstb);
            actions.add(spin);
        }
        Actions[] acts = actions.toArray(new Actions[0]);
        
        return acts;
    }

    /**
     * Adding "current" move to the solution, compressing two subsequent moves of the same face if possible.
     * @param current
     */
    public static void addToSolution(String current) {
		int lastIdx = Thistle.solution.size()-1;
    	//String NewCurrent = "";
		if (lastIdx < 0) {
		       Thistle.solution.add(current);
               ///NewCurrent = TranslateCube(current);
               //Thistlethwaite.StoreTranslatedSolution(NewCurrent);
               //TestCube.StoreTranslatedSolution(NewCurrent);
			return;
		}
		String last = Thistle.solution.get(lastIdx);
		if (current.charAt(0) == last.charAt(0)){ // we can compress it
			int times = (current.charAt(1) + last.charAt(1)) %4;
			Thistle.solution.remove(lastIdx);
			if (times>0){
				Thistle.solution.add(current.charAt(0) + "" + times);
	               //NewCurrent = TranslateCube(current);
	               //NewCurrent = TranslateCube(current.charAt(0) + "" + times);
	               //Thistlethwaite.StoreTranslatedSolution(NewCurrent);
	               //TestCube.StoreTranslatedSolution(NewCurrent);
			}
		} else {
			Thistle.solution.add(current);
            //NewCurrent = TranslateCube(current);
            //Thistlethwaite.StoreTranslatedSolution(NewCurrent);
            //TestCube.StoreTranslatedSolution(NewCurrent);
		}	
	
    }
    
    public static void printSolution() {
    	//List<String> storedsolution;
        //System.out.println("SOLUTION:\n=========");
        //System.out.println(Thistle.solution);
        //storedsolution = Thistle.solution;
        //String[] strarray = storedsolution.toArray(new String[0]);
        //System.out.println(strarray);
        //int human = Thistle.solution.size();
        //System.out.println("done in " + human + " human moves.");
        //int robot = 0;
        //Cube cube = new Cube(); cube.toSolvedStr(); // solved cube
        //for (Actions act : Thistle.robotSolution) {
        //    robot += countActions(cube, act);
        //}
        
        //System.out.println("done in " + robot + " robo moves.");
        
        //int overhead = robot - human;
        //System.out.format("overhead: %d (%.2f per move)\n", overhead, (double) overhead/human);
    }

    public static String TranslateCube(String StringMove){
		String RetValue = "";
		switch (StringMove){
		case "U1": RetValue = "D"; break;
		case "U2": RetValue = "D2"; break;
		case "U3": RetValue = "D'"; break;
		case "D1": RetValue = "U"; break;
		case "D2": RetValue = "U2"; break;
		case "D3": RetValue = "U'"; break;
		case "L1": RetValue = "R"; break;
		case "L2": RetValue = "R2"; break;
		case "L3": RetValue = "R'"; break;
		case "R1": RetValue = "L"; break;
		case "R2": RetValue = "L2"; break;
		case "R3": RetValue = "L'"; break;
		case "F1": RetValue = "B"; break;
		case "F2": RetValue = "B2"; break;
		case "F3": RetValue = "B'"; break;
		case "B1": RetValue = "F"; break;
		case "B2": RetValue = "F2"; break;
		case "B3": RetValue = "F'"; break;
		}
       return RetValue;
    }    
    
    
    public static int countActions(Cube cube, Actions next) {
        switch (next) {
        case BSTB_U:
            return countBringSideToBottom(cube, SideName.UP);
        case BSTB_D:
            return countBringSideToBottom(cube, SideName.DOWN);
        case BSTB_F:
            return countBringSideToBottom(cube, SideName.FRONT);
        case BSTB_B:
            return countBringSideToBottom(cube, SideName.BACK);
        case BSTB_R:
            return countBringSideToBottom(cube, SideName.RIGHT);
        case BSTB_L:
            return countBringSideToBottom(cube, SideName.LEFT);
        default:
            return 1;
        }
    }

    public static int countBringSideToBottom(Cube cube, SideName sideName){
        /* First we need to find where our desired side is */
        SideName location = cube.getSide(sideName).location;
        switch (location){
        case UP:
            cube.updateOrientation(Actions.FLIP, 2);
            return 2;
        case LEFT:
            cube.updateOrientation(Actions.FLIP, 1);
            return 1;
        case DOWN:
            return 0;
        case RIGHT:
            cube.updateOrientation(Actions.FLIP, 3);
            return 3;
        case FRONT:
            cube.updateOrientation(Actions.SPIN90CW, 1);
            cube.updateOrientation(Actions.FLIP, 1);
            return 2;
        case BACK:
            cube.updateOrientation(Actions.SPIN90CCW, 1);
            cube.updateOrientation(Actions.FLIP, 1);
            return 2;
        }
        return 0;
    }
}
