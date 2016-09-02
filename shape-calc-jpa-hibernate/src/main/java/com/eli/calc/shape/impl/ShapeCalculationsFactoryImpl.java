package com.eli.calc.shape.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eli.calc.shape.ShapeCalculationsFactory;
import com.eli.calc.shape.model.CalcType;
import com.eli.calc.shape.model.CircleCalculations;
import com.eli.calc.shape.model.CubeCalculations;
import com.eli.calc.shape.model.EqualDimShapeCalculations;
import com.eli.calc.shape.model.EquilateralTriangleCalculations;
import com.eli.calc.shape.model.ShapeName;
import com.eli.calc.shape.model.SphereCalculations;
import com.eli.calc.shape.model.SquareCalculations;
import com.eli.calc.shape.model.TetrahedronCalculations;

@Component
public class ShapeCalculationsFactoryImpl implements ShapeCalculationsFactory {


	@Autowired
	private CircleCalculations circleArea;
	@Autowired
	private CircleCalculations circleVolume;
	
	@Autowired
	private SquareCalculations squareArea;
	@Autowired
	private SquareCalculations squareVolume;
	
	@Autowired
	private EquilateralTriangleCalculations triangleArea;
	@Autowired
	private EquilateralTriangleCalculations triangleVolume;
	
	@Autowired
	private SphereCalculations sphereArea;
	@Autowired
	private SphereCalculations sphereVolume;
	
	@Autowired
	private CubeCalculations cubeArea;
	@Autowired
	private CubeCalculations cubeVolume;
	
	@Autowired
	private TetrahedronCalculations tetraArea;
	@Autowired
	private TetrahedronCalculations tetraVolume;
	
	@Override
	public EqualDimShapeCalculations createShapeCalculation(ShapeName shapeName, CalcType type) {
		
		if (null==shapeName) throw new IllegalArgumentException("Must have a ShapeName");
		if (null==type) throw new IllegalArgumentException("Must have a CalculationType");

		switch (shapeName) {

			case CIRCLE:
				switch (type) {
					case CALC_AREA:
							//return new CircleAreaCalculationImpl();
							return this.circleArea;
					case CALC_VOLUME:
							//return new CircleVolumeCalculationImpl();
							return this.circleVolume;
					default:
							throw new IllegalArgumentException("Unknown Calc Type: "+ type);
				}

	
			case SQUARE:
				switch (type) {
					case CALC_AREA:
							//return new SquareAreaCalculationImpl();
							return this.squareArea;
					case CALC_VOLUME:
							//return new SquareVolumeCalculationImpl();
							return this.squareVolume;
					default:
							throw new IllegalArgumentException("Unknown Calc Type: "+ type);
				}

	
			case EQUILATERALTRIANGLE:
				switch (type) {
					case CALC_AREA:
							//return new EquilateralTriangleAreaCalculationImpl();
							return this.triangleArea;
					case CALC_VOLUME:
							//return new EquilateralTriangleVolumeCalculationImpl();
							return this.triangleVolume;
					default:
							throw new IllegalArgumentException("Unknown Calc Type: "+ type);
				}

	
			case SPHERE:
				switch (type) {
					case CALC_AREA:
							//return new SphereAreaCalculationImpl();
							return this.sphereArea;
					case CALC_VOLUME:
							//return new SphereVolumeCalculationImpl();
							return this.sphereVolume;
					default:
							throw new IllegalArgumentException("Unknown Calc Type: "+ type);
				}


			case CUBE:
				switch (type) {
					case CALC_AREA:
							//return new CubeAreaCalculationImpl();
							return this.cubeArea;
					case CALC_VOLUME:
							//return new CubeVolumeCalculationImpl();
							return this.cubeVolume;
					default:
							throw new IllegalArgumentException("Unknown Calc Type: "+ type);
				}
	
				
			case TETRAHEDRON:
				switch (type) {
					case CALC_AREA:
							//return new TetrahedronAreaCalculationImpl();
							return this.tetraArea;
					case CALC_VOLUME:
							//return new TetrahedronVolumeCalculationImpl();
							return this.tetraVolume;
					default:
							throw new IllegalArgumentException("Unknown Calc Type: "+ type);
				}
	
	
			default: throw new IllegalArgumentException("Unknown ShapeName: "+ shapeName);

		}
	}
}
