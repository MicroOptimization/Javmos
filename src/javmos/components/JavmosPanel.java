package javmos.components;

import java.util.HashSet;
import java.util.LinkedList;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

import javmos.*;
import javmos.components.functions.*;
import javmos.listeners.PointClickListener;

public class JavmosPanel extends JPanel {

    public final JavmosGUI gui;
    public final LinkedList<JavmosComponent> components;

    public JavmosPanel(JavmosGUI gui) {
        this.gui = gui;
        this.components = new LinkedList<>();
    }

    public void setPlane(CartesianPlane plane) { //JM
        components.addFirst(plane);
    }

    public void setFunction(Function function) { //JM
        if (!components.contains(function)) {
            components.clear();
            this.setPlane(new CartesianPlane(gui));
            components.add(function);
        }
        double min = -(gui.getPlaneWidth() * gui.getDomainStep()) / (2 * gui.getZoom());
        double max = (gui.getPlaneWidth() * gui.getDomainStep()) / (2 * gui.getZoom());

        try {
            HashSet<Point> xInts = function.getXIntercepts(min, max);
            if (xInts != null) {
                components.addAll(xInts);
            }
            HashSet<Point> critPoints = function.getCriticalPoints(min, max);
            if (critPoints != null) {
                components.addAll(critPoints);
            }
            HashSet<Point> infPoints = function.getInflectionPoints(min, max);
            if (infPoints != null) {
                components.addAll(infPoints);
            }
            PointClickListener ptListener = new PointClickListener(gui);
            ptListener.setPoints(components);
            this.addMouseListener(ptListener);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Function getFunction() {
        String f = gui.getEquationField();
        if (f.contains("sin")) {
            return new Sine(gui, f);
        } else if (f.contains("cos")) {
            return new Cosine(gui, f);
        } else if (f.contains("tan")) {
            return new Tangent(gui, f);
        } else if (f.contains("log") || f.contains("ln")) {
            return new Logarithmic(gui, f);
        } else {
            return new Polynomial(gui, f);
        }
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        this.setPlane(new CartesianPlane(gui));
        try {
            this.setFunction(this.getFunction());
        } catch (Exception e) {
        }
        for (int i = 0; i < components.size(); i++) {
            try {
                //Ensures the function itself has a unique colour. Purely aesthetic.
                if (components.get(i) instanceof Function) {
                    graphics.setColor(Color.decode("#db1313"));
                }
                components.get(i).draw((Graphics2D) graphics);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
