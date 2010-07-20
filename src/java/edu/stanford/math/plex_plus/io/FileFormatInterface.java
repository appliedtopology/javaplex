package edu.stanford.math.plex_plus.io;

public interface FileFormatInterface<T> {
	T importFromFile(String path);
	void exportToFile(T object);
}
