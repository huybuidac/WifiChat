package huybdse02612.fpt.edu.Entity;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import huybdse02612.fpt.edu.Entity.CommandMessage;
import huybdse02612.fpt.edu.Entity.CommandMessageType;

/**
 * Created by huy on 7/21/2015.
 */
public class ProClient {
    ObjectInputStream ois;
    Socket clientSocket;
    ObjectOutputStream oos;
//    UpdateGUI updateGUI;
    Thread connectServerThread;
    public boolean running=true;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            updateGUI.updateState((CommandMessage)msg.obj);
        }
    };

    public void closeConnection() {
        try {
            oos.close();
            ois.close();
            clientSocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void sendCommand(CommandMessage command) {
        try {
            oos.writeObject(command);
        } catch (Exception e) {
        }
    }

//    public void setEventListener(UpdateGUI updateGUI) {
//        this.updateGUI = updateGUI;
//    }

    public void Start() {
        connectServerThread = new Thread(new Runnable() {
            public void run() {
                try {
                    Log.e("CHECK CONNECT FAIL", "======================");
                    clientSocket = new Socket("localhost", 2001);
                    Log.e("CHECK CONNECT FAIL 222", "======================");
                    oos = new ObjectOutputStream(
                            clientSocket.getOutputStream());
                    ois = new ObjectInputStream(clientSocket.getInputStream());
                    CommunicationThread commThread = new CommunicationThread();
                    new Thread(commThread).start();
                } catch (Exception e) {
                    Log.e("CONNECT FAIL", "====================");
                }

            }
        });

        connectServerThread.start();
    }

    class CommunicationThread implements Runnable {

        public void run() {
            while (!Thread.currentThread().isInterrupted() && running) {
                try {
                    CommandMessage read = (CommandMessage) ois.readObject();
                    Log.i("CHECK GET", ""+read.getmType());
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
}