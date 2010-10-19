package edu.stanford.math.plex4.deprecated_tests;

import java.io.IOException;

import edu.stanford.math.plex4.algebraic_structures.impl.RationalField;
import edu.stanford.math.plex4.algebraic_structures.interfaces.GenericOrderedField;
import edu.stanford.math.plex4.datastructures.pairs.GenericPair;
import edu.stanford.math.plex4.examples.SimplexStreamExamples;
import edu.stanford.math.plex4.free_module.DoubleFormalSum;
import edu.stanford.math.plex4.functional.GenericDoubleFunction;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.mapping.HomComplexComputation;
import edu.stanford.math.plex4.homology.mapping.MappingUtility;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;

public class HomComplexTest {
	public static void main(String[] args) {
		simplicialHomTest(RationalField.getInstance());
	}

	public static <F extends Number> void simplicialHomTest(GenericOrderedField<F> field) {
		//AbstractFilteredStream<Simplex> domainStream = SimplexStreamExamples.getTorus();
		//AbstractFilteredStream<Simplex> codomainStream = SimplexStreamExamples.getTorus();

		AbstractFilteredStream<Simplex> domainStream = SimplexStreamExamples.getCircle(20);
		AbstractFilteredStream<Simplex> codomainStream = SimplexStreamExamples.getCircle(20);

		HomComplexComputation<F, Simplex, Simplex> computation = new HomComplexComputation<F, Simplex, Simplex>(domainStream, codomainStream, SimplexComparator.getInstance(), SimplexComparator.getInstance(), field);

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
}
