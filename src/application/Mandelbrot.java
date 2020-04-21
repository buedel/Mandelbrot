package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Mandelbrot set with JavaFX.
 * 
 * @author hameister
 */
public class Mandelbrot extends Application {
	// Size of the canvas for the Mandelbrot set
	private static final int CANVAS_WIDTH = 740;
	private static final int CANVAS_HEIGHT = 605;
	// Left and right border
	private static final int X_OFFSET = 25;
	// Top and Bottom border
	private static final int Y_OFFSET = 25;
	// Values for the Mandelbro set
	private static double MANDELBROT_RE_MIN = -2;
	private static double MANDELBROT_RE_MAX = 1;
	private static double MANDELBROT_IM_MIN = -1.2;
	private static double MANDELBROT_IM_MAX = 1.2;

	@Override
	public void start(Stage primaryStage) {
		VBox fractalRootPane = new VBox();
		Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		canvas.setLayoutX(X_OFFSET);
		canvas.setLayoutY(Y_OFFSET);

		paintSet(canvas.getGraphicsContext2D(), MANDELBROT_RE_MIN, MANDELBROT_RE_MAX, MANDELBROT_IM_MIN,
				MANDELBROT_IM_MAX);

		HBox hbox = new HBox();
		Button setButton = new Button("set");
		TextField reMinField = new TextField("reMin");
		TextField reMaxField = new TextField("reMax");
		TextField imMinField = new TextField("imMin");
		TextField imMaxField = new TextField("imMax");

		reMinField.setText("" + MANDELBROT_RE_MIN);
		reMaxField.setText("" + MANDELBROT_RE_MAX);
		imMinField.setText("" + MANDELBROT_IM_MIN);
		imMaxField.setText("" + MANDELBROT_IM_MAX);

		hbox.getChildren().addAll(setButton, reMinField, reMaxField, imMinField, imMaxField);
		fractalRootPane.getChildren().add(canvas);
		fractalRootPane.getChildren().add(hbox);

		setButton.setOnAction(e -> {
			double remin = Double.parseDouble(reMinField.getText());
			double remax = Double.parseDouble(reMaxField.getText());
			double immin = Double.parseDouble(imMinField.getText());
			double immax = Double.parseDouble(imMaxField.getText());

			paintSet(canvas.getGraphicsContext2D(), remin, remax, immin, immax);
		});

		Scene scene = new Scene(fractalRootPane, CANVAS_WIDTH + 2 * X_OFFSET, CANVAS_HEIGHT + 2 * Y_OFFSET);
		scene.setFill(Color.BLACK);
		primaryStage.setTitle("Mandelbrot Set");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void paintSet(GraphicsContext ctx, double reMin, double reMax, double imMin, double imMax) {
		double precision = Math.max((reMax - reMin) / CANVAS_WIDTH, (imMax - imMin) / CANVAS_HEIGHT);
		int convergenceSteps = 50;
		for (double c = reMin, xR = 0; xR < CANVAS_WIDTH; c = c + precision, xR++) {
			for (double ci = imMin, yR = 0; yR < CANVAS_HEIGHT; ci = ci + precision, yR++) {
				double convergenceValue = checkConvergence(ci, c, convergenceSteps);
				double t1 = (double) convergenceValue / convergenceSteps;
				double c1 = Math.min(255 * 2 * t1, 255);
				double c2 = Math.max(255 * (2 * t1 - 1), 0);

				if (convergenceValue != convergenceSteps) {
					ctx.setFill(Color.color(c2 / 255.0, c1 / 255.0, c2 / 255.0));
				} else {
					ctx.setFill(Color.ALICEBLUE); // Convergence Color
				}
				ctx.fillRect(xR, yR, 1, 1);
			}
		}
	}

	/**
	 * Checks the convergence of a coordinate (c, ci) The convergence factor
	 * determines the color of the point.
	 */
	private int checkConvergence(double ci, double c, int convergenceSteps) {
		double z = 0;
		double zi = 0;
		for (int i = 0; i < convergenceSteps; i++) {
			double ziT = 2 * (z * zi);
			double zT = z * z - (zi * zi);
			z = zT + c;
			zi = ziT + ci;

			if (z * z + zi * zi >= 4.0) {
				return i;
			}
		}
		return convergenceSteps;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
