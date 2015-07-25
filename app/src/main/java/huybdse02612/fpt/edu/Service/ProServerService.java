package huybdse02612.fpt.edu.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import huybdse02612.fpt.edu.Entity.CommandMessage;
import huybdse02612.fpt.edu.Entity.ProClient;
import huybdse02612.fpt.edu.Entity.User;
import huybdse02612.fpt.edu.Util.ConstantVariance;

/**
 * Created by huy on 7/21/2015.
 */
public class ProServerService extends Service {

    private final String TAG = this.getClass().getName();
    Thread waitClientThread;
    private boolean mIsRunning;
    private DatagramSocket udpSocket;

    @Override
    public void onCreate() {
        mIsRunning = true;
        startListening();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        try {
            if (intent.getAction() != null) {
                switch (intent.getAction()) {
                    case ConstantVariance.ACTION_SEND:
                        final CommandMessage cmd = (CommandMessage) intent.getSerializableExtra(ConstantVariance.EXTRA_COMMAND_MESSAGE);
                        ProClient client = new ProClient(cmd, udpSocket);
                        client.start();
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "onStartCommand Exception " + e.getMessage());
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        closeServer();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void closeServer() {
        try {
            mIsRunning = false;
            udpSocket.close();
        } catch (Exception e) {
        }
    }

    public void startListening() {
        waitClientThread = new Thread(new Runnable() {
            public void run() {
                try {
                    udpSocket = new DatagramSocket(ConstantVariance.PORT);
                    while (mIsRunning) {
                        try {

                            byte[] recvBuf = new byte[1000];
                            DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                            udpSocket.receive(packet);
                            ByteArrayInputStream byteStream = new ByteArrayInputStream(recvBuf);
                            ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(byteStream));
                            CommandMessage cmd = (CommandMessage) is.readObject();
                            is.close();
                            switch (cmd.getmType()) {
                                case CONNECT:
                                    Log.i(TAG, "FROM " + packet.getAddress() + " " + cmd.toString());
                                    updatePeopleGui(cmd.getmFromUser(),packet.getAddress().getHostAddress());
                                    break;
                                case DISCONNECT:
                                    break;
                                case MESSAGE:
                                    break;
                            }
                        } catch (IOException e) {
                            Log.e(TAG, "serverSocket.accept IOException" + e.getMessage());
                        } catch (ClassNotFoundException e) {
                            Log.e(TAG, "serverSocket.accept ClassNotFoundException" + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "startListening " + e.getMessage());
                }
            }
        });
        waitClientThread.start();
    }
    Intent intent;
    private void updatePeopleGui(String user, String ipAddress) {
        try {
            User newUser = new User(ipAddress,user);
            intent = new Intent(ConstantVariance.MY_BROADCAST);
            intent.putExtra(ConstantVariance.ACTION, ConstantVariance.ACTION_ADD_USER);
            intent.putExtra(ConstantVariance.EXTRA_USER,newUser);
            getApplicationContext().sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
