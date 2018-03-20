import com.sun.tools.javac.util.ArrayUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/*
 * Linear classifier for digits classification
 */
public class LinClassifier {
    private final int DIGITS = 10;
    private int inputSize;
    private float[][] weights;
    private float[] adjustment;

    LinClassifier(String weightsFile) {
        try {
            Scanner scanner = new Scanner(new FileReader(weightsFile));
            inputSize = scanner.nextInt();
            weights = new float[DIGITS][inputSize];
            for (int i = 0; i < DIGITS; i++) {
                for (int j = 0; j < inputSize; j++) {
                    weights[i][j] = scanner.nextFloat();
                }
            }
            adjustment = new float[DIGITS];
            for (int i = 0; i < DIGITS; i++) {
                adjustment[i] = scanner.nextFloat();
            }
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public int predict(float[] input) {
        if (input.length != inputSize) {
            throw new RuntimeException("Input size must be equal to " + inputSize);
        }
        ArrayList<Double> probabilities = new ArrayList<>();
        for (int i = 0; i < DIGITS; i++) {
            double value = 0;
            for (int j = 0; j < inputSize; j++) {
                value += input[j] * weights[i][j];
            }
            value += adjustment[i];
            probabilities.add(value);
        }
        double max = Collections.max(probabilities);
        double expSum = 0;
        for (double x : probabilities) {
            expSum += Math.exp(x - max);
        }
        for (int i = 0; i < DIGITS; i++) {
            probabilities.set(i, Math.exp(probabilities.get(i) - max) / expSum);
        }
        return probabilities.indexOf(Collections.max(probabilities));
    }
}
