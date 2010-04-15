package edu.stanford.math.plex_plus.tree;

public class KDNode {
	private final double[] point;
	private KDNode left;
	private KDNode right;
	
	public KDNode(double[] point) {
		this.point = point;
	}
	
	public double[] getPoint() {
		return this.point;
	}
	
	public KDNode getLeft() {
		return this.left;
	}
	
	public KDNode getRight() {
		return this.right;
	}
	
	public void setLeft(KDNode node) {
		this.left = node;
	}
	
	public void setRight(KDNode node) {
		this.right = node;
	}
	
}
