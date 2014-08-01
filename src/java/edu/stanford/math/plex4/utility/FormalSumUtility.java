package edu.stanford.math.plex4.utility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.primitivelib.autogen.formal_sum.BooleanSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectSparseFormalSum;
import gnu.trove.TObjectIntIterator;

/**
 * This class contains various utility functions for interacting with formal
 * sums (chains).
 * 
 * @author Andrew Tausz
 * 
 */
public class FormalSumUtility {

	/**
	 * This function returns all of the basis elements in a chain which have
	 * non-zero coefficients. The primary use case will be to extract the set of
	 * simplicies in a chain.
	 * 
	 * @param chain
	 * @return the set of active basis elements in a formal sum
	 */
	public static <F, U> List<U> extractActiveBasisElements(ObjectSparseFormalSum<F, U> chain) {

		List<U> basisElements = new ArrayList<U>();

		for (Iterator<Map.Entry<U, F>> iterator = chain.iterator(); iterator.hasNext();) {
			Map.Entry<U, F> entry = iterator.next();

			basisElements.add(entry.getKey());
		}

		return basisElements;
	}

	/**
	 * This function returns all of the basis elements in a chain which have
	 * non-zero coefficients. The primary use case will be to extract the set of
	 * simplicies in a chain.
	 * 
	 * @param chain
	 * @return the set of active basis elements in a formal sum
	 */
	public static <U> List<U> extractActiveBasisElements(IntSparseFormalSum<U> chain) {

		List<U> basisElements = new ArrayList<U>();

		TObjectIntIterator<U> iter = chain.iterator();

		while (iter.hasNext()) {
			iter.advance();
			U obj = iter.key();
			basisElements.add(obj);
		}

		return basisElements;
	}

	/**
	 * This function returns all of the basis elements in a chain which have
	 * non-zero coefficients. The primary use case will be to extract the set of
	 * simplicies in a chain.
	 * 
	 * @param chain
	 * @return the set of active basis elements in a formal sum
	 */
	public static <U> List<U> extractActiveBasisElements(BooleanSparseFormalSum<U> chain) {

		List<U> basisElements = new ArrayList<U>();
		Iterator<U> iter = chain.iterator();

		while (iter.hasNext()) {
			U obj = iter.next();
			basisElements.add(obj);
		}

		return basisElements;
	}

	/**
	 * This function returns the coefficients for a formal sum in the form of a
	 * list.
	 * 
	 * @param chain
	 * @return the coefficients in the chain
	 */
	public static List<Integer> extractCoefficients(IntSparseFormalSum<?> chain) {
		List<Integer> coefficients = new ArrayList<Integer>();

		TObjectIntIterator<?> iter = chain.iterator();

		while (iter.hasNext()) {
			iter.advance();
			coefficients.add(iter.value());
		}

		return coefficients;
	}

	/**
	 * This function returns the coefficients for a formal sum in the form of a
	 * list.
	 * 
	 * @param chain
	 * @return the coefficients in the chain
	 */
	public static <F, U> List<F> extractCoefficients(ObjectSparseFormalSum<F, U> chain) {
		List<F> coefficients = new ArrayList<F>();

		for (Iterator<Map.Entry<U, F>> iterator = chain.iterator(); iterator.hasNext();) {
			Map.Entry<U, F> entry = iterator.next();

			coefficients.add(entry.getValue());
		}

		return coefficients;
	}

	/**
	 * This function extracts the set of vertices from a list of simplices and returns a 2-d
	 * array containing the vertices.
	 * 
	 * @param simplices
	 * @return the vertices for a list of simplices
	 */
	public static int[][] extractVertices(List<Simplex> simplices) {
		if (simplices.size() < 1) {
			return new int[0][0];
		}

		int[][] vertices = new int[simplices.size()][];

		int i = 0;
		for (Simplex s : simplices) {
			vertices[i++] = s.getVertices();
		}

		return vertices;
	}
}
