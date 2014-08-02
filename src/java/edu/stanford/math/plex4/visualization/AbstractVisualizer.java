package edu.stanford.math.plex4.visualization;

import java.util.List;

import edu.stanford.math.plex4.homology.barcodes.Interval;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.filtration.FiltrationConverter;
import edu.stanford.math.plex4.io.DoubleArrayReaderWriter;
import edu.stanford.math.plex4.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.metric.interfaces.AbstractSearchableMetricSpace;
import edu.stanford.math.plex4.streams.impl.VietorisRipsStream;

/**
 * Abstract class for visualizing a 3D point cloud and the persistent homology
 * classes corresponding to the Vietoris Rips filtration.
 * 
 * Based on processing code by Michael Vedjemo-Johanson
 * 
 * @author Jacobien Carstens
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractVisualizer extends PAppletSplitscreen {

	/*
	 * Global variables and initial setup
	 */

	DoubleArrayReaderWriter doubleArrayReaderWriter = DoubleArrayReaderWriter.getInstance();
	String fileName;
	float[][] pointsViz;
	double[][] pointsComp;
	AbstractSearchableMetricSpace<double[]> metricSpace;
	VietorisRipsStream<double[]> stream;
	FiltrationConverter fc;
	final int scale3D = 100;

	@Override
	public void doSetup() {
		background(255);
		openNewFile();
		imgWidth = width / 2;
	}

	void openNewFile() {
		final String inputFile = selectInput("Select a coordinate file to visualize (comma-separated, no header)");
		if (inputFile != null)
			fileName = inputFile;
		try {
			pointsComp = doubleArrayReaderWriter.importFromFile(fileName);
			metricSpace = new EuclideanMetricSpace(pointsComp);
			setFiltrationParameters(metricSpace);
			pointsViz = PointCloudScaling.scaleCoordinates(pointsComp, scale3D);
			computeVietorisRipsHomology();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Draw persistence diagram or barcode
	 */

	/**
	 * Drawing parameters
	 */
	float r = 2.0f;
	boolean fillTriangles = true;
	final int pointSize = 6;
	public static final String[][] colors = new String[][] { { "#001133", "#003BEB", "#80A8FF" }, { "#420000", "#D81007", "#FF8082" },
			{ "#0A2F09", "#0AAE04", "#81E77E" } };
	final int topMargin = 20, leftMargin = 60, rightMargin = 60, infinityMargin = 10;
	final float maxBarSpacing = 10;
	int imgWidth;

	/**
	 * Overwrite, checks if intervals are computed
	 * 
	 * @return true if intervals are available, false otherwise
	 */
	abstract boolean hasIntervals();

	/**
	 * Overwrite; return intervals at dimension
	 * 
	 * @param dimension
	 * @return list of intervals
	 */
	abstract List<Interval<Double>> getIntervalsAtDim(int dimension);

	/**
	 * Draw the 2D plot; i.e. barcodes or diagram
	 */
	@Override
	public void doDraw2D() {
		super.doDraw2D();
		printText();
		switch (plottype) {
		case BARCODE:
			if (hasIntervals()) {
				final List<Interval<Double>> ints = getIntervalsAtDim(dims.maxDimension);
				// Compute the canvas dimension
				final ImageRegion.Int barRegion = new ImageRegion.Int(height - leftMargin - rightMargin, imgWidth - leftMargin - topMargin, leftMargin,
						topMargin);
				// Compute the bar size
				final int intCount = ints.size();
				final float spacing = Math.min(maxBarSpacing, barRegion.height / (2 * intCount - 1));
				final float barHeight = spacing;
				// Calculate the bar scale
				final float maxBarWidth = (float) maxFiltrationValue.doubleValue();
				final float barScale = (barRegion.width) / maxBarWidth;
				// Set the amount of overlap for infinite extents
				final int infExtentOverlap = 5;
				// Draw the bars
				noStroke();
				double x0, x1;
				for (int i = 0; i < ints.size(); i++) {
					x0 = barScale * ints.get(i).getStart();
					if (ints.get(i).isInfinite())
						x1 = barRegion.width + infExtentOverlap;
					else
						x1 = barScale * ints.get(i).getEnd();
					fill(0, 0, 255);
					rect((float) (barRegion.xoffset + x0), barRegion.yoffset + spacing + (barHeight + spacing) * i, (float) (x1 - x0), barHeight);
				}
				// Draw the red vertical bar, indicating the filtration value
				fill(255, 0, 0);
				rect((float) (barRegion.xoffset + barScale * filtrationValue), barRegion.yoffset, 2, barRegion.height);
				drawDiagram(barRegion);
			}
			break;
		case DIAGRAM:
			if (hasIntervals()) {
				// Set the image properties
				final int plotWidth = width / 2 - leftMargin - rightMargin - infinityMargin;
				final int plotHeight = plotWidth + infinityMargin;
				// Compute the canvas dimension
				final ImageRegion.Int region = new ImageRegion.Int(plotHeight, plotWidth, leftMargin + infinityMargin / 2, topMargin);
				// draw axes
				stroke(0);
				line(region.xoffset, region.yoffset + region.height, region.xoffset, region.yoffset + infinityMargin);
				line(region.xoffset, region.yoffset + region.height, region.xoffset + region.width, region.yoffset + region.height);
				// draw infinity line
				stroke(220);
				line(region.xoffset, region.yoffset, region.xoffset + region.width, region.yoffset);
				// draw the box indicating the filtration value
				final float boxWidth = (float) (filtrationValue / maxFiltrationValue * region.width);
				final float boxHeight = (float) (region.width - filtrationValue / maxFiltrationValue * region.width);
				fill(120, 120, 120, 40);
				noStroke();
				rect(region.xoffset, region.yoffset + infinityMargin, boxWidth, boxHeight);
				// draw diagonal
				stroke(0, 100, 200);
				line(region.xoffset, region.yoffset + region.height, region.xoffset + region.width, region.yoffset + infinityMargin);
				noStroke();
				drawDiagram(region);
			}
			break;
		}
	}

	/**
	 * Overwrite to draw additional things in the persistence diagram
	 * 
	 * @param region
	 */
	abstract void drawDiagram(final ImageRegion.Int region);

	/*
	 * Add text to applet
	 */

	final int lineHeight = 12;
	final String font = "sans serif";

	void printText() {
		final int posX = width / 2 + leftMargin;
		final int posY = height - 6 * lineHeight;
		textFont(createFont(font, lineHeight));
		fill(0);
		final String[] fileNamePath = fileName.split("/");
		final String shortFileName = fileNamePath[fileNamePath.length - 1];
		text("Points from file: " + shortFileName, leftMargin, posY);
		text(fillTriangles ? "Coloring triangles blue, press 'f' to switch off." : "Not coloring triangles, press 'f' to switch on.", leftMargin, posY
				+ lineHeight);

		double rounded = Math.round(maxFiltrationValue * 1000);
		rounded = rounded / 1000;
		text("Max filtration value: " + rounded + ". Use '+' and '-' to adjust.", leftMargin, posY + 2 * lineHeight);
		text("Press 'h' to recompute homology", leftMargin, posY + 3 * lineHeight);
		if (plottype == PlotType.BARCODE)
			text("Showing barcode for homology in dimension " + dims.maxDimension + ".", posX, posY);
		else
			text("Showing persistence diagram in dimension" + (dims.visibleDims.length > 1 ? "s " : " "), posX, posY);
		text("Press '0'-'6' to change dimensions.", posX, posY + lineHeight);
		text("Use left and right arrow to increase filtration value.", posX, posY + 2 * lineHeight);
		text("Press 't' to switch plot type.", posX, posY + 3 * lineHeight);
		text("Press 'o' to open new coordinate file.", posX, posY + 4 * lineHeight);
	}

	/*
	 * Draw 3D point cloud
	 */

	/**
	 * Draws point cloud and the Vietoris Rips cpx up to current filtration
	 * value
	 */
	@Override
	public void doDraw3D() {
		background(255);
		stroke(0);
		fill(0);
		if (stream != null)
			for (final Simplex s : stream) {
				final double fv = fc.getFiltrationValue(stream.getFiltrationIndex(s));
				if (fv > filtrationValue)
					continue;
				int[] ix;
				ix = s.getVertices();

				switch (s.getDimension()) {
				case 0:
					fill(0);
					pushMatrix();
					translate(pointsViz[ix[0]][0], pointsViz[ix[0]][1], pointsViz[ix[0]][2]);
					sphere(r);
					popMatrix();
					break;
				case 1:
					fill(0);
					beginShape();
					vertex(pointsViz[ix[0]][0], pointsViz[ix[0]][1], pointsViz[ix[0]][2]);
					vertex(pointsViz[ix[1]][0], pointsViz[ix[1]][1], pointsViz[ix[1]][2]);
					endShape();
					break;
				case 2:
					if (fillTriangles) {
						fill(0, 0, 255, 20);
						beginShape();
						vertex(pointsViz[ix[0]][0], pointsViz[ix[0]][1], pointsViz[ix[0]][2]);
						vertex(pointsViz[ix[1]][0], pointsViz[ix[1]][1], pointsViz[ix[1]][2]);
						vertex(pointsViz[ix[2]][0], pointsViz[ix[2]][1], pointsViz[ix[2]][2]);
						vertex(pointsViz[ix[0]][0], pointsViz[ix[0]][1], pointsViz[ix[0]][2]);
						endShape();
					}
					break;
				default:
					continue;
				}
			}
		addTo3Dplot();
	}

	/**
	 * Overwrite to draw additional things in 3D plot
	 */
	abstract void addTo3Dplot();

	/**
	 * On keypress: Implement state changes and other effects as needed.
	 * 
	 * Key mapping implemented is case insensitive: O open new file Q quit 0-6
	 * (zero/one/two/zero and one/zero and two/one and two/zero and one and two)
	 * dimensional plot T switch plot type F switch filling triangles on or off
	 * + increase maxFiltrationValue by 5 - decrease maxFiltrationValue by 5 H
	 * recompute homology LEFT/RIGHT step Vietoris-Rips complex back/forward
	 */

	@Override
	public void keyPressed() {
		switch (key) {
		case 'o':
		case 'O':
			openNewFile();
			break;
		case 'q':
		case 'Q':
			exit();
			break;
		case '0':
			setCurrentDimension(Dimensions.ZERO);
			break;
		case '1':
			setCurrentDimension(Dimensions.ONE);
			break;
		case '2':
			setCurrentDimension(Dimensions.TWO);
			break;
		case '3':
			setCurrentDimension(Dimensions.ZEROandONE);
			break;
		case '4':
			setCurrentDimension(Dimensions.ZEROandTWO);
			break;
		case '5':
			setCurrentDimension(Dimensions.ONEandTWO);
			break;
		case '6':
			setCurrentDimension(Dimensions.ZEROandONEandTWO);
			break;
		case 'f':
		case 'F':
			switchFillTriangles();
			break;
		case '+':
			maxFiltrationValue += incrementFiltrationValue;
			break;
		case '-':
			maxFiltrationValue -= incrementFiltrationValue;
			break;
		case 'h':
		case 'H':
			computeVietorisRipsHomology();
			break;
		case 't':
		case 'T':
			plottype = (plottype == PlotType.BARCODE) ? PlotType.DIAGRAM : PlotType.BARCODE;
			break;
		case CODED:
			switch (keyCode) {
			case RIGHT:
				filtrationValue += stepSize;
				println(filtrationValue + ": " + stepSize);
				break;
			case LEFT:
				filtrationValue -= stepSize;
				println(filtrationValue + ": " + stepSize);
				if (filtrationValue < 0)
					filtrationValue = 0;
				break;
			}
		}
	}

	/**
	 * State variables
	 */
	Dimensions dims = Dimensions.ONE;
	PlotType plottype = PlotType.DIAGRAM;
	double filtrationValue = 0;
	Double stepSize = null;
	Double maxFiltrationValue = null;
	Double incrementFiltrationValue = null;

	void setFiltrationParameters(AbstractSearchableMetricSpace<double[]> metricSpace) {
		int n = pointsComp.length;
		double min[] = new double[n];
		maxFiltrationValue = 0.0;
		for (int i = 0; i < n; i++) {
			min[i] = Double.MAX_VALUE;
			for (int j = 0; j < n; j++) {
				if (i != j) {
					double d = metricSpace.distance(i, j);
					min[i] = Math.min(min[i], d);
				}
			}
			maxFiltrationValue = Math.max(maxFiltrationValue, min[i]);
		}
		incrementFiltrationValue = maxFiltrationValue / 10;
		stepSize = maxFiltrationValue / 20;
	}

	void switchFillTriangles() {
		fillTriangles = !fillTriangles;
	}

	void setCurrentDimension(final Dimensions dim) {
		dims = dim;
		computeVietorisRipsHomology();
	}

	/**
	 * Overwrite to run the persistent homology computation, update the
	 * intervals variable
	 */
	abstract void computeVietorisRipsHomology();

	enum Dimensions {
		ZERO(0, new int[] { 0 }, "0(blue)", "0, colors correspond to generators."), ONE(1, new int[] { 1 }, "1(red)", "1, colors correspond to generators."), TWO(
				2, new int[] { 2 }, "2(green)", "2, colors correspond to generators."), ZEROandONE(1, new int[] { 0, 1 }, "0(blue) and 1(red)",
				"0 and 1, colors correspond to generators, dimension are not distinguished."), ZEROandTWO(2, new int[] { 0, 2 }, "0(blue) and 2(green)",
				"0 and 2, colors correspond to generators, dimension are not distinguished."), ONEandTWO(2, new int[] { 1, 2 }, "1(red) and 2(green)",
				"1 and 2, colors correspond to generators, dimension are not distinguished."), ZEROandONEandTWO(2, new int[] { 0, 1, 2 },
				"0(blue), 1(red) and 2(green)", "0, 1 and 2, colors correspond to generators, dimension are not distinguished.");

		public final int maxDimension;
		public final int[] visibleDims;

		private Dimensions(final int maxDimension, final int[] visibleDims, final String explanationI, final String explanationII) {
			this.maxDimension = maxDimension;
			this.visibleDims = visibleDims;
		}
	}

	enum PlotType {
		BARCODE, DIAGRAM;
	}

}
