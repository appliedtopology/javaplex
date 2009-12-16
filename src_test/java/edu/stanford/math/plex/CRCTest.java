package edu.stanford.math.plex;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

/**
 * The <code>CRCTest</code> class.
 *
 * <p>Among the facilities provided by the <code>CRCTest</code> class
 * are whatever we want it to do.
 *
 * @version $ID$
 */
public class CRCTest {

	private java.util.List emptyList;

	private static Random rand = new Random();

	static byte[] make_random_vecb(int len) {
		byte[] return_val = new byte[len];
		for (int i = 0; i < len; i++)
			return_val[i] = (byte)rand.nextInt();
		return return_val;
	}

	static short[] make_random_vecs(int len) {
		short[] return_val = new short[len];
		for (int i = 0; i < len; i++)
			return_val[i] = (short)rand.nextInt();
		return return_val;
	}

	static int[] make_random_veci(int len) {
		int[] return_val = new int[len];
		for (int i = 0; i < len; i++)
			return_val[i] = rand.nextInt();
		return return_val;
	}


	static long[] make_random_vecl(int len) {
		long[] return_val = new long[len];
		for (int i = 0; i < len; i++)
			return_val[i] = rand.nextLong();
		return return_val;
	}


	/**
	 * Sets up the test fixture. 
	 * (Called before every test case method.)
	 */
	@Before
	public void setUp() {
		emptyList = new java.util.ArrayList();
	}

	/**
	 * Tears down the test fixture. 
	 * (Called after every test case method.)
	 */
	@After
	public void tearDown() {
		emptyList = null;
	}

	@Test
	public void testSomeBehavior() {




		assertEquals("hash64() vs known results", 6032571786410196032L, 
				CRC.hash64(6493655788058667850L, -8059720649035354885L));
		assertEquals("hash64() vs known results", -7545405129268806290L, 
				CRC.hash64(-4826024319188083941L, 2296441913178217571L));
		assertEquals("hash64() vs known results", 8482303710699910840L, 
				CRC.hash64(7793617151736177165L, -8742675927873901475L));
		assertEquals("hash64() vs known results", 7186347300825138971L, 
				CRC.hash64(-8590399596178740821L, -3136225913772387823L));
		assertEquals("hash64() vs known results", 6361592763524364868L, 
				CRC.hash64(7966436672045297884L, -7513189249503151647L));
		assertEquals("hash64() vs known results", -7567370645069542008L, 
				CRC.hash64(-7877688441466048790L, -8284931960287653334L));
		assertEquals("hash64() vs known results", 1837677831603613060L, 
				CRC.hash64(1705758918867886652L, -4608094936528631270L));
		assertEquals("hash64() vs known results", -8324081773154746428L, 
				CRC.hash64(-2333750770659246085L, -7151290098236933228L));
		assertEquals("hash64() vs known results", 3238470636471372185L, 
				CRC.hash64(-8333681197765536164L, 149075111678084331L));
		assertEquals("hash64() vs known results", 1172654483595358935L, 
				CRC.hash64(-5226751851997133591L, 3928364761344881872L));

		assertEquals("hash64() vs known results", 
				-3153723631256078275L, CRC.hash64(6493655788058667850L, 0));
		assertEquals("hash64() vs known results", 
				3532864025510917699L, CRC.hash64(-8059720649035354885L, 0));
		assertEquals("hash64() vs known results", 
				834313021983521126L, CRC.hash64(-4826024319188083941L, 0));
		assertEquals("hash64() vs known results", 
				-233124034201179335L, CRC.hash64(2296441913178217571L, 0));
		assertEquals("hash64() vs known results", 
				-8035689059781312722L, CRC.hash64(7793617151736177165L, 0));
		assertEquals("hash64() vs known results", 
				-4713891799828465575L, CRC.hash64(-8742675927873901475L, 0));
		assertEquals("hash64() vs known results", 
				8538319044851116518L, CRC.hash64(-8590399596178740821L, 0));
		assertEquals("hash64() vs known results", 
				6071268795809598228L, CRC.hash64(-3136225913772387823L, 0));
		assertEquals("hash64() vs known results", 
				-1436724243492114223L, CRC.hash64(7966436672045297884L, 0));
		assertEquals("hash64() vs known results", 
				-7638551461710678782L, CRC.hash64(-7513189249503151647L, 0));

		assertEquals("hash32() vs known results", 
				388237006, CRC.hash32(-7258739192358877901L, 0));
		assertEquals("hash32() vs known results", 
				-577998612, CRC.hash32(5838237949386953820L, 0));
		assertEquals("hash32() vs known results", 
				1247909994, CRC.hash32(-3662795734954562691L, 0));
		assertEquals("hash32() vs known results", 
				840130721, CRC.hash32(5568351980849578531L, 0));
		assertEquals("hash32() vs known results", 
				-934514563, CRC.hash32(8685604269466863285L, 0));
		assertEquals("hash32() vs known results", 
				-1748466434, CRC.hash32(-4872510571857784608L, 0));
		assertEquals("hash32() vs known results", 
				-1163897527, CRC.hash32(-4062244458711989170L, 0));
		assertEquals("hash32() vs known results", 
				-1660784663, CRC.hash32(8190802628558026886L, 0));
		assertEquals("hash32() vs known results", 
				177484261, CRC.hash32(-4771253648750399554L, 0));
		assertEquals("hash32() vs known results", 
				74797589, CRC.hash32(8220262932303541715L, 0));

	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testForException() {
		@SuppressWarnings("unused")
		Object o = emptyList.get(0);
	}
}

