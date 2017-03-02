package com.kafka.util;

/**
 * Assertion utility class that assists in validating arguments
 * 
 * @author Harish Thatikonda
 * @since 2017-march-01
 *
 */
public class Check {

	// No need to instantiate. All are static methods
	private Check() {

	}

	/**
	 * throws the {@link T} extends {@link Exception} error if the given
	 * expression fails
	 * 
	 * @param expression
	 * @param error
	 * @param clazz
	 * @throws Exception
	 */
	private static <T extends Exception> void isTrue(boolean expression, String error, Class<T> clazz)
			throws Exception {
		if (!expression) {
			throw clazz.getConstructor(String.class).newInstance(error);
		}
	}

	/**
	 * throws the {@link IllegalArgumentException} error if validation fails
	 * 
	 * @param expression
	 * @param error
	 * @throws Exception
	 */
	public static void isTrue(boolean expression, String error) throws Exception {
		isTrue(expression, error, IllegalArgumentException.class);
	}

	/**
	 * throws the {@link IllegalArgumentException} error if validation fails
	 * 
	 * @param expression
	 * @param error
	 * @throws Exception
	 */
	public static void notNull(Object object, String error) throws Exception {
		isTrue(object != null, error, IllegalArgumentException.class);
	}

	/**
	 * throws the {@link T} extends {@link Exception} error if validation fails
	 * 
	 * @param expression
	 * @param error
	 * @throws Exception
	 */
	public static <T extends Exception> void notNull(Object object, String error, Class<T> clazz) throws Exception {
		isTrue(object != null, error, clazz);
	}

	/**
	 * throws the {@link IllegalArgumentException} error if validation fails
	 * 
	 * @param expression
	 * @param error
	 * @throws Exception
	 */
	public static void isNull(Object object, String error) throws Exception {
		isTrue(object == null, error, IllegalArgumentException.class);
	}

	/**
	 * throws the {@link T} extends {@link Exception} error if validation fails
	 * 
	 * @param expression
	 * @param error
	 * @throws Exception
	 */
	public static <T extends Exception> void isNull(Object object, String error, Class<T> clazz) throws Exception {
		isTrue(object == null, error, clazz);
	}
}
