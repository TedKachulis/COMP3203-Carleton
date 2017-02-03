package COMP3203.FINAL_PROJECT;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

public class DataComponent extends JPanel{
	private static final long serialVersionUID = 1L;
	protected ArrayList<Beacon> BeaconList;
	protected int radius;
	protected Random rand;
	public final static int MAX_TRACE_LEN = 100 * Client.LINE_SCALE;
	public final static int CENTER_Y = 200;
	public static int WIDTH;
	public static int offsetX;
	public static Point lineStartPoint = new Point((Client.DEFAULT_WIDTH - MAX_TRACE_LEN)/2, CENTER_Y);
	public static Point lineEndPoint = new Point(Client.DEFAULT_WIDTH - (Client.DEFAULT_WIDTH - MAX_TRACE_LEN)/2, CENTER_Y);
	public static boolean animate = true;
	public static int nummoves= 0;
	
	public static int SUM = 0;
	
	public DataComponent(){
		BeaconList = new ArrayList<Beacon>();
		rand = new Random();
		initLayout();
		
	}
	
	public void initLayout(){
		setBackground(Color.WHITE);
	}
	
	@Override
	public void paintComponent(Graphics beacon){
		super.paintComponent(beacon);
		Graphics2D drawBeacon = (Graphics2D) beacon;
		
		drawLine(drawBeacon, new Point((Client.DEFAULT_WIDTH - MAX_TRACE_LEN)/2, CENTER_Y), 
			new Point(Client.DEFAULT_WIDTH - (Client.DEFAULT_WIDTH- MAX_TRACE_LEN)/2 ,CENTER_Y));
			
		Color c;
		switch(Client.algChoice){
		case "Simple":
			c = new Color(255,0,0,100);
			break;
		case "Rigid":
			c= new Color(0,255,0,100);
			break;
		case "CustomAlg":
			c = new Color(0,0,255,100);
			break;
			default:
				c = new Color(255,0,0,100);
				break;
		
		}
		for(Beacon b :BeaconList){
			b.draw(drawBeacon,c);
		}
	}
	
	
	public void drawLine(Graphics2D beacon, Point p1, Point p2){
		beacon.setColor(new Color(230,230,230));
		beacon.drawLine(p1.x, p1.y, p2.x, p2.y);
		repaint();
	}
	
	public void start(String choice){
		switch(choice){
		case "Simple":
			SimpleAlg(BeaconList);
			break;
			
		case "Rigid":
			RigidAlg(BeaconList);
			break;
			
		case "CustomAlg":
			CustomAlg(BeaconList);
			break;
		}
		repaint();
	}
	
	protected void RigidAlg(List<Beacon> beacons){
		sortX(beacons);
		int lineStartx =(int) lineStartPoint.getX();
		int lineEndx = (int) lineEndPoint.getX();
		int coveredTo = lineStartx;
		int count = 1;
		int oldX = 0;
		//int sumMoves = 0;
		for(Beacon b : beacons){
			nummoves++;
			addToSum(b.getX()-b.getR()-lineStartx);
			oldX = b.getX();
			
			if(coveredTo+ b.getR() >= lineEndx){	//All excess beacons are moved as right as possible (End of line)
				b.setX(lineEndx);
			}
			else{
				b.setX(coveredTo + b.getR());		//Sets the beacons to be evenly distributed along the line, covering exactly 2R each
				coveredTo += 2* b.getR();
			}if(animate){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				this.paintImmediately(getBounds());
			}
			Client.log.info("Beacon " + count + " moved from " +(oldX - (int)lineStartPoint.getX()) + " to " + (b.getX()- (int)lineStartPoint.getX()));
			count++;
		}
	}
	public static void addToSum(double value){
		SUM+=Math.abs(value);
	}
	protected void SimpleAlg(List<Beacon> beacons){
		sortX(beacons);
		int lineStartx =(int) lineStartPoint.getX();
		int lineEndx = (int) lineEndPoint.getX();
		int totalDist = lineEndx - lineStartx;
		int coveredTo = lineStartx;
		int prevX=0;
		int count =0;
		int oldX =0;
		//Client.log.info("Size of beacon:" + (lineEndx - lineStartx)/beacons.size());
		//Client.log.info("Total Dist:" + totalDist+ ", Beacons.size:" + beacons.size() + ", radius: " + radius);
		if(beacons.size()*2*radius <= totalDist){//Not enough, or exactly enough sensors to cover whole thing. So space evenly to cover whole thing
			for(Beacon b: beacons){
				nummoves++;
				addToSum((b.getX()-b.getR())-lineStartx);
				b.setX(coveredTo + b.getR());		//Sets the beacons to be evenly distributed along the line, covering exactly 2R each
				coveredTo += 2* b.getR();
				if(animate){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					this.paintImmediately(getBounds());
				}
			}
		}
		else{											//Case when we have enough sensors to cover more than the whole line
			for(Beacon b: beacons){
				
				
				oldX = b.getX();
				if(b.getX()- b.getR() > coveredTo){		//For first beacon
					nummoves++;
					addToSum((b.getX()-b.getR())-coveredTo);
					b.setX(coveredTo + b.getR());
				}
				else if(b.getX() - b.getR() > prevX +  b.getR()){nummoves++;
					addToSum((b.getX()-b.getR())-coveredTo);
					b.setX(prevX + 2*b.getR());
				}
				
				prevX = b.getX();
				coveredTo=(b.getX()+b.getR());
				if(animate){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					this.paintImmediately(getBounds());
				}
				Client.log.info("Beacon " + count + " moved from " +(oldX - (int)lineStartPoint.getX()) + " to " + (b.getX()- (int)lineStartPoint.getX()));
				count++;
			}
			
			sortXBackwards(beacons);					//Have to look at it from both sides to cover entire range
			coveredTo = lineEndx;
			
			for(Beacon b: beacons){
				
				oldX = b.getX();
				if(b.getX()+ b.getR() < coveredTo){		//For first beacon
					nummoves++;
					addToSum(coveredTo - (b.getX()+b.getR()));
					b.setX(coveredTo - b.getR());
				}
				else if(b.getX() + b.getR() < prevX -  b.getR()){
					nummoves++;
					addToSum(coveredTo - (b.getX()+b.getR()));
					b.setX(prevX - 2*b.getR());
				}
				
				else if (b.getX()-b.getR() <= lineStartx){
					//do nothing because we are already past the beginning of the line
				}
				
				prevX = b.getX();
				coveredTo=(b.getX()-b.getR());
				if(animate){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					this.paintImmediately(getBounds());
				}
				Client.log.info("Then beacon " + count + " moved from " +(oldX - (int)lineStartPoint.getX()) + " to " + (b.getX()- (int)lineStartPoint.getX()));
				count--;
			}
		}


	}

	protected void CustomAlg(List<Beacon> beacons){		//Recursively splits the beacons in half and sorts them: Should be log(n) time, faster and better coverage than the two above
		
		int lineStartx =(int) lineStartPoint.getX();
		int lineEndx = (int) lineEndPoint.getX();
		int totalDist = lineEndx - lineStartx;
		int coveredTo = lineStartx;
		if(beacons.size()*2*radius <= totalDist){//Not enough, or exactly enough sensors to cover whole thing. So space evenly to cover whole thing
			sortX(beacons);
			for(Beacon b: beacons){
				nummoves++;
				addToSum((b.getX()-b.getR())-lineStartx);
				b.setX(coveredTo + b.getR());		//Sets the beacons to be evenly distributed along the line, covering exactly 2R each
				coveredTo += 2* b.getR();
				if(animate){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					this.paintImmediately(getBounds());
				}
			}
		}
		else{
			sort(beacons,0, beacons.size()-1);
		}
	}
	
	private void sort(List<Beacon> beacon, int left, int right){
		int totalLength = (int)(lineEndPoint.getX() - lineStartPoint.getX());
		int offsetToStart = (int)lineStartPoint.getX();
		int r = beacon.get(1).getR();
		if( left < right){
			nummoves++;
			int center = (left + right)/2;
			int oldX = beacon.get(center).getX();
			beacon.get(center).setX(totalLength/BeaconList.size()*center + offsetToStart + r);
			addToSum(oldX-beacon.get(center).getX());
			if(animate){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				this.paintImmediately(getBounds());
			}
			sort(beacon, left, center);
			sort(beacon, center+1, right);
		}
	}

	
	public void create(String choice, int numBeacons, int r){
		int pos=0;
		radius = Client.LINE_SCALE * r;
		offsetX = (Client.DEFAULT_WIDTH - MAX_TRACE_LEN)/2;
		BeaconList.clear();
		
		for (int i=0; i < numBeacons; ++i){
			pos = rand.nextInt(MAX_TRACE_LEN) + offsetX;
			BeaconList.add(new Beacon(pos, CENTER_Y, radius));
		}
		
		repaint();
	}
	
	public void sortX(List<Beacon> beacons){
		Collections.sort(beacons, new Comparator<Beacon>(){
			@Override
			public int compare(Beacon b1, Beacon b2){
				return b1.getX() - b2.getX();
			}
		});
	}
	
	public void sortXBackwards(List<Beacon> beacons){
		Collections.sort(beacons, new Comparator<Beacon>(){
			@Override
			public int compare(Beacon b1, Beacon b2){
				return b2.getX() - b1.getX();
			}
		});
	}
}
