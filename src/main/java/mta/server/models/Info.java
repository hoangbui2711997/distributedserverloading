package mta.server.models;

import java.time.ZonedDateTime;

import static mta.server.services.times.TimeConvert.convertTimeZone;

public class Info extends Distributed{
    private String name;
    private String content;

    public Info(String name, String content) {
        this.name = name;
        this.content = content;
        this.timeCreated = convertTimeZone(ZonedDateTime.now());
        this.timeUpdate = convertTimeZone(ZonedDateTime.now());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.timeUpdate = convertTimeZone(this.timeUpdate.now());
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.timeUpdate = convertTimeZone(this.timeUpdate.now());
    }

    public ZonedDateTime getTimeCreated() {
        return this.timeCreated;
    }

    public ZonedDateTime getTimeUpdate() {
        return this.timeUpdate;
    }
}
