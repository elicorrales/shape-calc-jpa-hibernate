package com.eli.calc.shape.impl;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PsuedoTimeHelper {

	private static final Logger logger = LoggerFactory.getLogger(PsuedoTimeHelper.class);
 
	private final long generatedLong;
	
	PsuedoTimeHelper() {
		
	    long minLimit = 0L;
	    long maxLimit = 2000L;
	    generatedLong = minLimit + (long) (Math.random() * (maxLimit - minLimit));
	    logger.debug("Psuedo time is {}: ",generatedLong);
	}

	void sleep() {

		try { TimeUnit.MILLISECONDS.sleep(generatedLong); }
		catch (Exception e){}

	}
}
