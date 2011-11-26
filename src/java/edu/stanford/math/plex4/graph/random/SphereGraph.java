package edu.stanford.math.plex4.graph.random;

import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.utility.MathUtility;

/**
 * @author Andrew Tausz
 * 
 */
public class SphereGraph extends ManifoldGraph {

	public SphereGraph(int size, int k) {
		super(size, 2, k);
	}

	@Override
	protected double distance(double[] x, double[] y) {
		return MathUtility.greatCircleDistance(x, y);
	}

	@Override
	protected void generatePoints() {
		this.points = PointCloudExamples.getRandomSpherePoints(this.size, 2);
	}
	
	@Override
	public String toString() {
		return "SphereKNN(" + this.size + "," + this.dimension + "," + this.k + ")";
	}
}