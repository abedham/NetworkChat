/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.network.message.Message;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
public class Client implements Runnable {

    private Socket socket;
    private String userName;
    private String host;
    private int port;
    private List<MessageListner> messageListners = new ArrayList<>();
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private List<String> users = new ArrayList<>();

    private Map<String, List<Message>> messages = new HashMap<>();

    public Client(String userName, String host, int port) {
        this.userName = userName;
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(host, port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            // initialize with server and check username. ........
            Message msg = new Message();
            msg.setType(Type.LOGIN);
            msg.setUserName(userName);
            sendMessage(msg);
            msg = reciveMessage();
            if (msg.getType() == Type.ALL_USERS) {
                users = (List<String>) msg.getData();
                for (String user : users) {
                    messages.put(user, new ArrayList<>());
                }
                for (MessageListner listner : messageListners) {
                    listner.init(users);
                }
                while (true) {
                    msg = reciveMessage();
                    if (msg != null) {
                        processMessage(msg);
                    } else {
                        break;
                    }
                }
            } else if (msg.getType() == Type.FAILD_LOGIN) {
                for (MessageListner listner : messageListners) {
                    listner.onFaildLogin("User exist, please use another username");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public List<MessageListner> getMessageListners() {
        return messageListners;
    }

    public List<String> getUsers() {
        return users;
    }

    public Map<String, List<Message>> getMessages() {
        return messages;
    }

    public void addMessageListner(MessageListner messageListner) {
        messageListners.add(messageListner);
    }

    public void removeMessageListner(MessageListner messageListner) {
        messageListners.remove(messageListner);
    }

    private Message reciveMessage() {
        Message msg = null;
        try {
            msg = (Message) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Server disconnected");
        }
        return msg;
    }

    public boolean sendMessage(Message msg) {
        try {
            oos.writeObject(msg);
            oos.flush();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public String getUserName() {
        return userName;
    }

    public static byte[] readFileToByteArray(File file) {
        FileInputStream fis = null;
        // Creating a byte array using the length of the file
        // file.length returns long which is cast to int
        byte[] bArray = new byte[(int) file.length()];
        try {
            fis = new FileInputStream(file);
            fis.read(bArray);
            fis.close();

        } catch (IOException ioExp) {
            ioExp.printStackTrace();
        }
        return bArray;
    }

    public static File byteArrayToFile(byte[] bytes, String path, String fileName) {
        FileOutputStream fileos = null;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            file = new File(path + "/" + fileName);
            System.out.println(file.getAbsolutePath());
            file.createNewFile();
            fileos = new FileOutputStream(file);
            fileos.write(bytes);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileos.close();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return file;
    }

    private void processMessage(Message msg) {
        String senderUser = msg.getUserName();
        switch (msg.getType()) {
            case Type.CHAT_MESSAGE:
                for (MessageListner listner : messageListners) {
                    listner.onChatMessage(senderUser, (String) msg.getData());
                }
                break;
            case Type.IMAGE:
                for (MessageListner listner : messageListners) {
                    byte[] bytes = (byte[]) msg.getData();
                    listner.onImage(senderUser, msg.getFileName(), bytes);
                }
                break;
            case Type.FILE:
                for (MessageListner listner : messageListners) {
                    byte[] bytes = (byte[]) msg.getData();
                    listner.onFile(senderUser, msg.getFileName(), bytes);
                }
                break;
            case Type.CREATE_GROUP:
                for (MessageListner listner : messageListners) {
                    listner.onCreateGroup(msg.getReciverName(), msg.getUserName(), (List<String>) msg.getData());
                }
                break;
            case Type.GROUP_MESSAGE:
                for (MessageListner listner : messageListners) {
                    listner.onGroupMessage(msg.getReciverName(), msg.getUserName(), (String) msg.getData());
                }
                break;
            case Type.GROUP_IMAGE:
                for (MessageListner listner : messageListners) {
                    byte[] bytes = (byte[]) msg.getData();
                    listner.onGroupImage(msg.getReciverName(), msg.getFileName(), msg.getUserName(), bytes);
                }
                break;
            case Type.GROUP_FILE:
                for (MessageListner listner : messageListners) {
                    byte[] bytes = (byte[]) msg.getData();
                    listner.onGroupFile(msg.getReciverName(), msg.getFileName(), msg.getUserName(), bytes);
                }
                break;
            case Type.NEW_USER:
                users.add(msg.getUserName());
                for (MessageListner listner : messageListners) {
                    listner.onNewUserLogin((String) msg.getData());
                }
                break;
            case Type.LOGOUT:
                users.remove(msg.getUserName());
                for (MessageListner listner : messageListners) {
                    listner.onUserLogout(msg.getUserName());
                }
                break;
            default:
                for (MessageListner listner : messageListners) {
                    listner.onUnknownMessage(msg);
                }
        }
    }

    public static interface MessageListner {

        public void init(List<String> users);

        public void onChatMessage(String from, String message);

        public void onImage(String from, String fileName, byte[] bytes);

        public void onFile(String from, String fileName, byte[] bytes);

        public void onCreateGroup(String groupName, String from, List<String> users);

        public void onGroupMessage(String groupName, String from, String message);

        public void onGroupImage(String groupName, String fileName, String from, byte[] bytes);

        public void onGroupFile(String groupName, String fileName, String from, byte[] bytes);

        public void onNewUserLogin(String userName);

        public void onUserLogout(String userName);

        public void onFaildLogin(String msg);

        public void onUnknownMessage(Message message);

    }
}
