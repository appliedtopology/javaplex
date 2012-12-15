/**
 * 
 */
package edu.stanford.math.plex4.streams.impl;

import edu.stanford.math.plex4.graph.UndirectedWeightedListGraph;
import edu.stanford.math.plex4.homology.barcodes.Interval;
import edu.stanford.math.plex4.homology.barcodes.PersistenceInvariantDescriptor;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.filtration.FiltrationConverter;
import edu.stanford.math.plex4.homology.filtration.FiltrationUtility;
import edu.stanford.math.plex4.homology.utility.HomologyUtility;
import edu.stanford.math.plex4.streams.interfaces.PrimitiveStream;
import edu.stanford.math.plex4.streams.storage_structures.StreamStorageStructure;
import gnu.trove.TIntHashSet;
import gnu.trove.TIntIterator;

/**
 * <p>
 * This class implements a simplex stream with the property that given its
 * 1-skeleton, its higher skeletons are maximal in the following sense. A
 * k-simplex [v_0, ..., v_k] is a member of the complex iff all of its edges
 * [v_i, v_j] are in the complex.
 * </p>
 * 
 * <p>
 * This class is abstract in that it does not provide a mechanism for
 * constructing the 1-skeleton, and only constructs the higher order skeletons
 * inductively. The reason is that there are different choices for definitions
 * of the 1-skeletons, the main ones being the lazy witness complex, the
 * Vietoris-Rips complex, and the clique complex. Thus a child class must
 * provide the implementation of the 1-skeleton.
 * </p>
 * 
 * <p>
 * Also note that the FlagComplexStream construction can be used with any finite
 * metric space. For information on the implementation, consult the paper
 * "Fast Construction of the Vietoris-Rips Complex", by Afra Zomorodian. This
 * implementation uses the incremental algorithm described in the above paper.
 * </p>
 * 
 * @author Andrew Tausz
 * 
 */
public abstract class FlagComplexStream extends PrimitiveStream<Simplex> {
	/**
	 * The maximum allowable dimension of the complex.
	 */
	protected final int maxAllowableDimension;

	/**
	 * Stores the neighborhood graph.
	 */
	protected UndirectedWeightedListGraph neighborhoodGraph = null;

	/**
	 * This converts between filtration indices and values
	 */
	protected FiltrationConverter converter;

	protected int[] indices = null;

	/**
	 * This constructor initializes the class.
	 * 
	 * @param maxAllowableDimension
	 *            the maximum dimension of the simplicial complex
	 * @param converter
	 *            a FiltrationConverter for translating between filtration
	 *            indices and values
	 */
	public FlagComplexStream(int maxAllowableDimension, FiltrationConverter converter) {
		super(SimplexComparator.getInstance());
		this.maxAllowableDimension = maxAllowableDimension;
		this.converter = converter;
	}

	public FlagComplexStream(int maxAllowableDimension, FiltrationConverter converter, int[] indices) {
		super(SimplexComparator.getInstance());
		this.maxAllowableDimension = maxAllowableDimension;
		this.converter = converter;
		this.indices = indices;
	}

	/**
	 * Constructor.
	 * 
	 * @param maxAllowableDimension
	 *            the maximum dimension of the simplicial complex
	 * @param converter
	 *            a FiltrationConverter for translating between filtration
	 *            indices and values
	 * @param storageStructure
	 *            the StreamStorageStructure to use
	 */
	public FlagComplexStream(int maxAllowableDimension, FiltrationConverter converter, StreamStorageStructure<Simplex> storageStructure) {
		super(storageStructure);
		this.maxAllowableDimension = maxAllowableDimension;
		this.converter = converter;
	}

	/**
	 * This function implements the construction of the 1-skeleton. It must
	 * output a list containing pairs of filtration values and 1-simplices. The
	 * filtration value of an edge is the threshold value at which the edge
	 * appears in the filtered sequence of simplicial complexes.
	 * 
	 * @return the 1-skeleton of the complex along with its filtration values
	 */
	protected abstract UndirectedWeightedListGraph constructEdges();

	/**
	 * This returns the neighborhood graph (equivalent to the 1-skeleton) of the
	 * complex.
	 * 
	 * @return the neighborhood graph
	 */
	public UndirectedWeightedListGraph getNeighborhoodGraph() {
		return this.neighborhoodGraph;
	}

	public double getFiltrationValue(Simplex simplex) {
		return this.converter.getFiltrationValue(this.getFiltrationIndex(simplex));
	}

	/**
	 * This function returns the FiltrationConverter object used by the complex.
	 * 
	 * @return the FiltrationConverter used by the complex
	 */
	public FiltrationConverter getConverter() {
		return this.converter;
	}

	/**
	 * This function transforms the given collection of filtration index
	 * barcodes into filtration value barcodes.
	 * 
	 * @param <G>
	 * @param barcodeCollection
	 *            the set of filtration index barcodes
	 * @return the barcodes transformed into filtration value form
	 */
	public <G> PersistenceInvariantDescriptor<Interval<Double>, G> transform(PersistenceInvariantDescriptor<Interval<Integer>, G> barcodeCollection) {
		return FiltrationUtility.transform(barcodeCollection, this.converter);
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
	 * @param G
	 *            the neighborhood graph
	 * @param k
	 *            the maximum dimension
	 */
	protected void incrementalExpansion(UndirectedWeightedListGraph G, int k) {
		int n = G.getNumVertices();

		// inductively add all of the singletons as well as their cofaces
		for (int u = 0; u < n; u++) {
			this.addCofaces(G, k, new Simplex(new int[] { u }), G.getLowerNeighbors(u), this.converter.getInitialFiltrationValue());
		}
	}

	/**
	 * This function inductively adds all of the cofaces of the simplex tau to
	 * the complex. For more information about this algorithm, consult the paper
	 * "Fast Construction of the Vietoris-Rips Complex" by Afra Zomorodian.
	 * 
	 * @param G
	 *            the neighborhood graph
	 * @param k
	 *            the maximum allowable dimension
	 * @param tau
	 *            the current simplex to add
	 * @param N
	 *            the lower neighbors to investigate
	 * @param filtrationValue
	 *            the filtration value of the current simplex, tau
	 */
	protected void addCofaces(UndirectedWeightedListGraph G, int k, Simplex tau, TIntHashSet N, double filtrationValue) {

		Simplex newSimplex = null;
		if (this.indices != null) {
			newSimplex = HomologyUtility.convertIndices(tau, this.indices);
		} else {
			newSimplex = tau;
		}

		// add the current simplex to the complex
		this.storageStructure.addElement(newSimplex, this.converter.getFiltrationIndex(filtrationValue));

		// exit if the dimension is the maximum allowed
		if (tau.getDimension() >= k) {
			return;
		}

		double weight = 0;

		TIntIterator iterator = N.iterator();
		TIntHashSet M;

		// iterate through the lower neighborhood
		while (iterator.hasNext()) {
			int v = iterator.next();

			// create a new simplex by appending
			// ie. sigma = tau U {v}
			Simplex sigma = new Simplex(HomologyUtility.appendToArray(tau.getVertices(), v));

			// compute the intersection between N and the lower neighbors of v
			M = HomologyUtility.computeIntersection(N, G.getLowerNeighbors(v));

			// compute the weight of the simplex sigma
			// the weight is defined to be the maximum weight of all of the
			// simplex's
			// faces
			if (sigma.getDimension() == 1) {
				int i = sigma.getVertices()[0];
				int j = sigma.getVertices()[1];
				weight = G.getWeight(i, j);
			} else if (sigma.getDimension() > 1) {
				weight = filtrationValue;
				int[] tauVertices = tau.getVertices();
				for (int tauVertex : tauVertices) {
					weight = this.converter.computeInducedFiltrationValue(weight, G.getWeight(tauVertex, v));
				}
			}

			// recurse: add the cofaces of sigma
			this.addCofaces(G, k, sigma, M, weight);
		}
	}
}
