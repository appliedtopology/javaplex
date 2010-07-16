package edu.stanford.math.plex_plus.homology;

import java.util.List;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;

import edu.stanford.math.plex_plus.algebraic_structures.impl.RationalField;
import edu.stanford.math.plex_plus.algebraic_structures.interfaces.GenericOrderedField;
import edu.stanford.math.plex_plus.datastructures.DoubleFormalSum;
import edu.stanford.math.plex_plus.datastructures.pairs.GenericPair;
import edu.stanford.math.plex_plus.examples.SimplexStreamExamples;
import edu.stanford.math.plex_plus.free_module.AbstractGenericFormalSum;
import edu.stanford.math.plex_plus.functional.GenericDoubleFunction;
import edu.stanford.math.plex_plus.homology.chain_basis.Simplex;
import edu.stanford.math.plex_plus.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex_plus.homology.mapping.HomComplexComputation;
import edu.stanford.math.plex_plus.homology.mapping.MappingUtility;
import edu.stanford.math.plex_plus.homology.streams.interfaces.AbstractFilteredStream;

public class HomComplexTest {
	public static void main(String[] args) {
		simplicialHomTest(RationalField.getInstance());
	}
	
	public static <F extends Number> void simplicialHomTest(GenericOrderedField<F> field) {
		AbstractFilteredStream<Simplex> domainStream = SimplexStreamExamples.getCircle(4);
		AbstractFilteredStream<Simplex> codomainStream = SimplexStreamExamples.getCircle(4);
		
		HomComplexComputation<F, Simplex, Simplex> computation = new HomComplexComputation<F, Simplex, Simplex>(domainStream, codomainStream, SimplexComparator.getInstance(), SimplexComparator.getInstance(), field);
		
		System.out.println("Computing Generating Cycles");
		List<AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>>> generatingCycles = computation.computeGeneratingCycles();
		
		for (AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>> generatingCycle: generatingCycles) {
			System.out.println(generatingCycle);
		}
		
		AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>> cycleSum = computation.sumGeneratingCycles(generatingCycles);
		
		System.out.println("Computing Homotopies: ");
		List<AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>>> homotopies = computation.getChainHomotopies();
		System.out.println("Homotopy set size: " + homotopies.size());
		
		for (AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>> homotopy: homotopies) {
			System.out.println(homotopy);
		}
		
		
		//GenericDoubleFunction<DoubleFormalSum<Simplex>> imagePenaltyFunction = MappingUtility.getNormFunction(1);
		//GenericDoubleFunction<DoubleFormalSum<GenericPair<Simplex, Simplex>>> mappingPenaltyFunction = MappingUtility.computeInducedFunction(imagePenaltyFunction, domainStream);
		
		
		GenericDoubleFunction<DoubleFormalSum<GenericPair<Simplex, Simplex>>> mappingPenaltyFunction = MappingUtility.getSimplicialityLossFunction(domainStream, codomainStream);
		
		try {
			System.out.println("Computing Optimal Cycle");
			DoubleFormalSum<GenericPair<Simplex, Simplex>> optimalChainMap = computation.findOptimalChainMap(cycleSum, homotopies, mappingPenaltyFunction);
			//optimalChainMap = MappingUtility.roundCoefficients(optimalChainMap);
			System.out.println(optimalChainMap);
			System.out.println(mappingPenaltyFunction.evaluate(optimalChainMap));
			
		} catch (OptimizationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FunctionEvaluationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
