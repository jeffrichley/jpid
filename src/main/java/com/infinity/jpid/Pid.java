package com.infinity.jpid;

/**
 * Representation of a PID (Proportional, Integral, Derivative) control.
 * 
 * @author Jeffrey.Richley
 */
public interface Pid {

	/**
	 * Computes the new output value to be applied to the system
	 * @param input An input signal from the system
	 * @return An output to be applied to the system
	 */
	double compute(double input);
	
	/**
	 * Sets the constants used in deriving the PID's output
	 * @param kProportional The constant applied to the Proportional value
	 * @param kIntegral The constant applied to the Integral value
	 * @param kDerivative The constant applied to hte Derivative value
	 */
	void setTunings(double kProportional, double kIntegral, double kDerivative);
	
	/**
	 * <p>Set the minimum frequency to calculate the output.  If the PID
	 * is queried before this minimum amount of time has elapsed, the PID
	 * will return the last calculated output value.</p>
	 * 
	 * <p>This can cut computation time a good deal.  The higher the sampleTime
	 * is, the fewer times the PID will have to update internal values.</p>
	 * @param sampleTime The minimum computation frequency in seconds 
	 */
	void setSampleTime(long sampleTime);
	
	/**
	 * Set the limits for the PID's output.  It will not return any
	 * values below outputMinimum or above outputMaximum.
	 * @param outputMinimum The lowest desired output value
	 * @param outputMaximum The highest desired output value 
	 */
	void setOutputLimits(double outputMinimum, double outputMaximum);
	
	/**
	 * Tells the PID to automatically calculate the output values 
	 */
	void setAutomaticMode();
	
	/**
	 * Tells the PID to stop calculating the output values and
	 * simply return the outputValue instead.
	 * @param outputValue
	 */
	void setManualMode(double outputValue);
	
	/**
	 * Tells the PID if the output and input are directly proportional.
	 * If REVERSE is indicated, the system will negate the output.
	 * @param direction Tells if the output and input are directly or indirectly proportional
	 */
	void setDirection(Direction direction);
	
	/**
	 * Set the target system input value
	 * @param target The value that the system should trend toward
	 */
	void setTarget(double target);
	
}
