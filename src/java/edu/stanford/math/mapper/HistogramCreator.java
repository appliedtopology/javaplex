package edu.stanford.math.mapper;

public class HistogramCreator {
	protected final double[] values;
	protected final int numBins;
	protected final int[] bins;

	protected final String string;

	HistogramCreator(double[] values, int numBins) {
		this.values = values;
		this.numBins = numBins;

		this.bins = this.performBinning();

		this.string = this.generateString(20);
	}

	protected int[] performBinning() {
		double min = this.getMin();
		double max = this.getMax();

		int[] bins = new int[this.numBins];

		for (int i = 0; i < this.values.length; i++) {
			int bin = (int) Math.floor(this.numBins * (this.values[i] - min) / (max - min));
			if (bin >= this.numBins)
				bin = this.numBins - 1;

			bins[bin]++;
		}

		return bins;
	}

	protected double getMin() {
		double min = Double.POSITIVE_INFINITY;

		for (int i = 0; i < this.values.length; i++) {
			min = this.values[i] < min ? this.values[i] : min;
		}

		return min;
	}

	protected double getMax() {
		double max = Double.NEGATIVE_INFINITY;

		for (int i = 0; i < this.values.length; i++) {
			max = this.values[i] > max ? this.values[i] : max;
		}

		return max;
	}

	public int[] getCounts() {
		return this.bins;
	}

	protected double[] getBinLattice(double offset) {
		double[] values = new double[this.numBins];

		double min = this.getMin();
		double max = this.getMax();

		double binWidth = (max - min) / this.numBins;

		for (int i = 0; i < this.numBins; i++) {
			values[i] = (i + offset) * binWidth + min;
		}

		return values;
	}

	public double[] getBinStartingPoints() {
		return this.getBinLattice(0);
	}

	public double[] getBinEndingPoints() {
		return this.getBinLattice(1);
	}

	public double[] getBinMidPoints() {
		return this.getBinLattice(0.5);
	}

	public double getBinStartPoint(int binIndex) {
		return this.getBinLatticePoint(binIndex, 0);
	}
	
	public double getBinLatticePoint(int binIndex, double offset) {
		double min = this.getMin();
		double max = this.getMax();

		double binWidth = (max - min) / this.numBins;

		return (binIndex + offset) * binWidth + min;
	}
	
	public int getLastZeroBinIndex() {
		for (int i = this.numBins - 1; i >= 0; i--) {
			if (this.bins[i] == 0)
				return i;
		}

		return this.numBins;
	}

	protected String generateString(int maxHistogramStars) {
		double min = this.getMin();
		double max = this.getMax();

		double binWidth = (max - min) / this.numBins;

		StringBuilder builder = new StringBuilder();

		double maxBinCount = 0;

		for (int i = 0; i < this.numBins; i++) {
			maxBinCount = this.bins[i] > maxBinCount ? this.bins[i] : maxBinCount;
		}

		double starMultiplier = 1;

		if (maxBinCount > maxHistogramStars) {
			starMultiplier = maxHistogramStars / maxBinCount;
		}

		for (int i = 0; i < this.numBins; i++) {
			double start = i * binWidth + min;
			double end = (i + 1) * binWidth + min;

			if (i == this.numBins - 1) {
				builder.append(String.format("[%10.2f, %10.2f]", start, end));
			} else {
				builder.append(String.format("[%10.2f, %10.2f)", start, end));
			}

			int starCount = (int) Math.ceil(starMultiplier * this.bins[i]);

			String starString = HistogramCreator.repeatString("*", starCount);

			builder.append(": ");

			builder.append(String.format("(%10d) ", this.bins[i]));

			builder.append(starString);
			builder.append("\n");
		}

		return builder.toString();
	}

	protected static String repeatString(String s, int n) {
		return new String(new char[n]).replace("\0", s);
	}

	@Override
	public String toString() {
		return this.string;
	}
}
