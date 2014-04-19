package edu.stanford.math.plex4.visualization;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import processing.core.PApplet;

@SuppressWarnings("serial")
public class PAppletSplitscreen extends PApplet {

	/**
	 * Global variables to enable rotation and zoom of 3D image
	 */
	float rotX = 0.0f, rotY = 0.0f;
	float distX = 0.0f, distY = 0.0f;
	int lastX, lastY;
	float zoomZ = 0.0f;

	/**
	 * Overwrite this method to setup additional variables
	 */
	public void doSetup() {
	}

	@Override
	final public void setup() {
		size(1000, 500, P3D);
		addMouseWheelListener(new MouseWheelListener() {

			public void mouseWheelMoved(final MouseWheelEvent mwe) {
				mouseWheel(mwe.getWheelRotation());
			}
		});
		textMode(SCREEN);
		doSetup();
	}

	/**
	 * Overwrite this method to draw 2D objects in your sketch.
	 */
	public void doDraw2D() {
	}

	/**
	 * Overwrite this method to draw 3D objects in your sketch.
	 */
	public void doDraw3D() {
	}

	@Override
	final public void draw() {
		translate(2 * width / 7, height / 2, zoomZ);
		rotateX(rotX + distY);
		rotateY(rotY + distX);
		doDraw3D();
		hint(DISABLE_DEPTH_TEST);
		camera();
		noLights();
		fill(255, 0, 0);
		translate(width / 2, 0, 0);
		doDraw2D();
		hint(ENABLE_DEPTH_TEST);
	}

	/**
	 * Reset the view angle and
	 */
	protected void resetView() {
		rotX = 0.0f;
		rotY = 0.0f;
		distX = 0.0f;
		distY = 0.0f;
	}

	/**
	 * Wheel mouse taken from http://wiki.processing.org/index.php/Wheel_mouse
	 * Zoom in/out using mouse wheel
	 * 
	 * @author Rick Companje
	 */
	void mouseWheel(final int delta) {
		if (mouseX <= width / 2)
			zoomZ += delta * -15;
	}

	/**
	 * Rotate 3D image when mouse is dragged
	 */
	@Override
	public void mousePressed() {
		if (mouseX <= width / 2) {
			lastX = mouseX;
			lastY = mouseY;
		}
	}

	@Override
	public void mouseDragged() {
		distX = radians(mouseX - lastX);
		distY = radians(lastY - mouseY);
	}

	@Override
	public void mouseReleased() {
		rotX += distY;
		rotY += distX;
		distX = distY = 0.0f;
	}

}
