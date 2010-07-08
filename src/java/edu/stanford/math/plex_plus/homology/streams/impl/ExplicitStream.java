/**
 * 
 */
package edu.stanford.math.plex_plus.homology.streams.impl;

import java.util.Comparator;

import edu.stanford.math.plex_plus.homology.chain_basis.PrimitiveBasisElement;
import edu.stanford.math.plex_plus.homology.streams.interfaces.PrimitiveStream;

/**
 * This class implements the functionality of a user-defined filtered chain complex.
 * It derives from the PrimitiveStream class, meaning that it underlying basis type 
 * must be a geometric primitive.
 * 
 * @author Andrew Tausz
 *
 */

public class ExplicitStream<T extends PrimitiveBasisElement> extends PrimitiveStream<T> {

	/**
	 * Constructor which accepts a comparator for comparing the type T.
	 * This comparator defines the ordering on the type T. Thus the overall
	 * filtered objects are sorted first in order of filtration, and then by
	 * the ordering provided by the comparator. 
	 * 
	 * @param comparator a Comparator which provides an ordering of the objects
	 */
	public ExplicitStream(Comparator<T> comparator) {
		super(comparator);
	}
	
	/**
	 * This function adds a basis element the stream with specified filtration value.
	 * 
	 * @param basisElement the basis element to add
	 * @param filtrationValue the filtration value of the basis element
	 */
	public void addElement(T basisElement, double filtrationValue) {
		this.storageStructure.addElement(basisElement, filtrationValue);
	}

	@Override
	protected void constructComplex() {	}
	
	public void ensureAllFaces() {
		for (T basisElement: this.storageStructure) {
			double filtrationValue = this.getFiltrationValue(basisElement);

			// get the boundary
			T[] boundary = this.getBoundary(basisElement);

			// make sure that each boundary element is also inside the
			// complex with a filtration value less than or equal to the
			// current simplex
			for (T face: boundary) {
				
			}
		}
	}
	
	public void ensureFace(T basisElement) {
		T[] boundary = this.getBoundary(basisElement);
	}
}
