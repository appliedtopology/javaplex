// 
//  setLogging.java
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
//  setLogging.java
// 
//  $Id$
// 

package bsh.commands;

import bsh.CallStack;
import bsh.Interpreter;
import bsh.PlexReader;

/**
	Set the global defaults for input logging. The default directory is 
  ~/plex/ and the default setting is false.	
	void setLogging( [ String default_dir ] )
	void setLogging( [ boolean default_on_flag ] )
*/
public class setLogging 
{
	public static String usage() {
		return "usage: setLogging( boolean default_on_flag )\n" +
      "       setLogging( String default_logdir )\n" ;
	}

	/**
     Implement setLogging(String default_dir) command.
	*/
	public static void invoke(Interpreter env, CallStack callstack, String default_dir){
    PlexReader.setDefaultDir(default_dir);
	}

	/**
     Implement setLogging(boolean default_on_flag) command.
	*/
	public static void invoke(Interpreter env, CallStack callstack, 
                            boolean default_flag) {
    PlexReader.setDefaultFlag(default_flag);
  }
    
}

