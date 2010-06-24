package edu.stanford.math.plex_plus.kd;

/**
 * This class contains the functionality of a node
 * within a KD-tree.
 * 
 * @author Andrew Tausz
 *
 */
public class KDNode {
	private final int index;
	private KDNode left;
	private KDNode right;
	
	public KDNode(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return this.index;
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
