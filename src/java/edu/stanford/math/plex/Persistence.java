package edu.stanford.math.plex;

import java.util.*;

/**
 * The <code>Persistence</code> class class implements the algorithm of
 * Zomorodian and Carlsson for computing the persistent homology groups
 * with coefficients in a finite field. The details of this are too complex
 * to explain here. See A. Zomorodian and G. Carlsson, "Computing
 * persistent homology," <i>Discrete and Computational Geometry</i>, 33
 * (2), pp. 247-274.
 *
 *<p>
 *
 * The algorithm needs to be extended to return useful basis elements for
 * the homology, and do cohomology calculations. And a really big, open
 * question is how to do the same sort of calculation when using some
 * representation (possibly simplicial sets) that will scale better at the
 * persistence parameter increases.
 *
 * @version $Id$
 */
public class Persistence {

	// The characteristic of the coefficient field currently in
	// use. Default is 11 (for no particular reason).
	private static int p = 11;

	// The multiplicative inverses for the coefficient field currently in use.
	// Again, the default is for the field Z11.
	private static int[] pInverses = multiplicative_inverses(p);

	/**
	 * What is modulus of the coefficient field?
	 */    
	public static int baseModulus() {
		return p;
	}

	/**
	 * Set or reset the coefficient field.
	 * @param      modulus   Must be a prime in [2,255].
	 * @exception  IllegalArgumentException 
	 */    
	public static void setBaseModulus(int modulus) {
		if ((modulus <= 1) || (modulus >= 256))
			throw new IllegalArgumentException(modulus + " is not in [2, 255].");
		p = modulus;
		pInverses = multiplicative_inverses(modulus);
	}

	// Compute the multiplicative inverses for elements of Zp.
	private static int[] multiplicative_inverses(int p) {
		int[] return_value = new int[p];
		for (int i = 1; i < p; i++) {
			int inverse = 0;
			for (int j = 1; j < p; j++) {
				if (((j * i) % p) == 1) {
					inverse = j;
					break;
				}
			}

			if (inverse == 0) 
				throw new IllegalArgumentException(p + " is not a prime."); 

			return_value[i] = inverse;
		}
		return return_value;
	}

	/**
	 * Table in which to intern the simplices that form a basis for the cycle space.
	 */    
	SimplexTable marked;

	// Remove pivot rows from the boundary of
	private Chain removePivotRows(Simplex sigma) {
		Simplex[] b = sigma.boundaryArray();

		if (b == null)
			return null;

		for (int i = 0; i < b.length; i++)
			// If the entry isn't marked, we clear it, and if the entry is
			// marked, we replace it with the Simplex that came from the
			// stream, which has the findex properly set. This means that
			// we don't have to recompute or find the filtration indices
			// for the faces, and all of the simplices created by the
			// boundary() method die immediately.
			b[i] = marked.get(b[i]);

		Chain d = Chain.fromBoundary(b,p);

		while (!d.equals(Chain.zero(p))) {
			Simplex d_max = d.maxS();
			Chain t_i = d_max.chain();

			if (t_i == null)
				return d;
			else {
				int minus_q_inv = p - pInverses[t_i.maxC()];
				// NOTE: If we add support for fields other then Zp, we should
				// change this line to be a method that returns
				// -(d.maxC()/t_i.maxC()). Also, we'll have to change the Chain
				// code that does addition.
				int c = (d.maxC() * minus_q_inv) % p; 
				d = d.add(t_i, c);
			}
		}   

		return null;
	}


	/**
	 * Return the dimension of the cycle space.
	 * <p>
	 *
	 * This function is for performance tuning and pandering to aimless curiosity.
	 * <p>
	 *
	 * @return number of basis cycles.
	 *
	 */
	public int cycleSpaceDimension() {
		return marked.size();
	}

	/**
	 * Calculate the persistent homology.
	 * <p>
	 *
	 * @param      stream   generates simplices in persistence/dimension order.
	 * @return     PersistenceInterval.Float[] instances describing the
	 * homology.
	 */
	public PersistenceInterval.Float[] computeIntervals(SimplexStream stream) {
		return computeIntervals(stream, true, p);
	}

	/**
	 * Calculate the persistent homology with a specified Zp.
	 * <p>
	 *
	 * @param      stream   generates simplices in persistence/dimension order.
	 * @param      preserveStream If true, don't deplete the stream.
	 * @param      prime   Set the base field to be Z/prime.
	 * @return     PersistenceInterval.Float[] instances describing the
	 * homology.
	 */
	public PersistenceInterval.Float[] computeIntervals(SimplexStream stream, 
			boolean preserveStream,
			int prime) {
		PersistenceInterval[] raw = computeRawIntervals(stream, preserveStream, prime);
		PersistenceInterval.Float[] return_value = 
			new PersistenceInterval.Float[raw.length];
		int counter = 0;
		for(PersistenceInterval pi : raw) 
			return_value[counter++] = stream.convertInterval(pi);
		return return_value;
	}


	/**
	 * Calculate the raw persistent intervals for a stream, using a specified Zp.
	 * This method will probably be used only by internal tests.
	 * <p>
	 *
	 * @param      stream   generates simplices in persistence/dimension order.
	 * @param      preserveStream If true, don't deplete the stream.
	 * @param      prime   Set the base field to be Z/prime.
	 * @return     PersistenceInterval[] instances describing the
	 * homology in terms of the raw filtration indices.
	 */
	public PersistenceInterval[] computeRawIntervals (SimplexStream stream, 
			boolean preserveStream,
			int prime) {
		Iterator<Simplex> iterator = (preserveStream)?stream.iterator():stream;

		// when running with assertions, verify streams that aren't really big
		assert ((stream.size() < 10000000) && stream.verify());

		if(prime != p)
			setBaseModulus(prime);

		marked = new SimplexTable(stream.size());
		ArrayList<PersistenceInterval> intervals = 
			new ArrayList<PersistenceInterval>();
		while(iterator.hasNext()) {
			Simplex current = iterator.next();
			// Make sure that current.chain() is empty -- if we are consuming the
			// chain without having run an iterator, this must be so.
			current.clearChain();
			Chain d = removePivotRows(current);
			if (d == null) {
				marked.put(current);
			} else {
				Simplex d_max = d.maxS();
				int k = d_max.dimension();
				d_max.setChain(d);
				if (k < stream.maxDimension()) {
					if (d_max.findex() != current.findex()) {
						// leave out intervals that are created and destroyed "simultaneously"
						PersistenceInterval i = 
							new PersistenceInterval.Int(k, d_max.findex(), 
									current.findex());
						intervals.add(i);
					}
				} 
			}
		}

		for(Simplex sigma : marked) {
			if (sigma.chain() == null) {
				int k = sigma.dimension();
				if (k < stream.maxDimension()) {
					PersistenceInterval i = 
						new PersistenceInterval.Int(k, sigma.findex());
					intervals.add(i);
				} 
			}
		}

		{
			PersistenceInterval[] return_value = 
				new PersistenceInterval[intervals.size()];
			int counter = 0;
			for(PersistenceInterval pi : intervals) {
				return_value[counter++] = pi;
			}
			Arrays.sort(return_value);
			return return_value;
		}

	}
}
