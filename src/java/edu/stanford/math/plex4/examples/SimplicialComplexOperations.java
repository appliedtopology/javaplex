package edu.stanford.math.plex4.examples;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.utility.HomologyUtility;
import edu.stanford.math.plex4.streams.impl.ExplicitSimplexStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.autogen.array.IntArrayMath;

/**
 * This class contains various functions for creating new simplicial complexes out of old ones.
 * 
 * @author Andrew Tausz
 *
 */
public class SimplicialComplexOperations {
	/**
	 * This function computes the cone of a given filtered simplicial complex.
	 * 
	 * @param simplexStream the simplicial complex to compute the cone of
	 * @param vertexFiltrationIndex the filtration index of the vertex point of the cone
	 * @return the cone of the simplicial complex
	 */
	public static AbstractFilteredStream<Simplex> computeCone(AbstractFilteredStream<Simplex> simplexStream, int vertexFiltrationIndex) {
		ExplicitSimplexStream coneStream = new ExplicitSimplexStream();
		int maxFiltrationIndex = Integer.MIN_VALUE;
		int maxVertexIndex = Integer.MIN_VALUE;
		
		// add the existing simplices
		for (Simplex simplex: simplexStream) {
			int filtrationIndex = simplexStream.getFiltrationIndex(simplex);
			coneStream.addElement(simplex, filtrationIndex);
			if (simplex.getDimension() == 0 && (simplex.getVertices()[0] > maxVertexIndex)) {
				maxVertexIndex = simplex.getVertices()[0];
			}
			if (filtrationIndex > maxFiltrationIndex) {
				maxFiltrationIndex = filtrationIndex;
			}
		}
		
		// add the new cone vertex
		int coneVertexIndex = maxVertexIndex + 1;
		coneStream.addVertex(coneVertexIndex, vertexFiltrationIndex);
		
		// add the coned simplices
		for (Simplex simplex: simplexStream) {
			int filtrationIndex = simplexStream.getFiltrationIndex(simplex);
			int[] vertices = simplex.getVertices();
			int[] newVertices = HomologyUtility.appendToArray(vertices, coneVertexIndex);
			coneStream.addElement(newVertices, Math.max(filtrationIndex, vertexFiltrationIndex));
		}
		
		return coneStream;
	}
	
	/**
	 * This function computes the suspension of a given filtered simplicial complex.
	 * 
	 * @param simplexStream the simplicial complex to compute the suspension of
	 * @param vertexFiltrationIndex the filtration index of the two newly added vertices
	 * @return the suspension of the given simplicial complex
	 */
	public static AbstractFilteredStream<Simplex> computeSuspension(AbstractFilteredStream<Simplex> simplexStream, int vertexFiltrationIndex) {
		ExplicitSimplexStream suspensionStream = new ExplicitSimplexStream();
		int maxFiltrationIndex = Integer.MIN_VALUE;
		int maxVertexIndex = Integer.MIN_VALUE;
		
		// add the existing simplices
		for (Simplex simplex: simplexStream) {
			int filtrationIndex = simplexStream.getFiltrationIndex(simplex);
			suspensionStream.addElement(simplex, filtrationIndex);
			if (simplex.getDimension() == 0 && (simplex.getVertices()[0] > maxVertexIndex)) {
				maxVertexIndex = simplex.getVertices()[0];
			}
			if (filtrationIndex > maxFiltrationIndex) {
				maxFiltrationIndex = filtrationIndex;
			}
		}
		
		// add the new 2 new vertices
		int coneVertexIndex1 = maxVertexIndex + 1;
		int coneVertexIndex2 = coneVertexIndex1 + 1;
		suspensionStream.addVertex(coneVertexIndex1, vertexFiltrationIndex);
		suspensionStream.addVertex(coneVertexIndex2, vertexFiltrationIndex);
		
		// add the coned simplices on both sides
		for (Simplex simplex: simplexStream) {
			int filtrationIndex = simplexStream.getFiltrationIndex(simplex);
			int[] vertices = simplex.getVertices();
			
			int[] newVertices1 = HomologyUtility.appendToArray(vertices, coneVertexIndex1);
			suspensionStream.addElement(newVertices1, Math.max(filtrationIndex, vertexFiltrationIndex));
			
			int[] newVertices2 = HomologyUtility.appendToArray(vertices, coneVertexIndex2);
			suspensionStream.addElement(newVertices2, Math.max(filtrationIndex, vertexFiltrationIndex));
		}
		
		return suspensionStream;
	}
	
	/**
	 * This function computes the disjoint union of two filtered simplicial complex.
	 * 
	 * @param stream1 the first simplicial complex
	 * @param stream2 the second simplicial complex
	 * @return the disjoint union of the two simplicial complexes
	 */
	public static AbstractFilteredStream<Simplex> computeDisjointUnion(AbstractFilteredStream<Simplex> stream1, AbstractFilteredStream<Simplex> stream2) {
		ExplicitSimplexStream unionStream = new ExplicitSimplexStream();
		
		int maxFiltrationIndex = Integer.MIN_VALUE;
		int maxVertexIndex = Integer.MIN_VALUE;
		
		// add the simplices from the first streams
		for (Simplex simplex: stream1) {
			int filtrationIndex = stream1.getFiltrationIndex(simplex);
			unionStream.addElement(simplex, filtrationIndex);
			if (simplex.getDimension() == 0 && (simplex.getVertices()[0] > maxVertexIndex)) {
				maxVertexIndex = simplex.getVertices()[0];
			}
			if (filtrationIndex > maxFiltrationIndex) {
				maxFiltrationIndex = filtrationIndex;
			}
		}
		
		int translation = maxVertexIndex + 1;
		
		// add the simplices from the second stream - make sure to translated indices
		for (Simplex simplex: stream2) {
			int filtrationIndex = stream2.getFiltrationIndex(simplex);
			int[] vertices = simplex.getVertices();
			unionStream.addElement(IntArrayMath.scalarAdd(vertices, translation), filtrationIndex);
		}
		
		return unionStream;
	}
}
