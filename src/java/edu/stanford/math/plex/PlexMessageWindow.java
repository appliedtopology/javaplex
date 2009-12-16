//
//  PlexMessageWindow.java
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
//  PlexMessageWindow.java
//
//  $Id$
//

package edu.stanford.math.plex;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

/**
 * A <code>PlexMessageWindow</code> instance is a scrollable text window in
 * which we can insert messages.
 *
 * @version $Id$
 */
public class PlexMessageWindow {

  private JFrame frame;
  private StyledDocument document;
  private StyleContext context;
  private Style style;
  private JTextPane textPane;
  private JScrollPane scrollPane;

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
