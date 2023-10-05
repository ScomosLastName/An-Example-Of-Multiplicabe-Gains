package finalProject;

public class Point {
	int x; // x-coordinate of the point
	double y; // y-coordinate of the point
	
	public Point(int x, double y) { // Constructor for Point class, runs on initialization
		this.x = x; // Set the x-coordinate of the point
		this.y = y; // Set the y-coordinate of the point
	}
	
	public int getX() {
		return x; // Return the x-coordinate of the point
	}
	
	public double getY() {
		return y; // Return the y-coordinate of the point
	}
}