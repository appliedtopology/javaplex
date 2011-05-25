package edu.stanford.math.plex4.io;

import java.awt.image.BufferedImage;
import java.io.IOException;

import edu.stanford.math.plex4.homology.barcodes.Interval;
import edu.stanford.math.plex4.homology.barcodes.PersistenceInvariantDescriptor;
import edu.stanford.math.plex4.visualization.BarcodeVisualizer;


public class BarcodeWriter {
	private static final BarcodeWriter instance = new BarcodeWriter();
	
	private BarcodeWriter() {}
	
	public static BarcodeWriter getInstance() {
		return instance;
	}
	
	public <G> void writeToFile(PersistenceInvariantDescriptor<Interval<Double>, G> object, int dimension, double endPoint, String caption, String path) throws IOException {
		String label = String.format("%s (Dimension: %d)", caption, dimension);
		BufferedImage image = BarcodeVisualizer.drawBarcode(object.getIntervalsAtDimension(dimension), label, endPoint);
		BufferedImageWriter.getInstance(BufferedImageWriter.getDefaultEncoderFormat()).writeToFile(image, path);
	}

	public String getExtension() {
		return BufferedImageWriter.getDefaultEncoderFormat();
	}

}
