/**
 * 
 */
package edu.stanford.math.plex4.visualization;

/**
 * @author Tim Harrington
 * 
 */
public class ImageRegion {

	public static class Int {
		public final int height;
		public final int width;
		public final int xoffset; // the upper-left x value
		public final int yoffset; // the upper-right y value

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
		public final double height;
		public final double width;
		public final double xoffset; // the upper-left x value
		public final double yoffset; // the upper-right y value

		/**
		 * @param height
		 * @param width
		 * @param xoffset
		 * @param yoffset
		 */
		public Double(double height, double width, double xoffset, double yoffset) {
			this.height = height;
			this.width = width;
			this.xoffset = xoffset;
			this.yoffset = yoffset;
		}
	}
}