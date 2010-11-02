/**
 * 
 */
package edu.stanford.math.plex4.homology.chain_basis;

import java.util.Arrays;
import java.util.Collection;

import edu.stanford.math.plex4.homology.utility.HomologyUtility;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import edu.stanford.math.primitivelib.utility.CRC;

/**
 * This class implements the functionality of a cell within a CW complex.
 * A CW complex can be described as follows:
 * <ol>
 * <li>Start with a discrete set X_0 consisting of the 0-cells</li>
 * <li>Inductively form the n-skeleton by attaching n-cells e_a via the
 * maps phi_a: S^(n-1) -> X_{n-1} which map the boundary of e_a into the
 * n-1 skeleton of X.</li>
 * </ol>
 * 
 * <p>To define a cell within the CW complex, we require its dimensionality,
 * its boundary, and the degrees of the map attaching map followed by a 
 * quotient map. The user of this class is advised to be familiar with
 * cellular homology, as described in (for example) section 2.2 of the 
 * book "Algebraic Topology" by Allen Hatcher.</p>
 * 
 * <p>The required input degrees are the degrees of the composite map
 * S_a^{n-1}</p>
 * 
 * <p>For example, we will construct a torus using a cell complex.
 * <ul> 
 * <li>Start with 1 0-cell, called v.</li>
 * <li>Add 2 1-cells, a and b, each with boundary glued to the 0-cell. The degree
 * of such a mapping is zero.</li>
 * <li>Add a 2-cell with boundary elements (a, b, a, b), and degrees (1, -1, 1, -1)</li>
 * </ul>
 * This corresponds to the familiar construction of a square with opposite edges
 * identified.</p>
 * 
 * @author Andrew Tausz
 *
 */
public class Cell implements PrimitiveBasisElement {
	/**
	 * This holds the class-wide cell counter.
	 */
	private static int cellIdCounter;
	
	/**
	 * This array holds the contents of the boundary elements of the cell.
	 */
	private final PrimitiveBasisElement[] boundaryArray;
	
	/**
	 * This holds the cell ids of the boundary elements.
	 */
	private final int[] boundaryIds;
	
	/**
	 * This array holds the coefficients of the boundary. These are equal
	 * to the degrees of the composite attaching maps.
	 */
	private final int[] boundaryCoefficients;
	
	/**
	 * This holds the cached hash code to prevent recomputing.
	 */
	private final int cachedHashCode;
	
	/**
	 * Stores the dimension of the cell.
	 */
	private final int dimension;
	
	/**
	 * Stores the unique identifier of the cell.
	 */
	private final int cellId;
	
	/**
	 * This constructor initializes the cell to be a 0-cell (a vertex),
	 * with the default cellId as the vertex index.
	 * 
	 */
	public Cell() {
		this.boundaryArray = new PrimitiveBasisElement[0];
		this.boundaryCoefficients = new int[0];
		this.boundaryIds = new int[0];
		this.dimension = 0;
		this.cellId = cellIdCounter++;
		
		// precompute hashcode
		this.cachedHashCode = CRC.hash32(new int[]{this.cellId});
	}
	
	/**
	 * This constructor initializes a cell to be an n-cell with n > 0. Such a cell
	 * is specified by its boundary elements. The degrees of the attaching maps
	 * for this constructor are taken to be the default [1, -1, 1, -1, ...].
	 * 
	 * @param dimension the geometric dimension of the cell
	 * @param boundaryElements an array containing the objects in the boundary
	 */
	public Cell(int dimension, Collection<Cell> boundaryElements) {
		this(dimension, boundaryElements.toArray(new PrimitiveBasisElement[0]), HomologyUtility.getDefaultBoundaryCoefficients(boundaryElements.size()), getCellIds(boundaryElements));
	}
	
	/**
	 * This constructor initializes the cell to be an n-cell with n > 0. It allows the specification
	 * of the boundary elements as well as the attaching degrees.
	 * 
	 * @param dimension  the geometric dimension of the cell
	 * @param boundaryElements an array containing the objects in the boundary
	 * @param attachingDegrees the degrees of the attaching maps to the boundary objects
	 */
	public Cell(int dimension, Collection<Cell> boundaryElements, int[] attachingDegrees) {
		this(dimension, boundaryElements.toArray(new PrimitiveBasisElement[0]), attachingDegrees, getCellIds(boundaryElements));
	}
	
	/**
	 * This constructor initializes a cell to be an n-cell with n > 0. Such a cell
	 * is specified by its boundary elements. The degrees of the attaching maps
	 * for this constructor are taken to be the default [1, -1, 1, -1, ...].
	 * 
	 * @param dimension the geometric dimension of the cell
	 * @param boundaryElements an array containing the objects in the boundary
	 */
	public Cell(int dimension, Cell[] boundaryElements) {
		this(dimension, boundaryElements, HomologyUtility.getDefaultBoundaryCoefficients(boundaryElements.length), getCellIds(boundaryElements));
	}
	
	/**
	 * This constructor initializes the cell to be a n-cell with n > 0. Such a 
	 * cell is specified by its boundary elements, the degrees of the attaching
	 * maps to those elements and its dimension.
	 * 
	 * @param dimension the geometric dimension of the cell
	 * @param boundaryElements an array containing the objects in the boundary
	 * @param attachingDegrees the degrees of the attaching maps to the boundary objects
	 * @param boundaryIds the cell ids of the boundary elements
	 */
	private Cell(int dimension, PrimitiveBasisElement[] boundaryElements, int[] attachingDegrees, int[] boundaryIds) {
		ExceptionUtility.verifyNonNull(boundaryElements);
		ExceptionUtility.verifyEqual(boundaryElements.length, attachingDegrees.length);
		this.boundaryArray = boundaryElements;
		this.boundaryCoefficients = attachingDegrees;
		this.boundaryIds = boundaryIds;
		this.cellId = cellIdCounter++;
		this.dimension = dimension;
		this.cachedHashCode = this.precomputeHashCode();
	}
	
	/**
	 * This function produces an array containing the cell id's of the supplied
	 * collection of cells.
	 * 
	 * @param boundaryElements the collection of cells
	 * @return an array holding the cell id's
	 */
	private static int[] getCellIds(Collection<Cell> boundaryElements) {
		int[] id_array = new int[boundaryElements.size()];
		int i = 0;
		
		for (Cell cell: boundaryElements) {
			id_array[i] = cell.getCellId();
			i++;
		}
		
		return id_array;
	}
	
	/**
	 * This function produces an array containing the cell id's of the supplied
	 * array of cells
	 * 
	 * @param boundaryElements
	 * @return an array holding the cell id's
	 */
	private static int[] getCellIds(Cell[] boundaryElements) {
		int[] id_array = new int[boundaryElements.length];
		int i = 0;
		
		for (Cell cell: boundaryElements) {
			id_array[i] = cell.getCellId();
			i++;
		}
		
		return id_array;
	}

	/**
	 * This function returns the cell ids of the boundary elements.
	 * 
	 * @return an array containing the cell ids of the boundary elements.
	 */
	public int[] getBoundaryIds() {
		return this.boundaryIds;
	}
	
	/**
	 * This function returns the unique identifier of the cell.
	 * 
	 * @return the cellId of the cell
	 */
	public int getCellId() {
		return this.cellId;
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.chain_basis.PrimitiveBasisElement#getBoundaryArray()
	 */
	public PrimitiveBasisElement[] getBoundaryArray() {
		return this.boundaryArray;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.chain_basis.PrimitiveBasisElement#getDimension()
	 */
	public int getDimension() {
		return this.dimension;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.chain_basis.PrimitiveBasisElement#getBoundaryCoefficients()
	 */
	public int[] getBoundaryCoefficients() {
		return this.boundaryCoefficients;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ("[" + Integer.toString(this.cellId) + "]");
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return this.cachedHashCode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
	
	/**
	 * This function checks to see if the cell has been glued to boundary
	 * elements all of the same dimension.
	 */
	public void verifyDimension() {
		if (this.boundaryArray.length == 0) {
			return;
		}
		int boundaryDimension = this.boundaryArray[0].getDimension();
		for(int i = 1; i < this.boundaryArray.length; i++) {
			if (this.boundaryArray[i].getDimension() != boundaryDimension) {
				throw new IllegalArgumentException("Cell boundary elements are of unequal dimension.");
			}
		}
	}
	
	/**
	 * This function precomputes the hash code of the object. Note that this
	 * is acceptable to do since the object is completely immutable - all fields
	 * are final.
	 * 
	 * @return the hashcode of the cell
	 */
	private int precomputeHashCode() {
		int[] boundaryHashCodes = new int[this.boundaryArray.length + 1];

		for (int i = 0; i < this.boundaryArray.length; i++) {
			boundaryHashCodes[i] = this.boundaryArray[i].hashCode();
		}
		boundaryHashCodes[this.boundaryArray.length] = this.cellId;
		return CRC.hash32(boundaryHashCodes);
	}
}
