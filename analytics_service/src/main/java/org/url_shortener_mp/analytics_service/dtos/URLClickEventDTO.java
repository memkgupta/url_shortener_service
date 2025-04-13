package org.url_shortener_mp.analytics_service.dtos;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.sql.Timestamp;

public class URLClickEventDTO {
    private String id;
    private String short_url;
    private String long_url;
    private String ip;
    private Timestamp timestamp;
    private String referrer;
    private String agent;

    public URLClickEventDTO() {

    }

    public URLClickEventDTO(String id, String short_url, String long_url, String ip, Timestamp timestamp, String referrer, String agent) {
        this.id = id;
        this.short_url = short_url;
        this.long_url = long_url;
        this.ip = ip;
        this.timestamp = timestamp;
        this.referrer = referrer;
        this.agent = agent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShort_url() {
        return short_url;
    }

    public void setShort_url(String short_url) {
        this.short_url = short_url;
    }

    public String getLong_url() {
        return long_url;
    }

    public void setLong_url(String long_url) {
        this.long_url = long_url;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    @Override
    public String toString() {
        return "URLClickEventDTO{" +
                "id='" + id + '\'' +
                ", short_url='" + short_url + '\'' +
                ", long_url='" + long_url + '\'' +
                ", ip='" + ip + '\'' +
                ", timestamp=" + timestamp +
                ", referrer='" + referrer + '\'' +
                ", agent='" + agent + '\'' +
                '}';
    }
}
