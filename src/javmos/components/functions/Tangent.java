package javmos.components.functions;

import javmos.JavmosGUI;
import javmos.components.Point;
import javmos.enums.FunctionType;

public class Tangent extends Trigonometric {

    public Tangent(JavmosGUI gui, String function) {
        super(gui, function, "tangent");
        /*
         * Below, we use ternary operators to calculate the value of a/k.
         * Here, we have one case where there is no a/k in the actual function line and set a to one
         * Next, we use the next ternary operator to check if the value in front is "-" and react accordingly
         * Else, we have retrieve the value for a/k from the function
         */
        String temp = new String(function);
        function = function.indexOf('=') == -1 ? function : function.substring(function.indexOf('=') + 1);
        a = (function.indexOf('t') == 0) ? 1
                : (function.substring(0, function.indexOf('t')).equals("-"))
                ? -1 : Double.valueOf(function.substring(0, function.indexOf('t')));
        k = (function.indexOf('(') == function.indexOf('x') - 1) ? 1
                : (function.substring(function.indexOf('(') + 1, function.indexOf('x')).equals("-"))
                ? -1 : Double.valueOf(function.substring(function.indexOf('(') + 1, function.indexOf('x')));
        function = temp;
    }

    public String getFirstDerivative() {
        /*
         * This function returns the first derivative of a tangent
         * Ternary operations below check for whether the prefixes for the functions are 1 or -1 and acts accordingly
         * if a * -k is negative one, we use "". else, we go into our nested ternary operator
         * if a * -k is -1, we use "-1", else, we use a * -k
         */
        return "f'(x) = " + (a * k == 1 ? "" : (a * k == -1 ? "-" : a * k)) + "sec^2(" + (k == 1 ? "" : (k == -1 ? "-" : k)) + "x)";
    }

    public String getSecondDerivative() {
        /*
         * This function returns the second derivative of a tangent
         * Again, ternary operators below check if prefixes are 1/-1 and acts accordingly
         */
        return "f''(x) = " + (2 * a * k * k == 1 ? "" : (2 * a * k * k == -1 ? "-" : 2 * a * k * k)) + "sec^2("
                + (k == 1 ? "" : (k == -1 ? "-" : k)) + "x)tan(" + (k == 1 ? "" : (k == -1 ? "-" : k)) + "x)";
    }

    public double getValueAt(double x, FunctionType functionType) {
        double sum = 0;
        if (functionType == FunctionType.ORIGINAL) {
            sum = a * Math.tan(k * x);
        } else if (functionType == FunctionType.FIRST_DERIVATIVE) {
            sum = a * k * Math.pow(1 / Math.cos(k * x), 2);
        } else if (functionType == FunctionType.SECOND_DERIVATIVE) {
            sum = 2 * a * k * k * Math.pow(1 / Math.cos(k * x), 2) * Math.tan(k * x);
        } else {
            sum = 2 * a * k * k * (2 * k * Math.pow(Math.tan(k * x) / Math.cos(k * x), 2) + 2 * k * Math.pow(1 / Math.cos(k * x), 4));
        }
        return sum;
    }

    public java.util.HashSet<Point> getCriticalPoints() {
        return null;

    }

    public java.util.HashSet<Point> getInflectionPoints() {
        return null;

    }

}
