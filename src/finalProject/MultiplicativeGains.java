package finalProject;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class MultiplicativeGains extends JPanel implements ActionListener {

	// JFrame is the main window of the application.
    JFrame frame;

    // Buttons for various actions in the application.
    JButton flip;
    JButton timerButton;
    JButton restartButton;
    JButton confirmRestartButton;
    JButton submit;

    // Text fields for user input.
    JTextField wealthToBetInput;
    JTextField amountToWinInput;
    JTextField amountToLooseInput;
    JTextField biasToHeadsInput;

    // Labels to display information and captions.
    JLabel wealthToBetLabel;
    JLabel amountToGainLabel, amountToLooseLabel;
    JLabel percentSign0, percentSign1, percentSign2, percentSign3; // %
    JLabel formula;
    JLabel biasToHeadsLabel;
    TextArea coinFlipResultArea;

    // Variable to keep track of the current turn.
    int turn = 0;

    // ArrayList to store the sequence of coin flips.
    ArrayList<Character> flips = new ArrayList<Character>();

    // Variables to manage game settings and statistics.
    double balance = 100;
    double fractionOfWealthToBet = 1;
    double amountToGain = 0.8;
    double amountToLoose = 0.5;
    double biasToHeads = 0.5;

    // Point object to represent the y-intercept of the balance history graph.
    Point yIntercept = new Point(turn, (int) balance);

    // ArrayList to store the balance history as a series of points.
    ArrayList<Point> balanceHistoryPoints = new ArrayList<Point>();

    // LineGraph object to display the balance history graph.
    LineGraph balanceChart;

    int frameHeight = 800;
    int frameWidth = 1530;

    // Timer object for controlling the game's refresh rate.
    Timer timer1;

    // Configuration settings for the timer.
    int initialDelay = 10;
    int refreshRate = 50;
    boolean isTimeOn = false;

    // Variable to track if the game is waiting for restart confirmation.
    boolean waitToConfrimRestart = false;

    // Custom color object for GUI components.
    Color myColor = new Color(200, 200, 200);

    public void init() {
    	balanceHistoryPoints.add(yIntercept);
    	
        frame = new JFrame("Multiplicative Gains");
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        JPanel graphPanel = new JPanel() {

			@Override
        	protected void paintComponent(Graphics g) {
        	    super.paintComponent(g);

        	    balanceChart.updateSize(getWidth() - 40, getHeight() - 25);
        	    balanceChart.updateWindowSize(getWidth(), getHeight());
        	    
        	    balanceChart.drawAxis(g);
        	    balanceChart.graphPoints(g);

        	    Font myFont = new Font ("Courier New", 1, 40);
        	    g.setFont(myFont);
        	    Color myColor = new Color(200, 200, 200, 150);
        	    g.setColor(myColor);
        	    
    	        FontMetrics fontMetrics = g.getFontMetrics();
  
        	    int textWidth = fontMetrics.stringWidth("$" + balance);	    
    	        int textHeight = fontMetrics.getHeight();
    	        
    	        if (balance == 9.223372036854776E16) {
    	        	textWidth = fontMetrics.stringWidth("Lim x→∞");
    	        	g.drawString("Lim X→∞", (getWidth() - textWidth) / 2, (getHeight() - textHeight) / 2);
    	        } else {
    	        	g.drawString("$" + balance, (getWidth() - textWidth) / 2, (getHeight() - textHeight) / 2);
    	        }
        	}
        };
        
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints inputPanelConstarints = new GridBagConstraints();
        
        coinFlipResultArea = new TextArea("", 50, 2, TextArea.SCROLLBARS_VERTICAL_ONLY);
		coinFlipResultArea.setEditable(false);
		
		add(coinFlipResultArea);
        
        JPanel outputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints outputPanelConstarints = new GridBagConstraints();
        
        JPanel formulaPanel = new JPanel();

        flip = new JButton("Flip");
        timerButton = new JButton("Start / Stop");
        restartButton = new JButton("Restart");
        confirmRestartButton = new JButton("Confirm Restart");
        submit = new JButton("Submit");
        
        wealthToBetInput = new JTextField("" + fractionOfWealthToBet * 100, 4);
        amountToWinInput = new JTextField("" + amountToGain * 100, 4);
        amountToLooseInput = new JTextField("" + amountToLoose * 100, 4);
        biasToHeadsInput = new JTextField("" + biasToHeads * 100, 4);
        
        wealthToBetLabel = new JLabel("<html><body>&nbsp Percentage of wealth<br>&nbsp to bet:</body></html>");
        amountToGainLabel = new JLabel("<html><body>&nbsp Percentage of wealth<br>&nbsp fraction to gain:</body></html>");
        amountToLooseLabel = new JLabel("<html><body>&nbsp Percentage of wealth<br>&nbsp fraction to Lose:</body></html>");
        biasToHeadsLabel = new JLabel("<html><body>&nbsp Percentage chance<br>&nbsp of the coin landing<br>&nbsp on heads:</body></html>");
        
        wealthToBetLabel.setForeground(myColor);
        amountToGainLabel.setForeground(myColor);
        amountToLooseLabel.setForeground(myColor);
        biasToHeadsLabel.setForeground(myColor);       
        
        formula = new JLabel("r = (1 + 	" + fractionOfWealthToBet + " * " + amountToGain + ")^" + biasToHeads + " * " +
        					"(1 + 	" + fractionOfWealthToBet + " * " + amountToLoose + ")^" +  (1 - biasToHeads));
        formula.setForeground(Color.WHITE);
        
        timerButton.setBackground(Color.green);
        
// Adding to buttonPanel
        buttonPanel.add(flip);
        buttonPanel.add(timerButton);
        buttonPanel.add(restartButton);
        buttonPanel.add(confirmRestartButton);
        
// Adding to inputPanel
        inputPanelConstarints.anchor = GridBagConstraints.ABOVE_BASELINE;
        
        inputPanelConstarints.gridx = 0;
        
        inputPanelConstarints.gridy = 1;
        inputPanel.add(wealthToBetLabel, inputPanelConstarints);
        
        inputPanelConstarints.gridy++;
        inputPanel.add(wealthToBetInput, inputPanelConstarints);
        
        inputPanelConstarints.gridy++;
        inputPanel.add(amountToGainLabel, inputPanelConstarints);
        
        inputPanelConstarints.gridy++;
        inputPanel.add(amountToWinInput, inputPanelConstarints);
        
        inputPanelConstarints.gridy++;
        inputPanel.add(amountToLooseLabel, inputPanelConstarints);
        
        inputPanelConstarints.gridy++;
        inputPanel.add(amountToLooseInput, inputPanelConstarints);
        
        inputPanelConstarints.gridy++;
        inputPanel.add(biasToHeadsLabel, inputPanelConstarints);
        
        inputPanelConstarints.gridy++;
        inputPanel.add(biasToHeadsInput, inputPanelConstarints);
        
        inputPanelConstarints.gridy++;
        inputPanel.add(submit, inputPanelConstarints);
        
// Adding to outputPanel
        outputPanelConstarints.gridx = 1;
        outputPanelConstarints.gridy = 1;
        outputPanel.add(coinFlipResultArea, outputPanelConstarints);
        coinFlipResultArea.setEditable(false);
        
// Adding to formulaPanel
        formulaPanel.add(formula);
        
        confirmRestartButton.setVisible(false);

        balanceChart = new LineGraph(balanceHistoryPoints, 20, 20, getWidth() - 40, getHeight() - 30, Color.RED, Color.WHITE); //getWidth() - 40, getHeight() - 70,

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(graphPanel, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.WEST);
        mainPanel.add(outputPanel, BorderLayout.EAST);
        mainPanel.add(formulaPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(frameWidth, frameHeight);
        frame.setLocationRelativeTo(null);

        // Add Action Listeners
        flip.addActionListener(this);
        timerButton.addActionListener(this);
        restartButton.addActionListener(this);
        confirmRestartButton.addActionListener(this);
        submit.addActionListener(this);
        
        mainPanel.setBackground(Color.DARK_GRAY);
        buttonPanel.setBackground(Color.DARK_GRAY);
        graphPanel.setBackground(Color.DARK_GRAY);
        inputPanel.setBackground(Color.DARK_GRAY);
        outputPanel.setBackground(Color.DARK_GRAY);
        formulaPanel.setBackground(Color.DARK_GRAY);
        
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public double roundDownCent(double input) {
        double output = Math.round(input * 100.0) / 100.0;

        return output;
    }

    public static void main(String[] args) {
        new MultiplicativeGains().init();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == flip) {
            coinFlip();
        } else if (e.getSource() == timerButton) {
            if (isTimeOn) {
                timer1.cancel();
                isTimeOn = false;
                timerButton.setBackground(Color.green);
            } else {
                timer1 = new Timer();
                timer1.schedule(new AnimationTask(), initialDelay, refreshRate);
                isTimeOn = true;
                timerButton.setBackground(Color.red);
            }
        } else if (e.getSource() == restartButton) {
            if (!waitToConfrimRestart) {
            	waitToConfrimRestart = true;
                confirmRestartButton.setVisible(true);

                restartButton.setText("Cancel");

                timer1.cancel();
                isTimeOn = false;
                timerButton.setBackground(Color.green);
            } else {
            	waitToConfrimRestart = false;
                confirmRestartButton.setVisible(false);

                restartButton.setText("Restart");
            }

        } else if (e.getSource() == confirmRestartButton) {
            System.out.println("Restarting...");
            if (waitToConfrimRestart) {
                confirmRestartButton.setVisible(false);
                restartButton.setText("Restart");
                waitToConfrimRestart = false;

                restart();

                frame.repaint();
            }
        } else if (e.getSource() == submit || e.getSource() == confirmRestartButton) {
            // Process input values and update variables
            if (wealthToBetInput != null) {
                fractionOfWealthToBet = Double.parseDouble(wealthToBetInput.getText()) / 100;
            }
            if (amountToWinInput != null) {
                amountToGain = Double.parseDouble(amountToWinInput.getText()) / 100;
            }
            if (amountToLooseInput != null) {
                amountToLoose = Double.parseDouble(amountToLooseInput.getText()) / 100;
            }
            if (biasToHeadsInput != null) {
                biasToHeads = Double.parseDouble(biasToHeadsInput.getText()) / 100;
            }
            updateFormula(); // Update the formula with the new values
        }
    }
		
    public void coinFlip() {
    	turn++;
    	
        char result; // Variable to store the result of the coin flip

        double coinFlip = Math.random(); // Generate a random number between 0 and 1

        if (coinFlip < biasToHeads) {
            result = 'H'; // If the random number is less than the bias, set the result to heads
        } else {
            result = 'T'; // Otherwise, set the result to tails
        }
        
        // Add the result to the output text area
		coinFlipResultArea.setText(coinFlipResultArea.getText() + Character.toString(result) + "\n");

        // Add the result to the list of flips
        flips.add(result);
        
        // Update the balance based on the result
        if (result == 'H') {
            balance = balance + ((balance * fractionOfWealthToBet) * amountToGain);
        } else {
            balance = balance - ((balance * fractionOfWealthToBet) * amountToLoose);
        }
        
        // Round down the balance to two decimal places
        balance = roundDownCent(balance);
        
        // Add the current balance to the balance history points
        balanceHistoryPoints.add(new Point(turn, balance));
        
        // Repaint the frame to reflect the updated balance
        frame.repaint();
        
        System.out.println(balance);
    }

    public class AnimationTask extends TimerTask {
        public void run() {
            // Perform a coin flip as part of the animation
            coinFlip();
        } 
    }
		
    public void restart() {
        // Reset the balance and turn count
        balance = 100;
        turn = 0;
        
        // Create a new y-intercept point for the balance history
        yIntercept = new Point(turn, (int) balance);
        
        // Clear the balance history points and add the y-intercept
        balanceHistoryPoints.clear();
        balanceHistoryPoints.add(yIntercept);
        
        // Reset the balance chart with the y-intercept
        balanceChart.reset(yIntercept);
        
        // Reset the coin flip result area
        coinFlipResultArea.setText(null);
        flips.clear();
    }

    public void updateFormula() {
        // Update the formula text based on the input values
        formula.setText("r = (1 + " + fractionOfWealthToBet + " * " + amountToGain + ")^" + biasToHeads + " * " +
                        "(1 + " + fractionOfWealthToBet + " * " + amountToLoose + ")^" + (1 - biasToHeads));
    }
}
