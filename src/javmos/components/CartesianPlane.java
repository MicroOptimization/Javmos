package javmos.components;

import javmos.JavmosComponent;
import javmos.JavmosGUI;

import java.awt.Graphics2D;
import java.awt.*;

public class CartesianPlane extends JavmosComponent {

    public CartesianPlane(JavmosGUI gui) {
        super(gui);
    }

    //Btw Zoom is actually size of a single square on the graph in pixels 
    //bigger zoom = bigger single square.
    public void draw(Graphics2D graphics) {
        //Measurement related variables are made
        int zoom = (int) gui.getZoom(); //Size of "Square unit" at current level of zoom in pixels.
        double xScale = gui.getDomainStep(); //Basically what each X interval is multiplied by to show the scale.
        double yScale = gui.getRangeStep(); //Same as above, but for Y intervals.
        int aestheticOffset = 3; //In pixels. It's basically the margins for the scale to make things look nicer.
        //Painting lines of interest
        graphics.setColor(Color.black);
        graphics.setStroke(new BasicStroke(3)); //Sets additional line thickness for the 2 axes.
        graphics.drawLine(0, 400, 800, 400); //Draws X axis 
        graphics.drawLine(400, 0, 400, 800); //Draws Y axis
        graphics.setStroke(new BasicStroke(1)); //Back to regular line thickness
        graphics.setFont(new Font("Helvetica", Font.PLAIN, zoom / 4));
        graphics.drawString(" 0.0", 400, 400 - aestheticOffset); //Draws (0, 0) grid label. It's either this, or I draw it 4 times.
        /*NOTE:
        -This loop iterates through pixels. Remember, the grid is 800 x 800 pixels total.
        -This loop draws grid lines (First method call) and label the intervals (Second method call).
        -This loop only iterates through half the pixels, because I have a method call for each 
        respective positive and negative axis. So it only goes to zero.
        Painting regular grid lines
        Formula/Params: (Drawing the scale)
        i: (Counts pixels to be drawn)
        First: (The value that's shown at given part of scale) 
        -Value shifted 1 because (0, 0) is painted separately. 
        -There's a space added to the front of the sign for aesthetic reasons.  
        -i gets divided by zoom to get # of lines from (0, 0) instead of pixels.
        -That way, when the scale gets multiplied in, it won't multiply with pixels,
        it'll multiply by # of lines to (0, 0)
        Second and Third: (X position and Y Position)
        -400 gets added to i or subtracted depending on which side of the X or Y axis is being drawn. 
        -Zoom is added to i for that 1 square offset.    
        -Aesthetic offset is subtracted to make everything slightly higher than the x axis and space
        things out a bit.
         */
        for (int i = 0; i < 400; i += zoom) {
            //Positive X Grid
            graphics.drawLine(400 + i, 0, 400 + i, 800);
            graphics.drawString(" " + (double) Math.round(100 * ((i / zoom + 1) * xScale)) / 100, 400 + (i + zoom), 400 - aestheticOffset);
            //Negative X Grid
            graphics.drawLine(400 - i, 0, 400 - i, 800);
            graphics.drawString(" -" + (double) Math.round(100 * ((i / zoom + 1) * xScale)) / 100, 400 - (i + zoom), 400 - aestheticOffset);

            //Positive Y Grid
            graphics.drawLine(0, 400 - i, 800, 400 - i);
            graphics.drawString(" " + (double) Math.round(100 * ((i / zoom + 1) * yScale)) / 100, 400, 400 - (i + zoom) - aestheticOffset);
            //negative Y Grid
            graphics.drawLine(0, 400 + i, 800, 400 + i);
            graphics.drawString(" -" + (double) Math.round(100 * ((i / zoom + 1) * yScale)) / 100, 400, 400 + (i + zoom) - aestheticOffset);
        }
    }
}
