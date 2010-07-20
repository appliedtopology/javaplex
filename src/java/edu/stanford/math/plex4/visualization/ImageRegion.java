/**
 * 
 */
package edu.stanford.math.plex4.visualization;

/**
 * @author Tim Harrington
 * @date Dec 19, 2009
 * 
 */
public class ImageRegion {
	
	public static class Int {
		int height;
		int width;
		int xoffset; // the upper-left x value
		int yoffset; // the upper-right y value
	
		/**
		 * @param height
		 * @param width
		 * @param xoffset
		 * @param yoffset
		 */
		public Int(int height, int width, int xoffset, int yoffset) {
			this.height = height;
			this.width = width;
			this.xoffset = xoffset;
			this.yoffset = yoffset;
		}
	}
	
	public static class Double {
		double height;
		double width;
		double xoffset; // the upper-left x value
		double yoffset; // the upper-right y value

		/**
		 * @param height
		 * @param width
		 * @param xoffset
		 * @param yoffset
		 */
		public Double(double height, double width, double xoffset,
				double yoffset) {
			this.height = height;
			this.width = width;
			this.xoffset = xoffset;
			this.yoffset = yoffset;
		}
	}
}