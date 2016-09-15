package com.eli.calc.shape.tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

//import org.junit.runner.RunWith;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.eli.calc.shape.config.ShapeCalcBaseContext;
import com.eli.calc.shape.domain.CalculationRequest;
import com.eli.calc.shape.domain.CalculationResult;
import com.eli.calc.shape.model.CalcType;
import com.eli.calc.shape.model.ShapeName;
import com.eli.calc.shape.service.CalculatedResults;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={ShapeCalcBaseContext.class})
public class JUnitTestCalculatedResults  implements ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(JUnitTestCalculatedResults.class);

	private ApplicationContext ctx;

	@Autowired
	private CalculatedResults calculatedResults;
	
	@Override
	public void setApplicationContext(ApplicationContext ctx) {
		
		this.ctx = ctx;
	}
	
	
	@Before // each test
	public void setUp() throws Exception {

		calculatedResults.deleteAllResults();
	}

	@After // each test
	public void tearDown() throws Exception {

	}

	@Test
	public void testGetCalculatedResults() {
		List<CalculationResult> results = calculatedResults.getResults();
		assertEquals(0 ,results.size());
	}

	@Test
	public void testRemoveResult() {
		calculatedResults.removeResult( new CalculationResult(new CalculationRequest( ShapeName.CIRCLE, CalcType.CALC_AREA, 0.0),0.0));
	}

	@Test
	public void testputResult() {
		calculatedResults.putResult( new CalculationResult(new CalculationRequest( ShapeName.CIRCLE, CalcType.CALC_AREA, 0.0),0.0));
		long numResults = calculatedResults.getResults().size();
		assertEquals(1 ,numResults);
	}

	
	@Test
	public void testResultsForExceptionsDuringPossibleRaceConditions() {

		long numResults = calculatedResults.getResults().size();
		assertEquals(0 ,numResults);

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

		int loopMax = 300;

		Runnable deleteAllResultsTask = () -> {
			logger.debug("\n\ndelete\n\n");
			for (int i=0; i<loopMax; i++) {
				calculatedResults.deleteAllResults();
			}
		};
		
		Runnable addResultsTask = () -> {
			for (int dimension=0; dimension<loopMax; dimension++) {
				calculatedResults.putResult(
						new CalculationResult(
								new CalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, (double)dimension),
								(double)dimension)
						);
				logger.debug("\n\n"+dimension+"\n\n");
			}
		};

		Runnable addResultsTask2 = () -> {
			for (int dimension=0; dimension<loopMax; dimension++) {
				calculatedResults.putResult(
						new CalculationResult(
								new CalculationRequest(ShapeName.SQUARE, CalcType.CALC_VOLUME, (double)dimension),
								(double)dimension)
						);
				logger.debug("\n\n"+dimension+"\n\n");
			}
		};

		Runnable addResultsTask3 = () -> {
			for (int dimension=0; dimension<loopMax; dimension++) {
				calculatedResults.putResult(
						new CalculationResult(
								new CalculationRequest(ShapeName.SPHERE, CalcType.CALC_AREA, (double)dimension),
								(double)dimension)
						);
				logger.debug("\n\n"+dimension+"\n\n");
			}
		};


		final List<LatchedThread> threads = new ArrayList<LatchedThread>();
		new LatchedThread(addResultsTask,threads);
		new LatchedThread(addResultsTask2,threads);
		new LatchedThread(addResultsTask3,threads);

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

		long numResultsAfter = calculatedResults.getResults().size();
		assertEquals(loopMax*3 ,numResultsAfter);

	
	}

}
