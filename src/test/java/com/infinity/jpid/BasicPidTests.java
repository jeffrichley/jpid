package com.infinity.jpid;

import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class BasicPidTests {

	private BasicPid cut;

	@Before
	public void setUp() throws Exception {
		cut = new BasicPid();
		cut.setTarget(100.0);
	}

	@Test
	public void ensureCanCalculateProportional() {
		cut.setTunings(0.5, 0.0, 0.0);
		double output = cut.compute(50);
		assertThat(output, closeTo(25.0, 0.00001));
	}

	@Test
	public void ensureCanCalculateIntegral() {
		cut.setTunings(0.0, 0.1, 0.0);
		double output = cut.compute(50);
		assertThat(output, closeTo(5.0, 0.00001));
	}

	@Test
	public void ensureCanCalculateDerivative() {
		cut.setTunings(1.0, 0.0, 0.2);
		double output = cut.compute(50);
		assertThat(output, closeTo(40.0, 0.00001));

		output = cut.compute(60);
		assertThat(output, closeTo(38.0, 0.00001));
	}
	
	@Test
	public void ensureWaitsForCalculation() {
		cut.setTunings(0.5, 0.0, 0.0);
		cut.setSampleTime(1000);
		
		cut.compute(50);
		double output = cut.compute(100);
		assertThat(output, closeTo(25.0, 0.00001));
	}
	
	@Test
	public void ensureCanSwitchManualAutomatic() {
		cut.setTunings(0.5, 0.0, 0.0);
		double output = cut.compute(50);
		assertThat(output, closeTo(25.0, 0.00001));
		
		cut.setManualMode(10.0);
		output = cut.compute(50);
		assertThat(output, closeTo(10.0, 0.00001));
		
		cut.setAutomaticMode();
		output = cut.compute(50);
		assertThat(output, closeTo(25.0, 0.00001));
	}
	
	@Test
	public void ensureCanSetMinimumMaxiumValues() {
		cut.setOutputLimits(20, 50);
		cut.setTunings(1.0, 0.1, 1.0);
		cut.setSampleTime(0);
		
		double output = cut.compute(100.0);
		assertThat(output, closeTo(20.0, 0.00001));
		
		output = cut.compute(-1000.0);
		assertThat(output, closeTo(50.0, 0.00001));
	}
	
	@Test
	public void ensureRequiresPositiveTunings() {
		cut.setTunings(-1.0, 0.0, 0.0);
		assertThat(cut.getKProportional(), closeTo(0.0, 0.0001));
		
		cut.setTunings(0.0, -1.0, 0.0);
		assertThat(cut.getKIntegral(), closeTo(0.0, 0.0001));
		
		cut.setTunings(0.0, 0.0, -1.0);
		assertThat(cut.getKDerivative(), closeTo(0.0, 0.0001));
	}
	
	@Test
	public void ensureScalesTunings() {
		cut.setSampleTime(2);
		cut.setTunings(1.0, 0.5, 0.1);
		
		assertThat(cut.getKProportional(), closeTo(1.0, 0.0001));
		assertThat(cut.getKIntegral(), closeTo(1.0, 0.0001));
		assertThat(cut.getKDerivative(), closeTo(0.05, 0.0001));
		
		cut.setSampleTime(1);
		assertThat(cut.getKProportional(), closeTo(1.0, 0.0001));
		assertThat(cut.getKIntegral(), closeTo(0.5, 0.0001));
		assertThat(cut.getKDerivative(), closeTo(0.1, 0.0001));
	}
	
	@Test
	public void ensureCanChangeDirections() {
		cut.setSampleTime(0);
		
		cut.setDirection(Direction.REVERSE);
		cut.setTunings(0.5, 0.2, 0.1);
		double output = cut.compute(50);
		assertThat(output, closeTo(-30.0, 0.00001));
		
		cut.setDirection(Direction.DIRECT);
		output = cut.compute(50);
		assertThat(output, closeTo(25.0, 0.00001));
		
		cut.setDirection(Direction.REVERSE);
		output = cut.compute(50);
		assertThat(output, closeTo(-35.0, 0.00001));
	}
}
