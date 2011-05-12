package edu.stanford.math.plex4.interop;

import edu.stanford.math.plex.Simplex;
import edu.stanford.math.plex4.homology.utility.HomologyUtility;
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
	private final int[] vertexMapping;
	
	public static Plex3ToPlex4SimplexAdapter getInstance() {
		return instance;
	}
	
	public static Plex3ToPlex4SimplexAdapter getInstance(int[] vertexMapping) {
		return new Plex3ToPlex4SimplexAdapter(vertexMapping);
	}
	
	private Plex3ToPlex4SimplexAdapter() {
		this.vertexMapping = null;
	}
	
	private Plex3ToPlex4SimplexAdapter(int[] vertexMapping) {
		this.vertexMapping = vertexMapping;
	}
	
	public edu.stanford.math.plex4.homology.chain_basis.Simplex evaluate(Simplex argument) {
		if (this.vertexMapping == null) {
			return new edu.stanford.math.plex4.homology.chain_basis.Simplex(IntArrayMath.scalarAdd(argument.vertices(), -1));
		} else {
			return new edu.stanford.math.plex4.homology.chain_basis.Simplex(HomologyUtility.convertIndices(IntArrayMath.scalarAdd(argument.vertices(), -1), this.vertexMapping));
		}
	}
}
