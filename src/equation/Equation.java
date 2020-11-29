package equation;

import java.util.Stack;

/**
 * Evaluates equation using genetic algorithm
 */
public class Equation implements Comparable<Equation> {

//    String equation = "p7xn2*n1yp4n2/n9zp4+n2-sn7xp4";

    private final double x = -7.62; // x value to be substituted into equation
    private final double y = 11.43; // y value to be substituted into equation
    private final double z = 1.06;  // z value to be substituted into equation

    private String equation;

    public Equation(String equation) {
        this.equation = equation;
    }

    public String getEquation() {
        return equation;
    }

    public void setEquation(String equation) {
        this.equation = equation;
    }

    @Override
    public String toString() {
        return equation;
    }

    @Override
    public int compareTo(Equation o) {

        if (o.evaluate() == this.evaluate()) {
            return 0;
        } else if (this.evaluate() < o.evaluate()) {
            return 1;
        } else {
            return -1;
        }

    }

    public double evaluate() {
        double result;

        String[] parts = equation.split("[*/+-]+"); // split String by the operators
        String[] operators = equation.split("[a-z0-9]+"); // split String by numbers and letters

        result = evaluate(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            switch (operators[i]) {
                case "*" : result *= evaluate(parts[i]); break;
                case "/" : result /= evaluate(parts[i]); break;
                case "+" : result += evaluate(parts[i]); break;
                case "-" : result -= evaluate(parts[i]); break;
            }

            if (i == 1) {
                double power = parts[i].charAt(parts[i].length() - 1);
                if (parts[i].charAt(parts[i].length() - 2) != 'p') {
                    power *= -1;
                }

                result = Math.pow(result, power);
            }
        }

        if (result == Double.POSITIVE_INFINITY) {
            result = Double.MAX_VALUE;
        } else if (result == Double.NEGATIVE_INFINITY) {
            result = Double.MIN_VALUE;
        } else if (result == Double.NaN) {
            result = 0.0;
        }

        return result;
    }

    private double evaluate(String component) {

        double variable = 0.0;

        try {
            if (component.length() == 5 || component.length() == 7) {
                switch (component.charAt(2)) {
                    case 'x': variable = Double.parseDouble(String.valueOf(component.charAt(1))) * x; break;
                    case 'y': variable = Double.parseDouble(String.valueOf(component.charAt(1))) * y; break;
                    case 'z': variable = Double.parseDouble(String.valueOf(component.charAt(1))) * z; break;
                }

                if (component.charAt(0) != 'p') {
                    variable *= -1;
                }

                double power = Double.parseDouble(String.valueOf(component.charAt(4)));

                if (component.charAt(3) != 'p') {
                    power *= -1;
                }

                return Math.pow(variable, power);
            } else if (component.length() == 2) {
                variable = Double.parseDouble(String.valueOf(component.charAt(1)));
                if (component.charAt(0) != 'p') {
                    variable *= -1;
                }
                return Math.pow(Math.E, variable);
            } else if (component.length() == 6) {
                switch (component.charAt(0)) {
                    case 's': return Math.sin(evaluate(component.substring(1, component.length() - 1)));
                    case 'c': return Math.cos(evaluate(component.substring(1, component.length() - 1)));
                    case 't': return Math.tan(evaluate(component.substring(1, component.length() - 1)));
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("DEBUG" + component);
        }

        return 0.0;
    }
}

