package edu.stanford.math.plex4.io2;

import java.io.IOException;

public interface ObjectWriter<T> {
	void writeToFile(T object, String path) throws IOException;
	String getExtension();
}
