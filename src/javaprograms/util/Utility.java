package javaprograms.util;

import java.util.Random;
/**
 * Class to hold the functions that are used across the project. 
 * @author neenadamodaran
 *
 */
public class Utility {
	/**
	 * Utility function to create Array of random numbers of the given size.
	 * 
	 * @param arraySize
	 * @return
	 */
	public static int[] createArray(int arraySize) {
		if (arraySize < 0)
			return new int[0];

		int[] array = new int[arraySize];
		for (int i = 0; i < arraySize; i++) {
			array[i] = getRandomNumberInRange(0, arraySize);
		}
		return array;
	}

	/**
	 * utility function to print the elements of the given integer array.
	 * 
	 * @param array
	 */
	public static void printArray(int [] array) {
		if (array == null)
			System.out.println("Invalid Array");

		// System.out.println(" Array length " + array.length);
		for (int i = 0; i < array.length; i++) {
			System.out.println(array[i]);
		}

	}

	/**
	 * This function produces the random number that start with the min value
	 * and ends with the maximum value.
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		// to get random number starting with min and ending with max.
		return r.nextInt((max - min) + 1) + min;
	}

}
