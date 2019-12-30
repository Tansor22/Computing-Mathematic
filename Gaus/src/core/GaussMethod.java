package core;

import app.App;
import exception.SolveTimeException;

public class GaussMethod extends LinearSystem{
    private double eps = Double.valueOf("1E-07");
    private Matrix reverse;

    public GaussMethod(Matrix matrix, Vector b) {
        super(matrix, b);
        this.reverse = Matrix.getSingleMatrix(this.size);
    }
    public void setEpsilon(String eps) {
        this.eps = Double.valueOf(eps);
    }
    protected void swapRows(int i, int j) {
        super.swapRows(i, j);
        reverse.swapRows(i, j);
    }
    protected void swapColumns(int i, int j) {
        super.swapColumns(i, j);
        reverse.swapColumns(i, j);
    }
    private void toTriangle() {
        double c;
        int size = matrix.size;
        double[][] data = matrix.data;
        //Semi flag
        int maxK = -1;
        double main;
        for (int k = 0; k < size; k++) {
            main = data[k][k];
            //Searching for max element
            for(int f = k; f < size; f++) {
                if (Math.abs(data[f][k]) > Math.abs(main)) {
                    main = data[f][k];
                    // Index of row with max element
                    maxK = f;
                }
            }
            //If it was found swap rows
            if (maxK > -1) {
               // App.log(matrix);
                swapRows(k, maxK);
               // App.log(matrix);
                maxK = -1;
            }
            if (Math.abs(data[k][k]) < eps) {
                throw new SolveTimeException("Матрица вырождена!");
                //swapColumns();
            }
            for (int i = k + 1; i < size; i++) {
                c = data[i][k] / data[k][k];
                App.log("\r\n Элемент на главной диагонали " + String.valueOf(data[k][k])+ "\r\nС " +String.valueOf(c));
                for (int j = 0; j < size; j++) {
                    data[i][j] -= data[k][j] * c;
                    reverse.data[i][j] -= data[k][j] * c;
                    //App.log(matrix);
                }
             b.data[i] = b.data[i] - b.data[k] * c;
                //App.log(new Vector(b));
            }
        }
        App.log(matrix);
    }
    public void findX() {
        double[][] data = matrix.data;
        //if (wasSwapped)
        x.data[size - 1] = b.data[size - 1] / data[size - 1][size - 1];
        for (int k = size - 2; k >= 0; k--) {
            double sum = 0;
            for (int i = size - 1; i >= k + 1; i--) {
                sum += data[k][i] * x.data[i];
            }
            x.data[k] = (b.data[k] - sum) / data[k][k];
        }
    }
    private void findDet() {
        double[][] data = matrix.data;
        double det = 1;
        for (int i = 0; i < size; i++) {
            det *= data[i][i];
        }
        if (matrix.sumMoves % 2 != 0) {
            det = -det;
        }
        matrix.det = det;
    }
    public Matrix getMatrix() {
        return matrix;
    }
    public Matrix getReverse() {
        Matrix output;
        output = (isSolved) ? reverse : null;
        return output;
    }
    public void solve() {
            toTriangle();
            findX();
            findDet();
            findReverse();
            isSolved = true;
    }
    public void findReverse() {
        double[][] origin = matrix.data/*.clone()*/;
        for (int k = size - 1; k >= 0; k--) {
           for (int i = k - 1; i >= 0; i--) {
               for(int j = 0; j < size ; j++) {
                   //App.log(new Matrix(origin));
                   origin[k][j] *= origin[i][k] / origin[k][k];
                   origin[i][j] -= origin[k][j];

                   reverse.data[k][j] *= origin[i][k] / origin[k][k];
                   reverse.data[i][j]  -= origin[k][j];
               }
           }
            origin[k][k] *= 1 / origin[k][k];
            reverse.data[k][k] *= 1 / origin[k][k];
        }
    }
}
