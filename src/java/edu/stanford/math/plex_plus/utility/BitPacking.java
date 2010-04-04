package edu.stanford.math.plex_plus.utility;

public class BitPacking {
	protected static int VERTEX_BIT_SZ = 32;
	protected static int MAX_2_INDEX = Integer.MAX_VALUE;
	protected static long DIM_0_MASK = ~((long) MAX_2_INDEX);
	
	public static long pack(int i, int j) {
		return ((((long) j) << VERTEX_BIT_SZ) | ((long) i));
	}
	
	public static int unpackFirstComponent(long l) {
		return (int) (l & MAX_2_INDEX);
	}
	
	public static int unpackSecondComponent(long l) {
		return (int) ((l >>> VERTEX_BIT_SZ) & MAX_2_INDEX);
	}
}
