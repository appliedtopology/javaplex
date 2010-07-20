package edu.stanford.math.plex4.io;

import java.io.IOException;

public abstract class FormatInterface {
	protected static int fileCount = 0;

	protected static String generateBasicFilename(String label) {
		return "file_" + label + getFileCount();
	}

	protected static int getFileCount() {
		return fileCount++;
	}

	public static String buildFilename(String basicName, String extension) throws IOException {
		return (FileManager.getDailyOutputPath() + basicName + "." + extension);
	}

	public static String buildFilename(String basicName) throws IOException {
		return (FileManager.getDailyOutputPath() + basicName + "." + getDefaultExtension());
	}

	protected static String getDefaultExtension() {
		return "txt";
	}
}
