package tools;

public class DiscreteFourierTransform implements InterpolationTool {
    @Override
    public double interpolate(double[] arrX, double[] arrY, int length, double x) {
        // ищем дисретные коэффициенты Фурье (Ak) (4)
        ComplexNumber[] Ac = new ComplexNumber[length];
        for (int i = 0; i < length; i++) {
            Ac[i] = new ComplexNumber();
        }
        for (int k = 0; k < length; k++) {
            for (int l = 0; l < length; l++) {
                ComplexNumber c = ComplexNumber.exp(new ComplexNumber(
                        0,
                        -2 * Math.PI * k * arrX[l]
                ));
                Ac[k].add(
                        ComplexNumber.multiply(new ComplexNumber(arrX[l], 0),
                        c));

            }
            Ac[k].divide(new ComplexNumber(length, 0));
        }
        // Приминяем формулу (6)
        double fx = 0.0;
        ComplexNumber result = new ComplexNumber();
        for (int q = 0; q < length; q++) {
            ComplexNumber c = ComplexNumber.exp(new ComplexNumber(
                    0,
                    2 * Math.PI * q * x
            ));
            result.add(ComplexNumber.multiply(Ac[q], c));

        }
        System.out.println(result);
        return result.getRe();

    }

    private double Ak(double[] x, double[] y, int k, int j) {
        double Ak = 0.0;
        for (int i = 0; i < k; i++) {
            Ak += y[i] * Math.cos(j * x[i]);
        }
        Ak /= ((Ak * 2) / k);
        return Ak;
    }

    private double Bk(double[] x, double[] y, int k, int j) {
        double Bk = 0.0;
        for (int i = 0; i < k; i++) {
            Bk += y[i] * Math.sin(j * x[i]);
        }
        Bk /= ((Bk * 2) / k);
        return Bk;
    }

    // (5.24)
    private double an(double[] y, int m, int n) {
        double an = 0.0;
        for (int k = 0; k < m; k++) {
            an += y[k] * Math.cos((n * 2 * Math.PI * k) / m);
        }
        an /= ((an * 2) / m);
        return an;
    }

    // (5.25)
    private double bn(double[] y, int m, int n) {
        double bn = 0.0;
        for (int k = 0; k < m; k++) {
            bn += y[k] * Math.sin((n * 2 * Math.PI * k) / m);
        }
        bn /= ((bn * 2) / m);
        return bn;
    }

    // (5.20)
    private double QN(double[] arrX, double[] arrY, int N, int m, double x) {
        double[] a = new double[N];
        double T = arrX[m - 1] - arrX[0];
        a[0] = an(arrY, m, 0) / 2;
        for (int j = 1; j < N; j++) {
            double cosA = an(arrY, m, j);
            double sinB = bn(arrY, m, j);
            a[j] += cosA * Math.cos((j * x * 2 * Math.PI) / T) + sinB * Math.sin((j * x * 2 * Math.PI) / T);
        }

        return sum(a);
    }

    private void T(double[] arrX, double[] arrY, int length) {
        double[] newY = new double[length];
        for (int i = 0; i < length; i++) {
            newY[i] = Ak(arrX, arrY, length, 0) / 2;
            for (int j = 1; j < length/*M - порядок полинома*/; j++) {
                double cosA = Ak(arrX, arrY, length, j);
                double cosB = Bk(arrX, arrY, length, j);
                newY[i] += cosA * Math.cos(arrX[i] * j) + cosB * Math.sin(arrX[i] * j);
            }
        }
    }

    private double Q(double[] arrX, double[] arrY, int length, double x) {
        double[] a = new double[length];
        for (int i = 0; i < length; i++) {
            a[i] = Ak(arrX, arrY, length, 0) / 2;
            for (int j = 1; j < length/*M - порядок полинома*/; j++) {
                double cosA = Ak(arrX, arrY, length, j);
                double cosB = Bk(arrX, arrY, length, j);
                a[i] += cosA * Math.cos(arrX[i] * x) + cosB * Math.sin(arrX[i] * x * j /*???*/);
            }
        }
        return sum(a);
    }

    private double sum(double[] arr) {
        double output = 0.0;
        for (double number : arr)
            output += number;
        return output;
    }
}
