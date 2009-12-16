package edu.stanford.math.plex;

import java.util.*;

/**
 * A <code>WitnessStream</code> is an iterable Witness complex.
 *
 * <p>A WitnessStream is a SimplexStream whose elements are the simplices
 * of the Witness complex of a PointData instance.  That is, a simplex is
 * added to the complex only each face is present and a witness is found
 * for the entire simplex.  The details of this construction are described
 * in <i>Topological estimation using witness complexes</i>, Vin de Silva
 * and Gunnar Carlsson, Symposium on Point-Based Graphics, ETH, Zurich,
 * Switzerland, June 2-4, 2004. 
 *
 * @see        edu.stanford.math.plex.LazyWitnessStream
 *
 * @version $Id$
 */
public class WitnessStream extends SimplexStream {

	/**
	 * Randomly choose a subset of a given PointData for use as landmarks.
	 *
	 * <p>
	 * @param      data PointData from which to choose landmarks.
	 * @param      L number of landmarks to choose.
	 * @return     An int[] containing indices of the landmarks.
	 *
	 */
	public static int[] makeRandomLandmarks(PointData data, int L) {
		assert(L <= data.count());
		int N = data.count();
		// landmarks[0] is always 0
		int[] landmarks = new int[L+1];
		HashSet<Integer> used = new HashSet<Integer>(L);
		Random rand = new Random();
		for(int counter = 1; counter < landmarks.length; ){
			int possible = rand.nextInt(N) + 1;
			Integer pInteger = new Integer(possible);
			if (!used.contains(pInteger)) {
				used.add(pInteger);
				landmarks[counter++] = possible;
			}
		}
		return landmarks;
	}

	/**
	 * Estimate Rmax for a given PointData and landmark set.
	 *
	 * <p>
	 * @param      data The entire data set.
	 * @param      landmarks The landmark set.
	 * @return     The max/min of the landmarks to points distances.
	 *
	 */
	public static double estimateRmax(PointData data, int[] landmarks) {
		int L = landmarks.length - 1;
		int N = data.count();
		double Rmax = 0.0;

		for (int n = 1; n <= N; n++) {
			// for each point, find the closest landmark
			double m_n = HUGE;
			for (int l = 1; l <= L; l++) {
				m_n = min(m_n, data.distance(landmarks[l], n));
			}
			// then take the max over all points of the closest landmark
			Rmax = max(m_n, Rmax);
		}
		return Rmax;
	}

	// the pdata, landmarks and granularity can't change, but we (may)
	// allow the limit to increase, which can result in more simplices
	// being added.
	protected final PointData pdata;
	protected final double granularity;
	protected final int maxDimension;
	protected final int[] landmarks;
	protected double maxR;
	protected SimplexStream.Stack stack;

	/**
	 * Is there a next Simplex in the stream?
	 *
	 * <p>
	 * @return     Return true if the stream is not yet empty.
	 *
	 */
	public boolean hasNext() {
		return stack.hasNext();
	}

	/**
	 * Next Simplex in the stream.
	 *
	 * <p>
	 * @return     Return the smallest remaining Simplex instance.
	 *
	 */
	public Simplex next() {
		return stack.next();
	}

	/**
	 * Constructor for WitnessStream.
	 *
	 * <p>
	 * Make a stream of the simplices in a Witness complex for data.
	 * <p>
	 * @param      delta Granularity to apply to the underlying metric.
	 * @param      max_d   Maximum dimension of Simplex instances we construct.
	 * @param      R   Maximum R we will use.
	 * @param      landmarks Indices of points to use as landmarks. The
	 *                        simplices constructed will use the landmark
	 *                        indices, rather than the indices into data.
	 * @param      PointData data   Metric data to use in constructing stream.
	 *
	 */
	WitnessStream(double delta, int max_d, double R, 
			int[] landmarks, PointData data) {
		assert delta > 0.0;
		granularity = delta;
		this.landmarks = landmarks;
		maxR = R;
		pdata = data;
		maxDimension = max_d;
		stack = find_simplices(delta, R, max_d, landmarks, data);
	}

	/**
	 * How many simplices are in the stream?
	 *
	 * <p>
	 * @return     The number of simplices in the stream.
	 *
	 */
	public int size() {
		return stack.size();
	}

	/**
	 * Max dimension of simplices in the stream.
	 *
	 * <p>
	 * @return     Max dimension of simplices in the stream.
	 *
	 */
	public int maxDimension() {
		return stack.maxDimension();
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
		return granularity * fi;
	}

	/**
	 * Make an iterator for the stream.
	 * <p>
	 *
	 * @return  Iterator<Simplex> instance for the stream.
	 *
	 * @see        java.util.Iterator
	 */
	public Iterator<Simplex> iterator() {
		return stack.iterator();
	}

	// don't use
	protected WitnessStream() {
		granularity = 0.0;
		maxDimension = 0;
		landmarks = null;
		pdata = null;
	}


	// Implementation note: In the code below, L is the number of landmarks
	// and N is the number of data points. To make the code match the
	// papers a bit more, we have the indices go from 1 to L (or N),
	// instead of from 0 to L-1 (or N-1), which would be the normal way in
	// C (and hence C++ and Java). So the 0-th entry of things in nothing,
	// and when we make arrays that get indexed directly, they are 1
	// longer. When we index matrices we create a 1-array and then have an
	// indexing function that adjusts the arguments (that is, we can make
	// the 1-array exactly the right length). Since Java should inline
	// these indexing functions, and the compiler can't deal with 2-arrays
	// of fixed size (as it would in C or C++), I chose to implement
	// matrices this way.
	//
	// When there is at most one of each, I use l as the index over
	// landmarks and n as the index over data. (I have found it hard to
	// tell l from 1 in the code a couple of times while writing it, so
	// don't feel bad if you make the same mistake reading it.)


	// Get from (n,k) to the index of m(n, k). See the paper referenced
	// above for meaning of m[]. The k index is an ordinal, not any of the
	// l/n/d values that we've mentioned up to now.
	protected static int m_index(int n, int k, int N) {
		assert((k > 0) && (1 <= n) && (n <= N));
		return ((k-1) * N) + (n-1);
	}

	//
	// The following package-protected static members are used by
	// LazyWitnessStream.
	//

	// Compute distance matrix entry for given landmark/data_point.
	static double distance_ln(int l, int n, int[] landmarks, PointData data) {
		if (landmarks[l] == n)
			// exclude the landmark from consideration
			return HUGE;
		else
			return data.distance(landmarks[l], n);
	}

	static final double HUGE = Double.MAX_VALUE/2; 

	// D[] index for the distance from landmark l to data point n.
	static int D_index(int l, int n, int L, int N) {
		assert((l > 0) && (l <= L) && (n > 0) && (n <= N));
		return ((l-1) * N) + (n-1);
	}


	static double min(double x, double y) {
		if (x < y)
			return x;
		else
			return y;
	}

	static double max(double x, double y) {
		if (x > y)
			return x;
		else
			return y;
	}

	static int max(int x, int y) {
		if (x > y)
			return x;
		else
			return y;
	}

	//
	// The preceding package-protected static methods are used by
	// LazyWitnessStream.
	//


	// The algorithm here is a bit different than the the one for Rips
	// complexes, but it is still an iterative process in increasing
	// dimension of the simplices being constructed. A (non-lazy) Witness
	// complex contains a simplex s iff the complex contains each of the
	// faces of s, and there is a witness for s itself. The idea is to
	// start off adding the landmarks, and building the distance matrix, as
	// we do in LazyWitnessStream. However, after that we construct an
	// array m[][] of double[max_dimension] of the 1st, 2nd,
	// ... max_dimension-th smallest distances for each of the 1<=n<=N pts
	// in data.  Then, we construct the edge set just as for
	// LazyWitnessStream with nu=2. Now, at each step for dim = d, we have
	// the d-1 dimensional simplices, each with there filtration indices
	// set, stored in the queue. We then create an intering table for this
	// set of d-1 dimensional "faces", and iterate over the d-dimensional
	// simplices just as in RipsStream or LazyWitnessStream. However, the
	// first step in processing a d-dimensional simplex is to compute its
	// boundary, and confirm that each face is in the interning table. If
	// this succeeds we will return the maximum filtration index of all of
	// the faces. If it fails, we go onto the next d-dimensional simplex,
	// but if it succeeds, we must find the 1<=n<=N with the smallest
	// max(dist(i, p1), ... dist(i, pd)). We take this value, subtract from
	// it the entry in m[k-1][i] (the k-th smallest distance, indexed from
	// 0 to max_dimension-1). If this difference Rs is <= R_max, we accept
	// the simplex, and the filtration index is the maximum of the maximum
	// of the faces and floor(max(Rs, 0.0)/delta).  Note that this
	// iteration is more expensive both because it is necessary to
	// construct m[][], but also because the last step of acceptance
	// requires O(k*N) operations. I see no way of avoiding this.
	protected SimplexStream.Stack 
	find_simplices(double delta, double R_max, int max_dimension, 
			int[] landmarks, PointData data) {

		// Recall that we never use 0 as a simplex index, so landmarks[0]
		// should be empty
		assert(landmarks[0] == 0);
		// there is no point doing this unless 1 < L <= N and max_dimension <= L.
		assert((1 < (landmarks.length-1)) && 
				(max_dimension <= (landmarks.length-1)) && 
				((landmarks.length-1) < data.count()));

		if (max_dimension < 0)
			return null;

		// The landmarks are indexed from 1 to L, inclusive, and since 0 is excluded,
		// L = landmarks.length-1.
		int L = landmarks.length - 1;
		int max_findex = (int)(Math.floor(R_max/granularity));
		// Construct the tail of the queue -- we add any simplices of
		// dimension d < max_dimension to this queue, so that we can
		// iterate over them them in constructing the simplices of
		// dimension d+1.
		SimplexStream.Tail q = new SimplexStream.Tail();
		// Final repository for the elements of the Witness complex.
		SimplexStream.Stack stack = 
			new SimplexStream.Stack(max_findex, max_dimension);
		// Recall that "count" means that the points are indexed from 1 to
		// N, inclusive.
		int N = data.count();
		// we can think of this as 2-dimensional, but it's (supposed to me)
		// simpler and faster to convert the two indices to a single one
		// via an index function, so that's what we'll do
		double[] m = new double[N * (max_dimension+1)];
		double[] D = new double[L * N];

		// fill in the distance matrix
		for (int l = 1; l <= L; l++)
			for (int n = 1; n <= N; n++)
				D[D_index(l, n, L, N)] = distance_ln(l, n, landmarks, data);


		// Add all of the landmark Points (0-simplices) to the stack.
		for (int l = 1; l <= L; l++)
			stack.push(Simplex.makePoint(l, 0));

		// If max_dimension is 0, we just want the Points.
		if (max_dimension < 1)
			return stack;

		// Remember the start of dimension k, where in this case, k=1.
		SimplexStream.Head last_dim_start = new SimplexStream.Head(q);
		// Remember how many d-1 dimensional simplices are being
		// made. These are the faces of the d dimensional simplices, and we
		// want to intern all possible faces to make checking faster. See
		// below for more details.
		int faces_to_intern = 0;

		// construct the metric info m[] and process the edges
		{
			// Now we make m[] -- we want the smallest max_dimension
			// entries from each column of D.
			{
				double[] m_tmp = new double[L+1];

				for (int n = 1; n <= N; n++) {
					for (int l = 1; l <= L; l++) 
						m_tmp[l] = D[D_index(l, n, L, N)];
					// sort the initial contents of m_tmp -- it might be
					// faster to do this by hand, but it is a tiny amount
					// of the time spent in this function, and when I tried
					// once to do it by hand, I messed it up
					Arrays.sort(m_tmp);
					assert(m_tmp[0] == 0.0);

					// copy the contents of m_tmp into m
					for(int d = 1; d <= max_dimension+1; d++) {
						assert(m_tmp[d] < HUGE/2.0);
						m[m_index(n, d, N)] = m_tmp[d];
					}
				}
			}

			// Process the edges. Unlike RipsStream and LazyWitnessStream,
			// there is no special preprocessing going on at this step. The
			// only special thing about this case is that we know that ALL
			// of the faces of the edges (that is, all the vertices) are in
			// the complex, so we can skip the "intern the k-1 dimensional
			// simplices and then see if they exist" step here. But other
			// than that, the process of admitting an edge is the same as
			// the higher dimensional cases. We keep it here because we
			// need to start the dimensional iteration someplace, and
			// because it emphasizes the similarities between building this
			// type of complex and the lazy version. Here i and j are the
			// landmark indices -- this seemed better than using l1 and l2.
			for (int i = 1; i <= L; i++) {
				for (int j = i+1; j <= L; j++) {
					// compute the R at which the edge comes into existence
					// without the m[] adjustment
					double e_ij = HUGE;
					assert(e_ij >= 0.0);
					for (int n = 1; n <= N; n++) {
						double tmp_ijn = 
							max(D[D_index(i, n, L, N)], D[D_index(j, n, L, N)]) - 
							m[m_index(n, 2, N)];
						assert(tmp_ijn >= 0.0);
						e_ij = min(e_ij, tmp_ijn);
					}
					// if the edge appears early enough, create it, and
					// then push and enqueue it
					if (e_ij <= R_max) {
						int f_ij = (int)(Math.floor(e_ij/granularity));
						Simplex s = Simplex.makeEdge(i, j, f_ij);
						stack.push(s);
						q = q.enqueue(s);
						// count the edges -- these will be all possible
						// faces for the triangles, and we will be able to
						// immediately exclude any triangle whose boundary
						// has an edge not in this set
						faces_to_intern++;
					}
				}
			}
		}

		// We are ready to construct the higher dimensional data. Since we
		// already have the edges, we begin with 2-dimensional simplexes,
		// and continue until we have constructed those of max_dimension.
		int current_dimension = 2;
		while (current_dimension <= max_dimension) {
			// we start processing at last_dim_start
			SimplexStream.Head current = last_dim_start;
			SimplexStream.Head interning_current = current.copy();
			// At this point all simplices of current_dimension-1 have been
			// added to the queue (and none of current_dimension), so we
			// stop processing for this dimension when current reaches end.
			SimplexStream.Head end = new SimplexStream.Head(q);
			// Remember end as last_dim_start, for the next iteration
			last_dim_start = end;
			// We construct a scratch array in which to store any potential
			// vertex sets for possible simplices of dimension
			// current_dimension.
			int[] verts = new int[current_dimension + 1];
			// Make a table of all of the current_dimension-1 faces -- for
			// any constructed current_dimension simplex, we can quickly
			// check that all of its faces are present by taking the
			// boundary and verifying that each face is in the table.
			SimplexTable faces = new SimplexTable(faces_to_intern);
			// to avoid repeatedly looking up distances for the faces vertices,
			// we make an array in which to cache these values
			double[] face_R = new double[N+1];
			// indicate whether this has been initialize for this face
			boolean face_R_initialized = false;

			// reset the counter
			faces_to_intern = 0;

			while (interning_current.lessThan(end)) {
				// Until interning_current reaches end, extract simplices
				// and put them in the faces table.
				Simplex face = interning_current.nextEntry();
				faces.put(face);
			}

			while (current.lessThan(end)) {
				// Until current reaches end, extract simplices and copy
				// their vertices into the scratch array.
				Simplex face = current.nextEntry();
				assert(face.dimension() == (current_dimension-1));
				face.vertices(verts);

				face_R_initialized = false;

				// We know that any d dimensional simplex that we can admit
				// must have all of its faces in the list of d-1 simplices
				// (a simplex cannot belong to a complex unless all of its
				// faces belong, too). That means that any possible d
				// simplex [x1,...,x(d+1)] must have the face [x1,...,xd]
				// in the list. So we see that we can get to any possible d
				// simplex by adding a vertex "onto the end" (that is, the
				// new vertex is larger than any of the existing ones) of a
				// d-1 simplex. Further, it is easy to see that adding onto
				// the end of 2 distinct d-1 simplices can never yield the
				// same d simplex. So if we just add onto the end of the
				// simplices in the previous dimension, we will see all
				// potential d simplices precisely once. So if verts is
				// [x1,...,xd, <junk>], then we are interested in any
				// possible simplex [x1,...,xd,x] with x > xd. In this case
				// we must first take [x1,...,xd,x].boundary() and see if
				// all of the entries are in the faces table (although, of
				// course, we could ignore the first entry). Then if all
				// faces are present, we have to search for the smallest R
				// for which a witness exists for the the simplex. If that
				// R is less than R_max, then the simplex is added, and
				// it's filtration index is the min of the filtration
				// indices of the faces and floor(R/granularity).
				int largest_v = verts[current_dimension-1];
				for (int new_last = largest_v + 1; new_last <= L; new_last++) {
					verts[current_dimension] = new_last;
					Simplex possible = Simplex.getSimplexPresorted(verts);
					Simplex[] bdy = possible.boundaryArray();
					int max_face_fi = 0;
					for(int fi = 0; fi < bdy.length; fi++) {
						Simplex interned_face = faces.get(bdy[fi]);
						if (interned_face != null)
							max_face_fi = max(max_face_fi, interned_face.findex());
						else {
							max_face_fi = -1;
							break;
						}
					}
					if (max_face_fi >= 0) {
						double possible_R = HUGE;
						// Look for a witness for "possible" -- this part
						// of the code is where a large majority of the time is
						// spent. Is there some better way to do this?

						if (!face_R_initialized) {
							// this hack seems to save about 30% of the time
							// initialize face_R for the new face
							for (int n = 1; n <= N; n++) {
								double R_n = 0.0;
								for(int vi = 0; vi < current_dimension; vi++)
									R_n = max(R_n, D[D_index(verts[vi], n, L, N)]);
								face_R[n] = R_n;
							}
							face_R_initialized = true;
						}

						for (int n = 1; n <= N; n++) {
							double R_n =
								max(face_R[n], 
										D[D_index(verts[current_dimension], n, L, N)]);
							R_n -= m[m_index(n, current_dimension+1, N)];
							assert(R_n >= 0.0);
							possible_R = min(possible_R, R_n);
						}
						if (possible_R <= R_max) {
							possible.setfindex(max(max_face_fi, 
									(int)Math.floor(possible_R/delta)));
							stack.push(possible);
							if (current_dimension < max_dimension) {
								// if the dimension of possible isn't
								// maximal, then we put it in the queue and
								// count it for interning the next time
								q = q.enqueue(possible);
								faces_to_intern++;
							}
						}
					}
				}
			}
			// Make sure that if we are exiting we have processed all
			// simplices in q.
			assert((current_dimension < max_dimension) || 
					(last_dim_start.eql(new SimplexStream.Head(q))));
			current_dimension++;
		}

		return stack;
	}
}

