import java.awt.*;
import java.awt.image.BufferedImage;

class Jpeg2AsciiConverter {
    Jpeg2AsciiConverter() {
        this(0.2989f, 0.5866f, 0.1145f);
    }

    Jpeg2AsciiConverter(float redWeight, float greenWeight, float blueWeight) {
        this(redWeight, greenWeight, blueWeight, null);
    }

    Jpeg2AsciiConverter(float redWeight, float greenWeight, float blueWeight, char[] letters) {
        this.redWeight = redWeight;
        this.greenWeight = greenWeight;
        this.blueWeight = blueWeight;
        if (letters != null) {
            this.letters = letters;
        }
    }
    
    private float redWeight, greenWeight, blueWeight;
    private char[] letters = "    ...',;:clodxkO0KXNWM".toCharArray();
    
    private BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage resultImage = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resultImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return resultImage;
    }

    String[] convert(BufferedImage image) {
        String[] result = new String[image.getHeight()];
        for (int i = 0; i < image.getHeight(); i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < image.getWidth(); j++) {
                Color c = new Color(image.getRGB(j, i));
                int grayScale = rgbToGrayScale(c);
                row.append(GrayScaleToChar(grayScale));
            }
            result[i] = row.toString();
        }
        return result;
    }

    String[] convert(BufferedImage img, int resultWidth) {
        int resultHeight = (int) Math.ceil((double) img.getHeight() / img.getWidth() * resultWidth);
        BufferedImage resizedImage = resize(img, resultWidth, resultHeight);
        return convert(resizedImage);
    }

    private char GrayScaleToChar(int gray) {
        if (gray > 255 || gray < 0) {
            throw new RuntimeException("color is out of bounds: " + gray);
        }
        return letters[letters.length - 1 - (int) ((double) gray / 255 * (letters.length - 1))];
    }

    private  int rgbToGrayScale(Color c) {
        return (int) (redWeight * c.getRed() + greenWeight * c.getGreen() + blueWeight * c.getBlue());
    }
}
