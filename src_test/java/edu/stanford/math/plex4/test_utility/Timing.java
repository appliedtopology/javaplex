package edu.stanford.math.plex4.test_utility;

import cern.colt.Timer;

/**
 * This class contains static methods for measuring time intervals. It is exists
 * for convenience purposes so that one doesn't have to instantiate an object whenever
 * one wishes to measure a time interval.
 * 
 * @author Andrew Tausz
 *
 */
public class Timing {
	private static Timer timer;
	
	private static void initialize() {
		timer = new Timer();
		
	}
	
	public static void restart() {
		if (timer == null) {
			initialize();
		}
		timer.reset();
		timer.start();
	}
	
	public static void stop() {
		if (timer == null) {
			initialize();
		}
		timer.stop();
	}
	
	public static void reset() {
		if (timer == null) {
			initialize();
		}
		timer.reset();
	}
	
	public static float seconds() {
		if (timer == null) {
			initialize();
		}
		return timer.seconds();
	}
	
	public static void stopAndDisplay() {
		if (timer == null) {
			initialize();
		}
		timer.stop();
		System.out.println("Elapsed time (s): " + timer.seconds());
		timer.reset();
	}
	
	public static void stopAndDisplay(String label) {
		if (timer == null) {
			initialize();
		}
		timer.stop();
		System.out.println("Elapsed time (s) for " + label + ": " + timer.seconds());
		timer.reset();
	}
}
