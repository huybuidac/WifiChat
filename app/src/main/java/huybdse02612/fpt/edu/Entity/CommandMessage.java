package huybdse02612.fpt.edu.Entity;

/**
 * Created by huy on 7/21/2015.
 */
public class CommandMessage {
    private CommandMessageType mType;
    private Object mContent;
    public CommandMessage(CommandMessageType mType, Object mContent) {
        super();
        this.mType = mType;
        this.mContent = mContent;
    }

    public CommandMessageType getmType() {
        return mType;
    }

    public void setmType(CommandMessageType mType) {
        this.mType = mType;
    }
}
