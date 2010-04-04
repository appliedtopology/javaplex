package edu.stanford.math.plex_plus.tree;

public class BinaryNode {
	private final int index;
	private BinaryNode left;
	private BinaryNode right;
	
	public BinaryNode(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public BinaryNode getLeft() {
		return this.left;
	}
	
	public BinaryNode getRight() {
		return this.right;
	}
	
	public void setLeft(BinaryNode node) {
		this.left = node;
	}
	
	public void setRight(BinaryNode node) {
		this.right = node;
	}
	
}
