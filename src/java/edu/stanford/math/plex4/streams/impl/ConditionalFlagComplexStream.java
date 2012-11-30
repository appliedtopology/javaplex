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
import edu.stanford.math.primitivelib.autogen.pair.BooleanDoublePair;
import java.util.ArrayList; 

/**
 * <p>This class implements a simplex stream which is similar to the
 * FlagComplexStream class, except that higher order simplices are subject to
 * an abstract property.</p>
 * 
 * <p>A motivating example is the regular witness complex, which has the property
 * that a higher (n > 1) n-simplex is in the complex if all of its faces are in
 * the complex and it satisfies an additional property that there is a witness 
 * outside the landmark set for all of the vertices in the simplex.</p>
 * 
 * @author Andrew Tausz
 */
public abstract class ConditionalFlagComplexStream extends PrimitiveStream<Simplex> {	
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
	 * @param maxAllowableDimension the maximum dimension of the simplicial complex
	 * @param converter a FiltrationConverter for translating between filtration indices and values
	 */
	public ConditionalFlagComplexStream(int maxAllowableDimension, FiltrationConverter converter) {
		super(SimplexComparator.getInstance());
		this.maxAllowableDimension = maxAllowableDimension;
		this.converter = converter;
	}
	
	public ConditionalFlagComplexStream(int maxAllowableDimension, FiltrationConverter converter, int[] indices) {
		super(SimplexComparator.getInstance());
		this.maxAllowableDimension = maxAllowableDimension;
		this.converter = converter;
		this.indices = indices;
	}
	
	
	/**
	 * Constructor.
	 * 
	 * @param maxAllowableDimension the maximum dimension of the simplicial complex
	 * @param converter a FiltrationConverter for translating between filtration indices and values
	 * @param storageStructure the StreamStorageStructure to use
	 */
	public ConditionalFlagComplexStream(int maxAllowableDimension, FiltrationConverter converter, StreamStorageStructure<Simplex> storageStructure) {
		super(storageStructure);
		this.maxAllowableDimension = maxAllowableDimension;
		this.converter = converter;
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
	
	protected abstract BooleanDoublePair isMember(Simplex simplex);
	
	/**
	 * This returns the neighborhood graph (equivalent to the 1-skeleton) of the complex.
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
	 * This function transforms the given collection of filtration index barcodes into filtration value barcodes.
	 * 
	 * @param <G>
	 * @param barcodeCollection the set of filtration index barcodes
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
	 * @param G the neighborhood graph
	 * @param k the maximum dimension
	 */
	protected void incrementalExpansion(UndirectedWeightedListGraph G, int k) {
		int n = G.getNumVertices();
		
		// inductively add all of the singletons as well as their cofaces
		ArrayList<Integer> lower_vertices = new ArrayList<Integer>(n-1);
		for (int u = 0; u < n; u++) {
			/**
			 * Daniel: I assigned 0 as the filtration index for vertices.
			 * Is that always correct? Is 0 always the lowest filtration index?
			 */
			this.addCofaces(G, k, new Simplex(new int[]{u}), lower_vertices, 0);
			lower_vertices.add(u);
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
	 * @param filtrationValue the filtration value of the current simplex, tau
	 */
	protected void addCofaces(UndirectedWeightedListGraph G, int k, Simplex tau, 
			ArrayList<Integer> lower_vertices, int filtrationIndex) {
		Simplex newSimplex = null;
		if (this.indices != null) {
			newSimplex = HomologyUtility.convertIndices(tau, this.indices);
		} else {
			newSimplex = tau;
		}
		
		// add the current simplex to the complex if it satisfies the condition
		BooleanDoublePair member = this.isMember(tau);
		if (member.getFirst()) {
			filtrationIndex = Math.max(filtrationIndex, this.converter.getFiltrationIndex(member.getSecond()));
			this.storageStructure.addElement(newSimplex, filtrationIndex);
		}
	
		// exit if the dimension is the maximum allowed
		if (tau.getDimension() >= k) {
			return;
		}
		
		if (lower_vertices.size()==0) return;
		ArrayList<Integer> new_lower_vertices = new ArrayList<Integer>(lower_vertices.size()-1);
				
		// iterate through the lower neighborhood
		for (int v : lower_vertices) {
			// create a new simplex by appending
			// ie. sigma = tau U {v}
			Simplex sigma = new Simplex(HomologyUtility.appendToArray(tau.getVertices(), v));
			if (this.indices != null) {
				newSimplex = HomologyUtility.convertIndices(sigma, this.indices);
			} else {
				newSimplex = sigma;
			}
			
			int newFiltrationIndex = filtrationIndex;
			for (Simplex ds : newSimplex.getBoundaryArray()) {
				if (this.storageStructure.containsElement(ds)) {
					newFiltrationIndex = Math.max(newFiltrationIndex, this.storageStructure.getFiltrationIndex(ds));					
				}
				else {
					continue;
				}
			}
			
			// recurse: add the cofaces of sigma
			this.addCofaces(G, k, sigma, new_lower_vertices, newFiltrationIndex);
			new_lower_vertices.add(v);
		}
	}
}
