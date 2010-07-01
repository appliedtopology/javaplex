package edu.stanford.math.plex_plus.homology.mapping_deprecated;

import java.util.Comparator;
import java.util.List;

import edu.stanford.math.plex_plus.algebraic_structures.impl.IntFreeModule;
import edu.stanford.math.plex_plus.algebraic_structures.interfaces.IntField;
import edu.stanford.math.plex_plus.datastructures.IntFormalSum;
import edu.stanford.math.plex_plus.datastructures.pairs.GenericPair;
import edu.stanford.math.plex_plus.functional.GenericIntFunction;
import edu.stanford.math.plex_plus.homology.IntPersistentHomology;
import edu.stanford.math.plex_plus.homology.barcodes.AugmentedBarcodeCollection;
import edu.stanford.math.plex_plus.homology.chain_basis.PrimitiveBasisElement;
import edu.stanford.math.plex_plus.homology.streams.derived.HomStream;
import edu.stanford.math.plex_plus.homology.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import edu.stanford.math.plex_plus.utility.Infinity;
import edu.stanford.math.plex_plus.utility.RandomUtility;
import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.set.hash.THashSet;

@Deprecated
public class IntMappingComputationold<T extends PrimitiveBasisElement, U extends PrimitiveBasisElement> {
	private final IntField field;
	private final IntFreeModule<GenericPair<T, U>> chainModule;
	
	public IntMappingComputationold(IntField field) {
		this.field = field;
		chainModule = new IntFreeModule<GenericPair<T, U>>(this.field);
	}
	
	public void computeMapping(AbstractFilteredStream<T> stream1, AbstractFilteredStream<U> stream2, Comparator<T> comparator1, Comparator<U> comparator2) {
		HomStream<T, U> homStream = new HomStream<T, U>(stream1, stream2, comparator1, comparator2);
		homStream.finalizeStream();
		
		IntPersistentHomology<GenericPair<T, U>> homology = new IntPersistentHomology<GenericPair<T, U>>(field, homStream.getDerivedComparator());
		AugmentedBarcodeCollection<IntFormalSum<GenericPair<T, U>>> barcodes = homology.computeIntervals(homStream, 1);
		System.out.println(barcodes);
		
		List<IntFormalSum<GenericPair<T, U>>> D_1 = homology.getBoundaryColumns(homStream, 1);
		System.out.println(D_1);

		IntFormalSum<GenericPair<T, U>> generatingCycle = new IntFormalSum<GenericPair<T, U>>();
		int numCycles = barcodes.getBarcode(0).getSize();
		for (int i = 0; i < numCycles; i++) {
			generatingCycle = chainModule.add(generatingCycle, barcodes.getBarcode(0).getGeneratingCycle(i));
		}
		
		this.randomizedOptimization(generatingCycle, D_1, this.getImageSimpicialityPenalty(stream1, this.getNormFunction(0)), 50);
	}
	
	public void randomizedOptimization(IntFormalSum<GenericPair<T, U>> generatingCycle, List<IntFormalSum<GenericPair<T, U>>> chainHomotopies, GenericIntFunction<IntFormalSum<GenericPair<T, U>>> objective, int repititions) {
		int spaceDimension = chainHomotopies.size();
		int characteristic = this.field.characteristic();
		
		// make sure that we are in a field of nonzero characteristic
		ExceptionUtility.verifyPositive(characteristic);		
		
		int minObjectiveValue = Infinity.Int.getPositiveInfinity();
		IntFormalSum<GenericPair<T, U>> minimizingChain = null;
		
		for (int repitition = 0; repitition < repititions; repitition++) {
			IntFormalSum<GenericPair<T, U>> randomizedPoint = new IntFormalSum<GenericPair<T, U>>(generatingCycle);
			for (int i = 0; i < spaceDimension; i++) {
				int coefficient = RandomUtility.nextUniformInt(0, characteristic - 1);
				randomizedPoint = chainModule.add(randomizedPoint, chainModule.multiply(coefficient, chainHomotopies.get(i)));
			}
			
			int objectiveValue = objective.evaluate(randomizedPoint);
			if (objectiveValue < minObjectiveValue) {
				minObjectiveValue = objectiveValue;
				minimizingChain = randomizedPoint;
			}
			
			System.out.println("chain: " + randomizedPoint.toString());
			System.out.println("objective value: " + objectiveValue + "\n");
		}
	}
	
	public void greedyOptimization(IntFormalSum<GenericPair<T, U>> generatingCycle, List<IntFormalSum<GenericPair<T, U>>> chainHomotopies, GenericIntFunction<IntFormalSum<GenericPair<T, U>>> objective, int repititions) {
		int spaceDimension = chainHomotopies.size();
		int characteristic = this.field.characteristic();

		// make sure that we are in a field of nonzero characteristic
		ExceptionUtility.verifyPositive(characteristic);		

		int minObjectiveValue = this.field.valueOf(Infinity.Int.getPositiveInfinity());
		IntFormalSum<GenericPair<T, U>> minimizingChain = null;

		int metaMinimumValue = this.field.valueOf(Infinity.Int.getPositiveInfinity());
		IntFormalSum<GenericPair<T, U>> metaMinimizingChain = null;

		
		for (int repitition = 0; repitition < repititions; repitition++) {
			IntFormalSum<GenericPair<T, U>> randomizedPoint = new IntFormalSum<GenericPair<T, U>>(generatingCycle);
			// create random starting point
			for (int i = 0; i < spaceDimension; i++) {
				int coefficient = RandomUtility.nextUniformInt(0, characteristic - 1);
				//int coefficient = this.field.valueOf(i + 1);
				randomizedPoint = chainModule.add(randomizedPoint, chainModule.multiply(coefficient, chainHomotopies.get(i)));
			}

			int objectiveValue = objective.evaluate(randomizedPoint);
			minObjectiveValue = objectiveValue;
			minimizingChain = randomizedPoint;

			// perform greedy search
			boolean minimumFound = false;
			while (!minimumFound) {
				boolean functionValueDecreased = false;
				for (int i = 0; i < spaceDimension; i++){
					for(int coefficient = 1; coefficient < characteristic; coefficient++) {
						IntFormalSum<GenericPair<T, U>> newPoint = chainModule.add(randomizedPoint, chainModule.multiply(coefficient, chainHomotopies.get(i)));
						objectiveValue = objective.evaluate(newPoint);
						if (objectiveValue < minObjectiveValue) {
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
			
			if (minObjectiveValue < metaMinimumValue) {
				metaMinimumValue = minObjectiveValue;
				metaMinimizingChain = minimizingChain;
			}
		}
		
		System.out.println("minimizing chain: " + metaMinimizingChain.toString());
		System.out.println("minimum objective value: " + metaMinimumValue + "\n");
	}
	
	private GenericIntFunction<IntFormalSum<GenericPair<T, U>>> getObjectiveFunction(final IntField field) {
		GenericIntFunction<IntFormalSum<GenericPair<T, U>>> function = new GenericIntFunction<IntFormalSum<GenericPair<T, U>>>() {

			@Override
			public int evaluate(IntFormalSum<GenericPair<T, U>> argument) {
				return MappingUtilityOld.norm(argument, 1, field);
			}
			
		};
		
		return function;
	}
	
	private GenericIntFunction<IntFormalSum<U>> getNormFunction(final int p) {
		GenericIntFunction<IntFormalSum<U>> function = new GenericIntFunction<IntFormalSum<U>>() {

			@Override
			public int evaluate(IntFormalSum<U> argument) {
				return MappingUtilityOld.norm(argument, p, field);
			}
			
		};
		return function;
	}
	
	private GenericIntFunction<IntFormalSum<GenericPair<T, U>>> getStreamedFunction(final AbstractFilteredStream<T> stream, final GenericIntFunction<IntFormalSum<U>> baseFunction) {
		GenericIntFunction<IntFormalSum<GenericPair<T, U>>> function = new GenericIntFunction<IntFormalSum<GenericPair<T, U>>>() {

			@Override
			public int evaluate(IntFormalSum<GenericPair<T, U>> argument) {
				return sumFunctionOverStream(stream, argument, baseFunction);
			}
			
		};
		
		return function;
	}
	
	private GenericIntFunction<IntFormalSum<GenericPair<T, U>>> getImageSimpicialityPenalty(final AbstractFilteredStream<T> stream, final GenericIntFunction<IntFormalSum<U>> baseFunction) {
		GenericIntFunction<IntFormalSum<GenericPair<T, U>>> function = new GenericIntFunction<IntFormalSum<GenericPair<T, U>>>() {

			@Override
			public int evaluate(IntFormalSum<GenericPair<T, U>> argument) {
				THashSet<U> domainMap = new THashSet<U>();
				int penalty = 0;
				for (T i: stream) {
					IntFormalSum<U> image = MappingUtilityOld.computeImage(argument, i);
					for (TObjectIntIterator<U> iterator = image.iterator(); iterator.hasNext(); ) {
						iterator.advance();
						if (domainMap.contains(iterator.key())) {
							penalty++;
						} else {
							domainMap.add(iterator.key());
						}
					}
				}
				
				return penalty;
			}
			
		};
		
		return function;
	}
	
	
	
	private int sumFunctionOverStream(AbstractFilteredStream<T> stream, IntFormalSum<GenericPair<T, U>> mapping, GenericIntFunction<IntFormalSum<U>> functional) {
		int value = 0;
		
		for (T i: stream) {
			value += functional.evaluate(MappingUtilityOld.computeImage(mapping, i));
		}
		
		return value;
	}
}
