package edu.stanford.math.plex_plus.homology.mapping_deprecated;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import edu.stanford.math.plex_plus.algebraic_structures.conversion.ModuleMorphismRepresentation;
import edu.stanford.math.plex_plus.algebraic_structures.impl.GenericFreeModule;
import edu.stanford.math.plex_plus.algebraic_structures.interfaces.GenericOrderedField;
import edu.stanford.math.plex_plus.datastructures.GenericFormalSum;
import edu.stanford.math.plex_plus.datastructures.pairs.GenericPair;
import edu.stanford.math.plex_plus.functional.GenericFunction;
import edu.stanford.math.plex_plus.homology.GenericPersistentHomology;
import edu.stanford.math.plex_plus.homology.barcodes.AugmentedBarcodeCollection;
import edu.stanford.math.plex_plus.homology.streams.derived.HomStream;
import edu.stanford.math.plex_plus.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex_plus.utility.ArrayUtility2;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import edu.stanford.math.plex_plus.utility.Infinity;
import edu.stanford.math.plex_plus.utility.RandomUtility;
import gnu.trove.set.hash.THashSet;

@Deprecated
public class GenericMappingComputationOld<F extends Number, T, U> {
	private final GenericOrderedField<F> field;
	private final GenericFreeModule<F, GenericPair<T, U>> chainModule;

	public GenericMappingComputationOld(GenericOrderedField<F> field) {
		this.field = field;
		chainModule = new GenericFreeModule<F, GenericPair<T, U>>(this.field);
	}

	public void computeMapping(AbstractFilteredStream<T> stream1, AbstractFilteredStream<U> stream2, Comparator<T> comparator1, Comparator<U> comparator2) {
		HomStream<T, U> homStream = new HomStream<T, U>(stream1, stream2, comparator1, comparator2);
		homStream.finalizeStream();

		GenericPersistentHomology<F, GenericPair<T, U>> homology = new GenericPersistentHomology<F, GenericPair<T, U>>(field, homStream.getDerivedComparator());
		AugmentedBarcodeCollection<GenericFormalSum<F, GenericPair<T, U>>> barcodes = homology.computeIntervals(homStream, 1);
		System.out.println(barcodes);

		List<GenericFormalSum<F, GenericPair<T, U>>> D_1 = homology.getBoundaryColumns(homStream, 1);
		System.out.println(D_1);

		GenericFormalSum<F, GenericPair<T, U>> generatingCycle = new GenericFormalSum<F, GenericPair<T, U>>();
		int numCycles = barcodes.getBarcode(0).getSize();
		for (int i = 0; i < numCycles; i++) {
			generatingCycle = chainModule.add(generatingCycle, barcodes.getBarcode(0).getGeneratingCycle(i));
		}
		
		//this.randomizedOptimization(generatingCycle, D_1, this.getImageSimpicialityPenalty(stream1), 100);
		
		ModuleMorphismRepresentation<F, T, U> morphismRep = new ModuleMorphismRepresentation<F, T, U>(stream1, stream2);
		
		System.out.println(ArrayUtility2.toString(morphismRep.toDoubleMatrix(generatingCycle)));
		
		for (GenericFormalSum<F, GenericPair<T, U>> homotopy: D_1) {
			System.out.println(ArrayUtility2.toString(morphismRep.toDoubleMatrix(homotopy)));
		}
	}
	
	public void computeMatrices(GenericFormalSum<F, GenericPair<T, U>> generatingCycle, List<GenericFormalSum<F, GenericPair<T, U>>> chainHomotopies) {
		
	}

	public void randomizedOptimization(GenericFormalSum<F, GenericPair<T, U>> generatingCycle, List<GenericFormalSum<F, GenericPair<T, U>>> chainHomotopies, GenericFunction<GenericFormalSum<F, GenericPair<T, U>>, F> objective, int repititions) {
		int spaceDimension = chainHomotopies.size();
		// make sure that we are in a field of nonzero characteristic
		//ExceptionUtility.verifyPositive(characteristic);		

		F minObjectiveValue = field.valueOf(Infinity.Int.getPositiveInfinity());
		GenericFormalSum<F, GenericPair<T, U>> minimizingChain = null;

		for (int repitition = 0; repitition < repititions; repitition++) {
			GenericFormalSum<F, GenericPair<T, U>> randomizedPoint = new GenericFormalSum<F, GenericPair<T, U>>(generatingCycle);
			for (int i = 0; i < spaceDimension; i++) {
				int coefficient = RandomUtility.nextUniformInt(-1, 1);
				randomizedPoint = chainModule.add(randomizedPoint, chainModule.multiply(coefficient, chainHomotopies.get(i)));
			}

			F objectiveValue = objective.evaluate(randomizedPoint);
			if (this.field.compare(objectiveValue, minObjectiveValue) < 0) {
				minObjectiveValue = objectiveValue;
				minimizingChain = randomizedPoint;
			}
		}
		
		System.out.println("chain: " + minimizingChain.toString());
		System.out.println("objective value: " + minObjectiveValue + "\n");
	}

	public void greedyOptimization(GenericFormalSum<F, GenericPair<T, U>> generatingCycle, List<GenericFormalSum<F, GenericPair<T, U>>> chainHomotopies, GenericFunction<GenericFormalSum<F, GenericPair<T, U>>, F> objective, int repititions) {
		int spaceDimension = chainHomotopies.size();
		int characteristic = this.field.characteristic();

		// make sure that we are in a field of nonzero characteristic
		ExceptionUtility.verifyPositive(characteristic);		

		F minObjectiveValue = this.field.valueOf(Infinity.Int.getPositiveInfinity());
		GenericFormalSum<F, GenericPair<T, U>> minimizingChain = null;

		F metaMinimumValue = this.field.valueOf(Infinity.Int.getPositiveInfinity());
		GenericFormalSum<F, GenericPair<T, U>> metaMinimizingChain = null;

		
		for (int repitition = 0; repitition < repititions; repitition++) {
			GenericFormalSum<F, GenericPair<T, U>> randomizedPoint = new GenericFormalSum<F, GenericPair<T, U>>(generatingCycle);
			// create random starting point
			for (int i = 0; i < spaceDimension; i++) {
				int coefficient = RandomUtility.nextUniformInt(0, characteristic - 1);
				//int coefficient = this.field.valueOf(i + 1);
				randomizedPoint = chainModule.add(randomizedPoint, chainModule.multiply(coefficient, chainHomotopies.get(i)));
			}

			F objectiveValue = objective.evaluate(randomizedPoint);
			minObjectiveValue = objectiveValue;
			minimizingChain = randomizedPoint;

			// perform greedy search
			boolean minimumFound = false;
			while (!minimumFound) {
				boolean functionValueDecreased = false;
				for (int i = 0; i < spaceDimension; i++){
					for(int coefficient = 1; coefficient < characteristic; coefficient++) {
						GenericFormalSum<F, GenericPair<T, U>> newPoint = chainModule.add(randomizedPoint, chainModule.multiply(coefficient, chainHomotopies.get(i)));
						objectiveValue = objective.evaluate(newPoint);
						if (this.field.compare(objectiveValue, minObjectiveValue) < 0) {
							minObjectiveValue = objectiveValue;
							minimizingChain = randomizedPoint;
							functionValueDecreased = true;
						}
					}
				}
				if (!functionValueDecreased) {
					minimumFound = true;
				}
			}
			
			if (this.field.compare(minObjectiveValue, metaMinimumValue) < 0) {
				metaMinimumValue = minObjectiveValue;
				metaMinimizingChain = minimizingChain;
			}
		}
		
		System.out.println("minimizing chain: " + metaMinimizingChain.toString());
		System.out.println("minimum objective value: " + metaMinimumValue + "\n");
	}

	private GenericFunction<GenericFormalSum<F, GenericPair<T, U>>, F> getObjectiveFunction() {
		GenericFunction<GenericFormalSum<F, GenericPair<T, U>>, F> function = new GenericFunction<GenericFormalSum<F, GenericPair<T, U>>, F>() {

			@Override
			public F evaluate(GenericFormalSum<F, GenericPair<T, U>> argument) {
				return MappingUtilityOld.norm(argument, 1, field);
			}

		};

		return function;
	}

	private GenericFunction<GenericFormalSum<F, U>, F> getNormFunction(final int p) {
		GenericFunction<GenericFormalSum<F, U>, F> function = new GenericFunction<GenericFormalSum<F, U>, F>() {

			@Override
			public F evaluate(GenericFormalSum<F, U> argument) {
				return MappingUtilityOld.norm(argument, p, field);
			}

		};
		return function;
	}

	private GenericFunction<GenericFormalSum<F, GenericPair<T, U>>, F> getStreamedFunction(final AbstractFilteredStream<T> stream, final GenericFunction<GenericFormalSum<F, U>, F> baseFunction) {
		GenericFunction<GenericFormalSum<F, GenericPair<T, U>>, F> function = new GenericFunction<GenericFormalSum<F, GenericPair<T, U>>, F>() {

			@Override
			public F evaluate(GenericFormalSum<F, GenericPair<T, U>> argument) {
				return sumFunctionOverStream(stream, argument, baseFunction);
			}

		};

		return function;
	}

	private GenericFunction<GenericFormalSum<F, GenericPair<T, U>>, F> getImageSimpicialityPenalty(final AbstractFilteredStream<T> stream) {
		GenericFunction<GenericFormalSum<F, GenericPair<T, U>>, F> function = new GenericFunction<GenericFormalSum<F, GenericPair<T, U>>, F>() {

			@Override
			public F evaluate(GenericFormalSum<F, GenericPair<T, U>> argument) {
				THashSet<U> domainMap = new THashSet<U>();
				F penalty = field.getZero();
				for (T i: stream) {
					GenericFormalSum<F, U> image = MappingUtilityOld.computeImage(argument, i);
					for (Iterator<Entry<U, F>> iterator = image.iterator(); iterator.hasNext(); ) {
						Entry<U, F> entry = iterator.next();
						if (domainMap.contains(entry.getKey())) {
							penalty = field.add(1, penalty);
						} else {
							domainMap.add(entry.getKey());
						}
					}
				}

				return penalty;
			}

		};

		return function;
	}



	private F sumFunctionOverStream(AbstractFilteredStream<T> stream, GenericFormalSum<F, GenericPair<T, U>> mapping, GenericFunction<GenericFormalSum<F, U>, F> functional) {
		F value = this.field.getZero();

		for (T i: stream) {
			value = field.add(functional.evaluate(MappingUtilityOld.computeImage(mapping, i)), value);
		}

		return value;
	}
}
