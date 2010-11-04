package edu.stanford.math.plex4.homology.deprecated;

public class PersistenceCalculationData {
	public enum PersistenceAlgorithmType {
		GenericClassicalHomology,
		GenericAbsoluteHomology,
		GenericAbsoluteCohomology,
		IntClassicalHomology,
		IntAbsoluteHomology,
		IntAbsoluteCohomology,
		Plex3Homology
	}
	
	private int maxDimension;
	private double maxFiltrationValue;
	private int numDivisions;
	private PersistenceAlgorithmType type;
	
	public PersistenceCalculationData(int maxDimension, double maxFiltrationValue, int numDivisions, PersistenceAlgorithmType type) {
		this.maxDimension = maxDimension;
		this.maxFiltrationValue = maxFiltrationValue;
		this.numDivisions = numDivisions;
		this.type = type;
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
	
	public PersistenceAlgorithmType getType() {
		return this.type;
	}
}
