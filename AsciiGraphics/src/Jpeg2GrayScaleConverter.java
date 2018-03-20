import java.awt.*;
import java.awt.image.BufferedImage;

class Jpeg2GrayScaleConverter {
    private float redWeight, greenWeight, blueWeight;

    Jpeg2GrayScaleConverter() {
        this(0.2989f, 0.5866f, 0.1145f);
    }

    Jpeg2GrayScaleConverter(float redWeight, float greenWeight, float blueWeight) {
        this.redWeight = redWeight;
        this.greenWeight = greenWeight;
        this.blueWeight = blueWeight;
    }

    private BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage resultImage = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resultImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return resultImage;
    }

    float[][] convert(BufferedImage image) {
        float[][] result = new float[image.getHeight()][image.getWidth()];
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                Color c = new Color(image.getRGB(j, i));
                result[i][j] = rgbToGrayScale(c);
            }
        }
        return result;
    }

    float[][] convert(BufferedImage img, int resultWidth) {
        int resultHeight = (int) Math.ceil((double) img.getHeight() / img.getWidth() * resultWidth);
        BufferedImage resizedImage = resize(img, resultWidth, resultHeight);
        return convert(resizedImage);
    }

    private float rgbToGrayScale(Color c) {
        return Math.max(0, Math.min(1, (redWeight * c.getRed() + greenWeight * c.getGreen() + blueWeight * c.getBlue()) / 255));
    }
}
