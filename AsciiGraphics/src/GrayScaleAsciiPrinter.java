class GrayScaleAsciiPrinter {
    private static char[] letters = "    ...',;:clodxkO0KXNWM".toCharArray();

    private static char grayScaleToChar(float gray) {
        if (gray > 1 || gray < 0) {
            throw new RuntimeException("color is out of bounds: " + gray);
        }
        return letters[(int) ((1 - gray) * (letters.length - 1))];
    }

    static void print(float[][] image) {
        for (float[] row : image) {
            for (float color : row) {
                System.out.print(grayScaleToChar(color));
                System.out.print(' ');
            }
            System.out.print('\n');
        }
    }
}
