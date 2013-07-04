

import java.awt.BasicStroke;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Represents the visual drawing area of the application.
 * Also acts as a handler for a couple of operations.
 * 
 * @author plamen
 *
 */
public class DisplayManager extends Panel implements Runnable,
		java.awt.event.MouseMotionListener, java.awt.event.MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6980861201088676792L;

//	private static final Color tomato = new Color((float) (1.000000), (float) (0.388235), (float) (0.278431));

//	private static final Color deeppink = new Color((float) (1.000000), (float) (0.078431), (float) (0.576471));

	private static final Color darkorange = new Color((float) (1.000000), (float) (0.549020), (float) (0.00000));

//	private static final Color cornflowerblue = new Color((float) (0.392157), (float) (0.584314), (float) (0.929412));

	private static final Color teal = new Color((float) (0.000000), (float) (0.501961),	(float) (0.501961));

//	private static final Color blanchedalmond = new Color((float) (1.000000), (float) (0.921569), (float) (0.803922));

//	private static final Color forestgreen = new Color((float) (0.133333), (float) (0.545098),	(float) (0.133333));

//	private static final Color mediumorchid = new Color((float) (0.729412), (float) (0.333333),	(float) (0.827451));

//	private static final Color darkcyan = new Color((float) (0.000000), (float) (0.545098),	(float) (0.545098));

//	private static final Color firebrick = new Color((float) (0.698039), (float) (0.133333), (float) (0.133333));

//	private static final Color palevioletred = new Color((float) (0.858824), (float) (0.439216), (float) (0.576471));
	
	public static final Color darkgray = new Color((float) (0.576471), (float) (0.576471),	(float) (0.576471));

	private Image double_buffer = null;

	private static final int MAX_POINT_NUMBER = 10000;

	private Graphics dg = null;

	private Thread thread;

	private Dimension aread;

	private Knot Center;

	private boolean arePointsVisible = false;

	private boolean isInitialVisible = true;

	private boolean isInfoVisible = false;

	boolean isOpened = true;

	private double scale;
	
	private boolean dragging = false;
	
	private Knot draggedKnot = null;

	private Dimension dd;

	boolean startsAnimation = false;
	
	Button animationButton = null;

	private int interval = 200;

	private int iterationNumber = 5;

	private Subdivision mCurve;

	private boolean informations[] = new boolean[15];

	private ChaikinCurve parent;

	/**
	 * Construct a new visual area with the given size 
	 * and specify the curve o draw.
	 * 
	 * @param width
	 * @param height
	 * @param curve
	 */
	public DisplayManager(int width, int height, Subdivision curve) {
		dd = new Dimension();
		dd.width = width;
		dd.height = height;
		mCurve = curve;
		Center = new Knot(0, 0);
		scale = 1.0;
		this.setForeground(Color.black);
		this.setBackground(Color.white);
		thread = new Thread(this);
		this.setSize(width, height);
		aread = this.getSize();
		aread.width = aread.width + 10;
		aread.height = aread.height + 10;
		int i;
		for (i = 0; i < informations.length; i++)
			informations[i] = false;

	}

	/**
	 * Set the parent and add the necesary handlers for all parent components.
	 * 
	 * @param parent
	 */
	public void setParent(ChaikinCurve parent) {
		this.parent = parent;
		parent.btnClear.addMouseListener(this);
		parent.btnInitPoly.addMouseListener(this);
		parent.btnApply.addMouseListener(this);
		parent.btnAnimate.addMouseListener(this);
		parent.btnMove.addMouseListener(this);
		parent.btnReCon.addMouseListener(this);
		parent.chRatio.addMouseListener(this);
		parent.txtAnimSpeed.addMouseListener(this);
		parent.chbPointsVisible.addMouseListener(this);
		parent.chbInitialVisible.addMouseListener(this);
		parent.chbInfoVisible.addMouseListener(this);
		parent.chbStartBoundary.addMouseListener(this);
		parent.chbEndBoundary.addMouseListener(this);
		parent.chbOpenClosed.addMouseListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	public void setAnimationSpeed(int speed) {
		interval = speed;
	}

	public void setEvolutionParameter(int numberOfSteps) {
		iterationNumber = numberOfSteps;
	}

	/**
	 * Change tghe current subdivision and do the necessary repaint.
	 * 
	 * @param subdivision the new subdivision
	 */
	public void drawSubdivision(Subdivision subdivision) {
		mCurve = subdivision;
		this.repaint();
	}

	/**
	 * Do a full reconstruction of the subdivision curve, 
	 * making necesary repaint.
	 */
	public void reconstructCurve() {
		int i;
		if (mCurve.getNumberOfPoints() >= 3 && !startsAnimation) {
			int num = mCurve.numberOfSubdivisions;
			mCurve.initPoly();
			for (i = 0; i < num; i++) {
				mCurve.apply(isOpened);
				if (mCurve.getNumberOfPoints() >= MAX_POINT_NUMBER) {
					// EnviError();
					break;
				}
			}
		} else {
			// EnviError();
		}
		this.repaint();
	}

	/**
	 * Initialise visibilities.
	 * 
	 * @param initialVisible
	 * @param pointsVisible
	 * @param infoVisible
	 * @param openClosed
	 */
	public void setVisiblility(
			boolean initialVisible, boolean pointsVisible, 
			boolean infoVisible, boolean openClosed) {
		
		isInitialVisible = pointsVisible;
		arePointsVisible = initialVisible;
		isInfoVisible = infoVisible;
		
		boolean oldValue = isOpened;
		isOpened = openClosed;
		if (isOpened != oldValue) {
			reconstructCurve();
		}
		
		this.repaint();
	}

	public void doLayout() {
		Image img = double_buffer;
		double_buffer = createImage(dd.width, dd.height);
		if (double_buffer != null) {
			dg = double_buffer.getGraphics();
		}
		if (img != null) {
			dg.drawImage(img, 0, 0, null);
		}
	}

	/**
	 * Initialise the subdivision curve to the initial polygon.
	 */
	public void initialPolygon() {
		mCurve.numberOfSubdivisions = 0;
		mCurve.initPoly();
		this.repaint();

	}

	/**
	 * Apply one step of Chaikin's algorithm, doing the necesary repaint.
	 */
	public void apply() {
		int i;
		if (mCurve.getNumberOfPoints() < MAX_POINT_NUMBER && !startsAnimation) {
			for (i = 0; i < iterationNumber; i++) {
				System.out.print(".");
				mCurve.apply(isOpened);
				if (mCurve.getNumberOfPoints() >= MAX_POINT_NUMBER) {
					break;
				}
			}
		} else {
			// EnviError();
		}
		this.repaint();
	}
	
	/**
	 * Clear all points from the screen setting up initial state of the application.
	 */
	public void clear() {
		clearScreen();
		startsAnimation = false;
		scale = 1.0;
		interval = 200;
		aread = this.getSize();
		isInitialVisible = true;
		arePointsVisible = false;
		isInfoVisible = false;
		isOpened = true;
		thread.stop();
		thread = new Thread(this);
		System.gc();
	}

	/**
	 * Cleares the screen releasing any memory used for the drawing.
	 */
	public void clearScreen() {
		if (dg != null) {
			dg.dispose();
			mCurve = null;
			System.gc();
			
			doLayout();
		}
	}

	/**
	 * Starts the animation thread.
	 */
	public void startAnimationThread() {
		if (!startsAnimation) {
			startsAnimation = true;
			thread = new Thread(this);
			thread.start();
			this.repaint();
		} else {
			startsAnimation = false;
			thread.stop();
			this.repaint();
		}

	}

	/**
	 * Draws the visual area and the current state of the subdivision's curves.
	 * 
	 * @param g the graphics to use
	 */
	public void draw(Graphics g) {
		g.setColor(darkgray);
		g.fillRect(0, 0, dd.width, dd.height);
		g.setColor(Color.black);

		Graphics2D dg2 = (Graphics2D) g;
		dg2.setStroke(new BasicStroke(1.1f)); // 0.9-pixel lines
	    dg2.setFont(new Font("Serif", Font.PLAIN, 12)); // 18-point font
	    dg2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.setColor(Color.white);
		g.drawString("number of points: " + mCurve.getNumberOfPoints(), 5, 10);
		g.drawString("number of subdivision: " + mCurve.numberOfSubdivisions, 5, 20);
		g.setColor(Color.black);

		Knot now;
		if (mCurve.getNumberOfPoints() >= 2) {
			if (isInitialVisible) {
				// draw initial polygon
				now = mCurve.IHead;
				g.setColor(Color.black);
				while (now.next.next != mCurve.ITail) {
					now = now.next;
					drawLine(now, now.next);
					drawInitialPoint(now);
				}
				if (now.next != null) {
					drawInitialPoint(now.next);
				}
				
				if (isOpened) {
					drawLine(mCurve.ITail.back, mCurve.IHead.next);
				}
				g.setColor(Color.black);
			}

			if (mCurve.numberOfSubdivisions != 0) {
				g.setColor(Color.white);
				now = mCurve.Head;
				while (now.next.next != mCurve.Tail) {
					now = now.next;
					drawLine(now, now.next);
				}
				if (isOpened) {
					drawLine(mCurve.Tail.back, mCurve.Head.next);
				}
				g.setColor(Color.black);
			}			
		} else if (mCurve.getNumberOfPoints() >= 1 && isInitialVisible) {
			drawInitialPoint(mCurve.IHead.next);
		}

		if (mCurve.getNumberOfPoints() >= 1 && arePointsVisible) {
			
			now = mCurve.Head;
			while (now.next != mCurve.Tail) {
				now = now.next;
				drawPoint(now);
			}
		}
		
		drawMessages();
	}

	/**
	 * Draws all required messages.
	 */
	private void drawMessages() {
		if (mCurve.getNumberOfPoints() >= MAX_POINT_NUMBER) {
			dg.setColor(Color.red);
			Font dummy = dg.getFont();
			dg.setFont(new Font("TimesRoman", Font.BOLD, 20));
			dg.drawString("Procedure is stopped.", 200, 20);
			dg.drawString("Reached maximum number of points.", 200, 40);
			dg.setFont(dummy);
			dg.setColor(Color.black);
		}
		
		if (mCurve.getNumberOfPoints() >= MAX_POINT_NUMBER && isInfoVisible) {
			dg.setColor(Color.white);
			dg.drawString("Caution! Too many points. Subdivision is stopped.", 5, 340);
			dg.drawString("Please click Clear or Initialize Subdivision button.", 5, 360);
			dg.setColor(Color.black);

		}
		
		if (isInfoVisible) {
			drawInformations();
		}
	}

	/**
	 * Draws a knot of the subdivision's current curve.
	 * 
	 * @param k the knot to be visualised
	 */
	private void drawPoint(Knot k) {
		DoubleVector v = parametrise(k);
		dg.fillArc((int) (v.x - 2), (int) (v.y - 2), 4, 4, 0, 360);
	}

	/**
	 * Draws an initial knot of the curve.
	 * 
	 * @param k the knot to be visualised
	 */
	private void drawInitialPoint(Knot k) {
		DoubleVector v = parametrise(k);
		
		Color previouse = dg.getColor();
		int thicknes = 6;
		if (dragging && k == draggedKnot) {
			dg.setColor(Color.red);
			thicknes = 10;
			dg.fillRect((int)(v.x - 4), (int)(v.y - 4), thicknes, thicknes);
		} else {
			dg.setColor(Color.orange);
			dg.fillRect((int)(v.x - 2), (int)(v.y - 2), thicknes, thicknes);
		}
		dg.setColor(previouse);
	}

	/**
	 * Draws a line from knot a to knot b.
	 * 
	 * @param a
	 * @param b
	 */
	private void drawLine(Knot a, Knot b) {
		DoubleVector u = parametrise(a);
		DoubleVector v = parametrise(b);
		dg.drawLine((int) (u.x), (int) (u.y), (int) (v.x), (int) (v.y));
	}

	/**
	 * Draws information strings on the visual area as it is repainted.
	 */
	public void drawInformations() {
		dg.setColor(Color.white);
		Font dummy = dg.getFont();
		dg.setFont(new Font("TimesRoman", Font.PLAIN, 15));
		if (informations[0]) {
			dg.drawString(
					"Clear: Clear all memory and initialize all conditions.",
					5, 40);
		} else if (informations[1]) {
			dg.drawString(
					"Init: Reset the subdivision curve to initial input ",
					5, 40);
			dg.drawString(
					"polygonal curve. The conditions are preserved.", 
					5, 60);
		} else if (informations[2]) {
			dg.drawString(
					"Apply: Execute Chaikin's algorithm one time for the polygonal ",
					5, 40);
			dg.drawString(
					"curve. The curve will converge to a Quadratic B-spline curve.",
					5, 60);
		} else if (informations[3]) {
			dg.drawString("Animate: Show the animation of subdivisions.", 5, 40);
			dg.drawString("Suspend: Stop the animation thread.", 5, 60);
			dg.drawString(
					"The curve will converge to a Quadratic B-spline curve.",
					5, 80);
		} else if (informations[4]) {
			dg.drawString(
					"Reconstruct: recalculate the subdivision curve, where ",
					5, 40);
			dg.drawString(
					"the conditions and control points remain the same.",
					5, 60);
		} else if (informations[5]) {
			dg.drawString(
					"Iteration: This value is a repetition number of Chaikin's algorithm.",
					5, 40);
			dg.drawString(
					"Please input a natural number. Beep sound indicates an error.",
					5, 60);

		} else if (informations[6]) {
			dg.drawString(
					"Animation Speed: This value represents a sleep time in ", 
					5, 40);
			dg.drawString(
					"milli-seconds (7-500) at one subdivision step. Please ", 
					5, 60);
			dg.drawString(
					"input a nature number. Beep sound indicates an error range.",
					5, 80);

		} else if (informations[7]) {
			dg.drawString(
					"Vertices Visible: Visualise/Hide the points that form the curve.", 
					5, 40);
		} else if (informations[8]) {
			dg.drawString(
					"Initial Poly Visible: Visualise/Hide the control points ", 
					5, 40);
			dg.drawString("of the initial polygon curve.", 5, 60);

		} else if (informations[9]) {
			dg.drawString(
					"Information: Visualise/Hide help and error messages on the display.",
					5, 40);
		} else if (informations[10]) {
			dg.drawString(
					"Start: Set a start point boundary to fixed/variable.", 
					5, 40);
			dg.drawString(
					"There is no effect, if the objective curve is a closed curve.",
					5, 60);

		} else if (informations[11]) {
			dg.drawString("End: Set an end point boundary to fixed/variable.",
					5, 40);
			dg.drawString(
					"There is no effect, if the objective curve is a closed curve.",
					5, 60);
		} else if (informations[12]) {
			dg.drawString(
					"Open/Close Curve: Change the initial polygonal curve to open/closed curve.",
					5, 40);
		} else if (informations[13]) {
			dg.drawString(
					"Change the ratio in which Chaikin's algorithm intersects each segment ",
					5, 40);
			dg.drawString(
					"from the current polygonal curve. You'll see that 0.25 is most appropriate.",
					5, 60);
		} else if (informations[14]) {
			dg.drawString(
					"Animate moving all intial vertices in random directions and with different speed.",
					5, 40);
			dg.drawString(
					"This animation is only supported for closed curves.",
					5, 60);
		}
		dg.setFont(dummy);
		dg.setColor(Color.black);
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e1) {
				System.out.println("run error!");
				System.exit(0);
			}
			if (mCurve.getNumberOfPoints() < MAX_POINT_NUMBER) {
				System.out.print(".");
				try {
					if (animationButton == parent.btnAnimate) {
						mCurve.apply(isOpened);
					} else if (animationButton == parent.btnMove) {
						randomMoveCurve(mCurve);
					}
					this.repaint();
				} catch (RuntimeException e) {
					resetAnimationButton();
					enviError();
					this.startAnimationThread();
				} catch (OutOfMemoryError e) {
					resetAnimationButton();
					enviError();
					System.gc();
					this.startAnimationThread();
				}
			} else {
				resetAnimationButton();				
				// EnviError();
				this.startAnimationThread();
			}
		}
	}

	/**
	 * Resets the animation button.
	 */
	private void resetAnimationButton() {
		if (animationButton == parent.btnAnimate) {
			parent.btnAnimate.setLabel("Animate");
			parent.btnAnimate.setForeground(teal);
		} else if (animationButton == parent.btnMove) {
			parent.btnMove.setLabel("Move");
			parent.btnMove.setForeground(teal);
		}
	}

	private void enviError() {
		System.out.println("The number of points is " + mCurve.getNumberOfPoints() + ".");
		System.out.println("Suspended animation thread.");
		System.out.println("If you want to generate more points,");
		System.out.println("Please modify DisplayManager class's MAXPOINTNUMBER.");
		System.out.println("And assign extra memory for java program.");
	}
	
	/**
	 * Generate different random translations for each of 
	 * the subdivision's initial knots.
	 * 
	 * @param curve the curve to streatch in and out
	 */
	private void randomMoveCurve(Subdivision curve) {
		Knot current = curve.IHead;
		while (current.next != curve.ITail) {
			current = current.next;
			
			for (int count = 0; count < 10; count++) {
				DoubleVector translation = new DoubleVector(
						(Math.random() - 0.5) * 0.7,
						(Math.random() - 0.5) * 0.4);
				
				if (isInside(current, translation)) {
					mCurve.move(current, translation);
					break;
				}
			}			
		}
	}

	/**
	 * Wait for the animation thread to stop.
	 */
	public void destroy() {
		System.out.println(" ");
		System.out.println("destroy thread");
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void paint(Graphics g) {
		try {
			draw(dg);
			g.drawImage(double_buffer, 0, 0, this);
		} catch (java.lang.NullPointerException e) {
		}
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		System.out.println("Mouse pressed event: " + e);
		
		if (e.getComponent() == this) {
			Knot mouseKnot = deparametrise(e.getX(), e.getY());
			System.out.println("After deparametrization: " + mouseKnot + ".");
			
			if (isInitialPointSelected(e)) {
				Knot knot = mCurve.findOriginalKnotAt(mouseKnot.x, mouseKnot.y);
				if (knot != null) {
					System.out.println("Found original knot.");
					draggedKnot = knot;
					dragging = true;
				}
			} else {
				// add new knot to original curve
				if (mCurve.getNumberOfPoints() <= MAX_POINT_NUMBER) {
					if (!startsAnimation) {
						double nx = mouseKnot.x;
						double ny = mouseKnot.y;
						new Knot(nx, ny).append(mCurve.Tail);
						mCurve.setNumberOfPoints(mCurve.getNumberOfPoints() + 1);
						new Knot(nx, ny).append(mCurve.ITail);
						mCurve.setNumberOfPointsBuffer(mCurve.getNumberOfPointsBuffer() + 1);
						reconstructCurve();
					}
				}
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
		if (isInfoVisible) {
			if (e.getComponent() != this) {
				for (int i = 0; i < informations.length; i++) {
					informations[i] = false;
				}

				if (e.getComponent() == parent.btnClear) {
					informations[0] = true;
					parent.btnClear.setForeground(darkorange);
				} else if (e.getComponent() == parent.btnInitPoly) {
					informations[1] = true;
					parent.btnInitPoly.setForeground(darkorange);
				} else if (e.getComponent() == parent.btnApply) {
					informations[2] = true;
					parent.btnApply.setForeground(darkorange);
				} else if (e.getComponent() == parent.btnAnimate) {
					informations[3] = true;
					parent.btnAnimate.setForeground(darkorange);
				} else if (e.getComponent() == parent.btnReCon) {
					informations[4] = true;
					parent.btnReCon.setForeground(darkorange);
				} else if (e.getComponent() == parent.txtAnimSpeed) {
					informations[6] = true;
					parent.txtAnimSpeed.setForeground(darkorange);
				} else if (e.getComponent() == parent.chbPointsVisible) {
					informations[7] = true;
					parent.chbPointsVisible.setForeground(darkorange);
				} else if (e.getComponent() == parent.chbInitialVisible) {
					informations[8] = true;
					parent.chbInitialVisible.setForeground(darkorange);
				} else if (e.getComponent() == parent.chbInfoVisible) {
					informations[9] = true;
					parent.chbInfoVisible.setForeground(darkorange);
				} else if (e.getComponent() == parent.chbStartBoundary) {
					informations[10] = true;
					parent.chbStartBoundary.setForeground(darkorange);
				} else if (e.getComponent() == parent.chbEndBoundary) {
					informations[11] = true;
					parent.chbEndBoundary.setForeground(darkorange);
				} else if (e.getComponent() == parent.chbOpenClosed) {
					informations[12] = true;
					parent.chbOpenClosed.setForeground(darkorange);
				} else if (e.getComponent() == parent.chRatio) {
					informations[13] = true;
					parent.chRatio.setForeground(darkorange);
					parent.chRatio.repaint();
				} else if (e.getComponent() == parent.btnMove) {
					informations[14] = true;
					parent.btnMove.setForeground(darkorange);
				}
				this.repaint(5, 20, 600, 65);
			}
		}		
	}

	public void mouseExited(MouseEvent e) {
		if (e.getComponent() != this) {
			if (isInfoVisible) {
				for (int i = 0; i < informations.length; i++) {
					informations[i] = false;
				}
				parent.btnClear.setForeground(teal);
				parent.btnInitPoly.setForeground(teal);
				parent.btnApply.setForeground(teal);
				parent.btnAnimate.setForeground(teal);
				parent.btnMove.setForeground(teal);
				parent.btnReCon.setForeground(teal);
				parent.chRatio.setForeground(teal);
				parent.txtAnimSpeed.setForeground(teal);
				parent.chbPointsVisible.setForeground(teal);
				parent.chbInitialVisible.setForeground(teal);
				parent.chbInfoVisible.setForeground(teal);
				parent.chbStartBoundary.setForeground(teal);
				parent.chbEndBoundary.setForeground(teal);
				parent.chbOpenClosed.setForeground(teal);
				repaint();
			}			
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (e.getComponent() == this) {
			if (dragging) {
				Knot mouseKnot = deparametrise(e.getX(), e.getY());
				Knot knot = draggedKnot;
				if (knot != null) {
					DoubleVector translation = new DoubleVector(
							mouseKnot.x - knot.x, mouseKnot.y - knot.y);
					if (isOpened) {
						mCurve.move(knot, translation);
					} else {
						knot.move(translation);
					}
					repaint();
				}
			}
		}		
	}
	
	public void mouseReleased(MouseEvent e) {
		if (e.getComponent() == this) {
			if (dragging) {
				dragging = false;
				draggedKnot = null;
				if (isOpened) {
					repaint();
				} else {
					reconstructCurve();
				}				
			} else {
				repaint();
			}
		}
	}

	public void mouseMoved(MouseEvent e) {
	}
	


	/**
	 * Find out if an initial point is being targeted by the mouse event.
	 * 
	 * @param e mouse event
	 * @return true if an initial point is targeted
	 */
	private boolean isInitialPointSelected(MouseEvent e) {
		int targetRGB = Color.orange.getRGB();
		
		int rgb = 0, rgb1 = 0, rgb2 = 0, rgb3 = 0, rgb4 = 0;
		try {
			rgb = ((BufferedImage)double_buffer).getRGB(e.getX(), e.getY());
			rgb1 = ((BufferedImage)double_buffer).getRGB(e.getX() + 1, e.getY() + 1);
			rgb2 = ((BufferedImage)double_buffer).getRGB(e.getX() + 1, e.getY() - 1);
			rgb3 = ((BufferedImage)double_buffer).getRGB(e.getX() - 1, e.getY() + 1);
			rgb4 = ((BufferedImage)double_buffer).getRGB(e.getX() - 1, e.getY() - 1);
		} catch (RuntimeException ee) {
			// ignore
		}
		
		return rgb == targetRGB || 
			rgb1 == targetRGB || rgb2 == targetRGB || 
			rgb3 == targetRGB || rgb4 == targetRGB;
	}

	/**
	 * Parametrise curve coordinates of a knot into screen coordinates.
	 * 
	 * @param k knot representation of a curve point
	 * @return screen representation of the point
	 */
	public DoubleVector parametrise(Knot k) {
		double ddx = (Center.x + (double) ((mCurve.scale) * (k.x - 4.0)) + 400.0);
		double ddy = (Center.y + (double) (aread.height - ((double) ((mCurve.scale) * (k.y - 2.0)) + 200.0)));
		return new DoubleVector(ddx, ddy);
	}

	/**
	 * Deparametrise from screen coordinates to curve coordinates.
	 * 
	 * @param dx
	 * @param dy
	 * @return curve knot representation of the screen point
	 */
	public Knot deparametrise(double dx, double dy) {
		double ddx, ddy;
		ddx = ((dx - (Center.x + 400.0)) / mCurve.scale + 4.0);
		ddy = ((-dy + (Center.y + ((double) (aread.height))) - 200.0)
				/ mCurve.scale + 2.0);
		return new Knot(ddx, ddy);
	}
	
	/**
	 * Test if the given knot is inside the visual area.
	 * 
	 * @param k the knot to test
	 * @return true if the knot is inside the visual area
	 */
	public boolean isInside(Knot k) {
		return (k.x > 0.05 && k.x < 6.8) && (k.y > 0.4 && k.y < 3.9);
	}
	
	/**
	 * Test if the given knot will be inside the visual area 
	 * after translation with the vector.
	 * 
	 * @param k the knot to test
	 * @return true if the knot is inside the visual area
	 */
	public boolean isInside(Knot k, DoubleVector v) {
		Knot newKnot = new Knot(k);
		newKnot.move(v);
		return isInside(newKnot);
	}

	private int xrv(int dx) {
		double dv = ((double) (dx) - 400.0) - Center.x + 400.0;
		return ((int) (dv));
	}

	private int yrv(int dy) {
		double dv = ((double) (dy) - 200.0) - Center.y + 200.0;
		return ((int) (dv));
	}

	public Subdivision getCurve() {
		return mCurve;
	}

}
