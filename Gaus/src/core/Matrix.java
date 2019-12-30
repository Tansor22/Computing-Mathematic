package core;

import exception.SolveTimeException;
import interfaces.Logeable;

import java.io.*;
import java.util.ArrayList;
import java.util.Locale;

public class Matrix implements Logeable {
    public double[][] data;
    protected int size;
    protected double det;
    protected int sumMoves = 0;

    private String format = "%.2f";

    private Matrix() {

    }

    public Matrix(double[][] data) {
        for (int i = 0; i < data.length; i++) {
            if (data[i].length != data.length) {
                throw new SolveTimeException("Матрица не квадратная!");
            }
        }
        this.size = data.length;
        this.data = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.data[i][j] = data[i][j];
            }
        }
    }

    public Matrix mult(Matrix m) {
        double[][] data = new double[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    data[i][j] += this.data[i][k] * m.data[k][j];
                }
            }
            return new Matrix(data);
    }

    public static Matrix getMatrix(int size) {
        return getMatrix(size, 0);
    }

    public static Matrix getMatrix(int size, int fillWith) {
        double[][] data = new double[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                data[i][j] = fillWith;
        return new Matrix(data);
    }

    public Matrix deduct(Matrix matrix) {
        Matrix output = this.cloneMatrix();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                output.data[i][j] -= matrix.data[i][j];
            }
        }
        return output;
    }

    public Matrix multiply(double num) {
        Matrix output = this.cloneMatrix();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                output.data[i][j] *= num;
            }
        }
        return output;
    }

    public Matrix deduct(double num) {
        Matrix output = this.cloneMatrix();
        for (int i = 0; i < size; i++) {
            output.data[i][i] -= num;
        }
        return output;
    }

    public Matrix transpose() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                data[j][i] = data[i][j];
            }
        }
        return this;
    }

    public Pair findEigenvalue(Vector oldX, double eps) {
        Vector nextX = oldX.multyplayMatrix(this);
        double oldEigenvalue = nextX.dotProduct(oldX) / oldX.dotProduct(oldX);
        double eigenvalue = 0.0;
        int i = 0;
        while (Math.abs(eigenvalue - oldEigenvalue) > eps) {
            oldEigenvalue = eigenvalue;
            oldX = nextX.cloneVector();
            nextX = nextX.multyplayMatrix(this);
            eigenvalue = nextX.dotProduct(oldX) / oldX.dotProduct(oldX);
            i++;
            if (i % 5 == 0) {
                nextX.normilize();
            }
        }
        if (i % 5 != 0) {
            nextX.normilize();
        }
        return new Pair(eigenvalue, nextX);
    }

    public int getSize() {
        return size;
    }

    public double getDet() {
        return det;
    }

    public void setData(double[][] data) {
        this.size = data.length;
        this.data = data;
    }
    public Vector mult(Matrix m, Vector v) {
        double[] data = new double[m.size];
        for (int i = 0; i < m.size; i++) {
            for (int j = 0; j < v.size; j++) {
                data[i] += m.data[i][j] * v.data[j];
            }
        }
        return new Vector(data);
    }

    public static Matrix makeMatrix(String[] src) {
        int size = src.length;
        String[] buf;
        double[][] data = new double[size][size];
        for (int i = 0; i < size; i++) {
            buf = src[i].split(" ");
            if (buf.length != size) {
                throw new SolveTimeException("Матрица не квадратная!");
            }
            for (int j = 0; j < buf.length; j++) {
                data[i][j] = Double.valueOf(buf[j]);
            }
        }
        return new Matrix(data);
    }

    public Matrix cloneMatrix() {
        Matrix output = new Matrix();
        output.data = data.clone();
        for (int i = 0; i < output.data.length; i++) {
            output.data[i] = data[i].clone();
        }
        output.size = size;
        return output;
    }

    public double getAt(int i, int j) {
        return data[i][j];
    }

    public void setAt(int i, int j, double value) {
        data[i][j] = value;
    }

    public void swapRows(int i, int j) {
        double[] buf = data[i];
        data[i] = data[j];
        data[j] = buf;
        sumMoves++;
    }

    public void swapColumns(int i, int j) {
        for (int k = 0; k < size; k++) {
            double buf = data[k][i];
            data[k][i] = data[k][j];
            data[k][j] = buf;
        }
        sumMoves++;
    }

    public static String[] loadSrcFromFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            ArrayList<String> lines = new ArrayList<>();
            String buf;
            while ((buf = reader.readLine()) != null) {
                lines.add(buf);
            }
            return lines.toArray(new String[0]);
        } catch (IOException e) {
            throw new SolveTimeException("Ошибка чтения файла: " + e.getMessage());
        }
    }

    public static Matrix getSingleMatrix(int size) {
        double[][] semiOutput = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    semiOutput[i][j] = 1;
                } else {
                    semiOutput[i][j] = 0;
                }
            }
        }
        return new Matrix(semiOutput);
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("\r\n");
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                output.append(String.format(Locale.ROOT, format, data[i][j]));
                output.append(" ");
            }
            output.append("\r\n");
        }
        output.append("----------------------------------------");
        output.append("\r\n");
        return output.toString();
    }

    @Override
    public String log() {
        return this.toString();
    }

    public void print() {
        System.out.println(this.toString());
    }

    public Matrix getLower() {
        double[][] data = new double[size][size];
        for (int i = 0; i < this.data.length; i++)
            for (int j = 0; j < this.data.length; j++) {
                data[i][j] = (i >= j) ? this.data[i][j] : 0;
            }
        return new Matrix(data);
    }

    public Matrix getUpper() {
        double[][] data = new double[size][size];
        for (int i = 0; i < this.data.length; i++)
            for (int j = 0; j < this.data.length; j++) {
                if (i == j) {
                    data[i][j] = 1.0;
                    continue;
                }
                data[i][j] = (i < j) ? this.data[i][j] : 0;
            }
        return new Matrix(data);
    }
}
