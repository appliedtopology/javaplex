package edu.stanford.math.plex4.graph.random;

import edu.stanford.math.primitivelib.autogen.array.DoubleArrayMath;

public class TorusGraph extends ManifoldGraph {

	private static final long serialVersionUID = 101044478862814881L;

	public TorusGraph(int size, int dimension, int k) {
		super(size, dimension, k);
	}
	
	public TorusGraph(int size, int k) {
		super(size, 2, k);
	}

	@Override
	protected double distance(double[] x, double[] y) {
		double distance = DoubleArrayMath.distance(x, y);
		
		for (int d = 0; d < this.dimension; d++) {
			// shift up by 1 in dimension
			y[d] += 1;
			distance = Math.min(distance, DoubleArrayMath.distance(x, y, 2));
			y[d] -= 1;
			
			// shift down by 1 in dimension
			y[d] -= 1;
			distance = Math.min(distance, DoubleArrayMath.distance(x, y, 2));
			y[d] += 1;
		}
		
		return distance;
	}

	@Override
	protected void generatePoints() {
		this.generateUniformPoints();
	}

	@Override
	public String toString() {
		return "TorusKNN(" + this.size + "," + this.dimension + "," + this.k + ")";
	}
}
