package com.eli.calc.shape.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.eli.calc.shape.model.CalcType;
import com.eli.calc.shape.model.ShapeName;
import com.eli.calc.shape.model.SphereCalculations;

@Component("sphereVolume")
public final class SphereVolumeCalculationImpl extends AbstractShapeCalculationsImpl implements SphereCalculations {

	 private static final Logger logger = LoggerFactory.getLogger(SphereVolumeCalculationImpl.class);

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

		logger.debug("calc volume of Sphere of {}", n);
		
		faketime.sleep();
		
		double fourdivthree = (4.0/3.0);
		double powofthree   = Math.pow(n, 3);
		return fourdivthree*Math.PI*powofthree;
	}

}
