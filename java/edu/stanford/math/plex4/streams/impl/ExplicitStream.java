/**
 * 
 */
package edu.stanford.math.plex4.streams.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.stanford.math.plex4.homology.chain_basis.PrimitiveBasisElement;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.streams.interfaces.PrimitiveStream;
import gnu.trove.TObjectIntHashMap;
import gnu.trove.TObjectIntIterator;

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
	 * This constructor initializes the stream from an existing stream. It copies all
	 * of the elements of the supplied stream to the current one.
	 * 
	 * @param stream the stream to copy from
	 * @param comparator a Comparator which provides an ordering on the basis elements
	 */
	public ExplicitStream(AbstractFilteredStream<T> stream, Comparator<T> comparator) {
		super(comparator);
		this.addAllElements(stream);
	}
	
	/**
	 * This function copies all of the elements in the supplied stream to the current one.
	 * 
	 * @param stream the stream to copy from
	 */
	public void addAllElements(AbstractFilteredStream<T> stream) {
		for (T element: stream) {
			this.storageStructure.addElement(element, stream.getFiltrationIndex(element));
		}
	}
	
	/**
	 * This function adds a basis element the stream with specified filtration index.
	 * 
	 * @param basisElement the basis element to add
	 * @param filtrationIndex the filtration index of the basis element
	 */
	public void addElement(T basisElement, int filtrationIndex) {
		this.storageStructure.addElement(basisElement, filtrationIndex);
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
	 * present in the stream. It adds the missing faces with filtration indices that
	 * are consistent. In other words, it ensures that the filtration indices of an
	 * element is less than or equal to the filtration values of its cofaces.
	 */
	public void ensureAllFaces() {
		List<T> elementQueue = new ArrayList<T>();
		TObjectIntHashMap<T> newElements = new TObjectIntHashMap<T>();
		
		for (T basisElement: this.storageStructure) {
			// add element to the queue
			elementQueue.add(basisElement);
		}
		
		while (!elementQueue.isEmpty()) {
			T basisElement = elementQueue.remove(elementQueue.size() - 1);
			int elementFiltrationIndex = this.getFiltrationIndex(basisElement);
			T[] boundary = this.getBoundary(basisElement);
			
			for (T face: boundary) {
				if (this.storageStructure.containsElement(face)) {
					// do nothing
				} else {
					if (newElements.containsKey(face)) {
						newElements.adjustValue(face, Math.min(elementFiltrationIndex, newElements.get(face)));
					} else {
						newElements.put(face, elementFiltrationIndex);
						if (!elementQueue.contains(face)) {
							elementQueue.add(face);
						}
					}
				}
			}
		}
		
		for (TObjectIntIterator<T> iterator = newElements.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			this.addElement(iterator.key(), iterator.value());
		}
	}
}
