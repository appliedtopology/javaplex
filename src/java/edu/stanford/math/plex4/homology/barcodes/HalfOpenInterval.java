package edu.stanford.math.plex4.homology.barcodes;

public interface HalfOpenInterval extends Comparable<HalfOpenInterval> {
	
	public boolean isLeftInfinite();
	public boolean isRightInfinite();
	public boolean isInfinite();
	
	public boolean containsPoint(double point);
	
	public String toString();
	
	public double getStart();
	public double getEnd();
	
}
