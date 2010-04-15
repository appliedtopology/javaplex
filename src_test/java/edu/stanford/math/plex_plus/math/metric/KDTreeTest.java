package edu.stanford.math.plex_plus.math.metric;

import java.util.Set;

import edu.stanford.math.plex_plus.math.metric.impl.KDTree;
import edu.stanford.math.plex_plus.utility.ArrayUtility;

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
		double[] nearestPoint = null;
		Set<double[]> neighborhood = null;
		
		queryPoint = new double[]{2.2, 2.9};
		nearestPoint = tree.nearestNeighborSearch(queryPoint);
		System.out.println("Query:" + ArrayUtility.toString(queryPoint) + " Answer:" + ArrayUtility.toString(nearestPoint));
		
		queryPoint = new double[]{5, 4};
		nearestPoint = tree.nearestNeighborSearch(queryPoint);
		System.out.println("Query:" + ArrayUtility.toString(queryPoint) + " Answer:" + ArrayUtility.toString(nearestPoint));
		
		queryPoint = new double[]{11, 34};
		nearestPoint = tree.nearestNeighborSearch(queryPoint);
		System.out.println("Query:" + ArrayUtility.toString(queryPoint) + " Answer:" + ArrayUtility.toString(nearestPoint));
		
		queryPoint = new double[]{0, 0};
		nearestPoint = tree.nearestNeighborSearch(queryPoint);
		System.out.println("Query:" + ArrayUtility.toString(queryPoint) + " Answer:" + ArrayUtility.toString(nearestPoint));
		
		queryPoint = new double[]{-2, -3};
		nearestPoint = tree.nearestNeighborSearch(queryPoint);
		System.out.println("Query:" + ArrayUtility.toString(queryPoint) + " Answer:" + ArrayUtility.toString(nearestPoint));
		
		queryPoint = new double[]{9, 1};
		nearestPoint = tree.nearestNeighborSearch(queryPoint);
		System.out.println("Query:" + ArrayUtility.toString(queryPoint) + " Answer:" + ArrayUtility.toString(nearestPoint));
		
		queryPoint = new double[]{9, 5};
		nearestPoint = tree.nearestNeighborSearch(queryPoint);
		System.out.println("Query:" + ArrayUtility.toString(queryPoint) + " Answer:" + ArrayUtility.toString(nearestPoint));
		
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
