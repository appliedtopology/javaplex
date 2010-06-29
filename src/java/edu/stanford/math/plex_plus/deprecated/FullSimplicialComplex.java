package edu.stanford.math.plex_plus.deprecated;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.stanford.math.plex_plus.homology.chain_basis.Simplex;
import edu.stanford.math.plex_plus.homology.utility.HomologyUtility;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * This class is a basic implementation of a simplicial complex.
 * 
 * TODO: This class was just written up temporarily for testing 
 * purposes. It should be rewritten in a more efficient way. It 
 * currently stores all of the simplicies in the simplicial complex,
 * thus there is considerable redundancy.
 * 
 * @author Andrew Tausz
 *
 */

@Deprecated
public class FullSimplicialComplex {
	private SortedSet<Simplex> simplices = new TreeSet<Simplex>();
	
	public void addSimplex(Simplex simplex) {
		ExceptionUtility.verifyNonNull(simplex);
		
		this.simplices.add(simplex);
		
		int[] vertices = simplex.getVertices();
		if (vertices.length > 1) {
			for (int i = 0; i < vertices.length; i++) {
				this.addSimplex(new Simplex(HomologyUtility.removeIndex(vertices, i)));
			}
		}
	}

	public Iterator<Simplex> iterator() {
		return this.simplices.iterator();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		for (Simplex simplex : this.simplices) {
			builder.append(simplex.toString());
			builder.append(" ");
		}
		builder.append("}");
		return builder.toString();
	}
}
