package edu.stanford.math.plex4.examples;

import edu.stanford.math.plex4.homology.chain_basis.Cell;
import edu.stanford.math.plex4.streams.impl.ExplicitCellStream;

/**
 * This class contains various static functions for producing cell complexes.
 * 
 * @author Andrew Tausz
 *
 */
public class CellStreamExamples {

	/**
	 * This function returns the filtered cell complex which is the running example in the
	 * paper "Dualities in Persistent (Co)homology" by de Silva, Morozov and Johansson.
	 * 
	 * @return a filtered cell stream
	 */
	public static ExplicitCellStream getMorozovJohanssonExample() {
		ExplicitCellStream stream = new ExplicitCellStream();

		Cell vertex_1 = new Cell();
		Cell vertex_2 = new Cell();
		Cell edge_3 = new Cell(1, new Cell[]{vertex_1, vertex_2});
		Cell edge_4 = new Cell(1, new Cell[]{vertex_1, vertex_2});
		Cell face_5 = new Cell(2, new Cell[]{edge_3, edge_4});
		Cell face_6 = new Cell(2, new Cell[]{edge_3, edge_4});

		stream.addElement(vertex_1, 1);
		stream.addElement(vertex_2, 2);
		stream.addElement(edge_3, 3);
		stream.addElement(edge_4, 4);
		stream.addElement(face_5, 5);
		stream.addElement(face_6, 6);

		stream.finalizeStream();

		return stream;
	}

	/**
	 * This function returns a cell decomposition of the n-sphere. The decomposition contains
	 * 1 n-cell with its boundary glued to a single point.
	 * 
	 * @param dimension the dimension of the sphere to produce
	 * @return a cellular sphere of the specified dimension
	 */
	public static ExplicitCellStream getCellularSphere(int dimension) {
		ExplicitCellStream stream = new ExplicitCellStream();

		int v = stream.addNewVertex();
		stream.attachNewCellToPoint(dimension, v);

		stream.finalizeStream();

		return stream;
	}

	/**
	 * This function returns a static cell complex containing a torus.
	 * 
	 * @return a cellular torus
	 */
	public static ExplicitCellStream getCellularTorus() {
		ExplicitCellStream stream = new ExplicitCellStream();

		int v = stream.addNewVertex();
		int e_1 = stream.attachNewCellToPoint(1, v);
		int e_2 = stream.attachNewCellToPoint(1, v);
		stream.attachNewCell(2, new int[]{e_1, e_2, e_1, e_2}, new int[]{1, 1, -1, -1});

		stream.finalizeStream();

		return stream;
	}

	/**
	 * This function returns a cellular Klein bottle.
	 * 
	 * @return a cellular Klein bottle
	 */
	public static ExplicitCellStream getCellularKleinBottle() {
		ExplicitCellStream stream = new ExplicitCellStream();

		int v = stream.addNewVertex();
		int e_1 = stream.attachNewCellToPoint(1, v);
		int e_2 = stream.attachNewCellToPoint(1, v);
		stream.attachNewCell(2, new int[]{e_1, e_2, e_1, e_2}, new int[]{1, -1, -1, -1});

		stream.finalizeStream();

		return stream;
	}

	/**
	 * This function returns a cell decomposition of RP^2 (the real projective plane).
	 * 
	 * @return a cellular RP^2
	 */
	public static ExplicitCellStream getCellularRP2() {
		ExplicitCellStream stream = new ExplicitCellStream();

		int v = stream.addNewVertex();
		int e_1 = stream.attachNewCellToPoint(1, v);
		int e_2 = stream.attachNewCellToPoint(1, v);
		stream.attachNewCell(2, new int[]{e_1, e_2, e_1, e_2}, new int[]{1, 1, 1, 1});

		stream.finalizeStream();

		return stream;
	}

	/**
	 * This function returns a cell decomposition of the Mobius band.
	 * 
	 * @return a cellular mobius band
	 */
	public static ExplicitCellStream getCellularMobiusBand() {
		ExplicitCellStream stream = new ExplicitCellStream();

		int v_1 = stream.addNewVertex();
		int v_2 = stream.addNewVertex();
		int e_1 = stream.attachNewCell(1, new int[]{v_2, v_1});
		int e_2 = stream.attachNewCell(1, new int[]{v_2, v_1});
		int e_3 = stream.attachNewCell(1, new int[]{v_1, v_2});
		stream.attachNewCell(2, new int[]{e_1, e_3, e_1, e_2}, new int[]{1, 1, 1, -1});

		stream.finalizeStream();

		return stream;
	}
}
