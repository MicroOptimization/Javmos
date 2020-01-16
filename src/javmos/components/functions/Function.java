package javmos.components.functions;

import javmos.JavmosComponent;
import javmos.JavmosGUI;
import javmos.enums.FunctionType;
import javmos.enums.RootType;
import javmos.components.Point;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.*;

public abstract class Function extends JavmosComponent {

    protected Function(JavmosGUI gui) {
        super(gui);
    }

    public abstract double getValueAt(double x, FunctionType functionType);

    public abstract String getFirstDerivative();

    public abstract String getSecondDerivative();

    public void draw(Graphics2D graphics2D) {
        graphics2D.setStroke(new BasicStroke(3)); //Aesthetic thickness
        //Must be > 0
        double precision = 0.001;
        //The lower the number, the closer the points are horizontally, which makes a more precisely drawn polynomial.
        //Various measurement related variables.
        double zoom = gui.getZoom();
        double xScale = gui.getDomainStep();
        double yScale = gui.getRangeStep();

        double maxDomain = gui.getMaxDomain();
        double minDomain = gui.getMinDomain();
        double maxRange = gui.getMaxRange();
        double minRange = gui.getMinRange();

        double positionalOffset = gui.getPlaneWidth() / 2.0; //Centers things and provides a count for the upcoming loop

        double xPosition = (-positionalOffset * zoom / xScale) + positionalOffset;
        double yPosition = -(getValueAt(positionalOffset, FunctionType.ORIGINAL) * zoom / yScale) + positionalOffset;

        double a = 0, k = 0;
        if (gui.getEquationField().contains("tan")) { //Sets trig variables for Tan so Asymptotes don't get drawn.
            String function = gui.getEquationField();
            a = (function.indexOf('t') == function.indexOf('=') + 1) ? 1 : (function.substring(function.indexOf('=') + 1, function.indexOf('t')).equals("-")) ? -1 : Integer.valueOf(function.substring(function.indexOf('=') + 1, function.indexOf('t')));
            k = (function.indexOf('(') == function.indexOf('x') - 1) ? 1 : (function.substring(function.indexOf('(') + 1, function.indexOf('x')).equals("-")) ? -1 : Integer.valueOf(function.substring(function.indexOf('(') + 1, function.indexOf('x')));
        }

        for (double xValue = -(gui.getPlaneWidth() * gui.getDomainStep()) / (2 * gui.getZoom()); xValue < (gui.getPlaneWidth() * gui.getDomainStep()) / (2 * gui.getZoom()); xValue += precision) {
            double yValue = getValueAt(xValue, FunctionType.ORIGINAL); //This and xValue are the real life numerical values for each point.
            //These next two variables are the physical position in the grid for each point.
            double xPosition2 = (xValue * zoom / xScale) + positionalOffset;
            double yPosition2 = -(yValue * zoom / yScale) + positionalOffset;

            if (!Double.isNaN(yValue)) {
                if (((xValue >= minDomain) & (xValue <= maxDomain)) && ((yValue >= minRange) & (yValue <= maxRange))) {
                    if (!gui.getEquationField().contains("tan")) {
                        graphics2D.draw(new Line2D.Double(xPosition, yPosition, xPosition2, yPosition2));
                    } else if ((yPosition - yPosition2) * (a * k) > 0) { //Insures Asymptotes aren't drawn
                        graphics2D.draw(new Line2D.Double(xPosition, yPosition, xPosition2, yPosition2));
                    }
                }
            }
            xPosition = xPosition2;
            yPosition = yPosition2;
        }
    }

    public HashSet<Point> getXIntercepts(double minDomain, double maxDomain) {
        return RootType.X_INTERCEPT.getRoots(gui, this, minDomain, maxDomain);
    }

    public HashSet<Point> getCriticalPoints(double minDomain, double maxDomain) {
        if (!(this instanceof Polynomial && this.getFirstDerivative().charAt(this.getFirstDerivative().indexOf('=') + 1) == '0')) {
            return RootType.CRITICAL_POINT.getRoots(gui, this, minDomain, maxDomain);
        } else {
            return null;
        }
    }

    public HashSet<javmos.components.Point> getInflectionPoints(double minDomain, double maxDomain) {
        if (!(this instanceof Polynomial && this.getSecondDerivative().charAt(this.getSecondDerivative().indexOf('=') + 1) == '0')) {
            return RootType.INFLECTION_POINT.getRoots(gui, this, minDomain, maxDomain);
        } else {
            return null;
        }
    }
}
