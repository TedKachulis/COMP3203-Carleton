package COMP3203.FINAL_PROJECT;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Client extends JFrame{
	private static final long serialVersionUID = 1L;
	public static int DEFAULT_WIDTH = 800, DEFAULT_HEIGHT = 600;	//Size of window
	public static int MAX_BEACONS = 200;
	public static int MAX_RADIUS = 20;		
	public static int LINE_SCALE = 7;		//Size of the beacons and line in the window
	public static int SAMPLE_SIZE = 20;		//Sample size to determine average # moves
	public static Logger log = Logger.getLogger("COMP3203Logger");
	
	private View view;
	protected static String algChoice = "Simple";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		FileHandler handler = null;
		try {
			handler = new FileHandler("LogFile.log", false);
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		log.addHandler(handler);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
		}
		catch(Exception e){
			
		}
		
		new Client("COMP 3203 FINAL PROJECT").setVisible(true);

	}

	/**
	 * Create the application.
	 */
	public Client(String winName) {
		super(winName);
		add(view = new View());
		handleEvents();
		
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setButtons(false);
		//view.getDisplay().getCreateButton().doClick();
	}
	
	protected void handleEvents() {		
		view.getDisplay().getCreateButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				handleCreate();
				setButtons(true);
			}
		});

		view.getDisplay().getSimpleButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				algChoice = "Simple";
				handleStart();
				setButtons(false);
			}
		});
		
		view.getDisplay().getRigidButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				algChoice = "Rigid";
				handleStart();
				setButtons(false);
			}
		});	
		
		view.getDisplay().getCustomAlgButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				algChoice = "CustomAlg";
				handleStart();
				setButtons(false);
			}
		});
		
		view.getDisplay().getGraphButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				handleGraph();
			}
		});
	}
	
	protected void setButtons(boolean onOff){
		view.getDisplay().getSimpleButton().setEnabled(onOff);
		view.getDisplay().getRigidButton().setEnabled(onOff);
		view.getDisplay().getCustomAlgButton().setEnabled(onOff);
		//view.getDisplay().getGraphButton().setEnabled(onOff);
	}
	
	protected void handleStart() {
		log.info("N=" + Display.CURRENT_BEACONS + ", R=" + Display.CURRENT_RADIUS + " (in relation to interval [0,1]: " + Display.CURRENT_RADIUS/100f + ")");
		view.getData().start(algChoice);
	}
	
	protected void handleCreate() {
		int beacons = view.getDisplay().getBeaconSlider().getValue();
		int radius = view.getDisplay().getRadiusSlider().getValue();
		
		if(radius < 1) {
			showAlert("Radius must be larger than zero.");
		} else {
			view.getData().create(algChoice, beacons, radius);
		}
	}
	
	protected void handleGraph(){
		Map <Integer, List<Double>> map = new HashMap<Integer,List<Double>>();
		Map <Integer, List<Double>> mapNumMoves = new HashMap<Integer,List<Double>>();
		DataComponent.animate = false;
		int beacons = view.getDisplay().getBeaconSlider().getValue();
		int r = view.getDisplay().getRadiusSlider().getValue();
		log.info("Current Alg Choice: " + algChoice);
		for(int i = 1; i <= Display.RADIUS_MAX; ++i){
			List<Double> list = new ArrayList<Double>();
			List<Double> listNumMoves = new ArrayList<Double>();
			view.getDisplay().getRadiusSlider().setValue(i);
			int radius = view.getDisplay().getRadiusSlider().getValue();
			
			
			for(int j=0; j<SAMPLE_SIZE; ++j){
				DataComponent.SUM=0;
				DataComponent.nummoves=0;
				view.getData().create(algChoice, beacons, radius);
				view.getData().start(algChoice);
				//Gets the sum of movements from the algorithms
				list.add((double)DataComponent.SUM);
				listNumMoves.add((double)DataComponent.nummoves);
			}
			
			//list.add((double)DataComponent.SUM/Display.CURRENT_BEACONS);
			//Puts the list of doubles in the map, calculates the average later
			map.put(i, list);
			mapNumMoves.put(i, listNumMoves);
			
		}
		view.getDisplay().getRadiusSlider().setValue(r);
		view.getDisplay().getCreateButton().doClick();
		switch(algChoice){
		
		}
		view.getDisplay().getSimpleButton().doClick();
		log.info("Sum of moves map: " + map.toString());
		log.info("Number of moves map: " + mapNumMoves.toString());
		DataComponent.animate = true;
		new CreateGraph("COMP3203 Final Project" ,"Radius vs Sum of Movements of "+Display.CURRENT_BEACONS+" Beacons using "+algChoice + " Algorithm", map, 0).setVisible(true);
		new CreateGraph("COMP3203 Final Project" ,"Radius vs Number of Movements of "+Display.CURRENT_BEACONS+" Beacons using "+algChoice + " Algorithm", mapNumMoves,1).setVisible(true);
	}
	

	public boolean showAlert(String message) {
		JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
		return false;
	}
}
