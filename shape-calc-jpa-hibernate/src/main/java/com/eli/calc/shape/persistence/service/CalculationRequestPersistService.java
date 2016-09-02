package com.eli.calc.shape.persistence.service;

import java.util.List;

import com.eli.calc.shape.domain.CalculationRequest;

public interface CalculationRequestPersistService {

	public void deleteAllRequests();
	
	public void saveRequest(CalculationRequest request);

	public List<CalculationRequest> getAllRequests();
	
	public void deleteRequest(CalculationRequest request);

	public long getNumRequests();
}
