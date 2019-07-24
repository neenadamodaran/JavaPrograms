package javaprograms.mutithreading;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

import javaprograms.util.Utility;

/**
 * This class is designed to show how to use Fork/Join framework to perform
 *  MergeSort of an array with high number of elements.
 * 
 * @author neenadamodaran
 *
 */
public class MergeSortUsingForkJoin {
	private static final int ARRAY_SIZE = 10000;

	public static void main(String[] args) throws InterruptedException {

		// ForkJoinPool is an executor Service for running ForkJoinTasks.
		ForkJoinPool pool = ForkJoinPool.commonPool();

		long startTime;
		long endTime;

		int[] array = Utility.createArray(ARRAY_SIZE);

		MergeSort mergeSort = new MergeSort(array, 0, array.length - 1);
		startTime = System.nanoTime();

		// Start execution and wait for result/return
		pool.invoke(mergeSort);

		endTime = System.nanoTime();
		System.out.println("Time taken in seconds: using Fork Join " + (endTime - startTime) / 1E9);

		//
		
		Utility.printArray(array);
	}

}

/**
 * This class is designed to show how to use Fork/Join framework to perform
 *  MergeSort of an array with high number of elements.
 * 
 * Merge sort is a divide and conquer algorithm. Here we recursively split the
 * array till the array size is less than or equal to a Threshold and then we sort the array and 
 * perform the merging. And during merging as well we sort the elements of both the sorted arrays.
 * 
 * MergeSort extends Recursive action because the compute function doesnot
 * return anything. Incase the compute function is required to returns a value
 * then we extend RecursiveTask.
 * 
 * @author neenadamodaran
 *
 */
class MergeSort extends RecursiveAction {
	private int[] array;
	private int left;
	private int right;
	private static final int THRESHOlD = 10; 
	
	public MergeSort(int[] array, int left, int right) {
		super();
		this.array = array;
		this.left = left;
		this.right = right;
	}

	@Override
	protected void compute() {
		/*
		 * Here we have performed an optimization ie. if the length(ie. high
		 * - low) of the array that is to be sorted is within a given threshold
		 * then sort the elements of the array using a sequential sort
		 *  or else use the Fork/Join MergeSort.
		 * 
		 * .
		 */
		if(right -left <= THRESHOlD){
			Arrays.sort(array, left, right + 1);
		}
		else {
			int mid = (left + right) / 2;
			// System.out.println("mid value " + mid);
			RecursiveAction leftSort = new MergeSort(array, left, mid);
			RecursiveAction rightSort = new MergeSort(array, mid + 1, right);
			invokeAll(leftSort, rightSort);
			merge(left, mid, right);

		}

	}

	/**
	 * Sorts and Merges the two splitted arrays.
	 * @param left
	 * @param mid
	 * @param right
	 */
	private void merge(int left, int mid, int right) {
		

		int leftArrayELementCount = mid - left + 1;
		int rightArrayElementCount = right - mid;

		int[] leftArray = new int[leftArrayELementCount];
		int[] rightArray = new int[rightArrayElementCount];

		for (int i = 0; i < leftArray.length; i++) {
			leftArray[i] = array[left + i];
		}

		for (int i = 0; i < rightArray.length; i++) {
			rightArray[i] = array[mid + i + 1];
		}

		int leftArrayCounter = 0;
		int rightArrayCounter = 0;
		int origArrayCounter = left;

		while (leftArrayCounter < leftArrayELementCount && rightArrayCounter < rightArrayElementCount) {
			if (leftArray[leftArrayCounter] <= rightArray[rightArrayCounter]) {
				array[origArrayCounter] = leftArray[leftArrayCounter];
				leftArrayCounter++;

			} else {
				array[origArrayCounter] = rightArray[rightArrayCounter];
				rightArrayCounter++;
			}

			origArrayCounter++;
		}

		while (leftArrayCounter < leftArrayELementCount) {
			array[origArrayCounter] = leftArray[leftArrayCounter];
			leftArrayCounter++;
			origArrayCounter++;
		}

		while (rightArrayCounter < rightArrayElementCount) {
			array[origArrayCounter] = rightArray[rightArrayCounter];
			rightArrayCounter++;
			origArrayCounter++;
		}

	}

}
