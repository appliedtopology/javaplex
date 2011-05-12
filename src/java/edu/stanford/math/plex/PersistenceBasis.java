package edu.stanford.math.plex;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Vector;

/** The class PersistenceBasis implements the Persistence algorithm by 
 *  Carlsson-Zomorodian, with additional basis element tracking to make
 *  sure that afterwards, basis elements for the persistence intervals may
 *  be extracted.
 */
public class PersistenceBasis {
	protected static int p = 11;
	protected static int[] pInverses = multiplicative_inverses(p);


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
	protected static int[] multiplicative_inverses(int p) {
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
	 * Returns a boundary matrix for a given simple simplex stream in a format usable from Matlab.
	 * <p>
	 * @param stream SimpleSimplexStream providing the simplices
	 * @param findex The filtration index at which to perform the computation
	 * @param degree We want the boundary matrix taking simplices of dimension degree to simplices of dimension degree-1 
	 */

	public static double[][] boundaryMatrix(SimplexStream stream, double findex, int degree) {
		boolean DEBUGGING = false;
		Vector<Simplex> fromBasis = new Vector<Simplex>();
		Vector<Simplex> toBasis = new Vector<Simplex>();
		Iterator<Simplex> ss = stream.iterator(); // avoid consuming the entire stream

		// Construct a basis for the matrix.
		while(ss.hasNext()) {
			Simplex s = ss.next();
			if(stream.convert_filtration_index(s.findex()) > findex)
				continue;
			if(DEBUGGING) System.out.printf("Considering %s\n", s);
			if(s.dimension() == degree) {
				fromBasis.add(s);
				if(DEBUGGING) System.out.println("\tfromBasis");
			} else if(s.dimension() == degree-1) {
				toBasis.add(s);
				if(DEBUGGING) System.out.println("\ttoBasis");
			}
		}

		if(DEBUGGING) System.out.printf("# fromBasis:\t%d\t# toBasis:\t%d\n", fromBasis.size(), toBasis.size());

		double[][] returnMatrix = new double[toBasis.size()][fromBasis.size()];

		for(int i=0; i<fromBasis.size(); i++) { 
			Chain boundary = fromBasis.get(i).boundary(p); 
			if(boundary == null) 
				continue;
			for(int j=0; j<toBasis.size(); j++) {
				returnMatrix[j][i] = boundary.rewriteModP(boundary.coefficientOf(toBasis.get(j)));
			} 
		}

		return returnMatrix;    
	}

	/** 
	 * Returns a sparse boundary matrix for a given simple simplex stream in a format usable from Matlab.
	 * In the return format, a double[n][3] matrix is returned, with each row consisting of rowindex, columnindex and 
	 * value, so that the three sections may be fed into Matlab's sparse command.
	 * <p>
	 * @param stream SimpleSimplexStream providing the simplices
	 * @param findex The filtration index at which to perform the computation
	 * @param degree We want the boundary matrix taking simplices of dimension degree to simplices of dimension degree-1 
	 */	
	public static double[][] boundaryMatrixSparse(SimplexStream stream, double findex, int degree) {
		boolean DEBUGGING = false;
		Vector<Simplex> fromBasis = new Vector<Simplex>();
		Vector<Simplex> toBasis = new Vector<Simplex>();
		Iterator<Simplex> ss = stream.iterator(); // avoid consuming the entire stream

		// Construct a basis for the matrix.
		while(ss.hasNext()) {
			Simplex s = ss.next();
			if(stream.convert_filtration_index(s.findex()) > findex)
				continue;
			if(DEBUGGING) System.out.printf("Considering %s\n", s);
			if(s.dimension() == degree) {
				fromBasis.add(s);
				if(DEBUGGING) System.out.println("\tfromBasis");
			} else if(s.dimension() == degree-1) {
				toBasis.add(s);
				if(DEBUGGING) System.out.println("\ttoBasis");
			}
		}

		if(DEBUGGING) System.out.printf("# fromBasis:\t%d\t# toBasis:\t%d\n", fromBasis.size(), toBasis.size());

		Vector<double[]> returnMatrix = new Vector<double[]>(stream.size()+1);
		double[] entry = new double[3];
		entry[0] = fromBasis.size()-1;
		entry[1] = toBasis.size()-1;
		entry[2] = 0;
		returnMatrix.add(entry);
		for(int i=0; i<fromBasis.size(); i++) { 
			Chain boundary = fromBasis.get(i).boundary(p); 
			if(boundary == null) 
				continue;
			for(int j=0; j<toBasis.size(); j++) {
				double coeff = boundary.rewriteModP(boundary.coefficientOf(toBasis.get(j)));
				if(coeff == 0) continue;
				entry = new double[3];
				entry[0] = i;
				entry[1] = j;
				entry[2] =  coeff;
				returnMatrix.add(entry);
			} 
		}

		double[][] ret = new double[returnMatrix.size()][3];
		int i = 0;
		for(double[] retEntry : returnMatrix) {
			for(int j=0; j<3; j++)
				ret[i][j] = retEntry[j];
			i++;
		}

		return ret;
	}

	public static double[] chainVector(SimplexStream stream, Chain chain, double findex) {
		Iterator<Simplex> ss = stream.iterator();
		int degree = chain.maxS().dimension();
		// Construct a basis
		Vector<Simplex> basis = new Vector<Simplex>();

		while(ss.hasNext()) {
			Simplex s = ss.next();
			if(stream.convert_filtration_index(s.findex()) > findex)
				break;
			if(s.dimension() == degree)
				basis.add(s);
		}

		// Rewrite vector in this basis

		double[] returnVector = new double[basis.size()];
		for(int i=0; i<basis.size(); i++) {
			returnVector[i] = chain.rewriteModP(chain.coefficientOf(basis.get(i)));
		}
		return returnVector;
	}

	public static int[][] basis(SimplexStream stream, int degree, double findex) {
		Iterator<Simplex> ss = stream.iterator();
		// Construct a basis
		Vector<Simplex> basis = new Vector<Simplex>();

		while(ss.hasNext()) {
			Simplex s = ss.next();
			if(stream.convert_filtration_index(s.findex()) > findex)
				break;
			if(s.dimension() == degree)
				basis.add(s);
		}

		int[][] basisArray = new int[degree+1][basis.size()];
		for(int i=0; i<basis.size(); i++) {
			int[] basisVertices = basis.get(i).vertices();
			for(int j=0; j<=degree; j++) {
				basisArray[j][i] = basisVertices[j];
			}
		}
		return basisArray;
	}

	/**
	 * Compute the persistent homology intervals of the simplex stream
	 * given. This function is a wrapper to make the call easy with good 
	 * default behaviour.
	 * <p>
	 * @param sstream The simplex stream representing the filtered 
	 *                complex of which the homology is computed.
	 * @return An array of type PersistenceBasisInterval carrying the
	 *         relevant persistence intervals with basis elements.
	 */
	public static PersistenceBasisInterval.Float[]
	                                             computeIntervals(SimplexStream sstream) {
		PersistenceBasis pb = new PersistenceBasis();
		return pb.computeIntervals(sstream,11,false);
	}

	/**
	 * Compute the persistent homology intervals of the simplex stream
	 * given with an option flag to control which type of basis we want
	 * to choose.
	 * <p>
	 * @param sstream The simplex stream representing the filtered
	 *                complex of which the homology is computed.
	 * @param retrofit If false, the basis element is guaranteed to 
	 *                 exist throughout the entire interval. If true, 
	 *                 the basis element of a finite interval will 
	 *                 correspond to the reduced boundary of the simplex
	 *                 killing the interval.
	 * @return An array of type PersistenceBasisInterval carrying the
	 *          relevant persistence intervals with basis elements.
	 */
	public static PersistenceBasisInterval.Float[]
	                                             computeIntervals(SimplexStream sstream, boolean retrofit) {
		PersistenceBasis pb = new PersistenceBasis();
		return pb.computeIntervals(sstream,11,retrofit);
	}

	/**
	 * Compute the persistent homology intervals of the simplex stream 
	 * given with explicitly given characteristic of the underlying field
	 * as well as an option flag to control which type of basis we want to 
	 * choose.
	 * <p>
	 *
	 * @param sstream The simplex stream representing the filtered
	 *                complex of which the homology is computed.
	 * @param prime The characteristic of the base field of the computation.
	 * @param retrofit If false, the basis element is guaranteed to
	 *                 exist throughout the entire interval. If true,
	 *                 the basis element of a finite interval will
	 *                 correspond to the reduced boundary of the simplex
	 *                 killing the interval.
	 * @return An array of type PersistenceBasisInterval carrying the
	 *          relevant persistence intervals with basis elements.
	 */
	public PersistenceBasisInterval.Float[] 
	                                      computeIntervals(SimplexStream sstream, int prime, boolean retrofit) {
		PersistenceBasisInterval[] raw = computeRawIntervals(sstream, prime);
		Vector<PersistenceBasisInterval.Float> preret = new Vector<PersistenceBasisInterval.Float>();
		for(PersistenceBasisInterval pi : raw) {
			PersistenceBasisInterval.Float newInterval = sstream.convertInterval(pi);
			if(newInterval.end != newInterval.start)
				preret.add(newInterval);
		}
		PersistenceBasisInterval.Float[] ret = 
			new PersistenceBasisInterval.Float[preret.size()];
		int counter = 0;
		for(PersistenceBasisInterval.Float pi : preret)
			ret[counter++] = pi;
		return ret;
	}

	// Compute raw intervals - amalgamates the old computeIntervals and 
	// removePivotRows

	protected enum SimplexType {
		CHAIN, CYCLE, BOUNDARY
	}
	public PersistenceBasisInterval[]
	                                computeRawIntervals(SimplexStream sstream, int prime) {
		boolean DEBUGGING = false;
		if(prime != p)
			setBaseModulus(prime);

		// Notice that the findex renumbering we are performing in order to guarantee that the
		// arithmetic works well means we can have constant time interactions for all the 
		// simplex-to-something else tables.
		int nSpx = sstream.size();
		Simplex killer[] = new Simplex[nSpx];
		Chain tadpole[] = new Chain[nSpx];
		Chain preimage[] = new Chain[nSpx];
		SimplexType type[] = new SimplexType[nSpx];
		int localFindex[] = new int[nSpx];

		List<Simplex> simplices = new Vector<Simplex>();
		Integer currentFindex = 0;
		Iterator<Simplex> iterator = sstream.iterator();

		while(iterator.hasNext()) {
			Simplex nextSimplex = iterator.next();
			Simplex current = nextSimplex.copy();
			localFindex[currentFindex]=nextSimplex.findex();
			current.setfindex(currentFindex);
			currentFindex++;

			type[current.findex()] = SimplexType.CHAIN;
			simplices.add(current);

			if(DEBUGGING) System.out.printf("%s\n",current);

			Chain d = current.boundary(p);
			d = d.filter(simplices);
			Chain w = new Chain(p);

			// Reduce d modulo the boundaries
			reduce:
				while(!Chain.isZero(d)) {
					Simplex sigma = d.maxS();
					switch (type[sigma.findex()]) {
					case CHAIN: 
						//System.out.printf("\t%s leads a chain\n", sigma);
						if(DEBUGGING) System.out.printf("\tLeading term of %s is %s is white\n", d, sigma);
						throw new IllegalArgumentException("Simplex stream is not a complex.");
					case CYCLE: 
						if(DEBUGGING) System.out.printf("\tLeading term of %s is %s is yellow\n", d, sigma);
						break reduce;
					case BOUNDARY: 
						if(DEBUGGING) System.out.printf("\tLeading term of %s is %s is pink\n", d, sigma);
						int q = d.maxC();
						Chain dt = tadpole[sigma.findex()];
						Chain t = preimage[sigma.findex()];

						if(Chain.isZero(t)) {
							// We are in a degenerate case.
							break reduce;
						}

						d = d.add(dt,p-q);
						w = w.add(t,q);
						break;
					}
				}

			if(Chain.isZero(d)) {
				type[current.findex()] = SimplexType.CYCLE;
				Chain newTadpole = new Chain(p,1,current);
				tadpole[current.findex()] = newTadpole.add(w,p-1);
				if(DEBUGGING) System.out.printf("\tNew cycle with tadpole: %s lead by %s\n", tadpole[current.findex()], current);
			} else {
				Simplex sigma = d.maxS();
				type[sigma.findex()] = SimplexType.BOUNDARY;
				killer[sigma.findex()] = current;
				int c = pInverses[d.maxC()];
				Chain newTadpole = new Chain(p);
				newTadpole = newTadpole.add(d,c);
				tadpole[sigma.findex()] =newTadpole;
				Chain newPreimage = new Chain(p);
				newPreimage = newPreimage.add(new Chain(p,1,current),c);
				newPreimage = newPreimage.add(w,p-c);
				preimage[sigma.findex()] = newPreimage;
				if(DEBUGGING) System.out.printf("\tNew boundary with tadpole: %s lead by %s\n", tadpole[sigma.findex()], sigma);
				if(DEBUGGING) System.out.printf("\t\tkiller(%s) = %s\n", sigma, killer[sigma.findex()]);
				if(DEBUGGING) System.out.printf("\t\tpreimage(%s) = %s\n", sigma, preimage[sigma.findex()]);
			}
		}

		// Assemble the persistence intervals
		List<PersistenceBasisInterval> intervals = new Vector<PersistenceBasisInterval>();
		for(Simplex s : simplices) {
			PersistenceBasisInterval pbi;
			switch (type[s.findex()]) {
			case CHAIN:
				continue;
			case CYCLE:
				if(s.dimension() >= sstream.maxDimension())
					break;
				pbi = new PersistenceBasisInterval.Int(tadpole[s.findex()],localFindex[s.findex()]);
				intervals.add(pbi);
				break;
			case BOUNDARY:
				if(s.dimension() >= sstream.maxDimension())
					break;
				if(localFindex[s.findex()] == localFindex[killer[s.findex()].findex()])
					break;
				pbi = new PersistenceBasisInterval.Int(tadpole[s.findex()],
						localFindex[s.findex()],
						localFindex[killer[s.findex()].findex()]);
				intervals.add(pbi);
				break;
			}
		}

		{
			PersistenceBasisInterval[] returnvalue = 
				new PersistenceBasisInterval[intervals.size()];
			int counter = 0;
			for(PersistenceBasisInterval pi : intervals)
				returnvalue[counter++] = pi;
			Arrays.sort(returnvalue);
			return returnvalue;
		}
	}	

	

	protected static class ListItem {
		public Chain cocycle;
		public Chain coboundary;
		public int findex;
	}

	/** 
	 * Computes persistent cohomology using a zig-zag based approach due to Dmitriy Morozov.
	 * <p>
	 * Seems to compute wrongly. Need to step through small examples and work out what exactly what's happening.
	 * Diagnosis: too high dimensional cocycles occurring, and way too many of them. The cocycles expected do not occur.
	 * <p>
	 * @param stream SimpleSimplexStream providing the simplices
	 * @return PersistenceBasisInterval[] carrying the computed intervals and their basis elements.
	 */
	public static PersistenceBasisInterval[] computePersistentCohomologyZigZag(SimplexStream stream, int prime) {
		boolean DEBUGGING = false;
		if(prime != p)
			setBaseModulus(prime);

		// compute all coboundaries, and set them in the simplex chain properties

		Vector<Simplex> simplices = new Vector<Simplex>(stream.size());
		{
			Map<Simplex,Chain> coboundary = new LinkedHashMap<Simplex,Chain>();
			Iterator<Simplex> sIt = stream.iterator();
			while(sIt.hasNext()) {
				Simplex s = sIt.next();
				//				System.out.println(s);
				coboundary.put(s, new Chain(prime));
				Chain boundary = s.boundary(prime);
				for(Simplex t : boundary.simplices) {
					coboundary.put(t,coboundary.get(t).add(new Chain(p,1,s),boundary.coefficientOf(t)));
				}
			}

			for(Simplex s : coboundary.keySet()) {
				s.clearChain();
				s.setChain(coboundary.get(s));
				simplices.add(s);
			}
		}


		//		System.out.printf("*************************\n");
		/*  go through the algorithm  */

		// We need lists for the cocycles and coboundaries
		LinkedList<ListItem> bases = new LinkedList<ListItem>();
		LinkedList<PersistenceBasisInterval.Int> intervals = new LinkedList<PersistenceBasisInterval.Int>();

		if(DEBUGGING) System.out.printf("# simplices: %d\n", simplices.size());

		for(Simplex s : simplices) {
			if(DEBUGGING)
				System.out.println(s);
			ListIterator<ListItem> wIt = bases.listIterator();
			ListItem w=null;
			// Find first coboundary with nonzero coefficient for s
			while(wIt.hasNext()) {
				w = wIt.next();
				if(w.coboundary.coefficientOf(s) != 0) {
					break;
				}
			}

			if(DEBUGGING) System.out.printf("\tFound %s, %s\n", w!=null?w.cocycle:"null", w!=null?w.coboundary:"null");

			if(w == null || w.coboundary.coefficientOf(s) == 0) {
				// We are starting an interval.
				ListItem nextItem = new ListItem();
				nextItem.cocycle = new Chain(prime,1,s);
				nextItem.coboundary = s.chain();
				nextItem.findex = s.findex();
				bases.addFirst(nextItem);
				if(DEBUGGING)
					System.out.printf("\tAdded to bases:\n\t\tcocycle:\t%s\n\t\tcoboundary:\t%s\n\t\tfindex:\t%d\n",
							nextItem.cocycle, nextItem.coboundary, nextItem.findex);
			} else { 
				assert(w.coboundary.coefficientOf(s) != 0);
				// We are finishing an interval.
				// There is a z* which is the first to contain s. Also, idx points to z* and the 
				// current value of dz*.

				// Remove w from bases, and then adjust all the rest.
				wIt.remove();

				int dzstarC = w.coboundary.coefficientOf(s);

				while(wIt.hasNext()) {
					ListItem z = wIt.next();
					Chain zc = z.cocycle;
					Chain dz = z.coboundary;
					if(DEBUGGING)
						System.out.printf("\tConsidering %s and %s\n", zc, dz);
					if(dz.coefficientOf(s)!=0) {
						if(DEBUGGING)
							System.out.printf("\tAdding to %s and %s:\n\t%s and %s\n", zc, dz,
									w.cocycle, w.coboundary); 

						z.cocycle = zc.add(w.cocycle,(pInverses[dzstarC]*(prime-dz.coefficientOf(s)))%prime);
						z.coboundary = dz.add(w.coboundary,(pInverses[dzstarC]*(prime-dz.coefficientOf(s)))%prime);
						if(DEBUGGING)
							System.out.printf("\tResult of addition:\n\t%s and %s\n", z.cocycle, z.coboundary);
					}
				}

				// Now, construct the persistence interval
				if(w.findex != s.findex() && w.cocycle.maxS().dimension() < stream.maxDimension()) {
					PersistenceBasisInterval.Int pi = new PersistenceBasisInterval.Int(w.cocycle,w.findex,s.findex());
					intervals.add(pi);
				}
			}
		}
		if(DEBUGGING)
			System.out.printf("# infinite bases: %d\n", bases.size());
		ListIterator<ListItem> wIt = bases.listIterator();
		while(wIt.hasNext()) {
			ListItem nextItem = wIt.next();
			if(nextItem.cocycle.maxS().dimension() < stream.maxDimension()) {
				PersistenceBasisInterval.Int pi = new PersistenceBasisInterval.Int(nextItem.cocycle, nextItem.findex);
				intervals.add(pi);
			}
		}

		Vector<PersistenceBasisInterval> rets = new Vector<PersistenceBasisInterval>();
		for(PersistenceBasisInterval.Int pi : intervals) {
			PersistenceBasisInterval.Float newpi = stream.convertInterval(pi);
			if(newpi.start != newpi.end)
				rets.add(newpi);
		}

		PersistenceBasisInterval[] retpi = new PersistenceBasisInterval[rets.size()];
		int i = 0;
		for(PersistenceBasisInterval pi : rets) {
			retpi[i++] = pi;
		}

		return retpi;
	}

	public static PersistenceBasisInterval[] computePersistentCohomologyZigZag(SimplexStream stream) {
		return computePersistentCohomologyZigZag(stream,11);
	}
}
