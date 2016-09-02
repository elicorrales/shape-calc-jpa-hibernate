package com.eli.calc.shape.model;

public interface EqualDimShapeCalculations {

	ShapeName getShapeName();
	
	CalcType getCalcType();
	
	double calculate(double d);

	int hashCode();
	boolean equals(Object object);
}
