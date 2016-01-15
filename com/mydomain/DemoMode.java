package com.mydomain;

import com.mydomain.Kociemba;
import javax.swing.JOptionPane;
import javax.swing.JFrame;

import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
public class DemoMode {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//default icon, custom title
		// create a jframe
	    JFrame frame = new JFrame("JOptionPane showMessageDialog example");
	    Object[] options = {"Kociemba","Thistlethwaite","Demo Mode","Scramble Cube"};
	    int n = JOptionPane.showOptionDialog(frame,
	    	    "Which program would you like to run?",
	    	    "An Important Question",
	    	    JOptionPane.INFORMATION_MESSAGE, 0, 
	    	    null,     //do not use a custom Icon
	    	    options,  //the titles of buttons
	    	    options[0]); //default button title	    
        switch (n) {
        case 0: 
        	 JOptionPane.showMessageDialog(null, "1. Power off the NXT Brick by pressing dark grey button. \n2. Reset Color Sensor, Arm and Turntable. \n3. Power on the NXT Brick by pressing large orange button. \n4. Place cube White side up and Green side closest to Arm.");
        	 Kociemba.main(null); 
             break;
        case 1: 
          	 JOptionPane.showMessageDialog(null, "1. Power off the NXT Brick by pressing dark grey button. \n2. Reset Color Sensor, Arm and Turntable. \n3. Power on the NXT Brick by pressing large orange button. \n4. Place cube Yellow side up and Blue side closest to Arm.");
        	 Thistlethwaite.main(null); 
             break;
        case 2: 
        	 JOptionPane.showMessageDialog(null, "1. Power off the NXT Brick by pressing dark grey button. \n2. Reset Color Sensor, Arm and Turntable. \n3. Power on the NXT Brick by pressing large orange button. \n4. Place cube White side up and Green side closest to Arm.");
        	 int i = 1;
        	 TouchSensor touch = new TouchSensor(SensorPort.S2);
        	 while (i<1000) { 
                  if (touch.isPressed())
        	        	  arm.main(null);
                  
        	 }
        case 3: 
         	 JOptionPane.showMessageDialog(null, "1. Power off the NXT Brick by pressing dark grey button. \n2. Reset Color Sensor, Arm and Turntable. \n3. Power on the NXT Brick by pressing large orange button. \n4. Place cube White side up and Green side closest to Arm.");
       	     Scramb.main(null); 
             break;
            }
     //System.exit(0);
	}
}
