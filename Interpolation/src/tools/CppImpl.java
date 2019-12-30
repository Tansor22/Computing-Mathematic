package tools;

import net.objecthunter.exp4j.ExpressionBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CppImpl implements InterpolationTool {
    int n;
    double pi = Math.PI;
    double[] b, a;
    String function;

    double A(int j) {
        double S = 0;
        int ii;
        for (int i = -n; i < n + 1; i++) {

            S = S + Function(2 * pi * (double) i / (2 * n + 1)) * Math.cos(2 * pi * (double) j * (double) i / (2 * n + 1));
        }
        if (j == 0) return 1 / (double) (2 * n + 1) * S;

        return 2 / (double) (2 * n + 1) * S;
    }

    double B(int j) {
        int ii;
        double S = 0;
        for (int i = -n; i < n + 1; i++) {
            ii = -n + i;
            S = S + Function(2 * pi * (double) i / (double) (2 * n + 1)) * Math.sin(2 * pi * (double) (j * i) / (double) (2 * n + 1));
        }
        return 2 / (double) (2 * n + 1) * S;
    }

    double Function(double x) {
        if (function == null)
            return x* x;//Math.sin(x);
        else return new ExpressionBuilder(function).variables("x").build().setVariable("x", x).evaluate();

        //

        /*if (x < -1.5) return 1.0;
        if (x >= -1.5 && x < 1.6) return 0.5;
        return -0.333;*/
    }

    double Interpolate(double x) {
        double S = a[0];
        for (int i = 1; i < n; i++) {
            S = S + a[i] * Math.cos((double) i * x) + b[i] * Math.sin((double) i * x);
        }
        return S;
    }

    @Override
    public double interpolate(double[] arrX, double[] arrY, int length, double x) {
        n = 2 * length; // => true, or /
        //n = length / 2;
        a = new double[n];
        b = new double[n];
        // a
        for (int i = 0; i < n; i++) {
            a[i] = A(i);
            System.out.println("a" + i + " = " + a[i]);
        }
        // b
        for (int i = 1; i < n; i++) {
            b[i] = B(i);
            System.out.println("b" + i + " = " + b[i]);
        }
        double arg;
        List<Double> args = new ArrayList<>();
        List<Double> funcY = new ArrayList<>();
        List<Double> interpolY = new ArrayList<>();

        for (int j = 0; j < length; j++) {
            arg = -pi + 2 * pi / length * j;
            args.add(arg);
            System.out.println("Arg = " + arg);
            System.out.println("Func = " + Function(arg));
            funcY.add(Function(arg));
        }
        for (int j = 0; j < length; j++) {
            arg = -pi + 2 * pi / length * j;
            interpolY.add(Interpolate(arg));
            System.out.println("Interpol = " + Interpolate(arg));
        }
        XYChart chart = new XYChart(300, 300);
        chart.addSeries("f(x)", new int[]{0});
        chart.addSeries("l(x)", new int[]{0});
        chart.getStyler().setMarkerSize(0);
        chart.getStyler().setLegendVisible(false);
        chart.updateXYSeries("f(x)", args, funcY, null);
        chart.updateXYSeries("l(x)", args, interpolY, null);
        new SwingWrapper<>(chart).displayChart("Graphic").setVisible(true);
        String command;
        Scanner sc = new Scanner(System.in);
        System.out.println("Read from " + args.get(0) + " to " + args.get(args.size() - 1));
        while(!(command = sc.nextLine()).equals("end")) {
            System.out.println("Read from " + args.get(0) + " to " + args.get(args.size() - 1));
            double inter = Interpolate(Double.parseDouble(command));
            double func = Function(Double.parseDouble(command));
            System.out.println("Interpol = " + inter);
            System.out.println("Func = " + func);
        }


        return 0;
    }
}
