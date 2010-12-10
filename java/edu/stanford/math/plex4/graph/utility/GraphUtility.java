package edu.stanford.math.plex4.graph.utility;

import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex4.graph.AbstractWeightedUndirectedGraph;
import edu.stanford.math.primitivelib.autogen.array.DoubleArrayUtility;
import edu.stanford.math.primitivelib.autogen.matrix.DoubleSparseMatrix;
import edu.stanford.math.primitivelib.autogen.matrix.IntSparseMatrix;
import edu.stanford.math.primitivelib.autogen.pair.IntIntPair;
import edu.stanford.math.primitivelib.utility.Infinity;

/**
 * This class contains various static functions for manipulating graphs.
 * 
 * @author Andrew Tausz
 *
 */
public class GraphUtility {
	
	/**
	 * This function computes all of the distances between pairs of vertices using
	 * the Floyd-Warshall algorithm.
	 *
	 * @param graph the graph
	 * @return the matrix of path lengths between each pair of vertices
	 */
	public static double[][] computeShortestPaths(AbstractUndirectedGraph graph) {
		int n = graph.getNumVertices();
		double[][] pathLengths = DoubleArrayUtility.createMatrix(n, n);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (graph.containsEdge(i, j)) {
					pathLengths[i][j] = 1;
				} else {
					pathLengths[i][j] = Infinity.Double.getPositiveInfinity();
				}
			}
		}
		
		for (int k = 0; k < n; k++) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					pathLengths[i][j] = Math.min(pathLengths[i][j], pathLengths[i][k] + pathLengths[k][j]);
				}
			}
		}
		
		for (int i = 0; i < n; i++) {
			pathLengths[i][i] = 0;
		}
		
		return pathLengths;
	}
	
	/**
	 * This function computes the adjacency matrix of the given graph.
	 * 
	 * @param graph the graph to compute the adjacency matrix of
	 * @return the adjacency matrix of the graph
	 */
	public static int[][] getAdjacencyMatrix(AbstractUndirectedGraph graph) {
		int n = graph.getNumVertices();
		int[][] matrix = new int[n][n];
		
		for (IntIntPair edge: graph) {
			int i = edge.getFirst();
			int j = edge.getSecond();
			matrix[i][j] = 1;
			matrix[j][i] = 1;
		}
		
		return matrix;
	}
	
	/**
	 * This function computes the adjacency matrix of the given graph.
	 * It returns the result as a sparse matrix.
	 * 
	 * @param graph the graph to compute the adjacency matrix of
	 * @return the adjacency matrix of the graph
	 */
	public static IntSparseMatrix getSparseAdjacencyMatrix(AbstractUndirectedGraph graph) {
		int n = graph.getNumVertices();
		IntSparseMatrix matrix = new IntSparseMatrix(n, n);
		
		for (IntIntPair edge: graph) {
			int i = edge.getFirst();
			int j = edge.getSecond();
			matrix.set(i, j, 1);
			matrix.set(j, i, 1);
		}
		
		return matrix;
	}
	
	/**
	 * This function computes the adjacency matrix of the given graph.
	 * 
	 * @param graph the graph to compute the adjacency matrix of
	 * @return the adjacency matrix of the graph
	 */
	public static double[][] getAdjacencyMatrix(AbstractWeightedUndirectedGraph graph) {
		int n = graph.getNumVertices();
		double[][] matrix = new double[n][n];
		
		for (IntIntPair edge: graph) {
			int i = edge.getFirst();
			int j = edge.getSecond();
			double w = graph.getWeight(i, j);
			matrix[i][j] = w;
			matrix[j][i] = w;
		}
		
		return matrix;
	}
	
	/**
	 * This function computes the adjacency matrix of the given graph.
	 * 
	 * @param graph the graph to compute the adjacency matrix of
	 * @return the adjacency matrix of the graph
	 */
	public static DoubleSparseMatrix getSparseAdjacencyMatrix(AbstractWeightedUndirectedGraph graph) {
		int n = graph.getNumVertices();
		DoubleSparseMatrix matrix = new DoubleSparseMatrix(n, n);
		
		for (IntIntPair edge: graph) {
			int i = edge.getFirst();
			int j = edge.getSecond();
			double w = graph.getWeight(i, j);
			matrix.set(i, j, w);
			matrix.set(j, i, w);
		}
		
		return matrix;
	}
	
	/**
	 * This function computes the Laplacian matrix of a graph.
	 * 
	 * @param graph the graph
	 * @return the Laplacian matrix of the graph
	 */
	public static int[][] getLaplacianMatrix(AbstractUndirectedGraph graph) {
		int n = graph.getNumVertices();
		int[][] matrix = new int[n][n];
		int[] degrees = new int[n];
		
		for (IntIntPair edge: graph) {
			int i = edge.getFirst();
			int j = edge.getSecond();
			matrix[i][j] = -1;
			matrix[j][i] = -1;
			degrees[i]++;
			degrees[j]++;
		}
		
		for (int k = 0; k < n; k++) {
			matrix[k][k] = degrees[k];
		}
		
		return matrix;
	}
	
	/**
	 * This function computes the Laplacian matrix of a graph.
	 * 
	 * @param graph the graph
	 * @return the Laplacian matrix of the graph
	 */
	public static IntSparseMatrix getSparseLaplacianMatrix(AbstractUndirectedGraph graph) {
		int n = graph.getNumVertices();
		IntSparseMatrix matrix = new IntSparseMatrix(n, n);
		int[] degrees = new int[n];
		
		for (IntIntPair edge: graph) {
			int i = edge.getFirst();
			int j = edge.getSecond();
			matrix.set(i, j, -1);
			matrix.set(j, i, -1);
			degrees[i]++;
			degrees[j]++;
		}
		
		for (int k = 0; k < n; k++) {
			matrix.set(k, k, degrees[k]);
		}
		
		return matrix;
	}
	
	/**
	 * This function computes the Laplacian matrix of a graph.
	 * 
	 * @param graph the graph
	 * @return the Laplacian matrix of the graph
	 */
	public static double[][] getLaplacianMatrix(AbstractWeightedUndirectedGraph graph) {
		int n = graph.getNumVertices();
		double[][] matrix = new double[n][n];
		double[] degrees = new double[n];
		
		for (IntIntPair edge: graph) {
			int i = edge.getFirst();
			int j = edge.getSecond();
			double w = graph.getWeight(i, j);
			matrix[i][j] = -w;
			matrix[j][i] = -w;
			degrees[i] += w;
			degrees[j] += w;
		}
		
		for (int k = 0; k < n; k++) {
			matrix[k][k] = degrees[k];
		}
		
		return matrix;
	}
	
	/**
	 * This function computes the Laplacian matrix of a graph.
	 * 
	 * @param graph the graph
	 * @return the Laplacian matrix of the graph
	 */
	public static DoubleSparseMatrix getSparseLaplacianMatrix(AbstractWeightedUndirectedGraph graph) {
		int n = graph.getNumVertices();
		DoubleSparseMatrix matrix = new DoubleSparseMatrix(n, n);
		double[] degrees = new double[n];
		
		for (IntIntPair edge: graph) {
			int i = edge.getFirst();
			int j = edge.getSecond();
			double w = graph.getWeight(i, j);
			matrix.set(i, j, -w);
			matrix.set(j, i, -w);
			degrees[i] += w;
			degrees[j] += w;
		}
		
		for (int k = 0; k < n; k++) {
			matrix.set(k, k, degrees[k]);
		}
		
		return matrix;
	}
}
