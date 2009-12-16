package edu.stanford.math.plex;

import java.util.*;

import static edu.stanford.math.plex.WitnessStream.*;

/**
 * A <code>RipsStream</code> instance is SimplexStream whose elements are
 * the simplices of the Rips complex of a PointData instance. The
 * implementation of this class is quite similar to that of
 * LazyWitnessStream.
 *
 * @see        edu.stanford.math.plex.LazyWitnessStream
 * @see        edu.stanford.math.plex.WitnessStream
 *
 * @version $Id$
 */
public class RipsStream extends SimplexStream {

	/**
	 * The PointData underlying this Rips complex.
	 *
	 */
	public final PointData pdata;

	/**
	 * The granularity used in assigning filtration indices to simplices.
	 *
	 * <p> A simplex in a Rips complex is regarded as coming into existence
	 * when the "last" of its edges does some. Specifically, we assign a
	 * <i> persistence parameter</i> to each Simplex which is the the
	 * maximum of the persistence parameters assigned to each of the
	 * component edges. The most obvious way to do that is to use the
	 * length of an admissible edge as the persistence parameter of that
	 * edge. (Actually, since the persistence parameters must be integers,
	 * the appropriate index into a sorted array of edge lengths is
	 * used. And recall that we allow there to be a maximum length of the
	 * edges that we consider.) Since this can result in an set of
	 * persistence indices that grows quadratically in the number of
	 * points, we allow the user to control this growth by specifying a
	 * positive granularity. In this case the filtration index of an edge
	 * is the floor of the ratio of the edge length divided by the
	 * granularity. This means that we know that the filtration indices
	 * will never exceed the floor of the ratio of the max edge length
	 * divided by the granularity.
	 *
	 */
	public final double granularity;

	/**
	 * The maximum dimension of a Simplex that will be admitted to the complex.
	 */
	public final int maxDimension;

	/**
	 * The maximum length of an edge that will be admitted to the
	 * complex. Currently protected.
	 */
	public final double edge_length_limit;


	protected double[] lengths_array;
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
	 * Constructor for RipsStream.
	 *
	 * <p>
	 * Make a stream of the simplices in a Rips complex for data.
	 * <p>
	 * @param      double delta Granularity to apply to the underlying metric.
	 * @param      int max_d   Maximum dimension of Simplex instances we construct.
	 * @param      double max_length   Maximum edge we will allow.
	 * @param      PointData data   Metric data to use in constructing stream.
	 *
	 */
	RipsStream(double delta, int max_d, double max_length, PointData data) {
		assert delta >= 0.0;
		granularity = delta;
		edge_length_limit = max_length;
		pdata = data;
		maxDimension = max_d;
		stack = find_simplices(max_length, max_d, data);
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
		if (granularity > 0.0) 
			return granularity * fi;
		else 
			return lengths_array[fi];
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

	/**
	 * Make an iterator for the stream elements of fixed dimension.
	 * <p>
	 *
	 * @param      d dimension of simplices of interest.
	 * @return  Iterator<Simplex> instance for the stream.
	 *
	 * @see        java.util.Iterator
	 */
	public Iterator<Simplex> iterator(int d) {
		return stack.iterator(d);
	}

	/**
	 * Return simplices of specified dimension that compare "favorably" with
	 * a given findex.  <p>
	 *
	 * @param      dimension dimenion of interest.
	 * @param      test_index findex we are comparing against.
	 * @param      ct Type of comparison to use.
	 * @return  Simplex[] of matching simplices.
	 */
	public Simplex[] matchingSimplices(int dimension, int test_index, 
			ComparisonType ct) {
		Iterator<Simplex> iter = iterator(dimension);
		ArrayList<Simplex> results = new ArrayList<Simplex>();
		while(iter.hasNext()) {
			Simplex n = iter.next();
			boolean push_it = false;
			switch(ct) {
			case LT:
				push_it = (n.findex() < test_index);
				break;
			case LE:
				push_it = (n.findex() <= test_index);
				break;
			case EQ:
				push_it = (n.findex() == test_index);
				break;
			case GE:
				push_it = (n.findex() >= test_index);
				break;
			case GT:
				push_it = (n.findex() > test_index);
				break;
			}
			if (push_it)
				results.add(n);
		}
		// extract results, put into Simplex[]
		                                     {
		                                    	 Simplex[] return_value = 
		                                    		 new Simplex[results.size()];
		                                    	 int counter = 0;
		                                    	 for(Simplex s : results) 
		                                    		 return_value[counter++] = s;
		                                    	 // I contend we don't need to sort -- Arrays.sort(return_value);
		                                    	 return return_value;
		                                     }
	}

	/**
	 * Return simplices of specified dimension that compare "favorably" with
	 * a given length.  <p>
	 *
	 * @param      dimension dimenion of interest.
	 * @param      test_len length we are comparing against.
	 * @param      ct Type of comparison to use.
	 * @return  Simplex[] of matching simplices.
	 */
	public Simplex[] matchingSimplices(int dimension, double test_len, 
			ComparisonType ct) {
		int test_index = 0;
		if (lengths_array == null)
			// if we have no lengths_array, just make it into an findex in the
			// usual way
			test_index = (int)(Math.floor(test_len/granularity));
		else {
			// translate into findex via the lengths_array
			test_index = Arrays.binarySearch(lengths_array, test_len);
			if (test_index < 0) {
				test_index = (-test_index) - 1;
				// not present -- test_index is now the insertion point for test_len,
				// so we have to examine the ct argument
				switch(ct) {
				case LT:
				case GE:
					break;
				case LE:
					ct = ComparisonType.LT;
					break;
				case GT:
					ct = ComparisonType.GE;
					break;
				case EQ:
					return new Simplex[0];
				}
			}
		}
		return matchingSimplices(dimension, test_index, ct);
	}

	// don't use
	protected RipsStream() {
		edge_length_limit = 0.0;
		granularity = 0.0;
		maxDimension = 0;
		pdata = null;
	}

	//
	// The following package-protected static members are used by
	// LazyWitnessStream.
	//

	// Get from (i,j) to the index of the edge_info for the edge from i->j.
	static int edge_index(int i, int j, int n) {
		assert((i > 0) && (i < j) && (j <= n));
		return (((n * (n - 1)) - ((n - i + 1) * (n - i))) / 2) + (j - (i + 1));
	}

	// Test function.
	static boolean check_edge_index(int n) {
		int index_limit = (n * (n-1))/2;
		int prev_index = -1;
		// System.out.printf("\n\n");
		for (int i = 1; i <= n; i++) {
			for (int j = i+1; j <= n; j++) {
				int current_index = edge_index(i, j, n);
				// System.out.printf(" %d = edge_index(%d, %d, %d)\n", 
				// current_index, i, j, n);
				assert(current_index == (1 + prev_index));
				assert(current_index < index_limit);
				assert(current_index >= 0);
				prev_index = current_index;
			}
		}
		assert(index_limit == (1 + prev_index));
		//System.out.printf(" >>>>>> LIMIT = %d, last index = %d\n", 
		// index_limit, prev_index);
		return true;
	}

	static int all_edges_present(int[] verts, 
			int[] edge_info, int n) {
		int last_vert = verts[verts.length - 1];
		int max_findex = 0;
		for (int i = 0; i < verts.length - 1; i++) {
			int f_ij = edge_info[edge_index(verts[i], last_vert, n)];
			if (f_ij < 0)
				return -1;
			else if (f_ij > max_findex)
				max_findex = f_ij;
		}
		return max_findex;
	}

	//
	// The previous package-protected static members are used by
	// LazyWitnessStream.
	//

	// The algorithm here is quite simple. We build a SimplexStream.stack
	// and push all of the Points into it. We then make a queue for storing
	// Simplices as they are built, and note the beginning (via something
	// we call a Head). We then we make the edges that are short
	// enough, adding those to the queue. While adding the edges, we record
	// the distance information in an array (which we think of as the upper
	// half of a distance matrix). This "matrix" stores the findex for the
	// edge from i->j (i < j) or -1 if there is no edge. We then note the
	// "end of the 1-dimensional simplices" with another Head. Then,
	// at any point we have Head instances that mark the beginning and
	// end of the simplices of dimension k. We then iterate over each
	// Simplex s of dimension k, and for each p > max(s.vertices()), we
	// check to see if all the edges from p to each vertex exist. If they
	// do, then we create a new Simplex of dimension k+1 (and if k+1 isn't
	// the max dimension, we enqueue the new Simplex). This procedure means
	// that we examine only the possible combinations of vertices in each
	// dimension of interest, and we only examine a combination once
	// (because we look at sorted lists).
	protected SimplexStream.Stack find_simplices(double max_dist, 
			int max_dimension, 
			PointData data) {
		if (max_dimension < 0)
			return null;

		// Recall that "count" means that the points are indexed from 1 to N, inclusive.
		int N = data.count();
		int[] edge_info = new int[(N * (N - 1)) / 2];
		int max_findex;
		if (granularity > 0.0) {
			max_findex = (int)(Math.floor(max_dist/granularity));
		} else {
			max_findex = ((N * (N - 1)) / 2);
		}
		// Construct the tail of the queue -- we add any simplices of less
		// than max dimension to this queue, so that we can go over go over
		// them again in a later pass
		SimplexStream.Tail q = new SimplexStream.Tail();
		// Final repository for the elements of the Rips complex.
		SimplexStream.Stack stack = new SimplexStream.Stack(max_findex, max_dimension);

		// Fill in the edge_info array with -1's, which indicate "no edge".
		for (int i = 0; i < edge_info.length; i++)
			edge_info[i] = -1;

		// Add all of the points to the stack.
		for (int i = 1; i <= N; i++)
			stack.push(Simplex.makePoint(i, 0));

		// If max_dimension is 0, we just want the points.
		if (max_dimension < 1)
			return stack;

		// Remember the start of dimension k, where in this case, k=1.
		SimplexStream.Head last_dim_start = new SimplexStream.Head(q);

		// Process the edges. We both enqueue them, and create the
		// edge_info[] which we use to bootstrap from dimension k to
		// dimension k+1.
		if (granularity == 0.0) {
			double[] edge_lens = new double[(N * (N - 1)) / 2];
			int lengths_counter = 1;

			// for granularity == 0 we sort the lengths and use the index into
			// the sorted array of lengths as the filtration index
			for (int i = 1; i <= N; i++) {
				for (int j = i+1; j <= N; j++) {
					double dij = data.distance(i, j);
					if (dij <= max_dist) {
						edge_lens[edge_index(i, j, N)] = dij;
						lengths_counter++;
					} else 
						edge_lens[edge_index(i, j, N)] = HUGE;
				}
			}

			// Copy the non-HUGE lengths into the lengths_array (leaving a 0 in
			// the first entry), and sort it -- we will use the index of dij in
			// lengths_array as the persistence index for the corresponding edge
			// simplex. We don't bother looking for duplicate lengths, because
			// these will occur with probability 0 in real data.
			lengths_array = new double[lengths_counter];
			lengths_counter = 1;
			for (int i = 0; i < edge_lens.length; i++) {
				if (edge_lens[i] != HUGE)
					lengths_array[lengths_counter++] = edge_lens[i];
			}
			Arrays.sort(lengths_array);

			// Use binary search to find the index to which the dij value
			// was moved during sorting. If the performance of this
			// matters, it would be possible to write a version of the sort
			// routine that keeps a map of the permutation performed by the
			// sort, but that seems unlikely to matter here.
			for (int i = 1; i <= N; i++) {
				for (int j = i+1; j <= N; j++) {
					int index = edge_index(i, j, N);
					double dij = edge_lens[index];
					if (dij < HUGE) {
						int pindex = Arrays.binarySearch(lengths_array, dij);
						assert lengths_array[pindex] == dij;
						edge_info[index] = pindex;
						Simplex s = Simplex.makeEdge(i, j, pindex);
						stack.push(s);
						q = q.enqueue(s);
					}
				}
			}
		} else {
			for (int i = 1; i <= N; i++) {
				for (int j = i+1; j <= N; j++) {
					double dij = data.distance(i, j);
					if (dij <= max_dist) {
						int f_ij = (int)(Math.floor(dij/granularity));
						edge_info[edge_index(i, j, N)] = f_ij;
						Simplex s = Simplex.makeEdge(i, j, f_ij);
						stack.push(s);
						q = q.enqueue(s);
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
				assert(face.dimension() == (current_dimension-1));
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
				// "diameter" is the max of diam([v1,...,vk]) and the
				// lengths of the edges (v1,x),...(vk,x).
				int largest_v = verts[current_dimension-1];
				for (int new_last = largest_v + 1; new_last <= N; new_last++) {
					verts[current_dimension] = new_last;
					int f_new = all_edges_present(verts, edge_info, N);
					if (f_new >= 0) {
						Simplex newSimplex = 
							Simplex.getSimplex(verts, Math.max(face.findex(), f_new));
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
			assert((current_dimension < max_dimension) || 
					(last_dim_start.eql(new SimplexStream.Head(q))));
			current_dimension++;
		}

		return stack;
	}
}
