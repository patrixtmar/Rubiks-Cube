package com.mydomain;

import org.kociemba.twophase.Search;

public class Solver {

int maxDepth = 21;  //Sets the maximum amount of manoeuvre to start with.
long timetosearch = 15; //Sets the maximum time to find a solution in seconds.
long timesearching = 0; 
long timestart = 0; 
long timeend = 0;        
int maxTime = 15; 
//String cubeString = "RBBUURFDLRBFDLBDLUFRDDBLBDULUDRDLRFUDBRFRFUFLLRBUFLFUB"; 
String cubeString = ""; 
String result = ""; 
String fresult = ""; 
boolean useSeparator = false;        
// U  - RBBUURFDL
// R  - DBRFRFUFL
// F  - LRBUFLFUB
// D  - LUDRDLRFU
// L  - RBFDLBDLU
// B  - FRDDBLBDU

Solver()
{
    calculate();
}

void calculate()
{
    //cubeString += "RBBUURFDL";  //U
    //cubeString += "DBRFRFUFL";  //R
    //cubeString += "LRBUFLFUB";  //F
    //cubeString += "RRLFDUULD";  //D
    //cubeString += "FBUBLLRDD";  //L
    //cubeString += "DLURBDFDB";  //B

    cubeString += "RBFLULFLL";  //U
    cubeString += "DBUFRBBBR";  //R
    cubeString += "DUBLFDUDR";  //F
    cubeString += "LRDFDRBRU";  //D
    cubeString += "DFLFLDURB";  //L
    cubeString += "LDFUBUFUR";  //B
    		
	timestart = System.currentTimeMillis(); // Get the time when calculation is started.
    result = Search.solution(cubeString, maxDepth, maxTime, useSeparator); // Get the solution by handing over the calculation to Her-bert Kociebas Two Phase Algorithm.
    System.out.println(cubeString);
    System.out.println(result);
    //while (timesearching < timetosearch*1000) // The program will keep searching for a shorter solution after a solution is found for 30 sec-onds.
    //{      
    //      fresult = result; //fresult is the final solution
    //      result = Search.solution(cubeString, maxDepth, maxTime, useSeparator); // Get the solution by handing over the calculation to Her-bert Kociebas Two Phase Algorithm.
    //      System.out.println(cubeString); 
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

}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		  { 
             new Solver();
		  } 

	}

}
