package edu.stanford.math.plex;

/**
 * <code>PersistenceBasisInterval</code> instances encode a dimension and a
 * duration (e.g., start and end times) as well as a basis element for each 
 * interval. The integer start/end times are
 * used internally in the Persistence class, but these may be converted to
 * form that uses doubles for start/end.
 *
 */
abstract public class PersistenceBasisInterval 
implements Comparable<PersistenceBasisInterval> {

	/**
	 * The dimension of the PersistenceBasisInterval.
	 */
	public final int dimension;

	/**
	 * Is this PersistenceBasisInterval semi-infinite in extent?
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

	/**
	 * The basis representing the interval.
	 *
	 * <p>
	 * @return Chain the associated basis element.
	 */
	public abstract Chain basis();

	/**
	 * Conversion to PersistenceInterval
	 *
	 * <p>
	 * @return PersistenceInterval
	 */
	public abstract PersistenceInterval toPersistenceInterval();

	// used just by subclasses
	protected PersistenceBasisInterval(int d) {
		assert(d >= 0);
		dimension = d;
	}

	/**
	 * Represent a PersistenceBasisInterval as a String. 
	 *
	 * <p>
	 * @return     A string that describes a PersistenceBasisInterval.
	 */
	public String toString() { 
		if (this instanceof PersistenceBasisInterval.Float) {
			PersistenceBasisInterval.Float tmp = (PersistenceBasisInterval.Float) this;
			return tmp.toString();
		} else {
			PersistenceBasisInterval.Int tmp = (PersistenceBasisInterval.Int) this;
			return tmp.toString();
		}
	}

	public static class Int extends PersistenceBasisInterval {

		public static int MAX_END = 0x7fffffff;
		public final int start;
		public final int end;
		public final Chain basisElement;

		// don't use
		protected Int() { super(0); start = 0; end = 0; basisElement = null; }

		public Int(Chain basisElement, int start, int end) {
			super(basisElement.maxS().dimension());
			assert((start >= 0) && (end >= start));
			this.start = start;
			this.end = end;
			this.basisElement = basisElement;
		}

		public Int(Chain basisElement, int start) {
			super(basisElement.maxS().dimension());
			assert((start >= 0) && (MAX_END > start));
			this.start = start;
			this.end = MAX_END;
			this.basisElement = basisElement;
		}

		/**
		 * Is this PersistenceBasisInterval semi-infinite in extent?
		 *
		 * <p>
		 * @return     True or false depending on the right endpoint.
		 */
		public boolean infiniteExtent() {
			return end == MAX_END;
		}

		/**
		 * Represent a PersistenceBasisInterval.Int as a String.
		 *
		 * <p>
		 * @return     A string that describes a PersistenceBasisInterval.Int.
		 */
		public String toString() { 
			if (end == MAX_END)
				return String.format("[%d: (%d,inf)] %s", 
						dimension, start, basisElement.toString());
			else
				return String.format("[%d: (%d,%d)] %s", 
						dimension, start, end, basisElement.toString());
		}

		public PersistenceInterval toPersistenceInterval() {
			return (PersistenceInterval) new
			PersistenceInterval.Int(dimension,start,end); 
		}

		/**
		 * Represent a PersistenceBasisInterval.Int as a double[].
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
		 * Retrieves the basis element of an interval.
		 *
		 * <p>
		 * @return   The Chain object representing the stored basis element.
		 */
		public Chain basis() {
			return basisElement;
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
		 * PersistenceBasisInterval is equivalent to obj.
		 *
		 */
		public boolean equals(Object obj) {
			if (!(obj instanceof PersistenceBasisInterval.Int))
				return false;
			PersistenceBasisInterval.Int p = (PersistenceBasisInterval.Int) obj;
			return ((p.dimension == dimension) &&
					(p.start == start) &&
					(p.end == end) &&
					(p.basis().equals(basisElement)));
		}

		/**
		 * Implements Comparable interface.
		 *
		 * <p>
		 * @param      p    PersistenceBasisInterval to compare.
		 * @return     negative, 0, or positive, if this <, =, resp. > than p.
		 *
		 */
		public int compareTo(PersistenceBasisInterval p) {
			if (p instanceof PersistenceBasisInterval.Float)
				return -1;
			PersistenceBasisInterval.Int pi = (PersistenceBasisInterval.Int) p;
			if (dimension != pi.dimension)
				return (dimension - pi.dimension);
			if (start != pi.start)
				return (start - pi.start);
			return (end - pi.end);
		}
	}


	public static class Float extends PersistenceBasisInterval {

		public static double MAX_END = Double.POSITIVE_INFINITY;
		public final double start;
		public final double end;
		public final Chain basisElement;

		// don't use
		protected Float() { super(0); start = 0; end = 0; basisElement = null; }

		public Float(Chain basisElement, double start, double end) {
			super(basisElement.maxS().dimension());
			assert((start >= 0) && (end >= start));
			this.start = start;
			this.end = end;
			this.basisElement = basisElement;
		}

		public Float(Chain basisElement, double start) {
			super(basisElement.maxS().dimension());
			assert((start >= 0) && (MAX_END > start));
			this.start = start;
			this.end = MAX_END;
			this.basisElement = basisElement;
		}

		/**
		 * Is this PersistenceBasisInterval semi-infinite in extent?
		 *
		 * <p>
		 * @return     True or false depending on the right endpoint.
		 */
		public boolean infiniteExtent() {
			return end == MAX_END;
		}

		/**
		 * Represent a PersistenceBasisInterval.Float as a String.
		 *
		 * <p>
		 * @return     A string that describes a PersistenceBasisInterval.
		 */
		public String toString() { 
			if (end == MAX_END)
				return String.format("[%d: (%f,inf) %s]", 
						dimension, start, basisElement.toString());
			else
				return String.format("[%d: (%f,%f)] %s", 
						dimension, start, end, basisElement.toString());
		}

		public PersistenceInterval toPersistenceInterval() {
			return (PersistenceInterval) new 
			PersistenceInterval.Float(dimension, start, end);
		}

		/**
		 * Represent a PersistenceBasisInterval.Int as a double[].
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
		 * Retrieves the basis element of an interval.
		 *
		 * <p>
		 * @return   The Chain object representing the stored basis element.
		 */
		public Chain basis() {
			return basisElement;
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
		 * PersistenceBasisInterval is equivalent to obj.
		 *
		 */
		public boolean equals(Object obj) {
			if (!(obj instanceof PersistenceBasisInterval.Float))
				return false;
			PersistenceBasisInterval.Float p = (PersistenceBasisInterval.Float) obj;
			return ((p.dimension == dimension) &&
					(p.start == start) &&
					(p.end == end) &&
					(p.basis().equals(basisElement)));
		}

		/**
		 * Implements Comparable interface.
		 *
		 * <p>
		 * @param      p    PersistenceBasisInterval to compare.
		 * @return     negative, 0, or positive, if this <, =, resp. > than p.
		 *
		 */
		public int compareTo(PersistenceBasisInterval p) {
			if (p instanceof PersistenceBasisInterval.Int)
				return 1;
			PersistenceBasisInterval.Float pf = (PersistenceBasisInterval.Float) p;
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
