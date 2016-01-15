package validation;

import global.Config;
import lejos.nxt.Button;
import lejos.nxt.LCD;

/**
 * Logging information on NXT brick
 * @author Harel Fuchs and Naama Baidatch
 */
public class Log {
	
	public enum DebugLevel {
		VeryVerbose,	// more info
		Verbose, 		// some info
		Error,    		// info only on error
	}
	
	// always wait
	public static void a(String msg) {
		LCD.clear();
		System.out.println(msg);
		Button.waitForAnyPress();
	}
	
	// wait on error
	public static void e (String msg) {
		LCD.clear();
		System.out.println(msg);
		wait(DebugLevel.Error);
	}
	
	// wait on verbose or error
	public static void v (String msg) {
		LCD.clear();
		System.out.println(msg);
		wait(DebugLevel.Verbose);
	}
	
	// wait on very-verbose, verbose, or error
	public static void vv (String msg) {
		LCD.clear();
		System.out.println(msg);
		wait(DebugLevel.VeryVerbose);
	}

	private static void wait(DebugLevel lvl) {
		if (Config.debugLevel.ordinal() <= lvl.ordinal()){
			Button.waitForAnyPress();
		}
	}

}
