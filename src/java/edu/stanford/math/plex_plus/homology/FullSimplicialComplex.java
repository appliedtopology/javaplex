package edu.stanford.math.plex_plus.homology;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.stanford.math.plex_plus.utility.ArrayUtility;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * This class is a basic implementation of a simplicial complex.
 * 
 * TODO: This class was just written up temporarily for testing 
 * purposes. It should be rewritten in a more efficient way. It 
 * currently stores all of the simplicies in the simplicial complex,
 * thus there is considerable redundancy.
 * 
 * @author Andris
 *
 */
public class FullSimplicialComplex extends SimplicialComplex {
	private Map<Integer, SortedSet<Simplex>> skeletons = new HashMap<Integer, SortedSet<Simplex>>();
	private int maxDimension = 0;

	@Override
	public void addSimplex(Simplex simplex) {
		ExceptionUtility.verifyNonNull(simplex);
		int d = simplex.dimension();
		this.maxDimension = Math.max(this.maxDimension, d);

		if (!skeletons.containsKey(d)) {
			skeletons.put(d, new TreeSet<Simplex>());
		}
		skeletons.get(d).add(simplex);
		int[] vertices = simplex.getVertices();
		if (vertices.length > 1) {
			for (int i = 0; i < vertices.length; i++) {
				this.addSimplex(new ArraySimplex(ArrayUtility.removeIndex(vertices, i)));
			}
		}
	}

	@Override
	public int[][] getDenseBoundaryMatrix(int k) {
		ExceptionUtility.verifyPositive(k);		
		// TODO Auto-generated method stub
		return null;		
	}

	@Override
	public int getDimension() {
		return this.maxDimension;
	}

	@Override
	public SortedSet<Simplex> getSkeleton(int k) {
		ExceptionUtility.verifyNonNegative(k);
		return this.skeletons.get(k);
	}
}
