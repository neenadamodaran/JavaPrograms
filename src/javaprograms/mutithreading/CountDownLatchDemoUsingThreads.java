package javaprograms.mutithreading;

import java.util.concurrent.CountDownLatch;

import javaprograms.util.Utility;

/**
 * This class is written to demonstrate how by using CountDownLatch, A program could make the threads to wait for 
 * every other thread to be ready before they start executing their  respective tasks and how a program could 
 * wait for all the threads to finish before the program could do something else.
 *  
 * @author neenadamodaran
 *
 */

public class CountDownLatchDemoUsingThreads {
	
	public static void main(String []args) throws InterruptedException {
		int noOFWorkerThreads = 5; 
		
		CountDownLatch startWorkersSignal = new CountDownLatch(1);
		CountDownLatch workerDoneSignalCounter = new CountDownLatch(noOFWorkerThreads);
		
		for(int i = 0 ; i < noOFWorkerThreads; i++) {
			new Thread(new Worker(startWorkersSignal, workerDoneSignalCounter, "THREAD-" + i)).start();
		}
		
		long startTime = System.nanoTime(); 
		
		System.out.println("start Time in Seconds " + startTime); 
		//To signal all the threads to start
		startWorkersSignal.countDown();
		//waoiting for all the thread to complete
		workerDoneSignalCounter.await();
		long endTime = System.nanoTime();
		System.out.println("Time in Secongds " + Utility.getTimeInSeconds(startTime, endTime)); 
		
	}
}


class Worker implements Runnable{
	
	private CountDownLatch startCounter; 
	private CountDownLatch workDoneCounter;
	private String threadName; 
	
	

	public Worker(CountDownLatch startCounter, CountDownLatch workDownCounter, String threadName) {
		super();
		this.startCounter = startCounter;
		this.workDoneCounter = workDownCounter;
		this.threadName = threadName;
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println(threadName + " waiting for other threads");
		try {
			startCounter.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		System.out.println(threadName + " Started working ");
		//Signal completion of tast by thread
		workDoneCounter.countDown(); 
	}
}