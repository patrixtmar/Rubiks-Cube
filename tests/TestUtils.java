package tests;

import global.Utils;

import java.util.Random;

//import com.sun.xml.internal.ws.encoding.soap.DeserializationException;

import rep.Cube;

public class TestUtils {

	static public String baseCube = "666666666555555555333333333444444444111111111222222222";
	static private String invalidEdgeCube = "666666616555555555333333333444444444161111111222222222";
	static private String invalidCornerCube = "666666661555555555333333333644444444114111111222222222";
	static private String invalidColorCube = "666666666555555555333333233444444444111111111222222222";
	
	public static String twistUpCW(String cube) {
		char[] str = cube.toCharArray();
		char cubie0 = cube.charAt(0);
		str[0] = str[6];
		str[6] = str[8];
		str[8] = str[2];
		str[2] = cubie0;
		char cubie1 = cube.charAt(1);
		str[1] = str[3];
		str[3] = str[7];
		str[7] = str[5];
		str[5] = cubie1;
		char cubie18 = cube.charAt(18);
		char cubie19 = cube.charAt(19);
		char cubie20 = cube.charAt(20);
		str[18] = str[36];
		str[19] = str[37];
		str[20] = str[38];
		str[36] = str[27];
		str[37] = str[28];
		str[38] = str[29];
		str[27] = str[45];
		str[28] = str[46];
		str[29] = str[47];
		str[45] = cubie18;
		str[46] = cubie19;
		str[47] = cubie20;
		
		return new String(str);
	}
	
	
	private String twistUpCCW(String cube) {
		char[] str = cube.toCharArray();
		char cubie0 = cube.charAt(0);
		str[0] = str[2];
		str[2] = str[8];
		str[8] = str[6];
		str[6] = cubie0;
		char cubie1 = cube.charAt(1);
		str[1] = str[5];
		str[5] = str[7];
		str[7] = str[3];
		str[3] = cubie1;
		char cubie47 = cube.charAt(47);
		char cubie46 = cube.charAt(47);
		char cubie45 = cube.charAt(47);
		str[47] = str[29];
		str[46] = str[28];
		str[45] = str[27];
		str[29] = str[38];
		str[28] = str[37];
		str[27] = str[36];
		str[38] = str[20];
		str[37] = str[19];
		str[36] = str[18];
		str[20] = cubie47;
		str[19] = cubie46;
		str[18] = cubie45;
		
		return new String(str);
	}
	
	public static String twistDownCW(String cube) {
		char[] str = cube.toCharArray();
		char cubie9 = cube.charAt(9);
		str[9] = str[15];
		str[15] = str[17];
		str[17] = str[11];
		str[11] = cubie9;
		char cubie10 = cube.charAt(10);
		str[10] = str[12];
		str[12] = str[16];
		str[16] = str[14];
		str[14] = cubie10;
		char cubie51 = cube.charAt(51);
		char cubie52 = cube.charAt(52);
		char cubie53 = cube.charAt(53);
		str[51] = str[33];
		str[52] = str[34];
		str[53] = str[35];
		str[33] = str[42];
		str[34] = str[43];
		str[35] = str[44];
		str[42] = str[24];
		str[43] = str[25];
		str[44] = str[26];
		str[24] = cubie51;
		str[25] = cubie52;
		str[26] = cubie53;
		
		return new String(str);
	}
	
	private String twistDownCCW(String cube) {
		char[] str = cube.toCharArray();
		char cubie9 = cube.charAt(9);
		str[9] = str[11];
		str[11] = str[17];
		str[17] = str[15];
		str[15] = cubie9;
		char cubie10 = cube.charAt(10);
		str[10] = str[14];
		str[14] = str[16];
		str[16] = str[13];
		str[12] = cubie10;
		char cubie51 = cube.charAt(51);
		char cubie52 = cube.charAt(52);
		char cubie53 = cube.charAt(53);
		str[51] = str[24];
		str[52] = str[25];
		str[53] = str[26];
		str[24] = str[42];
		str[25] = str[43];
		str[26] = str[44];
		str[42] = str[33];
		str[43] = str[34];
		str[44] = str[35];
		str[33] = cubie51;
		str[34] = cubie52;
		str[35] = cubie53;
		
		return new String(str);
	}
	
	public static String twistFrontCW(String cube) {
		char[] str = cube.toCharArray();
		char cubie37 = cube.charAt(37);
		str[37] = str[39];
		str[39] = str[43];
		str[43] = str[41];
		str[41] = cubie37;
		char cubie36 = cube.charAt(36);
		str[36] = str[42];
		str[42] = str[44];
		str[44] = str[38];
		str[38] = cubie36;
		char cubie6 = cube.charAt(6);
		char cubie7 = cube.charAt(7);
		char cubie8 = cube.charAt(8);
		str[6] = str[26];
		str[7] = str[23];
		str[8] = str[20];
		str[26] = str[11];
		str[23] = str[10];
		str[20] = str[9];
		str[11] = str[27];
		str[10] = str[30];
		str[9] = str[33];
		str[27] = cubie6;
		str[30] = cubie7;
		str[33] = cubie8;
		
		return new String(str);
	}
	
	private String twistFrontCCW(String cube) {
		char[] str = cube.toCharArray();
		char cubie37 = cube.charAt(37);
		str[37] = str[41];
		str[41] = str[43];
		str[43] = str[39];
		str[39] = cubie37;
		char cubie36 = cube.charAt(36);
		str[36] = str[38];
		str[38] = str[44];
		str[44] = str[42];
		str[42] = cubie36;
		char cubie6 = cube.charAt(6);
		char cubie7 = cube.charAt(7);
		char cubie8 = cube.charAt(8);
		str[6] = str[27];
		str[7] = str[30];
		str[8] = str[33];
		str[27] = str[11];
		str[30] = str[10];
		str[33] = str[9];
		str[11] = str[26];
		str[10] = str[23];
		str[9] = str[20];
		str[26] = cubie6;
		str[23] = cubie7;
		str[20] = cubie8;
		
		return new String(str);
	}
	
	public static String twistRightCW(String cube) {
		char[] str = cube.toCharArray();
		char cubie28 = cube.charAt(28);
		str[28] = str[30];
		str[30] = str[34];
		str[34] = str[32];
		str[32] = cubie28;
		char cubie27 = cube.charAt(27);
		str[27] = str[33];
		str[33] = str[35];
		str[35] = str[29];
		str[29] = cubie27;
		char cubie2 = cube.charAt(2);
		char cubie5 = cube.charAt(5);
		char cubie8 = cube.charAt(8);
		str[2] = str[38];
		str[5] = str[41];
		str[8] = str[44];
		str[38] = str[11];
		str[41] = str[14];
		str[44] = str[17];
		str[11] = str[51];
		str[14] = str[48];
		str[17] = str[45];
		str[51] = cubie2;
		str[48] = cubie5;
		str[45] = cubie8;
		
		return new String(str);
	}
	

	private String twistRightCCW(String cube) {
		char[] str = cube.toCharArray();
		char cubie28 = cube.charAt(28);
		str[28] = str[32];
		str[32] = str[34];
		str[34] = str[30];
		str[30] = cubie28;
		char cubie27 = cube.charAt(27);
		str[27] = str[29];
		str[29] = str[35];
		str[35] = str[33];
		str[33] = cubie27;
		char cubie2 = cube.charAt(2);
		char cubie5 = cube.charAt(5);
		char cubie8 = cube.charAt(8);
		str[2] = str[51];
		str[5] = str[48];
		str[8] = str[45];
		str[51] = str[11];
		str[48] = str[14];
		str[45] = str[17];
		str[11] = str[38];
		str[14] = str[41];
		str[17] = str[44];
		str[38] = cubie2;
		str[41] = cubie5;
		str[44] = cubie8;
		
		return new String(str);
	}
	
	public static String twistLeftCW(String cube) {
		char[] str = cube.toCharArray();
		char cubie20 = cube.charAt(20);
		str[20] = str[18];
		str[18] = str[24];
		str[24] = str[26];
		str[26] = cubie20;
		char cubie19 = cube.charAt(19);
		str[19] = str[21];
		str[21] = str[25];
		str[25] = str[23];
		str[23] = cubie19;
		char cubie0 = cube.charAt(0);
		char cubie3 = cube.charAt(3);
		char cubie6 = cube.charAt(6);
		str[0] = str[53];
		str[3] = str[50];
		str[6] = str[47];
		str[53] = str[9];
		str[50] = str[12];
		str[47] = str[15];
		str[9] = str[36];
		str[12] = str[39];
		str[15] = str[42];
		str[36] = cubie0;
		str[39] = cubie3;
		str[42] = cubie6;
		
		return new String(str);
	}
	
	private String twistLeftCCW(String cube) {
		char[] str = cube.toCharArray();
		char cubie20 = cube.charAt(20);
		str[20] = str[26];
		str[26] = str[24];
		str[24] = str[18];
		str[18] = cubie20;
		char cubie19 = cube.charAt(19);
		str[19] = str[23];
		str[23] = str[25];
		str[25] = str[21];
		str[21] = cubie19;
		char cubie0 = cube.charAt(0);
		char cubie3 = cube.charAt(3);
		char cubie6 = cube.charAt(6);
		str[0] = str[36];
		str[3] = str[39];
		str[6] = str[42];
		str[36] = str[9];
		str[39] = str[12];
		str[42] = str[15];
		str[9] = str[53];
		str[12] = str[50];
		str[15] = str[47];
		str[53] = cubie0;
		str[50] = cubie3;
		str[47] = cubie6;
		
		return new String(str);
	}
	
	public static String twistBackCW(String cube) {
		char[] str = cube.toCharArray();
		char cubie45 = cube.charAt(45);
		str[45] = str[51];
		str[51] = str[53];
		str[53] = str[47];
		str[47] = cubie45;
		char cubie46 = cube.charAt(46);
		str[46] = str[48];
		str[48] = str[52];
		str[52] = str[50];
		str[50] = cubie46;
		char cubie29 = cube.charAt(29);
		char cubie32 = cube.charAt(32);
		char cubie35 = cube.charAt(35);
		str[29] = str[17];
		str[32] = str[16];
		str[35] = str[15];
		str[17] = str[24];
		str[16] = str[21];
		str[15] = str[18];
		str[24] = str[0];
		str[21] = str[1];
		str[18] = str[2];
		str[0] = cubie29;
		str[1] = cubie32;
		str[2] = cubie35;
		
		return new String(str);
	}
	
	private String twistBackCCW(String cube) {
		char[] str = cube.toCharArray();
		char cubie45 = cube.charAt(45);
		str[45] = str[47];
		str[47] = str[53];
		str[53] = str[51];
		str[51] = cubie45;
		char cubie46 = cube.charAt(46);
		str[46] = str[50];
		str[50] = str[52];
		str[52] = str[48];
		str[48] = cubie46;
		char cubie29 = cube.charAt(29);
		char cubie32 = cube.charAt(32);
		char cubie35 = cube.charAt(35);
		str[29] = str[0];
		str[32] = str[1];
		str[35] = str[2];
		str[0] = str[24];
		str[1] = str[21];
		str[2] = str[18];
		str[24] = str[17];
		str[21] = str[16];
		str[18] = str[15];
		str[17] = cubie29;
		str[16] = cubie32;
		str[15] = cubie35;
		
		return new String(str);
	}
	
	private static String chooseRandomTwist(String cube) {
		int random = (int) Math.round(6 * Math.random());
		switch (random) {
		case 1:
			return twistUpCW(cube);
		case 2:
			return twistDownCW(cube);
		case 3:
			return twistFrontCW(cube);
		case 4:
			return twistBackCW(cube);
		case 5:
			return twistRightCW(cube);
		case 6:
		case 0:
			return twistLeftCW(cube);
		default:
			throw new IllegalArgumentException("invalid random number: " + random + " - should NEVER happen!");
		}
	}
	
	static private Cube generateRandomCube(String cube) {
		String initialCube = new String(cube);
		int numOfTwists = (int) Math.round(Math.random() * 35);
		for (int i = 0; i < numOfTwists; i++) {
			initialCube = chooseRandomTwist(initialCube);
		} 
		return Utils.deserializeCube(initialCube);
	}
	
	static public Cube generateRandomValidCube() {
		return generateRandomCube(baseCube);
	}
	
	static public Cube generateRandomInvalidEdgeCube() {
		return generateRandomCube(invalidEdgeCube);
	}
	
	static public Cube generateRandomInvalidCornerCube() {
		return generateRandomCube(invalidCornerCube);
	}
	
	static public Cube generateRandomInvalidColorsCube() {		
		return generateRandomCube(invalidColorCube);
	}
}
