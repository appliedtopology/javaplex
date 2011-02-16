package edu.stanford.math.plex4.streams.utility;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.math.plex4.graph.AbstractUndirectedGraph;
import edu.stanford.math.plex4.graph.UndirectedListGraph;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.utility.HomologyUtility;
import edu.stanford.math.plex4.streams.derived.TensorStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.autogen.formal_sum.DoublePrimitiveFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.DoubleSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntMatrixConverter;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntPrimitiveFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.matrix.IntSparseMatrix;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;

/**
 * This class contains various static functions for querying and manipulating filtered chain complexes.
 * 
 * @author Andrew Tausz
 *
 */
public class StreamUtility {
	
	/**
	 * This function dumps the k-skeleton of a chain complex into a list.
	 * 
	 * @param <T> the type of the basis elements
	 * @param stream the stream to search
	 * @param k the dimension of the objects to retrieve
	 * @return the objects with the requested dimension
	 */
	public static <T> List<T> getSkeleton(AbstractFilteredStream<T> stream, int k) {
		List<T> skeleton = new ArrayList<T>();
		
		for (T basisElement: stream) {
			int elementDimension = stream.getDimension(basisElement);
			if (elementDimension == k) {
				skeleton.add(basisElement);
			}
		}
		
		return skeleton;
	}
	
	/**
	 * This function computes the size of the k-skeleton of a chain complex.
	 * 
	 * @param <T> the type of the basis elements
	 * @param stream the stream to search
	 * @param k the dimension of the objects to retrieve
	 * @return the size of the k-seleton of the complex
	 */
	public static <T> int getSkeletonSize(AbstractFilteredStream<T> stream, int k) {
		int size = 0;
		
		for (T basisElement: stream) {
			if (stream.getDimension(basisElement) == k) {
				size++;
			}
		}
		
		return size;
	}
	
	/**
	 * This function returns the boundary matrix for the entire complex.
	 * 
	 * @param <T> the underlying basis type
	 * @param stream the complex
	 * @return the boundary matrix as an IntSparseFormalSum
	 */
	public static <T> IntSparseFormalSum<ObjectObjectPair<T, T>> createBoundaryMatrixAsIntSum(AbstractFilteredStream<T> stream) {
		IntPrimitiveFreeModule<ObjectObjectPair<T, T>> chainModule = new IntPrimitiveFreeModule<ObjectObjectPair<T, T>>();
		IntSparseFormalSum<ObjectObjectPair<T, T>> sum = new IntSparseFormalSum<ObjectObjectPair<T, T>>();
		for (T basisElement: stream) {
			int[] boundaryCoefficients = stream.getBoundaryCoefficients(basisElement);
			T[] boundaryElements = stream.getBoundary(basisElement);
			for (int i = 0; i < boundaryElements.length; i++) {
				chainModule.accumulate(sum, new ObjectObjectPair<T, T>(boundaryElements[i], basisElement), boundaryCoefficients[i]);
			}
		}
		return sum;
	}
	
	/**
	 * This function returns the boundary matrix for the entire complex.
	 * 
	 * @param <T> the underlying basis type
	 * @param stream the complex
	 * @return the boundary matrix as a DoubleSparseFormalSum
	 */
	public static <T> DoubleSparseFormalSum<ObjectObjectPair<T, T>> createBoundaryMatrixAsDoubleSum(AbstractFilteredStream<T> stream) {
		DoublePrimitiveFreeModule<ObjectObjectPair<T, T>> chainModule = new DoublePrimitiveFreeModule<ObjectObjectPair<T, T>>();
		DoubleSparseFormalSum<ObjectObjectPair<T, T>> sum = new DoubleSparseFormalSum<ObjectObjectPair<T, T>>();
		for (T basisElement: stream) {
			int[] boundaryCoefficients = stream.getBoundaryCoefficients(basisElement);
			T[] boundaryElements = stream.getBoundary(basisElement);
			for (int i = 0; i < boundaryElements.length; i++) {
				chainModule.accumulate(sum, new ObjectObjectPair<T, T>(boundaryElements[i], basisElement), boundaryCoefficients[i]);
			}
		}
		return sum;
	}
	
	/**
	 * This function returns the boundary matrix for the entire complex.
	 * 
	 * @param <T> the underlying basis type
	 * @param stream the complex
	 * @param dimension the dimension to get the boundary for
	 * @return the boundary matrix as an IntSparseFormalSum
	 */
	public static <T> IntSparseFormalSum<ObjectObjectPair<T, T>> createBoundaryMatrixAsIntSum(AbstractFilteredStream<T> stream, int dimension) {
		IntPrimitiveFreeModule<ObjectObjectPair<T, T>> chainModule = new IntPrimitiveFreeModule<ObjectObjectPair<T, T>>();
		IntSparseFormalSum<ObjectObjectPair<T, T>> sum = new IntSparseFormalSum<ObjectObjectPair<T, T>>();
		for (T basisElement: stream) {
			int elementDimension = stream.getDimension(basisElement);
			if (elementDimension != dimension) {
				continue;
			}
			int[] boundaryCoefficients = stream.getBoundaryCoefficients(basisElement);
			T[] boundaryElements = stream.getBoundary(basisElement);
			for (int i = 0; i < boundaryElements.length; i++) {
				chainModule.accumulate(sum, new ObjectObjectPair<T, T>(boundaryElements[i], basisElement), boundaryCoefficients[i]);
			}
		}
		return sum;
	}
	
	/**
	 * This function returns the boundary matrix for the entire complex.
	 * 
	 * @param <T> the underlying basis type
	 * @param stream the complex
	 * @param dimension the dimension to get the boundary for
	 * @return the boundary matrix as a DoubleSparseFormalSum
	 */
	public static <T> DoubleSparseFormalSum<ObjectObjectPair<T, T>> createBoundaryMatrixAsDoubleSum(AbstractFilteredStream<T> stream, int dimension) {
		DoublePrimitiveFreeModule<ObjectObjectPair<T, T>> chainModule = new DoublePrimitiveFreeModule<ObjectObjectPair<T, T>>();
		DoubleSparseFormalSum<ObjectObjectPair<T, T>> sum = new DoubleSparseFormalSum<ObjectObjectPair<T, T>>();
		for (T basisElement: stream) {
			int elementDimension = stream.getDimension(basisElement);
			if (elementDimension != dimension) {
				continue;
			}
			int[] boundaryCoefficients = stream.getBoundaryCoefficients(basisElement);
			T[] boundaryElements = stream.getBoundary(basisElement);
			for (int i = 0; i < boundaryElements.length; i++) {
				chainModule.accumulate(sum, new ObjectObjectPair<T, T>(boundaryElements[i], basisElement), boundaryCoefficients[i]);
			}
		}
		return sum;
	}
	
	/**
	 * This function returns the boundary matrix at the specified dimension as a list of columns
	 * 
	 * @param <T> the underlying basis type
	 * @param stream the complex
	 * @param dimension the dimension to get the boundary for
	 * @return the boundary matrix as a list of formal sums
	 */
	public static <T> List<IntSparseFormalSum<T>> getBoundaryMatrixColumns(AbstractFilteredStream<T> stream, int dimension) {
		List<IntSparseFormalSum<T>> boundaryMatrixColumns = new ArrayList<IntSparseFormalSum<T>>();
		IntPrimitiveFreeModule<T> chainModule = new IntPrimitiveFreeModule<T>();
		
		for (T basisElement: stream) {
			int elementDimension = stream.getDimension(basisElement);
			if (elementDimension == dimension) {
				boundaryMatrixColumns.add(chainModule.createNewSum(stream.getBoundaryCoefficients(basisElement), stream.getBoundary(basisElement)));
			}
		}
		
		return boundaryMatrixColumns;
	}
	
	/**
	 * This function produces a list of formal sums which are the columns of the boundary 
	 * matrix at the requested dimension.
	 * 
	 * @param <R> the type of the ring in which to compute
	 * @param <T> the type of the basis elements
	 * @param stream the stream to perform the operation on
	 * @param dimension the dimension
	 * @param chainModule the module which performs the algebraic operations (forming the sums)
	 * @return the columns of the boundary matrix at the request dimension
	 */
	public static <R, T> List<ObjectSparseFormalSum<R, T>> getBoundaryMatrixColumns(AbstractFilteredStream<T> stream, int dimension, ObjectAlgebraicFreeModule<R, T> chainModule) {
		List<ObjectSparseFormalSum<R, T>> boundaryMatrixColumns = new ArrayList<ObjectSparseFormalSum<R, T>>();
		
		for (T basisElement: stream) {
			int elementDimension = stream.getDimension(basisElement);
			if (elementDimension == dimension) {
				boundaryMatrixColumns.add(chainModule.createNewSum(stream.getBoundaryCoefficients(basisElement), stream.getBoundary(basisElement)));
			}
		}
		
		return boundaryMatrixColumns;
	}
	
	/**
	 * This function returns a graph containing the 1-skeleton of a given filtered simplicial complex. It only adds
	 * edges whose filtration index is greater than the specified value.
	 * 
	 * @param stream the stream to perform the operation on
	 * @param minFiltrationIndex the minimum filtration index
	 * @return the 1-skeleton of the complex in the form of an undirected graph
	 */
	public static UndirectedListGraph getNeighborhoodGraph(AbstractFilteredStream<Simplex> stream, int minFiltrationIndex) {
		int maxVertexIndex = Integer.MIN_VALUE;
		
		for (Simplex basisElement: stream) {
			int elementDimension = stream.getDimension(basisElement);
			if (elementDimension == 0 && (maxVertexIndex < basisElement.getVertices()[0])) {
				maxVertexIndex = basisElement.getVertices()[0];
			}
		}
		
		UndirectedListGraph graph = new UndirectedListGraph(maxVertexIndex + 1);
		
		for (Simplex basisElement: stream) {
			int elementDimension = stream.getDimension(basisElement);
			if (elementDimension == 1 && (stream.getFiltrationIndex(basisElement) >= minFiltrationIndex)) {
				int[] vertices = basisElement.getVertices();
				int i = vertices[0];
				int j = vertices[1];
				graph.addEdge(i, j);
			}
		}
		
		return graph;
	}
	
	/**
	 * This function returns the 1-skeleton of a filtered simplicial complex in the form of an undirected graph.
	 * 
	 * @param stream the stream to perform the operation on
	 * @return the 1-skeleton of the complex
	 */
	public static AbstractUndirectedGraph getNeighborhoodGraph(AbstractFilteredStream<Simplex> stream) {
		return getNeighborhoodGraph(stream, 0);
	}
	
	public static IntSparseMatrix createAlexanderWhitneyMatrix(AbstractFilteredStream<Simplex> stream) {
		TensorStream<Simplex, Simplex> tensorStream = new TensorStream<Simplex, Simplex>(stream, stream);
		IntMatrixConverter<Simplex, ObjectObjectPair<Simplex, Simplex>> matrixConverter = new IntMatrixConverter<Simplex, ObjectObjectPair<Simplex, Simplex>>(stream, tensorStream);
		IntSparseMatrix matrix = new IntSparseMatrix(tensorStream.getSize(), stream.getSize());
		
		for (Simplex simplex: stream) {
			List<ObjectObjectPair<Simplex, Simplex>> awMap = alexanderWhitneyDiagonal(simplex);
			int col = matrixConverter.getDomainRepresentation().getIndex(simplex);
			for (ObjectObjectPair<Simplex, Simplex> codomainElement : awMap) {
				int row = matrixConverter.getCodomainRepresentation().getIndex(codomainElement);
				matrix.set(row, col, 1);
			}
		}
		
		return matrix;
	}
	
	/**
	 * This function computes the Alexander-Whitney diagonal map of a given simplex defined by
	 * Delta([v_0, ... v_n]) = sum_i [v_0, ... v_i] tensor [v_i, ... v_n]
	 * 
	 * @param simplex the simplex argument
	 * @return the Alexander-Whitney diagonal map of the given simplex
	 */
	public static List<ObjectObjectPair<Simplex, Simplex>> alexanderWhitneyDiagonal(Simplex simplex) {
		List<ObjectObjectPair<Simplex, Simplex>> result = new ArrayList<ObjectObjectPair<Simplex, Simplex>>();
		int[] vertices = simplex.getVertices();
		
		for (int i = 0; i < vertices.length; i++) {
			result.add(new ObjectObjectPair<Simplex, Simplex>(new Simplex(HomologyUtility.lowerEntries(vertices, i)), new Simplex(HomologyUtility.upperEntries(vertices, i))));
		}
		
		return result;
	}
}
