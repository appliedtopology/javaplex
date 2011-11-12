package edu.stanford.math.plex4.streams.multi;

import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;

public interface AbstractStreamFlattener<T> {
	AbstractFilteredStream<T> collapse(AbstractMultifilteredStream<T> multifilteredStream);
}
