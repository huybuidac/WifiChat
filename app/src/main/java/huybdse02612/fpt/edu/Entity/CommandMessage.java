package huybdse02612.fpt.edu.Entity;

import java.io.Serializable;

/**
 * Created by hoang anh tuan on 7/21/2015.
 */
public class CommandMessage implements Serializable {
    private CommandMessageType mType;
    private String mFromUser;
    private String mContent;
    private String mReceiAddress;
    private String mSenderAddress;

    public CommandMessage(CommandMessageType mType, String from , String mContent, String receiAddres) {
        super();
        this.mType = mType;
        this.mFromUser=from;
        this.mContent = mContent;
        this.mReceiAddress =receiAddres;
    }

    @Override
    public String toString() {
        return "CommandMessage{" +
                "mType=" + mType +
                ", mFromUser='" + mFromUser + '\'' +
                ", mContent='" + mContent + '\'' +
                ", mReceiAddress='" + mReceiAddress + '\'' +
                '}';
    }

    public CommandMessageType getmType() {
        return mType;
    }

    public void setmType(CommandMessageType mType) {
        this.mType = mType;
    }
    public String getmFromUser() {
        return mFromUser;
    }

    public void setmFromUser(String mFromUser) {
        this.mFromUser = mFromUser;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public String getReceiAddress() {
        return mReceiAddress;
    }

    public void setmReceiAddress(String mReceiAddress) {
        this.mReceiAddress = mReceiAddress;
    }

    public String getSenderAddress() {
        return mSenderAddress;
    }

    public void setSenderAddress(String mSenderAddress) {
        this.mSenderAddress = mSenderAddress;
    }
}
