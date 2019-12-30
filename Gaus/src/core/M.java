package core;

import no.uib.cipr.matrix.AbstractMatrix;
import no.uib.cipr.matrix.Matrix;

public class M extends AbstractMatrix {
    protected M(int numRows, int numColumns) {
        super(numRows, numColumns);
    }

    protected M(Matrix A) {
        super(A);
    }
}
