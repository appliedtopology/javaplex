package edu.stanford.math.plex_plus.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import edu.stanford.math.plex_plus.utility.ArrayUtility;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * This class holds a table of string values. It contains functionality
 * for outputting in comma separated and pretty printed formats. 
 * 
 * @author Andrew Tausz
 *
 */
public class Table {
	// the title of the table
	protected String title = "";
	// the number of columns
	protected int numColumns = 0;
	// the number of rows
	protected int numRows = 0;
	// this array holds the name of each column
	protected ArrayList<String> columnHeadings = new ArrayList<String>();
	// this array holds the contents of the table, by row
	protected ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();

	public Table() {}

	/**
	 * This function sets the title of the table.
	 * 
	 * @param title the title to set to
	 */
	public void setTitle(String title) {
		ExceptionUtility.verifyNonNull(title);
		this.title = title;
	}

	/**
	 * This function returns the title of the table.
	 * 
	 * @return the table's title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * This function adds a new column to the table.
	 * 
	 * @param columnHeading the name of the column to add
	 */
	public void addColumn(String columnHeading) {
		this.columnHeadings.add(columnHeading);
		this.numColumns++;
	}

	/**
	 * This function sets the heading of the specified column.
	 * 
	 * @param column the index of the column
	 * @param columnHeading the new heading of the column
	 */
	public void setColumnHeading(int column, String columnHeading) {
		ExceptionUtility.verifyLessThan(column, this.numColumns);
		this.columnHeadings.set(column, columnHeading);
	}
	
	/**
	 * This function returns the heading of the specified column.
	 * 
	 * @param column the index of the column
	 * @return the heading of the specified column
	 */
	public String getColumnHeading(int column) {
		ExceptionUtility.verifyLessThan(column, this.numColumns);
		return this.columnHeadings.get(column);
	}
	
	/**
	 * This function adds a data row to the table. It 
	 * converts each object in the input to a string, by
	 * calling the toString function.
	 *  
	 * @param row a collection containing the data to add
	 */
	public void addRow(Collection<Object> row) {
		// make sure that the row contains the right number of entries
		ExceptionUtility.verifyEqual(row.size(), this.columnHeadings.size());
		// create a new string array
		ArrayList<String> stringRow = new ArrayList<String>();
		// convert each object to a string
		for (Object element : row) {
			stringRow.add(element.toString());
		}
		this.rows.add(stringRow);
		this.numRows++;
	}
	
	/**
	 * This function adds a data row to the table. 
	 *  
	 * @param row a collection containing the data to add
	 */
	public void addRow(List<String> row) {
		// make sure that the row contains the right number of entries
		ExceptionUtility.verifyEqual(row.size(), this.columnHeadings.size());
		// create a new string array
		ArrayList<String> stringRow = new ArrayList<String>();
		// convert collection to a list
		stringRow.addAll(row);
		this.rows.add(stringRow);
		this.numRows++;
	}
	
	/**
	 * This function adds a data row to the table. 
	 *  
	 * @param row a collection containing the data to add
	 */
	public void addRow(String[] row, boolean trimElements) {
		// make sure that the row contains the right number of entries
		ExceptionUtility.verifyEqual(row.length, this.columnHeadings.size());
		// create a new string array
		ArrayList<String> stringRow = new ArrayList<String>();
		// add the elements to the array
		for (String rowElement : row) {
			if (trimElements) {
				stringRow.add(rowElement.trim());
			} else {
				stringRow.add(rowElement);
			}
		}
		this.rows.add(stringRow);
		this.numRows++;
	}

	/**
	 * This function returns the entry in the table located at
	 * (row, column). 
	 * 
	 * @param row the row of the entry
	 * @param column the column of the entry
	 * @return the element at the specified location
	 */
	public String getEntry(int row, int column) {
		ExceptionUtility.verifyLessThan(row, this.numRows);
		ExceptionUtility.verifyLessThan(column, this.numColumns);
		ExceptionUtility.verifyNonNegative(row);
		ExceptionUtility.verifyNonNegative(column);
		return this.rows.get(row).get(column);
	}

	/**
	 * This function returns the string representation of the table.
	 * For now it uses the pretty print version.
	 */
	@Override
	public String toString() {
		return this.toPrettyString(" | ");
	}

	/**
	 * This function creates a string holding the contents of the 
	 * table in comma separated format. 
	 * 
	 * @param separator the separator between each column
	 * @param includeTitle if true, then the title is also printed
	 * @param includeHeadings if true, then the headings are printed
	 * @return a string containing the contents of the table
	 */
	public String toCSVString(String separator, boolean includeTitle, boolean includeHeadings) {
		StringBuilder builder = new StringBuilder();
		
		// print the title if required
		if (includeTitle) {
			builder.append(this.title + "\n");
		}
		
		// print the column headings
		for (int j = 0; j < this.columnHeadings.size(); j++) {
			if (j > 0) {
				builder.append(separator);
			}
			builder.append(this.columnHeadings.get(j));
		}
		
		builder.append('\n');
		
		// print the table contents
		for (int i = 0; i < this.rows.size(); i++) {
			ArrayList<String> row = this.rows.get(i);
			for (int j = 0; j < row.size(); j++) {
				if (j > 0) {
					builder.append(separator);
				}
				builder.append(row.get(j));
			}
			builder.append("\n");
		}

		return builder.toString();
	}

	/**
	 * This function returns a string holding the contents
	 * of the table. It pads the columns with spaces so that the
	 * columns have uniform width.
	 * 
	 * @return a visually appealing textual representation of the table
	 */
	public String toPrettyString(String separator) {
		StringBuilder builder = new StringBuilder();
		int[] columnWidths = new int[this.numColumns];

		// print the title
		builder.append(this.title + "\n");

		// compute the width of each column
		for (int j = 0; j < this.numColumns; j++) {
			int columnWidth = this.columnHeadings.get(j).length();
			for (int i = 0; i < this.numRows; i++) {
				columnWidth = Math.max(columnWidth, this.getEntry(i, j).length());
			}
			columnWidths[j] = columnWidth;
		}
		
		// print the column names
		for (int j = 0; j < this.numColumns; j++) {
			String label = this.columnHeadings.get(j);
			if (j > 0) {
				builder.append(separator);
			}
			builder.append(this.padString(label, columnWidths[j], ' '));
		}
		builder.append('\n');
		
		// print a separator between Headings and data
		int totalWidth = ArrayUtility.sum(columnWidths) + (this.numColumns - 1) * separator.length();
		builder.append(this.repeatCharacter('-', totalWidth));
		builder.append('\n');

		// print the table contents
		for (int i = 0; i < this.numRows; i++) {
			for (int j = 0; j < this.numColumns; j++) {
				String label = this.getEntry(i, j);
				if (j > 0) {
					builder.append(separator);
				}
				builder.append(this.padString(label, columnWidths[j], ' '));
			}
			builder.append('\n');
		}

		return builder.toString();
	}

	/**
	 * This function pads the input string with padCharacter so that the
	 * resulting length is equal to the parameter length.
	 * 
	 * @param input the string to pad
	 * @param length the total length of the resulting string
	 * @param padCharacter the character to pad with
	 * @return the padded string
	 */
	private String padString(String input, int length, char padCharacter) {
		ExceptionUtility.verifyNonNull(input);
		int padLength = length - input.length();
		ExceptionUtility.verifyNonNegative(padLength);
		if (padLength == 0) {
			return input;
		} else {
			return (input + repeatCharacter(padCharacter, padLength));
		}
	}

	/**
	 * This function returns a string containing the input character repeated.
	 * 
	 * @param character the character to repeat
	 * @param length the number of times to repeat
	 * @return a string containing the character repeated length times
	 */
	private String repeatCharacter(char character, int length) {
		ExceptionUtility.verifyPositive(length);
		char[] charArray = new char[length];
		Arrays.fill(charArray, character);
		return new String(charArray);
	}
}
