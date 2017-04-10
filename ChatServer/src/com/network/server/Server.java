package com.network.server;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.abed.network.project.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abed
 */
public class Server implements Runnable {

    private int port;

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true) {
            try {
                Socket socket = server.accept();
                new Thread(new HandleClient(socket)).start();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static ServerSocket server;
    public static Map<String, Client> clients = new HashMap<>();
    public static Map<String, List<Client>> groups = new HashMap<>();
    public static List<String> users = new ArrayList<>();
}

class Client {

    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private String userName;

    public Client(Socket socket) {
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Message reciveMessage() {
        Message msg = null;
        try {
            msg = (Message) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            logout();
//            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return msg;
    }

    public boolean sendMessage(Message msg) {
        try {
            oos.writeObject(msg);
            oos.flush();
            return true;
        } catch (IOException ex) {
//            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            logout();
            return false;
        }
    }

    private void logout() {
        Message msg = new Message();
        System.out.println("User " + userName + " disconnected");
        msg.setType(Type.LOGOUT);
        msg.setUserName(userName);
        Server.clients.remove(userName);
        Server.clients.values().forEach(c -> c.sendMessage(msg));
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

class HandleClient implements Runnable {

    private final Socket socket;
    private Client client;

    public HandleClient(Socket socket) {
        this.socket = socket;
        client = new Client(socket);
    }

    @Override
    public void run() {

        Message msg = client.reciveMessage();

        boolean valid = false;
        if (msg.getType() == Type.LOGIN) {
            Client clientX = Server.clients.get(msg.getUserName());
            if (clientX == null) {
                System.out.println(msg.getUserName() + " is Connected");
                valid = true;
                client.setUserName(msg.getUserName());
                Message usersMessage = new Message();
                usersMessage.setType(Type.ALL_USERS);
                usersMessage.setData(Server.users);
                client.sendMessage(usersMessage);

                Message msgNewUser = new Message();

                msgNewUser.setData(msg.getUserName());
                msgNewUser.setType(Type.NEW_USER);
                Server.clients.values().forEach((c) -> {
                    c.sendMessage(msgNewUser);
                });
                Server.users.add(msg.getUserName());
                Server.clients.put(msg.getUserName(), client);

            } else {
                valid = false;
                msg = new Message();
                msg.setData("User Name exist, choose another User Name");
                msg.setType(Type.ERROR);
                client.sendMessage(msg);
            }

        } else {
            valid = false;
            msg = new Message();
            msg.setData("You must login");
            msg.setType(Type.FAILD_LOGIN);
            client.sendMessage(msg);
        }
        if (valid) {
            while (true) {
                msg = client.reciveMessage();
                if (msg != null) {
                    processMessage(msg);
                } else {
                    break;
                }
            }
        }
    }

    private void processMessage(Message msg) {
        System.out.println(msg.getUserName() + ": " + msg.getData());
        msg.setUserName(client.getUserName());
        switch (msg.getType()) {
            case Type.CHAT_MESSAGE:
            case Type.IMAGE:
            case Type.FILE:
                Client reciver = Server.clients.get(msg.getReciverName());
                if (reciver != null) {
                    reciver.sendMessage(msg);
                } else {
                    msg = new Message();
                    msg.setType(Type.ERROR);
                    msg.setData("Reciver does not exist");
                    client.sendMessage(msg);
                }

                break;
            case Type.CREATE_GROUP:
                List<String> users = (List<String>) msg.getData();
                List<Client> list = Server.groups.get(msg.getReciverName());
                if (list == null) {
                    list = new ArrayList<>();
                    for (String user : users) {
                        Client c = Server.clients.get(user);
                        if (c != null) {
                            list.add(c);
                            c.sendMessage(msg);
                        }
                    }
                    list.add(client);
                    Server.groups.put(msg.getReciverName(), list);
                } else {
                    msg = new Message();
                    msg.setUserName("Server");
                    msg.setType(Type.ERROR);
                    msg.setReciverName(client.getUserName());
                    msg.setData("Group Exist, create group with different name");
                }
                break;
            case Type.GROUP_MESSAGE:
            case Type.GROUP_IMAGE:
            case Type.GROUP_FILE:
                List< Client> group = Server.groups.get(msg.getReciverName());
                if (group != null) {
                    if (group.contains(client)) {
                        for (Client c : group) {
                            if (c != client) {
                                c.sendMessage(msg);
                            }
                        }
                    } else {
                        msg = new Message();
                        msg.setType(Type.ERROR);
                        msg.setUserName("Server");
                        msg.setReciverName(client.getUserName());
                        msg.setData("You are not User in the group");
                        client.sendMessage(msg);
                    }
                }
                break;

            default:
                msg = new Message();
                msg.setType(Type.UNKNOWN_MESSAGE);
                msg.setUserName("Server");
                msg.setReciverName(client.getUserName());
                msg.setData("Not Valid message");
                client.sendMessage(msg);
        }
    }
}
