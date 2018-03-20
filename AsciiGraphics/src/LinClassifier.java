import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/*
 * Linear classifier for digits classification
 */
class LinClassifier {
    private final int CLASSES_CNT = 10;
    private int inputSize;
    private float[][] weights;
    private float[] adjustment;

    LinClassifier(String weightsFile) {
        try {
            Scanner scanner = new Scanner(new FileReader(weightsFile));
            inputSize = scanner.nextInt();
            weights = new float[CLASSES_CNT][inputSize];
            for (int i = 0; i < CLASSES_CNT; i++) {
                for (int j = 0; j < inputSize; j++) {
                    weights[i][j] = scanner.nextFloat();
                }
            }
            adjustment = new float[CLASSES_CNT];
            for (int i = 0; i < CLASSES_CNT; i++) {
                adjustment[i] = scanner.nextFloat();
            }
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    int predict(float[][] input) {
        if (input.length == 0 || input[0] == null || input.length * input[0].length != inputSize) {
            throw new RuntimeException("Flattened array size must be equal to " + inputSize);
        }
        float[] flattened = new float[inputSize];
        int i = 0;
        for (float[] row : input) {
            for (float x : row) {
                flattened[i] = x;
                i++;
            }
        }
        return predict(flattened);
    }

    private int predict(float[] input) {
        ArrayList<Double> probabilities = new ArrayList<>();
        for (int i = 0; i < CLASSES_CNT; i++) {
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
        for (int i = 0; i < CLASSES_CNT; i++) {
            probabilities.set(i, Math.exp(probabilities.get(i) - max) / expSum);
        }
        return probabilities.indexOf(Collections.max(probabilities));
    }
}
