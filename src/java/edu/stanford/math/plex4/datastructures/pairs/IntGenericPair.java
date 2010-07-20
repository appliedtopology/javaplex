package edu.stanford.math.plex4.datastructures.pairs;

public class IntGenericPair<T> {
	private final int first;
	private final T second;
	
	public IntGenericPair(int first, T second) {
		this.first = first;
		this.second = second;
	}
	
	/**
	 * Get the first component.
	 * 
	 * @return the first component
	 */
	public int getFirst() {
		return this.first;
	}
	
	/**
	 * Get the second component.
	 * 
	 * @return the second component
	 */
	public T getSecond() {
		return this.second;
	}
	
	@Override
	public String toString() {
		return ("(" + first + ", " + second.toString() + ")");
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + first;
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IntGenericPair))
			return false;
		IntGenericPair<?> other = (IntGenericPair<?>) obj;
		if (first != other.first)
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		return true;
	}
}
