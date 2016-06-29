package com.infinity.jpid;

/**
 * <p>Tells the PID if the output and input are directly related.</p>
 * 
 * <p>For example, DIRECT would be used for a fan controlling a fire.
 * The more air is delivered to the fire, the hotter the system will
 * be.  REVERSE would be used for a refrigeration unit, the more
 * energy applied, the colder it will get.</p>
 * 
 * @author Jeffrey.Richley
 */
public enum Direction {

	/**
	 * Indicates that the input and output are directly related.
	 */
	DIRECT, 
	/**
	 * Indicates that the input and output are indirectly related. 
	 */
	REVERSE
	
}
