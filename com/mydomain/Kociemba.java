package com.mydomain;
///// Rubik's Cube Solver \\\\\
//Matura paper Kantonschule Sargans
//Author: Kevin Jörg
//Supervisor: Thomas Büsser

//package com.mydomain; // removed by CM 

//RequiRedValue from Lejos

import org.kociemba.twophase.*;  //Herbert Kociemba Two-Phase Algorithm from http://kociemba.org 
import lejos.nxt.Motor; // From lejos.org
import lejos.nxt.SensorPort; // From lejos.org
import lejos.util.*;  // From lejos.org
//import solution.Thistle;
import lejos.nxt.addon.ColorHTSensor; // From lejos.org

public class Kociemba { 

// ----------- Environment Variables ------------------------------------------
// ----------- Updatable Variables to adjust speed of solving the cube --------
int maxDepth = 22;  //Sets the maximum amount of moves to start the solution
long timetosearch = 15; //The time(seconds) to find a solution.
int maxTime = 15; // Max time to search for solution
int MotorRotatePower = 35; // Adjust motor rotation power
int ArmPower = 15;  // Adjust arm rotation power
int DebugSetting = 0; // Change to 1 to for debugging values

// ------------ Static Cube Variables -----------------------------------------
int U = 1;  // Variable for White, Up 
int R = 2;  // Variable for Red, Right
int F = 3;  // Variable for Green, Front
int D = 4;  // Variable for Yellow, Down
int L = 5;  // Variable for Orange, Left
int B = 6;  // Variable for Blue, Back
int X = 0;  // Variable for a side when it is reassigned

//Variables for each of the 9 facelets to each side.
String f1 = "";  
String f2 = ""; 
String f3 = ""; 
String f4 = ""; 
String f5 = ""; 
String f6 = ""; 
String f7 = ""; 
String f8 = ""; 
String f9 = ""; 

/*
   * The first digit equals the color on top, the second digit equals the color in right. Used to assign the side which h
as to be turned to the proper cube position.
   * 1 = white U
   * 2 = red R
   * 3 = green F
   * 4 = yellow D
   * 5 = orange L
   * 6 = blue B
   */

// ------------- Variables to manage cube solving process --------------------
String side = "";  //Current Side
String side1 = "";  // New Side 
String cubeString = "";  // All facelets on all side concatenated together
String cubeStringU = "";  // All facelets on side U concatenated together
String cubeStringR = "";  // All facelets on side R concatenated together
String cubeStringF = "";  // All facelets on side F concatenated together
String cubeStringD = "";  // All facelets on side D concatenated together
String cubeStringL = "";  // All facelets on side L concatenated together
String cubeStringB = "";  // All facelets on side B concatenated together
String printcubeString = "";  // All facelets on all side concatenated together
String printcubeStringU = "";  // All facelets on side U concatenated together
String printcubeStringR = "";  // All facelets on side R concatenated together
String printcubeStringF = "";  // All facelets on side F concatenated together
String printcubeStringD = "";  // All facelets on side D concatenated together
String printcubeStringL = "";  // All facelets on side L concatenated together
String printcubeStringB = "";  // All facelets on side B concatenated together
String result = "";  // The solution from Kaciemba two phase algorithm
String fresult = ""; // The solution from Kaciemba two phase algorithm
String move = "";  // Current solving more being performed
String movemodifier = ""; // Move modifier to turn the side multiple times or backwards
boolean useSeparator = false; // The use of a separator, (default is space)        
long timesearching = 0;   
long timestart = 0; 
long timeend = 0;        
int substringStart = 0; // String start for move
int substringEnd = 1;  // String end for move
int substringStart2 = substringStart +1; // Next string start for next move
int substringEnd2 = substringEnd +1;  // Next string end for the next move
int ColorIDValue = 0;  // ColorIDValue returned from HT color sensor 
int RedValue = 0; // RedValue value returned from HT color sensor
int BlueValue = 0; // BlueValue value returned from HT color sensor
int GreenValue = 0; // GreenValue value returned from HT color sensor
long startscan;
long startcalculate;
long startsolve;
long endtime;


public static String PrintTranslatedSolution="";
public static int NumOfMoves=0;


Kociemba() 
  { 
 	 startscan = System.currentTimeMillis();
     ScanCube();  // Scan the full cube
     if (DebugSetting == 1)
     {
       System.out.println("Executed ScanCube()"); 
     }
    //cubeString="RBFLULFLLDBUFRBBBRDUBLFDUDRLRDFDRBRUDFLFLDURBLDFUBUFUR";
       Motor.B.rotateTo(20); //Move color sensor arm out of way for cube movement
       startcalculate = System.currentTimeMillis();

       Calculate(); // Calculate solution use Kaciemba two phase algorithm
        //fresult="U L F' R' U B' R2 F D F L' B U D F2 D F2 L2 F2 U' B2 L2  ";
     if (DebugSetting == 1)
     {
       System.out.println("Executed Calculate()"); 
     }
     System.out.println("----------------------- Kociemba Test Start ------------------------------");
     System.out.println("    " + printcubeStringU.substring(0, 3));
     System.out.println("    " + printcubeStringU.substring(3, 6));
     System.out.println("    " + printcubeStringU.substring(6, 9));
     System.out.println(printcubeStringL.substring(0, 3) + " " + printcubeStringF.substring(0, 3) + " " + printcubeStringR.substring(0, 3)+ " " + printcubeStringB.substring(0, 3));
     System.out.println(printcubeStringL.substring(3, 6) + " " + printcubeStringF.substring(3, 6) + " " + printcubeStringR.substring(3, 6)+ " " + printcubeStringB.substring(3, 6));
     System.out.println(printcubeStringL.substring(6, 9) + " " + printcubeStringF.substring(6, 9) + " " + printcubeStringR.substring(6, 9)+ " " + printcubeStringB.substring(6, 9));
     System.out.println("    " + printcubeStringD.substring(0, 3));
     System.out.println("    " + printcubeStringD.substring(3, 6));
     System.out.println("    " + printcubeStringD.substring(6, 9));

     startsolve = System.currentTimeMillis();
     SolveCube(); // The cube is getting solved. 
     if (DebugSetting == 1)
     {
       System.out.println("Executed SolveCube()"); 
     }
       Motor.B.rotateTo(0); // Resets color sensor arm for next scan
       endtime = System.currentTimeMillis();
       System.out.println("Scan Time             = " + (startcalculate - startscan)/1000 + " Seconds or " + (startcalculate - startscan) + " MilliSeconds");
       System.out.println("Calculate Time        = " + (startsolve - startcalculate)/1000 + " Seconds or "+ (startsolve - startcalculate) + " MilliSeconds");
       System.out.println("Solve Time            = " + (endtime - startsolve)/1000 + " Seconds or " + (endtime - startsolve) + " MilliSeconds");
       System.out.println("Total Time            = " + (endtime - startscan)/1000 + " Seconds or "+ (endtime - startscan) + " MilliSeconds");
       System.out.println("Number of human moves = " + NumOfMoves);
       System.out.println("Solution              = " + PrintTranslatedSolution);
       System.out.println("----------------------- Kociemba Test Finish ------------------------------");
       System.out.println("----------------------Copy Results to File---------------------------------");
 } 

//  All Moves 

public static void  PrintStoreTranslatedSolution(String PrintnewSolution)
{
        
        
	    if (PrintnewSolution != "") 
        	{
              PrintTranslatedSolution += PrintnewSolution += " ";
        	  NumOfMoves++;
        	}
}

public void RotateCubeSides(int NoSides)
{
    if (NoSides > 0);
    {
       int i = 1;
       while (i <= NoSides)
    	{
          X = F; // Reassign variables for F, B, L and R. X is used to store the current value of F.
          F = R; 
          R = B; 
          B = L; 
          L = X; 
          Motor.A.setPower(MotorRotatePower); 
          Motor.A.rotate(-90); 
          i++;		
	    }
    }
    if (NoSides < 0);
    {
       int i = 1;
       while (i <= (NoSides*-1))
    	{
           X = F; // Reassign variables for F, B, L and R. X is used to store the current value of F.
           F = L; 
           L = B; 
           B = R; 
           R = X; 
           Motor.A.setPower(MotorRotatePower); 
           Motor.A.rotate(90); 
          i++;		
	    }
    }
}

void Flip() // Turns the cube with the arm.
  { 
        X = F; // Reassign the variables F, D, B and U. X is used to store the current value of F.
        F = D; 
        D = B; 
        B = U; 
        U = X;       
    	Motor.C.setPower(ArmPower);
        Motor.C.rotate(88); // Moves the arm and flips the cube.
        Delay.msDelay(200); // Wait for cube to stop.
        Motor.C.rotate(-88); //Move the arm back to original position.
        Delay.msDelay(200); // Wait for cube to stop.

  } 

public void RotateBottomRow(int NoSides)
{
    if (NoSides > 0);
    {
       int i = 1;
       while (i <= NoSides)
    	{
          Motor.A.setPower(MotorRotatePower); 
          Motor.A.rotate(95); 
          Motor.A.rotate(-5); 
          i++;		
	    }
    }
    if (NoSides < 0);
    {
       int i = 1;
       while (i <= (NoSides*-1))
    	{
          Motor.A.setPower(MotorRotatePower); 
          Motor.A.rotate(-95); 
          Motor.A.rotate(5); 
          i++;		
	    }
    }
}




void LowerArm() // Lowers the arm in position to changed the bottom row of cube. 
  { 
	    Motor.C.setPower(ArmPower);
        Motor.C.rotate(30);
  } 

void RaiseArm() // Raises the arm back in original position.  This allows for normal movement of the turntable.
  { 
        Motor.C.setPower(ArmPower); //setPower 
        Motor.C.rotate(-30); 
  } 

////Method to assign a color to a side \\\\ 
 @SuppressWarnings("static-access") 
 public String ScanColor() // Returns the side which the facelet be-longs to in a string.
  { 
        Delay.msDelay(300); // Wait 200 ms to make sure that the sensor is not moving.
        ColorHTSensor color = new ColorHTSensor(SensorPort.S1); 
        ColorIDValue = color.getColorID(); // The method getColortID() returns a number between 0 and 16 for different colors.
        GreenValue = color.getRGBComponent(color.GREEN); // The GreenValue value is used to distinguish between orange and RedValue, because it's not possible to differentiate RedValue and orange with getColorID(). 
        RedValue = color.getRGBComponent(color.RED); // The RedValue value is used to distinguish between orange and RedValue, because it's not possible to differentiate RedValue and orange with getColorID(). 
        BlueValue = color.getRGBComponent(color.BLUE); // The BlueValue value is used to distinguish between orange and RedValue, because it's not possible to differentiate RedValue and orange with getColorID(). 

        String side = ""; 
      
        // Assign the color sensor values to the according side.
        if (DebugSetting == 1)
        {
            System.out.println("ColorIDValue="+String.valueOf(ColorIDValue)+": GreenValue="+GreenValue+", RedValue="+RedValue+",BlueValue="+BlueValue); 
        }
        if (ColorIDValue == 1) //Front, Green
        { 
              side = "F"; 
        } 
        if (ColorIDValue == 4) //Blue
        { 
              side = "B"; 
        } 
        if (ColorIDValue == 3 || ColorIDValue == 6)  //White or Yellow
        {
            if (BlueValue <= 60) side = "D"; 
            else side = "U"; 
     	
        }
        if (ColorIDValue == 0 || ColorIDValue == 5)  //Red or Orange
        {
            if (GreenValue <= 150) side = "R"; 
            else side = "L"; 
     	
        }

       
        return side;    
  } 

////Method to read in one face \\\\
void ScanFace()  
  { 
        String testface = ""; // Variable to check if every facelet was detected.
        while (testface.length() != 9) // Loop to check if every face-let was detected.
        { 
        testface = ""; 
/*
        The facelet variables have the following order on a face:
        f1*f2*f3
        f4*f5*f6
        f7*f8*f9
        */
        
        Motor.B.setPower(40); 
        Motor.A.setPower(40); 
        Motor.B.rotateTo(140); // Rotate to the middle facelet and get it's according side.
        f5 = ScanColor();  
        
        Motor.B.rotateTo(173);  
        f9 = ScanColor(); 

        Motor.A.rotateTo(-90); // Rotate the cube +- 90 degrees to get side of top left facelet.
        f3 = ScanColor(); 

        Motor.A.rotateTo(-180); // Rotate the cube +- 90 degrees to get side of bottom left facelet.
        f1 = ScanColor(); 

        Motor.A.rotateTo(-270); // Rotate the cube and the light sensor to the bottom right faclet and get it's according side.
        f7 = ScanColor(); 
        
        Motor.B.rotateTo(168);  
        Motor.A.rotateTo(-230);
        f4 = ScanColor();

        Motor.A.rotateTo(-140);
        f2= ScanColor();

        Motor.A.rotateTo(-50);
        f6= ScanColor();

        Motor.A.rotateTo(40);
        f8= ScanColor();
        
        Motor.A.rotateTo(0);
        Motor.B.rotateTo(20);



        testface += f1; 
        testface += f2; 
        testface += f3; 
        testface += f4; 
        testface += f5; 
        testface += f6; 
        testface += f7; 
        testface += f8; 
        testface += f9; 

        if (DebugSetting == 1)
        {
            System.out.println("f5="+f5); 
            System.out.println("f9="+f9); 
            System.out.println("f3="+f3); 
            System.out.println("f1="+f1); 
            System.out.println("f7="+f7); 
            System.out.println("f4="+f4); 
            System.out.println("f2="+f2); 
            System.out.println("f6="+f6); 
            System.out.println("f8="+f8); 
            System.out.println("testface="+testface); 
        }

        } 
  } 

////The following three methods add the facelet variables properly to cu-beString. \\\\

void CubeStringU() // Used for White or Up side
  { 
        cubeStringU += f1; 
        cubeStringU += f2; 
        cubeStringU += f3; 
        cubeStringU += f4; 
        cubeStringU += "U"; // For the facelet with the Rubik's Cube Logo.
        cubeStringU += f6; 
        cubeStringU += f7; 
        cubeStringU += f8; 
        cubeStringU += f9;
        printcubeStringU += PrintCube(f1);
        printcubeStringU += PrintCube(f2);
        printcubeStringU += PrintCube(f3);
        printcubeStringU += PrintCube(f4);
        printcubeStringU += PrintCube(f5);
        printcubeStringU += PrintCube(f6);
        printcubeStringU += PrintCube(f7);
        printcubeStringU += PrintCube(f8);
        printcubeStringU += PrintCube(f9);
        
  } 

void CubeStringR() // Used for Red or Right side
  { 
        cubeStringR += f7; 
        cubeStringR += f4; 
        cubeStringR += f1; 
        cubeStringR += f8; 
        cubeStringR += f5; 
        cubeStringR += f2; 
        cubeStringR += f9; 
        cubeStringR += f6; 
        cubeStringR += f3; 
        printcubeStringR += PrintCube(f7);
        printcubeStringR += PrintCube(f4);
        printcubeStringR += PrintCube(f1);
        printcubeStringR += PrintCube(f8);
        printcubeStringR += PrintCube(f5);
        printcubeStringR += PrintCube(f2);
        printcubeStringR += PrintCube(f9);
        printcubeStringR += PrintCube(f6);
        printcubeStringR += PrintCube(f3);
  } 
void CubeStringF() // Used for Green or Front side
{ 
        cubeStringF += f7; 
        cubeStringF += f4; 
        cubeStringF += f1; 
        cubeStringF += f8; 
        cubeStringF += f5; 
        cubeStringF += f2; 
        cubeStringF += f9; 
        cubeStringF += f6; 
        cubeStringF += f3; 
        printcubeStringF += PrintCube(f7);
        printcubeStringF += PrintCube(f4);
        printcubeStringF += PrintCube(f1);
        printcubeStringF += PrintCube(f8);
        printcubeStringF += PrintCube(f5);
        printcubeStringF += PrintCube(f2);
        printcubeStringF += PrintCube(f9);
        printcubeStringF += PrintCube(f6);
        printcubeStringF += PrintCube(f3);
} 

void CubeStringD() // 
{ 
        cubeStringD += f7; 
        cubeStringD += f4; 
        cubeStringD += f1; 
        cubeStringD += f8; 
        cubeStringD += f5; 
        cubeStringD += f2; 
        cubeStringD += f9; 
        cubeStringD += f6; 
        cubeStringD += f3; 
        printcubeStringD += PrintCube(f7);
        printcubeStringD += PrintCube(f4);
        printcubeStringD += PrintCube(f1);
        printcubeStringD += PrintCube(f8);
        printcubeStringD += PrintCube(f5);
        printcubeStringD += PrintCube(f2);
        printcubeStringD += PrintCube(f9);
        printcubeStringD += PrintCube(f6);
        printcubeStringD += PrintCube(f3);
} 
void CubeStringL() // Used for Orange or Left side
{ 
        cubeStringL += f9; 
        cubeStringL += f8; 
        cubeStringL += f7; 
        cubeStringL += f6; 
        cubeStringL += f5; 
        cubeStringL += f4; 
        cubeStringL += f3; 
        cubeStringL += f2; 
        cubeStringL += f1; 
        printcubeStringL += PrintCube(f9);
        printcubeStringL += PrintCube(f8);
        printcubeStringL += PrintCube(f7);
        printcubeStringL += PrintCube(f6);
        printcubeStringL += PrintCube(f5);
        printcubeStringL += PrintCube(f4);
        printcubeStringL += PrintCube(f3);
        printcubeStringL += PrintCube(f2);
        printcubeStringL += PrintCube(f1);
} 

void CubeStringB() // Used for Blue or Back side
{ 
        cubeStringB += f9; 
        cubeStringB += f8; 
        cubeStringB += f7; 
        cubeStringB += f6; 
        cubeStringB += f5; 
        cubeStringB += f4; 
        cubeStringB += f3; 
        cubeStringB += f2; 
        cubeStringB += f1; 
        printcubeStringB += PrintCube(f9);
        printcubeStringB += PrintCube(f8);
        printcubeStringB += PrintCube(f7);
        printcubeStringB += PrintCube(f6);
        printcubeStringB += PrintCube(f5);
        printcubeStringB += PrintCube(f4);
        printcubeStringB += PrintCube(f3);
        printcubeStringB += PrintCube(f2);
        printcubeStringB += PrintCube(f1);
} 


void ScanCube()  // Scan whole cube
  { 
        ScanFace(); // Scan in side U.
        CubeStringU(); // Store facelets to CubeStringU
        //RotateCube1Side(); // Rotate cube to Side R (Red)
        RotateCubeSides(1); // Rotate cube to Side R (Red)

        Flip(); // Flip cube to place Side R (Red) on top
        RotateCubeSides(-1); // Rotate cube to Side R (Red)
        //RotateCube1SideCC(); // Returns the turntable to original position
        Delay.msDelay(200); // Wait to make sure cube has stopped moving
        ScanFace(); // Scan in side R.
        CubeStringR();// Store facelets to CubeStringR

        Flip(); // Flip cube to place Side F (Green) on top   
        ScanFace(); // Read in side F.
        CubeStringF(); // Store facelets to cubeStringF.

        RotateCubeSides(1); // Rotate cube to Side D (Red)
        Flip(); //Flip cube to place Side D (Yellow) on top.
        RotateCubeSides(-1); // Returns the turntable to original position
        ScanFace(); // Read in side D.
        CubeStringD(); // Store facelets to cubeStringD. 

        Flip(); // Move the cube to get side L on top.   
        ScanFace(); // Read in side L.
        CubeStringL(); // Store facelets to cubeStringL.

        RotateCubeSides(1); // Rotate cube to Side B (Red)
        Flip(); // Flip cube to place Side B (Blue) on top.
        RotateCubeSides(-1); // Returns the turntable to original position
        ScanFace(); // Read in side B.
        CubeStringB();// Add the read in facelets to cubeString.
        cubeString = cubeStringU + cubeStringR + cubeStringF + cubeStringD + cubeStringL + cubeStringB;
        Flip();
          Motor.B.rotateTo(20);  // Moves color sensor arm out of way of the cube movements
        if (DebugSetting == 1)
        {
            System.out.println("cubeString="+cubeString); 
        }

  } 


void Calculate()  // Passes all facelets to Kaciemba Two-Phase Algorithm
  { 
        // System.out.println(cubeString); 
    	timestart = System.currentTimeMillis(); // Get the time when calculation is started.
              result = Search.solution(cubeString, maxDepth, maxTime, useSeparator); // Get the solution by handing over the calculation to Her-bert Kociebas Two Phase Algorithm.
              fresult = result;
              if (DebugSetting == 1)
              {
                  System.out.println(fresult); 
              }

 } 

//  Reads next move from fresult variable
  String Getmove()  
  {      
        //System.out.println(fresult); 
	    move = fresult.substring(substringStart, substringEnd); // Read out one character from fresult. 

        //Check if it's just a 90 degree clockwise turn or if there is a modifier that belongs to the move.
        if (fresult.substring(substringStart2, substringEnd2).equals("2")) // Checks if the next character is a two and therefore belongs to the same move.
          { 
             move += "2"; // The two is added to move.
             substringStart++; // All variables are increased by one.
             substringEnd++; 
             substringStart2++; 
             substringEnd2++; 
          } 
       else
          if (fresult.substring(substringStart2, substringEnd2).equals("'")) // Checks if the next character is a dash and therefore belongs to the same move.
             { 
                 move += "'"; // The dash is added to move.
                 substringStart++; // All variables are increased by one.
                 substringEnd++; 
                 substringStart2++; 
                 substringEnd2++; 
             } 

             substringStart++; // All variables are increased by one.
             substringEnd++; 
             substringStart2++; 
             substringEnd2++; 

             return move; // Returns move.
  } 

  public static String PrintTranslateCube(String StringMove){
		String RetValue = "";
		switch (StringMove){
		case "U": RetValue = "W"; break;
		case "U2": RetValue = "W2"; break;
		case "U'": RetValue = "W3"; break;
		case "D": RetValue = "Y"; break;
		case "D2": RetValue = "Y2"; break;
		case "D'": RetValue = "Y3"; break;
		case "L": RetValue = "O"; break;
		case "L2": RetValue = "O2"; break;
		case "L'": RetValue = "O3"; break;
		case "R": RetValue = "R"; break;
		case "R2": RetValue = "R2"; break;
		case "R'": RetValue = "R3"; break;
		case "F": RetValue = "G"; break;
		case "F2": RetValue = "G2"; break;
		case "F'": RetValue = "G3"; break;
		case "B": RetValue = "B"; break;
		case "B2": RetValue = "B2"; break;
		case "B'": RetValue = "B3"; break;
		}
	   return RetValue;
	}    

  public static String PrintCube(String StringColor){
		String RetValue = "";
		switch (StringColor){
		case "B": RetValue = "B"; break;
		case "F": RetValue = "G"; break;
		case "L": RetValue = "O"; break;
		case "R": RetValue = "R"; break;
		case "U": RetValue = "W"; break;
		case "D": RetValue = "Y"; break;
		}
	   return RetValue;
	}    

  
////Apply one move on the cube. \\\\
  String Solve()  
  { 
     int side2 = 0; // Local variable side2. Used to get assign a number to the side which has to be turned.
     int i = 0; // Local variable to check if there was not jet a move applied. Necessary because after a move is applied the side could equal the value of a new face variable.
     String m = Getmove(); // String m is a string for the move. Get the move with Getmove().
        PrintStoreTranslatedSolution(PrintTranslateCube(m));
        
     if (m.length() == 1) // If the length of the move is one, the according side has to get twisted 90 degrees.
              { 
//Assign the string of the sides to a number to be able to check it easily.
        if (m.equals("U")) side2 = 1;  
        if (m.equals("F")) side2 = 3; 
        if (m.equals("R")) side2 = 2; 
        if (m.equals("B")) side2 = 6; 
        if (m.equals("L")) side2 = 5; 
        if (m.equals("D")) side2 = 4; 
        if (side2 == F && i == 0) // Checks if the side which has to be turned is now positioned in the front. 
              {                        
                    RotateCubeSides(2); // Turn the cube to have the side in the bottom.
                    Flip(); 
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(1); // Twist the side by 90 degrees.
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 
        if (side2 == U && i == 0) // Checks if the side which has to be turned is now positioned on top.
              { 
                    Flip(); // Turn the cube to have the side in the bottom.
                    Flip(); 
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(1); // Twist the side by 90 degrees.
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 
        if (side2 == D && i == 0) // Checks if the side which has to be turned is now positioned on the bottom.
              { 
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(1); // Twist the side by 90 degrees. 
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 

        if (side2 == R && i == 0) // Checks if the side which has to be turned is now positioned in the right.
              { 
                    RotateCubeSides(-1); // Turn the cube to have the side in the bottom.
                    Flip(); 
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(1); // Twist the side by 90 degrees. 
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 

        if (side2 == B && i == 0) // Checks if the side which has to be turned is now positioned in the back.
              {                        
                    Flip(); // Turn the cube to have the side in the bottom.
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(1); // Twist the side by 90 degrees. 
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 

        if (side2 == L && i == 0) // Checks if the side which has to be turned is now positioned in the left.
              { 
                    RotateCubeSides(1); // Turn the cube to have the side in the bottom.
                    Flip(); 
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(1); // Twist the side by 90 degrees.
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 
        } 
  else  // If the length of m was not one.
  { 

  // Check if the modifier is two. If the modifier is two, the according side has to get twisted 180 degrees.
     if (m.substring(1, 2).equals("2")) 
        { 

  // Assign the string of the sides to a number to be able to check it easily.
     if (m.equals("U2")) side2 = 1; 
     if (m.equals("F2")) side2 = 3; 
     if (m.equals("R2")) side2 = 2; 
     if (m.equals("B2")) side2 = 6; 
     if (m.equals("L2")) side2 = 5; 
     if (m.equals("D2")) side2 = 4; 
     if (side2 == F && i == 0) // Checks if the side which has to be turned is now positioned in the front.
              {                        
                    RotateCubeSides(2); // Turn the cube to have the side in the bottom.
                    Flip(); 
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(2); // Twist the side by 180 degrees.
                    //RotateBottomRow(); 
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 
     if (side2 == U && i == 0) // Checks if the side which has to be turned is now positioned on top.
              { 
                    Flip(); // Turn the cube to have the side in the bottom.
                    Flip(); 
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(2); // Twist the side by 180 degrees.
                    //RotateBottomRow();   
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 

     if (side2 == D && i == 0) // Checks if the side which has to be turned is now positioned on bottom.
              { 
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(2); // Twist the side by 180 degrees.
                    //RotateBottomRow();   
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 

     if (side2 == R && i == 0) // Checks if the side which has to be turned is now positioned on the right.
              { 
                    RotateCubeSides(-1); // Turn the cube to have the side in the bottom.
                    Flip(); 
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(2); // Twist the side by 180 degrees.
                    //RotateBottomRow();   
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 

     if (side2 == B && i == 0) // Checks if the side which has to be turned is now positioned in the back.
              { 
                    Flip(); // Turn the cube to have the side in the bottom.
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(2); // Twist the side by 180 degrees.
                    //RotateBottomRow();   
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 

     if (side2 == L && i == 0) // Checks if the side which has to be turned is now positioned on the left.
              { 
                    RotateCubeSides(1); // Turn the cube to have the side in the bottom.
                    Flip(); 
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(2); // Twist the side by 180 degrees.
                    //RotateBottomRow(); 
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 
        } 

//Check if the modifier is a dash. If the modifier is a dash, the according side has to get twisted minus 90 degres.
    if (m.substring(1, 2).equals("'")) 
        { 

        // Assign the string of the sides to a number to be able to check it easily.
        if (m.equals("U'")) side2 = 1; 
        if (m.equals("F'")) side2 = 3; 
        if (m.equals("R'")) side2 = 2; 
        if (m.equals("B'")) side2 = 6; 
        if (m.equals("L'")) side2 = 5; 
        if (m.equals("D'")) side2 = 4; 

        if (side2 == F && i == 0) // Checks if the side which has to be turned is now positioned in front.
              { 
                    RotateCubeSides(2); // Turn the cube to have the side in the bottom.
                    Flip();                        
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(-1); // Twist the side by minus 90 degrees.
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 

        if (side2 == U && i == 0) // Checks if the side which has to be turned is now positioned on top.
              { 
                    Flip(); // Turn the cube to have the side in the bottom.
                    Flip();                        
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(-1); // Twist the side by minus 90 degrees.
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 

        if (side2 == D && i == 0) // Checks if the side which has to be turned is now positioned on the bottom.
              {            
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(-1); // Twist the side by minus 90 degrees.
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 

        if (side2 == R && i == 0) // Checks if the side which has to be turned is now positioned on the right.
              { 
                    RotateCubeSides(-1); // Turn the cube to have the side in the bottom.
                    Flip();                        
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(-1); // Twist the side by minus 90 degrees.
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 

        if (side2 == B && i == 0) // Checks if the side which has to be turned is now positioned in the back.
              { 
                    Flip(); // Turn the cube to have the side in the bottom.      
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(-1); // Twist the side by minus 90 degrees.
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 

        if (side2 == L && i == 0) // Checks if the side which has to be turned is now positioned on the left.
              { 
                    RotateCubeSides(1); // Turn the cube to have the side in the bottom.
                    Flip(); 
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(-1); // Twist the side by minus 90 degrees.
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 
        } 
        } 
return m; // Return m to be able to count how far the robot is in fresult. Used in SolveCube().
  } 

void SolveCube()  
  { 
      int s1 = 0; // Local variable to control while loop.
      int s2 = 1; 
      fresult += " "; // Add a space to solution string in the end.
      while (fresult.substring(s1, s2) != " ") 

      // The method solve is applied until the space which was added is reached.
        { 
        if (Solve().length() == 2)// Checks the length of the variable m. If it's two the local variables have to be increased by another one.
              { 
                    s1++; 
                    s2++; 
              } 
              s1++; 
              s2++; 
              if (s2 == fresult.length()) break;
        } 
  } 

public static void  main(String[] args) { 
  new Kociemba(); 
  } 
} 

