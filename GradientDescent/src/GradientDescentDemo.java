public class GradientDescentDemo {
    private final int MAX_LENGTH = 1_000;

    public static void main(String[] args) {
        GradientDescentDemo gdd = new GradientDescentDemo();
        gdd.go(1, 1, 1e-3);
    }

    private double go(int bx, int by, double eps) {
        double[] x = new double[MAX_LENGTH];
        double[] y = new double[MAX_LENGTH];
        double[] alpha = new double[MAX_LENGTH];
        int k;

        //Начальное приближение u[0]
        x[0] = bx;
        y[0] = by;

        System.out.println("Начальное приближение: x(0), (" + x[0] + "," + y[0] + ")");

        for (k = 0; ; k++) {
            //Находим alpha_k как минимум функции g на отрезке -10000,100000
            alpha[k] = dichotomy(-1_000, 1_000, eps, x[k], y[k]);
            //Вычисляем u[k]
            x[k + 1] = x[k] - alpha[k] * f_dx(x[k], y[k]);
            y[k + 1] = y[k] - alpha[k] * f_dy(x[k], y[k]);

            // debug
            double xk = x[k + 1];
            double yk = y[k + 1];
            double f = f(xk, yk);

            System.out.println(String
                    .format("x(%1$d): (%2$.3f,%3$.3f) \n f(%2$.3f,%3$.3f) = %4$.3f\n", k + 1, xk, yk, f));

            if (k > 1) {
                //Проверяем условие остановки
                if (norm(x[k + 1] - x[k], y[k + 1] - y[k]) < eps) {
                    break;
                }
            }
        }
        System.out.println(String
                .format("Точка минимума (epsilon = %.3f)\n f(%.3f,%.3f) = %.3f\n", eps, x[k + 1], y[k + 1], f(x[k + 1], y[k + 1])));

        return f(x[k + 1], y[k + 1]);

    }

    private double dichotomy(int a, int b, double eps, double x, double y) {
        //Номер шага
        int k;
        //Отклонени от середины отрезка влево, вправо
        double lk, mk;
        //Величина на которую мы отклонимся от середины отрезка
        double delta = 0.5 * eps;
        //Точка минимума
        double x_;
        //Отрезок локализации минимума
        double ak = a, bk = b;
        k = 1;
        //Пока длина отрезка больше заданной точности
        do {
            lk = (ak + bk - delta) / 2;
            mk = (ak + bk + delta) / 2;

            k++;
            //Проверяем в какую часть попадает точка минимума слева от разбиения или справа и выбираем соответствующую точку
            if (g(x, y, lk) <= g(x, y, mk)) {
                //Теперь правая граница отрезка локализации равна mk
                bk = mk;
            } else {
                //Теперь левая граница отрезка локализации равна mk
                ak = lk;
            }
        } while ((bk - ak) >= eps);
        x_ = (ak + bk) / 2; //minimum point

        return x_;
    }

    //Это функция g d методе наискорейшего (градиентного) спуска
    private double g(double x, double y, double alpha) {
        return f(x - alpha * f_dx(x, y), y - alpha * f_dy(x, y));
    }

    //Собственно здесь записывается наша функция
    double f(double x, double y) {
        return x * x + 2 * x * y + 3 * y * y - 2 * x - 3 * y;
    }

    //Это первая производная по dx
    double f_dx(double x, double y) {
        return 2 * x + 2 * y - 2;
    }

    //Это первая производная по dy
    double f_dy(double x, double y) {
        return 2 * x + 6 * y - 3;
    }

    //двумерная норма
    double norm(double x, double y) {
        return Math.sqrt(x * x + y * y);
    }

}
