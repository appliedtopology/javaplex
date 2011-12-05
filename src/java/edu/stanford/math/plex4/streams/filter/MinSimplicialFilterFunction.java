package edu.stanford.math.plex4.streams.filter;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;

public class MinSimplicialFilterFunction implements FilterFunction<Simplex> {
	private final IntFilterFunction intFilterFunction;
	public MinSimplicialFilterFunction(IntFilterFunction intFilterFunction) {
		this.intFilterFunction = intFilterFunction;
	}
	
	public double evaluate(Simplex simplex) {
		int[] vertices = simplex.getVertices();
		
		double minValue = 0;
		double vertexValue = 0;
		
		for (int i = 0; i < vertices.length; i++) {
			vertexValue = this.intFilterFunction.evaluate(vertices[i]);
			if (i == 0 || vertexValue < minValue) {
				minValue = vertexValue;
			}
		}
		
		return minValue;
	}
}
