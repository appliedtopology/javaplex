/**
 * 
 */
package edu.stanford.math.plex_plus.math.metric.landmark;

import edu.stanford.math.plex_plus.math.metric.interfaces.FiniteMetricSpace;
import edu.stanford.math.plex_plus.utility.RandomUtility;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.set.hash.TIntHashSet;

/**
 * @author Andrew Tausz
 *
 */
public class RandomLandmarkSelector<T> extends LandmarkSelector<T> {

	public RandomLandmarkSelector(FiniteMetricSpace<T> metricSpace, int landmarkSetSize) {
		super(metricSpace, landmarkSetSize);
	}
	
	@Override
	protected int[] computeLandmarkSet() {
		TIntHashSet landmarkSet = RandomUtility.randomSubset(this.landmarkSetSize, this.metricSpace.size());
		int[] indices = new int[this.landmarkSetSize];
		
		int index = 0;
		for (TIntIterator iterator = landmarkSet.iterator(); iterator.hasNext(); ) {
			indices[index] = iterator.next();
			index++;
		}
		
		return indices;
	}
}
