package edu.stanford.math.plex_plus.homology.simplex_streams;

import edu.stanford.math.plex_plus.homology.simplex.Simplex;


public interface SimplexStream extends Iterable<Simplex> {
	public double getFiltrationIndex(Simplex simplex);
}
