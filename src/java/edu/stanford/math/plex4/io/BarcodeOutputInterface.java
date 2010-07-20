package edu.stanford.math.plex4.io;

import java.awt.image.BufferedImage;
import java.io.IOException;

import edu.stanford.math.plex4.homology.barcodes.Barcode;
import edu.stanford.math.plex4.visualization.BarcodeVisualizer;

public class BarcodeOutputInterface extends FormatInterface {
	protected static String extension = "png";

	public static String exportImage(Barcode barcode, String label) throws IOException {
		String filename = buildFilename(generateBasicBarcodeFilename(getBasicLabel(label)), extension);
		saveImage(barcode, filename);
		return filename;
	}

	protected static String getBasicLabel(String label) {
		int bracketIndex = label.indexOf('(');
		if (bracketIndex > 0) {
			return label.substring(0, bracketIndex);
		} else {
			return label;
		}
	}

	protected static String generateBasicBarcodeFilename(String label) {
		return "barcode_" + label + "_ " + getFileCount();
	}

	/**
	 * @param barcode
	 *            the barcode to make an image of
	 * @param filePath
	 *            the name to give the image file (minus extension)
	 * @throws IOException
	 */
	public static void saveImage(Barcode barcode, String filePath) throws IOException {
		if (barcode == null) {
			throw new IllegalArgumentException();
		}
		BufferedImage im = BarcodeVisualizer.drawBarcode(barcode);
		FileIOUtility.saveImage(im, filePath);
	}

}
