package com.eli.calc.shape.persistence.service;

import java.util.List;

import com.eli.calc.shape.domain.CalculationRequest;
import com.eli.calc.shape.domain.CalculationResult;

public interface CalculationResultPersistService {

	public void saveResult(CalculationResult result);
	
	public void deleteResult(CalculationResult result);
	
	public void deleteAllResults();
	
	public List<CalculationResult> getAllResults();
	
	public CalculationResult findResultByRequest(CalculationRequest request);
}
