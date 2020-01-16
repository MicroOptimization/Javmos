package javmos.components.functions;

import javmos.JavmosGUI;
import javmos.enums.FunctionType;

import java.util.*;

public final class Polynomial extends Function {

    private final double[] coefficients;
    private final double[] degrees;
    private final String polynomial;

    public Polynomial(JavmosGUI gui, String polynomial) {
        super(gui);
        String y = "";
        HashMap<Double, Double> coefDeg = new HashMap<>();
        double[] coefficients;
        double[] degrees;
        int terms;
        int polyIndex = 0;
        ArrayList<String> termString = new ArrayList<>();
        String sign = "";

        if (polynomial.contains("f(x)=") || polynomial.contains("y=")) {
            y = polynomial.substring(0, polynomial.indexOf("=") + 1);
            polynomial = polynomial.substring(polynomial.indexOf("=") + 1);
        } else {
            if (!polynomial.contains("=")) {
                y = "y=";
            } else {
                try {
                    throw new Exception(polynomial + " is not a valid function!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (polynomial.charAt(polynomial.indexOf("=") + 1) == '-') {
            polynomial = polynomial.substring(polynomial.indexOf("=") + 2);
            sign = "-";
        } else {
            polynomial = polynomial.substring(polynomial.indexOf("=") + 1);
        }

        for (int i = 0; i < polynomial.length(); i++) {
            if (polynomial.charAt(i) == '+' || (i != 0 && polynomial.charAt(i - 1) != '^' && polynomial.charAt(i) == '-')) {
                termString.add(sign + polynomial.substring(polyIndex, i));
                sign = i != 0 && polynomial.charAt(i) == '-' ? "-" : "";
                polyIndex = i + 1;
            } else if (!(polynomial.substring(i).contains("+") || (polynomial.substring(i).contains("-")))) {
                termString.add(sign + polynomial.substring(i));
                break;
            }
        }
        terms = termString.size();

        double curCoef, curDeg;
        for (int i = 0; i < terms; i++) {
            if (termString.get(i).contains("x")) {
                curDeg = termString.get(i).contains("^") ? Double.parseDouble(termString.get(i).substring(termString.get(i).indexOf('^') + 1)) : 1;
                if (termString.get(i).indexOf("x") == 1 && termString.get(i).indexOf("-") == 0) {
                    curCoef = -1;
                } else {
                    curCoef = termString.get(i).indexOf("x") == 0 ? (double) 1 : Double.parseDouble(termString.get(i).substring(0, termString.get(i).indexOf("x")));
                }
                if (!termString.get(i).contains("^") && !termString.get(i).substring(termString.get(i).indexOf("x") + 1).isEmpty()) {
                    try {
                        throw new Exception(polynomial + " is not a valid function!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                curDeg = 0;
                curCoef = Double.parseDouble(termString.get(i));
            }
            if (coefDeg.containsKey(curDeg)) {
                coefDeg.put(curDeg, coefDeg.get(curDeg) + curCoef);
            } else {
                coefDeg.put(curDeg, curCoef);
            }
        }

        coefficients = new double[coefDeg.size()];
        degrees = new double[coefDeg.size()];

        Set<Double> keys = coefDeg.keySet();
        Double[] keyArray = keys.toArray(new Double[keys.size()]);
        Arrays.sort(keyArray);

        for (int i = 0; i < coefficients.length; i++) {
            degrees[i] = keyArray[coefficients.length - 1 - i];
            coefficients[i] = coefDeg.get(keyArray[coefficients.length - 1 - i]);
        }
        this.coefficients = coefficients;
        this.degrees = degrees;
        polynomial = y;
        for (int i = 0; i < coefficients.length; i++) {
            if (i != 0) {
                polynomial += coefficients[i] > 0 ? "+" : "";
            }
            if (Math.abs(coefficients[i]) != 1) {
                polynomial += coefficients[i];
            } else {
                if (coefficients[i] < 0) {
                    polynomial += "-";
                }
                if (degrees[i] == 0) {
                    polynomial += "1";
                }
            }
            if (degrees[i] != 0) {
                polynomial += "x";
                if (degrees[i] != 1) {
                    polynomial += "^" + degrees[i];
                }
            }
        }
        this.polynomial = polynomial;
    }

    public double getValueAt(double x, FunctionType functionType) {
        double[] coeff;
        double[] deg;
        String first = this.getFirstDerivative();
        String second = this.getSecondDerivative();
        String third = new Polynomial(gui, second.substring(second.indexOf('=') + 1)).getFirstDerivative();
        if (functionType == FunctionType.ORIGINAL) {
            //original
            coeff = coefficients;
            deg = degrees;
        } else if (functionType == FunctionType.FIRST_DERIVATIVE) {
            //first derivative
            coeff = new Polynomial(gui, first.substring(first.indexOf('=') + 1)).coefficients;
            deg = new Polynomial(gui, first.substring(first.indexOf('=') + 1)).degrees;
        } else if (functionType == FunctionType.SECOND_DERIVATIVE) {
            //second derivative
            coeff = new Polynomial(gui, second.substring(second.indexOf('=') + 1)).coefficients;
            deg = new Polynomial(gui, second.substring(second.indexOf('=') + 1)).degrees;
        } else {
            //Third Derivative
            coeff = new Polynomial(gui, third.substring(third.indexOf('=') + 1)).coefficients;
            deg = new Polynomial(gui, third.substring(third.indexOf('=') + 1)).degrees;
        }
        double y = 0;
        for (int i = 0; i < coeff.length; i++) {
            y += coeff[i] * Math.pow(x, deg[i]);
        }
        return y;
    }

    public String getFirstDerivative() {
        String derivative = polynomial.substring(0, polynomial.indexOf("=") + 1);
        for (int i = 0; i < degrees.length; i++) {
            if (coefficients[i] * degrees[i] != 0) {
                if (coefficients[i] * degrees[i] > 0 && i != 0) {
                    derivative += "+";
                }
                if (degrees[i] != 1) {
                    derivative += (coefficients[i] * degrees[i]) + "x";
                    if (degrees[i] != 2) {
                        derivative += "^" + (degrees[i] - 1);
                    }
                } else {
                    derivative += coefficients[i];
                }
            }
        }
        if (derivative.equals(polynomial.substring(0, polynomial.indexOf("=") + 1))) {
            derivative += "0";
        }
        return derivative;
    }

    public String getSecondDerivative() {
        return (new Polynomial(gui, this.getFirstDerivative())).getFirstDerivative();
    }
}
