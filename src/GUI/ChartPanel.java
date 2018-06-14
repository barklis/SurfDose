package GUI;

import java.util.ArrayList;
import java.util.List;

import DataModule.DcmData;
import DataModule.Point;
import application.Preferences;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class ChartPanel{

	GUI gui;
	List<AreaChart<Number,Number>> charts;
	
	public ChartPanel(GUI gui) {
		this.gui = gui;
		charts = new ArrayList<AreaChart<Number,Number>>();
	}
	
	public boolean isListInited() {
		return charts.size() == 0 ? false : true;
	}
	
	public synchronized void initChartList() {
		charts.clear();
		for(int i = 0; i < DcmData.getNumberOfFrames(); ++i)
			charts.add(createChart(i));
	}
	
	public synchronized AreaChart<Number,Number> getChart(){
		return charts.get(gui.getCenterPanel().getDrawingPanel().getCurrentFrame());
	}
	
	private AreaChart<Number, Number> createChart(int frameNumber) {
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel(Preferences.getLabel("contourLength") + " [mm]");
		yAxis.setLabel(Preferences.getLabel("dose") + " [GY]");
		
		final AreaChart<Number, Number> lineChart = new AreaChart<Number, Number>(xAxis, yAxis);
		lineChart.setTitle(Preferences.getLabel("calculatedDose"));
		lineChart.setCreateSymbols(false);
		lineChart.setLegendVisible(false);
		final XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		series.setName("Dose");
		
		List<Point> data = DcmData.getDcmFrames().get(frameNumber).getContourById(DcmData.getCurrentContourId()).getData();
		if(data.size() != 0) {	
			double pos = 0;
			for(int i = 0; i < data.size(); ++i) {
				if(i != 0)
					pos += Point.distance(data.get(i), data.get(i-1));
				series.getData().add(new XYChart.Data<Number, Number>(pos, data.get(i).getValue()));
			}
		}
		lineChart.getData().add(series);
		return lineChart;
	}
	
}
