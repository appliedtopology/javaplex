package edu.stanford.math.plex4.homology.barcodes;

import java.io.Serializable;

//import edu.stanford.math.primitivelib.utility.Infinity;

/**
 * This class implements the functionality of an interval of values of any comparable type. It supports
 * finite, semi-infinite, and infinite intervals with open or closed end points. 
 * 
 * @author Andrew Tausz
 *
 * @param <T> the underlying type - e.g. most likely Integer, Double, or Float
 */
public class Interval<T extends Comparable<T>> implements Comparable<Interval<T>>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3434702188482864510L;
	private final T start, end;
	private final boolean isLeftClosed, isRightClosed;
	private final boolean isLeftInfinite, isRightInfinite;

	/**
	 * This function returns a finite closed interval.
	 * 
	 * @param <T>
	 * @param start
	 * @param end
	 * @return a finite closed interval
	 */
	public static <T extends Comparable<T>> Interval<T> makeFiniteClosedInterval(T start, T end) {
		return new Interval<T>(start, end, true, true, false, false);
	}

	/**
	 * This function returns a finite right-open interval.
	 * 
	 * @param <T>
	 * @param start
	 * @param end
	 * @return a finite right-open interval
	 */
	public static <T extends Comparable<T>> Interval<T> makeFiniteRightOpenInterval(T start, T end) {
		return new Interval<T>(start, end, true, false, false, false);
	}

	/**
	 * This function returns a finite left-open interval.
	 * 
	 * @param <T>
	 * @param start
	 * @param end
	 * @return a finite left-open interval
	 */
	public static <T extends Comparable<T>> Interval<T> makeFiniteLeftOpenInterval(T start, T end) {
		return new Interval<T>(start, end, false, true, false, false);
	}

	/**
	 * This function returns a finite open interval.
	 * 
	 * @param <T>
	 * @param start
	 * @param end
	 * @return a finite open interval
	 */
	public static <T extends Comparable<T>> Interval<T> makeFiniteOpenInterval(T start, T end) {
		return new Interval<T>(start, end, false, false, false, false);
	}

	/**
	 * This function returns a right-infinite closed interval.
	 * 
	 * @param <T>
	 * @param start
	 * @return a right-infinite closed interval
	 */
	public static <T extends Comparable<T>> Interval<T> makeRightInfiniteClosedInterval(T start) {
		return new Interval<T>(start, null, true, true, false, true);
	}

	/**
	 * This function returns a right-infinite right-open interval.
	 * 
	 * @param <T>
	 * @param start
	 * @return a right-infinite right-open interval
	 */
	public static <T extends Comparable<T>> Interval<T> makeRightInfiniteRightOpenInterval(T start) {
		return new Interval<T>(start, null, true, false, false, true);
	}

	/**
	 * This function returns a right-infinite left-open interval.
	 * 
	 * @param <T>
	 * @param start
	 * @return a right-infinite left-open interval
	 */
	public static <T extends Comparable<T>> Interval<T> makeRightInfiniteLeftOpenInterval(T start) {
		return new Interval<T>(start, null, false, true, false, true);
	}

	/**
	 * This function returns a right-infinite open interval.
	 * 
	 * @param <T>
	 * @param start
	 * @return a right-infinite open interval
	 */
	public static <T extends Comparable<T>> Interval<T> makeRightInfiniteOpenInterval(T start) {
		return new Interval<T>(start, null, false, false, false, true);
	}

	/**
	 * This function returns a left-infinite closed interval.
	 * 
	 * @param <T>
	 * @param end
	 * @return a left-infinite closed interval
	 */
	public static <T extends Comparable<T>> Interval<T> makeLeftInfiniteClosedInterval(T end) {
		return new Interval<T>(null, end, true, true, true, false);
	}

	/**
	 * This function returns a left-infinite right-open interval.
	 * 
	 * @param <T>
	 * @param end
	 * @return a left-infinite right-open interval
	 */
	public static <T extends Comparable<T>> Interval<T> makeLeftInfiniteRightOpenInterval(T end) {
		return new Interval<T>(null, end, true, false, true, false);
	}

	/**
	 * This function returns a left-infinite left-open interval.
	 * 
	 * @param <T>
	 * @param end
	 * @return a left-infinite left-open interval
	 */
	public static <T extends Comparable<T>> Interval<T> makeLeftInfiniteLeftOpenInterval(T end) {
		return new Interval<T>(null, end, false, true, true, false);
	}

	/**
	 * This function returns a left-infinite open interval.
	 * 
	 * @param <T>
	 * @param end
	 * @return a left-infinite open interval
	 */
	public static <T extends Comparable<T>> Interval<T> makeLeftInfiniteOpenInterval(T end) {
		return new Interval<T>(null, end, false, false, true, false);
	}

	/**
	 * This function returns an interval with the desired parameters.
	 * 
	 * @param <T>
	 * @param start
	 * @param end
	 * @param isLeftClosed
	 * @param isRightClosed
	 * @param isLeftInfinite
	 * @param isRightInfinite
	 * @return an interval specified by the given parameters
	 */
	public static <T extends Comparable<T>> Interval<T> makeInterval(T start, T end, boolean isLeftClosed, boolean isRightClosed, boolean isLeftInfinite, boolean isRightInfinite) {
		return new Interval<T>(start, end, isLeftClosed, isRightClosed, isLeftInfinite, isRightInfinite);
	}

	/**
	 * This private constructor initializes the interval with all of its parameters.
	 * 
	 * @param start
	 * @param end
	 * @param isLeftClosed
	 * @param isRightClosed
	 * @param isLeftInfinite
	 * @param isRightInfinite
	 */
	private Interval(T start, T end, boolean isLeftClosed, boolean isRightClosed, boolean isLeftInfinite, boolean isRightInfinite) {
		this.start = start;
		this.end = end;
		this.isLeftClosed = isLeftClosed;
		this.isRightClosed = isRightClosed;
		this.isLeftInfinite = isLeftInfinite;
		this.isRightInfinite = isRightInfinite;
	}

	/**
	 * This function returns the start of the interval.
	 * 
	 * @return the starting point
	 */
	public T getStart() {
		return start;
	}

	/**
	 * This function returns the end of the interval.
	 * 
	 * @return the end point
	 */
	public T getEnd() {
		return end;
	}

	/**
	 * This function indicates whether the interval is closed on the left.
	 * 
	 * @return true if the interval is closed on the left, and false otherwise
	 */
	public boolean isLeftClosed() {
		return isLeftClosed;
	}

	/**
	 * This function indicates whether the interval is closed on the right.
	 * 
	 * @return true if the interval is closed on the right, and false otherwise
	 */
	public boolean isRightClosed() {
		return isRightClosed;
	}

	/**
	 * This function indicates whether the interval is left-infinite.
	 * 
	 * @return true if the interval is left-infinite and false otherwise
	 */
	public boolean isLeftInfinite() {
		return isLeftInfinite;
	}

	/**
	 * This function indicates whether the interval is right-infinite.
	 * 
	 * @return true if the interval is right-infinite and false otherwise
	 */
	public boolean isRightInfinite() {
		return isRightInfinite;
	}

	/**
	 * This function indicates whether the interval is infinite (either left or
	 * right infinite, or both).
	 * 
	 * @return true if the interval is infinite, and false otherwise
	 */
	public boolean isInfinite() {
		return isLeftInfinite || isRightInfinite;
	}
	
	/**
	 * This function determines whether the given point is a member of 
	 * the interval.
	 * 
	 * @param point the point to test
	 * @return true if the point is in the interval and false otherwise
	 */
	public boolean containsPoint(T point) {
		if (!this.isLeftInfinite) {
			if (this.isLeftClosed && (point.compareTo(this.start) < 0)) {
				return false;
			}

			if (!this.isLeftClosed && (point.compareTo(this.start) <= 0)) {
				return false;
			}
		}

		if (!this.isRightInfinite) {
			if (this.isRightClosed && (point.compareTo(this.end) > 0)) {
				return false;
			}

			if (!this.isRightClosed && (point.compareTo(this.end) >= 0)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		if (this.isLeftClosed) {
			builder.append("[");
		} else {
			builder.append("(");
		}

		if (this.isLeftInfinite) {
			builder.append("-infinity");
		} else {
			builder.append(start);
		}

		builder.append(", ");

		if (this.isRightInfinite) {
			builder.append("infinity");
		} else {
			builder.append(end);
		}

		if (this.isRightClosed) {
			builder.append("]");
		} else {
			builder.append(")");
		}

		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + (isLeftClosed ? 1231 : 1237);
		result = prime * result + (isLeftInfinite ? 1231 : 1237);
		result = prime * result + (isRightClosed ? 1231 : 1237);
		result = prime * result + (isRightInfinite ? 1231 : 1237);
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Interval<?> other = (Interval<?>) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (isLeftClosed != other.isLeftClosed)
			return false;
		if (isLeftInfinite != other.isLeftInfinite)
			return false;
		if (isRightClosed != other.isRightClosed)
			return false;
		if (isRightInfinite != other.isRightInfinite)
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}

	public int compareTo(Interval<T> arg0) {
		
		// First try to compare ends - the interval that ends later is "bigger"
		// If the ends cannot be compared, compare the starts
		
		int endComparison = 0;
		
		if (this.isRightInfinite && arg0.isRightInfinite) {
			endComparison = 0;
		} else if (this.isRightInfinite && !arg0.isRightInfinite) {
			endComparison = 1;
		} else if (!this.isRightInfinite && arg0.isRightInfinite) {
			endComparison = -1;
		} else {
			// both right-finite
			endComparison = this.end.compareTo(arg0.end);
		}
		
		if (endComparison != 0) {
			return endComparison;
		}
		
		int startComparison = 0;
		
		if (this.isLeftInfinite && arg0.isLeftInfinite) {
			startComparison = 0;
		} else if (this.isLeftInfinite && !arg0.isLeftInfinite) {
			startComparison = -1;
		} else if (!this.isLeftInfinite && arg0.isLeftInfinite) {
			startComparison = 1;
		} else {
			startComparison = this.start.compareTo(arg0.start);
		}
		
		return startComparison;
	}
	
	public int compareTo2(Interval<T> arg0) {
		int type = getTypeCode(this);
		int otherType = getTypeCode(arg0);

		if (type > otherType) {
			return 1;
		} else if (type < otherType) {
			return -1;
		}

		if (type == 4) {
			return 0;
		}

		if (type == 3) {
			return this.end.compareTo(arg0.end);
		}

		if (type == 2) {
			return this.start.compareTo(arg0.start);
		}

		int comparison = this.start.compareTo(arg0.start);
		if (comparison != 0) {
			return comparison;
		}

		return this.end.compareTo(arg0.end);
	}

	private static <T extends Comparable<T>> int getTypeCode(Interval<T> interval) {
		if (interval.isLeftInfinite && interval.isRightInfinite) {
			return 4;
		}

		if (interval.isLeftInfinite) {
			return 3;
		}

		if (interval.isRightInfinite) {
			return 2;
		}

		return 1;
	}
}
