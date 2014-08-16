package edu.stanford.math.mapper;

import edu.stanford.math.plex4.homology.barcodes.Interval;
import edu.stanford.math.plex4.streams.filter.IntFilterFunction;
import gnu.trove.TIntHashSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class computes the pre-image of a cover of the codomain of a function.
 * The function is assumed to be of the type IntFilterFunction. The result is is
 * accessible as an Iterable set of TIntHashSets which contain the pre-images of
 * each element of the cover.
 * 
 * @author Andrew Tausz
 * 
 */
public class SetCover1D implements Iterable<TIntHashSet> {
	private IntFilterFunction filter;
	private Iterable<Interval<Double>> rangeCover;
	private List<TIntHashSet> setCover = new ArrayList<TIntHashSet>();

	public SetCover1D(IntFilterFunction filter, Iterable<Interval<Double>> rangeCover) {
		super();
		this.filter = filter;
		this.rangeCover = rangeCover;

		this.initialize();
	}

	void initialize() {
		double[] values = filter.getValues();

		// note that this is not necessarily the most efficient in case more is
		// known about the range cover
		// will leave it generic for now
		for (Interval<Double> interval : this.rangeCover) {
			TIntHashSet set = new TIntHashSet();
			for (int i = 0; i < values.length; i++) {
				if (interval.containsPoint(values[i])) {
					set.add(i);
				}
			}
			this.setCover.add(set);
		}
	}

	public Iterator<TIntHashSet> iterator() {
		return this.setCover.iterator();
	}
}
