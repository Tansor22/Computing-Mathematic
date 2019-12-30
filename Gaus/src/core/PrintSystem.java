package core;

import java.util.Locale;

public class PrintSystem {
    private Matrix matrix;
    private Vector x;
    private Vector b;
    private String format = "%.2f";

    public PrintSystem(Matrix matrix, Vector x, Vector b) {
        this.matrix = matrix;
        this.x = x;
        this.b = b;
    }
    public void print() {
        StringBuilder output = new StringBuilder("\r\n");
        for (int i = 0; i < matrix.data.length; i++) {
            for (int j = 0; j < matrix.data.length; j++) {
                output.append(String.format(Locale.ROOT, format, matrix.data[i][j]));
                output.append(" ");
            }
            output.append("| " + x.data[i] + " | ");
            if (matrix.size / 2 == i) {
                output.append(" =");
            }
            output.append(b.data[i]);
            output.append("\r\n");
        }
        output.append("----------------------------------------");
        output.append("\r\n");
        System.out.println(output.toString());
    }
}
