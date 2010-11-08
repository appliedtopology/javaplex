package edu.stanford.math.plex4.interop;

import edu.stanford.math.plex.Simplex;
import edu.stanford.math.primitivelib.autogen.array.IntArrayMath;
import edu.stanford.math.primitivelib.autogen.functional.ObjectObjectFunction;

/**
 * This class implements a function object which converts plex 3 simplices to plex 4 simplices. Since plex 4
 * operates in a 0-based way, it also shifts the indices down by 1.
 * 
 * @author Andrew Tausz
 *
 */
public class Plex3ToPlex4SimplexAdapter implements ObjectObjectFunction<edu.stanford.math.plex.Simplex, edu.stanford.math.plex4.homology.chain_basis.Simplex> {
	private static Plex3ToPlex4SimplexAdapter instance = new Plex3ToPlex4SimplexAdapter();
	
	public static Plex3ToPlex4SimplexAdapter getInstance() {
		return instance;
	}
	
	private Plex3ToPlex4SimplexAdapter(){}
	
	public edu.stanford.math.plex4.homology.chain_basis.Simplex evaluate(Simplex argument) {
		return new edu.stanford.math.plex4.homology.chain_basis.Simplex(IntArrayMath.scalarAdd(argument.vertices(), -1));
	}
}
