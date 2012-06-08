package edu.stanford.math.plex4.streams.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.stanford.math.plex4.homology.chain_basis.Cell;
import edu.stanford.math.plex4.homology.chain_basis.CellComparator;
import gnu.trove.TIntObjectHashMap;

/**
 * This class is a wrapper for the ExplicitStream class and provides 
 * convenience functions when the base elements are cells.
 * 
 * @author Andrew Tausz
 *
 */
public class ExplicitCellStream extends ExplicitStream<Cell> {
	
	/**
	 * This maps cell ids to cell objects.
	 */
	private TIntObjectHashMap<Cell> indexCellMap = new TIntObjectHashMap<Cell>();
	
	/**
	 * Default constructor.
	 */
	public ExplicitCellStream() {
		super(CellComparator.getInstance());
	}
	
	public ExplicitCellStream(double maxFiltrationValue) {
		super(CellComparator.getInstance(), maxFiltrationValue);
	}

	/**
	 * This adds a new vertex to the cell complex.
	 * 
	 * @return the cell id of the newly added vertex
	 */
	public int addNewVertex() {
		Cell cell = new Cell();
		this.indexCellMap.put(cell.getCellId(), cell);
		this.storageStructure.addElement(cell, 0);
		return cell.getCellId();
	}
	
	/**
	 * This attaches a n-cell to an existing point in the complex.
	 * 
	 * @param dimension the dimension of the cell to add
	 * @param pointIndex the point to attach the boundary to
	 * @return the cell id of the newly added cell
	 */
	public int attachNewCellToPoint(int dimension, int pointIndex) {
		Cell cell = new Cell(dimension, this.getCellsByIds(new int[]{pointIndex}), new int[]{0});
		this.indexCellMap.put(cell.getCellId(), cell);
		this.storageStructure.addElement(cell, 0);
		return cell.getCellId();
	}
	
	/**
	 * This adds a new cell to the complex with boundary specified by the supplied
	 * array of cell ids. It uses the standard attaching coefficients.
	 * 
	 * @param dimension the dimension of the cell to add
	 * @param boundaryIndices an array of cell ids to attach to
	 * @return the cell id of the newly added cell
	 */
	public int attachNewCell(int dimension, int[] boundaryIndices) {
		Cell cell = new Cell(dimension, this.getCellsByIds(boundaryIndices));
		this.indexCellMap.put(cell.getCellId(), cell);
		this.storageStructure.addElement(cell, 0);
		return cell.getCellId();
	}
	
	/**
	 * This adds a new cell to the complex with boundary and boundary coefficients given.
	 * 
	 * @param dimension the dimension fo the cell to add
	 * @param boundaryIndices an array of cell ids to attach to
	 * @param boundaryCoefficients the array of attaching map degrees
	 * @return the cell id of the newly added cell
	 */
	public int attachNewCell(int dimension, int[] boundaryIndices, int[] boundaryCoefficients) {
		Cell cell = new Cell(dimension, this.getCellsByIds(boundaryIndices), boundaryCoefficients);
		this.indexCellMap.put(cell.getCellId(), cell);
		this.storageStructure.addElement(cell, 0);
		return cell.getCellId();
	}
	
	@Override
	public void addElement(Cell basisElement, double filtrationValue) {
		super.addElement(basisElement, filtrationValue);
		this.indexCellMap.put(basisElement.getCellId(), basisElement);
	}
	
	/**
	 * This obtains a collection of cell objects from an array of cell ids
	 * @param cellIds
	 * @return a collection of cells with the given cell ids
	 */
	private Collection<Cell> getCellsByIds(int[] cellIds) {
		List<Cell> cells = new ArrayList<Cell>();
		
		for (int index: cellIds) {
			cells.add(this.indexCellMap.get(index));
		}
		
		return cells;
	}
}
