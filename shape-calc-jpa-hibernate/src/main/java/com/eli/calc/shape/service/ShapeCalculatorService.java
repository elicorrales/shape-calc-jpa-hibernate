package com.eli.calc.shape.service;

import java.util.List;

import com.eli.calc.shape.domain.CalculationRequest;
import com.eli.calc.shape.domain.CalculationResult;
import com.eli.calc.shape.model.CalcType;
import com.eli.calc.shape.model.ShapeName;

public interface ShapeCalculatorService {

	void deleteAllPendingRequests();
	
	void deleteAllResults();
	
	/**
	 * 
	 * @param shapeName - must not be null;
	 * @param calcType  - must not be null;
	 * @param dimension - must be greater than / equal to zero;
	 * 
	 * Attempting to queue another request with the same
	 * param values will have no effect.
	 * 
	 * An IllegalArgumentException will be thrown
	 * if any of the params do not meet criteria
	 */
	void queueCalculationRequest(
			ShapeName shapeName,
			CalcType calcType,
			double dimension
			);
	
	/**
	 * @return - the list of pending requests
	 * (not yet run)
	 */
	List<CalculationRequest> getAllPendingRequests();
	
	/**
	 * @return - the list of results
	 */
	List<CalculationResult> getAllCalculationResults();
	
	/**
	 * Runs the calculations of all pending requests
	 * in a multi-threaded manner.
	 * 
	 * Not all request queue attempts will make it
	 * into the queue. Any previously queued, or
	 * previously run request, will not be re-run.
	 * 
	 * Pending/Queued requests are thrown away once
	 * they have been run.  Thus, an invocation of
	 * this operation removes all pending requests
	 * that existed at that instance in time.
	 * 
	 * @return - the number of Requests run
	 */
	int runAllPendingRequestsStopOnError();
	int runAllPendingRequestsNoStopOnError();
	
}