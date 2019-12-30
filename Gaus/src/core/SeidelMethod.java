package core;

import exception.SolveTimeException;

import java.util.ArrayList;

public class SeidelMethod extends JacobiMethod{
    public SeidelMethod(Matrix matrix, Vector b) {
        super(matrix, b);
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
                    sum += matrix.data[i][j] * x.data[j];
                }
                x.data[i] = (b.data[i] - sum) / matrix.data[i][i];
            }
            vectors.add(x.cloneVector());
            Vector deducted = x.deductVectors(oldX, x);
            if (deducted.getNorm() < eps) {
                //Достигнута заданная точность
                isSolved = true;
                return ;
            }
        }
        isSolved = true;
        throw new SolveTimeException("Заданная точность " + eps + " не достигнута за " + k + " итераций(ю)");
    }
}
