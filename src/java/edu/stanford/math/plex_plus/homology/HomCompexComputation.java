package edu.stanford.math.plex_plus.homology;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.stanford.math.plex_plus.algebraic_structures.interfaces.IntField;
import edu.stanford.math.plex_plus.homology.complex.IntHomComplex;
import edu.stanford.math.plex_plus.homology.complex.IntSimplicialComplex;
import edu.stanford.math.plex_plus.homology.simplex.Simplex;
import edu.stanford.math.plex_plus.math.linear_algebra.IntFieldDecompositions;
import edu.stanford.math.plex_plus.utility.ArrayUtility;
import edu.stanford.math.plex_plus.utility.ArrayUtility2;
import edu.stanford.math.plex_plus.utility.ListUtility;

/**
 * This class performs the hom-complex computation
 * as per the work of Yi Ding. The algorithms are based
 * the content of Yi Ding's thesis.
 * 
 * @author Andrew Tausz
 *
 */
public class HomCompexComputation {
	public static void performComputation(IntSimplicialComplex<Simplex> complex1, IntSimplicialComplex<Simplex> complex2, IntField field) {
		IntHomComplex<Simplex> homComplex = new IntHomComplex<Simplex>(complex1, complex2, field);
		int[][] D_0 = homComplex.getDenseBoundaryMatrix(0);
		int[][] D_1 = homComplex.getDenseBoundaryMatrix(1);
		
		System.out.println(ArrayUtility2.toString(D_1));
		
		int[][] N_0 = IntFieldDecompositions.computeNullSpace(D_0, field);
		
		Set<int[]> reps = ClassifyAll(N_0, D_1, field);
		//List<int[]> reps = RemoveNull(N_0, D_1, field);
		System.out.println(ListUtility.toString(reps));
		
		for (int[] v: reps) {
			System.out.println(homComplex.getSumRepresentation(v, 0));
		}
		
		double[] v = new double[]{-0.1244, -0.0000,0.6667, -0.2089,  0.0000,  0.0000, -0.3333,  0.0000,  0.0000, -0.3333,  0.0000,  0.0000, -0.3333,  0.0000,  0.0000, -0.0000, -0.0000,  0.6667,  0.4578, -0.2089,  0.0000, -0.0000, -0.0000,  0.0000, -0.0000,  0.0000, -0.0000, -0.0000,  0.0000, -0.0000, -0.0000,  0.0000, -0.0000, -0.0000, -0.0000,  0.3333};
		
		System.out.println(homComplex.getSumRepresentation(v, 0).toString());
		
		
	}
	
	public static List<int[]> RemoveNull(int[][] N_0, int[][] D_1, IntField field) {
		int r = IntFieldDecompositions.rank(D_1, field);
		List<int[]> list = new ArrayList<int[]>();
		
		int numColumns = N_0[0].length;
		
		for (int j = 0; j < numColumns; j++) {
			int[] z = ArrayUtility.extractColumn(N_0, j);
			
			int[][] augmentedMatrix = ArrayUtility.appendColumn(D_1, z);
			int r_prime = IntFieldDecompositions.rank(augmentedMatrix, field);
			
			if (r != r_prime) {
				list.add(z);
			}
		}
		
		return list;
	}
	
	public static Set<int[]> ClassifyAll(int[][] N_0, int[][] D_1, IntField field) {
		List<int[]> list = RemoveNull(N_0, D_1, field);
		Set<int[]> reps = new HashSet<int[]>();
		
		for (int i = 0; i < list.size(); i++) {
			int[] z =  list.get(i);
			int[][] augmentedMatrix = ArrayUtility.appendColumn(D_1, z);
			int r = IntFieldDecompositions.rank(augmentedMatrix, field);
			int num_homotopic = 0;
			for (int j = i + 1; j < list.size(); j++) {
				int[] z_prime = list.get(j);
				int r_prime = IntFieldDecompositions.rank(ArrayUtility.appendColumn(augmentedMatrix, z_prime), field);
				if (r == r_prime) {
					num_homotopic++;
				}	
			}
			if (num_homotopic == 0) {
				reps.add(z);
			}
		}
		
		return reps;
	}
}
