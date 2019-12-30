package controllers;


import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import utils.*;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainController extends Controller {
    @FXML
    private BorderPane rootBox;
    @FXML
    private TextArea out;
    @FXML
    private ToolBar toolBar;
    private String func;
    private Expression e;
    private double eps;
    private double lags;
    private double begin;
    private double end;


    //Разностная форма
    private double differenceForm(double x, double dx) {
        return (func(x + (dx / 2)) -
                func(x - (dx / 2))) / dx;
    }

    private double func(double x) {
        return e.setVariable("x", x).evaluate();
    }

    private double def(double x, int n) {
        //Шаг
        double dx = 0.05;

        if (n == 1)
            return differenceForm(x, dx);
        double first = x, second = x;
        while (n-- > 1) {
            first = differenceForm(first + (dx / 2.0), dx);
            second = differenceForm(second - dx / 2.0, dx);
        }
        return (first - second) / dx;
    }

    private double[] find(double a, double b, double n) {
        double h = (b - a) / n;
        List<Double> semiOutput = new ArrayList<>();
        for (double i = a; i < b; i += h) {
            if (Math.abs(func(i)) <= eps) {
                semiOutput.add(i);
                continue;
            }
            if ((func(i) * func(i + h) < 0)) {//если функция монотонна на отрезке и имеет разные знаки
                double A, B;
                if (func(i) * def(i, 2) > 0) {// по второй производной определяем концы отрезка
                    A = i;
                    B = i + h;
                } else {
                    A = i + h;
                    B = i;
                }
                while (Math.abs(A - B) > eps) {
                    double nextA = A - (func(A) / def(A, 1));
                    double nextB = B - (((A - B) / (func(A) - func(B))) * func(B));
                    A = nextA;
                    B = nextB;
                }
                semiOutput.add(A);
            }
        }
        return semiOutput.stream().mapToDouble(Double::doubleValue).toArray();
    }

    private boolean getParams() {
        //Получение данных
        TextField field = (TextField) toolBar.lookup("#lagField");
        String text = field.getText();
        if (!Helper.isDouble(text)) return false;
        lags = Double.valueOf(text);

        field = (TextField) toolBar.lookup("#epsField");
        text = field.getText();
        if (!Helper.isDouble(text)) return false;
        eps = Double.parseDouble(text);

        field = (TextField) toolBar.lookup("#beginField");
        text = field.getText();
        if (!Helper.isDouble(text)) return false;
        begin = Double.valueOf(text);

        field = (TextField) toolBar.lookup("#endField");
        text = field.getText();
        if (!Helper.isDouble(text)) return false;
        end = Double.valueOf(text);

        field = (TextField) toolBar.lookup("#funcField");
        text = field.getText();
        if (text.isEmpty()) return false;
        func = text;
        e = new ExpressionBuilder(func).variable("x").build();
        return true;
    }

    @Override
    public void beforeShow() {
        //Подсказка
        final Label helpLabel = (Label) toolBar.lookup("#helpLabel");
        final Tooltip helpTooltip = new Tooltip("Правила записи подынтегральной функции:\n" +
                "Сложение: 2 + 2\n" +
                "Вычитание: 2 - 2\n" +
                "Умножение: 2 * 2\n" +
                "Деление: 2 / 2\n" +
                "Возведение в степень: 2 ^ 2\n" +
                "Унарные минус и плюс: +2 - (-2)\n" +
                "Остаток от деления: 2 % 2\n" +
                "Встроенные функции: \n" +
                "abs: Абсолютное значение\n" +
                "acos: Арккосинус\n" +
                "asin: Арксинус\n" +
                "atan: Арктангенс\n" +
                "cbrt: Кубический корень\n" +
                "ceil: Ближайшее целое сверху\n" +
                "cos: Косинус\n" +
                "cosh: Гиперболический косинус\n" +
                "exp: Экспонента (e^x)\n" +
                "floor: Ближайшее целое снизу\n" +
                "log: Натуральный логарифм (основание e)\n" +
                "log10: Логариф (основание 10)\n" +
                "log2: Логарифм (основание 2)\n" +
                "sin: Синус\n" +
                "sinh: Гиперболический синус\n" +
                "sqrt: квадратный корень\n" +
                "tan: Тангенс\n" +
                "tanh: Гиперболический тангенс\n" +
                "signum: Сигнум");
        helpLabel.setOnMouseMoved(event -> helpTooltip.show(helpLabel, event.getScreenX(), event.getScreenY() + 15));
        helpLabel.setOnMouseExited(event -> helpTooltip.hide());

        // Установка системного потока вывода на TextArea
        try {
            System.setOut(new PrintStream(new Console(out), true, "Cp866"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Программирование кнопки
        Button button = (Button) toolBar.lookup("#calculateButton");
        button.setOnAction(event -> {
            if (getParams()) {
                System.out.println("Функция - " + func);
                for (double value : find(begin, end, lags))
                    System.out.println(value);
            } else {
                Helper.showAlert("Не все поля заполнены корректно!");
            }
        });
    }
}
