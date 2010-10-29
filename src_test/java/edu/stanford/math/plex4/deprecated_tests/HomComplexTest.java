package edu.stanford.math.plex4.deprecated_tests;

import java.io.IOException;

import edu.stanford.math.plex4.algebraic_structures.impl.RationalField;
import edu.stanford.math.plex4.algebraic_structures.interfaces.GenericOrderedField;
import edu.stanford.math.plex4.datastructures.pairs.GenericPair;
import edu.stanford.math.plex4.examples.PointCloudExamples;
import edu.stanford.math.plex4.examples.SimplexStreamExamples;
import edu.stanford.math.plex4.free_module.DoubleFormalSum;
import edu.stanford.math.plex4.functional.GenericDoubleFunction;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.mapping.HomComplexComputation;
import edu.stanford.math.plex4.homology.mapping.MappingUtility;
import edu.stanford.math.plex4.homology.streams.impl.ExplicitStream;
import edu.stanford.math.plex4.homology.streams.impl.LazyWitnessStream;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.math.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.math.metric.interfaces.SearchableFiniteMetricSpace;
import edu.stanford.math.plex4.math.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex4.math.metric.landmark.MaxMinLandmarkSelector;
import edu.stanford.math.plex4.utility.RandomUtility;

public class HomComplexTest {
	public static void main(String[] args) {
		simplicialHomTest(RationalField.getInstance());
	}

	public static <F extends Number> void simplicialHomTest(GenericOrderedField<F> field) {
		/*
		int domainCardinality = 4;
		SearchableFiniteMetricSpace<double[]> domainMetricSpace = new EuclideanMetricSpace(getFigure8Vertices());
		SearchableFiniteMetricSpace<double[]> codomainMetricSpace = new EuclideanMetricSpace(PointCloudExamples.getEquispacedCirclePoints(domainCardinality));
		AbstractFilteredStream<Simplex> domainStream = getFigure8();
		AbstractFilteredStream<Simplex> codomainStream =  SimplexStreamExamples.getCircle(domainCardinality);
		*/
		/*
		int domainCardinality = 40;
		int codomainCardinality = 70;
		SearchableFiniteMetricSpace<double[]> domainMetricSpace = new EuclideanMetricSpace(PointCloudExamples.getEquispacedCirclePoints(domainCardinality));
		SearchableFiniteMetricSpace<double[]> codomainMetricSpace = new EuclideanMetricSpace(PointCloudExamples.getEquispacedCirclePoints(codomainCardinality));
		AbstractFilteredStream<Simplex> domainStream = SimplexStreamExamples.getCircle(domainCardinality);
		AbstractFilteredStream<Simplex> codomainStream = SimplexStreamExamples.getCircle(codomainCardinality);
		*/
		
		//AbstractFilteredStream<Simplex> domainStream = SimplexStreamExamples.getTorus();
		//AbstractFilteredStream<Simplex> codomainStream = SimplexStreamExamples.getTorus();

		//AbstractFilteredStream<Simplex> domainStream = SimplexStreamExamples.getCircle(10);
		//AbstractFilteredStream<Simplex> codomainStream = SimplexStreamExamples.getCircle(3);
		/*
		SearchableFiniteMetricSpace<double[]> domainMetricSpace = new EuclideanMetricSpace(PointCloudExamples.gettetrahedronVertices());
		SearchableFiniteMetricSpace<double[]> codomainMetricSpace = new EuclideanMetricSpace(PointCloudExamples.getoctahedronVertices());
		AbstractFilteredStream<Simplex> domainStream = SimplexStreamExamples.getTetrahedron();
		AbstractFilteredStream<Simplex> codomainStream = SimplexStreamExamples.getOctahedron();
		*/
		//SearchableFiniteMetricSpace<double[]> domainMetricSpace = new EuclideanMetricSpace(PointCloudExamples.getEquispacedCirclePoints(domainCardinality));
		//SearchableFiniteMetricSpace<double[]> codomainMetricSpace = new EuclideanMetricSpace(PointCloudExamples.getEquispacedCirclePoints(codomainCardinality));
		RandomUtility.initializeWithSeed(0);
		int n = 500;
		int l = 14;
		double r_max = 0.2;
		int d_max = 3;
		SearchableFiniteMetricSpace<double[]> domainMetricSpace = new EuclideanMetricSpace(PointCloudExamples.getRandomSpherePoints(n, 1));
		SearchableFiniteMetricSpace<double[]> codomainMetricSpace = new EuclideanMetricSpace(PointCloudExamples.getRandomSpherePoints(n, 1));
		LandmarkSelector<double[]> domainSelector = new MaxMinLandmarkSelector<double[]>(domainMetricSpace, l);
		LandmarkSelector<double[]> codomainSelector = new MaxMinLandmarkSelector<double[]>(codomainMetricSpace, l);
		AbstractFilteredStream<Simplex> domainStream = new LazyWitnessStream<double[]>(domainMetricSpace, domainSelector, d_max, r_max, 20);
		AbstractFilteredStream<Simplex> codomainStream = new LazyWitnessStream<double[]>(codomainMetricSpace, codomainSelector, d_max, r_max, 20);
		domainStream.finalizeStream();
		codomainStream.finalizeStream();
		/*
		RandomUtility.initializeWithSeed(0);
		int n = 200;
		int l = 20;
		double r_max = 0.1;
		int d_max = 2;
		int codomainCardinality = 5;
		SearchableFiniteMetricSpace<double[]> domainMetricSpace = new EuclideanMetricSpace(PointCloudExamples.getRandomSpherePoints(n, 1));
		SearchableFiniteMetricSpace<double[]> codomainMetricSpace = new EuclideanMetricSpace(PointCloudExamples.getEquispacedCirclePoints(codomainCardinality));
		LandmarkSelector<double[]> domainSelector = new MaxMinLandmarkSelector<double[]>(domainMetricSpace, l);
		AbstractFilteredStream<Simplex> domainStream = new LazyWitnessStream<double[]>(domainMetricSpace, domainSelector, d_max, r_max, 20);
		AbstractFilteredStream<Simplex> codomainStream = SimplexStreamExamples.getCircle(codomainCardinality);
		domainStream.finalizeStream();
		codomainStream.finalizeStream();
		*/
		
		HomComplexComputation<F> computation = new HomComplexComputation<F>(domainStream, codomainStream, SimplexComparator.getInstance(), SimplexComparator.getInstance(), field);

		//System.out.println("Computing Generating Cycles");
		//List<AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>>> generatingCycles = computation.computeGeneratingCycles();

		//for (AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>> generatingCycle: generatingCycles) {
		//	System.out.println(generatingCycle);
		//}

		//AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>> cycleSum = computation.sumGeneratingCycles(generatingCycles);

		//System.out.println("Computing Homotopies: ");
		//List<AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>>> homotopies = computation.getChainHomotopies();
		//System.out.println("Homotopy set size: " + homotopies.size());

		//for (AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>> homotopy: homotopies) {
		//	System.out.println(homotopy);
		//}

		//GenericDoubleFunction<DoubleFormalSum<Simplex>> imagePenaltyFunction = MappingUtility.getNormFunction(1);
		//GenericDoubleFunction<DoubleFormalSum<GenericPair<Simplex, Simplex>>> mappingPenaltyFunction = MappingUtility.computeInducedFunction(imagePenaltyFunction, domainStream);

		GenericDoubleFunction<DoubleFormalSum<GenericPair<Simplex, Simplex>>> mappingPenaltyFunction = MappingUtility.getSimplicialityLossFunction(domainStream, codomainStream);

		//GenericDoubleFunction<DoubleFormalSum<GenericPair<Simplex, Simplex>>> 

		mappingPenaltyFunction = MappingUtility.getDiameterLossFunction(domainStream, codomainStream);

		try {
			System.out.println("Computing Optimal Cycle");
			//DoubleFormalSum<GenericPair<Simplex, Simplex>> optimalChainMap = computation.findOptimalChainMap(cycleSum, homotopies, mappingPenaltyFunction);
			//optimalChainMap = MappingUtility.roundCoefficients(optimalChainMap);
			//System.out.println(optimalChainMap);


			//System.out.println(mappingPenaltyFunction.evaluate(optimalChainMap));

			computation.produceMatlabOutput();

			//} catch (OptimizationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//} catch (FunctionEvaluationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static ExplicitStream<Simplex> getFigure8() {
		ExplicitStream<Simplex> stream = new ExplicitStream<Simplex>(SimplexComparator.getInstance());

		stream.addElement(new Simplex(new int[] {0}), 0);
		stream.addElement(new Simplex(new int[] {1}), 0);
		stream.addElement(new Simplex(new int[] {2}), 0);
		stream.addElement(new Simplex(new int[] {3}), 0);
		stream.addElement(new Simplex(new int[] {4}), 0);
		stream.addElement(new Simplex(new int[] {5}), 0);
		stream.addElement(new Simplex(new int[] {6}), 0);

		stream.addElement(new Simplex(new int[] {0, 1}), 0);
		stream.addElement(new Simplex(new int[] {0, 3}), 0);
		stream.addElement(new Simplex(new int[] {1, 2}), 0);
		stream.addElement(new Simplex(new int[] {2, 3}), 0);
		stream.addElement(new Simplex(new int[] {2, 4}), 0);
		stream.addElement(new Simplex(new int[] {4, 5}), 0);
		stream.addElement(new Simplex(new int[] {5, 6}), 0);
		stream.addElement(new Simplex(new int[] {2, 6}), 0);
		
		stream.finalizeStream();
		
		return stream;
	}
	
	public static double[][] getFigure8Vertices() {
		double[][] points = new double[7][2];
		
		points[0] = new double[]{0, 2};
		points[1] = new double[]{-1, 1};
		points[2] = new double[]{0, 0};
		points[3] = new double[]{1, 1};
		points[4] = new double[]{-1, -1};
		points[5] = new double[]{0, -2};
		points[6] = new double[]{1, -1};
		
		return points;
	}
}
