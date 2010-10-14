package edu.stanford.math.plex4.io2;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;

import gnu.trove.THashMap;

public class BufferedImageWriter implements ObjectWriter<BufferedImage> {
	private final String encoderFormat;
	
	private static final THashMap<String, BufferedImageWriter> instanceHash = new THashMap<String, BufferedImageWriter>();
	
	public static BufferedImageWriter getInstance(String parameter) {
		if (!instanceHash.containsKey(parameter)) {
			initializeInstance(parameter);
		}
		
		return instanceHash.get(parameter);
	}
	
	private static void initializeInstance(String parameter) {
		instanceHash.put(parameter, new BufferedImageWriter(parameter));
	}
	
	private BufferedImageWriter(String encoderFormat) {
		this.encoderFormat = encoderFormat;
	}
	
	public static String getDefaultEncoderFormat() {
		return "PNG";
	}	
	
	public void writeToFile(BufferedImage object, String path) throws IOException {
		BufferedOutputStream out = null;
		ImageEncoder encoder;
		try {
			out = new BufferedOutputStream(new FileOutputStream(path));
			encoder = ImageCodec.createImageEncoder(encoderFormat, out, null);
			encoder.encode(object);
		} catch (IOException e) {
			throw e;
		} finally {
			if (out != null) {
				out.close();
			}
		}		
	}

	public String getExtension() {
		return this.encoderFormat;
	}
}
