package edu.stanford.math.plex_plus.utility;

/**
 * The <code>CRC</code> class provides good hash methods for int/long and string data.
 * 
 * @author Harlan Sexton
 * 
 * @version $Id$
 */
public class CRC {

	private CRC() {
		// no instances
	}

	// CRC Hashing Explained:
	//
	// The underlying idea here is that the string (or whatever) being
	// hashed is regarded as being a polynomial in Z/2[X].  (This is
	// trivial, but in case it isn't obvious, you can identify an array of
	// length N of bytes with the integer formed by summing the terms
	// (byte[i]*(8**(N - (1+i)))) -- in other words, look at the entire
	// array of bytes as one giant bitfield, with the "significance" of the
	// bits decreasing as you move from left to right.  Then, you can
	// identify an integer I with p(x) in Z/2[X] such that the coefficient
	// of X**k is non-zero iff the LOGICAL_AND (2**k, I) is non-zero.)
	// 
	// The trick is to pick some polynomial M(x), and take as the hash
	// value for the string the remainder of the string modulo M(x).
	// Clearly, in order for the strings to hash to more possible values,
	// the degree of M should be as large as feasible. In order to make the
	// make the hash values "more random" for short strings, we "multiply"
	// the string by some power of X. To make this more concrete, let M be
	// a polynomial of degree 32, and shift the strings 32bits (multiply by
	// X**32). In other words, define the hash value of a byte array to be
	// the remainder, modulo M(x), of the new array constructed by
	// appending 4 null bytes. What was not instantly obvious to me is that
	// this calculation can be done, by using a table, examining the string
	// a byte at a time. Here's how this works:
	// 
	// Let string = [Byte0][Byte1][Byte2][Byte3][Byte4][Byte5]....
	// 
	// and M(x)   = 1[byte0][byte1][byte2][byte3]
	// 
	// Now, string is equal to
	// 
	// string = [00000][Byte1][Byte2][Byte3][Byte4][Byte5]....
	// +[Byte0]...all zeros....
	// 
	// and this has the same remainder as
	// 
	// string1 = [00000][Byte1][Byte2][Byte3][Byte4][Byte5]....
	// +[00000][rem-0][rem-1][rem-2][rem-4]....all zeros...
	// 
	// where the bytes rem-i are the bytes of the remainder of
	// [Byte0][00000][00000][00000][00000] when divided by M(x). (This may
	// need some thinking about, but it is straightforward once you
	// remember that the remainder is computed by successively XORing
	// shifts of M(x) so that the leading bit of M(x) cancels the
	// appropriate bit of the polynomial we are dividing -- this means that
	// any bits more than 4 bytes from the "last cancelled bit", that is,
	// bits in [Byte5] and beyond are unaffected.)
	// 
	// That means that the remainder of string is the same as the remainder of
	// 
	// string1 = [Byte1][Byte2][Byte3][Byte4][Byte5]....
	// +[rem-0][rem-1][rem-2][rem-4]....all zeros...
	// 
	// or string1 = [Btr_1][Btr_2][Btr_3][Btr_4][Btr_5]....
	// 
	// where Btr_i = rem-i XOR Bytei, for i <= 4, and Bytei for i > 4.
	// 
	// 
	// In other words, for a given string, we process the first byte by
	// taking the 32bit remainder of that "shifted" byte (and since there
	// are only 256 of these bytes, we can compute these remainders and
	// keep them in a table), and then XOR that remainder word with the
	// succeeding 4 bytes in the string. Further, since XOR is associative
	// and commutative, we can just keep that remainder in an accumulator,
	// and do the XORing "virtually".
	// 
	// To see this, consider the next step in the hashing:
	// 
	// string1 = [Byte1][Byte2][Byte3][Byte4][Byte5]....
	// +[rem-0][rem-1][rem-2][rem-4]....all zeros...
	// 
	// has the same remainder as
	// 
	// string2 = [Byte2][Byte3][Byte4][Byte5]....
	// +[rem-1][rem-2][rem-4]....all zeros...
	// +[rem21][rem22][rem21][rem22]
	// 
	// where [rem2i] is the remainder of [Byte1 ^ rem-0][0][0][0][0].
	// 
	// This, in turn is equal to
	// 
	// string2 = [Byte2][Byte3][Byte4][Byte5]....
	// +[rem2-0][rem2-1][rem2-3][rem2-4]....all zeros...
	// 
	// where the rem2-i are the bytes of ((first-rem) << BITS_PER_BYTE) XOR
	// (second-rem).  In other words, the algorithm is to start with
	// ACCUMULATOR = 0, and looping through each i in the byte array,
	// 
	// table_index = byte[i] ^ (ACCUMULATOR>> (BITS_PER_ULONG - BITS_PER_BYTE));
	// ACCUMULATOR = (ACCUMULATOR << BITS_PER_BYTE) ^ REMAINDER_TABLE[table_index];
	// 
	// Now, looking at this code, we see we can eliminate one shift and replace
	// it by a logical and, if we make an alteration. Let the contents of
	// REMAINDER_TABLE be byte-reversed (that is, the bytes are present, but
	// byte[i] is switched with byte[3-i]). Then, start with REV_ACCUMULATOR =
	// 0; and loop through each i,
	// 
	// table_index = byte[i] ^ (REV_ACCUMULATOR && 0xff);
	// ACCUMULATOR = (REV_ACCUMULATOR >> 8) ^ REV_REMAINDER_TABLE[table_index];
	// 
	// Now, whether this is better or not is somewhat unclear -- there is
	// no perceptible difference on a Sun4, and I doubt that there is on
	// any RISC machine, although I suppose some CISC machines might
	// magically do something to do the first line just above in a single
	// instruction.
	// 
	// This thing is a linear code, and I seem to recall that it is clear
	// (during the short time that one remembers the details) that the
	// polynomial should be prime. In fact, if you choose one with the
	// correct roots, you can guarantee that nothing in the kernel of the
	// linear map is of degree <= K (excluding 0, of course) for some K
	// that's bigger than you think... The reference for this code, a paper
	// which is much less clear than this comment and has a lot of
	// extraneous junk and no theory in it, is: Aram Perez, IEEE Micro,
	// June 1983, pp.  40-50.  Knuth has a short reference that is better,
	// but doesn't explain how to use a table to speed up the calculation.
	// Finally, the table is made from the 33bit number 0x1e4725ca3, which
	// is a prime polynomial of degree 32.
	// 
	// A moment's reflection will convince you that this same argument
	// applies to any prime of degree B*n (where B is the number of bits in
	// a byte).  The question is how to apply this method when you want to
	// use a polynomial Q(x) of degree, say, 29. The answer is simple. Make
	// a table as above, and apply the same algorithm. You will get a
	// "remainder" which is of degree 31 -- this is a polynomial that has
	// the same remainder modulo Q(x) as the original value, so all that is
	// necessary is to compute the remainders of possible polynomials of
	// the form:
	// 
	// [Byte][0][0][0] 
	// 
	// modulo Q(x). That is, you need a second ("dregs") table of the same
	// size mas the first, and you have to apply the iterative step of the
	// algorithm one last time using the dregs table.


	/**
	 * Internal table used by CRC code.
	 */

	// This table is made from the 65bit number 0x1000000000000001b,
	// considered as a prime polynomial of degree 64. The polynomial x64 +
	// x4 + x3 + x + 1 (as described in ISO 3309) is prime, and presumably
	// the advantage that the entries in the table are smaller is one of
	// the reasons that it was chosen. We also have a table below made from
	// a prime with a lot more bits in it.
	protected static long[] hash64tab0 = new long[] { 
		0x000L, 0x01bL, 0x036L, 0x02dL, 0x06cL, 0x077L, 
		0x05aL, 0x041L, 0x0d8L, 0x0c3L, 0x0eeL, 0x0f5L, 
		0x0b4L, 0x0afL, 0x082L, 0x099L, 0x1b0L, 0x1abL, 
		0x186L, 0x19dL, 0x1dcL, 0x1c7L, 0x1eaL, 0x1f1L, 
		0x168L, 0x173L, 0x15eL, 0x145L, 0x104L, 0x11fL, 
		0x132L, 0x129L, 0x360L, 0x37bL, 0x356L, 0x34dL, 
		0x30cL, 0x317L, 0x33aL, 0x321L, 0x3b8L, 0x3a3L, 
		0x38eL, 0x395L, 0x3d4L, 0x3cfL, 0x3e2L, 0x3f9L, 
		0x2d0L, 0x2cbL, 0x2e6L, 0x2fdL, 0x2bcL, 0x2a7L, 
		0x28aL, 0x291L, 0x208L, 0x213L, 0x23eL, 0x225L, 
		0x264L, 0x27fL, 0x252L, 0x249L, 0x6c0L, 0x6dbL, 
		0x6f6L, 0x6edL, 0x6acL, 0x6b7L, 0x69aL, 0x681L, 
		0x618L, 0x603L, 0x62eL, 0x635L, 0x674L, 0x66fL, 
		0x642L, 0x659L, 0x770L, 0x76bL, 0x746L, 0x75dL, 
		0x71cL, 0x707L, 0x72aL, 0x731L, 0x7a8L, 0x7b3L, 
		0x79eL, 0x785L, 0x7c4L, 0x7dfL, 0x7f2L, 0x7e9L, 
		0x5a0L, 0x5bbL, 0x596L, 0x58dL, 0x5ccL, 0x5d7L, 
		0x5faL, 0x5e1L, 0x578L, 0x563L, 0x54eL, 0x555L, 
		0x514L, 0x50fL, 0x522L, 0x539L, 0x410L, 0x40bL, 
		0x426L, 0x43dL, 0x47cL, 0x467L, 0x44aL, 0x451L, 
		0x4c8L, 0x4d3L, 0x4feL, 0x4e5L, 0x4a4L, 0x4bfL, 
		0x492L, 0x489L, 0xd80L, 0xd9bL, 0xdb6L, 0xdadL, 
		0xdecL, 0xdf7L, 0xddaL, 0xdc1L, 0xd58L, 0xd43L, 
		0xd6eL, 0xd75L, 0xd34L, 0xd2fL, 0xd02L, 0xd19L, 
		0xc30L, 0xc2bL, 0xc06L, 0xc1dL, 0xc5cL, 0xc47L, 
		0xc6aL, 0xc71L, 0xce8L, 0xcf3L, 0xcdeL, 0xcc5L, 
		0xc84L, 0xc9fL, 0xcb2L, 0xca9L, 0xee0L, 0xefbL, 
		0xed6L, 0xecdL, 0xe8cL, 0xe97L, 0xebaL, 0xea1L, 
		0xe38L, 0xe23L, 0xe0eL, 0xe15L, 0xe54L, 0xe4fL, 
		0xe62L, 0xe79L, 0xf50L, 0xf4bL, 0xf66L, 0xf7dL, 
		0xf3cL, 0xf27L, 0xf0aL, 0xf11L, 0xf88L, 0xf93L, 
		0xfbeL, 0xfa5L, 0xfe4L, 0xfffL, 0xfd2L, 0xfc9L, 
		0xb40L, 0xb5bL, 0xb76L, 0xb6dL, 0xb2cL, 0xb37L, 
		0xb1aL, 0xb01L, 0xb98L, 0xb83L, 0xbaeL, 0xbb5L, 
		0xbf4L, 0xbefL, 0xbc2L, 0xbd9L, 0xaf0L, 0xaebL, 
		0xac6L, 0xaddL, 0xa9cL, 0xa87L, 0xaaaL, 0xab1L, 
		0xa28L, 0xa33L, 0xa1eL, 0xa05L, 0xa44L, 0xa5fL, 
		0xa72L, 0xa69L, 0x820L, 0x83bL, 0x816L, 0x80dL, 
		0x84cL, 0x857L, 0x87aL, 0x861L, 0x8f8L, 0x8e3L, 
		0x8ceL, 0x8d5L, 0x894L, 0x88fL, 0x8a2L, 0x8b9L, 
		0x990L, 0x98bL, 0x9a6L, 0x9bdL, 0x9fcL, 0x9e7L, 
		0x9caL, 0x9d1L, 0x948L, 0x953L, 0x97eL, 0x965L, 
		0x924L, 0x93fL, 0x912L, 0x909L };

	/**
	 * Internal table used by CRC code.
	 */

	// This table is made from the 65bit number 0x1dbf5da0ff9e2a9e3,
	// considered as a prime polynomial of degree 64. */
	protected static long[] hash64tab1 = new long[] { 
		0x0000000000000000L, 0xdbf5da0ff9e2a9e3L, 0x6c1e6e100a27fa25L, 
		0xb7ebb41ff3c553c6L, 0xd83cdc20144ff44aL, 0x03c9062fedad5da9L, 
		0xb422b2301e680e6fL, 0x6fd7683fe78aa78cL, 0x6b8c624fd17d4177L,
		0xb079b840289fe894L, 0x07920c5fdb5abb52L, 0xdc67d65022b812b1L, 
		0xb3b0be6fc532b53dL, 0x684564603cd01cdeL, 0xdfaed07fcf154f18L,
		0x045b0a7036f7e6fbL, 0xd718c49fa2fa82eeL, 0x0ced1e905b182b0dL,
		0xbb06aa8fa8dd78cbL, 0x60f37080513fd128L, 0x0f2418bfb6b576a4L, 
		0xd4d1c2b04f57df47L, 0x633a76afbc928c81L, 0xb8cfaca045702562L, 
		0xbc94a6d07387c399L, 0x67617cdf8a656a7aL, 0xd08ac8c079a039bcL,
		0x0b7f12cf8042905fL, 0x64a87af067c837d3L, 0xbf5da0ff9e2a9e30L,
		0x08b614e06defcdf6L, 0xd343ceef940d6415L, 0x75c45330bc17ac3fL, 
		0xae31893f45f505dcL, 0x19da3d20b630561aL, 0xc22fe72f4fd2fff9L, 
		0xadf88f10a8585875L, 0x760d551f51baf196L, 0xc1e6e100a27fa250L,
		0x1a133b0f5b9d0bb3L, 0x1e48317f6d6aed48L, 0xc5bdeb70948844abL,
		0x72565f6f674d176dL, 0xa9a385609eafbe8eL, 0xc674ed5f79251902L, 
		0x1d81375080c7b0e1L, 0xaa6a834f7302e327L, 0x719f59408ae04ac4L, 
		0xa2dc97af1eed2ed1L, 0x79294da0e70f8732L, 0xcec2f9bf14cad4f4L,
		0x153723b0ed287d17L, 0x7ae04b8f0aa2da9bL, 0xa1159180f3407378L,
		0x16fe259f008520beL, 0xcd0bff90f967895dL, 0xc950f5e0cf906fa6L, 
		0x12a52fef3672c645L, 0xa54e9bf0c5b79583L, 0x7ebb41ff3c553c60L, 
		0x116c29c0dbdf9becL, 0xca99f3cf223d320fL, 0x7d7247d0d1f861c9L,
		0xa6879ddf281ac82aL, 0xeb88a661782f587eL, 0x307d7c6e81cdf19dL,
		0x8796c8717208a25bL, 0x5c63127e8bea0bb8L, 0x33b47a416c60ac34L, 
		0xe841a04e958205d7L, 0x5faa145166475611L, 0x845fce5e9fa5fff2L, 
		0x8004c42ea9521909L, 0x5bf11e2150b0b0eaL, 0xec1aaa3ea375e32cL,
		0x37ef70315a974acfL, 0x5838180ebd1ded43L, 0x83cdc20144ff44a0L,
		0x3426761eb73a1766L, 0xefd3ac114ed8be85L, 0x3c9062fedad5da90L, 
		0xe765b8f123377373L, 0x508e0ceed0f220b5L, 0x8b7bd6e129108956L, 
		0xe4acbedece9a2edaL, 0x3f5964d137788739L, 0x88b2d0cec4bdd4ffL,
		0x53470ac13d5f7d1cL, 0x571c00b10ba89be7L, 0x8ce9dabef24a3204L,
		0x3b026ea1018f61c2L, 0xe0f7b4aef86dc821L, 0x8f20dc911fe76fadL, 
		0x54d5069ee605c64eL, 0xe33eb28115c09588L, 0x38cb688eec223c6bL, 
		0x9e4cf551c438f441L, 0x45b92f5e3dda5da2L, 0xf2529b41ce1f0e64L,
		0x29a7414e37fda787L, 0x46702971d077000bL, 0x9d85f37e2995a9e8L,
		0x2a6e4761da50fa2eL, 0xf19b9d6e23b253cdL, 0xf5c0971e1545b536L, 
		0x2e354d11eca71cd5L, 0x99def90e1f624f13L, 0x422b2301e680e6f0L, 
		0x2dfc4b3e010a417cL, 0xf6099131f8e8e89fL, 0x41e2252e0b2dbb59L,
		0x9a17ff21f2cf12baL, 0x495431ce66c276afL, 0x92a1ebc19f20df4cL,
		0x254a5fde6ce58c8aL, 0xfebf85d195072569L, 0x9168edee728d82e5L, 
		0x4a9d37e18b6f2b06L, 0xfd7683fe78aa78c0L, 0x268359f18148d123L, 
		0x22d85381b7bf37d8L, 0xf92d898e4e5d9e3bL, 0x4ec63d91bd98cdfdL,
		0x9533e79e447a641eL, 0xfae48fa1a3f0c392L, 0x211155ae5a126a71L,
		0x96fae1b1a9d739b7L, 0x4d0f3bbe50359054L, 0x0ce496cd09bc191fL, 
		0xd7114cc2f05eb0fcL, 0x60faf8dd039be33aL, 0xbb0f22d2fa794ad9L, 
		0xd4d84aed1df3ed55L, 0x0f2d90e2e41144b6L, 0xb8c624fd17d41770L,
		0x6333fef2ee36be93L, 0x6768f482d8c15868L, 0xbc9d2e8d2123f18bL,
		0x0b769a92d2e6a24dL, 0xd083409d2b040baeL, 0xbf5428a2cc8eac22L, 
		0x64a1f2ad356c05c1L, 0xd34a46b2c6a95607L, 0x08bf9cbd3f4bffe4L, 
		0xdbfc5252ab469bf1L, 0x0009885d52a43212L, 0xb7e23c42a16161d4L,
		0x6c17e64d5883c837L, 0x03c08e72bf096fbbL, 0xd835547d46ebc658L,
		0x6fdee062b52e959eL, 0xb42b3a6d4ccc3c7dL, 0xb070301d7a3bda86L, 
		0x6b85ea1283d97365L, 0xdc6e5e0d701c20a3L, 0x079b840289fe8940L, 
		0x684cec3d6e742eccL, 0xb3b936329796872fL, 0x0452822d6453d4e9L,
		0xdfa758229db17d0aL, 0x7920c5fdb5abb520L, 0xa2d51ff24c491cc3L,
		0x153eabedbf8c4f05L, 0xcecb71e2466ee6e6L, 0xa11c19dda1e4416aL, 
		0x7ae9c3d25806e889L, 0xcd0277cdabc3bb4fL, 0x16f7adc2522112acL, 
		0x12aca7b264d6f457L, 0xc9597dbd9d345db4L, 0x7eb2c9a26ef10e72L,
		0xa54713ad9713a791L, 0xca907b927099001dL, 0x1165a19d897ba9feL,
		0xa68e15827abefa38L, 0x7d7bcf8d835c53dbL, 0xae380162175137ceL, 
		0x75cddb6deeb39e2dL, 0xc2266f721d76cdebL, 0x19d3b57de4946408L, 
		0x7604dd42031ec384L, 0xadf1074dfafc6a67L, 0x1a1ab352093939a1L,
		0xc1ef695df0db9042L, 0xc5b4632dc62c76b9L, 0x1e41b9223fcedf5aL,
		0xa9aa0d3dcc0b8c9cL, 0x725fd73235e9257fL, 0x1d88bf0dd26382f3L, 
		0xc67d65022b812b10L, 0x7196d11dd84478d6L, 0xaa630b1221a6d135L, 
		0xe76c30ac71934161L, 0x3c99eaa38871e882L, 0x8b725ebc7bb4bb44L,
		0x508784b3825612a7L, 0x3f50ec8c65dcb52bL, 0xe4a536839c3e1cc8L,
		0x534e829c6ffb4f0eL, 0x88bb58939619e6edL, 0x8ce052e3a0ee0016L, 
		0x571588ec590ca9f5L, 0xe0fe3cf3aac9fa33L, 0x3b0be6fc532b53d0L, 
		0x54dc8ec3b4a1f45cL, 0x8f2954cc4d435dbfL, 0x38c2e0d3be860e79L,
		0xe3373adc4764a79aL, 0x3074f433d369c38fL, 0xeb812e3c2a8b6a6cL,
		0x5c6a9a23d94e39aaL, 0x879f402c20ac9049L, 0xe8482813c72637c5L, 
		0x33bdf21c3ec49e26L, 0x84564603cd01cde0L, 0x5fa39c0c34e36403L, 
		0x5bf8967c021482f8L, 0x800d4c73fbf62b1bL, 0x37e6f86c083378ddL,
		0xec132263f1d1d13eL, 0x83c44a5c165b76b2L, 0x58319053efb9df51L,
		0xefda244c1c7c8c97L, 0x342ffe43e59e2574L, 0x92a8639ccd84ed5eL, 
		0x495db993346644bdL, 0xfeb60d8cc7a3177bL, 0x2543d7833e41be98L, 
		0x4a94bfbcd9cb1914L, 0x916165b32029b0f7L, 0x268ad1acd3ece331L,
		0xfd7f0ba32a0e4ad2L, 0xf92401d31cf9ac29L, 0x22d1dbdce51b05caL,
		0x953a6fc316de560cL, 0x4ecfb5ccef3cffefL, 0x2118ddf308b65863L, 
		0xfaed07fcf154f180L, 0x4d06b3e30291a246L, 0x96f369ecfb730ba5L, 
		0x45b0a7036f7e6fb0L, 0x9e457d0c969cc653L, 0x29aec91365599595L,
		0xf25b131c9cbb3c76L, 0x9d8c7b237b319bfaL, 0x4679a12c82d33219L,
		0xf1921533711661dfL, 0x2a67cf3c88f4c83cL, 0x2e3cc54cbe032ec7L, 
		0xf5c91f4347e18724L, 0x4222ab5cb424d4e2L, 0x99d771534dc67d01L, 
		0xf600196caa4cda8dL, 0x2df5c36353ae736eL, 0x9a1e777ca06b20a8L, 
		0x41ebad735989894bL };

	/**
	 * Internal table used by CRC code.
	 */

	// This table is made from the 33bit number 0x1e4725ca3, considered
	// as a prime polynomial of degree 32.
	protected static int[] hash32tab0 = new int[] { 
		0x00000000, 0xe4725ca3, 0x2c96e5e5, 0xc8e4b946, 0x592dcbca,
		0xbd5f9769, 0x75bb2e2f, 0x91c9728c, 0xb25b9794, 0x5629cb37,
		0x9ecd7271, 0x7abf2ed2, 0xeb765c5e, 0x0f0400fd, 0xc7e0b9bb,
		0x2392e518, 0x80c5738b, 0x64b72f28, 0xac53966e, 0x4821cacd,
		0xd9e8b841, 0x3d9ae4e2, 0xf57e5da4, 0x110c0107, 0x329ee41f,
		0xd6ecb8bc, 0x1e0801fa, 0xfa7a5d59, 0x6bb32fd5, 0x8fc17376,
		0x4725ca30, 0xa3579693, 0xe5f8bbb5, 0x018ae716, 0xc96e5e50,
		0x2d1c02f3, 0xbcd5707f, 0x58a72cdc, 0x9043959a, 0x7431c939,
		0x57a32c21, 0xb3d17082, 0x7b35c9c4, 0x9f479567, 0x0e8ee7eb,
		0xeafcbb48, 0x2218020e, 0xc66a5ead, 0x653dc83e, 0x814f949d,
		0x49ab2ddb, 0xadd97178, 0x3c1003f4, 0xd8625f57, 0x1086e611,
		0xf4f4bab2, 0xd7665faa, 0x33140309, 0xfbf0ba4f, 0x1f82e6ec,
		0x8e4b9460, 0x6a39c8c3, 0xa2dd7185, 0x46af2d26, 0x2f832bc9,
		0xcbf1776a, 0x0315ce2c, 0xe767928f, 0x76aee003, 0x92dcbca0,
		0x5a3805e6, 0xbe4a5945, 0x9dd8bc5d, 0x79aae0fe, 0xb14e59b8,
		0x553c051b, 0xc4f57797, 0x20872b34, 0xe8639272, 0x0c11ced1,
		0xaf465842, 0x4b3404e1, 0x83d0bda7, 0x67a2e104, 0xf66b9388,
		0x1219cf2b, 0xdafd766d, 0x3e8f2ace, 0x1d1dcfd6, 0xf96f9375,
		0x318b2a33, 0xd5f97690, 0x4430041c, 0xa04258bf, 0x68a6e1f9,
		0x8cd4bd5a, 0xca7b907c, 0x2e09ccdf, 0xe6ed7599, 0x029f293a,
		0x93565bb6, 0x77240715, 0xbfc0be53, 0x5bb2e2f0, 0x782008e8,
		0x9c525b4b, 0x54b6e20d, 0xb0c4beae, 0x210dcc22, 0xc57f9081,
		0x0d9b29c7, 0xe9e97564, 0x4abee3f7, 0xaeccbf54, 0x66280612,
		0x825a5ab1, 0x1393283d, 0xf7e1749e, 0x3f05cdd8, 0xdb77917b,
		0xf8e57463, 0x1c9728c0, 0xd4739186, 0x3001cd25, 0xa1c8bfa9,
		0x45bae30a, 0x8d5e5a4c, 0x692c06ef, 0x5f065792, 0xbb740b31,
		0x7390b277, 0x97e2eed4, 0x062b9c58, 0xe259c0fb, 0x2abd79bd,
		0xcecf251e, 0xed5dc006, 0x092f9ca5, 0xc1cb25e3, 0x25b97940,
		0xb4700bcc, 0x5002576f, 0x98e6ee29, 0x7c94b28a, 0xdfc32419,
		0x3bb178ba, 0xf355c1fc, 0x17279d5f, 0x86eeefd3, 0x629cb370,
		0xaa780a36, 0x4e0a5695, 0x6d98b38d, 0x89eaef2e, 0x410e5668,
		0xa57c0acb, 0x34b57847, 0xd0c724e4, 0x18239da2, 0xfc51c101,
		0xbafeec27, 0x5e8cb084, 0x966809c2, 0x721a5561, 0xe3d327ed,
		0x07a17b4e, 0xcf45c208, 0x2b379eab, 0x08a57bb3, 0xecd72710,
		0x24339e56, 0xc041c2f5, 0x5188b079, 0xb5faecda, 0x7d1e559c,
		0x996c093f, 0x3a3b9fac, 0xde49c30f, 0x16ad7a49, 0xf2df26ea,
		0x63165466, 0x876408c5, 0x4f80b183, 0xabf2ed20, 0x88600838,
		0x6c12549b, 0xa4f6eddd, 0x4084b17e, 0xd14dc3f2, 0x353f9f51,
		0xfddb2617, 0x19a97ab4, 0x70857c5b, 0x94f720f8, 0x5c1399be,
		0xb861c51d, 0x29a8b791, 0xcddaeb32, 0x053e5274, 0xe14c0ed7,
		0xc2deebcf, 0x26acb76c, 0xee480e2a, 0x0a3a5289, 0x9bf32005,
		0x7f817ca6, 0xb765c5e0, 0x53179943, 0xf0400fd0, 0x14325373,
		0xdcd6ea35, 0x38a4b696, 0xa96dc41a, 0x4d1f98b9, 0x85fb21ff,
		0x61897d5c, 0x421b9844, 0xa669c4e7, 0x6e8d7da1, 0x8aff2102,
		0x1b36538e, 0xff440f2d, 0x37a0b66b, 0xd3d2eac8, 0x957dc7ee,
		0x710f9b4d, 0xb9eb220b, 0x5d997ea8, 0xcc500c24, 0x28225087,
		0xe0c6e9c1, 0x04b4b562, 0x2726507a, 0xc3540cd9, 0x0bb0b59f,
		0xefc2e93c, 0x7e0b9bb0, 0x9a79c713, 0x529d7e55, 0xb6ef22f6,
		0x15b8b465, 0xf1cae8c6, 0x392e5180, 0xdd5c0d23, 0x4c957faf,
		0xa8e7230c, 0x60039a4a, 0x8471c6e9, 0xa7e323f1, 0x43917f52,
		0x8b75c614, 0x6f079ab7, 0xfecee83b, 0x1abcb498, 0xd2580dde,
		0x362a517d };

	/**
	 * Incremental 32-bit CRC calculation for a single byte.
	 *
	 * <p>
	 *
	 * @param      b   The next data in the CRC calculation (only lowest byte is used).
	 * @param      hcode   The current int result.
	 * @return     Updated int value of CRC calculation.
	 */
	protected static int update32l(long b, int hcode, int[] tbl) {
		return ( hcode << 8 ) ^ tbl[(int)((b ^ ( hcode >>> 24 )) & 0xffL)];
	}

	/**
	 * Incremental 32-bit CRC calculation for a single byte.
	 *
	 * <p>
	 *
	 * @param      b   The next data in the CRC calculation.
	 * @param      hcode   The current long result.
	 * @return     Updated int value of CRC calculation.
	 */
	protected static int update32(byte b, int hcode, int[] tbl) {
		hcode = update32l(b, hcode, tbl);
		return hcode;
	}

	/**
	 * Incremental 32-bit CRC calculation for a single short.
	 *
	 * <p>
	 *
	 * @param      s   The next data in the CRC calculation.
	 * @param      hcode   The current long result.
	 * @return     Updated int value of CRC calculation.
	 */
	protected static int update32(short s, int hcode, int[] tbl) {
		hcode = update32l(s, hcode, tbl);
		hcode = update32l((s >>> 8), hcode, tbl);
		return hcode;
	}

	/**
	 * Incremental 32-bit CRC calculation for a single int.
	 *
	 * <p>
	 *
	 * @param      i   The next data in the CRC calculation.
	 * @param      hcode   The current long result.
	 * @return     Updated int value of CRC calculation.
	 */
	protected static int update32(int i, int hcode, int[] tbl) {
		hcode = update32l(i, hcode, tbl);
		hcode = update32l((i >>> 8), hcode, tbl);
		hcode = update32l((i >>> 16), hcode, tbl);
		hcode = update32l((i >>> 24), hcode, tbl);
		return hcode;
	}

	/**
	 * Incremental 32-bit CRC calculation for a single long.
	 *
	 * <p>
	 *
	 * @param      l   The next data in the CRC calculation.
	 * @param      hcode   The current long result.
	 * @return     Updated int value of CRC calculation.
	 */
	protected static int update32(long l, int hcode, int[] tbl) {
		hcode = update32l(l, hcode, tbl);
		hcode = update32l((l >>> 8), hcode, tbl);
		hcode = update32l((l >>> 16), hcode, tbl);
		hcode = update32l((l >>> 24), hcode, tbl);
		hcode = update32l((l >>> 32), hcode, tbl);
		hcode = update32l((l >>> 40), hcode, tbl);
		hcode = update32l((l >>> 48), hcode, tbl);
		hcode = update32l((l >>> 56), hcode, tbl);
		return hcode;
	}


	/**
	 * Incremental 64-bit CRC calculation for a single byte.
	 *
	 * <p>
	 *
	 * @param      b   The next data in the CRC calculation (only lowest byte is used).
	 * @param      hcode   The current long result.
	 * @return     Updated long value of CRC calculation.
	 */
	protected static long update64l(long b, long hcode, long[] tbl) {
		return ( hcode << 8 ) ^ tbl[(int)((b ^ ( hcode >>> 56 )) & 0xffL)];
	}

	/**
	 * Incremental 64-bit CRC calculation for a single byte.
	 *
	 * <p>
	 *
	 * @param      b   The next data in the CRC calculation.
	 * @param      hcode   The current long result.
	 * @return     Updated long value of CRC calculation.
	 */
	protected static long update64(byte b, long hcode, long[] tbl) {
		hcode = update64l(b, hcode, tbl);
		return hcode;
	}

	/**
	 * Incremental 64-bit CRC calculation for a single short.
	 *
	 * <p>
	 *
	 * @param      s   The next data in the CRC calculation.
	 * @param      hcode   The current long result.
	 * @return     Updated long value of CRC calculation.
	 */
	protected static long update64(short s, long hcode, long[] tbl) {
		hcode = update64l(s, hcode, tbl);
		hcode = update64l((s >>> 8), hcode, tbl);
		return hcode;
	}

	/**
	 * Incremental 64-bit CRC calculation for a single int.
	 *
	 * <p>
	 *
	 * @param      i   The next data in the CRC calculation.
	 * @param      hcode   The current long result.
	 * @return     Updated long value of CRC calculation.
	 */
	protected static long update64(int i, long hcode, long[] tbl) {
		hcode = update64l(i, hcode, tbl);
		hcode = update64l((i >>> 8), hcode, tbl);
		hcode = update64l((i >>> 16), hcode, tbl);
		hcode = update64l((i >>> 24), hcode, tbl);
		return hcode;
	}

	/**
	 * Incremental 64-bit CRC calculation for a single long.
	 *
	 * <p>
	 *
	 * @param      l   The next data in the CRC calculation.
	 * @param      hcode   The current long result.
	 * @return     Updated long value of CRC calculation.
	 */
	protected static long update64(long l, long hcode, long[] tbl) {
		hcode = update64l(l, hcode, tbl);
		hcode = update64l((l >>> 8), hcode, tbl);
		hcode = update64l((l >>> 16), hcode, tbl);
		hcode = update64l((l >>> 24), hcode, tbl);
		hcode = update64l((l >>> 32), hcode, tbl);
		hcode = update64l((l >>> 40), hcode, tbl);
		hcode = update64l((l >>> 48), hcode, tbl);
		hcode = update64l((l >>> 56), hcode, tbl);
		return hcode;
	}

	/**
	 * Compute 64-bit CRC for an array of bytes.
	 *
	 * <p>
	 *
	 * @param      v   The byte array.
	 * @return     Long containing CRC hash of array.
	 */
	protected static long hash64(byte[] v) {
		long hcode = 0L;
		for(byte x : v) {
			hcode = update64(x, hcode, hash64tab1);
		}
		return hcode;
	}

	/**
	 * Compute 64-bit CRC for an array of shorts.
	 *
	 * <p>
	 *
	 * @param      v   The short array.
	 * @return     Long containing CRC hash of array.
	 */
	public static long hash64(short[] v) {
		long hcode = 0L;
		for(short x : v) {
			hcode = update64(x, hcode, hash64tab1);
		}
		return hcode;
	}

	/**
	 * Incremental 64-bit CRC for an integer.
	 *
	 * <p>
	 *
	 * @param      x   The int.
	 * @param      hcode   The hash so far.
	 * @return     Long which is the updated hcode for x.
	 */
	public static long update_hash(int x, long hcode) {
		return update64(x, hcode, hash64tab1);
	}

	/**
	 * Incremental 64-bit CRC for a long.
	 *
	 * <p>
	 *
	 * @param      x   The long.
	 * @param      hcode   The hash so far.
	 * @return     Long which is the updated hcode for x.
	 */
	public static long update_hash(long x, long hcode) {
		return update64(x, hcode, hash64tab1);
	}

	/**
	 * Compute 64-bit CRC for an array of ints.
	 *
	 * <p>
	 *
	 * @param      v   The int array.
	 * @return     Long containing CRC hash of array.
	 */
	public static long hash64(int[] v) {
		long hcode = 0L;
		for(int x : v) {
			hcode = update64(x, hcode, hash64tab1);
		}
		return hcode;
	}

	/**
	 * Compute 64-bit CRC for an array of float.
	 *
	 * <p>
	 *
	 * @param      v   The float array.
	 * @return     CRC hash of array.
	 */
	public static long hash64(float[] v) {
		long hcode = 0L;
		for(float xf : v) {
			int x = Float.floatToRawIntBits(xf);
			hcode = update64(x, hcode, hash64tab1);
		}
		return hcode;
	}

	/**
	 * Compute 64-bit CRC for an array of longs.
	 *
	 * <p>
	 *
	 * @param      v   The long array.
	 * @return     Long containing CRC hash of array.
	 */
	public static long hash64(long[] v) {
		long hcode = 0L;
		for(long x : v) {
			hcode = update64(x, hcode, hash64tab1);
		}
		return hcode;
	}

	/**
	 * Compute 64-bit CRC for an array of double.
	 *
	 * <p>
	 *
	 * @param      v   The double array.
	 * @return     CRC hash of array.
	 */
	public static long hash64(double[] v) {
		long hcode = 0L;
		for(double xd : v) {
			long x = Double.doubleToRawLongBits(xd);
			hcode = update64(x, hcode, hash64tab1);
		}
		return hcode;
	}

	/**
	 * Compute 32-bit CRC for an array of bytes.
	 *
	 * <p>
	 *
	 * @param      v   The byte array.
	 * @return     Int containing CRC hash of array.
	 */
	public static int hash32(byte[] v) {
		int hcode = 0;
		for(byte x : v) {
			hcode = update32(x, hcode, hash32tab0);
		}
		return hcode;
	}

	/**
	 * Compute 32-bit CRC for an array of shorts.
	 *
	 * <p>
	 *
	 * @param      v   The short array.
	 * @return     Int containing CRC hash of array.
	 */
	public static int hash32(short[] v) {
		int hcode = 0;
		for(short x : v) {
			hcode = update32(x, hcode, hash32tab0);
		}
		return hcode;
	}

	/**
	 * Compute 32-bit CRC for an array of ints.
	 *
	 * <p>
	 *
	 * @param      v   The int array.
	 * @return     Int containing CRC hash of array.
	 */
	public static int hash32(int[] v) {
		int hcode = 0;
		for(int x : v) {
			hcode = update32(x, hcode, hash32tab0);
		}
		return hcode;
	}

	/**
	 * Compute 32-bit CRC for an array of float.
	 *
	 * <p>
	 *
	 * @param      v   The float array.
	 * @return     CRC hash of array.
	 */
	public static int hash32(float[] v) {
		int hcode = 0;
		for(float xf : v) {
			int x = Float.floatToRawIntBits(xf);
			hcode = update32(x, hcode, hash32tab0);
		}
		return hcode;
	}

	/**
	 * Compute 32-bit CRC for an array of longs.
	 *
	 * <p>
	 *
	 * @param      v   The long array.
	 * @return     Int containing CRC hash of array.
	 */
	public static int hash32(long[] v) {
		int hcode = 0;
		for(long x : v) {
			hcode = update32(x, hcode, hash32tab0);
		}
		return hcode;
	}

	/**
	 * Compute 32-bit CRC for an array of double.
	 *
	 * <p>
	 *
	 * @param      v   The double array.
	 * @return     Int containing CRC hash of array.
	 */
	public static int hash32(double[] v) {
		int hcode = 0;
		for(double xd : v) {
			long x = Double.doubleToRawLongBits(xd);
			hcode = update32(x, hcode, hash32tab0);
		}
		return hcode;
	}

	/**
	 * Compute 32-bit CRC for a matrix of doubles. Used to compute a hash
	 * value for instances of Matrix.
	 *
	 * <p>
	 *
	 * @param      A   The double[][].
	 * @param      rows The length of A.
	 * @param      cols The length of A[0].
	 * @return     CRC hash of A.
	 */
	public static int hash32(double[][] A, int rows, int cols) {
		int hcode = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				long x = Double.doubleToRawLongBits(A[i][j]);
				hcode = update32(x, hcode, hash32tab0);
			}
		}
		return hcode;
	}

	/**
	 * Easily testable function which computes 64-bit CRC for a single long.
	 *
	 * <p> These functions are only intended as entry points for test
	 * code. They are structurally the same as a some functions in a
	 * long-used and well-tested C library, so we were easily able to
	 * generate testcases for this code from the C code.  <p>
	 *
	 * @param      x   The long to be hashed.
	 * @param      initial   The CRC value to be updated.
	 * @return     Long containing CRC hash value.
	 */
	public static long hash64(long x, long initial) {
		long hcode = initial;
		hcode = update64(x, hcode, hash64tab1);
		return hcode;
	}


	/**
	 * Easily testable function which computes 32-bit CRC for a single long.
	 *
	 *
	 * <p> These functions are only intended as entry points for test
	 * code. They are structurally the same as a some functions in a
	 * long-used and well-tested C library, so we were easily able to
	 * generate testcases for this code from the C code.  <p>
	 *
	 * @param      x   The long to be hashed.
	 * @param      initial   The CRC value to be updated.
	 * @return     Int containing CRC hash value.
	 */
	public static int hash32(long x, int initial) {
		int hcode = initial;
		hcode = update32(x, hcode, hash32tab0);
		return hcode;
	}
}
