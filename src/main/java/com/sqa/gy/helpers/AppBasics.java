/**
 *   File Name: AppBasics.java<br>
 *
 *   Yakovenko, Galina<br>
 *   Created: Feb 13, 2017
 *
 */

package com.sqa.gy.helpers;

import java.util.*;

import com.sqa.gy.helpers.exceptions.*;

/**
 * AppBasics Helper methods for: -requesting input from user -working with
 * integer arrays
 *
 * @author Yakovenko, Galina
 *
 */
public class AppBasics {

	static Scanner scanner = new Scanner(System.in);

	/**
	 * Takes int array and returns the average
	 *
	 * @param anArray
	 *            - integer array
	 * @return average of elements of integer array as a double
	 */
	public static double calcsAverageOfIntsInArray(int[] anArray) {
		int total = calcsTotalOfIntsInArray(anArray);
		double average = (double) total / anArray.length;
		return average;
	}

	/**
	 * Takes int array and returns the total
	 *
	 * @param anArray
	 *            - integer array
	 * @return total of elements of integer array as an int
	 */
	public static int calcsTotalOfIntsInArray(int[] anArray) {
		int total = 0;
		for (int i = 0; i < anArray.length; i++) {
			total += anArray[i];
		}
		return total;
	}

	/**
	 * Overloads convertStringtoInt method for when no integer range is provided
	 *
	 * @param input
	 *            - string expected to be able to parse int from this string
	 * @return integer parsed from String input
	 */
	public static int convertStringtoInt(String input) {
		return convertStringtoInt(input, 0, 0);
	}

	/**
	 * Returns integer parsed from string and handles exceptions when the
	 * integer is not in accepted range or string cannot be parsed
	 *
	 * @param input
	 *            - String expected to be able to parse int from this string
	 * @return integer parsed from String input
	 */
	public static int convertStringtoInt(String input, int min, int max) {
		int result = 0;
		boolean isNotValid = true;
		while (isNotValid) {
			try {
				result = Integer.parseInt(input);
				if (min != 0 && max != 0) {
					if (result < max && result > min) {
						isNotValid = false;
					} else {
						throw new NumberNotInRangeException();
					}
				}
			} catch (NumberFormatException e) {
				System.out.println("You have not entered a correct number value.");
			} catch (NumberNotInRangeException e) {
				System.out.println("You have not entered a number within the range of " + min + " and " + max + ".");
			}
			isNotValid = false;
		}
		return result;
	}

	/**
	 * Prints the elements of an int array divided by commas
	 *
	 * @param anArray
	 *            - integer array
	 */
	public static void printIntArray(int[] anArray) {
		System.out.print(anArray[0]);
		for (int i = 1; i < anArray.length; i++) {
			System.out.print(Integer.toString(anArray[i]));
			System.out.print(", ");
		}
	}

	/**
	 * Requests user to input value that can be parse to a double and converts
	 * to double Overloads requestDoubleFromUse method for when no range is
	 * provided
	 *
	 * @param question
	 *            - prompt for user to input a double
	 * @return double parsed from String input
	 */
	public static double requestDoubleFromUser(String question) {
		return requestDoubleFromUser(question, 0, 0);
	}

	/**
	 * Returns double parsed from user-provided string and handles exceptions
	 * when the double is not in accepted range or string cannot be parsed
	 *
	 * @param question
	 *            - prompt for user to input a double
	 * @param min
	 *            - inclusive minimum for double range
	 * @param max
	 *            - inclusive max for double range
	 * @return double parsed from String input
	 */
	public static double requestDoubleFromUser(String question, double min, int max) {
		String input;
		double result = 0;
		boolean isNotValid = true;
		while (isNotValid) {
			try {
				System.out.print(question + " ");
				input = scanner.nextLine();
				result = Double.parseDouble(input);
				if (min != 0 && max != 0) {
					if (result < max && result > min) {
						isNotValid = false;
					} else {
						throw new NumberNotInRangeException();
					}
				}
			} catch (NumberFormatException e) {
				System.out.println("You have not entered a correct number value.");
			} catch (NumberNotInRangeException e) {
				System.out.println("You have not entered a number within the range of " + min + " and " + max + ".");
			}
			isNotValid = false;
		}
		return result;
	}

	/**
	 * Requests user to input value that can be parse to an int and converts to
	 * int Overloads requestIntFromUse method for when no range is provided
	 *
	 * @param question
	 *            - prompt for user to input an int
	 * @return int parsed from String input
	 */
	public static int requestIntFromUser(String question) {
		return requestIntFromUser(question, 0, 0);
	}

	/**
	 * Returns int parsed from user-provided string and handles exceptions when
	 * the integer is not in accepted range or string cannot be parsed
	 *
	 * @param question
	 *            - prompt for user to input a int
	 * @param min
	 *            - inclusive minimum for int range
	 * @param max
	 *            - inclusive max for double range
	 * @return int parsed from String input
	 */
	public static int requestIntFromUser(String question, int min, int max) {
		String input;
		int result = 0;
		boolean isNotValid = true;
		while (isNotValid) {
			try {
				System.out.print(question + " ");
				input = scanner.nextLine();
				result = Integer.parseInt(input);
				if (min != 0 && max != 0) {
					if (result < max && result > min) {
						isNotValid = false;
					} else {
						throw new NumberNotInRangeException();
					}
				}
			} catch (NumberFormatException e) {
				System.out.println("You have not entered a correct number value.");
			} catch (NumberNotInRangeException e) {
				System.out.println("You have not entered a number within the range of " + min + " and " + max + ".");
			}
			isNotValid = false;
		}
		return result;
	}

	/**
	 * Print a prompt to user and returns the user's input as a String
	 *
	 * @param prompt
	 *            - prompt to be displayed to user
	 * @return user's input as String
	 */
	public static String requestUserInfo(String prompt) {
		System.out.print(prompt + " ");
		String response = scanner.nextLine();
		return response;
	}

	/**
	 * Print a prompt to user and returns the user's input as a boolean User is
	 * expected to enter "y" for yes. All other responses return false
	 *
	 * @param prompt
	 *            - prompt to be displayed to user
	 * @return user's input as boolean
	 */
	public static boolean requestYesorNo(String prompt) {
		String strYes = "y";
		String response = AppBasics.requestUserInfo(prompt + " (y/n): ");
		return response.equalsIgnoreCase(strYes);
	}

	/**
	 * Takes int array and returns a string with all elements separated by
	 * commas. TODO: Rewrite with StringBuilder
	 *
	 * @param anArray
	 *            - integer array
	 * @return String of array elements separated by commas
	 */
	public static String returnIntArrayAsString(int[] anArray) {
		String arrayInAString = Integer.toString(anArray[0]);
		for (int i = 1; i < anArray.length; i++) {
			{
				arrayInAString = arrayInAString + ", ";
				arrayInAString = arrayInAString + Integer.toString(anArray[i]);
			}
		}
		return arrayInAString;
	}
}
