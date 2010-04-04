package edu.stanford.math.plex_plus.math.matrix.impl.sparse;

import edu.stanford.math.plex_plus.math.matrix.interfaces.DoubleAbstractMatrixIterator;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.iterator.TIntDoubleIterator;
import gnu.trove.iterator.TIntObjectIterator;

public class DoubleSparseMatrixIterator implements DoubleAbstractMatrixIterator {
	private final TIntObjectIterator<DoubleSparseVector> rowIterator;
	private TIntDoubleIterator columnIterator = null;
	
	public DoubleSparseMatrixIterator(DoubleSparseMatrix matrix) {
		ExceptionUtility.verifyNonNull(matrix);
		this.rowIterator = matrix.map.iterator();
		this.rowIterator.advance();
		this.columnIterator = this.rowIterator.value().map.iterator();
	}
	
	@Override
	public boolean hasNext() {
		if (this.rowIterator.hasNext()) {
			return true;
		} else {
			return this.columnIterator.hasNext();
		}
	}

	@Override
	public void advance() {
		if (this.columnIterator.hasNext()) {
			this.columnIterator.advance();
		} else {
			this.rowIterator.advance();
			this.columnIterator = this.rowIterator.value().map.iterator();
			this.columnIterator.advance();
		}
	}

	@Override
	public void remove() {
		this.columnIterator.remove();
	}
	
	@Override
	public int row() {
		return this.rowIterator.key();
	}
	
	@Override
	public int column() {
		return this.columnIterator.key();
	}
	
	@Override
	public double value() {
		return this.columnIterator.value();
	}
	
}