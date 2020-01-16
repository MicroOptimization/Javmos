package javmos.enums;

import javmos.JavmosGUI;
import javmos.components.Point;
import javmos.components.functions.Function;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashSet;

public enum RootType {

    X_INTERCEPT(Color.GREEN, "X-Intercept"),
    CRITICAL_POINT(Color.RED, "Critical Point"),
    INFLECTION_POINT(Color.BLUE, "Inflection Point");

    public final int ATTEMPTS = 100;
    public final FunctionType functionOne;
    public final FunctionType functionTwo;
    private final Color rootColor;
    private final String rootName;

    RootType(Color color, String name) {
        this.rootColor = color;
        this.rootName = name;
        if (this.rootName.equals("X-Intercept")) {
            functionOne = FunctionType.ORIGINAL;
            functionTwo = FunctionType.FIRST_DERIVATIVE;
        } else if (this.rootName.equals("Critical Point")) {
            functionOne = FunctionType.FIRST_DERIVATIVE;
            functionTwo = FunctionType.SECOND_DERIVATIVE;
        } else {
            functionOne = FunctionType.SECOND_DERIVATIVE;
            functionTwo = FunctionType.THIRD_DERIVATIVE;
        }
    }

    public Color getRootColor() {
        return this.rootColor;
    }

    public String getRootName() {
        return this.rootName;
    }

    public HashSet<Point> getRoots(JavmosGUI gui, Function function, double minDomain, double maxDomain) {
        HashSet<Point> set = new HashSet<>();
        DecimalFormat df = new DecimalFormat("##.###");

        for (double testX = minDomain; testX < maxDomain; testX += 0.1) {
            Point point;
            Double root = newtonsMethod(function, testX, ATTEMPTS);
            if (root != null) {
                double y = function.getValueAt(root, functionOne);
                if (Math.abs(y) <= 0.01) {
                    point = new Point(gui, this, Double.parseDouble(df.format(root)), Double.parseDouble(df.format(function.getValueAt(root, FunctionType.ORIGINAL))));
                    set.add(point);
                }
            }
        }
        return set;
    }

    public Double newtonsMethod(Function function, double guess, int attempts) {
        try {
            BigDecimal bdMainFunction = BigDecimal.valueOf(function.getValueAt(guess, functionOne));
            BigDecimal bdDerivative = BigDecimal.valueOf(function.getValueAt(guess, functionTwo));
            BigDecimal bdGuess = BigDecimal.valueOf(guess);
            if (bdMainFunction.equals(BigDecimal.valueOf(0)) || attempts <= 0) {
                return guess;
            } else if (bdDerivative.doubleValue() == 0) {
                return newtonsMethod(function, bdGuess.subtract(bdMainFunction).doubleValue(), attempts - 1);
            } else {
                return newtonsMethod(function, bdGuess.subtract(bdMainFunction.divide(bdDerivative, 5, RoundingMode.HALF_UP)).doubleValue(), attempts - 1);
            }
        } catch (Exception e) {
        }
        return null;
    }
}
