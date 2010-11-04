package edu.stanford.math.plex4.homology.deprecated;


public class VietorisRipsSpecifier extends FiltrationSpecifier {
	private final double[][] pointCloud;
	
	public VietorisRipsSpecifier(double[][] pointCloud, int maxDimension, double maxFiltrationValue, int numDivisions) {
		super(maxDimension, maxFiltrationValue, numDivisions);
		this.pointCloud = pointCloud;
	}

	public double[][] getPointCloud() {
		return this.pointCloud;
	}
}
