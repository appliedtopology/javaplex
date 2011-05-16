// 
//  PlexReader.java
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
//  PlexReader.java
// 
//  $Id$
// 

package bsh;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;

/**
 * The <code>PlexReader</code> class is a wrapper for the input Reader for
 * interactive Interpreter instances. We intercept the <code>read()</code>
 * operations, and when input logging is enabled, we write (with conversion
 * if needed) the chars <code>read()</code> into a log file. Such a file
 * can be read and executed later via the <code>source()</code> command,
 * for instance.
 */
public class PlexReader extends FilterReader {

	private static boolean logging_by_default = false;
	private static String default_logfile_dir = System.getProperty("user.home") + File.separator + "plex";
	private static String default_logfile_prefix = "log";
	private static String default_logfile_suffix = ".txt";

	// Something to throw when we get PlexReader errors
	private static final class Error extends IOException {
		public final static long serialVersionUID = 0;
		@SuppressWarnings("unused")
		public Error() {
			super("PlexReader error");
		}
		public Error(String message) {
			super("PlexReader error: " + message);
		}
	}

	// Something to throw when we get "expected" PlexReader errors
	private static final class ContinuableError extends IOException {
		public final static long serialVersionUID = 0;
		@SuppressWarnings("unused")
		public ContinuableError() {
			super("PlexReader exception");
		}
		public ContinuableError(String message) {
			super("PlexReader exception: " + message);
		}
	}

	// Something to throw when we get PlexReader errors we can't handle
	private static final class FatalError extends IOException {
		public final static long serialVersionUID = 0;
		@SuppressWarnings("unused")
		public FatalError() {
			super("Unrecoverable PlexReader error");
		}
		public FatalError(String message) {
			super("Unrecoverable PlexReader error: " + message);
		}
	}

	// We need a place to store a copy of the characters from the underlying
	// stream so that we can convert all of the backslash-u escaped expansions that
	// wind up in these streams for some mysterious reason. Anyway, we copy
	// the characters in bulk from the input stream buffer where they got put
	// by the read, and then we consume as many of them as we can convert at
	// a time, and put those in the actual buffer that gets written to the
	// logfile.
	private static final class CharQ {
		private int first;
		private int free;
		private char[] buf;
		private final static int INIT_SZ = 1024;

		public CharQ() {
			first = 0;
			free = 0;
			buf = new char[INIT_SZ];
		}

		private void reset() {
			first = 0;
			free = 0;
		}

		private final void ensureSpace(int need) {
			if (first > 0) {
				if (first < free) {
					for (int i = first; i < free; i++) {
						buf[i-first] = buf[i];
					}
				}
				free -= first;
				first = 0;
			}
			if ((free + need) > buf.length) {
				// grow the buffer
				assert(first == 0);
				int new_size = 2*buf.length;
				if (new_size < (need + free))
					new_size = need + free;
				char[] new_buf = new char[new_size];
				for (int i = 0; i < free; i++) {
					new_buf[i] = buf[i];
				}
			}
		}

		@SuppressWarnings("unused")
		public final void add(char c) {
			ensureSpace(1);
			buf[free++] = c;
		}

		public final void bulkAdd(char[] b, int pos, int len) {
			ensureSpace(len);
			for (int i = 0; i < len; i++) {
				buf[free++] = b[pos+i];
			}
		}

		public final String toString() {
			if (first < free)
				return new String(buf, first, free-first);
			else
				return new String("");
		}

		public final int available() {
			return (free-first);
		}

		public final boolean hasNext() {
			return (first < free);
		}

		static final int char_to_hex(char c) throws FatalError {
			if ((c >= '0') && (c <= '9'))
				return (c - '0');
			else if ((c >= 'a') && (c <= 'f'))
				return (c - 'a') + 10;
			else if ((c >= 'A') && (c <= 'F'))
				return (c - 'A') + 10;
			else 
				throw new FatalError("Unexpected value, " + c + ", in hex escape sequence.");
		}


		public final char next() throws Error {
			if (first < free)
				return buf[first++];
			else
				throw new Error("reading past end of queue");
		}

		public final char peek() throws Error {
			if (first < free)
				return buf[first];
			else
				throw new Error("peeking past end of queue");
		}

		public final int current() {
			return first;
		}

		public final void rewind(int new_first) {
			if ((0 <= new_first) && (new_first <= first))
				first = new_first;
			else
				throw new IllegalArgumentException("rewinding first to illegal value");
		}

		public final char convertNext() throws Error, ContinuableError, FatalError {
			int initial = current();
			int available = available();

			// Should never get called when available <= 0.
			char c = next();

			if (c != '\\') {
				return c;
			} else {
				// backslash case
				if (available == 1) {
					rewind(initial);
					throw new ContinuableError("trailing backslash");
				} else if (peek() != 'u') {
					return c;
				} else if (available < 6) {
					rewind(initial);
					throw new ContinuableError("incomplete binary escape sequence");
				} else {
					// backslash-u case with enough characters. Unless the stream is
					// garbled, we can decode. If the stream is garbled, we throw a
					// FatalError with a message that shows the queue contents. The
					// FatalError signals the calling method to turn off logging, and
					// perhaps the unlucky user send us a bug report with the
					// message, if we are not also unlucky.

					// eat the 'u'
					next();

					c = (char)(char_to_hex(next()) << 12 |
							char_to_hex(next()) << 8 |
							char_to_hex(next()) << 4 |
							char_to_hex(next()));
					return c;
				} 
			}
		}
	}

	private boolean logging;
	private String logfile_prefix;
	private String logfile_suffix;
	private String logfile_dir;
	// can be set to override generated names
	private String logfile_name;
	private final char[] logbits;
	private int bits_offset;
	private OutputStreamWriter logstream;
	private final CharQ logqueue;
	private final static int LOGBITS_LEN = 10;

	/**
	 * Constructor PlexReader, only called by PlexInterpreter(). We ignore the name argument.
	 * <p>
	 * @param      in Input Reader for an interative interpreter instance.
	 * @param      ws_name Workspace name, which we ignore.
	 */
	public PlexReader(Reader in, String ws_name) {
		super(in);
		logging = logging_by_default;
		logfile_prefix = default_logfile_prefix + "_"; //  ws_name + "_";
		logfile_suffix = default_logfile_suffix;
		logfile_dir = default_logfile_dir;
		logbits = new char[LOGBITS_LEN];
		logqueue = new CharQ();
		bits_offset = 0;
	}

	// Clear buffer state.
	private void reset_log_data() {
		logqueue.reset();
		bits_offset = 0;
	}

	/**
	 * Wrap Reader instance in a PlexReader, unless we already did. We ignore the name argument.
	 * <p>
	 * @param      in Input Reader for an interative interpreter instance.
	 * @param      ws_name Workspace name, which we ignore.
	 * @return A PlexReader wrapper for in.
	 */
	static Reader log(Reader in, String ws_name) {
		if (in instanceof PlexReader)
			return in;
		else {
			PlexReader return_value = null;
			try {
				return_value = new PlexReader(in, ws_name);
				if (logging_by_default)
					return_value.enable(null);
			} catch (java.io.IOException ioe) {
				return (in);
			}
			return return_value;
		}
	}

	/**
	 * Wrap InputStream instance in a PlexReader. We ignore the name argument.
	 * <p>
	 * @param      in InputStream for an terminal-based interpreter instance.
	 * @param      ws_name Workspace name, which we ignore.
	 * @return A PlexReader wrapper for in.
	 */
	static Reader log(InputStream in, String ws_name) {
		PlexReader return_value = null;
		try {
			return_value = new PlexReader(new InputStreamReader(in), ws_name);
			if (logging_by_default)
				return_value.enable(null);
		} catch (java.io.IOException ioe) {
			return (new InputStreamReader(in));
		}
		return return_value;
	}

	// Try to make a new logfile with a name that makes it easy to find the
	// newest one (e.g., ls lists the files in order of creation). If that
	// doesn't seem to be working, give up and use the tempfile method.
	private static File createNewFile(String prefix, 
			String suffix, 
			String dir) throws IOException {
		for (int i = 0; i < 100; i++) {
			String name = String.format("%s%s%s%02d%s",
					dir, File.separator, prefix, i, suffix);
			File tmp = new File(name);
			if (!tmp.exists())
				return tmp;
		}

		// give up
		return(File.createTempFile(prefix, suffix, new File(dir)));
	}


	/**
	 * Enable logging, making sure that there is an underlying logstream.
	 * <p>
	 * @param      log_name Name to use as logfile, or null, if the default is okay.
	 */
	public void enable(String log_name) throws IOException {
		if (logstream == null) {
			// no logstream, so make one
			logfile_name = log_name;
			if (logfile_name != null) {
				logstream = 
					new OutputStreamWriter(new FileOutputStream(new File(logfile_name)));
			} else {
				File lfile = createNewFile(logfile_prefix, logfile_suffix, logfile_dir);
				logfile_name = lfile.toString();
				logstream = new OutputStreamWriter(new FileOutputStream(lfile));
			}
		}
		logging = true;
	}

	/**
	 * Enable logging, using default settings if there is no underlying logstream.
	 * <p>
	 */
	final void enable() {
		if (logstream != null) {
			if (!logging)
				bits_offset = 0;
			logging = true;
		} else {
			try {
				enable(null);
			} catch (java.io.IOException ioe) {
				logging = false;
			}
			return;
		}
	}

	/**
	 * Disable logging, flushing any unwritten input, and leaving the
	 * underlying logstream, if any, in place. <p>
	 */
	final void disable() {
		if (logging) {
			flush();
		}
		logging = false;
	}

	/**
	 * Force logstream to go to specified filename.
	 * <p>
	 * @param new_log_name Name to use as logfile -- must be the name of a
	 * writable file.
	 */
	public void setLogfile (String new_log_name) {
		if (logstream != null) {
			File old_file = new File(logfile_name);
			File new_file = new File(new_log_name);
			if (old_file.equals(new_file))
				return;
			else {
				try {
					logstream.flush();
				} catch (IOException e) {
				}
				try {
					logstream.close();
				} catch (IOException e) {
				}
				reset_log_data();
				logstream = null;
			}
		}
		logfile_name = new String(new_log_name);
		try {
			logstream = 
				new OutputStreamWriter(new FileOutputStream(new File(logfile_name)));
		} catch (IOException e) {
			throw new IllegalArgumentException("Failed setting logfile to: " +
					logfile_name + "\n" +
					e.getMessage());
		}
	}

	// Append the log to the specified log file.
	private boolean flush() {
		if (logging && (logstream != null) && (bits_offset > 0)) {
			try {
				logstream.write(logbits, 0, bits_offset);
				logstream.flush();
			} catch (java.io.IOException ioe) {
				// turn off logging -- we're losing here
				logging = false;
				bits_offset = 0;
				return false;
			}
		}
		bits_offset = 0;
		return true;
	}

	// Read the next character out of the logqueue, and do whatever
	// conversions are appropriate. If we encounter a FatalError, turn off
	// logging and try to indicate the nature of the lossage to the user. If
	// we encounter a ContinuableError, return. Encountering an Error is a
	// sign of a programming mistake, so we want to hear about that, too, but
	// I doubt that we will, and I hope that it never comes up.
	private void convertInput() {
		int q_pos = 0;
		try {
			while(logqueue.hasNext()) {
				q_pos = logqueue.current();
				char last_char = logbits[bits_offset++] = logqueue.convertNext();
				if ((bits_offset == logbits.length) ||
						(last_char == '\n')) {
					boolean success = flush();
					if (!success)
						throw new IllegalStateException("Writing log to " + logfile_name + 
						" failed, and is now disabled.");
				}
			}
		} catch (ContinuableError ce) {
			return;
		} catch (FatalError fe) {
			logging = false;
			logqueue.rewind(q_pos);
			throw new IllegalStateException("Input logging disabled, unrecognized input: " 
					+ logqueue.toString());
		} catch (Error e) {
			throw new IllegalStateException("this should be impossible: " +
					e.getMessage());
		}
	}

	// Log b, if logging is on, and return the character.
	private void log(char[] buf, int off, int len) {
		if (logging) {    
			logqueue.bulkAdd(buf, off, len);
			convertInput();
		}
	}

	/**
	 * Wrapper for normal read to buffer operation. If logging is on, log the
	 * characters that were read, otherwise, do nothing.
	 * <p>
	 * @param buf Where to write the characters read.
	 * @param off Offset at which to start writing the characters.
	 * @param len Max number of characters to read.
	 * @return Number of characters read.
	 */
	public int read(char[] buf, int off, int len) throws IOException {
		int chars_read = in.read(buf, off, len);
		if (chars_read != -1) {
			log(buf, off, chars_read);
		}
		return chars_read;
	}

	/**
	 * Set the default logging flag. The default default is <code>false</code>.
	 * Invoked via the <code>setLogging()</code> command.
	 * <p>
	 * @param flag If true, input logging is on by default, else off.
	 */
	public static void setDefaultFlag(boolean flag) {
		logging_by_default = flag;
	}

	/**
	 * Return a String describing the logging status, including defaults.
	 * <p>
	 * @return Log status string.
	 */
	public String log_status() {

		String instance_status = "";
		if (logstream == null) {
			instance_status = "Logging is off";
		} else if (logging) {
			instance_status = "Logging is enabled, and the logfile is " +
			logfile_name;
		} else {
			instance_status = "Logging is diabled, and the logfile is " +
			logfile_name;
		}

		String default_status = "";
		if (logging_by_default) {
			default_status = "By default, it is on and the logfile directory is " +
			default_logfile_dir;
		} else
			default_status = "By default, it is off and the logfile directory is " +
			default_logfile_dir;

		return instance_status + ". " + default_status + ".";
	}

	/**
	 * Set the default logging directory. The default default is <code>~/plex</code>.
	 * Invoked via the <code>setLogging()</code> command.
	 * <p>
	 * @param pathname Set the default directory in which to store log files.
	 */
	public static void setDefaultDir(String pathname) {
		File tmp = new File(pathname);
		if (tmp.exists() && tmp.isDirectory()) {
			// that's good enough for me
			default_logfile_dir = new String(pathname);
		} else 
			throw new IllegalArgumentException
			("Default logging directory not a directory: "  + pathname);
	}
}
