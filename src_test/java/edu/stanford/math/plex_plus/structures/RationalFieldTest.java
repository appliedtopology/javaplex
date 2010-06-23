/**
 * 
 */
package edu.stanford.math.plex_plus.structures;

import edu.stanford.math.plex_plus.algebraic_structures.impl.RationalField;

/**
 * @author Andrew Tausz
 *
 */
public class RationalFieldTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RationalField Q = RationalField.getInstance();
		System.out.println(Q.divide(24, 6).toString());
		System.out.println(Q.power(Q.valueOf(4), -4));
	}
}
