package edu.stanford.math.plex4.test_utility;

import cern.colt.Timer;

public class Timing {
	private static Timer timer;
	
	private static void initialize() {
		timer = new Timer();
		
	}
	
	public static void start() {
		if (timer == null) {
			initialize();
		}
		
		timer.start();
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
