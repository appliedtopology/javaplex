package edu.stanford.math.plex4.examples;


import edu.stanford.math.plex4.homology.barcodes.IntAnnotatedBarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Cell;
import edu.stanford.math.plex4.streams.impl.ExplicitCellStream;
import edu.stanford.math.primitivelib.autogen.algebraic.ObjectAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectSparseFormalSum;

/**
 * This class contains the cell complex example contained in the paper
 * "Dualities in persistent (co)homology" by Vin de Silva, Dmitriy Morozov and Mikael
 * Vejdemo-Johansson.
 * 
 * @author Andrew Tausz
 *
 * @param <R>
 */
public class DeSilvaMorozovJohanssonExample<R> {
	
	private final ObjectAbstractField<R> field;
	private final ObjectAlgebraicFreeModule<R, Cell> module;
	private final ExplicitCellStream cellComplex;
	
	private final IntAnnotatedBarcodeCollection<ObjectSparseFormalSum<R, Cell>> absoluteHomologyBarcodes = new IntAnnotatedBarcodeCollection<ObjectSparseFormalSum<R, Cell>>();
	private final IntAnnotatedBarcodeCollection<ObjectSparseFormalSum<R, Cell>> relativeHomologyBarcodes = new IntAnnotatedBarcodeCollection<ObjectSparseFormalSum<R, Cell>>();
	private final IntAnnotatedBarcodeCollection<ObjectSparseFormalSum<R, Cell>> absoluteCohomologyBarcodes = new IntAnnotatedBarcodeCollection<ObjectSparseFormalSum<R, Cell>>();
	private final IntAnnotatedBarcodeCollection<ObjectSparseFormalSum<R, Cell>> relativeCohomologyBarcodes = new IntAnnotatedBarcodeCollection<ObjectSparseFormalSum<R, Cell>>();
	
	/**
	 * This constructor initializes the class with the specified field.
	 * 
	 * @param field the field over which to perform the algebraic operations
	 */
	public DeSilvaMorozovJohanssonExample(ObjectAbstractField<R> field) {
		this.field = field;
		this.module = new ObjectAlgebraicFreeModule<R, Cell>(this.field);
		this.cellComplex = this.initialize();
	}
	
	private ExplicitCellStream initialize() {
		ExplicitCellStream stream = new ExplicitCellStream();
		
		Cell vertex_1 = new Cell();
		Cell vertex_2 = new Cell();
		Cell edge_3 = new Cell(1, new Cell[]{vertex_1, vertex_2});
		Cell edge_4 = new Cell(1, new Cell[]{vertex_1, vertex_2});
		Cell face_5 = new Cell(2, new Cell[]{edge_3, edge_4});
		Cell face_6 = new Cell(2, new Cell[]{edge_3, edge_4});

		stream.addElement(vertex_1, 1);
		stream.addElement(vertex_2, 2);
		stream.addElement(edge_3, 3);
		stream.addElement(edge_4, 4);
		stream.addElement(face_5, 5);
		stream.addElement(face_6, 6);

		stream.finalizeStream();
		
		this.absoluteHomologyBarcodes.addRightInfiniteInterval(0, 1, module.createNewSum(field.getOne(), vertex_1));
		this.absoluteHomologyBarcodes.addInterval(0, 2, 3, module.subtract(vertex_1, vertex_2));
		this.absoluteHomologyBarcodes.addInterval(1, 4, 5, module.subtract(edge_3, edge_4));
		this.absoluteHomologyBarcodes.addRightInfiniteInterval(2, 6, module.subtract(face_6, face_5));
		
		this.relativeHomologyBarcodes.addLeftInfiniteInterval(0, 1, module.createNewSum(field.getOne(), vertex_1));
		this.relativeHomologyBarcodes.addInterval(1, 2, 3, module.createNewSum(field.getOne(), edge_3));
		this.relativeHomologyBarcodes.addInterval(2, 4, 5, module.createNewSum(field.getOne(), face_5));
		this.relativeHomologyBarcodes.addLeftInfiniteInterval(2, 6, module.subtract(face_6, face_5));
		
		this.absoluteCohomologyBarcodes.addRightInfiniteInterval(0, 1, module.add(vertex_1, vertex_2));
		this.absoluteCohomologyBarcodes.addInterval(0, 2, 3, module.createNewSum(field.getOne(), vertex_2));
		this.absoluteCohomologyBarcodes.addInterval(1, 4, 5, module.createNewSum(field.getOne(), edge_4));
		this.absoluteCohomologyBarcodes.addRightInfiniteInterval(2, 6, module.createNewSum(field.getOne(), face_6));
		
		this.relativeCohomologyBarcodes.addLeftInfiniteInterval(0, 1, module.add(vertex_1, vertex_2));
		this.relativeCohomologyBarcodes.addInterval(1, 2, 3, module.multiply(-1, module.add(edge_3, vertex_2)));
		this.relativeCohomologyBarcodes.addInterval(2, 4, 5, module.multiply(-1, module.add(face_6, face_5)));
		this.relativeCohomologyBarcodes.addLeftInfiniteInterval(2, 6, module.createNewSum(field.getOne(), face_6));
		
		return stream;
	}

	/**
	 * This function returns the cell complex.
	 * 
	 * @return the cell complex of the example
	 */
	public ExplicitCellStream getCellComplex() {
		return this.cellComplex;
	}
	
	/**
	 * This function returns the correct absolute homology barcodes along with the generators.
	 * 
	 * @return the barcodes and generators for absolute homology
	 */
	public IntAnnotatedBarcodeCollection<ObjectSparseFormalSum<R, Cell>> getAbsoluteHomologyBarcodes() {
		return this.absoluteHomologyBarcodes;
	}
	
	/**
	 * This function returns the correct relative homology barcodes along with the generators.
	 * 
	 * @return the barcodes and generators for relative homology
	 */
	public IntAnnotatedBarcodeCollection<ObjectSparseFormalSum<R, Cell>> getRelativeHomologyBarcodes() {
		return this.relativeHomologyBarcodes;
	}
	
	/**
	 * This function returns the correct absolute cohomology barcodes along with the generators.
	 * 
	 * @return the barcodes and generators for absolute cohomology
	 */
	public IntAnnotatedBarcodeCollection<ObjectSparseFormalSum<R, Cell>> getAbsoluteCohomologyBarcodes() {
		return this.absoluteCohomologyBarcodes;
	}
	
	/**
	 * This function returns the correct relative cohomology barcodes along with the generators.
	 * 
	 * @return the barcodes and generators for relative cohomology
	 */
	public IntAnnotatedBarcodeCollection<ObjectSparseFormalSum<R, Cell>> getRelativeCohomologyBarcodes() {
		return this.relativeCohomologyBarcodes;
	}
}
