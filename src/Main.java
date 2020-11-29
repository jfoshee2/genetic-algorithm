import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Main {

    private static class Sample implements Comparable<Sample> {
        int value;
        String binaryCode;
        int eval;

        @Override
        public int compareTo(Sample o) {
            if (this.value < o.value) {
                return 1;
            } else if (this.value == o.value) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    private static class Fitness {
        float avgFitness;
        Sample max;
    }

    private static Random random = new Random(System.currentTimeMillis()); // Seed random number generator

    private static int initPopulation = 6;
    private static int candidateSize = 5;
    private static float keep = (float) 0.5;
    private static float mutateRate = (float) 0.3;
    private static int limit = 10;

    public static void main(String[] args) {

        ArrayList<Sample> candidates = new ArrayList<>();

        // 1. Initialize Population
        for (int i = 0; i < initPopulation; i++) {
            Sample sample = new Sample();
            sample.binaryCode = "";
            for (int j = 0; j < candidateSize; j++) {
                sample.binaryCode += random.nextInt(2);
            }
            sample.value = convertBinaryToDecimal(sample.binaryCode);
            sample.eval = (int) Math.pow(sample.value, 2);
            candidates.add(sample);
        }

        displayCandidates(candidates);

        System.out.println("Initialized Population....");
        // 2. Evaluate and 3. Select
        Collections.sort(candidates);
        System.out.println();
        displayCandidates(candidates);
        System.out.println("------------------------------");
        int count = 0;
        while (count < limit) {

            // 4. Crossover
            ArrayList<Sample> newSamples = crossOver(candidates);

            // 5. Mutate
            mutate(newSamples);

            candidates = newSamples;
            // 2. Evaluate and 3. Select
            Collections.sort(candidates);

            displayCandidates(candidates);
            System.out.println("----------------------");
            System.out.println("Generation: " + count);

            ++count;
        }

        System.out.println();
        displayCandidates(candidates);

    }

    private static String convertToBinary(int x) {

        // Base case
        if (x == 1) {
            return "1";
        } else if (x == 0) {
            return "0";
        }
        if (x % 2 == 0) {
            x /= 2;
            return convertToBinary(x) + "0";
        } else {
            x /= 2;
            return convertToBinary(x) + "1";
        }
    }

    private static int convertBinaryToDecimal(String binary) {

        int result = 0;

        int x = 1;

        for (int i = binary.length() - 1; i >= 0; i--) {
            if (binary.charAt(i) == '1') {
                result += x;
            }

            x *= 2;
        }

        return result;
    }

    private static void displayCandidates(ArrayList<Sample> arrayList) {
        for (Sample sample: arrayList) {
            System.out.println(sample.value + " -> " + sample.binaryCode + " -> " + sample.eval);
        }
    }

    private static ArrayList<Sample> crossOver(ArrayList<Sample> samplesIn) {

        ArrayList<Sample> samplesOut = new ArrayList<>();

        for (int i = 0; i < samplesIn.size() / 2; i++) {
            Sample candidateOne = samplesIn.get(random.nextInt(samplesIn.size() / 2));
            Sample candidateTwo = samplesIn.get(random.nextInt(samplesIn.size() / 2));

            Sample newCandidateOne = new Sample();
            Sample newCandidateTwo = new Sample();

            newCandidateOne.value = -1;
            newCandidateOne.eval = -1;
            newCandidateTwo.value = -1;
            newCandidateTwo.eval = -1;

            newCandidateOne.binaryCode = "";
            newCandidateTwo.binaryCode = "";


            int cutPoint = random.nextInt((candidateSize - 1) + 1); // (rand() % (candidateSize - 1)) + 1;

            for (int j = 0; j < candidateSize; j++) {
                if (j < cutPoint) {
                    newCandidateOne.binaryCode += candidateOne.binaryCode.charAt(j);
                    newCandidateTwo.binaryCode += candidateTwo.binaryCode.charAt(j);
                } else {
                    newCandidateOne.binaryCode += candidateTwo.binaryCode.charAt(j);
                    newCandidateTwo.binaryCode += candidateOne.binaryCode.charAt(j);
                }
            }

            newCandidateOne.value = convertBinaryToDecimal(newCandidateOne.binaryCode);
            newCandidateTwo.value = convertBinaryToDecimal(newCandidateTwo.binaryCode);

            newCandidateOne.eval = (int) Math.pow(newCandidateOne.value, 2);
            newCandidateTwo.eval = (int) Math.pow(newCandidateTwo.value, 2);

            samplesOut.add(newCandidateOne);
            samplesOut.add(newCandidateTwo);
        }

        return samplesOut;
    }

    private static void mutate(ArrayList<Sample> samplesIn) {

        for (int i = 0; i < samplesIn.size(); i++) {
            // Mutate?
            if ((float) random.nextInt(101) / 100 < mutateRate) {
                int index = random.nextInt(candidateSize);
                if (samplesIn.get(i).binaryCode.charAt(index) == '0') {
                    String replacement = samplesIn.get(i).binaryCode
                            .replace(samplesIn.get(i).binaryCode.charAt(index), '1');
                    samplesIn.get(i).binaryCode = replacement;
                    samplesIn.get(i).value = convertBinaryToDecimal(replacement);
                    samplesIn.get(i).eval = (int) Math.pow(samplesIn.get(i).value, 2);
                } else {
                    String replacement = samplesIn.get(i).binaryCode
                            .replace(samplesIn.get(i).binaryCode.charAt(index), '0');
                    samplesIn.get(i).binaryCode = replacement;
                    samplesIn.get(i).value = convertBinaryToDecimal(replacement);
                    samplesIn.get(i).eval = (int) Math.pow(samplesIn.get(i).value, 2);
                }
            }
        }
    }

}
