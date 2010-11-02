package edu.stanford.math.plex4.homology;

import edu.stanford.math.plex4.homology.PersistenceCalculationData.PersistenceAlgorithmType;
import edu.stanford.math.plex4.homology.barcodes.DoubleBarcodeCollection;

public class PersistenceAlgorithmResult {
	private PersistenceAlgorithmType type;
	private DoubleBarcodeCollection barcodeCollection;
	private int numSimplices;
	private int numPoints;
	private int maxDimension;
	private double maxFiltrationValue;
	private float seconds;
	
	public DoubleBarcodeCollection getBarcodeCollection() {
		return barcodeCollection;
	}
	public void setBarcodeCollection(DoubleBarcodeCollection barcodeCollection) {
		this.barcodeCollection = barcodeCollection;
	}
	public int getMaxDimension() {
		return maxDimension;
	}
	public void setMaxDimension(int maxDimension) {
		this.maxDimension = maxDimension;
	}
	public double getMaxFiltrationValue() {
		return maxFiltrationValue;
	}
	public void setMaxFiltrationValue(double maxFiltrationValue) {
		this.maxFiltrationValue = maxFiltrationValue;
	}
	public int getNumPoints() {
		return numPoints;
	}
	public void setNumPoints(int numPoints) {
		this.numPoints = numPoints;
	}
	public int getNumSimplices() {
		return numSimplices;
	}
	public void setNumSimplices(int numSimplices) {
		this.numSimplices = numSimplices;
	}
	public float getSeconds() {
		return seconds;
	}
	public void setSeconds(float seconds) {
		this.seconds = seconds;
	}
	public PersistenceAlgorithmType getType() {
		return type;
	}
	public void setType(PersistenceAlgorithmType type) {
		this.type = type;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("Type: ");
		builder.append(this.type);
		builder.append("\n");
		
		builder.append("Number of Points: ");
		builder.append(this.numPoints);
		builder.append("\n");
		
		builder.append("Number of Simplices: ");
		builder.append(this.numSimplices);
		builder.append("\n");
		
		builder.append("Max Dimension: ");
		builder.append(this.maxDimension);
		builder.append("\n");
		
		builder.append("Max Filtration Value: ");
		builder.append(this.maxFiltrationValue);
		builder.append("\n");
		
		builder.append("Time Elapsed (s): ");
		builder.append(this.seconds);
		builder.append("\n");
		
		return builder.toString();
	}
}
