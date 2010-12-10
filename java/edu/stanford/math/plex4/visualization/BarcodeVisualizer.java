package edu.stanford.math.plex4.visualization;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.stanford.math.plex4.homology.barcodes.DoubleBarcode;
import edu.stanford.math.plex4.homology.barcodes.DoubleHalfOpenInterval;

/**
 * This class contains functions for visualizing barcodes.
 * 
 * @author Tim Harrington
 *
 */
public class BarcodeVisualizer {

	public static class BarAppearance {
		int spacing;
		int height;

		/**
		 * @param spacing
		 * @param height
		 */
		public BarAppearance(int spacing, int height) {
			this.spacing = spacing;
			this.height = height;
		}
	}

	public static final int DEFAULT_WIDTH = 600;

	public static BufferedImage drawBarcode(DoubleBarcode barcode, double endPoint) throws IOException {
		return drawBarcode(barcode, barcode.getLabel(), endPoint);
	}

	public static BufferedImage drawBarcode(DoubleBarcode barcode, String title, double endPoint) throws IOException {
		if (barcode == null) {
			throw new IllegalArgumentException();
		}

		List<DoubleHalfOpenInterval> intervals = barcode.getIntervals();

		// Set the image generation parameters
		int barHeight = 3;
		int barSpacing = 3;
		int imgWidth = DEFAULT_WIDTH;
		int topMargin = 25;
		int bottomMargin = 30;
		int leftMargin = 20;
		int rightMargin = 20;
		int titleMargin = 3; // ensure topMargin-2*titleMargin is enough room for title

		// Compute the bar region
		int barLayoutHeight = intervals.size() * (barHeight + barSpacing) + barSpacing;
		int barLayoutWidth = imgWidth - leftMargin - rightMargin;
		ImageRegion.Int barRegion = new ImageRegion.Int(barLayoutHeight, barLayoutWidth, leftMargin, topMargin);

		// Compute the image height
		int imgHeight = topMargin + barRegion.height + bottomMargin;

		// Make the image stream
		BufferedImage im = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_3BYTE_BGR);

		// Set up the canvas to draw on
		Graphics2D graphics = createGraphics(im);

		// Calculate the bar scale
		double maxBarWidth = endPoint;
		double barScale = (barRegion.width) / maxBarWidth;

		// Draw the ticks
		drawTicksInPlace(graphics, barRegion, bottomMargin, barScale);

		// Draw the title
		drawTitleInPlace(graphics, title, imgWidth / 2, (int) (topMargin * (1d / 3d)), topMargin - 2 * titleMargin, imgWidth - 2 * titleMargin);

		// Draw the bars
		drawBarsInPlace(graphics, intervals, barRegion, barHeight, barSpacing, barScale);

		return im;
	}

	protected static void drawTitleInPlace(Graphics2D graphics, String title, int center_x, int center_y, int maxHeight, int maxWidth) {
		graphics.setColor(Color.BLACK);
		int fontSize = 15;
		FontMetrics fm;
		int stringHeight;
		int stringWidth;
		while (true) {
			graphics.setFont(new Font("sansserif", Font.BOLD, fontSize));
			fm = graphics.getFontMetrics();
			stringHeight = fm.getHeight();
			stringWidth = fm.stringWidth(title);
			if (maxHeight < stringHeight || maxWidth < stringWidth) {
				fontSize -= 1;
			} else {
				break;
			}
			if (fontSize == 0) {
				throw new IllegalArgumentException("Cannot fit the title in the given image region.");
			}
		}
		graphics.drawString(title, center_x - (stringWidth / 2), center_y + (stringHeight / 2));
	}

	// Set up the image in memory
	protected static Graphics2D createGraphics(BufferedImage im) {
		Graphics2D graphics = im.createGraphics();
		Map<RenderingHints.Key, Object> hints = new HashMap<RenderingHints.Key, Object>();
		hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.addRenderingHints(hints);
		graphics.setBackground(Color.WHITE);
		graphics.clearRect(0, 0, im.getWidth(), im.getHeight());
		return graphics;
	}

	protected static void drawTicksInPlace(Graphics2D graphics, ImageRegion.Int region, int bottomMargin, double scale) {

		Color lastTickColor = new Color(0xff, 0x00, 0x00);
		Color majorTickColor = new Color(0x0f, 0x85, 0xff);
		Color minorTickColor = new Color(0xa9, 0xcc, 0xdf);
		// Color someColor = new Color(0x97, 0xd8, 0xff);

		int tickGap = 8;
		int majorTickEvery = 5;
		int bigOverlap = 10;
		int smallOverlap = 4;

		// estimate the number of ticks
		int howManyTicks = (int) Math.floor(((double) region.width) / ((double) tickGap));

		// adjust so that it is a multiple of majorTickEvery
		howManyTicks = howManyTicks - (howManyTicks % (2 * majorTickEvery));
		double tickWidth = ((double) region.width) / ((double) howManyTicks);

		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics.setFont(new Font("sansserif", Font.PLAIN, 10));
		FontMetrics fm = graphics.getFontMetrics();
		int fontHeight = fm.getHeight();

		int x0, x1, y0, y1, labelWidth;
		y0 = region.yoffset;
		String tickLabel;
		double tick;
		for (int i = 0; i <= howManyTicks; i++) {

			// all these casts are to minimize rounding error
			x0 = (int) (((i) * tickWidth) + region.xoffset);
			x1 = x0;

			if (i % majorTickEvery == 0) { // major tick
				// Prepare the tick label
				tick = ((i) * tickWidth) / scale;
				tickLabel = String.format("%4.4f", tick);
				labelWidth = fm.stringWidth(tickLabel);

				graphics.setColor(majorTickColor);
				if (i % (2 * majorTickEvery) == 0) {
					if (i == howManyTicks) {
						graphics.setColor(lastTickColor);
					}
					// long tick
					y1 = region.yoffset + region.height + bigOverlap;
				} else {
					// short tick
					y1 = region.yoffset + region.height + smallOverlap;
				}
				graphics.drawString(tickLabel, x0 - (labelWidth) / 2, y1 + fontHeight - 2);
			} else { // minor tick
				graphics.setColor(minorTickColor);
				y1 = region.yoffset + region.height;
			}
			graphics.drawLine(x0, y0, x1, y1);
		}

		// Draw the top and bottom colors
		graphics.setColor(majorTickColor);
		graphics.drawLine(region.xoffset, region.yoffset, region.xoffset + region.width, region.yoffset);
		graphics.drawLine(region.xoffset, region.yoffset + region.height, region.xoffset + region.width, region.yoffset + region.height);
	}

	protected static void drawBarsInPlace(Graphics2D graphics, List<DoubleHalfOpenInterval> intervals, ImageRegion.Int region, int barHeight, int spacing, double scale) {
		graphics.setColor(Color.BLACK);

		// Set the amount of overlap for infinite extents
		int infExtentOverlap = 5;

		// Draw the bars
		double x0, x1;
		Rectangle2D.Double bar;
		for (int i = 0; i < intervals.size(); i++) {
			x0 = scale * intervals.get(i).getStart();
			if (intervals.get(i).isInfinite()) {
				x1 = region.width + infExtentOverlap;
			} else {
				x1 = scale * intervals.get(i).getEnd();
			}
			// The constructor arguments are x-coord, y-coord, width, height
			bar = new Rectangle2D.Double(region.xoffset + x0, region.yoffset + spacing + (barHeight + spacing) * i, x1 - x0, barHeight);
			graphics.fill(bar);
		}
	}

	protected static double getMaxBarWidth(List<DoubleHalfOpenInterval> intervals) {
		// Compute a multiplier to scale the barcodes to the image width
		double max_x = 0;
		for (int j = 0; j < intervals.size(); j++) {
			if (intervals.get(j).isInfinite())
				continue;
			if (max_x < intervals.get(j).getEnd()) {
				max_x = intervals.get(j).getEnd();
			}
		}
		if (max_x == 0) { // all intervals are 'infinite'
			max_x = 1;
		}
		// Multiplier to scale the barcodes to fit the image width
		return max_x;
	}
}
