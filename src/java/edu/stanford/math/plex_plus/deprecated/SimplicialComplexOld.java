package edu.stanford.math.plex_plus.deprecated;

import edu.stanford.math.plex_plus.homology.simplex.AbstractSimplex;
import gnu.trove.set.hash.THashSet;

/**
 * This abstract class defines functionality for a static simplicial complex.
 * It does not provide functionality for filtration. For that, one must
 * use the SimplexStream class. This is designed for computing non-persistent
 * homology.
 * 
 * The simplicial complex is expected to respect the ordering on the simplices,
 * by allowing the user to access the simplices in order. See the Simplex class
 * for the definition of the ordering.
 * 
 * @author Andrew Tausz
 *
 */

@Deprecated
public abstract class SimplicialComplexOld {
	
	/**
	 * This function adds a simplex to the simplicial complex.
	 * 
	 * @param simplex the simplex to add
	 */
	public abstract void addSimplex(AbstractSimplex simplex);
	
	/**
	 * This function returns the maximum dimension of the simplices
	 * in the simplicial complex.
	 * 
	 * @return the dimension of the simplicial complex
	 */
	public abstract int getDimension();
	
	/**
	 * This function returns the k-skeleton of the simplicial complex, which
	 * is defined to be the set of simplices in the complex with dimension
	 * less than or equal to k.
	 * 
	 * @param k the dimension
	 * @return the k-skeleton of the simplicial complex
	 */
	public abstract THashSet<AbstractSimplex> getSkeleton(int k);
	
	/**
	 * This function returns the index of the supplied simplex,
	 * viewed as a member of the current simplicial complex.
	 * 
	 * @param simplex the simplex to query
	 * @return the index of the simplex within the complex
	 */
	public abstract int getIndex(AbstractSimplex simplex);
	
	/**
	 * This function returns the simplex at the supplied index.
	 * This relies on the defined ordering on the Simplex class.
	 * 
	 * @param index the index of the simplex to retrieve
	 * @return the simplex at the given index within the complex
	 */
	public abstract AbstractSimplex getAtIndex(int index);
	
	/*
	IntLabeledGrid<AbstractChainBasisElement> getBoundaryMatrix(int k) {
		ExceptionUtility.verifyPositive(k);
		List<AbstractChainBasisElement> kSkeleton = new ArrayList<AbstractChainBasisElement>();
		kSkeleton.addAll(this.getSkeleton(k));
		
		IntLabeledGrid<AbstractChainBasisElement> grid = new IntLabeledGrid<AbstractChainBasisElement>();
		
		for (int i = 0; i < kSkeleton.size(); i++) {
			int[] vertices = kSkeleton.get(i).getVertices();
			for (int j = 0; j < vertices.length; j++) {
				int[] boundaryVertex = HomologyUtility.removeIndex(vertices, j);
				grid.setEntry(new Simplex(boundaryVertex), kSkeleton.get(i), (j % 2 == 0 ? 1 : -1));
			}
		}
		return grid;		
	}
	*/
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int d = 0; d <= this.getDimension(); d++) {
			builder.append(d + ": ");
			builder.append('{');
			THashSet<AbstractSimplex> skeleton = this.getSkeleton(d);
			for (AbstractSimplex simplex : skeleton) {
				builder.append(simplex.toString());
				builder.append(' ');
			}
			builder.append("}\n");
		}
		return builder.toString();
	}
}
