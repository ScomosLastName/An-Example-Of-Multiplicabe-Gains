package finalProject;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class LineGraph {
	
	ArrayList<Point> points; // List to store data points for the graph
	int x, y, width, height; // Coordinates and dimensions of the graph
	Color colour, frameColour; // Colors for the graph and its frame
	int windowHeight; // Height of the window
	int windowWidth; // Width of the window
	
	public LineGraph(ArrayList<Point> points, int x, int y, int width, int height, Color colour, Color frameColour) { 
		// Constructor for LineGraph class
		this.points = points; // Assign the list of data points
		this.x = x; // Set the x-coordinate of the graph
		this.y = y; // Set the y-coordinate of the graph
		this.width = width; // Set the width of the graph
		this.height = height; // Set the height of the graph
		this.colour = colour; // Set the color of the graph
		this.frameColour = frameColour; // Set the color of the graph's frame
	}
	
	public void graphPoints(Graphics g) {
		g.setColor(colour); // Set the color for drawing the graph
		
		// Calculate the dilation factors for x and y coordinates
		double xFactorDilation = (double) width / points.size();
		double yFactorDilation = (double) height / getRangeMaximum();
		
		// Iterate over the points list and draw lines between consecutive points
		for (int i = 1; i < points.size(); i++) {
			g.drawLine(
				(int) Math.round(points.get(i - 1).getX() * xFactorDilation) + ((windowWidth - width) / 2),
				(int) (windowHeight - (points.get(i - 1).getY() * yFactorDilation + ((windowHeight - height) / 2))) + 8,
				(int) Math.round(points.get(i).getX() * xFactorDilation) + ((windowWidth - width) / 2),
				(int) (windowHeight - (points.get(i).getY() * yFactorDilation + ((windowHeight - height) / 2))) + 8
			);
		}	
	}
	
	public void setSize(int width, int height) {
		this.width = width; // Set the width of the graph
		this.height = height; // Set the height of the graph
	}
	
	public void setColour(Color colour) {
		this.colour = colour; // Set the color of the graph
	}
	
	public void drawAxis(Graphics g) {
		g.setColor(frameColour); // Set the color for drawing the graph's frame
		
		// Draw the vertical axis
		g.drawLine(x, y, x, y + height);
		
		// Draw the horizontal axis
		g.drawLine(x, y + height, x + width, y + height);
	}
	
	public void updateArray(ArrayList<Point> destArray) {
		this.points = destArray; // Update the data points array
	}
	
	public void updateSize(int width, int height) {
		this.width = width; // Update the width of the graph
		this.height = height; // Update the height of the graph
	}
	
	public void updateWindowSize(int width, int height) {
		windowWidth = width; // Update the width of the window
		windowHeight = height; // Update the height of the window
	}
	
	public void reset(Point yIntercept) {
		this.points.clear(); // Clear the data points list
		this.points.add(yIntercept); // Add the specified y-intercept point to the list
	}
	
	public double getRangeMaximum() {
		double max = points.get(0).getY(); // Initialize the maximum value with the y-value of the first point
		
		// Iterate over the points list to find the maximum y-value
		for (int i = 0; i < points.size() - 1; i++) {
			if (points.get(i).getY() > max) {
				max = points.get(i).getY();
			}
		}
		return max; // Return the maximum y-value
	}
}