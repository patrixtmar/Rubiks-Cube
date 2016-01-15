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
import lejos.nxt.addon.ColorHTSensor; // From lejos.org

public class Scramb { 

// ----------- Environment Variables ------------------------------------------
// ----------- Updatable Variables to adjust speed of solving the cube --------
int maxDepth = 22;  //Sets the maximum amount of moves to start the solution
long timetosearch = 15; //The time(seconds) to find a solution.
int maxTime = 15; // Max time to search for solution
int MotorRotatePower = 35; // Adjust motor rotation power
int ArmPower = 15;  // Adjust arm rotation power
int DebugSetting = 1; // Change to 1 to for debugging values

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


Scramb() 
  { 
     //ScanCube();  // Scan the full cube
     //if (DebugSetting == 1)
     //{
     //  System.out.println("Executed ScanCube()"); 
     //}
     cubeString="RBFLULFLLDBUFRBBBRDUBLFDUDRLRDFDRBRUDFLFLDURBLDFUBUFUR";
       Motor.B.rotateTo(20); //Move color sensor arm out of way for cube movement
     Calculate(); // Calculate solution use Kaciemba two phase algorithm
        //fresult="U L F' R' U B' R2 F D F L' B U D F2 D F2 L2 F2 U' B2 L2  ";
     if (DebugSetting == 1)
     {
       System.out.println("Executed Calculate()"); 
     }
     SolveCube(); // The cube is getting solved. 
     if (DebugSetting == 1)
     {
       System.out.println("Executed SolveCube()"); 
     }
       Motor.B.rotateTo(0); // Resets color sensor arm for next scan
  } 

//  All Moves 

void RotateCube5() // Rotates the turntable clockwise by 5 degrees. Used to fix after the cube twist.
  { 
        Motor.A.setPower(MotorRotatePower); 
        Motor.A.rotate(-5); 
  } 

void RotateCube5CC() // Rotates the turntable counter clockwise by 5 degrees. Used to fix after the cube twist..
  { 
        Motor.A.setPower(MotorRotatePower); 
        Motor.A.rotate(5); 
  } 

void RotateCube1Side() // Rotates the turntable clockwise.  Rotates faces F, B, L and R.
  { 
        X = F; // Reassign variables for F, B, L and R. X is used to store the current value of F.
        F = R; 
        R = B; 
        B = L; 
        L = X; 
        Motor.A.setPower(MotorRotatePower); 
        Motor.A.rotate(-90); 
  }      

public void RotateCube1SideCC() // Rotates the turntable counter clockwise.  Rotates faces F, B, L and R.
  { 
        X = F; // Reassign variables for F, B, L and R. X is used to store the current value of F.
        F = L; 
        L = B; 
        B = R; 
        R = X; 
        Motor.A.setPower(MotorRotatePower); 
        Motor.A.rotate(90); 
  } 

void RotateCube2Sides() // Rotates the turntable clockwise by 180 degrees,      
  { 
        RotateCube1Side(); 
        RotateCube1Side(); 
  } 

//void RotateCube2SidesCC() // Rotates the turntable counter clockwise by 180 degrees,
//  { 
//        RotateCube1SideCC(); 
//        RotateCube1SideCC(); 
//  } 

//void RotateCube3Sides()  // Rotates the turntable clockwise by 270 degrees,
//  { 
//        RotateCube1Side(); 
//        RotateCube1Side(); 
//        RotateCube1Side(); 
//  } 

//void RotateCube3SidesCC() 
//  { 
//        RotateCube1SideCC(); 
//        RotateCube1SideCC(); 
//        RotateCube1SideCC(); 
//  } 

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
void RotateBottomRow() 
   // Rotates the turntable with the arm in the loweRedValue position 
   // by rotating clockwise 95 degrees and back counterclockwise by 5 degrees.
  { 
        Motor.A.setPower(MotorRotatePower); 
        Motor.A.rotate(95); 
        Motor.A.rotate(-5); 
        //RotateCube5(); 
  } 
void RotateBottomRowCC() 
// Rotates the turntable with the arm in the loweRedValue position  
// by rotating counterclockwise 95 degrees and back clockwise by 5 degrees.
  { 
        Motor.A.setPower(MotorRotatePower); 
        Motor.A.rotate(-95); 
        Motor.A.rotate(5); 
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
        Delay.msDelay(400); // Wait 400 ms to make sure that the sensor is not moving.
        ColorHTSensor color = new ColorHTSensor(SensorPort.S1); 
        ColorIDValue = color.getColorID(); // The method getColortID() returns a number between 0 and 16 for different colors.
        GreenValue = color.getRGBComponent(color.GREEN); // The GreenValue value is used to distinguish between orange and RedValue, because it's not possible to differentiate RedValue and orange with getColorID(). 
        RedValue = color.getRGBComponent(color.RED); // The RedValue value is used to distinguish between orange and RedValue, because it's not possible to differentiate RedValue and orange with getColorID(). 
        BlueValue = color.getRGBComponent(color.BLUE); // The BlueValue value is used to distinguish between orange and RedValue, because it's not possible to differentiate RedValue and orange with getColorID(). 

        String side = ""; 
      
        // Assign the color sensor values to the according side.
        if (DebugSetting == 1)
        {
            System.out.println("ColorIDVAlue="+String.valueOf(ColorIDValue)+": GreenValue="+GreenValue+", RedValue="+RedValue+",BlueValue="+BlueValue); 
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
        Motor.B.rotateTo(146); // Rotate to the middle facelet and get it's according side.
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
        cubeString += f1; 
        cubeString += f2; 
        cubeString += f3; 
        cubeString += f4; 
        cubeString += "U"; // For the facelet with the Rubik's Cube Logo.
        cubeString += f6; 
        cubeString += f7; 
        cubeString += f8; 
        cubeString += f9;        
  } 

void CubeStringRFD() // Used for Red or Right side
  { 
        cubeString += f7; 
        cubeString += f4; 
        cubeString += f1; 
        cubeString += f8; 
        cubeString += f5; 
        cubeString += f2; 
        cubeString += f9; 
        cubeString += f6; 
        cubeString += f3; 
  } 
void CubeStringF() // Used for Green or Front side
{ 
        cubeString += f7; 
        cubeString += f4; 
        cubeString += f1; 
        cubeString += f8; 
        cubeString += f5; 
        cubeString += f2; 
        cubeString += f9; 
        cubeString += f6; 
        cubeString += f3; 
} 

void CubeStringD() // 
{ 
        cubeString += f7; 
        cubeString += f4; 
        cubeString += f1; 
        cubeString += f8; 
        cubeString += f5; 
        cubeString += f2; 
        cubeString += f9; 
        cubeString += f6; 
        cubeString += f3; 
} 
void CubeStringLB() // Used for Orange or Left side
{ 
        cubeString += f9; 
        cubeString += f8; 
        cubeString += f7; 
        cubeString += f6; 
        cubeString += f5; 
        cubeString += f4; 
        cubeString += f3; 
        cubeString += f2; 
        cubeString += f1; 
} 
void CubeStringB() // Used for Blue or Back side
{ 
        cubeString += f9; 
        cubeString += f8; 
        cubeString += f7; 
        cubeString += f6; 
        cubeString += f5; 
        cubeString += f4; 
        cubeString += f3; 
        cubeString += f2; 
        cubeString += f1; 
} 


void ScanCube()  // Scan whole cube
  { 
        ScanFace(); // Scan in side U.
        CubeStringU(); // Store facelets to CubeStringU
        RotateCube1Side(); // Rotate cube to Side R (Red)

        Flip(); // Flip cube to place Side R (Red) on top
        RotateCube1SideCC(); // Returns the turntable to original position
        Delay.msDelay(200); // Wait to make sure cube has stopped moving
        ScanFace(); // Scan in side R.
        CubeStringRFD();// Store facelets to CubeStringR

        Flip(); // Flip cube to place Side F (Green) on top   
        ScanFace(); // Read in side F.
        CubeStringRFD(); // Store facelets to cubeStringF.

        RotateCube1Side(); // Move the cube to get side D on top.
        Flip(); //Flip cube to place Side D (Yellow) on top.
        RotateCube1SideCC(); // Returns the turntable to original position
        ScanFace(); // Read in side D.
        CubeStringRFD(); // Store facelets to cubeStringD. 

        Flip(); // Move the cube to get side L on top.   
        ScanFace(); // Read in side L.
        CubeStringLB(); // Store facelets to cubeStringL.

        RotateCube1Side(); // Move the cube to get side B on top.
        Flip(); // Flip cube to place Side B (Blue) on top.
        RotateCube1SideCC(); // Returns the turntable to original position
        ScanFace(); // Read in side B.
        CubeStringLB();// Add the read in facelets to cubeString.

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
        //while (timesearching < timetosearch*1000) // The program will keep searching for a shorter solution after a solution is found for 30 sec-onds.
        //{      
        //      fresult = result; //fresult is the final solution
              result = Search.solution(cubeString, maxDepth, maxTime, useSeparator); // Get the solution by handing over the calculation to Her-bert Kociebas Two Phase Algorithm.
              fresult = result;
        //      //System.out.println(result); 
        //      timeend = System.currentTimeMillis(); // Get the current time.
        //      timesearching = timeend - timestart; // Calculate for how long the solution is already beeing searched.

        //      if (result == "Error 7") // Stops the loop if the program runs into a Error 7. Error 7 means there is no better solution than already found.
        //      { 
        //          timesearching = timetosearch*1000; // Jump out of the while loop.
        //      } 
              
        //      if (result == "Error 8") // Stops the loop if the program runs into Error 8. Error 8 means timeout, no better solution found within the maximum time to search.
        //      { 
        //          timesearching =timetosearch*1000; // Jump out of the while loop.
        //      } 
        //maxDepth = maxDepth -1; // Decrease search depth by one.
        //} 
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

////Apply one move on the cube. \\\\
  String Solve()  
  { 
     int side2 = 0; // Local variable side2. Used to get assign a number to the side which has to be turned.
     int i = 0; // Local variable to check if there was not jet a move applied. Necessary because after a move is applied the side could equal the value of a new face variable.
     String m = Getmove(); // String m is a string for the move. Get the move with Getmove().
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
                    RotateCube2Sides(); // Turn the cube to have the side in the bottom.
                    Flip(); 
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(); // Twist the side by 90 degrees.
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 
        if (side2 == U && i == 0) // Checks if the side which has to be turned is now positioned on top.
              { 
                    Flip(); // Turn the cube to have the side in the bottom.
                    Flip(); 
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(); // Twist the side by 90 degrees.
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 
        if (side2 == D && i == 0) // Checks if the side which has to be turned is now positioned on the bottom.
              { 
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(); // Twist the side by 90 degrees. 
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 

        if (side2 == R && i == 0) // Checks if the side which has to be turned is now positioned in the right.
              { 
                    RotateCube1SideCC(); // Turn the cube to have the side in the bottom.
                    Flip(); 
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(); // Twist the side by 90 degrees. 
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 

        if (side2 == B && i == 0) // Checks if the side which has to be turned is now positioned in the back.
              {                        
                    Flip(); // Turn the cube to have the side in the bottom.
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(); // Twist the side by 90 degrees. 
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 

        if (side2 == L && i == 0) // Checks if the side which has to be turned is now positioned in the left.
              { 
                    RotateCube1Side(); // Turn the cube to have the side in the bottom.
                    Flip(); 
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(); // Twist the side by 90 degrees.
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
                    RotateCube2Sides(); // Turn the cube to have the side in the bottom.
                    Flip(); 
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(); // Twist the side by 180 degrees.
                    RotateBottomRow(); 
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 
     if (side2 == U && i == 0) // Checks if the side which has to be turned is now positioned on top.
              { 
                    Flip(); // Turn the cube to have the side in the bottom.
                    Flip(); 
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(); // Twist the side by 180 degrees.
                    RotateBottomRow();   
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 

     if (side2 == D && i == 0) // Checks if the side which has to be turned is now positioned on bottom.
              { 
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(); // Twist the side by 180 degrees.
                    RotateBottomRow();   
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 

     if (side2 == R && i == 0) // Checks if the side which has to be turned is now positioned on the right.
              { 
                    RotateCube1SideCC(); // Turn the cube to have the side in the bottom.
                    Flip(); 
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(); // Twist the side by 180 degrees.
                    RotateBottomRow();   
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 

     if (side2 == B && i == 0) // Checks if the side which has to be turned is now positioned in the back.
              { 
                    Flip(); // Turn the cube to have the side in the bottom.
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(); // Twist the side by 180 degrees.
                    RotateBottomRow();   
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 

     if (side2 == L && i == 0) // Checks if the side which has to be turned is now positioned on the left.
              { 
                    RotateCube1Side(); // Turn the cube to have the side in the bottom.
                    Flip(); 
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRow(); // Twist the side by 180 degrees.
                    RotateBottomRow(); 
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
                    RotateCube2Sides(); // Turn the cube to have the side in the bottom.
                    Flip();                        
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRowCC(); // Twist the side by minus 90 degrees.
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 

        if (side2 == U && i == 0) // Checks if the side which has to be turned is now positioned on top.
              { 
                    Flip(); // Turn the cube to have the side in the bottom.
                    Flip();                        
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRowCC(); // Twist the side by minus 90 degrees.
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 

        if (side2 == D && i == 0) // Checks if the side which has to be turned is now positioned on the bottom.
              {            
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRowCC(); // Twist the side by minus 90 degrees.
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 

        if (side2 == R && i == 0) // Checks if the side which has to be turned is now positioned on the right.
              { 
                    RotateCube1SideCC(); // Turn the cube to have the side in the bottom.
                    Flip();                        
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRowCC(); // Twist the side by minus 90 degrees.
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 

        if (side2 == B && i == 0) // Checks if the side which has to be turned is now positioned in the back.
              { 
                    Flip(); // Turn the cube to have the side in the bottom.      
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRowCC(); // Twist the side by minus 90 degrees.
                    RaiseArm(); // Release the cube.
                    i++; // Increase i by one to block another move.
              } 

        if (side2 == L && i == 0) // Checks if the side which has to be turned is now positioned on the left.
              { 
                    RotateCube1Side(); // Turn the cube to have the side in the bottom.
                    Flip(); 
                    LowerArm(); // Hold the cube with the arm.
                    RotateBottomRowCC(); // Twist the side by minus 90 degrees.
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
        } 
  } 

public static void  main(String[] args) { 
  new Scramb(); 
  } 
} 

