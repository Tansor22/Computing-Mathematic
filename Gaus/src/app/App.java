package app;

import core.*;
import core.Vector;
import exception.SolveTimeException;
import interfaces.Logeable;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.io.*;
import java.util.*;

public class App {
    private static File logFile;
    private static final int MAX_NUM = 10;
    private static String logsDirectory = "." + File.separator + "logs";

    static {
        logFile = new File(logsDirectory + File.separator + "log_" + new Date().getTime() + ".log");
    }

    public static void main(String[] args) {
        try {
            int n = 50;
            Matrix matrix = generateMatrix(n)/*Matrix.makeMatrix(Matrix.loadSrcFromFile(new File(args[0])))*/;
           /* matrix = new Matrix(new double[][]{
                    {2, -2, 0},
                    {4, 1, 3},
                    {0, 3, 1}
            });*/
            Matrix transp = matrix.transpose();
            Vector ch = new Vector(new double[]{
                    1, 2, -1
            });
            //Vector mult = matrix.mult(matrix, ch);
            //mult.print();
            matrix.print();
            double[] data = new double[n];
            for (int i = 0; i < n; i++) {
                data[i] = matrix.data[0][i];
            }
            matrix.print();

            Vector b = /*new Vector(Vector.loadSrcFromFile(new File(args[1])))*/generateVector(matrix.getSize());
            b = new Vector(data);
            b.print();
            MinMisclosuresMethod rm = new MinMisclosuresMethod(matrix, b);
            rm.setEps(2);
            rm.solve();
            /*rm.getVectors().forEach(Vector::print);*/
           /* List<Double> W = new ArrayList<>();
            List<Integer> k = new ArrayList<>();
            for (double w = 0.8; w < 1.8; w += 0.05) {
                rm.w = w;
                W.add(w);
                k.add(rm.solvedFor);
                rm.solve();
                System.out.println("K = " + rm.solvedFor);
                System.out.println("W = " + rm.w + '\n');
            }
            XYChart chart = QuickChart.getChart("Sample Chart", "W", "k", "y(x)", W, k);
            new SwingWrapper(chart).displayChart();*/
            /*
            LABA 1 MAGA
            int n = 200;
            Matrix matrix = generateMatrix(n)*//*Matrix.makeMatrix(Matrix.loadSrcFromFile(new File(args[0])))*//*;
            Vector x = new Vector(Vector.loadSrcFromFile(new File(args[1])))*//*generateVector(matrix.getSize())*//*;
            double[] vec = new double[n];
            for (int i = 0; i < n; i++) {
                vec[i] = matrix.data[i][0] + matrix.data[i][1];
            }
            x = new Vector(vec);
            LUDecomposition system = new LUDecomposition(matrix, x);
            system.solve();*/
/*
            EigenvaluesProblem system = new EigenvaluesProblem(matrix, x);
            system.solve();
            log("Точность: " + system.EPS + "\n");
            log("Минимальное собственное число: " + system.getMin() + "\n");
            log("Максимальное собственное число: " + system.getMax() + "\n");
            log("Первая пара: " + system.getFirst() + "\n");
            log("Вторая пара: " + system.getSecond() + "\n");
*/
        } catch (SolveTimeException ex) {
            System.out.println(ex);
        }
    }

    public static void log(Logeable object) {
        try {
            if (object == null) {
                return;
            }
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            FileWriter writer = new FileWriter(logFile, true);
            writer.write(object.log());
            writer.close();
        } catch (IOException e) {
            throw new SolveTimeException("Ошибка логирования: " + e.getMessage());
        }
    }

    public static void log(String mes) {
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            FileWriter writer = new FileWriter(logFile, true);
            writer.write(mes);
            writer.close();
        } catch (IOException e) {
            throw new SolveTimeException("Ошибка логирования: " + e.getMessage());
        }
    }

    public static void log(List list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            FileWriter writer = new FileWriter(logFile, true);
            for (Object object : list) {
                writer.write(((Logeable) object).log());
            }
            writer.close();
        } catch (IOException e) {
            throw new SolveTimeException("Ошибка логирования: " + e.getMessage());
        }

    }

    public static void printMatrix(Matrix matrix) {
        System.out.println(matrix);

    }

    public static double[] loadVecrot(File file) {
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

    private static Vector generateVector(int size) {
        double[] output = new double[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            output[i] = 1 + (MAX_NUM - 1) * rand.nextDouble();
        }
        return new Vector(output);
    }

    public static Matrix generateMatrix(int size) {
        Random rand = new Random();
        double[][] buff = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //От 1 до MAX_NUM
                buff[i][j] = 1 + (MAX_NUM - 1) * rand.nextDouble();
            }
        }
        return new Matrix(buff);
    }
}

