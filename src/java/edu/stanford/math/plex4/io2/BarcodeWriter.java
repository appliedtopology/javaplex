package edu.stanford.math.plex4.io2;

import java.awt.image.BufferedImage;
import java.io.IOException;

import edu.stanford.math.plex4.homology.barcodes.DoubleBarcode;
import edu.stanford.math.plex4.visualization.BarcodeVisualizer;

public class BarcodeWriter implements ObjectWriter<DoubleBarcode> {
	private static final BarcodeWriter instance = new BarcodeWriter();
	
	private BarcodeWriter() {}
	
	public static BarcodeWriter getInstance() {
		return instance;
	}
	
	public void writeToFile(DoubleBarcode object, String path) throws IOException {
		if (object == null) {
			throw new IllegalArgumentException();
		}
		BufferedImage image = BarcodeVisualizer.drawBarcode(object);
		
		BufferedImageWriter.getInstance(BufferedImageWriter.getDefaultEncoderFormat()).writeToFile(image, path);
	}

	public String getExtension() {
		return BufferedImageWriter.getDefaultEncoderFormat();
	}

}
