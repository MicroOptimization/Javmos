package javmos;

import java.awt.*;

public abstract class JavmosComponent {

    protected JavmosGUI gui;

    public JavmosComponent(JavmosGUI gui) {
        this.gui = gui;
    }

    public abstract void draw(Graphics2D graphics2D);
}
