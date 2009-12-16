package edu.stanford.math.plex;

/**
 * <code>PersistenceInterval</code> instances encode a dimension and a
 * duration (e.g., start and end times). The integer start/end times are
 * used internally in the Persistence class, but these may be converted to
 * form that uses doubles for start/end.
 *
 * @version $Id$
 */
abstract public class PersistenceInterval implements Comparable<PersistenceInterval> {

	/**
	 * The dimension of the PersistenceInterval.
	 */
	public final int dimension;

	/**
	 * Is this PersistenceInterval semi-infinite in extent?
	 *
	 * <p>
	 * @return True or false depending on whether or not the right endpoint
	 * is infinite.
	 */
	public abstract boolean infiniteExtent();

	/**
	 * Return the start/endpoints as double[].
	 *
	 * <p>
	 * @return double[] describing the start and end points of the interval.
	 */
	public abstract double[] toDouble();

	// used just by subclasses
	private PersistenceInterval(int d) {
		assert(d >= 0);
		dimension = d;
	}

	/**
	 * Represent a PersistenceInterval as a String. 
	 *
	 * <p>
	 * @return     A string that describes a PersistenceInterval.
	 */
	public String toString() { 
		if (this instanceof PersistenceInterval.Float) {
			PersistenceInterval.Float tmp = (PersistenceInterval.Float) this;
			return tmp.toString();
		} else {
			PersistenceInterval.Int tmp = (PersistenceInterval.Int) this;
			return tmp.toString();
		}
	}

	public static class Int extends PersistenceInterval {

		public static final int MAX_END = 0x7fffffff;
		public final int start;
		public final int end;

		// don't use
		private Int() { super(0); start = 0; end = 0; }

		public Int(int dimension, int start, int end) {
			super(dimension);
			assert((start >= 0) && (end >= start));
			this.start = start;
			this.end = end;
		}

		public Int(int dimension, int start) {
			super(dimension);
			assert((start >= 0) && (MAX_END > start));
			this.start = start;
			this.end = MAX_END;
		}

		/**
		 * Is this PersistenceInterval semi-infinite in extent?
		 *
		 * <p>
		 * @return     True or false depending on the right endpoint.
		 */
		public boolean infiniteExtent() {
			return end == MAX_END;
		}

		/**
		 * Represent a PersistenceInterval.Int as a String.
		 *
		 * <p>
		 * @return     A string that describes a PersistenceInterval.Int.
		 */
		public String toString() { 
			if (end == MAX_END)
				return String.format("[%d: (%d,inf)]", dimension, start);
			else
				return String.format("[%d: (%d,%d)]", dimension, start, end);
		}

		/**
		 * Represent a PersistenceInterval.Int as a double[].
		 *
		 * <p>
		 * @return     A double[] that describes the endpoints.
		 */
		public double[] toDouble() { 
			if (end == MAX_END)
				return new double[] { (double) start, Double.POSITIVE_INFINITY };
			else
				return new double[] { (double) start, (double) end };
		}

		/**
		 * Overrides Object hashcode.
		 *
		 * <p>
		 * @return     CRC hash of the slots set.
		 *
		 */
		public int hashCode() {
			int hash32 = CRC.hash32(dimension, 0);
			hash32 = CRC.hash32(start, hash32);
			return CRC.hash32(end, hash32);
		}

		/**
		 * Are two persistence intervals equivalent. Overrides Object equal.
		 *
		 * <p>
		 * @param      obj   object to compare.
		 * @return true or false, depending on whether or not the
		 * PersistenceInterval is equivalent to obj.
		 *
		 */
		public boolean equals(Object obj) {
			if (!(obj instanceof PersistenceInterval.Int))
				return false;
			PersistenceInterval.Int p = (PersistenceInterval.Int) obj;
			return ((p.dimension == dimension) &&
					(p.start == start) &&
					(p.end == end));
		}

		/**
		 * Implements Comparable interface.
		 *
		 * <p>
		 * @param      p    PersistenceInterval to compare.
		 * @return     negative, 0, or positive, if this <, =, resp. > than p.
		 *
		 */
		public int compareTo(PersistenceInterval p) {
			if (p instanceof PersistenceInterval.Float)
				return -1;
			PersistenceInterval.Int pi = (PersistenceInterval.Int) p;
			if (dimension != pi.dimension)
				return (dimension - pi.dimension);
			if (start != pi.start)
				return (start - pi.start);
			return (end - pi.end);
		}
	}


	public static class Float extends PersistenceInterval {

		public static final double MAX_END = Double.POSITIVE_INFINITY;
		public final double start;
		public final double end;

		// don't use
		private Float() { super(0); start = 0; end = 0; }

		public Float(int dimension, double start, double end) {
			super(dimension);
			assert((start >= 0) && (end >= start));
			this.start = start;
			this.end = end;
		}

		public Float(int dimension, double start) {
			super(dimension);
			assert((start >= 0) && (MAX_END > start));
			this.start = start;
			this.end = MAX_END;
		}

		/**
		 * Is this PersistenceInterval semi-infinite in extent?
		 *
		 * <p>
		 * @return     True or false depending on the right endpoint.
		 */
		public boolean infiniteExtent() {
			return end == MAX_END;
		}

		/**
		 * Represent a PersistenceInterval.Float as a String.
		 *
		 * <p>
		 * @return     A string that describes a PersistenceInterval.
		 */
		public String toString() { 
			if (end == MAX_END)
				return String.format("[%d: (%f,inf)]", dimension, start);
			else
				return String.format("[%d: (%f,%f)]", dimension, start, end);
		}

		/**
		 * Represent a PersistenceInterval.Int as a double[].
		 *
		 * <p>
		 * @return     A double[] that describes the endpoints.
		 */
		public double[] toDouble() { 
			if (end == MAX_END)
				return new double[] { start, Double.POSITIVE_INFINITY };
			else
				return new double[] { start, end };
		}

		/**
		 * Overrides Object hashcode.
		 *
		 * <p>
		 * @return     CRC hash of the slots set.
		 *
		 */
		public int hashCode() {
			int hash32 = CRC.hash32(dimension, 0);
			hash32 = CRC.hash32((int)start, hash32);
			return CRC.hash32((int)end, hash32);
		}

		/**
		 * Are two persistence intervals equivalent. Overrides Object equal.
		 *
		 * <p>
		 * @param      obj   object to compare.
		 * @return true or false, depending on whether or not the
		 * PersistenceInterval is equivalent to obj.
		 *
		 */
		public boolean equals(Object obj) {
			if (!(obj instanceof PersistenceInterval.Float))
				return false;
			PersistenceInterval.Float p = (PersistenceInterval.Float) obj;
			return ((p.dimension == dimension) &&
					(p.start == start) &&
					(p.end == end));
		}

		/**
		 * Implements Comparable interface.
		 *
		 * <p>
		 * @param      p    PersistenceInterval to compare.
		 * @return     negative, 0, or positive, if this <, =, resp. > than p.
		 *
		 */
		public int compareTo(PersistenceInterval p) {
			if (p instanceof PersistenceInterval.Int)
				return 1;
			PersistenceInterval.Float pf = (PersistenceInterval.Float) p;
			if (dimension != pf.dimension)
				return (dimension - pf.dimension);
			if (start != pf.start)
				return (start < pf.start)?-1:1;
			if (end != pf.end)
				return (end < pf.end)?-1:1;
			return 0;
		}
	}
}
