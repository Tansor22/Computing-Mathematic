package core;

import java.util.concurrent.atomic.AtomicInteger;

public interface IntegrationTool {
    double integrate(IntegralFunction f, double a, double b, double h);

    double integrateAuto(IntegralFunction f, double a, double b, double eps, AtomicInteger amount);
}
