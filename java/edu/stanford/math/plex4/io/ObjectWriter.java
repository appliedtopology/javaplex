package edu.stanford.math.plex4.io;

import java.io.IOException;

public interface ObjectWriter<T> {
	void writeToFile(T object, String path) throws IOException;
	String getExtension();
}
