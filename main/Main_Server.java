package main;
import global.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import rep.Cube;
import rep.CubieCube;
import solution.Thistle;
import validation.Dump;
import validation.Sanity;

/**
 * Server main class
 * @author Harel Fuchs and Naama Baidatch
 *
 */
public class Main_Server {

	static NXTComm nxtComm = null;

    public static void main_server() throws IOException {
        while (true)
        {
	        /*
	         * POLLING FOR CONNECTION WITH ROBOT
	         */
        	if (!connectToRobot()) continue;
    		System.out.println("Connected to NXT");

        	/*
	         * GETTING CUBE FROM ROBOT
	         */
			String result = getFromRobot(54);
	    	Cube cube = Utils.deserializeCube(result);
	    	Dump.cubeSides(cube);

	        /*
	         *  COMPUTE SOLUTION
	         */
	        Main.phase = Phase.SOLVE;
	        CubieCube cc = cube.cubieCubeFromSides();
	        if (cc == null || !Sanity.validateAllCubies(cc) || !Sanity.isCubeConfigurationValid(cc)) {
	        	// cube is not valid, need to re-read everything.
	        	System.out.println("cube invalid. re-reading.");
	        	Thistle.robotSolution = null;
	        }
	        else {
	        	Thistle.solve(cc);
	        }

	        /*
	         * SEND SOLUTION TO ROBOT.
	         * NOTE: server might return "cube invalid" (null)
	         */
	        String solution = null;
	        if (Thistle.robotSolution != null){
	        	solution = "s" + Utils.serializeSolution(Thistle.robotSolution) + "x";  // s = start, x = end
	        } else {
	        	solution = "E"; // E for Error, cube invalid
	        }
			sendToRobot(solution);
	        if (solution.equals("E"))
	        	continue;
    	
	    	break;
        }

        //done
		nxtComm.close();
    }

    private static boolean connectToRobot() {
    	if (nxtComm == null){
    		try {
    			nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
            	NXTInfo[] nxts = nxtComm.search(null);
            	if (nxts == null || nxts.length == 0){
            		nxtComm = null;
            		return false;
            	}
            	NXTInfo nxt = nxts[0];
            	nxtComm.open(nxt);
    		} catch (NXTCommException e1) {
    			e1.printStackTrace();
    			return false;
    		}
    	}
    	return true;
	}

  
    private static String getFromRobot(int length) throws IOException {
    	DataInputStream in = new DataInputStream(nxtComm.getInputStream());
		String result = "";
		
		Character curr = null;
		try {
			while((curr = in.readChar()) == null); // wait until robot sends cube
		} catch (IOException e) {
			throw new IOException("IO Exception. Make sure you first run the NXT program, and only after it finished loading, run the PC program.");
		}
		
		result += curr;
		if (result.length() == length) return result;
		while(true){
			try {
				while ((curr = in.readChar()) != null) {
					result += curr; // read cube from robot
					if (result.length() == length){
						break;
					}
				}
				break;
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("\nWill try again in 1 second");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}

		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

    private static void sendToRobot(String msg) throws IOException {
    	DataOutputStream out = new DataOutputStream(nxtComm.getOutputStream());
		out.writeChars(msg);
		out.flush();
		out.close();
	}
}
