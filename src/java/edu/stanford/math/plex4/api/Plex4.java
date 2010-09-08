package edu.stanford.math.plex4.api;

import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.streams.impl.ExplicitSimplexStream;

/**
 * This class is designed to be a straightforward API called by matlab users.
 * 
 * @author Andrew Tausz
 *
 */
public class Plex4 {
	ExplicitSimplexStream createExplicitSimplexStream() {
		return new ExplicitSimplexStream(SimplexComparator.getInstance());
	}
}
