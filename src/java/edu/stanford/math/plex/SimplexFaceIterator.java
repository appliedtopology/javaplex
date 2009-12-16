package edu.stanford.math.plex;

/**
 * The <code>SimplexFaceIterator</code> class.
 *
 * <p>A <code>SimplexFaceIterator</code> instance makes an iterator that
 * returns all of the faces of a Simplex (specified via a Simplex or a
 * SimplexHandle) in a given dimension range. It also keeps track of the
 * sign of the last one returned, so that this can be used to compute the
 * boundary of a simplex. 
 *
 * @version $Id$
 */
public class SimplexFaceIterator {

	protected int signOfLast;
	// The number of elements in the counter and current arrays -- the size
	// of the subsets that we are iterating over.
	protected final int k;
	// The number of elements in the vertices set -- the size of the
	// superset whose k-sets we are iterating over.
	protected final int N;
	// The actual vertex set that we are iterating over.
	protected final int[] vertices;
	// The k-length array of indices (that is , 0,...,N-1).
	protected final int[] counter;
	// The current "vertex subset".
	protected final int[] current;


	// Are there any more elements?
	public boolean hasNext() {
		return ((counter[k-1] < (N - 1)) || (counter[0] < (N - k)));
	}

	// This is a little tricky. We want a way to iterate over all of the
	// subsets of the vertex set, and return simplices for each of those
	// subsets. (These subsets are the lower dimensional faces of the
	// simplex.) If we consider the set with N elements to be {0,...,N-1},
	// then the k subsets of N are given by all possible {x0<...<x(k-1)}, so we
	// can enumerate this with a k vector where we iterate over all
	// possible such arrangements of values. It doesn't particularly matter
	// what order we use, so we generate the sets lexicographically since
	// this seems simplest to understand. Clearly the first subset in this
	// ordering will be [0,...,(k-1)], and for any subset S = [x0,....x(k-1)] where
	// x(k-1)<N, the next subset is [x0,...,x(k-1)+1]. In fact, for any S =
	// [x0,...,xj,..,x(k-1)], the next subset is [x0,...,x(j-1)] adjoined to
	// the next subset for [xj,...,x(k-1)]. Notice that we can consider
	// [xj,...,x(k-1)] just as a subset of {xj,...,N} (the xi are increasing).
	// Since the last entry in {xj,...,N} will be [(N-j)+1,...,N-1,N], all
	// we need to do to find the next entry after S is to find a j so that
	// [xj,...,x(k-1)] is not of that form. That is, we find the last subscript
	// j such that xj < N-(k-j). If there is no such index, then S =
	// [N-k,...,N-1] -- in other words, S is the last subset. If there is
	// such a j, then the next subset is [1+xj,2+xj,...] adjoined to
	// [x0,...,x(j-1)]. Therefore, if are any subsets, that is, if N>k>0,
	// we set an array we use as a "counter" to the initial value of
	// [0,...,k-2,k-2] (that is, the last entry is one less than it should
	// be), that when we ask for the first time for the "next" set, we get
	// the first set. 
	//
	// In practice our vertex sets are given as an array of indices
	// V[]=[v0,v1,...,v(N-1)], so the k-subsets are actually enumerated by
	// enumerating the indices -- that is we enumerate [x0,...,x(k-1)] in the
	// set [0,...N-1], and then the sets {V[x0],...,V[x(k-1)]} iterate over
	// all k-sets of V. This is the reason that the code here is using
	// 0-based counting.

	/**
	 * Get the next face in an iteration of all faces of dimension k.
	 *
	 * <p>
	 *
	 * @return     vertices for the next k-face.
	 *
	 */
	public int[] next () {
		int i;
		if (counter[k-1] < (N-1))
			counter[k-1]++;
		else {
			for (i = k-2; i >= 0; i--) {
				if (counter[i] < ((N-k)+i)) {
					int base = counter[i] + 1;
					for (int j = i; j < k; j++)
						counter[j] = base++;
					break;
				}
			}
		}
		for (i = 0; i < k; i++)
			current[i] = vertices[counter[i]];
		signOfLast = -signOfLast;
		return current;
	}


	protected SimplexFaceIterator() {
		// do not use
		k = 0;
		N = 0;
		counter = null;
		current = null;
		vertices = null;
		signOfLast = 0;
	}


	/**
	 * Construct a new iterator for faces of a simplex given by an array.
	 *
	 * <p>
	 *
	 * @param      vertices   An int[] specifying the vertices.
	 * @param dimension Dimension of the faces we care about.
	 *
	 */
	public SimplexFaceIterator(int[] vertices, int dimension) {
		if ((dimension >= 0) && (vertices.length > dimension)) {
			k = dimension + 1;
			N = vertices.length;

			if ((k+1) == N)
				signOfLast = -1;
			else
				signOfLast = 0;

			this.vertices = vertices;
			counter = new int[k];
			for (int i = 0; i < k-1; i++)
				counter[i] = i;
			counter[k-1] = k-2;
			current = new int[k];
		} else {
			k = 1;
			N = 1;
			counter = new int[1];
			current = new int[1];
			this.vertices = null;
			signOfLast = 0;
		}
	}


	/**
	 * Construct a new iterator for the faces specified dimension.
	 *
	 * <p>
	 *
	 * @param      s   Simplex whose faces are of interest.
	 * @param dimension Dimension of the faces we care about (often
	 *        dimension of the simplex - 1).
	 *
	 */
	public SimplexFaceIterator(Simplex s, int dimension) {
		this(s.vertices(), dimension);
	}

	/**
	 * Return the dimension of the faces generated by this iterator.
	 */    
	public int getDimension() {
		return k - 1;
	}

	/**
	 * Return the sign of the last face -- only useful when codimension is 1.
	 */    
	public int getSignOfLast() {
		return signOfLast;
	}
}
