import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 0 || args[0].equals("--help") || args[0].equals("-h")) {
            System.out.println("Single mode arguments: FILE");
            System.out.println("Parallel mode arguments: FILE --parallel COUNT");
            System.out.println("In parallel mode image must be a table with COUNT x COUNT digits");
            return;
        }

        BufferedImage source = ImageIO.read(new File(args[0]));
        Jpeg2GrayScaleConverter converter = new Jpeg2GrayScaleConverter();
        LinClassifier classifier = new LinClassifier("data/weights.txt");
        
        int imgSize = 28; // size required by classifier
        if (args.length != 3 || !args[1].equals("--parallel")) {
            // jpeg to ASCII example, single digit classification
            float[][] image = converter.convert(source, imgSize);
            GrayScaleAsciiPrinter.print(image);
            System.out.println(classifier.predict(image));
        } else {
            // parallel classification
            int imagesCount = Integer.valueOf(args[2]);
            float[][] image = converter.convert(source, imagesCount * imgSize);
            int[][] ans = new int[imagesCount][imagesCount];
            ArrayList<Thread> workers = new ArrayList<>();
            for (int i = 0; i < imagesCount; i++) {
                for (int j = 0; j < imagesCount; j++) {
                    final int x = j, y = i;
                    Thread worker = new Thread(() -> {
                        float[][] chunk = ImageCutter.cut(image, x * imgSize, y * imgSize, imgSize, imgSize);
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
