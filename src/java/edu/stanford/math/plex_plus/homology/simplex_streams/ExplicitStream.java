/**
 * 
 */
package edu.stanford.math.plex_plus.homology.simplex_streams;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.stanford.math.plex_plus.homology.simplex.FiltrationSimplex;
import edu.stanford.math.plex_plus.homology.simplex.Simplex;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.map.hash.TObjectDoubleHashMap;

/**
 * @author atausz
 *
 */
public class ExplicitStream implements SimplexStream {
	private final SortedSet<FiltrationSimplex> simplices = new TreeSet<FiltrationSimplex>();
	private final TObjectDoubleHashMap<Simplex> filtrationIndices = new TObjectDoubleHashMap<Simplex>();
	
	public void addSimplex(int[] vertices, double filtrationIndex) {
		ExceptionUtility.verifyNonNull(vertices);
		this.simplices.add(new FiltrationSimplex(vertices, filtrationIndex));
		this.filtrationIndices.put(new Simplex(vertices), filtrationIndex);
	}
	
	public boolean verifyConsistency() {
		// TODO: Complete
		return true;
	}	
	
	@Override
	public Iterator<Simplex> iterator() {
		return new ExplicitStreamIterator(this);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for (FiltrationSimplex simplex : this.simplices) {
			builder.append(simplex.toString());
			builder.append('\n');
		}
		
		return builder.toString();
	}

	@Override
	public double getFiltrationIndex(Simplex simplex) {
		return this.filtrationIndices.get(simplex);
	}
	
	public class ExplicitStreamIterator implements Iterator<Simplex> {
		private final Iterator<FiltrationSimplex> internalIterator;
		
		public ExplicitStreamIterator(ExplicitStream stream) {
			this.internalIterator = stream.simplices.iterator();
		}
		
		@Override
		public boolean hasNext() {
			return this.internalIterator.hasNext();
		}

		@Override
		public Simplex next() {
			return this.internalIterator.next().getSimplex();
		}

		@Override
		public void remove() {
			this.internalIterator.remove();
		}
		
	}
}
