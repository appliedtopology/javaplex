package edu.stanford.math.plex4.homology.nonautogen;

import java.util.Set;

import edu.stanford.math.plex4.homology.barcodes.IntAnnotatedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceBasisAlgorithm;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import gnu.trove.THashSet;
import gnu.trove.TObjectIntHashMap;
import gnu.trove.TObjectIntIterator;

public class ZeroDimHomology implements AbstractPersistenceBasisAlgorithm<Simplex, Simplex> {

	public IntAnnotatedBarcodeCollection<Simplex> computeAnnotatedIntervals(AbstractFilteredStream<Simplex> stream) {
		THashSet<Simplex> vertex_set = new THashSet<Simplex>();
		IntAnnotatedBarcodeCollection<Simplex> collection = new IntAnnotatedBarcodeCollection<Simplex>();

		for (Simplex element: stream) {
			if (stream.getDimension(element) == 0) {
				vertex_set.add(element);
			} else if (stream.getDimension(element) == 1) {
				Simplex[] boundaryElements = stream.getBoundary(element);
				if (vertex_set.contains(boundaryElements[0])) {
					vertex_set.remove(boundaryElements[0]);
					int filtration_index_i = stream.getFiltrationIndex(boundaryElements[0]);
					int filtration_index_j = stream.getFiltrationIndex(boundaryElements[1]);
					if (filtration_index_i != filtration_index_j) {
						collection.addInterval(0, filtration_index_i, filtration_index_j, boundaryElements[0]);
					}
				} else {
					vertex_set.remove(boundaryElements[1]);
				}
			}
		}
		
		for (Simplex element: vertex_set) {
			collection.addRightInfiniteInterval(0, stream.getFiltrationIndex(element), element);
		}
		
		return collection;
	}

	
	
	public IntBarcodeCollection computeIntervals(AbstractFilteredStream<Simplex> stream) {
		THashSet<Simplex> vertex_set = new THashSet<Simplex>();
		IntBarcodeCollection collection = new IntBarcodeCollection();
		
		for (Simplex element: stream) {
			if (stream.getDimension(element) == 0) {
				vertex_set.add(element);
			} else if (stream.getDimension(element) == 1) {
				Simplex[] boundaryElements = stream.getBoundary(element);
				if (vertex_set.contains(boundaryElements[0])) {
					vertex_set.remove(boundaryElements[0]);
					int start_filtration_index = stream.getFiltrationIndex(boundaryElements[0]);
					int end_filtration_index = stream.getFiltrationIndex(element);
					if (start_filtration_index != end_filtration_index) {
						collection.addInterval(0, start_filtration_index, end_filtration_index);
					}
				} else {
					//vertex_set.remove(boundaryElements[1]);
				}
			}
		}
		
		for (Simplex element: vertex_set) {
			collection.addRightInfiniteInterval(0, stream.getFiltrationIndex(element));
		}
		
		return collection;
	}

	
	public IntBarcodeCollection computeIntervals2(AbstractFilteredStream<Simplex> stream) {
		IntBarcodeCollection collection = new IntBarcodeCollection();
		
		TObjectIntHashMap<Simplex> vertexIndices = new TObjectIntHashMap<Simplex>();
		Set<Simplex> edges = new THashSet<Simplex>();
		
		int vertex_index = 0;
		for (Simplex element: stream) {
			if (stream.getDimension(element) == 0) {
				vertexIndices.put(element, vertex_index++);
			} else if (stream.getDimension(element) == 1) {
				edges.add(element);
			}
		}
		
		DisjointSetSystem components = new DisjointSetSystem(vertexIndices.size());
		int[] deathFiltrationIndices = new int[vertexIndices.size()];
		
		for (Simplex edge: edges) {
			Simplex[] boundary = stream.getBoundary(edge);
			int i_index = vertexIndices.get(boundary[0]);
			int j_index = vertexIndices.get(boundary[1]);
			components.union(i_index, j_index);
			//deathFiltrationIndices[i_index] = stream.getFiltrationIndex(edge);
			deathFiltrationIndices[j_index] = stream.getFiltrationIndex(edge);
		}
		
		for (TObjectIntIterator<Simplex> iterator = vertexIndices.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			int index = iterator.value();
			int parent = components.find(index);
			if (index == parent) {
				collection.addRightInfiniteInterval(0, stream.getFiltrationIndex(iterator.key()));
			} else {
				if (stream.getFiltrationIndex(iterator.key()) < deathFiltrationIndices[index]) {
					collection.addInterval(0, stream.getFiltrationIndex(iterator.key()), deathFiltrationIndices[index]);
				}
			}
		}
		
		return collection;
	}
}
