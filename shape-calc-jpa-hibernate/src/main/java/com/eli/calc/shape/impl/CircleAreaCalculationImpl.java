package com.eli.calc.shape.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.eli.calc.shape.model.CalcType;
import com.eli.calc.shape.model.CircleCalculations;
import com.eli.calc.shape.model.ShapeName;

@Component("circleArea")
public final class CircleAreaCalculationImpl extends AbstractShapeCalculationsImpl implements CircleCalculations {

	 private static final Logger logger = LoggerFactory.getLogger(CircleAreaCalculationImpl.class);

	@Override
	public CalcType getCalcType() { return CalcType.CALC_AREA; }

	@Override
	public ShapeName getShapeName() { return name; }

	/**
	 * this calculates the area
	 */
	@Override
	public double calculate(double n) {
		
		if (0>n) throw new IllegalArgumentException("dimension must not be less than zero");

		logger.debug("calc area of Circle of " + n);

		faketime.sleep();
		
		return Math.PI*n*n;
	}

}
