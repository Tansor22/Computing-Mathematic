package core;

import interfaces.Logeable;

public class Pair implements Logeable {
    public final double EIGENVALUE;
    public final Vector VECTOR;

    public Pair(double eigenvalue, Vector vector) {
        EIGENVALUE = eigenvalue;
        VECTOR = vector;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("\r\n");
        output.append("Собственное число: ").append(EIGENVALUE).append("\n");
        output.append("Собственный вектор: ").append(VECTOR).append("\n");
        return output.toString();
    }

    @Override
    public String log() {
       return toString();
    }
}
