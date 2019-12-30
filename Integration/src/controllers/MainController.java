package controllers;

import core.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import utils.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class MainController extends Controller {
    @FXML
    private VBox rootBox;
    @FXML
    private ToolBar toolBar;
    private String integral;
    private double eps;
    private double step;
    private int a;
    private int b;


    private boolean getParams() {
        //Получение данных
        TextField field = (TextField) toolBar.lookup("#stepsNumberField");
        String text = field.getText();
        if (!Helper.isInteger(text)) return false;
        int numSteps = Integer.parseInt(text);

        field = (TextField) toolBar.lookup("#epsField");
        text = field.getText();
        if (!Helper.isDouble(text)) return false;
        eps = Double.parseDouble(text);

        field = (TextField) toolBar.lookup("#aField");
        text = field.getText();
        if (!Helper.isInteger(text)) return false;
        a = Integer.parseInt(text);

        field = (TextField) toolBar.lookup("#bField");
        text = field.getText();
        if (!Helper.isInteger(text)) return false;
        b = Integer.parseInt(text);
        step = ((double) (b - a) / numSteps);

        field = (TextField) toolBar.lookup("#integralField");
        text = field.getText();
        integral = text;
        return true;
    }

    private Task<Void> setupTask(IntegrationTool tool, IntegralFunction f) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Font font = new Font("Timew New Roman", 20);
                Label nameMethodLabel = new Label(tool.getClass().getSimpleName());
                Label constLabel = new Label("Фиксированный шаг = " + tool.integrate(f, a, b, step));
                constLabel.setFont(font);
                AtomicInteger amount = new AtomicInteger(0);
                Label autoLabel = new Label("Автоматический шаг = " + tool.integrateAuto(f, a, b, eps, amount));
                autoLabel.setFont(font);
                Label amountLabel = new Label("Число шагов =  " + amount);
                amountLabel.setFont(font);
                VBox box = new VBox();
                box.getChildren().addAll(constLabel, autoLabel, amountLabel);
                TitledPane pane = new TitledPane();
                pane.setText(nameMethodLabel.getText());
                pane.setContent(box);
                rootBox.getChildren().add(pane);
                return null;
            }
        };
    }

    private List<Task<Void>> setupTools(IntegralFunction f) {
        List<Task<Void>> output = new ArrayList<>(4);
        output.add(setupTask(new LeftRectMethod(), f));
        //output.add(setupTask(new RightRectMethod(), f));
        output.add(setupTask(new MiddleRectMethod(), f));
        output.add(setupTask(new TrapeziumMethod(), f));
        output.add(setupTask(new SimpsonMethod(), f));
        return output;
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
        //Программирование кнопки
        Button button = (Button) toolBar.lookup("#calculateButton");
        button.setOnAction(event -> {
            if (getParams()) {
                final Expression e;
                try {
                    e = new ExpressionBuilder(integral).variable("x").build();
                } catch (IllegalArgumentException ex) {
                    Helper.showAlert("Ошибка в записи подынтегральной функции!");
                    return ;
                }
                IntegralFunction f = (x) -> {
                    e.setVariable("x", x);
                    return e.evaluate();
                };
                List<Task<Void>> tasks = setupTools(f);
                rootBox.getChildren().clear();
                for (Task task : tasks) {
                    Platform.runLater(task);
                }
            } else {
                Helper.showAlert("Не все поля заполнены корректно!");
            }
        });

    }
}
