package pl.edu.pw.fizyka.pojava.BozekKlis.application.DicomDataModule;

// Stores information about value in certain position
public class Point{
	double x, y;
	double value;
	double angle;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
		this.value = 0.0;
		this.angle = 0.0;
	}
	
	public Point(double x, double y, double value) {
		this.x = x;
		this.y = y;
		this.value = value;
	}
	
	public double getAngle() {
		return angle;
	}

	public void calculateAngle(double[] uVector, double x0, double y0) {
		double x2 = x-x0;
		double y2 = y-y0;
		angle = Math.atan2(uVector[0]*y2-x2*uVector[1], uVector[0]*x2+uVector[1]*y2);
		if(angle < 0)
			angle += 2*Math.PI;
	}

	public void addValue(double value) {
		this.value += value;
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