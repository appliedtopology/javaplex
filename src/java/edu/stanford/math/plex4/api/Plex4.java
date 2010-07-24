package edu.stanford.math.plex4.api;

import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.streams.impl.ExplicitSimplexStream;

public class Plex4 {
	ExplicitSimplexStream createExplicitSimplexStream() {
		return new ExplicitSimplexStream(SimplexComparator.getInstance());
	}
}
