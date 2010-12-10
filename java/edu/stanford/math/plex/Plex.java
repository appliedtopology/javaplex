package edu.stanford.math.plex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * The <code>Plex</code> class provides some utility functions that don't
 * have a clearly defined home, and some entry points into plex library
 * code that are especially useful when in matlab. It is (apparently)
 * impossible in matlab to execute code like <code>new PlexClass()</code>,
 * so we provide static methods for creating Plex objects in matlab. The
 * naming convention for these methods is that the constructor for
 * instances of <code>PlexClass</code> will be named
 * <code>PlexClass()</code>, and its argument spectrum will be whatever the
 * usual form (or forms) of the associated constuctor are.
 *
 * @version $Id$
 */
public class Plex {

	protected static Random rand = new Random();

	// Compute the size of the intersection of v1 and v2, assuming that no
	// elements appear in either v1 or v2 more than once. This method has the
	// advantage that it doesn't need the arguments to sorted. If the sets
	// are already sorted or we actually want the intersection, see below.
	public static int intersection_count(int[] v1, int[] v2) {
		int[] tmp = new int[v1.length + v2.length];
		if (tmp.length == 0)
			return 0;
		int tmp_counter = 0;
		for (int i = 0; i < v1.length; i++)
			tmp[tmp_counter++] = v1[i];
		for (int i = 0; i < v2.length; i++)
			tmp[tmp_counter++] = v2[i];
		assert(tmp_counter == tmp.length);
		Arrays.sort(tmp);

		int intersection_counter = 0;
		int current_tmpval = tmp[0];
		int current_hits = 1;
		for (int i = 1; i < tmp.length; i++) {
			int x = tmp[i];
			if (x == current_tmpval)
				current_hits++;
			else {
				if (current_hits > 1) {
					assert(current_hits == 2);
					intersection_counter++;
				}
				current_hits = 1;
				current_tmpval = x;
			}
		}

		return intersection_counter;
	}

	/**
	 * Compute the overlap size for two sorted arrays using the zipper algorithm.
	 *
	 * @param      v1  First sorted array.
	 * @param      v2  Second sorted array.
	 * @return     The number of common elements of v1 and v2.
	 *
	 */
	public static int sorted_intersection_count(int[] v1, int[] v2) {
		int count = 0;

		int i = 0;
		int i_max = v1.length-1;

		int j = 0;
		int j_max = v2.length-1;

		if ((i > i_max) || (j > j_max))
			return count;

		int max_xi = v1[i_max];
		int max_xj = v2[j_max];

		int xi = v1[i];
		if (xi > max_xj) return count;
		int xj = v2[j];
		if (xj > max_xi) return count;

		while (true) {
			if (xi == xj) {
				count++;
				i++;
				j++;
				if ((i > i_max) || (j > j_max))
					return count;
				else {
					xi = v1[i];
					if (xi > max_xj) return count;
					xj = v2[j];
					if (xj > max_xi) return count;
				}
			} else if (xi < xj) {
				i++;
				xi = v1[i];
				if (xi > max_xj) return count;
			} else {
				j++;
				xj = v2[j];
				if (xj > max_xi) return count;
			}
		}
	}


	/**
	 * Take subset_size distinct entries from initial segment of length
	 * set_size in set[] and put them in the initial segment of subset[]. The
	 * ordering of the entries is preserved -- that is, if set[i] and
	 * set[i+d] both go into subset, the index of set[i] is less than that of
	 * set[i+d].  <p>
	 * @param      set an int[] from which to select.
	 * @param      set_size  an int specifying how many entries to use.
	 * @param      subset an int[] in which to deposit the selections.
	 * @param      subset_size an int specifying how many entries to select.
	 */
	public static void random_subset(int[] set, int set_size, 
			int[] subset, int subset_size) {
		if (subset_size > set_size)
			throw new IllegalArgumentException("Requested subset is larger than argument set.");
		if (subset_size > subset.length)
			throw new IllegalArgumentException("Requested subset is larger than storage given.");
		if (set_size > set.length)
			throw new IllegalArgumentException("Set storage smaller than argument size.");
		if (subset_size <= 0)
			return;

		int[] tmp = new int[set_size];
		random_permutation(tmp);
		for (int i = 0; i < subset_size; i++) {
			subset[i] = tmp[i];
		}
		Arrays.sort(subset, 0, subset_size);
		for (int i = 0; i < subset_size; i++) {
			subset[i] = set[subset[i]];
		}
	}

	/**
	 * Take subset.length distinct entries from set[], and put them in
	 * subset[]. 
	 * <p>
	 * @param      set an int[] from which to select.
	 * @param      subset an int[] in which to deposit the selections.
	 */
	public static void random_subset(int[] set, int[] subset) {
		random_subset(set, set.length, subset, subset.length);
	}

	/**
	 * Take subset_size distinct entries from the integers 1 through N,
	 * and put them in the initial segment of subset[].
	 * <p>
	 * @param      N  an int specifying the range from which to choose the subset.
	 * @param      subset an int[] in which to deposit the selections.
	 * @param      subset_size  an int specifying how many entries to use.
	 */
	public static void random_subset(int N, int[] subset, int subset_size) {
		if (subset_size > subset.length)
			throw new IllegalArgumentException("Requested subset is larger than storage given.");
		if (subset.length <= 0)
			return;
		int[] tmp = new int[N];
		random_permutation(tmp);
		// The entries of tmp range from 0 to N-1, so we need to add 1 to each of them.
		for (int i = 0; i < subset_size; i++) {
			subset[i] = tmp[i]+1;
		}
		Arrays.sort(subset, 0, subset_size);
	}

	/**
	 * Take subset.length distinct entries from the integers 1 through N,
	 * and put them in subset[].
	 * <p>
	 * @param      N  an int specifying the range from which to choose the subset.
	 * @param      subset an int[] in which to deposit the selections.
	 */
	public static void random_subset(int N, int[] subset) {
		random_subset(N, subset, subset.length);
	}

	// confirm that this is a permutation
	protected static boolean is_permutation (int[] perm) {
		int[] tmp = new int[perm.length];
		for (int i = 0; i < perm.length; i++)
			tmp[perm[i]]++;
		for (int i = 0; i < perm.length; i++) {
			if (tmp[i] != 1)
				return false;
		}
		return true;
	}

	/**
	 * Make a random permutation of the integers 0 through perm.length-1
	 * and put them in perm.
	 * <p>
	 * @param      perm  an int[] to hold a random permutation.
	 */
	public static void random_permutation(int[] perm) {
		if (perm.length <= 0)
			return;
		// start with the identity
		for (int i = 0; i < perm.length; i++)
			perm[i] = i;
		for (int current = perm.length; current > 0; current--) {
			int swap_to = rand.nextInt(current);
			int swap_from = current - 1;
			int tmp = perm[swap_to];
			perm[swap_to] = perm[swap_from];
			perm[swap_from] = tmp;
		}
		assert(is_permutation(perm));
	}

	/**
	 * Take an array of int and make a string.
	 * <p>
	 * @param      x  an int[] we want to examine.
	 * @param      max_entries  The maximum number of entries we wish to examine.
	 * @return     A String instance that "looks like" x.
	 */
	public static String toString(int[] x, int max_entries) {
		if (x == null)
			return "<null>";
		int max = Math.min(max_entries, x.length);
		String start = "{";
		if (max >= 1)
			start = start + " " + x[0];
		for (int i = 1; i < max; i++) {
			start = start + ", " + x[i];
		}
		if (max < x.length)
			start = start + " ...";
		return start + " }";
	}

	/**
	 * Take an array of int and make a string.
	 * <p>
	 * @param      x  an int[] we want to examine.
	 * @return     A String instance that "looks like" x.
	 */
	public static String toString(int[] x) {
		return toString(x, 100);
	}

	/**
	 * Take an array of byte and make a string.
	 * <p>
	 * @param      x  an byte[] we want to examine.
	 * @param      max_entries  The maximum number of entries we wish to examine.
	 * @return     A String instance that "looks like" x.
	 */
	public static String toString(byte[] x, int max_entries) {
		if (x == null)
			return "<null>";
		int max = Math.min(max_entries, x.length);
		String start = "{";
		if (max >= 1)
			start = start + " " + x[0];
		for (int i = 1; i < max; i++) {
			start = start + ", " + x[i];
		}
		if (max < x.length)
			start = start + " ...";
		return start + " }";
	}

	/**
	 * Take an array of byte and make a string.
	 * <p>
	 * @param      x  an byte[] we want to examine.
	 * @return     A String instance that "looks like" x.
	 */
	public static String toString(byte[] x) {
		return toString(x, 100);
	}

	/**
	 * Take an array of int[] and make a string.
	 * <p>
	 * @param      x  an int[][] we want to examine.
	 * @param      max_entries  The maximum number of entries we wish to examine.
	 * @return     A String instance that "looks like" x.
	 */
	public static String toString(int[][] x, int max_entries) {
		int max = Math.min(max_entries, x.length);
		String start = "{";
		if (max >= 1)
			start = start + " " + toString(x[0], max_entries) + "\n";
		for (int i = 1; i < max; i++) {
			start = start + ", " + toString(x[i], max_entries) + "\n";
		}
		if (max < x.length)
			start = start + " ...";
		return start + " }";
	}


	/**
	 * Take an int[][] and make a string.
	 * <p>
	 * @param      x  an int[][] we want to examine.
	 * @return     A String instance that "looks like" x.
	 */
	public static String toString(int[][] x) {
		return toString(x, 100);
	}

	/**
	 * Take an array of double and make a string.
	 * <p>
	 * @param      x  a double[] we want to examine.
	 * @param      max_entries  The maximum number of entries we wish to examine.
	 * @return     A String instance that "looks like" x.
	 */
	public static String toString(double[] x, int max_entries) {
		if (x == null)
			return "<null>";
		int max = Math.min(max_entries, x.length);
		String start = "{";
		if (max >= 1)
			start = start + String.format(" %.3g", x[0]);
		for (int i = 1; i < max; i++) {
			start = start + String.format(", %.3g", x[i]);
		}
		if (max < x.length)
			start = start + " ...";
		return start + " }";
	}

	/**
	 * Take an array of double and make a string.
	 * <p>
	 * @param      x  a double[] we want to examine.
	 * @return     A String instance that "looks like" x.
	 */
	public static String toString(double[] x) {
		return toString(x, 100);
	}


	/**
	 * Take an array of double[] and make a string.
	 * <p>
	 * @param      x  an double[][] we want to examine.
	 * @param      max_entries  The maximum number of entries we wish to examine.
	 * @return     A String instance that "looks like" x.
	 */
	public static String toString(double[][] x, int max_entries) {
		int max = Math.min(max_entries, x.length);
		String start = "{";
		if (max >= 1)
			start = start + " " + toString(x[0], max_entries) + "\n";
		for (int i = 1; i < max; i++) {
			start = start + ", " + toString(x[i], max_entries) + "\n";
		}
		if (max < x.length)
			start = start + " ...";
		return start + " }";
	}



	/**
	 * Take an double[][] and make a string.
	 * <p>
	 * @param      x  an double[][] we want to examine.
	 * @return     A String instance that "looks like" x.
	 */
	public static String toString(double[][] x) {
		return toString(x, 100);
	}



	/**
	 * Take an array of long and make a string.
	 * <p>
	 * @param      x  a long[] we want to examine.
	 * @param      max_entries  The maximum number of entries we wish to examine.
	 * @return     A String instance that "looks like" x.
	 */
	public static String toString(long[] x, int max_entries) {
		int max = Math.min(max_entries, x.length);
		String start = "[ ";
		for (int i = 0; i < max; i++) {
			start = start + " " + x[i];
		}
		if (max < x.length)
			start = start + " ...";
		return start + " ]";
	}

	/**
	 * Take an array of long and make a string.
	 * <p>
	 * @param      x  a long[] we want to examine.
	 * @return     A String instance that "looks like" x.
	 */
	public static String toString(long[] x) {
		return toString(x, 100);
	}


	/**
	 * Take subset_size distinct entries from the integers 0 through N-1,
	 * and put them in the initial segment of subset[].
	 * <p>
	 * @param      N  an int specifying the range from which to choose the subset.
	 * @param      subset an int[] in which to deposit the selections.
	 * @param      subset_size  an int specifying how many entries to use.
	 */
	public static void random_index_subset(int N, int[] subset, int subset_size) {
		if (subset_size > subset.length)
			throw new IllegalArgumentException("Requested subset is larger than storage given.");
		if (subset.length <= 0)
			return;

		int min_value = subset_size-1;
		int max_value = N-1;
		int current_index = subset_size - 1;
		int select_value = min_value;
		while (current_index >= 0) {
			if (max_value > min_value) 
				select_value = min_value + rand.nextInt(1 + (max_value - min_value));
			else 
				select_value = min_value;
			subset[current_index] = select_value;
			assert((select_value >= 0) && (select_value <= N-1));
			assert((current_index >= (subset_size-1)) || 
					(subset[current_index] < subset[current_index+1]));
			current_index--;
			max_value = select_value-1;
			min_value--;
		}
		assert(current_index == -1);
	}

	// Compare two int[] arguments, returning negative if less, 0 if equal,
	// and positive if greater so that we can use it in binary search.
	static int comparePtArrays (int[] pts1, int[] pts2) {
		int len1 = pts1.length;
		int len2 = pts2.length;
		int count = (len2 > len1)?len2:len1;
		for (int i = 0; i < count; i++) {
			if (pts1[i] != pts2[i])
				return (pts1[i] - pts2[i]);
		}
		return (len1 - len2);
	}

	// Compare two int[] arguments, returning TRUE if equal, else FALSE.
	static boolean equalPtArrays (int[] pts1, int[] pts2) {
		if (pts1 == pts2)
			return true;
		else if ((pts1 == null) || (pts2 == null))
			return false;
		else if (pts1.length != pts2.length)
			return false;
		else {
			for (int i = 0; i < pts1.length; i++) {
				if (pts1[i] != pts2[i])
					return false;
			}
			return true;
		}
	}

	// Find the first index i such that arg >= pts[i]. Returns -1 for failure.
	// We start from argument "lo", which is 
	protected static int firstSmallerPtIndex (int arg, int lo, int[] pts) {
		// Binary search.
		int hi = pts.length - 1;

		if ((lo > hi) || (pts[hi] > arg))
			return -1;
		else if (arg >= pts[lo])
			return lo;
		else {
			// Do a binary search. At the top of the loop, we always have
			// that (pts[lo] > arg), lo != hi, and (arg >= pts[hi]).
			while (lo < hi) {
				int mid = (lo + hi)/2;

				assert(lo < hi);
				assert(pts[lo] > arg);
				assert(pts[hi] <= arg);

				if (mid == lo)
					return hi;
				else if (pts[mid] > arg)
					lo = mid;
				else
					hi = mid;
			}
			// should never get here
			assert(false);
			return -1;
		}
	}

	// Returns TRUE if ipts is a subset of jpts.
	protected static boolean ptArrayIsSubset (int[] ipts, int[] jpts) {
		int j = 0;

		for (int i = 0; i < ipts.length; i++) {
			if ((ipts.length - jpts.length) > (i - j))
				// this the same as (ipts.length-i) > (jpts.length-j), which
				// would mean that we have more points to find than we have
				// places to look for them
				return false;

			j = firstSmallerPtIndex (ipts[i], j, jpts);
			if ((j < 0) || (ipts[i] != jpts[j]))
				// jpts is decreasing, so if the first entry is too small,
				// we'll never find a match
				return false;
			else
				j++;            
		}

		return true;
	}


	/**
	 * An array of Betti numbers (int[]) boxed up for convenience.
	 */
	public static class BettiNumbers {
		protected int[] numbers;

		// make sure the numbers array is okay.
		static protected boolean init_okay(int[] init) {
			for (int i = 0; i < init.length; i++) {
				if (init[i] < 0)
					return false;
			}
			if ((init.length == 0) || (init[init.length-1] == 0))
				return false;
			return true;
		}

		// unused
		protected BettiNumbers() { numbers = null; }

		/**
		 * Encapsulate a verified array of Betti numbers.
		 *
		 * <p>
		 * @param      init an int[] of betti numbers.
		 * @exception  IllegalArgumentException 
		 */        
		public BettiNumbers(int[] init) {
			if (!init_okay(init))
				throw new IllegalArgumentException("Bad betti numbers");
			numbers = new int[init.length];
			for(int i = 0; i < init.length; i++)
				numbers[i] = init[i];
		}

		/**
		 * Make BettiNumbers printable.
		 *
		 * <p>
		 * @return      String form of an array of Betti numbers.
		 */        
		public String toString() {
			String base = "BN{";
			for (int i = 0; i < numbers.length-1; i++) {
				base = base + String.format("%d, ", numbers[i]);
			}
			base = base + String.format("%d}", numbers[numbers.length-1]);
			return base;
		}

		/**
		 * Determine if two instances of BettiNumbers are equal.
		 *
		 * <p>
		 * param  obj    Object to check equality against.
		 * @return      True if equal, else false.
		 */        
		public boolean equals(Object obj) {
			if (!(obj instanceof BettiNumbers))
				return false;
			BettiNumbers b = (BettiNumbers) obj;
			return Plex.equalPtArrays(numbers, b.numbers);
		}
	}

	/**
	 * Summarize the semi-infinite PersistenceIntervals.
	 *
	 * <p>
	 * @param      coll  A collection of PersistenceIntervals.
	 * @return     An instance of Plex.BettiNumbers.
	 */
	public static BettiNumbers FilterInfinite(PersistenceInterval[] coll) {
		int max_dimension = 0;
		for (PersistenceInterval i : coll) {
			if (i.infiniteExtent() && (i.dimension > max_dimension))
				max_dimension = i.dimension;
		}
		int[] results = new int[max_dimension+1];
		for (PersistenceInterval i : coll) {
			if (i.infiniteExtent())
				results[i.dimension]++;
		}
		return new BettiNumbers(results);
	}

	/**
	 * Summarize the semi-infinite PersistenceBasisIntervals.
	 *
	 * <p>
	 * @param coll A collection of PersistenceBasisIntervals.
	 * @return An isntance of Plex.BettiNumbers.
	 */
	public static BettiNumbers FilterInfinite(PersistenceBasisInterval[] coll) {
		int max_dimension = 0;
		for(PersistenceBasisInterval i : coll) {
			if (i.infiniteExtent() && (i.dimension > max_dimension))
				max_dimension = i.dimension;
		}
		int[] results = new int[max_dimension+1];
		for(PersistenceBasisInterval i : coll) {
			if (i.infiniteExtent())
				results[i.dimension]++;
		}
		return new BettiNumbers(results);
	}

	/**
	 * Create an empty SimplexStream.Stack.
	 *
	 * <p> The object returned from this method can be used to create a new
	 * SimplexStream from the contents of another one, or using an
	 * Interator<Simplex> constructed from such a stream.  <p>
	 *
	 * @param      max_findex   Largest filtration index used by entries in this stream.
	 * @param      max_dimension Maximum dimension of Simplex instances stored in this stream.
	 * @return    An empty SimplexStream.Stack.
	 *
	 */
	public static SimplexStream.Stack SimplexStack(int max_findex, int max_dimension) {
		return new SimplexStream.Stack(max_findex, max_dimension);
	}


	/**
	 * Create a Persistence instance.
	 *
	 * <p> Used to calculate persistent homology; e.g., p.computeIntervals(stream).
	 *
	 * @return    A new Persistence instance.
	 *
	 */
	public static Persistence Persistence () {
		return new Persistence();
	}

	/**
	 * Create a PersistenceBasis instance.
	 *
	 * <p> Used to calculate persistent homology returning bases for the resulting intervals;
	 * e.g., p.computeIntervals(stream).
	 *
	 * @return    A new PersistenceBasis instance.
	 *
	 */
	public static PersistenceBasis PersistenceBasis () {
		return new PersistenceBasis();
	}


	/**
	 * Create a random EuclideanArrayData instance.
	 *
	 * <p>
	 *
	 * @param      num_pts  How many points.
	 * @param      dimension Dimension of the space.
	 * @return    A randomly generated EuclideanArrayData instance.
	 *
	 */
	public static EuclideanArrayData  EuclideanArrayData(int num_pts, int dimension) {
		return new EuclideanArrayData(num_pts, dimension);
	}

	/**
	 * Create a EuclideanArrayData instance from a data array.
	 *
	 * <p>
	 *
	 * @param     data  The data.
	 * @param     dimension  The dimension of the data.
	 * @return    EuclideanArrayData instance encapsulating the argument data.
	 *
	 */
	public static EuclideanArrayData EuclideanArrayData(double[] data, int dimension) {
		return new EuclideanArrayData(data, dimension);
	}

	/**
	 * Create a EuclideanArrayData instance from a double[][].
	 *
	 * <p>
	 *
	 * @param     data  The data.
	 * @return    EuclideanArrayData instance encapsulating the argument data.
	 *
	 */
	public static EuclideanArrayData EuclideanArrayData(double[][] data) {
		return new EuclideanArrayData(data);
	}

	/**
	 * Create a EuclideanArrayData instance from a text file containing a
	 * distance matrix.
	 *
	 * <p>
	 *
	 * @param     filename  The name of the datafile.
	 * @return    EuclideanArrayData instance encapsulating the argument data.
	 *
	 */
	public static EuclideanArrayData EuclideanArrayData(String filename) {
		return new EuclideanArrayData(readMatrix(filename));
	}

	/**
	 * Read a double[][] from a stream. The format is fairly general, where
	 * the elements are separated by whitespace and all the elements for each
	 * row appear on a single line, and the last row is followed by a blank
	 * line.
	 * @param input A Reader instance.
	 * @return The matrix as a double[][].
	 */
	public static double[][] readMatrix(Reader input) {
		try {
			StreamTokenizer tokenizer= new StreamTokenizer(input);

			// Although StreamTokenizer will parse numbers, it doesn't recognize
			// scientific notation (E or D); however, Double.valueOf does.
			// The strategy here is to disable StreamTokenizer's number parsing.
			// We'll only get whitespace delimited words, EOL's and EOF's.
			// These words should all be numbers, for Double.valueOf to parse.

			tokenizer.resetSyntax();
			tokenizer.wordChars(0,255);
			tokenizer.whitespaceChars(0, ' ');
			tokenizer.eolIsSignificant(true);
			ArrayList<Double> first_row = new ArrayList<Double>();
			ArrayList<double[]> all_rows = new ArrayList<double[]>();

			// Ignore initial empty lines
			while (tokenizer.nextToken() == StreamTokenizer.TT_EOL);
			if (tokenizer.ttype == StreamTokenizer.TT_EOF)
				throw new java.io.IOException("Unexpected EOF on matrix read.");
			do {
				first_row.add(Double.valueOf(tokenizer.sval)); // Read & store 1st row.
			} while (tokenizer.nextToken() == StreamTokenizer.TT_WORD);

			int n = first_row.size();  // Now we've got the number of columns!
			double row[] = new double[n];
			for (int j=0; j<n; j++)  // extract the elements of the 1st row.
				row[j]=first_row.get(j).doubleValue();
			first_row.clear();
			all_rows.add(row);  // Now process the rows
			while (tokenizer.nextToken() == StreamTokenizer.TT_WORD) {
				// While non-empty lines
				all_rows.add(row = new double[n]);
				int j = 0;
				do {
					if (j >= n) throw new java.io.IOException
					("Row " + all_rows.size() + " is too long.");
					row[j++] = Double.valueOf(tokenizer.sval).doubleValue();
				} while (tokenizer.nextToken() == StreamTokenizer.TT_WORD);
				if (j < n) throw new java.io.IOException
				("Row " + all_rows.size() + " is too short.");
			}
			double[][] A = all_rows.toArray(new double[all_rows.size()][]);
			return A;
		} catch(IOException e) {
			// Make this a RuntimeException so this is easier to call from
			// the interpreter.
			throw new IllegalStateException(e.getMessage());
		}
	}

	/**
	 * Read a double[][] from a file. The format is fairly general, where
	 * the elements are separated by whitespace and all the elements for each
	 * row appear on a single line, and the last row is followed by a blank
	 * line.
	 * @param inputFilename An input file.
	 * @return The matrix as a double[][].
	 */
	public static double[][] readMatrix(String inputFilename) {
		InputStreamReader reader = null;
		try {
			reader = 
				new InputStreamReader(new FileInputStream(new File(inputFilename)));
		} catch(IOException e) {
			// Make this a RuntimeException so this is easier to call from
			// the interpreter.
			throw new IllegalStateException("Failed opening matrix file \"" +
					inputFilename + "\": " + e.getMessage());
		} 

		try {
			return readMatrix(reader);
		} catch(IllegalStateException e) {
			// Catch and rethrow so we get the filename in it
			throw new IllegalStateException("Failed reading matrix from file \"" +
					inputFilename + "\": " + e.getMessage());
		}
	}

	// make sure that the matrix is square
	protected static boolean d_okay(double[][] d) {
		for(int i = 0; i < d.length; i++) {
			if (d.length != d[i].length)
				return false;
		}
		return true;
	}

	/**
	 * Create a DistanceData instance.
	 *
	 * <p>
	 *
	 * @param      d  The distance matrix
	 * @return    A DistanceData instance.
	 *
	 */
	public static DistanceData  DistanceData(double[][] d) {
		assert d_okay(d);
		return new DistanceData(d);
	}

	/**
	 * Create a Torus with a given granulatity.
	 * <p>
	 *
	 * @param      n   Split the Torus into n^^d squares, return the corners.
	 * @return    PointData for a d-dimensional Torus.
	 *
	 */
	public static Torus Torus (int n, int d) {
		return new Torus(n, d);
	}

	/**
	 * Make a stream of the simplices in a Rips complex for given data set.
	 * <p>
	 * @param      delta Granularity to apply to the underlying metric. 
	 * @param      max_distance   Maximum edge length we will consider.
	 * @param      max_d   Maximum dimension of Simplex instances we construct.
	 * @param      data   Metric data to use in constructing stream.
	 *
	 * @return    A SimplexStream.Stack containing the Rips complex.
	 */
	public static RipsStream RipsStream(double delta, int max_d, 
			double max_distance, PointData data) {
		return new RipsStream(delta, max_d, max_distance, data);
	}

	/**
	 * Make an empty ExplicitStream. These are normally populated "by hand".
	 * <p>
	 *
	 * @return    An empty ExplicitStream.
	 */
	public static ExplicitStream ExplicitStream() {
		return new ExplicitStream();
	}

	/**
	 * Convert a SimplexStream into an explicit one.
	 *
	 * <p>
	 * @param      str A SimplexStream instance.
	 * @return     An ExplicitStream with equivalent Simplex entries.
	 *
	 */
	public static ExplicitStream makeExplicit(SimplexStream str) {
		return ExplicitStream.makeExplicit(str);
	}

	/**
	 * Make a new SimplexTable.
	 *
	 * <p>
	 *
	 * @param      limit   An upper bound on the number of simplices that 
	 *         will be interned in the table.
	 *
	 * @return    An empty SimplexTable.
	 *
	 * @see        edu.stanford.math.plex.SimplexTable#SimplexTable
	 */
	public static SimplexTable SimplexTable (int limit) {
		return new SimplexTable(limit);
	}

	/**
	 * Make a stream of the simplices in a Witness complex for data.
	 * <p>
	 * @param      delta Granularity to apply to the underlying metric.
	 * @param      max_d   Maximum dimension of Simplex instances we construct.
	 * @param      R   Maximum R we will use.
	 * @param      landmarks Indices of points to use as landmarks. The
	 *                        simplices constructed will use the landmark
	 *                        indices, rather than the indices into data.
	 * @param      data   Metric data to use in constructing stream.
	 *
	 * @return    A WitnessStream for the given data.
	 */
	public static WitnessStream WitnessStream(double delta, int max_d, double R, 
			int[] landmarks, PointData data) {
		return new WitnessStream(delta, max_d, R, landmarks, data);
	}


	/**
	 * Make a stream of the simplices in a lazy Witness complex for data.
	 * <p>
	 * @param      delta Granularity to apply to the underlying metric.
	 * @param      max_d   Maximum dimension of Simplex instances we construct.
	 * @param      R   Maximum R we will use.
	 * @param      nu     See <i>Topological estimation using witness complexes</i>, above.
	 * @param      landmarks Indices of points to use as landmarks. The
	 *                        simplices constructed will use the landmark
	 *                        indices, rather than the indices into data.
	 * @param      data   Metric data to use in constructing stream.
	 *
	 * @return    A LazyWitnessStream for the given data.
	 */
	public static LazyWitnessStream LazyWitnessStream(double delta, int max_d, 
			double R, int nu, int[] landmarks, 
			PointData data) {
		return new LazyWitnessStream(delta, max_d, R, nu, landmarks, data);
	}

	// Get rid of really short PersistenceInterval.Float instances.
	protected static PersistenceInterval.Float[] filter_pintervals
	(PersistenceInterval.Float[] vec, double delta) {
		int return_len = 0;
		for (int i = 0; i < vec.length; i++) {
			PersistenceInterval.Float p = vec[i];
			if (Math.abs(p.start - p.start) > delta)
				return_len++;
		}
		PersistenceInterval.Float[] return_value = 
			new PersistenceInterval.Float[return_len];
		return_len = 0;
		for (int i = 0; i < vec.length; i++) {
			PersistenceInterval.Float p = vec[i];
			if (Math.abs(p.start - p.start) > delta)
				return_value[return_len++] = vec[i];
		}
		return return_value;
	}

	/**
	 * Are two PersistenceInterval.Float[] arguments sufficiently equal?
	 *
	 * <p>
	 * @param      arg1 PersistenceInterval.Float[].
	 * @param      arg2 PersistenceInterval.Float[].
	 * @param      delta double
	 * @return     true if the arguments are equivalent up the precision of the stream and delta.
	 *
	 */
	public static boolean equalPersistenceIntervals(PersistenceInterval.Float[] arg1, 
			PersistenceInterval.Float[] arg2, 
			double delta) {
		if (arg1 == arg2)
			return true; 
		else {
			arg1 = filter_pintervals(arg1, delta);
			arg2 = filter_pintervals(arg2, delta);
			if (arg1.length != arg2.length)
				return false;
			for (int i = 0; i < arg1.length; i++) {
				PersistenceInterval.Float p1 = arg1[i];
				PersistenceInterval.Float p2 = arg2[i];
				if ((Math.abs(p1.start - p2.start) > 1.5 * delta) ||
						(Math.abs(p1.end - p2.end) > 1.5 * delta))
					return false;
			}
			return true;
		}
	}


	// Find the smallest "two decimal digit" double >= arg.
	protected static double normalizeUpperBound(double arg) {
		if (arg < 1.0) {
			double current = .1;
			while (current > arg) {
				current *= .1;
			}
			return (current * normalizeUpperBound(arg/current));
		} else if (arg > 10.0) {
			double current = 10.0;
			while (current < arg) {
				current *= 10.0;
			}
			return (current * normalizeUpperBound(arg/current));
		} else {
			assert((arg >= 1.0) || (arg <= 10.0));
			if ((arg == 1.0) || (arg == 10.0))
				return arg;
			else 
				return ((double)(((int)(10.0*arg)) + 1))/10.0;
		}
	}

	// Convert arg into ordered and sorted-by-dimension double[2], suitable for
	// printing.
	protected static double[][][] printable_intervals(PersistenceInterval[] arg) {
		PersistenceInterval[][] pi_tmp;
		double[][][] print_copy;
		{
			int[] dimensions = new int[0];
			int max_dimension = -1;
			for (int i = 0; i < arg.length; i++) {
				if (arg[i].dimension >= dimensions.length) {
					int[] new_dimensions = new int[Math.max(arg[i].dimension+1, 2*dimensions.length)];
					for (int j = 0; j < dimensions.length; j++) {
						new_dimensions[j] = dimensions[j];
					}
					dimensions = new_dimensions;
				}
				max_dimension = Math.max(max_dimension, arg[i].dimension);
				dimensions[arg[i].dimension]++;
			}
			pi_tmp = new PersistenceInterval[max_dimension+1][];
			print_copy = new double[max_dimension+1][][];
			for (int i = 0; i < pi_tmp.length; i++) {
				pi_tmp[i] = new PersistenceInterval[dimensions[i]];
			}
		}
		{
			int[] counters = new int[pi_tmp.length];
			for (int i = 0; i < arg.length; i++) {
				int d = arg[i].dimension;
				pi_tmp[d][counters[d]++] = arg[i];
			}
			for (int i = 0; i < pi_tmp.length; i++) {
				Arrays.sort(pi_tmp[i]);
			}
		}
		for (int i = 0; i < pi_tmp.length; i++) {
			print_copy[i] = new double[pi_tmp[i].length][];
			for (int j = 0; j < pi_tmp[i].length; j++) {
				print_copy[i][j] = pi_tmp[i][j].toDouble();
			}
		}
		return print_copy;
	}

	/**
	 * Plot an array of Plex barcodes (that is, a PersistenceInterval[]).
	 *
	 * <p>
	 * @param      arg PersistenceInterval[].
	 * @param      label A String to use as the label.
	 * @param upperBound A limit on the plot width. This limit is not strict.
	 *                   The actual plotted range will be at least this size,
	 *                   and no larger than 10% greater.
	 */
	public static void plot(PersistenceInterval[] arg, String label, double upperBound) {
		double[][][] print_tmp = printable_intervals(arg);
		for (int i = 0; i < print_tmp.length; i++) {
			if (print_tmp[i].length > 0) {
				BCPlot.doPlot(String.format("%s: Dimension %d", label, i),
						print_tmp[i], normalizeUpperBound(upperBound));
			}
		}
	}

	/**
	 * Plot an array of Plex barcodes computed with bases.
	 *
	 * <p>
	 * @param arg PersistenceBasisInterval[].
	 * @param label A string to use as a label.
	 * @param upperBound A limit on the plot width. This limit is not strict.
	 *            The actual plotted range will be at least this size, and 
	 *            no larger than 10% greater.
	 */
	public static void plot(PersistenceBasisInterval[] arg,
			String label, 
			double upperBound) {
		PersistenceInterval[] newarg = new PersistenceInterval[arg.length];
		for(int i = 0; i < arg.length; i++)
			newarg[i] = arg[i].toPersistenceInterval();
		plot((PersistenceInterval[])newarg, label, upperBound);
	}

	/**
	 * Make a scatterplot of a PersistenceInterval[].
	 *
	 * <p>
	 * @param      arg PersistenceInterval[].
	 * @param      label A String to use as the label.
	 * @param upperBound A limit on the plot width. This limit is not strict.
	 *                   The actual plotted range will be at least this size,
	 *                   and no larger than 10% greater.
	 */
	public static void scatter(PersistenceInterval[] arg, String label, double upperBound) {
		double[][][] print_tmp = printable_intervals(arg);
		for (int i = 0; i < print_tmp.length; i++) {
			if (print_tmp[i].length > 0) {
				BCPlot.doScatter(String.format("%s: Dimension %d", label, i),
						print_tmp[i], normalizeUpperBound(upperBound));
			}
		}
	}

	/** 
	 * Make a scatterplot of a PersistenceBasisInterval[].
	 *
	 * <p>
	 * @param   arg PersistenceBasisInterval[].
	 * @param      label A String to use as the label.
	 * @param upperBound A limit on the plot width. This limit is not strict.
	 *                    The actual plotted range will be at least this size,
	 *                    and no larger than 10% greater.
	 */
	public static void scatter(PersistenceBasisInterval[] arg,
			String label,
			double upperBound) {
		PersistenceInterval[] newarg = new PersistenceInterval[arg.length];
		for(int i = 0; i < arg.length; i++) 
			newarg[i] = arg[i].toPersistenceInterval();
		scatter((PersistenceInterval[]) newarg, label, upperBound);
	}

	protected static boolean useMessageWindow = false;

	/**
	 * Use a PlexMessageWindow for messages?
	 *
	 * <p>
	 * @param      flag Set useMessageWindow.
	 */
	public static void useMessageWindow(boolean flag) {
		useMessageWindow = flag;
	}

	/**
	 * Should we use a PlexMessageWindow for messages?
	 *
	 * <p>
	 * @return true if a PlexMessageWindow is preferable, else false.
	 */
	public static boolean useMessageWindow() {
		return useMessageWindow;
	}

	/**
	 * Make a Plex message window.
	 *
	 * <p>
	 * @param      title Window title.
	 * @return     A PlexMessageWindow instance.
	 */
	public static PlexMessageWindow messageWindow(String title) {
		return new PlexMessageWindow(title);
	}

	/**
	 * Entry point to Plex from the java interpreter. 
	 *
	 * <p> This stub will, when time permits, be turned into something that
	 * allows users to make some non-trivial use of Plex from the java
	 * launcher.  
	 * <p>
	 *
	 * @param      args   Strings that will need to be parsed.
	 *
	 */
	public static void main(String[] args) {
		SimplexStream stream = TmpStream.PaperTestCase();
		Persistence P = new Persistence();
		PersistenceInterval[] intervals = P.computeIntervals(stream);
		for (PersistenceInterval i : intervals) {
			System.out.printf("%s\n", i.toString());
		}
		plot(intervals, "Main Test", 26.23);
	}
}
