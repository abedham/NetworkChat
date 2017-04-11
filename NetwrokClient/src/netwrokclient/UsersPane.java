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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 *
 * @author Abed
 */
public class UsersPane extends VBox {

    private Label lbHeader;
    private ListView<String> lvUsers;
    private TextField tfUsersGroup;
    private TextField tfGroupName;
    private Button btnAddToGroup;

    public UsersPane() {
        lbHeader = new Label("Users");
        lvUsers = new ListView<>();
        lvUsers.setMaxWidth(150);
        lvUsers.setItems(FXCollections.observableArrayList());
        btnAddToGroup = new Button("Add to Group");
        tfUsersGroup = new TextField("");
        tfGroupName = new TextField("");
        tfUsersGroup.setPromptText("Type Users here");
        tfGroupName.setPromptText("Type Group Name here");
        getChildren().addAll(lbHeader, lvUsers, tfUsersGroup, tfGroupName, btnAddToGroup);
        setPadding(new Insets(5));
        setAlignment(Pos.CENTER);
        setSpacing(2);
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

    public TextField getTfUsersGroup() {
        return tfUsersGroup;
    }

    public TextField getTfGroupName() {
        return tfGroupName;
    }
}
