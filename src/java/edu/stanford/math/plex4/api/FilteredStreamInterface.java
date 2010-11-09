package edu.stanford.math.plex4.api;

import edu.stanford.math.plex.EuclideanArrayData;
import edu.stanford.math.plex.Plex;
import edu.stanford.math.plex.RipsStream;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.interop.Plex3Stream;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.streams.impl.ExplicitCellStream;
import edu.stanford.math.plex4.streams.impl.ExplicitSimplexStream;
import edu.stanford.math.plex4.streams.impl.LazyWitnessStream;
import edu.stanford.math.plex4.streams.impl.VietorisRipsStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.primitivelib.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.primitivelib.metric.interfaces.AbstractSearchableMetricSpace;

public class FilteredStreamInterface {
	public static ExplicitSimplexStream createExplicitSimplexStream() {
		return new ExplicitSimplexStream();
	}
	
	public static ExplicitCellStream createExplicitCellStream() {
		return new ExplicitCellStream();
	}
	
	public static VietorisRipsStream<double[]> createPlex4VietorisRipsStream(double[][] points, int maxDimension, double maxFiltrationValue, int numDivisions) {
		
		AbstractSearchableMetricSpace<double[]> metricSpace = new EuclideanMetricSpace(points);
		VietorisRipsStream<double[]> stream = new VietorisRipsStream<double[]>(metricSpace, maxFiltrationValue, maxDimension, numDivisions);
		stream.finalizeStream();
		
		return stream;
	}
	
	public static AbstractFilteredStream<Simplex> createPlex3VietorisRipsStream(double[][] points, int maxDimension, double maxFiltrationValue, int numDivisions) {
		
		EuclideanArrayData pData = Plex.EuclideanArrayData(points);		
		RipsStream plex3RipsStream = Plex.RipsStream(maxFiltrationValue / numDivisions, maxDimension, maxFiltrationValue, pData);
		AbstractFilteredStream<Simplex> stream = new Plex3Stream(plex3RipsStream);
		stream.finalizeStream();
		
		return stream;
	}
	
	public static LazyWitnessStream<double[]> createPlex4LazyWitnessStream(LandmarkSelector<double[]> selector, int maxDimension, double maxFiltrationValue, int numDivisions) {

		LazyWitnessStream<double[]> stream = new LazyWitnessStream<double[]>(selector.getUnderlyingMetricSpace(), selector, maxDimension, maxFiltrationValue, numDivisions);
		stream.finalizeStream();
		
		return stream;
	}
	
	public static AbstractFilteredStream<Simplex> createPlex3LazyWitnessStream(LandmarkSelector<double[]> selector, int maxDimension, double maxFiltrationValue, int numDivisions) {

		EuclideanArrayData pData = Plex.EuclideanArrayData(selector.getUnderlyingMetricSpace().getPoints());
		edu.stanford.math.plex.LazyWitnessStream plex3WitnessStream = Plex.LazyWitnessStream(maxFiltrationValue / numDivisions, maxDimension, maxFiltrationValue, 2, convertTo1Based(selector.getLandmarkPoints()), pData);
		AbstractFilteredStream<Simplex> stream = new Plex3Stream(plex3WitnessStream);
		stream.finalizeStream();
		
		return stream;
	}
	
	private static int[] convertTo1Based(int[] array) {
		int[] result = new int[array.length + 1];
		result[0] = 0;
		for (int i = 0; i < array.length; i++) {
			result[i + 1] = array[i] + 1;
		}
		return result;
	}
}
