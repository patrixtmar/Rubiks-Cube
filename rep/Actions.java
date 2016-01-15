package rep;

/**
 * Actions performed by the robot.
 * @author Harel Fuchs and Naama Baidatch
 */
public enum Actions {
    SPIN90CCW,
    SPIN90CW,
    FLIP,
    SPIN_BOTTOM_90CW,
    SPIN_BOTTOM_90CCW,
    SPIN_BOTTOM_180,	
    BSTB_U, // Bring Side To Bottom - Up
    BSTB_D, // redundant, but makes code more convenient.
    BSTB_F, // Bring Side To Bottom - Front	
    BSTB_B, // Bring Side To Bottom - Back
    BSTB_R, // Bring Side To Bottom - Right	
    BSTB_L, // Bring Side To Bottom - Left	
}
