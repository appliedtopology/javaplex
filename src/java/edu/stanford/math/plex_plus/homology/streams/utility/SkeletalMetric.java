package edu.stanford.math.plex_plus.homology.streams.utility;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.math.plex_plus.homology.chain_basis.Simplex;
import edu.stanford.math.plex_plus.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex_plus.math.metric.interfaces.FiniteMetricSpace;
import edu.stanford.math.plex_plus.utility.ArrayUtility2;
import edu.stanford.math.plex_plus.utility.Infinity;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;

public class SkeletalMetric implements FiniteMetricSpace<Simplex> {
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
	
	SkeletalMetric(AbstractFilteredStream<Simplex> stream) {
		this.initializeMappings(stream);
		this.distances = this.computeShortestPaths();
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
		double[][] pathLengths = ArrayUtility2.newDoubleMatrix(N, N);
		
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
			if (dimension_i == 0) {
				continue;
			}
			int[] vertices_i = sigma_i.getVertices();
			for (int j_index = 0; j_index < n; j_index++) {
				Simplex sigma_j = this.basisMapping.get(j_index);
				int dimension_j = sigma_j.getDimension();
				if (dimension_j == 0) {
					continue;
				}
				int[] vertices_j = sigma_j.getVertices();
				
				double distance = Infinity.Double.getPositiveInfinity();
				
				for (int i_vertex_index = 0; i_vertex_index < vertices_i.length; i_vertex_index++) {
					for (int j_vertex_index = 0; j_vertex_index < vertices_j.length; j_vertex_index++) {
						distance = Math.min(distance, pathLengths[this.vertexIndexMap.get(i_vertex_index)][this.vertexIndexMap.get(j_vertex_index)]);
					}
				}
				
				pathLengths[i_index][j_index] = distance;				
			}
		}
		
		return pathLengths;
	}

}
