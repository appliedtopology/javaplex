package edu.stanford.math.plex;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * The <code>BCPlot</code> class does simple plotting of PersistenceIntervals.
 *
 * @version $Id$
 */
public class BCPlot extends JPanel {

	protected static final int CIRCLE_SIZE = 7;

	protected static final long serialVersionUID = 1L;

	// If it is not a barcode plot it will be a scatter plot
	protected boolean plotTypeScatter = false;

	// Fixed Ruler for the bar graph
	protected Ruler fixedRuler;

	// The amount of space in pixels for the fixed ruler at top of scroll pain
	protected static final int FIXED_RULER_HEIGHT = 35;

	//
	protected static final int MAX_GRID_HEIGHT_PER_PAGE = 1500;

	//
	protected static final int MAX_DISPLAYABLE_BARS = MAX_GRID_HEIGHT_PER_PAGE - 1;

	//
	protected static final int DEFAULT_BARS_PER_PAGE = 249;

	// The panel that actually holds the Graphics of the plot
	protected DrawingPane barcodePane;

	protected Action fileSaveMenuHandler;

	// This dimension is of the region Used For the Plot Legend, Ruler,
	// Labels, and Title
	protected Rectangle baseRegionUsedForPlotLegendRulerLabels;

	// This is the height in Pixels of the region Used For the Plot Legend,
	// Ruler, Labels, and Tittle
	protected int heightOfBaseRegionUsedForPlotLegendRulerLabels = 75;
	protected int widthOfSideRegionUsedForPlotLegendRulerLabels = 100;

	protected int leftMargin = widthOfSideRegionUsedForPlotLegendRulerLabels/2;
	protected int rightMargin = widthOfSideRegionUsedForPlotLegendRulerLabels/2;

	protected String legendMajorString = "", legendMinorString = null;

	// Set Visualization Region Size
	protected int fullVisualizationWidth = 1200;

	// Width of the grid for the plot
	protected int gridWidth;

	// Height of the grid for the plot 10 inch at 1500 dpi
	protected int gridHeight = MAX_GRID_HEIGHT_PER_PAGE;

	protected int fullVisualizationHeight;

	// barcode values
	protected double[][] plotValue;

	// barcode plot upper bound
	protected double plotUpperBound;

	protected Color gridRulerFontColor, tickUpperBoundColor, 
	gridTickMinorColor, gridTickMajorColor;
	protected Color upperBoundFontColor, labelFontColor, 
	legendMinorFontColor, legendMajorFontColor;
	protected Color fixedRulerBorderColor, fixedRulerColor, barcodeBarColor;
	protected Color fixedRulerFontColor, 
	fixedRulerMinorTickColor, fixedRulerMajorTickColor;
	protected Font labelFont, legendMinorFont, legendMajorFont, 
	gridRulerFont, fixedRulerFont;
	protected FontMetrics legendMinorFontMetrics, legendMajorFontMetrics;
	protected FontMetrics fixedRulerFontMetrics, labelFontMetrics, gridRulerFontMetrics;

	// Writable Region
	protected int upperMargin;

	// Barcode Parameters
	protected int barcodeBarHeight, barcodeBarSpacing;

	// is a  grid visible behind the plot
	protected Boolean gridIsVisible;

	// Flag indicating plot is being saved to a file
	protected Boolean plotBeingSaved = false;

	// unit value for the pixels unit value in rulers
	protected double unitsPerPixel;

	// how how many pixels are in a unit
	protected double pixelsPerUnit;

	// How often do you place a major tick mark on a ruler or grid
	protected int tickMajorPlacement;

	// How often do you place a tick in terms of pixels
	protected int tickGap;

	// Used to load and save grahpic2d regions as graphic files
	protected BufferedImage bufferedImage;

	// Indicator that a background image is loaded
	protected Boolean thereIsaBackgroundImage;

	// Directory path to background image
	protected String backgroundImageName;

	/**
	 * Returns the maximum number of barcodes that can be "plotted".
	 */
	public int getMaxDisplayableBars(){
		return MAX_DISPLAYABLE_BARS;
	}

	/**
	 * Sets the Major Legend
	 */
	protected void setMajorLegend(String s){
		legendMajorString = s;
	}

	protected BCPlot() {
		throw new IllegalStateException("Do not use");
	}

	// protected constructor.
	protected BCPlot(double[][] values, double upperBound, boolean forceScatter) {
		validate(values, upperBound);

		// Set Barcode Properties
		// if the interval count <= {250,300,375,500,750,1500};
		// then {barcodeBarHeight,barcodeBarSpacing} is 
		// {{3,3},{3,2},{2,2},{2,1},{1,1},{1,0}};
		gridWidth = fullVisualizationWidth;

		if (forceScatter || (plotValue.length > MAX_DISPLAYABLE_BARS)) {
			plotTypeScatter = true;
			barcodeBarHeight = 0;
			barcodeBarSpacing = 0;
			upperMargin = 0;

			gridWidth = fullVisualizationWidth - rightMargin - leftMargin;
			gridHeight = gridWidth;
			fullVisualizationHeight = gridHeight + 
			heightOfBaseRegionUsedForPlotLegendRulerLabels;
		} else { 
			// At this point it looks to be a barcode plot
			plotTypeScatter = false;
			fullVisualizationWidth += 20;
			// Set barcode plot visual properties
			if(plotValue.length <= DEFAULT_BARS_PER_PAGE){
				barcodeBarHeight = 3;
				barcodeBarSpacing = 3;
				upperMargin = 3;

			}else if(plotValue.length <= 299){
				barcodeBarHeight = 3;
				barcodeBarSpacing = 2;
				upperMargin = 2;

			}else if(plotValue.length <= 374){
				barcodeBarHeight = 2;
				barcodeBarSpacing = 2;
				upperMargin = 2;

			}else if(plotValue.length <= 499){
				barcodeBarHeight = 2;
				barcodeBarSpacing = 1;
				upperMargin = 1;

			}else if(plotValue.length <= 749){
				barcodeBarHeight = 1;
				barcodeBarSpacing = 1;
				upperMargin = 1;

			}else if(plotValue.length < MAX_DISPLAYABLE_BARS){
				barcodeBarHeight = 1;
				barcodeBarSpacing = 0;
				upperMargin = 1;

			}else if(plotValue.length == MAX_DISPLAYABLE_BARS){
				barcodeBarHeight = 1;
				barcodeBarSpacing = 0;
				upperMargin = 0;

			}

			gridHeight = upperMargin + 
			((barcodeBarHeight + barcodeBarSpacing) * plotValue.length);
			fullVisualizationHeight = gridHeight + 
			heightOfBaseRegionUsedForPlotLegendRulerLabels;
		}

		// run it!
		initJFrame();
	}

	// Put together an error string for the argument checker.
	protected static String illegal_interval_string(int i, double a, double b) {
		return String.format("Interval %d is [%.8g, %.8g), " +
				"not a non-empty subset of [0.0, %.8g)",
				i, a, b, Double.MAX_VALUE);
	}

	/**
	 * Validate the upperBound and the values used for the Barcode Plot
	 */
	protected void validate(double[][] numbers, double upperBound) {
		if (numbers == null)
			throw new IllegalArgumentException("No plot values");
		if ((upperBound >= Double.MAX_VALUE) || (upperBound <= 0)) {
			throw new IllegalArgumentException
			("upperBound, "  + upperBound + ", must be in the range " +
					"[0.0, " + Double.MAX_VALUE + ")");
		}
		for (int i = 0; i < numbers.length; i++) {
			if ((numbers[i][0] < 0) ||
					(numbers[i][0] >= Double.MAX_VALUE) ||
					(numbers[i][0] > numbers[i][1]) ||
					(numbers[i][1] > Double.POSITIVE_INFINITY))
				throw new IllegalArgumentException
				(illegal_interval_string(i, numbers[i][0], numbers[i][1]));
		}
		// set boundries and values for the plot
		plotUpperBound = upperBound;
		plotValue = numbers;
	}

	/**
	 *
	 */
	protected void initJFrame(){
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		JPanel instructionPanel = new JPanel(new GridLayout(0,1));
		instructionPanel.setFocusable(true);

		//Set up the drawing area.
		barcodePane = new DrawingPane();
		barcodePane.setBackground(Color.white);
		barcodePane.setPreferredSize(new Dimension(fullVisualizationWidth,
				fullVisualizationHeight));

		//Create a scroll panel with a fixed Ruler along the top.
		JScrollPane barcodeScrollPane = new JScrollPane(barcodePane);
		barcodeScrollPane.setPreferredSize(new Dimension(fullVisualizationWidth+20,
				fullVisualizationHeight));
		barcodeScrollPane.setViewportBorder
		(BorderFactory.createLineBorder(fixedRulerBorderColor));

		//Create a fixed header for display pane.
		fixedRuler = new Ruler(barcodePane);
		barcodeScrollPane.setColumnHeaderView(fixedRuler);

		//Put it in this panel.
		add(barcodeScrollPane,BorderLayout.CENTER);
		setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

		// Create the handler for the Menu Bar Items
		fileSaveMenuHandler =
			new fileSaveMenuHandler("fileSaveMenuHandler", null,
					"Handles the File Menu Save action event",
					new Integer(2));
	}

	/*
	 * Create The Menu Bar
	 */
	protected JMenuBar createMenuBar() {
		JMenuItem menuItem = null;
		JMenuBar menuBar;

		//Create the menu bar.
		menuBar = new JMenuBar();

		//Create the first menu.
		JMenu mainMenu = new JMenu("Save as");
		mainMenu.setMnemonic(KeyEvent.VK_S);
		mainMenu.setMnemonic(KeyEvent.VK_S);
		mainMenu.setIcon(new ImageIcon());

		//Give all image file formats that are available as menu options
		String[] imageFileFormats = getFormats();
		for (int i = 0; i < imageFileFormats.length; i++) {
			menuItem = new JMenuItem(imageFileFormats[i]);
			menuItem.setIcon(null);
			menuItem.addActionListener(fileSaveMenuHandler);
			mainMenu.add(menuItem);
		}

		//Set up the menu bar.
		menuBar.add(mainMenu);
		return menuBar;
	}

	/*
	 * Return the formats sorted alphabetically and in lower case
	 */
	protected String[] getFormats() {
		String[] formats = ImageIO.getWriterFormatNames();
		TreeSet<String> formatSet = new TreeSet<String>();
		for (String s : formats) {
			formatSet.add(s.toLowerCase());
		}
		return formatSet.toArray(new String[0]);
	}

	/*
	 * This handles the File Save Menu action events
	 */
	protected class fileSaveMenuHandler extends AbstractAction {
		protected static final long serialVersionUID = 1L;
		protected fileSaveMenuHandler(String text, ImageIcon icon,
				String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		/**
		 * Action events handler
		 */
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();

			if (Arrays.asList(getFormats()).contains(cmd)){

				// Use the format name to initialise the file suffix.
				File file = new File("untitledBarcode." + cmd);

				JFileChooser fc = new JFileChooser();
				fc.setSelectedFile(file);
				int rval = fc.showSaveDialog(BCPlot.this);
				if (rval == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();
					barcodePane.saveImage(file, cmd);
				}
			}
		}
	}

	/**
	 * The component inside the scroll pane.
	 * This is were the BARCODE PLOT is set up displayed and saved from
	 */
	protected class DrawingPane extends JPanel {
		protected static final long serialVersionUID = 1L;
		public static final int ON_TOP = 0;
		public static final int ON_BOTTOM = 1;
		public static final int ON_LEFT = 3;
		public static final int ON_RIGHT = 4;

		/*
		 * This is were the BARCODE PLOT is set up and drawn
		 */
		protected DrawingPane(){
			super();

			// Display loaded background image
			thereIsaBackgroundImage = false;

			// Display a background grid
			gridIsVisible = true;    

			// Visualization Colors
			barcodeBarColor = Color.black;

			tickUpperBoundColor = new Color(0xff,0x00,0x00);
			gridTickMajorColor = new Color(0x0f,0x85,0xff);
			gridTickMinorColor = new Color(0xa9,0xcc,0xdf);

			// Fixed Ruler Colors.
			fixedRulerColor = new Color(0x97,0xd8,0xff);
			fixedRulerMinorTickColor = Color.black;
			fixedRulerMajorTickColor = Color.black;
			fixedRulerBorderColor = Color.black;

			// Ruler Text
			fixedRulerFont = new Font("sansserif", Font.PLAIN, 10);
			fixedRulerFontColor = Color.black;
			fixedRulerFontMetrics = this.getFontMetrics(fixedRulerFont);

			// Fonts
			labelFont = new Font("sansserif", Font.BOLD, 14);
			labelFontColor = Color.red;
			labelFontMetrics = this.getFontMetrics(labelFont);

			legendMinorFont = new Font("sansserif", Font.BOLD, 10);
			legendMinorFontColor =  Color.red; //new Color(0x01,0x01,0xe5);
			legendMinorFontMetrics = this.getFontMetrics(legendMinorFont);

			legendMajorFont = new Font("sansserif", Font.BOLD, 20);
			legendMajorFontColor =  new Color(0x01,0x01,0xe5);
			legendMajorFontMetrics = this.getFontMetrics(legendMajorFont);

			gridRulerFont = new Font("sansserif", Font.PLAIN, 10);
			gridRulerFontColor =  new Color(0x0f,0x85,0xff);
			gridRulerFontMetrics = this.getFontMetrics(gridRulerFont);

			upperBoundFontColor =  new Color(0xff,0x00,0x00);

			// Set dimension of the region Used For the Plot Legend, Ruler,
			// Labels, and Title
			baseRegionUsedForPlotLegendRulerLabels =
				new Rectangle(0, gridHeight, gridWidth,
						heightOfBaseRegionUsedForPlotLegendRulerLabels);
			unitsPerPixel = plotUpperBound/gridWidth;
			pixelsPerUnit = (double)gridWidth/plotUpperBound;
			tickMajorPlacement = 5;
			tickGap = (gridWidth/100);
		}

		/*
		 *
		 */
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			int xOffset = !plotTypeScatter ? 0 : leftMargin;

			// Make the background white
			g.setColor(Color.white);
			g.fillRect(0, 0, fullVisualizationWidth, fullVisualizationHeight);

			// if there is one, Draw Background Image
			if (thereIsaBackgroundImage){
				g.drawImage(bufferedImage, 0, 0, null);
			}

			// if it is visible Draw grid
			if (gridIsVisible){
				drawGridLines(g);
			}
			// Horizontal line at bottom of graphing region
			g.setColor(gridTickMajorColor);
			g.drawLine(0+xOffset, gridHeight+1, gridWidth+xOffset, gridHeight+1);

			//draw a ruler at the bottom
			drawRuler(g,DrawingPane.ON_BOTTOM);

			if (!plotTypeScatter){
				// Draw Barcodes
				int y, barcodeBarLength;
				g.setColor(barcodeBarColor);
				for (int i = 0; i < plotValue.length; i++) {
					double a = plotValue[i][0];
					double b = plotValue[i][1];
					barcodeBarLength = (int)((b - a) * pixelsPerUnit);
					y = (i * (barcodeBarSpacing + barcodeBarHeight)) + upperMargin;

					// If the B value is greater than the UPPER BOUND of the barcode plot
					if (b > plotUpperBound) {
						barcodeBarLength = (int)((plotUpperBound - a) * pixelsPerUnit) + 10;
					}
					int x = (int)(a * pixelsPerUnit);
					g.fillRect(x, y, barcodeBarLength, barcodeBarHeight);
				}
			}else{
				// Draw a ruler at the bottom
				//drawRuler(g,DrawingPane.ON_LEFT);
				// Plot the dots
				g.setColor(barcodeBarColor);
				int aCount=0, bCount=0;
				int x=0, y=0;
				for (int i = 0; i < plotValue.length; i++) {
					double a = plotValue[i][0];
					double b = plotValue[i][1];
					y = gridWidth - (int)(b * pixelsPerUnit);
					x = (int)(a * pixelsPerUnit);
					if (a > plotUpperBound) {
						// If the A value is greater than the UPPER BOUND 
						// of the scatter plot so is B so just count it
						aCount++;
						bCount++;
					} else if (b > plotUpperBound) {
						// If the B value is greater than the UPPER BOUND of the 
						// scatter plot mark it on the A=B co-ordinate with a 
						// green dot and count it
						g.setColor(Color.red);
						g.fillOval(x+xOffset-(CIRCLE_SIZE/2), -(CIRCLE_SIZE/2), CIRCLE_SIZE, CIRCLE_SIZE);
						bCount++;
					} else {
						g.setColor(barcodeBarColor);
						g.fillOval(x+xOffset-(CIRCLE_SIZE/2), y-(CIRCLE_SIZE/2), CIRCLE_SIZE, CIRCLE_SIZE);
					}
				}
				// Write out the legends at the center under the plot
				if ((aCount > 0 || bCount > 0) && (!plotBeingSaved)) {
					String aText = "";
					if (aCount > 0) {
						if (aCount > 1) 
							aText = "There are " + aCount + " X,Y points completely off the grid.";
						else  
							aText = "One of the X,Y points is completely off the grid.";
					}

					String bText = "";
					if ((bCount-aCount) > 0) {
						if ((bCount-aCount) > 1) 
							bText = "There are " + (bCount-aCount) + " points above the top of the grid.";
						else  
							bText = "One of the X,Y points is above the top of the grid.";
					}

					centerText(aText,
							labelFont,
							labelFontMetrics,
							labelFontColor,
							bText,
							labelFont,
							labelFontMetrics,
							labelFontColor,
							g,
							(int)(gridWidth * 0.75)+xOffset,(int)(gridWidth * 0.75),
							60,
							40,
							0);
				}
			}

			// Write out the legends at the center under the plot
			centerText(legendMajorString,
					legendMajorFont,
					legendMajorFontMetrics,
					legendMajorFontColor,
					legendMinorString,
					legendMinorFont,
					legendMinorFontMetrics,
					legendMinorFontColor,
					g,
					0+xOffset, gridHeight+(2*gridRulerFontMetrics.getHeight()) + 3,
					baseRegionUsedForPlotLegendRulerLabels.width,
					baseRegionUsedForPlotLegendRulerLabels.height - 
					(2*gridRulerFontMetrics.getHeight())-3,
					0);
		}

		/*
		 * Assumption for fullVisualization
		 * 150 dpi and 8"x10" plot region => 1200x1500 pixels
		 * tick
		 */
		protected void drawGridLines(Graphics g){
			int xOffset = !plotTypeScatter ? 0 : leftMargin;

			// Draw the vertical lines
			int tickMinorCount = 0;
			for (int i=0; i <= gridWidth; i+=tickGap) {
				if(tickMinorCount%tickMajorPlacement == 0){
					if(i == gridWidth){
						g.setColor(tickUpperBoundColor);
					}else{
						g.setColor(gridTickMajorColor);
					}
				}else{
					g.setColor(gridTickMinorColor);
				}
				g.drawLine(i+xOffset, 0, i+xOffset, gridHeight);
				tickMinorCount++;
			}
			// Draw the horizontal lines
			if (plotTypeScatter){
				tickMinorCount = 0;
				for (int i=0; i <= gridWidth; i+=tickGap) {
					if(tickMinorCount%tickMajorPlacement == 0){
						if(i == 0){
							g.setColor(tickUpperBoundColor);
						}else{
							g.setColor(gridTickMajorColor);
						}
					}else{
						g.setColor(gridTickMinorColor);
					}
					g.drawLine(0+xOffset, i, gridWidth+xOffset, i);
					tickMinorCount++;
				}
				g.setColor(tickUpperBoundColor);
				g.drawLine(0+xOffset, gridWidth, gridWidth+xOffset, 0);
			}

		}

		/*
		 * Assumption for fullVisualization
		 * 150 dpi and 8"x10" plot region => 1200x1500 pixels
		 * tick
		 */
		protected void drawRuler(Graphics g, int position){
			int xOffset = !plotTypeScatter ? 0 : leftMargin;

			Font rf;
			FontMetrics rfm;
			Color rfc;
			int tickMajorLength;
			int tickMinorLength;
			int toggleLength;

			int rulerY;
			Color tMajorC, tMinorC;

			String text = null;
			boolean itShouldBePlacedHigh = true;
			int tickLineLength = 0;

			// Used for drawing lines and placing text
			int fromY=0,toY=0,atY=0,fromX=0,toX=0,atX=0;

			// The angle in PI radians to rotate text
			double sr = 0;

			switch (position) {
			case ON_TOP: 
				rf = fixedRulerFont;
				rfm = fixedRulerFontMetrics;
				rfc = fixedRulerFontColor;
				tickMajorLength = 5;
				tickMinorLength = 1;
				rulerY = 0;
				tMinorC = fixedRulerMinorTickColor;
				tMajorC = fixedRulerMajorTickColor;
				toggleLength = 10;
				break;
			case ON_BOTTOM: 
				rf = gridRulerFont;
				rfm = gridRulerFontMetrics;
				rfc = gridRulerFontColor;
				tickMajorLength = 5;
				tickMinorLength = 1;
				rulerY = gridHeight+1;
				tMajorC = gridTickMajorColor;
				tMinorC = gridTickMinorColor;
				toggleLength = 10;
				break;
			case ON_LEFT: 
				rf = gridRulerFont;
				rfm = gridRulerFontMetrics;
				rfc = gridRulerFontColor;
				tickMajorLength = 5;
				tickMinorLength = 1;
				rulerY = gridHeight+1;
				tMajorC = gridTickMajorColor;
				tMinorC = gridTickMinorColor;
				toggleLength = 10;
				break;
			case ON_RIGHT: 
				// Someday implement
			default:
				rf = fixedRulerFont;
			rfm = fixedRulerFontMetrics;
			rfc = fixedRulerFontColor;
			tickMajorLength = 5;
			tickMinorLength = 1;
			rulerY = 0;
			tMinorC = fixedRulerMinorTickColor;
			tMajorC = fixedRulerMajorTickColor;
			toggleLength = 10;
			break;
			}

			// Draw The Ticks And Labels
			int i = 0;
			for (; i <= gridWidth; i+=tickGap) {
				// if it is a major tick
				if (i % tickMajorPlacement == 0)  {
					// alternate the length every other one
					if (itShouldBePlacedHigh){
						tickLineLength = tickMajorLength + toggleLength;
						itShouldBePlacedHigh = false;
					}else{
						tickLineLength = tickMajorLength;
						itShouldBePlacedHigh = true;
					}

					// Set Tick Color And Determine Tick Text
					switch (position) {
					case ON_TOP: 
					case ON_BOTTOM: 
						text = String.format("%.6g", i * unitsPerPixel);
						if(i != gridWidth){
							g.setColor(tMajorC);
						}else{
							g.setColor(tickUpperBoundColor);
							rfc = upperBoundFontColor;
						}
						break;
					case ON_LEFT: 
					case ON_RIGHT: 
						text = String.format("%.6g", (gridWidth - i) * unitsPerPixel);
						if( i != 0){
							g.setColor(tMajorC);
						}else{
							g.setColor(tickUpperBoundColor);
							rfc = upperBoundFontColor;
						}
						break;
					default:
						break;
					}

					// If there is a decimal point there might be trailing zeros
					if (text.indexOf('.') > -1){
						// Remove any trailing zeros
						while (text.endsWith("0")){
							text = text.substring(0, text.length()-1);
						}
						// Remove if any trailing decimal point
						if (text.endsWith(".")){
							text = text.substring(0, text.length()-1);
						}
					}

				} else {
					g.setColor(tMinorC);
					text = null;
					tickLineLength = tickMinorLength;
				}

				if (tickLineLength != 0){

					// Determine Where To Draw The Tick And Place The Text
					switch (position) {
					case ON_TOP: 
						fromY = FIXED_RULER_HEIGHT-1;
						toY = FIXED_RULER_HEIGHT-tickLineLength-1;
						atY = toY + 1 - rfm.getHeight();
						fromX = i+xOffset+1;
						toX   = i+xOffset+1;
						atX = toX;
						sr = 0;
						if(i == gridWidth)
							atX = atX - rfm.stringWidth(text)/2;
						break;
					case ON_BOTTOM: 
						fromY = rulerY+1;
						toY = rulerY + tickLineLength;
						atY = toY + 1;
						fromX = i+xOffset;
						toX   = i+xOffset;
						atX = toX;
						sr = 0;
						if(i == gridWidth)
							atX = atX - rfm.stringWidth(text)/2;
						break;
					case ON_LEFT: 
						fromY = i;
						toY = i;
						atY = toY + 1;
						fromX = xOffset;
						toX   = xOffset - tickLineLength;
						atX = toX - 1;
						sr = 1.5;
						if(i == 0)
							atY = atY + rfm.stringWidth(text)/2;
						break;
					case ON_RIGHT: 
						// Someday implement
					default:
						break;
					}

					// Draw the tick
					g.drawLine(fromX, fromY, toX, toY);

					// Place the text
					if (text != null)
						centerText(text,
								rf,
								rfm,
								rfc,
								null,
								null,
								null,
								null,
								g,
								atX, atY,
								3,
								3,
								sr);
				}
			}
		}

		/**
		 * Center text to be displayed within a given rectangle
		 */
		protected void centerText(String s1, Font f1, FontMetrics m1, Color color1,
				String s2, Font f2, FontMetrics m2, Color color2,
				Graphics g,
				int x, int y, int regionWidth, int regionHeight, 
				double stringRotationPiRadians)
		{

			// init
			int stringWidth1=0, stringWidth2 = 0, x0=0, x1=0, y0=0, y1=0,
			fontAscent1=0, fontHeight1=0, fontHeight2=0;

			// Font Height
			fontHeight1 = m1.getHeight();

			// Width of first string
			stringWidth1 = m1.stringWidth(s1);

			// Font Height From Baseline
			fontAscent1 = m1.getAscent();

			if (s2 != null) {
				fontHeight2 = m2.getHeight();
				stringWidth2 = m2.stringWidth(s2);
			}

			// Determine position
			if ((stringRotationPiRadians == 0.5) || (stringRotationPiRadians == 1.5)){
				// Center the strings horizontally
				y0 = y - (regionHeight - stringWidth1)/2;
				y1 = y - (regionHeight - stringWidth2)/2;

				if (s2 == null) {
					// Center Vertically
					x0 = x + (regionWidth - fontHeight1)/2;
				} else {
					x0 = x + ((regionWidth - (int)(fontHeight1 + (fontHeight2 * 1.2)))/2);
					x1 = x0 + (int)(fontHeight2 * 1.2 );
				}
			}else{
				// Center the strings horizontally
				x0 = x + (regionWidth - stringWidth1)/2;
				x1 = x + (regionWidth - stringWidth2)/2;

				if (s2 == null) {
					// Center Vertically
					y0 = y + (regionHeight - fontHeight1)/2 + fontAscent1;
				} else {
					y0 = y + ((regionHeight - (int)(fontHeight1 + (fontHeight2 * 1.2)))/2) + 
					fontAscent1;
					y1 = y0 + (int)(fontHeight2 * 1.2 );
				}
			}
			if (g instanceof Graphics2D) {
				Map<RenderingHints.Key, Object> hints = new HashMap<RenderingHints.Key, Object>();
				hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				((Graphics2D)g).addRenderingHints(hints);
			}

			Font f3 = f1.deriveFont (AffineTransform.getRotateInstance 
					(stringRotationPiRadians * Math.PI));
			g.setFont (f3);
			g.setColor(color1);
			g.drawString (s1, x0, y0);

			if (s2 != null) {
				Font f4 = f2.deriveFont (AffineTransform.getRotateInstance 
						(stringRotationPiRadians * Math.PI));
				g.setFont (f4);
				g.setColor(color2);
				g.drawString(s2, x1, y1);
			}
		}

		/*
		 * Saves the barcode plot as an image
		 */
		protected void saveImage(File file, String format){

			// Write the image in the selected format, to the file chosen by the user.
			Graphics g=null;
			if (thereIsaBackgroundImage){
				try {
					bufferedImage = ImageIO.read(new File(backgroundImageName));
					g = bufferedImage.getGraphics();
					g.drawImage(bufferedImage, 0, 0, null);

				} catch (IOException e1) {
					bufferedImage =
						new BufferedImage(fullVisualizationWidth, fullVisualizationHeight,
								BufferedImage.TYPE_INT_BGR);
					g = bufferedImage.createGraphics();
				}
			}else {
				bufferedImage =
					new BufferedImage(fullVisualizationWidth, fullVisualizationHeight,
							BufferedImage.TYPE_INT_BGR);
				g = bufferedImage.createGraphics();
			}

			plotBeingSaved = true;
			paintComponent(g);
			plotBeingSaved = false;

			try {
				ImageIO.write( bufferedImage, format, file);
			} catch (IOException e1) {
			}
		}

	}

	/*
	 * Draws a ruler for the Plot the barcode plot
	 */
	protected class Ruler extends JComponent {
		protected static final long serialVersionUID = 1L;

		protected DrawingPane dp;
		protected Ruler(DrawingPane bc) {
			dp = bc;
			setPreferredSize(new Dimension(gridWidth, FIXED_RULER_HEIGHT));
		}

		protected void paintComponent(Graphics g) {
			Rectangle graphicRegion = g.getClipBounds();

			// Fill clipping area
			g.setColor(fixedRulerColor);
			g.fillRect(graphicRegion.x, graphicRegion.y,
					graphicRegion.width, graphicRegion.height);

			dp.drawRuler(g,DrawingPane.ON_TOP);
		}
	}
	/**
	 * Create the plot window and display results. For thread safety, this
	 * method should be invoked from the event-dispatching thread.
	 */
	protected static void plot(String legend, double[][] values, 
			double upperBound, boolean forceScatter) {
		//Create and set up the window.
		JFrame frame = new JFrame(legend);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		if (values == null)
			throw new IllegalArgumentException("No barcodes to plot");

		//Create and set up the content pane.
		BCPlot bcplot = new BCPlot(values, upperBound, forceScatter);
		bcplot.setMajorLegend(legend);
		frame.setJMenuBar(bcplot.createMenuBar());
		bcplot.setOpaque(true); //content panes must be opaque
		frame.setContentPane(bcplot);

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Make a barcode plot -- call via Plex.plot().
	 *
	 * <p>
	 *
	 * @param      label Window label.
	 * @param      intervals An array of double[2] of [x,y] intervals to plot.
	 * @param      upperBound Display the range from 0 to upperBound.
	 *
	 * @see        edu.stanford.math.plex.Plex#plot
	 */
	public static void doPlot(String label, double[][] intervals, double upperBound) {
		//Schedule a job for the event-dispatching thread to do the actual
		//plotting.
		final double[][] values = intervals;
		final double ubound = upperBound;
		final String legend = label;
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				plot(legend, values, ubound, false);
			}   
		});
	}

	/**
	 * Make a barcode scatterplot -- call via Plex.scatter().
	 *
	 * <p>
	 *
	 * @param      label Window label.
	 * @param      intervals An array of double[2] of x,y points to plot.
	 * @param      upperBound Display the square from 0 to upperBound.
	 *
	 * @see        edu.stanford.math.plex.Plex#scatter
	 */
	public static void doScatter(String label, double[][] intervals, double upperBound) {
		//Schedule a job for the event-dispatching thread to do the actual
		//plotting.
		final double[][] values = intervals;
		final double ubound = upperBound;
		final String legend = label;
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				plot(legend, values, ubound, true);
			}   
		});
	}
}
