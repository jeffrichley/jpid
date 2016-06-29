package com.infinity.jpid;

/**
 * Enumerates the various modes that the PID can operate.
 * 
 * @author Jeffrey.Richley
 */
public enum Mode {

	/**
	 * Sets the PID into an automatic mode where it will calculate
	 * the output for the system. 
	 */
	AUTOMATIC, 
	/**
	 * Sets the PID into a mode where it no longer is calculating
	 * any outputs and it will use the value that it was told to use. 
	 */
	MANUAL
	
}
