import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new RuntimeException("file name must be passed as a first argument");
        }
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(args[0]));
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
        // jpeg to ASCII example
        Jpeg2GrayScaleConverter converter = new Jpeg2GrayScaleConverter();
        GrayScaleAsciiPrinter.print(converter.convert(img, 100));
        // classification example
        Jpeg2GrayScaleConverter converter2 = new Jpeg2GrayScaleConverter();
        GrayScaleAsciiPrinter.print(converter2.convert(img, 28));
     }
}
