package edu.stanford.math.plex4.example_tests;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.utility.FormalSumUtility;
import edu.stanford.math.primitivelib.autogen.formal_sum.BooleanPrimitiveFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.BooleanSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectSparseFormalSum;

public class FormalSumTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		IntSparseFormalSum<String> sum = new IntSparseFormalSum<String>();

		sum.put(1, "a");
		sum.put(3, "b");
		sum.put(-4, "z");

		System.out.println("IntSparseFormalSum: " + sum);

		List<?> basisElements = FormalSumUtility.extractActiveBasisElements(sum);

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

		List<?> basisElements = FormalSumUtility.extractActiveBasisElements(sum);

		System.out.println("basis elements: " + basisElements);
	}

	public static void testObjectFormalSum() {
		ObjectSparseFormalSum<Integer, String> sum = new ObjectSparseFormalSum<Integer, String>();

		sum.put(3, "a");
		sum.put(9, "b");
		sum.put(4, "z");

		System.out.println("ObjectSparseFormalSum: " + sum);

		List<?> basisElements = FormalSumUtility.extractActiveBasisElements(sum);

		System.out.println("basis elements: " + basisElements);

		List<?> coefficients = FormalSumUtility.extractCoefficients(sum);

		System.out.println("coefficients: " + coefficients);
	}

	public static void testSimplices() {
		IntSparseFormalSum<Simplex> sum = new IntSparseFormalSum<Simplex>();

		sum.put(2, Simplex.makeSimplex(new int[] { 0, 1, 3 }));
		sum.put(-5, Simplex.makeSimplex(new int[] { 5, 6 }));

		System.out.println("IntSparseFormalSum: " + sum);

		List<Simplex> basisElements = FormalSumUtility.extractActiveBasisElements(sum);

		System.out.println("basis elements: " + basisElements);

		int[][] vertices = FormalSumUtility.extractVertices(basisElements);

		System.out.println("vertices:");

		for (int[] v : vertices) {
			System.out.println(Arrays.toString(v));
		}

	}
}
