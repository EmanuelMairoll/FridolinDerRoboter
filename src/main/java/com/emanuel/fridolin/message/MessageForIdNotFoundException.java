package com.emanuel.fridolin.message;

public class MessageForIdNotFoundException extends Exception {
    private final String messageId;


    public MessageForIdNotFoundException(String messageId) {
        this.messageId = messageId;
    }


    public String getMessageId() {
        return messageId;
    }
}
