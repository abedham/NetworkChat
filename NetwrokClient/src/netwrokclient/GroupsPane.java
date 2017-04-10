/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netwrokclient;

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
        lbHeader = new Label("Users");
        lvGroups = new ListView<>();
        getChildren().addAll(lbHeader, lvGroups);
        setPadding(new Insets(10));
        setAlignment(Pos.CENTER);
        setSpacing(10);
    }

    public Label getLbHeader() {
        return lbHeader;
    }

    public ListView<String> getLvUsers() {
        return lvGroups;
    }

}
