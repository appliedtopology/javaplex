package edu.stanford.math.plex;

/**
 * <p>The <code>Packed8Simplex</code> class implements the abstract class
 * <code>Simplex</code> for Simplices of dimension 6-7 by storing the
 * vertices as 8 packed positive integer values in three long
 * integers. This limits us to H6 calculations with this representation,
 * but it seems very unlikely that we can go even this far an alteration of
 * the basic algorithm.
 *
 * @version $Id$
 */
public class Packed8Simplex extends Simplex {

	public final long bits_lo;
	public final long bits_lo_mid;
	public final long bits_hi_mid;
	public final long bits_hi;

	// This is the largest vertex index that we can use for this kind of
	// Simplex.
	protected static int VERTEX_BIT_SZ = 32;
	protected static int MAX_8_INDEX = Integer.MAX_VALUE;
	protected static long DIM_6_MASK = ~((long) MAX_8_INDEX);

	static boolean checkConstants() {
		assert(MAX_8_INDEX == 0x7fffffff);
		assert(DIM_6_MASK == 0xffffffff80000000L);
		return true;
	}

	protected static boolean check_8_vertices(int[] v) {
		if (v.length <= 6)
			return false;
		else if ((v.length == 7) && (v[0] > 0) && (v[1] > v[0]) && (v[2] > v[1]) &&
				(v[3] > v[2]) && (v[4] > v[3]) && (v[5] > v[4]) && (v[6] > v[5]))
			return true;
		else if ((v.length == 8) && (v[0] > 0) && (v[1] > v[0]) && (v[2] > v[1]) &&
				(v[3] > v[2]) && (v[4] > v[3]) && (v[5] > v[4]) && (v[6] > v[5]) && 
				(v[7] > v[6]))
			return true;
		else
			return false;
	}

	protected static void assert_8_vertices(int[] v) {
		if (!check_8_vertices(v))
			throw new 
			IllegalArgumentException
			("Packed8Simplex instances must have either 7 or 8 " + 
			"distinct positive integer vertices.");
	}

	// Convert an array of vertices into the "bits_lo" of a
	// Packed8Simplex. Vertices MUST BE SORTED.
	protected static long p8_v_to_l_lo(int[] vertices) {
		return (((long)vertices[1]) << VERTEX_BIT_SZ) | ((long)vertices[0]);
	}

	// Convert an array of vertices into the "bits_lo_mid" of a
	// Packed8Simplex. Vertices MUST BE SORTED.
	protected static long p8_v_to_l_lo_mid(int[] vertices) {
		return (((long)vertices[3]) << VERTEX_BIT_SZ) | ((long)vertices[2]);
	}

	// Convert an array of vertices into the "bits_hi_mid" of a
	// Packed8Simplex. Vertices MUST BE SORTED.
	protected static long p8_v_to_l_hi_mid(int[] vertices) {
		return (((long)vertices[5]) << VERTEX_BIT_SZ) | ((long)vertices[4]);
	}

	// Convert an array of vertices into the "bits_hi" of a
	// Packed8Simplex. Vertices MUST BE SORTED.
	protected static long p8_v_to_l_hi(int[] vertices) {
		if (vertices.length == 7)
			return (long) vertices[6];
		else 
			return (((long)vertices[7]) << VERTEX_BIT_SZ) | ((long)vertices[6]);
	}

	// Extract largest vertex.
	protected int v8() {
		return (int) ((bits_hi >>> VERTEX_BIT_SZ) & MAX_8_INDEX);
	}

	// Extract 5th vertex (second hi bits vertex).
	protected int v7() {
		return (int) (bits_hi & MAX_8_INDEX);
	}

	// Extract largest vertex.
	protected int v6() {
		return (int) ((bits_hi_mid >>> VERTEX_BIT_SZ) & MAX_8_INDEX);
	}

	// Extract 5th vertex (second hi bits vertex).
	protected int v5() {
		return (int) (bits_hi_mid & MAX_8_INDEX);
	}

	// Extract 4th vertex.
	protected int v4() {
		return (int) ((bits_lo_mid >>> VERTEX_BIT_SZ) & MAX_8_INDEX);
	}

	// Extract 3rd vertex.
	protected int v3() {
		return (int) (bits_lo_mid & MAX_8_INDEX);
	}

	// Extract 2nd vertex.
	protected int v2() {
		return (int) ((bits_lo >>> VERTEX_BIT_SZ) & MAX_8_INDEX);
	}

	// Extract smallest vertex.
	protected int v1() {
		return (int) (bits_lo & MAX_8_INDEX);
	}

	// We only use the constructors for Packed8Simplex internally. This one
	// is never used externally because none of bits_lo, bits_mid, or bits_hi
	// is ever 0.
	protected Packed8Simplex() {
		bits_lo = 0;
		bits_lo_mid = 0;
		bits_hi_mid = 0;
		bits_hi = 0;
	}

	// We only use the constructors for Packed8Simplex internally. 
	protected Packed8Simplex(long bits_lo_val, long bits_lo_mid_val,
			long bits_hi_mid_val, long bits_hi_val) {
		bits_lo = bits_lo_val;
		bits_lo_mid = bits_lo_mid_val;
		bits_hi_mid = bits_hi_mid_val;
		bits_hi = bits_hi_val;
	}

	// In the explicit constructors, the indices must all be non-zero and
	// be in increasing order.
	protected Packed8Simplex(int v1, int v2, int v3, int v4, int v5, int v6, 
			int v7) {
		assert((v7 > v6) && (v6 > v5) && (v5 > v4) && (v4 > v3) && 
				(v3 > v2) && (v2 > v1) && (v1 > 0));
		bits_hi = (long) v7;
		bits_hi_mid = (((long) v6) << VERTEX_BIT_SZ) | ((long) v5);
		bits_lo_mid = (((long) v4) << VERTEX_BIT_SZ) | ((long) v3);
		bits_lo = (((long) v2) << VERTEX_BIT_SZ) | ((long) v1);
	}
	protected Packed8Simplex(int v1, int v2, int v3, int v4, int v5, int v6, 
			int v7, int v8) {
		assert((v8 > v7) && (v7 > v6) && (v6 > v5) && (v5 > v4) && 
				(v4 > v3) && (v3 > v2) && (v2 > v1) && (v1 > 0));
		bits_hi = (((long) v8) << VERTEX_BIT_SZ) | ((long) v7);
		bits_hi_mid = (((long) v6) << VERTEX_BIT_SZ) | ((long) v5);
		bits_lo_mid = (((long) v4) << VERTEX_BIT_SZ) | ((long) v3);
		bits_lo = (((long) v2) << VERTEX_BIT_SZ) | ((long) v1);
	}


	/**
	 * Overrides Object hashcode.
	 *
	 * <p>
	 * @return     CRC hash of the vertex set.
	 *
	 */
	public int hashCode() {
		return CRC.hash32(bits_hi, 
				CRC.hash32(bits_hi_mid, 
						CRC.hash32(bits_lo_mid, 
								CRC.hash32(bits_lo, 0))));
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
		if (!(obj instanceof Packed8Simplex))
			return false;
		Packed8Simplex s = (Packed8Simplex) obj;
		return ((bits_lo == s.bits_lo) && (bits_lo_mid == s.bits_lo_mid) && 
				(bits_hi_mid == s.bits_hi_mid) && (bits_hi == s.bits_hi));
	}

	// Internal comparison routine.
	protected int compareTo(Packed8Simplex s) {
		if (bits_hi > s.bits_hi)
			return 1;
		else if (bits_hi < s.bits_hi)
			return -1;
		else if (bits_hi_mid > s.bits_hi_mid)
			return 1;
		else if (bits_hi_mid < s.bits_hi_mid)
			return -1;
		else if (bits_lo_mid > s.bits_lo_mid)
			return 1;
		else if (bits_lo_mid < s.bits_lo_mid)
			return -1;
		else if (bits_lo > s.bits_lo)
			return 1;
		else if (bits_lo < s.bits_lo)
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
		// The Packed8Simplex instances are of larger dimension than any
		// other kind of Simplex, so if s is a Simplex but not a
		// Packed8Simplex, then this > s.
		if (!(s instanceof Packed8Simplex))
			return 1;
		else 
			return this.compareTo((Packed8Simplex)s);
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
		else if (s instanceof Packed8Simplex) {
			Packed8Simplex ps = (Packed8Simplex) s;
			if (bits_hi < ps.bits_hi)
				return true;
			else if (bits_hi == ps.bits_hi) {
				if (bits_hi_mid < ps.bits_hi_mid)
					return true;
				else if (bits_hi_mid == ps.bits_hi_mid) {
					if (bits_lo_mid < ps.bits_lo_mid)
						return true;
					else if (bits_lo_mid == ps.bits_lo_mid)
						return (bits_lo < ps.bits_lo);
					else
						return false;
				} else 
					return false;
			} else
				return false;
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
		if (!(s instanceof Packed8Simplex))
			return false;
		Packed8Simplex ps = (Packed8Simplex) s;
		return ((bits_lo == ps.bits_lo) && (bits_lo_mid == ps.bits_lo_mid) && 
				(bits_hi_mid == ps.bits_hi_mid) && (bits_hi == ps.bits_hi));
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
		return new Packed8Simplex(bits_lo, bits_lo_mid, bits_hi_mid, bits_hi);
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
		if ((bits_hi & DIM_6_MASK) == 0) 
			return 6;
		else
			return 7;
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
		int dimension = dimension();
		int verts[] = new int[dimension+1];

		verts[0] = this.v1();
		verts[1] = this.v2();
		verts[2] = this.v3();
		verts[3] = this.v4();
		verts[4] = this.v5();
		verts[5] = this.v6();


		if (dimension == 6) {
			verts[6] = this.v7();
			return verts;
		}

		verts[6] = this.v7();
		verts[7] = this.v8();
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
		int dimension = dimension();

		verts[0] = this.v1();
		verts[1] = this.v2();
		verts[2] = this.v3();
		verts[3] = this.v4();
		verts[4] = this.v5();
		verts[5] = this.v6();

		if (dimension == 6) {
			verts[6] = this.v7();
			return verts;
		}

		verts[6] = this.v7();
		verts[7] = this.v8();
		return verts;
	}

	// The obvious thing.
	public static Simplex makeSimplex(int[] vertices) {
		Simplex.vertex_sort(vertices);
		assert_8_vertices(vertices);
		return new Packed8Simplex(p8_v_to_l_lo(vertices), 
				p8_v_to_l_lo_mid(vertices), 
				p8_v_to_l_hi_mid(vertices), 
				p8_v_to_l_hi(vertices));
	}

	// The other obvious thing.
	public static Simplex makeSimplexPresorted(int[] vertices) {
		assert_8_vertices(vertices);
		return new Packed8Simplex(p8_v_to_l_lo(vertices), 
				p8_v_to_l_lo_mid(vertices), 
				p8_v_to_l_hi_mid(vertices), 
				p8_v_to_l_hi(vertices));
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
		Simplex[] return_value = new Simplex[dimension+1];

		if (dimension == 6) {
			return_value[0] = new Packed6Simplex(this.v2(), this.v3(), this.v4(), 
					this.v5(), this.v6(), this.v7());
			return_value[1] = new Packed6Simplex(this.v1(), this.v3(), this.v4(), 
					this.v5(), this.v6(), this.v7());
			return_value[2] = new Packed6Simplex(this.v1(), this.v2(), this.v4(), 
					this.v5(), this.v6(), this.v7());
			return_value[3] = new Packed6Simplex(this.v1(), this.v2(), this.v3(), 
					this.v5(), this.v6(), this.v7());
			return_value[4] = new Packed6Simplex(this.v1(), this.v2(), this.v3(), 
					this.v4(), this.v6(), this.v7());
			return_value[5] = new Packed6Simplex(this.v1(), this.v2(), this.v3(), 
					this.v4(), this.v5(), this.v7());
			return_value[6] = new Packed6Simplex(this.v1(), this.v2(), this.v3(), 
					this.v4(), this.v5(), this.v6());
		} else {
			return_value[0] = 
				new Packed8Simplex(this.v2(), this.v3(), this.v4(), 
						this.v5(), this.v6(), this.v7(), this.v8());
			return_value[1] = 
				new Packed8Simplex(this.v1(), this.v3(), this.v4(), 
						this.v5(), this.v6(), this.v7(), this.v8());
			return_value[2] = 
				new Packed8Simplex(this.v1(), this.v2(), this.v4(), 
						this.v5(), this.v6(), this.v7(), this.v8());
			return_value[3] = 
				new Packed8Simplex(this.v1(), this.v2(), this.v3(), 
						this.v5(), this.v6(), this.v7(), this.v8());
			return_value[4] = 
				new Packed8Simplex(this.v1(), this.v2(), this.v3(), 
						this.v4(), this.v6(), this.v7(), this.v8());
			return_value[5] = 
				new Packed8Simplex(this.v1(), this.v2(), this.v3(), 
						this.v4(), this.v5(), this.v7(), this.v8());
			return_value[6] = 
				new Packed8Simplex(this.v1(), this.v2(), this.v3(), 
						this.v4(), this.v5(), this.v6(), this.v8());
			return_value[7] = 
				new Packed8Simplex(this.v1(), this.v2(), this.v3(), 
						this.v4(), this.v5(), this.v6(), this.v7());
		}
		return return_value;
	}
}
