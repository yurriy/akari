import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new RuntimeException("file name must be passed as a first argument");
        }

        BufferedImage source = null;
        try {
            source = ImageIO.read(new File(args[0]));
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
        
        Jpeg2GrayScaleConverter converter = new Jpeg2GrayScaleConverter();
        LinClassifier classifier = new LinClassifier("weights.txt");
        
        int imageSize = 28;
        if (args.length == 1 || !args[1].equals("--parallel")) {
            // jpeg to ASCII example, single digit classification
            float[][] image = converter.convert(source, imageSize);
            GrayScaleAsciiPrinter.print(image);
            System.out.println(classifier.predict(image));
        } else {
            // parallel classification
            int imagesCount = 10;
            float[][] image = converter.convert(source, imagesCount * imageSize);
            int[][] ans = new int[imagesCount][imagesCount];
            ArrayList<Thread> workers = new ArrayList<>();
            for (int i = 0; i < imagesCount; i++) {
                for (int j = 0; j < imagesCount; j++) {
                    final int x = j, y = i;
                    Thread worker = new Thread(() -> {
                        float[][] chunk = ImageCutter.cut(image, x * imageSize, y * imageSize, imageSize, imageSize);
                        int prediction = classifier.predict(chunk);
                        synchronized(classifier) {
                            ans[y][x] = prediction;
                        }
                    });
                    worker.start();
                    workers.add(worker);
                }
            }
            for (Thread worker : workers) {
                try {
                    worker.join();
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            for (int i = 0; i < imagesCount; i++) {
                for (int j = 0; j < imagesCount; j++) {
                    System.out.print(ans[i][j] + " ");
                }
                System.out.print('\n');
            }
        }
     }
}
