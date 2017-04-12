/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netwrokclient;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

/**
 *
 * @author Abed
 */
public class GroupsPane extends VBox {

    private Label lbHeader;
    private ListView<String> lvGroups;

    public GroupsPane() {
        lbHeader = new Label("Groups");
        lvGroups = new ListView<>();
        lvGroups.setItems(FXCollections.observableArrayList());
        lvGroups.setMaxWidth(150);
        getChildren().addAll(lbHeader, lvGroups);
        setPadding(new Insets(5));
        setAlignment(Pos.CENTER);
        setSpacing(2);
        setStyle("-fx-background-color: rgba(149, 165, 166,1.0);");
    }

    public Label getLbHeader() {
        return lbHeader;
    }

    public ListView<String> getLvGroups() {
        return lvGroups;
    }

}
