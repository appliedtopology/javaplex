package bsh;

// 
//  JTerm.java
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
//  JTerm.java
// 
//  $Id: JTerm.java,v 1.1 2008/09/16 17:21:32 hsexton Exp $
// 

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;
import bsh.util.*;
import bsh.*;

/**
 * The <code>JTerm</code> class is the entry point for Plex users into
 * Beanshell who want a simple "terminal" interface (no windows, menus,
 * etc.).  <p>
 *
 * @version $Id: JTerm.java,v 1.1 2008/09/16 17:21:32 hsexton Exp $
 */
public class JTerm {
  /**
   * The arguments to main are ignored. This method should be invoked via:
   * <p>
   * <code>java -cp plex.jar JTerm</code>
   */
	public static void main( String args[] ) {
		if (!Capabilities.classExists( "bsh.util.Util" ))
			System.out.println("Can't find the BeanShell utilities.");

    try {
      new Interpreter().eval("makeListener(\"top level\")");
    } catch ( EvalError e ) {
      System.err.println("Couldn't start listener: "+e);
    }
  } 
}


