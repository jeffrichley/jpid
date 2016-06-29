package com.infinity.jpid;

/**
 * A basic PID controller algorithm borrowed liberally from Brett Beauregard's 
 * article http://brettbeauregard.com/blog/2011/04/improving-the-beginners-pid-introduction/.
 * 
 * @author Jeffrey.Richley
 */
public class BasicPid implements Pid {

	/**
	 * The target input to move the system towards
	 */
	private double setTarget;
	
	/**
	 * The constant to multiply the Proportional with
	 */
	private double kProportional;
	
	/**
	 * The constant to multiply the Integral with
	 */
	private double kIntegral;

	/**
	 * The constant to multiply the Derivative with
	 */
	private double kDerivative;
	
	/**
	 * The previously calculated Integral signal
	 */
	private double iTerm = 0.0;

	/**
	 * Previous information that was received from the system
	 */
	private double lastInput = 0.0;

	/**
	 * Determines how often the PID should calculate a new output value.
	 * Measured in seconds
	 */
	private long sampleTime = 1;

	/**
	 * Tells when the previous calculation was performed
	 */
	private long lastTime = 0;
	
	/**
	 * The previously calculated output
	 */
	private double lastOutput = 0.0;

	/**
	 * Tells the PID if it should be calculating the output value for automatic
	 * or if it should return a constant in manual mode.
	 */
	private Mode mode = Mode.AUTOMATIC;

	/**
	 * If the PID is set in Manual mode, it will return the manualOutputValue
	 */
	private double manualOutputValue = 0.0;

	/**
	 * The minimum that the PID should return as its output
	 */
	private double outputMinimum = Double.NEGATIVE_INFINITY;

	/**
	 * The maximum that the PID should return as its output
	 */
	private double outputMaximum = Double.POSITIVE_INFINITY;

	/**
	 * Tells the PID if the output is in direct or indirect relation
	 * with the input value
	 */
	private Direction direction = Direction.DIRECT;
	
	/* (non-Javadoc)
	 * @see com.infinity.jpid.Pid#compute(double)
	 */
	public double compute(double input) {
		if (mode == Mode.MANUAL) {
			return manualOutputValue;
		}
		
		long now = System.currentTimeMillis();
		long timeChange = now - lastTime ;
		
		if (timeChange >= sampleTime) {
			/*Compute all the working error variables*/
		    double error = setTarget - input;
		    iTerm += kIntegral * error;
		    if (iTerm > outputMaximum) {
		    	iTerm = outputMaximum;
		    } else if (iTerm < outputMinimum) {
		    	iTerm = outputMinimum;
		    }
		    double dInput = input - lastInput;
		    
		    /*Compute PID Output*/
		    double output = (kProportional * error) + iTerm - (kDerivative * dInput);
		    if (output > outputMaximum) {
		    	output = outputMaximum;
		    } else if (output < outputMinimum) {
		    	output = outputMinimum;
		    }
		    
		    /*Remember some variables for next time*/
		    lastInput = input;
		    lastTime = now;
		    lastOutput = output;
		    		
		    return output;
		}
	    
		return lastOutput;
	}

	/* (non-Javadoc)
	 * @see com.infinity.jpid.Pid#setTunings(double, double, double)
	 */
	public void setTunings(double kProportional, double kIntegral, double kDerivative) {
		if (kProportional < 0 || kIntegral < 0 || kDerivative < 0) {
			return;
		}
		
		long tempSampleTime = this.sampleTime == 0 ? 1 : this.sampleTime;
		
		this.kProportional = kProportional;
		this.kIntegral = kIntegral * tempSampleTime;
		this.kDerivative = kDerivative / tempSampleTime;
		
		if (Direction.REVERSE == this.direction) {
			this.kProportional = 0 - this.kProportional;
			this.kIntegral = 0 - this.kIntegral;
			this.kDerivative = 0 - this.kDerivative;
		}
	}

	/* (non-Javadoc)
	 * @see com.infinity.jpid.Pid#setSampleTime(long)
	 */
	public void setSampleTime(long sampleTime) {
		if (this.sampleTime > 0) {
			double ratio = (double)sampleTime / (double)this.sampleTime;
			this.kIntegral *= ratio;
			this.kDerivative /= ratio;
		}
		
		this.sampleTime = sampleTime;
	}

	/* (non-Javadoc)
	 * @see com.infinity.jpid.Pid#setOutputLimits(double, double)
	 */
	public void setOutputLimits(double outputMinimum, double outputMaximum) {
		this.outputMinimum = outputMinimum;
		this.outputMaximum = outputMaximum;
	}

	/* (non-Javadoc)
	 * @see com.infinity.jpid.Pid#setAutomaticMode()
	 */
	public void setAutomaticMode() {
		if (Mode.MANUAL == this.mode) {
			iTerm = lastOutput;
			if (iTerm > outputMaximum) {
				iTerm = outputMaximum;
			} else if (iTerm < outputMinimum) {
				iTerm = outputMinimum;
			}
		}
		
		this.mode = Mode.AUTOMATIC;
	}

	/* (non-Javadoc)
	 * @see com.infinity.jpid.Pid#setManualMode(double)
	 */
	public void setManualMode(double outputValue) {
		this.mode = Mode.MANUAL;
		this.manualOutputValue = outputValue;
	}

	/* (non-Javadoc)
	 * @see com.infinity.jpid.Pid#setDirection(com.infinity.jpid.Direction)
	 */
	public void setDirection(Direction direction) {
		if (direction != this.direction) {
			this.kProportional = 0 - this.kProportional;
			this.kIntegral = 0 - this.kIntegral;
			this.kDerivative = 0 - this.kDerivative;
		}
		
		this.direction = direction;
	}

	/* (non-Javadoc)
	 * @see com.infinity.jpid.Pid#setTarget(double)
	 */
	public void setTarget(double target) {
		this.setTarget = target;
	}

	/**
	 * Gets the constant for the proportional
	 * @return The constant for the proportional
	 */
	public double getKProportional() {
		return kProportional;
	}
	
	/**
	 * Gets the constant for the integral
	 * @return The constant for the integral
	 */
	public double getKIntegral() {
		return kIntegral;
	}
	
	/**
	 * Gets the constant for the derivative
	 * @return The constant for the derivative
	 */
	public double getKDerivative() {
		return kDerivative;
	}
	
}
