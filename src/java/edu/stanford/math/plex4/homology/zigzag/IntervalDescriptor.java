/**
 * 
 */
package edu.stanford.math.plex4.homology.zigzag;


public class IntervalDescriptor<T, G> {
	protected final T start;
	protected final G generator;
	protected final int dimension;
	
	public IntervalDescriptor(T start, int dimension, G generator) {
		super();
		this.start = start;
		this.dimension = dimension;
		this.generator = generator;
	}
	
	/**
	 * @return the start
	 */
	public T getStart() {
		return start;
	}

	/**
	 * @return the dimension
	 */
	public int getDimension() {
		return dimension;
	}

	/**
	 * @return the generator
	 */
	public G getGenerator() {
		return generator;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[dim=" + dimension + ", " + (start != null ? "start=" + start : "") + ", " + (generator != null ? "" + generator : "") + "]";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dimension;
		result = prime * result + ((generator == null) ? 0 : generator.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
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
		if (getClass() != obj.getClass())
			return false;
		IntervalDescriptor<?, ?> other = (IntervalDescriptor<?, ?>) obj;
		if (dimension != other.dimension)
			return false;
		if (generator == null) {
			if (other.generator != null)
				return false;
		} else if (!generator.equals(other.generator))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}
}