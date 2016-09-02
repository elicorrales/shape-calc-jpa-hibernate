package com.eli.calc.shape.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.eli.calc.shape.model.CalcType;
import com.eli.calc.shape.model.EquilateralTriangleCalculations;
import com.eli.calc.shape.model.ShapeName;

@Component("triangleArea")
public final class EquilateralTriangleAreaCalculationImpl extends AbstractShapeCalculationsImpl implements EquilateralTriangleCalculations {

	private static final Logger logger = LoggerFactory.getLogger(EquilateralTriangleAreaCalculationImpl.class);

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

		logger.debug("calc area of Equilateral Triangle of {}", n);

		faketime.sleep();
		
		return (Math.sqrt(3) / 4 ) * (n * n);
		
	}

}
