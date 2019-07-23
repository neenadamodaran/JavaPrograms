package javaprograms.mutithreading;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * 
 * @author neenadamodaran
 *
 */
public class MergeSortUsingForkJoin {
	private static final int ARRAY_SIZE = 10000000;

	public static void main(String[] args) {
		ForkJoinPool pool = new ForkJoinPool();
		long startTime;
		long endTime;
		int[] array = createArray(ARRAY_SIZE);
		//printArray(array);
		MergeSort mergeSort = new MergeSort(array, 0, array.length - 1);
		startTime = System.currentTimeMillis();
		// Start execution and wait for result/return
		pool.invoke(mergeSort);
		endTime = System.currentTimeMillis();
		System.out.println("Time taken: using Fork Join " + (endTime - startTime) + " millis");
		printArray(array); 
	}

	private static int[] createArray(int arraySize) {
		if (arraySize < 0)
			return new int[0];

		int[] array = new int[arraySize];
		for (int i = 0; i < arraySize; i++) {
			array[i] = getRandomNumberInRange(0, arraySize);
		}
		return array;
	}
	
	private static void printArray(int []array) {
		if (array == null)
			System.out.println("Invalid Array");
		
		//System.out.println(" Array length " + array.length);
		for (int i = 0; i < array.length; i++) {
			System.out.println(array[i]);
		}
		
	}


	private static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

}

/**
 * Merge sort is a divide and conquer algorithm.
 * Here we recursively spilt the array till the array size is 2 and then we start merging. 
 * And during merging we sort the element. 
 * @author neenadamodaran
 *
 */
class MergeSort extends RecursiveAction {
	private int[] array;
	private int left;
	private int right;

	public MergeSort(int[] array, int left, int right) {
		super();
		this.array = array;
		this.left = left;
		this.right = right;
	}

	@Override
	protected void compute() {
		
		
		if (left < right) {
			int mid = (left + right) / 2;
			//System.out.println("mid value " + mid); 
			RecursiveAction leftSort = new MergeSort(array, left, mid);
			RecursiveAction rightSort = new MergeSort(array, mid + 1, right);
			invokeAll(leftSort, rightSort);
			merge(left, mid, right);

		}

	}

	private void merge(int left, int mid, int right) {
		//System.out.println( "left " + left +" mid value " + mid + " right " + right); 
		// TODO Auto-generated method stub

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
