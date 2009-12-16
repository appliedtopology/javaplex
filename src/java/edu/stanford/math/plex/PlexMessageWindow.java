package edu.stanford.math.plex;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 * A <code>PlexMessageWindow</code> instance is a scrollable text window in
 * which we can insert messages.
 *
 * @version $Id$
 */
public class PlexMessageWindow {

	protected JFrame frame;
	protected StyledDocument document;
	protected StyleContext context;
	protected Style style;
	protected JTextPane textPane;
	protected JScrollPane scrollPane;

	/**
	 * Create a new PlexMessageWindow with the given title.
	 * <p>
	 * @param      title Title string for the window.
	 */
	public PlexMessageWindow(String title) {
		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		context = new StyleContext();
		document = new DefaultStyledDocument(context);
		style = context.getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);
		StyleConstants.setFontSize(style, 14);
		//StyleConstants.setSpaceAbove(style, 1);
		//StyleConstants.setSpaceBelow(style, 1);
		textPane = new JTextPane(document);
		textPane.setEditable(false);
		scrollPane = new JScrollPane(textPane);
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.setSize(300, 150);
	}

	/**
	 * Print a message string in the window.
	 * <p>
	 * @param      messageString  String to print.
	 */
	public void message(String messageString) {
		final String msg = messageString;
		javax.swing.SwingUtilities.invokeLater
		(new Runnable() {
			public void run() {
				try {
					textPane.setEditable(true);
					document.insertString(document.getLength(), msg, style);
					frame.setVisible(true);
				} catch (BadLocationException e) {
					throw new IllegalStateException("Failed to display message: " + 
							msg);
				}
				textPane.setEditable(false);
			}   
		});
	}
}
