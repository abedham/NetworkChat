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
public class UsersPane extends VBox {

    private Label lbHeader;
    private ListView<String> lvUsers;
    private Button btnAddToGroup;

    public UsersPane() {
        lbHeader = new Label("Users");
        lvUsers = new ListView<>();
        btnAddToGroup = new Button("Add to Group");
        getChildren().addAll(lbHeader, lvUsers, btnAddToGroup);
        setPadding(new Insets(10));
        setAlignment(Pos.CENTER);
        setSpacing(10);
    }

    public Label getLbHeader() {
        return lbHeader;
    }

    public ListView<String> getLvUsers() {
        return lvUsers;
    }

    public Button getBtnAddToGroup() {
        return btnAddToGroup;
    }

}
