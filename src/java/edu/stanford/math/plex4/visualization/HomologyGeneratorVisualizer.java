package edu.stanford.math.plex4.visualization;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import processing.core.PApplet;
import edu.stanford.math.plex4.api.Plex4;
import edu.stanford.math.plex4.autogen.homology.BooleanAbsoluteHomology;
import edu.stanford.math.plex4.homology.barcodes.AnnotatedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.Interval;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.primitivelib.autogen.formal_sum.BooleanSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;

@SuppressWarnings("serial")
public class HomologyGeneratorVisualizer extends AbstractVisualizer {

	// *************************************************************************************
	// ******************* GLOBAL VARIABLES FOR HOMOLOGY AND GENERATORS
	// *******************
	// *************************************************************************************
	private Map<Point2D, List<BooleanSparseFormalSum<Simplex>>> diagramToGenerators;
	private final List<List<BooleanSparseFormalSum<Simplex>>> visibleGenerators = new LinkedList<List<BooleanSparseFormalSum<Simplex>>>();
	private final List<Point2D> visibleGeneratorCoordinates = new LinkedList<Point2D>();
	private final String[] genColors = new String[] { "#6A6BCD", "#9A0798", "#246B5F", "#EC305E", "#3CB43E", "#D16A33", "#71D0AF", "#BFA240" };
	private AnnotatedBarcodeCollection<Double, BooleanSparseFormalSum<Simplex>> intervals = null;

	public static void main(final String[] args) {
		PApplet.main(new String[] { "edu.stanford.math.plex4.visualization.HomologyGeneratorVisualizer" });
	}

	@Override
	void computeVietorisRipsHomology() {
		final int numDivisions = 1000;
		stream = Plex4.createVietorisRipsStream(pointsComp, dims.maxDimension + 1, maxFiltrationValue, numDivisions);
		fc = stream.getConverter();
		BooleanAbsoluteHomology<Simplex> persistenceAlgorithm = new BooleanAbsoluteHomology<Simplex>(SimplexComparator.getInstance(), 0, dims.maxDimension + 1);
		intervals = persistenceAlgorithm.computeAnnotatedIntervals(stream);
		visibleGenerators.clear();
		visibleGeneratorCoordinates.clear();
		println(intervals);
	}

	@Override
	boolean hasIntervals() {
		return (intervals != null);
	}

	@Override
	List<Interval<Double>> getIntervalsAtDim(int dimension) {
		return (intervals.getIntervalsAtDimension(dimension));
	}

	@Override
	void drawDiagram(ImageRegion.Int region) {
		switch (plottype) {
		case BARCODE:
			break;
		case DIAGRAM:
			for (final int visibleDim : dims.visibleDims) {
				final List<ObjectObjectPair<Interval<Double>, BooleanSparseFormalSum<Simplex>>> intervalGeneratorPairs = intervals
						.getIntervalGeneratorPairsAtDimension(visibleDim);
				diagramToGenerators = new HashMap<Point2D, List<BooleanSparseFormalSum<Simplex>>>();
				for (final ObjectObjectPair<Interval<Double>, BooleanSparseFormalSum<Simplex>> intervalGeneratorPair : intervalGeneratorPairs) {
					fill(Color.decode(colors[visibleDim][1]).getRed(), Color.decode(colors[visibleDim][1]).getGreen(), Color.decode(colors[visibleDim][1])
							.getBlue());
					final Interval<Double> interval = intervalGeneratorPair.getFirst();
					if (interval.getStart() > filtrationValue)
						fill(Color.decode(colors[visibleDim][2]).getRed(), Color.decode(colors[visibleDim][2]).getGreen(), Color.decode(colors[visibleDim][2])
								.getBlue());
					final double xpos = interval.getStart() / maxFiltrationValue;
					final int x = region.xoffset + (int) (xpos * region.width);
					int y;
					if (interval.isRightInfinite())
						y = region.yoffset;
					else {
						if (interval.getEnd() < filtrationValue)
							fill(Color.decode(colors[visibleDim][0]).getRed(), Color.decode(colors[visibleDim][0]).getGreen(),
									Color.decode(colors[visibleDim][0]).getBlue());
						final double ypos = interval.getEnd() / maxFiltrationValue;
						y = region.yoffset + region.height - (int) (region.width * ypos);
					}

					ellipse(x, y, pointSize, pointSize);
					final Point2D coordinates = new Point2D(x, y);
					final BooleanSparseFormalSum<Simplex> generator = intervalGeneratorPair.getSecond();
					List<BooleanSparseFormalSum<Simplex>> list = diagramToGenerators.get(coordinates);
					if (list == null)
						list = new ArrayList<BooleanSparseFormalSum<Simplex>>();
					list.add(generator);
					diagramToGenerators.put(coordinates, list);
				}
			}
			int i = 0;
			for (final Point2D point : visibleGeneratorCoordinates) {
				final Color c = Color.decode(genColors[i]);
				fill(c.getRed(), c.getGreen(), c.getBlue());
				ellipse(point.x, point.y, pointSize + 2, pointSize + 2);
				i = i + 1 % 7;
			}
			break;
		}
	}

	protected void drawBarsInPlace(final List<Interval<Double>> ints, final ImageRegion.Int region, final float barHeight, final float spacing,
			final double scale) {

	}

	@Override
	void addTo3Dplot() {
		if (visibleGenerators.size() > 0) {
			int i = 0;
			for (final List<BooleanSparseFormalSum<Simplex>> generators : visibleGenerators) {
				for (BooleanSparseFormalSum<Simplex> generator : generators)
					highlightGenerator(generator, Color.decode(genColors[i]));
				i = ((i + 1) % 8);
			}
		}
	}

	public void highlightGenerator(final BooleanSparseFormalSum<Simplex> generator, final Color c) {
		fill(c.getRed(), c.getGreen(), c.getBlue());
		stroke(c.getRed(), c.getGreen(), c.getBlue());
		final Iterator<Simplex> iterator = generator.iterator();
		while (iterator.hasNext()) {
			final Simplex simplex = iterator.next();
			final int ix[] = simplex.getVertices();
			switch (simplex.getDimension()) {
			case 0:
				pushMatrix();
				translate(pointsViz[ix[0]][0], pointsViz[ix[0]][1], pointsViz[ix[0]][2]);
				sphere(r);
				popMatrix();
				break;
			case 1:
				beginShape();
				vertex(pointsViz[ix[0]][0], pointsViz[ix[0]][1], pointsViz[ix[0]][2]);
				vertex(pointsViz[ix[1]][0], pointsViz[ix[1]][1], pointsViz[ix[1]][2]);
				endShape();
				break;
			case 2:
				if (fillTriangles) {
					beginShape();
					vertex(pointsViz[ix[0]][0], pointsViz[ix[0]][1], pointsViz[ix[0]][2]);
					vertex(pointsViz[ix[1]][0], pointsViz[ix[1]][1], pointsViz[ix[1]][2]);
					vertex(pointsViz[ix[2]][0], pointsViz[ix[2]][1], pointsViz[ix[2]][2]);
					vertex(pointsViz[ix[0]][0], pointsViz[ix[0]][1], pointsViz[ix[0]][2]);
					endShape();
				}
				break;
			}
		}
	}

	@Override
	public void mousePressed() {
		super.mousePressed();
		if (mouseX > width / 2) {
			final int mouseXinDiagram = mouseX - width / 2;
			for (final Point2D point : diagramToGenerators.keySet()) {
				if (Math.abs(mouseXinDiagram - point.x) < pointSize && Math.abs(mouseY - point.y) < pointSize) {
					final List<BooleanSparseFormalSum<Simplex>> generators = diagramToGenerators.get(point);
					if (visibleGeneratorCoordinates.contains(point)) {
						visibleGenerators.remove(generators);
						visibleGeneratorCoordinates.remove(point);
					} else {
						visibleGenerators.add(generators);
						visibleGeneratorCoordinates.add(point);
					}
				}
			}
			System.out.println("X-coordinate: " + (mouseX - width / 2) + " and Y-coordinate: " + mouseY);
		}
	}

}
