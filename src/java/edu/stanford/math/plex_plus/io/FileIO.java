package edu.stanford.math.plex_plus.io;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;

/**
 * This class contains various static functions for reading and writing
 * files of different types.
 * 
 * @author Tim Harrington
 * @author Andrew Tausz
 *
 */
public class FileIO {

	/**
	 * Saves a BufferedImage stream in PNG format.
	 * 
	 * @param image the buffered image to save to disk
	 * @param folderPath the folder to save the image file in
	 * @param imageName the name to give the image file (minus extension)
	 * @throws IOException
	 */
	public static void saveImage(BufferedImage image, String filePath)
	throws IOException {
		BufferedOutputStream out = null;
		ImageEncoder encoder;
		try {
			out = new BufferedOutputStream(new FileOutputStream(filePath));
			encoder = ImageCodec.createImageEncoder("PNG", out, null);
			encoder.encode(image);
		} catch (IOException e) {
			throw e;
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * This function saves a string to a text file.
	 * 
	 * @param filePath the path of the file to write to
	 * @param contents a string holding the contents to write
	 * @param append if true, then the file will be appended to
	 * @throws IOException
	 */
	public static void writeTextFile(String filePath, String contents, boolean append) 
	throws IOException {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(filePath, append));
			writer.write(contents);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	/**
	 * This function reads a CSV file and outputs the values into a Table object.
	 * The format of the file is as follows:
	 * [<Title>]
	 * [<Heading1, ..., HeadingN>]
	 * <Entry11, ..., Entry1N>
	 * <EntryM1, ..., EntryMN>
	 * 
	 * The input argument readTitle should be true if and only if the file contains a
	 * title on its first line. The input argument readHeadings should be true if and only
	 * if the file contains the column headings on the file just before the first data row.  
	 * 
	 * Columns are assumed to be separated by the specified separator, and rows are
	 * assumed to be separated by a newline.
	 * 
	 * @param filename the filename of the file to read from
	 * @param separator the separator between columns
	 * @param readTitle should be true iff the file contains a title
	 * @param readHeadings should be true iff the file contains the column headings 
	 * @return a Table object containing the contents of the file
	 * @throws IOException
	 */
	public static Table readCSVFile(String filename, String separator, boolean readTitle, boolean readHeadings) 
	throws IOException {
		BufferedReader reader = null;
		String line = null;
		String[] entries = null;
		Table table = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
			table = new Table();

			// read the title if necessary
			if (readTitle) {
				line = reader.readLine();
				table.setTitle(line);
			}

			// read the column headings if necessary
			if (readHeadings) {
				// get the line containing the column headings
				line = reader.readLine();
				// split the line into the individual headings
				String[] headings = line.split(separator);
				// add the headings to the table
				for (String heading : headings) {
					table.addColumn(heading.trim());
				}
			} else {
				// read the first data line
				line = reader.readLine();
				// split the line into the individual tokens
				entries = line.split(separator);
				// add generic headings to the table
				for (int headingIndex = 0; headingIndex < entries.length; headingIndex++) {
					table.addColumn("Column_" + headingIndex);
				}
				// add the data to the table
				table.addRow(entries, true);
			}

			// continue reading the data
			while ((line = reader.readLine()) != null) {
				// split the line into the individual tokens
				entries = line.split(separator);
				// add the data to the table
				table.addRow(entries, true);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// close the reader if necessary
			if (reader != null) {
				reader.close();
			}
		}
		// return the created table
		return table;
	}

	public static List<double[]> readNumericCSVFile(String filename, String separator) throws IOException {
		BufferedReader reader = null;
		String line = null;
		String[] entries = null;
		List<double[]> contents = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
			contents = new ArrayList<double[]>();
			
			// continue reading the data
			while ((line = reader.readLine()) != null) {
				// split the line into the individual tokens
				entries = line.split(separator);
				
				double[] row = new double[entries.length];
				for (int j = 0; j < entries.length; j++) {
					row[j] = Double.parseDouble(entries[j]);
				}
				
				contents.add(row);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// close the reader if necessary
			if (reader != null) {
				reader.close();
			}
		}

		return contents;
	}

	/**
	 * Try to save an object for quick reloading later.
	 * @throws Exception 
	 */
	public static void serializeObject(Object obj,String destPath) 
	throws IOException {
		if (obj == null) {
			throw new IllegalArgumentException(
			"Cannot serialize a null object.");
		}
		FileOutputStream f;
		ObjectOutputStream s = null;
		try {
			f = new FileOutputStream(destPath,false);
			s = new ObjectOutputStream(f);
			s.writeObject(obj);
			s.close();
		} catch (IOException e) {
			if (s != null) {
				s.close();
			}
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * @param inputPath
	 * @return a deserialized object loaded from inputPath
	 * @throws Exception
	 */
	public static Object loadObject(String inputPath) throws IOException {
		Object retval = null;
		if (!(new File(inputPath)).exists()) {
			throw new IllegalStateException("Input file (" + 
					inputPath + ") does not exist");
		}
		FileInputStream f;
		ObjectInputStream o = null;
		try {
			f = new FileInputStream(inputPath);
			o = new ObjectInputStream(f);
			retval = o.readObject();
			o.close();
			return retval;
		}
		catch (IOException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		} finally {
			if (o!=null) {
				try {
					o.close();
				} catch (IOException e1) {
				}
			}
		}
	}		

}
