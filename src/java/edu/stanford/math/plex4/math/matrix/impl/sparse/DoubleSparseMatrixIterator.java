package edu.stanford.math.plex4.math.matrix.impl.sparse;

import edu.stanford.math.plex4.math.matrix.interfaces.DoubleAbstractMatrixIterator;
import edu.stanford.math.plex4.math.matrix.interfaces.DoubleAbstractVectorIterator;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import gnu.trove.iterator.TIntObjectIterator;

public class DoubleSparseMatrixIterator implements DoubleAbstractMatrixIterator {
	private final TIntObjectIterator<DoubleSparseVector> rowIterator;
	private DoubleAbstractVectorIterator columnIterator = null;
	
	public DoubleSparseMatrixIterator(DoubleSparseMatrix matrix) {
		ExceptionUtility.verifyNonNull(matrix);
		this.rowIterator = matrix.map.iterator();
		this.rowIterator.advance();
		this.columnIterator = this.rowIterator.value().iterator();
	}
	
	public boolean hasNext() {
		if (this.rowIterator.hasNext()) {
			return true;
		} else {
			return this.columnIterator.hasNext();
		}
	}

	public void advance() {
		if (this.columnIterator.hasNext()) {
			this.columnIterator.advance();
		} else {
			this.rowIterator.advance();
			this.columnIterator = this.rowIterator.value().iterator();
			this.columnIterator.advance();
		}
	}

	public void remove() {
		this.columnIterator.remove();
	}
	
	public int row() {
		return this.rowIterator.key();
	}
	
	public int column() {
		return this.columnIterator.index();
	}
	
	public double value() {
		return this.columnIterator.value();
	}
	
}