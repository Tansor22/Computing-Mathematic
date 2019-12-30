package core;

import exception.SolveTimeException;

import java.util.ArrayList;

public class RelaxationMethod extends JacobiMethod {
    public int solvedFor;
    public double w = 1.5;
    public RelaxationMethod(Matrix matrix, Vector b) {
        super(matrix, b);
    }

    @Override
    public void solve() {
        // check diagonally dominant

        //Начальное приближение
        for (int i = 0; i < size; i++) {
            x.data[i] = 1 + (20 - 1) * Math.random()/*b.data[i] / matrix.data[i][i]*/;
        }

        // Initialize
        Vector prevX;
        Vector nextX;
        int z;
        int n = matrix.size;
        double[] d = new double[n];
        vectors = new ArrayList<>();
        vectors.add(x.cloneVector());
        vectors.add(Vector.empty(n));

        for (z = 1; z < k - 1; z++) {
            prevX = vectors.get(z - 1);
            nextX = vectors.get(z);
            // main
            nextC(prevX.data, nextX.data, n, matrix.data, d, b.data, w);
            vectors.add(nextX.cloneVector());
            // stop condition
            Vector deducted = nextX.deductVectors(prevX, nextX);
            double norm = deducted.getNorm();
            if (norm < eps) {
                //Достигнута заданная точность
                isSolved = true;
                solvedFor = z;
                return;
            }
        }
        isSolved = true;
        solvedFor = k;
        //throw new SolveTimeException("Заданная точность " + eps + " не достигнута за " + k + " итераций(ю)");
    }

    /**
     * My own relaxation method implementation.
     *
     * @param prevX  Previous X vector.
     * @param nextX  Next X vector.
     * @param n      Dimension.
     * @param matrix The A matrix.
     * @param d      The d vector.
     * @param f      The b vector.
     * @param w      Relaxation coefficient.
     */
    private void nextC(double[] prevX, double[] nextX, int n, double[][] matrix, double[] d, double[] f, double w) {
        int i, j;
        double s1, s2;
        double[][] c = new double[n][n];
        for (i = 0; i < n; i++) {
            s1 = 0;
            s2 = 0;
            for (j = 0; j < i; j++) {
                c[i][j] = -matrix[i][j] * w / matrix[i][i];
                s1 += c[i][j] * nextX[j];
            }
            for (j = i + 1; j < n; j++) {
                c[i][j] = -matrix[i][j] * w / matrix[i][i];
                s2 = s2 + c[i][j] * prevX[j];
            }
            d[i] = f[i] * w / matrix[i][i];
            nextX[i] = s1 + s2 + d[i] - prevX[i] * (w - 1);
        }
    }
}
