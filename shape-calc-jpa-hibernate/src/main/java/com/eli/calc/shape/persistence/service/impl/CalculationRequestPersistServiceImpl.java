package com.eli.calc.shape.persistence.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eli.calc.shape.domain.CalculationRequest;
import com.eli.calc.shape.persistence.repository.CalculationRequestRepository;
import com.eli.calc.shape.persistence.service.CalculationRequestPersistService;

@Service
public class CalculationRequestPersistServiceImpl implements CalculationRequestPersistService {

	private static final Logger logger = LoggerFactory.getLogger(CalculationRequestPersistServiceImpl.class);
	
	@Autowired
	private CalculationRequestRepository repository;

	@Override
	public void saveRequest(CalculationRequest request) {
		logger.debug("\n\nInside saveRequest\n\n");
		//repository.save(request);
		repository.saveAndFlush(request);
		
	}

	@Override
	public void deleteRequest(CalculationRequest request) {
		logger.debug("\n\nInside deleteRequest\n\n");
		repository.delete(request);
		
	}


	@Override
	public void deleteAllRequests() {
		logger.debug("\n\nInside deleteAllRequest\n\n");
		repository.deleteAll();
	}


	@Override
	public List<CalculationRequest> getAllRequests() {
		logger.debug("\n\nInside getAllPendingRequest\n\n");
		List<CalculationRequest> source = repository.findAllByOrderByShapeNameAscCalcTypeAscDimensionAsc();
		return source;
	}


	@Override
	public long getNumRequests() {
		return repository.count();
	}

}
