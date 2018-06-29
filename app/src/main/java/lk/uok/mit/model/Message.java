package lk.uok.mit.model;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    // Id – of type int
    private int id;
    // contactNumber – of type String
    private String contactNumber;
    // messageText – of type String
    private String messageText;
    // sentTime – of type java.util.Date
    private Date sentTime;
    // sentStatus – of type boolean
    private boolean sentStatus;
    //retryCount – of type short
    private short retryCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }

    public boolean isSentStatus() {
        return sentStatus;
    }

    public void setSentStatus(boolean sentStatus) {
        this.sentStatus = sentStatus;
    }

    public short getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(short retryCount) {
        this.retryCount = retryCount;
    }
}
