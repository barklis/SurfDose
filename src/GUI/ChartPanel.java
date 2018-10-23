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

	private List<AreaChart<Number,Number>> charts;
	
	public ChartPanel() {
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
		return charts.get(GUI.instance().getCenterPanel().getDrawingPanel().getCurrentFrame());
	}
	
	private AreaChart<Number, Number> createChart(int frameNumber) {
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel(Preferences.getLabel("contourLength") + " [°]");
		yAxis.setLabel(Preferences.getLabel("dose") + " [GY]");
		
		final AreaChart<Number, Number> lineChart = new AreaChart<Number, Number>(xAxis, yAxis);
		lineChart.setTitle(Preferences.getLabel("calculatedDose"));
		lineChart.setCreateSymbols(false);
		lineChart.setLegendVisible(false);
		final XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		series.setName("Dose");
		
		List<Point> data = DcmData.getDcmFrames().get(frameNumber).getContourById(DcmData.getCurrentContourId()).getData();
		if(data.size() != 0) {	
			int startingIndex = getStartingIndex(data);
			for(int i = startingIndex; i < data.size(); ++i) {
				if(data.get(i).getAngle() > DcmData.getAngularWidth())
					break;
				series.getData().add(new XYChart.Data<Number, Number>(Math.round(data.get(i).getAngle()*180/Math.PI), data.get(i).getValue()/100));
			}
			for(int i = 0; i < startingIndex; ++i) {
				if(data.get(i).getAngle() > DcmData.getAngularWidth())
					break;
				series.getData().add(new XYChart.Data<Number, Number>(Math.round(data.get(i).getAngle()*180/Math.PI), data.get(i).getValue()/100));
			}
		}
		lineChart.getData().add(series);
		return lineChart;
	}
	
	public int getStartingIndex(List<Point> points) {
		int index = 0;
		double minAngle = 10.0;
		for(int i = 0; i < points.size(); ++i) {
			if(points.get(i).getAngle() < minAngle) {
				minAngle = points.get(i).getAngle();
				index = i;
			}
		}
		return index;
	}
	
}
