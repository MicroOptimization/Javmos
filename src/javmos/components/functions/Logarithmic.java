package javmos.components.functions;

import javmos.JavmosGUI;
import javmos.enums.FunctionType;

public class Logarithmic extends Function {

    public double a, base, k;

    public Logarithmic(JavmosGUI gui, String function) {
        super(gui);
        try {
            String temp = new String(function);
            function = function.indexOf('=') == -1 ? function : function.substring(function.indexOf('=') + 1);
            /*
             * Below, we use ternary operators to calculate the value of a/k.
             * Here, we have one case where there is no a/k in the actual function line and set a to one
             * Next, we use the next ternary operator to check if the value in front is "-" and react accordingly
             * Else, we have retrieve the value for a/k from the function
             */
            a = (function.indexOf('l') == 0) ? 1
                    : (function.substring(0, function.indexOf('l')).equals("-"))
                    ? -1 : Double.valueOf(function.substring(0, function.indexOf('l')));
            k = (function.indexOf('(') == function.indexOf('x') - 1) ? 1
                    : (function.substring(function.indexOf('(') + 1, function.indexOf('x')).equals("-"))
                    ? -1 : Double.valueOf(function.substring(function.indexOf('(') + 1, function.indexOf('x')));
            /*
             * Here, we are using ternary operators to calculate the base
             * We check if there is an 'n' in the function, which allows us to see if it is log or ln
             * if it is ln, we set base to e
             * Else, we check if there is a base in the function
             * if not, we set it to 10
             * else, we retrieve the base from the function.
             */
            base = (function.indexOf('n') != -1) ? Math.E
                    : ((function.indexOf('g') + 1 == function.indexOf('(')) ? 10
                    : Integer.valueOf(function.substring(function.indexOf('g') + 1, function.indexOf('('))));
            function = temp;
        } catch (Exception e) {
            try {
                throw new Exception(function + " is not a valid function!");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public String getFirstDerivative() {
        /*
         * returns the first derivative for logarithmic functions
         * Here, we check if it is ln or log
         * if it ln, the derivative is just 1/x
         * Else, we have to account for the ln(base)
         */
        return ("f'(x) = " + a + " / " + (base == Math.E ? "x" : "(xln(" + base + "))"));
    }

    public String getSecondDerivative() {
        /*
         * returns the second derivative for logarithmic function
         * Again, we check if it is ln or log, and we react appropriately
         */
        return ("f''(x) = " + -a + " / " + (base == Math.E ? "x ^ 2" : "(x ^ 2 * ln(" + base + "))"));
    }

    public double getValueAt(double x, FunctionType functionType) {
        double value = 0;
        if (functionType == FunctionType.ORIGINAL) {
            value = a * Math.log(k * x) / Math.log(base);
        } else if (functionType == FunctionType.FIRST_DERIVATIVE) {
            value = a / ((Math.log(base) / Math.log(Math.E)) * x);
        } else if (functionType == FunctionType.SECOND_DERIVATIVE) {
            value = -a / ((Math.log(base) / Math.log(Math.E)) * x * x);
        } else {
            value = 2 * a / ((Math.log(base) / Math.log(Math.E)) * x * x * x);
        }
        return value;
    }
}
