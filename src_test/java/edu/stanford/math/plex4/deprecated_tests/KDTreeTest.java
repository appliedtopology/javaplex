package edu.stanford.math.plex4.deprecated_tests;

import edu.stanford.math.plex4.kd.KDTree;
import edu.stanford.math.plex4.utility.ArrayUtility2;
import gnu.trove.set.hash.TIntHashSet;

public class KDTreeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testKD();
	}

	public static void testKD() {
		double[][] points = new double[][]{new double[]{2, 3}, 
				new double[]{4, 7},
				new double[]{5, 4},
				new double[]{7, 2},
				new double[]{8, 1},
				new double[]{9, 6}};
		
		KDTree tree = new KDTree(points);
		tree.constructTree();
		
		System.out.println("Nearest Neighbor Search");
		double[] queryPoint = null;
		int nearestIndex = 0;
		TIntHashSet neighborhood = null;
		
		queryPoint = new double[]{2.2, 2.9};
		nearestIndex = tree.nearestNeighborSearch(queryPoint);
		System.out.println("Query:" + ArrayUtility2.toString(queryPoint) + " Answer:" + ArrayUtility2.toString(points[nearestIndex]));
		
		queryPoint = new double[]{5, 4};
		nearestIndex = tree.nearestNeighborSearch(queryPoint);
		System.out.println("Query:" + ArrayUtility2.toString(queryPoint) + " Answer:" + ArrayUtility2.toString(points[nearestIndex]));
		
		queryPoint = new double[]{11, 34};
		nearestIndex = tree.nearestNeighborSearch(queryPoint);
		System.out.println("Query:" + ArrayUtility2.toString(queryPoint) + " Answer:" + ArrayUtility2.toString(points[nearestIndex]));
		
		queryPoint = new double[]{0, 0};
		nearestIndex = tree.nearestNeighborSearch(queryPoint);
		System.out.println("Query:" + ArrayUtility2.toString(queryPoint) + " Answer:" + ArrayUtility2.toString(points[nearestIndex]));
		
		queryPoint = new double[]{-2, -3};
		nearestIndex = tree.nearestNeighborSearch(queryPoint);
		System.out.println("Query:" + ArrayUtility2.toString(queryPoint) + " Answer:" + ArrayUtility2.toString(points[nearestIndex]));
		
		queryPoint = new double[]{9, 1};
		nearestIndex = tree.nearestNeighborSearch(queryPoint);
		System.out.println("Query:" + ArrayUtility2.toString(queryPoint) + " Answer:" + ArrayUtility2.toString(points[nearestIndex]));
		
		queryPoint = new double[]{9, 5};
		nearestIndex = tree.nearestNeighborSearch(queryPoint);
		System.out.println("Query:" + ArrayUtility2.toString(queryPoint) + " Answer:" + ArrayUtility2.toString(points[nearestIndex]));
		
		queryPoint = new double[]{2, 2};
		neighborhood = tree.epsilonNeighborhoodSearch(queryPoint, 1.1);
		System.out.println(neighborhood);
		
		queryPoint = new double[]{2, 2};
		neighborhood = tree.epsilonNeighborhoodSearch(queryPoint, 5);
		System.out.println(neighborhood);
		
		queryPoint = new double[]{2, 2};
		neighborhood = tree.epsilonNeighborhoodSearch(queryPoint, 500);
		System.out.println(neighborhood);
	}
}
