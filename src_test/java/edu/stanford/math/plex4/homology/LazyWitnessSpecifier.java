package edu.stanford.math.plex4.homology;

import edu.stanford.math.plex4.homology.streams.impl.LazyWitnessStream;
import edu.stanford.math.plex4.math.metric.landmark.LandmarkSelector;

public class LazyWitnessSpecifier extends FiltrationSpecifier {
	private final int nu;
	private final LandmarkSelector<double[]> selector;
	
	public LazyWitnessSpecifier(LandmarkSelector<double[]> selector, int maxDimension, double maxFiltrationValue, int numDivisions) {
		super(maxDimension, maxFiltrationValue, numDivisions);
		this.selector = selector;
		this.nu = LazyWitnessStream.getDefaultNuValue();
	}
	
	public int getNu() {
		return this.nu;
	}
	
	public LandmarkSelector<double[]> getSelector() {
		return this.selector;
	}
}
