package huybdse02612.fpt.edu.Entity;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hoang anh tuan on 7/23/2015.
 */
public class User implements Serializable {
    private String mIpAddress;
    private String mSender;
    private String mReceiver;
    private boolean mIsOnline;
    private ArrayList<String> mLstMessages;
    private int mCount;

    public User(String mIpAddress, String mSender, String mReceiver) {
        this.mIpAddress = mIpAddress;
        this.mSender = mSender;
        this.mReceiver=mReceiver;
        this.mIsOnline =true;
        this.mLstMessages = new ArrayList();
        this.mCount=0;
    }

    public String getAllMessage(){
        return TextUtils.join("\r\n", mLstMessages)+"\r\n";
    }

    public void addMessage(String mess,boolean addCount) {
        mLstMessages.add(mess);
        if (addCount) {
            mCount++;
        } else {
            mCount=0;
        }
    }

    public String getIpAddress() {
        return mIpAddress;
    }

    public String getSender() {
        return mSender;
    }

    public void setSender(String mSender) {
        this.mSender = mSender;
    }

    public boolean isIsOnline() {
        return mIsOnline;
    }

    public void setIsOnline(boolean mIsOnline) {
        this.mIsOnline = mIsOnline;
    }

    public ArrayList getLstMessages() {
        return mLstMessages;
    }

    public void setLstMessages(ArrayList mLstMessages) {
        this.mLstMessages = new ArrayList<String>(mLstMessages);
    }

    public String getReceiver() {
        return mReceiver;
    }

    public void setReceiver(String mReceiver) {
        this.mReceiver = mReceiver;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int mCount) {
        this.mCount = mCount;
    }
}
