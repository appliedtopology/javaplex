package edu.stanford.math.plex4.homology;


public class FiltrationSpecifier {
	/*
	public enum FiltrationType {
		VietorisRips,
		LazyWitnessRandom,
		LazyWitnessMaxMin
	}
	*/

	private final int maxDimension;
	private final double maxFiltrationValue;
	private final int numDivisions;
	
	public FiltrationSpecifier(int maxDimension, double maxFiltrationValue, int numDivisions) {
		this.maxDimension = maxDimension;
		this.maxFiltrationValue = maxFiltrationValue;
		this.numDivisions = numDivisions;
	}
	
	/*
	public static FiltrationSpecifier createVietorisRipsSpecifier(int maxDimension, double maxFiltrationValue, int numDivisions) {
		return new FiltrationSpecifier(FiltrationType.VietorisRips, maxDimension, maxFiltrationValue, numDivisions);
	}
	
	public static FiltrationSpecifier createLazyWitnessRandomSpecifier(int maxDimension, double maxFiltrationValue, int numDivisions) {
		return new FiltrationSpecifier(FiltrationType.VietorisRips, maxDimension, maxFiltrationValue, numDivisions);
	}
	*/

	public int getMaxDimension() {
		return this.maxDimension;
	}
	
	public double getMaxFiltrationValue() {
		return this.maxFiltrationValue;
	}
	
	public int getNumDivisions() {
		return this.numDivisions;
	}
}
