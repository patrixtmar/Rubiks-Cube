package main;

import java.io.IOException;


/**
 * Main class, used for dispatching between NXT project and PC project
 * @author Harel Fuchs and Naama Baidatch
 *
 */
public class Main {

	public static Phase phase;

	public static void main(String[] args) throws IOException  {
		// Choose wisely ... 
		
		//Main_Robo.main_robo();
		Main_Server.main_server();
	}

	

}