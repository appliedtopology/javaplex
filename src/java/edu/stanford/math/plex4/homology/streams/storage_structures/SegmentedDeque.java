package edu.stanford.math.plex4.homology.streams.storage_structures;

import java.util.Iterator;

public class SegmentedDeque<T> implements Iterable<T> {
	
	private Segment[] segments;

	class Segment<T> {
		private final Object[] items;
		int index;
		public Segment(int size) {
			items = new Object[size];
		}
	}
	
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
}
