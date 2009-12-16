// 
//  PlexInterpreter.java
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
//  PlexInterpreter.java
// 
//  $Id$
// 

package bsh;


/**
 * The <code>PlexInterpreter</code> class is a wrapper for Interpreter
 * whose only purpose at the moment is to allow users to log their input to
 * a file. The <code>PlexReader</code> class is the one that does the
 * actual logging. Instances of this class are easier to get our hands on
 * in some scripts, so we put methods to enable/disable logging and specify
 * the logfile name in here.
 */
public class PlexInterpreter extends Interpreter {

  // Needed because Interpreter is serializable.
  public static final long serialVersionUID = 0;

  /**
   * Constructor PlexInterpreter, only called in the makeWorkspace.bsh
   * script. We ignore the name argument.
   * <p>
   * @param      console Console for the workspace using this interpreter instance.
   * @param      name Workspace name, which we ignore.
   */
  public PlexInterpreter(ConsoleInterface console, String name) {
    super(PlexReader.log(console.getIn(), name), console.getOut(), console.getErr(), 
          true, null);
		setConsole(console);
  }

  /**
   * Constructor PlexInterpreter, only called in the makeListener.bsh.
   * <p>
   * @param      name Workspace name, which we ignore.
   */
  public PlexInterpreter(String name) {
    super(PlexReader.log(System.in, name), System.out, System.err, true);
  }

  /**
   * Specify the name of the logfile.
   * <p>
   * @param      logfile_name Where to write the input log, if we keep one.
   */
  public void setLogfile(String logfile_name) {
    if (in instanceof PlexReader)
      ((PlexReader)in).setLogfile(logfile_name);
  }

  /**
   * Turn ON input logging for this Interpreter.
   * <p>
   */
  public void enableLogging() {
    if (in instanceof PlexReader)
      ((PlexReader)in).enable();
  }

  /**
   * Return the status string
   * <p>
   */
  public String logStatus() {
    if (in instanceof PlexReader) {
      return ((PlexReader)in).log_status();
    } else
      return "No logging status available for this Interpreter instance.";
  }

  /**
   * Turn OFF input logging for this Interpreter.
   * <p>
   */
  public void disableLogging() {
    if (in instanceof PlexReader)
      ((PlexReader)in).disable();
  }
}
