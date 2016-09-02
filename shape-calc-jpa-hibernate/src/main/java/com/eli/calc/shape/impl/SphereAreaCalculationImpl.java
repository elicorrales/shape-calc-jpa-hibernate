package com.eli.calc.shape.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.eli.calc.shape.model.CalcType;
import com.eli.calc.shape.model.ShapeName;
import com.eli.calc.shape.model.SphereCalculations;

@Component("sphereArea")
public final class SphereAreaCalculationImpl extends AbstractShapeCalculationsImpl implements SphereCalculations {

	 private static final Logger logger = LoggerFactory.getLogger(SphereAreaCalculationImpl.class);

	@Override
	public CalcType getCalcType() { return CalcType.CALC_AREA; }

	@Override
	public ShapeName getShapeName() { return name; }

	/**
	 * this calculates area
	 */
	@Override
	public double calculate(double n) {
		
		if (0>n) throw new IllegalArgumentException("dimension must not be less than zero");

		logger.debug("calc surface area of Sphere of {}",n);

		faketime.sleep();
		
		return 4*Math.PI*n*n;
	}
	
}
