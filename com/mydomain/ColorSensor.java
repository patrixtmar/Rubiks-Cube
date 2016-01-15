package com.mydomain;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;

//import lejos.util.Delay;

public class ColorSensor {
    /**
     * @param args
     */
    public static void main(String[] args) {

//    	static float cut_black = 0.0f;
 //   	static float cut_green = 0.0f;
  //  	static float cut_bw = 0.0f;


            //Delay.msDelay(600); // Wait 500 ms to make sure that the sensor is not moving.

    	    LightSensor light = new LightSensor(SensorPort.S1);
    		light.setFloodlight(true);
    		
    		LCD.clear();
    		LCD.drawString("Calibrate colors", 0, 0);
    		
    		LCD.drawString("White [ENTER]", 0, 2);
    		int white = light.readNormalizedValue();
    		Sound.beepSequenceUp();
    		
    		LCD.drawString("Black [ENTER]", 0, 3);
    		int black = light.readNormalizedValue();
    		Sound.beepSequenceUp();

    		Sound.beep();
    		LCD.drawString("Green [ENTER]", 0, 4);
    		int green = light.readNormalizedValue();
    		Sound.beepSequenceUp();

    		// Listing colors by decreasing reflective values
    		LCD.clear();
    		LCD.drawString("Color codes", 0, 0);
    		LCD.drawString("white = " + white, 0, 2);
    		LCD.drawString("green = " + green, 0, 3);
    		LCD.drawString("black = " + black, 0, 4);
    		
    //		cut_black = (black + green) / 2.0f;
    //		cut_green = (green + white) / 2.0f;
    //		cut_bw = (black + white) / 2.0f;


    	    
//    	    while (true) {
 //      	      LCD.drawInt(light.getLightValue(), 4, 0, 0);
  //   	      LCD.drawInt(light.getNormalizedLightValue(), 4, 0, 1);
   // 	      LCD.drawInt(SensorPort.S1.readRawValue(), 4, 0, 2);
   // 	      LCD.drawInt(SensorPort.S1.readValue(), 4, 0, 3);
    //	    }
    	}
    }
