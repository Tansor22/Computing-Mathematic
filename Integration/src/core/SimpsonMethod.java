package core;

import java.util.concurrent.atomic.AtomicInteger;

public class SimpsonMethod implements IntegrationTool {

    @Override
    public double integrate(IntegralFunction f, double a, double b, double h) {
        double x, output = 0;
        for (x = a; x < b; x += h)
            output += f.function(x) + 4 * f.function(x + h / 2) + f.function(x + h);
        output *= h / 6;
        return output;
    }

    @Override
    public double integrateAuto(IntegralFunction f, double a, double b, double eps, AtomicInteger amount) {
        double output1 = integrate(f, a, b, b - a), output2 = integrate(f, a, b, (b - a) / 2);
        amount.incrementAndGet();
        if (Math.abs(output1 - output2) < eps * (b - a) / (b - a)) return output2;
        else return integrateAuto(f, a, (a + b) / 2, eps, amount) + integrateAuto(f, (a + b) / 2, b, eps, amount);
    }
}
