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
import com.eli.calc.shape.model.CalcType;
import com.eli.calc.shape.model.ShapeName;
import com.eli.calc.shape.service.PendingRequests;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={ShapeCalcBaseContext.class})
public class JUnitTestPendingRequests  implements ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(JUnitTestPendingRequests.class);

	private ApplicationContext ctx;

	@Autowired
	private PendingRequests pendingRequests;
	
	@Override
	public void setApplicationContext(ApplicationContext ctx) {
		
		this.ctx = ctx;
	}
	
	
	@Before // each test
	public void setUp() throws Exception {

		pendingRequests.deleteAllRequests();
	}

	@After // each test
	public void tearDown() throws Exception {

	}

	@Test
	public void testGetPendingRequests() {
		List<CalculationRequest> requests = pendingRequests.getRequests();
		assertEquals(0 ,requests.size());
	}

	@Test
	public void testGetNumRequests() {
		long numRequests = pendingRequests.getNumRequests();
		assertEquals(0 ,numRequests);
	}

	@Test
	public void testRemoveRequest() {
		pendingRequests.removeRequest(
				new CalculationRequest(
						ShapeName.CIRCLE,
						CalcType.CALC_AREA,
						0.0)
				);
	}

	@Test
	public void testputRequest() {
		pendingRequests.putRequest(
				new CalculationRequest(
						ShapeName.CIRCLE,
						CalcType.CALC_AREA,
						0.0)
				);
		long numRequests = pendingRequests.getNumRequests();
		assertEquals(1 ,numRequests);
	}

	
	@Test
	public void testRequestsForExceptionsDuringPossibleRaceConditions() {

		long numRequests = pendingRequests.getNumRequests();
		assertEquals(0 ,numRequests);

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

		Runnable deleteAllRequestsTask = () -> {
			logger.debug("\n\ndelete\n\n");
			for (int i=0; i<loopMax; i++) {
				pendingRequests.deleteAllRequests();
			}
		};
		
		Runnable addRequestsTask = () -> {
			for (int dimension=0; dimension<loopMax; dimension++) {
				pendingRequests.putRequest(new CalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, (double)dimension));
				logger.debug("\n\n"+dimension+"\n\n");
			}
		};

		Runnable addRequestsTask2 = () -> {
			for (int dimension=0; dimension<loopMax; dimension++) {
				pendingRequests.putRequest(new CalculationRequest(ShapeName.SQUARE, CalcType.CALC_VOLUME, (double)dimension));
				logger.debug("\n\n"+dimension+"\n\n");
			}
		};

		Runnable addRequestsTask3 = () -> {
			for (int dimension=0; dimension<loopMax; dimension++) {
				pendingRequests.putRequest(new CalculationRequest(ShapeName.SPHERE, CalcType.CALC_AREA, (double)dimension));
				logger.debug("\n\n"+dimension+"\n\n");
			}
		};


		final List<LatchedThread> threads = new ArrayList<LatchedThread>();
		//new LatchedThread(deleteAllRequestsTask,threads);
		new LatchedThread(addRequestsTask,threads);
		new LatchedThread(addRequestsTask2,threads);
		new LatchedThread(addRequestsTask3,threads);

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

		long numRequestsAfter = pendingRequests.getNumRequests();
		assertEquals(loopMax*3 ,numRequestsAfter);

	
	}

}
