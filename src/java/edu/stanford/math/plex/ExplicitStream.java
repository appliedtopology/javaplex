package edu.stanford.math.plex;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A <code>ExplicitStream</code> instance is SimplexStream whose elements
 * are given explicitly, along with associated values of a persistence
 * parameter. Instances of ExplicitStream can be edited after creation,
 * unlike other SimplexStream subclasses, and an expected use is to create
 * empty ExplicitStreams and add simplices (and associated persistence
 * parameters) to the stream. Instances of this class exist in two states,
 * open (meaning open for additions) and closed. A stream that is closed
 * can be reopened at any time, but streams that are open cannot be used as
 * arguments to the methods in the Persistence class, iterated over, or
 * have their contents dumped for examination. Further, iterators or dump
 * state created when an ExplicitStream is closed become invalid (and
 * useless) once a stream is reopened. There is no explicit way to open one
 * of these streams -- it happens implicitly when an empty stream is
 * created, and when the stream is edited by adding/removing simplices. You
 * can create an iterator on an open stream, but you can't use the iterator
 * until the stream is closed. Dumping the contents of a stream will close
 * it, as will calling the <code>close()</code> method. There is a
 * verification process which must succeed in order for a close to happen
 * -- if the stream is in an inconsistent state (e.g., missing faces or the
 * persistence parameters of a face exceed that of a simplex), an
 * IllegalStateException will be thrown. It should be possible to catch
 * this exception, fix the problem, and proceed, but it might not work in
 * every case. There is also a <code>verify()</code> method which will
 * safely tell if a close() would succeed, and a verbose version that will
 * print messages about all of the errors found.
 *
 * @see        edu.stanford.math.plex.RipsStream
 *
 * @version $Id$
 */
public class ExplicitStream extends SimplexStream {

	// Store the simplices entries by dimension -- recall that the
	// Persistence algorithm only requires that the stream return the
	// simplices in persistence order within each dimension, and that
	// all of the faces of a simplex are seen before the simplex is
	// seen. This means that we can sort the simplices either by
	// dimension first, or by findex first. The SimplexStream.Stack,
	// used by the "industrial strength" streams in Plex, sorts first
	// by findex, and then dimension. However, because we want to be
	// able to return the contents of these streams as matrices to
	// Matlab and R, it is more convenient to keep the simplices
	// grouped by dimension, so that all of the simplices of a given
	// dimension can be returned as single matrix with dimension+1
	// columns.
	protected Simplex[][] simplices;
	// map between persistence parameters and findices.
	protected double[] persistence_parameters;

	protected PlexMessageWindow message_window;

	// False if the stream is open for the addition/removal of
	// simplices. Closing the stream results in updating the simplices and
	// lengths arrays, and confirming that the set of simplices does form a
	// simplicial complex with consistent parameters.
	protected boolean closed;

	// Used to iterate over simplices if the stream is closed -- get reset
	// every time it is open.
	protected int current_dimension;
	protected int current_index;
	// Increment every time this is opened. Iterators for this class can only
	// work when their cached open_count is == to the stream's open_count.
	protected int open_count;

	// The state below is used whenever the stream is open for additions, and
	// gets flushed at the point that the stream is closed.

	// Used to map simplices being added to stream to their persistence
	// parameters. The contents of this table are always new Simplex
	// instances with unset _findex slots.
	protected HashMap<Simplex,Double> simplex_table;
	// Any parameter seen goes in this table. When we are done the contents
	// are used to fill in the persistence_parameters array, which is then
	// sorted, and indices from there are used to fill in the simplices in
	// the simplex_table, which are in turn stored, by dimension and then
	// sorted by findex within the dimensions, in the simplices array.
	protected HashSet<Double> parameters_table;


	/**
	 * A DComplex (for Dimensional Complex) is the set of simplices (and
	 * associated persistence parameters) of specified dimension for an
	 * ExplicitStream. The slots are accessed only by read methods. It is
	 * expected that this will be used primarily to connect Plex with Matlab.
	 *
	 */
	public static class DComplex {
		protected ExplicitStream parent;
		protected int dimension;
		protected int open_count;
		protected int[][] C;
		protected double[] F;

		/**
		 * Constructor for DComplex;
		 *
		 * <p>
		 * @param      parent The ExplicitStream from which the simplices came.
		 * @param      dimension The dimension of the simplices.
		 * @param      C The actual array of vertex arrays (simplices).
		 * @param      F The array of associated persistence parameters.
		 */
		public DComplex(ExplicitStream parent, int dimension, int[][] C, double[] F) {
			this.parent = parent;
			this.open_count = parent.open_count;
			this.dimension = dimension;
			this.C = C;
			this.F = F;
		}

		// Are the parent stream and the iterator consistent?
		protected boolean parent_is_consistent() {
			return (parent.closed && (open_count == parent.open_count));
		}

		/**
		 * Is there a larger dimensional DComplex for this stream?
		 * <p>
		 * @return true if the parent is consistent with this DComplex
		 * and there is are larger dimensional simplices in the parent, else
		 * false.
		 *
		 */
		public boolean hasNext() {
			if (!parent_is_consistent())
				return false;
			else 
				return (dimension < parent.maxDimension());
		}

		/**
		 * Return a DComplex of the next largest dimension, if there is one,
		 * else null.
		 *
		 * <p>
		 * @return A DComplex of dimension 1 more, if there is one, else null.
		 */
		public DComplex next() {
			if (!parent_is_consistent())
				return null;
			else 
				return parent.dump(dimension+1);
		}

		/**
		 * Return the parent ExplicitStream of this DComplex.
		 *
		 * <p>
		 * @return     The parent ExplicitStream.
		 *
		 */
		public ExplicitStream parent() {
			return parent;
		}
		/**
		 * Return the dimension of this DComplex.
		 *
		 * <p>
		 * @return     The dimension of this DComplex.
		 *
		 */
		public int dimension() {
			return dimension;
		}
		/**
		 * Return the array of vertex arrays for the simplices of this dimension.
		 *
		 * <p>
		 * @return Return the array describing the part of the complex having
		 * the current dimension.
		 *
		 */
		public int[][] C() {
			return C;
		}
		/**
		 * Return the array of persistence parameters corresponding to the
		 * simplices of this dimension.
		 *
		 * <p>
		 * @return     A double[] of the persistence parameters for this dimension.
		 *
		 */
		public double[] F() {
			return F;
		}
	}


	/**
	 * Return a DComplex of the requested dimension, if there is one, else null;
	 *
	 * <p>
	 * @param      dim The dimension of the simplices we want.
	 * @return     Return a DComplex if dim >= 0 and <= maxDimension(), else null.
	 *
	 */
	public DComplex dump(int dim) {
		if (!closed)
			close();
		if ((dim < 0) || (dim > maxDimension()))
			return null;
		else {
			int[][] cvec = new int[simplices[dim].length][];
			double[] fvec = new double[cvec.length];
			for (int i = 0; i < cvec.length; i++) {
				Simplex s = simplices[dim][i];
				cvec[i] = s.vertices();
				fvec[i] = persistence_parameters[s.findex()];
			}
			return new DComplex(this, dim, cvec, fvec);
		}
	}

	/**
	 * Is there a next Simplex in the stream?
	 *
	 * <p>
	 * @return     Return true if the stream is not yet empty.
	 *
	 */
	public boolean hasNext() {
		// NOTE: For this to work it must be the case that any CLOSED
		// ExplicitStream has the property that the Simplex[] for the highest
		// dimension has positive length. That is, if the largest dimensional
		// Simplex in the stream has dimension 3, then simplices.length must
		// be 4. It is, of course, possible that simplices[i] has 0 length
		// for some i, just not for i == simplices.length-1.
		if (!closed)
			return false;
		else 
			return ((current_dimension < (simplices.length-1)) ||
					(current_dimension == (simplices.length-1)) &&
					(current_index < simplices[simplices.length-1].length));
	}

	// Go to the "next" index pair. This code assumes that there is such a
	// pair, and so needs to be called only when we know that this is the
	// case.
	protected void skip_to_next_index_pair() {
		current_index++;
		while((current_dimension < simplices.length) &&
				(current_index >= simplices[current_dimension].length)) {
			current_index = 0;
			current_dimension++;
		}
	}

	/**
	 * Next Simplex in the stream.
	 *
	 * <p>
	 * @return     Return the smallest remaining Simplex instance.
	 *
	 */
	public Simplex next() {
		if (!hasNext())
			return null;
		Simplex next = simplices[current_dimension][current_index];
		skip_to_next_index_pair();
		return next;
	}

	// Do the obvious conversions in case we need to return double[].
	public static double[] toDouble(int[] in) {
		if (in == null)
			return null;
		double[] return_value = new double[in.length];
		for (int i = 0; i < in.length; i++) {
			return_value[i] = (double)in[i];
		}
		return return_value;
	}

	// Do the obvious conversions in case we need to return double[][].
	public static double[][] toDouble(int[][] in) {
		if (in == null)
			return null;
		double[][] return_value = new double[in.length][];
		for (int i = 0; i < in.length; i++) {
			return_value[i] = toDouble(in[i]);
		}
		return return_value;
	}

	// Do the obvious conversions in case we are passed a simplex as a double[].
	public static int[] toInt(double[] in) {
		if (in == null)
			return null;
		int[] return_value = new int[in.length];
		for (int i = 0; i < in.length; i++) {
			return_value[i] = (int)Math.round(in[i]);
		}
		return return_value;
	}

	// Do the obvious conversions in case we are passed simplices as double[][].
	protected static int[][] toInt(double[][] in) {
		if (in == null)
			return null;
		int[][] return_value = new int[in.length][];
		for (int i = 0; i < in.length; i++) {
			return_value[i] = toInt(in[i]);
		}
		return return_value;
	}

	/**
	 * Constructor for ExplicitStream.
	 *
	 * <p>
	 * Make an empty, open, ExplicitStream.
	 */
	public ExplicitStream() {
		simplices = null;
		persistence_parameters = null;
		closed = false;
		current_dimension = 0;
		current_index = 0;
		open_count = 1;
		parameters_table = new HashSet<Double>(100);
		simplex_table = new HashMap<Simplex,Double>(100);
	}

	// Open the stream for further additions, after creating the internal
	// state needed to record these additions. In particular, we put the
	// existing state into these tables and then throw away that existing
	// state.
	protected void open() {
		if (closed) {
			if (parameters_table == null)
				parameters_table = new HashSet<Double>(100);
			if (simplex_table == null)
				simplex_table = new HashMap<Simplex,Double>(100);
			if (simplices != null) {
				for(int d = 0; d < simplices.length; d++) {
					for(int i = 0; i < simplices[d].length; i++) {
						Simplex s = simplices[d][i];
						Double P = new Double(persistence_parameters[s.findex()]);
						simplex_table.put(Simplex.getSimplex(s.vertices()), P);
						parameters_table.add(P);
					}
				}
			}
			simplices = null;
			persistence_parameters = null;
			closed = false;
			open_count++;
		}
	}

	/**
	 * Convert a SimplexStream into an explicit one.
	 *
	 * <p>
	 * @param      str A SimplexStream instance.
	 * @return     An ExplicitStream with equivalent Simplex entries.
	 *
	 */
	public static ExplicitStream makeExplicit(SimplexStream str) {
		if (str instanceof ExplicitStream)
			return (ExplicitStream) str;
		else {
			ExplicitStream estr = new ExplicitStream();
			estr.open();
			if (str instanceof Iterable<?>) {
				Iterator<Simplex> iter = str.iterator();
				while(iter.hasNext()) {
					Simplex s = iter.next();
					estr.add(s.vertices(), str.convert_filtration_index(s.findex()));
				}
			} else {
				while(str.hasNext()) {
					Simplex s = str.next();
					estr.add(s.vertices(), str.convert_filtration_index(s.findex()));
				}
			}
			estr.close();
			return estr;
		}
	}

	/**
	 * Remove a simplex, specified by its vertex vector, from the stream. If
	 * the simplex isn't present, do nothing.
	 * <p>
	 * @param      vertices The vertex array of the Simplex to remove.
	 */
	public void remove(int[] vertices) {
		if (closed)
			open();
		Simplex s = Simplex.getSimplex(vertices);
		simplex_table.remove(s);
		assert(!simplex_table.containsKey(s));
	}

	/**
	 * Remove a simplex, specified by its vertex vector, and any simplex that
	 * contains it, from the stream. If nothing in the stream contains the
	 * simplex, do nothing.  <p>
	 * @param      vertices The vertex array of the Simplex to remove.
	 */
	public void prune(int[] vertices) {
		if (closed)
			open();
		Simplex tp = Simplex.getSimplex(vertices);
		int[] tp_vec = new int[tp.dimension()+1];
		int[] s_vec = new int[10];

		HashMap<Simplex,Double> pending = new HashMap<Simplex,Double>(100);
		for(Map.Entry<Simplex,Double> E : simplex_table.entrySet()) {
			Simplex s = E.getKey();
			Double P = E.getValue();
			if ((s.dimension()+1) > s_vec.length) {
				s_vec = new int[2 * (s.dimension()+1)];
			}
			if (tp.subset(s, tp_vec, s_vec))
				pending.put(s, P);
		}
		for(Map.Entry<Simplex,Double> E : pending.entrySet()) {
			Simplex s = E.getKey();
			simplex_table.remove(s);
		}

		assert(!simplex_table.containsKey(tp));
	}

	/**
	 * Remove a simplex, specified by its vertex vector as an array of
	 * double, from the stream. If the simplex isn't present, do nothing.
	 * <p>
	 * @param vertices The vertex array of the Simplex to remove, where the
	 * vertices are double instead of float -- this may be useful when called
	 * from Matlab.
	 */
	public void remove(double[] vertices) {
		remove(toInt(vertices));
	}

	/**
	 * Prune a simplex, specified by an array of double, from the stream. 
	 * <p>
	 * @param vertices The vertex array of the Simplex to prune, where the
	 * vertices are double instead of float -- this may be useful when called
	 * from Matlab.
	 */
	public void prune(double[] vertices) {
		prune(toInt(vertices));
	}

	/**
	 * Remove an array of simplices, specified by vertex vectors, from the stream.
	 * <p>
	 * @param simplex_array An array of arrays of vertices of the Simplices to remove.
	 * from Matlab.
	 */
	public void remove(int[][] simplex_array) {
		for (int i = 0; i < simplex_array.length; i++) {
			remove(simplex_array[i]);
		}
	}

	/**
	 * Prune an array of simplices, specified by vertex vectors, from the stream.
	 * <p>
	 * @param simplex_array An array of arrays of vertices of the Simplices to prune.
	 * from Matlab.
	 */
	public void prune(int[][] simplex_array) {
		for (int i = 0; i < simplex_array.length; i++) {
			prune(simplex_array[i]);
		}
	}

	/**
	 * Remove an array of simplices, specified by double[] vertex vectors,
	 * from the stream.  <p>
	 * @param simplex_array An array of arrays of vertices of the Simplices,
	 * given as double[][], to remove. This may be useful when called from Matlab.
	 */
	public void remove(double[][] simplex_array) {
		remove(toInt(simplex_array));
	}

	/**
	 * Prune an array of simplices, specified by double[] vertex vectors,
	 * from the stream.  <p>
	 * @param simplex_array An array of arrays of vertices of the Simplices,
	 * given as double[][], to prune. This may be useful when called from Matlab.
	 */
	public void prune(double[][] simplex_array) {
		prune(toInt(simplex_array));
	}

	// Internal version -- add a Simplex with a specified persistence parameter.
	protected void add(Simplex s, double p) {
		if (closed)
			open();
		Double P = new Double(p);
		parameters_table.add(P);
		Double sP = simplex_table.get(s);
		if (sP == null) {
			simplex_table.put(s, P);
		} else {
			simplex_table.put(s, (P.doubleValue() > sP.doubleValue())?sP:P);
		}
	}

	/**
	 * Add a simplex, specified by its vertex vector and persistence
	 * parameter, to the stream. If the simplex is already present, make sure
	 * that the associated persistence parameter is the minimum of the new
	 * value and the one already in use.
	 * <p>
	 * @param vertices The vertex array of the Simplex to add.
	 * @param parameter The persistence parameter of the Simplex to add.
	 */
	public void add(int[] vertices, double parameter) {
		Simplex s = Simplex.getSimplex(vertices);
		add(s, parameter);
	}

	/**
	 * Add a simplex, specified by its vertex vector and persistence
	 * parameter, to the stream. If the simplex is already present, make sure
	 * that the associated persistence parameter is the minimum of the new
	 * value and the one already in use.
	 * <p>
	 * @param vertices The vertex array of the Simplex to add, given as a double[].
	 * @param parameter The persistence parameter of the Simplex to add.
	 */
	public void add(double[] vertices, double parameter) {
		add(toInt(vertices), parameter);
	}

	/**
	 * Add an array of simplices, specified by matching vertex vectors and
	 * persistence parameters, to the stream.
	 * <p>
	 * @param simplex_array The array of array of vertices specifying the
	 * Simplices to add.
	 * @param parray The array of associated persistence parameters.
	 */
	public void add(int[][] simplex_array, double[] parray) {
		if (simplex_array.length != parray.length)
			throw new 
			IllegalArgumentException
			("Length mismatch between arguments to add().");
		for (int i = 0; i < simplex_array.length; i++) {
			add(simplex_array[i], parray[i]);
		}
	}

	/**
	 * Add an array of simplices, specified by matching vertex vectors and
	 * persistence parameters, to the stream.
	 * <p>
	 * @param simplex_array The array of array of vertices specifying the
	 * Simplices to add, given as arrays of double[].
	 * @param parray The array of associated persistence parameters.
	 */
	public void add(double[][] simplex_array, double[] parray) {
		add(toInt(simplex_array), parray);
	}

	// Make sure that all of the faces are present of a Simplex are
	// present. If some are missing, add them with the smallest persistence
	// parameter associated with that face in the pending table.
	protected void ensure_faces(Simplex s, Double P, HashMap<Simplex,Double> pending) {
		assert(P instanceof Double);
		if (s.dimension() > 0) {
			SimplexFaceIterator fi = new SimplexFaceIterator(s, s.dimension()-1);
			// add missing faces
			while (fi.hasNext()) {
				Simplex face = Simplex.getSimplex(fi.next());
				if (!simplex_table.containsKey(face)) {
					if (pending.containsKey(face)) {
						Double prior_faceP = pending.get(face);
						if (prior_faceP.doubleValue() < P.doubleValue())
							P = prior_faceP;
					}
					pending.put(Simplex.getSimplex(face.vertices()), P);
					ensure_faces(face, P, pending);
				}
			} 
		}
	}

	/**
	 * Make sure all of the faces of simplices in simplex_table are present
	 * in the table as well.  <p> In doing so we use the smallest relevant
	 * persistence parameter. This does NOT ensure that the full complex will
	 * be consistent, but if it fails, the complex was doomed anyway.  <p>
	 *
	 */
	public void ensure_all_faces() {
		if (!closed) {
			HashMap<Simplex,Double> pending = new HashMap<Simplex,Double>(100);
			for(Map.Entry<Simplex,Double> E : simplex_table.entrySet()) {
				Simplex s = E.getKey();
				Double P = E.getValue();
				ensure_faces(s, P, pending);
			}
			for(Map.Entry<Simplex,Double> E : pending.entrySet()) {
				Simplex s = E.getKey();
				Double P = E.getValue();
				parameters_table.add(P);
				simplex_table.put(s, P);
			}
		}
	}

	// Realloc() for int[].
	protected int[] ensure_length(int[] v, int index) {
		if (index < v.length)
			return v;
		int[] new_v = new int[index+1];
		for (int i = 0; i < v.length; i++) 
			new_v[i] = v[i];
		return new_v;
	}

	// Don't make the window unless we actually need it.
	protected void message(String msg) {
		if ((message_window == null) && (Plex.useMessageWindow()))
			message_window = new PlexMessageWindow("Messages: " + this.toString());
		if (message_window == null)
			System.out.printf("%s", msg);
		else 
			message_window.message(msg);
	}

	// Iterate over all of the faces of s and make sure that they are present
	// in simplex_table. This only applies to open ExplicitStreams, since the
	// process of closing a stream requires that it be consistent.
	protected boolean faces_are_consistent(Simplex s, Double P,
			boolean error, boolean print) {
		boolean success = true;
		if (!closed) {
			SimplexFaceIterator fi = new SimplexFaceIterator(s, s.dimension()-1);
			// check that the faces are all present and have smaller persistence parameter
			while (fi.hasNext()) {
				Simplex face = Simplex.getSimplex(fi.next());
				Double faceP = simplex_table.get(face);
				if (faceP == null) {
					success = false;
					if (error) {
						persistence_parameters = null;
						throw new
						IllegalStateException("Simplex " + 
								s.toString() + 
								" is in the stream, but not face " +
								face.toString());
					} else if (print) {
						message(String.format("Simplex %s is present, but its face %s is not.\n",
								s.toString(), face.toString()));
					}
				} else if ((faceP.doubleValue() > P.doubleValue()) ||
						(face.findex() > s.findex())) {
					success = false;
					if (error) {
						persistence_parameters = null;
						throw new
						IllegalStateException("Simplex " + 
								s.toString() + 
								" has persistence value " +
								P.toString() +
								" filtration index " +
								s.findex() +
								", but persistence value of " +
								face.toString() +
								" or filtration index of " +
								face.findex() +
						" is larger");
					} else if (print) {
						message(String.format
								("Simplex %s has value %.4f, but face %s has value %.4f.\n",
										s.toString(), P.doubleValue(), 
										face.toString(), faceP.doubleValue()));
					}
				}
			}
		}
		return success;
	}

	/**
	 * Quietly verify the ExplicitStream. Any successfully closed stream must
	 * be consistent.  <p>
	 * @return     Return true if the stream is consistent, else false.
	 *
	 */
	public boolean verify() {
		return verify(false);
	}

	/**
	 * Verify the ExplicitStream. This is only applied to a stream that has been
	 * opened for additions. Any successfully closed stream must be consistent.
	 *
	 * <p>
	 * @param      verbose if true print a message for every mistake found.
	 * @return     Return true if the stream is consistent, else false.
	 *
	 */
	public boolean verify(boolean verbose) {
		boolean success = true;
		if (!closed) {
			for(Map.Entry<Simplex,Double> E : simplex_table.entrySet()) {
				Simplex s = E.getKey();
				Double P = E.getValue();
				assert(P instanceof Double);
				boolean good_face = faces_are_consistent(s, P, false, verbose);
				if (success)
					success = good_face;
			}
		}
		return success;
	}

	/**
	 * Close the stream the current additions, provided that the contents are
	 * consistent and complete. Otherwise, throw an error indicating the
	 * problem. Note that even if the close is successful, further additions
	 * can be made at any time. It isn't obvious that this method needs to be
	 * public, but it makes some testing simpler.
	 */
	public void close() {
		if (!closed) {
			int pcount =
				(parameters_table != null)?parameters_table.size():0;
				persistence_parameters = new double[pcount];
				if (parameters_table != null) {
					int pcounter = 0;
					for (Double P : parameters_table) {
						persistence_parameters[pcounter++] = P.doubleValue();
					}
				}
				Arrays.sort(persistence_parameters);
				if (simplex_table != null) {
					int[] dimensions = new int[10];
					int max_dimension = -1;
					for(Map.Entry<Simplex,Double> E : simplex_table.entrySet()) {
						Simplex s = E.getKey();
						Double P = E.getValue();
						assert(P instanceof Double);
						dimensions = ensure_length(dimensions, s.dimension());
						dimensions[s.dimension()]++;
						max_dimension = Math.max(max_dimension, s.dimension());          
						// check that the faces are all present and have persistence
						// parameters that are no larger
						faces_are_consistent(s, P, true, false);
					}
					simplices = new Simplex[max_dimension+1][];
					int[] counters = new int[max_dimension+1];
					for (int i = 0; i < simplices.length; i++) 
						simplices[i] = new Simplex[dimensions[i]];
					for(Map.Entry<Simplex,Double> E : simplex_table.entrySet()) {
						Simplex s = E.getKey();
						Double P = E.getValue();
						double s_p = P.doubleValue();
						if (s.findex() < 0)
							// only bother setting the findex values if they haven't been set
							s.setfindex(Arrays.binarySearch(persistence_parameters, s_p));
						simplices[s.dimension()][counters[s.dimension()]++] = s;
					}
					// Sort the simplices entries within each dimension. This will
					// result in a state that allows us to return the contents of the
					// stream in an order suitable for the Persistence calculation.
					for (int i = 0; i < simplices.length; i++) {
						// assert that the simplices subarrays are full
						assert (simplices[i].length == counters[i]);
						Simplex.persistence_sort(simplices[i]);
					}
				}
				simplex_table = null;
				parameters_table = null;
				closed = true;
		}
	}

	/**
	 * How many simplices are in the stream?
	 *
	 * <p>
	 * @return The number of simplices in a closed stream, else 0 if the
	 * stream is open.
	 *
	 */
	public int size() {
		if (!closed)
			return 0;
		else {
			if (simplices == null)
				return 0;
			else {
				int total = 0;
				for (int i = 0; i < simplices.length; i++) {
					total += simplices[i].length;
				}
				return total;
			}
		}
	}

	/**
	 * Max dimension of simplices in the stream.
	 *
	 * <p>
	 * @return Max dimension of simplices in the stream, if closed and
	 * non-empty, else -1.
	 *
	 */
	public int maxDimension() {
		if (!closed)
			return -1;
		else {
			if (simplices == null)
				return -1;
			else {
				return simplices.length-1;
			}
		}
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
		return persistence_parameters[fi];
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
		return new ExplicitStreamIterator(this);
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
		return new ExplicitStreamIterator(this, d);
	}

	/**
	 * Instances provide Iterator<Simplex> for non-destructive iterating
	 * over entries. That is, repeated iteration over an ExplicitStream
	 * instance is possible with multiple instances of this class.
	 */
	protected static class ExplicitStreamIterator implements Iterator<Simplex> {
		protected int current_index;
		protected int current_dimension;
		protected boolean fixed_dimension;
		protected int open_count;
		protected ExplicitStream parent;

		protected ExplicitStreamIterator(ExplicitStream str) {
			current_dimension = 0;
			current_index = 0;
			open_count = str.open_count;
			fixed_dimension = false;
			parent = str;
		}

		protected ExplicitStreamIterator(ExplicitStream str, int dimension) {
			if ((dimension < 0) || (dimension >= str.simplices.length))
				throw new IllegalArgumentException(dimension + 
						" must be >= 0 and <= " + 
						(str.simplices.length-1)); 
			current_dimension = dimension;
			current_index = 0;
			open_count = str.open_count;
			fixed_dimension = true;
			parent = str;
		}

		// Go to the "next" index pair. This code assumes that there is such a
		// pair, and so needs to be called only when we know that this is the
		// case.
		protected void skip_to_next_index_pair() {
			current_index++;
			while((current_dimension < parent.simplices.length) &&
					(current_index >= parent.simplices[current_dimension].length)) {
				current_index = 0;
				current_dimension++;
			}
		}

		// Are the parent stream and the iterator consistent?
		protected boolean parent_is_consistent() {
			return (parent.closed && (open_count == parent.open_count));
		}

		// NOTE: For this to work it must be the case that any CLOSED
		// ExplicitStream has the property that the Simplex[] for the highest
		// dimension has positive length. That is, if the largest dimensional
		// Simplex in the stream has dimension 3, then simplices.length must
		// be 4. It is, of course, possible that simplices[i] has 0 length
		// for some i, just not for i == simplices.length-1.
		protected boolean has_next_index() {
			return (fixed_dimension?
					(current_index < parent.simplices[current_dimension].length):
						((current_dimension < (parent.simplices.length-1)) ||
								(current_dimension == (parent.simplices.length-1)) &&
								(current_index < 
										parent.simplices[parent.simplices.length-1].length)));
		}

		/**
		 * Returns <tt>true</tt> if the iterator has more simplices.
		 *
		 * @return <tt>true</tt> if the iterator has more simplices, else
		 * <tt>false</tt>.
		 */
		public boolean hasNext() {
			if (!parent_is_consistent())
				return false;
			else
				return has_next_index();
		}

		/**
		 * Returns the next Simplex in the iteration of the stream.
		 * Used in concert with the {@link #hasNext()} method returns
		 * return each Simplex in the stream exactly once. Does
		 * <em>not</em> have any side effects on the stream.
		 * <p>
		 * @return the next Simplex in the stream.
		 * @exception NoSuchElementException stream has no more elements.
		 */
		public Simplex next() {
			if (!parent_is_consistent())
				throw new
				IllegalStateException("The parent stream (" + parent.toString() +
				") has changed since this iterator was created.");
			if (!has_next_index())
				throw new NoSuchElementException();

			Simplex next = parent.simplices[current_dimension][current_index];
			skip_to_next_index_pair();
			return next;
		}

		/**
		 * Unsupported remove() operation.
		 *
		 * @exception UnsupportedOperationException 
		 */
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Convert a Simplex[][] into an ExplicitStream.
	 * <p>
	 * @param complex A Simplex[][] collection of simplices which are
	 * believed to a simplicial complex with findex values usable as
	 * persistence parameters.
	 * @return An ExplicitStream instance the encloses the Simplex instances.
	 */
	public static ExplicitStream enclose (Simplex[][] complex) {
		ExplicitStream estr = new ExplicitStream();
		estr.open();
		for (int i = 0; i < complex.length; i++) {
			Simplex[] sarray = complex[i];
			for (int j = 0; j < sarray.length; j++) {
				Simplex s = sarray[j];
				double p = (s.findex() < 0)?0.0:((double)s.findex());
				estr.add(s.copy(), p);
			}
		}
		estr.close();
		return estr;
	}
}
