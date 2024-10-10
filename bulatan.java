import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class bulatan extends Application {

    // Enum untuk pilihan pengiraan
    enum Pilihan {
        MANUAL, AUTO
    }

    private TextField jejariInput;
    private TextArea outputArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Pengiraan Bulatan dan Bentuk Lain");

        Label label = new Label("Masukkan jejari bulatan (positif):");
        jejariInput = new TextField();
        Button calculateButton = new Button("Kira");
        outputArea = new TextArea();
        outputArea.setEditable(false);
        Button saveButton = new Button("Simpan Hasil");

        calculateButton.setOnAction(e -> calculate());
        saveButton.setOnAction(e -> saveResults());

        VBox layout = new VBox(10, label, jejariInput, calculateButton, outputArea, saveButton);
        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void calculate() {
        double jejari = getJejari();

        if (jejari > 0) {
            double diameter = 2 * jejari;
            double lilitan = 2 * Math.PI * jejari;
            double luas = kiraLuas(jejari);

            String result = String.format("Diameter: %s unit\nLilitan: %s unit\nLuas: %s unit persegi\n",
                    bundarkan(diameter, 3), bundarkan(lilitan, 3), bundarkan(luas, 3));

            outputArea.setText(result);

            // Tentukan saiz bulatan berdasarkan luas
            outputArea.appendText(luas > 100 ? "Luas bulatan besar." : "Luas bulatan kecil.");
        }
    }

    private double getJejari() {
        try {
            return Double.parseDouble(jejariInput.getText());
        } catch (NumberFormatException e) {
            showAlert("Masukkan jejari yang sah.");
            return -1;
        }
    }

    private void saveResults() {
        String results = outputArea.getText();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("hasil_pengiraan.txt", true))) {
            writer.write(results);
            writer.newLine();
            showAlert("Hasil disimpan ke dalam fail hasil_pengiraan.txt");
        } catch (IOException e) {
            showAlert("ERROR! Gagal menyimpan hasil: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Fungsi untuk mengira luas bulatan
    private double kiraLuas(double jejari) {
        return Math.PI * jejari * jejari;
    }

    // Fungsi untuk membundarkan kepada n tempat perpuluhan menggunakan BigDecimal
    private String bundarkan(double nilai, int tempatPerpuluhan) {
        BigDecimal bd = new BigDecimal(Double.toString(nilai));
        bd = bd.setScale(tempatPerpuluhan, RoundingMode.HALF_UP);
        return bd.toString();
    }
}