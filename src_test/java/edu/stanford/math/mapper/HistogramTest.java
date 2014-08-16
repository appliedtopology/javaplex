package edu.stanford.math.mapper;

import edu.stanford.math.plex4.utility.RandomUtility;

public class HistogramTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		double[] values = RandomUtility.normalArray(10000);

		HistogramCreator hist = new HistogramCreator(values, 20);

		System.out.println(hist);
	}

}
