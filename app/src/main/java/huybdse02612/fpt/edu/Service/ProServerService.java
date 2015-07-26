package huybdse02612.fpt.edu.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import huybdse02612.fpt.edu.Entity.CommandMessage;
import huybdse02612.fpt.edu.Entity.CommandMessageType;
import huybdse02612.fpt.edu.Entity.ProClient;
import huybdse02612.fpt.edu.Entity.User;
import huybdse02612.fpt.edu.Util.ConstantValue;

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
        Log.d(TAG, "GOTO onCreate");
        try {
            mListLocalAddress=new ArrayList<>();
            getLocalIpAddress();
            mIsRunning = true;
            startListening();
            Thread.sleep(500);
        } catch (Exception e) {
            Log.e(TAG,"onCreate Exception");
            e.printStackTrace();
        }
        Log.d(TAG, "OUT onCreate");
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.d(TAG,"GOTO onStartCommand");
        try {
            if (mIsRunning!=true) startListening();
            if (intent.getAction() != null) {
                CommandMessage cmd = (CommandMessage) intent.getSerializableExtra(ConstantValue.EXTRA_COMMAND_MESSAGE);
                switch (intent.getAction()) {
                    case ConstantValue.ACTION_CONNECT:
                        ProClient client = new ProClient(cmd, udpSocket);
                        client.start();
                        break;
                    case ConstantValue.ACTION_SEND_MESSAGE:
                        ProClient clientSend = new ProClient(cmd, udpSocket);
                        clientSend.start();
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "onStartCommand Exception");
            e.getStackTrace();
        }
        Log.d(TAG,"OUT onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    private ArrayList<String> mListLocalAddress;

    private void getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) { mListLocalAddress.add(inetAddress.getHostAddress().toString()); }
                }
            }
        } catch (SocketException ex) {
            Log.e("ServerActivity", ex.toString());
        }
    }
    @Override
    public void onDestroy() {
        Log.d(TAG,"GOTO onDestroy");
        closeServer();
        super.onDestroy();
        Log.d(TAG, "GOTO onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void closeServer() {
        Log.d(TAG,"GOTO closeServer");
        try {
            mIsRunning = false;
            udpSocket.close();
        } catch (Exception e) {
            Log.e(TAG,"closeServer exception");
            e.getStackTrace();
        }
        Log.d(TAG,"GOTO closeServer");
    }

    public void startListening() {
        Log.d(TAG,"GOTO startListening");
        waitClientThread = new Thread(new Runnable() {
            public void run() {
                try {
                    udpSocket = new DatagramSocket(ConstantValue.PORT);
                    while (mIsRunning) {
                        try {
                            Log.d(TAG,"GOTO receive mess");
                            byte[] recvBuf = new byte[1000];
                            DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                            udpSocket.receive(packet);
                            ByteArrayInputStream byteStream = new ByteArrayInputStream(recvBuf);
                            ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(byteStream));
                            CommandMessage cmd = (CommandMessage) is.readObject();
                            cmd.setSenderAddress(packet.getAddress().getHostAddress());
                            Log.i(TAG, cmd.getmType().toString() + " FROM " + cmd.getmFromUser() + " " + packet.getAddress() + " " + cmd.toString());
                            is.close();
                            switch (cmd.getmType()) {
                                case CONNECT:
                                    updatePeopleGui(cmd.getmFromUser(),packet.getAddress().toString().replace("/",""),true);
//                                    ProClient client = new ProClient(new CommandMessage(CommandMessageType.CONNECT,), udpSocket);
//                                    client.start();
                                    break;
                                case CONNECT_RESPOND:
                                    updatePeopleGui(cmd.getmFromUser(),packet.getAddress().toString().replace("/",""),false);
                                    break;
                                case DISCONNECT:
                                    break;
                                case MESSAGE:
                                    updateChatGui(cmd);
                                    break;
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Wait receive mess EXCEPTION");
                            e.getStackTrace();
                        }
                        Log.d(TAG,"OUT receive mess");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "startListening excepetion");
                    e.getStackTrace();
                }
            }
        });
        waitClientThread.start();
        Log.d(TAG, "OUT startListening");
    }

    private synchronized void updateChatGui(CommandMessage cmd) {
        Log.d(TAG,"GOTO updateChatGui");
        try {
            intent = new Intent(ConstantValue.MY_BROADCAST);
            intent.putExtra(ConstantValue.ACTION, ConstantValue.ACTION_RECEIVE_MESSAGE);
            intent.putExtra(ConstantValue.EXTRA_CMD,cmd);
            getApplicationContext().sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG,"OUT updateChatGui");
    }

    Intent intent;
    private synchronized void updatePeopleGui(String user, String ipAddress, boolean isNeedRespon) {
        Log.d(TAG,"GOTO updatePeopleGui");
        try {
            if (mListLocalAddress.contains(ipAddress)) return;
            User newUser = new User(ipAddress,user,"");
            intent = new Intent(ConstantValue.MY_BROADCAST);
            intent.putExtra(ConstantValue.ACTION, ConstantValue.ACTION_ADD_USER);
            intent.putExtra(ConstantValue.EXTRA_USER,newUser);
            intent.putExtra(ConstantValue.EXTRA_NEED_RESPOND,isNeedRespon);
            getApplicationContext().sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG,"OUT updatePeopleGui");
    }

}
