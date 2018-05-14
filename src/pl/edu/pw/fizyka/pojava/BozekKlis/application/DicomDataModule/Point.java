package pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule;

public class Point{
	double x, y;
	double value;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
		this.value = 0;
	}
	
	public Point(double x, double y, double value) {
		this.x = x;
		this.y = y;
		this.value = value;
	}
	
	public static double distance(Point p1, Point p2) {
		return Math.sqrt((p2.getX()-p1.getX())*(p2.getX()-p1.getX()) + (p2.getY()-p1.getY())*(p2.getY()-p1.getY()));
	}
	
	public double getValue() {
		return value;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
