package edu.stanford.math.plex4.homology.zigzag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.stanford.math.plex4.homology.barcodes.IntBarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.streams.impl.VietorisRipsStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.utility.RandomUtility;
import edu.stanford.math.primitivelib.algebraic.impl.ModularIntField;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;

public class RipsBootstrapper {
	private final double[][] points;
	private final List<int[]> indexSelections;
	IntAbstractField intField = ModularIntField.getInstance(2);
	
	private final double maxDistance;
	private final int maxDimension;
	
	public RipsBootstrapper(double[][] points, double maxDistance, int maxDimension, List<int[]> indexSelections) {
		this.points = points;
		this.indexSelections = indexSelections;
		this.maxDimension = maxDimension;
		this.maxDistance = maxDistance;
	}

	public RipsBootstrapper(double[][] points, double maxDistance, int maxDimension, int numSelections, int selectionSize) {
		this.points = points;
		this.indexSelections = new ArrayList<int[]>();
		this.maxDimension = maxDimension;
		this.maxDistance = maxDistance;
		
		selectionSize = Math.min(points.length, selectionSize);
		
		for (int selection = 0; selection < numSelections; selection++) {
			int[] set = RandomUtility.randomSubset(selectionSize, points.length).toArray();
			set = makeMonotone(set);
			this.indexSelections.add(set);
		}
	}
	
	public IntBarcodeCollection performBootstrap() {
		HomologyBasisTracker<Simplex> basisTracker = new HomologyBasisTracker<Simplex>(intField, SimplexComparator.getInstance());
		int increment = 1;
		
		int[] i_indices = this.indexSelections.get(0);
		VietorisRipsStream<double[]> X_i = new VietorisRipsStream<double[]>(new EuclideanMetricSpace(getSubset(points, i_indices)), maxDistance, maxDimension + 1, i_indices);
		X_i.finalizeStream();
		
		List<Simplex> X_i_set = dump(X_i);
		Collections.sort(X_i_set, SimplexComparator.getInstance());
		
		//basisTracker.setBasisComparator(new FilteredComparator<Simplex>(X_i, SimplexComparator.getInstance()));
		
		for (Simplex simplex: X_i_set) {
			basisTracker.add(simplex, 0);
		}
		
		for (int j = 1; j < this.indexSelections.size(); j++) {
			int[] j_indices = this.indexSelections.get(j);
			int[] ij_indices = union(i_indices, j_indices);
			
			VietorisRipsStream<double[]> X_j = new VietorisRipsStream<double[]>(new EuclideanMetricSpace(getSubset(points, j_indices)), maxDistance, maxDimension + 1, j_indices);
			VietorisRipsStream<double[]> X_ij = new VietorisRipsStream<double[]>(new EuclideanMetricSpace(getSubset(points, ij_indices)), maxDistance, maxDimension + 1, ij_indices);
			X_j.finalizeStream();
			X_ij.finalizeStream();
			
			//List<Simplex> X_j_set = dump(X_j);
			//Collections.sort(X_j_set, SimplexComparator.getInstance());
			
			//List<Simplex> X_ij_set = dump(X_ij);
			//Collections.sort(X_ij_set, SimplexComparator.getInstance());
			
			
			//basisTracker.setBasisComparator(new FilteredComparator<Simplex>(X_ij, SimplexComparator.getInstance()));
			
			List<Simplex> X_ij_sub_X_i = getDifference(X_ij, X_i);
			List<Simplex> X_ij_sub_X_j = getDifference(X_ij, X_j);
			
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
		
		return basisTracker.getBarcodes().filterByMaxDimension(maxDimension);
	}
	
	private static <U> List<U> dump(AbstractFilteredStream<U> X) {
		List<U> list = new ArrayList<U>();
		
		for (U x: X) {
			list.add(x);
		}
		
		return list;
	}
	
	private static <U> List<U> getDifference(AbstractFilteredStream<U> X, AbstractFilteredStream<U> A) {
		List<U> list = new ArrayList<U>();
		
		for (U x: X) {
			list.add(x);
		}
		
		for (U a: A) {
			list.remove(a);
		}
		
		return list;
	}
	
	private static double[][] getSubset(double[][] points, int[] indices) {
		double[][] result = new double[indices.length][];
		
		for (int i = 0; i < indices.length; i++) {
			result[i] = points[indices[i]];
		}
		
		return result;
	}
	
	private static boolean isMonotoneIncreasing(int[] a) {
		for (int i = 1; i < a.length; i++) {
			if (a[i] <= a[i - 1]) {
				return false;
			}
		}
		return true;
	}
	
	private static int[] makeMonotone(int[] a) {
		int[] temp = Arrays.copyOf(a, a.length);
		Arrays.sort(temp);
		int k = 0, i = 0;
		int[] result = new int[temp.length];
		while (i < temp.length && k < result.length) {
			if (k > 0 && result[k - 1] == temp[i]) {
				i++;
				continue;
			}
			result[k] = temp[i];
			i++;
			k++;
		}
		return Arrays.copyOf(result, k);
	}
	
	private static int[] union(int[] a, int[] b) {
		int i = 0, j = 0, k = 0;

		int[] temp = new int[a.length + b.length];
		
		while (i < a.length && j < b.length) {
			if (a[i] < b[j]) {
				temp[k] = a[i];
				k++;
				i++;
			} else if (a[i] > b[j]) {
				temp[k] = b[j];
				k++;
				j++;
			} else {
				temp[k] = a[i];
				i++;
				j++;
				k++;
			}
		}
		
		while (i < a.length) {
			temp[k] = a[i];
			i++;
			k++;
		}
		
		while (j < b.length) {
			temp[k] = b[j];
			j++;
			k++;
		}
		
		return Arrays.copyOf(temp, k);
	}
}
