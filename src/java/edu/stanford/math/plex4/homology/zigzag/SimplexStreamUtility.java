package edu.stanford.math.plex4.homology.zigzag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPair;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPairComparator;
import edu.stanford.math.plex4.streams.impl.ExplicitSimplexStream;
import edu.stanford.math.plex4.streams.impl.ExplicitStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;

public class SimplexStreamUtility {
	static <U> List<U> getDifference(Iterable<U> X, Iterable<U> A) {
		Set<U> list = new HashSet<U>();

		for (U x: X) {
			list.add(x);
		}

		for (U a: A) {
			list.remove(a);
		}

		return new ArrayList<U>(list);
	}

	static <U> List<U> getSortedList(Iterable<U> X, Comparator<U> comparator) {
		List<U> list = new ArrayList<U>();

		for (U x: X) {
			list.add(x);
		}

		Collections.sort(list, comparator);

		return list;
	}

	static Simplex getFirstVertex(Iterable<Simplex> X) {
		for (Simplex x: X) {
			if (x.getDimension() == 0) {
				return x;
			}
		}

		return null;
	}

	static List<SimplexPair> embedFirst(Iterable<Simplex> X, Simplex y) {
		List<SimplexPair> list = new ArrayList<SimplexPair>();

		for (Simplex x: X) {
			list.add(SimplexPair.createPair(x, y));
		}

		return list;
	}

	static List<SimplexPair> embedSecond(Simplex x, Iterable<Simplex> Y) {
		List<SimplexPair> list = new ArrayList<SimplexPair>();

		for (Simplex y: Y) {
			list.add(SimplexPair.createPair(x, y));
		}

		return list;
	}
	
	static Iterable<SimplexPair> embedFirstOnline(final Iterable<Simplex> X, final Simplex y) {
		return new Iterable<SimplexPair>() {

			public Iterator<SimplexPair> iterator() {
				return new Iterator<SimplexPair>() {
					private final Iterator<Simplex> internalIterator = X.iterator();
					
					public boolean hasNext() {
						return internalIterator.hasNext();
					}

					public SimplexPair next() {
						Simplex x = internalIterator.next();
						return SimplexPair.createPair(x, y);
					}

					public void remove() {
						internalIterator.remove();
					}
				};
			}
		};
	}
	
	static Iterable<SimplexPair> embedSecondOnline(final Simplex x, final Iterable<Simplex> Y) {
		return new Iterable<SimplexPair>() {

			public Iterator<SimplexPair> iterator() {
				return new Iterator<SimplexPair>() {
					private final Iterator<Simplex> internalIterator = Y.iterator();
					
					public boolean hasNext() {
						return internalIterator.hasNext();
					}

					public SimplexPair next() {
						Simplex y = internalIterator.next();
						return SimplexPair.createPair(x, y);
					}

					public void remove() {
						internalIterator.remove();
					}
				};
			}
		};
	}

	static List<SimplexPair> createProductList(Iterable<Simplex> X, Iterable<Simplex> Y) {
		List<SimplexPair> list = new ArrayList<SimplexPair>();

		for (Simplex x: X) {
			for (Simplex y: Y) {
				list.add(SimplexPair.createPair(x, y));
			}
		}

		Collections.sort(list, SimplexPairComparator.getInstance());

		return list;
	}

	static AbstractFilteredStream<SimplexPair> createProductStream(AbstractFilteredStream<Simplex> X, AbstractFilteredStream<Simplex> Y) {
		ExplicitStream<SimplexPair> product = new ExplicitStream<SimplexPair>(SimplexPairComparator.getInstance());

		for (Simplex x: X) {
			for (Simplex y: Y) {
				product.addElement(SimplexPair.createPair(x, y), Math.max(X.getFiltrationIndex(x), Y.getFiltrationIndex(y)));
			}
		}

		return product;
	}

	static AbstractFilteredStream<Simplex> projectFirst(AbstractFilteredStream<SimplexPair> Z) {
		ExplicitSimplexStream projection = new ExplicitSimplexStream();

		for (SimplexPair pair: Z) {
			if (projection.containsElement(pair.getFirst())) {
				continue;
			}
			projection.addElement(pair.getFirst(), Z.getFiltrationIndex(pair));
		}

		projection.finalizeStream();
		projection.ensureAllFaces();

		return projection;
	}

	static AbstractFilteredStream<Simplex> projectSecond(AbstractFilteredStream<SimplexPair> Z) {
		ExplicitSimplexStream projection = new ExplicitSimplexStream();

		for (SimplexPair pair: Z) {
			if (projection.containsElement(pair.getSecond())) {
				continue;
			}
			projection.addElement(pair.getSecond(), Z.getFiltrationIndex(pair));
		}

		projection.finalizeStream();
		projection.ensureAllFaces();

		return projection;
	}
	
	/**
	 * This function returns the simplicial complex induced by the given collection of simplices. In other
	 * words, it produces the minimal simplicial complex containing the set of simplices.
	 * 
	 * @param simplices
	 * @return
	 */
	static Iterable<Simplex> getInducedStream(Iterable<Simplex> simplices) {
		
		ExplicitSimplexStream stream = new ExplicitSimplexStream();
		
		for (Simplex simplex: simplices) {
			stream.addElement(simplex, 0);
		}
		
		stream.finalizeStream();
		stream.ensureAllFaces();
		
		return stream;
	}
	
	public static void sortAscending(List<Simplex> list) {
		Collections.sort(list, SimplexComparator.getInstance());
	}
	
	public static void sortDescending(List<Simplex> list) {
		Collections.sort(list, SimplexComparator.getInstance());
		Collections.reverse(list);
	}
	
	public static void sortAscendingPairs(List<SimplexPair> list) {
		Collections.sort(list, SimplexPairComparator.getInstance());
	}
	
	public static void sortDescendingPairs(List<SimplexPair> list) {
		Collections.sort(list, SimplexPairComparator.getInstance());
		Collections.reverse(list);
	}
}
