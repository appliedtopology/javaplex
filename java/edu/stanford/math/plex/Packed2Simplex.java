package edu.stanford.math.plex;

/**
 * <p>The <code>Packed2Simplex</code> class implements the abstract class
 * <code>Simplex</code> for Simplices that are points or edges by storing
 * the vertices as 2 packed 32-bit values in a single long int. This limits
 * us to H0 calculations with this representation, but this is the most
 * important case for Mapper use, and increased vertex sizes, and
 * simplicity and speed of this form make it worthwhile.
 *
 * @version $Id$
 */
public class Packed2Simplex extends Simplex {

	public final long bits;

	// This is the largest vertex index that we can use for this kind of
	// Simplex.
	protected static int VERTEX_BIT_SZ = 32;
	protected static int MAX_2_INDEX = Integer.MAX_VALUE;
	protected static long DIM_0_MASK = ~((long) MAX_2_INDEX);

	static boolean checkConstants() {
		assert(MAX_2_INDEX == 0x7fffffff);
		assert(DIM_0_MASK == 0xffffffff80000000L);
		return true;
	}

	// Extract smallest vertex.
	protected int v1() {
		return (int) (bits & MAX_2_INDEX);
	}

	// Extract second vertex.
	protected int v2() {
		return (int) ((bits >>> VERTEX_BIT_SZ) & MAX_2_INDEX);
	}

	// We only use the constructors for Packed2Simplex internally. This one
	// is never used.
	protected Packed2Simplex() {
		bits = 0;
	}

	// We only use the constructors for Packed2Simplex internally. 
	protected Packed2Simplex(long bits) {
		this.bits = bits;
	}

	protected static boolean check_2_vertices(int[] v) {
		if (v.length == 0)
			return true;
		if ((v.length == 1) && (v[0] > 0))
			return true;
		else if ((v.length == 2) && (v[0] > 0) && (v[1] > v[0]))
			return true;
		else
			return false;
	}

	protected static void assert_2_vertices(int[] v) {
		if (!check_2_vertices(v)) {
			throw new 
			IllegalArgumentException
			("Packed2Simplex instances must have between 0 and 2" + 
			"distinct positive integer vertices.");
		}
	}

	// In the explicit constructors, the indices must all be non-zero and
	// be in increasing order.
	protected Packed2Simplex(int v1) {
		assert(v1 > 0);
		bits = v1;
	}
	Packed2Simplex(int v1, int v2) {
		assert((v2 > v1) && (v1 > 0));
		bits = ((((long)v2) << VERTEX_BIT_SZ) | ((long)v1));
	}

	/**
	 * Overrides Object hashcode.
	 *
	 * <p>
	 * @return     CRC hash of the vertex set.
	 *
	 */
	public int hashCode() {
		return CRC.hash32(bits, 0);
	}

	/**
	 * Overrides Object equals.
	 *
	 * <p>
	 * @param      obj   object to compare.
	 * @return true or false, depending on whether or not the Simplex is =
	 *                        to obj.
	 *
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof Packed2Simplex))
			return false;
		Packed2Simplex s = (Packed2Simplex) obj;
		return (bits == s.bits);
	}

	// Internal comparison routine.
	protected int compareTo(Packed2Simplex s) {
		if (bits > s.bits)
			return 1;
		else if (bits < s.bits)
			return -1;
		else
			return 0;
	}

	/**
	 * Implements Comparable interface.
	 *
	 * <p>
	 * @param      s   Simplex to compare.
	 * @return     negative, 0, or positive, if this <, =, resp. > than s.
	 *
	 */
	public int compareTo(Simplex s) {
		if (!(s instanceof Packed2Simplex))
			return (this.dimension() - s.dimension());
		else 
			return this.compareTo((Packed2Simplex)s);
	}

	/**
	 * Simplex Less Than. Used internally in Persistence and Chain
	 * code. Not the same as compareTo.
	 *
	 * <p>
	 * @param      s   Simplex to compare.
	 * @return     true if this < s, else false.
	 *
	 */
	boolean slt(Simplex s) {
		if (this.findex() != s.findex())
			return (this.findex() < s.findex());
		else if (s instanceof Packed2Simplex) {
			Packed2Simplex ps = (Packed2Simplex) s;
			return (bits < ps.bits);
		} else 
			return (this.dimension() < s.dimension());
	}

	/**
	 * Simplex EQuals. Used internally in Persistence and Chain code.  
	 *
	 * <p>
	 * @param      s   Simplex to compare.
	 * @return     true is equal, else false.
	 *
	 */
	boolean seq(Simplex s) {
		if (!(s instanceof Packed2Simplex))
			return false;
		Packed2Simplex ps = (Packed2Simplex) s;
		return (bits == ps.bits);
	}

	/**
	 * Make a "blank" copy of the Simplex -- equivalent to getSimplex(this.vertices()).
	 * <p>
	 *
	 *
	 * @return     the copied instance.
	 *
	 */    
	public Simplex copy() {
		return new Packed2Simplex(bits);
	}

	/**
	 * Returns the dimension of self.
	 * <p>
	 *
	 *
	 * @return     int, the dimension of the simplex.
	 *
	 * @see        edu.stanford.math.plex.Simplex#dimension
	 */    
	public int dimension() { 
		if (bits == 0) 
			return -1;
		else if ((bits & DIM_0_MASK) == 0) 
			return 0;
		else
			return 1;
	}

	/**
	 * Returns the indices of self as an array.
	 * <p>
	 *
	 *
	 * @return     an int[] of vertices, or null if no vertices.
	 *
	 * @see        edu.stanford.math.plex.Simplex#vertices
	 */    
	public int[] vertices() { 
		int dim = dimension();
		int verts[] = new int[dimension()+1];

		if (dim == -1) 
			return verts;

		if (dim == 0) {
			verts[0] = this.v1();
			return verts;
		}

		verts[0] = this.v1();
		verts[1] = this.v2();
		return verts;
	}


	/**
	 * Returns the indices of self in the given array argument.
	 * <p>
	 *
	 *
	 * @param      verts   the int[] into which the vertices, if any, are written.
	 * @return     the given array argument, or null if no vertices.
	 *
	 * @see        edu.stanford.math.plex.Simplex#vertices
	 */
	public int[] vertices(int[] verts) { 
		if (bits==0) 
			return verts;

		if ((bits & DIM_0_MASK) == 0) {
			verts[0] = this.v1();
			return verts;
		}

		verts[0] = this.v1();
		verts[1] = this.v2();
		return verts;
	}


	// The obvious thing.
	public static Simplex makeSimplex(int[] vertices) {
		assert(vertices.length <= 2);
		Simplex.vertex_sort(vertices);
		assert_2_vertices(vertices);
		if (vertices.length == 0) 
			return new Packed2Simplex();
		else if (vertices.length == 1) 
			return new Packed2Simplex(vertices[0]);
		else 
			return new Packed2Simplex(vertices[0], vertices[1]);
	}

	// Another obvious thing.
	public static Simplex makeSimplexPresorted(int[] vertices) {
		assert(vertices.length <= 2);
		assert(check_2_vertices(vertices));
		if (vertices.length == 0) 
			return new Packed2Simplex();
		else if (vertices.length == 1) 
			return new Packed2Simplex(vertices[0]);
		else
			return new Packed2Simplex(vertices[0], vertices[1]);
	}

	// Still another obvious thing.
	public static Simplex makeEdge(int v1, int v2) {
		if ((v1 < 1) || (v2 < 1) || (v1 == v2))
			throw new 
			IllegalArgumentException
			("Edge vertices must be distinct positive integers.");
		if (v2 > v1)
			return new Packed2Simplex(v1, v2);
		else 
			return new Packed2Simplex(v2, v1);
	}

	// The most obvious thing.
	public static Simplex makePoint(int v) {
		if (v < 1)
			throw new 
			IllegalArgumentException
			("Simplex vertices must be positive integers.");
		return new Packed2Simplex(v);
	}

	/**
	 * Returns the boundary of self.  
	 *
	 * <p> It is simple enough to do explicitly, and it needs to be fast.
	 *
	 *
	 * @return     [face0, face1, ...]
	 *
	 * @see        edu.stanford.math.plex.Simplex#vertices
	 */
	public Simplex[] boundaryArray() {
		int dimension = dimension();
		if (dimension <= 0)
			return null;
		else {
			Simplex[] return_value = new Simplex[2];
			return_value[0] = new Packed2Simplex(this.v2());
			return_value[1] = new Packed2Simplex(this.v1());
			return return_value;
		}
	}
}
