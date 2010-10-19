package edu.stanford.math.plex4.homology.mapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.MultivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.MultivariateRealOptimizer;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.SimpleScalarValueChecker;
import org.apache.commons.math.random.GaussianRandomGenerator;
import org.apache.commons.math.random.MersenneTwister;
import org.apache.commons.math.random.RandomVectorGenerator;
import org.apache.commons.math.random.UncorrelatedRandomVectorGenerator;

import edu.stanford.math.plex4.algebraic_structures.interfaces.GenericOrderedField;
import edu.stanford.math.plex4.array_utility.ArrayCreation;
import edu.stanford.math.plex4.datastructures.pairs.GenericPair;
import edu.stanford.math.plex4.free_module.AbstractGenericFormalSum;
import edu.stanford.math.plex4.free_module.AbstractGenericFreeModule;
import edu.stanford.math.plex4.free_module.DoubleFormalSum;
import edu.stanford.math.plex4.free_module.DoubleFreeModule;
import edu.stanford.math.plex4.free_module.NumericFreeModuleRepresentation;
import edu.stanford.math.plex4.free_module.NumericModuleMorphismRepresentation;
import edu.stanford.math.plex4.free_module.UnorderedGenericFreeModule;
import edu.stanford.math.plex4.functional.GenericDoubleFunction;
import edu.stanford.math.plex4.homology.GenericAbsoluteHomology;
import edu.stanford.math.plex4.homology.barcodes.AugmentedBarcodeCollection;
import edu.stanford.math.plex4.homology.streams.derived.HomStream;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.homology.streams.utility.StreamUtility;
import edu.stanford.math.plex4.io2.MatlabInterface;
import edu.stanford.math.plex4.io2.MatlabWriter;
import edu.stanford.math.plex4.math.matrix.impl.sparse.DoubleSparseVector;
import edu.stanford.math.plex4.optimization.NesterovGradientOptimizer;
import edu.stanford.math.plex4.utility.RandomUtility;


public class HomComplexComputation<F extends Number, M, N> {
	private final GenericOrderedField<F> field;
	private final AbstractGenericFreeModule<F, GenericPair<M, N>> genericChainModule;
	private final DoubleFreeModule<GenericPair<M, N>> doubleChainModule = new DoubleFreeModule<GenericPair<M, N>>();

	private final AbstractFilteredStream<M> domainStream;
	private final AbstractFilteredStream<N> codomainStream;
	private final Comparator<M> domainComparator;
	private final Comparator<N> codomainComparator;

	private final HomStream<M, N> homStream;

	public HomComplexComputation(AbstractFilteredStream<M> domainStream, 
			AbstractFilteredStream<N> codomainStream, 
			Comparator<M> domainComparator, 
			Comparator<N> codomainComparator, 
			GenericOrderedField<F> field) {

		this.domainStream = domainStream;
		this.codomainStream = codomainStream;
		this.domainComparator = domainComparator;
		this.codomainComparator = codomainComparator;

		this.field = field;
		genericChainModule = new UnorderedGenericFreeModule<F, GenericPair<M, N>>(this.field);

		this.homStream = new HomStream<M, N>(this.domainStream, this.codomainStream, this.domainComparator, this.codomainComparator);
		homStream.finalizeStream();
	}

	public List<AbstractGenericFormalSum<F, GenericPair<M, N>>> computeGeneratingCycles() {
		//GenericPersistentHomologyOld<F, GenericPair<M, N>> homology = new GenericPersistentHomologyOld<F, GenericPair<M, N>>(this.field, homStream.getDerivedComparator(), 1);
		GenericAbsoluteHomology<F, GenericPair<M, N>> homology = new GenericAbsoluteHomology<F, GenericPair<M, N>>(this.field, homStream.getDerivedComparator(), 1);
		AugmentedBarcodeCollection<AbstractGenericFormalSum<F, GenericPair<M, N>>> barcodes = homology.computeAugmentedIntervals(homStream);

		List<AbstractGenericFormalSum<F, GenericPair<M, N>>> generatingCycles = new ArrayList<AbstractGenericFormalSum<F, GenericPair<M, N>>>();

		int numCycles = barcodes.getBarcode(0).getSize();
		for (int i = 0; i < numCycles; i++) {
			generatingCycles.add(barcodes.getBarcode(0).getGeneratingCycle(i));
		}

		return generatingCycles;
	}

	public AbstractGenericFormalSum<F, GenericPair<M, N>> sumGeneratingCycles(List<AbstractGenericFormalSum<F, GenericPair<M, N>>> generatingCycles) {
		AbstractGenericFormalSum<F, GenericPair<M, N>> sum = this.genericChainModule.createNewSum();
		int numCycles = generatingCycles.size();

		for (int i = 0; i < numCycles; i++) {
			sum = this.genericChainModule.add(sum, generatingCycles.get(i));
		}

		return sum;
	}

	public List<AbstractGenericFormalSum<F, GenericPair<M, N>>> getChainHomotopies() {
		GenericAbsoluteHomology<F, GenericPair<M, N>> homology = new GenericAbsoluteHomology<F, GenericPair<M, N>>(this.field, homStream.getDerivedComparator(), 1);

		return homology.getBoundaryColumns(this.homStream, 1);
	}

	private DoubleFormalSum<GenericPair<M, N>> computeHomCycle(double[] homotopyCoefficients,
			DoubleFormalSum<GenericPair<M, N>> generatingCycle,
			List<DoubleFormalSum<GenericPair<M, N>>> homotopies) {

		DoubleFormalSum<GenericPair<M, N>> homCycle = new DoubleFormalSum<GenericPair<M, N>>();

		homCycle = doubleChainModule.add(homCycle, generatingCycle);

		int i = 0;
		for (DoubleFormalSum<GenericPair<M, N>> homotopy: homotopies) {
			homCycle = doubleChainModule.add(homCycle, doubleChainModule.multiply(homotopyCoefficients[i], homotopy));
			i++;
		}

		return homCycle;		
	}

	private DoubleFormalSum<GenericPair<M, N>> computeHomCycle(double[] homotopyCoefficients,
			AbstractGenericFormalSum<F, GenericPair<M, N>> generatingCycle,
			List<AbstractGenericFormalSum<F, GenericPair<M, N>>> homotopies) {
		return this.computeHomCycle(homotopyCoefficients, MappingUtility.toDoubleFormalSum(generatingCycle), MappingUtility.toDoubleFormalSumList(homotopies));
	}

	public RealPointValuePair findOptimalCoefficients(final AbstractGenericFormalSum<F, GenericPair<M, N>> generatingCycle, 
			final List<AbstractGenericFormalSum<F, GenericPair<M, N>>> homotopies,
			final GenericDoubleFunction<DoubleFormalSum<GenericPair<M, N>>> mappingPenaltyFunction) throws OptimizationException, FunctionEvaluationException, IllegalArgumentException {

		MultivariateRealFunction objective = this.getObjectiveFunctionViaMappingPenalty(generatingCycle, homotopies, mappingPenaltyFunction);

		//System.out.println("Convex: " + ConvexUtility.randomizedConvexityTest(objective, homotopies.size(), 1000));

		//MultivariateRealOptimizer optimizer = new NelderMead();
		RandomVectorGenerator generator = new UncorrelatedRandomVectorGenerator(homotopies.size(), new GaussianRandomGenerator(new MersenneTwister()));
		//MultivariateRealOptimizer optimizer = new MultiStartMultivariateRealOptimizer(new NelderMead(), 1, generator);

		//DiscreteOptimization discreteOptimizer = new DiscreteOptimization();
		double tolerance = 1e-7;
		RealConvergenceChecker checker = new SimpleScalarValueChecker(tolerance, tolerance);

		MultivariateRealOptimizer optimizer = new NesterovGradientOptimizer(checker, 0, 0);


		double initialValue = objective.value(new double[homotopies.size()]);
		System.out.println(initialValue);

		RealPointValuePair optimum = optimizer.optimize(objective, GoalType.MINIMIZE, RandomUtility.normalArray(homotopies.size()));

		//RealPointValuePair optimum = discreteOptimizer.multistartOptimize(objective, homotopies.size(), 20);

		System.out.println(optimum.getValue());
		System.out.println(MatlabWriter.toRowVector(optimum.getPoint()));

		return optimum;
	}

	public DoubleFormalSum<GenericPair<M, N>> findOptimalChainMap(final AbstractGenericFormalSum<F, GenericPair<M, N>> generatingCycle, 
			final List<AbstractGenericFormalSum<F, GenericPair<M, N>>> homotopies,
			final GenericDoubleFunction<DoubleFormalSum<GenericPair<M, N>>> mappingPenaltyFunction) throws OptimizationException, FunctionEvaluationException, IllegalArgumentException {

		RealPointValuePair pair = this.findOptimalCoefficients(generatingCycle, homotopies, mappingPenaltyFunction);
		return this.computeHomCycle(MappingUtility.round(pair.getPoint()), generatingCycle, homotopies);
	}

	MultivariateRealFunction getObjectiveFunctionViaMappingPenalty(final AbstractGenericFormalSum<F, GenericPair<M, N>> generatingCycle, 
			final List<AbstractGenericFormalSum<F, GenericPair<M, N>>> homotopies,
			final GenericDoubleFunction<DoubleFormalSum<GenericPair<M, N>>> mappingPenaltyFunction) {

		return new MultivariateRealFunction() {

			public double value(double[] arg0) throws FunctionEvaluationException, IllegalArgumentException {
				DoubleFormalSum<GenericPair<M, N>> homCycle = computeHomCycle(arg0, generatingCycle, homotopies);
				return mappingPenaltyFunction.evaluate(homCycle) + 0 * computePenalty(arg0);
			}
		};
	}

	private double computePenalty(double[] arg0) {
		double penalty = 0;

		for (int i = 0; i < arg0.length; i++) {
			penalty += Math.cos(i) * arg0[i];
		}

		return penalty;
	}

	MultivariateRealFunction getObjectiveFunctionViaImagePenalty(final AbstractGenericFormalSum<F, GenericPair<M, N>> generatingCycle, 
			final List<AbstractGenericFormalSum<F, GenericPair<M, N>>> homotopies,
			final GenericDoubleFunction<DoubleFormalSum<N>> imagePenaltyFunction) {

		return getObjectiveFunctionViaMappingPenalty(generatingCycle, homotopies, MappingUtility.computeInducedFunction(imagePenaltyFunction, domainStream));
	}

	public void produceMatlabOutput() throws IOException {
		//String filename = FileManager.getUniqueFilePath("hom", "m");
		MatlabWriter writer = MatlabInterface.makeNewMatlabWriter("hom_data");
		NumericModuleMorphismRepresentation<F, M, N> rep = new NumericModuleMorphismRepresentation<F, M, N>(this.domainStream, this.codomainStream);
		System.out.println("Computing Generating Cycles");
		List<AbstractGenericFormalSum<F, GenericPair<M, N>>> generatingCycles = this.computeGeneratingCycles();
		AbstractGenericFormalSum<F, GenericPair<M, N>> cycleSum = this.sumGeneratingCycles(generatingCycles);
		List<AbstractGenericFormalSum<F, GenericPair<M, N>>> homotopies = this.getChainHomotopies();
		writer.writeClearAll();
		writer.turnOnCommentMode();
		writer.writeLine("Generating Cycles");
		for (AbstractGenericFormalSum<F, GenericPair<M, N>> generatingCycle: generatingCycles) {
			writer.writeLine(generatingCycle.toString());
		}
		writer.writeLine("Cycle Sum");
		writer.writeLine(cycleSum.toString());
		writer.writeLine("Homotopies");
		for (AbstractGenericFormalSum<F, GenericPair<M, N>> homotopy: homotopies) {
			writer.writeLine(homotopy.toString());
		}
		writer.writeLine("Domain Basis");
		for (int i = 0; i < rep.getDomainRepresentation().getDimension(); i++) {
			writer.writeLine(i + ": " + rep.getDomainRepresentation().getBasisElement(i));
		}
		writer.writeLine("Codomain Basis");
		for (int i = 0; i < rep.getCodomainRepresentation().getDimension(); i++) {
			writer.writeLine(i + ": " + rep.getCodomainRepresentation().getBasisElement(i));
		}
		writer.turnOffCommentMode();
		writer.assignValue("domain_dimension", rep.getDomainRepresentation().getDimension());
		writer.newLine();
		writer.assignValue("codomain_dimension", rep.getCodomainRepresentation().getDimension());
		writer.newLine();
		writer.assignValue("homotopies_dimension", homotopies.size());
		writer.newLine();
		writer.assignValue("domain_vertices", StreamUtility.getSkeletonSize(domainStream, 0));
		writer.newLine();
		writer.assignValue("codomain_vertices", StreamUtility.getSkeletonSize(codomainStream, 0));
		writer.newLine();
		writer.writeSparseMatrix(rep.toDoubleMatrix(cycleSum), "cycle_sum");
		writer.newLine();
		writer.write("homotopies = cell(homotopies_dimension, 1);");
		writer.newLine();
		int i = 1;
		for (AbstractGenericFormalSum<F, GenericPair<M, N>> homotopy: homotopies) {
			writer.writeSparseMatrix(rep.toDoubleMatrix(homotopy), "homotopies{ " + i + "}");
			writer.newLine();
			i++;

		}
		writer.close();
	}
	
	public void produceMatlabOutput2() throws IOException {
		//String filename = FileManager.getUniqueFilePath("hom", "m");
		MatlabWriter writer = MatlabInterface.makeNewMatlabWriter("hom_data2");
		//ModuleMorphismRepresentation<F, M, N> rep = new ModuleMorphismRepresentation<F, M, N>(this.domainStream, this.codomainStream);
		NumericFreeModuleRepresentation<F, GenericPair<M, N>> rep = new NumericFreeModuleRepresentation<F, GenericPair<M, N>>(this.homStream);
		System.out.println("Computing Generating Cycles");
		List<AbstractGenericFormalSum<F, GenericPair<M, N>>> generatingCycles = this.computeGeneratingCycles();
		AbstractGenericFormalSum<F, GenericPair<M, N>> cycleSum = this.sumGeneratingCycles(generatingCycles);
		List<AbstractGenericFormalSum<F, GenericPair<M, N>>> homotopies = this.getChainHomotopies();
		writer.writeClearAll();
		writer.turnOnCommentMode();
		rep.toString();
		
		writer.turnOffCommentMode();
		
		writer.writeSparseVector(rep.toDoubleArray(cycleSum), "cycle_sum");
		
		//double[][] homotopyMatrix = this.createHomotopyMatrix(homotopies, rep);
		//writer.writeSparseMatrix(homotopyMatrix, "homotopies");
		
		List<DoubleSparseVector> constraints = new ArrayList<DoubleSparseVector>();
		
		for (AbstractGenericFormalSum<F, GenericPair<M, N>> homotopy: homotopies) {
			
		}
		
		writer.close();
	}
	
	private double[] flatten(double[][] matrix) {
		int m = matrix.length;
		int n = matrix[0].length;
		double[] array = new double[m * n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				array[i * n + j] = matrix[i][j];
			}
		}
		
		return array;
	}
	
	private double[][] createHomotopyMatrix(List<AbstractGenericFormalSum<F, GenericPair<M, N>>> homotopies, NumericFreeModuleRepresentation<F, GenericPair<M, N>> rep) {
		int m = homotopies.size();
		int n = rep.getDimension();
		double[][] matrix = ArrayCreation.newDoubleMatrix(m, n);
		int j = 0;
		for (AbstractGenericFormalSum<F, GenericPair<M, N>> homotopy: homotopies) {
			double[] col = rep.toDoubleArray(homotopy);
			for (int i = 0; i < m; i++) {
				matrix[i][j] = col[i];
			}
			j++;
		}
		return matrix;
	}
}

