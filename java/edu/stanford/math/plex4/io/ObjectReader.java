package edu.stanford.math.plex4.io;

import java.io.IOException;

public interface ObjectReader<T> {
	T importFromFile(String path) throws IOException;
}
