package javmos.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import javmos.JavmosGUI;
import javmos.components.CartesianPlane;
import javmos.components.JavmosPanel;
import javmos.components.functions.Function;

public class DrawListener implements ActionListener {

    private final JavmosGUI gui;
    private final JavmosPanel panel;

    public DrawListener(JavmosGUI gui, JavmosPanel panel) {
        this.gui = gui;
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            if (!gui.getEquationField().isEmpty()) {
                Function function = panel.getFunction();
                panel.setFunction(function);
                gui.setFirstDerivativeLabel(function.getFirstDerivative());
                gui.setSecondDerivativeLabel(function.getSecondDerivative());
                System.out.println(panel.components);
            }
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Function Error", JOptionPane.ERROR_MESSAGE);
        }
        panel.repaint();
    }
}
