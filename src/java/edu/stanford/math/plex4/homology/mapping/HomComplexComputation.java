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
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.streams.derived.HomStream;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.homology.streams.utility.StreamUtility;
import edu.stanford.math.plex4.io2.MatlabInterface;
import edu.stanford.math.plex4.io2.MatlabWriter;
import edu.stanford.math.plex4.math.matrix.impl.sparse.DoubleSparseVector;
import edu.stanford.math.plex4.optimization.NesterovGradientOptimizer;
import edu.stanford.math.plex4.utility.RandomUtility;
import gnu.trove.TObjectDoubleIterator;


public class HomComplexComputation<F extends Number> {
	private final GenericOrderedField<F> field;
	private final AbstractGenericFreeModule<F, GenericPair<Simplex, Simplex>> genericChainModule;
	private final DoubleFreeModule<GenericPair<Simplex, Simplex>> doubleChainModule = new DoubleFreeModule<GenericPair<Simplex, Simplex>>();

	private final AbstractFilteredStream<Simplex> domainStream;
	private final AbstractFilteredStream<Simplex> codomainStream;
	private final Comparator<Simplex> domainComparator;
	private final Comparator<Simplex> codomainComparator;

	private final HomStream<Simplex, Simplex> homStream;

	public HomComplexComputation(AbstractFilteredStream<Simplex> domainStream, 
			AbstractFilteredStream<Simplex> codomainStream, 
			Comparator<Simplex> domainComparator, 
			Comparator<Simplex> codomainComparator, 
			GenericOrderedField<F> field) {

		this.domainStream = domainStream;
		this.codomainStream = codomainStream;
		this.domainComparator = domainComparator;
		this.codomainComparator = codomainComparator;

		this.field = field;
		genericChainModule = new UnorderedGenericFreeModule<F, GenericPair<Simplex, Simplex>>(this.field);

		this.homStream = new HomStream<Simplex, Simplex>(this.domainStream, this.codomainStream, this.domainComparator, this.codomainComparator);
		homStream.finalizeStream();
	}

	public List<AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>>> computeGeneratingCycles() {
		//GenericPersistentHomologyOld<F, GenericPair<M, N>> homology = new GenericPersistentHomologyOld<F, GenericPair<M, N>>(this.field, homStream.getDerivedComparator(), 1);
		GenericAbsoluteHomology<F, GenericPair<Simplex, Simplex>> homology = new GenericAbsoluteHomology<F, GenericPair<Simplex, Simplex>>(this.field, homStream.getDerivedComparator(), 1);
		AugmentedBarcodeCollection<AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>>> barcodes = homology.computeAugmentedIntervals(homStream);

		List<AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>>> generatingCycles = new ArrayList<AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>>>();

		int numCycles = barcodes.getBarcode(0).getSize();
		for (int i = 0; i < numCycles; i++) {
			generatingCycles.add(barcodes.getBarcode(0).getGeneratingCycle(i));
		}

		return generatingCycles;
	}

	public AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>> sumGeneratingCycles(List<AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>>> generatingCycles) {
		AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>> sum = this.genericChainModule.createNewSum();
		int numCycles = generatingCycles.size();

		for (int i = 0; i < numCycles; i++) {
			sum = this.genericChainModule.add(sum, generatingCycles.get(i));
		}

		return sum;
	}

	public List<AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>>> getChainHomotopies() {
		GenericAbsoluteHomology<F, GenericPair<Simplex, Simplex>> homology = new GenericAbsoluteHomology<F, GenericPair<Simplex, Simplex>>(this.field, homStream.getDerivedComparator(), 1);

		return homology.getBoundaryColumns(this.homStream, 1);
	}

	private DoubleFormalSum<GenericPair<Simplex, Simplex>> computeHomCycle(double[] homotopyCoefficients,
			DoubleFormalSum<GenericPair<Simplex, Simplex>> generatingCycle,
			List<DoubleFormalSum<GenericPair<Simplex, Simplex>>> homotopies) {

		DoubleFormalSum<GenericPair<Simplex, Simplex>> homCycle = new DoubleFormalSum<GenericPair<Simplex, Simplex>>();

		homCycle = doubleChainModule.add(homCycle, generatingCycle);

		int i = 0;
		for (DoubleFormalSum<GenericPair<Simplex, Simplex>> homotopy: homotopies) {
			homCycle = doubleChainModule.add(homCycle, doubleChainModule.multiply(homotopyCoefficients[i], homotopy));
			i++;
		}

		return homCycle;		
	}

	private DoubleFormalSum<GenericPair<Simplex, Simplex>> computeHomCycle(double[] homotopyCoefficients,
			AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>> generatingCycle,
			List<AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>>> homotopies) {
		return this.computeHomCycle(homotopyCoefficients, MappingUtility.toDoubleFormalSum(generatingCycle), MappingUtility.toDoubleFormalSumList(homotopies));
	}

	public RealPointValuePair findOptimalCoefficients(final AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>> generatingCycle, 
			final List<AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>>> homotopies,
			final GenericDoubleFunction<DoubleFormalSum<GenericPair<Simplex, Simplex>>> mappingPenaltyFunction) throws OptimizationException, FunctionEvaluationException, IllegalArgumentException {

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

	public DoubleFormalSum<GenericPair<Simplex, Simplex>> findOptimalChainMap(final AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>> generatingCycle, 
			final List<AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>>> homotopies,
			final GenericDoubleFunction<DoubleFormalSum<GenericPair<Simplex, Simplex>>> mappingPenaltyFunction) throws OptimizationException, FunctionEvaluationException, IllegalArgumentException {

		RealPointValuePair pair = this.findOptimalCoefficients(generatingCycle, homotopies, mappingPenaltyFunction);
		return this.computeHomCycle(MappingUtility.round(pair.getPoint()), generatingCycle, homotopies);
	}

	MultivariateRealFunction getObjectiveFunctionViaMappingPenalty(final AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>> generatingCycle, 
			final List<AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>>> homotopies,
			final GenericDoubleFunction<DoubleFormalSum<GenericPair<Simplex, Simplex>>> mappingPenaltyFunction) {

		return new MultivariateRealFunction() {

			public double value(double[] arg0) throws FunctionEvaluationException, IllegalArgumentException {
				DoubleFormalSum<GenericPair<Simplex, Simplex>> homCycle = computeHomCycle(arg0, generatingCycle, homotopies);
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

	MultivariateRealFunction getObjectiveFunctionViaImagePenalty(final AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>> generatingCycle, 
			final List<AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>>> homotopies,
			final GenericDoubleFunction<DoubleFormalSum<Simplex>> imagePenaltyFunction) {

		return getObjectiveFunctionViaMappingPenalty(generatingCycle, homotopies, MappingUtility.computeInducedFunction(imagePenaltyFunction, domainStream));
	}

	public void produceMatlabOutput() throws IOException {
		//String filename = FileManager.getUniqueFilePath("hom", "m");
		MatlabWriter writer = MatlabInterface.makeNewMatlabWriter("hom_data");
		NumericModuleMorphismRepresentation<F, Simplex, Simplex> rep = new NumericModuleMorphismRepresentation<F, Simplex, Simplex>(this.domainStream, this.codomainStream);
		System.out.println("Computing Generating Cycles");
		List<AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>>> generatingCycles = this.computeGeneratingCycles();
		AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>> cycleSum = this.sumGeneratingCycles(generatingCycles);
		List<AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>>> homotopies = this.getChainHomotopies();
		writer.writeClearAll();
		writer.turnOnCommentMode();
		writer.writeLine("Generating Cycles");
		for (AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>> generatingCycle: generatingCycles) {
			writer.writeLine(generatingCycle.toString());
		}
		writer.writeLine("Cycle Sum");
		writer.writeLine(cycleSum.toString());
		writer.writeLine("Homotopies");
		for (AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>> homotopy: homotopies) {
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
		for (AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>> homotopy: homotopies) {
			writer.writeSparseMatrix(rep.toDoubleMatrix(homotopy), "homotopies{ " + i + "}");
			writer.newLine();
			i++;

		}
		
		writer.write("domain_aw_maps = sparse(domain_dimension^2, domain_dimension);");
		writer.newLine();
		writer.write("codomain_aw_maps = sparse(codomain_dimension^2, codomain_dimension);");
		writer.newLine();
		
		int N = rep.getDomainRepresentation().getDimension();
		
		for (Simplex element: this.domainStream) {
			DoubleFormalSum<GenericPair<Simplex, Simplex>> sum = MappingUtility.computeAlexanderWhitneyMap(element);
			int col = rep.getDomainRepresentation().getIndex(element) + 1;
			for (TObjectDoubleIterator<GenericPair<Simplex, Simplex>> iterator = sum.iterator(); iterator.hasNext(); ) {
				iterator.advance();
				GenericPair<Simplex, Simplex> pair = iterator.key();
				int c1 = rep.getDomainRepresentation().getIndex(pair.getFirst());
				int c2 = rep.getDomainRepresentation().getIndex(pair.getSecond());
				int row = this.flattenIndex(c1, c2, N, N) + 1;
				writer.write("domain_aw_maps(" + row + ", " + col + ") = 1;");
				writer.newLine();
			}
		}
		
		N = rep.getCodomainRepresentation().getDimension();
		
		for (Simplex element: this.codomainStream) {
			DoubleFormalSum<GenericPair<Simplex, Simplex>> sum = MappingUtility.computeAlexanderWhitneyMap(element);
			int col = rep.getCodomainRepresentation().getIndex(element) + 1;
			for (TObjectDoubleIterator<GenericPair<Simplex, Simplex>> iterator = sum.iterator(); iterator.hasNext(); ) {
				iterator.advance();
				GenericPair<Simplex, Simplex> pair = iterator.key();
				int c1 = rep.getCodomainRepresentation().getIndex(pair.getFirst());
				int c2 = rep.getCodomainRepresentation().getIndex(pair.getSecond());
				int row = this.flattenIndex(c1, c2, N, N) + 1;
				writer.write("codomain_aw_maps(" + row + ", " + col + ") = 1;");
				writer.newLine();
			}
		}
		
		writer.close();
	}
	
	public void produceMatlabOutput2() throws IOException {
		//String filename = FileManager.getUniqueFilePath("hom", "m");
		MatlabWriter writer = MatlabInterface.makeNewMatlabWriter("hom_data2");
		//ModuleMorphismRepresentation<F, M, N> rep = new ModuleMorphismRepresentation<F, M, N>(this.domainStream, this.codomainStream);
		NumericFreeModuleRepresentation<F, GenericPair<Simplex, Simplex>> rep = new NumericFreeModuleRepresentation<F, GenericPair<Simplex, Simplex>>(this.homStream);
		System.out.println("Computing Generating Cycles");
		List<AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>>> generatingCycles = this.computeGeneratingCycles();
		AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>> cycleSum = this.sumGeneratingCycles(generatingCycles);
		List<AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>>> homotopies = this.getChainHomotopies();
		writer.writeClearAll();
		writer.turnOnCommentMode();
		rep.toString();
		
		writer.turnOffCommentMode();
		
		writer.writeSparseVector(rep.toDoubleArray(cycleSum), "cycle_sum");
		
		//double[][] homotopyMatrix = this.createHomotopyMatrix(homotopies, rep);
		//writer.writeSparseMatrix(homotopyMatrix, "homotopies");
		
		List<DoubleSparseVector> constraints = new ArrayList<DoubleSparseVector>();
		
		for (AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>> homotopy: homotopies) {
			
		}
		
		writer.close();
	}
	
	private int flattenIndex(int i, int j, int I, int J) {
		return i * J + j;
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
	
	private double[][] createHomotopyMatrix(List<AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>>> homotopies, NumericFreeModuleRepresentation<F, GenericPair<Simplex, Simplex>> rep) {
		int m = homotopies.size();
		int n = rep.getDimension();
		double[][] matrix = ArrayCreation.newDoubleMatrix(m, n);
		int j = 0;
		for (AbstractGenericFormalSum<F, GenericPair<Simplex, Simplex>> homotopy: homotopies) {
			double[] col = rep.toDoubleArray(homotopy);
			for (int i = 0; i < m; i++) {
				matrix[i][j] = col[i];
			}
			j++;
		}
		return matrix;
	}
}

