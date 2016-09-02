package com.eli.calc.shape.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eli.calc.shape.ShapeCalculationsFactory;
import com.eli.calc.shape.domain.CalculationRequest;
import com.eli.calc.shape.domain.CalculationResult;
import com.eli.calc.shape.model.CalcType;
import com.eli.calc.shape.model.ShapeName;
import com.eli.calc.shape.service.CalculatedResults;
import com.eli.calc.shape.service.PendingRequests;
import com.eli.calc.shape.service.ShapeCalculatorService;

@Service
public class ShapeCalculatorServiceImpl implements ShapeCalculatorService {

	private static final Logger logger = LoggerFactory.getLogger(ShapeCalculatorServiceImpl.class);
	
	@Autowired
	private ExecutorService executor;
	
	@Autowired
	private ShapeCalculationsFactory shapeFactory;

	@Autowired
	private PendingRequests pendingRequests;
	
	@Autowired
	private CalculatedResults calculatedResults;
	
	public void deleteAllPendingRequests() {
		pendingRequests.deleteAllRequests();
	}

	public void deleteAllResults() {
		calculatedResults.deleteAllResults();
	}

	public void queueCalculationRequest(ShapeName shapeName, CalcType calcType, double dimension) {

		if (null==shapeName) { throw new IllegalArgumentException("ShapeName can not be null"); }
		if (null==calcType) { throw new IllegalArgumentException("CalcType can not be null"); }
		if (0>dimension) { throw new IllegalArgumentException("dimension must be zero or positive"); }
		
		CalculationRequest request = new CalculationRequest(shapeName,calcType,dimension);
		
		if (calculatedResults.containsRequest(request)) { return; }
		
		pendingRequests.putRequest(request);
	}

	public List<CalculationRequest> getAllPendingRequests() {
		return pendingRequests.getRequests();
	}

	public List<CalculationResult> getAllCalculationResults() {
		return calculatedResults.getResults();
	}

	public int runAllPendingRequestsStopOnError() {
		return runAllPendingRequests(true);
	}

	public int runAllPendingRequestsNoStopOnError() {
		return runAllPendingRequests(false);
	}

	private int runAllPendingRequests(boolean stopOnError) {
		

		if (pendingRequests.getNumRequests()<1) {
			logger.warn("NO calculations run - there are no pending requests");
			int numRun = 0;
			return numRun;
		}
	
		//lazy instantiation - see inside loop
		List<Future<CalculationResult>> futureResults = null;

		for (CalculationRequest cr :  pendingRequests.getRequests()) {
			
			// we may have previously cached a result - if so no need to re-run the request
			if (calculatedResults.containsRequest(cr)){ continue; }

			// if we made it to here, we have a request we need to run calc on

			//lazy instantiation
			if (null==futureResults) { futureResults = new ArrayList<Future<CalculationResult>>(); }

			CalculatorSingleTask task = new CalculatorSingleTask(shapeFactory,cr);
			Future<CalculationResult> futureResult = executor.submit(task);
			futureResults.add(futureResult);
		}

		int numRun = 0;

		if (null!=futureResults) {
			for (Future<CalculationResult> futureResult : futureResults) {
				try {
					CalculationResult result = futureResult.get();

					if (stopOnError && result.isError()) {

						CalculationRequest req = result.getRequest();
						pendingRequests.removeRequest(req); //bad request, get it off queue
						throw new IllegalArgumentException("Bad Param "+((null!=req)?req.toString():""));

					} else if (result.isError()) {

						CalculationRequest req = result.getRequest();
						logger.error("Bad Param "+((null!=req)?req.toString():""));
						pendingRequests.removeRequest(req); //bad request, get it off queue
						continue;

					}

					pendingRequests.removeRequest(result.getRequest()); //ran request, get it off queue
					calculatedResults.putResult(result);
					numRun++;

				} catch (ExecutionException e) {
					logger.error(e.getMessage(),e);
					throw new RuntimeException(e);
				} catch (InterruptedException e) {
					logger.error(e.getMessage(),e);
					throw new RuntimeException(e);
				}
			}
		}

		return numRun;
	}

}