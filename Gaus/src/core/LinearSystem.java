package core;

import exception.SolveTimeException;

public abstract class LinearSystem {
    protected Matrix matrix;
    protected boolean isSolved = false;
    protected Vector x;
    protected Vector b;
    protected int size;
    public abstract void solve();
    public LinearSystem(Matrix matrix, Vector b) {
        if(matrix.getSize() != b.data.length) {
            throw new SolveTimeException("Размерность матрицы не совпадает с длиной вектора!");
        }
        this.size = matrix.getSize();
        this.matrix = matrix;
        this.x = new Vector(this.size);
        for (int i = 0; i < size; i++) {
            x.data[i] = i;
        }
        this.b = b;
    }
    protected void swapRows(int i, int j) {
        matrix.swapRows(i, j);
        double buf = b.data[i];
        b.data[i] = b.data[j];
        b.data[j] = buf;
    }
    protected void swapColumns(int i, int j) {
        matrix.swapColumns(i, j);
        double buf = x.data[i];
        x.data[i] = x.data[j];
        x.data[j] = buf;
    }
    public Vector getX() {
        /*Vector output;
        output = (isSolved) ? x : null;*/
        return x;
    }
}
