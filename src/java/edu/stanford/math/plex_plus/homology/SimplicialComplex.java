package edu.stanford.math.plex_plus.homology;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.math.plex_plus.datastructures.IntLabeledGrid;
import edu.stanford.math.plex_plus.homology.utility.HomologyUtility;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.set.hash.THashSet;

/**
 * This abstract class defines functionality for a simplicial complex.
 * 
 * @author Andrew Tausz
 *
 */
public abstract class SimplicialComplex {
	public abstract void addSimplex(Simplex simplex);
	public abstract int getDimension();
	public abstract THashSet<Simplex> getSkeleton(int k);
		
	IntLabeledGrid<Simplex> getBoundaryMatrix(int k) {
		ExceptionUtility.verifyPositive(k);
		List<Simplex> kSkeleton = new ArrayList<Simplex>();
		kSkeleton.addAll(this.getSkeleton(k));
		
		IntLabeledGrid<Simplex> grid = new IntLabeledGrid<Simplex>();
		
		for (int i = 0; i < kSkeleton.size(); i++) {
			int[] vertices = kSkeleton.get(i).getVertices();
			for (int j = 0; j < vertices.length; j++) {
				int[] boundaryVertex = HomologyUtility.removeIndex(vertices, j);
				grid.setEntry(new ArraySimplex(boundaryVertex), kSkeleton.get(i), (j % 2 == 0 ? 1 : -1));
			}
		}
		return grid;		
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int d = 0; d <= this.getDimension(); d++) {
			builder.append(d + ": ");
			builder.append('{');
			THashSet<Simplex> skeleton = this.getSkeleton(d);
			for (Simplex simplex : skeleton) {
				builder.append(simplex.toString());
				builder.append(' ');
			}
			builder.append("}\n");
		}
		return builder.toString();
	}
}
