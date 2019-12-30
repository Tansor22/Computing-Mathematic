package core;

public class EigenvaluesProblem {
    private Matrix matrix;
    private Vector x;
    private double max;
    private double min;
    private Pair first;
    private Pair second;
    public final double EPS = Double.valueOf("1E-04");

    public EigenvaluesProblem(Matrix matrix, Vector x) {
        this.matrix = matrix;
        this.x = x;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public Pair getFirst() {
        return first;
    }

    public Pair getSecond() {
        return second;
    }

    public void solve() {
        Matrix transpMatrix = matrix.cloneMatrix().transpose();
        first = matrix.findEigenvalue(x, EPS);
        Matrix deducted = matrix.deduct(first.EIGENVALUE);
        double buf = deducted.findEigenvalue(x, EPS).EIGENVALUE;
        min = (first.EIGENVALUE < buf + first.EIGENVALUE) ? first.EIGENVALUE : buf + first.EIGENVALUE;
        max = (first.EIGENVALUE > buf + first.EIGENVALUE) ? first.EIGENVALUE : buf + first.EIGENVALUE;
        //TODO: собственное число не равно первому
        Vector g  = transpMatrix.findEigenvalue(x, EPS).VECTOR;
        Matrix d = first.VECTOR.multiplication(g);
        Matrix c = matrix.deduct(d.multiply(first.EIGENVALUE / Vector.dotProduct(first.VECTOR, g)));
        second = c.findEigenvalue(x, EPS);
    }
}
