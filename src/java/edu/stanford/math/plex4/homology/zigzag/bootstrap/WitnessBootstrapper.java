package edu.stanford.math.plex4.homology.zigzag.bootstrap;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPair;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPairComparator;
import edu.stanford.math.plex4.homology.zigzag.HomologyBasisTracker;
import edu.stanford.math.plex4.homology.zigzag.IntervalTracker;
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
		WitnessStream<T> X_stream = new WitnessStream<T>(this.metricSpace, indexSelections.get(0), maxDimension + 1, maxDistance, indexSelections.get(0).getLandmarkPoints());
		X_stream.setPlex3Compatbility(false);
		X_stream.finalizeStream();

		WitnessStream<T> Y_stream = null;

		IntervalTracker<Integer, Integer, IntSparseFormalSum<Simplex>> result = null;

		HomologyBasisTracker<SimplexPair> ZTracker = null;
		HomologyBasisTracker<Simplex> XTracker = null;
		HomologyBasisTracker<Simplex> YTracker = null;
		
		for (int j = 1; j < this.indexSelections.size(); j++) {
			Y_stream = new WitnessStream<T>(this.metricSpace, indexSelections.get(j), maxDimension + 1, maxDistance, indexSelections.get(j).getLandmarkPoints());
			Y_stream.setPlex3Compatbility(false);
			Y_stream.finalizeStream();

			WitnessBicomplex<T> Z_stream = new WitnessBicomplex<T>(X_stream, Y_stream, maxDimension);
			Z_stream.finalizeStream();
			Z_stream.ensureAllFaces();

			if (XTracker == null) {
				XTracker = new HomologyBasisTracker<Simplex>(intField, SimplexComparator.getInstance());
				XTracker.getIntervalTracker().setUseRightClosedIntervals(false);
				for (Simplex x: X_stream) {
					XTracker.add(x, j - 1);
				}
				
				{
					System.out.println("Barcodes for X_" + (j - 1));
					System.out.println(XTracker.getAnnotatedBarcodes().filterByMaxDimension(maxDimension).toString());
				}
			}

			ZTracker = new HomologyBasisTracker<SimplexPair>(intField, SimplexPairComparator.getInstance());
			ZTracker.getIntervalTracker().setUseRightClosedIntervals(false);
			for (SimplexPair z: Z_stream) {
				ZTracker.add(z, j);
			}

			YTracker = new HomologyBasisTracker<Simplex>(intField, SimplexComparator.getInstance());
			YTracker.getIntervalTracker().setUseRightClosedIntervals(false);
			for (Simplex y: Y_stream) {
				YTracker.add(y, j);
			}
			
			{
				System.out.println("Barcodes for X_" + (j));
				System.out.println(YTracker.getAnnotatedBarcodes().filterByMaxDimension(maxDimension).toString());
			}
			
			if (result == null) {
				result = InducedHomologyMappingUtility.project(XTracker, ZTracker, YTracker, XTracker.getStateWithoutFiniteBarcodes(), chainModule, Z_chainModule, (j - 1), j);
			} else {
				result = InducedHomologyMappingUtility.project(XTracker, ZTracker, YTracker, result, chainModule, Z_chainModule, (j - 1), j);
			}

			X_stream = Y_stream;
			XTracker = YTracker;
		}
		
		result.endAllIntervals(this.indexSelections.size() - 1);
		
		return BarcodeCollection.forgetGeneratorType(result.getAnnotatedBarcodes().filterByMaxDimension(maxDimension));
		
		//result.endAllIntervals(2 * this.indexSelections.size() - 2);
		//return BarcodeCollection.forgetGeneratorType(AnnotatedBarcodeCollection.filterEvenIntervals(result.getAnnotatedBarcodes()).filterByMaxDimension(maxDimension));
		
	}
}
