package core;

import no.uib.cipr.matrix.AbstractMatrix;
import no.uib.cipr.matrix.AbstractVector;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.sparse.*;

import java.util.ArrayList;

public class MinMisclosuresMethod extends JacobiMethod {
    public int solvedFor;
    protected int k = 100_000;

    public MinMisclosuresMethod(Matrix matrix, Vector b) {
        super(matrix, b);
    }

    @Override
    public void solve() {
        //Начальное приближение
        for (int i = 0; i < size; i++) {
            x.data[i] = /*1 + (20 - 1) * Math.random()*/b.data[i] / matrix.data[i][i];
        }
        // Initialize
        Vector prevX;
        Vector nextX;
        int n = matrix.size;
        vectors = new ArrayList<>();
        vectors.add(x.cloneVector());
        vectors.add(Vector.empty(n));
        double bLen = b.length();
        for (int i = 1; i < k - 1; i++) {
            prevX = vectors.get(i - 1);
            Vector ri = b.treat(matrix.mult(matrix, prevX), b, (a, b) -> a - b);
            double tau = Vector.dotProduct(ri, matrix.mult(matrix, ri))
                    / Vector.dotProduct(matrix.mult(matrix, ri), matrix.mult(matrix, ri));
            nextX = prevX.
                    treat(prevX,
                            ri.treat(ri, tau, (r, t) -> r * t), (x, ty) -> x - ty);

            // условие выхода другое
            //System.out.println("ri.length() / bLen = " + ri.length() / bLen);
            if (ri.length() / bLen < eps) {
                //Достигнута заданная точность
                isSolved = true;
                solvedFor = i;
                System.out.println("Answer:  " + i);
                nextX.print();
                return;
            }
            vectors.add(nextX);
        }
    }
}
