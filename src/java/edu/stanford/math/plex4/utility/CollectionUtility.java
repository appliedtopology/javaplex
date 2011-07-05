package edu.stanford.math.plex4.utility;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtility {

	public static <U> List<U> dump(Iterable<U> X) {
		List<U> list = new ArrayList<U>();

		for (U x: X) {
			list.add(x);
		}

		return list;
	}

	public static <U> List<U> getDifference(Iterable<U> X, Iterable<U> A) {
		List<U> list = new ArrayList<U>();

		for (U x: X) {
			list.add(x);
		}

		for (U a: A) {
			list.remove(a);
		}

		return list;
	}
}
