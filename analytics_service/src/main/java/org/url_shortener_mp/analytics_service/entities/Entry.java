package org.url_shortener_mp.analytics_service.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

@Document
public class Entry {
    @Id
    private String id;
    @Field(name = "url",write = Field.Write.NON_NULL)
    private String url;
    private Instant startTime;
    private Instant endTime;
    private Map<String,Long> agentMap;
    private Map<String,Long> deviceMap;
    private Map<String,Long> geoMap;
    private Long clicks;
    private Timestamp timestamp;

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", agentMap=" + agentMap +
                ", deviceMap=" + deviceMap +
                ", geoMap=" + geoMap +
                ", clicks=" + clicks +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Map<String, Long> getAgentMap() {
        return agentMap;
    }

    public void setAgentMap(Map<String, Long> agentMap) {
        this.agentMap = agentMap;
    }

    public Map<String, Long> getDeviceMap() {
        return deviceMap;
    }

    public void setDeviceMap(Map<String, Long> deviceMap) {
        this.deviceMap = deviceMap;
    }

    public Map<String, Long> getGeoMap() {
        return geoMap;
    }

    public void setGeoMap(Map<String, Long> geoMap) {
        this.geoMap = geoMap;
    }

    public Long getClicks() {
        return clicks;
    }

    public void setClicks(Long clicks) {
        this.clicks = clicks;
    }
}
