package com.eli.calc.shape.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.eli.calc.shape.model.CalcType;
import com.eli.calc.shape.model.ShapeName;
import com.eli.calc.shape.model.TetrahedronCalculations;

@Component("tetraVolume")
public final class TetrahedronVolumeCalculationImpl extends AbstractShapeCalculationsImpl implements TetrahedronCalculations {

	 private static final Logger logger = LoggerFactory.getLogger(TetrahedronVolumeCalculationImpl.class);

	@Override
	public CalcType getCalcType() { return CalcType.CALC_VOLUME; }

	@Override
	public ShapeName getShapeName() { return name; }

	/**
	 * this calculates volume
	 */
	@Override
	public double calculate(double n) {

		if (0>n) throw new IllegalArgumentException("dimension must not be less than zero");

		logger.debug("calc volume of Tetrahedron of {}",n);

		faketime.sleep();

		double sqrt = Math.sqrt(2);
		double denominator = (6*sqrt);
		double nCubed = n*n*n;
		
		return nCubed / denominator;
	}


}
