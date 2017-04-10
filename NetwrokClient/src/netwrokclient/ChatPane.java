/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netwrokclient;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 *
 * @author Abed
 */
public class ChatPane extends VBox {

    private Label lbHeader;
    private ScrollPane sp;
    private VBox vbMessages;

    public ChatPane(String header) {
        lbHeader = new Label(header);
        vbMessages = new VBox(10);
        sp = new ScrollPane(vbMessages);
        getChildren().addAll(lbHeader, sp);
        setPadding(new Insets(10));
        setAlignment(Pos.CENTER);
        setSpacing(10);
    }

    public void reciveFile(String path, String from) {
        Label lbFrom = new Label(from);
        Text text = new Text(path);
        text.setFill(Color.BLUE);
        text.setOnMouseEntered(e -> {
            text.setUnderline(true);
        });
        text.setOnMouseEntered(e -> {
            text.setUnderline(false);
        });
        text.setOnMouseClicked(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    File myFile = new File(path);
                    Desktop.getDesktop().open(myFile);
                } catch (IOException ex) {
                    // no application registered for PDFs
                }
            }
        });
        text.setWrappingWidth(200);
        VBox vBox = new VBox(2, lbFrom, text);
        vbMessages.getChildren().add(vBox);
        vBox.setAlignment(Pos.BASELINE_RIGHT);
    }

    public void reciveText(String msg, String from) {
        Label lbFrom = new Label(from);
        Text text = new Text(msg);
        text.setWrappingWidth(200);
        VBox vBox = new VBox(2, lbFrom, text);
        vbMessages.getChildren().add(vBox);
        vBox.setAlignment(Pos.BASELINE_RIGHT);
    }

    public void reciveImage(String path, String from) {
        Label lbFrom = new Label(from);
        ImageView image = new ImageView(path);
        image.setOnMouseClicked(e -> {
            if (e.getClickCount() > 1) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        File myFile = new File(path);
                        Desktop.getDesktop().open(myFile);
                    } catch (IOException ex) {
                        // no application registered for PDFs
                    }
                }
            }
        });
        image.setFitHeight(200);
        image.setFitWidth(200);
        VBox vBox = new VBox(2, lbFrom, image);
        vbMessages.getChildren().add(vBox);
        vBox.setAlignment(Pos.BASELINE_RIGHT);

    }

    public void sendImage(String path, String user) {
        Label lbFrom = new Label(user);
        ImageView image = new ImageView(path);
        image.setOnMouseClicked(e -> {
            if (e.getClickCount() > 1) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        File myFile = new File(path);
                        Desktop.getDesktop().open(myFile);
                    } catch (IOException ex) {
                        // no application registered for PDFs
                    }
                }
            }
        });
        image.setFitHeight(200);
        image.setFitWidth(200);
        VBox vBox = new VBox(2, lbFrom, image);
        vbMessages.getChildren().add(vBox);
        vBox.setAlignment(Pos.BASELINE_LEFT);
    }

    public void sendText(String msg, String user) {
        Label lbFrom = new Label(user);
        Text text = new Text(msg);
        text.setWrappingWidth(200);
        VBox vBox = new VBox(2, lbFrom, text);
        vbMessages.getChildren().add(vBox);
        vBox.setAlignment(Pos.BASELINE_LEFT);
    }

    public void sendFile(String path, String user) {
        Label lbFrom = new Label(user);
        Text text = new Text(path);
        text.setFill(Color.BLUE);
        text.setOnMouseEntered(e -> {
            text.setUnderline(true);
        });
        text.setOnMouseEntered(e -> {
            text.setUnderline(false);
        });
        text.setOnMouseClicked(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    File myFile = new File(path);
                    Desktop.getDesktop().open(myFile);
                } catch (IOException ex) {
                    // no application registered for PDFs
                }
            }
        });
        text.setWrappingWidth(200);
        VBox vBox = new VBox(2, lbFrom, text);
        vbMessages.getChildren().add(vBox);
        vBox.setAlignment(Pos.BASELINE_LEFT);
    }

}