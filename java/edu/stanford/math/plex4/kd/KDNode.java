package edu.stanford.math.plex4.kd;

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
	private final int splitAxis;

	public KDNode(int index, int splitAxis) {
		this.left = null;
		this.right = null;
		this.index = index;
		this.splitAxis = splitAxis;
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
	
	public int getSplitAxis() {
		return this.splitAxis;
	}
	
	public void setLeft(KDNode node) {
		this.left = node;
	}
	
	public void setRight(KDNode node) {
		this.right = node;
	}
}
