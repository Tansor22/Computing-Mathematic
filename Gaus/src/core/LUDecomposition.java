package core;

public class LUDecomposition {
    private Matrix matrix;
    private Vector b;
    private Matrix LU;

    public LUDecomposition(Matrix matrix, Vector b) {
        this.matrix = matrix;
        this.b = b;
        this.LU = matrix.cloneMatrix();
    }

    public void solve() {
        for (int k = 0; k < matrix.size; k++) {
            // L
            for (int i = 0; i < matrix.size; i++) {
                if (i >= k)
                    LU.data[i][k] = l(i, k, LU.data, LU.data);
            }
            // U
            for (int j = 0; j < matrix.size; j++) {
                if (k < j)
                    LU.data[k][j] = u(k, j, LU.data, LU.data);
            }
        }
        System.out.println("Двумерный массив хранящий LU-разложение: ");
        LU.print();
        System.out.println("L-матрица: ");
        LU.getLower().print();
        System.out.println("U-матрица: ");
        LU.getUpper().print();
        System.out.println("Вектор y (L*x): ");
        Vector y = findSolution(LU.getLower(), b, -323);
        y.print();
        System.out.println("Вектор x (U*y): ");
        findSolution(LU.getUpper(), y, 323 ).print();
    }

    private double l(int i, int j, double[][] l, double[][] u) {
        double sum = 0.0;
        for (int s = 0; s < j; s++) {
            sum += l[i][s] * u[s][j];
        }
        return matrix.data[i][j] - sum;
    }

    private double u(int i, int j, double[][] l, double[][] u) {
        double sum = 0.0;
        for (int s = 0; s < i; s++) {
            sum += l[i][s] * u[s][j];
        }
        return (matrix.data[i][j] - sum) / l[i][i];
    }

    private Vector findSolution(Matrix m, Vector b, int dir) {
        int n = m.size;
        Vector output = new Vector(n);
        int j, i;
        if (dir > 0) {
            // up
            // first lower el
            output.data[n - 1] = b.data[n - 1] / m.data[n - 1][n - 1];
            for (i = n - 2; i >= 0; i--) {
                j = n - 1;
                double sum = 0.0;
                while (j >= i) {
                    sum += m.data[i][j] * output.data[j];
                    j--;
                }
                output.data[i] = (b.data[i] - sum) / m.data[i][i];
            }
        } else {
            // down
            output.data[0] = b.data[0] / m.data[0][0];
            for (i = 1; i < n; i++) {
                j = 0;
                double sum = 0.0;
                while (j <= i) {
                    sum += m.data[i][j] * output.data[j];
                    j++;
                }
                output.data[i] = (b.data[i] - sum) / m.data[i][i];

            }
        }
        return output;
    }
}
