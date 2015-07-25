package huybdse02612.fpt.edu.Entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hoang anh tuan on 7/23/2015.
 */
public class User implements Serializable {
    private String mIpAddress;
    private String mName;
    private boolean mIsOnline;
    private ArrayList mLstMessages;

    public User(String mIpAddress, String mName) {
        this.mIpAddress = mIpAddress;
        this.mName = mName;
        mLstMessages=new ArrayList();
    }

    public String getmIpAddress() {
        return mIpAddress;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public boolean ismIsOnline() {
        return mIsOnline;
    }

    public void setmIsOnline(boolean mIsOnline) {
        this.mIsOnline = mIsOnline;
    }

    public ArrayList getmLstMessages() {
        return mLstMessages;
    }

    public void setmLstMessages(ArrayList mLstMessages) {
        this.mLstMessages = mLstMessages;
    }
}
