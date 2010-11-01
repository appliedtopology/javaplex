package edu.stanford.math.plex4.homology.mapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import edu.stanford.math.plex4.homology.GenericAbsoluteHomology;
import edu.stanford.math.plex4.homology.barcodes.AugmentedBarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.streams.derived.HomStream;
import edu.stanford.math.plex4.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex4.homology.streams.utility.StreamUtility;
import edu.stanford.math.plex4.io2.MatlabInterface;
import edu.stanford.math.plex4.io2.MatlabWriter;
import edu.stanford.math.primitivelib.autogen.algebraic.ObjectAbstractField;
import edu.stanford.math.primitivelib.autogen.array.DoubleArrayUtility;
import edu.stanford.math.primitivelib.autogen.formal_sum.DoubleMatrixConverter;
import edu.stanford.math.primitivelib.autogen.formal_sum.DoublePrimitiveFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.DoubleSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.formal_sum.DoubleVectorConverter;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.matrix.DoubleSparseVector;
import edu.stanford.math.primitivelib.autogen.pair.ObjectObjectPair;
import gnu.trove.TObjectDoubleIterator;


public class HomComplexComputation<F extends Number> {
	private final ObjectAbstractField<F> field;
	private final ObjectAlgebraicFreeModule<F, ObjectObjectPair<Simplex, Simplex>> genericChainModule;
	private final DoublePrimitiveFreeModule<ObjectObjectPair<Simplex, Simplex>> doubleChainModule = new DoublePrimitiveFreeModule<ObjectObjectPair<Simplex, Simplex>>();

	private final AbstractFilteredStream<Simplex> domainStream;
	private final AbstractFilteredStream<Simplex> codomainStream;
	private final Comparator<Simplex> domainComparator;
	private final Comparator<Simplex> codomainComparator;

	private final HomStream<Simplex, Simplex> homStream;

	public HomComplexComputation(AbstractFilteredStream<Simplex> domainStream, 
			AbstractFilteredStream<Simplex> codomainStream, 
			Comparator<Simplex> domainComparator, 
			Comparator<Simplex> codomainComparator, 
			ObjectAbstractField<F> field) {

		this.domainStream = domainStream;
		this.codomainStream = codomainStream;
		this.domainComparator = domainComparator;
		this.codomainComparator = codomainComparator;

		this.field = field;
		genericChainModule = new ObjectAlgebraicFreeModule<F, ObjectObjectPair<Simplex, Simplex>>(this.field);

		this.homStream = new HomStream<Simplex, Simplex>(this.domainStream, this.codomainStream, this.domainComparator, this.codomainComparator);
		homStream.finalizeStream();
	}

	public List<ObjectSparseFormalSum<F, ObjectObjectPair<Simplex, Simplex>>> computeGeneratingCycles() {
		//GenericPersistentHomologyOld<F, ObjectObjectPair<M, N>> homology = new GenericPersistentHomologyOld<F, ObjectObjectPair<M, N>>(this.field, homStream.getDerivedComparator(), 1);
		GenericAbsoluteHomology<F, ObjectObjectPair<Simplex, Simplex>> homology = new GenericAbsoluteHomology<F, ObjectObjectPair<Simplex, Simplex>>(this.field, homStream.getDerivedComparator(), 1);
		AugmentedBarcodeCollection<ObjectSparseFormalSum<F, ObjectObjectPair<Simplex, Simplex>>> barcodes = homology.computeAugmentedIntervals(homStream);

		List<ObjectSparseFormalSum<F, ObjectObjectPair<Simplex, Simplex>>> generatingCycles = new ArrayList<ObjectSparseFormalSum<F, ObjectObjectPair<Simplex, Simplex>>>();

		int numCycles = barcodes.getBarcode(0).getSize();
		for (int i = 0; i < numCycles; i++) {
			generatingCycles.add(barcodes.getBarcode(0).getGeneratingCycle(i));
		}

		return generatingCycles;
	}

	public ObjectSparseFormalSum<F, ObjectObjectPair<Simplex, Simplex>> sumGeneratingCycles(List<ObjectSparseFormalSum<F, ObjectObjectPair<Simplex, Simplex>>> generatingCycles) {
		ObjectSparseFormalSum<F, ObjectObjectPair<Simplex, Simplex>> sum = this.genericChainModule.createNewSum();
		int numCycles = generatingCycles.size();

		for (int i = 0; i < numCycles; i++) {
			sum = this.genericChainModule.add(sum, generatingCycles.get(i));
		}

		return sum;
	}

	public List<ObjectSparseFormalSum<F, ObjectObjectPair<Simplex, Simplex>>> getChainHomotopies() {
		GenericAbsoluteHomology<F, ObjectObjectPair<Simplex, Simplex>> homology = new GenericAbsoluteHomology<F, ObjectObjectPair<Simplex, Simplex>>(this.field, homStream.getDerivedComparator(), 1);

		return homology.getBoundaryColumns(this.homStream, 1);
	}

	private DoubleSparseFormalSum<ObjectObjectPair<Simplex, Simplex>> computeHomCycle(double[] homotopyCoefficients,
			DoubleSparseFormalSum<ObjectObjectPair<Simplex, Simplex>> generatingCycle,
			List<DoubleSparseFormalSum<ObjectObjectPair<Simplex, Simplex>>> homotopies) {

		DoubleSparseFormalSum<ObjectObjectPair<Simplex, Simplex>> homCycle = doubleChainModule.createNewSum();

		homCycle = doubleChainModule.add(homCycle, generatingCycle);

		int i = 0;
		for (DoubleSparseFormalSum<ObjectObjectPair<Simplex, Simplex>> homotopy: homotopies) {
			homCycle = doubleChainModule.add(homCycle, doubleChainModule.multiply(homotopyCoefficients[i], homotopy));
			i++;
		}

		return homCycle;		
	}
/*
	private DoubleSparseFormalSum<ObjectObjectPair<Simplex, Simplex>> computeHomCycle(double[] homotopyCoefficients,
			ObjectSparseFormalSum<F, ObjectObjectPair<Simplex, Simplex>> generatingCycle,
			List<ObjectSparseFormalSum<F, ObjectObjectPair<Simplex, Simplex>>> homotopies) {
		return this.computeHomCycle(homotopyCoefficients, MappingUtility.toDoubleFormalSum(generatingCycle), MappingUtility.toDoubleFormalSumList(homotopies));
	}
*/
	/*
	public RealPointValuePair findOptimalCoefficients(final ObjectSparseFormalSum<F, ObjectObjectPair<Simplex, Simplex>> generatingCycle, 
			final List<ObjectSparseFormalSum<F, ObjectObjectPair<Simplex, Simplex>>> homotopies,
			final GenericDoubleFunction<DoubleSparseFormalSum<ObjectObjectPair<Simplex, Simplex>>> mappingPenaltyFunction) throws OptimizationException, FunctionEvaluationException, IllegalArgumentException {

		MultivariateRealFunction objective = this.getObjectiveFunctionViaMappingPenalty(generatingCycle, homotopies, mappingPenaltyFunction);

		//System.out.println("Convex: " + ConvexUtility.randomizedConvexityTest(objective, homotopies.size(), 1000));

		//MultivariateRealOptimizer optimizer = new NelderMead();
		RandomVectorGenerator generator = new UncorrelatedRandomVectorGenerator(homotopies.size(), new GaussianRandomGenerator(new MersenneTwister()));
		//MultivariateRealOptimizer optimizer = new MultiStartMultivariateRealOptimizer(new NelderMead(), 1, generator);

		//DiscreteOptimization discreteOptimizer = new DiscreteOptimization();
		double tolerance = 1e-7;
		RealConvergenceChecker checker = new SimpleScalarValueChecker(tolerance, tolerance);

		//MultivariateRealOptimizer optimizer = new NesterovGradientOptimizer(checker, 0, 0);


		double initialValue = objective.value(new double[homotopies.size()]);
		System.out.println(initialValue);

		//RealPointValuePair optimum = optimizer.optimize(objective, GoalType.MINIMIZE, RandomUtility.normalArray(homotopies.size()));

		//RealPointValuePair optimum = discreteOptimizer.multistartOptimize(objective, homotopies.size(), 20);

		//System.out.println(optimum.getValue());
		//System.out.println(MatlabWriter.toRowVector(optimum.getPoint()));

		return optimum;
	}*/
/*
	public DoubleSparseFormalSum<ObjectObjectPair<Simplex, Simplex>> findOptimalChainMap(final ObjectSparseFormalSum<F, ObjectObjectPair<Simplex, Simplex>> generatingCycle, 
			final List<ObjectSparseFormalSum<F, ObjectObjectPair<Simplex, Simplex>>> homotopies,
			final GenericDoubleFunction<DoubleSparseFormalSum<ObjectObjectPair<Simplex, Simplex>>> mappingPenaltyFunction) throws OptimizationException, FunctionEvaluationException, IllegalArgumentException {

		RealPointValuePair pair = this.findOptimalCoefficients(generatingCycle, homotopies, mappingPenaltyFunction);
		return this.computeHomCycle(MappingUtility.round(pair.getPoint()), generatingCycle, homotopies);
	}
*/

	private double computePenalty(double[] arg0) {
		double penalty = 0;

		for (int i = 0; i < arg0.length; i++) {
			penalty += Math.cos(i) * arg0[i];
		}

		return penalty;
	}

	public void produceMatlabOutput() throws IOException {
		//String filename = FileManager.getUniqueFilePath("hom", "m");
		MatlabWriter writer = MatlabInterface.makeNewMatlabWriter("hom_data");
		DoubleMatrixConverter<Simplex, Simplex> rep = new DoubleMatrixConverter<Simplex, Simplex>(this.domainStream, this.codomainStream);
		System.out.println("Computing Generating Cycles");
		List<ObjectSparseFormalSum<F, ObjectObjectPair<Simplex, Simplex>>> generatingCycles = this.computeGeneratingCycles();
		ObjectSparseFormalSum<F, ObjectObjectPair<Simplex, Simplex>> cycleSum = this.sumGeneratingCycles(generatingCycles);
		List<ObjectSparseFormalSum<F, ObjectObjectPair<Simplex, Simplex>>> homotopies = this.getChainHomotopies();
		writer.writeClearAll();
		writer.turnOnCommentMode();
		writer.writeLine("Generating Cycles");
		for (ObjectSparseFormalSum<F, ObjectObjectPair<Simplex, Simplex>> generatingCycle: generatingCycles) {
			writer.writeLine(generatingCycle.toString());
		}
		writer.writeLine("Cycle Sum");
		writer.writeLine(cycleSum.toString());
		writer.writeLine("Homotopies");
		for (ObjectSparseFormalSum<F, ObjectObjectPair<Simplex, Simplex>> homotopy: homotopies) {
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
		writer.writeSparseMatrix(rep.toArray(toDoubleSum(cycleSum)), "cycle_sum");
		writer.newLine();
		writer.write("homotopies = cell(homotopies_dimension, 1);");
		writer.newLine();
		int i = 1;
		for (ObjectSparseFormalSum<F, ObjectObjectPair<Simplex, Simplex>> homotopy: homotopies) {
			writer.writeSparseMatrix(rep.toArray(toDoubleSum(homotopy)), "homotopies{ " + i + "}");
			writer.newLine();
			i++;

		}
		
		writer.write("domain_aw_maps = sparse(domain_dimension^2, domain_dimension);");
		writer.newLine();
		writer.write("codomain_aw_maps = sparse(codomain_dimension^2, codomain_dimension);");
		writer.newLine();
		
		int N = rep.getDomainRepresentation().getDimension();
		
		for (Simplex element: this.domainStream) {
			DoubleSparseFormalSum<ObjectObjectPair<Simplex, Simplex>> sum = MappingUtility.computeAlexanderWhitneyMap(element);
			int col = rep.getDomainRepresentation().getIndex(element) + 1;
			for (TObjectDoubleIterator<ObjectObjectPair<Simplex, Simplex>> iterator = sum.iterator(); iterator.hasNext(); ) {
				iterator.advance();
				ObjectObjectPair<Simplex, Simplex> pair = iterator.key();
				int c1 = rep.getDomainRepresentation().getIndex(pair.getFirst());
				int c2 = rep.getDomainRepresentation().getIndex(pair.getSecond());
				int row = this.flattenIndex(c1, c2, N, N) + 1;
				writer.write("domain_aw_maps(" + row + ", " + col + ") = 1;");
				writer.newLine();
			}
		}
		
		N = rep.getCodomainRepresentation().getDimension();
		
		for (Simplex element: this.codomainStream) {
			DoubleSparseFormalSum<ObjectObjectPair<Simplex, Simplex>> sum = MappingUtility.computeAlexanderWhitneyMap(element);
			int col = rep.getCodomainRepresentation().getIndex(element) + 1;
			for (TObjectDoubleIterator<ObjectObjectPair<Simplex, Simplex>> iterator = sum.iterator(); iterator.hasNext(); ) {
				iterator.advance();
				ObjectObjectPair<Simplex, Simplex> pair = iterator.key();
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
		DoubleVectorConverter<ObjectObjectPair<Simplex, Simplex>> rep = new DoubleVectorConverter<ObjectObjectPair<Simplex, Simplex>>(this.homStream);
		System.out.println("Computing Generating Cycles");
		List<ObjectSparseFormalSum<F, ObjectObjectPair<Simplex, Simplex>>> generatingCycles = this.computeGeneratingCycles();
		ObjectSparseFormalSum<F, ObjectObjectPair<Simplex, Simplex>> cycleSum = this.sumGeneratingCycles(generatingCycles);
		List<ObjectSparseFormalSum<F, ObjectObjectPair<Simplex, Simplex>>> homotopies = this.getChainHomotopies();
		writer.writeClearAll();
		writer.turnOnCommentMode();
		rep.toString();
		
		writer.turnOffCommentMode();
		
		writer.writeSparseVector(rep.toArray(toDoubleSum(cycleSum)), "cycle_sum");
		
		//double[][] homotopyMatrix = this.createHomotopyMatrix(homotopies, rep);
		//writer.writeSparseMatrix(homotopyMatrix, "homotopies");
		
		List<DoubleSparseVector> constraints = new ArrayList<DoubleSparseVector>();
		
		for (ObjectSparseFormalSum<F, ObjectObjectPair<Simplex, Simplex>> homotopy: homotopies) {
			
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
	
	private double[][] createHomotopyMatrix(List<ObjectSparseFormalSum<F, ObjectObjectPair<Simplex, Simplex>>> homotopies, DoubleVectorConverter<ObjectObjectPair<Simplex, Simplex>> rep) {
		int m = homotopies.size();
		int n = rep.getDimension();
		double[][] matrix = DoubleArrayUtility.createMatrix(m, n);
		int j = 0;
		for (ObjectSparseFormalSum<F, ObjectObjectPair<Simplex, Simplex>> homotopy: homotopies) {
			double[] col = rep.toArray(toDoubleSum(homotopy));
			for (int i = 0; i < m; i++) {
				matrix[i][j] = col[i];
			}
			j++;
		}
		return matrix;
	}
	
	private static <M, R extends Number> DoubleSparseFormalSum<M> toDoubleSum(ObjectSparseFormalSum<R, M> sum) {
		DoublePrimitiveFreeModule module = new DoublePrimitiveFreeModule();
		DoubleSparseFormalSum<M> result = module.createNewSum();
		
		for (Iterator<Entry<M, R>> iterator = sum.iterator(); iterator.hasNext(); ) {
			Entry<M, R> entry = iterator.next();
			result.put(entry.getValue().doubleValue(), entry.getKey());
		}
		
		return result;
	}
}

