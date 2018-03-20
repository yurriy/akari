public class ImageCutter {
    static float[][] cut(float[][] source, int x, int y, int w, int h) {
        float[][] result = new float[h][w];
        for (int i = 0; i < h; i++) {
            System.arraycopy(source[y + i], x, result[i], 0, h);
        }
        return result;
    }
}
