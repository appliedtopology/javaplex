package edu.stanford.math.plex4.homology.zigzag.bootstrap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.zigzag.HomologyBasisTracker;
import edu.stanford.math.plex4.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.streams.impl.VietorisRipsStream;
import edu.stanford.math.plex4.utility.ArrayUtility;
import edu.stanford.math.plex4.utility.CollectionUtility;
import edu.stanford.math.plex4.utility.RandomUtility;
import edu.stanford.math.primitivelib.algebraic.impl.ModularIntField;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;

public class VietorisRipsBootstrapper {
	private final double[][] points;
	private final List<int[]> indexSelections;
	IntAbstractField intField = ModularIntField.getInstance(2);
	
	private final double maxDistance;
	private final int maxDimension;
	
	public VietorisRipsBootstrapper(double[][] points, double maxDistance, int maxDimension, List<int[]> indexSelections) {
		this.points = points;
		this.indexSelections = indexSelections;
		this.maxDimension = maxDimension;
		this.maxDistance = maxDistance;
	}

	public VietorisRipsBootstrapper(double[][] points, double maxDistance, int maxDimension, int numSelections, int selectionSize) {
		this.points = points;
		this.indexSelections = new ArrayList<int[]>();
		this.maxDimension = maxDimension;
		this.maxDistance = maxDistance;
		
		selectionSize = Math.min(points.length, selectionSize);
		
		for (int selection = 0; selection < numSelections; selection++) {
			int[] set = RandomUtility.randomSubset(selectionSize, points.length).toArray();
			set = ArrayUtility.makeMonotone(set);
			this.indexSelections.add(set);
		}
	}
	
	public BarcodeCollection<Integer> performBootstrap() {
		HomologyBasisTracker<Simplex> basisTracker = new HomologyBasisTracker<Simplex>(intField, SimplexComparator.getInstance());
		int increment = 1;
		
		int[] i_indices = this.indexSelections.get(0);
		VietorisRipsStream<double[]> X_i = new VietorisRipsStream<double[]>(new EuclideanMetricSpace(ArrayUtility.getSubset(points, i_indices)), maxDistance, maxDimension + 1, i_indices);
		X_i.finalizeStream();
		
		List<Simplex> X_i_set = CollectionUtility.dump(X_i);
		Collections.sort(X_i_set, SimplexComparator.getInstance());
	
		for (Simplex simplex: X_i_set) {
			basisTracker.add(simplex, 0);
		}
		
		for (int j = 1; j < this.indexSelections.size(); j++) {
			int[] j_indices = this.indexSelections.get(j);
			int[] ij_indices = ArrayUtility.union(i_indices, j_indices);
			
			VietorisRipsStream<double[]> X_j = new VietorisRipsStream<double[]>(new EuclideanMetricSpace(ArrayUtility.getSubset(points, j_indices)), maxDistance, maxDimension + 1, j_indices);
			VietorisRipsStream<double[]> X_ij = new VietorisRipsStream<double[]>(new EuclideanMetricSpace(ArrayUtility.getSubset(points, ij_indices)), maxDistance, maxDimension + 1, ij_indices);
			X_j.finalizeStream();
			X_ij.finalizeStream();
			
			//List<Simplex> X_j_set = dump(X_j);
			//Collections.sort(X_j_set, SimplexComparator.getInstance());
			
			//List<Simplex> X_ij_set = dump(X_ij);
			//Collections.sort(X_ij_set, SimplexComparator.getInstance());
			
			
			//basisTracker.setBasisComparator(new FilteredComparator<Simplex>(X_ij, SimplexComparator.getInstance()));
			
			List<Simplex> X_ij_sub_X_i = CollectionUtility.getDifference(X_ij, X_i);
			List<Simplex> X_ij_sub_X_j = CollectionUtility.getDifference(X_ij, X_j);
			
			Collections.sort(X_ij_sub_X_i, SimplexComparator.getInstance());
			Collections.sort(X_ij_sub_X_j, SimplexComparator.getInstance());
			Collections.reverse(X_ij_sub_X_j);
			
			for (Simplex simplex: X_ij_sub_X_i) {
				basisTracker.add(simplex, increment * j);
			}
			
			for (Simplex simplex: X_ij_sub_X_j) {
				basisTracker.remove(simplex, increment * j);
			}
			
			X_i = X_j;
			i_indices = j_indices;
		}
		
		return BarcodeCollection.forgetGeneratorType(basisTracker.getAnnotatedBarcodes().filterByMaxDimension(maxDimension));
	}
}
