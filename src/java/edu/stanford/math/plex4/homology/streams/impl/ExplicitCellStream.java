package edu.stanford.math.plex4.homology.streams.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.stanford.math.plex4.homology.chain_basis.Cell;
import edu.stanford.math.plex4.homology.chain_basis.CellComparator;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import gnu.trove.TIntObjectHashMap;

public class ExplicitCellStream extends ExplicitStream<Cell> {
	private TIntObjectHashMap<Cell> indexCellMap = new TIntObjectHashMap<Cell>();
	
	public ExplicitCellStream() {
		super(CellComparator.getInstance());
	}

	public int addNewVertex() {
		Cell cell = new Cell();
		this.indexCellMap.put(cell.getCellId(), cell);
		this.storageStructure.addElement(cell, 0);
		return cell.getCellId();
	}
	
	public int attachNewCellToPoint(int dimension, int pointIndex) {
		Cell cell = new Cell(dimension, this.getBoundaryElements(new int[]{pointIndex}), new int[]{0});
		this.indexCellMap.put(cell.getCellId(), cell);
		this.storageStructure.addElement(cell, 0);
		return cell.getCellId();
	}
	
	public int attachNewCell(int dimension, int[] boundaryIndices) {
		Cell cell = new Cell(dimension, this.getBoundaryElements(boundaryIndices));
		this.indexCellMap.put(cell.getCellId(), cell);
		this.storageStructure.addElement(cell, 0);
		return cell.getCellId();
	}
	
	public int attachNewCell(int dimension, int[] boundaryIndices, int[] boundaryCoefficients) {
		Cell cell = new Cell(dimension, this.getBoundaryElements(boundaryIndices), boundaryCoefficients);
		this.indexCellMap.put(cell.getCellId(), cell);
		this.storageStructure.addElement(cell, 0);
		return cell.getCellId();
	}
	
	@Override
	public void addElement(Cell basisElement, int filtrationIndex) {
		this.indexCellMap.put(basisElement.getCellId(), basisElement);
		this.storageStructure.addElement(basisElement, filtrationIndex);
	}
	
	/*
	public int addElement(int cellId, int dimension, int[] boundaryIndices, int[] boundaryCoefficients) {
		ExceptionUtility.verifyTrue(!this.indexCellMap.contains(cellId));
		Cell cell = new Cell(cellId, dimension, this.getBoundaryElements(boundaryIndices), boundaryCoefficients);
		this.indexCellMap.put(cell.getCellId(), cell);
		this.storageStructure.addElement(cell, 0);
		this.cellIdCounter = Math.max(this.cellIdCounter, cellId + 1);
		return cell.getCellId();
	}
	*/
	
	private Collection<Cell> getBoundaryElements(int[] boundaryIndices) {
		List<Cell> boundaryElements = new ArrayList<Cell>();
		
		for (int index: boundaryIndices) {
			ExceptionUtility.verifyTrue(this.indexCellMap.contains(index));
			boundaryElements.add(this.indexCellMap.get(index));
		}
		
		return boundaryElements;
	}
}
