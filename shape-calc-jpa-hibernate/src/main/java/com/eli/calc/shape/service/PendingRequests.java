package com.eli.calc.shape.service;

import java.util.List;

import com.eli.calc.shape.domain.CalculationRequest;

public interface PendingRequests {

	void deleteAllRequests();
	
	void putRequest(CalculationRequest request);
	
	List<CalculationRequest> getRequests();
	
	void removeRequest(CalculationRequest request);

	long getNumRequests();

}