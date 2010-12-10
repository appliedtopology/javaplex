package edu.stanford.math.plex;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A <code>SimplexTable</code> is an interning table for instances of
 * Simplex. That is, the simplices in the table are both key and
 * value. There is no removal operation, and putting a simplex equivalent
 * to one already in the table throws an IllegalArgumentException. The
 * largest table we can make will hold around 976M simplices. Attempting to
 * add a simplex to table that is full throwns an IllegalStateException.
 *
 * @version $Id$
 */
public class SimplexTable implements Iterable<Simplex> {

	// The simplest possible hashtable. We don't support removing things,
	// and an attempt to put a Simplex twice will throw an
	// IllegalArgumentException.

	protected int size = 0;
	protected int entry_limit = 0;
	protected int mask = 0;
	protected Simplex[] table;
	protected static final double LOAD_FACTOR = 1.4;
	protected int MAX_TABLE_SIZE = (1 << 30);
	protected int MAX_ENTRY_LIMIT = (int) Math.floor(MAX_TABLE_SIZE / LOAD_FACTOR);

	// don't use
	protected SimplexTable() {}


	/**
	 * Make a new SimplexTable.
	 *
	 * <p>
	 *
	 * You must make sure that limit is as large as the number of simplices
	 * you will be storing in the table, as the table will <em>NOT</em>
	 * grow, and attempting to add simplices past the limit throws an
	 * exception which isn't caught in the Persistence algorithm.
	 *
	 * <p>
	 *
	 * @param      limit   An upper bound on the number of simplices that 
	 *         will be interned in the table -- must be less than or equal 
	 *        to MAX_ENTRY_LIMIT (which is about 976M).
	 *
	 * @exception  IllegalArgumentException 
	 *
	 * @see        java.lang.System#getProperty(java.lang.String)
	 * @see        SecurityManager#checkPermission
	 */
	public SimplexTable(int limit) {
		entry_limit = limit;
		int required_table_limit = (int)Math.floor((LOAD_FACTOR * limit) + 1.0);
		if (required_table_limit > MAX_TABLE_SIZE)
			throw new IllegalArgumentException("SimplexTable size too large: " + 
					limit + " (max=" + 
					MAX_ENTRY_LIMIT + ")");
		int table_size  = 1;
		while (table_size < required_table_limit) 
			table_size <<= 1;
		if ((table_size - 1)/LOAD_FACTOR > entry_limit)
			entry_limit = (int)((table_size - 1)/LOAD_FACTOR);
		assert(table_size <= MAX_TABLE_SIZE);
		table = new Simplex[table_size];
		mask = table_size - 1;
	}

	// The obvious way to grow a table -- it isn't clear that there is any
	// point in trying to be more clever here.
	protected void grow_table(int newLimit) {
		if (newLimit > entry_limit) {
			SimplexTable tmp_table = new SimplexTable(newLimit);
			// transfer the contents of the table to the tmp new one
			for (int i = 0; i < table.length; i++) {
				Simplex tmp = table[i];
				if (tmp != null)
					tmp_table.put(tmp);
			}
			// clear the old array and copy the slots of the new table into the old one
			for (int i = 0; i < table.length; i++)
				table[i] = null;
			size = tmp_table.size;
			table = tmp_table.table;
			entry_limit = tmp_table.entry_limit;
			mask = tmp_table.mask;
			tmp_table.table = null;
			tmp_table.size = 0;
			tmp_table.entry_limit = 0;
			tmp_table.mask = 0;
		}
	}

	/**
	 * The number of simplices that have been interned.
	 * <p>
	 * @return     the number of simplices put in the table.
	 */
	public int size() {
		return size;
	}

	/**
	 * The number of simplices that can be interned before growing the table.
	 * <p>
	 * @return     the number of simplices that can be put in the table before growing it.
	 */
	public int limit() {
		return entry_limit;
	}

	/**
	 * Get the interned version of the Simplex s.
	 *
	 * <p> Because the CRC hashing -- which we use for simplices -- is
	 * good, we just mask the hash code for our initial index. We keep the
	 * table sufficiently empty that we don't get a lot of false hits, so
	 * we reprobe with increment 1 to improve locality of reference in
	 * these (potentially) very large tables. <p>
	 *
	 * @param      s   The simplex for which we want an interned version.
	 * @return     An interned version of s, or null if there is none.
	 */
	public Simplex get(Simplex s) {
		int hash = s.hashCode();
		int index = hash & mask;
		int reprobe_index = index;
		do {
			Simplex si = table[reprobe_index];
			if (si == null)
				// it's a miss
				return null;
			if (si.seq(s))
				// it's a hit
				return si;         
			reprobe_index++;
			if (reprobe_index >= mask)
				reprobe_index = 0;
		} while (reprobe_index != index);

		// we checked everything
		return null;
	}


	/**
	 * Intern a Simplex.
	 *
	 * <p> Intern s in the table. When used by the Persistence algorithm, s
	 * should be a Simplex from the SimplexStream, which means that it will
	 * the filtration index set. An attempt to intern the same simplex twice
	 * throws an exception. <p>
	 *
	 * @param      s   the Simplex to intern
	 * @return     the interned Simplex.
	 *
	 * @exception  IllegalStateException when attempting to grow too big.
	 * @exception  IllegalArgumentException when attempting to put a Simplex twice.
	 *
	 */
	public Simplex put(Simplex s) {
		int hash = s.hashCode();
		int index = hash & mask;
		int reprobe_index = index;
		if (size >= entry_limit) 
			grow_table(2 * entry_limit);
		do {
			Simplex si = table[reprobe_index];
			if (si == null) {
				// a blank space for our Simplex.
				size++;
				table[reprobe_index] = s;
				return s;
			}
			if (si.seq(s)) 
				throw new IllegalArgumentException("SimplexTable duplicate put: " + 
						s.toString());
			reprobe_index++;
			if (reprobe_index >= mask)
				reprobe_index = 0;
		} while (reprobe_index != index);

		// this shouldn't happen
		throw new IllegalStateException("SimplexTable implementation error");
	}


	/**
	 * Return the interned version of a Simplex.
	 *
	 * <p> Like put(), except no error if something is already in the table. <p>
	 *
	 * @param      s   the Simplex key
	 * @return     the interned Simplex.
	 *
	 * @exception  IllegalStateException when attempting to grow too big.
	 *
	 */
	public Simplex getInterned(Simplex s) {
		int hash = s.hashCode();
		int index = hash & mask;
		int reprobe_index = index;
		if (size >= entry_limit) 
			grow_table(2 * entry_limit);
		do {
			Simplex si = table[reprobe_index];
			if (si == null) {
				// a blank space for our Simplex.
				size++;
				table[reprobe_index] = s;
				return s;
			}
			if (si.seq(s)) 
				return si;

			reprobe_index++;
			if (reprobe_index >= mask)
				reprobe_index = 0;
		} while (reprobe_index != index);

		// this shouldn't happen
		throw new IllegalStateException("SimplexTable implementation error");
	}

	/**
	 * Make an iterator for the table.
	 * <p>
	 *
	 * @return  Iterator<Simplex> instance for the table.
	 *
	 * @see        java.util.Iterator
	 */
	public Iterator<Simplex> iterator() {
		return new SimpleTableIterator(this);
	}

	/**
	 * Instances provide Iterator<Simplex> for non-destructive iterating
	 * over entries. That is, repeated iteration over an SimplexTable
	 * instance is possible with multiple instances of this class.
	 */
	protected static class SimpleTableIterator implements Iterator<Simplex> {
		protected int current_index;
		protected int current_count;
		protected final int start_size;
		protected final Simplex[] vec;
		protected final SimplexTable parent;

		protected SimpleTableIterator(SimplexTable tbl) {
			current_index = -1;
			current_count = 0;
			start_size = tbl.size;
			vec = tbl.table;
			parent = tbl;
		}

		// Are the parent stream and the iterator consistent?
		protected boolean parent_is_consistent() {
			return ((parent.table == vec) && (parent.size == start_size));
		}

		/**
		 * Returns <tt>true</tt> if the iterator has more simplices.
		 *
		 * @return <tt>true</tt> if the iterator has more simplices, else
		 * <tt>false</tt>.
		 */
		public boolean hasNext() {
			if (!parent_is_consistent())
				return false;
			else
				return (current_count < start_size);
		}

		/**
		 * Returns the next Simplex in the iteration over the table.
		 * Used in concert with the {@link #hasNext()} method returns
		 * return each Simplex in the table exactly once. Does
		 * <em>not</em> have any side effects on the table.
		 * <p>
		 * @return the next Simplex in the table.
		 * @exception NoSuchElementException table has no more elements.
		 */
		public Simplex next() {
			if (!parent_is_consistent())
				throw new
				IllegalStateException("The parent SimplexTable (" + parent.toString() +
				") has changed since this iterator was created.");
			if (current_count >= start_size)
				throw new NoSuchElementException();
			Simplex tmp = vec[++current_index];
			while (tmp == null)
				tmp = vec[++current_index];
			current_count++;
			return tmp;
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
}
