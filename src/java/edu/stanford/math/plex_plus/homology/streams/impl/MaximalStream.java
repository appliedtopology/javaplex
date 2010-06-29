/**
 * 
 */
package edu.stanford.math.plex_plus.homology.streams.impl;

import edu.stanford.math.plex_plus.graph.UndirectedWeightedListGraph;
import edu.stanford.math.plex_plus.homology.chain_basis.Simplex;
import edu.stanford.math.plex_plus.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex_plus.homology.streams.interfaces.PrimitiveStream;
import edu.stanford.math.plex_plus.homology.utility.HomologyUtility;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.set.TIntSet;

/**
 * This class implements a simplex stream with the property that
 * given its 1-skeleton, its higher skeletons are maximal in the 
 * following sense. A k-simplex [v_0, ..., v_k] is a member of the
 * complex iff all of its edges [v_i, v_j] are in the complex.
 * Thus such a complex is the maximal complex given its 1-skeleton.
 * 
 * This class is abstract in that it does not provide a mechanism 
 * for constructing the 1-skeleton, and only constructs the higher
 * order skeletons inductively. The reason is that there are different
 * choices for definitions of the 1-skeletons, the main ones being the
 * lazy witness complex, the Vietoris-Rips complex, and the clique complex. 
 * Thus a child class must provide the implementation of the 1-skeleton.
 * 
 * Also note that the MaximalStream construction can be used with 
 * any finite metric space. For information on the implementation,
 * consult the paper "Fast Construction of the Vietoris-Rips Complex",
 * by Afra Zomorodian. This implementation uses the incremental
 * algorithm described in the above paper.
 * 
 * @author Andrew Tausz
 *
 */
public abstract class MaximalStream extends PrimitiveStream<Simplex> {	
	/**
	 * The maximum allowable dimension of the complex.
	 */
	protected final int maxAllowableDimension;
	
	/**
	 * Stores the neighborhood graph.
	 */
	protected UndirectedWeightedListGraph neighborhoodGraph = null;

	/**
	 * The maximum distance to use in the construction of the complex.
	 */
	protected final double maxDistance;
	
	/**
	 * Constructor.
	 * 
	 * @param maxDistance the maximum allowable distance in the complex
	 * @param maxAllowableDimension the maximum dimension of the complex
	 */
	public MaximalStream(int maxAllowableDimension, double maxDistance) {
		super(SimplexComparator.getInstance());
		this.maxAllowableDimension = maxAllowableDimension;
		this.maxDistance = maxDistance;
	}
	
	/**
	 * This function implements the construction of the 1-skeleton. It must output
	 * a list containing pairs of filtration values and 1-simplices. The filtration
	 * value of an edge is the threshold value at which the edge appears in the
	 * filtered sequence of simplicial complexes.
	 * 
	 * @return the 1-skeleton of the complex along with its filtration values
	 */
	protected abstract UndirectedWeightedListGraph constructEdges();
	
	public UndirectedWeightedListGraph getNeighborhoodGraph() {
		return this.neighborhoodGraph;
	}
	
	@Override
	protected void constructComplex() {
		// construct the neighborhood graph
		this.neighborhoodGraph = this.constructEdges();
		
		// expand higher order simplices
		this.incrementalExpansion(neighborhoodGraph, this.maxAllowableDimension);
	}
	
	
	/**
	 * This function performs the incremental expansion of the complex.
	 * 
	 * @param G the neighborhood graph
	 * @param k the maximum dimension
	 */
	protected void incrementalExpansion(UndirectedWeightedListGraph G, int k) {
		int n = G.getNumVertices();
		
		// inductively add all of the singletons as well as their cofaces
		for (int u = 0; u < n; u++) {
			this.addCofaces(G, k, new Simplex(new int[]{u}), G.getLowerNeighbors(u), 0);
		}
	}
	
	/**
	 * This function inductively adds all of the cofaces of the simplex tau to the
	 * complex. For more information about this algorithm, consult the paper
	 * "Fast Construction of the Vietoris-Rips Complex" by Afra Zomorodian.
	 * 
	 * @param G the neighborhood graph
	 * @param k the maximum allowable dimension
	 * @param tau the current simplex to add
	 * @param N the lower neighbors to investigate
	 */
	protected void addCofaces(UndirectedWeightedListGraph G, int k, Simplex tau, TIntSet N, double filtrationValue) {
		// add the current simplex to the complex
		this.storageStructure.addElement(tau, filtrationValue);
		
		// exit if the dimension is the maximum allowed
		if (tau.getDimension() >= k) {
			return;
		}
		
		double weight = 0;
		
		TIntIterator iterator = N.iterator();
		
		// iterate through the lower neighborhood
		while (iterator.hasNext()) {
			int v = iterator.next();
			
			// create a new simplex by appending
			// ie. sigma = tau U {v}
			Simplex sigma = new Simplex(HomologyUtility.appendToArray(tau.getVertices(), v));
			
			// compute the intersection between N and the lower neighbors of v
			TIntSet M = HomologyUtility.computeIntersection(N, G.getLowerNeighbors(v));
			
			// compute the weight of the simplex sigma
			// the weight is defined to be the maximum weight of all of the simplex's
			// faces
			if (sigma.getDimension() == 1) {
				int i = sigma.getVertices()[0];
				int j = sigma.getVertices()[1];
				weight = G.getWeight(i, j);
			} else if (sigma.getDimension() > 1) {
				weight = filtrationValue;
				int[] tauVertices = tau.getVertices();
				for(int tauVertex : tauVertices) {
					weight = Math.max(weight, G.getWeight(tauVertex, v));
				}
			}
			
			// recurse: add the cofaces of sigma
			this.addCofaces(G, k, sigma, M, weight);
		}
	}
}
