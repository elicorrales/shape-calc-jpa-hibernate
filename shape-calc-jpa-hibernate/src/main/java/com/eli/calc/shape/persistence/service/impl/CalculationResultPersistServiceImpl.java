package com.eli.calc.shape.persistence.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eli.calc.shape.domain.CalculationRequest;
import com.eli.calc.shape.domain.CalculationResult;
import com.eli.calc.shape.persistence.repository.CalculationResultRepository;
import com.eli.calc.shape.persistence.service.CalculationResultPersistService;

@Service
public class CalculationResultPersistServiceImpl implements CalculationResultPersistService {

	private static final Logger logger = LoggerFactory.getLogger(CalculationResultPersistServiceImpl.class);
	
	@Autowired
	private CalculationResultRepository repository;

	@Override
	public void saveResult(CalculationResult result) {
		
		repository.save((CalculationResult)result);
	}

	@Override
	public void deleteResult(CalculationResult result) {
		
		repository.delete((CalculationResult)result);
	}

	@Override
	public List<CalculationResult> getAllResults() {
		List<CalculationResult> source = repository.findAllByOrderByShapeNameAscCalcTypeAscDimensionAsc();
		return source;
	}


	@Override
	public CalculationResult findResultByRequest(CalculationRequest request) {
		CalculationResult result = 
				repository.findByShapeNameAndCalcTypeAndDimension( request.getShapeName(), request.getCalcType(), request.getDimension());

		return result;
	}

	@Override
	public void deleteAllResults() {
		
		repository.deleteAll();
		
	}

}
