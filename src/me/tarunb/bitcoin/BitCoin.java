package me.tarunb.bitcoin;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * ConsoleApp by Tarun Boddupalli
 * <p>
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2015 Tarun Boddupalli
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
public class BitCoin extends Application {

    private static double currentPrice;

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public static String getPrice() {
        try {
            StringBuilder builder = new StringBuilder();
            URL url = new URL("https://api.bitcoinaverage.com/ticker/USD/");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String s;
            while ((s = in.readLine()) != null) {
                builder.append(s);
            }
            currentPrice = (double) ((JSONObject) new JSONParser().parse(builder.toString())).get("24h_avg");
            return "$" + currentPrice;
        } catch (Exception e) {
            return "Error!";
        }
    }

    @Override
    public void start(Stage stage) throws Exception {

        Text price = new Text("Current Price: " + getPrice());
        price.setFont(Font.font("Helvetica", 24));
        price.setFill(Color.AQUA);


        Button refresh = new Button("Refresh Price");
        refresh.setOnAction(actionEvent -> price.setText("Current Price: " + getPrice()));

        Text text = new Text("Type your BitCoins below:");
        text.setFont(Font.font("Helvetica", 18));
        text.setFill(Color.GREEN);
        text.setTextAlignment(TextAlignment.CENTER);

        final TextField checkPrice = new TextField();
        checkPrice.setAlignment(Pos.CENTER);

        final Button checkPriceButton = new Button("Check the price of your BitCoins");
        checkPriceButton.setOnAction(actionEvent -> {
            try {
                double coins = Double.parseDouble(checkPrice.getText());
                String s = String.format("%f BitCoins%n is currently worth%n $%.2f", coins, (currentPrice * coins));
                text.setText(s);
            } catch (NumberFormatException e) {
                text.setText("Please Enter your\n coins without a\n Dollar Sign. Example:\n 0.27");
            }
        });

        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(price, refresh, text, checkPrice, checkPriceButton);
        vBox.setPadding(new Insets(0, 50, 0, 50));

        StackPane pane = new StackPane();
        pane.getChildren().add(vBox);

        pane.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, null, null)));

        stage.getIcons().add(new Image(getClass().getResourceAsStream("bc.png")));
        stage.setResizable(false);
        stage.setScene(new Scene(pane, 300, 300, Color.DARKGRAY));
        stage.setTitle("BitCoin Price");
        stage.show();
    }

}
