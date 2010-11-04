package edu.stanford.math.plex4.unit_tests;


import edu.stanford.math.plex4.homology.barcodes.IntAugmentedBarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Cell;
import edu.stanford.math.plex4.streams.impl.ExplicitCellStream;
import edu.stanford.math.primitivelib.autogen.algebraic.ObjectAbstractField;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectAlgebraicFreeModule;
import edu.stanford.math.primitivelib.autogen.formal_sum.ObjectSparseFormalSum;

public class DeSilvaMorozovJohanssonExample<R> {
	
	private final ObjectAbstractField<R> field;
	private final ObjectAlgebraicFreeModule<R, Cell> module;
	private final ExplicitCellStream cellComplex;
	
	private final IntAugmentedBarcodeCollection<ObjectSparseFormalSum<R, Cell>> absoluteHomologyBarcodes = new IntAugmentedBarcodeCollection<ObjectSparseFormalSum<R, Cell>>();
	private final IntAugmentedBarcodeCollection<ObjectSparseFormalSum<R, Cell>> relativeHomologyBarcodes = new IntAugmentedBarcodeCollection<ObjectSparseFormalSum<R, Cell>>();
	private final IntAugmentedBarcodeCollection<ObjectSparseFormalSum<R, Cell>> absoluteCohomologyBarcodes = new IntAugmentedBarcodeCollection<ObjectSparseFormalSum<R, Cell>>();
	private final IntAugmentedBarcodeCollection<ObjectSparseFormalSum<R, Cell>> relativeCohomologyBarcodes = new IntAugmentedBarcodeCollection<ObjectSparseFormalSum<R, Cell>>();
	
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

	public ExplicitCellStream getCellComplex() {
		return this.cellComplex;
	}
	
	public IntAugmentedBarcodeCollection<ObjectSparseFormalSum<R, Cell>> getAbsoluteHomologyBarcodes() {
		return this.absoluteHomologyBarcodes;
	}
	
	public IntAugmentedBarcodeCollection<ObjectSparseFormalSum<R, Cell>> getRelativeHomologyBarcodes() {
		return this.relativeHomologyBarcodes;
	}
	
	public IntAugmentedBarcodeCollection<ObjectSparseFormalSum<R, Cell>> getAbsoluteCohomologyBarcodes() {
		return this.absoluteCohomologyBarcodes;
	}
	
	public IntAugmentedBarcodeCollection<ObjectSparseFormalSum<R, Cell>> getRelativeCohomologyBarcodes() {
		return this.relativeCohomologyBarcodes;
	}
}
