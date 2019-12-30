package core;

import exception.SolveTimeException;
import interfaces.Logeable;

import java.io.*;
import java.util.Locale;
import java.util.Optional;
import java.util.function.DoubleBinaryOperator;

public class Vector implements Logeable {
    double data[];
    int size;
    private String format = "%.10f";

    public Vector(int size) {
        this.size = size;
        this.data = new double[size];
    }

    public Vector(double[] data) {
        this.data = data;
        size = data.length;
    }

    public int getSize() {
        return size;
    }

    public static Vector empty(int n) {
        return new Vector(new double[n]);
    }

    public double dotProduct(Vector vector) {
        if (this.size != vector.size) return 0;
        double output = 0.0;
        for (int i = 0; i < size; i++) {
            output += data[i] * vector.data[i];
        }
        return output;
    }
    public Vector minus(Vector a, Vector b) {
        double[] data = new double[size];
        for (int i = 0; i < size; i++) {
            data[i] = a.data[i] - b.data[i];
        }
        return new Vector(data);
    }
    public Vector treat(Vector a, Vector b, DoubleBinaryOperator op) {
        double[] data = new double[size];
        for (int i = 0; i < size; i++) {
            data[i] = op.applyAsDouble(a.data[i], b.data[i]);
        }
        return new Vector(data);
    }
    public Vector treat(Vector a, double b, DoubleBinaryOperator op) {
        double[] data = new double[size];
        for (int i = 0; i < size; i++) {
            data[i] = op.applyAsDouble(a.data[i], b);
        }
        return new Vector(data);
    }

    public static double dotProduct(Vector vector1, Vector vector2) {
        double output = 0.0;
        for (int i = 0; i < vector1.size; i++) {
            output += vector1.data[i] * vector2.data[i];
        }
        return output;
    }

    public void normilize() {
        double norm = Math.sqrt(this.dotProduct(this));
        for (int i = 0; i < size; i++) {
            data[i] /= norm;
        }
    }

    public Matrix multiplication(Vector row) {
        double[][] semiOutput = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                semiOutput[i][j] = data[i] * row.data[j];
            }
        }
        return new Matrix(semiOutput);
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < size; i++) {
            output.append(String.format(Locale.ROOT, format, data[i]));
            output.append("\r\n");
        }
        return output.toString();
    }

    public void print() {
        System.out.println(this);
    }

    @Override
    public String log() {
        StringBuilder builder = new StringBuilder();
        builder.append("\r\n");
        builder.append(this);
        builder.append("\r\n");
        return builder.toString();
    }
    public double length() {
        double output = 0.0;
        for (int i = 0; i < size; i++) {
            output += data[i] * data[i];
        }
        return Math.sqrt(output);
    }

    public static double[] loadSrcFromFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line = reader.readLine();
            String[] buf = line.split(" ");
            int size = buf.length;
            double[] output = new double[buf.length];
            for (int i = 0; i < size; i++) {
                output[i] = Double.valueOf(buf[i]);
            }
            return output;
        } catch (IOException e) {
            throw new SolveTimeException("Ошибка чтения файла: " + e.getMessage());
        }
    }

    public Vector cloneVector() {
        double[] semiOutput = data.clone();
        return new Vector(semiOutput);
    }

    public Vector multyplayMatrix(Matrix matrix) {
        if (matrix.size != size) {
            throw new SolveTimeException("Различные размерности матрицы и вектора!");
        }
        double[] buf = new double[size];
        for (int i = 0; i < size; i++) {
            double sum = 0;
            for (int j = 0; j < size; j++) {
                sum += matrix.data[i][j] * data[j];
            }
            buf[i] = sum;
        }
        return new Vector(buf);
    }

    public void deductVector(Vector vector) {
        for (int i = 0; i < vector.size; i++) {
            this.data[i] -= vector.data[i];
        }
    }

    public double getNorm() {
        double output = Math.abs(data[0]);
        for (int i = 1; i < size; i++) {
            double next = Math.abs(data[i]);
            if (next > output) {
                output = next;
            }
        }
        return output;
    }

    public Vector deductVectors(Vector vector1, Vector vector2) {
        Vector output = vector2.cloneVector();
        output.deductVector(vector1);
        return output;
    }
}
