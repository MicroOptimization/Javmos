package javmos.components;

import javmos.JavmosComponent;
import javmos.JavmosGUI;
import javmos.enums.RootType;

import java.awt.BasicStroke;
import java.awt.geom.Ellipse2D;
import java.awt.Graphics2D;
import java.util.Objects;

public class Point extends JavmosComponent {

    private final int RADIUS = 5;
    private Ellipse2D.Double point;
    private RootType rootType;
    private double x;
    private double y;

    public Point(JavmosGUI gui, RootType type, double x, double y) {
        super(gui);
        this.rootType = type;
        this.x = x + 0.0; //Adding 0.0 prevents the value from displaying as -0.0 as a result of IEEE754
        this.y = y + 0.0;
    }

    public RootType getRootType() {
        return rootType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x);
    }

    public boolean equals(Object object) {
        if (object instanceof Point) {
            return hashCode() == object.hashCode();
        }
        return false;
    }

    @Override
    public String toString() {
        if (x >= gui.getMinDomain() && x <= gui.getMaxDomain() && y >= gui.getMinRange() && y <= gui.getMaxRange()) {
            return rootType.getRootName() + ": (" + x + ", " + y + ")";
        } else {
            return "";
        }
    }

    public Ellipse2D.Double getPoint() {
        return point;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void draw(Graphics2D g) {
        g.setStroke(new BasicStroke(0));
        g.setColor(getRootType().getRootColor());

        double width = 15;
        double height = 15;
        double xLoc = (x * gui.getZoom()) / gui.getDomainStep() + 400 - (width / 2);
        double yLoc = -(y * gui.getZoom()) / gui.getRangeStep() + 400 - (height / 2);
        point = new Ellipse2D.Double(xLoc, yLoc, width, height);

        double maxDomain = (gui.getMaxDomain() * gui.getZoom()) / gui.getDomainStep() + 400 - (width / 2);
        double minDomain = (gui.getMinDomain() * gui.getZoom()) / gui.getDomainStep() + 400 - (width / 2);
        double maxRange = -(gui.getMaxRange() * gui.getZoom()) / gui.getRangeStep() + 400 - (height / 2);
        double minRange = -(gui.getMinRange() * gui.getZoom()) / gui.getRangeStep() + 400 - (height / 2);
        if (xLoc >= minDomain && xLoc <= maxDomain && yLoc <= minRange && yLoc >= maxRange) {
            g.fill(point);
        }
    }
}
