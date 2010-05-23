package edu.stanford.math.plex_plus.structures;

import org.apache.commons.math.fraction.Fraction;

import edu.stanford.math.plex_plus.datastructures.GenericFormalSum;
import edu.stanford.math.plex_plus.datastructures.IntFormalSum;
import edu.stanford.math.plex_plus.homology.simplex.Simplex;
import edu.stanford.math.plex_plus.math.structures.impl.GenericFreeModule;
import edu.stanford.math.plex_plus.math.structures.impl.IntFreeModule;
import edu.stanford.math.plex_plus.math.structures.impl.ModularIntField;
import edu.stanford.math.plex_plus.math.structures.impl.RationalField;

/**
 * 
 * Basic test of free module arithmetic.
 * 
 * @author Andrew Tausz
 *
 */
public class GenericFreeModuleTest {

	public static void main(String[] args) {
		
		// Formal sums of String objects with Fraction coefficients
		GenericFreeModule<Fraction, String> M = new GenericFreeModule<Fraction, String>(RationalField.getInstance());
		GenericFormalSum<Fraction, String> sum = M.multiply(5, M.add(M.add("b", "b"), "a"));
		System.out.println(sum.toString());
		
		// Formal sums of Simplex objects with Fraction coefficients
		//GenericFreeModule<Fraction, AbstractChainBasisElement> C = new  GenericFreeModule<Fraction, AbstractChainBasisElement>(RationalField.getInstance());
		//GenericFormalSum<Fraction, AbstractChainBasisElement> chain = C.add(new Simplex(new int[]{0, 1}), new Simplex(new int[]{2, 1}));
		//chain = C.add(chain, chain);
		//System.out.println(chain);
		
		// Formal sums of Simplex objects with int coefficients
		IntFreeModule<Simplex> D = new IntFreeModule<Simplex>(ModularIntField.getInstance(3));
		IntFormalSum<Simplex> chain2 = D.add(new Simplex(new int[]{0, 1}), new Simplex(new int[]{2, 1}));
		chain2 = D.add(chain2, chain2);
		chain2 = D.add(chain2, new Simplex(new int[]{0, 1}));
		System.out.println(chain2);
	}
}
