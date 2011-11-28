package edu.stanford.math.plex4.streams.multi;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.streams.filter.FilterFunction;
import edu.stanford.math.plex4.streams.filter.IntFilterFunction;

public class InducedSimplicialFilterFunction implements FilterFunction<Simplex> {
	private final IntFilterFunction intFilterFunction;
	public InducedSimplicialFilterFunction(IntFilterFunction intFilterFunction) {
		this.intFilterFunction = intFilterFunction;
	}
	
	public double evaluate(Simplex simplex) {
		int[] vertices = simplex.getVertices();
		
		double maxValue = 0;
		double vertexValue = 0;
		
		for (int i = 0; i < vertices.length; i++) {
			vertexValue = this.intFilterFunction.evaluate(vertices[i]);
			if (i == 0 || vertexValue > maxValue) {
				maxValue = vertexValue;
			}
		}
		
		return maxValue;
	}
}
