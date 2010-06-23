/**
 * 
 */
package edu.stanford.math.plex_plus.homology.simplex_streams;

import java.util.Iterator;

import edu.stanford.math.plex_plus.homology.simplex.Simplex;
import edu.stanford.math.plex_plus.math.metric.impl.EuclideanMetricSpace;

/**
 * This class implements the functionality of a simplex stream realized
 * as a filtered simplicial complex in Euclidean space.
 * 
 * @author Andrew Tausz
 *
 */
public class EuclideanStream implements SimplexStream<Simplex> {
	private final SimplexStream<Simplex> abstractSimplexStream;
	private final EuclideanMetricSpace euclideanMetricSpace;
	
	public EuclideanStream(SimplexStream<Simplex> abstractSimplexStream, EuclideanMetricSpace euclideanMetricSpace) {
		this.abstractSimplexStream = abstractSimplexStream;
		this.euclideanMetricSpace = euclideanMetricSpace;
	}
	
	@Override
	public void finalizeStream() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getDimension() {
		return this.abstractSimplexStream.getDimension();
	}

	@Override
	public double getFiltrationValue(Simplex simplex) {
		return this.abstractSimplexStream.getFiltrationValue(simplex);
	}

	@Override
	public boolean isFinalized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<Simplex> iterator() {
		return this.abstractSimplexStream.iterator();
	}

}
