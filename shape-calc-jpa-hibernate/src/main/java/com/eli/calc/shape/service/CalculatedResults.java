package com.eli.calc.shape.service;


import java.util.List;

import com.eli.calc.shape.domain.CalculationRequest;
import com.eli.calc.shape.domain.CalculationResult;

public interface CalculatedResults {

	void deleteAllResults();
	
	void putResult(CalculationResult result);
	
	void removeResult(CalculationResult result);
	
	boolean containsRequest(CalculationRequest request);
	
	List<CalculationResult> getResults();

}