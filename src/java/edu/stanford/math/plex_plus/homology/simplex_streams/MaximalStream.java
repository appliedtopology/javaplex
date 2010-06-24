/**
 * 
 */
package edu.stanford.math.plex_plus.homology.simplex_streams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import edu.stanford.math.plex_plus.datastructures.pairs.DoubleGenericPair;
import edu.stanford.math.plex_plus.datastructures.pairs.DoubleGenericPairComparator;
import edu.stanford.math.plex_plus.datastructures.pairs.DoubleOrderedIterator;
import edu.stanford.math.plex_plus.graph.UndirectedWeightedListGraph;
import edu.stanford.math.plex_plus.homology.simplex.Simplex;
import edu.stanford.math.plex_plus.homology.simplex.SimplexComparator;
import edu.stanford.math.plex_plus.homology.utility.HomologyUtility;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.hash.TObjectDoubleHashMap;
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
public abstract class MaximalStream implements SimplexStream<Simplex> {	
	/**
	 * The maximum allowable dimension of the complex.
	 */
	protected final int maxDimension;
	
	/**
	 * This will store the 1-skeleton of the complex in increasing order of filtration.
	 */
	protected List<DoubleGenericPair<Simplex>> simplices = new ArrayList<DoubleGenericPair<Simplex>>();
	
	/**
	 * This hash table stores the filtration values for the 1-skeleton of the complex.
	 * It is designed to provide fast access to the filtration values.
	 */
	protected final TObjectDoubleHashMap<Simplex> filtrationValues = new TObjectDoubleHashMap<Simplex>();
	
	/**
	 * This comparator defines the standard ordering on the filtered simplices.
	 */
	protected final DoubleGenericPairComparator<Simplex> comparator = new DoubleGenericPairComparator<Simplex>(SimplexComparator.getInstance());
	
	/**
	 * Stores the maximum dimension in the complex
	 */
	private int dimension = 0;
	
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
	 * @param maxDimension the maximum dimension of the complex
	 */
	public MaximalStream(int maxDimension, double maxDistance) {
		this.maxDimension = maxDimension;
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
	
	protected void constructComplex() {
		// construct the neighborhood graph
		this.neighborhoodGraph = this.constructEdges();
		
		// expand higher order simplices
		this.incrementalExpansion(neighborhoodGraph, this.maxDimension);
		
		// sort simplices by filtration order
		Collections.sort(this.simplices, this.comparator);
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
		this.addSimplex(tau, filtrationValue);
		
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
	
	/**
	 * This function simply updates the simplicial complex with a new simplex
	 * 
	 * @param simplex the simplex to add
	 * @param filtrationValue its filtration value
	 */
	protected void addSimplex(Simplex simplex, double filtrationValue) {
		this.simplices.add(new DoubleGenericPair<Simplex>(filtrationValue, simplex));
		this.filtrationValues.put(simplex, filtrationValue);
		this.dimension = Math.max(this.dimension, simplex.getDimension());
	}
	
	@Override
	public void finalizeStream() {
		this.constructComplex();
	}

	@Override
	public double getFiltrationValue(Simplex simplex) {
		return this.filtrationValues.get(simplex);
	}

	@Override
	public boolean isFinalized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<Simplex> iterator() {
		return new DoubleOrderedIterator<Simplex>(this.simplices);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for (Simplex simplex : this) {
			builder.append(simplex.toString());
			builder.append('\n');
		}
		
		return builder.toString();
	}
	
	@Override
	public int getDimension() {
		return this.dimension;
	}	
}
