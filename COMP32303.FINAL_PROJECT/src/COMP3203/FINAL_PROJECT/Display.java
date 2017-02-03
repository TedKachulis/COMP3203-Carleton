package COMP3203.FINAL_PROJECT;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Display extends JPanel{

	private static final long serialVersionUID = 1L;
	protected JSlider beaconSlider;
	protected JSlider radiusSlider;
	protected Label beaconLabel;
	protected Label radiusLabel;
	
	protected JButton btnSimple;
	protected JButton btnRigid;
	protected JButton btnCreate;
	protected JButton btnStart;
	protected JButton btnGraph;
	protected JButton btnCustomAlg;
	
	protected Label header;
	
	
	public final static int BEACON_MIN = 0;
	public final static int BEACON_MAX = Client.MAX_BEACONS;
	public final static int BEACON_INIT = 10;
	
	public final static int RADIUS_MIN = 0;
	public final static int RADIUS_MAX = Client.MAX_RADIUS;
	public final static int RADIUS_INIT = 5;
	
	public static int CURRENT_RADIUS = RADIUS_INIT;
	public static int CURRENT_BEACONS = BEACON_INIT;
	
	public Display(){
		initLayout();
		handleEvents();
		update();
	}
	
	
	protected void initLayout(){
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		setLayout(layout);

		
		beaconLabel = new Label("Beacons");
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(5, 10, 0, 10);
		layout.setConstraints(beaconLabel, constraints);
		add(beaconLabel);
		
		beaconSlider = new JSlider(JSlider.HORIZONTAL, BEACON_MIN, BEACON_MAX, BEACON_INIT);
		beaconSlider.setMajorTickSpacing(10);
		beaconSlider.setMinorTickSpacing(5);
		beaconSlider.setPaintTicks(true);
		beaconSlider.setPaintLabels(true);
		beaconSlider.setFocusable(false);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 6;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(0, 10, 5 ,10);
		layout.setConstraints(beaconSlider, constraints);
		add(beaconSlider);
		
		radiusLabel = new Label("Radius");
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(5, 10, 0, 10);
		layout.setConstraints(radiusLabel, constraints);
		add(radiusLabel);
		
		radiusSlider = new JSlider(JSlider.HORIZONTAL, RADIUS_MIN, RADIUS_MAX, RADIUS_INIT);
		radiusSlider.setMajorTickSpacing(10);
		radiusSlider.setMinorTickSpacing(1);
		radiusSlider.setPaintTicks(true);
		radiusSlider.setPaintLabels(true);
		radiusSlider.setFocusable(false);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 6;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(0, 10, 5 ,10);
		layout.setConstraints(radiusSlider, constraints);
		add(radiusSlider);
		
		btnCreate = new JButton("Create");
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(5, 10, 5, 10);
		layout.setConstraints(btnCreate, constraints);
		add(btnCreate);
		
		btnSimple = new JButton("Simple");
		constraints.gridx = 2;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(5, 10, 5, 10);
		layout.setConstraints(btnSimple, constraints);
		add(btnSimple);
		
		btnRigid = new JButton("Rigid");
		constraints.gridx = 3;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(5, 10, 5, 10);
		layout.setConstraints(btnRigid, constraints);
		add(btnRigid);
		
		btnCustomAlg = new JButton("Our Algorithm");
		constraints.gridx = 4;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(5, 10, 5, 10);
		layout.setConstraints(btnCustomAlg, constraints);
		add(btnCustomAlg);
		
		btnGraph = new JButton("Graph");
		constraints.gridx = 5;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(5, 10, 5, 10);
		layout.setConstraints(btnGraph, constraints);
		add(btnGraph);
		
		
	}
	
	protected void handleEvents(){
		beaconSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent event){
				update();
			}
		});
		
		radiusSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent event){
				update();
			}
		});
	}
	
	
	protected void update(){
		
		beaconLabel.setText("Beacons (n=" + beaconSlider.getValue() + "):");
		CURRENT_RADIUS = radiusSlider.getValue();
		CURRENT_BEACONS = beaconSlider.getValue();
	}
	
	public void updateRadiusLabel(int scale) {
		radiusLabel.setText("Radius (r=" + radiusSlider.getValue() + "/" + scale + "):");
	}
	
	public JSlider getBeaconSlider(){return beaconSlider;}
	public JSlider getRadiusSlider(){return radiusSlider;}
	public JButton getCreateButton(){return btnCreate;}
	public JButton getSimpleButton(){return btnSimple;}
	public JButton getRigidButton(){return btnRigid;}
	public JButton getCustomAlgButton(){return btnCustomAlg;}
	public JButton getGraphButton(){return btnGraph;}
	
}
