package huybdse02612.fpt.edu.Entity;

import android.os.Handler;
import android.os.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import huybdse02612.fpt.edu.Entity.CommandMessage;
import huybdse02612.fpt.edu.Entity.CommandMessageType;

/**
 * Created by huy on 7/21/2015.
 */
public class ProServer {

    Thread waitClientThread;
    ServerSocket serverSocket;
//    UpdateGUI updateGUI;
    Socket clientSocket;
    ObjectOutputStream oos;
    Thread clientThread;
    ObjectInputStream ois;
    public boolean running=true;
//    int[][] numberTable;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message status) {
//            updateGUI.updateState((CommandMessage) status.obj);
        }
    };

    public ProServer() {

    }


    public void closeServer() {
        try {
            running=false;
            oos.close();
            ois.close();
            serverSocket.close();
        } catch (Exception e) {
        }

    }

//    public void setEventListener(UpdateGUI dataDisplay) {
//        updateGUI = dataDisplay;
//    }

    public void startListening() {
        waitClientThread = new Thread(new Runnable() {
            public void run() {
                try {
                    serverSocket = new ServerSocket(2001);
                    clientSocket = serverSocket.accept();
                    ois = new ObjectInputStream(
                            clientSocket.getInputStream());
                    oos = new ObjectOutputStream(
                            clientSocket.getOutputStream());
                    CommunicationThread commThread = new CommunicationThread();
                    new Thread(commThread).start();
                    Message clientmessage = Message.obtain();
//                    GameSetting gs = new GameSetting(SettingData.matrixSize, SettingData.ruleGame,numberTable);
                    CommandMessage cm = new CommandMessage(CommandMessageType.CONNECT, "gs");
                    clientmessage.obj = cm;
                    mHandler.sendMessage(clientmessage);
                    sendCommand(cm);
                } catch (Exception e) {
                }
            }
        });

        waitClientThread.start();

    }


    class CommunicationThread implements Runnable {

        public void run() {
            while (!Thread.currentThread().isInterrupted() && running) {
                try {
                    CommandMessage read = (CommandMessage) ois.readObject();
                    Message clientmessage = Message.obtain();
                    clientmessage.obj = read;
                    mHandler.sendMessage(clientmessage);
                } catch (Exception e) {
                    running=false;
                    Message clientmessage = Message.obtain();
                    clientmessage.obj = new CommandMessage(CommandMessageType.DISCONNECT, "");
                    mHandler.sendMessage(clientmessage);
                }
            }
        }

    }

    public void sendCommand(CommandMessage command) {
        try {
            oos.writeObject(command);
        } catch (Exception e) {
        }
    }

}
