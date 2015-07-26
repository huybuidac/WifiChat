package huybdse02612.fpt.edu.Entity;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import huybdse02612.fpt.edu.Util.ConstantValue;

/**
 * Created by hoang anh tuan on 7/21/2015.
 */
public class ProClient extends Thread {
    private final String TAG=this.getClass().getName();

    private final CommandMessage mCmd;
    private final DatagramSocket udpSocket;

    public ProClient(CommandMessage cmd, DatagramSocket udpSocket) {
        this.mCmd = cmd;
        this.udpSocket = udpSocket;
    }

    @Override
    public void run() {
        Log.d(TAG,"GOTO RUN");
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream(1000);
            ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(byteStream));
            os.flush();
            os.writeObject(mCmd);
            os.flush();
            //retrieves byte array
            byte[] sendBuf = byteStream.toByteArray();
            DatagramPacket packet = new DatagramPacket(sendBuf, sendBuf.length, InetAddress.getByName(mCmd.getReceiAddress()), ConstantValue.PORT);
            udpSocket.send(packet);
            os.close();
        } catch (Exception e) {
            Log.e(TAG,"ERROR client Thread");
            e.printStackTrace();
        }
        Log.d(TAG,"OUT RUN");
    }

}