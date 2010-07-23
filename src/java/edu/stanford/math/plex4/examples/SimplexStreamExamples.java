package edu.stanford.math.plex4.examples;

import edu.stanford.math.plex4.array_utility.ArrayGeneration;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.streams.impl.ExplicitStream;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.utility.ExceptionUtility;

/**
 * This class contains various functions for producing examples of 
 * filtered and static simplicial complexes. Note that we use the term
 * static complex to refer to the fact that all filtration values are zero.
 * 
 * @author Andrew Tausz
 *
 */
public class SimplexStreamExamples {
	/**
	 * This function returns a filtered simplicial complex which is shown in 
	 * Figure 1 of the paper "Computing Persistent Homology" by Zomorodian and Carlsson.
	 * 
	 * @return a filtered simplex stream
	 */
	public static ExplicitStream<Simplex> getZomorodianCarlssonExample() {
		ExplicitStream<Simplex> stream = new ExplicitStream<Simplex>(SimplexComparator.getInstance());

		stream.addElement(new Simplex(new int[]{0}), 0);
		stream.addElement(new Simplex(new int[]{1}), 0);

		stream.addElement(new Simplex(new int[]{2}), 1);
		stream.addElement(new Simplex(new int[]{3}), 1);
		stream.addElement(new Simplex(new int[]{0, 1}), 1);
		stream.addElement(new Simplex(new int[]{1, 2}), 1);

		stream.addElement(new Simplex(new int[]{2, 3}), 2);
		stream.addElement(new Simplex(new int[]{3, 0}), 2);

		stream.addElement(new Simplex(new int[]{0, 2}), 3);

		stream.addElement(new Simplex(new int[]{0, 1, 2}), 4);

		stream.addElement(new Simplex(new int[]{0, 2, 3}), 5);

		stream.finalizeStream();
		
		return stream;
	}
	
	public static ExplicitStream<Simplex> getFilteredTriangle() {
		ExplicitStream<Simplex> stream = new ExplicitStream<Simplex>(SimplexComparator.getInstance());

		stream.addElement(new Simplex(new int[]{1}), 1);
		stream.addElement(new Simplex(new int[]{2}), 2);
		stream.addElement(new Simplex(new int[]{3}), 3);
		stream.addElement(new Simplex(new int[]{1, 2}), 4);
		stream.addElement(new Simplex(new int[]{2, 3}), 5);
		stream.addElement(new Simplex(new int[]{1, 3}), 6);
		stream.addElement(new Simplex(new int[]{1, 2, 3}), 7);

		stream.finalizeStream();
		
		return stream;
	}
	
	
	/**
	 * This function returns a simplicial triangle (which happens
	 * to be equal to a simplicial 1-sphere).
	 * 
	 * @return a simplicial triangle
	 */
	public static ExplicitStream<Simplex> getTriangle() {
		return getSimplicialSphere(1);
	}
	
	/**
	 * This function returns a simplicial tetrahedron (which happens
	 * to be equal to a simplicial 2-sphere).
	 * 
	 * @return a simplicial tetrahedron
	 */
	public static ExplicitStream<Simplex> getTetrahedron() {
		return getSimplicialSphere(2);
	}
	
	/**
	 * This function returns a static simplicial complex containing an n-sphere.
	 * 
	 * @param dimension the dimension of the sphere to produce
	 * @return a simplicial sphere
	 */
	public static ExplicitStream<Simplex> getSimplicialSphere(int dimension) {
		ExplicitStream<Simplex> stream = new ExplicitStream<Simplex>(SimplexComparator.getInstance());

		stream.addElement(new Simplex(ArrayGeneration.range(0, dimension + 2)), 0);
		stream.ensureAllFaces();
		stream.removeElementIfPresent(new Simplex(ArrayGeneration.range(0, dimension + 2)));
		stream.finalizeStream();
		
		return stream;
	}
	
	/**
	 * This function returns a simplicial 2-torus. This construction was taken from the 
	 * original version of Javaplex. Note that the complex returned is static meaning that
	 * all filtration values are zero.
	 * 
	 * @return a simplicial complex containing a 2-torus.
	 */
	public static ExplicitStream<Simplex> getTorus() {
		ExplicitStream<Simplex> stream = new ExplicitStream<Simplex>(SimplexComparator.getInstance());

		stream.addElement(new Simplex(new int[] {1}), 0);
		stream.addElement(new Simplex(new int[] {2}), 0);
		stream.addElement(new Simplex(new int[] {3}), 0);

		stream.addElement(new Simplex(new int[] {4}), 0);
		stream.addElement(new Simplex(new int[] {5}), 0);
		stream.addElement(new Simplex(new int[] {6}), 0);

		stream.addElement(new Simplex(new int[] {7}), 0);
		stream.addElement(new Simplex(new int[] {8}), 0);
		stream.addElement(new Simplex(new int[] {9}), 0);

		stream.addElement(new Simplex(new int[] {1, 2}), 0);
		stream.addElement(new Simplex(new int[] {1, 3}), 0);
		stream.addElement(new Simplex(new int[] {2, 3}), 0);

		stream.addElement(new Simplex(new int[] {1, 4}), 0);
		stream.addElement(new Simplex(new int[] {2, 5}), 0);
		stream.addElement(new Simplex(new int[] {3, 6}), 0);

		stream.addElement(new Simplex(new int[] {1, 6}), 0);
		stream.addElement(new Simplex(new int[] {2, 4}), 0);
		stream.addElement(new Simplex(new int[] {3, 5}), 0);

		stream.addElement(new Simplex(new int[] {4, 5}), 0);
		stream.addElement(new Simplex(new int[] {4, 6}), 0);
		stream.addElement(new Simplex(new int[] {5, 6}), 0);
		stream.addElement(new Simplex(new int[] {4, 7}), 0);
		stream.addElement(new Simplex(new int[] {4, 9}), 0);
		stream.addElement(new Simplex(new int[] {5, 7}), 0);
		stream.addElement(new Simplex(new int[] {5, 8}), 0);
		stream.addElement(new Simplex(new int[] {6, 8}), 0);
		stream.addElement(new Simplex(new int[] {6, 9}), 0);

		stream.addElement(new Simplex(new int[] {7, 9}), 0);
		stream.addElement(new Simplex(new int[] {8, 9}), 0);
		stream.addElement(new Simplex(new int[] {7, 1}), 0);
		stream.addElement(new Simplex(new int[] {7, 3}), 0);
		stream.addElement(new Simplex(new int[] {7, 8}), 0);
		stream.addElement(new Simplex(new int[] {8, 1}), 0);
		stream.addElement(new Simplex(new int[] {8, 2}), 0);
		stream.addElement(new Simplex(new int[] {9, 2}), 0);
		stream.addElement(new Simplex(new int[] {9, 3}), 0);

		stream.addElement(new Simplex(new int[] {1, 2, 4}), 0);
		stream.addElement(new Simplex(new int[] {2, 4, 5}), 0);
		stream.addElement(new Simplex(new int[] {2, 3, 5}), 0);
		stream.addElement(new Simplex(new int[] {3, 5, 6}), 0);
		stream.addElement(new Simplex(new int[] {1, 4, 6}), 0);
		stream.addElement(new Simplex(new int[] {1, 3, 6}), 0);

		stream.addElement(new Simplex(new int[] {4, 5, 7}), 0);
		stream.addElement(new Simplex(new int[] {5, 7, 8}), 0);
		stream.addElement(new Simplex(new int[] {5, 6, 8}), 0);
		stream.addElement(new Simplex(new int[] {6, 8, 9}), 0);
		stream.addElement(new Simplex(new int[] {4, 7, 9}), 0);
		stream.addElement(new Simplex(new int[] {4, 6, 9}), 0);

		stream.addElement(new Simplex(new int[] {7, 8, 1}), 0);
		stream.addElement(new Simplex(new int[] {8, 1, 2}), 0);
		stream.addElement(new Simplex(new int[] {8, 9, 2}), 0);
		stream.addElement(new Simplex(new int[] {9, 2, 3}), 0);
		stream.addElement(new Simplex(new int[] {7, 1, 3}), 0);
		stream.addElement(new Simplex(new int[] {7, 9, 3}), 0);

		stream.finalizeStream();
		
		return stream;
	}
	
	/**
	 * This function returns a simplicial circle with m vertices in it.
	 * It requires that m >= 3.
	 * 
	 * @param m the number of vertices in the circle
	 * @return a simplicial circle with m vertices
	 */
	public static ExplicitStream<Simplex> getCircle(int m) {
		ExceptionUtility.verifyGreaterThanOrEqual(m, 3);
		
		ExplicitStream<Simplex> stream = new ExplicitStream<Simplex>(SimplexComparator.getInstance());

		for (int i = 0; i < m; i++) {
			stream.addElement(new Simplex(new int[]{i}), 0);
			stream.addElement(new Simplex(new int[]{i, (i + 1) % m}), 0);
		}

		stream.finalizeStream();
		
		return stream;
	}	
}
