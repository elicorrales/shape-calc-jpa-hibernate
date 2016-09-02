package com.eli.calc.shape.service.impl;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eli.calc.shape.ShapeCalculationsFactory;
import com.eli.calc.shape.domain.CalculationRequest;
import com.eli.calc.shape.domain.CalculationResult;
import com.eli.calc.shape.model.EqualDimShapeCalculations;

public class CalculatorSingleTask implements Callable<CalculationResult> {

	private static final Logger logger = LoggerFactory.getLogger(CalculatorSingleTask.class);

	private final ShapeCalculationsFactory shapeFactory;

	private final CalculationRequest request;

	
	public CalculatorSingleTask(
			ShapeCalculationsFactory shapeFactory,
			CalculationRequest request) {
		this.shapeFactory = shapeFactory;
		this.request = request;
	}
	
	
	public CalculationResult call() throws Exception {

		CalculationResult sr = new CalculationResult(this.request);

		try {
			logger.debug("starting {} calucation of {} for value {}" ,
					request.getCalcType(),
					request.getShapeName(),
					request.getDimension()
					);


			EqualDimShapeCalculations shape = this.shapeFactory.createShapeCalculation(request.getShapeName(),request.getCalcType());
			sr.setResult(shape.calculate(this.request.getDimension()));

		} catch (IllegalArgumentException e) {

			logger.error(e.getMessage(),e);
			logger.error("bad params for this thread but perhaps we can continue");
			sr.setError(true);
			return sr;
	
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			logger.error("Possible fatal error...");
			sr.setError(true);
			throw e;
		}
	
		return sr;
	}
	
}