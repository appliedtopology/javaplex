package edu.stanford.math.plex4.examples;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.math.plex4.homology.chain_basis.Cell;
import edu.stanford.math.plex4.streams.impl.ExplicitCellStream;
import edu.stanford.math.primitivelib.autogen.array.IntArrayMath;
import edu.stanford.math.primitivelib.autogen.array.IntArrayQuery;

/**
 * This class contains utility functions for working with cell complexes.
 * 
 * @author Andrew Tausz
 *
 */
public class CellComplexOperations {
	
	/**
	 * This function produces a cell complex that is the disjoint union of two cell complexes.
	 * 
	 * @param stream1 the first cell complex
	 * @param stream2 the second cell complex
	 * @return the disjoint union of the supplied complexes
	 */
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
	
	/**
	 * This function collapses the points in the supplied array to a single point.
	 * 
	 * @param stream the cell complex
	 * @param vertices the vertices to identify
	 */
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
				int[] boundary = cell.getBoundaryIds();
				for (int i = 0; i < boundary.length; i++) {
					if (boundary[i] != new_vertex_index && IntArrayQuery.contains(vertices, boundary[i])) {
						cell.getBoundaryIds()[i] = new_vertex_index;
					}
				}
			}
		}
		
		for (Cell cell: removalList) {
			stream.removeElementIfPresent(cell);
		}
	}
}
