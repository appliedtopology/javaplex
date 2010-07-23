package edu.stanford.math.plex4.homology;

import edu.stanford.math.plex4.homology.streams.impl.LazyWitnessStream;

public class FiltrationSpecifier {
	public enum FiltrationType {
		VietorisRips,
		LazyWitnessRandom,
		LazyWitnessMaxMin
	}
	
	private final FiltrationType type;

	private final int maxDimension;
	private final double maxFiltrationValue;
	private final int numDivisions;
	
	// Lazy witness specific data
	private final int nu;
	
	public FiltrationSpecifier(FiltrationType type, int maxDimension, double maxFiltrationValue, int numDivisions) {
		this.type = type;
		this.maxDimension = maxDimension;
		this.maxFiltrationValue = maxFiltrationValue;
		this.numDivisions = numDivisions;
		this.nu = LazyWitnessStream.getDefaultNuValue();
	}
	
	public FiltrationSpecifier(FiltrationType type, int maxDimension, double maxFiltrationValue, int numDivisions, int nu) {
		this.type = type;
		this.maxDimension = maxDimension;
		this.maxFiltrationValue = maxFiltrationValue;
		this.numDivisions = numDivisions;
		this.nu = nu;
	}
	
	public static FiltrationSpecifier createVietorisRipsSpecifier(int maxDimension, double maxFiltrationValue, int numDivisions) {
		return new FiltrationSpecifier(FiltrationType.VietorisRips, maxDimension, maxFiltrationValue, numDivisions);
	}
	
	public static FiltrationSpecifier createLazyWitnessRandomSpecifier(int maxDimension, double maxFiltrationValue, int numDivisions) {
		return new FiltrationSpecifier(FiltrationType.VietorisRips, maxDimension, maxFiltrationValue, numDivisions);
	}
	
	public FiltrationType getType() {
		return type;
	}
	
	public int getMaxDimension() {
		return this.maxDimension;
	}
	
	public double getMaxFiltrationValue() {
		return this.maxFiltrationValue;
	}
	
	public int getNumDivisions() {
		return this.numDivisions;
	}
	
	public int getNu() {
		return this.nu;
	}
}
