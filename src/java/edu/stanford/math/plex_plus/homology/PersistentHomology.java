package edu.stanford.math.plex_plus.homology;

import java.util.Comparator;

import edu.stanford.math.plex_plus.homology.simplex.AbstractSimplex;
import edu.stanford.math.plex_plus.math.structures.interfaces.IntField;

/**
 * This class computes persistent (co)homology based on the paper
 * "Dualities in persistent (co)homology" by de Silva, Morozov, and
 * Vejdemo-Johansson. The algorithms described in this paper offer
 * significant performance gains and provide a more complete understanding
 * of persistent homology.
 * 
 * @author Andrew Tausz
 *
 * @param <BasisElementType>
 */
public class PersistentHomology<BasisElementType extends AbstractSimplex> {
	private final IntField field;
	private final Comparator<BasisElementType> comparator;
	
	public PersistentHomology(IntField field, Comparator<BasisElementType> comparator) {
		this.field = field;
		this.comparator = comparator;
	}
}
