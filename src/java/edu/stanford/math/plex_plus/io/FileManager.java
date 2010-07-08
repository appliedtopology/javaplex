package edu.stanford.math.plex_plus.io;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class returns canonical paths so that they are in the appropriate format
 * for the OS in use (e.g. / in linux vs. \ in windows).
 * 
 * We use the following terminology:
 * 
 * Base output path: ".data/runs" Daily output path: ".data/runs/yyMMdd" Unique
 * output path: ".data/runs/yyMMdd/HHmmssSSS" Category output path:
 * ".data/runs/yyMMdd/<category>" Unique Category output path:
 * ".data/runs/yyMMdd/<category>/HHmmssSSS"
 * 
 * An example of a category would be "sampledata" as used in previous versions
 * of this class.
 * 
 * @author Andrew Tausz
 * @author Tim Harrington
 */
public class FileManager {
	// the base output path as defined above: ".data/runs"
	private static String baseOutputPath = "data/runs";

	// the daily output path as defined above: ".data/runs/yyMMdd/"
	private static String dailyOutputPath = "";

	// this hashtable maps categories to their "Category output path"
	// e.g. key = "sampledata", value = ".data/runs/yyMMdd/sampledata/"
	private static Map<String, String> categoryOutputPaths = new HashMap<String, String>();

	// the wait time to prevent duplicate unique category output paths from
	// being created
	private static final long waitTime = 100;

	private FileManager() {
		// do not allow this to be constructed
	}

	/**
	 * This function returns the base output path.
	 * 
	 * @return the base output path
	 */
	public static String getBaseOutputPath() {
		return FileManager.baseOutputPath + System.getProperty("file.separator");
	}

	/**
	 * This function returns the daily output path. It is of the form
	 * "<base output path>/yyMMdd/".
	 * 
	 * @return the daily output path
	 * @throws IOException
	 */
	public static String getDailyOutputPath() throws IOException {
		if (FileManager.dailyOutputPath == "") {
			FileManager.initializeDailyOutputPath();
		}
		return FileManager.dailyOutputPath + System.getProperty("file.separator");
	}

	/**
	 * This function returns a unique output path. It is of the form
	 * "<base output path>/yyMMdd/HHmmssSSS/".
	 * 
	 * @return a unique output path
	 * @throws IOException
	 */
	public static String getUniqueOutputPath() throws IOException {
		String timeStamp = FileManager.getTimeStamp();
		String uniqueCategoryOutputPath = FileManager.createDirectory(getDailyOutputPath() + timeStamp);
		FileManager.pause();
		return uniqueCategoryOutputPath + System.getProperty("file.separator");
	}

	/**
	 * This function returns the category output path. It is of the form
	 * "<base output path>/yyMMdd/<category>/".
	 * 
	 * @param category
	 *            the name of the category
	 * @return the canonical path of the category output path
	 * @throws IOException
	 */
	public static String getCategoryOutputPath(String category) throws IOException {
		if (!FileManager.categoryOutputPaths.containsKey(category)) {
			FileManager.initializeCategoryOutputPath(category);
		}
		return FileManager.categoryOutputPaths.get(category) + System.getProperty("file.separator");
	}

	/**
	 * This function returns a unique category output path for the specified
	 * category. It is of the form
	 * "<base output path>/yyMMdd/<category>/HHmmssSSS"
	 * 
	 * @param category
	 *            the name of the category
	 * @return the canonical path of the unique category output path
	 * @throws IOException
	 */
	public static String getUniqueCategoryOutputPath(String category) throws IOException {
		String categoryOutputPath = FileManager.getCategoryOutputPath(category);
		String timeStamp = FileManager.getTimeStamp();
		String uniqueCategoryOutputPath = FileManager.createDirectory(categoryOutputPath + timeStamp);
		FileManager.pause();
		return uniqueCategoryOutputPath + System.getProperty("file.separator");
	}

	/**
	 * This method creates a directory with path directoryPath.
	 * 
	 * This method is fail safe (i.e. it sets the path fields only if the
	 * directory is successfully created or if it already exists).
	 * 
	 * @param directoryPath
	 *            the path of the directory to create
	 * @return the canonical path of the directory created
	 * @throws IOException
	 */
	private static String createDirectory(String directoryPath) throws IOException {
		boolean directoryCreated = false;
		File directory = new File(directoryPath);
		directoryCreated = directory.isDirectory() || directory.mkdirs();
		if (!directoryCreated) {
			throw new IOException("Could not create directory: " + directoryPath);
		}
		return directory.getCanonicalPath() + System.getProperty("file.separator");
	}

	/**
	 * This function initializes the daily output path as defined above.
	 * 
	 * @throws IOException
	 */
	private static void initializeDailyOutputPath() throws IOException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
		String dateString = formatter.format(new Date());
		FileManager.dailyOutputPath = FileManager.createDirectory(getBaseOutputPath() + dateString);
	}

	/**
	 * This function initializes the category output path as defined above.
	 * 
	 * @param category
	 *            the name of the category
	 * @throws IOException
	 */
	private static void initializeCategoryOutputPath(String category) throws IOException {
		String outputPath = FileManager.createDirectory(getDailyOutputPath() + category);
		FileManager.categoryOutputPaths.put(category, outputPath);
	}

	/**
	 * This function returns a string containing the current time in HHmmssSSS
	 * format.
	 * 
	 * @return a timestamp in HHmmssSSS format
	 */
	private static String getTimeStamp() {
		SimpleDateFormat formatter = new SimpleDateFormat("HHmmssSSS");
		return formatter.format(new Date());
	}

	/**
	 * This function pauses the current thread. It is used to ensure that
	 * directories created in rapid succession do not have the same names.
	 */
	private static void pause() {
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * This function creates a unique filename. It is actually of the form
	 * yyMMddHHmmssSSS. The filename does not have any extension.
	 * 
	 * @return a filename of the form yyMMddHHmmssSSS
	 */
	public static String generateUniqueFileName() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmssSSS");
		String name = formatter.format(new Date());
		FileManager.pause();
		return name;
	}

}
