package edu.stanford.math.plex_plus.math.matrix.impl.sparse;

import edu.stanford.math.plex_plus.math.matrix.interfaces.IntAbstractMatrixIterator;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.iterator.TIntIntIterator;
import gnu.trove.iterator.TIntObjectIterator;

public class IntSparseMatrixIterator implements IntAbstractMatrixIterator {
	private final TIntObjectIterator<IntSparseVector> rowIterator;
	private TIntIntIterator columnIterator = null;
	
	public IntSparseMatrixIterator(IntSparseMatrix matrix) {
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
	public int value() {
		return this.columnIterator.value();
	}
	
}