package com.audiotracker.audiotracker_cli.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Session {
    private Long id;
    private String date;
    private int length;
    private Long audiobookId;

    public Session() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getLength() { return length; }
    public void setLength(int length) { this.length = length; }

    public Long getAudiobookId() { return audiobookId; }
    public void setAudiobookId(Long audiobookId ) { this.audiobookId = audiobookId; }
}
