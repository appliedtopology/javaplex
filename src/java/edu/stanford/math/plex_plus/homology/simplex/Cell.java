/**
 * 
 */
package edu.stanford.math.plex_plus.homology.simplex;

import java.util.Arrays;
import java.util.Collection;

import edu.stanford.math.plex_plus.utility.CRC;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * @author Andrew Tausz
 *
 */
public class Cell implements ChainBasisElement {
	private final ChainBasisElement[] boundaryArray;
	private final int cachedHashCode;
	private final int dimension;
	private final int cellId;
	
	/**
	 * This constructor initializes the cell to be a 0-cell (a vertex),
	 * with the specified cellId as the vertex index.
	 * 
	 * @param cellId the index of the vertex
	 */
	public Cell(int cellId) {
		this.boundaryArray = new ChainBasisElement[0];
		this.dimension = 0;
		this.cellId = cellId;
		
		// precompute hashcode
		this.cachedHashCode = CRC.hash32(new int[]{this.cellId});
	}
	
	/**
	 * This constructor initializes the cell to be a n-cell with n > 0. Such
	 * a cell is specified by its boundary elements, as well as a unique cellId
	 * since two distinct cells can have the same boundary.
	 * 
	 * @param cellId
	 * @param boundaryElements
	 */
	public Cell(int cellId, int dimension, Collection<? extends ChainBasisElement> boundaryElements) {
		ExceptionUtility.verifyNonNull(boundaryElements);
		//ExceptionUtility.verifyNonEmpty(boundaryElements);
		this.boundaryArray = boundaryElements.toArray(new ChainBasisElement[0]);
		this.cellId = cellId;
		this.dimension = dimension;
		this.cachedHashCode = this.precomputeHashCode();
	}
	
	public Cell(int cellId, int dimension, Cell[] boundaryElements) {
		ExceptionUtility.verifyNonNull(boundaryElements);
		//ExceptionUtility.verifyPositive(boundaryElements.length);
		this.boundaryArray = boundaryElements;
		this.cellId = cellId;
		this.dimension = dimension;
		this.cachedHashCode = this.precomputeHashCode();
	}

	public int getCellId() {
		return this.cellId;
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.homology.simplex.AbstractSimplex#getBoundaryArray()
	 */
	@Override
	public ChainBasisElement[] getBoundaryArray() {
		return this.boundaryArray;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex_plus.homology.simplex.AbstractSimplex#getDimension()
	 */
	@Override
	public int getDimension() {
		return this.dimension;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.cachedHashCode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Cell))
			return false;
		Cell other = (Cell) obj;
		if (!Arrays.equals(boundaryArray, other.boundaryArray))
			return false;
		if (cachedHashCode != other.cachedHashCode)
			return false;
		if (cellId != other.cellId)
			return false;
		if (dimension != other.dimension)
			return false;
		return true;
	}
	
	private int verifyDimension() {
		int boundaryDimension = this.boundaryArray[0].getDimension();
		for(int i = 1; i < this.boundaryArray.length; i++) {
			if (this.boundaryArray[i].getDimension() != boundaryDimension) {
				throw new IllegalArgumentException("Cell boundary elements are of unequal dimension.");
			}
		}
		return boundaryDimension + 1;
	}
	
	private int precomputeHashCode() {
		int[] boundaryHashCodes = new int[this.boundaryArray.length + 1];

		for (int i = 0; i < this.boundaryArray.length; i++) {
			boundaryHashCodes[i] = this.boundaryArray[i].hashCode();
		}
		boundaryHashCodes[this.boundaryArray.length] = this.cellId;
		return CRC.hash32(boundaryHashCodes);
	}
}
