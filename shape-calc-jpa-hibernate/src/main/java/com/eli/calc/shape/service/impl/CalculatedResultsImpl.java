package com.eli.calc.shape.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eli.calc.shape.domain.CalculationRequest;
import com.eli.calc.shape.domain.CalculationResult;
import com.eli.calc.shape.persistence.service.CalculationResultPersistService;
import com.eli.calc.shape.service.CalculatedResults;

@Component
public class CalculatedResultsImpl implements CalculatedResults {

	@Autowired
	private CalculationResultPersistService resultsPersistService;
	
	public void deleteAllResults() {
		resultsPersistService.deleteAllResults();
	}

	public void putResult(CalculationResult result) {
		resultsPersistService.saveResult(result);
	}

	public void removeResult(CalculationResult result) {
		resultsPersistService.deleteResult(result);
	}

	public boolean containsRequest(CalculationRequest request) {
		return (null!=resultsPersistService.findResultByRequest(request))?true:false;
	}

	public List<CalculationResult> getResults() {
		return resultsPersistService.getAllResults();
	}


}