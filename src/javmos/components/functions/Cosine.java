package javmos.components.functions;

import javmos.JavmosGUI;
import javmos.enums.FunctionType;

public final class Cosine extends Trigonometric {

    public Cosine(JavmosGUI gui, String function) {
        super(gui, function, "cosine");
        /*
         * Below, we use ternary operators to calculate the value of a/k.
         * Here, we have one case where there is no a/k in the actual function line and set a to one
         * Next, we use the next ternary operator to check if the value in front is "-" and react accordingly
         * Else, we have retrieve the value for a/k from the function
         */
        String temp = new String(function);
        function = function.indexOf('=') == -1 ? function : function.substring(function.indexOf('=') + 1);
        a = (function.indexOf('c') == 0) ? 1
                : (function.substring(0, function.indexOf('c')).equals("-"))
                ? -1 : Double.valueOf(function.substring(0, function.indexOf('c')));
        k = (function.indexOf('(') == function.indexOf('x') - 1) ? 1
                : (function.substring(function.indexOf('(') + 1, function.indexOf('x')).equals("-"))
                ? -1 : Double.valueOf(function.substring(function.indexOf('(') + 1, function.indexOf('x')));
        function = temp;
    }

    public String getFirstDerivative() {
        /*
         * This function returns the first derivative of a cosine
         * Ternary operations below check for whether the prefixes for the functions are 1 or -1 and acts accordingly
         * if a * -k is negative one, we use "". else, we go into our nested ternary operator
         * if a * -k is -1, we use "-1", else, we use a * -k
         */
        return "f'(x) = " + (a * -k == 1 ? "" : (a * -k == -1 ? "-" : a * -k)) + "sin(" + (k == 1 ? "" : (k == -1 ? "-" : k)) + "x)";
    }

    public String getSecondDerivative() {
        /*
         * This function returns the second derivative of a cosine
         * Again, ternary operators below check if prefixes are 1/-1 and acts accordingly
         */
        return "f''(x) = " + (a * k * -k == 1 ? "" : (a * k * -k == -1 ? "-" : a * k * -k)) + "cos(" + (k == 1 ? "" : (k == -1 ? "-" : k)) + "x)";
    }

    public double getValueAt(double x, FunctionType functionType) {
        double value = 0;
        if (functionType == FunctionType.ORIGINAL) {
            value = a * Math.cos(k * x);
        } else if (functionType == FunctionType.FIRST_DERIVATIVE) {
            value = -a * k * Math.sin(k * x);
        } else if (functionType == FunctionType.SECOND_DERIVATIVE) {
            value = -a * k * k * Math.cos(k * x);
        } else {
            value = a * k * k * k * Math.sin(k * x);
        }
        return value;
    }
}
