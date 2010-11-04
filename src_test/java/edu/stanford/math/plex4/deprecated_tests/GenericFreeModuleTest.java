package edu.stanford.math.plex4.deprecated_tests;


import org.apache.commons.math.fraction.Fraction;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.primitivelib.algebraic.impl.ModularIntField;
import edu.stanford.math.primitivelib.algebraic.impl.RationalField;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectSparseFormalSum;

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
		ObjectAlgebraicFreeModule<Fraction, String> M = new ObjectAlgebraicFreeModule<Fraction, String>(RationalField.getInstance());
		ObjectSparseFormalSum<Fraction, String> sum = M.multiply(5, M.add(M.add("b", "b"), "a"));
		System.out.println(sum.toString());
		
		// Formal sums of Simplex objects with Fraction coefficients
		//GenericFreeModule<Fraction, AbstractChainBasisElement> C = new  GenericFreeModule<Fraction, AbstractChainBasisElement>(RationalField.getInstance());
		//GenericFormalSum<Fraction, AbstractChainBasisElement> chain = C.add(new Simplex(new int[]{0, 1}), new Simplex(new int[]{2, 1}));
		//chain = C.add(chain, chain);
		//System.out.println(chain);
		
		// Formal sums of Simplex objects with int coefficients
		IntAlgebraicFreeModule<Simplex> D = new IntAlgebraicFreeModule<Simplex>(ModularIntField.getInstance(3));
		IntSparseFormalSum<Simplex> chain2 = D.add(new Simplex(new int[]{0, 1}), new Simplex(new int[]{2, 1}));
		chain2 = D.add(chain2, chain2);
		chain2 = D.add(chain2, new Simplex(new int[]{0, 1}));
		System.out.println(chain2);
	}
}
