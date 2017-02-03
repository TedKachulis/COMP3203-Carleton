package COMP3203.FINAL_PROJECT;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class CreateGraph extends JFrame{
	private static final long serialVersionUID = 1L;

	public CreateGraph(String appName, String chartName, Map<Integer,List<Double>> map, int mapType){
		super(appName);
		create(chartName, map, mapType);
		pack();
	}
	protected void create(String title, Map<Integer,List<Double>> map, int mapType){
		XYSeries s;
		if(mapType == 1)
			s = new XYSeries("Num Moves");
		else  
			s= new XYSeries("Sum of Moves");
		
		List<Double> l = new ArrayList<Double>();
		
		try{
			for(int i=1; i <= Display.RADIUS_MAX; ++i){
				double ave= 0;
				l.clear();
				l = (List<Double>) map.get(i);
				
				for(int j=0; j < l.size(); ++j){
					ave += l.get(j);
					//s.add(i, j);
				}
				ave= ave/l.size();
				if(mapType == 1){
					s.add(i, ave);
				}
				else{
					s.add(i, ave/DataComponent.MAX_TRACE_LEN);
				}
				//s.add(i, ave/DataComponent.MAX_TRACE_LEN);
				//s.add(i, )
			}
		}
		
		catch(NullPointerException e){
			
		}
		XYSeriesCollection dataset =  new XYSeriesCollection();
		
		dataset.addSeries(s);
		XYPlot plot;
		if(mapType == 0){	//Sum of moves
			NumberAxis x = new NumberAxis("Radius");
			NumberAxis y = new NumberAxis("Sum of movements");
			XYSplineRenderer r = new XYSplineRenderer(3);
			 plot = new XYPlot(dataset, x, y, r);

		}
		else{				//Number of moves
			NumberAxis x = new NumberAxis("Radius");
			NumberAxis y = new NumberAxis("Number of movements");
			XYSplineRenderer r = new XYSplineRenderer(3);
			 plot = new XYPlot(dataset, x, y, r);

		}
		if(Client.algChoice == "Simple")
			plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0, Color.RED);
		else if (Client.algChoice == "Rigid")
			plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0, Color.GREEN);
		else{
			plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0, Color.BLUE);
		}
		JFreeChart chart = new JFreeChart(plot);
		chart.setTitle(title);
		ChartPanel panel = new ChartPanel(chart);
		add(panel);
	}

	
}
