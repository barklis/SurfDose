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
	List<List<AreaChart<Number,Number>>> charts;
	
	public ChartPanel(GUI gui) {
		this.gui = gui;
		charts = new ArrayList<List<AreaChart<Number,Number>>>();
	}
	
	public boolean isListInited() {
		return charts.size() == 0 ? false : true;
	}
	
	public synchronized void initChartList() {
		charts.clear();
		for(int c = 0; c <= DcmData.getMaxContourId(); c++) {
			List<AreaChart<Number,Number>> tmpList = new ArrayList<AreaChart<Number,Number>>();
			for(int i = 0; i < DcmData.getNumberOfFrames(); ++i)
				tmpList.add(createChart(i, c));
			charts.add(tmpList);
		}
	}
	
	public synchronized AreaChart<Number,Number> getChart(){
		return charts.get(DcmData.getCurrentContourId()).get(gui.getCenterPanel().getDrawingPanel().getCurrentFrame());
	}
	
	private AreaChart<Number, Number> createChart(int frameNumber, int id) {
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
		
		List<Point> data = DcmData.getDcmFrames().get(frameNumber).getContourById(id).getData();
		if(data.size() != 0) {	
			double pos = 0;
			for(int i = 0; i < data.size(); ++i) {
				if(i != 0)
					pos += Point.distance(data.get(i), data.get(i-1));
				series.getData().add(new XYChart.Data<Number, Number>(pos, data.get(i).getValue()/100));
			}
		}
		lineChart.getData().add(series);
		return lineChart;
	}
	
}
