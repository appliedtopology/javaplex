
package edu.stanford.math.plex;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An instance of the abstract class <code>SimplexStream</code> provides
 * the simplices of a filtered complex in lexicographic order on
 * persistence index and dimension. The ordering is unspecified beyond that
 * point, and may even vary from instance to instance. The reason that
 * SimplexStream has both <code>hasNext()</code> and <code>next()</code>
 * methods <em>and</em> implements the <code>Iterable</code> interface is
 * that the internal iteration methods are allowed to be destructive. That
 * is, it is possible (and in fact does happen) that
 * <code>stream.next()</code> will remove simplices from
 * <code>stream</code>, making it impossible to use this method to iterate
 * over <code>stream</code> a second time. Therefore, unless you are
 * prepared to <em>consume</em> the stream, use the <code>Iterable</code>
 * interface.
 *
 * @version $Id$
 */

public abstract class SimplexStream 
implements Iterable<Simplex>, Iterator<Simplex> {

	/**
	 * Is there a next Simplex in the stream?
	 *
	 * <p>
	 * @return     Return true if the stream is not yet empty.
	 *
	 */
	public abstract boolean hasNext();

	/**
	 * Next Simplex in the stream.
	 *
	 * <p>
	 * @return     Return the smallest remaining Simplex instance.
	 *
	 */
	public abstract Simplex next();

	/**
	 * Unsupported remove() operation.
	 *
	 * @exception UnsupportedOperationException 
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * How many simplices are in the stream?
	 *
	 * <p>
	 * @return     The number of simplices in the stream.
	 *
	 */
	public abstract int size();

	/**
	 * Max dimension of simplices in the stream.
	 *
	 * <p>
	 * @return     Max dimension of simplices in the stream.
	 *
	 */
	public abstract int maxDimension();

	/**
	 * Convert a PersistenceInterval into Float format in a stream-specific manner.
	 *
	 * <p>
	 * @param      p PersistenceInterval to convert.
	 * @return     Converted PersistenceInterval.Float.
	 *
	 */
	public PersistenceInterval.Float convertInterval(PersistenceInterval p) {
		if (p instanceof PersistenceInterval.Float)
			return (PersistenceInterval.Float) p;
		else {
			PersistenceInterval.Int pi = (PersistenceInterval.Int) p;
			double start = convert_filtration_index(pi.start);
			double end = p.infiniteExtent()?
					PersistenceInterval.Float.MAX_END:
						convert_filtration_index(pi.end);
			return new PersistenceInterval.Float(pi.dimension, start, end);
		} 
	}

	/**
	 * convert a PersistenceBasisInterval into Float format in a stream-specific manner.
	 *
	 * <p>
	 * @param    p PersistenceBasisInterval to convert.
	 * @return   Converted PersistenceBasisInterval.Float.
	 */
	public PersistenceBasisInterval.Float convertInterval(PersistenceBasisInterval p) {
		if(p instanceof PersistenceBasisInterval.Float)
			return (PersistenceBasisInterval.Float) p;
		else {
			PersistenceBasisInterval.Int pbi = (PersistenceBasisInterval.Int) p;
			PersistenceInterval.Int pii = 
				new PersistenceInterval.Int(pbi.dimension, pbi.start, pbi.end);
			PersistenceInterval.Float pif = convertInterval(pii);
			return new PersistenceBasisInterval.Float(pbi.basisElement,
					pif.start, pif.end);
		}
	}


	/**
	 * Convert a filtration index into a persistence parameter (i.e., double)
	 * -- gets overloaded by some subclasses.
	 *
	 * <p>
	 * @param      fi Filtration index to convert.
	 * @return     double persistence parameter.
	 *
	 */
	public double convert_filtration_index(int fi) {
		return (double) fi;
	}


	/**
	 * Check the stream as much as possible.
	 *
	 * <p>
	 * @return     True if the stream is consistent, else false.
	 *
	 */
	public boolean verify() {
		SimplexTable faces = new SimplexTable(size());
		for(Simplex s : this) {
			Simplex[] bdy = s.boundaryArray();
			if (bdy != null) {
				for(Simplex f : bdy) {
					Simplex tmp = faces.get(f);
					if ((tmp == null) || (tmp.findex() > s.findex()))
						return false;
				}
			}
			faces.put(s);
		}
		return true;
	}

	public enum ComparisonType { LT, LE, EQ, GE, GT };

	// As we are constructing the stream contents, we need to temporarily
	// store simplices in a FIFO queue. Since we want to deal with the case
	// where we have a LOT of simplices in the stream, we make some
	// special-purpose classes with a very small amounts of overhead. NOTE:
	// The FIFO queue functionality requires using both the Tail, to
	// which objects are added, and the Head, from which they are
	// extracted.

	/**
	 * An instance of <code>Tail</code> provides the FI part of a FIFO
	 * queue. To retrieve the enqueued simplices in FO order, attach a
	 * <code>Head</code> to this instance.
	 */
	public static class Tail {

		static final int ENTRIES_DEFAULT_LENGTH = 100;
		Simplex[] entries;
		int current;
		Tail next;

		// make an empty SimplexStream.Segment with the default length
		public Tail() {
			entries = new Simplex[ENTRIES_DEFAULT_LENGTH];
			current = 0;
			next = null;
		}

		// make an empty SimplexStream.Segment with the given length
		public Tail(int len) {
			entries = new Simplex[len];
			current = 0;
			next = null;
		}

		public String toString() {
			return String.format("[QT(%d:%d): {%d}%s]",
					current, entries.length, 
					System.identityHashCode(entries),
					(next != null)?("->"+next.toString()):"");
		}

		public Tail enqueue(Simplex s) {
			if (current < entries.length) {
				assert(next == null);
				entries[current++] = s;
				return this;
			} else {
				next = new Tail(entries.length);
				next.entries[next.current++] = s;
				return next;
			}
		}
	}

	/**
	 * A <code>Head</code> provides the FO part of a FIFO queue when
	 * attached to a <code>Tail</code> to which simplices have been
	 * enqueued.
	 *
	 */
	public static class Head {

		Simplex[] entries;
		int current;
		Tail orig;

		// don't use
		protected Head() {
		}

		// make a head for a Tail
		public Head(Tail q) {
			entries = q.entries;
			current = q.current;
			orig = q;
		}

		// Duplicate a Head. Note that interating with a copy has no effect
		// on the original, and no effect on the underlying Tail.
		public Head copy() {
			Head tmp = new Head();
			tmp.entries = entries;
			tmp.current = current;
			tmp.orig = orig;
			return tmp;
		}

		public String toString() {
			return String.format("[QH(%d:%d): {%d} (%s)]",
					current, entries.length, 
					System.identityHashCode(entries),
					orig.toString());
		}

		/**
		 * Get the next entry from the underlying Queue.
		 *
		 * <p> 
		 * This only works is we don't outrun the underlying Tail.
		 * <p>
		 * @return     next Simplex in the Queue.
		 *
		 */
		public Simplex nextEntry() {
			if (current == entries.length) {
				// System.out.printf("nextEntry() for %s\n", toString());
				Tail n = orig.next;
				entries = n.entries;
				current = 0;
				orig = n;
			}
			return entries[current++];
		}

		/**
		 * Compare one Head to another.
		 *
		 * <p> This is a very fast comparison that will only work if both
		 * Head instances are heads for the same Tail. 
		 * <p>
		 * @param      q  Head to compare
		 * @return     true if this is prior to q, else false.
		 *
		 */
		public boolean lessThan (Head q) {
			return ((entries != q.entries) || (current < q.current));
		}


		/**
		 * Check equality of Head with another.
		 *
		 * <p> This is used only by assertions.
		 * <p>
		 * @param      q  Head to compare
		 * @return     true if this is equal to q, else false.
		 *
		 */
		public boolean eql(Head q) {
			return ((entries == q.entries) || (current == q.current));
		}
	}

	/**
	 * <code>Stack</code> is a multiway segmented stack for sorted storage
	 * of simplices.
	 *
	 * <p> The class <code>SimplexStream.Stack</code> provides a simple,
	 * fast, scalable implementation of SimplexStream. Instances are arrays
	 * of segmented stacks of simplices, simplices are less than,
	 * equivalent, or greater than one another (for the purposes of the
	 * Persistence computations) if and only if the corresponding indices
	 * for their segmented stacks are <, ==, or >. In addition, the next()
	 * method drops references to the segments as soon as possible, which
	 * means that the stack consumes progressively less memory as its
	 * contents are processed.
	 */
	public static class Stack extends SimplexStream {

		// Rather like the Queue code above, but this is even simpler --
		// the building block of a segmented stack.
		protected static class Segment {
			protected final Simplex[] entries;
			protected int current;
			protected Segment next;

			// don't use
			protected Segment() {
				entries = null;
				current = 0;
				next = null;
			}

			// make an empty SimplexStream.Segment with a specified length
			protected Segment(int len) {
				entries = new Simplex[len];
				current = 0;
				next = null;
			}

			public String toString() {
				return String.format("[Seg(%d:%d): {%d}%s]",
						current, entries.length, 
						System.identityHashCode(entries),
						(next != null)?("->"+next.toString()):"");
			}

			protected Segment push(Simplex s) {
				if (current < entries.length) {
					entries[current++] = s;
					return this;
				} else {
					Segment tmp = new Segment(entries.length);
					tmp.next = this;
					tmp.entries[tmp.current++] = s;
					return tmp;
				}
			}
		}

		protected static int STACK_SEGMENT_LENGTH = 10;

		protected final Segment[] segments;
		protected final int findex_bound;
		protected final int dimension_bound;
		protected final int segment_length;
		protected int size;

		protected int segments_index;
		protected int simplex_index;

		// don't use
		protected Stack() {
			segment_length = 0;
			segments = null;
			dimension_bound = 0;
			findex_bound = 0;
			segments_index = 0;
			simplex_index = 0;
			size = 0;
		}

		/**
		 * Constructor for Stack.
		 *
		 * <p>
		 * Make a multi-way stack that automatically sorts simplices.
		 * <p>
		 * @param      max_findex Maximum Filtration Index we'll use.
		 * @param      max_d Maximum dimension of a Simplex to be pushed.
		 *
		 */
		public Stack(int max_findex, int max_d) {
			segment_length = STACK_SEGMENT_LENGTH;
			dimension_bound = (max_d + 1);
			findex_bound = (max_findex + 1);
			segments = new Segment[dimension_bound * findex_bound];
			segments_index = 0;
			simplex_index = 0;
			size = 0;
		}

		public String toString() {
			return String.format("[STK(%d,%d): %d/%d, si=%d/%d]",
					dimension_bound, findex_bound, 
					segments_index, segments.length,
					simplex_index, segment_length);
		}

		protected int simplexIndex(Simplex s) {
			assert((s.findex() < findex_bound) && (s.dimension() < dimension_bound));
			return (s.findex() * dimension_bound) + s.dimension();
		}

		/**
		 * Push a Simplex into the stream.
		 *
		 * <p> Add the Simplex s to the stream so that it will be retrieved
		 * in the proper order. 
		 * <p>
		 * @param      s  Simplex instance to push.
		 * @return     s
		 *
		 */
		public Simplex push(Simplex s) {
			int index = simplexIndex(s);
			size++;
			if (segments[index] == null) 
				segments[index] = new Segment(segment_length);
			segments[index] = (segments[index]).push(s);
			return s;
		}

		/**
		 * How many simplices are in the stream?
		 *
		 * <p>
		 * @return     The number of simplices in the stream.
		 *
		 */
		public int size() {
			return size;
		}

		/**
		 * Max dimension of simplices in the stream.
		 *
		 * <p>
		 * @return     Max dimension of simplices in the stream.
		 *
		 */
		public int maxDimension() {
			return (dimension_bound - 1);
		}

		/**
		 * Is there a next Simplex in the stream?
		 *
		 * <p>
		 * @return     Return true if the stream is not yet empty.
		 *
		 */
		public boolean hasNext() {
			if (segments[segments_index] != null) {
				if (simplex_index < segments[segments_index].current)
					return true;
				else {
					simplex_index = 0;
					Segment next = segments[segments_index].next;
					segments[segments_index] = next;
					if (next != null) {
						assert(0 < next.current);
						return true;
					}
				}
			}

			assert(segments[segments_index] == null);
			assert(simplex_index == 0);

			while ((segments_index < segments.length) && 
					(segments[segments_index] == null))
				segments_index++;

			if (segments_index == segments.length)
				return false;

			assert(segments[segments_index] != null);
			assert(0 < segments[segments_index].current);
			return true;
		}

		/**
		 * Return the next Simplex in the stream. This operations
		 * <em>removes</em> the simplex returned from the stream, so
		 * repeated iterations are impossible with the method.
		 *
		 * <p>
		 * @return     The smallest remaining Simplex instance.
		 *
		 */
		public Simplex next() {
			Segment seg = segments[segments_index];
			Simplex s;
			if (seg != null) {
				if (simplex_index < seg.current) {
					s = seg.entries[simplex_index];
					seg.entries[simplex_index++] = null;
					assert(size > 0);
					size--;
					return s;
				} else {
					simplex_index = 0;
					seg = segments[segments_index] = segments[segments_index].next;
					if (seg != null) {
						assert(0 < seg.current);
						s = seg.entries[simplex_index];
						seg.entries[simplex_index++] = null;
						assert(size > 0);
						size--;
						return s;
					}
				}
			}

			assert(segments[segments_index] == null);
			assert(simplex_index == 0);

			while ((segments_index < segments.length) && 
					((seg = segments[segments_index]) == null))
				segments_index++;

			if (segments_index == segments.length)
				return null;

			assert(segments[segments_index] != null);
			assert(0 < seg.current);
			s = seg.entries[simplex_index];
			seg.entries[simplex_index++] = null;
			assert(size > 0);
			size--;
			return s;
		}

		/**
		 * Instances provide Iterator<Simplex> for non-destructive
		 * iterating over Stack entries. That is, repeated iteration over a
		 * Stack instance is possible with multiple instances of this
		 * class.
		 */
		protected static class StackIterator implements Iterator<Simplex> {
			protected Segment[] segments;
			protected int segments_index;
			protected Segment current_seg;
			protected int simplex_index;
			protected int fixed_dimension;
			protected final int dimension_bound;

			protected StackIterator() {
				segments = null;
				segments_index = 0;
				current_seg = null;
				simplex_index = 0;
				fixed_dimension = -1;
				dimension_bound = 0;
			}

			public StackIterator(Stack stack) {
				segments = stack.segments;
				segments_index = 0;
				current_seg = null;
				simplex_index = 0;
				fixed_dimension = -1;
				dimension_bound = stack.dimension_bound;
			}

			public StackIterator(Stack stack, int dimension) {
				segments = stack.segments;
				segments_index = -1;
				current_seg = null;
				simplex_index = 0;
				if ((dimension < 0) || (dimension >= stack.dimension_bound))
					throw new IllegalArgumentException(dimension + 
							" must be >= 0 and <= " + 
							stack.dimension_bound); 
				fixed_dimension = dimension;
				dimension_bound = stack.dimension_bound;
			}

			/**
			 * Returns <tt>true</tt> if the iterator has more simplices.
			 *
			 * @return <tt>true</tt> if the iterator has more simplices, else
			 * <tt>false</tt>.
			 */
			public boolean hasNext() {
				while(true) {
					while (current_seg == null) {
						if (segments_index >= segments.length)
							return false;
						else {
							if (fixed_dimension >= 0) {
								if (segments_index < 0)
									segments_index = fixed_dimension;
								else
									segments_index += dimension_bound;
								if (segments_index >= segments.length)
									return false;
								else
									current_seg = segments[segments_index];
							} else
								current_seg = segments[segments_index++];
						}
					}
					while (current_seg != null) {
						while ((simplex_index < current_seg.current) &&
								(current_seg.entries[simplex_index] == null))
							simplex_index++;
						if (simplex_index < current_seg.current)
							return true;
						else {
							simplex_index = 0;
							current_seg = current_seg.next;
						}
					}
				}
			}

			/**
			 * Returns the next Simplex in the iteration of the Stack.
			 * Used in concert with the {@link #hasNext()} method returns
			 * return each Simplex in the Stack exactly once. Does
			 * <em>not</em> have any side effects on the stream.
			 * <p>
			 * @return the next Simplex in the Stack.
			 * @exception NoSuchElementException Stack has no more elements.
			 */
			public Simplex next() {
				while(true) {
					while (current_seg == null) {
						if (segments_index >= segments.length)
							throw new NoSuchElementException();
						else {
							if (fixed_dimension >= 0) {
								if (segments_index < 0)
									segments_index = fixed_dimension;
								else
									segments_index += dimension_bound;
								if (segments_index >= segments.length)
									throw new NoSuchElementException();
								else
									current_seg = segments[segments_index];
							} else
								current_seg = segments[segments_index++];
						}
					}
					while (current_seg != null) {
						while ((simplex_index < current_seg.current) &&
								(current_seg.entries[simplex_index] == null))
							simplex_index++;
						if (simplex_index < current_seg.current) {
							return current_seg.entries[simplex_index++];
						} else {
							simplex_index = 0;
							current_seg = current_seg.next;
						}
					}
				}
			}

			/**
			 * Unsupported remove() operation.
			 *
			 * @exception UnsupportedOperationException 
			 */
			public void remove() {
				throw new UnsupportedOperationException();
			}
		}

		/**
		 * Make a non-destructive iterator for the Stack.
		 * <p>
		 *
		 * @return  Iterator<Simplex> instance for the stack.
		 *
		 * @see        java.util.Iterator
		 */
		public Iterator<Simplex> iterator() {
			return new StackIterator(this);
		}

		/**
		 * Make a non-destructive iterator for fixed dimensional entries in the Stack.
		 * <p>
		 *
		 * @param      d  dimension of entries we want to iterate over
		 * @return  Iterator<Simplex> instance for the stack.
		 *
		 * @see        java.util.Iterator
		 */
		public Iterator<Simplex> iterator(int d) {
			return new StackIterator(this, d);
		}
	}
}
