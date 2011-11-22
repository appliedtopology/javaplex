package edu.stanford.math.plex4.bottleneck;

public class FlowEdge {
	private final int source;
	private final int dest;
	private int capacity;
	
	private FlowEdge reverseEdge = null;
	
	public FlowEdge(int source, int dest, int capacity) {
		this.source = source;
		this.dest = dest;
		this.capacity = capacity;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getSource() {
		return source;
	}

	public int getDest() {
		return dest;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dest;
		result = prime * result + source;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FlowEdge other = (FlowEdge) obj;
		if (dest != other.dest)
			return false;
		if (source != other.source)
			return false;
		return true;
	}

	public FlowEdge getReverseEdge() {
		return reverseEdge;
	}

	public void setReverseEdge(FlowEdge reverseEdge) {
		this.reverseEdge = reverseEdge;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(source);
		builder.append(",");
		builder.append(dest);
		builder.append(" (");
		builder.append(capacity);
		builder.append(")");
		builder.append("]");
		return builder.toString();
	}
}
