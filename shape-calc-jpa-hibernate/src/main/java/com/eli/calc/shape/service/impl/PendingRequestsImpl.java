package com.eli.calc.shape.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eli.calc.shape.domain.CalculationRequest;
import com.eli.calc.shape.persistence.service.CalculationRequestPersistService;
import com.eli.calc.shape.service.PendingRequests;

@Component
public class PendingRequestsImpl implements PendingRequests {

	private static final Logger logger = LoggerFactory.getLogger(PendingRequestsImpl.class);

	@Autowired
	private CalculationRequestPersistService requestsPersistService;
	
	public void deleteAllRequests() {
		logger.debug("\n\ndeleteAllRequests\n\n");
		requestsPersistService.deleteAllRequests();
	}

	public List<CalculationRequest> getRequests() {
		return requestsPersistService.getAllRequests();
	}

	public void putRequest(CalculationRequest request) {
		logger.debug("\n\nputRequest\n\n");
		requestsPersistService.saveRequest(request);
	}

	public void removeRequest(CalculationRequest request) {
		requestsPersistService.deleteRequest(request);
	}

	public long getNumRequests() {
		return requestsPersistService.getNumRequests();
	}


}