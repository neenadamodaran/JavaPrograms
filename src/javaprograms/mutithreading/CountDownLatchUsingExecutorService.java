package javaprograms.mutithreading;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @author neenadamodaran
 * 
 * Class is used to demonstrate the how Coundown latch could be used to wait for all the task to complete.
 * Here the task is executed by the threads pooled by an Executor Service.
 *
 */
public class CountDownLatchUsingExecutorService {
	
	public static void main(String[] args) throws InterruptedException {
		int noOfWorkerThreads = 5;
		CountDownLatch doneSignal = new CountDownLatch(noOfWorkerThreads);
		ExecutorService threadExecutor = Executors.newFixedThreadPool(noOfWorkerThreads);

		for (int i = 0; i < noOfWorkerThreads; i++) {
			threadExecutor.execute(new WorkerTask(doneSignal));
		}
		
		//Here waiting for all the tasks to be completed. 
		doneSignal.await();
		System.out.println("Done waiting");
		threadExecutor.shutdown();
	}
}

class WorkerTask implements Runnable {

	CountDownLatch doneSignal;

	public WorkerTask(CountDownLatch doneSignal) {
		super();

		this.doneSignal = doneSignal;
	}

	@Override
	public void run() {
		System.out.println("Task Executed");
		System.out.println("done Executed " + doneSignal.getCount());
		//Signalling completion of task by thread.
		doneSignal.countDown();
	}

}
