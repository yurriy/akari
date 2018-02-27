import java.io.*;

class Color {
    public byte red, green, blue;

    Color(byte red, byte green, byte blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
}

class PpmImage {
    static int MAX_N = (int) 1e6;

    public int width, height;
    public byte data[];

    PpmImage(int width, int height) {
        this.width = width;
        this.height = height;
        this.data = new byte[width * height * 3];
    }

    public PpmImage(InputStream is) throws Exception {
        byte[] streamData = new byte[MAX_N];
        int len = is.read(streamData);
        BufferedReader in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(streamData)));
        assertPpm(in.readLine().equals("P6"));
        String sizeLine = in.readLine();
        String[] tokens = sizeLine.split(" ");
        assertPpm(tokens.length == 2);
        width = Integer.valueOf(tokens[0]);
        height = Integer.valueOf(tokens[1]);
        assertPpm(in.readLine().equals("255"));
        int headerLen = 8 + sizeLine.length();
        int sz = width * height * 3;
        assertPpm(sz == len - headerLen);
        data = new byte[sz];
        System.arraycopy(streamData, headerLen, data, 0, sz);
    }

    private void assertPpm(boolean check) throws Exception {
        if (!check) {
            throw new Exception("incorrect ppm");
        }
    }

    public void write(OutputStream os) throws IOException {
        PrintStream ps = new PrintStream(os);
        ps.println("P6");
        ps.println(String.format("%d %d", width, height));
        ps.println(255);
        ps.write(data);
        ps.close();
    }

    void set(int row, int column, Color color) {
        int offset = row * width * 3 + column * 3;
        data[offset] = color.red;
        data[offset + 1] = color.green;
        data[offset + 2] = color.blue;
    }

    Color get(int row, int column) {
        int offset = row * width * 3 + column * 3;
        return new Color(data[offset], data[offset + 1], data[offset + 2]);
    }
}

public class Main {

    public static void main(String[] args) throws Exception {
        PpmImage input;
        if (args.length >= 1 && !args[0].startsWith("-")) {
            input = new PpmImage(new FileInputStream(args[0]));
        } else {
            input = new PpmImage(System.in);
        }
        PpmImage output = new PpmImage(input.width / 2, input.height / 2);
        OutputStream os = System.out;
        if (args.length >= 2 && !args[1].startsWith("-")) {
            os = new FileOutputStream(args[1]);
        }
        for (int i = 0; i < input.height; i++) {
            for (int j = 0; j < input.width; j++) {
                if (i % 2 == 1 && j % 2 == 1) {
                    Color color = input.get(i, j);
                    output.set(i / 2, j / 2, color);
                }
            }
        }
        output.write(os);
    }

}
