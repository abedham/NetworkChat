/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netwrokclient;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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
        sp.setMinHeight(400);
        getChildren().addAll(lbHeader, sp);
        setAlignment(Pos.CENTER);
        setSpacing(2);
    }

    public void reciveFile(String path, String from) {
        Label lbFrom = new Label(from + ": ");
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
        HBox hBox = new HBox(2, lbFrom, text);
        vbMessages.getChildren().add(hBox);
        hBox.setAlignment(Pos.BASELINE_RIGHT);
    }

    public void reciveText(String msg, String from) {
        Label lbFrom = new Label(from + ": ");
        Text text = new Text(msg);
        text.setWrappingWidth(200);
        HBox hBox = new HBox(2, lbFrom, text);
        vbMessages.getChildren().add(hBox);
        hBox.setAlignment(Pos.BASELINE_RIGHT);
    }

    public void reciveImage(File file, String from)  {
        try {
            Label lbFrom = new Label(from + ": ");
            ImageView image = new ImageView(file.toURI().toURL().toExternalForm());
            image.setOnMouseClicked(e -> {
                if (e.getClickCount() > 1) {
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().open(file);
                        } catch (IOException ex) {
                            // no application registered for PDFs
                        }
                    }
                }
            });
            image.setFitHeight(200);
            image.setFitWidth(200);
            HBox hBox = new HBox(2, lbFrom, image);
            vbMessages.getChildren().add(hBox);
            hBox.setAlignment(Pos.BASELINE_RIGHT);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ChatPane.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void sendImage(File file, String user) {
        try {
            Label lbFrom = new Label(user + ": ");
            ImageView image = new ImageView(file.toURI().toURL().toExternalForm());
            image.setOnMouseClicked(e -> {
                if (e.getClickCount() > 1) {
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().open(file);
                        } catch (IOException ex) {
                            // no application registered for PDFs
                        }
                    }
                }
            });
            image.setFitHeight(200);
            image.setFitWidth(200);
            HBox hBox = new HBox(2, lbFrom, image);
            vbMessages.getChildren().add(hBox);
            hBox.setAlignment(Pos.BASELINE_LEFT);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ChatPane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendText(String msg, String user) {
        Label lbFrom = new Label(user + ": ");
        Text text = new Text(msg);
        text.setWrappingWidth(200);
        HBox hBox = new HBox(2, lbFrom, text);
        vbMessages.getChildren().add(hBox);
        hBox.setAlignment(Pos.BASELINE_LEFT);
    }

    public void sendFile(String path, String user) {
        Label lbFrom = new Label(user + ": ");
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
        HBox hBox = new HBox(2, lbFrom, text);
        vbMessages.getChildren().add(hBox);
        hBox.setAlignment(Pos.BASELINE_LEFT);
    }

}
