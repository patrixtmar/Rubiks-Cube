package com.mydomain;

import lejos.nxt.Motor;
import lejos.util.Delay;

public class arm {
    /**
     * @param args
     */
    
arm()
{
    //readtopface();
	//rotate90();
	//rotateb90();
    adjustcolorsensorarm();
    flipcube();
    readtopface();
	lowerarm();
    raisearm();
    rotate90();
    rotate90();
    rotate90();
    rotate90();
	resetcolorsensorarm();
	//flipcube();
    //resetpower();
}

void flipcube()
{
//  Flip Cube
    	Motor.C.setPower(25);
        Motor.C.rotate(88);
        Delay.msDelay(200); // Wait 200 ms to make sure the cube is settled.
        Motor.C.rotate(-88);
        Delay.msDelay(200); // Wait 200 ms to make sure the cube is settled.
        //Motor.C.rotate(-10);
        Delay.msDelay(200); // Wait 200 ms to make sure the cube is settled.
}

void lowerarm()
{        
        
        //  Hold for cube turn
   	    Motor.C.setPower(25);
        Motor.C.rotate(30);
        Delay.msDelay(200); // Wait 200 ms to make sure the cube is settled.
}
void raisearm()
{       
   	    Motor.C.setPower(25);
    	Motor.C.rotate(-30);
        Delay.msDelay(200); // Wait 200 ms to make sure the cube is settled.
        //Motor.C.rotate(-10);
        Delay.msDelay(200); // Wait 200 ms to make sure the cube is settled.
}      

void adjustcolorsensorarm()
{
	  Motor.B.setPower(25);
	  Motor.B.rotateTo(40);
}
void resetcolorsensorarm()
{
	Motor.B.setPower(25);
	Motor.B.rotateTo(0);
}

void rotate90() // Rotates the turntable by 90 degrees.
{ 
	  //Motor.B.rotateTo(20);
      Motor.A.setPower(35); 
      Motor.A.rotate(-95); 
      Motor.A.rotate(5); 
}      

void rotateb90() // Rotates the turntable by 90 degrees.
{ 
	  //Motor.B.rotateTo(20);
      Motor.A.setPower(35); 
      Motor.A.rotate(95); 
      Motor.A.rotate(5); 
}      


void readtopface()
{
    Motor.B.setPower(40); 
	Motor.B.rotateTo(141); // Rotate to the middle facelet and get it's according side.
    Motor.B.rotateTo(40);
}

void resetpower()
{
	Motor.A.setPower(0);
	Motor.B.setPower(0);
	Motor.C.setPower(0);
}

public static void  main(String[] args) { 
	  new arm(); 
	  } 
    

}

