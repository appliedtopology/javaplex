/**
 * 
 */
package edu.stanford.math.plex_plus.homology.streams.impl;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

import edu.stanford.math.plex_plus.homology.chain_basis.PrimitiveBasisElement;
import edu.stanford.math.plex_plus.homology.streams.interfaces.PrimitiveStream;
import gnu.trove.iterator.TObjectDoubleIterator;
import gnu.trove.map.hash.TObjectDoubleHashMap;

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

	/**
	 * This function removes the specified element from the stream. 
	 * In the case that the element is not present, this function does not do
	 * anything.
	 * 
	 * @param basisElement the element to remove.
	 * @return true if the element was found and removed, and false otherwise
	 */
	public boolean removeElementIfPresent(T basisElement) {
		if (this.storageStructure.containsElement(basisElement)) {
			// remove the element if present
			this.storageStructure.removeElement(basisElement);
			return true;
		}
		return false;
	}
	
	@Override
	protected void constructComplex() {	}
	
	/**
	 * This function ensures that all of the faces of the all of the elements are
	 * present in the stream. It adds the missing faces with filtration values that
	 * are consistent. In other words, it ensures that the filtration value of an
	 * element is less than or equal to the filtration values of its cofaces.
	 */
	public void ensureAllFaces() {
		Queue<T> elementQueue = new LinkedList<T>();
		TObjectDoubleHashMap<T> newElements = new TObjectDoubleHashMap<T>();
		
		for (T basisElement: this.storageStructure) {
			// add element to the queue
			elementQueue.add(basisElement);
		}
		
		while (!elementQueue.isEmpty()) {
			T basisElement = elementQueue.remove();
			double elementFiltrationValue = this.getFiltrationValue(basisElement);
			T[] boundary = this.getBoundary(basisElement);
			
			for (T face: boundary) {
				if (this.storageStructure.containsElement(face)) {
					// do nothing
				} else {
					if (newElements.containsKey(face)) {
						newElements.adjustValue(face, Math.min(elementFiltrationValue, newElements.get(face)));
					} else {
						newElements.put(face, elementFiltrationValue);
						if (!elementQueue.contains(face)) {
							elementQueue.add(face);
						}
					}
				}
			}
		}
		
		for (TObjectDoubleIterator<T> iterator = newElements.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			this.addElement(iterator.key(), iterator.value());
		}
	}
	
	public String toString() {
		return this.storageStructure.toString();
	}
}
