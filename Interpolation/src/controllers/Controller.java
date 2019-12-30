package controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import tools.*;


import javax.swing.*;
import java.net.URL;
import java.util.*;
import java.util.Timer;

public class Controller implements Initializable {
    @FXML
    private TextField x0Field;
    @FXML
    private TextField xNField;
    @FXML
    private TextField stepField;
    @FXML
    private TextField xValueField;
    @FXML
    private ListView<PointView> listView;
    @FXML
    private SwingNode swingNode;
    @FXML
    private Pane swingPane;

    private XChartPanel<XYChart> chartPanel;
    private InterpolationTool tool = new LagrangePolynom();
    private final int DEFAULT_X0 = -1;
    private final int DEFAULT_XN = 1;
    private final double DEFAULT_STEP = 0.01;
    private String function = "sin(x)";
    private Scene scene;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        x0Field.setText(Integer.toString(DEFAULT_X0));
        xNField.setText(Integer.toString(DEFAULT_XN));
        stepField.setText(Double.toString(DEFAULT_STEP));
        listView.setEditable(true);
        listView.setCellFactory((list) -> new TextFieldListCell<>(new StringConverter<PointView>() {
            @Override
            public String toString(PointView object) {
                if (object == null) return null;
                return object.toString();
            }

            @Override
            public PointView fromString(String string) {
                if (PointView.isPointView(string)) {
                    return new PointView(string);
                } else {
                    return null;
                }
            }
        }));
        listView.setOnEditCommit(e -> {
            if (e.getNewValue() == null) return;
            if (PointView.isPointView(e.getNewValue().toString())) {
                listView.getItems().set(e.getIndex(), e.getNewValue());
            }
        });
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    private List<String> getDefaultFunctions() {
        List<String> output = new ArrayList<>(19);
        output.add("sin(x)");
        output.add("cos(x)");
        output.add("abs(x)");
        output.add("asin(x)");
        output.add("atan(x)");
        output.add("cbrt(x)");
        output.add("ceil(x)");
        output.add("cosh(x)");
        output.add("exp(x)");
        output.add("floor(x)");
        output.add("log(x)");
        output.add("log10(x)");
        output.add("log2(x)");
        output.add("acos(x)");
        output.add("sinh(x)");
        output.add("sqrt(x)");
        output.add("tan(x)");
        output.add("tanh(x)");
        output.add("signum(x)");
        return output;
    }

    private void setupChart() {
        VBox vbox = (VBox) scene.lookup("#vbox");
        swingPane.setMinSize(scene.getWidth() - vbox.getWidth() - 20d, 530d);
        XYChart chart = new XYChart((int) swingPane.getWidth(), (int) swingPane.getHeight());
        chart.addSeries("f(x)", new int[]{0});
        chart.addSeries("l(x)", new int[]{0});
        chart.getStyler().setMarkerSize(0);
        chart.getStyler().setLegendVisible(false);
        chartPanel = new XChartPanel<>(chart);
        chartPanel.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        swingNode.setContent(chartPanel);
    }

    private void setBittonsDisabled(boolean isDisable) {
        Button button = (Button) scene.lookup("#drawFuncButton");
        button.setDisable(isDisable);
        button = (Button) scene.lookup("#drawPolynomButton");
        button.setDisable(isDisable);
    }

    public void beforeShow() {
        //Конструирование графической компоненты
        setupChart();
        //Программирование боксов
        ChoiceBox<String> funcChoiceBox = (ChoiceBox<String>) scene.lookup("#funcChoice");
        funcChoiceBox.getItems().addAll(getDefaultFunctions());
        funcChoiceBox.getSelectionModel().selectFirst();
        // adding custom function
        funcChoiceBox.getItems().add("Добавить...");
        funcChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("Добавить...")) {
                TextInputDialog tid = new TextInputDialog();
                tid.setTitle("Задайте функцию от x..");
                tid.setHeaderText("Задайте функцию от x..");
                Optional<String> result = tid.showAndWait();
                if (result.isPresent()) {
                    // TODO:: result is valid
                    funcChoiceBox.getItems().remove("Добавить...");
                    funcChoiceBox.getItems().add(result.get());
                    funcChoiceBox.getSelectionModel().selectLast();
                    funcChoiceBox.getItems().add("Добавить...");
                    function = result.get();
                } else {
                    funcChoiceBox.getSelectionModel().selectPrevious();
                }
            } else {
                function = newValue;
            }
        });

        ChoiceBox<String> choiceBox = (ChoiceBox<String>) scene.lookup("#methodChoice");
        choiceBox.getItems().addAll("Полином Лагранджа", "Кубический сплайн", "ДПФ", "БПФ", "CPP");
        choiceBox.getSelectionModel().selectFirst();
        choiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue.intValue()) {
                case 0:
                    tool = new LagrangePolynom();
                    break;
                case 1:
                    tool = new CubicSpline();
                    break;
                case 2:
                    tool = new DiscreteFourierTransform();
                    break;
                case 3:
                    tool = new FastFourierTransform();
                case 4:
                    tool = new CppImpl();
                    break;
            }
        });
        // Программирование кнопок
        setBittonsDisabled(true);
        Button button = (Button) scene.lookup("#fillTableButton");
        button.setOnAction(e -> {
            double x0 = Double.parseDouble(x0Field.getText());
            double xN = Double.parseDouble(xNField.getText());
            double step = Double.parseDouble(stepField.getText());
            ObservableList<PointView> points = FXCollections.observableArrayList();
            for (double x = x0; x <= xN; x += step) {
                double y = new ExpressionBuilder(function).variable("x").build().setVariable("x", x).evaluate();
                points.add(new PointView(x, y));
            }
            listView.getItems().clear();
            listView.setItems(points);
            setBittonsDisabled(false);
        });
        button = (Button) scene.lookup("#drawFuncButton");
        button.setOnAction(e -> {
            List<Double> yData = new ArrayList<>();
            List<Double> xData = new ArrayList<>();
            double x0 = Double.parseDouble(x0Field.getText());
            double xN = Double.parseDouble(xNField.getText());
            double step = Double.parseDouble(stepField.getText());
            for (double x = x0; x <= xN; x += step) {
                double y = new ExpressionBuilder(function).variable("x").build().setVariable("x", x).evaluate();
                yData.add(y);
                xData.add(x);
            }
            TimerTask chartUpdaterTask = new TimerTask() {
                private int length = yData.size();
                private int i = 1;

                public void run() {
                    if (i < length) {
                        chartPanel.getChart().updateXYSeries("f(x)", xData.subList(0, i),
                                yData.subList(0, i), null);
                        i++;
                        SwingUtilities.invokeLater(() -> chartPanel.repaint());
                    } else {
                        cancel();
                    }
                }
            };
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(chartUpdaterTask, 0L, 5L);
        });
        button = (Button) scene.lookup("#clearButton");
        button.setOnAction(e -> {
            setupChart();
            listView.getItems().clear();
            setBittonsDisabled(true);
        });
        button = (Button) scene.lookup("#drawPolynomButton");
        button.setOnAction(e -> {
            ObservableList<PointView> points = listView.getItems();
            int length = points.size();
            double[] X = new double[length];
            double[] Y = new double[length];
            List<Double> yData = new ArrayList<>();
            List<Double> xData = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                PointView point = points.get(i);
                X[i] = point.x;
                Y[i] = point.y;
            }
            for (double x = Double.parseDouble(x0Field.getText()); x <= Double.parseDouble(xNField.getText()); x += Double.parseDouble(stepField.getText())) {
                double y = tool.interpolate(X, Y, length, x);
                yData.add(y);
                xData.add(x);
            }
            TimerTask chartUpdaterTask = new TimerTask() {
                private int length = yData.size();
                private int i = 1;

                public void run() {
                    if (i < length) {
                        chartPanel.getChart().updateXYSeries("l(x)", xData.subList(0, i),
                                yData.subList(0, i), null);
                        i++;
                        SwingUtilities.invokeLater(() -> chartPanel.repaint());
                    } else {
                        cancel();
                    }
                }
            };
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(chartUpdaterTask, 0L, 3L);
        });
        button = (Button) scene.lookup("#calculateButton");
        button.setOnAction(e -> {
            double x = Double.valueOf(xValueField.getText());
            Label label = (Label) scene.lookup("#labelF");
            double y = new ExpressionBuilder(function).variables("x").build().setVariable("x", x).evaluate();
            label.setText("F(x) = " + String.format("%.3f", y));
            ObservableList<PointView> points = listView.getItems();
            int length = points.size();
            double[] X = new double[length];
            double[] Y = new double[length];
            for (int i = 0; i < length; i++) {
                PointView point = points.get(i);
                X[i] = point.x;
                Y[i] = point.y;
            }
            label = (Label) scene.lookup("#labelL");
            label.setText("L(x) = " + tool.interpolate(X, Y, length, x));
        });
    }
}
