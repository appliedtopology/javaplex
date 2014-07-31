package edu.stanford.math.plex4.example_tests;

import java.util.List;

import edu.stanford.math.plex4.utility.FormalSumUtility;
import edu.stanford.math.primitivelib.autogen.formal_sum.BooleanPrimitiveFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.BooleanSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectSparseFormalSum;

public class FormalSumTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		testIntFormalSum();
		testBooleanFormalSum();
		testObjectFormalSum();
	}

	public static void testIntFormalSum() {
		IntSparseFormalSum<String> sum = new IntSparseFormalSum<String>();

		sum.put(1, "a");
		sum.put(3, "b");
		sum.put(-4, "z");

		System.out.println("IntSparseFormalSum: " + sum);

		List<String> basisElements = FormalSumUtility.extractActiveBasisElements(sum);

		System.out.println("basis elements: " + basisElements);

		List<Integer> coefficients = FormalSumUtility.extractCoefficients(sum);

		System.out.println("coefficients: " + coefficients);
	}

	public static void testBooleanFormalSum() {
		BooleanPrimitiveFreeModule<String> chainModule = new BooleanPrimitiveFreeModule<String>();

		BooleanSparseFormalSum<String> sum = chainModule.createNewSum();

		sum.put(true, "a");
		sum.put(true, "b");
		sum.put(true, "z");

		System.out.println("BooleanPrimitiveFreeModule: " + sum);

		List<String> basisElements = FormalSumUtility.extractActiveBasisElements(sum);

		System.out.println("basis elements: " + basisElements);
	}

	public static void testObjectFormalSum() {
		ObjectSparseFormalSum<Integer, String> sum = new ObjectSparseFormalSum<Integer, String>();

		sum.put(3, "a");
		sum.put(9, "b");
		sum.put(4, "z");

		System.out.println("ObjectSparseFormalSum: " + sum);

		List<String> basisElements = FormalSumUtility.extractActiveBasisElements(sum);

		System.out.println("basis elements: " + basisElements);

		List<Integer> coefficients = FormalSumUtility.extractCoefficients(sum);

		System.out.println("coefficients: " + coefficients);
	}

}
