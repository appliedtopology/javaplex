package edu.stanford.math.plex4.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.fraction.Fraction;

import edu.stanford.math.plex4.autogen.homology.BooleanAbsoluteHomology;
import edu.stanford.math.plex4.autogen.homology.BooleanClassicalHomology;
import edu.stanford.math.plex4.autogen.homology.BooleanRelativeHomology;
import edu.stanford.math.plex4.autogen.homology.IntAbsoluteHomology;
import edu.stanford.math.plex4.autogen.homology.IntClassicalHomology;
import edu.stanford.math.plex4.autogen.homology.IntRelativeHomology;
import edu.stanford.math.plex4.autogen.homology.ObjectAbsoluteHomology;
import edu.stanford.math.plex4.autogen.homology.ObjectClassicalHomology;
import edu.stanford.math.plex4.autogen.homology.ObjectRelativeHomology;
import edu.stanford.math.plex4.homology.chain_basis.Cell;
import edu.stanford.math.plex4.homology.chain_basis.CellComparator;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPair;
import edu.stanford.math.plex4.homology.chain_basis.SimplexPairComparator;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceBasisAlgorithm;
import edu.stanford.math.plex4.interop.Plex3PersistenceAlgorithm;
import edu.stanford.math.primitivelib.algebraic.impl.ModularIntField;
import edu.stanford.math.primitivelib.algebraic.impl.RationalField;
import edu.stanford.math.primitivelib.autogen.algebraic.IntAbstractField;
import edu.stanford.math.primitivelib.autogen.algebraic.ObjectAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.BooleanSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectSparseFormalSum;

public class PersistenceAlgorithmInterface {
	
	private static IntAbstractField intField = ModularIntField.getInstance(2);
	private static ObjectAbstractField<Fraction> fractionField = RationalField.getInstance();
	
	/*
	 * Simplicial Homology.
	 */
	
	public static AbstractPersistenceAlgorithm<Simplex> getPlex3SimplicialAbsoluteHomology(int maxDimension) {
		return new Plex3PersistenceAlgorithm(maxDimension);
	}
	
	public static AbstractPersistenceBasisAlgorithm<Simplex, BooleanSparseFormalSum<Simplex>> getBooleanSimplicialAbsoluteHomology(int maxDimension) {
		return new BooleanAbsoluteHomology<Simplex>(SimplexComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceBasisAlgorithm<Simplex, IntSparseFormalSum<Simplex>> getIntSimplicialAbsoluteHomology(int maxDimension) {
		return new IntAbsoluteHomology<Simplex>(intField, SimplexComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceBasisAlgorithm<Simplex, ObjectSparseFormalSum<Fraction, Simplex>> getRationalSimplicialAbsoluteHomology(int maxDimension) {
		return new ObjectAbsoluteHomology<Fraction, Simplex>(fractionField, SimplexComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceAlgorithm<Simplex> getBooleanSimplicialClassicalHomology(int maxDimension) {
		return new BooleanClassicalHomology<Simplex>(SimplexComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceAlgorithm<Simplex> getIntSimplicialClassicalHomology(int maxDimension) {
		return new IntClassicalHomology<Simplex>(intField, SimplexComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceAlgorithm<Simplex> getRationalSimplicialClassicalHomology(int maxDimension) {
		return new ObjectClassicalHomology<Fraction, Simplex>(fractionField, SimplexComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceBasisAlgorithm<Simplex, BooleanSparseFormalSum<Simplex>> getBooleanSimplicialRelativeHomology(int maxDimension) {
		return new BooleanRelativeHomology<Simplex>(SimplexComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceBasisAlgorithm<Simplex, IntSparseFormalSum<Simplex>> getIntSimplicialRelativeHomology(int maxDimension) {
		return new IntRelativeHomology<Simplex>(intField, SimplexComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceBasisAlgorithm<Simplex, ObjectSparseFormalSum<Fraction, Simplex>> getRationalSimplicialRelativeHomology(int maxDimension) {
		return new ObjectRelativeHomology<Fraction, Simplex>(fractionField, SimplexComparator.getInstance(), 0, maxDimension);
	}
	
	public static List<AbstractPersistenceAlgorithm<Simplex>> getAllPlex4SimplicialAbsoluteHomologyAlgorithms(int maxDimension) {
		List<AbstractPersistenceAlgorithm<Simplex>> list = new ArrayList<AbstractPersistenceAlgorithm<Simplex>>();
		
		list.add(getBooleanSimplicialAbsoluteHomology(maxDimension));
		list.add(getIntSimplicialAbsoluteHomology(maxDimension));
		list.add(getRationalSimplicialAbsoluteHomology(maxDimension));
		list.add(getBooleanSimplicialClassicalHomology(maxDimension));
		list.add(getIntSimplicialClassicalHomology(maxDimension));
		
		return list;
	}
	
	public static List<AbstractPersistenceAlgorithm<Simplex>> getAllSimplicialAbsoluteHomologyAlgorithms(int maxDimension) {
		List<AbstractPersistenceAlgorithm<Simplex>> list = getAllPlex4SimplicialAbsoluteHomologyAlgorithms(maxDimension);
		list.add(getPlex3SimplicialAbsoluteHomology(maxDimension));
		return list;
	}
	
	/*
	 * Cellular Homology.
	 */
	
	public static AbstractPersistenceBasisAlgorithm<Cell, BooleanSparseFormalSum<Cell>> getBooleanCellularAbsoluteHomology(int maxDimension) {
		return new BooleanAbsoluteHomology<Cell>(CellComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceBasisAlgorithm<Cell, IntSparseFormalSum<Cell>> getIntCellularAbsoluteHomology(int maxDimension) {
		return new IntAbsoluteHomology<Cell>(intField, CellComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceBasisAlgorithm<Cell, ObjectSparseFormalSum<Fraction, Cell>> getRationalCellularAbsoluteHomology(int maxDimension) {
		return new ObjectAbsoluteHomology<Fraction, Cell>(fractionField, CellComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceAlgorithm<Cell> getBooleanCellularClassicalHomology(int maxDimension) {
		return new BooleanClassicalHomology<Cell>(CellComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceAlgorithm<Cell> getIntCellularClassicalHomology(int maxDimension) {
		return new IntClassicalHomology<Cell>(intField, CellComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceAlgorithm<Cell> getRationalCellularClassicalHomology(int maxDimension) {
		return new ObjectClassicalHomology<Fraction, Cell>(fractionField, CellComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceBasisAlgorithm<Cell, BooleanSparseFormalSum<Cell>> getBooleanCellularRelativeHomology(int maxDimension) {
		return new BooleanRelativeHomology<Cell>(CellComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceBasisAlgorithm<Cell, IntSparseFormalSum<Cell>> getIntCellularRelativeHomology(int maxDimension) {
		return new IntRelativeHomology<Cell>(intField, CellComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceBasisAlgorithm<Cell, ObjectSparseFormalSum<Fraction, Cell>> getRationalCellularRelativeHomology(int maxDimension) {
		return new ObjectRelativeHomology<Fraction, Cell>(fractionField, CellComparator.getInstance(), 0, maxDimension);
	}
	
	public static List<AbstractPersistenceAlgorithm<Cell>> getAllCellularAbsoluteHomologyAlgorithms(int maxDimension) {
		List<AbstractPersistenceAlgorithm<Cell>> list = new ArrayList<AbstractPersistenceAlgorithm<Cell>>();
		
		list.add(getBooleanCellularAbsoluteHomology(maxDimension));
		list.add(getIntCellularAbsoluteHomology(maxDimension));
		list.add(getRationalCellularAbsoluteHomology(maxDimension));
		list.add(getBooleanCellularClassicalHomology(maxDimension));
		list.add(getIntCellularClassicalHomology(maxDimension));
		
		return list;
	}
	
	/*
	 * Simplex Pair Homology.
	 */
	
	public static AbstractPersistenceBasisAlgorithm<SimplexPair, BooleanSparseFormalSum<SimplexPair>> getBooleanSimplexPairAbsoluteHomology(int maxDimension) {
		return new BooleanAbsoluteHomology<SimplexPair>(SimplexPairComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceBasisAlgorithm<SimplexPair, IntSparseFormalSum<SimplexPair>> getIntSimplexPairAbsoluteHomology(int maxDimension) {
		return new IntAbsoluteHomology<SimplexPair>(intField, SimplexPairComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceBasisAlgorithm<SimplexPair, ObjectSparseFormalSum<Fraction, SimplexPair>> getRationalSimplexPairAbsoluteHomology(int maxDimension) {
		return new ObjectAbsoluteHomology<Fraction, SimplexPair>(fractionField, SimplexPairComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceAlgorithm<SimplexPair> getBooleanSimplexPairClassicalHomology(int maxDimension) {
		return new BooleanClassicalHomology<SimplexPair>(SimplexPairComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceAlgorithm<SimplexPair> getIntSimplexPairClassicalHomology(int maxDimension) {
		return new IntClassicalHomology<SimplexPair>(intField, SimplexPairComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceAlgorithm<SimplexPair> getRationalSimplexPairClassicalHomology(int maxDimension) {
		return new ObjectClassicalHomology<Fraction, SimplexPair>(fractionField, SimplexPairComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceBasisAlgorithm<SimplexPair, BooleanSparseFormalSum<SimplexPair>> getBooleanSimplexPairRelativeHomology(int maxDimension) {
		return new BooleanRelativeHomology<SimplexPair>(SimplexPairComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceBasisAlgorithm<SimplexPair, IntSparseFormalSum<SimplexPair>> getIntSimplexPairRelativeHomology(int maxDimension) {
		return new IntRelativeHomology<SimplexPair>(intField, SimplexPairComparator.getInstance(), 0, maxDimension);
	}
	
	public static AbstractPersistenceBasisAlgorithm<SimplexPair, ObjectSparseFormalSum<Fraction, SimplexPair>> getRationalSimplexPairRelativeHomology(int maxDimension) {
		return new ObjectRelativeHomology<Fraction, SimplexPair>(fractionField, SimplexPairComparator.getInstance(), 0, maxDimension);
	}
	
	public static List<AbstractPersistenceAlgorithm<SimplexPair>> getAllSimplexPairAbsoluteHomologyAlgorithms(int maxDimension) {
		List<AbstractPersistenceAlgorithm<SimplexPair>> list = new ArrayList<AbstractPersistenceAlgorithm<SimplexPair>>();
		
		list.add(getBooleanSimplexPairAbsoluteHomology(maxDimension));
		list.add(getIntSimplexPairAbsoluteHomology(maxDimension));
		list.add(getRationalSimplexPairAbsoluteHomology(maxDimension));
		list.add(getBooleanSimplexPairClassicalHomology(maxDimension));
		list.add(getIntSimplexPairClassicalHomology(maxDimension));
		
		return list;
	}
}
