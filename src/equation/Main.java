package equation;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by James on 7/8/2017.
 */
public class Main {

    private static Random random = new Random(System.currentTimeMillis());

    private static int initPopulation = 6;
    private static int candidateSize = 29;
    private static float keep = (float) 0.5;
    private static float mutateRate = (float) 0.2;
    private static int limit = 10;

    public static void main(String[] args) {

        ArrayList<Equation> candidates = new ArrayList<>();

        // 1. Initialize Population
        Equation candidate;
        for (int i = 0; i < initPopulation; i++) {
            candidate = generateRandomCandidate();
            candidates.add(candidate);
        }

        displayCandidates(candidates);

        // 2. Evaluate and 3. Select
        Collections.sort(candidates);
        System.out.println("--------------------------------");
        System.out.println("Initialized Population....");

        displayCandidates(candidates);

        System.out.println("----------------------------------");

        int count = 0;

        while (count < limit) {

            //4. Crossover
            ArrayList<Equation> newCandidates = crossOver(candidates);

            // 5. Mutate
            mutate(newCandidates);
            candidates = newCandidates;

            // 2. Evaluate and 3. Select
            Collections.sort(candidates);

            displayCandidates(candidates);
            System.out.println("Generation: " + count);
            System.out.println("Highest fitness: " + candidates.get(0).evaluate());
            double sum = 0.0;
            for (Equation equation : candidates) {
                sum += equation.evaluate();
            }
            System.out.println("Average fitness: " + sum / candidateSize);
            System.out.println("----------------------");

            count++;
        }

    }

    private static void displayCandidates(ArrayList<Equation> candidates) {
        for (Equation candidate : candidates) {
            System.out.println(candidate + "\t\t->\t\t" + candidate.evaluate());
        }
    }

    private static Equation generateRandomCandidate() {

        String equation = "";

        equation += generateComponent();
        equation += generateOperator();
        equation += generateComponent();
        equation += generateNumber();
        equation += generateOperator();
        equation += generateComponent();
        equation += generateNumber();
        equation += generateOperator();

        int g = random.nextInt(3);

        switch (g) {
            case 0: equation += "s"; break;
            case 1: equation += "c"; break;
            case 2: equation += "t"; break;
        }

        equation += generateComponent();

        return new Equation(equation);
    }

    private static String generateComponent() {
        String component = "";

        component += generateNumber();

        int number = random.nextInt(3);

        switch (number) {
            case 0: component += "x"; break;
            case 1: component += "y"; break;
            case 2: component += "z"; break;
        }

        component += generateNumber(); // Random number 0-9

        return component;
    }

    private static String generateOperator() {

        int operator = random.nextInt(4);

        switch (operator) {
            case 0: return "*";
            case 1: return "/";
            case 2: return "+";
            default: return "-";
        }


    }

    private static String generateNumber() {
        String number = "";

        boolean positiveOrNegative = random.nextBoolean();

        if (positiveOrNegative) {
            number += "p";
        } else {
            number += "n";
        }

        number += random.nextInt(10); // Random number 0-9

        return number;
    }

    private static ArrayList<Equation> crossOver(ArrayList<Equation> samplesIn) {

        ArrayList<Equation> samplesOut = new ArrayList<>();

        for (int i = 0; i < samplesIn.size() / 2; i++) {
            Equation candidateOne = samplesIn.get(random.nextInt(samplesIn.size() / 2))       ;
            Equation candidateTwo = samplesIn.get(random.nextInt(samplesIn.size() / 2));

            String newCandidateOneString;
            String newCandidateTwoString;

            int cutPoint = random.nextInt(candidateOne.getEquation().length() - 1) + 1;

            newCandidateOneString = candidateOne.getEquation().substring(0, cutPoint) +
                    candidateTwo.getEquation().substring(cutPoint, candidateTwo.getEquation().length());
            newCandidateTwoString = candidateTwo.getEquation().substring(0, cutPoint) +
                    candidateOne.getEquation().substring(cutPoint, candidateOne.getEquation().length());

            samplesOut.add(new Equation(newCandidateOneString));
            samplesOut.add(new Equation(newCandidateTwoString));
        }

        return samplesOut;
    }

    private static void mutate(ArrayList<Equation> samplesIn) {


        int[] positiveOrNegativeIndices = {0, 3, 6, 9, 11, 14, 17, 20, 24, 27};
        ArrayList positiveOrNegative = new ArrayList<>(Arrays.asList(positiveOrNegativeIndices));

        int[] numberIndices = {1, 4, 7, 10, 12, 15, 18, 21, 25, 28};
        ArrayList numbers = new ArrayList<>(Arrays.asList(numberIndices));

        int[] xyzIndices = {2, 8, 16, 26};
        ArrayList xyz = new ArrayList<>(Arrays.asList(xyzIndices));

        int[] operatorIndices = {5, 13, 19, 22};
        ArrayList operators = new ArrayList<>(Arrays.asList(operatorIndices));

        for (int i = 0; i < samplesIn.size(); i++) {
            if (random.nextInt(100) / (float) 100 < mutateRate) {
                int index = random.nextInt(candidateSize - 1);
                String prevEquation = samplesIn.get(i).getEquation();
                String newEquation;
                if (positiveOrNegative.contains(index)) {

                    if (samplesIn.get(i).getEquation().charAt(index) == 'p') {
                        newEquation = prevEquation.substring(0, index - 1) + "n" +
                                prevEquation.substring(index + 1, prevEquation.length());
                    }
                    else {
                        newEquation = prevEquation.substring(0, index - 1) + "p" +
                                prevEquation.substring(index + 1, prevEquation.length());
                    }

                    samplesIn.get(i).setEquation(newEquation);
                } else if (numbers.contains(index)) {
                    int newNumber = random.nextInt(10);
                    newEquation = prevEquation.substring(0, index - 1) + newNumber +
                            prevEquation.substring(index + 1, prevEquation.length());
                    samplesIn.get(i).setEquation(newEquation);
                } else if (xyz.contains(index)) {
                    int number = random.nextInt(3);
                    switch (number) {
                        case 0:
                            newEquation = prevEquation.substring(0, index - 1) + "x" +
                                prevEquation.substring(index + 1, prevEquation.length());
                            break;
                        case 1:
                            newEquation = prevEquation.substring(0, index - 1) + "y" +
                                    prevEquation.substring(index + 1, prevEquation.length());
                            break;
                        case 2:
                            newEquation = prevEquation.substring(0, index - 1) + "z" +
                                    prevEquation.substring(index + 1, prevEquation.length());
                            break;
                        default: newEquation = prevEquation; break;
                    }

                    samplesIn.get(i).setEquation(newEquation);
                } else if (operators.contains(index)) {
                    String operator = generateOperator();
                    newEquation = prevEquation.substring(0, index) + operator +
                            prevEquation.substring(index + 1, prevEquation.length());
                    samplesIn.get(i).setEquation(newEquation);
                } else if (index == 23) {
                    int g = random.nextInt(3);

                    switch (g) {
                        case 0:
                            newEquation = prevEquation.substring(0, index - 1) + "s" +
                                    prevEquation.substring(index + 1, prevEquation.length());
                            break;
                        case 1:
                            newEquation = prevEquation.substring(0, index - 1) + "c" +
                                    prevEquation.substring(index + 1, prevEquation.length());
                            break;
                        case 2:
                            newEquation = prevEquation.substring(0, index - 1) + "t" +
                                    prevEquation.substring(index + 1, prevEquation.length());
                            break;
                        default: newEquation = prevEquation; break;
                    }

                    samplesIn.get(i).setEquation(newEquation);
                }
            }
            if (samplesIn.get(i).getEquation().length() == 30) {
                System.out.println("This should not be printing");
            }
        }
    }
}
