package tests;

//import com.mydomain.arm;

//import global.Utils;
//import junit.framework.Assert;

//import org.junit.Test;

//import rep.Actions;
import rep.Cube;
import rep.CubieCube;
import solution.SolUtils;
import solution.Thistle;
import global.Utils;
//import validation.Sanity;


public class TestCube {

	//String cc = "";

public static String TranslatedSolution="";
	
TestCube()
{
	testSolutionValidCube();
	//testValidCube();
}


public static void  StoreTranslatedSolution(String newSolution)
{
TranslatedSolution += newSolution += " ";
}



	public void testSolutionValidCube() {
  		//Cube cube = Utils.deserializeCube("666666611555555555333333333444444444661111111222222222");
		// Cube cube = Utils.deserializeCube("166166144233255255666333333544544544311511511224226226");
		//Cube cube = Utils.deserializeCube("223362416213251416325535156266643164254414346145623135");
		Cube cube = Utils.deserializeCube("223362416213251416325535156265643164254414346145623135");
		//Cube cube = TestUtils.generateRandomValidCube();
         
	    //System.out.println(serializeCube(Cube cube)); 
		CubieCube cc = cube.cubieCubeFromSides();

       
        Thistle.solve(cc);
        //Actions[] solution = Thistle.robotSolution;

		for (int i = 0; i < Thistle.solution.size(); i++) {
			TestCube.StoreTranslatedSolution(TranslateCube(Thistle.solution.get(i)));
		}

		//System.out.println("Translated Solution..:"+TranslatedSolution); 
        //Thistlethwaite.StoreTranslatedSolution(NewCurrent);
        //TestCube.StoreTranslatedSolution(NewCurrent);

        
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
	
//	public void test() {
//		Cube cube = Utils.deserializeCube("666666616555555555333333333444444444161111111222222222");
//		CubieCube cc = cube.cubieCubeFromSides();
		//Assert.assertTrue(!Sanity.validateAllCubies(cc));			
//	}
	


public static void  main(String[] args) { 
	  new TestCube(); 
	  } 
}