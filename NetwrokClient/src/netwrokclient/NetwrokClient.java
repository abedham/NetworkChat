/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netwrokclient;

import client.Client;
import client.Type;
import com.abed.network.project.Message;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Abed
 */
public class NetwrokClient extends Application {

    Label lbHeader;
    //Controling nodes
    private Button btnSendImage;
    private Button btnSendFile;
    private Button btnSendText;
    private TextField tfMessage;
    private HBox hbControl;

    private ChatPane chatPane;
    private VBox vbChat;

    private UsersPane usersPane;
    private GroupsPane groupsPane;
    private VBox vbList;

    private BorderPane root;

    private Map<String, ChatPane> usersChatPanes = new HashMap<>();
    private Map<String, ChatPane> groupsChatPanes = new HashMap<>();

    private Client client;
    private Label lbError = new Label("Text");

    private boolean isGroup;
    private String messageTo;

    @Override
    public void start(Stage primaryStage) {

        Label lbUserNmae = new Label("User Name: ");
        TextField tfUserName = new TextField();
        HBox hBox = new HBox(10, lbUserNmae, tfUserName);
        hBox.setAlignment(Pos.CENTER);

        Button login = new Button("Login");

        VBox vBox = new VBox(10, hBox, login, lbError);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(10));
        login.setOnAction(e -> {
            client = new Client(tfUserName.getText(), "localhost", 2222);
            client.addMessageListner(new Client.MessageListner() {

                @Override
                public void init(List<String> users) {
                    Platform.runLater(() -> {
                        primaryStage.setScene(chatScene());

                        for (String userName : users) {
                            ChatPane chat = new ChatPane(userName);
                            usersChatPanes.put(userName, chat);
                            usersPane.getLvUsers().getItems().add(userName);
                        }
                    });
                }

                @Override
                public void onChatMessage(String from, String message) {
                    Platform.runLater(() -> {
                        ChatPane chat = usersChatPanes.get(from);
                        chat.reciveText(message, from);

                    });
                }

                @Override
                public void onImage(String from, byte[] bytes) {
                    Platform.runLater(() -> {
                        ChatPane chat = usersChatPanes.get(from);
                        File file = client.byteArrayToFile(bytes, "/" + from + "/" + System.currentTimeMillis());
                        chat.reciveImage(file.getPath(), from);

                    });
                }

                @Override
                public void onFile(String from, byte[] bytes) {
                    Platform.runLater(() -> {
                        ChatPane chat = usersChatPanes.get(from);
                        File file = client.byteArrayToFile(bytes, "/" + from + "/" + System.currentTimeMillis());
                        chat.reciveFile(file.getPath(), from);

                    });
                }

                @Override
                public void onCreateGroup(String groupName, String from, List<String> users) {
                    Platform.runLater(() -> {
                        ChatPane chat = new ChatPane("Group " + groupName + ": " + users);
                        groupsChatPanes.put(groupName, chat);
                        groupsPane.getLvGroups().getItems().add(groupName);

                    });
                }

                @Override
                public void onGroupMessage(String groupName, String from, String message) {
                    Platform.runLater(() -> {
                        ChatPane chat = groupsChatPanes.get(groupName);
                        chat.reciveText(message, from);
                    });
                }

                @Override
                public void onGroupImage(String groupName, String from, byte[] bytes) {
                    Platform.runLater(() -> {
                        ChatPane chat = groupsChatPanes.get(from);
                        File file = client.byteArrayToFile(bytes, "/" + groupName + "/" + from + "/" + System.currentTimeMillis());
                        chat.reciveImage(file.getPath(), from);
                    });
                }

                @Override
                public void onGroupFile(String groupName, String from, byte[] bytes) {
                    Platform.runLater(() -> {
                        ChatPane chat = groupsChatPanes.get(from);
                        File file = client.byteArrayToFile(bytes, "/" + groupName + "/" + from + "/" + System.currentTimeMillis());
                        chat.reciveFile(file.getPath(), from);
                    });
                }

                @Override
                public void onNewUserLogin(String userName) {
                    Platform.runLater(() -> {
                        ChatPane chat = new ChatPane(userName);
                        usersChatPanes.put(userName, chat);
                        usersPane.getLvUsers().getItems().add(userName);

                    });
                }

                @Override
                public void onUnknownMessage(Message message) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Unknown Message");
                        alert.setHeaderText("Look, you are recive unknown message");
                        alert.setContentText("Message content: " + message.getData());
                        alert.show();

                    });
                }

                @Override
                public void onUserLogout(String userName) {
                    Platform.runLater(() -> {
                        usersChatPanes.remove(userName);
                        usersPane.getLvUsers().getItems().remove(userName);

                    });
                }

                @Override
                public void onFaildLogin(String msg) {
                    System.out.println("Text");
                    Platform.runLater(() -> {
                        lbError.setText("Error");
                    });
                }
            });
            new Thread(client).start();
        });
        Scene scene = new Scene(vBox, 300, 200);
        primaryStage.setResizable(false);
        primaryStage.setTitle("network !!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Scene chatScene() {
        initComponents();
        Scene scene = new Scene(root, 600, 486);
        return scene;
    }

    private void initComponents() {
        lbHeader = new Label();

        btnSendFile = new Button("File");
        btnSendImage = new Button("Image");
        btnSendText = new Button("Send");
        btnSendText.setOnAction(e -> sendText());
        tfMessage = new TextField();
        tfMessage.setMinWidth(290);

        hbControl = new HBox();
        hbControl.getChildren().addAll(btnSendFile, btnSendImage, tfMessage, btnSendText);
        hbControl.setSpacing(1);
        hbControl.setAlignment(Pos.CENTER);

        chatPane = new ChatPane("Chating View, please select User to view his chats");
        vbChat = new VBox();
        vbChat.getChildren().addAll(chatPane, hbControl);
        vbChat.setSpacing(5);
        vbChat.setAlignment(Pos.CENTER);

        usersPane = new UsersPane();

        groupsPane = new GroupsPane();

        usersPane.getLvUsers().getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends Object> observable, Object oldValue, Object newValue) -> {
                    vbChat.getChildren().remove(0);
                    vbChat.getChildren().add(0, usersChatPanes.get(newValue));
                    isGroup = false;
                    messageTo = (String) newValue;
                });
        usersPane.getBtnAddToGroup().setOnAction(e -> {
            Message message = new Message();
            message.setData(Arrays.asList(usersPane.getTfUsersGroup().getText().split(" ")));
            message.setReciverName(usersPane.getTfGroupName().getText());
            message.setUserName(client.getUserName());
            message.setType(Type.CREATE_GROUP);
            client.sendMessage(message);
            ChatPane chat = new ChatPane("Group " + message.getReciverName() + ": " + message.getData());
            groupsChatPanes.put(message.getReciverName(), chat);
            groupsPane.getLvGroups().getItems().add(message.getReciverName());
        });
        groupsPane.getLvGroups().getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends Object> observable, Object oldValue, Object newValue) -> {
                    vbChat.getChildren().remove(0);
                    vbChat.getChildren().add(0, groupsChatPanes.get(newValue));
                    isGroup = true;
                    messageTo = (String) newValue;
                });
        vbList = new VBox();
        vbList.getChildren().addAll(usersPane, groupsPane);
        vbList.setSpacing(5);
        vbList.setAlignment(Pos.CENTER);

        root = new BorderPane();

        root.setTop(lbHeader);
        root.setLeft(vbList);
        root.setCenter(vbChat);
        root.setPadding(new Insets(10));

    }

    private void sendText() {
        String message = tfMessage.getText();

        if (!message.isEmpty()) {
            Message msg = new Message();
            msg.setUserName(client.getUserName());
            msg.setData(tfMessage.getText());
            msg.setReciverName(messageTo);
            if (isGroup) {
                msg.setType(Type.GROUP_MESSAGE);
                groupsChatPanes.get(messageTo).sendText(message, client.getUserName());
            } else {
                msg.setType(Type.CHAT_MESSAGE);
                usersChatPanes.get(messageTo).sendText(message, client.getUserName());
            }
            client.sendMessage(msg);

        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
