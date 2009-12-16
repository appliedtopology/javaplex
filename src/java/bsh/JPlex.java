package bsh;

// 
//  JPlex.java
// 
//  ***************************************************************************
// 
//  Copyright 2008, Stanford University
// 
//  Permission to use, copy, modify, and distribute this software and its
//  documentation for any purpose and without fee is hereby granted,
//  provided that the above copyright notice appear in all copies and that
//  both that copyright notice and this permission notice appear in
//  supporting documentation, and that the name of Stanford University not
//  be used in advertising or publicity pertaining to distribution of the
//  software without specific, written prior permission.  Stanford
//  University makes no representations about the suitability of this
//  software for any purpose.  It is provided "as is" without express or
//  implied warranty.
// 
//  ***************************************************************************
// 
//  JPlex.java
// 
//  $Id: JPlex.java,v 1.2 2008/09/25 17:50:25 hsexton Exp $
// 

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;
import bsh.util.*;
import bsh.*;
import edu.stanford.math.plex.*;

/**
 * The <code>JPlex</code> class is the entry point for Plex users into Beanshell.
 * This file is a trivial modification of the analogous code in Beanshell.
 * <p>
 *
 * @version $Id: JPlex.java,v 1.2 2008/09/25 17:50:25 hsexton Exp $
 */
public class JPlex {
  /**
   * The arguments to main are ignored. This method should be invoked via:
   * <p>
   * <code>java -cp plex.jar JPlex</code>
   */
	public static void main( String args[] ) {
		if (!Capabilities.classExists( "bsh.util.Util" ))
			System.out.println("Can't find the BeanShell utilities.");

		if (Capabilities.haveSwing()) {
			try {
        Plex.useMessageWindow(true);
				new Interpreter().eval("desktop()");
			} catch ( EvalError e ) {
				System.err.println("Couldn't start desktop: "+e);
			}
		} else {
			System.err.println("Can't find required javax.swing package");
		}
	}
}

