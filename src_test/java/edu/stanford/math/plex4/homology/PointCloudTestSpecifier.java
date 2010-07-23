package edu.stanford.math.plex4.homology;


public class PointCloudTestSpecifier extends FiltrationSpecifier {
	private final double[][] pointCloud;
	
	public PointCloudTestSpecifier(double[][] pointCloud, FiltrationType type, int maxDimension, double maxFiltrationValue, int numDivisions) {
		super(type, maxDimension, maxFiltrationValue, numDivisions);
		this.pointCloud = pointCloud;
	}

	public double[][] getPointCloud() {
		return this.pointCloud;
	}
}
