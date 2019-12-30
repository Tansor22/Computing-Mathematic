package core;

import java.util.concurrent.atomic.AtomicInteger;

public class TrapeziumMethod implements IntegrationTool {

    @Override
    public double integrate(IntegralFunction f, double a, double b, double h) {
        double x, output = (f.function(a) + f.function(b)) / 2;
        for (x = (a + h); x < b; x += h) {
            output += f.function(x);
        }
        output *= h;
        return output;
    }

    @Override
    public double integrateAuto(IntegralFunction f, double a, double b, double eps, AtomicInteger amount) {
        double output1 = integrate(f, a, b, b - a), output2 = integrate(f, a, b, (b - a) / 2);
        amount.incrementAndGet();
        if (Math.abs(output1 - output2) / 3 < eps * (b - a) / (b - a)) return output2;
        else return integrateAuto(f, a, (a + b) / 2, eps, amount) + integrateAuto(f, (a + b) / 2, b, eps, amount);
    }
}
