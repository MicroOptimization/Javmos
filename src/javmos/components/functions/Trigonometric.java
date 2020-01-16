package javmos.components.functions;

import javmos.JavmosGUI;
import javmos.enums.FunctionType;

public abstract class Trigonometric extends Function {

    protected double a, k;

    public Trigonometric(JavmosGUI gui, java.lang.String function, java.lang.String name) {
        super(gui);
    }

    public abstract String getFirstDerivative();

    public abstract String getSecondDerivative();

    public abstract double getValueAt(double x, FunctionType functionType);
}
