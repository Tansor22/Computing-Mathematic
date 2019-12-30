package controllers;

public class Go {
    private double a;
    private double b;
    private double h;
    private double Eps;

    public Go(double a, double b, double Eps, double h) {
        this.a = a;
        this.b = b;
        this.Eps = Eps;
        this.h = h;
    }

    double f(double x) {
        return Math.sin(x) / x;
    }

    double Pr1(double x) {
        double pr1 = (f(x + h) - f(x - h)) / (2 * h); // центральная
        return pr1;
    }

    double Pr2(double x) {
        double pr2 = (f(x + h) - 2 * f(x) + f(x - h)) / (h * h);
        return pr2;
    }

    public  void go() {
        int i = 0;
        do {
            System.out.println("Iteration Number : " + i);
            System.out.println("a = " + a);

            if (f(a) * Pr2(f(a)) < 0) {
                System.out.println("f(a) * f''(f(a)) < 0, computing a...");
                a = a - f(a) * ((a - b)) / (f(a) - f(b));
                System.out.println("Result:");

                System.out.println("a = " + a);
            } else if (f(a) * Pr2(f(a)) > 0) {
                System.out.println("f(a) * f''(f(a)) > 0, computing a...");

                a = a - (f(a) / Pr1(a));
                System.out.println("Result:");

                System.out.println("a = " + a);
            }

            ////////////////////////////

            System.out.println("b = " + b);

            if (f(b) * Pr2(f(b)) < 0) {
                System.out.println("f(b) * f''(f(b)) < 0, computing b...");

                b = b - f(b) * ((b - a)) / (f(b) - f(a));
                System.out.println("Result:");

                System.out.println("b = " + b);
            } else if (f(b) * Pr2(f(b)) > 0) {
                System.out.println("f(b) * f''(f(b)) > 0, computing b...");

                b = b - (f(b) / Pr1(b));
                System.out.println("Result:");

                System.out.println("b = " + b);
            }


            System.out.println("=============");


            i++;

        } while (Math.abs(a - b) > 2 * Eps);

        System.out.println("Precision is reached!");

        System.out.println("x = (a+b)/2\r\n");
        double answer = (a + b) / 2;

        System.out.println("x = " + answer);

    }
}

