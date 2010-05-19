/**
 * 
 */
package edu.stanford.math.plex_plus.homology.simplex_streams;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.stanford.math.plex_plus.graph.UndirectedListGraph;
import edu.stanford.math.plex_plus.homology.simplex.FiltrationSimplex;
import edu.stanford.math.plex_plus.homology.simplex.Simplex;
import edu.stanford.math.plex_plus.homology.utility.HomologyUtility;
import edu.stanford.math.plex_plus.math.metric.interfaces.GenericAbstractFiniteMetricSpace;
import edu.stanford.math.plex_plus.utility.Infinity;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.hash.TObjectDoubleHashMap;
import gnu.trove.set.hash.TIntHashSet;

/**
 * @author Andrew Tausz
 *
 */
public class VietorisRipsStream<T> implements SimplexStream {
	private final GenericAbstractFiniteMetricSpace<T> metricSpace;
	private final double maxDistance;
	private final int numDivisions;
	private final UndirectedListGraph neighborhoodGraph;
	
	private final SortedSet<FiltrationSimplex> edges = new TreeSet<FiltrationSimplex>();
	private final TObjectDoubleHashMap<Simplex> filtrationValues = new TObjectDoubleHashMap<Simplex>();
	
	public VietorisRipsStream(GenericAbstractFiniteMetricSpace<T> metricSpace, int numDivisions, double maxDistance) {
		this.metricSpace = metricSpace;
		this.maxDistance = maxDistance;
		this.numDivisions = numDivisions;
		this.neighborhoodGraph = new UndirectedListGraph(this.metricSpace.size());
		
		this.constructNeighborhoodGraph();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		/*
		for (Simplex simplex : this) {
			builder.append(simplex.toString());
			builder.append('\n');
		}
		*/
		
		VietorisRipsIterator iterator = new VietorisRipsIterator(this);
		while (iterator.hasNext()) {
			builder.append(iterator.next());
			builder.append('\n');
		}
		
		return builder.toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Simplex> iterator() {
		return new VietorisRipsIterator(this);
	}
	
	private void constructNeighborhoodGraph() {
		int n = this.metricSpace.size();
		TIntHashSet neighborhood = null;
			
		for (int i = 0; i < n; i++) {
			// obtain the neighborhood of the i-th point
			neighborhood = this.metricSpace.getNeighborhood(metricSpace.getPoint(i), this.maxDistance);
			
			// get the pairwise distances of the points and store them
			TIntIterator iterator = neighborhood.iterator();
			while (iterator.hasNext()) {
				int j = iterator.next();
				
				if (i == j) {
					continue;
				}
				
				double distance = this.metricSpace.distance(i, j);
				Simplex simplex = new Simplex(new int[]{i, j});
				edges.add(new FiltrationSimplex(simplex, distance));
				filtrationValues.put(simplex, distance);
				this.neighborhoodGraph.addEdge(i, j);
			}
		}
	}

	@Override
	public double getFiltrationIndex(Simplex simplex) {
		if (simplex.getDimension() == 0) {
			return 0;
		} else if (simplex.getDimension() == 1) {
			return this.filtrationValues.get(simplex);
		} else {
			int[] vertices = simplex.getVertices();
			double maxFiltrationValue = Infinity.getNegativeInfinity();
			double currentFiltrationValue;
			
			for (int i = 0; i < vertices.length; i++) {
				for (int j = i + 1; j < vertices.length; j++) {
					currentFiltrationValue = this.filtrationValues.get(new Simplex(new int[]{vertices[i], vertices[j]}));
					if (currentFiltrationValue > maxFiltrationValue) {
						maxFiltrationValue = currentFiltrationValue;
					}
				}
			}
			
			return maxFiltrationValue;
		}
	}
	
	public class VietorisRipsIterator implements Iterator<Simplex> {
		private final VietorisRipsStream stream;
		private int currentDimension = 0;
		private int localIndex = -1;
		
		private Iterator<FiltrationSimplex> simplexIterator; 
		
		private SortedSet<FiltrationSimplex> kSimplicies = new TreeSet<FiltrationSimplex>();
		
		public VietorisRipsIterator(VietorisRipsStream stream) {
			this.stream = stream;
			this.simplexIterator = stream.edges.iterator();
		}
		
		@Override
		public boolean hasNext() {
			if (this.currentDimension == 0) {
				if (this.localIndex < this.stream.metricSpace.size() - 1) {
					return true;
				} else {
					return !this.stream.edges.isEmpty();
				}
			} else {
				// currentDimension >= 1
				if (this.simplexIterator.hasNext()) {
					return true;
				} else {
					this.expandNextDimension(this.currentDimension + 1);
					return !this.kSimplicies.isEmpty();
				}
			}
		}

		@Override
		public Simplex next() {
			if (this.currentDimension == 0) {
				this.localIndex++;
				if (this.localIndex < this.stream.metricSpace.size()) {
					return new Simplex(new int[]{this.localIndex});
				} else {
					// advance to dimension 1
					this.currentDimension = 1;
					this.simplexIterator = this.stream.edges.iterator();
					return this.simplexIterator.next().getSimplex();					
				}
			} else {
				// currentDimension >= 1
				
				if (this.simplexIterator.hasNext()) {
					return this.simplexIterator.next().getSimplex();
				} else {
					// advance to next dimension
					this.currentDimension++;
					// Already expanded new level of simplicies in hasNext!
					this.simplexIterator = this.kSimplicies.iterator();
					return this.simplexIterator.next().getSimplex();
				}
			}
		}

		private void expandNextDimension(int newDimension) {
			SortedSet<FiltrationSimplex> newSimplicies = new TreeSet<FiltrationSimplex>();
			SortedSet<FiltrationSimplex> sourceSimplices = null;
			
			if (newDimension == 2) {
				sourceSimplices = this.stream.edges;
			} else {
				sourceSimplices = this.kSimplicies;
			}
			
			for (FiltrationSimplex tau : sourceSimplices) {
				int[] vertices = tau.getVertices();
				TIntHashSet N = null;
				for (int vertex : vertices) {
					if (N == null) {
						N = this.stream.neighborhoodGraph.getLowerNeighbors(vertex);
					} else {
						this.intersectSets(N, this.stream.neighborhoodGraph.getLowerNeighbors(vertex));
					}
				}
				
				TIntIterator NIterator = N.iterator();
				while (NIterator.hasNext()) {
					int v = NIterator.next();
					int[] newVertices = HomologyUtility.appendToArray(vertices, v);
					Simplex newSimplex = new Simplex(newVertices);
					double newFiltrationValue = this.stream.getFiltrationIndex(newSimplex);
					newSimplicies.add(new FiltrationSimplex(newSimplex, newFiltrationValue));
				}
			}
			
			this.kSimplicies = newSimplicies;
		}
		
		private void intersectSets(TIntHashSet destination, TIntHashSet other) {
			TIntIterator iterator = destination.iterator();
			while (iterator.hasNext()) {
				int currentValue = iterator.next();
				if (!other.contains(currentValue)) {
					iterator.remove();
				}
			}
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
}
