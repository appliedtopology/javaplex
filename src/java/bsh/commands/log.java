// 
//  log.java
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
//  log.java
// 
//  $Id$
// 

package bsh.commands;

import bsh.CallStack;
import bsh.Interpreter;
import bsh.PlexInterpreter;

/**
	Turn on/off input logging in the current interpreter, specify the
	specific log file to use, or return the logging status string.
	void log( [ String logfile_name ] )
	void log( [ boolean on_flag ] )
	String log()
*/
public class log 
{
	public static String usage() {
		return "usage: log( boolean on_flag )\n" +
      "       log( String logfile_name )\n" +
      "       log( )\n" ;
	}

	/**
     Implement log() command.
	*/
	public static String invoke(Interpreter env, CallStack callstack){
    if (env instanceof PlexInterpreter) {
      return ((PlexInterpreter)env).logStatus();
    } else
      throw new IllegalArgumentException("No input logging for this " + 
                                         "interpreter instance");
  }

	/**
     Implement log(String logfile_name) command.
	*/
	public static void invoke(Interpreter env, CallStack callstack, String logfile_name){
    if (env instanceof PlexInterpreter) {
      ((PlexInterpreter)env).setLogfile(logfile_name);
    } else
      throw new IllegalArgumentException("No input logging for this " + 
                                         "interpreter instance");
  }

	/**
     Implement log(boolean on_flag) command.
	*/
	public static void invoke(Interpreter env, CallStack callstack, 
                            boolean on_flag) {
    if (env instanceof PlexInterpreter) {
      if (on_flag)
        ((PlexInterpreter)env).enableLogging();
      else
        ((PlexInterpreter)env).disableLogging();
    } else
      throw new IllegalArgumentException("No input logging for this " + 
                                         "interpreter instance");
  }
    
}

