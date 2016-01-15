package global;

import validation.Log.DebugLevel;
//import lejos.nxt.Motor;
//import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;

/**
 * NXT global configurations
 * @author Harel Fuchs and Naama Baidatch
 */
public class Config {

    public static DebugLevel debugLevel = DebugLevel.Error;
    
    /* moving the eye any faster might result in incorrect spinBottom */
    public static final int speedTray = 650;
    /* moving the eye any faster might result in incorrect positions */
    public static final int speedEye = 500;
    public static final int speedArm = 500;
 
//    public static NXTRegulatedMotor motorTray = Motor.A;
//    public static NXTRegulatedMotor motorEye  = Motor.B;
//    public static NXTRegulatedMotor motorArm  = Motor.C;
 
    public static SensorPort colorSensorPort = SensorPort.S2;
    public static SensorPort ultraSensorPort = SensorPort.S4;
 
}
