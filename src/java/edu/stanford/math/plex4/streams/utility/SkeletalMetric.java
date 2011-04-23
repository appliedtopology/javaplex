package edu.stanford.math.plex4.streams.utility;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.metric.interfaces.AbstractObjectMetricSpace;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.autogen.array.DoubleArrayUtility;
import edu.stanford.math.primitivelib.autogen.formal_sum.DoubleSparseFormalSum;
import edu.stanford.math.primitivelib.utility.Infinity;
import gnu.trove.TIntIntHashMap;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TObjectDoubleIterator;
import gnu.trove.TObjectIntHashMap;

/**
 * This class implements a metric on a simplicial complex. The distance between two simplices
 * is defined to be the maximum of the distances between the any two pairs of vertices where one
 * vertex is in the first simplex and the other is in the second simplex.
 * 
 * @author Andrew Tausz
 *
 */
public class SkeletalMetric implements AbstractObjectMetricSpace<Simplex> {
	/**
	 * This maps a basis element to an index. The index is the appropriate
	 * index of the standard basis vector the generating element is mapped to.
	 */
	private final TObjectIntHashMap<Simplex> indexMapping = new TObjectIntHashMap<Simplex>();
	
	/**
	 * This maps an index (ie. the index of a standard basis vector) to a generating
	 * element of the free module.
	 */
	private final TIntObjectHashMap<Simplex> basisMapping = new TIntObjectHashMap<Simplex>();
	
	private final TIntIntHashMap vertexIndexMap = new TIntIntHashMap();
	private final TIntIntHashMap indexVertexMap = new TIntIntHashMap();
	
	private int numVertices = 0;
	
	private List<Simplex> edgeSet = new ArrayList<Simplex>();
	
	private final double[][] distances;
	
	public SkeletalMetric(AbstractFilteredStream<Simplex> stream) {
		this.initializeMappings(stream);
		this.distances = this.computeShortestPaths();
	}
	
	public double getDiameter(DoubleSparseFormalSum<Simplex> chain) {
		double diameter = 0;
		
		for (TObjectDoubleIterator<Simplex> iterator_1 = chain.iterator(); iterator_1.hasNext(); ) {
			iterator_1.advance();
			Simplex simplex_1 = iterator_1.key();
			
			for (TObjectDoubleIterator<Simplex> iterator_2 = chain.iterator(); iterator_2.hasNext(); ) {
				iterator_2.advance();
				Simplex simplex_2 = iterator_2.key();
				
				double distance = this.distance(simplex_1, simplex_2);
				
				diameter = Math.max(diameter, distance * iterator_1.value() * iterator_2.value());				
			}			
		}
		
		return diameter;
	}
	
	
	public Simplex getPoint(int index) {
		return this.basisMapping.get(index);
	}

	public int size() {
		return this.basisMapping.size();
	}

	public double distance(int i, int j) {
		return this.distances[i][j];
	}
	
	public double distance(Simplex simplex_i, Simplex simplex_j) {
		return this.distances[this.indexMapping.get(simplex_i)][this.indexMapping.get(simplex_j)];
	}
	
	/**
	 * This function initializes the basis-index mappings.
	 */
	private void initializeMappings(AbstractFilteredStream<Simplex> stream) {
		int index = 0;
		for (Simplex basisElement: stream) {
			int dimension = basisElement.getDimension();
			if (dimension == 0) {
				this.numVertices++;
				this.vertexIndexMap.put(basisElement.getVertices()[0], index);
				this.indexVertexMap.put(index, basisElement.getVertices()[0]);
			} else if (dimension == 1){
				this.edgeSet.add(basisElement);
			}
			
			this.indexMapping.put(basisElement, index);
			this.basisMapping.put(index, basisElement);
			index++;
		}
	}
	
	/**
	 * This function computes the shortest paths distances between all pairs within 
	 * the 1-skeleton of the complex using the Floyd-Warshall algorithm.
	 * 
	 * @return
	 */
	private double[][] computeShortestPaths() {
		int N = this.size();
		int n = this.numVertices;
		double[][] pathLengths = DoubleArrayUtility.createMatrix(N, N);
		
		// initialize distances to +infinity
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				pathLengths[i][j] = Infinity.Double.getPositiveInfinity();
			}
		}
		
		// compute immediate distances between points
		for (Simplex edge: this.edgeSet) {
			int[] vertices = edge.getVertices();
			int i = this.vertexIndexMap.get(vertices[0]);
			int j = this.vertexIndexMap.get(vertices[1]);
			
			pathLengths[i][j] = 1;
			pathLengths[j][i] = 1;
		}
		
		// fill in distances between all vertices using the Floyd-Warshall algorithm
		for (int k_index = 0; k_index < n; k_index++) {
			int k = this.indexVertexMap.get(k_index);
			for (int i_index = 0; i_index < n; i_index++) {
				int i = this.indexVertexMap.get(i_index);
				for (int j_index = 0; j_index < n; j_index++) {
					int j = this.indexVertexMap.get(j_index);
					pathLengths[i][j] = Math.min(pathLengths[i][j], pathLengths[i][k] + pathLengths[k][j]);
				}
			}
		}
		
		// compute the distances between other simplices
		for (int i_index = 0; i_index < N; i_index++) {
			Simplex sigma_i = this.basisMapping.get(i_index);
			int dimension_i = sigma_i.getDimension();
			int[] vertices_i = sigma_i.getVertices();
			pathLengths[i_index][i_index] = 0;
			for (int j_index = 0; j_index < i_index; j_index++) {			
				Simplex sigma_j = this.basisMapping.get(j_index);
				int dimension_j = sigma_j.getDimension();
				if (dimension_i == 0 && dimension_j == 0) {
					continue;
				}
				int[] vertices_j = sigma_j.getVertices();
				
				double distance = Infinity.Double.getPositiveInfinity();
				
				for (int i_vertex_index = 0; i_vertex_index < vertices_i.length; i_vertex_index++) {
					for (int j_vertex_index = 0; j_vertex_index < vertices_j.length; j_vertex_index++) {
						if (i_vertex_index != j_vertex_index) {
							distance = Math.min(distance, pathLengths[this.vertexIndexMap.get(i_vertex_index)][this.vertexIndexMap.get(j_vertex_index)]);
						}
					}
				}
				
				pathLengths[i_index][j_index] = distance;
				pathLengths[j_index][i_index] = distance;
			}
		}
		
		return pathLengths;
	}

	public Simplex[] getPoints() {
		throw new UnsupportedOperationException();
	}
}
