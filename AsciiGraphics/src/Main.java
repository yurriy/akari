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
        int width = 100;
        Jpeg2AsciiConverter converter = new Jpeg2AsciiConverter();
        String[] res = converter.convert(img, width);
        for (String row : res) {
            for (char c : row.toCharArray()) {
                System.out.print(c);
                System.out.print(' ');
            }
            System.out.println("");
        }
    }
}
