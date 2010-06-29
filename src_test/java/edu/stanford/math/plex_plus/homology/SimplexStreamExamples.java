package edu.stanford.math.plex_plus.homology;

import edu.stanford.math.plex_plus.homology.chain_basis.Cell;
import edu.stanford.math.plex_plus.homology.chain_basis.CellComparator;
import edu.stanford.math.plex_plus.homology.chain_basis.Simplex;
import edu.stanford.math.plex_plus.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex_plus.homology.streams.impl.ExplicitStream;
import edu.stanford.math.plex_plus.homology.streams.interfaces.AbstractFilteredStream;

public class SimplexStreamExamples {
	
	/**
	 * This implements the example in Figure 1 of the paper
	 * "Computing Persistent Homology" by Zomorodian and Carlsson.
	 * 
	 */
	public static AbstractFilteredStream<Simplex> getZomorodianCarlssonExample() {
		ExplicitStream<Simplex> stream = new ExplicitStream<Simplex>(SimplexComparator.getInstance());

		stream.addElement(new Simplex(new int[]{0}), 0);
		stream.addElement(new Simplex(new int[]{1}), 0);

		stream.addElement(new Simplex(new int[]{2}), 1);
		stream.addElement(new Simplex(new int[]{3}), 1);
		stream.addElement(new Simplex(new int[]{0, 1}), 1);
		stream.addElement(new Simplex(new int[]{1, 2}), 1);

		stream.addElement(new Simplex(new int[]{2, 3}), 2);
		stream.addElement(new Simplex(new int[]{3, 0}), 2);

		stream.addElement(new Simplex(new int[]{0, 2}), 3);

		stream.addElement(new Simplex(new int[]{0, 1, 2}), 4);

		stream.addElement(new Simplex(new int[]{0, 2, 3}), 5);

		stream.finalizeStream();
		
		return stream;
	}
	
	public static AbstractFilteredStream<Cell> getMorozovJohanssonExample() {
		ExplicitStream<Cell> stream = new ExplicitStream<Cell>(CellComparator.getInstance());

		Cell vertex_1 = new Cell(1);
		Cell vertex_2 = new Cell(2);
		Cell edge_3 = new Cell(3, 1, new Cell[]{vertex_1, vertex_2});
		Cell edge_4 = new Cell(4, 1, new Cell[]{vertex_1, vertex_2});
		Cell face_5 = new Cell(5, 2, new Cell[]{edge_3, edge_4});
		Cell face_6 = new Cell(6, 2, new Cell[]{edge_3, edge_4});
		
		stream.addElement(vertex_1, 1);
		stream.addElement(vertex_2, 2);
		stream.addElement(edge_3, 3);
		stream.addElement(edge_4, 4);
		stream.addElement(face_5, 5);
		stream.addElement(face_6, 6);
		
		stream.finalizeStream();
		
		return stream;
	}
	
	public static AbstractFilteredStream<Cell> getCellularSphere(int dimension) {
		ExplicitStream<Cell> stream = new ExplicitStream<Cell>(CellComparator.getInstance());

		Cell vertex_1 = new Cell(1);
		Cell n_cell = new Cell(2, dimension, new Cell[]{});
		
		stream.addElement(vertex_1, 0);
		stream.addElement(n_cell, 0);
		
		stream.finalizeStream();
		
		return stream;
	}
	
	public static AbstractFilteredStream<Cell> getCellularTorus() {
		ExplicitStream<Cell> stream = new ExplicitStream<Cell>(CellComparator.getInstance());

		Cell vertex_1 = new Cell(1);
		Cell edge_2 = new Cell(2, 1, new Cell[]{vertex_1, vertex_1});
		Cell edge_3 = new Cell(3, 1, new Cell[]{vertex_1, vertex_1});
		Cell face_4 = new Cell(4, 2, new Cell[]{edge_2, edge_3, edge_2, edge_3}, new int[]{1, 1, -1, -1});
		
		stream.addElement(vertex_1, 0);
		stream.addElement(edge_2, 0);
		stream.addElement(edge_3, 0);
		stream.addElement(face_4, 0);
		
		stream.finalizeStream();
		
		return stream;
	}
	
	public static AbstractFilteredStream<Cell> getCellularKleinBottle() {
		ExplicitStream<Cell> stream = new ExplicitStream<Cell>(CellComparator.getInstance());

		Cell vertex_1 = new Cell(1);
		Cell edge_2 = new Cell(2, 1, new Cell[]{vertex_1, vertex_1});
		Cell edge_3 = new Cell(3, 1, new Cell[]{vertex_1, vertex_1});
		Cell face_4 = new Cell(4, 2, new Cell[]{edge_2, edge_3, edge_2, edge_3});
		
		stream.addElement(vertex_1, 0);
		stream.addElement(edge_2, 0);
		stream.addElement(edge_3, 0);
		stream.addElement(face_4, 0);
		
		stream.finalizeStream();
		
		return stream;
	}
	
	public static AbstractFilteredStream<Cell> getCellularRP2() {
		ExplicitStream<Cell> stream = new ExplicitStream<Cell>(CellComparator.getInstance());

		Cell vertex_1 = new Cell(1);
		Cell edge_2 = new Cell(2, 1, new Cell[]{vertex_1, vertex_1});
		Cell edge_3 = new Cell(3, 1, new Cell[]{vertex_1, vertex_1});
		Cell face_4 = new Cell(4, 2, new Cell[]{edge_2, edge_3, edge_2, edge_3}, new int[]{1, 1, 1, 1});
		
		stream.addElement(vertex_1, 0);
		stream.addElement(edge_2, 0);
		stream.addElement(edge_3, 0);
		stream.addElement(face_4, 0);
		
		stream.finalizeStream();
		
		return stream;
	}
	
	public static AbstractFilteredStream<Simplex> getTriangle() {
		ExplicitStream<Simplex> stream = new ExplicitStream<Simplex>(SimplexComparator.getInstance());

		stream.addElement(new Simplex(new int[]{0}), 0);
		stream.addElement(new Simplex(new int[]{1}), 0);
		stream.addElement(new Simplex(new int[]{2}), 0);
		stream.addElement(new Simplex(new int[]{0, 1}), 0);
		stream.addElement(new Simplex(new int[]{1, 2}), 0);
		stream.addElement(new Simplex(new int[]{2, 0}), 0);
		//stream.addElement(new Simplex(new int[]{0, 1, 2}), 3);

		stream.finalizeStream();
		
		return stream;
	}
	
	public static AbstractFilteredStream<Simplex> getTetrahedron() {
		ExplicitStream<Simplex> stream = new ExplicitStream<Simplex>(SimplexComparator.getInstance());

		stream.addElement(new Simplex(new int[]{0}), 0);
		stream.addElement(new Simplex(new int[]{1}), 0);
		stream.addElement(new Simplex(new int[]{2}), 0);
		stream.addElement(new Simplex(new int[]{3}), 0);
		stream.addElement(new Simplex(new int[]{0, 1}), 0);
		stream.addElement(new Simplex(new int[]{1, 2}), 0);
		stream.addElement(new Simplex(new int[]{0, 2}), 0);
		stream.addElement(new Simplex(new int[]{0, 3}), 0);
		stream.addElement(new Simplex(new int[]{1, 3}), 0);
		stream.addElement(new Simplex(new int[]{2, 3}), 0);
		stream.addElement(new Simplex(new int[]{0, 1, 2}), 0);
		stream.addElement(new Simplex(new int[]{0, 1, 3}), 0);
		stream.addElement(new Simplex(new int[]{0, 2, 3}), 0);
		stream.addElement(new Simplex(new int[]{1, 2, 3}), 0);
		
		stream.finalizeStream();
		
		return stream;
	}
	
	/**
	 * The 2-torus.
	 */
	public static AbstractFilteredStream<Simplex> getTorus() {
		ExplicitStream<Simplex> stream = new ExplicitStream<Simplex>(SimplexComparator.getInstance());

		stream.addElement(new Simplex(new int[] {1}), 0);
		stream.addElement(new Simplex(new int[] {2}), 0);
		stream.addElement(new Simplex(new int[] {3}), 0);

		stream.addElement(new Simplex(new int[] {4}), 0);
		stream.addElement(new Simplex(new int[] {5}), 0);
		stream.addElement(new Simplex(new int[] {6}), 0);

		stream.addElement(new Simplex(new int[] {7}), 0);
		stream.addElement(new Simplex(new int[] {8}), 0);
		stream.addElement(new Simplex(new int[] {9}), 0);

		stream.addElement(new Simplex(new int[] {1, 2}), 0);
		stream.addElement(new Simplex(new int[] {1, 3}), 0);
		stream.addElement(new Simplex(new int[] {2, 3}), 0);

		stream.addElement(new Simplex(new int[] {1, 4}), 0);
		stream.addElement(new Simplex(new int[] {2, 5}), 0);
		stream.addElement(new Simplex(new int[] {3, 6}), 0);

		stream.addElement(new Simplex(new int[] {1, 6}), 0);
		stream.addElement(new Simplex(new int[] {2, 4}), 0);
		stream.addElement(new Simplex(new int[] {3, 5}), 0);

		stream.addElement(new Simplex(new int[] {4, 5}), 0);
		stream.addElement(new Simplex(new int[] {4, 6}), 0);
		stream.addElement(new Simplex(new int[] {5, 6}), 0);
		stream.addElement(new Simplex(new int[] {4, 7}), 0);
		stream.addElement(new Simplex(new int[] {4, 9}), 0);
		stream.addElement(new Simplex(new int[] {5, 7}), 0);
		stream.addElement(new Simplex(new int[] {5, 8}), 0);
		stream.addElement(new Simplex(new int[] {6, 8}), 0);
		stream.addElement(new Simplex(new int[] {6, 9}), 0);

		stream.addElement(new Simplex(new int[] {7, 9}), 0);
		stream.addElement(new Simplex(new int[] {8, 9}), 0);
		stream.addElement(new Simplex(new int[] {7, 1}), 0);
		stream.addElement(new Simplex(new int[] {7, 3}), 0);
		stream.addElement(new Simplex(new int[] {7, 8}), 0);
		stream.addElement(new Simplex(new int[] {8, 1}), 0);
		stream.addElement(new Simplex(new int[] {8, 2}), 0);
		stream.addElement(new Simplex(new int[] {9, 2}), 0);
		stream.addElement(new Simplex(new int[] {9, 3}), 0);

		stream.addElement(new Simplex(new int[] {1, 2, 4}), 0);
		stream.addElement(new Simplex(new int[] {2, 4, 5}), 0);
		stream.addElement(new Simplex(new int[] {2, 3, 5}), 0);
		stream.addElement(new Simplex(new int[] {3, 5, 6}), 0);
		stream.addElement(new Simplex(new int[] {1, 4, 6}), 0);
		stream.addElement(new Simplex(new int[] {1, 3, 6}), 0);

		stream.addElement(new Simplex(new int[] {4, 5, 7}), 0);
		stream.addElement(new Simplex(new int[] {5, 7, 8}), 0);
		stream.addElement(new Simplex(new int[] {5, 6, 8}), 0);
		stream.addElement(new Simplex(new int[] {6, 8, 9}), 0);
		stream.addElement(new Simplex(new int[] {4, 7, 9}), 0);
		stream.addElement(new Simplex(new int[] {4, 6, 9}), 0);

		stream.addElement(new Simplex(new int[] {7, 8, 1}), 0);
		stream.addElement(new Simplex(new int[] {8, 1, 2}), 0);
		stream.addElement(new Simplex(new int[] {8, 9, 2}), 0);
		stream.addElement(new Simplex(new int[] {9, 2, 3}), 0);
		stream.addElement(new Simplex(new int[] {7, 1, 3}), 0);
		stream.addElement(new Simplex(new int[] {7, 9, 3}), 0);

		stream.finalizeStream();
		
		return stream;
	}
	
	public static AbstractFilteredStream<Simplex> getCircle(int m) {		
		ExplicitStream<Simplex> stream = new ExplicitStream<Simplex>(SimplexComparator.getInstance());

		for (int i = 0; i < m; i++) {
			stream.addElement(new Simplex(new int[]{i}), 0);
			stream.addElement(new Simplex(new int[]{i, (i + 1) % m}), 0);
		}

		stream.finalizeStream();
		
		
		return stream;
	}	
}
