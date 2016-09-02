package com.eli.calc.shape.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.eli.calc.shape.model.CalcType;
import com.eli.calc.shape.model.ShapeName;
import com.eli.calc.shape.model.TetrahedronCalculations;

@Component("tetraArea")
public final class TetrahedronAreaCalculationImpl extends AbstractShapeCalculationsImpl implements TetrahedronCalculations {

	 private static final Logger logger = LoggerFactory.getLogger(TetrahedronAreaCalculationImpl.class);

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

		logger.debug("calc surface area of Tetrahedron of {}",n);

		faketime.sleep();
		
		return Math.sqrt(3)  * (n * n);
		
	}


}
