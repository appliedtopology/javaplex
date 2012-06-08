package edu.stanford.math.plex4.streams.impl;

import java.util.Comparator;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;

/**
 * This class is a wrapper for the ExplicitStream class and provides 
 * convenience functions when the base elements are simplices.
 * 
 * @author Andrew Tausz
 *
 */
public class ExplicitSimplexStream extends ExplicitStream<Simplex> {

	/**
	 * This constructor initializes the class with the given simplex
	 * comparator.
	 * 
	 * @param comparator the Comparator to initialize with
	 */
	public ExplicitSimplexStream(Comparator<Simplex> comparator) {
		super(comparator);
	}
	
	public ExplicitSimplexStream(Comparator<Simplex> comparator, double maxFiltrationValue) {
		super(comparator, maxFiltrationValue);
	}
	
	/**
	 * This constructor initializes the class with the default simplex comparator.
	 */
	public ExplicitSimplexStream() {
		super(SimplexComparator.getInstance());
	}
	
	public ExplicitSimplexStream(double maxFiltrationValue) {
		super(SimplexComparator.getInstance(), maxFiltrationValue);
	}

	/**
	 * This function adds a new vertex to the complex with a filtration index
	 * of 0.
	 * 
	 * @param index the index of the vertex
	 */
	public void addVertex(int index) {
		this.addElement(new Simplex(new int[]{index}), 0);
	}
	
	/**
	 * This function adds a new vertex to the complex with specified
	 * filtration index.
	 * 
	 * @param index the index of the vertex to add
	 * @param filtrationIndex the filtration index
	 */
	public void addVertex(int index, double filtrationValue) {
		this.addElement(new Simplex(new int[]{index}), filtrationValue);
	}
	
	/**
	 * This function adds a simplex with the provided vertices.
	 * 
	 * @param vertices the vertices of the simplex to add
	 */
	public void addElement(int[] vertices) {
		this.addElement(new Simplex(vertices), 0);
	}
	
	/**
	 * This function adds a simplex with the provided vertices and
	 * given filtration index.
	 * 
	 * @param vertices the vertices of the simplex to add
	 * @param filtrationIndex the filtration index
	 */
	public void addElement(int[] vertices, double filtrationValue) {
		this.addElement(new Simplex(vertices), filtrationValue);
	}
	
	public boolean removeElementIfPresent(int[] vertices) {
		return this.removeElementIfPresent(new Simplex(vertices));
	}
}
