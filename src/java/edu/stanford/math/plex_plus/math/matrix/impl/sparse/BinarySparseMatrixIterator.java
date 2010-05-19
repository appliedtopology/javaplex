/**
 * 
 */
package edu.stanford.math.plex_plus.math.matrix.impl.sparse;

import edu.stanford.math.plex_plus.math.matrix.interfaces.BinaryAbstractMatrixIterator;
import edu.stanford.math.plex_plus.math.matrix.interfaces.BinaryAbstractVectorIterator;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.iterator.TIntObjectIterator;

/**
 * @author atausz
 *
 */
public class BinarySparseMatrixIterator implements BinaryAbstractMatrixIterator {
	private final TIntObjectIterator<BinarySparseVector> rowIterator;
	private BinaryAbstractVectorIterator columnIterator = null;
	
	public BinarySparseMatrixIterator(BinarySparseMatrix matrix) {
		ExceptionUtility.verifyNonNull(matrix);
		this.rowIterator = matrix.map.iterator();
		this.rowIterator.advance();
		this.columnIterator = this.rowIterator.value().iterator();
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
			this.columnIterator = this.rowIterator.value().iterator();
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
		return this.columnIterator.index();
	}
	
	@Override
	public boolean value() {
		return this.columnIterator.value();
	}
}
