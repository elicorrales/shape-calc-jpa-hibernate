package com.eli.calc.shape;


import com.eli.calc.shape.model.CalcType;
import com.eli.calc.shape.model.EqualDimShapeCalculations;
import com.eli.calc.shape.model.ShapeName;

public interface ShapeCalculationsFactory {

	EqualDimShapeCalculations createShapeCalculation(ShapeName shapeName, CalcType type);
}