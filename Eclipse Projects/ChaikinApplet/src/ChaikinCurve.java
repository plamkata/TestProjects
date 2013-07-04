

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.TextEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Represents the application/applet of the Chaikin curve demo.
 * Contains all controls for detailisation of the algorithm and 
 * asigns action handlers for the components.
 * 
 * @author plamen
 *
 */
public class ChaikinCurve extends Applet implements
		java.awt.event.ActionListener, java.awt.event.ItemListener,
		java.awt.event.TextListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2342812792363811525L;

	private static final Color backgroung = Color.lightGray;

	private static final Color foreground = new Color((float) (0.000000), (float) (0.501961), (float) (0.501961));

	Button btnClear, btnInitPoly, btnApply, btnReCon, btnAnimate, btnMove;

	TextField txtAnimSpeed;
	
	Choice chRatio;
	
	Label lbAnim, lbRatio, lbBou, lbCurve, lbVisible;

	Checkbox chbPointsVisible, chbInitialVisible, chbInfoVisible;

	Checkbox chbStartBoundary, chbEndBoundary;

	Checkbox chbOpenClosed;

	Panel bfield, bfield2, bfield3;

	private DisplayManager displayManager = null;
	
	private Subdivision mCurve;

	public ChaikinCurve() {
		initComponents();
		setDefaults();
	}
	
	@Override
	public void init() {
		super.init();
//		initComponents();
//		setDefaults();
	}
	
	@Override
	public void start() {
		super.start();
		setEvolutionParameter();
		if (displayManager.getCurve() == null && mCurve != null) {
			displayManager.drawSubdivision(mCurve);
		}		
	}
	
	@Override
	public void stop() {
		super.stop();
		// keep the original curve in order to restore it
		mCurve = displayManager.getCurve();
		displayManager.clearScreen();
	}

	/**
	 * Creates all components.
	 */
	private void initComponents() {
		chbPointsVisible = makeCheckBox("Vertices Visible", true);
		chbInitialVisible = makeCheckBox("Initial Poly Visible", true);
		chbInfoVisible = makeCheckBox("Information", false);
		chbStartBoundary = makeCheckBox("Start", false);
		chbEndBoundary = makeCheckBox("End", false);
		chbOpenClosed = makeCheckBox("Open/Close Curve", false);

		btnClear = makeButton("Clear");
		btnInitPoly = makeButton("Init");
		btnApply = makeButton("Apply");
		btnReCon = makeButton("Reconstruct");
		btnAnimate = makeButton("Animate");
		btnMove = makeButton("Move");

		lbCurve = makeLabel("Curve Conditions: ");
		lbVisible = makeLabel("Visible: ");
		lbBou = makeLabel("   Boundary fixed for");

		txtAnimSpeed = makeTextF("200", 3);
		lbAnim = makeLabel("    Animation Speed(7-500): ");
		
		chRatio = makeChoiceRatio();
		lbRatio = makeLabel("Ratio:");
		
		bfield = new Panel();
		bfield.add(btnClear);
		bfield.add(btnApply);
		bfield.add(btnInitPoly);
		bfield.add(btnReCon);
		bfield.add(btnAnimate);
		bfield.add(btnMove);
		
		bfield2 = new Panel();
		bfield2.add(lbCurve);
		bfield2.add(chbOpenClosed);
		bfield2.add(lbBou);
		bfield2.add(chbStartBoundary);
		bfield2.add(chbEndBoundary);
		bfield2.add(lbRatio);
		bfield2.add(chRatio);

		bfield3 = new Panel();
		bfield3.add(lbVisible);
		bfield3.add(chbPointsVisible);
		bfield3.add(chbInitialVisible);
		bfield3.add(chbInfoVisible);
		bfield3.add(lbAnim);
		bfield3.add(txtAnimSpeed);

		Subdivision mCurve = new Subdivision(700, 400);
		displayManager = new DisplayManager(700, 400, mCurve);

		BorderLayout gridbag = new BorderLayout();
		this.setLayout(gridbag);

		Panel topField = new Panel();
		topField.setLayout(new GridLayout(2, 1));
		topField.add(bfield);
		topField.add(bfield3);
		
		this.add(topField, BorderLayout.NORTH);
		this.add(displayManager, BorderLayout.CENTER);
		this.add(bfield2, BorderLayout.SOUTH);

		this.setForeground(foreground);
		this.setBackground(backgroung);

		System.out.println("Computational Geometry: Subdivision Curve");
		System.out.println("Chaikin's Algorithm (Corner Cutting - Quadratic B-spline curve)");

		displayManager.setParent(this);
		registerListeners();	
	}

	/**
	 * Registers all listeners for the components.
	 */
	private void registerListeners() {
		chbPointsVisible.addItemListener(this);
		chbInitialVisible.addItemListener(this);
		chbInfoVisible.addItemListener(this);
		chbStartBoundary.addItemListener(this);
		chbEndBoundary.addItemListener(this);
		chbOpenClosed.addItemListener(this);
		
		btnClear.addActionListener(this);
		btnInitPoly.addActionListener(this);
		btnApply.addActionListener(this);
		btnReCon.addActionListener(this);
		btnAnimate.addActionListener(this);
		btnMove.addActionListener(this);
		
		txtAnimSpeed.addTextListener(this);
		chRatio.addItemListener(this);
	}

	private Label makeLabel(String str) {
		Label tmp = new Label(str);
		tmp.setFont(new Font("TimesRoman", Font.PLAIN, 10));
		tmp.setForeground(foreground);
		tmp.setBackground(backgroung);
		return tmp;
	}

	private Checkbox makeCheckBox(String str, boolean bt) {
		Checkbox tmp = new Checkbox(str, bt);
		tmp.setFont(new Font("TimesRoman", Font.ITALIC, 10));
//		tmp.addItemListener(this);
		tmp.setForeground(foreground);
		tmp.setBackground(backgroung);
		return tmp;
	}

	private Button makeButton(String str) {
		Button tmp = new Button(str);
		tmp.setFont(new Font("TimesRoman", Font.PLAIN, 18));
//		tmp.addActionListener(this);
		tmp.setForeground(foreground);
		tmp.setBackground(backgroung);
		return tmp;
	}

	private TextField makeTextF(String str, int dn) {
		TextField txt = new TextField(str, dn);
		// tmp.setFont(new Font("TimesRoman", Font.PLAIN, 18));
		txt.setForeground(foreground);
		txt.setBackground(backgroung);
//		txt.addTextListener(this);
		return txt;
	}
	
	/**
	 * @return
	 */
	private Choice makeChoiceRatio() {
		Choice chRatio = new Choice();
		chRatio.setForeground(foreground);
		chRatio.setBackground(backgroung);
//		chRatio.addItemListener(this);
		
		// add some data
		chRatio.addItem("0.05");
		chRatio.addItem("0.1");
		chRatio.addItem("0.15");
		chRatio.addItem("0.2");
		chRatio.addItem("0.25");
		chRatio.addItem("0.3");
		chRatio.addItem("0.35");
		chRatio.addItem("0.4");
		chRatio.addItem("0.45");
		chRatio.addItem("0.5");
		chRatio.addItem("0.55");
		chRatio.addItem("0.6");
		
		chRatio.select("0.25");
		return chRatio;
	}

	private int getIntTF(TextField dtf) {
		double dummyd = 1.0;
		try {
			dummyd = (Double.valueOf(dtf.getText().trim())).doubleValue();
		} catch (java.lang.NumberFormatException e) {
			Toolkit tkForBeep = getToolkit();
			tkForBeep.beep();
			System.out.println("Erorr ! This is not a number.");
			System.out.println("Please input again.");
		}
		return ((int) (dummyd));
	}
	
	private double getDoubleCH(Choice choice) {
		double ratio = 0.4;
		try {
			ratio = (Double.valueOf(choice.getSelectedItem())).doubleValue();
		} catch (java.lang.NumberFormatException e) {
			Toolkit tkForBeep = getToolkit();
			tkForBeep.beep();
			System.out.println("Erorr ! This is not a number.");
			System.out.println("Please input again.");
		}
		return ratio;
	}

	public void textValueChanged(TextEvent e) {
		System.out.println("Text event: " + e);
		
		setEvolutionParameter();
	}

	public void itemStateChanged(ItemEvent evt) {
		System.out.println("Item event: " + evt);
		
		boolean isOpened = chbOpenClosed.getState();
		Subdivision mCurve = displayManager.getCurve();
		
		boolean oldState = mCurve.startboundary;
		mCurve.startboundary = this.chbStartBoundary.getState();
		if (mCurve.startboundary != oldState) {
			if (!isOpened) {
				displayManager.reconstructCurve();
			}			
		}
		
		oldState = mCurve.endboundary;
		mCurve.endboundary = this.chbEndBoundary.getState();
		if (mCurve.endboundary != oldState) {
			if (!isOpened) {
				displayManager.reconstructCurve();
			}			
		}
		
		double oldRatio = mCurve.ratio;
		mCurve.ratio = getDoubleCH(chRatio);
		if (mCurve.ratio != oldRatio) {
			displayManager.reconstructCurve();
		}
		
		displayManager.setVisiblility(chbPointsVisible.getState(), 
				chbInitialVisible.getState(), chbInfoVisible.getState(), isOpened);
	}

	public void setEvolutionParameter() {
		int iteraion = 1;
		if (displayManager != null) {
			displayManager.setEvolutionParameter(iteraion);
		}
		
		int animSpeed = getIntTF(txtAnimSpeed);
		if (animSpeed <= 6 || animSpeed >= 501) {
			Toolkit tkForBeep = getToolkit();
			tkForBeep.beep();
			System.out.println("Erorr ! Parameter is not in correct range.");
			System.out.println("Please input again.");
		} else {
			if (displayManager != null) {
				displayManager.setAnimationSpeed(animSpeed);
			}
		}
	}

	private void setDefaults() {
		btnAnimate.setForeground(foreground);
		btnAnimate.setLabel("Animate");
		btnMove.setForeground(foreground);
		btnMove.setLabel("Move");
		chRatio.select("0.25");
		chbPointsVisible.setState(false);
		chbInitialVisible.setState(true);
		chbInfoVisible.setState(false);
		chbOpenClosed.setState(true);
		chbStartBoundary.setState(false);
		chbEndBoundary.setState(false);

		txtAnimSpeed.setText("200");
		this.setEvolutionParameter();
	}

	public void actionPerformed(ActionEvent evt) {
		System.out.println("Btn event: " + evt);
		
		if (btnClear.getLabel().equals(evt.getActionCommand())) {
			this.setDefaults();
			System.out.println("in clear");
			Subdivision mCurve = new Subdivision(700, 400);
			displayManager.clear();
			displayManager.drawSubdivision(mCurve);

		} else if (btnAnimate.getLabel().equals(evt.getActionCommand())) {
			if (displayManager.isOpened) {
				displayManager.animationButton = btnAnimate;
				if (!displayManager.startsAnimation) {
					this.setEvolutionParameter();
					displayManager.startAnimationThread();
					btnAnimate.setLabel("Suspend");
					btnAnimate.setForeground(Color.red);

				} else {
					displayManager.startAnimationThread();
					btnAnimate.setLabel("Animate");
					btnAnimate.setForeground(foreground);
				}
			}
		} else if (btnMove.getLabel().equals(evt.getActionCommand())) {
			if (displayManager.getCurve().getNumberOfPoints() >= 3) {
				displayManager.animationButton = btnMove;
				if (!displayManager.startsAnimation) {
					this.setEvolutionParameter();
					displayManager.startAnimationThread();
					btnMove.setLabel("Suspend");
					btnMove.setForeground(Color.red);

				} else {
					displayManager.startAnimationThread();
					btnMove.setLabel("Move");
					btnMove.setForeground(foreground);
				}
			}
		} else if (btnApply.getLabel().equals(evt.getActionCommand())) {
			this.setEvolutionParameter();
			displayManager.apply();
		} else if (btnReCon.getLabel().equals(evt.getActionCommand())) {
			this.setEvolutionParameter();
			displayManager.reconstructCurve();
		} else if (btnInitPoly.getLabel().equals(evt.getActionCommand())) {
			displayManager.initialPolygon();
		}

	}

	public static void main(String[] args) {
		ChaikinCurve app = new ChaikinCurve();
		final Frame frame = new Frame();
		frame.add(app);
		frame.setSize(700, 500);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.setVisible(false);
				frame.dispose();
			}
		});
	}
}
