package edu.stanford.math.plex4.examples;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.math.plex4.homology.chain_basis.Cell;
import edu.stanford.math.plex4.homology.streams.impl.ExplicitCellStream;
import edu.stanford.math.primitivelib.autogen.array.IntArrayMath;
import edu.stanford.math.primitivelib.autogen.array.IntArrayQuery;

public class CellComplexOperations {
	/**
	 * Suppose that we are given a CW complex X. This function produces the cell complex
	 * X x I, where I is the unit interval [0, 1].
	 * 
	 * @param stream
	 * @return
	 */
	public static ExplicitCellStream intervalProduct(ExplicitCellStream stream) {
		ExplicitCellStream product = new ExplicitCellStream();
		
		for (Cell cell: stream) {
			
		}
		
		return product;
	}
	
	public static ExplicitCellStream disjointUnion(ExplicitCellStream stream1, ExplicitCellStream stream2) {
		ExplicitCellStream union = new ExplicitCellStream();

		for (Cell cell: stream1) {
			union.addElement(cell, stream1.getFiltrationIndex(cell));
		}
		
		for (Cell cell: stream2) {
			union.addElement(cell, stream2.getFiltrationIndex(cell));
		}
		
		return union;
	}
	
	public static void identifyPoints(ExplicitCellStream stream, int[] vertices) {
		int new_vertex_index = IntArrayMath.min(vertices);
		
		List<Cell> removalList = new ArrayList<Cell>();
		
		for (Cell cell: stream) {
			int cellId = cell.getCellId();
			if (cell.getDimension() == 0) {				
				// make sure to not add points that collapse
				if ((cellId != new_vertex_index) && (IntArrayQuery.contains(vertices, cellId))) {
					// remove cell
					removalList.add(cell);
				}
			} else {
				// the dimension is greater than 0 - we need to make sure that the boundary is also "collapsed"
				int[] boundary = cell.getBoundaryIndices();
				for (int i = 0; i < boundary.length; i++) {
					if (boundary[i] != new_vertex_index && IntArrayQuery.contains(vertices, boundary[i])) {
						cell.getBoundaryIndices()[i] = new_vertex_index;
					}
				}
				
			}
		}
		
		for (Cell cell: removalList) {
			stream.removeElementIfPresent(cell);
		}
	}
	
	/*
	public static ExplicitCellStream identifyPoints(ExplicitCellStream stream, int[] vertices) {
		ExplicitCellStream quotient = new ExplicitCellStream();
		int new_vertex_index = IntArrayMath.min(vertices);
		
		for (Cell cell: stream) {
			int cellId = cell.getCellId();
			if (cell.getDimension() == 0) {				
				// make sure to not add points that collapse
				if ((cellId != new_vertex_index) && (IntArrayQuery.contains(vertices, cellId))) {
					// don't add
				} else {
					//quotient.addElement(cellId, 0, cell.getBoundaryIndices(), cell.getBoundaryCoefficients());
					
				}
			} else {
				// the dimension is greater than 0 - we need to make sure that the boundary is also "collapsed"
				int[] boundary = cell.getBoundaryCoefficients().clone();
				for (int i = 0; i < boundary.length; i++) {
					if (boundary[i] != new_vertex_index && IntArrayQuery.contains(vertices, boundary[i])) {
						boundary[i] = new_vertex_index;
					}
				}
				
				quotient.addElement(cell.getCellId(), cell.getDimension(), boundary, cell.getBoundaryCoefficients());
			}
		}
		
		return quotient;
	}*/
}
