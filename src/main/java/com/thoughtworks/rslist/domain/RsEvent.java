package com.thoughtworks.rslist.domain;

public class RsEvent {
    private String eventName;
    private String keyWord;

    public String getEventName() {
        return eventName;
    }

    public String getKeyWord() {
        return keyWord;
    }
    public RsEvent() {

    }
    public RsEvent(String eventName, String keyWord) {
        this.eventName = eventName;
        this.keyWord = keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}