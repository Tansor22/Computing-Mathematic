package core;

public class LUDecompositionV2 {
    private Matrix matrix;
    private Vector x;
    private Matrix L;
    private Matrix U;

    public LUDecompositionV2(Matrix matrix, Vector x) {
        this.matrix = matrix;
        this.x = x;
        this.L = Matrix.getMatrix(matrix.size);
        this.U = matrix.cloneMatrix();
    }

    public void solve() {
        int n = matrix.size;
        for (int i = 0; i < n; i++)
            for (int j = i; j < n; j++)
                L.data[j][i] = U.data[j][i] / U.data[i][i];

        for (int k = 1; k < n; k++) {
            for (int i = k - 1; i < n; i++)
                for (int j = i; j < n; j++)
                    L.data[j][i] = U.data[j][i] / U.data[i][i];

            for (int i = k; i < n; i++)
                for (int j = k - 1; j < n; j++)
                    U.data[i][j] = U.data[i][j] - L.data[i][k - 1] * U.data[k - 1][j];
        }
        matrix.print();
        L.print();
        U.print();
    }
}
