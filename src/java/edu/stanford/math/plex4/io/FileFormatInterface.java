package edu.stanford.math.plex4.io;

public interface FileFormatInterface<T> {
	T importFromFile(String path);
	void exportToFile(T object);
}
