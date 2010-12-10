package edu.stanford.math.plex4.io;

import java.awt.image.BufferedImage;
import java.io.IOException;

import edu.stanford.math.plex4.homology.barcodes.DoubleBarcode;
import edu.stanford.math.plex4.visualization.BarcodeVisualizer;

public class BarcodeWriter {
	private static final BarcodeWriter instance = new BarcodeWriter();
	
	private BarcodeWriter() {}
	
	public static BarcodeWriter getInstance() {
		return instance;
	}
	
	public void writeToFile(DoubleBarcode object, String path, double endPoint) throws IOException {
		if (object == null) {
			throw new IllegalArgumentException();
		}
		BufferedImage image = BarcodeVisualizer.drawBarcode(object, endPoint);
		
		BufferedImageWriter.getInstance(BufferedImageWriter.getDefaultEncoderFormat()).writeToFile(image, path);
	}

	public String getExtension() {
		return BufferedImageWriter.getDefaultEncoderFormat();
	}

}
