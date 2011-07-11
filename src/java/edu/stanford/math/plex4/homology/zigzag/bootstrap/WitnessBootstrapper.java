package edu.stanford.math.plex4.homology.zigzag.bootstrap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.stanford.math.plex4.homology.barcodes.AnnotatedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPair;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPairComparator;
import edu.stanford.math.plex4.homology.zigzag.IntervalTracker;
import edu.stanford.math.plex4.homology.zigzag.SimpleHomologyBasisTracker;
import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.metric.landmark.RandomLandmarkSelector;
import edu.stanford.math.plex4.streams.impl.WitnessBicomplex;
import edu.stanford.math.plex4.streams.impl.WitnessStream;
import edu.stanford.math.primitivelib.algebraic.impl.ModularIntField;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;

public class WitnessBootstrapper<T> {
	protected final AbstractSearchableMetricSpace<T> metricSpace;
	protected final List<LandmarkSelector<T>> indexSelections;
	protected final IntAbstractField intField = ModularIntField.getInstance(2);
	protected final IntAlgebraicFreeModule<Simplex> chainModule = new IntAlgebraicFreeModule<Simplex>(this.intField);
	protected final IntAlgebraicFreeModule<SimplexPair> Z_chainModule = new IntAlgebraicFreeModule<SimplexPair>(this.intField);
	
	protected final int maxDimension;
	protected final double maxDistance;

	public WitnessBootstrapper(AbstractSearchableMetricSpace<T> metricSpace, List<LandmarkSelector<T>> indexSelections, int maxDimension, double maxDistance) {
		this.metricSpace = metricSpace;
		this.indexSelections = indexSelections;
		this.maxDimension = maxDimension;
		this.maxDistance = maxDistance;
	}
	
	public LandmarkSelector<T> getLandmarkSelector(int index) {
		return this.indexSelections.get(index);
	}

	public WitnessBootstrapper(AbstractSearchableMetricSpace<T> metricSpace, double maxDistance, int maxDimension, int numSelections, int selectionSize) {
		this.metricSpace = metricSpace;
		this.indexSelections = new ArrayList<LandmarkSelector<T>>();
		this.maxDimension = maxDimension;
		this.maxDistance = maxDistance;

		selectionSize = Math.min(metricSpace.size(), selectionSize);

		for (int selection = 0; selection < numSelections; selection++) {
			LandmarkSelector<T> selector = new RandomLandmarkSelector<T>(metricSpace, selectionSize);
			this.indexSelections.add(selector);
		}
	}

	public BarcodeCollection<Integer> performProjectionBootstrap() {
		return performProjectionBootstrap(null);
	}
	
	public BarcodeCollection<Integer> performProjectionBootstrap(int[] expectedBettiNumbers) {
		WitnessStream<T> X_stream = new WitnessStream<T>(this.metricSpace, indexSelections.get(0), maxDimension + 1, maxDistance, indexSelections.get(0).getLandmarkPoints());
		X_stream.setPlex3Compatbility(false);
		X_stream.finalizeStream();

		WitnessStream<T> Y_stream = null;

		IntervalTracker<Integer, Integer, IntSparseFormalSum<Simplex>> result = null;

		SimpleHomologyBasisTracker<SimplexPair> ZTracker = null;
		SimpleHomologyBasisTracker<Simplex> XTracker = null;
		SimpleHomologyBasisTracker<Simplex> YTracker = null;
		
		for (int j = 1; j < this.indexSelections.size(); j++) {
			if (XTracker == null) {
				XTracker = new SimpleHomologyBasisTracker<Simplex>(intField, SimplexComparator.getInstance(), 0, maxDimension);
				XTracker.getIntervalTracker().setUseRightClosedIntervals(false);
				XTracker.getIntervalTracker().setMaxDimension(maxDimension);
				for (Simplex x: X_stream) {
					XTracker.add(x, X_stream.getFiltrationIndex(x));
				}
				
				System.out.println("Barcodes for X_" + (j - 1));
				AnnotatedBarcodeCollection<Integer, IntSparseFormalSum<Simplex>> XBarcodes = XTracker.getAnnotatedBarcodes();
				
				if (expectedBettiNumbers != null && !Arrays.equals(XBarcodes.getBettiSequence(), expectedBettiNumbers)) {
					this.indexSelections.set(0, new RandomLandmarkSelector<T>(metricSpace, this.indexSelections.get(0).getLandmarkPoints().length));
					X_stream = new WitnessStream<T>(this.metricSpace, indexSelections.get(0), maxDimension + 1, maxDistance, indexSelections.get(0).getLandmarkPoints());
					j--;
					continue;
				}
				System.out.println(XBarcodes.toString());
			}
			
			AnnotatedBarcodeCollection<Integer, IntSparseFormalSum<Simplex>> YBarcodes = null;
			
			if (expectedBettiNumbers != null) {
				boolean found = false;
				
				while (!found) {
					Y_stream = new WitnessStream<T>(this.metricSpace, indexSelections.get(j), maxDimension + 1, maxDistance, indexSelections.get(j).getLandmarkPoints());
					Y_stream.setPlex3Compatbility(false);
					Y_stream.finalizeStream();

					YTracker = new SimpleHomologyBasisTracker<Simplex>(intField, SimplexComparator.getInstance(), 0, maxDimension);
					YTracker.getIntervalTracker().setUseRightClosedIntervals(false);
					YTracker.getIntervalTracker().setMaxDimension(maxDimension);
					for (Simplex y: Y_stream) {
						YTracker.add(y, Y_stream.getFiltrationIndex(y));
					}

					YBarcodes = YTracker.getAnnotatedBarcodes();
					
					if (Arrays.equals(YBarcodes.getInfiniteIntervals().getBettiSequence(), expectedBettiNumbers)) {
						found = true;
					} else {
						System.out.println("Barcodes " + Arrays.toString(YBarcodes.getInfiniteIntervals().getBettiSequence()) + " do not match the expected ones: " + Arrays.toString(expectedBettiNumbers));
						LandmarkSelector<T> selector = new RandomLandmarkSelector<T>(metricSpace, this.indexSelections.get(j).size());
						this.indexSelections.set(j, selector);
					}
				}
			} else {
				Y_stream = new WitnessStream<T>(this.metricSpace, indexSelections.get(j), maxDimension + 1, maxDistance, indexSelections.get(j).getLandmarkPoints());
				Y_stream.setPlex3Compatbility(false);
				Y_stream.finalizeStream();

				YTracker = new SimpleHomologyBasisTracker<Simplex>(intField, SimplexComparator.getInstance(), 0, maxDimension);
				YTracker.getIntervalTracker().setUseRightClosedIntervals(false);
				YTracker.getIntervalTracker().setMaxDimension(maxDimension);
				for (Simplex y: Y_stream) {
					YTracker.add(y, Y_stream.getFiltrationIndex(y));
				}

				YBarcodes = YTracker.getAnnotatedBarcodes();
			}
			
			
			WitnessBicomplex<T> Z_stream = new WitnessBicomplex<T>(X_stream, Y_stream, maxDimension);
			Z_stream.finalizeStream();
			Z_stream.ensureAllFaces();

			ZTracker = new SimpleHomologyBasisTracker<SimplexPair>(intField, SimplexPairComparator.getInstance(), 0, maxDimension);
			ZTracker.getIntervalTracker().setUseRightClosedIntervals(false);
			ZTracker.getIntervalTracker().setMaxDimension(maxDimension);
			for (SimplexPair z: Z_stream) {
				ZTracker.add(z, Z_stream.getFiltrationIndex(z));
			}
			
			//System.out.println("Barcodes for X_" + (j-1) + "," + (j));
			//AnnotatedBarcodeCollection<Integer, IntSparseFormalSum<SimplexPair>> ZBarcodes = ZTracker.getAnnotatedBarcodes();
			//System.out.println(ZBarcodes.toString());
			
			
			System.out.println("Barcodes for X_" + (j));
			System.out.println(YBarcodes.toString());
			
			
			if (result == null) {
				result = XTracker.getStateWithoutFiniteBarcodes(j - 1);
				result.setUseLeftClosedIntervals(true);
				result.setUseRightClosedIntervals(true);
			}
			
			result = InducedHomologyMappingUtility.project(XTracker, ZTracker, YTracker, result, chainModule, Z_chainModule, (j - 1), j);

			X_stream = Y_stream;
			XTracker = YTracker;
		}
		
		result.endAllIntervals(this.indexSelections.size() - 1);
		
		return BarcodeCollection.forgetGeneratorType(result.getAnnotatedBarcodes().filterByMaxDimension(maxDimension));
	}
	
	
}
