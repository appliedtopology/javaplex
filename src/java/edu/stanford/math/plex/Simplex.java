package edu.stanford.math.plex;

/**
 * A <code>Simplex</code> is the basic unit for simplicial homology
 * calculations, literally. That is, the module in which simplicial
 * homology is defined is the free module over all simplices, so a simplex
 * is actually a basis element for this module. Formally, simplices are
 * elements of a set S of non-empty finite subsets of a set X which have
 * the property that if B is an element of S, and A is a non-empty subset
 * of B, then A is an element of S. Intuitively, if you imagine that the
 * points of X are in some n-dimensional Euclidean space, then a simplex is
 * that subset of Euclidean space that is given by the affine combinations
 * of its "corners". For instance, if B is the triangle given by corners
 * {a, b, c} is one of our simplices, then we require that the edge (line
 * segment) given by {a, c} is also a simplex. While we can describe this
 * relationship in a purely combinatorial fashion, the "meaning" is given
 * by the choice of simplices S, and this methodology used here always
 * makes some use of geometric techniques (e.g., metric spaces).
 *
 * <p> For reasons of efficiency, we use more than one encoding for
 * simplices, instead of using the obvious "array of vertices". The reason
 * for this is that these encodings of simplices can be much smaller, and
 * the code to manage them much faster, than if we use the obvious
 * implementation. We make <code>Simplex</code> an abstract class, instead
 * of an interface, both because code written to this class will be
 * slightly faster than if it were an interface, and because the root class
 * is a convenient repository for some utilities needed by the extension
 * classes, and for "global information" such as the characteristic of the
 * base field.
 *
 * <p> All but the most naive encoding must have, either implicitly or
 * explicitly, limits on the allowed dimension and number of landmarks that
 * can be used to construct simplices. By default all limits will either be
 * the explicit in the representation of the simplex or will be implicitly
 * the largest values that are feasible for that representation. See the
 * specific implementations for more details on this point.
 *
 * <p> The vertex set of any simplex depends on the context -- that is, for
 * a Simplex s, s.vertices() is an array of integers of limited size, and
 * the meaning of those integers will depend on the source of the Simplex
 * s.  The data sets we operate on frequently have millions of points (and
 * we hope to push these even further in the future), but we only construct
 * simplices from much smaller sets, often representative subsets we call
 * "landmark sets". Since we are able to restrict landmarks to be in the
 * thousands, or even hundreds, we use a smaller number of bits to encode
 * the vertex sets for a simplex, often by "indirecting" through an array
 * of landmark indices to some much larger data set. Thus, a vertex of 10
 * encoded within an instance of <code>Simplex</code> generally refers to
 * the point whose index is the 10th entry in some landmark set, not the
 * 10th point in the full data set.
 *
 * @author Harlan Sexton
 * @version 0.1
 *
 */
abstract public class Simplex implements Comparable<Simplex> {

	/**
	 * The filtration index of the Simplex.  <p> Theoretically this could
	 * be abstract, but in all of the implementations we use this value is
	 * hard enough to find that it makes sense to cache it in the
	 * instance. Also, doing it this way allows us to make it effectively
	 * final without forcing the implementations to compute it during
	 * instance creation. We also get rid of the table T[] in the algorithm
	 * (see {@link edu.stanford.math.plex.Persistence} and references therein) by storing the
	 * reduced boundary chain directly in the Simplex instance. This is
	 * simpler, faster, and may even be smaller (it depends on what
	 * percentage of slots in T[] actually got filled).
	 */    
	protected int _findex = -1;
	protected Chain _chain;

	/**
	 * Set the findex value, provided it hasn't already been done.
	 * <p>
	 *
	 *
	 * @return     the findex value.
	 *
	 */    
	public int setfindex (int i) {
		assert(_findex < 0);
		if (_findex < 0) 
			_findex = i;
		return _findex;
	}

	/**
	 * Decrement the findex value -- this is a hack that should ONLY be used
	 * if you know what you are doing. Changing the findex of a Simplex
	 * already in a stream is a disaster.  <p>
	 *
	 *
	 * @return     the findex value.
	 *
	 */    
	public int decrement_findex () {
		if (_findex < 0) 
			_findex--;
		return _findex;
	}


	/**
	 * Set the chain value, provided it hasn't already been set.
	 * <p>
	 *
	 *
	 * @return     the chain value.
	 * @exception IllegalStateException when setChain() has already been
	 *             called without an intervening clearChain() call.
	 *
	 */    
	public Chain setChain (Chain c) {
		if (_chain != null)
			throw new IllegalStateException("Simplex " + this.toString() + 
					"already has chain entry: " + 
					_chain.toString());
		_chain = c;
		return _chain;
	}

	/**
	 * Clear the chain value; used when we reuse the elementes in a stream.
	 * <p>
	 *
	 */    
	public void clearChain () {
		_chain = null;
	}

	/**
	 * Get the findex value. Should be as fast as if the slot were accessible.
	 * <p>
	 *
	 *
	 * @return     the findex value.
	 *
	 */    
	public int findex () {
		return _findex;
	}

	/**
	 * Get the chain value. Should be as fast as if the slot were accessible.
	 * <p>
	 *
	 *
	 * @return     the chain value.
	 *
	 */    
	public Chain chain () {
		return _chain;
	}

	/**
	 * Returns the dimension of self.
	 * <p>
	 *
	 *
	 * @return     int, the dimension (i.e., 1 more than the number of vertices) of the simplex.
	 *
	 */    
	abstract public int dimension();

	/**
	 * Make a "blank" copy of the Simplex -- equivalent to getSimplex(this.vertices()).
	 * <p>
	 *
	 *
	 * @return     the copied instance.
	 *
	 */    
	abstract public Simplex copy();

	/**
	 * Returns the indices of self as an array.
	 * <p>
	 *
	 *
	 * @return     an int[] of indices into the LandmarkIndexArray.
	 *
	 */    
	abstract public int[] vertices();

	/**
	 * Returns the indices of self as an array.
	 * <p>
	 *
	 *
	 * @param verts An int[] in which to store the vertices -- it must be at
	 *               at least as long as the number of vertices.
	 * @return     an int[] of indices into the LandmarkIndexArray.
	 *
	 */    
	abstract public int[] vertices(int verts[]);



	/**
	 * Is this a subset of s?
	 * <p>
	 *
	 *
	 * @param s The simplex to which to compare this.
	 * @return     True if this is a subset of s, else false.
	 */    
	public boolean subset(Simplex s) {
		int stop = dimension();
		int s_stop = s.dimension();
		if (stop > s_stop)
			return false;
		return subset(s, new int[stop+1], new int[s_stop+1]);
	}


	/**
	 * Is this a subset of s?
	 * <p>
	 *
	 *
	 * @param s The simplex to which to compare this.
	 * @param v An int[] in which to put the vertices of this -- must be at
	 *           least as long as the number of vertices.
	 * @param sv An int[] in which to put the vertices of s  -- must be at
	 *           least as long as the number of vertices of s.
	 * @return     True if this is a subset of s, else false.
	 */    
	public boolean subset(Simplex s, int[] v, int[] sv) {
		int stop = dimension();
		int s_stop = s.dimension();
		if (stop > s_stop)
			return false;
		vertices(v);
		s.vertices(sv);
		int current = 0;
		int s_current = 0;
		while (current <= stop) {
			int to_find = v[current++];
			while(s_current <= s_stop) {
				if (sv[s_current++] == to_find)
					break;
				else if (s_current > s_stop)
					return false;
			}
		}
		assert(current == (stop+1));
		return true;
	}

	/**
	 * Overrides Object hashcode.
	 *
	 * <p>
	 * @return     CRC hash of the vertex set.
	 *
	 */
	public int hashCode() {
		return CRC.hash32(this.vertices());
	}


	/**
	 * Overrides Object equals. 
	 *
	 * <p>
	 * This test is equivalent to an equality test on the vertices.
	 * <p>
	 * @param      obj   object to compare.
	 * @return     true or false, depending on whether or not the Simplex is = to obj.
	 *
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof Simplex))
			return false;
		Simplex s = (Simplex) obj;
		int this_dimension = this.dimension();
		int s_dimension = s.dimension();
		if (s_dimension == this_dimension)
			return (0 == Plex.comparePtArrays(this.vertices(), s.vertices()));
		else 
			return false;
	}

	/**
	 * Implements Comparable interface.
	 *
	 * <p>
	 * This test is equivalent to lexicographic comparison of the vertex arrays.
	 *<p>
	 * @param      s   Simplex to compare.
	 * @return     negative, 0, or positive, if this <, =, resp. > than s.
	 *
	 */
	public int compareTo(Simplex s) {
		int this_dimension = this.dimension();
		int s_dimension = s.dimension();
		if (s_dimension != this_dimension)
			return (this_dimension - s_dimension);
		else 
			return Plex.comparePtArrays(this.vertices(), s.vertices());
	}

	// The seq() and slt() methods are used to perform Chain and
	// Persistence operations. For instance, simplices in a Chain are
	// sorted so that arithmetic on Chains is linear in the size of the
	// instances. The findex() of a simplex is an essential part of the
	// ordering in the Persistence algorithm, but we don't regard it as
	// intrinsic to the Simplex otherwise. (The presence of an findex slot
	// in Simplex is for caching purposes, not because it is essential to
	// the definition of the type.) Therefore, compareTo(), hash() and
	// equals() ignore the value of the findex, and changing the findex
	// slot of a Simplex instance won't change its location in a
	// hashtable. In particular, it means that we can use a hashtable to
	// record the findex values of instances of Simplex, if we wish
	// (although I have no plans to do that at the moment except in initial
	// test code). To be explicit, the contract for slt() is that it sort
	// first on findex, second on dimension, and then after that,
	// lexicographically on vertices. The contract for a.seq(b) is that it
	// be functionally equivalent to !(a.slt(b) || b.slt(a)) in the places
	// that it is used, but as an optimization, in the places where slt()
	// and seq() are called, we can suppose that if the vertices for a and
	// b are the same, then their findex() values are the same, too.

	/**
	 * Simplex Less Than. Used internally in Persistence and Chain
	 * code. Not the same as compareTo.
	 *
	 * <p>
	 * @param      s   Simplex to compare.
	 * @return     true if less than, else false.
	 *
	 */
	abstract boolean slt(Simplex s);

	/**
	 * Simplex EQuals. Used internally in Persistence and Chain code. 
	 *
	 * <p>
	 * @param      s   Simplex to compare.
	 * @return     true is equal, else false.
	 *
	 */
	abstract boolean seq(Simplex s);


	/**
	 * Get the simplex for a given set of vertices. 
	 *
	 * <p>
	 * @param      vertices   The vertex set.
	 * @return     A simplex for that set of vertices.
	 */
	public static Simplex getSimplex (int[] vertices) {
		if (vertices.length <= 2)
			return Packed2Simplex.makeSimplex(vertices);
		else if (vertices.length <= 4)
			return Packed4Simplex.makeSimplex(vertices);
		else if (vertices.length <= 6)
			return Packed6Simplex.makeSimplex(vertices);
		else if (vertices.length <= 8)
			return Packed8Simplex.makeSimplex(vertices);
		// we may make a Packed10Simplex, or larger, someday
		else
			throw 
			new IllegalArgumentException("Simplices with more than 7 dimensions are not supported.");
	}

	/**
	 * Get the simplex for a given set of vertices, including setting findex. 
	 *
	 * <p>
	 * @param      vertices   The vertex set.
	 * @param      findex   The Filtration index.
	 * @return     A simplex for that set of vertices.
	 */
	public static Simplex getSimplex (int[] vertices, int findex) {
		Simplex s = getSimplex(vertices);
		s.setfindex(findex);
		return s;
	}

	/**
	 * Get the simplex for an already verified array of vertices. 
	 *
	 * <p>
	 * @param      vertices   The vertex set.
	 * @return     A simplex for that set of vertices.
	 */
	public static Simplex getSimplexPresorted (int[] vertices) {
		if (vertices.length <= 2)
			return Packed2Simplex.makeSimplexPresorted(vertices);
		else if (vertices.length <= 4)
			return Packed4Simplex.makeSimplexPresorted(vertices);
		else if (vertices.length <= 6)
			return Packed6Simplex.makeSimplexPresorted(vertices);
		else if (vertices.length <= 8)
			return Packed8Simplex.makeSimplexPresorted(vertices);
		// we may make a Packed10Simplex, or larger, someday
		else
			throw 
			new IllegalArgumentException("Simplices with more than 7 dimensions are not supported.");
	}

	/**
	 * Get the simplex for an already verified array of vertices, including
	 * setting findex.
	 *
	 * <p>
	 * @param      vertices   The vertex set.
	 * @param      findex   The Filtration index.
	 * @return     A simplex for that set of vertices.
	 */
	public static Simplex getSimplexPresorted(int[] vertices, int findex) {
		Simplex s = getSimplexPresorted(vertices);
		s.setfindex(findex);
		return s;
	}

	/**
	 * Get the edge for a pair of vertices. 
	 *
	 * <p>
	 * @param      v1   One vertex.
	 * @param      v2   Another vertex.
	 * @return     A 1-simplex, or edge.
	 */
	public static Simplex makeEdge (int v1, int v2, int findex) {
		Simplex s = Packed2Simplex.makeEdge(v1, v2);
		s.setfindex(findex);
		return s;
	}

	/**
	 * Turn a vertex into a 0-simplex. 
	 *
	 * <p>
	 * @param      v   The vertex.
	 * @return     A 0-simplex, or point.
	 */
	public static Simplex makePoint (int v, int findex) {
		Simplex s = Packed2Simplex.makePoint(v);
		s.setfindex(findex);
		return s;
	}

	/**
	 * Add a vertex to a simplex.
	 *
	 * <p> I can't recall why this method exists, so the implementation is
	 * the easiest one I can think of.
	 * @param      v   A vertex to adjoin.
	 * @return     The new simplex.
	 */
	public Simplex addVertex(int v) {
		int[] v_old = vertices();
		int[] v_new = new int[v_old.length+1];
		for (int i = 0; i < v_old.length; i++)
			v_new[i] = v_old[i];
		v_new[v_old.length] = v;
		return Simplex.getSimplex(v_new);
	}

	/**
	 * Add a vertex to a simplex, and set the findex.
	 *
	 * <p>
	 * @param      v   A vertex to adjoin.
	 * @param      f   The new findex.
	 * @return     The new simplex.
	 */
	public Simplex addVertex (int v, int f) {
		Simplex s = this.addVertex(v);
		s.setfindex(f);
		return s;
	}


	/**
	 * Represent a Simplex as a String.
	 *
	 * <p>
	 * @return     A string that describes a Simplex.
	 */
	public String toString() { 
		int[] vertices = this.vertices();
		String base = "<";
		if (_findex >= 0)
			base = String.format("<(%d)", _findex);
		if (vertices.length == 0)
			return base + ">";
		else if (_findex >= 0)
			base = base + " ";
		for (int i = 0; i < vertices.length-1; i++) {
			base = base + String.format("%d, ", vertices[i]);
		}
		base = base + String.format("%d>", vertices[vertices.length-1]);
		return base;
	}

	/**
	 * Sort a set of vertices into increasing order, as needed for some
	 * Simplex operations.  We use the bubblesort algorithm, since that is
	 * fastest for very short arrays, which is what we have in practice.
	 *
	 * <p>
	 * @param      vertices   The vertex set.
	 * @return     The sorted vertices.
	 */
	public static int[] vertex_sort (int[] vertices) {

		if ((vertices.length <= 1))
			return vertices;

		for (int j = vertices.length - 1; j > 0; j--) {
			for (int i = 0; i < j; i++) {
				if (vertices[i+1] < vertices[i]) {
					int dummy = vertices[i];
					vertices[i] = vertices[i+1];
					vertices[i+1] = dummy;
				}
			}
		}
		return vertices;
	}

	/**
	 * Sort an array of double into increasing order, as needed for some
	 * Simplex operations.  We use the bubblesort algorithm, since that is
	 * fastest for very short arrays, which is what we have in practice.
	 *
	 * <p>
	 * @param      distances   An array of double to be sorted..
	 * @return     The sorted array.
	 */
	public static double[] dist_sort (double[] distances) {

		if ((distances.length <= 1))
			return distances;

		for (int j = distances.length - 1; j > 0; j--) {
			for (int i = 0; i < j; i++) {
				if (distances[i+1] < distances[i]) {
					double dummy = distances[i];
					distances[i] = distances[i+1];
					distances[i+1] = dummy;
				}
			}
		}
		return distances;
	}

	/**
	 * Sort a set of vertices into decreasing order, as needed for some
	 * Simplex operations.  We use the bubblesort algorithm, since that is
	 * fastest for very short arrays, which is what we have in practice.
	 *
	 * <p>
	 * @param      vertices   The vertex set.
	 * @return     The reverse sorted vertices.
	 */
	public static int[] simplex_reverse_sort (int[] vertices) {

		if (vertices.length <= 1)
			return vertices;

		for (int j = vertices.length - 1; j > 0; j--) {
			for (int i = 0; i < j; i++) {
				if (vertices[i+1] > vertices[i]) {
					int dummy = vertices[i];
					vertices[i] = vertices[i+1];
					vertices[i+1] = dummy;
				}
			}
		}
		return vertices;
	}

	/**
	 * Sort, in place and using "persistence order", an array of
	 * simplices. Null entries will be pushed to the end of the array -- that
	 * is, null is considered to be larger than any non-null Simplex.
	 */
	public static void persistence_sort(Simplex[] sarray) {
		PlexSort.comp_sort((Object[])sarray, 0, sarray.length, 
				new PlexSort.CompObj() {
			public int fn(Object o1, Object o2) { 
				if (o1 == o2)
					return 0;
				if (o1 == null)
					return 1;
				if (o2 == null)
					return -1;
				Simplex s1 = (Simplex)o1;
				Simplex s2 = (Simplex)o2;
				if (s1.slt(s2))
					return -1;
				else if (s2.slt(s1))
					return 1;
				else
					return 0;
			}
		} );
	}

	/**
	 * Return an array of simplices that describes the boundary. The sign
	 * associated with a face is -1 to the power of the index of that face in
	 * the array. There is a static class method in Chain that will convert
	 * an array of this type into a Chain for use in homology calculations.
	 *
	 * <p>
	 * @return     [face0, face1, ...]
	 */
	abstract public Simplex[] boundaryArray();

	/**
	 * Return the boundary of a simplex as a chain over a given characteristic.
	 * <p>
	 * @param p The characteristic
	 * @return A chain representing the boundary.
	 */

	public Chain boundary(int p) {
		Simplex[] boundarySimplices = this.boundaryArray();
		if(boundarySimplices == null)
			boundarySimplices = new Simplex[0];
		int[] boundaryCoefficients = new int[boundarySimplices.length];
		int sign = 1;
		for(int i=0; i<boundaryCoefficients.length; i++) {
			boundaryCoefficients[i] = (sign>0)?sign:p+sign;
			sign *= -1;
		}
		return new Chain(p,boundaryCoefficients,boundarySimplices);
	}


	//
	// Test code 
	// 
	// The code below this point is for testing purposes only.
	//

	/**
	 * Return a random Simplex. A test function.
	 *
	 * <p>
	 * @return     A "random" Simplex.
	 */
	static Simplex random() { 
		int length = (int) Math.floor((Math.random() * 9.0));
		if (length > 8)
			length = 8;
		if (length < 1)
			length = 1;
		int[] vertices = new int[length];
		for(int i = 0; i < length; i++) {
			int x = 1 + (int) Math.floor((Math.random() * 1000.0));
			vertices[i] = x;
		}
		vertex_sort(vertices);
		for(int i = 0; i < length-1; i++) {
			if (vertices[i] == vertices[i+1]) {
				for(int j = i+1; j < length; j++) {
					vertices[j]++;

				}
			}
		}

		return getSimplexPresorted(vertices);
	}

}
