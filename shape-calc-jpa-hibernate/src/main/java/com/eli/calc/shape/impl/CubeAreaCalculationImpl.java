package com.eli.calc.shape.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.eli.calc.shape.model.CalcType;
import com.eli.calc.shape.model.CubeCalculations;
import com.eli.calc.shape.model.ShapeName;

@Component("cubeArea")
public final class CubeAreaCalculationImpl extends AbstractShapeCalculationsImpl implements CubeCalculations {

	private static final Logger logger = LoggerFactory.getLogger(CubeAreaCalculationImpl.class);

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

		logger.debug("calc surface area of Cube of {}", n);
		
		faketime.sleep();

		return 6*n*n;
	}

}
