package core;

import exception.SolveTimeException;

import java.util.ArrayList;

public class JacobiMethod extends LinearSystem {
    protected double eps = Double.valueOf("1E-05");
    protected ArrayList<Vector> vectors;
    protected int k = 1000;

    public JacobiMethod(Matrix matrix, Vector b) {
        super(matrix, b);
    }

    public ArrayList<Vector> getVectors() {
        return vectors;
    }

    public void setEps(int n) {
        eps = Double.valueOf("1E-" + n);
    }

    protected boolean check() {
        for (int k = 0; k < size; k++) {
            double sum = 0.0;
            for (int i = 0; i < size - 1; i++) {
                if (k == i) continue;
                sum += Math.abs(matrix.data[k][i]);
            }
            if (matrix.data[k][k] < sum) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void solve() {
        //Проверка условия сходимости
        if (!check()) {
            throw new SolveTimeException("Не выполнено условие сходимости!");
        }
        //Начальное приближение
        for (int i = 0; i < size; i++) {
            x.data[i] = b.data[i] / matrix.data[i][i];
        }
        vectors = new ArrayList<>();
        vectors.add(x.cloneVector());
        //Решение
        Vector oldX;
        for (int z = 1; z < k; z++) {
            oldX = vectors.get(z - 1);
            for (int i = 0; i < size; i++) {
                double sum = 0.0;
                for (int j = 0; j < size; j++) {
                    if (i == j) continue;
                    sum += matrix.data[i][j] * oldX.data[j];
                }
                x.data[i] = (b.data[i] - sum) / matrix.data[i][i];
            }
            vectors.add(x.cloneVector());
            Vector deducted = x.deductVectors(oldX, x);
            if (deducted.getNorm() < eps) {
                //Достигнута заданная точность
                isSolved = true;
                return;
            }
        }
        isSolved = true;
        throw new SolveTimeException("Заданная точность " + eps + " не достигнута за " + k + " итераций(ю)");
    }
}
