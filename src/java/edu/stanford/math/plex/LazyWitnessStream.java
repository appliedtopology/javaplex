package edu.stanford.math.plex;

import static edu.stanford.math.plex.RipsStream.all_edges_present;
import static edu.stanford.math.plex.RipsStream.edge_index;
import static edu.stanford.math.plex.WitnessStream.D_index;
import static edu.stanford.math.plex.WitnessStream.HUGE;
import static edu.stanford.math.plex.WitnessStream.distance_ln;
import static edu.stanford.math.plex.WitnessStream.max;
import static edu.stanford.math.plex.WitnessStream.min;

import java.util.Arrays;
import java.util.Iterator;

/**
 * <p>
 * A LazyWitnessStream is a SimplexStream whose elements are the simplices of
 * the lazy Witness complex of a PointData instance. The implementation is very
 * similar to the RipsStream, and in fact the results are analogous for nu=0.
 * The details of this construction are described in <i>Topological estimation
 * using witness complexes</i>, Vin de Silva and Gunnar Carlsson, Symposium on
 * Point-Based Graphics, ETH, Zurich, Switzerland, June 2-4, 2004.
 * 
 * @see edu.stanford.math.plex.RipsStream
 * @see edu.stanford.math.plex.WitnessStream
 * 
 * 
 * @version $Id$
 */
public class LazyWitnessStream extends SimplexStream {

	// the pdata, landmarks, granularity and nu can't change, but we (may)
	// allow the limit to increase, which can result in more simplices
	// being added.
	public final PointData pdata;
	public final double granularity;
	public final int maxDimension;
	public final int[] landmarks;
	public final int nu;
	protected double maxR;
	protected double[] finite_eij_array;
	protected SimplexStream.Stack stack;

	/**
	 * Is there a next Simplex in the stream?
	 * 
	 * <p>
	 * 
	 * @return Return true if the stream is not yet empty.
	 * 
	 */
	public boolean hasNext() {
		return stack.hasNext();
	}

	/**
	 * Next Simplex in the stream.
	 * 
	 * <p>
	 * 
	 * @return Return the smallest remaining Simplex instance.
	 * 
	 */
	public Simplex next() {
		return stack.next();
	}

	/**
	 * Constructor for LazyWitnessStream.
	 * 
	 * <p>
	 * Make a stream of the simplices in a lazy Witness complex for data.
	 * <p>
	 * 
	 * @param delta
	 *            Granularity to apply to the underlying metric.
	 * @param max_d
	 *            Maximum dimension of Simplex instances we construct.
	 * @param R
	 *            Maximum R we will use.
	 * @param nu
	 *            See <i>Topological estimation using witness complexes</i>,
	 *            above.
	 * @param landmarks
	 *            Indices of points to use as landmarks. The simplices
	 *            constructed will use the landmark indices, rather than the
	 *            indices into data.
	 * @param data
	 *            Metric data to use in constructing stream.
	 * 
	 */
	LazyWitnessStream(double delta, int max_d, double R, int nu, int[] landmarks, PointData data) {
		assert delta >= 0.0;

		granularity = delta;
		this.landmarks = landmarks;
		this.nu = nu;
		maxR = R;
		pdata = data;
		maxDimension = max_d;
		stack = find_simplices(delta, R, nu, max_d, landmarks, data);
	}

	/**
	 * How many simplices are in the stream?
	 * 
	 * <p>
	 * 
	 * @return The number of simplices in the stream.
	 * 
	 */
	public int size() {
		return stack.size();
	}

	/**
	 * Max dimension of simplices in the stream.
	 * 
	 * <p>
	 * 
	 * @return Max dimension of simplices in the stream.
	 * 
	 */
	public int maxDimension() {
		return stack.maxDimension();
	}

	/**
	 * Convert a filtration index into a persistence parameter (i.e., double) --
	 * gets overloaded by some subclasses.
	 * 
	 * <p>
	 * 
	 * @param fi
	 *            Filtration index to convert.
	 * @return double persistence parameter.
	 * 
	 */
	public double convert_filtration_index(int fi) {
		if (granularity > 0.0)
			return granularity * fi;
		else
			return finite_eij_array[fi];
	}

	/**
	 * Make an iterator for the stream.
	 * <p>
	 * 
	 * @return Iterator<Simplex> instance for the stream.
	 * 
	 * @see java.util.Iterator
	 */
	public Iterator<Simplex> iterator() {
		return stack.iterator();
	}

	// don't use
	protected LazyWitnessStream() {
		granularity = 0.0;
		maxDimension = 0;
		landmarks = null;
		pdata = null;
		nu = 0;
	}

	// The algorithm here is essentially the same as the one for Rips
	// complexes, as it would be for constructing any complex where a
	// simplex is part of the complex if and only if all of its edges are,
	// and where the filtration index of a simplex is the max of the
	// filtration indices of all of the component edges. The difference
	// between this algorithm and the RipsStream one is how the edge_info[]
	// is initialized.
	protected SimplexStream.Stack find_simplices(double delta, double R_max, int nu, int max_dimension, int[] landmarks, PointData data) {

		// Recall that we never use 0 as a simplex index, so landmarks[0] should
		// be empty
		assert (landmarks[0] == 0);
		// there is no point doing this unless 1 < L <= N.
		assert ((landmarks.length > 2) && (data.count() >= (landmarks.length - 1)));
		// nu == 0, 1, or 2 in practice, but 0 <= nu <= L is all that we require
		assert ((nu >= 0) && (nu <= (landmarks.length - 1)));

		if (max_dimension < 0)
			return null;

		// The landmarks are indexed from 1 to L, inclusive, and since 0 is
		// excluded,
		// L = landmarks.length-1.
		int L = landmarks.length - 1;
		int[] edge_info = new int[(L * (L - 1)) / 2];
		int max_findex;
		if (granularity > 0.0)
			max_findex = (int) (Math.floor(R_max / granularity));
		else
			max_findex = (L * (L - 1)) / 2;

		// Construct the tail of the queue -- we add any simplices of less
		// than max dimension to this queue, so that we can go over go over
		// them again in a later pass
		SimplexStream.Tail q = new SimplexStream.Tail();
		// repository for the elements of the LazyWitness complex.
		SimplexStream.Stack stack = new SimplexStream.Stack(max_findex, max_dimension);

		// Fill in the edge_info array with -1's, which indicate "no edge".
		for (int i = 0; i < edge_info.length; i++)
			edge_info[i] = -1;

		// Add all of the points to the stack.
		for (int l = 1; l <= L; l++)
			stack.push(Simplex.makePoint(l, 0));

		// If max_dimension is 0, we just want the points.
		if (max_dimension < 1)
			return stack;

		// Remember the start of dimension k, where in this case, k=1.
		SimplexStream.Head last_dim_start = new SimplexStream.Head(q);

		// Construct the edge_info[] -- the only iteration over the full
		// data set occurs within this block, and we use the variable n for
		// that. The other indexing that occurs is over the landmarks set.
		// We try to create a cached D matrix of distances, but if we don't
		// have enough memory for that, we recalculate the distances as
		// needed.
		{
			// Recall that "count" means that the points are indexed from 1 to
			// N, inclusive.
			int N = data.count();
			double[] m = new double[N + 1];
			double[] D = null;
			try {
				if ((L * N) > 0) {
					// fill in the distance matrix, if we can
					D = new double[L * N];
					for (int l = 1; l <= L; l++)
					{
						for (int n = 1; n <= N; n++)
						{
							D[D_index(l, n, L, N)] = distance_ln(l, n, landmarks, data);
						}
					}
				}
			} catch (OutOfMemoryError oom) {
			} finally {
			}

			if (nu > 0) {
				double[] m_tmp = new double[L + 1];
				for (int n = 1; n <= N; n++) {
					for (int l = 1; l <= L; l++)
						if (D == null)
							m_tmp[l] = distance_ln(l, n, landmarks, data);
						else
							m_tmp[l] = D[D_index(l, n, L, N)];
					Arrays.sort(m_tmp);
					assert (m_tmp[0] == 0.0);
					m[n] = m_tmp[nu];
					assert (m[n] > 0.0);
				}
			}

			// Process the edges. We both enqueue them, and create the
			// edge_info[] which we use to bootstrap from dimension k to
			// dimension k+1. What happens in this section of the code is the
			// essential difference between a Rips complex and a LazyWitness
			// complex.
			{
				double[] i_cache = (D == null) ? null : new double[N + 1];
				double[] eij_array = new double[(L * (L - 1)) / 2];
				int finite_eij_counter = 1;

				for (int i = 1; i <= L; i++) {
					if (D == null) {
						for (int n = 1; n <= N; n++)
							i_cache[n] = distance_ln(i, n, landmarks, data);
					}
					for (int j = i + 1; j <= L; j++) {
						// Compute the R at which the edge comes into
						// existence. We use the m[] values to adjust the
						// distance_ln() values.
						double e_ij = HUGE;
						for (int n = 1; n <= N; n++) {
							double d_ijn;
							if (D == null)
								d_ijn = max(i_cache[n], distance_ln(j, n, landmarks, data));
							else
								d_ijn = max(D[D_index(i, n, L, N)], D[D_index(j, n, L, N)]);
							if (d_ijn < m[n])
								d_ijn = 0.0;
							else
								d_ijn = d_ijn - m[n];
							e_ij = min(e_ij, d_ijn);
						}

						if (e_ij <= R_max) {
							finite_eij_counter++;
							eij_array[edge_index(i, j, L)] = e_ij;
						} else
							eij_array[edge_index(i, j, L)] = HUGE;
					}
				}

				// If the granularity is 0, then instead of making the
				// filtration
				// indices be proportional to the e_ij, we make an array of the
				// e_ij, sort it, and use the index into this sorted array as
				// the
				// filtration index for the corresponding edge.
				if (granularity == 0.0) {
					// Copy the non-HUGE lengths into the finite_eij_array
					// (leaving a 0
					// in the first entry), and sort it -- we will use the index
					// of e_ij
					// in finite_eij_array as the persistence index for the
					// corresponding
					// edge simplex. We don't bother looking for duplicate
					// lengths,
					// because these will occur with probability 0 in real data.
					finite_eij_array = new double[finite_eij_counter];
					finite_eij_counter = 1;
					for (int i = 0; i < eij_array.length; i++) {
						if (eij_array[i] != HUGE)
							finite_eij_array[finite_eij_counter++] = eij_array[i];
					}
					Arrays.sort(finite_eij_array);
				}

				// Use either division or binary search to find filtration index
				// for the edge from i to j.
				for (int i = 1; i <= L; i++) {
					for (int j = i + 1; j <= L; j++) {
						double e_ij = eij_array[edge_index(i, j, L)];
						// if the edge appears early enough, create it, and
						// then push and enqueue it
						if (e_ij < HUGE) {
							int f_ij = ((granularity > 0.0) ? ((int) (Math.floor(e_ij / granularity))) : Arrays.binarySearch(finite_eij_array, e_ij));
							edge_info[edge_index(i, j, L)] = f_ij;
							Simplex s = Simplex.makeEdge(i, j, f_ij);
							stack.push(s);
							q = q.enqueue(s);
						}
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

			while (current.lessThan(end)) {
				// Until current reaches end, extract simplices and copy
				// their vertices into the scratch array.
				Simplex face = current.nextEntry();
				assert (face.dimension() == (current_dimension - 1));
				face.vertices(verts);
				// We know that any k dimensional simplex that we can admit
				// must have all of its faces in the list of k-1 simplices
				// (a simplex cannot belong to a complex unless all of its
				// faces belong, too). That means that any possible k
				// simplex [x1,...,x(k+1)] must have the face [x1,...,xk]
				// in the list. So we see that we can get to any possible k
				// simplex by adding a vertex "onto the end" (that is, the
				// new vertex is larger than any of the existing ones) of a
				// k-1 simplex. Further, it is easy to see that adding onto
				// the end of 2 distinct k-1 simplices can never yield the
				// same k simplex. So if we just add onto the end of the
				// simplices in the previous dimension, we will see all
				// potential k simplices precisely once. So if verts is
				// [v1,...,vk, <junk>], then we are interested in any
				// possible simplex [v1,...,vk,x] with x > vk. In this case
				// we merely need to check if (v1, x),...,(v1, k) are
				// edges. If so, [v1,...,vk,x] is a simplex, and its
				// filtration index is the max of ([v1,...,vk]).findex()
				// and filtration indices of the edges (v1,x),...(vk,x).
				int largest_v = verts[current_dimension - 1];
				for (int new_last = largest_v + 1; new_last <= L; new_last++) {
					verts[current_dimension] = new_last;
					int f_new = all_edges_present(verts, edge_info, L);
					if (f_new >= 0) {
						Simplex newSimplex = Simplex.getSimplexPresorted(verts, Math.max(face.findex(), f_new));
						stack.push(newSimplex);
						if (current_dimension < max_dimension)
							// if the dimension of newSimplex isn't maximal,
							// then we put it in the queue
							q = q.enqueue(newSimplex);
					}
				}
			}
			// Make sure that if we are exiting we have processed all
			// simplices in q.
			assert ((current_dimension < max_dimension) || (last_dim_start.eql(new SimplexStream.Head(q))));
			current_dimension++;
		}

		return stack;
	}
}
