package com.eli.calc.shape.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.eli.calc.shape.config.ShapeCalcBaseContext;
import com.eli.calc.shape.domain.CalculationRequest;
import com.eli.calc.shape.domain.CalculationResult;
import com.eli.calc.shape.model.CalcType;
import com.eli.calc.shape.model.ShapeName;
import com.eli.calc.shape.service.ShapeCalculatorService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={ShapeCalcBaseContext.class})
public class JUnitTest implements ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(JUnitTest.class);

	private ApplicationContext ctx;
	
	@Autowired
	private ShapeCalculatorService calculator;
	
	@Rule
	public ExpectedException illegalArgThrown = ExpectedException.none();
	
	@Override
	public void setApplicationContext(ApplicationContext ctx) {
		
		this.ctx = ctx;
	}
	
	
	@Before // each test
	public void setUp() throws Exception {
		calculator.deleteAllPendingRequests();
		calculator.deleteAllResults();
	}

	@After // each test
	public void tearDown() throws Exception {
	}

	@Test
	public void testDeleteAllResults() {
		calculator.deleteAllResults();
	}

	@Test
	public void testQueueCalculationRequest() {
		double dimension = 0;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);
	}

	@Test
	public void testGetAllPendingRequests() {
		calculator.getAllPendingRequests();
	}

	@Test
	public void testGetAllCalculationResults() {
		calculator.getAllCalculatedResults();
	}

	@Test
	public void testRunAllPendingRequestsStopOnError() {
		calculator.runAllPendingRequestsStopOnError();
	}

	@Test
	public void testRunAllPendingRequestsNoStopOnError() {
		calculator.runAllPendingRequestsNoStopOnError();
	}

	@Test
	public void testQueueRequestWithNullShapeName() {
		
		illegalArgThrown.expect(IllegalArgumentException.class);
		double dimension = 0;
		calculator.queueCalculationRequest(null, CalcType.CALC_AREA, dimension);
	}

	@Test
	public void testQueueRequestWithNullCalcType() {
		
		illegalArgThrown.expect(IllegalArgumentException.class);
		double dimension = 0;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, null, dimension);
	}

	@Test
	public void testQueueRequestWithNegativeDimension() {
		
		illegalArgThrown.expect(IllegalArgumentException.class);
		double dimension = -0.01;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);
	}

	@Test
	public void testQueueRequestAndRetrievePendingRequest() {

		calculator.deleteAllPendingRequests();

		double dimension = 0;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);
		List<CalculationRequest> requests = calculator.getAllPendingRequests();
		
		assertNotNull(requests);
		assertEquals(1,requests.size());
		
		calculator.deleteAllPendingRequests();

		requests = calculator.getAllPendingRequests();

		assertNotNull(requests);
		assertEquals(0,requests.size());
	}


	@Test
	public void testQueueRequestAndRetrievePendingMultipleSameRequests() {

		calculator.deleteAllPendingRequests();

		double dimension = 0;

		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);

		List<CalculationRequest> requests = calculator.getAllPendingRequests();
		
		assertNotNull(requests);
		assertEquals(1,requests.size());
		
		calculator.deleteAllPendingRequests();

		requests = calculator.getAllPendingRequests();

		assertNotNull(requests);
		assertEquals(0,requests.size());
	}


	@Test
	public void testQueueRequestAndRetrievePendingMultipleDifferentRequests() {

		calculator.deleteAllPendingRequests();

		double dimension = 0.000;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);
		dimension = 0.001;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);
		dimension = 0.002;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);

		dimension = 0.000;
		calculator.queueCalculationRequest(ShapeName.SQUARE, CalcType.CALC_AREA, dimension);
		dimension = 0.001;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_VOLUME, dimension);
		dimension = 0.002;
		calculator.queueCalculationRequest(ShapeName.SPHERE, CalcType.CALC_AREA, dimension);


		List<CalculationRequest> requests = calculator.getAllPendingRequests();
		assertNotNull(requests);
		assertEquals(6,requests.size());
		
		calculator.deleteAllPendingRequests();
		requests = calculator.getAllPendingRequests();
		assertNotNull(requests);
		assertEquals(0,requests.size());
	}


	@Test
	public void testQueueRequestAndRunNoStop() {

		calculator.deleteAllPendingRequests();

		double dimension = 0.000;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);
		dimension = 0.001;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);
		dimension = 0.002;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);

		dimension = 0.000;
		calculator.queueCalculationRequest(ShapeName.SQUARE, CalcType.CALC_AREA, dimension);
		dimension = 0.001;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_VOLUME, dimension);
		dimension = 0.002;
		calculator.queueCalculationRequest(ShapeName.SPHERE, CalcType.CALC_AREA, dimension);


		List<CalculationRequest> requests = calculator.getAllPendingRequests();
		assertNotNull(requests);
		assertEquals(6,requests.size());
	
		int numRun = calculator.runAllPendingRequestsNoStopOnError();
		assertEquals(6,numRun);

		requests = calculator.getAllPendingRequests();
		assertNotNull(requests);
		assertEquals(0,requests.size());
		
		List<CalculationResult> results = calculator.getAllCalculatedResults();
		assertNotNull(results);
		assertEquals(6,results.size());

		calculator.deleteAllResults();
		results = calculator.getAllCalculatedResults();
		assertNotNull(results);
		assertEquals(0,results.size());
	}

	@Test
	public void testTestForCorrectCalculatedResults() {

		calculator.deleteAllPendingRequests();

		double dimension = 1;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);
		dimension = 2;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_VOLUME, dimension);
		dimension = 3;
		calculator.queueCalculationRequest(ShapeName.SQUARE, CalcType.CALC_AREA, dimension);
		dimension = 4;
		calculator.queueCalculationRequest(ShapeName.SQUARE, CalcType.CALC_VOLUME, dimension);
		dimension = 5;
		calculator.queueCalculationRequest(ShapeName.EQUILATERALTRIANGLE, CalcType.CALC_AREA, dimension);
		dimension = 6;
		calculator.queueCalculationRequest(ShapeName.EQUILATERALTRIANGLE, CalcType.CALC_VOLUME, dimension);
		dimension = 1;
		calculator.queueCalculationRequest(ShapeName.SPHERE, CalcType.CALC_AREA, dimension);
		dimension = 2;
		calculator.queueCalculationRequest(ShapeName.SPHERE, CalcType.CALC_VOLUME, dimension);
		dimension = 3;
		calculator.queueCalculationRequest(ShapeName.CUBE, CalcType.CALC_AREA, dimension);
		dimension = 4;
		calculator.queueCalculationRequest(ShapeName.CUBE, CalcType.CALC_VOLUME, dimension);
		dimension = 5;
		calculator.queueCalculationRequest(ShapeName.TETRAHEDRON, CalcType.CALC_AREA, dimension);
		dimension = 6;
		calculator.queueCalculationRequest(ShapeName.TETRAHEDRON, CalcType.CALC_VOLUME, dimension);


		List<CalculationRequest> requests = calculator.getAllPendingRequests();
		assertNotNull(requests);
		assertEquals(12,requests.size());
	
		int numRun = calculator.runAllPendingRequestsNoStopOnError();
		assertEquals(12,numRun);

		requests = calculator.getAllPendingRequests();
		assertNotNull(requests);
		assertEquals(0,requests.size());
		
		List<CalculationResult> results = calculator.getAllCalculatedResults();
		assertNotNull(results);
		assertEquals(12,results.size());

		//for direct access so we can test for correct result
		Map<CalculationRequest,CalculationResult> resultsMap = new HashMap<CalculationRequest, CalculationResult>(results.size());
		for (CalculationResult res : results) {
			resultsMap.put(res.getRequest(),res);
		}

		dimension = 1;
		assertEquals(3.14,resultsMap.get(new CalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension)).getResult(),0.01);
		dimension = 2;
		assertEquals(0,resultsMap.get(new CalculationRequest(ShapeName.CIRCLE, CalcType.CALC_VOLUME, dimension)).getResult(),0.0);
		dimension = 3;
		assertEquals(9,resultsMap.get(new CalculationRequest(ShapeName.SQUARE, CalcType.CALC_AREA, dimension)).getResult(),0.0);
		dimension = 4;
		assertEquals(0,resultsMap.get(new CalculationRequest(ShapeName.SQUARE, CalcType.CALC_VOLUME, dimension)).getResult(),0.0);
		dimension = 5;
		assertEquals(10.83,resultsMap.get(
				new CalculationRequest(ShapeName.EQUILATERALTRIANGLE, CalcType.CALC_AREA, dimension)).getResult(),0.01);
		dimension = 6;
		assertEquals(0,resultsMap.get(
				new CalculationRequest(ShapeName.EQUILATERALTRIANGLE, CalcType.CALC_VOLUME, dimension)).getResult(),0.0);
		dimension = 1;
		assertEquals(12.57,resultsMap.get(new CalculationRequest(ShapeName.SPHERE, CalcType.CALC_AREA, dimension)).getResult(),0.01);
		dimension = 2;
		assertEquals(33.51,resultsMap.get(new CalculationRequest(ShapeName.SPHERE, CalcType.CALC_VOLUME, dimension)).getResult(),0.01);
		dimension = 3;
		assertEquals(54,resultsMap.get(new CalculationRequest(ShapeName.CUBE, CalcType.CALC_AREA, dimension)).getResult(),0.0);
		dimension = 4;
		assertEquals(64,resultsMap.get(new CalculationRequest(ShapeName.CUBE, CalcType.CALC_VOLUME, dimension)).getResult(),0.0);
		dimension = 5;
		assertEquals(43.3,resultsMap.get(new CalculationRequest(ShapeName.TETRAHEDRON, CalcType.CALC_AREA, dimension)).getResult(),0.01);
		dimension = 6;
		assertEquals(25.46,resultsMap.get(new CalculationRequest(ShapeName.TETRAHEDRON, CalcType.CALC_VOLUME, dimension)).getResult(),0.01);


		calculator.deleteAllResults();
		results = calculator.getAllCalculatedResults();
		assertNotNull(results);
		assertEquals(0,results.size());
	}

	
	@Test
	public void testRequestsForExceptionsDuringPossibleRaceConditions() {


		// this class will run the Runnable tasks (see further down)
		// in a coordinated (with main thread) fashion
		final class LatchedThread extends Thread {
			private CountDownLatch _readyLatch;
			private CountDownLatch _startLatch;
			private CountDownLatch _stopLatch;
			LatchedThread(Runnable runnable, List<LatchedThread> threads){
				super(runnable);
				threads.add(this);
			}

			void setReadyLatch(CountDownLatch l) { _readyLatch = l; }

			void setStartLatch(CountDownLatch l) { _startLatch = l; }

			void setStopLatch(CountDownLatch l) { _stopLatch = l; }

			public void start() {
				if (null==_readyLatch) { throw new IllegalArgumentException("_readyLatch not set"); }
				if (null==_startLatch) { throw new IllegalArgumentException("_startLatch not set"); }
				if (null==_stopLatch) { throw new IllegalArgumentException("_stopLatch not set"); }
				super.start();
			}

			public void run() {
				try {
					_readyLatch.countDown(); //this thread signals its readiness 
					_startLatch.await();     //this thread waits to run
					super.run();
					_stopLatch.countDown();  //this thread signals its finished
				} catch (InterruptedException ie) {}
			}
		}

		double counter = 0.0;
		double loopMax = 100;
		double incOnes     = 1; double ones = 1;
		double incTenths     = 0.1; double tens = 10;

		double incDecimalFactor = incOnes; double placeValue = ones;
		//double incDecimalFactor = incTenths; double placeValue = tens;
		
		double expectedNumResults1 = loopMax * placeValue;
		
		Runnable addRequestsTask = () -> {
			for (double dimension=counter; dimension<loopMax; dimension+=incDecimalFactor) {
				logger.debug("\n\n"+dimension+"\n\n");
				calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);
			}
		};

		Runnable addRequestsTask2 = () -> {
			for (double dimension=counter; dimension<loopMax; dimension+=incDecimalFactor) {
				logger.debug("\n\n"+dimension+"\n\n");
				calculator.queueCalculationRequest(ShapeName.SQUARE, CalcType.CALC_VOLUME, dimension);
			}
		};

		Runnable addRequestsTask3 = () -> {
			for (double dimension=counter; dimension<loopMax; dimension+=incDecimalFactor) {
				logger.debug("\n\n"+dimension+"\n\n");
				calculator.queueCalculationRequest(ShapeName.SPHERE, CalcType.CALC_AREA, dimension);
			}
		};

		Runnable runAllRequestsNoStopTask = () -> {
			for (double dimension=counter; dimension<loopMax; dimension+=incDecimalFactor) {
				logger.debug("\n\nrun\n\n");
				calculator.runAllPendingRequestsNoStopOnError();
			}
		};

		final List<LatchedThread> threads = new ArrayList<LatchedThread>();
		new LatchedThread(addRequestsTask,threads);
		new LatchedThread(addRequestsTask2,threads);
		new LatchedThread(addRequestsTask3,threads);
		new LatchedThread(runAllRequestsNoStopTask,threads);

		CountDownLatch readyLatch = new CountDownLatch(threads.size());
		CountDownLatch startLatch = new CountDownLatch(threads.size());
		CountDownLatch stopLatch = new CountDownLatch(threads.size());

		//do the initial start...
		for (LatchedThread t : threads) {
			t.setReadyLatch(readyLatch);
			t.setStartLatch(startLatch);
			t.setStopLatch(stopLatch);
			t.start();
		}

		logger.debug("\n\nMain thread has started child threads..waiting...\n\n");

		//wait until all threads are in position to start
		// each thread will count down the ready latch, and main thread will
		// move beyond this point
		try { readyLatch.await(); } catch (InterruptedException ie) {}
		
		logger.debug("\n\nAll child threads read to run - Main thread will set them off.....\n\n");


		//now the main thread will count down the start latch, so that the
		//task threads can all leave the starting gate
		for (Thread t : threads) {
			startLatch.countDown();
		}
	
		logger.debug("\n\nMain thread waiting for child threads to finish.....\n\n");

		//now the main thread must wait (to exit)
		//until all the task threads are done
		try { stopLatch.await(); } catch (InterruptedException ie) {}
		
		logger.debug("\n\nMain thread testing some results.....\n\n");

		List<CalculationResult> results = calculator.getAllCalculatedResults();
		assertNotNull(results);
		assertEquals((expectedNumResults1*3) ,results.size(),0.0);

	
	}

}