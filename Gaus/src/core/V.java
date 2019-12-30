package core;

import no.uib.cipr.matrix.AbstractVector;
import no.uib.cipr.matrix.Vector;

public class V extends AbstractVector {
    protected V(int size) {
        super(size);
    }

    protected V(Vector x) {
        super(x);
    }
}
