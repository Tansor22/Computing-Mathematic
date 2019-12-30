package core;

import java.util.concurrent.atomic.AtomicInteger;

public class LeftRectMethod implements IntegrationTool {

    @Override
    public double integrate(IntegralFunction f, double a, double b, double h) {
        double output = 0, x;
        for (x = a; x < b; x += h)  output = (output + f.function(x));
        output *= h;
        return output;
    }

    @Override
    public double integrateAuto(IntegralFunction f, double a, double b, double eps, AtomicInteger amount) {
        double output1 = integrate(f, a, b, b - a);
        double output2 = integrate(f, a, b, (b - a) / 2);
        amount.incrementAndGet();
        if (Math.abs(output1 - output2) < eps * (b - a) / (b - a)) return output2;
        else return integrateAuto(f, a, (a + b) / 2, eps, amount) + integrateAuto(f, (a + b) / 2, b, eps, amount);
    }
}
